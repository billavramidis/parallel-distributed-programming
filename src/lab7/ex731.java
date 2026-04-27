package lab7;

public class ex731 {
	public static void main(String[] args) {
		int numThreads = 0;
		int size = 0;

		if (args.length != 2) {
			System.out.println("Usage: java SieveOfEratosthenes <size> <number_of_threads>");
			System.exit(1);
		}

		try {
			size = Integer.parseInt(args[0]);
			numThreads = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException nfe) {
			System.out.println("Integer argument expected");
			System.exit(1);
		}

		boolean[] prime = new boolean[size+1];

		for(int i = 2; i <= size; i++)
			prime[i] = true;

		long start = System.currentTimeMillis();

		int limit = (int)Math.sqrt(size) + 1;
		for (int p = 2; p <= limit; p++)
		{
			if(prime[p])
			{
				SleeveThread[] threads = new SleeveThread[numThreads];
				for (int i = 0; i < numThreads; i++) {
					threads[i] = new SleeveThread(prime, p, i, numThreads, size);
					threads[i].start();
				}

				for (int i = 0; i < numThreads; i++) {
					try {
						threads[i].join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		long elapsedTimeMillis = System.currentTimeMillis()-start;

		int count = 0;
		for(int i = 2; i <= size; i++)
			if (prime[i]) {
				count++;
			}

		System.out.println("number of primes "+count);
		System.out.println("time in ms = "+ elapsedTimeMillis);
	}

	static class SleeveThread extends Thread{
		private final boolean[] prime;
		private final int p;
		private final int start;
		private final int end;
		private final int size;

		public SleeveThread(boolean[] prime, int p, int threadID, int numThreads, int size){
			this.prime = prime;
			this.p = p;
			this.size = size;

			int first = p * p;
			int totalMultiples = (size - first) / p + 1;
			int chunk = totalMultiples / numThreads;

			this.start = first + (threadID * chunk * p);
			if (threadID == numThreads - 1) {
				this.end = size;
			} else {
				this.end = first + ((threadID + 1) * chunk * p) - p;
			}
		}

		@Override
		public void run() {
			for (int i = start; i <= end; i += p) {
				if (i <= size && i >= p*p) {
					prime[i] = false;
				}
			}
		}
	}

	/*
		CPU: 11th Gen Intel(R) Core(TM) i5-1135G7 @ 2.40GHz
		RAM: 8 GB

		Test Results (Parallel Sieve of Eratosthenes - Normal Distribution):

		Size (n)       | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
		10,000,000     | 140ms    | 119ms     | 166ms     | 261ms     |
		100,000,000    | 1063ms   | 784ms     | 832ms     | 940ms     |
		1,000,000,000  | 10734ms  | 7194ms    | 5812ms    | 5424ms    |
	 */
}