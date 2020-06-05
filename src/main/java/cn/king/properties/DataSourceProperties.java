package cn.king.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: wjl@king.cn
 * @time: 2020/6/4 21:36
 * @version: 1.0.0
 * @description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "my-datasource")
public class DataSourceProperties {

    /**
     * 主库
     */
    private Map<String, String> masterDataSource;

    /**
     * 从库
     */
    private List<Map<String, String>> slaveDataSources;

    /**
     * 从库的访问策略
     */
    private String visitStrategy;

}
