package cc.xuepeng.ray.framework.tcp.util;

import io.netty.channel.ChannelHandlerContext;

/**
 * Netty上下文工具类。
 *
 * @author xuepeng
 */
public class ContextUtil {

    /**
     * 构造函数。
     */
    private ContextUtil() {
    }

    /**
     * 获取客户端IP地址。
     *
     * @param ctx ChannelHandlerContext对象。
     * @return 客户端IP地址。
     */
    public static String getClientIp(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString();
    }

}
