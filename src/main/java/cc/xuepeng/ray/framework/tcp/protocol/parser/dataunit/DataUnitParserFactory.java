package cc.xuepeng.ray.framework.tcp.protocol.parser.dataunit;

import cc.xuepeng.ray.framework.tcp.protocol.enums.CommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 数据单元解析器工厂。
 *
 * @author xuepeng
 */
@Component
public class DataUnitParserFactory {

    /**
     * 获取数据单元解析器。
     *
     * @param commandType 命令类型。
     * @return 数据单元解析器。
     */
    public DataUnitParser getInstance(final CommandType commandType) {
        Optional<DataUnitParser> instance = dataUnitParsers
                .stream()
                .filter(s -> s.getCommandType().equals(commandType)).findFirst();
        if (instance.isPresent()) {
            return instance.get();
        } else {
            throw new IllegalArgumentException("请检查传入的发送类型");
        }
    }


    /**
     * 自动装配数据单元解析器。
     *
     * @param dataUnitParsers 数据单元解析器。
     */
    @Autowired
    public void setDataUnitParsers(List<DataUnitParser> dataUnitParsers) {
        this.dataUnitParsers = dataUnitParsers;
    }

    /**
     * 数据单元解析器。
     */
    private List<DataUnitParser> dataUnitParsers;

}
