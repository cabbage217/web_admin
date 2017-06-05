package com.exemple.demo.domain;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Caby on 2017-05-28 1:50 PM.
 */
public class User {
    private Integer id;
    private String username;
    private Integer status;
    private Date createTime;
    private List<Role> roles;

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getStatus() { return this.status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return this.createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public List<Role> getRoles() { return this.roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }

    @Override
    public String toString() {
        return (new StringBuilder("{")).append(" id=").append(this.getId())
                .append(", username=").append(this.getUsername())
                .append(", status=").append(this.getStatus())
                .append(", createTime=").append(DateFormatUtils.format(this.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                .append(", roles=").append(this.getRoles().toString())
                .append(" }")
                .toString();
    }
}
