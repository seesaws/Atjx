package com.atjx.mapper;

import com.atjx.model.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by jiangyunxiong on 2018/3/10.
 */
@Mapper
public interface ItemMapper {

    Item findById(Item item);

    Item findMaxId();

    void delete(Item item);

    List<Item> list(Item item);

    List<Item> listS(Item item);

    int count(Item item);

    int insert(Item item);

    int update(Item item);


    List<Item> selectAll();

}
