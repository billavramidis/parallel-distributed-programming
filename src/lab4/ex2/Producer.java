package lab4.ex2;

import java.util.concurrent.BlockingQueue;

public class Producer extends Thread {
	private final BlockingQueue<Integer> blockingQueue;
	private final int reps;
	private final int scale;

	// Constructor
	public Producer(BlockingQueue<Integer> blockingQueue, int reps, int scale) {
		this.blockingQueue = blockingQueue;
		this.reps = reps;
		this.scale = scale;
	}

	// Producer runs for reps times with random(scale) intervals
	public void run() {
		for(int i = 0; i < reps; i++) {
            try {
                blockingQueue.put(i);
				System.out.println("Produced " + i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
				sleep((int)(Math.random()*scale));
			} catch (InterruptedException e) { }
		}
	}
}
