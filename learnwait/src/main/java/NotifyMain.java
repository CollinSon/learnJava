/**
 * @author 许瑞锐
 * @date 2020/9/8 12:01
 * @description {java类描述}
 */
public class NotifyMain {
    private static Object resource = new Object();
    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            synchronized (NotifyMain.class){
                System.out.println("t1 work");
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                resource.notifyAll();
            }
        });
        Thread t2 = new Thread(()->{

            while (true) {
                synchronized (resource) {
                    System.out.println("t2 work");
                    try {
                        resource.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("t2 continue work");

                }
            }
        });
        t1.start();
        t2.start();
    }

}
