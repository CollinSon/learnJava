import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 许瑞锐
 * @date 2020/8/25 17:57
 * @description {java类描述}
 */
public class FutureMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<String> futureResult=new CaculateResult().process();
        while(!futureResult.isDone()) {
            System.out.println("Calculating...");
            try {
                Thread.sleep(300);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(futureResult.get());
    }

    static class CaculateResult{

        private ExecutorService executor = Executors.newSingleThreadExecutor();


        public Future<String> process(){
            try {
                Thread.sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }
            return executor.submit(()->{
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return "futureResult";
            });
        }
    }
}
