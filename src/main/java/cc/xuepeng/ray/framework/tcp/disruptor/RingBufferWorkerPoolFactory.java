package cc.xuepeng.ray.framework.tcp.disruptor;

import cc.xuepeng.ray.framework.tcp.server.config.NettyServerConfig;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Disruptor工厂。
 * 管理了生产者和消费者的对象。
 *
 * @author xuepeng
 */
@Component
@Slf4j
public class RingBufferWorkerPoolFactory {

    /**
     * 启动Disruptor。
     *
     * @param producerType 生产者类型。
     * @param bufferSize   ringBuffer大小。
     * @param waitStrategy 等待策略。
     * @param consumerSize 消费者数量。
     */
    public void start(final ProducerType producerType,
                      final int bufferSize,
                      final WaitStrategy waitStrategy,
                      final int consumerSize) {
        MessageConsumer[] messageConsumers = new MessageConsumer[consumerSize];
        for (int i = 0; i < messageConsumers.length; i++) {
            messageConsumers[i] = new MessageConsumer("Consumer" + i);
        }
        // 构建ringBuffer
        this.ringBuffer = RingBuffer.create(
                producerType,
                BaseProtocolWapper::new,
                bufferSize,
                waitStrategy);
        // 构建sequenceBarrier
        SequenceBarrier sequenceBarrier = this.ringBuffer.newBarrier();
        // 构建工作池
        WorkerPool<BaseProtocolWapper> workerPool = new WorkerPool<>(
                this.ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                messageConsumers
        );
        // 添加到messageConsumer到池中
        for (MessageConsumer messageConsumer : messageConsumers) {
            this.consumers.put(messageConsumer.getConsumerId(), messageConsumer);
        }
        // 添加sequences
        this.ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        // 启动工作池
        workerPool.start(new ThreadPoolExecutor(
                nettyServerConfig.getDisruptor().getConsumerThreadPool().getCoreSize(),
                nettyServerConfig.getDisruptor().getConsumerThreadPool().getMaximunSize(),
                nettyServerConfig.getDisruptor().getConsumerThreadPool().getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(nettyServerConfig.getDisruptor().getConsumerThreadPool().getWorkQueueSize()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy())
        );
    }

    /**
     * 根据生产者ID获取生产者。
     *
     * @param producerId 生产者ID。
     * @return 生产者。
     */
    public MessageProducer getMessageProducer(final String producerId) {
        MessageProducer messageProducer = this.producers.get(producerId);
        lock.lock();
        try {
            if (Objects.isNull(messageProducer)) {
                messageProducer = new MessageProducer(producerId, this.ringBuffer);
                this.producers.put(producerId, messageProducer);
            }
        } finally {
            lock.unlock();
        }
        return messageProducer;
    }

    /**
     * 根据消费者ID获取消费者。
     *
     * @param consumerId 消费者ID。
     * @return 消费者。
     */
    public MessageConsumer getMessageConsumer(final String consumerId) {
        return this.consumers.get(consumerId);
    }

    /**
     * Disruptor处理异常。
     *
     * @author xuepeng
     */
    static class EventExceptionHandler implements ExceptionHandler<BaseProtocolWapper> {

        @Override
        public void handleEventException(Throwable throwable, long l, BaseProtocolWapper baseProtocol) {
            log.error("Disruptor处理时发生异常，{}", throwable.getMessage());
        }

        @Override
        public void handleOnStartException(Throwable throwable) {
            log.error("Disruptor启动时发生异常，{}", throwable.getMessage());
        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {
            log.error("Disruptor关闭时发生异常，{}", throwable.getMessage());
        }

    }

    /**
     * 自动装配服务端配置信息。
     *
     * @param nettyServerConfig 服务端配置信息。
     */
    @Autowired
    public void setNettyServerConfig(NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
    }

    /**
     * 生产者对象集合。
     */
    private final Map<String, MessageProducer> producers = new ConcurrentHashMap<>();

    /**
     * 消费者对象集合。
     */
    private final Map<String, MessageConsumer> consumers = new ConcurrentHashMap<>();

    /**
     * ringBuffer对象。
     */
    private RingBuffer<BaseProtocolWapper> ringBuffer;

    /**
     * 可重入锁，用于控制生产者的创建。
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 服务端配置信息。
     */
    private NettyServerConfig nettyServerConfig;

}
