package org.codefx.demo.task_manager.flow;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * Abstract superclass for a publisher that can have a single subscriber.
 *
 * Not thread-safe.
 */
abstract class SimplePublisher<T> implements Publisher<T> {

	private Subscriber<? super T> subscriber;
	private RequestCountingSubscription subscription;

	@Override
	public final void subscribe(Subscriber<? super T> subscriber) {
		cancelExistingSubscription();
		establishNewSubscription(subscriber);
	}

	private void cancelExistingSubscription() {
		if (!subscribed())
			return;

		this.subscriber.onComplete();
		this.subscriber = null;
		this.subscription = null;
	}

	private void establishNewSubscription(Subscriber<? super T> subscriber) {
		this.subscriber = subscriber;
		this.subscription = new RequestCountingSubscription(this::itemsRequested, this::cancelExistingSubscription);
		subscriber.onSubscribe(subscription);
	}

	abstract void itemsRequested(long newlyRequested);

	protected final void publishItem(T item) {
		if (subscribed() && subscription.moreRequested()) {
			subscriber.onNext(item);
			subscription.itemPublished();
		}
	}

	private boolean subscribed() {
		return subscriber != null && subscription != null;
	}

	protected final void publishError(Throwable throwable) {
		subscriber.onError(throwable);
	}

	protected final void publishCompletion() {
		subscriber.onComplete();
	}

}
