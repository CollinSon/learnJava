package com.github.collinson.learnreentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 许瑞锐
 * @date 2020/8/18 11:05
 * @description {java类描述}
 */
public class LockDemo {

    private static final Lock lock = new ReentrantLock(false);

    public static void main(String[] args) {
        new Thread(()->test(),"线程A").start();
        System.out.println("这里是主线程1");
        new Thread(()->test(),"线程B").start();
        System.out.println("这里是主线程2");
        new Thread(()->test(),"线程C").start();
        System.out.println("这里是主线程3");
        new Thread(()->test(),"线程D").start();
        System.out.println("这里是主线程4");
        new Thread(()->test(),"线程E").start();
        System.out.println("这里是主线程5");

    }

    public static void test() {
        for (int i = 0; i < 2; i++) {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "获取了锁");
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + "释放了锁");
                lock.unlock();
            }
        }
    }


}
