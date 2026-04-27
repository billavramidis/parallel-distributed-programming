package lab5;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ex521 {
    public static void main(String[] args) {
        int numThreads = 0;

        String fileNameR = null;
        String fileNameW = null;

        //Input and Output files using command line arguments
        if (args.length != 3) {
            System.out.println("Usage: java ex521 <file to read > <file to write> <number of threads>");
            System.exit(1);
        }
        fileNameR = args[0];
        fileNameW = args[1];

        try {
            numThreads = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }
        if (numThreads == 0) numThreads = Runtime.getRuntime().availableProcessors();

        //Reading Input file to an image
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileNameR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Start timing
        long start = System.currentTimeMillis();

        Thread[] threads = new Thread[numThreads];


        for (int i = 0; i < numThreads; i++) {
            threads[i] = new RGBConvertorThread(img, numThreads, i);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        //Stop timing
        long elapsedTimeMillis = System.currentTimeMillis()-start;

        //Saving the modified image to Output file
        try {
            File file = new File(fileNameW);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {}

        System.out.println("Done...");
        System.out.println("time in ms = "+ elapsedTimeMillis);
    }

    static class RGBConvertorThread extends Thread{
        private final BufferedImage image;
        private final int start;
        private int end;

        public RGBConvertorThread(BufferedImage image, int numThreads, int threadID){
            this.image = image;

            this.start = (image.getHeight() / numThreads) * threadID;
            this.end = start + (image.getHeight() / numThreads);

            if (threadID == (numThreads - 1)) this.end = image.getHeight();
        }

        @Override
        public void run() {
            //Coefficients of RGB to GrayScale.
            double redCoefficient = 0.299;
            double greenCoefficient = 0.587;
            double blueCoefficient = 0.114;

            for (int y = start; y < end; y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    //Retrieving contents of a pixel
                    int pixel = image.getRGB(x,y);
                    //Creating a Color object from pixel value
                    Color color = new Color(pixel, true);
                    //Retrieving the R G B values, 8 bits per r,g,b
                    //Calculating GrayScale
                    int red = (int) (color.getRed() * redCoefficient);
                    int green = (int) (color.getGreen() * greenCoefficient);
                    int blue = (int) (color.getBlue() * blueCoefficient);
                    //Creating new Color object
                    color = new Color(red+green+blue,
                            red+green+blue,
                            red+green+blue);
                    //Setting new Color object to the image
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }
    }

    /*
        CPU: 11th Gen Intel( R) Core(TM) i5-1135G7 @ 2.40GHz
        RAM: 8 GB

        Test Results:

        Image Size | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
  1)     719 KB      283ms       203ms       184ms       148ms
  2)     122 KB      94ms        97ms        86ms        69ms
  3)     527 KB      137ms       131ms       114ms       98ms
  4)    4879 KB      795ms       477ms       412ms       313ms
  5)    1805 KB      880ms       538ms       491ms       355ms
  6)    2957 KB      847ms       574ms       513ms       400ms
  7)    6953 KB      853ms       549ms       483ms       333ms
     */

}
