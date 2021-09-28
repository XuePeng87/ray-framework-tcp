package cc.xuepeng.ray.framework.tcp.server;

import io.netty.channel.ChannelFuture;

import java.net.InetAddress;

/**
 * Netty服务端处理器。
 */
public interface NettyServer {

    /**
     * 启动服务端。
     *
     * @return ChannelFuture对象。
     * @throws InterruptedException 抛出中断异常。
     */
    ChannelFuture startUp() throws InterruptedException;

    /**
     * 启动服务端。
     *
     * @param host 监听host。
     * @param port 监听port。
     * @return ChannelFuture对象。
     * @throws InterruptedException 抛出中断异常。
     */
    ChannelFuture startUp(final InetAddress host, final int port) throws InterruptedException;

    /**
     * 启动服务端。
     *
     * @param host 监听host。
     * @param port 监听port。
     * @return ChannelFuture对象。
     * @throws InterruptedException 抛出中断异常。
     */
    ChannelFuture startUp(final String host, final int port) throws InterruptedException;

    /**
     * 关闭服务端。
     */
    void shutdown();

}
