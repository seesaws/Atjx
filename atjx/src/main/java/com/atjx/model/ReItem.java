package com.atjx.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReItem extends BaseObject {
    private int id;
    private String title;
    private String sellPoint;
    private BigDecimal price;
    private int num;
    private String barcode;
    private String image;
    private int cid;
    private int status;
    private Date recovered;
    private String recoveredStr;
    private BigDecimal del_price;
}