package lab5;

import java.lang.Math;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

class ex531 {
	
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
        
        // Saves Results but occupies large space
        ConcurrentLinkedQueue<String> output = new ConcurrentLinkedQueue<>();
        Thread[] threads = new Thread[numThreads];
        
        long start = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CircuitThread(iterations, size, numThreads, i, output);
            threads[i].start();
        }


        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
           
        long elapsedTimeMillis = System.currentTimeMillis()-start;

        Iterator<String> iterator = output.iterator();

        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

        System.out.println("Took " + elapsedTimeMillis + " ms");
        
    }

    static class CircuitThread extends Thread{
        private final int start;
        private int end;
        private final int size;
        private final ConcurrentLinkedQueue<String> output;
        private boolean[] v;

        public CircuitThread(int iterations, int size, int numThreads, int threadID, ConcurrentLinkedQueue<String> output){
            this.start = (iterations / numThreads) * threadID;
            this.end = start + (iterations / numThreads);

            if (threadID == (numThreads - 1)) this.end = iterations;

            this.size = size;
            this.output = output;

            this.v = new boolean[size];
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                checkCircuit(i, size, output);
            }
        }

        public void checkCircuit (int z, int size, ConcurrentLinkedQueue<String> output) {
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
                saveResult(size, z, output);
            }
        }

        public void saveResult (int size, int z, ConcurrentLinkedQueue<String> output) {
            String result = null;
            result = String.valueOf(z);

            for (int i=0; i< size; i++)
                if (v[i]) result += " 1";
                else result += " 0";

            //Just print result	for debugging
            //System.out.println(result);
            //Save result
            output.add("\n"+result);
        }
    }

    /*
        CPU: 11th Gen Intel( R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results:

        Vector Size | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
         23             146ms       131ms       110ms       112ms
         24             252ms       172ms       147ms       136ms
         25             527ms       315ms       245ms       239ms
         26             966ms       583ms       399ms       344ms
     */
}
