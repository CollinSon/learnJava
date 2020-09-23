import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 许瑞锐
 * @date 2020/8/26 14:27
 * @description atomicInteger练习
 */
public class AtomicIntegerMain {

    @Test
    public void atomicIntegerTest(){
        int temValue=0;
        AtomicInteger i=new AtomicInteger(temValue);
        //返回旧值并且将新值设为传入的值
        temValue = i.getAndSet(3);
        //temvalue:0;  i:3
        System.out.println("temvalue:" + temValue + ";  i:" + i);
        //内部是通过cas(compire and swap 来实现的)
        //cas对比内存地址是否发生了改变
        //如果cas结果为false那么会一直循环再次获取内存地址直到值修改完毕

    }
}
