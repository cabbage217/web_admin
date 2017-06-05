package com.exemple.demo.mapper;

import com.exemple.demo.entity.Navigation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Caby on 2017-06-01 3:13 PM.
 */
public interface NavigationMapper {
    @Select("SELECT n.id, n.name, n.status, n.order_num createTime, n.ctime createTime, GROUP_CONCAT(i.id) interfaceIds " +
            "FROM navigation n " +
            "LEFT JOIN interface i ON n.id = i.nid " +
            "GROUP BY n.id " +
            "ORDER BY n.order_num DESC, n.id, i.order_num DESC, i.id")
    List<Navigation> allNavigations();

    @Select("SELECT n.id, n.`name`, n.`status`, n.ctime createTime, n.order_num orderNumber, GROUP_CONCAT(i.id) interfaceIds " +
            "FROM navigation n " +
            "LEFT JOIN interface i ON n.id = i.nid " +
            "GROUP BY n.id " +
            "ORDER BY n.order_num DESC, n.id, i.order_num DESC, i.id " +
            "LIMIT #{offset}, #{pageSize}")
    List<Navigation> navigationList(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("SELECT COUNT(id) FROM navigation")
    Integer navigationCount();

    @Select("SELECT n.id, n.`name`, n.`status`, n.ctime createTime, n.order_num orderNumber, GROUP_CONCAT(i.id) interfaceIds " +
            "FROM navigation n " +
            "LEFT JOIN interface i ON n.id = i.nid " +
            "WHERE n.status = 0 " +
            "GROUP BY n.id " +
            "ORDER BY n.order_num DESC, n.id, i.order_num DESC, i.id")
    List<Navigation> navigationsEnabled();

    @Select("SELECT n.id, n.`name`, n.`status`, n.ctime createTime, n.order_num orderNumber, GROUP_CONCAT(i.id) interfaceIds " +
            "FROM navigation n " +
            "INNER JOIN interface i ON n.id = i.nid AND i.status = 0 " +
            "INNER JOIN role_interface ri ON i.id = ri.iid AND ri.status = 0 " +
            "INNER JOIN user_role ur ON ri.rid = ur.rid AND ur.status = 0 AND ur.uid = #{uid} " +
            "WHERE n.status = 0 " +
            "GROUP BY n.id " +
            "ORDER BY n.order_num DESC, n.id, i.order_num DESC, i.id")
    List<Navigation> navigationsShowInView(@Param("uid") Integer uid);

    @Insert("INSERT INTO navigation(`name`, `order_num`, `status`) VALUES(#{name}, #{orderNumber}, #{status})")
    Integer addNavigation(@Param("name") String name, @Param("orderNumber") Integer orderNumber, @Param("status") Integer status);

    @Update("UPDATE navigation SET `name` = #{name}, order_num = #{orderNumber}, status = #{status} WHERE id = #{nid}")
    void updateNavigation(@Param("nid") Integer nid, @Param("name") String name, @Param("orderNumber") Integer orderNumber,
                          @Param("status") Integer status);

    @Delete("DELETE FROM navigation WHERE id = #{nid}")
    void deleteNavigation(@Param("nid") Integer nid);
}
