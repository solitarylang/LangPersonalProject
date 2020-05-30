package cn.lang.kafka.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：jianglang
 * @date ：Created in 2020/5/30 6:49 PM
 * @description ：kafka stream的单词单词统计，旨在了解书写kafka stream程序的组件以及本地调试部署
 *          代码来源[应用开发](http://kafka.apache.org/25/documentation/streams/tutorial)
 * @version: 1.0.0$
 */
public class Pipe {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        builder.stream("streams-plaintext-input").to("streams-pipe-output");

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
        // >learn kafka stream demo

        // console消费者
        // langjiang@langs-MacBook-Pro kafka_2.11-2.1.0 % bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic streams-pipe-output
        // learn kafka stream demo
    }
}
