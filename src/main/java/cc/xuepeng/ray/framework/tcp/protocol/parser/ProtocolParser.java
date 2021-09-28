package cc.xuepeng.ray.framework.tcp.protocol.parser;

import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import io.netty.buffer.ByteBuf;

/**
 * 新能源GB32960协议解析器。
 *
 * @author xuepeng
 */
public interface ProtocolParser {

    /**
     * 对协议进行解码。
     *
     * @param byteBuf 协议原文。
     * @return 解码后的协议。
     */
    BaseProtocol decode(final ByteBuf byteBuf);

    /**
     * 对协议进行编码。
     *
     * @param baseProtocol 协议内容。
     * @return 编码后的协议。
     */
    ByteBuf encode(final BaseProtocol baseProtocol);

}
