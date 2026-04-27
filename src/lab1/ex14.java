package lab1;

public class ex14 {
    public static void main(String[] args){
        int numThreads = 10;

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new MultiplesThread(i + 1, numThreads);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try{
                threads[i].join();
            }catch (InterruptedException e){}
        }

        /*
            Στο συγκεκριμένο πείραμμα επιβεβαιώνεται το γεγονός ότι τα νήματα εκτελούνται αυτόνομα μεταξύ τους και ότι
            η σειρά εκτέλεσης τους εξαρτάται με τον τρόπο που θα τα δρομολογήσει το OS τη δεδομένη χρονική στιγμή. Βέβαια έαν
            κοιτάξουμε το terminal, παρατηρούμε ότι ο κώδικας που εκτελείται μέσα στα νήματα εκτελείται ακολουθιακά.
            Αυτό σημαίνει ότι πάντα πρώτα βρίσκει το πολλαπλάσιο i * n και μετά το πολλαπλάσιο (i+1) * n. Με άλλα λόγια,
            τα νήματα είναι υπομονάδες του προγράμματος που δεν εκτελούνται πάντα με την ίδια σειρά, αλλά ο κώδικας μέσα τους
            ακολουθεί τις αρχές του διαδικαστικού προγραμματισμού.
        */
    }
}

class MultiplesThread extends Thread{
    private int multiplier;
    private int numThreads;

    public MultiplesThread(int multiplier, int numThreads){
        this.multiplier = multiplier;
        this.numThreads = numThreads;
    }

    public void findMultiples(){
        for (int i = 0; i < numThreads; i++) {
            System.out.println(multiplier + " * " + (i + 1) + " = " + multiplier * (i + 1));
        }
    }

    @Override
    public void run() {
        System.out.println("Begging execution for Thread " + multiplier);
        findMultiples();
    }
}
