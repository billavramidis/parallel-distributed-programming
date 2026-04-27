package lab8;

import java.util.Random;

// Java program for Merge Sort
public class ex82 {
    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    public static void merge(int arr[], int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Copy data to temp arrays
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        // Merge the temp arrays
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using merge()
    public static void sort(int arr[], int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    // A utility function to print array of size n
    public static void printArray(int arr[]) {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }

    public static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false; // Βρέθηκε στοιχείο μεγαλύτερο από το επόμενο, άρα δεν είναι ταξινομημένος
            }
        }
        return true; // Όλα τα στοιχεία είναι σε σωστή σειρά
    }

    // Driver code
    public static void main(String[] args) {
        int size = 10000000;
        int[] array = new int[size];
        Random rand = new Random();

        // Γέμισμα με τυχαίους αριθμούς
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(1000000);
        }

        Recursive mythread = new Recursive(0, size, 100, array);

        long startTime = System.currentTimeMillis();

        mythread.start();

        /* wait for thread 0 */
        try {
            mythread.join();
        } catch (InterruptedException e) {
        }

        long endTime = System.currentTimeMillis();

        System.out.println(isSorted(array));
        System.out.printf("time to sort = %f seconds\n", (double) (endTime - startTime) / 1000);
    }


    public static class Recursive extends Thread {
        private final int myFrom;
        private final int myTo;
        private final int myLimit;
        private final int[] myA;
        public int myResult;

        public Recursive(int from, int to, int limit, int[] a) {
            this.myFrom = from;
            this.myTo = to;
            this.myLimit = limit;
            this.myA = a;
            this.myResult = 0;
        }

        public void run() {
            int workload = myTo - myFrom;
            if (workload <= myLimit) {
                sort(myA, myFrom, myTo - 1);
            } else {
                int mid = myFrom + workload / 2;
                Recursive threadL = new Recursive(myFrom, mid, myLimit, myA);
                Recursive threadR = new Recursive(mid, myTo, myLimit, myA);

                threadL.start();
                threadR.start();

                try {
                    threadL.join();
                    threadR.join();
                    merge(myA, myFrom, mid - 1, myTo - 1);
                } catch (InterruptedException e) {}
            }
        }
    }

    /*
    REPORT: Parallel Merge Sort Performance Analysis
    CPU: AMD Ryzen AI 7 350 (8 Cores / 16 Threads)
    RAM: 32 GB
    Dataset: 10,000,000 integers (Random)
    Implementation: Recursive Multithreading (extends Thread)

    ----------------------------------------------------------
    Test Results:
    ----------------------------------------------------------
    Array Size  |  Threshold (Limit) |  Execution Time (sec)
    ----------------------------------------------------------
    10,000,000  |  1,000             |  12.873 s // Απίστευτος χρόνος!!! Δημιουργεί χιλιάδες threads, μη-αποδοτικό!
    10,000,000  |  10,000            |  0.711 s
    10,000,000  |  100,000           |  0.527 s
    10,000,000  |  1,000,000         |  0.450 s
    10,000,000  |  10,000,000 |  1.878 s // Ουσιαστικά, το υπολογίζει σειριακά...
    ---------------------------------------------------------- */
}
