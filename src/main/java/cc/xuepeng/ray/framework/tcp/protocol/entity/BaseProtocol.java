package cc.xuepeng.ray.framework.tcp.protocol.entity;

import cc.xuepeng.ray.framework.tcp.protocol.enums.CommandType;
import cc.xuepeng.ray.framework.tcp.protocol.enums.EncryptionType;
import cc.xuepeng.ray.framework.tcp.protocol.enums.ResponseType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新能源GB32960协议。
 *
 * @author xuepeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProtocol {

    /**
     * 命令枚举。
     */
    private CommandType commandType;
    /**
     * 响应标识枚举。
     */
    private ResponseType responseType;
    /**
     * 车架号。
     */
    private String vin;
    /**
     * 加密方式。
     */
    private EncryptionType encryptionType;
    /**
     * 数据单元长度
     */
    private int length;
    /**
     * 数据单元内容。
     */
    private ByteBuf data;
    /**
     * BCC校验。
     */
    private boolean bcc = Boolean.TRUE;

    //------------------------------
    // 非国标数据
    //------------------------------

    /**
     * 协议头固定字符
     */
    public static final String BEGIN_CHAR = "##";
    /**
     * 协议头校验。
     */
    private boolean begin = Boolean.TRUE;
    /**
     * 数据单元内容JSON格式。
     */
    private String jsonData;


}
