package lab7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ex71 {

    public static void main(String[] args) throws IOException {
        int numThreads = 0;

        if (args.length != 2) {
            System.out.println("StringHistogram <file_name> <number_of_threads>");
            System.exit(1);
        }

        try {
            numThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("argument " + args[1] + " must be int");
            System.exit(1);
        }

        StringHistogramThread[] threads = new StringHistogramThread[numThreads];

        String fileString = new String(Files.readAllBytes(Paths.get(args[0])));//, StandardCharsets.UTF_8);
        char[] text = new char[fileString.length()];
        int n = fileString.length();

        for (int i = 0; i < n; i++) {
            text[i] = fileString.charAt(i);
        }

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new StringHistogramThread(text, i, numThreads);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        int alphabetSize = 256;
        int[] histogram = new int[alphabetSize];

        for (int i = 0; i < numThreads; i++) {
            int[] local = threads[i].getLocalHistogram();
            for (int j = 0; j < alphabetSize; j++) {
                histogram[j] += local[j];
            }
        }

        long endTime = System.currentTimeMillis();


        for (int i = 0; i < alphabetSize; i++) {
            System.out.println((char)i+": "+histogram[i]);
        }

        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }

    static class StringHistogramThread extends Thread{
        private final int[] localHistogram;
        private final char[] text;
        private final int start;
        private int end;


        public StringHistogramThread(char[] text, int threadID, int numThreads){
            int alphabetSize = 256;
            this.localHistogram = new int[alphabetSize];

            this.text = text;

            int size = text.length;
            this.start = (size / numThreads) * threadID;
            this.end = this.start + (size / numThreads);
            if (threadID == (numThreads - 1)) this.end = size;
        }


        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                localHistogram[(int) text[i]]++;
            }
        }

        public int[] getLocalHistogram() {
            return localHistogram;
        }
    }

    /*
        CPU: 11th Gen Intel(R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results (Character Histogram - King James Bible):

        File Size          | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
        16timesbible.txt   | 166ms    | 220ms     | 76ms      | 60ms      |
        32timesbible.txt   | 261ms    | 241ms     | 132ms     | 107ms     |
        64timesbible.txt   | 840ms    | 300ms     | 289ms     | 142ms     |
        128timesbible.txt  | 2283ms   | 928ms     | 560ms     | 319ms     |
     */
}



