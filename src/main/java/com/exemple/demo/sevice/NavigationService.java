package com.exemple.demo.sevice;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.domain.Interface;
import com.exemple.demo.domain.Navigation;
import com.exemple.demo.mapper.NavigationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Caby on 2017-06-01 3:26 PM.
 */
@Service
public class NavigationService implements INavigationService {
    @Autowired
    private NavigationMapper navigationMapper;
    @Autowired
    private IInterfaceService interfaceService;

    private Interface findInterfaceByIid(Integer iid, List<Interface> interfaceList) {
        if (interfaceList == null || interfaceList.isEmpty() || iid == null) {
            return null;
        }
        for (Interface oneInterface : interfaceList) {
            if (iid.equals(oneInterface.getId())) {
                return oneInterface;
            }
        }
        return null;
    }

    private List<Navigation> transform(List<com.exemple.demo.entity.Navigation> navigationList) {
        List<Navigation> resultList = Lists.newArrayList();
        if (navigationList == null || navigationList.isEmpty()) {
            return resultList;
        }
        List<Interface> allInterfaces = interfaceService.allInterfaces();
        for (com.exemple.demo.entity.Navigation navigation : navigationList) {
            Navigation one = new Navigation();
            one.setId(navigation.getId());
            one.setName(navigation.getName());
            one.setStatus(navigation.getStatus());
            one.setOrderNumber(navigation.getOrderNumber());
            one.setCreateTime(navigation.getCreateTime());
            if (one.getInterfaceList() == null) {
                one.setInterfaceList(Lists.newArrayList());
            }
            if (!Strings.isNullOrEmpty(navigation.getInterfaceIds())) {
                List<String> interfaceIds = Splitter.on(',')
                        .trimResults()
                        .omitEmptyStrings()
                        .splitToList(navigation.getInterfaceIds());
                for (String idString : interfaceIds) {
                    Integer iid = Integer.parseInt(idString);
                    if (this.findInterfaceByIid(iid, one.getInterfaceList()) == null) { // 尚未加入导航栏下面的接口列表
                        Interface interfaceFound = this.findInterfaceByIid(iid, allInterfaces);
                        if (interfaceFound != null) {
                            one.getInterfaceList().add(interfaceFound);
                        }
                    }
                }
            }

            resultList.add(one);
        }
        return resultList;
    }

    /**
     * 获取全部导航栏列表
     * @return List<Navigation>
     */
    @Override
    public List<Navigation> allNavigations() {
        return this.transform(navigationMapper.allNavigations());
    }

    /**
     * 获取所有导航栏
     * @param pageNo Integer, 分页数
     * @return List<Navigation>
     */
    @Override
    public List<Navigation> navigationList(Integer pageNo) {
        if (pageNo == null) {
            pageNo = 0;
        }
        return this.transform(navigationMapper.navigationList(pageNo * SqlParam.PageSize, SqlParam.PageSize));
    }

    /**
     * 获取导航栏总数量
     * @return Integer
     */
    @Override
    public Integer navigationCount() {
        return navigationMapper.navigationCount();
    }

    /**
     * 获取已启用的所有导航栏
     * @return List<Navigation>
     */
    @Override
    public List<Navigation> navigationsEnabled() {
        return this.transform(navigationMapper.navigationsEnabled());
    }

    /**
     * 获取所有在前面页面中显示的导航栏
     * @param uid Integer, 用户id
     * @return List<Navigation>
     */
    public List<Navigation> navigationsShowInView(Integer uid) {
        return this.transform(navigationMapper.navigationsShowInView(uid));
    }

    /**
     * 添加导航栏
     * @param name String, 导航栏名称
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return Integer, 新记录的导航栏id
     */
    @Override
    public Integer addNavigation(String name, Integer orderNumber, Integer status) {
        return navigationMapper.addNavigation(name, orderNumber, status);
    }

    /**
     * 更新导航栏
     * @param nid Integer, 导航栏id
     * @param name String, 导航栏名称
     * @param orderNumber orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return boolean
     */
    @Override
    public boolean updateNavigation(Integer nid, String name, Integer orderNumber, Integer status) {
        navigationMapper.updateNavigation(nid, name, orderNumber, status);
        return true;
    }

    /**
     * 删除导航栏
     * @param nid Integer, 要删除的导航栏id
     * @return boolean
     */
    @Override
    public boolean deleteNavigation(Integer nid) {
        navigationMapper.deleteNavigation(nid);
        return true;
    }
}
