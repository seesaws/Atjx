package com.atjx.model;

import lombok.Data;

import java.util.Date;

@Data
public class Item extends BaseObject {
    private int id;
    private String title;
    private String sellPoint;
    private int price;
    private int num;
    private String barcode;
    private String image;
    private int cid;
    private int status;
    private Date created;
    private Date updated;
    private String createdStr;
    private String updatedStr;
    private String categoryName;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer minNum;
    private Integer maxNum;
    private int del_price;
    private int sales;
    private int money;
    private Address address;
    private Specification specification;

}
