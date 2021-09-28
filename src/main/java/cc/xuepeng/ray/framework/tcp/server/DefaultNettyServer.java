package cc.xuepeng.ray.framework.tcp.server;

import cc.xuepeng.ray.framework.tcp.disruptor.RingBufferWorkerPoolFactory;
import cc.xuepeng.ray.framework.tcp.server.config.NettyServerConfig;
import cc.xuepeng.ray.framework.tcp.server.handler.DefaultMessageDecodeHandler;
import cc.xuepeng.ray.framework.tcp.server.handler.DefaultMessageEncodeHandler;
import cc.xuepeng.ray.framework.tcp.server.handler.DefaultMessageInboundHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * NettyServer的框架默认实现类。
 * 负责启动和关闭Netty，配置Netty参数，配置Netty的Pipeline。
 *
 * @author xuepeng
 */
@Component
@Slf4j
public class DefaultNettyServer implements NettyServer {

    /**
     * 启动服务端。
     *
     * @return ChannelFuture对象。
     * @throws InterruptedException 抛出中断异常。
     */
    public ChannelFuture startUp() throws InterruptedException {
        return this.startUp(nettyServerConfig.getHost(), nettyServerConfig.getPort());
    }

    /**
     * 启动服务端。
     *
     * @param host 监听host。
     * @param port 监听port。
     * @return ChannelFuture对象。
     * @throws InterruptedException 抛出中断异常。
     */
    public ChannelFuture startUp(final InetAddress host, final int port) throws InterruptedException {
        return this.startUp(host.getHostAddress(), port);
    }

    /**
     * 启动服务端。
     *
     * @param host 监听host。
     * @param port 监听port。
     * @return ChannelFuture对象。
     * @throws InterruptedException 抛出中断异常。
     */
    public ChannelFuture startUp(final String host, final int port) throws InterruptedException {
        // 启动Disruptor
        ringBufferWorkerPoolFactory.start(
                ProducerType.MULTI,
                nettyServerConfig.getDisruptor().getBufferSize(),
                new YieldingWaitStrategy(),
                nettyServerConfig.getDisruptor().getConsumerCount());
        // 启动Netty
        final ChannelFuture channelFuture;
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup(nettyServerConfig.getWorkCount());
        bootstrap.group(bossGroup, workGroup)
                // SYNC与ACCEPT的队列和
                .option(ChannelOption.SO_BACKLOG, nettyServerConfig.getOption().getBackLog())
                // 接收缓冲区大小
                .option(ChannelOption.RCVBUF_ALLOCATOR,
                        new FixedRecvByteBufAllocator(nettyServerConfig.getOption().getRecBufAllocator()))
                // 缓冲区池化操作
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyServerConfig.getOption().getConnectTimeout())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.FALSE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .channel(NioServerSocketChannel.class)
                .childHandler(initializerChildHeader());
        // 绑定端口并同步等待
        log.info("Start Up......");
        channelFuture = bootstrap.bind(host, port).sync();
        log.info("Start Up Success.");
        return channelFuture;
    }

    /**
     * 关闭服务端。
     *
     */
    public void shutdown() {
        log.info("Shutdown......");
        workGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success.");
    }

    /**
     * 用户创建ChannelInitializer中需要注册到Pipeline中的Handler
     *
     * @return ChannelInitializer对象
     */
    private ChannelInitializer<SocketChannel> initializerChildHeader() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline()
                        // 心跳检测处理
                        .addLast(new IdleStateHandler(
                                nettyServerConfig.getIdleState().getReaderIdleTime(),
                                nettyServerConfig.getIdleState().getWriterIdleTime(),
                                nettyServerConfig.getIdleState().getAllIdleTime(),
                                TimeUnit.SECONDS)
                        )
                        // 处理粘包
                        .addLast(new LengthFieldBasedFrameDecoder(
                                nettyServerConfig.getProtocolDecoder().getMaxFrameLength(),
                                nettyServerConfig.getProtocolDecoder().getLengthFieldOffset(),
                                nettyServerConfig.getProtocolDecoder().getLengthFieldLength(),
                                nettyServerConfig.getProtocolDecoder().getLengthAdjustment(),
                                nettyServerConfig.getProtocolDecoder().getInitialBytesToStrip(),
                                nettyServerConfig.getProtocolDecoder().getFailFast()
                        ))
                        // 消息入站解码处理
                        .addLast(new DefaultMessageDecodeHandler())
                        // 消息出站编码处理
                        .addLast(new DefaultMessageEncodeHandler())
                        // 消息入站处理
                        .addLast(new DefaultMessageInboundHandler());
            }
        };
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
     * 自动装配Disruptor工厂。
     *
     * @param ringBufferWorkerPoolFactory Disruptor工厂。
     */
    @Autowired
    public void setRingBufferWorkerPoolFactory(RingBufferWorkerPoolFactory ringBufferWorkerPoolFactory) {
        this.ringBufferWorkerPoolFactory = ringBufferWorkerPoolFactory;
    }

    /**
     * 服务端NIO线程组。
     */
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    /**
     * 服务端配置信息。
     */
    private NettyServerConfig nettyServerConfig;


    /**
     * Disruptor工厂。
     */
    protected RingBufferWorkerPoolFactory ringBufferWorkerPoolFactory;

}
