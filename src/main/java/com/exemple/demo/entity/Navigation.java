package com.exemple.demo.entity;

import java.util.Date;

/**
 * Created by Caby on 2017-06-01 3:06 PM.
 */
public class Navigation {
    private Integer id;
    private String name;
    private Integer status;
    private Date createTime;
    private Integer orderNumber;
    private String interfaceIds;

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Integer getStatus() { return this.status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return this.createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Integer getOrderNumber() { return this.orderNumber; }
    public void setOrderNumber(Integer orderNumber) { this.orderNumber = orderNumber; }

    public String getInterfaceIds() { return this.interfaceIds; }
    public void setInterfaceIds(String interfaceIds) { this.interfaceIds = interfaceIds; }
}
