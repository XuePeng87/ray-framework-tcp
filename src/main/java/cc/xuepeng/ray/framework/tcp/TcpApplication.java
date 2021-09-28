package cc.xuepeng.ray.framework.tcp;

import cc.xuepeng.ray.framework.tcp.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Yesway TCP Framework启动类。
 *
 * @author xuepeng
 */
@SpringBootApplication
@ComponentScan("cc.xuepeng.*")
public class TcpApplication implements CommandLineRunner {

    /**
     * 程序启动方法。
     *
     * @param args 启动参数。
     */
    public static void main(String[] args) {
        SpringApplication.run(TcpApplication.class, args);
    }

    /**
     * Netty服务端启动方法。
     *
     * @param args 启动参数。
     * @throws Exception 启动过程中有异常则抛出。
     */
    @Override
    public void run(String... args) throws Exception {
        // 启动NettyServer
        final ChannelFuture future = nettyServer.startUp();
        // 挂接关闭NettyServer，
        Runtime.getRuntime().addShutdownHook(new Thread("ShutdownHook-Thread") {
            @Override
            public void run() {
                nettyServer.shutdown();
            }
        });
        // 监听关闭事件
        future.channel().closeFuture().sync();
    }

    /**
     * 自动装配Netty服务端处理器。
     *
     * @param nettyServer Netty服务端处理器。
     */
    @Autowired
    public void setNettyServer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    /**
     * Netty服务端处理器。
     */
    private NettyServer nettyServer;

}
