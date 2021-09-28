package cc.xuepeng.ray.framework.tcp.server.handler;

import cc.xuepeng.ray.framework.tcp.protocol.parser.BaseProtocolParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Message的框架默认解码器。
 * 将数据按照GB32960协议进行解析。
 *
 * @author xuepeng
 */
@Slf4j
@Component
public class DefaultMessageDecodeHandler extends ByteToMessageDecoder {

    /**
     * 将当前类托管到Sprint IOC，所以声明该变量。
     */
    private static DefaultMessageDecodeHandler self;

    /**
     * 通过@PostConstruct注解将当前类托管到Sprint IOC。
     */
    @PostConstruct
    public synchronized void init() {
        self = this;
    }

    /**
     * 协议解码。
     *
     * @param channelHandlerContext ChannelHandlerContext对象。
     * @param byteBuf               协议数据。
     * @param list                  输出集合。
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        list.add(self.baseProtocolParser.decode(byteBuf));
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
