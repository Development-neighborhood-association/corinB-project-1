package org.example.wms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.wms.dto.ApiResponse;
import org.example.wms.dto.crud.ProductCreateRequest;
import org.example.wms.dto.crud.ProductUpdateRequest;
import org.example.wms.dto.info.ProductInfoDTO;
import org.example.wms.dto.list.ProductListDTO;
import org.example.wms.service.ProductService;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 상품 관리 REST API Controller
 * Base URL: /api/products
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final IdEncryptionUtil idEncryptionUtil;

    /**
     * 상품 생성
     * POST /api/products
     *
     * @param request 상품 생성 요청 (name, price, description, encryptedManufacturerId)
     * @return 201 Created + 생성된 상품 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductInfoDTO>> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        ProductInfoDTO created = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "상품이 생성되었습니다."));
    }

    /**
     * 상품 상세 조회
     * GET /api/products/{encryptedId}
     *
     * @param encryptedId 암호화된 상품 ID
     * @return 200 OK + 상품 상세 정보
     */
    @GetMapping("/{encryptedId}")
    public ResponseEntity<ApiResponse<ProductInfoDTO>> getProduct(
            @PathVariable String encryptedId) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        ProductInfoDTO product = productService.getProduct(encryptedId);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * 상품 목록 조회 (페이징)
     * GET /api/products
     *
     * @param pageable 페이지 정보 (page, size, sort)
     * @return 200 OK + 상품 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductListDTO>>> getAllProducts(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<ProductListDTO> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * 상품명으로 검색
     * GET /api/products/search?name=검색어
     *
     * @param name 검색할 상품명
     * @param pageable 페이지 정보
     * @return 200 OK + 검색된 상품 목록
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductListDTO>>> searchByName(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<ProductListDTO> products = productService.searchByName(name, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * 제조사명으로 상품 검색
     * GET /api/products/search/by-manufacturer?manufacturer=검색어
     *
     * @param manufacturer 검색할 제조사명
     * @param pageable 페이지 정보
     * @return 200 OK + 검색된 상품 목록
     */
    @GetMapping("/search/by-manufacturer")
    public ResponseEntity<ApiResponse<Page<ProductListDTO>>> searchByManufacturer(
            @RequestParam String manufacturer,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<ProductListDTO> products = productService.searchByManufacturerName(manufacturer, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * 가격 범위로 검색
     * GET /api/products/search/by-price-range?minPrice=1000&maxPrice=5000
     *
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param pageable 페이지 정보
     * @return 200 OK + 검색된 상품 목록
     */
    @GetMapping("/search/by-price-range")
    public ResponseEntity<ApiResponse<Page<ProductListDTO>>> searchByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<ProductListDTO> products = productService.searchByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * 상품 정보 수정
     * PUT /api/products/{encryptedId}
     *
     * @param encryptedId 암호화된 상품 ID
     * @param request 수정할 정보 (모든 필드 선택적)
     * @return 200 OK + 수정 완료 메시지
     */
    @PutMapping("/{encryptedId}")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @PathVariable String encryptedId,
            @Valid @RequestBody ProductUpdateRequest request) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        productService.updateProduct(encryptedId, request);
        return ResponseEntity.ok(ApiResponse.success(null, "상품 정보가 수정되었습니다."));
    }

    /**
     * 상품 삭제
     * DELETE /api/products/{encryptedId}
     *
     * @param encryptedId 암호화된 상품 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{encryptedId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String encryptedId) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        productService.deleteProduct(encryptedId);
        return ResponseEntity.noContent().build();
    }
}