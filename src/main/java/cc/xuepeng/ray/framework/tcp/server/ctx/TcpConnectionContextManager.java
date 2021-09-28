package cc.xuepeng.ray.framework.tcp.server.ctx;

/**
 * TCP连接管理器。
 *
 * @author xuepeng
 */
public interface TcpConnectionContextManager<T extends TcpConnectionContext> {

    /**
     * 添加连接到管理器中。
     *
     * @param tcpConnectionContext TCP链接对象。
     */
    void addConnection(final T tcpConnectionContext);

    /**
     * 根据IP从连接管理器中移除连接。
     *
     * @param tcpConnectionContext TCP链接对象。
     */
    void removeConnection(final T tcpConnectionContext);

    /**
     * 断开连接时从连接管理器中移除。
     *
     * @param tcpConnectionContext TCP链接对象。
     */
    void breakConnection(final T tcpConnectionContext);

}
