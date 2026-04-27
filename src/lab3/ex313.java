package lab3;

public class ex313 {
    public static void main(String[] args) {
        int end = 10000;
        int counter = 0;
        int[] array = new int[end];
        int numThreads = 4;

        SharedData sharedData = new SharedData(end, array, counter);
        SharedIncrementor sharedIncrementor = new SharedIncrementor(sharedData);
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

    static class SharedData{
        private final int end;
        private final int[] array;
        private final int counter;

        public SharedData(int end, int[] array, int counter) {
            this.end = end;
            this.array = array;
            this.counter = counter;
        }

        public int getEnd() {
            return end;
        }

        public int[] getArray() {
            return array;
        }

        public int getCounter() {
            return counter;
        }
    }

    static class SharedIncrementor{
        private int counter;
        private final int[] array;
        private final int end;

        public SharedIncrementor(SharedData sharedData){
            this.counter = sharedData.getCounter();
            this.array = sharedData.getArray();
            this.end = sharedData.getEnd();
        }

        private void increment(){
            this.array[counter]++;
            this.counter++;
        }

        public synchronized boolean tryIncrement(){
            if (this.counter >= end)
                return false;

            this.increment();

            return true;
        }
    }
}

