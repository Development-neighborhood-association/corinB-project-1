package org.example.wms.service;

import org.example.wms.dto.crud.WarehouseCreateRequest;
import org.example.wms.dto.crud.WarehouseUpdateRequest;
import org.example.wms.dto.info.WarehouseInfoDTO;
import org.example.wms.dto.list.WarehouseListDTO;
import org.example.wms.util.IdEncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WarehouseServiceTest {
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private IdEncryptionUtil idEncryptionUtil;

    @Test
    void createWarehouseAndGetWarehouse() {
        WarehouseCreateRequest request = WarehouseCreateRequest
                .builder()
                .name("TTT")
                .location("Test Location")
                .contact("123-1234-1234")
                .build();
        WarehouseInfoDTO result = warehouseService.createWarehouse(request);
        System.out.println(result);
        String encryptedId = result.getWarehouseId();
        System.out.println(encryptedId);
        WarehouseInfoDTO warehouse = warehouseService.getWarehouse(encryptedId);
        System.out.println(warehouse);
    }


    @Test
    void searchByName() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<WarehouseListDTO> result = warehouseService.searchByName("TTT", pageable);
        result.getContent().forEach(System.out::println);
    }

    @Test
    void searchByLocation() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<WarehouseListDTO> result = warehouseService.searchByLocation("Test", pageable);
        result.getContent().forEach(System.out::println);
    }

    @Test
    void updateWarehouse() {
        String encryptedId = idEncryptionUtil.encrypt(7L);
        WarehouseUpdateRequest request = WarehouseUpdateRequest.builder()
                .name("Updated Warehouse")
                .location("Updated Location")
                .contact("123-1234-1234")
                .build();
        warehouseService.updateWarehouse(encryptedId, request);
        WarehouseInfoDTO updatedWarehouse = warehouseService.getWarehouse(encryptedId);
        System.out.println(updatedWarehouse);
    }

    @Test
    void deleteWarehouse() {
        String encryptedId = idEncryptionUtil.encrypt(7L);
        warehouseService.deleteWarehouse(encryptedId);
        WarehouseInfoDTO deletedWarehouse = warehouseService.getWarehouse(encryptedId);
        System.out.println(deletedWarehouse);
    }
}