import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 许瑞锐
 * @date 2020/9/9 15:13
 * @description {java类描述}
 */
public class DebugInvocationHandler implements InvocationHandler {


    /**
     * 代理类中的真实对象
     */
    private final Object target;

    public DebugInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before method " + method.getName());
        Object result = method.invoke(target,args);
        System.out.println("after method " + method.getName());
        return result;
    }

    public static void main(String[] args) {
        //生成需要代理的真实对象
        IMessageService iMessageService = new MessageService();
        //生成代理对象
        Object waitProxy = JDKProxyFactory.getProxy(iMessageService);
        ((IMessageService) waitProxy).sendMessage("this is a proxy message");
        createProxyClassFile();
    }

    private static void createProxyClassFile(){
        String name = "ProxyMessage";
        byte[] data = ProxyGenerator.generateProxyClass(name,new Class[]{IMessageService.class});
        FileOutputStream out =null;
        try {
            out = new FileOutputStream(name+".class");
            System.out.println((new File("hello")).getAbsolutePath());
            out.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=out) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
