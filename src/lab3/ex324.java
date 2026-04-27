package lab3;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ex324 {
    public static void main(String[] args) {

        int end = 1000;
        int numThreads = 4;

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        for (int i = 0; i < end; i++) {
            concurrentHashMap.putIfAbsent(i, 0);
        }

        CounterThread[] threads = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(end, concurrentHashMap);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(end, concurrentHashMap, numThreads);
    }

    static void check_array (int end, ConcurrentHashMap<Integer, Integer> concurrentHashMap, int numThreads)  {
        int i, errors = 0;

        System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
            if (concurrentHashMap.get(i) != numThreads*i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, concurrentHashMap.get(i), numThreads*i);
            }
        }
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {
        private final int end;
        private final ConcurrentHashMap<Integer, Integer> concurrentHashMap;

        public CounterThread(int end, ConcurrentHashMap<Integer, Integer> concurrentHashMap) {
            this.end = end;
            this.concurrentHashMap = concurrentHashMap;
        }

        public void run() {

            for (int i = 0; i < end; i++) {
                for (int j = 0; j < i; j++){
                    concurrentHashMap.computeIfPresent(i, (key, val) -> val + 1);
                }
            }
        }
    }
}
