package com.zk.dblock;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class DbLockTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DbLockConfig.class);
        ctx.refresh();

        // 栅栏增加线程执行一致性
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

        // 模拟10个线程同时添加相同数据
       for(int i=0;i<10;i++){
           new Thread(new MultTask(ctx.getBean(DbLockService.class), "dblock", cyclicBarrier)).start();
       }

        /*try {
            ctx.getBean(DbLockService.class).addAuthcode("anotherlock");
        } catch (Exception e) {
            System.out.println("lock wrong .............................. ");
        }

        System.out.println("start to shut down..................");

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ctx.getBean(DbLockService.class).addAuthcode("anotherlock2");
        } catch (Exception e) {
            System.out.println("lock wrong .............................. ");
        }*/

        ctx.start();
    }


    static class MultTask implements Runnable {

        private DbLockService dbLockService;
        private String code;
        private CyclicBarrier cyclicBarrier;

        public MultTask(DbLockService dbLockService, String code, CyclicBarrier cyclicBarrier){
            this.dbLockService = dbLockService;
            this.code = code;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " wait");
                cyclicBarrier.await();
                int result = dbLockService.addAuthcode(code);
                System.out.println(Thread.currentThread().getName() + " result : " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("got error....................");
            }

        }
    }
}
