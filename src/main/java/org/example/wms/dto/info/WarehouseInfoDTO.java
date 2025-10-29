package org.example.wms.dto.info;

import lombok.*;
import org.example.wms.entity.WarehouseEntity;
import org.example.wms.util.IdEncryptionUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 창고 상세 정보 DTO
 * 재고 정보 포함
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"warehouseId"})
public class WarehouseInfoDTO {
    private String warehouseId;  // 암호화된 ID
    private String name;
    private String location;
    private String contact;
    private Integer totalInventoryCount;  // 보관 중인 재고 종류 수
    private List<SimpleInventoryInfo> inventories;  // 간단한 재고 정보

    /**
     * 간단한 재고 정보 (내부 클래스)
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class SimpleInventoryInfo {
        private String productName;
        private Integer quantity;
    }

    /**
     * Entity → DTO 변환 (ID 암호화)
     */
    public static WarehouseInfoDTO of(WarehouseEntity warehouse, IdEncryptionUtil encryptionUtil) {
        List<SimpleInventoryInfo> inventoryInfos = warehouse.getInventories().stream()
                .map(inventory -> SimpleInventoryInfo.builder()
                        .productName(inventory.getProduct().getName())
                        .quantity(inventory.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return WarehouseInfoDTO.builder()
                .warehouseId(encryptionUtil.encrypt(warehouse.getWarehouseId()))
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .contact(warehouse.getContact())
                .totalInventoryCount(inventoryInfos.size())
                .inventories(inventoryInfos)
                .build();
    }
}