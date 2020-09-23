import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author 许瑞锐
 * @date 2020/8/23 20:23
 * @description 线程池学习
 */
public class ExecutorPoolMain {



    @Test
    public  void fixedThreadPoolTest(){
        //        官方提供了好几种线程池的创建方法
        //newFixedThreadPool只使用固定数量的线程进行执行，即使添加多个任务进入线程池也最终执行固定数量的线程
        ExecutorService pool = Executors.newFixedThreadPool(2);

        int executeTimes=0;
        while (executeTimes <= 15) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "is working");
            });

            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "is working");
            });

            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "is working");
            });
            executeTimes++;
        }

        //这里的输出结果永远没有第三条线程，因为这个线程池仅仅使用固定数量的线程
    }

    @Test
    public void  singleThreadExecutorTest(){
        //newSingleThreadExecutor只允许一条线程在线程池中
        ExecutorService pool = Executors.newSingleThreadExecutor();

        int executeTimes=0;
        while (executeTimes <= 15) {
            pool.execute(() -> {
                System.out.println("this is task1");
                System.out.println(Thread.currentThread().getName() + "is working");
            });

            pool.execute(() -> {
                System.out.println("this is task2");
                System.out.println(Thread.currentThread().getName() + "is working");
            });

            pool.execute(() -> {
                System.out.println("this is task3");
                System.out.println(Thread.currentThread().getName() + "is working");
            });



            executeTimes++;
        }
        //输出结果是只有一条线程在执行
    }

    @Test
    public void newCachedThreadPoolTest(){
        //可缓存的线程池
        ExecutorService pool = Executors.newCachedThreadPool();

        int executeTimes=0;
        while (executeTimes <= 30) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is working");
            });

            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is working");
            });

            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is working");
            });
            executeTimes++;
        }
        //输出甚至能到24的线程，是个无限膨胀的线程池，会复用之前的线程，但是如果线程都在工作那么会新起一条线程
    }

    @Test
    public void  newScheduledThreadPoolTest() throws Exception{
        //周期性的线程池
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);
        //延迟1秒执行，就执行一次
        //可以用于延迟队列
        pool.schedule(()->{
            System.out.println(Thread.currentThread().getName()+"->schedule task exec-1");
        },1,TimeUnit.SECONDS);
        //延迟1秒执行 每3秒执行一次
        //可以用于定时器
        pool.scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName()+"->schedule task exec");
        },1,3,TimeUnit.SECONDS);


        while (true){
            Thread.sleep(3);
        }

    }





    public static void main(String[] args) {

    }


}
