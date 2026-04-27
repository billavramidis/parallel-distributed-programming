package lab3;

public class ex316 {
    public static void main(String[] args) {
        int end = 10000;
        int counter = 0;
        int[] array = new int[end];
        int numThreads = 4;

        SharedIncrementor sharedIncrementor = new SharedIncrementor(counter, array, end);

        CounterThread[] threads = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(sharedIncrementor);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(end, array);
    }

    static void check_array (int end, int[] array)  {
        int i, errors = 0;

        System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
            if (array[i] != 1) {
                errors++;
                System.out.printf("%d: %d should be 1\n", i, array[i]);
            }
        }
        System.out.println(errors+" errors.");
    }


    static class CounterThread extends Thread {
        private final SharedIncrementor sharedIncrementor;

        public CounterThread(SharedIncrementor sharedIncrementor) {
            this.sharedIncrementor = sharedIncrementor;
        }

        public void run() {

            while (sharedIncrementor.tryIncrement()) {}
        }
    }

    static class SharedIncrementor{
        private int counter;
        private final int[] array;
        private final int end;
        private final Object dummyObject;

        public SharedIncrementor(int counter, int[] array, int end){
            this.counter = counter;
            this.array = array;
            this.end = end;
            this.dummyObject = new Object();
        }

        private void increment(){
            this.array[counter]++;
            this.counter++;
        }

        public boolean tryIncrement(){
            synchronized (dummyObject){
                if (this.counter >= end)
                    return false;

                this.increment();

                return true;
            }
        }
    }
}

