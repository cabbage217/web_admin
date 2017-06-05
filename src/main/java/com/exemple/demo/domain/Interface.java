package com.exemple.demo.domain;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * Created by Caby on 2017-05-27 12:31 PM.
 */
public class Interface {
    private Integer id;
    private String name;
    private Integer type;
    private String url;
    private Integer status;
    private Integer orderNumber;
    private Date createTime;
    private Integer nid;
    private String navigationName;

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

    public Integer getOrderNumber() { return this.orderNumber; }
    public void setOrderNumber(Integer orderNumber) { this.orderNumber = orderNumber; }

    public Date getCreateTime() { return this.createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Integer getNid() { return this.nid; }
    public void setNid(Integer nid) { this.nid = nid; }

    public String getNavigationName() { return this.navigationName; }
    public void setNavigationName(String navigationName) { this.navigationName = navigationName; }

    @Override
    public String toString() {
        return (new StringBuilder("Interface{"))
                .append(" id=").append(this.getId())
                .append(", name=").append(this.getName())
                .append(", type=").append(this.getType())
                .append(", url=").append(this.getUrl())
                .append(", status=").append(this.getStatus())
                .append(", orderNumber=").append(this.getOrderNumber())
                .append(", nid=").append(this.getNid())
                .append(", navigationName=").append(this.getNavigationName())
                .append(", createTime=").append(DateFormatUtils.format(this.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                .append(" }")
                .toString();
    }
}
