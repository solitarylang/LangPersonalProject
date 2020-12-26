package cn.lang.java;

/**
 * @author ：jianglang
 * @date ：Created in 2019/5/23 9:22 PM
 * @description ：java基础有关集合相关的知识点
 */
/*
 *  集合重点：继承树、数据存储特点、元素存储流程
 *  1.Java的集合框架：集合就是数据动态存储的容器
 *  2.Collection接口的API：添加，删除，转化成数组等
 *  3.iterator方法（collection特有），其对象成为迭代器：迭代器中hasnext()和next()方法的配合使用；
foreach循环遍历集合元素；
 *  4.Collection子接口：List接口，有序可重复（典型ArrayList（底层是数组，初始化长度为10，扩容按照1.5）（ArrayList、LinkedList、Vector）增删频繁的时候仍然是ArrayList快，因为实际增删的时候是查找+增删的复合操作；
 *  5.Collection子接口：Set接口，无序不可重复（典型HashSet）--（HashSet、TreeSet、HashTable）
 *  6.Map接口，映射关系的键值对（典型HashMap）（HashMap、TreeMap、HashTable）
 *  		判断两个key相等的标准是两个key通过equals方法返回true，hashCode值也相等；（hashcode方法是有规定的重写原则的）
 *  		判断两个value相等的标准是两个value通过equals方法返回true；
 *  7.Collection工具类(均为static方法)：shuffle(List); int frequency(Collection,object);copy(List dest,List src);
 *  		copy方法的前提条件是dest集合里面已经有元素；
 *  注意：
 *  		①集合存储数据较数组存储数据的优点；（存储数据类型更丰富，API更多，长度可变，底层数据结构更加丰富）
 *  		②List集合内的remove（int index）方法，如何根据内容删除相应元素；（remove(new Integer(1));）
 *  重点》》》 	③HashMap/HashSet的存储方式（HashMap将Value固定成常量，就是HashSet）:存入元素时，调用该对象所在类的hashcode方法计算其hashcode的值，然后根据hashcode的值确定该对象的存储位置； 当hashcode的值相同的时候，就会调用该对象的equals方法进行比较，返回值true表示相同，后者覆盖前者，返回false，将会以链表的形式存储； 当链表存储的数量超过8以后就改变成红黑树的方式进行存储；
 *  		④LinkedHashSet底层存储顺序和HashSet一样，同时使用链表表示元素的插入顺序；
 *  		⑤TreeSet的两种排序方法：
 *  			自然排序：实现comparable接口->重写compareTo方法->设计排序的逻辑方法->添加元素
 *  			定制排序：创建comparator接口的匿名实现类->重写compare方法->设计排序的逻辑方法->添加元素
 *  		⑥HashMap的存储结构是数组+链表+红黑树；
 *  		⑦Properties实现类；读取文件的步骤创建properties的对象->创建File的对象->创建文件输入流的对象->加载流->输出->关闭流
 *  		⑧从Map里面遍历key(单独取出来就是set的集合)、value(单独取出来就是collection的集合)、entry（node）的方法：Set keySet(),Collection values(),Set entrySet();
 *  		⑨SynchronizedXxx（）方法可使指定集合包装成线程同步的集合，从而解决了多线程并发访问集合时线程安全的问题；
 *  		⑩Enumeration是iterator迭代器的古老版本；
 *  问题：
 *  		①为什么复写hashcode方法会有31这个数字（31是素数，减少冲突，（2的5次方 - 1）运算起来比较方便）
 *  		②为什么HashMap底层存储扩容是按照2倍进行扩容的（底层存储的时候有hashcode和存储容量的与运算，保证hashcode能取到容量内的所有值）；
 *  		③Collection，Set，Map存储自定义类对象的时候，需要自定义类分别重写（euqals）、（hashcode和equals）、（hashcode和equals）方法
 *  		④简述HashTable和HashMap的区别（线程是否安全，是否能够存放null）；
 */
public class MyCollection {
}
/* 	1.研究main方法；
 *		main方法每个部分关键字的意义和作用；
 *		后面的形参列表不能换成可变形参；
 *	2.JVM的实现原理；
 *	3."=="和"equals"的区别；==如果是基本数据类型，比较的就是值的大小，如果是引用数据类型，就是数据的引用地址；
 *		自定义类中没有重写equals方法的时候，默认就是==的比较方法，但是通常我们需要比较两个应用数据的内容是否相同，
 *		所以我们通常需要重写equals方法进行内容的自定义比较，而且使用中常常用的都是重写后的equals方法进行比较；
 *	4.如果跳出多重循环
 *		1）使用带标签的break语句；2）使用布尔类型的变量进行监测（boolean isFlag = true）；
 *	5.使用final修饰的时候表示的是引用不能改变不是说引用的对象不能改变；
 *	6.两个字符串进行比较的时候，尽量将实际的字符串放在前面，引用名放在后面，因为可能会报空指针的异常；
 *	7.修饰符之间的混用是否可行的面试题？
 *	8.什么是API，API的具体作用和功能是什么？
 *	9.因为计算机在计算浮点型数据运算的时候会产生误差，所有在判断语句总尽量减少浮点型数据类型的计算；采用BigDecimal
 *	10.java也存在指针，只是隐藏了，不由开发人员直接操作转而由java虚拟机进行操作；
 *	11.形参与实参，形参全名为形式参数，为方法创建的时候使用的参数用于接收调用该方法传入的实际值；
 *				实参全名为实际参数，为方法调用的时候实际传入该方法的实际值；
 *		总结就是形参就是为了用来接收实参的；
 *	12.引用字符串进行拼接的时候会默认生成一个StringBuilder，然后进行扩容、新建、赋值的操作；
 *	13.继承关系中，如果父类存在空参的构造方法，那么子类默认继承父类的空参构造方法，
 *		如果父类显示定义了带参的构造方法，那么子类就必须显示定义构造方法；不一定需要重写；
 *		同时构造方法如果显示定义了系统就不再默认提供空参的构造方法了；
 *	14.有抽象方法必须是抽象类，但是抽象类可以没有抽象方法；
 *	15.抽象类和接口之间的区别;单继承和多继承，是否可以有非抽象方法，是否有构造方法；都是不能直接创建实例的；
 */