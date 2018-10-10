package org.luwenbin888.ch2;

public class LazyInitRace {
    private static LazyInitRace instance = null;

    private LazyInitRace() {}

    public static LazyInitRace getInstance() {
        if (instance == null)
            instance = new LazyInitRace();
        return instance;
    }
}

class Main{
    public static void main(String[] args) {
        int instanceNum = 20;
        LazyInitRace[] instances = new LazyInitRace[instanceNum];

        Thread[] threads = new Thread[instanceNum];

        Object obj = new Object();

        for (int i = 0; i < instanceNum; i++) {
            final int j = i;
            threads[i] = new Thread(() -> {

                synchronized(obj) {
                    try {
                        obj.wait();
                    }
                    catch(InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                instances[j] = LazyInitRace.getInstance();
            });
        }

        for (int i = 0; i < instanceNum; i++) {
            threads[i].start();
        }

        try {
            System.out.println("Waiting 1 sec");
            Thread.sleep(1000);
        }
        catch(InterruptedException ex) {
            ex.printStackTrace();
        }

        synchronized(obj) {
            obj.notifyAll();
        }

        for (int i = 0; i < instanceNum; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        for (LazyInitRace instance: instances) {
            System.out.println(instance);
        }

        int i = 0;
        for (; i < instanceNum - 1; i++) {
            if (instances[i] != instances[i + 1]) {
                System.out.println("Distinct LazyInitRace instances created...");
                break;
            }
        }

        if (i == instanceNum - 1) {
            System.out.println("All LazyInitRace instances are equals");
        }
    }
}
