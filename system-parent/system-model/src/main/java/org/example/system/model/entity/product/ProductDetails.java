package org.example.system.model.entity.product;

import lombok.Data;
import org.example.system.model.entity.base.BaseEntity;

@Data
public class ProductDetails extends BaseEntity {

	private Long productId;
	private String imageUrls;

}