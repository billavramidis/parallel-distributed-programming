package lab8;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ex81 {
    public static void main(String[] args) {
        int numSteps = 0;

        if (args.length != 1) {
            System.out.println("arguments:  number_of_steps");
            System.exit(1);
        }

        try {
            numSteps = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("argument " + args[0] + " must be int");
            System.exit(1);
        }

        double[] sumArray = new double[numSteps];
        double step = 1.0 / (double) numSteps;

        long startTime = System.currentTimeMillis();

        double pi = sumVectorUsingForkJoin(sumArray, numSteps, step);

        long endTime = System.currentTimeMillis();

        System.out.printf("recursive program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n" , pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }

    private static double sumVectorUsingForkJoin(double[] sumArray, int arraySize, double step) {
        double total = 0.0;
        ForkJoinPool pool = new ForkJoinPool(); // create thread pool for fork/join
        PiCalculationTask task = new PiCalculationTask(0, arraySize, step);
        total = pool.invoke(task); // submit the task to fork/join pool
        pool.shutdown();
        return total;
    }

    public static class PiCalculationTask extends RecursiveTask<Double> {

        private static final int acceptableSize = 10000000;
        private final int start;
        private final int end;
        private final double step;

        public PiCalculationTask(int start, int end, double step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        protected Double compute() {
            double sum = 0.0;
            int workLoadSize = end - start;
            if (workLoadSize < acceptableSize) {
                for (long i = start; i < end; ++i) {
                    double x = ((double) i + 0.5) * step;
                    sum += 4.0 / (1.0 + x * x) * step;
                }
            } else {
                int mid = start + workLoadSize / 2;
                PiCalculationTask left = new PiCalculationTask(start, mid, step);
                PiCalculationTask right = new PiCalculationTask(mid, end, step);

                left.fork();
                double rightResult = right.compute();
                double leftResult = left.join();
                sum = leftResult + rightResult;
            }
            return sum;
        }
    }

    /*
    CPU: AMD Ryzen AI 7 350 w/ Radeon 860M (8 Cores / 16 Threads)
    RAM: 32 GB
    Task: Pi Approximation (100,000,000 steps)

    Test Results:

    Threshold Size | Execution Time (ms) |
    --------------------------------------
    100            | 153ms               |
    1,000          | 394ms               |
    10,000         | 426ms               |
    100,000        | 89ms                |
    1,000,000      | 102ms               |
    10,000,000     | 154ms               |
*/

    /*
    Παρατήρησα ότι η μέγιστη επίδοση στον συγκεκριμένο επεξεργαστή είναι με threshold 100.000 γιατί
    δημιουργούνται περίπου 1000 tasks και είναι αρκετά για να κρατάνε τα 16 threads της CPU απασχολημένα
    αλλά όχι τόσο όσο για να υπάρχει χάος στη διαχείριση.
     */
}
