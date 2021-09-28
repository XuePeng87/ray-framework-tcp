package cc.xuepeng.ray.framework.tcp.protocol.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新能源GB32960时间协议。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeProtocol {

    /**
     * 年。
     */
    private short year;
    /**
     * 月。
     */
    private short month;
    /**
     * 日。
     */
    private short day;
    /**
     * 时。
     */
    private short hours;
    /**
     * 分。
     */
    private short minutes;
    /**
     * 秒。
     */
    private short seconds;

}
