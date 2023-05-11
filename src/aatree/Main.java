package aatree;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
    public static final int ARRAY_SIZE = 10_000;

    public static final int SEARCH_TEST_SIZE = 100;

    public static final int DELETE_SIZE = 1000;

    public static void main(String[] args) throws FileNotFoundException {
        AATree tree = new AATree();
        int[] a = randomArray();

        insertionTest(tree, a);

        Arrays.sort(a);
        
        searchTest(tree, a);
        deleteTest(tree, a);
    }
    public static void insertionTest(AATree tree, int[] a) throws FileNotFoundException {
        PrintWriter out = new PrintWriter("insert output.txt");
        int totalIters = 0;
        long startTimeTotal = System.nanoTime();
        for (int j = 0; j < a.length; j++) {
            int i = a[j];
            long startTime = System.nanoTime();
            int iters = tree.insert(i);
            totalIters+=iters;
            float duration = ((System.nanoTime() - startTime) / 1000f);
            out.println(j+" "+iters+" "+duration);
        }
        int avgIters = totalIters/a.length;
        float avgTime = (System.nanoTime() - startTimeTotal)/1000/1000f/a.length;
        out.println(avgIters+" "+ avgTime);
        out.close();
    }

    public static void searchTest(AATree tree, int[] a) throws FileNotFoundException {
        PrintWriter out = new PrintWriter("search output.txt");
        int totalIters = 0;
        long startTimeTotal = System.nanoTime();
        for (int j = 0; j < SEARCH_TEST_SIZE; j++) {
            int i = a[(int)(Math.random()*a.length)];
            long startTime = System.nanoTime();
            int iters = tree.lookFor(i);
            totalIters+=iters;
            float duration = ((System.nanoTime() - startTime) / 1000f);
            out.println(j+" "+iters+" "+duration);
        }
        int avgIters = totalIters/SEARCH_TEST_SIZE;
        float avgTime = (System.nanoTime() - startTimeTotal)/1000/1000f/SEARCH_TEST_SIZE;
        out.println(avgIters+" "+ avgTime);
        out.close();
    }

    public static void deleteTest(AATree tree, int[] a) throws FileNotFoundException {
        PrintWriter out = new PrintWriter("delete output.txt");
        int totalIters = 0;
        long startTimeTotal = System.nanoTime();
        for (int j = 0; j < DELETE_SIZE; j++) {
            int i = a[(int)(Math.random()*a.length)];
            long startTime = System.nanoTime();
            int iters = tree.delete(i);
            totalIters += iters;
            float duration = ((System.nanoTime() - startTime)/1000/1000f);
            out.println(j+" "+iters+" "+duration);
        }
        int avgIters = totalIters/DELETE_SIZE;
        float avgTime = (System.nanoTime() - startTimeTotal)/1000/1000f/DELETE_SIZE;
        out.println(avgIters+" "+ avgTime);
        out.close();
    }

    public static int[] randomArray(){
        int[] a = new int[ARRAY_SIZE];

        for (int i = 0; i < a.length; i++) {
            a[i] = (int)(Math.random()*100_000_000);
        }

        return a;
    }
}