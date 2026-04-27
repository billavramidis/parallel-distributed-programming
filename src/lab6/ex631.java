package lab6;

import java.util.ArrayList;
import java.util.List;

public class ex631 {
	
	public static void main(String[] args) {  
        int numThreads = 0;
        int size = 0;


        if (args.length != 2) {
            System.out.println("Usage: java ex531 <vector size> <number of threads>");
            System.exit(1);
        }

        try {
            size = Integer.parseInt(args[0]);
            numThreads = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }

        if (numThreads == 0) numThreads = Runtime.getRuntime().availableProcessors();

		if (size <= 0) {
			System.out.println("size should be positive integer");
			System.exit(1);
        }

        int iterations = (int) Math.pow(2, size);

        List<String> finalOutput = new ArrayList<>();
        CircuitThread[] threads = new CircuitThread[numThreads];
        
        long start = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CircuitThread(iterations, size, numThreads, i);
            threads[i].start();
        }


        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                finalOutput.addAll(threads[i].getLocalOutput());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
           
        long elapsedTimeMillis = System.currentTimeMillis()-start;

        for (String s : finalOutput) {
            System.out.println(s);
        }

        System.out.println("Took " + elapsedTimeMillis + " ms");
        
    }

    static class CircuitThread extends Thread{
        private final int start;
        private int end;
        private final int size;
        private final int threadId;
        private boolean[] v;
        private final ArrayList<String> localOutput;

        public CircuitThread(int iterations, int size, int numThreads, int threadID){
            this.start = (iterations / numThreads) * threadID;
            this.end = start + (iterations / numThreads);

            if (threadID == (numThreads - 1)) this.end = iterations;

            this.size = size;
            this.localOutput = new ArrayList<>();
            this.threadId = threadID;

            this.v = new boolean[size];
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                checkCircuit(i, size, localOutput, threadId);
            }
        }

        public void checkCircuit(int z, int size, ArrayList<String> localOutput, int threadID) {
            for (int i = size-1; i >= 0; i--)
                v[i] = (z & (1 << i)) != 0;


            boolean value =
                    (  v[0]  ||  v[1]  )
                            && ( !v[1]  || !v[3]  )
                            && (  v[2]  ||  v[3]  )
                            && ( !v[3]  || !v[4]  )
                            && (  v[4]  || !v[5]  )
                            && (  v[5]  || !v[6]  )
                            && (  v[5]  ||  v[6]  )
                            && (  v[6]  || !v[15] )
                            && (  v[7]  || !v[8]  )
                            && ( !v[7]  || !v[13] )
                            && (  v[8]  ||  v[9]  )
                            && (  v[8]  || !v[9]  )
                            && ( !v[9]  || !v[10] )
                            && (  v[9]  ||  v[11] )
                            && (  v[10] ||  v[11] )
                            && (  v[12] ||  v[13] )
                            && (  v[13] || !v[14] )
                            && (  v[14] ||  v[15] )
                            && (  v[14] ||  v[16] )
                            && (  v[17] ||  v[1]  )
                            && (  v[18] || !v[0]  )
                            && (  v[19] ||  v[1]  )
                            && (  v[19] || !v[18] )
                            && ( !v[19] || !v[9]  )
                            && (  v[0]  ||  v[17] )
                            && ( !v[1]  ||  v[20] )
                            && ( !v[21] ||  v[20] )
                            && ( !v[22] ||  v[20] )
                            && ( !v[21] || !v[20] )
                            && (  v[22] || !v[20] );


            if (value) {
                saveResult(size, z, localOutput, threadID);
            }
        }

        public void saveResult(int size, int z, ArrayList<String> localOutput, int threadID) {
            String result = null;
            result = String.valueOf(z);

            for (int i=0; i< size; i++)
                if (v[i]) result += " 1";
                else result += " 0";

            //Just print result	for debugging
            //System.out.println(result);
            //Save result
            localOutput.add("\n"+result);
        }

        public List<String> getLocalOutput() {
            return localOutput;
        }
    }

    /*
        CPU: 11th Gen Intel( R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results:

        Vector Size | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
         23             129ms       83ms       116ms       142ms
         24             242ms       159ms       148ms       137ms
         25             458ms       279ms       214ms       204ms
         26             915ms       553ms       376ms       370ms
     */

    /* θεώρησα καλύτερη πρακτική να έχω σε κάθε νήμα από μια τοπική λίστα στην οποία θα αποθηκεύω το αποτέλεσμα
        τοπικά και ύστερα καθώς κάνω join τα νήματα να τις συνενώσω σε μια τελική λίστα όπου και θα εκτυπώνω στην
        κονσόλα. Γίνεται θεωρητικά να το κάνει κάποιος με μια List<List<String>> όμως θα είχαμε περιττές αναφορές στο heap
        καθώς θα έπρεπε να κάνουμε συνέχεια .get(i) για να πάρουμε την i-οστη λίστα από την λίστα των λιστών, ενώ
        με τις τοπικές λίστες έχουμε αναφορά στο stack του κάθε νήματος στο οποίο έχει αποθηκευμένη την διεύθυνση της λίστας.
        Οπότε κατέληξα στην κομψή πρακτική του map-reduce.
     */
}
