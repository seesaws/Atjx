package com.atjx.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItem extends BaseObject implements Serializable {
    private String id;

    private String itemId;

    private String orderId;

    private Integer num;

    private String title;

    private Long price;

    private Long totalFee;

    private String picPath;
}