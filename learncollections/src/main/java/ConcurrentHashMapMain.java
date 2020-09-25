import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 许瑞锐
 * @date 2020/9/23 15:43
 * @description {java类描述}
 */
public class ConcurrentHashMapMain {

    /*
    * 首先看构造器 无参构造器 什么都没有
    * public ConcurrentHashMap() {
       }
    * */

    /*
    带参构造器
    public ConcurrentHashMap(int initialCapacity) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException();
    int cap = ((initialCapacity >= (MAXIMUM_CAPACITY >>> 1)) ?
               MAXIMUM_CAPACITY :
               tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1));
    this.sizeCtl = cap;
    }
    * */

    public static void main(String[] args) {
        ConcurrentHashMap<String,Object> cMap=new ConcurrentHashMap<>(10);
        cMap.put("1","1");
        System.out.println(1 >>> 3);
    }
}
