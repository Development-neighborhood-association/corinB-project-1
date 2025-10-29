package org.example.wms.dto.list;

import lombok.*;
import org.example.wms.entity.ProductEntity;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.data.domain.Page;

/**
 * 상품 목록 조회용 DTO
 * 간소화된 정보만 포함
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"productId"})
public class ProductListDTO {
    private String productId;  // 암호화된 ID
    private String name;
    private Double price;
    private String manufacturerName;

    /**
     * Entity → DTO 변환 (ID 암호화)
     */
    public static ProductListDTO of(ProductEntity product, IdEncryptionUtil encryptionUtil) {
        return ProductListDTO.builder()
                .productId(encryptionUtil.encrypt(product.getProductId()))
                .name(product.getName())
                .price(product.getPrice())
                .manufacturerName(product.getManufacturer().getCompanyName())
                .build();
    }

    public static Page<ProductListDTO> of(Page<ProductEntity> page, IdEncryptionUtil encryptionUtil){
        return page.map(e -> ProductListDTO.of(e, encryptionUtil));
    }

}