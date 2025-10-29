package org.example.wms.service;

import lombok.RequiredArgsConstructor;
import org.example.wms.dto.crud.ManufacturerCreateRequest;
import org.example.wms.dto.crud.ManufacturerUpdateRequest;
import org.example.wms.dto.info.ManufacturerInfoDTO;
import org.example.wms.dto.list.ManufacturerListDTO;
import org.example.wms.entity.ManufacturerEntity;
import org.example.wms.repository.ManufacturerRepository;
import org.example.wms.util.IdEncryptionUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final IdEncryptionUtil idEncryptionUtil;

    //--------------------------------생성-------------------------------------------------

    /**
     * 제조사 생성
     * @param request 제조사 생성 요청 DTO
     * @return 생성된 제조사 정보 (암호화된 ID 포함)
     */
    @Transactional
    public ManufacturerInfoDTO createManufacturer(ManufacturerCreateRequest request){
        ManufacturerEntity entity = ManufacturerEntity.builder()
                .companyName(request.getCompanyName())
                .email(request.getEmail())
                .contact(request.getContact())
                .location(request.getLocation())
                .build();
        ManufacturerEntity saved =  manufacturerRepository.saveAndFlush(entity);
        return ManufacturerInfoDTO.of(saved, idEncryptionUtil);
    }

    //--------------------------------조회-------------------------------------------------
    /**
     * 제조사 단건 조회
     * @param encryptedId 암호화된 제조사 ID
     * @return 제조사 상세 정보
     * @throws IllegalArgumentException 제조사를 찾을 수 없는 경우
     */
    public ManufacturerInfoDTO getManufacturer(String encryptedId) {
        var id =idEncryptionUtil.decrypt(encryptedId);
        return manufacturerRepository.findById(id)
                .map(entity -> ManufacturerInfoDTO.of(entity, idEncryptionUtil))
                .orElseThrow(() -> new IllegalArgumentException("제조사를 찾을 수 없습니다."));
    }

    /**
     * 전체 제조사 목록 조회 (페이징)
     * @param pageable 페이지 정보
     * @return 제조사 목록
     */
    public Page<ManufacturerListDTO> getAllManufacturers(Pageable pageable) {
        return ManufacturerListDTO.of(
                    manufacturerRepository
                        .findAll(pageable),
                    idEncryptionUtil);
    }



    /**
     * 회사명으로 제조사 검색 (부분 일치)
     * @param companyName 검색할 회사명
     * @param pageable 페이지 정보
     * @return 검색된 제조사 목록
     */
    Page<ManufacturerListDTO> searchByCompanyName(String companyName, Pageable pageable){
        return ManufacturerListDTO.of(
                manufacturerRepository
                    .findByCompanyNameContaining(companyName, pageable),
                idEncryptionUtil);
    }

    //--------------------------------수정-------------------------------------------------

    /**
     * 제조사 정보 수정
     * @param encryptedId 암호화된 제조사 ID
     * @param request 수정할 정보
     * @throws IllegalArgumentException 제조사를 찾을 수 없는 경우
     */
    @Transactional
    void updateManufacturer(String encryptedId, ManufacturerUpdateRequest request){
        Long id = idEncryptionUtil.decrypt(encryptedId);
        ManufacturerEntity entity = manufacturerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제조사를 찾을 수 없습니다."));

        if(request.getCompanyName() != null)
            entity.setCompanyName(request.getCompanyName());
        if (request.getContact() != null)
            entity.setContact(request.getContact());
        if (request.getLocation() != null)
            entity.setLocation(request.getLocation());
        if (request.getEmail() != null)
            entity.setEmail(request.getEmail());

        manufacturerRepository.saveAndFlush(entity);
    }

    //--------------------------------삭제-------------------------------------------------

    /**
     * 제조사 삭제
     * @param encryptedId 암호화된 제조사 ID
     * @throws IllegalArgumentException 제조사를 찾을 수 없는 경우
     * @throws DataIntegrityViolationException 연관된 상품이 있는 경우
     */
    @Transactional
    public void deleteManufacturer(String encryptedId){
        Long id = idEncryptionUtil.decrypt(encryptedId);
        ManufacturerEntity entity = manufacturerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제조사를 찾을 수 없습니다. id=" + encryptedId));

        try {
            manufacturerRepository.delete(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("연관된 상품이 있어 삭제할 수 없습니다.", e);
        }
    }

}
