package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.Category;
import org.example.system.model.vo.product.CategoryExcelVo;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectCategoryByParentId(Long id);

    int selectCountByParentId(Long id);

    List<Category> findAll();

    void batchInsert(List<CategoryExcelVo> cachedDataList);
}
