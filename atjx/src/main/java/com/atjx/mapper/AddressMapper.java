package com.atjx.mapper;

import com.atjx.model.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AddressMapper {


    int insert(Address address);

    int update(Address address);

    void delete(Integer add_id);

    List<Address> selectAll(Integer item_id);
}
