package cc.xuepeng.ray.framework.tcp.server.ctx;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 自定义的TCP链接对象。
 * 内部封装了Netty的ChannelHandlerContext对象。
 * 并封装了常用的业务数据。
 *
 * @author xuepeng
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TcpConnectionContext {

    /**
     * @return 连接通道。
     */
    public Channel getChannel() {
        return this.ctx.channel();
    }

    /**
     * 连接上线。
     */
    public void online() {
        online.compareAndSet(Boolean.FALSE, Boolean.TRUE);
    }

    /**
     * 连接下线。
     */
    public void offline() {
        online.compareAndSet(Boolean.TRUE, Boolean.FALSE);
    }

    /**
     * 链接处理对象。
     */
    private ChannelHandlerContext ctx;

    /**
     * IP地址。
     */
    private String ip;

    /**
     * 车架号。
     */
    private String vin;

    /**
     * 是否在线。
     */
    private final AtomicBoolean online = new AtomicBoolean(Boolean.FALSE);

}
