package org.example.system.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.system.model.entity.base.BaseEntity;

@Data
@Schema(description = "商品搜索条件实体类")
public class ProductDto extends BaseEntity {

    @Schema(description = "品牌id")
    private Long brandId;

    @Schema(description = "一级分类id")
    private Long category1Id;

    @Schema(description = "二级分类id")
    private Long category2Id;

    @Schema(description = "三级分类id")
    private Long category3Id;

}
