package org.codefx.demo.task_manager.task;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Container for a set of tasks as collected at one point in time. Immutable.
 */
public class Tasks {

	private final List<Task> tasks;

	public Tasks(Stream<Task> tasks) {
		this.tasks = tasks.collect(toList());
	}

	public static Tasks all() {
		Stream<Task> tasks = ProcessHandle
				.allProcesses()
				.map(Task::fromProcessHandle);
		return new Tasks(tasks);
	}

	public Stream<Task> tasks() {
		return tasks.stream();
	}

}
