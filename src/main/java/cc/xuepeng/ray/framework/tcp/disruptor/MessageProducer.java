package cc.xuepeng.ray.framework.tcp.disruptor;

import cc.xuepeng.ray.framework.tcp.protocol.entity.BaseProtocol;
import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Disruptor生产者。
 *
 * @author xuepeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageProducer {

    /**
     * 发送事件。
     *
     * @param baseProtocol 新能源GB32960数据。
     */
    public void publish(final BaseProtocol baseProtocol) {
        long sequence = ringBuffer.next();
        try {
            BaseProtocolWapper wapper = ringBuffer.get(sequence);
            wapper.setBaseProtocol(baseProtocol);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 生产者ID。
     */
    private String producerId;

    /**
     * RingBuffer对象。
     */
    private RingBuffer<BaseProtocolWapper> ringBuffer;

}
