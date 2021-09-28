package cc.xuepeng.ray.framework.tcp.command;

import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import cc.xuepeng.ray.framework.tcp.protocol.enums.CommandType;
import io.netty.channel.ChannelHandlerContext;

/**
 * 车辆上行命令处理接口。
 *
 * @author xuepeng
 */
public interface VehicleCommand {

    /**
     * 执行命令。
     *
     * @param ctx          ChannelHandlerContext对象。
     * @param baseProtocol 数据协议。
     */
    void execute(final ChannelHandlerContext ctx, final BaseProtocol baseProtocol);

    /**
     * @return 获取命令类型。
     */
    CommandType getCommandType();

}
