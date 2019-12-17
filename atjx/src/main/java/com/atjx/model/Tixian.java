package com.atjx.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname Tixian
 * @Description TODO
 * @Date 2019/12/6 9:30
 * @Created by Administrator
 */

@Data
public class Tixian extends BaseObject {
    private Integer id;
    private String  t_openid;
    private BigDecimal t_money;
    private String t_nickname;
    private Date creatTime;
    private String createStr;
    private String t_status;
}