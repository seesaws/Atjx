package com.atjx.mobile.model;

import java.io.Serializable;

/**
 * @Classname JsApiTicket
 * @Description 封装jsapi_ticket的实体
 * @Date 2019/11/22 15:01
 * @Created by Administrator
 */

public class JsApiTicket implements Serializable {
    /**ticket的值*/
    private String ticket;
    /**ticket的有效时间*/
    private int expireIn;

    public String getTicket() {
        return ticket;
    }
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
    public int getExpireIn() {
        return expireIn;
    }
    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }
}

