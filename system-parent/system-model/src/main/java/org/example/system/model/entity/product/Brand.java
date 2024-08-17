package org.example.system.model.entity.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.system.model.entity.base.BaseEntity;

@Data
@Schema(description = "品牌实体类")
public class Brand extends BaseEntity {

	@Schema(description = "品牌名称")
	private String name;

	@Schema(description = "品牌logo")
	private String logo;

}