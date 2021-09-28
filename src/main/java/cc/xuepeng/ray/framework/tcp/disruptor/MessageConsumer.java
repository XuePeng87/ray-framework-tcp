package cc.xuepeng.ray.framework.tcp.disruptor;

import com.lmax.disruptor.WorkHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Disruptor消费者。
 *
 * @author xuepeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MessageConsumer implements WorkHandler<BaseProtocolWapper> {

    /**
     * 消费事件。
     *
     * @param baseProtocolWapper Disruptor发送的数据实体。
     */
    @Override
    public void onEvent(final BaseProtocolWapper baseProtocolWapper) {
        log.info(baseProtocolWapper.getBaseProtocol().getVin());
    }

    /**
     * 消费者ID。
     */
    private String consumerId;

}
