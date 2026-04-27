package lab1;

public class ex12 {
    public static void main(String[] args){
        int numThreads = 10;

        Thread[] aThreads = new Thread[numThreads];
        Thread[] bThreads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            aThreads[i] = new ThreadA(i);
            bThreads[i] = new ThreadB(i);

            aThreads[i].start();
            bThreads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try{
                aThreads[i].join();
                bThreads[i].join();
            }catch(InterruptedException e){}
        }

        /*
           Σε αυτό το πρόγραμμα βλέπουμε πως γίνεται να δημιουργήσουμε δύο διαφορετικά μεταξύ τους αντικείμενα με τις
           ιδιότητες των Threads. Επίσης, θα μπορούσαμε να είχαμε χρησιμοποιήσει ίδιο Array και για τους δύο τύπους Threads,
           καθώς η λειτουργία του ενός δεν επικαλύπτει την λειτουργία του άλλου. Σε περιπτώσεις επεξεργασίας διαμοιραζόμενων δεδομένων,
           η παραπάνω τεχνική είναι η πιο ασφάλης για την αποφυγή σφαλμάτων και την εξασφάλιση λογικής εκτέλεσης. Τέλος, από την εκτέλεση του προγράμματος
           συμπεραίνουμε ότι κάθε Thread έχει τον κύκλο ζωής του αυτόνομα από τα υπόλοιπα Threads.
           Παραδείγματως χάρη, στην πρώτη εκτέλεση μπορεί να ολοκληρώσει πρώτα την λειτουργία του το ThreadA με αριθμό επανάληψης 2,
           ενώ στην δεύτερη μπορεί να ολοκληρώσει πρώτα το ThreadB με αριθμό επανάληψης 6. Η σειρά εκτέλεσης στο
           συγκεκριμένο κώδικα είναι καθαρά θέμα δρομολόγησης του OS.
        */

        Child child = new Child("john", "doe");
        Thread thread = new Thread(child);

        thread.start();
        try{
            thread.join();
        }catch(InterruptedException e) {}

        /*
            Έαν θέλουμε να χρησιμοποιήσουμε την δυνατότητα κληρονομικότητας που μας προσφέρει η Java τότε προτιμότερη
            λύση αποτελούν τα Runnables. Η διαφορά μεταξύ των Runnable και των Thread είναι ότι τα πρώτα είναι η λογική της
            εκτέλεσης, ενώ τα δεύτερα ο μηχανισμός της εκτέλεσης. Τα Runnable στη Java είναι υλοποιημένα ως Interfaces,
            οπότε μπορούμε να παρακάμψουμε την ιδιομορφία της Java να μην επιτρέπει σε μια κλάση να κληρονομεί (extends) πάνω
            από μια άλλη κλάση. Έτσι, μπορούμε να έχουμε μια κλάση Child που να κληρονομεί από μια άλλη κλάση Parent και
            η κλάση Child παράλληλα να έχει την δυνατότητα εκτέλεσης της από κάποιο Thread.
        */
    }

}

class ThreadA extends Thread{
    private int iterationCount;

    public ThreadA(int iterationCount){
        this.iterationCount = iterationCount;
    }

    @Override
    public void run() {
        System.out.println("Hello I am Thread A and I'm doing just fine! Iteration: " + iterationCount);
    }
}

class ThreadB extends Thread{
    private int iterationCount;

    public ThreadB(int iterationCount){
        this.iterationCount = iterationCount;
    }

    @Override
    public void run() {
        System.out.println("Hello I am Thread B and I'm not feeling really well... Iteration: " + iterationCount);
    }
}

class Parent{
    private String firstName;
    private String lastName;

    public Parent(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void greet(){
        System.out.println("Hello!");
    }
}

class Child extends Parent implements Runnable{
    public Child(String firstName, String lastName){
        super(firstName, lastName);
    }

    @Override
    public void run() {
        super.greet();
    }
}
