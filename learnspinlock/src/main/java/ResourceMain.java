/**
 * @author 许瑞锐
 * @date 2020/9/4 10:42
 * @description {java类描述}
 */
public class ResourceMain {

    private static int innerInt=1000;

    public static void main(String[] args) throws Exception {



        Thread t1 = new Thread(()->{
            for (;;){
                System.out.println(Thread.currentThread().getName()+":"+innerInt);
            }


        });

        Thread t2 = new Thread(()->{
            for (;;){
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                innerInt++;
            }
        });
        t1.start();
        t2.start();
    }
}
