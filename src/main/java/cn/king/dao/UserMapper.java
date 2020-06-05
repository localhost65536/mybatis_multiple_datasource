package cn.king.dao;


import cn.king.annotation.DataSource;
import cn.king.entity.User;
import cn.king.enumeration.DataSourceType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: wjl@king.cn
 * @time: 2020/5/13 16:19
 * @version: 1.0.0
 * @description:
 */
public interface UserMapper {

    @DataSource(DataSourceType.MASTER)
    @Insert("insert into tb_user ( `name` ) values ( #{name} )")
    boolean addUser(User user);



    @DataSource(DataSourceType.SLAVE)
    @Select("select * from tb_user")
    List<User> findAll();

}
