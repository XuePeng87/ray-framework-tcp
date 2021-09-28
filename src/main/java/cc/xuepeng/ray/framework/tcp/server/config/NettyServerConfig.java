package cc.xuepeng.ray.framework.tcp.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Netty Server端的配置信息。
 *
 * @author xuepeng
 */
@Data
@Configuration
@ConfigurationProperties("ray.framework.tcp")
public class NettyServerConfig {

    /**
     * 监听Host。
     */
    private String host = "127.0.0.1";

    /**
     * 监听Port。
     */
    private Integer port = 7070;

    /**
     * WorkGroup的线程数量。
     */
    private Integer workCount = Runtime.getRuntime().availableProcessors() / 2;

    /**
     * NettyServer参数配置。
     */
    private Option option = new Option();

    /**
     * NettyServer空闲状态检测配置参数。
     */
    private IdleState idleState = new IdleState();

    /**
     * 协议解码器配置参数。
     */
    private ProtocolDecoder protocolDecoder = new ProtocolDecoder();

    /**
     * 分发器配置信息。
     */
    private Disruptor disruptor = new Disruptor();

    /**
     * NettyServer配置参数。
     */
    @Data
    public class Option {

        /**
         * BackLog长度（SYNC+Accept的队列长度）。
         */
        private Integer backLog = 1024;

        /**
         * 接收缓冲区大小。
         */
        private Integer recBufAllocator = 1024;

        /**
         * 连接超时时间。
         */
        private Integer connectTimeout = 3000;

    }

    /**
     * NettyServer空闲状态检测配置参数。
     *
     * @author xuepeng
     */
    @Data
    public class IdleState {

        /**
         * 读超时时间，即服务端一定时间内未接收到客户端的消息。
         */
        private Integer readerIdleTime = 10;

        /**
         * 为写超时时间，即服务端一定时间内向客户端发送消息。
         */
        private Integer writerIdleTime = 0;

        /**
         * 所有类型的超时时间。
         */
        private Integer allIdleTime = 0;

    }

    /**
     * 协议解码器。
     *
     * @author xuepeng
     */
    @Data
    public class ProtocolDecoder {

        /**
         * 帧的最大长度。
         */
        private Integer maxFrameLength = 65535;

        /**
         * 字段偏移的地址。
         */
        private Integer lengthFieldOffset = 22;

        /**
         * 字段所占的字节长。
         */
        private Integer lengthFieldLength = 2;

        /**
         * 修改帧数据长度字段中定义的值，可以为负数。
         */
        private Integer lengthAdjustment = 1;

        /**
         * 解析时候跳过的长度。
         */
        private Integer initialBytesToStrip = 0;

        /**
         * 长度超过maxFrameLength时立即报TooLongFrameException异常。
         */
        private Boolean failFast = Boolean.TRUE;

    }

    /**
     * 分发器配置信息。
     *
     * @author xuepeng
     */
    @Data
    public class Disruptor {

        /**
         * RingBuffer长度。
         */
        private Integer bufferSize = 1024;

        /**
         * 消费者数量。
         */
        private Integer consumerCount = Runtime.getRuntime().availableProcessors() / 2;

        /**
         * 消费者线程池配置。
         */
        private ConsumerThreadPool consumerThreadPool = new ConsumerThreadPool();

        /**
         * 消费者线程池配置。
         *
         * @author xuepeng
         */
        @Data
        public class ConsumerThreadPool {

            /**
             * 核心线程数。
             */
            private Integer coreSize = Runtime.getRuntime().availableProcessors() / 2;

            /**
             * 最大线程数。
             */
            private Integer maximunSize = Runtime.getRuntime().availableProcessors();

            /**
             * 线程存活时间（秒）。
             */
            private Long keepAliveTime = 5L;

            /**
             * 等待队列长度。
             */
            private Integer workQueueSize = 128;

        }

    }

}
