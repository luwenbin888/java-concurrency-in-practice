package org.luwenbin888.ch3;

import java.util.Random;

/**
 * In Theory, below MutableInteger has below problems: visibility: the set() to value maybe not visible to other thread when use get()
 */
public class MutableInteger {
    private int value;

    public int get() {
        return value;
    }

    public void set(int value) { this.value = value; }
}

class Main {
    static class WriterThread extends Thread {
        private MutableInteger mi;

        public WriterThread(MutableInteger mi) {
            this.mi = mi;
        }

        public void run() {
            int it_cnt = 0;
            Random rand = new Random();

            while (it_cnt <= 1000) {
                int nextValue = rand.nextInt(10000);
                mi.set(nextValue);
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
        private MutableInteger mi;

        public ReadThread(MutableInteger mi) {
            this.mi = mi;
        }

        public void run() {
            Random rand = new Random();

            while (true) {

                try {
                    Thread.sleep(rand.nextInt(10000));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                int gotValue = mi.get();
                System.out.println("Got value " + gotValue);
            }
        }
    }

    public static void main(String[] args) {
        MutableInteger mutableInteger = new MutableInteger();

        Thread[] readThreads = new ReadThread[10];
        for (int i = 0; i < 10; i++) {
            readThreads[i] = new ReadThread(mutableInteger);
            readThreads[i].start();
        }

        Thread writerThread = new WriterThread(mutableInteger);
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
