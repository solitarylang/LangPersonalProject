package cn.lang.kafka.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;

/**
 * @Author ：lang
 * @Date ：Created in 2020/5/31 11:21 AM
 * @Description ：使用processor API完成单词统计的功能开发，并做好本地测试和注释
 * @link : http://kafka.apache.org/25/documentation/streams/developer-guide/processor-api.html
 * @Version :     1.0.0
 */
public class WordCount1 {
    /**
     * @Description 通过实现Processor重写process方法自定义Processor
     * 自定义的时候，先看上层接口，然后找一个内置的实现类参考比如KStreamAggregateProcessor
     */
    public static class WordCountProcessor implements Processor<String, String> {
        private ProcessorContext context;
        private KeyValueStore<String, Long> kvStore;

        @SuppressWarnings("unchecked")
        public void init(ProcessorContext context) {
            // keep the processor context locally because we need it in punctuate() and commit()
            this.context = context;
            // 获取名为Counts的状态
            kvStore = (KeyValueStore) context.getStateStore("Counts");
        }

        // 每接收到一条消息就会执行一次process,这里是将结果放回缓存中
        public void process(String key, String value) {
            String[] words = value.toLowerCase().split(" ");
            for (String word : words) {
                // 获取之前这个单词统计的数量，之前没有统计就设置为1
                Long preCount = this.kvStore.get(word);
                Long result = preCount == null ? 1 : preCount + 1;
                this.kvStore.put(word, result);
                this.context.forward(word,result.toString());
                System.out.println("process , key = " + word + ",and value = " + result);
            }
        }

        public void close() {
            // close any resources managed by this processor
            // Note: Do not close any StateStores as these are managed by the library
        }
    }

    public static void main(String[] args) {
        /* 1.应用配置参数 */
        Properties props = new Properties();
        // 可作为consumer的group id
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount1");
        // kafka的地址，多个逗号分隔，目前只支持单集群
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 序列化和反序列化，在读取和写出流的时候、在读取和写出state的时候都会用到
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        /* 2.拓扑 */
        StoreBuilder<KeyValueStore<String, Long>> countStoreSupplier = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("Counts"),
                Serdes.String(),
                Serdes.Long())
                .withLoggingDisabled(); // disable backing up the store to a changelog topic

        Topology builder = new Topology();

        // add the source processor node that takes Kafka topic "source-topic" as input
        builder.addSource("Source", "source-topic")
                // add the WordCountProcessor node which takes the source processor as its upstream processor
                .addProcessor("Process", WordCountProcessor::new, "Source")
                // add the count store associated with the WordCountProcessor processor
                .addStateStore(countStoreSupplier, "Process")
                // add the sink processor node that takes Kafka topic "sink-topic" as output
                // and the WordCountProcessor node as its upstream processor
                .addSink("Sink", "sink-topic", "Process");
        System.out.println(builder.describe());

        /* 3.流处理客户端实例 */
        KafkaStreams streams = new KafkaStreams(builder, props);

        /* 4.启动 */
        streams.start();
    }
}
