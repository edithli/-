package algs4.mixup;

import edu.princeton.cs.introcs.*;

import java.io.*;
import java.util.Scanner;


public class DemoCode {
    public static void main(String[] args) throws FileNotFoundException {
//        PlotFilter();
        FunctionGraph(20);
//        PlayConcertA();
//        PlayThatTune();
//        StdAudio.main(args);
    }
    private static void PlayThatTune() throws FileNotFoundException {
        String filename = "src/algs4/files/tomsdiner.txt";
        Scanner sc = new Scanner(new FileInputStream(filename));
        while (sc.hasNext()){
            int pitch = sc.nextInt();
            double duration = sc.nextDouble();
            double hz = 440 * Math.pow(2, pitch/12.0);
            int n = (int)(duration * StdAudio.SAMPLE_RATE);
            double[] samples = new double[n+1];
            for (int i = 0; i <= n; i++){
                samples[i] = Math.sin(2*Math.PI*hz*i/StdAudio.SAMPLE_RATE);
            }
            StdAudio.play(samples);
        }
    }
    private static void PlayConcertA(){
        int hz = 440; // hertz of concert A
        int sampling_rate = 44100; // 44100samples/seconds
        // function sin(2*PI*t*hz)
        int seconds = 2;
        int n = sampling_rate * seconds;
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++){
            a[i] = Math.sin(2*Math.PI*hz*i/sampling_rate);
        }
        StdAudio.play(a);
    }
    private static void FunctionGraph(int n){
        double[] x = new double[n+1];
        double[] y = new double[n+1];
        for (int i = 0; i <= n; i++){
            double xi = i* Math.PI/n;
            double yi = Math.sin(4*xi) + Math.sin(20*xi);
            StdDraw.point(xi, yi);
        }
        StdDraw.setXscale(0, Math.PI);
        StdDraw.setYscale(-2.0, 2.0);
        for (int i = 0; i < n; i++){
            StdDraw.line(x[i], y[i], x[i+1], y[i+1]);
        }
    }
    private static void PlotFilter() throws FileNotFoundException {
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream("src/algs4/files/USA.txt")));
        double x0 = scanner.nextDouble();
        double y0 = scanner.nextDouble();
        double x1 = scanner.nextDouble();
        double y1 = scanner.nextDouble();
        System.out.println(x0 + "\t" + y0);
        StdDraw.setXscale(x0, x1);
        StdDraw.setYscale(y0, y1);
        StdDraw.setPenRadius(0.005);
        StdDraw.enableDoubleBuffering();
        while (!scanner.hasNext()){
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            StdDraw.point(x, y);
        }
        System.out.println("finishing reading");
        StdDraw.show();
    }
}
