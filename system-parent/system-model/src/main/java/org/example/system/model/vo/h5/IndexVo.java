package org.example.system.model.vo.h5;


import lombok.Data;
import org.example.system.model.entity.product.Category;
import org.example.system.model.entity.product.ProductSku;

import java.util.List;

@Data
public class IndexVo {

    private List<Category> categoryList ;       // 一级分类的类别数据
    private List<ProductSku> productSkuList ;   // 畅销商品列表数据

}