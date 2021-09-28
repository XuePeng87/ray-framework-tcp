package cc.xuepeng.ray.framework.tcp.server.handler;

import cc.xuepeng.ray.framework.tcp.command.VehicleCommandFactory;
import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import cc.xuepeng.ray.framework.tcp.server.ctx.TcpConnectionContext;
import cc.xuepeng.ray.framework.tcp.server.ctx.TcpConnectionContextManager;
import cc.xuepeng.ray.framework.tcp.util.ContextUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Message Inbound的框架默认实现类。
 *
 * @author xuepeng
 */
@Slf4j
@Component
public class DefaultMessageInboundHandler extends SimpleChannelInboundHandler<BaseProtocol> {

    /**
     * 将当前类托管到Sprint IOC，所以声明该变量。
     */
    private static DefaultMessageInboundHandler self;

    /**
     * 通过@PostConstruct注解将当前类托管到Sprint IOC。
     */
    @PostConstruct
    public synchronized void init() {
        self = this;
    }

    /**
     * 处理客户端数据。
     *
     * @param ctx          ChannelHandlerContext对象。
     * @param baseProtocol 客户端数据。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseProtocol baseProtocol) {
        if (!baseProtocol.isBcc() || !baseProtocol.isBegin()) {
            log.warn("{} 数据包校验失败，校验和不正确。", baseProtocol.getVin());
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("车辆 {} 发送数据", baseProtocol.getVin());
        }
        self.vehicleCommandFactory
                .getInstance(baseProtocol.getCommandType())
                .execute(ctx, baseProtocol);
    }

    /**
     * 处理客户端断开链接。
     *
     * @param ctx ChannelHandlerContext对象。
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        self.tcpConnectionContextManager.breakConnection(
                TcpConnectionContext.builder()
                        .ip(ContextUtil.getClientIp(ctx))
                        .ctx(ctx)
                        .build()
        );
        ctx.close();
    }

    /**
     * 用户事件触发器。
     *
     * @param ctx ChannelHandlerContext对象。
     * @param evt 用户事件对象。
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            final IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                final String ip = ContextUtil.getClientIp(ctx);
                ctx.close();
                if (log.isDebugEnabled()) {
                    log.debug("{} 已超过心跳时间，关闭通道", ip);
                }
            }
        }
    }

    /**
     * 处理客户端数据异常。
     *
     * @param ctx   ChannelHandlerContext对象。
     * @param cause Throwable对象。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        log.error(cause.getMessage());
        super.channelInactive(ctx);
    }

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
     * 自动装配车辆命令工厂。
     *
     * @param vehicleCommandFactory 车辆命令工厂。
     */
    @Autowired
    public void setVehicleCommandFactory(VehicleCommandFactory vehicleCommandFactory) {
        this.vehicleCommandFactory = vehicleCommandFactory;
    }

    /**
     * TCP连接管理器。
     */
    private TcpConnectionContextManager<TcpConnectionContext> tcpConnectionContextManager;

    /**
     * 车辆命令工厂。
     */
    private VehicleCommandFactory vehicleCommandFactory;

}
