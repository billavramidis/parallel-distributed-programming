package lab3;

public class ex314 {
    public static void main(String[] args) {

        int end = 1000;
        int numThreads = 4;

        int[] array = new int[end];
        SharedData sharedData = new SharedData(end, array);
        SharedIncrementor sharedIncrementor = new SharedIncrementor(sharedData.getArray());

        CounterThread[] threads = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(sharedData.getEnd(), sharedIncrementor);
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

    static class SharedData{
        private final int end;
        private final int[] array;

        public SharedData(int end, int[] array){
            this.end = end;
            this.array = array;
        }

        public int getEnd() {
            return end;
        }

        public int[] getArray() {
            return array;
        }
    }

    static class SharedIncrementor{
        private final int[] array;

        public SharedIncrementor(int[] array){
            this.array = array;
        }

        public synchronized void increment(int index){
            array[index]++;
        }
    }
}
