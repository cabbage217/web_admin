package com.exemple.demo.entity;

import java.util.Date;

/**
 * Created by Caby on 2017-05-26 5:44 PM.
 */
public class Interface {
    private Integer id;
    private String name;
    private Integer type;
    private String url;
    private Integer status;
    private Date createTime;
    private Integer orderNumber;
    private Integer nid;
    private String navigationName;
    private Integer navigationOrderNumber;
    private Integer navigationStatus;
    private Date navigationCreateTime;

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Integer getType() { return this.type; }
    public void setType(Integer type) { this.type = type; }

    public String getUrl() { return this.url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getStatus() { return this.status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return this.createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Integer getOrderNumber() { return this.orderNumber; }
    public void setOrderNumber(Integer orderNumber) { this.orderNumber = orderNumber; }

    public Integer getNid() { return this.nid; }
    public void setNid(Integer nid) { this.nid = nid; }

    public String getNavigationName() { return this.navigationName; }
    public void setNavigationName(String navigationName) { this.navigationName = navigationName; }

    public Integer getNavigationOrderNumber() { return this.navigationOrderNumber; }
    public void setNavigationOrderNumber(Integer navigationOrderNumber) { this.navigationOrderNumber = navigationOrderNumber; }

    public Integer getNavigationStatus() { return this.navigationStatus; }
    public void setNavigationStatus(Integer navigationStatus) { this.navigationStatus = navigationStatus; }

    public Date getNavigationCreateTime() { return this.navigationCreateTime; }
    public void setNavigationCreateTime(Date navigationCreateTime) { this.navigationCreateTime = navigationCreateTime; }
}
