package org.example.wms.dto.info;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.wms.entity.ManufacturerEntity;
import org.example.wms.util.IdEncryptionUtil;

/**
 * 제조사 상세 정보 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"email", "contact"})
@ToString
public class ManufacturerInfoDTO {
    private String manufacturerId;

    @NotBlank
    private String companyName;

    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
            message = "올바른 전화번호 형식이 아닙니다 (예: 02-1234-5678, 010-1234-5678)")
    private String contact;

    private String location;

    /**
     * Entity → DTO 변환 (ID 암호화)
     */
    public static ManufacturerInfoDTO of(ManufacturerEntity manufacturer, IdEncryptionUtil idEncryptionUtil) {
        return ManufacturerInfoDTO.builder()
                .manufacturerId(idEncryptionUtil.encrypt(manufacturer.getManufacturerId()))
                .companyName(manufacturer.getCompanyName())
                .email(manufacturer.getEmail())
                .contact(manufacturer.getContact())
                .location(manufacturer.getLocation())
                .build();
    }
}