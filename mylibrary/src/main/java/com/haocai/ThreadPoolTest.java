package com.haocai;

/**
 * Created by Xionghu on 2018/7/4.
 * Desc:
 */

public class ThreadPoolTest {


    static  class MyRunnable implements Runnable{
        public volatile  boolean flag = true;
        @Override
        public void run() {
            while (flag&&!Thread.interrupted()) {
                try {
                    System.out.println("running");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

            }
            System.out.println("Thread running");
        }
    }
    public static void main(String args[]) throws InterruptedException {

//        final LinkedBlockingDeque queue = new LinkedBlockingDeque<Runnable>(10);
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 60, TimeUnit.MILLISECONDS, queue);
//
//        for (int i = 0; i < 16; i++) {
//            final int index = i;
//            threadPoolExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("index " + index +" queueSize=" +queue.size());
//                }
//            });
//        }


        MyRunnable runnable = new MyRunnable();

        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(1000);
        runnable.flag = false;
        thread.interrupt();

    }
}
