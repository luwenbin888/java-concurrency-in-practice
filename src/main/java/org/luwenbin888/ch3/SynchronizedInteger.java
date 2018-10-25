package org.luwenbin888.ch3;

import java.util.Random;

public class SynchronizedInteger {
    private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value) { this.value = value; }
}

class SynchronizedIntegerMain {
    static class WriterThread extends Thread {
        private SynchronizedInteger si;

        public WriterThread(SynchronizedInteger si) {
            this.si = si;
        }

        public void run() {
            int it_cnt = 0;
            Random rand = new Random();

            while (it_cnt <= 1000) {
                int nextValue = rand.nextInt(10000);
                si.set(nextValue);
                System.out.println("Set value to " + nextValue);
                try {
                    Thread.sleep(rand.nextInt(10000));
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    static class ReadThread extends Thread {
        private SynchronizedInteger si;

        public ReadThread(SynchronizedInteger si) {
            this.si = si;
        }

        public void run() {
            Random rand = new Random();

            while (true) {

                try {
                    Thread.sleep(rand.nextInt(10000));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                int gotValue = si.get();
                System.out.println("Got value " + gotValue);
            }
        }
    }

    public static void main(String[] args) {
        SynchronizedInteger synchronizedInteger = new SynchronizedInteger();

        Thread[] readThreads = new ReadThread[10];
        for (int i = 0; i < 10; i++) {
            readThreads[i] = new ReadThread(synchronizedInteger);
            readThreads[i].start();
        }

        Thread writerThread = new WriterThread(synchronizedInteger);
        writerThread.start();

        while (writerThread.isAlive()) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        for (int i = 0; i < 10; i++) {
            readThreads[i].interrupt();
        }

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.exit(0);
    }
}
