import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author 许瑞锐
 * @date 2020/8/23 11:12
 * @description callable练习
 */
public class CallableTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask=new FutureTask<String>(()->{
            return "this is a call";
        });
        Thread thread=new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());
    }
}
