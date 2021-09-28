package cc.xuepeng.ray.framework.tcp.server.ctx;

import cc.xuepeng.ray.framework.tcp.util.ContextUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 框架默认的TCP连接管理器。
 * 使用一个ConcurrentHashMap管理连接到服务端的连接。
 *
 * @author xuepeng
 */
@Component
@Slf4j
public class DefaultTcpConnectionContextManager implements TcpConnectionContextManager<TcpConnectionContext> {

    /**
     * VIN与Channel的映射关系。
     */
    private final Map<String, Channel> vinChannelMap = new ConcurrentHashMap<>();

    /**
     * Channel与VIN的映射关系。
     */
    private final Map<ChannelId, String> channelVinMap = new ConcurrentHashMap<>();

    /**
     * 添加连接到管理器中。
     *
     * @param tcpConnectionContext TCP链接对象。
     */
    @Override
    public void addConnection(final TcpConnectionContext tcpConnectionContext) {
        final String vin = tcpConnectionContext.getVin();
        final Channel channel = tcpConnectionContext.getChannel();
        if (vinChannelMap.containsKey(vin)) {
            log.warn("车架号 {} 已经在管理器中，本次操作会覆盖当前连接。", vin);
        }
        if (channelVinMap.containsKey(channel.id())) {
            log.warn("连接 {} 已经在管理器中，本次操作会覆盖当前连接。", channel.id());
        }
        vinChannelMap.put(vin, channel);
        channelVinMap.put(channel.id(), vin);
        tcpConnectionContext.online();
    }

    /**
     * 根据IP从连接管理器中移除连接。
     *
     * @param tcpConnectionContext TCP链接对象。
     */
    @Override
    public void removeConnection(final TcpConnectionContext tcpConnectionContext) {
        final String vin = tcpConnectionContext.getVin();
        final Channel channel = tcpConnectionContext.getChannel();
        if (!vinChannelMap.containsKey(vin)) {
            log.warn("车架号 {} 不在管理器中，无法从管理器中移除。", vin);
        }
        if (!channelVinMap.containsKey(channel.id())) {
            log.warn("连接 {} 不在管理器中，无法从管理器中移除。", channel.id());
        }
        vinChannelMap.remove(vin);
        channelVinMap.remove(tcpConnectionContext.getChannel().id());
        tcpConnectionContext.offline();
    }

    /**
     * 断开连接时从连接管理器中移除。
     *
     * @param tcpConnectionContext TCP链接对象。
     */
    @Override
    public void breakConnection(final TcpConnectionContext tcpConnectionContext) {
        final ChannelId channelId = tcpConnectionContext.getChannel().id();
        final String vin = channelVinMap.get(channelId);
        tcpConnectionContext.setVin(vin);
        removeConnection(tcpConnectionContext);
        if (log.isDebugEnabled()) {
            log.debug("客户端 {} 与服务器断开了连接。", ContextUtil.getClientIp(tcpConnectionContext.getCtx()));
        }
    }

}
