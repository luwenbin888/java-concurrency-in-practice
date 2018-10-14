package org.luwenbin888.misc;

import java.util.Random;

public class ProducerConsumerQueue {
    private final int MAX_SIZE = 10;
    private double[] data = new double[MAX_SIZE];

    // next producer index
    private int inBuf = 0;
    // next consumer index
    private int outBuf = 0;
    // total items in the queue
    private int count = 0;

    public synchronized void put(double d) throws InterruptedException {
        while (count == MAX_SIZE) {
            System.out.println("No space to put new items in queue, wait here...");
            this.wait();
        }

        data[inBuf] = d;
        inBuf = (inBuf + 1) % MAX_SIZE;

        count = count + 1;

        if (count == 1) {
            System.out.println("Notify consumer to continue get items...");
            this.notify();
        }
    }

    public synchronized double get() throws InterruptedException {
        while (count == 0) {
            System.out.println("Nothing to get from queue, wait here...");
            this.wait();
        }

        double value = data[outBuf];
        outBuf = (outBuf + 1) % MAX_SIZE;

        count = count - 1;

        if (count == MAX_SIZE - 1) {
            System.out.println("Notify producer to continue put items...");
            this.notify();
        }

        return value;
    }
}

class Main {
    private static class Producer extends Thread {
        private ProducerConsumerQueue queue;

        Random rand = new Random();

        public Producer(ProducerConsumerQueue q) {
            this.queue = q;
        }

        public void run() {
            while (true) {
                double value = rand.nextDouble();

                try {
                    queue.put(value);
                    System.out.println("Stored item " + value + " into queue");
                    Thread.sleep(rand.nextInt(1000));
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static class Consumer extends Thread {
        private ProducerConsumerQueue queue;

        Random rand = new Random();

        public Consumer(ProducerConsumerQueue q) {
            this.queue = q;
        }

        public void run() {
            while (true) {

                try {
                    double value = queue.get();
                    System.out.println("Get item from queue: " + value);
                    Thread.sleep(rand.nextInt(6000));
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerQueue queue = new ProducerConsumerQueue();
        Producer p = new Producer(queue);
        Consumer c = new Consumer(queue);

        p.start();
        c.start();
    }
}
