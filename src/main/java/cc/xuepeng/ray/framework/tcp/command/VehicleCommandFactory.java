package cc.xuepeng.ray.framework.tcp.command;

import cc.xuepeng.ray.framework.tcp.protocol.enums.CommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 车辆上行命令工厂类。
 *
 * @author xuepeng
 */
@Component
public class VehicleCommandFactory {

    /**
     * 获取数据单元解析器。
     *
     * @param commandType 命令类型。
     * @return 数据单元解析器。
     */
    public VehicleCommand getInstance(final CommandType commandType) {
        Optional<VehicleCommand> instance = commands
                .stream()
                .filter(s -> s.getCommandType().equals(commandType)).findFirst();
        if (instance.isPresent()) {
            return instance.get();
        } else {
            throw new IllegalArgumentException("请检查传入的发送类型");
        }
    }

    /**
     * 自动装配车辆上行命令。
     *
     * @param commands 车辆上行命令。
     */
    @Autowired
    public void setCommands(List<VehicleCommand> commands) {
        this.commands = commands;
    }

    /**
     * 车辆上行命令。
     */
    private List<VehicleCommand> commands;

}
