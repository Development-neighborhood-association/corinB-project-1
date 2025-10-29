package org.example.wms.repository;

import org.example.wms.entity.ManufacturerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<ManufacturerEntity, Long> {

    // Native Query 상수 정의
    String FIND_BY_PRODUCT_NAME_SQL = """
        SELECT DISTINCT m.* FROM manufacturers m
        LEFT JOIN products p ON m.manufacturer_id = p.manufacturer_id
        WHERE p.name LIKE CONCAT('%', :productName, '%')
        """;

    /**
     * ID로 제조사 조회 (생산 상품 포함)
     * @param id 제조사 ID
     * @return 제조사 정보
     */
    @EntityGraph(attributePaths = {"products"})
    Optional<ManufacturerEntity> findById(Long id);

    /**
     * 전체 제조사 조회 (생산 상품 포함)
     * @param pageable 페이징 정보
     * @return 제조사 목록
     */
    @EntityGraph(attributePaths = {"products"})
    Page<ManufacturerEntity> findAll(Pageable pageable);

    /**
     * 회사명으로 검색
     * @param companyName 회사명
     * @param pageable 페이지정보
     * @return 제조사 목록
     */
    Page<ManufacturerEntity> findByCompanyNameContaining(String companyName, Pageable pageable);

    /**
     * 이메일로 제조사 조회 (정확한 일치)
     * @param email 이메일
     * @return 제조사 정보
     */
    Optional<ManufacturerEntity> findByEmail(String email);

    /**
     * 연락처로 제조사 조회 (정확한 일치)
     * @param contact 연락처
     * @return 제조사 정보
     */
    Optional<ManufacturerEntity> findByContact(String contact);

    /**
     * 이메일 검색
     * @param email 이메일
     * @param pageable 페이지정보
     * @return 회사 정보
     */
    Page<ManufacturerEntity> findByEmailContaining(String email, Pageable pageable);

    /**
     * 연락처 검색
     * @param contact 연락처
     * @param pageable 페이지정보
     * @return 회사정보
     */
    Page<ManufacturerEntity> findByContactContaining(String contact, Pageable pageable);

    /**
     * 위치 검색
     * @param location 위치
     * @param pageable 페이지정보
     * @return 회사정보
     */
    Page<ManufacturerEntity> findByLocationContaining(String location, Pageable pageable);

    /**
     * 제품명으로 제조사 검색
     * @param productName 제품명
     * @param pageable 페이지정보
     * @return 회사정보
     */
    @Query(value = FIND_BY_PRODUCT_NAME_SQL, nativeQuery = true)
    Page<ManufacturerEntity> findByProductsNameContaining(
            @Param("productName") String productName,
            Pageable pageable
    );
}