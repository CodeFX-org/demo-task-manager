package org.codefx.demo.task_manager.task;

import java.time.Instant;
import java.util.Optional;

/**
 * A task managed by this application. Immutable.
 */
public class Task {

	public final long pid;
	public final Optional<String> command;
	public final Optional<String> user;
	public final Optional<Instant> start;

	public Task(long pid, Optional<String> command, Optional<String> user, Optional<Instant> start) {
		this.pid = pid;
		this.command = command;
		this.user = user;
		this.start = start;
	}

	static Task fromProcessHandle(ProcessHandle handle) {
		long pid = handle.pid();
		Optional<String> command = handle.info().command();
		Optional<String> user = handle.info().user();
		Optional<Instant> start = handle.info().startInstant();
		return new Task(pid, command, user, start);
	}

	@Override
	public String toString() {
		return "[" +
				"pid=" + pid +
				", command=" + command.orElse("") +
				", user=" + user.orElse("") +
				", start=" + start.map(Instant::toString).orElse("") +
				']';
	}
}
