package org.example.wms.dto.list;

import lombok.*;
import org.example.wms.entity.InventoryEntity;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.data.domain.Page;

/**
 * 재고 목록 조회용 DTO
 * 간소화된 정보만 포함
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"inventoryId"})
public class InventoryListDTO {
    private String inventoryId;  // 암호화된 ID
    private String productName;
    private String warehouseName;
    private Integer quantity;
    private String status;  // "충분", "부족", "없음"

    /**
     * Entity → DTO 변환 (ID 암호화)
     */
    public static InventoryListDTO of(InventoryEntity inventory, IdEncryptionUtil encryptionUtil) {
        return InventoryListDTO.builder()
                .inventoryId(encryptionUtil.encrypt(inventory.getInventoryId()))
                .productName(inventory.getProduct().getName())
                .warehouseName(inventory.getWarehouse().getName())
                .quantity(inventory.getQuantity())
                .status(determineStatus(inventory.getQuantity()))
                .build();
    }

    public static Page<InventoryListDTO> of(Page<InventoryEntity> page, IdEncryptionUtil encryptionUtil) {
        return page.map(inventory -> of(inventory, encryptionUtil));
    }


    /**
     * 재고 상태 판정
     */
    private static String determineStatus(Integer quantity) {
        if (quantity == 0) {
            return "없음";
        } else if (quantity <= 10) {
            return "부족";
        } else {
            return "충분";
        }
    }
}