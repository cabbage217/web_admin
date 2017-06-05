package com.exemple.demo.domain;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Caby on 2017-05-27 12:11 PM.
 */
public class Navigation {
    private Integer id;
    private String name;
    private Integer status;
    private Integer orderNumber;
    private List<Interface> interfaceList;
    private Date createTime;

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; };

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Integer getStatus() { return this.status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getOrderNumber() { return this.orderNumber; }
    public void setOrderNumber(Integer orderNumber) { this.orderNumber = orderNumber; }

    public List<Interface> getInterfaceList() { return this.interfaceList; }
    public void setInterfaceList(List<Interface> interfaceList) { this.interfaceList = interfaceList; }

    public Date getCreateTime() { return this.createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return (new StringBuilder("Navigation{"))
                .append(" id=").append(this.getId())
                .append(", name=").append(this.getName())
                .append(", status=").append(this.getStatus())
                .append(", orderNumber=").append(this.getOrderNumber())
                .append(", createTime=").append(DateFormatUtils.format(this.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                .append(", interfaceList=").append(this.getInterfaceList())
                .append(" }")
                .toString();
    }
}
