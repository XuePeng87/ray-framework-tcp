package cc.xuepeng.ray.framework.tcp.command;

import cc.xuepeng.ray.framework.tcp.disruptor.RingBufferWorkerPoolFactory;
import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import cc.xuepeng.ray.framework.tcp.server.ctx.TcpConnectionContext;
import cc.xuepeng.ray.framework.tcp.server.ctx.TcpConnectionContextManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 车辆上行命令处理抽象类。
 *
 * @author xuepeng
 */
@Component
public abstract class AbstractVehicleCommand implements VehicleCommand {

    /**
     * 执行命令。
     *
     * @param ctx          ChannelHandlerContext对象。
     * @param baseProtocol 新能源GB32960协议。
     */
    @Override
    public void execute(final ChannelHandlerContext ctx, final BaseProtocol baseProtocol) {
        post(ctx, baseProtocol);
        dispatch(baseProtocol);
        after(ctx, baseProtocol);
    }

    /**
     * 分发前的处理。
     *
     * @param ctx          ChannelHandlerContext对象。
     * @param baseProtocol 新能源GB32960协议。
     */
    protected abstract void post(final ChannelHandlerContext ctx, final BaseProtocol baseProtocol);

    /**
     * 数据分发。
     *
     * @param baseProtocol 新能源GB32960协议。
     */
    protected abstract void dispatch(final BaseProtocol baseProtocol);

    /**
     * 分发后的处理。
     *
     * @param ctx          ChannelHandlerContext对象。
     * @param baseProtocol 新能源GB32960协议。
     */
    protected abstract void after(final ChannelHandlerContext ctx, final BaseProtocol baseProtocol);

    /**
     * 自动装配TCP连接管理器。
     *
     * @param tcpConnectionContextManager TCP连接管理器。
     */
    @Autowired
    public void setTcpConnectionContextManager(TcpConnectionContextManager<TcpConnectionContext> tcpConnectionContextManager) {
        this.tcpConnectionContextManager = tcpConnectionContextManager;
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
     * TCP连接管理器。
     */
    protected TcpConnectionContextManager<TcpConnectionContext> tcpConnectionContextManager;

    /**
     * Disruptor工厂。
     */
    protected RingBufferWorkerPoolFactory ringBufferWorkerPoolFactory;

}
