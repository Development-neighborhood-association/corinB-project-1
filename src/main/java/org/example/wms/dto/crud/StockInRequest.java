package org.example.wms.dto.crud;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * 입고 처리 요청 DTO
 * 기존 재고 수량 증가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockInRequest {

    @NotNull(message = "재고 ID는 필수입니다")
    private String encryptedInventoryId;  // 암호화된 재고 ID

    @NotNull(message = "입고 수량은 필수입니다")
    @Positive(message = "입고 수량은 0보다 커야 합니다")
    private Integer quantity;

    private String reason;  // 입고 사유 (선택사항)
}