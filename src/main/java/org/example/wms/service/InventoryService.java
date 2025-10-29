package org.example.wms.service;

import lombok.RequiredArgsConstructor;
import org.example.wms.dto.crud.InventoryCreateRequest;
import org.example.wms.dto.crud.StockInRequest;
import org.example.wms.dto.crud.StockOutRequest;
import org.example.wms.dto.info.InventoryInfoDTO;
import org.example.wms.dto.list.InventoryListDTO;
import org.example.wms.entity.InventoryEntity;
import org.example.wms.entity.ProductEntity;
import org.example.wms.entity.WarehouseEntity;
import org.example.wms.repository.InventoryRepository;
import org.example.wms.repository.ProductRepository;
import org.example.wms.repository.WarehouseRepository;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private  final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final IdEncryptionUtil idEncryptionUtil;
    //--------------------------------생성-------------------------------------------------

    /**
     * 초기 재고 등록
     * @param request 재고 생성 요청 DTO (상품 ID, 창고 ID, 초기 수량)
     * @return 생성된 재고 정보 (암호화된 ID 포함)
     * @throws IllegalArgumentException 상품 또는 창고를 찾을 수 없는 경우
     * @throws DataIntegrityViolationException 이미 등록된 재고가 있는 경우
     */
    public InventoryInfoDTO createInventory(InventoryCreateRequest request){
        Long productId = idEncryptionUtil.decrypt(request.getEncryptedProductId());
        Long warehouseId = idEncryptionUtil.decrypt(request.getEncryptedWarehouseId());

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        WarehouseEntity warehouseEntity = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("창고를 찾을 수 없습니다."));

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .product(productEntity)
                .warehouse(warehouseEntity)
                .quantity(request.getQuantity())
                .build();

        InventoryEntity saved = inventoryRepository.saveAndFlush(inventoryEntity);
        return InventoryInfoDTO.of(saved, idEncryptionUtil);
    }

    //--------------------------------조회-------------------------------------------------

    /**
     * 재고 단건 조회
     * @param encryptedId 암호화된 재고 ID
     * @return 재고 상세 정보 (상품명, 창고명 포함)
     * @throws IllegalArgumentException 재고를 찾을 수 없는 경우
     */
    public InventoryInfoDTO getInventory(String encryptedId){
        Long id = idEncryptionUtil.decrypt(encryptedId);
        InventoryEntity entity = inventoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다."));
        return InventoryInfoDTO.of(entity, idEncryptionUtil);
    }

    /**
     * 전체 재고 목록 조회 (페이징)
     * @param pageable 페이지 정보
     * @return 재고 목록
     */
    public Page<InventoryListDTO> getAllInventories(Pageable pageable){
        Page<InventoryEntity> page = inventoryRepository.findAll(pageable);
        return InventoryListDTO.of(page,idEncryptionUtil);
    }

    /**
     * 특정 상품의 재고 목록 조회 (모든 창고)
     * @param encryptedProductId 암호화된 상품 ID
     * @param pageable 페이지 정보
     * @return 해당 상품의 창고별 재고 목록
     */
    public Page<InventoryListDTO> getInventoriesByProduct(String encryptedProductId, Pageable pageable){
        Long productId = idEncryptionUtil.decrypt(encryptedProductId);
        Page<InventoryEntity> page = inventoryRepository.findByProductProductId(productId, pageable);
        return InventoryListDTO.of(page,idEncryptionUtil);
    }

    /**
     * 특정 창고의 재고 목록 조회 (모든 상품)
     * @param encryptedWarehouseId 암호화된 창고 ID
     * @param pageable 페이지 정보
     * @return 해당 창고의 상품별 재고 목록
     */
    public Page<InventoryListDTO> getInventoriesByWarehouse(String encryptedWarehouseId, Pageable pageable){
        Long warehouseId = idEncryptionUtil.decrypt(encryptedWarehouseId);
        Page<InventoryEntity> page = inventoryRepository.findByWarehouseWarehouseId(warehouseId,pageable);
        return  InventoryListDTO.of(page,idEncryptionUtil);
    }

    //--------------------------------수정-------------------------------------------------
    /**
     * 재고 입고 (수량 증가)
     * @param request 입고 요청 DTO (상품 ID, 창고 ID, 입고 수량)
     * @throws IllegalArgumentException 재고를 찾을 수 없는 경우
     */
    @Transactional
    public void stockIn(StockInRequest request){
        Long id = idEncryptionUtil.decrypt(request.getEncryptedInventoryId());
        InventoryEntity entity= inventoryRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("재고를 찾을 수 없습니다."));

        entity.addQuantity(request.getQuantity());
        inventoryRepository.saveAndFlush(entity);
    }

    /**
     * 재고 출고 (수량 감소)
     * @param request 출고 요청 DTO (상품 ID, 창고 ID, 출고 수량)
     * @throws IllegalArgumentException 재고를 찾을 수 없거나 재고가 부족한 경우
     */
    public void stockOut(StockOutRequest request){
        Long id = idEncryptionUtil.decrypt(request.getEncryptedInventoryId());
        InventoryEntity entity= inventoryRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("재고를 찾을 수 없습니다."));

        entity.removeQuantity(request.getQuantity());
        inventoryRepository.save(entity);
    }
    //--------------------------------삭제-------------------------------------------------
    /**
     * 재고 삭제
     * @param encryptedId 암호화된 재고 ID
     * @throws IllegalArgumentException 재고를 찾을 수 없는 경우
     */
    public void deleteInventory(String encryptedId){
        Long id = idEncryptionUtil.decrypt(encryptedId);

        InventoryEntity entity = inventoryRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("재고를 찾을 수 없습니다."));
    }
}
