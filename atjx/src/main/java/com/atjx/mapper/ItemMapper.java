package com.atjx.mapper;

import com.atjx.model.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ItemMapper {

    Item findById(Item item);

    Integer findMaxId();

    void delete(Item item);

    List<Item> list(Item item);

    List<Item> listS(Item item);

    int count(Item item);

    int insert(Item item);

    int update(Item item);


    List<Item> selectAll();

    Item findAllInfo(Integer id );
    Integer getItemPage();
}
