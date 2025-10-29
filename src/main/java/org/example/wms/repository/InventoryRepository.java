package org.example.wms.repository;

import org.example.wms.entity.InventoryEntity;
import org.example.wms.entity.ProductEntity;
import org.example.wms.entity.WarehouseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    // Native Query 상수 정의
    String FIND_LOW_STOCK_SQL = """
        SELECT i.* FROM inventory i
        WHERE i.quantity <= :threshold
        ORDER BY i.quantity ASC
        """;

    String FIND_TOTAL_QUANTITY_BY_PRODUCT_SQL = """
        SELECT SUM(i.quantity) FROM inventory i
        WHERE i.product_id = :productId
        """;

    String FIND_AVAILABLE_WAREHOUSES_SQL = """
        SELECT DISTINCT w.* FROM warehouses w
        INNER JOIN inventory i ON w.warehouse_id = i.warehouse_id
        WHERE i.product_id = :productId AND i.quantity > 0
        """;

    /**
     * ID로 재고 조회 (상품 및 창고 정보 포함)
     * @param id 재고 ID
     * @return 재고 정보
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Optional<InventoryEntity> findById(Long id);

    /**
     * 전체 재고 조회 (상품 및 창고 정보 포함)
     * @param pageable 페이징 정보
     * @return 재고 목록
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<InventoryEntity> findAll(Pageable pageable);

    /**
     * 상품별 재고 조회
     * @param product 상품 엔티티
     * @param pageable 페이징 정보
     * @return 재고 목록
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<InventoryEntity> findByProduct(ProductEntity product, Pageable pageable);

    /**
     * 창고별 재고 조회
     * @param warehouse 창고 엔티티
     * @param pageable 페이징 정보
     * @return 재고 목록
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<InventoryEntity> findByWarehouse(WarehouseEntity warehouse, Pageable pageable);

    /**
     * 상품 ID로 재고 조회
     * @param productId 상품 ID
     * @param pageable 페이징 정보
     * @return 재고 목록
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<InventoryEntity> findByProductProductId(Long productId, Pageable pageable);

    /**
     * 창고 ID로 재고 조회
     * @param warehouseId 창고 ID
     * @param pageable 페이징 정보
     * @return 재고 목록
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<InventoryEntity> findByWarehouseWarehouseId(Long warehouseId, Pageable pageable);

    /**
     * 특정 상품의 특정 창고 재고 조회
     * @param productId 상품 ID
     * @param warehouseId 창고 ID
     * @return 재고 정보
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Optional<InventoryEntity> findByProductProductIdAndWarehouseWarehouseId(
            Long productId,
            Long warehouseId
    );

    /**
     * 수량 이상인 재고 조회
     * @param quantity 최소 수량
     * @param pageable 페이징 정보
     * @return 재고 목록
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<InventoryEntity> findByQuantityGreaterThanEqual(Integer quantity, Pageable pageable);

    /**
     * 수량 이하인 재고 조회
     * @param quantity 최대 수량
     * @param pageable 페이징 정보
     * @return 재고 목록
     */
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<InventoryEntity> findByQuantityLessThanEqual(Integer quantity, Pageable pageable);

    /**
     * 재고 부족 목록 조회 (낮은 재고부터 정렬)
     * @param threshold 임계값
     * @param pageable 페이징 정보
     * @return 재고 목록
     */
    @Query(value = FIND_LOW_STOCK_SQL, nativeQuery = true)
    Page<InventoryEntity> findLowStock(
            @Param("threshold") Integer threshold,
            Pageable pageable
    );

    /**
     * 상품의 전체 재고 수량 조회
     * @param productId 상품 ID
     * @return 총 재고 수량
     */
    @Query(value = FIND_TOTAL_QUANTITY_BY_PRODUCT_SQL, nativeQuery = true)
    Integer getTotalQuantityByProduct(@Param("productId") Long productId);

    /**
     * 특정 상품을 보유한 창고 목록 조회 (재고 있는 창고만)
     * @param productId 상품 ID
     * @param pageable 페이징 정보
     * @return 창고 목록
     */
    @Query(value = FIND_AVAILABLE_WAREHOUSES_SQL, nativeQuery = true)
    Page<WarehouseEntity> findAvailableWarehousesByProduct(
            @Param("productId") Long productId,
            Pageable pageable
    );
}