package org.codefx.demo.task_manager.print;

import org.codefx.demo.task_manager.task.Task;
import org.codefx.demo.task_manager.task.Tasks;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringUtils.abbreviate;
import static org.apache.commons.lang3.StringUtils.abbreviateMiddle;
import static org.apache.commons.lang3.StringUtils.rightPad;

/**
 * Prints the tasks to console.
 */
public class SystemOutTaskPresenter implements TaskPresenter {

	private final int pidWidth;
	private final int commandWidth;
	private final int userWidth;
	private final int startTimeWidth;

	private final DateTimeFormatter startTimeFormatter;

	public SystemOutTaskPresenter() {
		pidWidth = 6;
		userWidth = 10;
		startTimeWidth = 13;
		commandWidth = 40;

		startTimeFormatter = DateTimeFormatter
				.ofPattern("dd.MM., HH:mm")
				.withZone(ZoneId.systemDefault());
	}

	@Override
	public void present(Tasks tasks) {
		printHeader();
		tasks.tasks()
				.map(this::formatAsRow)
				.forEach(this::printRow);
		System.out.println();
	}

	private void printHeader() {
		var header = String.format(
				"%s | %s | %s | %s",
				rightsize("PID", pidWidth),
				rightsize("COMMAND", commandWidth),
				rightsize("USER", userWidth),
				rightsize("START", startTimeWidth));
		System.out.println(header);
	}

	private String formatAsRow(Task task) {
		String startTime = task.start
				.map(startTimeFormatter::format)
				.orElse("");
		return String.format(
				"%s | %s | %s | %s",
				rightsize(task.pid, pidWidth),
				middlesize(task.command.orElse(""), commandWidth),
				rightsize(task.user.orElse(""), userWidth),
				rightsize(startTime, startTimeWidth));
	}

	private void printRow(String row) {
		System.out.println(row);
	}

	private static String rightsize(Object string, int width) {
		return abbreviate(rightPad(string.toString(), width, ' '), "...", width);
	}

	private static String middlesize(Object string, int width) {
		return abbreviateMiddle(rightPad(string.toString(), width, ' '), "...", width);
	}

}
