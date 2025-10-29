package org.example.wms.repository;

import org.example.wms.entity.WarehouseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Long> {

    // Native Query 상수 정의
    String FIND_BY_PRODUCT_NAME_SQL = """
        SELECT DISTINCT w.* FROM warehouses w
        LEFT JOIN inventory i ON w.warehouse_id = i.warehouse_id
        LEFT JOIN products p ON i.product_id = p.product_id
        WHERE p.name LIKE CONCAT('%', :productName, '%')
        """;

    String FIND_BY_TOTAL_QUANTITY_SQL = """
        SELECT w.*, SUM(i.quantity) as total_quantity
        FROM warehouses w
        LEFT JOIN inventory i ON w.warehouse_id = i.warehouse_id
        GROUP BY w.warehouse_id
        HAVING total_quantity >= :minQuantity
        """;

    /**
     * ID로 창고 조회 (재고 정보 포함)
     * @param id 창고 ID
     * @return 창고 정보
     */
    @EntityGraph(attributePaths = {"inventories"})
    Optional<WarehouseEntity> findById(Long id);

    /**
     * 전체 창고 조회 (재고 정보 포함)
     * @param pageable 페이징 정보
     * @return 창고 목록
     */
    @EntityGraph(attributePaths = {"inventories"})
    Page<WarehouseEntity> findAll(Pageable pageable);

    /**
     * 창고명으로 검색
     * @param name 창고명 (부분 일치)
     * @param pageable 페이징 정보
     * @return 창고 목록
     */
    Page<WarehouseEntity> findByNameContaining(String name, Pageable pageable);

    /**
     * 위치로 검색
     * @param location 위치 (부분 일치)
     * @param pageable 페이징 정보
     * @return 창고 목록
     */
    Page<WarehouseEntity> findByLocationContaining(String location, Pageable pageable);

    /**
     * 연락처로 검색
     * @param contact 연락처 (부분 일치)
     * @param pageable 페이징 정보
     * @return 창고 목록
     */
    Page<WarehouseEntity> findByContactContaining(String contact, Pageable pageable);

    /**
     * 창고명으로 창고 조회 (정확한 일치)
     * @param name 창고명
     * @return 창고 정보
     */
    Optional<WarehouseEntity> findByName(String name);

    /**
     * 보관 상품명으로 창고 검색
     * @param productName 상품명 (부분 일치)
     * @param pageable 페이징 정보
     * @return 창고 목록
     */
    @Query(value = FIND_BY_PRODUCT_NAME_SQL, nativeQuery = true)
    Page<WarehouseEntity> findByInventoriesProductNameContaining(
            @Param("productName") String productName,
            Pageable pageable
    );

    /**
     * 총 재고량 기준으로 창고 검색
     * @param minQuantity 최소 재고량
     * @param pageable 페이징 정보
     * @return 창고 목록
     */
    @Query(value = FIND_BY_TOTAL_QUANTITY_SQL, nativeQuery = true)
    Page<WarehouseEntity> findByTotalQuantityGreaterThanEqual(
            @Param("minQuantity") Integer minQuantity,
            Pageable pageable
    );
}