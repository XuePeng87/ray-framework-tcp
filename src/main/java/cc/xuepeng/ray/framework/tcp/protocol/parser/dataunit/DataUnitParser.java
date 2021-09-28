package cc.xuepeng.ray.framework.tcp.protocol.parser.dataunit;

import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import cc.xuepeng.ray.framework.tcp.protocol.enums.CommandType;

/**
 * 数据单元解析器。
 *
 * @author xuepeng
 */
public interface DataUnitParser {

    /**
     * 对协议进行解码。
     *
     * @param baseProtocol 基本数据协议。
     */
    void decode(final BaseProtocol baseProtocol);

    /**
     * @return 获取协议命令。
     */
    CommandType getCommandType();

}
