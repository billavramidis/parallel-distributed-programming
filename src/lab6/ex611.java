package lab6;

public class ex611 {
    public static void main(String[] args) {

        long numSteps = 0L;
        int numThreads = 0;

        if (args.length != 2) {
            System.out.println("arguments:  number_of_steps   number_of_threads");
            System.exit(1);
        }

        try {
            numSteps = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("argument "+ args[0] +" must be long int");
            System.exit(1);
        }

        try {
            numThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("argument "+ args[1] +" must be int");
            System.exit(1);
        }

        Thread[] threads = new Thread[numThreads];
        double[] sumArray = new double[numThreads];

        /* start timing */
        long startTime = System.currentTimeMillis();

        double step = 1.0 / (double)numSteps;

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new PiThread(sumArray, step, numSteps, numThreads, i);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long endTime = System.currentTimeMillis();

        double pi = 0.0;

        for (int i = 0; i < numThreads; i++) {
            pi += sumArray[i];
        }

        System.out.printf("sequential program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n" , pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }

    static class PiThread extends Thread{
        private final double[] sumArray;
        private final int threadId;
        private final double step;
        private final long start;
        private long end;

        public PiThread(double[] sumArray, double step, long numOfSteps, int numThreads, int threadID){
            this.step = step;
            this.sumArray = sumArray;
            this.threadId = threadID;

            this.start = (numOfSteps / numThreads) * threadID;
            this.end = start + (numOfSteps / numThreads);
            if (threadID == (numThreads - 1)) this.end = numOfSteps;
        }

        @Override
        public void run() {
            double localSum = 0.0;

            for (long i=start; i < end; ++i) {
                double x = ((double)i+0.5)*step;
                localSum += 4.0/(1.0+x*x);
            }
            sumArray[threadId] = localSum * step;
        }

        /*
        CPU: 11th Gen Intel( R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results:

        Number of Steps | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
         10^6             0.005s      0.008s       0.011s       0.015s
         10^7             0.023s      0.017s       0.014s       0.015s
         10^8             0.247s      0.013s       0.066s       0.042s
         10^9             1.941s      1.002s       0.563s       0.323s
     */
    }
}
