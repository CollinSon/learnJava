import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 许瑞锐
 * @date 2020/9/22 8:47
 * @description {java类描述}
 */
public class ArrayListMain {

    public static void main(String[] args) {

       ArrayList list = new ArrayList<>();

       for (int i=0;i<1000;i++){
           list.add(i);
       }


       list.add(1002);

       list.addAll(new ArrayList());

       list.remove(1002);

       list.get(1002);


    }
    //所有集合类比较重要的都是构造器 add方法和remove方法
    //arrayList的构造器如下

   /* public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }*/

 /*   public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }*/

//    所以实际上arraylist的底层也是object[] object对象的数组而已，传入带数值的构造器能够初始化一个对应数值的对象数组
//    假如不传入数值那么默认初始化一个空数组{}

//    1、add方法如下
   /* public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }*/

//    1.1 首先是 ensureCapacityInternal(size+1)
     /*   private void ensureCapacityInternal(int minCapacity) {
            ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
        }*/

//      1.1.1  计算容量函数 calculateCapacity(elementData, minCapacity)
//    这个函数什么意思呢 就是判断传入的size+1，
//    a.如果当前对象数组为空，那么比较传入的size+1和默认容量(10)哪个比较大，返回较大的，一般来说对象数组为空的时候，size也是0，我觉得这里一直会返回1
//    b.如果当前对象数组为空，那么直接返回size+1，按我的理解也就是当size>=1时，这个函数返回的就是size+1
//    这个函数比较简单
/*        private static int calculateCapacity(Object[] elementData, int minCapacity) {
            if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
                return Math.max(DEFAULT_CAPACITY, minCapacity);
            }
            return minCapacity;
        }*/
//    这个函数是将计算容量的容量传入 minCapcacity，通过计算的容量减去当前对象数组的长度来决定要不要扩容，所谓modCount是用于快速失败的一个计数器  接下来看grow方法
//       1.1.2 ensureExplicitCapacity(int minCapacity)
   /*     private void ensureExplicitCapacity(int minCapacity) {
            modCount++;

            // overflow-conscious code
            if (minCapacity - elementData.length > 0)
                grow(minCapacity);
        }*/

/*
    a.如果需要扩容，计算新的容量为旧容量的1.5倍
    b.如果新容量小于计算出需要扩充的容量，那么新容量直接指定为计算出需要扩充的容量
    c.如果新容量甚至大于最大数组容量，那么走进hugeCapacity 巨大扩容函数
* */

 /*   private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }*/

    //hugeCapacity巨量扩容是这样的 如果最小容量小于0直接抛出内存溢出 如果最小容量大于最大数组容量那么使用最大int容量，否则使用最大数组容量
 /*   private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }*/

    //可能的面试题
    //1、arraylist扩容的话是扩容多少倍 1.5倍
    //2、arrayList什么时候扩容 arrayList里有两个属性，elementData和size elementData保存的真实的对象数组 而size只保存实际存在的元素，如果size+1>elementData
    // .length那么就应该金到grow方法进行扩容，如果size+1 > 1.5倍的elementData.length 那么就直接使用size+1作为新容量扩容
    //remove方法也是通过数组拷贝来实现的，拷贝到新的数组里
    //get方法使用随机查找实现
    //当arraylist已经add了1000个元素后，再add得话是不会扩容得,因为elementData.length == 1234 大于 1000 无需扩容
}
