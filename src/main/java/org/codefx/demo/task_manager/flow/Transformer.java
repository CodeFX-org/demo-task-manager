package org.codefx.demo.task_manager.flow;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Function;

public class Transformer<T, R>
		extends SimplePublisher<R>
		implements Subscriber<T>, Publisher<R> {

	private final Function<T, R> transformer;
	private Subscription subscription;

	public Transformer(Function<T, R> transformer) {
		this.transformer = transformer;
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
	}

	@Override
	public void onNext(T item) {
		publishItem(transformer.apply(item));
	}

	@Override
	public void onError(Throwable throwable) {
		publishError(throwable);
	}

	@Override
	public void onComplete() {
		publishCompletion();
	}

	@Override
	void itemsRequested(long newlyRequested) {
		subscription.request(newlyRequested);
	}

}
