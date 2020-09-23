import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author 许瑞锐
 * @date 2020/9/8 17:07
 * @description {java类描述}
 */
public class BlockQueueMain {

    private static ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(10,true);

    public static void main(String[] args) {
        for (int i =0 ;i<11;i++){
            blockingQueue.add(i);
        }
    }

}
