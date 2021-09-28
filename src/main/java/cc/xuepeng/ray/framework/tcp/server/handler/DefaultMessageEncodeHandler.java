package cc.xuepeng.ray.framework.tcp.server.handler;

import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import cc.xuepeng.ray.framework.tcp.protocol.parser.BaseProtocolParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Message的框架默认编码器。
 *
 * @author xuepeng
 */
@Slf4j
@Component
public class DefaultMessageEncodeHandler extends MessageToByteEncoder<BaseProtocol> {

    /**
     * 将当前类托管到Sprint IOC，所以声明该变量。
     */
    private static DefaultMessageEncodeHandler self;

    /**
     * 通过@PostConstruct注解将当前类托管到Sprint IOC。
     */
    @PostConstruct
    public synchronized void init() {
        self = this;
    }

    /**
     * 协议编码。
     *
     * @param channelHandlerContext ChannelHandlerContext对象。
     * @param baseProtocol          协议数据。
     * @param byteBuf               协议数据。
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseProtocol baseProtocol, ByteBuf byteBuf) {
        byteBuf.writeBytes(self.baseProtocolParser.encode(baseProtocol));
    }

    /**
     * 自动装配协议解析器。
     *
     * @param baseProtocolParser 协议解析器。
     */
    @Autowired
    public void setBaseProtocolParser(BaseProtocolParser baseProtocolParser) {
        this.baseProtocolParser = baseProtocolParser;
    }

    /**
     * 协议解析器。
     */
    private BaseProtocolParser baseProtocolParser;

}
