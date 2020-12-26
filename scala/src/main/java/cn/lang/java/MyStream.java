package cn.lang.java;

/**
 * @author ：jianglang
 * @date ：Created in 2019/5/23 9:24 PM
 * @description ：java基础有关io流的知识点
 */
/*
 * File:位置java.io.File
 * 	1）IO流体系：字节输入流（InputStream），字节输出流（OutputStream）；按照字节的单位进行读写，主要用于文件的复制；
 * 			     字符输入流（Reader），字符输出流（Writer）；按照字符（两个字节）的单位进行读写，主要用于纯文档的读写操作；
 * 			字符流用于复制文件的时候，底层会进行编译，输出文件会变大，且不能打开；一般只用于编码集的转换；
 * 		key:节点流包括四个文件流（FileInputStream,FileOutputStream,FileReader,FileWriter）
 *  2）缓冲流：BufferedInputStream，BufferedOutputStream，BufferedReader，BufferedWriter
 *  	2.1）相当于在数据传输的时候创建了在内存区域创建了一块缓存区域来缓存传输的数据，
 *  		在数据到达一定数量的时候在按照批次的形式进行传送，大大加强了数据的传输效率；
 *  3）转换流：InputStreamReader,OutputStreamWriter
 *  	3.1)用来字符型和字节型之间的转换，加强对他们代码的融合性；
 *  	3.2)用来将各种数据的存储方式之间的改变，比如ASCII、GBK、UTF-8等之间的转换；
 *  4)标准流：System.in和System.out
 *  	System.in：从控制台读取数据（实质是一个InputStream字节流的输入）
 *  	System.out：向控制台写入数据
 *  5)打印流：PrintStream
 *  	可以通过System.setOut(PrintStream)改变标准输出流的导向位置，将原本默认打印至控制台的数据打印到目标文件里面去；
 *  6)数据流：DataInputStream和DataOutputStream
 *  	数据流写入数据的格式和读取数据的格式必须一致；
 *  7)对象流:序列化（ObjectOutputStream）和反序列化（ObjectInputStream）
 *  	序列化对象：序列化的对象的类必须实现serilizable接口；
 *  			   序列化对象的类的属性除了基本数据之外，也必须全部实现serilizable接口；
 *  			   需要显示声明一个serialVersionUID(因为系统默认的这个ID会因为类的改变而改变)；
 *  注意：
 *  ①创建流和关闭流的时候都可能产生异常，必须进行处理，而且关闭流的时候必须单独处理，按照从大到小的顺序进行关闭；
 *  ②除了文件流以外的流都是处理流，都需要将字节流作为形参传入进行处理；同时将文件做为形参传给节点流进行读取或者写入；
 *  ③心中一定要有一个以程序为中心的，读取来源文件，写入目标文件这样一个流程线；
 */
public class MyStream {
}
