package org.codefx.demo.task_manager.flow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/**
 * A {@link Publisher} that {@link Subscriber subscribes} to another {@link Publisher}
 * and forwards all items that one publishes to all of its own subscribers.
 *
 * Not thread-safe.
 */
public class Multiplexer<T> implements Subscriber<T>, Publisher<T> {

	private Subscription subscription;
	private final Map<Subscriber<? super T>, RequestCountingSubscription> subscribers = new HashMap<>();

	// SUBSCRIBER

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
	}

	@Override
	public void onNext(T item) {
		// publish to all subscribers
		subscribers
				.entrySet().stream()
				.filter(entry -> entry.getValue().moreRequested())
				.forEach(entry -> {
					entry.getKey().onNext(item);
					entry.getValue().itemPublished();
				});
	}

	@Override
	public void onError(Throwable throwable) {
		subscribers.forEach((subscriber, __) -> subscriber.onError(throwable));
	}

	@Override
	public void onComplete() {
		subscribers.forEach((subscriber, __) -> subscriber.onComplete());
	}

	// PUBLISHER

	@Override
	public void subscribe(Subscriber<? super T> subscriber) {
		if (subscribers.containsKey(subscriber))
			return;

		var subscription = new RequestCountingSubscription(requested -> {
		}, () -> subscribers.remove(subscriber));
		subscribers.put(subscriber, subscription);
		subscriber.onSubscribe(subscription);
	}

}
