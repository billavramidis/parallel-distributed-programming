package lab7;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ex733 {
    public static void main(String[] args) {
        int size = 0;
        int numThreads = 0;

        if (args.length != 2) {
            System.out.println("Usage: java ex723 <size> <number of threads>");
            System.exit(1);
        }

        try {
            size = Integer.parseInt(args[0]);
            numThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }

        boolean[] primes = new boolean[size + 1];
        for (int i = 2; i <= size; i++) {
            primes[i] = true;
        }

        SharedData sharedData = new SharedData(size);

        long start = System.currentTimeMillis();

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new Worker(primes, size, sharedData));
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }

        long elapsedTimeMillis = System.currentTimeMillis() - start;

        int count = 0;
        for (int i = 2; i <= size; i++) {
            if (primes[i]) count++;
        }

        System.out.println("number of primes " + count);
        System.out.println("time in ms = " + elapsedTimeMillis);
    }

    static class SharedData {
        private int tasksAssigned = 2;
        private final int limit;
        private final Lock lock = new ReentrantLock();

        public SharedData(int size) {
            this.limit = (int) Math.sqrt(size);
        }

        public int getNextTask() {
            lock.lock();
            try {
                if (tasksAssigned <= limit)
                    return tasksAssigned++;
                else
                    return -1;
            } finally {
                lock.unlock();
            }
        }
    }

    static class Worker implements Runnable {
        private final boolean[] primes;
        private final int size;
        private final SharedData sharedData;

        public Worker(boolean[] primes, int size, SharedData sharedData) {
            this.primes = primes;
            this.size = size;
            this.sharedData = sharedData;
        }

        @Override
        public void run() {
            int p;
            while ((p = sharedData.getNextTask()) >= 0) {
                if (primes[p]) {
                    for (long i = (long) p * p; i <= size; i += p) {
                        primes[(int) i] = false;
                    }
                }
            }
        }
    }

    /*
        CPU: 11th Gen Intel(R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results (Parallel Sieve - Dynamic Distribution):

        Size (n)       | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
                1,000,000      | 10ms     | 12ms      | 19ms      | 17ms      |
                10,000,000     | 40ms     | 37ms      | 31ms      | 36ms      |
                100,000,000    | 882ms    | 532ms     | 371ms     | 270ms     |
                1,000,000,000  | 11811ms  | 5956ms    | 4625ms    | 3639ms    |

     */
}