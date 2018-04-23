package org.codefx.demo.task_manager.print;

import org.codefx.demo.task_manager.task.Tasks;

/**
 * Shows a list of tasks, e.g. in a GUI or by printing to console.
 */
public interface TaskPresenter {

	void present(Tasks tasks);

}
