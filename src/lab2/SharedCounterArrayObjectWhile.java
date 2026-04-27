package lab2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCounterArrayObjectWhile {
    public static void main(String[] args) {
        int end = 10000;
        int counter = 0;
        int[] array = new int[end];
        int numThreads = 4;
        Lock lock = new ReentrantLock();

        SharedData sharedData = new SharedData(end, array, counter, lock);
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
        private int counter;
        private Lock lock;

        public CounterThread(SharedData sharedData) {
            this.end = sharedData.getEnd();
            this.array = sharedData.getArray();
            this.counter = sharedData.getCounter();
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

            while (true) {
                lock.lock();
                try{
                    if (counter >= end)
                        break;
                    array[counter]++;
                    counter++;
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    static class SharedData{
        private int end;
        private int[] array;
        private int counter;
        private Lock lock;

        public SharedData(int end, int[] array, int counter, Lock lock) {
            this.end = end;
            this.array = array;
            this.counter = counter;
            this.lock = lock;
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

        public Lock getLock() {
            return lock;
        }
    }
}

