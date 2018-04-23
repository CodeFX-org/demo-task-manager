package org.codefx.demo.task_manager.flow;

import org.codefx.demo.task_manager.print.TaskPresenter;
import org.codefx.demo.task_manager.task.Tasks;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/**
 * A {@link Subscriber} that wraps a {@link TaskPresenter} and uses it
 * to present all {@link Tasks} it receives.
 *
 * Not thread-safe.
 */
public class TaskPresentingSubscriber implements Subscriber<Tasks> {

	private final TaskPresenter presenter;
	private Subscription subscription;

	public TaskPresentingSubscriber(TaskPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		System.out.println(" > subscribed");
		this.subscription = subscription;
		System.out.println(" > request next");
		this.subscription.request(1);
	}

	@Override
	public void onNext(Tasks tasks) {
		System.out.println(" > next tasks");
		presenter.present(tasks);
		System.out.println(" > request next");
		this.subscription.request(1);
	}

	@Override
	public void onError(Throwable throwable) {
		System.out.println(" > error occurred");
		System.out.println(throwable);
	}

	@Override
	public void onComplete() {
		System.out.println(" > printing completed");
	}

}
