package org.example.wms.dto.crud;

import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * 상품 수정 요청 DTO
 * null인 필드는 수정하지 않음 (부분 수정 지원)
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductUpdateRequest {

    private String name;

    @Positive(message = "가격은 0보다 커야 합니다")
    private Double price;

    private String description;

    private String encryptedManufacturerId;  // 암호화된 제조사 ID
}