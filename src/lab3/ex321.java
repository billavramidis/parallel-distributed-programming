package lab3;

import java.util.ArrayList;

public class ex321 {
    public static void main(String[] args) {

        int end = 1000;
        int numThreads = 4;

        int[] array = new int[end];

        SharedIncrementor sharedIncrementor = new SharedIncrementor(array, end);
        CounterThread[] threads = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(end, sharedIncrementor);
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
        private final SharedIncrementor sharedIncrementor;

        public CounterThread(int end, SharedIncrementor sharedIncrementor) {
            this.end = end;
            this.sharedIncrementor = sharedIncrementor;
        }

        public void run() {

            for (int i = 0; i < end; i++) {
                for (int j = 0; j < i; j++){
                    sharedIncrementor.increment(i);
                }
            }
        }
    }

    static class SharedIncrementor{
        private final int[] array;
        private final ArrayList<Object> dummyObjectList;

        public SharedIncrementor(int[] array, int end){
            this.array = array;

            this.dummyObjectList = new ArrayList<>();
            for (int i = 0; i < end; i++) {
                this.dummyObjectList.add(new Object());
            }
        }

        public void increment(int index){
            synchronized (this.dummyObjectList.get(index)){
                array[index]++;
            }
        }
    }
}
