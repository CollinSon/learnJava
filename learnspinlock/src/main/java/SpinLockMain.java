import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 许瑞锐
 * @date 2020/9/4 9:53
 * @description {java类描述}
 */
public class SpinLockMain {


    public static AtomicInteger atomicInteger = new AtomicInteger(5);

    public static  Resource staticResource= new Resource(1);



    static {
        atomicInteger.set(4);
        atomicInteger.getAndSet(5);
    }
    //所谓的自旋锁就是不断的自旋，每次都去获取内存值并且比对要修改的内存值，如果不相同的话就重新获取内存值再比对
    //假如获取内存值比对并且更新值

    public static void main(String[] args)  {
        Thread t1 = new Thread(()->{
            Resource localResource = staticResource ;
            //循环打印volatileInt的值
            for (;;){
                System.out.println(String.format("【%s】现在volatileInt的值是【%s】",Thread.currentThread().getName(),localResource));
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(()->{
            try {
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
           //准备修改volatileInt
            System.out.println(String.format("【%s】准备修改volatileInt",Thread.currentThread().getName(),
                    staticResource));
            staticResource.setId(50);
            System.out.println("修改volatileInt成功");
        });

        t1.start();
        t2.start();
    }

}
