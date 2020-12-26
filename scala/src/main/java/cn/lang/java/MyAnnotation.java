package cn.lang.java;

import java.lang.annotation.Annotation;

/**
 * @author ：jianglang
 * @date ：Created in 2019/5/23 9:03 PM
 * @description ：java基础有关注解的知识点
 */

/*	注解：代码里的特殊标记
 *	1.JDK内置的三个基本注解：
 *	    @override ：限定重写父类的方法
 *	    @deprecated ：表示某个程序元素已经过时
 *	    @supressWarnings : 抑制编译器警告
 *	2.自定义注解类型：Annotation的成员变量在Annotation定义中以无参方法的形式声明；
 *	    没有成员变量的注解称为标记，有成员变量的注解称为元数据注解；
 *	    可以提供默认值；如：String value() default ("nice");
 *	3.元注解（meta-annotation）用于修饰其他注解,也就是注解的注解
 *	    @retention：指定该注解可以保留多长时间，
 *                  使用该注解的时候必须为其成员变量retentionPolicy赋值（source class runtime）
 *	    @target：指定该注解能用来修饰那些类元素，包含一个value的成员变量，
 *	    @documented:指定该注解修饰的类能被javadoc工具提取成为文档（前提是retention设置为runtime）
 *	    @inherited:指定该注解具有继承性（应用较少）
 *	4.利用反射获取注解的信息（retention设置为runtime，也就是说注解的信息必须进入到class文件里才能被反射提取）
 *	5.其他注解：
 *	    @FunctionalInterface：用以表示该接口是函数式接口（接口里面只有一个抽象方法）
 *                            --可以添加该注解来判断某接口是否为函数是接口；
 */
public class MyAnnotation {

}


class Flag implements Annotation {
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}

class AnnotationFlag implements Annotation {

    public Class<? extends Annotation> annotationType() {
        return null;
    }
}