package org.luwenbin888.ch1;

import org.luwenbin888.annotation.GuardedBy;
import org.luwenbin888.annotation.ThreadSafe;

import java.util.Arrays;
import java.util.Random;

/**
 * This class demonstrates the use of synchronization to mark a method as a critical section
 * @author Wenbin Lu
 */
@ThreadSafe
public class Sequence {
    @GuardedBy("this")
    private int nextValue = 1;

    /** Protected by this **/
    public synchronized int getNext() {
        return nextValue++;
    }
}

class Main {
    private static boolean test() {

        Sequence seq = new Sequence();

        int threadNum = 30;

        int[] result = new int[threadNum];

        Thread[] threads = new Thread[threadNum];

        Random rand = new Random();

        for (int i = 0; i < threadNum; i++) {
            final int j = i;
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(rand.nextInt(100));
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }

                result[j] = seq.getNext();
            });
        }

        for (int i = 0; i < threadNum; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threadNum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i: result) {
            System.out.print(i + ",");
        }
        System.out.println();

        Arrays.sort(result);

        for (int i: result) {
            System.out.print(i + ",");
        }
        System.out.println();

        for (int i = 1; i <= threadNum; i++) {
            if (result[i-1] != i) return false;
        }

        return true;
    }

    public static void main(String[] args) {
        int testCnt = 20;

        for (int i = 0; i < testCnt; i++) {
            System.out.println("============== Run " + (i + 1) + "===============");
            System.out.println(test());
            System.out.println("============== Run " + (i + 1) + "===============");
            System.out.println();
        }
    }
}
