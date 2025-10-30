package org.example.wms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.wms.dto.ApiResponse;
import org.example.wms.dto.crud.WarehouseCreateRequest;
import org.example.wms.dto.crud.WarehouseUpdateRequest;
import org.example.wms.dto.info.WarehouseInfoDTO;
import org.example.wms.dto.list.WarehouseListDTO;
import org.example.wms.service.WarehouseService;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 창고 관리 REST API Controller
 * Base URL: /api/warehouses
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final IdEncryptionUtil idEncryptionUtil;

    /**
     * 창고 생성
     * POST /api/warehouses
     *
     * @param request 창고 생성 요청 (name, location, contact, manager)
     * @return 201 Created + 생성된 창고 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<WarehouseInfoDTO>> createWarehouse(
            @Valid @RequestBody WarehouseCreateRequest request) {
        WarehouseInfoDTO created = warehouseService.createWarehouse(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "창고가 생성되었습니다."));
    }

    /**
     * 창고 상세 조회
     * GET /api/warehouses/{encryptedId}
     *
     * @param encryptedId 암호화된 창고 ID
     * @return 200 OK + 창고 상세 정보
     */
    @GetMapping("/{encryptedId}")
    public ResponseEntity<ApiResponse<WarehouseInfoDTO>> getWarehouse(
            @PathVariable String encryptedId) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        WarehouseInfoDTO warehouse = warehouseService.getWarehouse(encryptedId);
        return ResponseEntity.ok(ApiResponse.success(warehouse));
    }

    /**
     * 창고 목록 조회 (페이징)
     * GET /api/warehouses
     *
     * @param pageable 페이지 정보 (page, size, sort)
     * @return 200 OK + 창고 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<WarehouseListDTO>>> getAllWarehouses(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<WarehouseListDTO> warehouses = warehouseService.getAllWarehouses(pageable);
        return ResponseEntity.ok(ApiResponse.success(warehouses));
    }

    /**
     * 창고명으로 검색
     * GET /api/warehouses/search?name=검색어
     *
     * @param name 검색할 창고명
     * @param pageable 페이지 정보
     * @return 200 OK + 검색된 창고 목록
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<WarehouseListDTO>>> searchByName(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<WarehouseListDTO> warehouses = warehouseService.searchByName(name, pageable);
        return ResponseEntity.ok(ApiResponse.success(warehouses));
    }

    /**
     * 위치로 검색
     * GET /api/warehouses/search/by-location?location=검색어
     *
     * @param location 검색할 위치
     * @param pageable 페이지 정보
     * @return 200 OK + 검색된 창고 목록
     */
    @GetMapping("/search/by-location")
    public ResponseEntity<ApiResponse<Page<WarehouseListDTO>>> searchByLocation(
            @RequestParam String location,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<WarehouseListDTO> warehouses = warehouseService.searchByLocation(location, pageable);
        return ResponseEntity.ok(ApiResponse.success(warehouses));
    }

    /**
     * 창고 정보 수정
     * PUT /api/warehouses/{encryptedId}
     *
     * @param encryptedId 암호화된 창고 ID
     * @param request 수정할 정보 (모든 필드 선택적)
     * @return 200 OK + 수정 완료 메시지
     */
    @PutMapping("/{encryptedId}")
    public ResponseEntity<ApiResponse<Void>> updateWarehouse(
            @PathVariable String encryptedId,
            @Valid @RequestBody WarehouseUpdateRequest request) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        warehouseService.updateWarehouse(encryptedId, request);
        return ResponseEntity.ok(ApiResponse.success(null, "창고 정보가 수정되었습니다."));
    }

    /**
     * 창고 삭제
     * DELETE /api/warehouses/{encryptedId}
     *
     * @param encryptedId 암호화된 창고 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{encryptedId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable String encryptedId) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        warehouseService.deleteWarehouse(encryptedId);
        return ResponseEntity.noContent().build();
    }
}