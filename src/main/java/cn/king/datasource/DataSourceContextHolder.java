package cn.king.datasource;

import cn.king.enumeration.DataSourceType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: wjl@king.cn
 * @time: 2020/6/4 21:58
 * @version: 1.0.0
 * @description:
 */
@Component
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    public static String getDataSource() {
        String dataSourceType = contextHolder.get();
        if (StringUtils.isEmpty(dataSourceType)) {
            dataSourceType = DataSourceType.MASTER.getName();
        }
        return dataSourceType;
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }

}
