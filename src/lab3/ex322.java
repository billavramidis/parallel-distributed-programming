package lab3;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ex322 {
    public static void main(String[] args) {

        int end = 1000;
        int numThreads = 4;

        int[] array = new int[end];

        SharedData sharedData = new SharedData(end, array);

        CounterThread[] threads = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(sharedData);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(end, array, numThreads);
    }

    static void check_array (int end, int[] array, int numThreads)  {
        int i, errors = 0;

        System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
            if (array[i] != numThreads*i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, array[i], numThreads*i);
            }
        }
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {
        private final int end;
        private final int[] array;
        private final ArrayList<Lock> lockList;

        public CounterThread(SharedData sharedData) {
            this.end = sharedData.getEnd();
            this.array = sharedData.getArray();
            this.lockList = sharedData.getLockList();
        }

        public void run() {

            for (int i = 0; i < end; i++) {
                for (int j = 0; j < i; j++){
                    lockList.get(i).lock();
                    try{
                        array[i]++;
                    }finally {
                        lockList.get(i).unlock();
                    }
                }
            }
        }
    }

    static class SharedData{
        private final int end;
        private final int[] array;
        private final ArrayList<Lock> lockList;

        public SharedData(int end, int[] array){
            this.end = end;
            this.array = array;

            this.lockList = new ArrayList<>();

            for (int i = 0; i < this.end; i++) {
                lockList.add(new ReentrantLock());
            }
        }

        public int getEnd() {
            return end;
        }

        public int[] getArray() {
            return array;
        }

        public ArrayList<Lock> getLockList() {
            return lockList;
        }
    }
}
