package cn.king.aspect;


import cn.king.annotation.DataSource;
import cn.king.datasource.DataSourceContextHolder;
import cn.king.enumeration.DataSourceType;
import cn.king.properties.DataSourceProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: wjl@king.cn
 * @time: 2020/5/13 17:04
 * @version: 1.0.0
 * @description:
 */
@Aspect
@Component
public class DataSourceAspect {

    @Resource
    private DataSourceProperties dataSourceProperties;

    /**
     * 计数器
     */
    private AtomicInteger counter = new AtomicInteger(-1);

    private static final String RANDOM_STRATEGY = "random";
    private static final String ROUND_ROBIN_STRATEGY = "roundRobin";

    @Pointcut("@annotation(cn.king.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    /**
     * @author: wjl@king.cn
     * @createTime: 2020/5/29 17:49
     * @param: point
     * @return: java.lang.Object
     * @description: 环绕通知。根据方法上的注解动态切换数据源。
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            // 获取当前执行的方法
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            // 获取方法上的@DataSource注解
            DataSource dataSource = method.getAnnotation(DataSource.class);
            String dataSourceName = dataSource.value().getName();
            // 切换数据源
            if (DataSourceType.MASTER.getName().equals(dataSourceName)) {
                DataSourceContextHolder.setDataSource(DataSourceType.MASTER.getName());
            }
            if (DataSourceType.SLAVE.getName().equals(dataSourceName)) {

                // 获取从库访问策略
                String visitStrategy = dataSourceProperties.getVisitStrategy();

                if (StringUtils.isEmpty(visitStrategy) || RANDOM_STRATEGY.equals(visitStrategy)) {
                    DataSourceContextHolder.setDataSource(randomSlaveKey());
                }

                if (StringUtils.isEmpty(visitStrategy) || ROUND_ROBIN_STRATEGY.equals(visitStrategy)) {
                    DataSourceContextHolder.setDataSource(roundRobinSlaveKey());
                }

            }

            // 调用目标方法，返回目标方法的返回值
            return point.proceed();

        } finally {
            // 清空数据源类型
            DataSourceContextHolder.clearDataSource();
        }
    }

    /**
     * @author: wjl@king.cn
     * @createTime: 2020/6/5 0:34
     * @param:
     * @return: java.lang.String
     * @description: 轮询策略
     */
    private String roundRobinSlaveKey() {
        // [0,从库数量-1]
        int size = dataSourceProperties.getSlaveDataSources().size();
        // 先获取再增加
        int index = counter.incrementAndGet() % size;
        if (counter.get() > 9999) {
            counter.set(-1);
        }
        for (int i = 0; i < size; i++) {
            if (index == i) {
                return DataSourceType.SLAVE.getName() + i;
            }
        }

        return randomSlaveKey();
    }

    /**
     * @author: wjl@king.cn
     * @createTime: 2020/6/5 0:34
     * @param:
     * @return: java.lang.String
     * @description: 随机策略
     */
    private String randomSlaveKey() {
        int i = (int) (Math.random() * dataSourceProperties.getSlaveDataSources().size());
        return DataSourceType.SLAVE.getName() + i;
    }

}
