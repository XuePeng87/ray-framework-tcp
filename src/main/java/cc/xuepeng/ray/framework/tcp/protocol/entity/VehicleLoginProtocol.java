package cc.xuepeng.ray.framework.tcp.protocol.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 新能源GB32960车辆登入协议。
 *
 * @author xuepeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLoginProtocol {

    /**
     * 采集时间。
     */
    private TimeProtocol time = new TimeProtocol();
    /**
     * 登入流水号。
     */
    private int serialNum;
    /**
     * ICCID。
     */
    private String iccid;
    /**
     * 可充电储能子系统个数。
     */
    private short count;
    /**
     * 可充电储能子系统编码长度。
     */
    private short length;
    /**
     * 可充电储能子系统编码集合。
     */
    private List<String> codes;

}
