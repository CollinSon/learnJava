package com.github.collinson.learnsynchronized;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 许瑞锐
 * @date 2020/8/24 21:37
 * @description 双重校验锁练习
 */
public class SynDoubleCheckLock {

    public static Map map=new HashMap();



    /**
     * 使用volatile保证变量可见性 防止指令重排序
     */
    private static volatile SynDoubleCheckLock synDoubleCheckLock;


    public static SynDoubleCheckLock getInstance(){
        if (synDoubleCheckLock == null){
            synchronized (SynDoubleCheckLock.class){
                //假设进入这里的是a线程
                //这里再次检验是否为空的原因是 b线程此时在syn外等待，a线程生成新实例并释放锁，b拿到锁，如果不进行判空的话那么又new多一个实例，而且a线程返回了旧实例，因为被b线程刷新了
                if (synDoubleCheckLock == null){
                    synDoubleCheckLock = new SynDoubleCheckLock();
                }
            }
        }
        return synDoubleCheckLock;
    }
}
