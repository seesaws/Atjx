package com.atjx.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Item extends BaseObject {
    //商品ID
    private int id;
    //商品标题
    private String title;
    //商品详情
    private String sell_Point;
    //商品价格
    private Integer price;
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
    private Integer del_price;
    private int sales;
    private BigDecimal money;
    private List<Address> address;
    private List<Specification> specification;

}
