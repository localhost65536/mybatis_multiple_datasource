package cn.king.test;


import cn.king.App;
import cn.king.dao.UserMapper;
import cn.king.entity.User;
import cn.king.properties.DataSourceProperties;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author: wjl@king.cn
 * @time: 2020/5/29 17:24
 * @version: 1.0.0
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class Test01 {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test01() {
        for (int i = 0; i < 100; i++) {
            System.out.println(userMapper.findAll());
        }
    }


    @Test
    public void test02() {
        System.out.println(JSONObject.toJSONString(dataSourceProperties));
    }


    @Test
    public void test03() {
        User user = new User();
        user.setName("1111111");
        userMapper.addUser(user);
    }

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Test
    public void test1000() {
        System.out.println(dataSourceProperties.getVisitStrategy());
    }


    @Test
    public void test2000() {
        for (int i = 0; i < 200; i++) {
            System.out.println(i % 8);
        }
    }

}
