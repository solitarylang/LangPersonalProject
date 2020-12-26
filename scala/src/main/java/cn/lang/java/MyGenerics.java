package cn.lang.java;

/**
 * @author ：jianglang
 * @date ：Created in 2019/5/23 9:25 PM
 * @description ：java基础有关泛型的知识点
 */
/*
 * 泛型的知识架构：（核心思想是将一个集合中的内容限制为一个特定的数据类型）
 * 1.为什么要有泛型；增强元素存储的安全性以及减少类型转换的繁琐阶段；
 * 				   泛型是编译时行为，类型推断按照从左往右的方向进行；
 * 				   泛型在运行的时候一定是具体的类型；
 * 2.如何使用泛型；
 * 				自定义泛型类：泛型默认时Object;静态方法不能使用类的泛型（静态方法加载的时候泛型还没有具体化）；
 * 						      泛型不同的引用加载的时候JVM只加载一个；不能在try-catch里面使用泛型定义；异常类不能是泛型；
 * 				自定义泛型方法：格式:[访问权限]<泛型>返回值类型 方法名（泛型标识 参数名称）{}，其中泛型标识和类的泛型无任何关系；
 * 				自定义泛型接口： 泛型接口和泛型类都是在名称后面添加<泛型>
 * 3.泛型在继承上的体现；如果B extends A ，那么A<T> = B<T>；编译不会出错（多态）；
 * 4.通配符的使用；<?>，<? extends Number>，<? super Number>，<? extends Comparable>
 * 				将任意元素加入到其中不是类型安全的；通配符修饰的类只能读取不能写入（除了null）；
 */
public class MyGenerics {
    interface Generic<T> {
        void compare(T t);
    }

    class MyGeneric<T> implements Generic<T> {
        private T t;

        MyGeneric(T t) {
            this.t = t;
        }

        public <K> T transform(K k) {
            if (k == null) {
                return this.t;
            } else {
                return null;
            }
        }

        public void compare(T t) {
            System.out.println("this.t.equals(t)");
        }
    }

    public static void main(String[] args) {

    }
}
