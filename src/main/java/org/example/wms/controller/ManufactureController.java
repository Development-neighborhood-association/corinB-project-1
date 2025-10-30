package org.example.wms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.wms.dto.ApiResponse;
import org.example.wms.dto.crud.ManufacturerCreateRequest;
import org.example.wms.dto.crud.ManufacturerUpdateRequest;
import org.example.wms.dto.info.ManufacturerInfoDTO;
import org.example.wms.dto.list.ManufacturerListDTO;
import org.example.wms.service.ManufacturerService;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 제조사 관리 REST API Controller
 * Base URL: /api/manufacturers
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manufacturers")
public class ManufactureController {
    private final ManufacturerService manufacturerService;
    private final IdEncryptionUtil idEncryptionUtil;

    /**
     * 제조사 생성
     * POST /api/manufacturers
     *
     * @param request 제조사 생성 요청 (companyName, email, contact, location)
     * @return 201 Created + 생성된 제조사 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ManufacturerInfoDTO>> createManufacturer(
            @Valid @RequestBody ManufacturerCreateRequest request) {
        ManufacturerInfoDTO created = manufacturerService.createManufacturer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "제조사가 생성되었습니다."));
    }

    /**
     * 제조사 상세 조회
     * GET /api/manufacturers/{encryptedId}
     *
     * @param encryptedId 암호화된 제조사 ID
     * @return 200 OK + 제조사 상세 정보
     */
    @GetMapping("/{encryptedId}")
    public ResponseEntity<ApiResponse<ManufacturerInfoDTO>> getManufacturer(
            @PathVariable String encryptedId) {
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        ManufacturerInfoDTO manufacturer = manufacturerService.getManufacturer(encryptedId);
        return ResponseEntity.ok(ApiResponse.success(manufacturer));
    }

    /**
     * 제조사 목록 조회 (페이징)
     * GET /api/manufacturers
     *
     * @param pageable 페이지 정보 (page, size, sort)
     * @return 200 OK + 제조사 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ManufacturerListDTO>>> getAllManufacturers(
            @PageableDefault(size = 10, sort = "companyName") Pageable pageable) {
        Page<ManufacturerListDTO> manufacturers = manufacturerService.getAllManufacturers(pageable);
        return ResponseEntity.ok(ApiResponse.success(manufacturers));
    }

    /**
     * 회사명으로 검색
     * GET /api/manufacturers/search?companyName=검색어
     *
     * @param companyName 검색할 회사명
     * @param pageable 페이지 정보
     * @return 200 OK + 검색된 제조사 목록
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ManufacturerListDTO>>> searchByCompanyName(
            @RequestParam String companyName,
            @PageableDefault(size = 10, sort = "companyName") Pageable pageable) {
        Page<ManufacturerListDTO> manufacturers = manufacturerService.searchByCompanyName(companyName, pageable);
        return ResponseEntity.ok(ApiResponse.success(manufacturers));
    }

    /**
     * 제조사 정보 수정
     * PUT /api/manufacturers/{encryptedId}
     *
     * @param encryptedId 암호화된 제조사 ID
     * @param request 수정할 정보 (모든 필드 선택적)
     * @return 200 OK + 수정 완료 메시지
     */
    @PutMapping("/{encryptedId}")
    public ResponseEntity<ApiResponse<Void>> updateManufacturer(
            @PathVariable String encryptedId,
            @Valid @RequestBody ManufacturerUpdateRequest request) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        manufacturerService.updateManufacturer(encryptedId, request);
        return ResponseEntity.ok(ApiResponse.success(null, "제조사 정보가 수정되었습니다."));
    }

    /**
     * 제조사 삭제
     * DELETE /api/manufacturers/{encryptedId}
     *
     * @param encryptedId 암호화된 제조사 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{encryptedId}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable String encryptedId) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        manufacturerService.deleteManufacturer(encryptedId);
        return ResponseEntity.noContent().build();
    }
}