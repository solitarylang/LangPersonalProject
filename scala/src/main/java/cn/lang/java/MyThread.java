package cn.lang.java;

/**
 * @author ：jianglang
 * @date ：Created in 2019/5/23 9:23 PM
 * @description ：java基础有关线程的知识点
 */
/*
线程的核心知识点：
1.创建线程的四种方法；
2.线程同步的方法；（特别是锁的机制）
3.线程之间的通信方法；

--------
一、创建线程
1.继承Thread类
 * ①创建一个类继承Thread类；
 * ②重写run方法；
 * ③将需要在分线程里面执行的方法体写在run方法内；
 * ④在主方法内创建该类的对象；
 * ⑤用该对象调用start方法，启动分线程；
注意：一个线程的对象只能用一次start方法，如果需要创建多个线程通知工作，需要重新创建线程的对象；
示例1：
```
public class ThreadTest {
    public static void main(String[] args) {
        new MyThread2().start();
    }
}
class MyThread2 extends Thread{
    @Override
    public void run() {
        // 分线程需要执行的逻辑代码
    }
}
//--------------------or----------------------
new Thread(){
    @Override
    public void run() {
// 分线程需要执行的逻辑代码
    }
}.start();
```
2.实现Runnable接口
 * ①创建一个类实现Runnable接口；
 * ②重写run方法；
 * ③将需要在分线程里面执行的方法体写在run方法内；
 * ④在主方法内创建该类的对象；
 * ⑤将该类的对象作为实参传递给创建的Thread；
 * ⑥用创建的Thread引用调用start方法，启动分线程；
示例2：
```
public class ThreadTest {
    public static void main(String[] args) {
        new Thread(new MyThread2()).start();
    }
}
class MyThread2 implements Runnable{
    public void run() {
        // 分线程需要实现的逻辑代码
    }
}
//--------------------or----------------------
new Thread(new Runnable() {
    @Override
    public void run() {
// 分线程需要执行的逻辑代码
    }
}.start();
```
3.实现callable接口
 * ①创建一个类实现callable接口；
 * ②重写call方法，将需要在分线程里面实现的内容写在call方法内；
 * ③创建callable接口实现类的的对象；
 * ④创建FutureTask的对象，并将实现类对象作为实参传入；
 * ⑤创建Thread对象，并将futureTask的对象作为实参传入；
 * ⑥通过Thread的对象调用start方法，启动分线程；
注意：该创建方法可以存在返回值；如果需要创建多线程，需要创建多个FutureTask对象；
示例3：
```
public class ThreadTest {
    public static void main(String[] args) {
        new Thread(new FutureTask(new MyThread2())).start();
    }
}
class MyThread2 implements Callable {
    public Object call() throws Exception {
        // 分线程需要实现的逻辑代码
        return null;
    }
}
```
4.创建线程池
 * ①通过（Executors.线程名）创建线程池；
 * ②线程池.execute(new Runnable(){重写run方法});
 * ③创建匿名实现类，开启分线程；
 * ④需要手动关闭线程池；
线程池提供了一个线程队列,队列中保存着所有等待状态的线程;
示例4：
```
public class TestThreadPool{
    public static void main(String[] args){
        // 1. 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        ThreadPoolDemo tpd = new ThreadPoolDemo();
        // 2. 为线程池中线程分配任务
        //    submit(Callable<T> task)
        //    submit(Runnable task)
        for(int i=0; i<10; i++){
            pool.submit(tpd);
        }
        // 3. 关闭线程池
        pool.shutdown();
    }
}
class ThreadPoolDemo implements Runnable{
    private int i=0;
    public void run(){
        while(i <= 100){
            System.out.println(Thread.currentThread().getName()+" : "+ i++)
        }
    }
}
```
二、线程同步
1.为什么需要线程同步？
 当多线程对同一数据源进行修改的时候可能会出现线程安全的问题；
2.线程同步有哪些方法？
3.同步锁的机制？
 同步锁，static的方法默认的锁是Class的对象，非static的方法默认的锁是实例对象；记忆小技巧：联想这两个方法的调用形式，static不需要实例对象就可以调用，非static的方法需要new实例对象才可以调用；
4.继承方式和实现方式的区别是什么?
 继承方式：监视器不可以是this,单继承，资源共享必须用static修饰，默认锁是运行时类的对象；实现方式：监视器可以是this,多实现，资源共享不需要static修饰，默认锁是this；
详细的锁请见JUC---；
三、线程通信
1.面试题：wait和sleep的区别？
 工作机制不同-wait阻塞，会释放手中的锁，需要其他进行notify将其唤醒，sleep睡眠，不会释放锁，睡醒继续工作；所属类不同，sleep是Thread类，wait是object类；
2.线程通信的主要方法？
wait(),sleep(),notify(),nofityAll(),yield(),interrupt()；
3.线程的生命周期？
 生命周期：创建、就绪、运行、阻塞、结束；
四、其他知识点
1.死锁的概念？
 一个简单的例子就是两个线程都需要两把同样的锁，但是双方各手持一把；
2.单例模式的懒汉式线程安全问题的解决方法;
 */
public class MyThread {
}
