package cc.xuepeng.ray.framework.tcp.protocol.enums;

/**
 * 新能源GB32960协议的命令枚举。
 *
 * @author xuepeng
 */
public enum CommandType {

    /**
     * 时间命令。
     */
    TIME((short) 0, "时间协议"),

    /**
     * 车辆登入命令。
     */
    VEHICLE_LOGIN((short) 1, "车辆登入"),

    /**
     * 车辆登出命令。
     */
    VEHICLE_LOGOUT((short) 2, "车辆登出"),

    /**
     * 实时数据上报命令。
     */
    REALTIME_DATA_REPORTING((short) 3, "实时数据上报"),

    /**
     * 补发数据上报命令。
     */
    REPLACEMENT_DATA_REPORTING((short) 4, "补发数据上报"),

    /**
     * 平台登入命令（发送国家平台使用）。
     */
    PLATFORM_LOGIN((short) 5, "平台登入"),

    /**
     * 平台登出命令（发送国家平台使用）。
     */
    PLATFORM_LOGOUT((short) 6, "平台登出"),

    /**
     * 心跳命令。
     */
    HEARTBEAT((short) 7, "心跳"),

    /**
     * 终端校时命令。
     */
    TERMINAL_CHECK_TIME((short) 8, "终端校时"),

    /**
     * 查询命令。
     */
    QUERY_COMMAND((short) 128, "查询命令"),

    /**
     * 设置命令。
     */
    SET_COMMAND((short) 129, "设置命令"),

    /**
     * 控制命令。
     */
    REMOTE_CONTROL_COMMAND((short) 130, "车载终端控制命令");

    /**
     * 根据ID获取枚举。
     *
     * @param id 类型ID。
     * @return 枚举。
     */
    public static CommandType findById(final Short id) {
        for (CommandType enums : CommandType.values()) {
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
    CommandType(final Short id, final String desc) {
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
