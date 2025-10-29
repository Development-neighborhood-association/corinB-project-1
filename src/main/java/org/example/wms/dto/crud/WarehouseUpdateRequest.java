package org.example.wms.dto.crud;

import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * 창고 수정 요청 DTO
 * null인 필드는 수정하지 않음 (부분 수정 지원)
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WarehouseUpdateRequest {

    private String name;

    private String location;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
            message = "올바른 전화번호 형식이 아닙니다 (예: 02-1234-5678, 010-1234-5678)")
    private String contact;
}