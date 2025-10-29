package org.example.wms.dto.list;

import lombok.*;
import org.example.wms.entity.ManufacturerEntity;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.data.domain.Page;

/**
 * 제조사 목록 조회용 DTO
 * 간소화된 정보만 포함
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"manufacturerId"})
public class ManufacturerListDTO {
    private String manufacturerId;  // 암호화된 ID
    private String companyName;
    private String location;
    private String contact;

    /**
     * Entity → DTO 변환 (ID 암호화)
     */
    public static ManufacturerListDTO of(ManufacturerEntity manufacturer, IdEncryptionUtil encryptionUtil) {
        return ManufacturerListDTO.builder()
                .manufacturerId(encryptionUtil.encrypt(manufacturer.getManufacturerId()))
                .companyName(manufacturer.getCompanyName())
                .location(manufacturer.getLocation())
                .contact(manufacturer.getContact())
                .build();
    }

    public static Page<ManufacturerListDTO> of(Page<ManufacturerEntity> page, IdEncryptionUtil encryptionUtil){
        return page.map(e -> ManufacturerListDTO.of(e, encryptionUtil));
    }
}