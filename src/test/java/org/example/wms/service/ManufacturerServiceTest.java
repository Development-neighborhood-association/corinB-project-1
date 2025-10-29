package org.example.wms.service;
import org.example.wms.dto.crud.ManufacturerCreateRequest;
import org.example.wms.dto.crud.ManufacturerUpdateRequest;
import org.example.wms.dto.info.ManufacturerInfoDTO;
import org.example.wms.dto.list.ManufacturerListDTO;
import org.example.wms.util.IdEncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ManufacturerServiceTest {
    @Autowired
    ManufacturerService manufacturerService;
    @Autowired
    IdEncryptionUtil idEncryptionUtil;

    @Test
    @Rollback(value = true)
    @Transactional
    void createManufacturer() {
        ManufacturerCreateRequest request = ManufacturerCreateRequest.builder()
                .companyName("Test Company")
                .email("test@example.com")
                .contact("1234567890")
                .location("Test Location")
                .build();
        ManufacturerInfoDTO result = manufacturerService.createManufacturer(request);

        assertThat(result.getCompanyName()).isEqualTo(request.getCompanyName());
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        assertThat(result.getContact()).isEqualTo(request.getContact());
        assertThat(result.getLocation()).isEqualTo(request.getLocation());
        System.out.println(result);
    }

    @Test
    void getManufacturer() {
        String encryptedId = idEncryptionUtil.encrypt(1L);
        ManufacturerInfoDTO result = manufacturerService.getManufacturer(encryptedId);
        System.out.println(result);
        assertThat(result.getManufacturerId()).isEqualTo(encryptedId);
    }

    @Test
    void getAllManufacturers() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ManufacturerListDTO> result = manufacturerService.getAllManufacturers(pageable);
        assertThat(result.getContent().size())
                .isEqualTo(3);
        result.getContent().forEach(System.out::println);
    }

    @Test
    void searchByCompanyName() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ManufacturerListDTO> result = manufacturerService.searchByCompanyName("삼성", pageable);
        result.getContent().forEach(System.out::println);
    }

    @Test
    void updateManufacturer() {
        ManufacturerCreateRequest request = ManufacturerCreateRequest.builder()
                .companyName("Test Company")
                .email("test@example.com")
                .contact("1234567890")
                .location("Test Location")
                .build();
        ManufacturerInfoDTO result = manufacturerService.createManufacturer(request);
        String encryptedId = result.getManufacturerId();
        System.out.println(encryptedId);
        ManufacturerUpdateRequest updateRequest = ManufacturerUpdateRequest.builder()
                .companyName("Updated Company")
                .email("updated@example.com")
                .contact("1234567890")
                .location("Updated Location")
                .build();
        manufacturerService.updateManufacturer(encryptedId, updateRequest);
        ManufacturerInfoDTO updatedResult = manufacturerService.getManufacturer(encryptedId);
        System.out.println(updatedResult);
    }

    @Test
    void deleteManufacturer() {
        String encryptedId = idEncryptionUtil.encrypt(13L);
        manufacturerService.deleteManufacturer(encryptedId);
    }
}