package com.exemple.demo.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.domain.Result;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.sevice.INavigationService;
import com.exemple.demo.utils.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Caby on 2017-06-01 3:38 PM.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/navigation")
public class NavigationController {
    private static Logger logger = LoggerFactory.getLogger(NavigationController.class);

    @Autowired
    private INavigationService navigationService;

    /**
     * 分页获取导航栏列表
     * @param pageNo Integer, 分页
     * @return Result
     */
    @GetMapping("/list")
    public Result allNavigations(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("navigationCount", navigationService.navigationCount());
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", SqlParam.PageSize);
        resultMap.put("navigationList", navigationService.navigationList(pageNo));
        return new Result<>(ResultEnum.SUCCESS, resultMap);
    }

    /**
     * 已启用的导航栏列表
     * @return Result
     */
    @GetMapping("/enabled")
    public Result navigationsEnabled() {
        return new Result<>(ResultEnum.SUCCESS, navigationService.navigationsEnabled());
    }

    /**
     * 根据用户获取页面中显示的导航栏
     * @param request HttpServletRequest
     * @return Result
     */
    @GetMapping("/navShow")
    public Result navigationsShowInView(HttpServletRequest request) {
        return new Result<>(ResultEnum.SUCCESS, navigationService.navigationsShowInView(SessionUtil.getUidWithRequest(request)));
    }

    /**
     * 添加导航栏
     * @param name String, 导航栏名称
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return Result
     */
    @PutMapping("/add")
    public Result addNavigation(@RequestParam("name") String name,
                                @RequestParam(value = "orderNumber", required = false, defaultValue = "0") Integer orderNumber,
                                @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {
        return new Result(
                navigationService.addNavigation(name, orderNumber, status) > 0
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 更新导航栏
     * @param nid Integer, 导航栏id
     * @param name String, 导航栏名称
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return Result
     */
    @PutMapping("/update")
    public Result updateNavigation(@RequestParam("nid") Integer nid, @RequestParam("name") String name,
                                   @RequestParam(value = "orderNumber", required = false, defaultValue = "0") Integer orderNumber,
                                   @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {
        return new Result(
                navigationService.updateNavigation(nid, name, orderNumber, status)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN);
    }

    /**
     * 删除导航栏
     * @param body String, 请求体, RequestParam不起作用
     * @return Result
     */
    @DeleteMapping("/del")
    public Result deleteNavigation(@RequestBody String body) { // RequestParam does not work!
        List<String> list = Splitter.on("=").splitToList(body);
        Integer nid = Integer.parseInt(list.get(1));
        return new Result(navigationService.deleteNavigation(nid) ? ResultEnum.SUCCESS : ResultEnum.ERR_UNKNOWN);
    }
}
