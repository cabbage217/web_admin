package com.exemple.demo.sevice;

import com.exemple.demo.domain.Navigation;

import java.util.List;

/**
 * Created by Caby on 2017-06-01 3:22 PM.
 */
public interface INavigationService {
    /**
     * 获取全部导航栏列表
     * @return List<Navigation>
     */
    List<Navigation> allNavigations();

    /**
     * 获取所有导航栏
     * @param pageNo Integer, 分页数
     * @return List<Navigation>
     */
    List<Navigation> navigationList(Integer pageNo);

    /**
     * 获取导航栏总数量
     * @return Integer
     */
    Integer navigationCount();

    /**
     * 获取已启用的所有导航栏
     * @return List<Navigation>
     */
    List<Navigation> navigationsEnabled();

    /**
     * 获取所有在前面页面中显示的导航栏
     * @param uid Integer, 用户id
     * @return List<Navigation>
     */
    List<Navigation> navigationsShowInView(Integer uid);

    /**
     * 添加导航栏
     * @param name String, 导航栏名称
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return Integer, 新记录的导航栏id
     */
    Integer addNavigation(String name, Integer orderNumber, Integer status);

    /**
     * 更新导航栏
     * @param nid Integer, 导航栏id
     * @param name String, 导航栏名称
     * @param orderNumber orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return boolean
     */
    boolean updateNavigation(Integer nid, String name, Integer orderNumber, Integer status);

    /**
     * 删除导航栏
     * @param nid Integer, 要删除的导航栏id
     * @return boolean
     */
    boolean deleteNavigation(Integer nid);
}
