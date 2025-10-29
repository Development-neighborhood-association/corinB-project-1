package org.example.wms.service;

import org.example.wms.dto.crud.ProductCreateRequest;
import org.example.wms.dto.crud.ProductUpdateRequest;
import org.example.wms.dto.info.ProductInfoDTO;
import org.example.wms.dto.list.ProductListDTO;
import org.example.wms.entity.ManufacturerEntity;
import org.example.wms.repository.ManufacturerRepository;
import org.example.wms.util.IdEncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private IdEncryptionUtil idEncryptionUtil;

    @Test
    void createProduct() {
        ManufacturerEntity manufacturer = manufacturerRepository.findById(14L).orElse(null);
        String encryptedManufacturerId = idEncryptionUtil.encrypt(manufacturer.getManufacturerId());
        ProductCreateRequest request1 = ProductCreateRequest.builder()
                .name("tp")
                .price(100000.0)
                .description("test")
                .encryptedManufacturerId(encryptedManufacturerId)
                .build();


        ProductCreateRequest request2 = ProductCreateRequest.builder()
                .name("tp2")
                .price(200000.0)
                .description("test2")
                .encryptedManufacturerId(encryptedManufacturerId)
                .build();

        ProductCreateRequest request3 = ProductCreateRequest.builder()
                .name("tp3")
                .price(300000.0)
                .description("test3")
                .encryptedManufacturerId(encryptedManufacturerId)
                .build();

        productService.createProduct(request1);
        productService.createProduct(request2);
        productService.createProduct(request3);
    }

    @Test
    void getProduct() {
        String encryptedId = idEncryptionUtil.encrypt(1L);
        ProductInfoDTO result = productService.getProduct(encryptedId);
        System.out.println(result);
    }

    @Test
    void getAllProducts() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductListDTO> result = productService.getAllProducts(pageable);
        result.getContent().forEach(System.out::println);
    }

    @Test
    void searchByName() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductListDTO> result = productService.searchByName("tp", pageable);
        result.getContent().forEach(System.out::println);
    }

    @Test
    void searchByManufacturerName() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductListDTO> result = productService.searchByManufacturerName("삼성", pageable);
        result.getContent().forEach(System.out::println);
    }

    @Test
    void searchByPriceRange() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ProductListDTO> result = productService.searchByPriceRange(100000.0, 200000.0, pageable);
        result.getContent().forEach(System.out::println);
    }

    @Test
    void updateProduct() {
        String encryptedId = idEncryptionUtil.encrypt(17L);
        String encryptedManufacturerId = idEncryptionUtil.encrypt(1L);
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .name("tpu")
                .encryptedManufacturerId(encryptedManufacturerId)
                .build();
        productService.updateProduct(encryptedId, request);
        ProductInfoDTO result = productService.getProduct(encryptedId);
        System.out.println(result);
    }

    @Test
    void deleteProduct() {
        String encryptedId = idEncryptionUtil.encrypt(17L);
        productService.deleteProduct(encryptedId);
    }
}