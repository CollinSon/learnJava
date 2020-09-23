import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 许瑞锐
 * @date 2020/9/21 22:41
 * @description {java类描述}
 */
public class CountdownlatchMain {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        final int[] tick = {0};

        for (int i=0;i<10;i++) {

            executorService.execute(() -> {
                tick[0]++;
                System.out.println("执行任务" + tick[0]);
                countDownLatch.countDown();
                try {
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }

            });

        }

        countDownLatch.await();
        executorService.shutdownNow();
        System.out.println("finish");
    }
}
