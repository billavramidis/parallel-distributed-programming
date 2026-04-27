package lab5;/* Vector Addition a = b + c                       */

public class ex512 {
	public static void main(String[] args) {
        int size = 0;
        int numThreads = 0;

        if (args.length != 2) {
            System.out.println("Usage: java ex512 <vector size> <number of threads>");
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

        double[] a = new double[size];
        double[] b = new double[size];
        double[] c = new double[size];
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < size; i++) {
            a[i] = 0.0;
            b[i] = 1.0;
            c[i] = 0.5;
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new VectorAdditionThread(a, b, c, size, numThreads, i);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try{
                threads[i].join();
            }catch (InterruptedException e){
                throw new RuntimeException();
            }
        }

        //Debugging.
        for (int i = 0; i < size; i++) {
            System.out.print(a[i] + " ");
        }

    }

    static class VectorAdditionThread extends Thread{
        private final double[] a;
        private final double[] b;
        private final double[] c;
        private final int start;
        private int end;

        public VectorAdditionThread(double[] a, double[] b, double[] c, int size, int numThreads, int threadID){
            this.a = a;
            this.b = b;
            this.c = c;
            this.start = (size / numThreads) * threadID;
            this.end = start + (size / numThreads);
            if (threadID == (numThreads - 1)) this.end = size;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                a[i] = b[i] + c[i];
            }
        }
    }
}
