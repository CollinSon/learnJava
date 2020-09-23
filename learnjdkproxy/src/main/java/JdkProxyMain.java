import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 许瑞锐
 * @date 2020/9/9 16:25
 * @description {java类描述}
 */
public class JdkProxyMain {

    interface MessageInteface{

        void sendSmsMessage(String m);
    }

    static class SmsMessage implements MessageInteface{

        @Override
        public void sendSmsMessage(String message){
            System.out.println("send a message : "+message);
        }
    }

    static class MyInvokeHandler implements InvocationHandler{

        private final Object beingProxyObject;

        public MyInvokeHandler(Object beingProxyObject) {
            this.beingProxyObject = beingProxyObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before method invoke");
            Object result=method.invoke(beingProxyObject,args);
            System.out.println("after method invoke");
            return result;
        }

        public static void main(String[] args) {
            MessageInteface proxy=(MessageInteface)(Proxy.newProxyInstance(SmsMessage.class.getClassLoader(),
                    SmsMessage.class.getInterfaces(),
                    new MyInvokeHandler(new SmsMessage())));
            proxy.sendSmsMessage("proxy message");
        }
    }
}
