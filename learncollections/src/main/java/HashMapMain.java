import java.util.HashMap;
import java.util.Map;

/**
 * @author 许瑞锐
 * @date 2020/9/23 9:27
 * @description {java类描述}
 */
public class HashMapMain {

    static final int MAXIMUM_CAPACITY = 1 << 30;
    //首先看构造器
    /*
    无参构造器是这样的
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
    只设置了一个负载因子，所以说默认用的话是不会去初始化容量的
    * */

    //再看看带容量的构造器
    /*
     public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);

    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }
    //tableSizeFor用于计算离传入的容量最近的2的幂数
    比如说
    输入21 输出32
    输入33 输出64
    输入65 输出128
    tableSizeFor生成的容量直接用于设置this.threshold阈值
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    所以构造器是不会去初始化hashmap的
    * */

    //接下来看put方法，非常重要
    /*
     public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
    hash函数又称为扰动函数
    如果key为null,那么返回的hashcode为0.如果不为null,那么就用key的hashcode和key的hashcode的掩码做一个低位掩码的异或
    为什么右移16位（h>>16）呢  因为key.hashcode >>> 16 右移16位那么就让高位16位变0，留下了低位掩码
    key.hashcode 异或 key.hashcode >> 16 会让造成一种低位和高位掩码混合的一种效果，能让hash值更分散
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    //hashcode是一个native方法，由c++编写
    public native int hashCode();


    putVal方法比较长，要慢慢的分析
    table是hashmap的一个属性 是这样的    transient Node<K,V>[] table; Node类属于hashmqp的一个节点,key-value键值对，所以说table其实也不过是一个数组
    1、如果table为空或者table的长度为0，那么进入resize()函数 也就是说还未初始化 那么进行初始化扩容

  final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    如果hashmaph还未初始化过，进行扩容
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    这段  (n - 1) & hash 比较有意思，是用于计算hash桶该键值对的位置的，和hash与的话就肯定不会超过n-1的数组长度，n-1的掩码是 11111 这种，hash的扰动会更小
    判断该键位是不是空，如果已有键值对那么走进else
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {

        Node<K,V> e; K k;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
        如果键值对完全相等，那么直接替换
            e = p;
        else if (p instanceof TreeNode)
        如果键值对是一个树节点，那么执行树节点的添加
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
        这步说明肯定是链表 链表大于等于8个元素进行转换树
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}

    * */

    /*
    resize方法也是非常重要，单独拿出来看
    *   final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        如果旧的hash桶不为空，那么进入这个分支，这是属于已有hash桶的情况
        if (oldCap > 0) {
            如果旧的hash桶已经超过了最大容量，那么不再扩容，因为扩容不动
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }

            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        //尚未被初始化的情况，也就是带数值的hashmap构造器初始化
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        最后一种情况的意思就是无参构造器hashmap，设置阈值为0.75 * 16
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
    * */

    public static void main(String[] args) {

        Map<String,Object> map = new HashMap<>(10);

        for (int i=0;i<1000;i++){
            map.put(String.valueOf(i),i);
        }

        map.put("2000","5000");
        map.get("2000");

        System.out.println(tableSizeFor(1<<30));

    }


    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
