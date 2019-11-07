package com.atjx.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReItem extends BaseObject {
    private int id;
    private String title;
    private String sell_Point;
    private Integer price;
    private int num;
    private String barcode;
    private String image;
    private int cid;
    private int status;
    private Date recovered;
    private String recoveredStr;
    private Integer del_price;
}