package com.exemple.demo.mapper;

import com.exemple.demo.DAOProvider.MainDAOProvider;
import com.exemple.demo.entity.Interface;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Caby on 2017-05-27 10:44 AM.
 */
public interface InterfaceMapper {
    /**
     * 获取所有接口数量
     * @return Integer
     */
    @Select("SELECT COUNT(id) FROM interface")
    Integer interfaceCount();

    /**
     * 获取所有接口列表
     * @return List<Interface>
     */
    @Select("SELECT i.id, i.name, i.url, i.order_num orderNumber, i.nid, i.status, i.type, i.ctime createTime, " +
            "    n.name navigationName, n.order_num navigationOrderNumber, n.status navigationStatus, " +
            "    n.ctime navigationCreateTime " +
            "FROM interface i " +
            "LEFT JOIN navigation n ON i.nid = n.id " +
            "ORDER BY n.order_num DESC, n.id, i.order_num DESC, i.id")
    List<Interface> allInterfaces();

    /**
     * 分页获取所有接口
     * @param offset Integer，分页数据起始偏移，不包含此位置的数据
     * @param pageSize Integer, 分页大小
     * @return List<Interface>
     */
    @Select("SELECT i.id, i.name, i.url, i.order_num orderNumber, i.nid, i.status, i.type, i.ctime createTime, " +
            "    n.name navigationName, n.order_num navigationOrderNumber, n.status navigationStatus, " +
            "    n.ctime navigationCreateTime " +
            "FROM interface i " +
            "LEFT JOIN navigation n ON i.nid = n.id " +
            "ORDER BY n.order_num DESC, n.id, i.order_num DESC, i.id " +
            "LIMIT #{offset}, #{pageSize}")
    List<Interface> interfaceList(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    /**
     * 添加接口
     * @param name String, 接口名称
     * @param url String, 接口url
     * @param type Integer, 接口类型
     * @param orderNumber Integer, 排序权重
     * @return Integer
     */
    @Insert("INSERT INTO interface (`name`, url, `type`, order_num, `status`) " +
            "VALUE (#{name}, #{url}, #{type}, #{orderNumber}, #{status})")
    Integer addInterface(@Param("name") String name, @Param("url") String url, @Param("type") Integer type,
                         @Param("orderNumber") Integer orderNumber, @Param("status") Integer status);

    /**
     * 删除接口
     * @param iid Integer, 接口id
     */
    @Delete("DELETE FROM interface WHERE id = #{iid}")
    void deleteInterface(@Param("iid") Integer iid);

    /**
     * 更新接口
     * @param iid Integer, 接口id
     * @param name String, 接口名称
     * @param url String, 接口url
     * @param type Integer, 接口类型
     * @param orderNumber Integer, 排序权重
     */
    @Update("UPDATE interface SET `name` = #{name}, `url` = #{url}, `type` = #{type}, " +
            "   `order_num` = #{orderNumber}, `status` = #{status} " +
            "WHERE id = #{iid}")
    void updateInterface(@Param("iid") Integer iid, @Param("name") String name, @Param("url") String url,
                         @Param("type") Integer type, @Param("orderNumber") Integer orderNumber,
                         @Param("status") Integer status);

    /**
     * 设置接口所属的导航栏
     * @param iid Integer, 接口id
     * @param nid Integer, 导航栏id
     */
    @Update("UPDATE interface SET nid = #{nid} WHERE id = #{iid}")
    void setNavigation(@Param("iid") Integer iid, @Param("nid") Integer nid);

    /**
     * 删除指定接口与所有角色的关系
     * @param iid Integer, 接口id
     */
    @Delete("DELETE FROM role_interface WHERE iid = #{iid}")
    void deleteAllInterfaceRoles(@Param("iid") Integer iid);

    /**
     * 设置指定接口的角色
     * @param iid Integer, 接口id
     * @param roleIds List<Integer> 角色id列表
     */
    @InsertProvider(type = MainDAOProvider.class, method = "addRolesForInterface")
    void addRolesForInterface(Integer iid, List<Integer> roleIds);

    @Select("SELECT COUNT(1) `count` FROM user_role ur "  +
            "INNER JOIN user u ON ur.uid = u.id AND u.status = 0 AND ur.uid = #{uid} " +
            "INNER JOIN (" +
            "   SELECT ri.rid FROM interface i " +
            "   INNER JOIN role_interface ri ON i.status = 0 AND i.`url` = #{uri} " +
            "       AND i.id = ri.iid AND ri.status = 0 " +
            "   INNER JOIN role r ON ri.rid = r.id AND r.status = 0 " +
            ") t ON ur.rid = t.rid AND ur.status = 0 ")
    Integer isUserHasPermissionToInterface(@Param("uid") Integer uid, @Param("uri") String uri);
}
