package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.base.ProductUnit;

import java.util.List;

@Mapper
public interface ProductUnitMapper {
    List<ProductUnit> findAll();
}
