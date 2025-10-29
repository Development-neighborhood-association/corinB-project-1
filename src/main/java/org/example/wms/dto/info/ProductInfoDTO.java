package org.example.wms.dto.info;

import lombok.*;
import org.example.wms.entity.ProductEntity;
import org.example.wms.util.IdEncryptionUtil;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"name","price","manufacturer"})
public class ProductInfoDTO {
    private String productId;
    public String name;
    public String description;
    public Double price;
    public String manufacturer;

    public static ProductInfoDTO of(ProductEntity product, IdEncryptionUtil encryptionUtil) {
        return ProductInfoDTO.builder()
                .productId(encryptionUtil.encrypt(product.getProductId()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .manufacturer(product.getManufacturer().getCompanyName())
                .build();
    }
}
