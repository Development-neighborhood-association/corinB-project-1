package org.example.wms.dto.info;

import lombok.*;
import org.example.wms.entity.InventoryEntity;
import org.example.wms.util.IdEncryptionUtil;

import java.time.LocalDateTime;

/**
 * 재고 상세 정보 DTO
 * 상품명, 창고명 포함
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"inventoryId"})
public class InventoryInfoDTO {
    private String inventoryId;  // 암호화된 ID
    private String productId;    // 암호화된 상품 ID
    private String productName;
    private String warehouseId;  // 암호화된 창고 ID
    private String warehouseName;
    private String warehouseLocation;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Entity → DTO 변환 (ID 암호화)
     */
    public static InventoryInfoDTO of(InventoryEntity inventory, IdEncryptionUtil encryptionUtil) {
        return InventoryInfoDTO.builder()
                .inventoryId(encryptionUtil.encrypt(inventory.getInventoryId()))
                .productId(encryptionUtil.encrypt(inventory.getProduct().getProductId()))
                .productName(inventory.getProduct().getName())
                .warehouseId(encryptionUtil.encrypt(inventory.getWarehouse().getWarehouseId()))
                .warehouseName(inventory.getWarehouse().getName())
                .warehouseLocation(inventory.getWarehouse().getLocation())
                .quantity(inventory.getQuantity())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}