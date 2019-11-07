package com.atjx.model;

import lombok.Data;

import java.util.Date;


@Data
public class WxOrder {
    private int wxoder_id;
    private String payment;
    private Date creat_time;
    private String openid;
    private String nickname;
    private String username;
    private String phone;
    private String desc;
    private String status_code;
    private String order_no;
    private String item_title;
    private String oder_desc;


}
