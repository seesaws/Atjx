package com.atjx.mapper;

import com.atjx.model.Specification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SpecificationMapper {



    int insert(Specification spe);

    int update(Specification spe);

    void delete(Integer spe_id);


    List<Specification> selectAll(Integer item_id);
}
