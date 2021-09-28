package cc.xuepeng.ray.framework.tcp.disruptor;

import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Disruptor发送的数据实体。
 *
 * @author xuepeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProtocolWapper {

    /**
     * 新能源GB32960协议。
     */
    private BaseProtocol baseProtocol;

}
