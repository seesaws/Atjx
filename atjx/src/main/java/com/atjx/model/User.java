package com.atjx.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User extends BaseObject implements Serializable {
    private int id;
    private String userName;
    private String password;
    private String realName;
    private String business;
    private String email;
    private String headPicture;
    private Date addDate;
    private Date updateDate;
    private int state;
}
