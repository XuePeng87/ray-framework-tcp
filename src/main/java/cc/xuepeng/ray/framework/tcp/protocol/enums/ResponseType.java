package cc.xuepeng.ray.framework.tcp.protocol.enums;

/**
 * 新能源GB32960协议的响应标识枚举。
 *
 * @author xuepeng
 */
public enum ResponseType {

    /**
     * 成功标识。
     */
    SUCCESS((short) 1, "成功"),
    /**
     * 错误标识。
     */
    FAILURE((short) 2, "错误"),
    /**
     * VIN重复标识。
     */
    VIN_REPEAT((short) 3, "VIN重复"),
    /**
     * 命令包标识。
     */
    COMMAND((short) 254, "表示数据包为命令包");

    /**
     * 根据ID获取枚举。
     *
     * @param id 类型ID。
     * @return 枚举。
     */
    public static ResponseType findById(final Short id) {
        for (ResponseType enums : ResponseType.values()) {
            if (enums.getId() == id) {
                return enums;
            }
        }
        return null;
    }

    /**
     * 构造函数。
     *
     * @param id   命令ID。
     * @param desc 命令描述。
     */
    ResponseType(final Short id, final String desc) {
        this.id = id;
        this.desc = desc;
    }

    /**
     * @return 获取命令ID。
     */
    public short getId() {
        return id;
    }

    /**
     * @return 获取命令描述。
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 命令ID。
     */
    private final Short id;
    /**
     * 命令描述。
     */
    private final String desc;

}
