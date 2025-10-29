package org.example.wms.dto.list;

import lombok.*;
import org.example.wms.entity.WarehouseEntity;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.data.domain.Page;

/**
 * 창고 목록 조회용 DTO
 * 간소화된 정보만 포함
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"warehouseId"})
public class WarehouseListDTO {
    private String warehouseId;  // 암호화된 ID
    private String name;
    private String location;
    private String contact;
    private Integer inventoryCount;  // 보관 중인 재고 종류 수

    /**
     * Entity → DTO 변환 (ID 암호화)
     */
    public static WarehouseListDTO of(WarehouseEntity warehouse, IdEncryptionUtil encryptionUtil) {
        return WarehouseListDTO.builder()
                .warehouseId(encryptionUtil.encrypt(warehouse.getWarehouseId()))
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .contact(warehouse.getContact())
                .inventoryCount(warehouse.getInventories().size())
                .build();
    }

    public static Page<WarehouseListDTO> of(Page<WarehouseEntity> page, IdEncryptionUtil encryptionUtil) {
        return page.map(warehouse -> of(warehouse, encryptionUtil));
    }
}