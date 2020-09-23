import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.time.Instant;
import java.util.concurrent.Executor;

/**
 * @author 许瑞锐
 * @date 2020/9/10 14:17
 * @description {java类描述}
 */
public class EventBusMain {

    static class CustomEvent {
        private int age;
        public CustomEvent(int age) {
            this.age = age;
        }
        public int getAge() {
            return this.age;
        }
    }

    static class EventListener1 {
        @Subscribe
        public void test1(CustomEvent event) {
            System.out.println(Instant.now() + "监听者1-->订阅者1,收到事件：" + event.getAge() + "，线程号为：" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Subscribe
        public void test2(CustomEvent event) {
            System.out.println(Instant.now() + "监听者1-->订阅者2,收到事件：" + event.getAge() + "，线程号为：" + Thread.currentThread().getName());

        }
    }


    public static class EventListener2 {
        @Subscribe
        public void test(CustomEvent event){
            System.out.println(Instant.now() +",监听者2,收到事件："+event.getAge()+"，线程号为："+Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static EventBus eventBus;

    private static AsyncEventBus asyncEventBus;

    private static Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    };

    //双重锁单例模式
    private static AsyncEventBus getAsynEventBus(){
        if(asyncEventBus==null){
            synchronized (AsyncEventBus.class){
                if(asyncEventBus==null){
                    asyncEventBus = new AsyncEventBus(executor);
                }
            }
        }
        return asyncEventBus;
    }

    private static EventBus getEventBus(){
        if(eventBus==null){
            synchronized (EventBus.class){
                if(eventBus==null){
                    eventBus = new EventBus();
                }
            }
        }
        return eventBus;
    }

    public static void post(Object event){
        getEventBus().post(event);
    }
    //异步方式发送事件
    public static void asyncPost(Object event){
        getAsynEventBus().post(event);
    }
    public static void register(Object object){
        getEventBus().register(object);
    }
    public static void asyncRegister(Object object){
        getAsynEventBus().register(object);
    }


    public static void main(String[] args) {
        EventListener1 listener1 = new EventListener1();
        EventListener2 listener2 = new EventListener2();
        CustomEvent customEvent = new CustomEvent(23);
        register(listener1);
        register(listener2);
        asyncRegister(listener1);
        asyncRegister(listener2);
//        post(customEvent);
        asyncPost(new CustomEvent(24));
//        EventBusUtil.asyncPost(customEvent);
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Instant.now() +",主线程执行完毕："+Thread.currentThread().getName());

    }


}
