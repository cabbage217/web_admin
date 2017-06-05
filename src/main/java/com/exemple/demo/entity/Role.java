package com.exemple.demo.entity;

import java.util.Date;

/**
 * Created by Caby on 2017-05-26 6:07 PM.
 */

public class Role {
    private Integer id;
    private String name;
    private String des;
    private Integer status;
    private Date createTime;
    private String interfaceIds;

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getDes() { return this.des; }
    public void setDes(String des) { this.des = des; }

    public Integer getStatus() { return this.status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return this.createTime; }
    public void setCreateTime(Date ctime) { this.createTime = ctime; }

    public String getInterfaceIds() { return this.interfaceIds; }
    public void setInterfaceIds(String interfaceIds) { this.interfaceIds = interfaceIds; }
}
