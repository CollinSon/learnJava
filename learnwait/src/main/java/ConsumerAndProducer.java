import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author 许瑞锐
 * @date 2020/9/8 14:58
 * @description 消费者和生产者
 */
public class ConsumerAndProducer {


    private static final int MAX_IN_STOCK_NUM = 10;

    private static LinkedList<Object> resourceList = new LinkedList<>();


    public static void main(String[] args) {
        Thread produceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    System.out.println("生产者试图锁");
                    synchronized (resourceList){
                        if (resourceList.size() > MAX_IN_STOCK_NUM){
                            System.out.println("库存将满，停止生产任务");
                            try {
                                System.out.println("生产者wait");
                                resourceList.wait();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else {
                            resourceList.add(new Object());
                            System.out.println("生产完毕，现在库存数为"+resourceList.size());
                            resourceList.notifyAll();
                        }
                    }
                }
            }
        });
        produceThread.start();

        Thread consumeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("消费者试图锁");
                    synchronized (resourceList) {
                        if (resourceList.size() <= 0) {
                            System.out.println("库存将空，停止消费任务");
                            try {
                                System.out.println("消费者wait");
                                resourceList.wait();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            resourceList.removeLast();
                            System.out.println("消费完毕，现在库存数为"+resourceList.size());
                            resourceList.notifyAll();
                        }
                    }
                }
            }
        });
        consumeThread.start();
    }

}
