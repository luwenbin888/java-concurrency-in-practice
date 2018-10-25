package org.luwenbin888.ch3;

/**
 * As to the Java concurrency book, below program will face:
 * 1) never end: the set ready to true in main thread may not be know by ReaderThread
 * 2) the number output maybe 0 as JVM may do some instruction reorder, so set to ready=true may come first
 *    before set number to 42
 *
 *    In my limited testing, the output is always 42
 */
public class NoVisibility {

    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();

            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();

        number = 42;
        ready = true;
    }
}
