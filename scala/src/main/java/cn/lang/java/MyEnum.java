package cn.lang.java;

import java.util.Arrays;

/**
 * @author ：jianglang
 * @date ：Created in 2019/5/23 9:21 PM
 * @description ：java基础有关枚举相关的知识点
 */

/*
 * 枚举:
 *  1.枚举也是一个类，继承java.lang.Enum(自带方法/属性可查看此抽象类)
 *      由于单继承的原因，枚举只能在实现其他接口而无法再继承其他类
 *  2.自定义枚举类
 *      私有化属性和构造器，并新建有限个枚举类的单例对象
 *      使用`Country.China`的方式调用
 *  3.使用细节
 *      在枚举类的第一行声明枚举类对象(建议注释说明)
 *      枚举类的所有实例都必须在枚举类中显示列出，以逗号分隔，以分号结尾
 *      可使用EnumSet或者EnumMap来存储枚举
 *  4.一个需求弄懂枚举
 *      定义一个枚举类Animal，其中有猴子(猴子会爬树)，鱼(鱼会游泳)
 *  5.枚举和常量的区别
 *      常量大部分是基础类型，没有限制，在枚举时值是基础类型的时候可以代替枚举如：`Constants.AMERICA`
 *      枚举是对象，有属性方法，可穷举(有限制不用在运行时检测)
 */
public class MyEnum {
    /* 常量 */
    public static class Constants {
        String CHINA = "中国";
        String JAPAN = "日本";
        String AMERICA = "美国";
    }

    /* 枚举也可以有属性和方法 */
    enum Country {
        // 调用构造器构造枚举项的单例对象
        CHINA("中国"), JAPAN("日本"), AMERICA("美国");
        private String chineseName;

        private Country(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getChineseName() {
            return this.chineseName;
        }

        // 也可以重写方法
        @Override
        public String toString() {
            return "中文名是： " + this.chineseName;
        }
    }

    /* 需求实现 */
    interface Creature {
        void move();
    }

    enum Animal implements Creature {
        MONKEY {
            public void move() {
                System.out.println("我是猴子我会爬树");
            }
        },
        FISH {
            public void move() {
                System.out.println("我是鱼我会游泳");
            }
        };
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(Country.values()));
        System.out.println(Country.AMERICA);
        System.out.println(Country.AMERICA.getChineseName());
        // 测试是不是单例
        System.out.println(System.identityHashCode(Country.AMERICA));
        System.out.println(System.identityHashCode(Country.AMERICA));

        // 需求测试
        Animal.MONKEY.move();
        Animal.FISH.move();
    }
    /* 打印结果
    [中文名是： 中国, 中文名是： 日本, 中文名是： 美国]
    中文名是： 美国
    美国
    225534817
    225534817
    我是猴子我会爬树
    我是鱼我会游泳
     */
}

/**
 * 以下是hadoop中使用的枚举例子,基本都是不带属性或者方法的
 */
// FileStatus.class
/* Flags for entity attributes.*/
enum AttrFlags {
    /* ACL information available for this entity. */
    HAS_ACL,
    /* Entity is encrypted. */
    HAS_CRYPT,
    /* Entity is stored erasure-coded. */
    HAS_EC,
    /* Snapshot capability enabled. */
    SNAPSHOT_ENABLED,
}

// TaskStatus.class
//enumeration for reporting current phase of a task.
enum Phase {STARTING, MAP, SHUFFLE, SORT, REDUCE, CLEANUP}

// what state is the task in?
enum State {
    RUNNING, SUCCEEDED, FAILED, UNASSIGNED, KILLED,
    COMMIT_PENDING, FAILED_UNCLEAN, KILLED_UNCLEAN, PREEMPTED
}
