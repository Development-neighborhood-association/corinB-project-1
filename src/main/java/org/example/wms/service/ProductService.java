package org.example.wms.service;

import lombok.RequiredArgsConstructor;
import org.example.wms.dto.crud.ProductCreateRequest;
import org.example.wms.dto.crud.ProductUpdateRequest;
import org.example.wms.dto.info.ProductInfoDTO;
import org.example.wms.dto.list.ProductListDTO;
import org.example.wms.entity.ManufacturerEntity;
import org.example.wms.entity.ProductEntity;
import org.example.wms.repository.ManufacturerRepository;
import org.example.wms.repository.ProductRepository;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ManufacturerRepository manufacturerRepository;
    private final ProductRepository productRepository;
    private final IdEncryptionUtil idEncryptionUtil;

    //--------------------------------생성-------------------------------------------------

    /**
     * 상품 생성
     * @param request 상품 생성 요청 DTO (제조사 ID 포함)
     * @return 생성된 상품 정보 (암호화된 ID 포함)
     */
    @Transactional
    public ProductInfoDTO createProduct(ProductCreateRequest request){
        Long manufacturerId = idEncryptionUtil.decrypt(request.getEncryptedManufacturerId());
        ManufacturerEntity manufacturer = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new IllegalArgumentException("제조사를 찾을 수 없습니다."));
        ProductEntity entity = ProductEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .manufacturer(manufacturer)
                .build();
        ProductEntity saved =  productRepository.saveAndFlush(entity);
        return ProductInfoDTO.of(saved, idEncryptionUtil);
    }

    //--------------------------------조회-------------------------------------------------

    /**
     * 상품 단건 조회
     * @param encryptedId 암호화된 상품 ID
     * @return 상품 상세 정보 (제조사 정보 포함)
     * @throws IllegalArgumentException 상품을 찾을 수 없는 경우
     */
    public ProductInfoDTO getProduct(String encryptedId){
        ProductEntity entity = productRepository.findById(idEncryptionUtil.decrypt(encryptedId))
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        return ProductInfoDTO.of(entity, idEncryptionUtil);
    }

    /**
     * 전체 상품 목록 조회 (페이징)
     * @param pageable 페이지 정보
     * @return 상품 목록 (제조사명 포함)
     */
    public Page<ProductListDTO> getAllProducts(Pageable pageable){
        return ProductListDTO.of(productRepository.findAll(pageable), idEncryptionUtil);
    }

    /**
     * 상품명으로 검색 (부분 일치)
     * @param name 검색할 상품명
     * @param pageable 페이지 정보
     * @return 검색된 상품 목록
     */
    public Page<ProductListDTO> searchByName(String name, Pageable pageable){
        return  ProductListDTO.of(productRepository.findByNameContaining(name, pageable),
                                  idEncryptionUtil);
    }

    /**
     * 제조사명으로 상품 검색 (부분 일치)
     * @param manufacturerName 검색할 제조사명
     * @param pageable 페이지 정보
     * @return 검색된 상품 목록
     */
    public Page<ProductListDTO> searchByManufacturerName(String manufacturerName, Pageable pageable){
        return ProductListDTO
                .of(productRepository
                        .findByManufacturerCompanyNameContaining(manufacturerName, pageable),
                     idEncryptionUtil);
    }

    /**
     * 가격 범위로 상품 검색
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param pageable 페이지 정보
     * @return 검색된 상품 목록
     */
    Page<ProductListDTO> searchByPriceRange(Double minPrice, Double maxPrice, Pageable pageable){
        return ProductListDTO
                .of(productRepository.findByPriceBetween(minPrice,maxPrice, pageable),
                    idEncryptionUtil);
    }



    //--------------------------------수정-------------------------------------------------

    /**
     * 상품 정보 수정
     * @param encryptedId 암호화된 상품 ID
     * @param request 수정할 정보
     * @throws IllegalArgumentException 상품을 찾을 수 없는 경우
     */
    @Transactional
    public void updateProduct(String encryptedId, ProductUpdateRequest request){
        Long id = idEncryptionUtil.decrypt(encryptedId);
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        if (request.getEncryptedManufacturerId() != null){
            Long newManufacturerId = idEncryptionUtil.decrypt(request.getEncryptedManufacturerId());
            ManufacturerEntity manufacturerEntity = manufacturerRepository.findById(newManufacturerId)
                    .orElseThrow(() -> new IllegalArgumentException("제조사를 찾을 수 없습니다."));
            entity.setManufacturer(manufacturerEntity);
        }
        if (request.getDescription() != null)
            entity.setDescription(request.getDescription());
        if (request.getPrice() != null)
            entity.setPrice(request.getPrice());
        if (request.getName() != null)
            entity.setName(request.getName());

        productRepository.saveAndFlush(entity);
    }
    //--------------------------------삭제-------------------------------------------------

    /**
     * 상품 삭제
     * @param encryptedId 암호화된 상품 ID
     * @throws IllegalArgumentException 상품을 찾을 수 없는 경우
     * @throws DataIntegrityViolationException 연관된 재고가 있는 경우
     */
    @Transactional
    public void deleteProduct(String encryptedId){
        Long id = idEncryptionUtil.decrypt(encryptedId);
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        try {
            productRepository.delete(entity);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("보관중인 재고가 아직 존재합니다.", e);
        }
    }
}
