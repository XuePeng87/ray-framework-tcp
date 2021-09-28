package cc.xuepeng.ray.framework.tcp.protocol.enums;

/**
 * 新能源GB32960协议的加密方式枚举。
 *
 * @author xuepeng
 */
public enum EncryptionType {

    /**
     * 不加密。
     */
    NONE((short) 1, "不加密"),
    /**
     * RSA加密。
     */
    RSA((short) 2, "RSA加密"),
    /**
     * AES128加密。
     */
    AES((short) 3, "AES加密"),
    /**
     * 异常。
     */
    ERROR((short) 254, "异常"),
    /**
     * 无效。
     */
    INVAILD((short) 255, "无效");

    /**
     * 根据ID获取枚举。
     *
     * @param id 类型ID。
     * @return 枚举。
     */
    public static EncryptionType findById(final Short id) {
        for (EncryptionType enums : EncryptionType.values()) {
            if (enums.getId() == id) {
                return enums;
            }
        }
        return ERROR;
    }

    /**
     * 构造函数。
     *
     * @param id   命令ID。
     * @param desc 命令描述。
     */
    EncryptionType(Short id, String desc) {
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
