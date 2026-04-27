package lab2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCounterArrayGlobalWhile {
  
    static int end = 10000;
    static int counter = 0;
    static int[] array = new int[end];
    static int numThreads = 4;
	static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        CounterThread[] threads = new CounterThread[numThreads];
	
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
        check_array ();
    }
     
    static void check_array ()  {
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (array[i] != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, array[i]);
			}         
		}
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {
  	
       public CounterThread() {
       }

	   /*
	   		Αυτό το παράδειγμα έχει την ιδιοτροπία ότι έχουμε δύο μοιραζόμενες global μεταβλητές, συγκεκριμένα
	   		την counter και τον πίνακα array. Το κάθε νήμα αυξάνει κάθε θέση του πίνακα array από
	   		0 σε 1 και μετακινείται στην επόμενη θέση του array. Οπότε αναγκαστικά πρέπει να κλειδώσουμε όλη
	   		την λειτουργία του νήματος για να αποφύγουμε race conditions. Με αυτή την υλοποιήση όλα τα νήματα
	   		κάνουν από κάποιες αυξήσεις οπότε επιτυγχάνεται ο παραλληλισμός.
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
}
  
