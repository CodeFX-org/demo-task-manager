package org.codefx.demo.task_manager;

import org.codefx.demo.task_manager.print.TaskPresenter;
import org.codefx.demo.task_manager.print.SystemOutTaskPresenter;
import org.codefx.demo.task_manager.task.Task;
import org.codefx.demo.task_manager.task.Tasks;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class TaskManager {

	private final TaskPresenter presenter;

	public TaskManager(TaskPresenter presenter) {
		this.presenter = presenter;
	}

	public static void main(String[] args) {
		Function<Stream<Task>, Stream<Task>> taskFilter = tasks -> tasks
				.filter(task -> task.user.isPresent())
				.filter(task -> !task.user.orElse("").equals("root"))
				.sorted(comparing((Task task) -> task.start.orElse(Instant.EPOCH)).reversed())
				.limit(20);

		var taskManager = new TaskManager(new SystemOutTaskPresenter(taskFilter));
		var scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(taskManager::presentTasks, 1, 1, TimeUnit.SECONDS);
		scheduler.schedule(scheduler::shutdown, 10, TimeUnit.SECONDS);
	}

	private void presentTasks() {
		presenter.present(Tasks.all());
	}

}
