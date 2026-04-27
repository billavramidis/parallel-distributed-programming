package lab2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCounterArrayGlobal {
  
    static int end = 1000;
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
			if (array[i] != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, array[i], numThreads*i);
			}         
        }
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {
  	
       public CounterThread() {
       }

	   /*
	   		Στο συγκεκριμένο παράδειγμα το critical section είναι η γραμμή κώδικα στην οποία γίνεται
	   		η ενημέρωση του πίνακα array (LINE 65). Επειδή είναι δηλωμένος ως static, ολόκληρο το αντικείμενο
	   		SharedCounterArrayGlobal βλέπει και επεξεργάζεται το ίδιο ακριβώς αντίγραφο της μεταβλητής array
	   		και αυτό μπορεί να προκαλέσει race conditions, έαν δεν υπάρχει μηχανισμός αμοιβαίου αποκλεισμού (Lock) για να
	   		τα αποτρέψει. Τέλος, δεν κλειδώνουμε όλοκληρη τη διπλή for επειδή στην ουσία θα επέτρεπε το πρόγραμμα
	   		σε μόνο ένα νήμα τη φορά να τρέχει και δεν θα διέφερε η υλοποιήση μας ιδιαίτερα από τον
	   		ακολουθιακό προγραμματισμό.
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
}
  
