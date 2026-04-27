package lab7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;

public class ex72 {
    public static void main(String[] args) throws IOException {
        int numThreads = 0;
    
        if (args.length != 2) {
			System.out.println("WordCount  <file_name> <number_of_threads>");
			System.exit(1);
        }

        try {
            numThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("argument " + args[1] + " must be int");
            System.exit(1);
        }
        
        String fileString = new String(Files.readAllBytes(Paths.get(args[0])));
        String[] words = fileString.split("[ \n\t\r.,;:!?(){}]");
        WordCountThread[] threads = new WordCountThread[numThreads];
        
        TreeMap<String, Integer> map = new TreeMap<String, Integer>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new WordCountThread(words, i, numThreads);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < numThreads; i++) {
            TreeMap<String, Integer> localMap = threads[i].getLocalMap();

            localMap.forEach((key, value) ->
                    map.merge(key, value, Integer::sum)
            );
        }

        long endTime = System.currentTimeMillis();

        for (String name: map.keySet()) {
            String value = map.get(name).toString();
    		System.out.println(name + "\t " + value);
        }

        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);

    }

    static class WordCountThread extends Thread{
        private final TreeMap<String, Integer> localMap;
        private final String[] words;
        private final int start;
        private int end;

        public WordCountThread(String[] words, int threadID, int numThreads){
            this.localMap = new TreeMap<>();
            this.words = words;

            int size = words.length;
            this.start = (size / numThreads) * threadID;
            this.end = this.start + (size / numThreads);
            if (threadID == (numThreads - 1)) this.end = size;
        }

        @Override
        public void run() {
            for (int wordCounter = start; wordCounter < end; wordCounter++) {
                String key = words[wordCounter].toLowerCase();
                if (!key.isEmpty()) {
                    if (localMap.get(key) == null) {
                        localMap.put(key, 1);
                    }
                    else {
                        int value = localMap.get(key);
                        value++;
                        localMap.put(key, value);
                    }
                }
            }
        }

        public TreeMap<String, Integer> getLocalMap() {
            return localMap;
        }
    }

    /*
        CPU: 11th Gen Intel(R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results (Word Count TreeMap - King James Bible):

        File Size          | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
        8timesbible.txt    | 1800ms   | 986ms     | 600ms     | 437ms     |
        16timesbible.txt   | 3235ms   | 2010ms    | 1203ms    | 968ms     |
        32timesbible.txt   | 6439ms   | 3688ms    | 2286ms    | 2496ms    |
     */
}
