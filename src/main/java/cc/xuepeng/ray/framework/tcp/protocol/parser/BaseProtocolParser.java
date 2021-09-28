package cc.xuepeng.ray.framework.tcp.protocol.parser;

import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import cc.xuepeng.ray.framework.tcp.protocol.enums.CommandType;
import cc.xuepeng.ray.framework.tcp.protocol.enums.EncryptionType;
import cc.xuepeng.ray.framework.tcp.protocol.enums.ResponseType;
import cc.xuepeng.ray.framework.tcp.protocol.parser.dataunit.DataUnitParser;
import cc.xuepeng.ray.framework.tcp.protocol.parser.dataunit.DataUnitParserFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * 新能源GB32960协议解析器。
 *
 * @author xuepeng
 */
@Component
public class BaseProtocolParser implements ProtocolParser {

    /**
     * 对协议进行解码。
     *
     * @param byteBuf 协议原文。
     * @return 解码后的协议。
     */
    @Override
    public BaseProtocol decode(final ByteBuf byteBuf) {
        final BaseProtocol protocol = new BaseProtocol();
        // 判断校验合
        final boolean checkBcc = checkBcc(byteBuf);
        protocol.setBcc(checkBcc);
        // 判断协议头为##
        final boolean checkBegin = StringUtils.equals(
                BaseProtocol.BEGIN_CHAR,
                byteBuf.readSlice(2).toString(Charset.forName(CharsetUtil.UTF_8.name()))
        );
        protocol.setBegin(checkBegin);
        protocol.setCommandType(CommandType.findById(byteBuf.readUnsignedByte()));
        protocol.setResponseType(ResponseType.findById(byteBuf.readUnsignedByte()));
        protocol.setVin(byteBuf.readSlice(17).toString(Charset.forName(CharsetUtil.UTF_8.name())));
        protocol.setEncryptionType(EncryptionType.findById(byteBuf.readUnsignedByte()));
        // 校验码正确与起始符号正确的时候才解析数据单元
        if (checkBcc && checkBegin) {
            int length = byteBuf.readUnsignedShort();
            protocol.setLength(length);
            if (length > 0) {
                protocol.setData(byteBuf.readSlice(length).copy());
            }
        }
        byteBuf.readUnsignedByte();
        // 解析数据单元
        dataUnitParser(protocol);
        return protocol;
    }

    /**
     * 对协议进行编码。
     *
     * @param baseProtocol 协议内容。
     * @return 编码后的协议。
     */
    @Override
    public ByteBuf encode(final BaseProtocol baseProtocol) {
        final String vin = baseProtocol.getVin();
        if (StringUtils.length(vin) != 17) {
            throw new IllegalArgumentException(String.format("VIN %s 的长度不正确。", vin));
        }
        ByteBuf bccBuffer = Unpooled.buffer();
        bccBuffer.writeByte(baseProtocol.getCommandType().getId());
        bccBuffer.writeByte(baseProtocol.getResponseType().getId());
        bccBuffer.writeBytes(vin.getBytes(Charset.forName(CharsetUtil.UTF_8.name())));
        bccBuffer.writeByte(baseProtocol.getEncryptionType().getId());
        if (baseProtocol.getData() != null && baseProtocol.getData().readableBytes() > 0) {
            bccBuffer.writeShort(baseProtocol.getData().readableBytes());
            bccBuffer.writeBytes(baseProtocol.getData());
        } else {
            bccBuffer.writeShort(0);
        }
        final byte bcc = signBcc(bccBuffer);
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(BaseProtocol.BEGIN_CHAR.getBytes(Charset.forName(CharsetUtil.UTF_8.name())));
        buffer.writeBytes(bccBuffer);
        buffer.writeByte(bcc);
        return buffer;
    }

    /**
     * 进行BCC校验合验证。
     *
     * @param byteBuf 协议数据。
     * @return 是否通过BCC校验。
     */
    private boolean checkBcc(final ByteBuf byteBuf) {
        final byte bcc = byteBuf.getByte(byteBuf.readableBytes() - 1);
        final ByteBuf slice = byteBuf.slice(2, byteBuf.readableBytes() - 3);
        final byte checkBcc = signBcc(slice);
        byteBuf.resetReaderIndex();
        return bcc == checkBcc;
    }

    /**
     * 生成协议的BCC校验合。
     *
     * @param byteBuf 协议数据。
     * @return BCC校验合。
     */
    private byte signBcc(final ByteBuf byteBuf) {
        byte cs = 0;
        while (byteBuf.isReadable()) {
            cs ^= byteBuf.readByte();
        }
        byteBuf.resetReaderIndex();
        return cs;
    }

    /**
     * 进行数据单元解析。
     *
     * @param baseProtocol 原始协议。
     */
    private void dataUnitParser(final BaseProtocol baseProtocol) {
        final DataUnitParser dataUnitParser = dataUnitParserFactory.getInstance(baseProtocol.getCommandType());
        dataUnitParser.decode(baseProtocol);
    }

    /**
     * 自动装配数据单元解析器工厂。
     *
     * @param dataUnitParserFactory 数据单元解析器工厂。
     */
    @Autowired
    public void setDataUnitParserFactory(DataUnitParserFactory dataUnitParserFactory) {
        this.dataUnitParserFactory = dataUnitParserFactory;
    }

    /**
     * 数据单元解析器工厂。
     */
    private DataUnitParserFactory dataUnitParserFactory;

}
