package lab2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCounterArrayObject {
    public static void main(String[] args) {

        int end = 1000;
        int numThreads = 4;

        int[] array = new int[end];

        Lock lock = new ReentrantLock();

        SharedData sharedData = new SharedData(end, array, lock);

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
        private int end;
        private int[] array;
        private Lock lock;

        public CounterThread(SharedData sharedData) {
            this.end = sharedData.getEnd();
            this.array = sharedData.getArray();
            this.lock = sharedData.getLock();
        }

         /*
	   		Στο συγκεκριμένο παράδειγμα έχει αλλάξει ο τρόπος χειρισμού των μεταβλητών, έχουν μετατραπεί από global
	   		(static) σε ειδικό αντικείμενο. Το πρόγραμμα λειτουργεί ακριβώς με τον ίδιο τρόπο, καθώς στη
	   		Java όταν περνάμε αντικείμενο ως όρισμα περνάμε την διεύθυνση του. Άρα, κάθε νήμα βλέπει στην
	   		στο ίδιο αντικείμενο SharedData, οπότε μια τροποποιήση από ένα νήμα είναι καθολική
	   		τροποποιήση στις ιδιότητες του ίδιου αντικείμενου. Το πρόβλημα των race conditions λύνεται με τον ίδιο
	   		τρόπο που λύθηκε στο πρόγραμμα με τις global μεταβλητές.
	   */

        public void run() {

            for (int i = 0; i < end; i++) {
                for (int j = 0; j < i; j++){
                    lock.lock();
                    try{
                        array[i]++;
                    }finally {
                        lock.unlock();
                    }
                }
            }
        }
    }

    static class SharedData{
        private int end;
        private int[] array;
        private Lock lock;

        public SharedData(int end, int[] array, Lock lock){
            this.end = end;
            this.array = array;
            this.lock = lock;
        }

        public int getEnd() {
            return end;
        }

        public int[] getArray() {
            return array;
        }

        public Lock getLock() {
            return lock;
        }
    }
}
