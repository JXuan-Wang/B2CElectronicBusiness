package org.example.system.manager.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.system.model.entity.product.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    //分类列表，每次查询一层数据
    List<Category> findCategoryList(Long id);

    //导出数据
    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
