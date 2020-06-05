package cn.king;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: wjl@king.cn
 * @time: 2020/6/4 20:23
 * @version: 1.0.0
 * @description:
 */
@MapperScan(basePackages = "cn.king.dao")
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(App.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
        System.out.println("启动成功 <================================");
    }
}
