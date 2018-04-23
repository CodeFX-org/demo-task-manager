package org.codefx.demo.task_manager;

import org.codefx.demo.task_manager.flow.ScheduledPublisher;
import org.codefx.demo.task_manager.flow.TaskPresentingSubscriber;
import org.codefx.demo.task_manager.flow.Transformer;
import org.codefx.demo.task_manager.print.SystemOutTaskPresenter;
import org.codefx.demo.task_manager.task.Task;
import org.codefx.demo.task_manager.task.Tasks;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class TaskManager {

	public static void main(String[] args) {
		var taskPublisher = new ScheduledPublisher<>(Tasks::all);
		var taskFilter = new Transformer<>(TaskManager::filterTasks);
		var presenter = new TaskPresentingSubscriber(new SystemOutTaskPresenter());

		taskPublisher.subscribe(taskFilter);
		taskFilter.subscribe(presenter);

		taskPublisher.publishAtFixedRate(1, TimeUnit.SECONDS);
		taskPublisher.shutdownAfter(10, TimeUnit.SECONDS);
	}

	private static Tasks filterTasks(Tasks tasks) {
		Stream<Task> taskStream = tasks.tasks()
				.filter(task -> task.user.isPresent())
				.filter(task -> !task.user.orElse("").equals("root"))
				.sorted(comparing((Task task) -> task.start.orElse(Instant.EPOCH)).reversed())
				.limit(20);
		return Tasks.of(taskStream);
	}

}
