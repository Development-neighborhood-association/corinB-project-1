package org.example.wms.service;


import lombok.RequiredArgsConstructor;
import org.example.wms.dto.crud.WarehouseCreateRequest;
import org.example.wms.dto.crud.WarehouseUpdateRequest;
import org.example.wms.dto.info.WarehouseInfoDTO;
import org.example.wms.dto.list.WarehouseListDTO;
import org.example.wms.entity.WarehouseEntity;
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
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final IdEncryptionUtil idEncryptionUtil;
    //--------------------------------생성-------------------------------------------------

    /**
     * 창고 생성
     * @param request 창고 생성 요청 DTO
     * @return 생성된 창고 정보 (암호화된 ID 포함)
     */
    @Transactional
    public WarehouseInfoDTO createWarehouse(WarehouseCreateRequest request){
        WarehouseEntity entity = WarehouseEntity.builder()
                .name(request.getName())
                .location(request.getLocation())
                .contact(request.getContact())
                .build();

        WarehouseEntity saved = warehouseRepository.saveAndFlush(entity);
        return WarehouseInfoDTO.of(saved, idEncryptionUtil);
    }

    //--------------------------------조회-------------------------------------------------
    /**
     * 창고 단건 조회
     * @param encryptedId 암호화된 창고 ID
     * @return 창고 상세 정보
     * @throws IllegalArgumentException 창고를 찾을 수 없는 경우
     */
    public WarehouseInfoDTO getWarehouse(String encryptedId){
        Long id = idEncryptionUtil.decrypt(encryptedId);
        WarehouseEntity entity = warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("창고를 찾을 수 없습니다."));
        return WarehouseInfoDTO.of(entity, idEncryptionUtil);
    }

    /**
     * 전체 창고 목록 조회 (페이징)
     * @param pageable 페이지 정보
     * @return 창고 목록
     */
    public Page<WarehouseListDTO> getAllWarehouses(Pageable pageable){
        Page<WarehouseEntity> page = warehouseRepository.findAll(pageable);
        return WarehouseListDTO.of(page, idEncryptionUtil);
    }

    /**
     * 창고명으로 검색 (부분 일치)
     * @param name 검색할 창고명
     * @param pageable 페이지 정보
     * @return 검색된 창고 목록
     */
    public Page<WarehouseListDTO> searchByName(String name, Pageable pageable){
        Page<WarehouseEntity> page = warehouseRepository.findByNameContaining(name, pageable);
        return  WarehouseListDTO.of(page, idEncryptionUtil);
    }

    /**
     * 위치로 창고 검색 (부분 일치)
     * @param location 검색할 위치
     * @param pageable 페이지 정보
     * @return 검색된 창고 목록
     */
    public Page<WarehouseListDTO> searchByLocation(String location, Pageable pageable){
        Page<WarehouseEntity> page = warehouseRepository.findByLocationContaining(location, pageable);
        return   WarehouseListDTO.of(page, idEncryptionUtil);
    }

    //--------------------------------수정-------------------------------------------------
    /**
     * 창고 정보 수정
     * @param encryptedId 암호화된 창고 ID
     * @param request 수정할 정보
     * @throws IllegalArgumentException 창고를 찾을 수 없는 경우
     */
    @Transactional
    public void updateWarehouse(String encryptedId, WarehouseUpdateRequest request){
        Long id = idEncryptionUtil.decrypt(encryptedId);
        WarehouseEntity entity = warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("창고를 찾을 수 없습니다."));

        if (request.getName() != null)
            entity.setName(request.getName());
        if (request.getLocation() != null)
            entity.setLocation(request.getLocation());
        if (request.getContact() != null)
            entity.setContact(request.getContact());

    }

    //--------------------------------삭제-------------------------------------------------

    /**
     * 창고 삭제
     * @param encryptedId 암호화된 창고 ID
     * @throws IllegalArgumentException 창고를 찾을 수 없는 경우
     * @throws DataIntegrityViolationException 연관된 재고가 있는 경우
     */
    @Transactional
    public void deleteWarehouse(String encryptedId){
        Long id = idEncryptionUtil.decrypt(encryptedId);
        WarehouseEntity entity = warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("창고를 찾을 수 없습니다."));
        warehouseRepository.delete(entity);
    }
}
