## HashMap总结

[TOC]



### 构造器

#### 无参构造器

```java
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}

```

- loadFactor hashmap的loadFactor的中文意思为负载因子
- DEFAULT_LOAD_FACTOR是0.75
- 所以hashmap无参构造器并不初始化对象数组，只初始化负载因子

#### 带参构造器

```java
public HashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}


public HashMap(int initialCapacity, float loadFactor) {
    //如果传入的数值小于0，那么抛出异常
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    //如果传入的数值大于MAXIMUM_CAPACITY（1<<30，1左移三十位，数值为1073741824），那么使用MAXIMUM_CAPACITY作为容量
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    //如果负载因子小于或等于0或者是负载因子不是一个数字，那么抛出异常
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    //设置负载因子loadFactor为默认的0.75
    this.loadFactor = loadFactor;
    //设置阈值为tableSizeFor函数计算的结果
    this.threshold = tableSizeFor(initialCapacity);
}

/*tableSizeFor用于计算离传入的容量最近的2的幂数
    比如说
    输入21 输出32
    输入33 输出64
    输入65 输出128
    输入1073741824 输出1073741824
    tableSizeFor生成的容量直接用于设置this.threshold阈值*/
static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

### put（）

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
/*hash方法是用于对键值对的键进行hash的函数，hashcode是一个native方法。这里的处理如下：
1、如果key为null，那么返回的hash值为0，这也是为什么hashmap可以存null - null这种键值对的原因，虽然只能存一个null键
2、如果key不为null,那么用key的hashcode与右移16位的key的hashcode做一个异或，为什么要这么做呢？h>>16后，其掩码的高位全部为0，再与异或就能保留下h的高位信息，这样子会让hash值更加分散更加有特征值*/
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

public native int hashCode();

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    //table是真正用于存储的Node对象数组，这里的意思是如果table为空或者长度为空，那么就resize扩容
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    //如果对应的数组索引卡槽为空
    //(n - 1) & hash是计算hash桶槽位（又或者叫数组索引）的方法，为什么要用n-1呢，因为n一般为2的倍数（这里是个面试问题，为什么hashmap每次扩容都是2的倍数，是因为2的倍数，计算数组索引 (n - 1) & hash 的时候能够更加分散）,n-1的话其掩码就是11111这种，和hash值与操作能够最大保留hashcode的散列状态，如果直接使用n 10000这种，0与1为0，0与0还是0，那么hash冲突会有很多，并不理想
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        //如果对应的数组卡槽不为空
        //首先判断卡槽里面的元素是否和传入的键完全相同
        Node<K,V> e; K k;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        //元素和传入的键不同，并且是一个树节点，那么增加树节点
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            //最后这种情况就是节点是一个链表节点
            for (int binCount = 0; ; ++binCount) {
                //遍历链表
                if ((e = p.next) == null) {
                    //这里的意思是如果遍历完整个链表还没有完全相同的节点那么就在最后的节点后面增加一个新的链表节点
                    p.next = newNode(hash, key, value, null);
                    //TREEIFY_THRESHOLD为8，从0开始遍历，意味着当链表节点数大于或等于8的时候，进行链表转树的操作
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
    //并发计算器
    ++modCount;
    //如果增加的容量大于阈值，那么执行扩容操作
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

### resize()/扩容

```java
 final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
     	//++size > threshold的情况
        if (oldCap > 0) {
            //大于最大容量的情况，阈值也设为最大容量，以后不会再进行扩容了
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            //DEFAULT_INITIAL_CAPACITY是1左移4位，就是16
            //如果两倍的旧容量小于最大容量 并且 旧容量大于并等于16，那么设置新阈值为两倍的旧阈值
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
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
```

