package org.luwenbin888.misc;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerBlockingQueue {
    private final int MAX_SIZE = 10;
    private BlockingQueue<Double> queue = new LinkedBlockingQueue<>(MAX_SIZE);

    public void put(double item) throws InterruptedException {
        queue.put(item);
    }

    public double get() throws InterruptedException {
        double value = queue.take();
        return value;
    }
}

class Main1 {
    private static class Producer extends Thread {
        private ProducerConsumerBlockingQueue queue;

        Random rand = new Random();

        public Producer(ProducerConsumerBlockingQueue q) {
            this.queue = q;
        }

        public void run() {
            while (true) {
                double value = rand.nextDouble();
                System.out.println("Put item " + value + " into queue");
                try {
                    queue.put(value);
                    Thread.sleep(1000);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static class Consumer extends Thread {
        private ProducerConsumerBlockingQueue queue;

        Random rand = new Random();

        public Consumer(ProducerConsumerBlockingQueue q) {
            this.queue = q;
        }

        public void run() {
            while (true) {
                try {
                    double value = queue.get();
                    System.out.println("Got item " + value + " from queue");
                    Thread.sleep(5000);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerBlockingQueue queue = new ProducerConsumerBlockingQueue();
        Producer p = new Producer(queue);
        Consumer c = new Consumer(queue);

        p.start();
        c.start();
    }
}
