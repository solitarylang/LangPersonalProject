package cn.lang.kafka.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：jianglang
 * @version : 1.0.0$
 * @date ：Created in 2020/5/30 7:14 PM
 * @description ：kafka streams DSL的word count,source=kafka,sink=kafka
 */
public class WordCount {
    public static void main(String[] args) {
        /* props */
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");//可作为consumer的group id
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");//kafka的地址，多个逗号分隔，目前只支持单集群
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());// 序列化和反序列化，在读取和写出流的时候、在读取和写出state的时候都会用到
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        /* topology */
        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream("streams-plaintext-input");//source processor,传入参数可定义key,value的序列化方式，以及时间提取器等

        source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" ")))//KString<String,String>
                .groupBy((key, value) -> value)// KGroupedStream<String,String>
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))//KTable<String,String>
                .toStream()//KStream<String,Long>
                .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));//sink processor,指定输出key,value的数据类型

        final Topology topology = builder.build();

        /* KafkaStreams实例 */
        final KafkaStreams streams = new KafkaStreams(topology, props);
        // CountDownLatch用await()阻塞当前线程，countDown()记录完成线程的数量
        // 当getCount()=0的时候继续执行await后续的代码
        final CountDownLatch latch = new CountDownLatch(1);

        System.out.println(topology.describe());// 打印流处理拓扑

        // 钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            // 执行
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
