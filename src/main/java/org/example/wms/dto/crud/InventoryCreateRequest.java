package org.example.wms.dto.crud;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

/**
 * 재고 등록 요청 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InventoryCreateRequest {

    @NotNull(message = "상품 ID는 필수입니다")
    private String encryptedProductId;  // 암호화된 상품 ID

    @NotNull(message = "창고 ID는 필수입니다")
    private String encryptedWarehouseId;  // 암호화된 창고 ID

    @NotNull(message = "수량은 필수입니다")
    @PositiveOrZero(message = "수량은 0 이상이어야 합니다")
    private Integer quantity;
}