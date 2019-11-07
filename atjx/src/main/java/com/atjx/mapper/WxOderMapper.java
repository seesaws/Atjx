package com.atjx.mapper;

import com.atjx.model.Address;
import com.atjx.model.WxOrder;

import java.util.List;

public interface WxOderMapper {

    int insert(WxOrder wxOrder);

    int update(WxOrder wxOrder);

    void delete(Integer order_id);


    List<Address> selectAll(Integer item_id);
}
