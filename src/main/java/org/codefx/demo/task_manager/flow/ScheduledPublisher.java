package org.codefx.demo.task_manager.flow;

import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Turns a {@link Supplier} into a {@link Publisher} by requesting items
 * following a fixed schedule and publishing them immediately.
 *
 * Thread-safe.
 */
public class ScheduledPublisher<T> extends SimplePublisher<T> {

	private final Supplier<T> supplier;
	private final ScheduledExecutorService scheduler;

	public ScheduledPublisher(Supplier<T> supplier, ScheduledExecutorService scheduler) {
		this.supplier = supplier;
		this.scheduler = scheduler;
	}

	public ScheduledPublisher(Supplier<T> supplier) {
		this(supplier, Executors.newScheduledThreadPool(1));
	}

	public void publishAtFixedRate(int period, TimeUnit unit) {
		scheduler.scheduleAtFixedRate(this::publish, 0, period, unit);
	}

	private void publish() {
		try {
			T item = supplier.get();
			publishItem(item);
		} catch (Throwable throwable) {
			publishError(throwable);
		}
	}

	public void shutdownAfter(int period, TimeUnit unit) {
		scheduler.schedule(this::shutdown, period, unit);
	}

	private void shutdown() {
		scheduler.shutdown();
		publishCompletion();
	}

	@Override
	void itemsRequested(long totalRequested) {
		// since items are created per schedule, there's nothing to be done if new items get requested
	}

}
