package org.example.wms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.wms.dto.ApiResponse;
import org.example.wms.dto.crud.InventoryCreateRequest;
import org.example.wms.dto.crud.StockInRequest;
import org.example.wms.dto.crud.StockOutRequest;
import org.example.wms.dto.info.InventoryInfoDTO;
import org.example.wms.dto.list.InventoryListDTO;
import org.example.wms.service.InventoryService;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 재고 관리 REST API Controller
 * Base URL: /api/inventories
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;
    private final IdEncryptionUtil idEncryptionUtil;

    /**
     * 초기 재고 등록
     * POST /api/inventories
     *
     * @param request 재고 생성 요청 (encryptedProductId, encryptedWarehouseId, quantity)
     * @return 201 Created + 생성된 재고 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InventoryInfoDTO>> createInventory(
            @Valid @RequestBody InventoryCreateRequest request) {
        InventoryInfoDTO created = inventoryService.createInventory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "재고가 등록되었습니다."));
    }

    /**
     * 재고 상세 조회
     * GET /api/inventories/{encryptedId}
     *
     * @param encryptedId 암호화된 재고 ID
     * @return 200 OK + 재고 상세 정보
     */
    @GetMapping("/{encryptedId}")
    public ResponseEntity<ApiResponse<InventoryInfoDTO>> getInventory(
            @PathVariable String encryptedId) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        InventoryInfoDTO inventory = inventoryService.getInventory(encryptedId);
        return ResponseEntity.ok(ApiResponse.success(inventory));
    }

    /**
     * 전체 재고 목록 조회 (페이징)
     * GET /api/inventories
     *
     * @param pageable 페이지 정보 (page, size, sort)
     * @return 200 OK + 재고 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<InventoryListDTO>>> getAllInventories(
            @PageableDefault(size = 10, sort = "quantity") Pageable pageable) {
        Page<InventoryListDTO> inventories = inventoryService.getAllInventories(pageable);
        return ResponseEntity.ok(ApiResponse.success(inventories));
    }

    /**
     * 상품별 재고 조회
     * GET /api/inventories/by-product/{encryptedProductId}
     *
     * @param encryptedProductId 암호화된 상품 ID
     * @param pageable 페이지 정보
     * @return 200 OK + 해당 상품의 재고 목록 (여러 창고에 분산된 재고)
     */
    @GetMapping("/by-product/{encryptedProductId}")
    public ResponseEntity<ApiResponse<Page<InventoryListDTO>>> getInventoriesByProduct(
            @PathVariable String encryptedProductId,
            @PageableDefault(size = 10, sort = "quantity") Pageable pageable) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedProductId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        Page<InventoryListDTO> inventories = inventoryService.getInventoriesByProduct(encryptedProductId, pageable);
        return ResponseEntity.ok(ApiResponse.success(inventories));
    }

    /**
     * 창고별 재고 조회
     * GET /api/inventories/by-warehouse/{encryptedWarehouseId}
     *
     * @param encryptedWarehouseId 암호화된 창고 ID
     * @param pageable 페이지 정보
     * @return 200 OK + 해당 창고의 재고 목록 (여러 상품들)
     */
    @GetMapping("/by-warehouse/{encryptedWarehouseId}")
    public ResponseEntity<ApiResponse<Page<InventoryListDTO>>> getInventoriesByWarehouse(
            @PathVariable String encryptedWarehouseId,
            @PageableDefault(size = 10, sort = "quantity") Pageable pageable) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedWarehouseId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        Page<InventoryListDTO> inventories = inventoryService.getInventoriesByWarehouse(encryptedWarehouseId, pageable);
        return ResponseEntity.ok(ApiResponse.success(inventories));
    }

    /**
     * 재고 입고
     * POST /api/inventories/stock-in
     *
     * @param request 입고 요청 (encryptedInventoryId, quantity)
     * @return 200 OK + 입고 완료 메시지
     */
    @PostMapping("/stock-in")
    public ResponseEntity<ApiResponse<Void>> stockIn(
            @Valid @RequestBody StockInRequest request) {
        inventoryService.stockIn(request);
        return ResponseEntity.ok(ApiResponse.success(null, "재고가 입고되었습니다."));
    }

    /**
     * 재고 출고
     * POST /api/inventories/stock-out
     *
     * @param request 출고 요청 (encryptedInventoryId, quantity)
     * @return 200 OK + 출고 완료 메시지
     */
    @PostMapping("/stock-out")
    public ResponseEntity<ApiResponse<Void>> stockOut(
            @Valid @RequestBody StockOutRequest request) {
        inventoryService.stockOut(request);
        return ResponseEntity.ok(ApiResponse.success(null, "재고가 출고되었습니다."));
    }

    /**
     * 재고 삭제
     * DELETE /api/inventories/{encryptedId}
     *
     * @param encryptedId 암호화된 재고 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{encryptedId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable String encryptedId) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        inventoryService.deleteInventory(encryptedId);
        return ResponseEntity.noContent().build();
    }
}