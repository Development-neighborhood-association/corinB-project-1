package org.example.wms.service;

import org.example.wms.dto.list.InventoryListDTO;
import org.example.wms.util.IdEncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryServiceTest {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private IdEncryptionUtil idEncryptionUtil;

    @Test
    void createInventory() {
    }

    @Test
    void getInventory() {
    }

    @Test
    void getAllInventories() {
    }

    @Test
    void getInventoriesByProduct() {
        String productId = idEncryptionUtil.encrypt(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<InventoryListDTO> result = inventoryService.getInventoriesByProduct(productId, pageable);
        result.getContent().forEach(System.out::println);
    }
}