package com.atjx.mapper;


import com.atjx.model.Item_Pic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PicMapper {

        Item_Pic findById(Item_Pic pic);

        void delete(Item_Pic pic);

        int insert(Item_Pic pic);

        int update(Item_Pic pic);

        List<Item_Pic> selectAll(Integer item_id);






}


