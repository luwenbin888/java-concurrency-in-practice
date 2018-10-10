package org.luwenbin888.ch1;

import org.luwenbin888.annotation.NotThreadSafe;

import java.util.Random;

/**
 * This class demonstrates that without synchronization, race condition may happen
 * @author Wenbin Lu
 */
@NotThreadSafe
public class UnsafeSequence {
    private int value = 1;

    /** return a unique value. **/
    public int getNext() {
        return value++;
    }

    public static void main(String[] args) {
        UnsafeSequence unsafeSeq = new UnsafeSequence();

        int threadNum = 30;
        Random rand = new Random();

        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            threads[i] = new Thread(() -> {
                try {
                    /** add the possibility that thread may interleave **/
                    Thread.sleep(rand.nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(unsafeSeq.getNext());
            });
        }

        for (int i = 0; i < threadNum; i++) {
            threads[i].start();
        }
    }
}
