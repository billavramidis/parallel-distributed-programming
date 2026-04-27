package lab1;

/*
 * HelloThreadArgs.java
 *
 * creates threads using a class extending Thread, with arguments.
 * 
 */
public class ex11 {

    public static void main(String[] args) {

        /* allocate array of thread objecst */
        int numThreads = 20;
        Thread[] threads = new Thread[numThreads];

        /* create and start threads */
        for (int i = 0; i < numThreads; ++i) {
            System.out.println("In main: create and start thread " + i);
            threads[i] = new MyThread(i, numThreads);
            threads[i].start();
            
        }

        /* wait for threads to finish */
        for (int i = 0; i < numThreads; ++i) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }
        /*
            Γενικά, οι δομές δεδομένων επιτρέπουν στον προγραμματιστή να οργανώνει και να επεκτείνει καλύτερα τον κώδικα του. Θα μπορούσαμε πολύ εύκολα να μην χρησιμοποιήσουμε
            σε αυτή την περίπτωση την δομή Array για να δημιουργήσουμε 20 Threads και να το κάναμε χειροκίνητα. Όμως, αυτό δεν θα ήταν εύκολα υλοποιήσιμο εάν είχαμε
            για παράδειγμα 100, 200, 1000 Threads. Θα έπρεπε να είχαμε 3*N γραμμές για το declaration/start/join για Ν Threads, γεγονός που προφανώς δυσκολεύει την αναγνωσιμότητα του
            κώδικα. Επιπλέον, πέρα από την βελτίωση στην οργάνωση του κώδικα, οι δομές δεδομένων επιτρέπουν και στην υλοποιήση πιο σύνθετων λειτουργίων. Για παράδειγμα, 
            θα μπορούσαμε να είχαμε ένα δέντρο απο Threads ή κάλιστα ένα HashMap από Threads που υλοποιούν σε κάθε περίπτωση διαφορετική σύνθετη λειτουργία. 

            Ακολουθεί ο κώδικας δημιουργίας και εκτέλεσης των 20 νημάτων χωρίς τη δομή Array:

            src.MyThread thread1 = new src.MyThread(1, numThreads);
            src.MyThread thread2 = new src.MyThread(2, numThreads);
            ...
            ...
            src.MyThread thread20 = new Thread(20, numThreads);


            thread1.start();
            thread2.start();
            ...
            ...
            thread20.start();


            try{
                thread1.join();
                thread2.join();
                ...
                ...
                thread20.join();
            
            }catch(InterruptedException e){}

            Ο παραπάνω κώδικας είναι πολύ μεγαλύτερος σε όγκο από τον παραπάνω κώδικα, γεγονός που επιβεβαίωνει ότι είναι χρήσιμες οι δομές δεδομένων.

        */

        System.out.println("In main: threads all done");
    }

    /* class containing code for each thread to execute */
    static class MyThread extends Thread {

        /* instance variables */
        private int myID;
        private int totalThreads;

        /* constructor */
        public MyThread(int myID, int totalThreads) {
            this.myID = myID;
            this.totalThreads = totalThreads;
        }

        /* thread code */
        public void run() {
            System.out.println("Hello from thread " + myID + " out of " + totalThreads);
            System.out.println("Thread " + myID + " exits");

        }

    }
}



