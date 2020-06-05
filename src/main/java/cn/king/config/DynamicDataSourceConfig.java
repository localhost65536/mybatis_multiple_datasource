package cn.king.config;


import cn.king.datasource.DynamicDataSource;
import cn.king.enumeration.DataSourceType;
import cn.king.properties.DataSourceProperties;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多数据源配置信息
 *
 * @author: wjl@king.cn
 * @time: 2020/2/10 9:47
 * @since: 1.0
 */
@Configuration
public class DynamicDataSourceConfig {

    @Resource
    private DataSourceProperties dataSourceProperties;

    // 配置主数据源
    @Bean
    public DataSource masterDataSource() throws Exception {
        Map<String, String> masterDataSource = dataSourceProperties.getMasterDataSource();
        return DruidDataSourceFactory.createDataSource(masterDataSource);
    }

    // 配置从数据源
    @Bean
    public List<DataSource> slaveDataSources() {
        List<Map<String, String>> slaveDataSources = dataSourceProperties.getSlaveDataSources();
        List<DataSource> dataSources = new ArrayList<>();
        slaveDataSources.forEach(slaveDataSource -> {
            try {
                dataSources.add(DruidDataSourceFactory.createDataSource(slaveDataSource));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return dataSources;
    }

    // 配置动态数据源
    @Bean
    // 有多个同类型Bean时，优先注入带有@Primary注解的Bean。必须指定。因为DynamicDataSource也是一个DataSource.
    @Primary
    // 该注解用于声明当前bean依赖于另外一个bean。所依赖的bean会被容器确保在当前bean实例化之前被实例化.
    // 该注解用于解决此处循环依赖报错
    @DependsOn({"masterDataSource", "slaveDataSources"})
    public DynamicDataSource dataSource(@Autowired @Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Autowired @Qualifier("slaveDataSources") List<DataSource> slaveDataSources) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER.getName(), masterDataSource);
        for (int i = 0; i < slaveDataSources.size(); i++) {
            targetDataSources.put(DataSourceType.SLAVE.getName() + i, slaveDataSources.get(i));
        }
        return new DynamicDataSource(targetDataSources, masterDataSource);
    }

}
