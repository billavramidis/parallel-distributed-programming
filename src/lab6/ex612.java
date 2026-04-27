package lab6;

public class ex612 {
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
            System.out.println("argument "+ args[2] +" must be int");
            System.exit(1);
        }

        Thread[] threads = new Thread[numThreads];
        double[] pi = new double[1];

        /* start timing */
        long startTime = System.currentTimeMillis();

        double step = 1.0 / (double)numSteps;

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new PiThread(pi, step, numSteps, numThreads, i);
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

        System.out.printf("sequential program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n" , pi[0]);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi[0] - Math.PI));
        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }

    static class PiThread extends Thread{
        private final double[] pi;
        private final double step;
        private final long start;
        private long end;

        public PiThread(double[] pi, double step, long numOfSteps, int numThreads, int threadID){
            this.pi = pi;
            this.step = step;

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

            updatePi(localSum);
        }

        public synchronized void updatePi(double localSum){
            pi[0] += localSum * step;
        }

        /*
        CPU: 11th Gen Intel( R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results:

        Number of Steps | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
         10^6             0.006s      0.007s       0.010s       0.015s
         10^7             0.024s      0.017s       0.016s       0.014s
         10^8             0.198s      0.109s       0.066s       0.051s
         10^9             1.933s      1.000s       0.570s       0.315s
     */
    }
}
