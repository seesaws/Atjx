package com.atjx.model;

import lombok.Data;

import java.util.Date;


@Data
public class WxOrder {
    private Integer wxorder_id;
    private String payment;
    private Date creat_time;
    private String createdStr;
    private String openid;
    private String username;
    private String phone;
    private String b_describe;
    private String status_code;
    private String order_no;
    private String item_title;
    private String order_desc;
    private Integer spe_id;
    private Integer item_id;
    private String s_opid;
    private Specification specification;


}
