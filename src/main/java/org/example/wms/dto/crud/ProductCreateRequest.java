package org.example.wms.dto.crud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * 상품 생성 요청 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductCreateRequest {

    @NotBlank(message = "상품명은 필수입니다")
    private String name;

    @NotNull(message = "가격은 필수입니다")
    @Positive(message = "가격은 0보다 커야 합니다")
    private Double price;

    private String description;

    @NotNull(message = "제조사 ID는 필수입니다")
    private String encryptedManufacturerId;  // 암호화된 제조사 ID
}