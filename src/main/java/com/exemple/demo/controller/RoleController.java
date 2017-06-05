package com.exemple.demo.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.exemple.demo.Const.SqlParam;
import com.exemple.demo.domain.Result;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.sevice.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Caby on 2017-05-31 7:18 AM.
 */

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    /**
     * 获取所有角色
     * @param pageNo Integer, 分页娄
     * @return Result
     */
    @GetMapping("/list")
    public Result roleList(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("roleCount", roleService.roleCount());
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", SqlParam.PageSize);
        resultMap.put("roleList", roleService.roleList(pageNo));
        return new Result<>(ResultEnum.SUCCESS, resultMap);
    }

    @GetMapping("all")
    public Result allRoles() {
        return new Result<>(ResultEnum.SUCCESS, roleService.allRoles());
    }

    /**
     * 获取所有已启用的角色
     * @return Result
     */
    @GetMapping("/enabled")
    public Result rolesEnabled() {
        return new Result<>(ResultEnum.SUCCESS, roleService.rolesEnabled());
    }

    /**
     * 获取接口所属的角色列表
     * @return Result
     */
    @GetMapping("/interfaceRoles")
    public Result rolesInterfaceUsing(@RequestParam("iid") Integer iid) {
        return new Result<>(ResultEnum.SUCCESS, roleService.rolesInterfaceUsing(iid));
    }

    /**
     * 添加角色
     * @param name String, 角色名
     * @param des String, 描述
     * @return Result
     */
    @PutMapping("/add")
    public Result addRole(@RequestParam("name") String name,
                          @RequestParam(value = "des", required = false, defaultValue = "") String des,
                          @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {
        return new Result(roleService.addRole(name, des, status) > 0 ? ResultEnum.SUCCESS : ResultEnum.ERR_UNKNOWN);
    }

    /**
     * 更新角色
     * @param rid Integer, 角色id
     * @param name String, 角色名
     * @param des String, 描述
     * @return Result
     */
    @PutMapping("/update")
    public Result updateRole(@RequestParam("rid") Integer rid, @RequestParam("name") String name,
                                   @RequestParam(value = "des", required = false, defaultValue = "") String des,
                             @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {
        return new Result(roleService.updateRole(rid, name, des, status) ? ResultEnum.SUCCESS : ResultEnum.ERR_UNKNOWN);
    }

    /**
     * 删除角色
     * @param body String, 请求体，格式为：rid=xx, 其中xx是要删除的角色的id
     * @return Result
     */
    @DeleteMapping("/del")
    public Result deleteRole(@RequestBody String body) { // RequestParam does not work!
        List<String> list = Splitter.on("=").splitToList(body);
        Integer rid = Integer.parseInt(list.get(1));
        return new Result(roleService.deleteRole(rid) ? ResultEnum.SUCCESS : ResultEnum.ERR_UNKNOWN);
    }
}
