package com.exemple.demo.entity;

import java.util.Date;

public class User {
    private Integer id;
    private String username;
    private String password;
    private Integer status;
    private Date createTime;
    private String roleIds;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id;}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getStatus() { return this.status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return this.createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getRoleIds() { return this.roleIds; }
    public void setRoleIds(String roleIds) { this.roleIds = roleIds; }
}
