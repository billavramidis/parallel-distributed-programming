package lab4.ex2;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
	private final BlockingQueue<Integer> blockingQueue;
	private final int scale;

	// Constructor
	public Consumer(BlockingQueue<Integer> blockingQueue, int scale) {
		this.blockingQueue = blockingQueue;
		this.scale = scale;
	}

	// Producer runs for reps times with random(scale) intervals
	public void run() {
		int value;
		while (true) {
			try {
				sleep((int)(Math.random()*scale));
			} catch (InterruptedException e) { }
            try {
                value = blockingQueue.take();
				System.out.println("Consumed " + value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
	}
}
