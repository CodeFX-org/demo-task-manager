package org.codefx.demo.task_manager.flow;

import java.util.concurrent.Flow.Subscription;
import java.util.function.LongConsumer;

/**
 * A subscription that counts the requested items.
 *
 * Not thread-safe.
 */
class RequestCountingSubscription implements Subscription {

	private long requestedItems;
	private final LongConsumer request;
	private final Runnable cancel;

	RequestCountingSubscription(LongConsumer request, Runnable cancel) {
		this.requestedItems = 0;
		this.request = request;
		this.cancel = cancel;
	}

	@Override
	public void request(long n) {
		requestedItems += n;
		request.accept(n);
	}

	@Override
	public void cancel() {
		cancel.run();
	}

	public void itemPublished() {
		requestedItems--;
	}

	public boolean moreRequested() {
		return requestedItems > 0;
	}

}
