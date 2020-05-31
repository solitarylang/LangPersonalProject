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
 * @date ：Created in 2020/5/30 7:14 PM
 * @description ：kafka streams DSL的word count,source=kafka,sink=kafka
 * @version : 1.0.0$
 */
public class WordCount {
    public static void main(String[] args) {
        Properties props = new Properties();
        // 可作为consumer的group id
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        // kafka的地址，多个逗号分隔，目前只支持单集群
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 序列化和反序列化，在读取和写出流的时候、在读取和写出state的时候都会用到
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream("streams-plaintext-input");

        source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" ")))
                .groupBy((key, value) -> value)
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
                .toStream()
                .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        // CountDownLatch用await()阻塞当前线程，countDown()记录完成线程的数量
        // 当getCount()=0的时候继续执行await后续的代码
        final CountDownLatch latch = new CountDownLatch(1);

        System.out.println(topology.describe());

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
        // langjiang@langs-MacBook-Pro kafka_2.11-2.1.0 % bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
        //    --topic streams-wordcount-output \
        //    --from-beginning \
        //    --formatter kafka.tools.DefaultMessageFormatter \
        //    --property print.key=true \
        //    --property print.value=true \
        //    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
        //    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
        //  kafkastream	1
        //  learn	1
        //  kafka	1
        //  stream	1
        //  demo	1
        //  hello	14
        //  hello	15
    }
}
