package de.upb.crc901.otftestbed.commons.utils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.BooleanSupplier;

import org.slf4j.Logger;

/**
 * A queue to execute methods. The boolean return value of the methods
 * indicates the success ({@code true}) or failure ({@code false}) of
 * execution.
 *
 * @author Roman
 *
 */
public class MethodQueue implements Runnable {

	/**
	 * The logger
	 */
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(MethodQueue.class);

	/**
	 * Queue which stores the jobs.
	 */
	private final Deque<BooleanSupplier> deque;

	/**
	 * Delay between 2 tries.
	 */
	private long delay = 0;

	/**
	 * Number of retries. Zero means infinite tries.
	 */
	private long retries = 0;

	/**
	 * The working thread.
	 */
	private Thread worker;


	public MethodQueue() {
		deque = new LinkedList<>();

		worker = new Thread(this);
		worker.start();
	}

	public MethodQueue(long delay, long retries) {
		this();

		setDelay(delay);
		setRetries(retries);
	}


	/**
	 * Gets actual number of retries.
	 *
	 * @return actual number of retries
	 */
	public long getRetries() {
		return retries;
	}

	/**
	 * Sets number of retries. Zero means infinite tries.
	 *
	 * Negative numbers are set to zero.
	 *
	 * @param retries - number of retries
	 */
	public void setRetries(long retries) {
		this.retries = (retries <= 0 ? 0 : retries);
	}

	/**
	 * Sets number of retries. Zero means infinite tries.
	 *
	 * Negative numbers are set to zero.
	 *
	 * @param retries - number of retries
	 * @return this {@link MethodQueue}
	 */
	public MethodQueue retries(long retries) {
		setRetries(retries);
		return this;
	}


	/**
	 * Gets actual delay between 2 tries.
	 *
	 * @return actual delay between 2 tries
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * Sets delay between 2 tries.
	 *
	 * Negative numbers are set to zero.
	 *
	 * @param delay - between 2 tries
	 */
	public void setDelay(long delay) {
		this.delay = (delay <= 0 ? 0 : delay);
	}

	/**
	 * Sets delay between 2 tries.
	 *
	 * Negative numbers are set to zero.
	 *
	 * @param delay - between 2 tries
	 * @return this {@link MethodQueue}
	 */
	public MethodQueue delay(long delay) {
		setDelay(delay);
		return this;
	}


	/**
	 * Adds supplier at the end of the queue.
	 *
	 * The boolean return value indicates the success ({@code true})
	 * or failure ({@code false}) of the execution.
	 *
	 * @param s - supplier to execute
	 * @return {@code true} if the element was added to the queue, else {@code false}
	 */
	public synchronized boolean add(BooleanSupplier s) {
		if (s == null) {
			return false;
		}

		if (!deque.offerLast(s)) {
			return false;
		}

		this.notifyAll();
		return true;
	}

	/**
	 * Gets and removes first element of the queue or {@code null} if the queue is empty.
	 *
	 * @return first element of queue or {@code null} if queue is empty
	 * @throws InterruptedException when interrupted while waiting for methods
	 */
	protected synchronized BooleanSupplier getHead() throws InterruptedException {
		BooleanSupplier next;

		while ((next = deque.pollFirst()) == null) {
			wait();
		}

		return next;
	}

	/**
	 * Put element back to the front of the queue.
	 *
	 * Used when exception occurs while actual element was
	 * not successfully executed. So the element can be
	 * executed after the exception is handled.
	 *
	 * @param s - supplier to put at the front of queue
	 */
	protected synchronized void putBackHead(BooleanSupplier s) {
		log.debug("Putting back method to the front of queue.");
		deque.addFirst(s);
	}

	@Override
	public void run() {
		BooleanSupplier supplier;
		long tryCount;

		while (!Thread.currentThread().isInterrupted()) {
			try {
				supplier = getHead();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}

			tryCount = 1;
			while (!supplier.getAsBoolean()) {
				if (getRetries() != 0 && tryCount >= getRetries()) {
					log.error("Failed to execute method {} times, abort retrying: {}", tryCount, supplier);

					break;
				}

				try {
					Thread.sleep(getDelay());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

				if (Thread.currentThread().isInterrupted()) {
					log.error("Got interrupted while retrying.");
					putBackHead(supplier);

					log.error("Exiting.");
					return;
				}

				++tryCount;
			}
		}

		log.error("Got interrupted. Exiting.");
	}
}
