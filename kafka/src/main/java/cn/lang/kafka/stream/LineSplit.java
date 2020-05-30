package cn.lang.kafka.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/30 6:55 PM
 * @description ：value的拆分回写，将value按照特定规则进行拆分，然后将结果回写到另外的kafka topic中
 * @version: 1.0.0$
 */
public class LineSplit {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-linesplit");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        // 读取数据源
        KStream<String, String> source = builder.stream("streams-plaintext-input");
        // 按照规则对数据进行转换,将value也就是我们存在kafka中的recod的body拆分
        KStream<String, String> words = source.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            public Iterable<String> apply(String s) {
                return Arrays.asList(s.toLowerCase().split(" "));
            }
        });
        // 将结果写出到另外的topic中
        words.to("streams-linesplit-output",
                Produced.with(Serdes.String(), Serdes.String()));

        final Topology topology = builder.build();

        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);


        // 测试结果
        // console生产者
        // langjiang@langs-MacBook-Pro kafka_2.11-2.1.0 % bin/kafka-console-producer.sh --broker-list localhost:9092 --topic streams-plaintext-input
        // >hello hello hello hello

        // console消费者
        // langjiang@langs-MacBook-Pro kafka_2.11-2.1.0 % bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic streams-linesplit-output
        // hello
        // hello
        // hello
        // hello
    }
}
