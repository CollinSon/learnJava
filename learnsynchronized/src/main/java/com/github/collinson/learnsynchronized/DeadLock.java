package com.github.collinson.learnsynchronized;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 许瑞锐
 * @date 2020/8/24 21:48
 * @description 死锁练习
 */
public class DeadLock {

    public static final String RESOURCE1 = "resource1";

    public static final String resource2 = "resource2";

    public static Lock lock1 = new ReentrantLock();

    public static Lock lock2 = new ReentrantLock();


    public static void main(String[] args) {

        //synchronized 模拟死锁情况

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (RESOURCE1){
                    System.out.println(Thread.currentThread().getName()+"锁定"+RESOURCE1);
                    System.out.println("睡眠3s等待其他线程。。。");
                    synchronized (resource2){
                        System.out.println(Thread.currentThread().getName()+"锁定"+resource2);
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (resource2){
                    System.out.println(Thread.currentThread().getName()+"锁定"+resource2);
                    System.out.println("睡眠3s等待其他线程。。。");
                    synchronized (RESOURCE1){
                        System.out.println(Thread.currentThread().getName()+"锁定"+RESOURCE1);
                    }
                }
            }
        });


//        thread1.start();
//        thread2.start();

        //使用ReentrantLock模拟死锁
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock1.lock();
                }catch (Exception e){
                    System.out.println("ReentrantLock1锁失败");
                }
                System.out.println("ReentrantLock1锁成功");
                System.out.println(Thread.currentThread().getName()+"睡眠3s等待其他线程。。。");
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    System.out.println("睡眠失败");
                }
                for (;;){
                    boolean tryLockResult=lock2.tryLock();
                    System.out.println(Thread.currentThread().getName()+"试图锁lock2,结果为"+tryLockResult);
                    if (!tryLockResult){
                        System.out.println("锁lock2失败，睡眠3s后尝试");
                        try {
                            lock1.unlock();
                        }catch (Exception e){
                            System.out.println("打断lock1锁失败");
                        }
                        System.out.println("lock1锁打断成功");
                        try {
                            Thread.sleep(3000);
                        }catch (Exception e){
                            System.out.println("睡眠失败");
                        }
                    }

                }
            }

        });

        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock2.lock();
                }catch (Exception e){
                    System.out.println("ReentrantLock2锁失败");
                }
                System.out.println("ReentrantLock2锁成功");
                System.out.println(Thread.currentThread().getName()+"睡眠3s等待其他线程。。。");
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    System.out.println("睡眠失败");
                }
                for (;;){
                    boolean tryLockResult=lock1.tryLock();
                    System.out.println(Thread.currentThread().getName()+"试图锁lock1,结果为"+tryLockResult);
                    if (!tryLockResult){
                        System.out.println("锁lock1失败，睡眠3s后尝试");
                        try {
                            Thread.sleep(3000);
                        }catch (Exception e){
                            System.out.println("睡眠失败");
                        }
                    }

                }
            }

        });

        thread3.start();
        thread4.start();





    }
}
