package com.exemple.demo.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.domain.Result;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.sevice.IInterfaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Caby on 2017-05-26 4:42 PM.
 */

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/interface")
public class InterfaceController {
    private static Logger logger = LoggerFactory.getLogger(InterfaceController.class);
    @Autowired
    private IInterfaceService interfaceService;

    /**
     * 分页获取接口列表
     * @param pageNo Integer, 分页
     * @return Result
     */
    @GetMapping("/list")
    public Result allInterface(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("interfaceCount", interfaceService.interfaceCount());
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", SqlParam.PageSize);
        resultMap.put("interfaceList", interfaceService.interfaceList(pageNo));
        return new Result<>(ResultEnum.SUCCESS, resultMap);
    }

    /**
     * 添加接口
     * @param name String, 接口名
     * @param url String, 接口地址
     * @param type Integer, 接口类型
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return Result
     */
    @PutMapping("/add")
    public Result addInterface(@RequestParam("name") String name, @RequestParam("url") String url,
                               @RequestParam("type") Integer type,
                               @RequestParam(value = "orderNumber", required = false, defaultValue = "0") Integer orderNumber,
                               @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {

        return new Result(
                interfaceService.addInterface(name, url, type, orderNumber, status)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 删除接口
     * @param body String, 请求体, RequestParam不起作用
     * @return Result
     */
    @DeleteMapping("/del")
    public Result deleteInterface(@RequestBody String body) { // RequestParam does not work!
        List<String> list = Splitter.on("=").splitToList(body);
        Integer iid = Integer.parseInt(list.get(1));
        return new Result(
                interfaceService.deleteInterface(iid)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 更新接口
     * @param iid Integer, 接口id
     * @param name String, 接口名
     * @param url String, 接口地址
     * @param type Integer, 接口类型
     * @param orderNumber Integer, 排序权重
     * @param status Integer, 状态
     * @return Result
     */
    @PutMapping("/update") // no update method
    public Result updateInterface(@RequestParam("iid") Integer iid, @RequestParam("name") String name,
                                  @RequestParam("url") String url, @RequestParam("type") Integer type,
                                  @RequestParam(value = "orderNumber", required = false, defaultValue = "0") Integer orderNumber,
                                  @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {

        return new Result(
                interfaceService.updateInterface(iid, name, url, type, orderNumber, status)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 设置接口导航栏
     * @param iid Integer, 接口id
     * @param nid Integer, 导航栏id
     * @return Result
     */
    @PutMapping("/setNav")
    public Result setNavigation(@RequestParam("iid") Integer iid, @RequestParam("nid") Integer nid) {
        return new Result(
                interfaceService.setNavigation(iid, nid)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }

    /**
     * 设置接口角色
     * @param iid Integer, 接口id
     * @param roleIds List<Integer>, 角色id列表
     * @return Result
     */
    @PutMapping("/setRoles")
    public Result setRoles(@RequestParam("iid") Integer iid,
                           @RequestParam(value = "roleIds[]", required = false) List<Integer> roleIds) {
        return new Result(
                interfaceService.setRoles(iid, roleIds)
                        ? ResultEnum.SUCCESS
                        : ResultEnum.ERR_UNKNOWN
        );
    }
}
