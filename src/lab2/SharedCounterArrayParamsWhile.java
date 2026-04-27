package lab2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCounterArrayParamsWhile {
    public static void main(String[] args) {
        int end = 10000;
        int[] counter = new int[1];
        int[] array = new int[end];
        int numThreads = 4;
        Lock lock = new ReentrantLock();
        CounterThread[] threads = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(end, array, counter, lock);
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
        private int end;
        private int[] array;
        private int[] counter;
        private Lock lock;

        public CounterThread(int end, int[] array, int[] counter, Lock lock) {
            this.end = end;
            this.array = array;
            this.counter = counter;
            this.lock = lock;
        }

	   /*
	   		Στο συγκεκριμένο παράδειγμα έχει αλλάξει ο τρόπος χειρισμού των μεταβλητών, έχουν μετατραπεί από global
	   		(static) σε ορίσματα μεθόδων/αντικειμένων. Το πρόβλημα όμως είναι ότι η μεταβλητή counter είναι τύπου
	   		int και στη Java όταν έχουμε μεταβλητή int ως όρισμα περνάει ένα διαφορετικό αντίγραφο της σε κάθε νήμα.
	   		Μια καλή λύση είναι η αλλαγή της σε δομή πίνακα με μια μόνο θέση (new int[1]) είτε να χρησιμοποιήσουμε μια
	   		πιο υψηλού επιπέδου δομή όπως AtomicInteger ή MutableInt

	   		Πηγή:
	   		https://www.geeksforgeeks.org/java/how-to-pass-integer-by-reference-in-java/
	   */

        public void run() {

            while (true) {
                lock.lock();
                try{
                    if (counter[0] >= end)
                        break;
                    array[counter[0]]++;
                    counter[0]++;
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}

