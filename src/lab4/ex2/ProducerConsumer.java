package lab4.ex2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer {
	public static void main(String[] args) {
		int queueSize = 5;
		int noIterations = 20;
		int producerDelay = 1;
		int consumerDelay = 100;
		int noProds = 3;
		int noCons = 2;
		Producer[] prod = new Producer[noProds];
		Consumer[] cons = new Consumer[noCons];

		// Blocking Queue
		BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(queueSize);
		
		// Producer threads
		for (int i=0; i<noProds; i++) {
			prod[i] = new Producer(blockingQueue, noIterations, producerDelay);
			prod[i].start();
		}

		// Consumer threads
		for (int j=0; j<noCons; j++) {
	        cons[j] = new Consumer(blockingQueue, consumerDelay);
			cons[j].start();
		}
	}
}
