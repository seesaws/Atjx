package com.atjx.mapper;

import com.atjx.model.WxOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface WxOderMapper {

    int insert(WxOrder wxOrder);

    int update(WxOrder wxOrder);

    void delete(Integer order_id);


    List<WxOrder> selectStatus(String status_code,String openid);

    List<WxOrder> findByUser(String openid);
}
