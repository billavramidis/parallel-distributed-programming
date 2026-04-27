package lab3;

public class ex311 {

    static int end = 1000;
    static int[] array = new int[end];
    static int numThreads = 4;
    static SharedIncrementor sharedIncrementor = new SharedIncrementor(array);

    public static void main(String[] args) {

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
        check_array();
    }

    static void check_array ()  {
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
        private final SharedIncrementor sharedIncrementor;

        public CounterThread(SharedIncrementor sharedIncrementor) {
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

        public SharedIncrementor(int[] array){
            this.array = array;
        }

        public void increment(int index){
            synchronized (this){
                array[index]++;
            }
        }
    }
}

