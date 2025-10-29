package org.example.wms.dto.crud;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * 제조사 생성 요청 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ManufacturerCreateRequest {

    @NotBlank(message = "회사명은 필수입니다")
    private String companyName;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "연락처는 필수입니다")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
            message = "올바른 전화번호 형식이 아닙니다 (예: 02-1234-5678, 010-1234-5678)")
    private String contact;

    @NotBlank(message = "위치는 필수입니다")
    private String location;
}