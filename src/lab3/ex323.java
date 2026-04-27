package lab3;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class ex323 {
    public static void main(String[] args) {

        int end = 1000;
        int numThreads = 4;

        int[] array = new int[end];

        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(array);

        CounterThread[] threads = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(end, atomicIntegerArray);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(end, atomicIntegerArray, numThreads);
    }

    static void check_array (int end, AtomicIntegerArray atomicIntegerArray, int numThreads)  {
        int i, errors = 0;

        System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
            if (atomicIntegerArray.get(i) != numThreads*i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, atomicIntegerArray.get(i), numThreads*i);
            }
        }
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {
        private final int end;
        private final AtomicIntegerArray atomicIntegerArray;

        public CounterThread(int end, AtomicIntegerArray atomicIntegerArray) {
            this.end = end;
            this.atomicIntegerArray = atomicIntegerArray;
        }

        public void run() {

            for (int i = 0; i < end; i++) {
                for (int j = 0; j < i; j++){
                    atomicIntegerArray.getAndIncrement(i);
                }
            }
        }
    }
}
