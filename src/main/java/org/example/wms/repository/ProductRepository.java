package org.example.wms.repository;

import org.example.wms.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    final String FIND_BY_MANUFACTURER_NAME_SQL = """
             SELECT p
                FROM ProductEntity p
                WHERE p.manufacturer.companyName LIKE CONCAT('%', :companyName, '%')
            """;

    final String FIND_BY_PRICE_RANGE_SQL = """
            select * from products
            where price between :startPrice and :endPrice
            """;

    // fetch join 해 제조사 정보도 같이 가져옴
    @EntityGraph(attributePaths = {"manufacturer"})
    Optional<ProductEntity> findById(Long id);

    @EntityGraph(attributePaths = {"manufacturer"})
    Page<ProductEntity> findAll(Pageable pageable);

    /**
     * 이름 검색해달라고
     * @param name 이름
     * @param pageable 페이지정보
     * @return 제품정보
     */
    @EntityGraph(attributePaths = {"manufacturer"})
    Page<ProductEntity> findByNameContaining(String name, Pageable pageable);


    /**
     * 제조시명으로 제품
     * @param companyName 제조시명
     * @param pageable 페이지정보
     * @return 제품정보
     **/
    @Query(value = FIND_BY_MANUFACTURER_NAME_SQL)
    Page<ProductEntity> findByManufacturerCompanyNameContaining(
            @Param("companyName") String companyName,
            Pageable pageable
    );


    /**
     * 가격 범위 검색
     * @param startPrice 시작가격
     * @param endPrice 종료가격
     * @param pageable 페이지정보
     * @return 제품정보
     **/
    @Query(value = FIND_BY_PRICE_RANGE_SQL, nativeQuery = true)
    Page<ProductEntity> findByPriceBetween(
            @Param("startPrice") Double startPrice,
            @Param("endPrice")Double endPrice,
            Pageable pageable
    );
}
