package com.exemple.demo.domain;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Caby on 2017-05-31 9:22 AM.
 */

public class Role {
    private Integer id;
    private String name;
    private String des;
    private Integer status;
    private Date createTime;
    private List<Interface> interfaceList;

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

    public List<Interface> getInterfaceList() { return this.interfaceList; }
    public void setInterfaceList(List<Interface> interfaceList) { this.interfaceList = interfaceList; }

    @Override
    public String toString() {
        return (new StringBuilder("Role{"))
                .append(" id=").append(this.getId())
                .append(", name=").append(this.getName())
                .append(", des=").append(this.getDes())
                .append(", status=").append(this.getStatus())
                .append(", createTime=").append(DateFormatUtils.format(this.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                .append(", interfaceList=").append(this.getInterfaceList())
                .append(" }")
                .toString();
    }
}
