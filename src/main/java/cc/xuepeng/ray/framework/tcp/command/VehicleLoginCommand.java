package cc.xuepeng.ray.framework.tcp.command;

import cc.xuepeng.ray.framework.core.util.bean.BeanUtil;
import cc.xuepeng.ray.framework.tcp.disruptor.MessageProducer;
import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import cc.xuepeng.ray.framework.tcp.protocol.entity.VehicleLoginProtocol;
import cc.xuepeng.ray.framework.tcp.protocol.enums.CommandType;
import cc.xuepeng.ray.framework.tcp.protocol.enums.ResponseType;
import cc.xuepeng.ray.framework.tcp.server.ctx.TcpConnectionContext;
import cc.xuepeng.ray.framework.tcp.util.ContextUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 车辆登入命令处理接口。
 *
 * @author xuepeng
 */
@Component
@Slf4j
public class VehicleLoginCommand extends AbstractVehicleCommand {

    /**
     * 分发前的处理。
     *
     * @param ctx          ChannelHandlerContext对象。
     * @param baseProtocol 新能源GB32960协议。
     */
    @Override
    public void post(final ChannelHandlerContext ctx, final BaseProtocol baseProtocol) {
        final VehicleLoginProtocol vehicleLoginProtocol = BeanUtil.getStrToObj(
                baseProtocol.getJsonData(), VehicleLoginProtocol.class
        );
        final String vin = baseProtocol.getVin();
        final String ip = ContextUtil.getClientIp(ctx);
        super.tcpConnectionContextManager.addConnection(
                TcpConnectionContext.builder()
                        .ip(ip)
                        .vin(baseProtocol.getVin())
                        .ctx(ctx)
                        .build()
        );
        if (log.isDebugEnabled()) {
            log.debug("车辆 {} 连接到了服务器，IP为 {}，数据为 {}", vin, ip, BeanUtil.getObjToStr(vehicleLoginProtocol));
        }
    }

    /**
     * 数据分发。
     *
     * @param baseProtocol 新能源GB32960协议。
     */
    @Override
    public void dispatch(final BaseProtocol baseProtocol) {
        MessageProducer messageProducer =
                super.ringBufferWorkerPoolFactory.getMessageProducer(getCommandType().getDesc());
        messageProducer.publish(baseProtocol);
    }

    /**
     * 分发后的处理。
     *
     * @param ctx          ChannelHandlerContext对象。
     * @param baseProtocol 新能源GB32960协议。
     */
    @Override
    public void after(final ChannelHandlerContext ctx, final BaseProtocol baseProtocol) {
        baseProtocol.setResponseType(ResponseType.SUCCESS);
        baseProtocol.setData(null);
        baseProtocol.setJsonData(StringUtils.EMPTY);
        ctx.writeAndFlush(baseProtocol);
    }

    /**
     * @return 获取命令类型。
     */
    @Override
    public CommandType getCommandType() {
        return CommandType.VEHICLE_LOGIN;
    }

}
