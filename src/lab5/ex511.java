package lab5;/* Matrix Addition A = B + C                       */

public class ex511 {
    public static void main(String[] args){
        int size = 0;
        int numThreads = 0;

        if (args.length != 2) {
            System.out.println("Usage: java ex511 <vector size> <number of threads>");
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

        double[][] a = new double[size][size];
        double[][] b = new double[size][size];
        double[][] c = new double[size][size];
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                a[i][j] = 0.0;
                b[i][j] = 0.3;
                c[i][j] = 0.5;
            }
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new MatrixAdditionThread(a, b, c, size, numThreads, i);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //Debugging.
        for (int i = 0; i < size; i++) {
          for (int j = 0; j < size; j++){
              System.out.print(a[i][j] + " ");
          }
          System.out.println();
        }
    }

    static class MatrixAdditionThread extends Thread{
        private final double[][] a;
        private final double[][] b;
        private final double[][] c;
        private final int size;
        private final int start;
        private int end;

        public MatrixAdditionThread(double[][] a, double[][] b, double[][] c, int size, int numThreads, int threadID){
            this.a = a;
            this.b = b;
            this.c = c;
            this.size = size;

            this.start = (size / numThreads) * threadID;
            this.end = start + (size / numThreads);
            if (threadID == (numThreads - 1)) this.end = size;

        }

        @Override
        public void run() {
            for (int i = start; i < end; i++){
                for (int j = 0; j < size; j++){
                    a[i][j] = b[i][j] + c[i][j];
                }
            }
        }
    }
}
