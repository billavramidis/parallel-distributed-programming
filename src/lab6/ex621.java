package lab6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ex621 {
    public static void main(String[] args) throws IOException {
        int numThreads = 0;
        Lock lock = new ReentrantLock();

        if (args.length != 3) {
            System.out.println("BruteForceStringMatch  <file name> <pattern> <number of threads>");
            System.exit(1);
        }

        try {
			numThreads = Integer.parseInt(args[2]);
		} catch (NumberFormatException nfe) {
			System.out.println("Integer argument expected");
			System.exit(1);
		}
		if (numThreads == 0)
			numThreads = Runtime.getRuntime().availableProcessors();

        String fileString = new String(Files.readAllBytes(Paths.get(args[0])));// , StandardCharsets.UTF_8);
        char[] text = new char[fileString.length()];
        int n = fileString.length();
        for (int i = 0; i < n; i++) {
            text[i] = fileString.charAt(i);
        }

        String patternString = args[1];
        char[] pattern = new char[patternString.length()];
        int m = patternString.length();
        for (int i = 0; i < m; i++) {
            pattern[i] = patternString.charAt(i);
        }

        int matchLength = n - m;
        char[] match = new char[matchLength + 1];
        for (int i = 0; i <= matchLength; i++) {
            match[i] = '0';
        }

        int[] matchCount = new int[1];

        // get current time
        long start = System.currentTimeMillis();

        StringThread[] threads = new StringThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new StringThread(matchCount, i, numThreads, m, matchLength, pattern, match, text, lock);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
			}
        }

        // get current time and calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis() - start;

        /* print results */
        // for (int i = 0; i < matchLength; i++) {
        // if (match[i] == '1') System.out.print(i+" ");
        // }
        // System.out.println();
        System.out.println("Total matches " + matchCount[0]);
        System.out.println("time in ms = " + elapsedTimeMillis);
    }

    static class StringThread extends Thread {
        private final int[] matchCount;
        private final char[] pattern;
        private final char[] text;
        private final char[] match;
        private final int myStart;
        private int myStop;
        private final int m;
        private final Lock lock;

        public StringThread(int[] matchCount, int myId, int numThreads, int m, int matchLength, char[] pattern, char[] match, char[] text, Lock lock){
            this.matchCount = matchCount;
            this.pattern = pattern;
            this.match = match;
            this.m = m;
            this.text = text;
            this.lock = lock;

            this.myStart = myId * (matchLength / numThreads);
			this.myStop = this.myStart + (matchLength / numThreads);
			if (myId == (numThreads - 1))
				this.myStop = matchLength;
        }

        public void run(){
            int localMatchCount = 0;

            for (int j = myStart; j < myStop; j++) {
                int i;
                for (i = 0; i < m && pattern[i] == text[i + j]; i++)
                    ;
                if (i >= m) {
                    match[j] = '1';
                    localMatchCount++;
                }
            }

            lock.lock();
            try{
                matchCount[0] += localMatchCount;
            }finally{
                lock.unlock();
            }
        }
    }

    /*
    CPU: 11th Gen Intel(R) Core(TM) i5-1135G7 @ 2.40GHz
    RAM: 8 GB

    Test Results (Brute Force String Match - E.coli DNA):

    File Size       | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
    ecoli16.txt       329ms       214ms       120ms       90ms
    ecoli32.txt       924ms       416ms       251ms       152ms
    ecoli64.txt       1564ms      766ms       497ms       300ms
*/
}
