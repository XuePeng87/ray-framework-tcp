package cc.xuepeng.ray.framework.tcp.protocol.parser.dataunit;

import cc.xuepeng.ray.framework.core.util.bean.BeanUtil;
import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import cc.xuepeng.ray.framework.tcp.protocol.entity.VehicleLoginProtocol;
import cc.xuepeng.ray.framework.tcp.protocol.enums.CommandType;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 时间数据单元解析器。
 *
 * @author xuepeng
 */
@Component
public class VehicleLoginProtocolParser implements DataUnitParser {

    /**
     * 对协议进行解码。
     *
     * @param baseProtocol 基本数据协议。
     */
    @Override
    public void decode(final BaseProtocol baseProtocol) {
        final ByteBuf byteBuf = baseProtocol.getData();
        final VehicleLoginProtocol vehicleLoginProtocol = new VehicleLoginProtocol();
        vehicleLoginProtocol.getTime().setYear(byteBuf.readUnsignedByte());
        vehicleLoginProtocol.getTime().setMonth(byteBuf.readUnsignedByte());
        vehicleLoginProtocol.getTime().setDay(byteBuf.readUnsignedByte());
        vehicleLoginProtocol.getTime().setHours(byteBuf.readUnsignedByte());
        vehicleLoginProtocol.getTime().setMinutes(byteBuf.readUnsignedByte());
        vehicleLoginProtocol.getTime().setSeconds(byteBuf.readUnsignedByte());
        vehicleLoginProtocol.setSerialNum(byteBuf.readUnsignedShort());
        vehicleLoginProtocol.setIccid(byteBuf.readSlice(20).toString(Charset.forName(CharsetUtil.UTF_8.name())));
        vehicleLoginProtocol.setCount(byteBuf.readUnsignedByte());
        vehicleLoginProtocol.setLength(byteBuf.readUnsignedByte());
        if (vehicleLoginProtocol.getCount() > 0 && vehicleLoginProtocol.getLength() > 0) {
            final List<String> codeList = new ArrayList<>();
            for (int i = 0; i < vehicleLoginProtocol.getCount(); i++) {
                String code = byteBuf.readSlice(vehicleLoginProtocol.getLength()).toString(Charset.forName(CharsetUtil.UTF_8.name()));
                codeList.add(code);
            }
            vehicleLoginProtocol.setCodes(codeList);
        }
        baseProtocol.setJsonData(BeanUtil.getObjToStr(vehicleLoginProtocol));
    }

    /**
     * @return 获取协议命令。
     */
    @Override
    public CommandType getCommandType() {
        return COMMAND_TYPE;
    }

    /**
     * 协议命令。
     */
    private static final CommandType COMMAND_TYPE = CommandType.VEHICLE_LOGIN;

}
