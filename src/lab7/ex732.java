package lab7;

public class ex732{
    public static void main(String[] args) {
        int size = 0;
        int numThreads = 0;

        if (args.length != 2) {
            System.out.println("Usage: java SieveOfEratosthenes <size> <number_of_threads>");
            System.exit(1);
        }

        try {
            size = Integer.parseInt(args[0]);
            numThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }

        boolean[] prime = new boolean[size + 1];
        for (int i = 2; i <= size; i++) {
            prime[i] = true;
        }

        long start = System.currentTimeMillis();

        int limit = (int) Math.sqrt(size);

        for (int p = 2; p <= limit; p++) {
            if (prime[p]) {
                SieveThread[] threads = new SieveThread[numThreads];

                for (int i = 0; i < numThreads; i++) {
                    threads[i] = new SieveThread(i, size, numThreads, p, prime);
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

        long elapsedTimeMillis = System.currentTimeMillis() - start;

        int count = 0;
        for (int i = 2; i <= size; i++) {
            if (prime[i]) {
                count++;
            }
        }

        System.out.println("number of primes " + count);
        System.out.println("time in ms = " + elapsedTimeMillis);
    }

    static class SieveThread extends Thread {
        private final int myRank;
        private final int size;
        private final int numThreads;
        private final int p;
        private final boolean[] prime;

        public SieveThread(int myRank, int size, int numThreads, int p, boolean[] prime) {
            this.myRank = myRank;
            this.size = size;
            this.numThreads = numThreads;
            this.p = p;
            this.prime = prime;
        }

        @Override
        public void run() {
            long firstMultiple = (long) p * p + ((long) myRank * p);
            long step = (long) numThreads * p;

            for (long i = firstMultiple; i <= size; i += step) {
                prime[(int) i] = false;
            }
        }
    }

    /*
        CPU: 11th Gen Intel(R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results (Parallel Sieve - Cyclic Distribution):

        Size (n)       | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
        1,000,000      | 28ms     | 44ms      | 74ms      | 98ms      |
        10,000,000     | 125ms    | 136ms     | 158ms     | 274ms     |
        100,000,000    | 1037ms   | 836ms     | 808ms     | 1017ms    |
        1,000,000,000  | 10693ms  | 8397ms    | 6678ms    | 7823ms    |
     */
}