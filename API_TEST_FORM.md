# API Test Form - 요청/응답 예시

WMS REST API 엔드포인트별 요청/응답 예시

**Base URL**: `http://localhost:8080`

---

## 1. ProductController (8개 엔드포인트)

### 1.1 상품 생성
```
POST /api/products
Content-Type: application/json
```

**Request:**
```json
{
  "name": "삼성 갤럭시 S24",
  "price": 1200000.0,
  "description": "최신 플래그십 스마트폰",
  "encryptedManufacturerId": "abc123xyz"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "productId": "encrypted_product_id_1",
    "name": "삼성 갤럭시 S24",
    "description": "최신 플래그십 스마트폰",
    "price": 1200000.0,
    "manufacturer": "삼성전자"
  },
  "message": "상품이 생성되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 1.2 상품 상세 조회
```
GET /api/products/{encryptedId}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "productId": "encrypted_product_id_1",
    "name": "삼성 갤럭시 S24",
    "description": "최신 플래그십 스마트폰",
    "price": 1200000.0,
    "manufacturer": "삼성전자"
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 1.3 상품 목록 조회
```
GET /api/products?page=0&size=10&sort=name
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productId": "encrypted_id_1",
        "name": "LG 그램",
        "price": 1500000.0,
        "manufacturerName": "LG전자"
      },
      {
        "productId": "encrypted_id_2",
        "name": "삼성 갤럭시 S24",
        "price": 1200000.0,
        "manufacturerName": "삼성전자"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalPages": 5,
    "totalElements": 50,
    "last": false,
    "first": true
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 1.4 상품명으로 검색
```
GET /api/products/search?name=갤럭시&page=0&size=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productId": "encrypted_id_1",
        "name": "삼성 갤럭시 S24",
        "price": 1200000.0,
        "manufacturerName": "삼성전자"
      },
      {
        "productId": "encrypted_id_2",
        "name": "삼성 갤럭시 Z Flip",
        "price": 1100000.0,
        "manufacturerName": "삼성전자"
      }
    ],
    "totalElements": 2
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 1.5 제조사명으로 상품 검색
```
GET /api/products/search/by-manufacturer?manufacturer=삼성&page=0&size=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productId": "encrypted_id_1",
        "name": "삼성 갤럭시 S24",
        "price": 1200000.0,
        "manufacturerName": "삼성전자"
      },
      {
        "productId": "encrypted_id_2",
        "name": "삼성 노트북",
        "price": 1500000.0,
        "manufacturerName": "삼성전자"
      }
    ],
    "totalElements": 2
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 1.6 가격 범위로 검색
```
GET /api/products/search/by-price-range?minPrice=1000000&maxPrice=1500000&page=0&size=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productId": "encrypted_id_1",
        "name": "삼성 갤럭시 S24",
        "price": 1200000.0,
        "manufacturerName": "삼성전자"
      },
      {
        "productId": "encrypted_id_2",
        "name": "LG 그램",
        "price": 1500000.0,
        "manufacturerName": "LG전자"
      }
    ],
    "totalElements": 2
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 1.7 상품 정보 수정
```
PUT /api/products/{encryptedId}
Content-Type: application/json
```

**Request:**
```json
{
  "name": "삼성 갤럭시 S24 Ultra",
  "price": 1400000.0,
  "description": "업그레이드된 최신 모델",
  "encryptedManufacturerId": "abc123xyz"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "상품 정보가 수정되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 1.8 상품 삭제
```
DELETE /api/products/{encryptedId}
```

**Response (204 No Content):**
```
(응답 본문 없음)
```

---

## 2. ManufacturerController (6개 엔드포인트)

### 2.1 제조사 생성
```
POST /api/manufacturers
Content-Type: application/json
```

**Request:**
```json
{
  "companyName": "삼성전자",
  "email": "contact@samsung.com",
  "contact": "02-1234-5678",
  "location": "서울특별시 강남구"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "manufacturerId": "encrypted_manufacturer_id_1",
    "companyName": "삼성전자",
    "email": "contact@samsung.com",
    "contact": "02-1234-5678",
    "location": "서울특별시 강남구"
  },
  "message": "제조사가 생성되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 2.2 제조사 상세 조회
```
GET /api/manufacturers/{encryptedId}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "manufacturerId": "encrypted_manufacturer_id_1",
    "companyName": "삼성전자",
    "email": "contact@samsung.com",
    "contact": "02-1234-5678",
    "location": "서울특별시 강남구"
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 2.3 제조사 목록 조회
```
GET /api/manufacturers?page=0&size=10&sort=companyName
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "manufacturerId": "encrypted_id_1",
        "companyName": "LG전자",
        "email": "contact@lg.com",
        "location": "서울특별시"
      },
      {
        "manufacturerId": "encrypted_id_2",
        "companyName": "삼성전자",
        "email": "contact@samsung.com",
        "location": "서울특별시 강남구"
      }
    ],
    "totalElements": 2
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 2.4 회사명으로 검색
```
GET /api/manufacturers/search?companyName=삼성&page=0&size=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "manufacturerId": "encrypted_id_1",
        "companyName": "삼성전자",
        "email": "contact@samsung.com",
        "location": "서울특별시 강남구"
      }
    ],
    "totalElements": 1
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 2.5 제조사 정보 수정
```
PUT /api/manufacturers/{encryptedId}
Content-Type: application/json
```

**Request:**
```json
{
  "companyName": "삼성전자(주)",
  "email": "new-contact@samsung.com",
  "contact": "02-9999-8888",
  "location": "서울특별시 서초구"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "제조사 정보가 수정되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 2.6 제조사 삭제
```
DELETE /api/manufacturers/{encryptedId}
```

**Response (204 No Content):**
```
(응답 본문 없음)
```

---

## 3. WarehouseController (7개 엔드포인트)

### 3.1 창고 생성
```
POST /api/warehouses
Content-Type: application/json
```

**Request:**
```json
{
  "name": "서울 중앙 창고",
  "location": "서울특별시 송파구",
  "contact": "02-1111-2222",
  "manager": "홍길동"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "warehouseId": "encrypted_warehouse_id_1",
    "name": "서울 중앙 창고",
    "location": "서울특별시 송파구",
    "contact": "02-1111-2222",
    "manager": "홍길동"
  },
  "message": "창고가 생성되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 3.2 창고 상세 조회
```
GET /api/warehouses/{encryptedId}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "warehouseId": "encrypted_warehouse_id_1",
    "name": "서울 중앙 창고",
    "location": "서울특별시 송파구",
    "contact": "02-1111-2222",
    "manager": "홍길동"
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 3.3 창고 목록 조회
```
GET /api/warehouses?page=0&size=10&sort=name
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "warehouseId": "encrypted_id_1",
        "name": "부산 남부 창고",
        "location": "부산광역시",
        "manager": "김철수"
      },
      {
        "warehouseId": "encrypted_id_2",
        "name": "서울 중앙 창고",
        "location": "서울특별시 송파구",
        "manager": "홍길동"
      }
    ],
    "totalElements": 2
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 3.4 창고명으로 검색
```
GET /api/warehouses/search?name=서울&page=0&size=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "warehouseId": "encrypted_id_1",
        "name": "서울 중앙 창고",
        "location": "서울특별시 송파구",
        "manager": "홍길동"
      }
    ],
    "totalElements": 1
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 3.5 위치로 검색
```
GET /api/warehouses/search/by-location?location=서울&page=0&size=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "warehouseId": "encrypted_id_1",
        "name": "서울 중앙 창고",
        "location": "서울특별시 송파구",
        "manager": "홍길동"
      }
    ],
    "totalElements": 1
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 3.6 창고 정보 수정
```
PUT /api/warehouses/{encryptedId}
Content-Type: application/json
```

**Request:**
```json
{
  "name": "서울 중앙 물류센터",
  "location": "서울특별시 송파구 잠실동",
  "contact": "02-3333-4444",
  "manager": "이영희"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "창고 정보가 수정되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 3.7 창고 삭제
```
DELETE /api/warehouses/{encryptedId}
```

**Response (204 No Content):**
```
(응답 본문 없음)
```

---

## 4. InventoryController (8개 엔드포인트)

### 4.1 초기 재고 등록
```
POST /api/inventories
Content-Type: application/json
```

**Request:**
```json
{
  "encryptedProductId": "encrypted_product_id",
  "encryptedWarehouseId": "encrypted_warehouse_id",
  "quantity": 100
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "inventoryId": "encrypted_inventory_id_1",
    "productName": "삼성 갤럭시 S24",
    "warehouseName": "서울 중앙 창고",
    "quantity": 100
  },
  "message": "재고가 등록되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 4.2 재고 상세 조회
```
GET /api/inventories/{encryptedId}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "inventoryId": "encrypted_inventory_id_1",
    "productName": "삼성 갤럭시 S24",
    "warehouseName": "서울 중앙 창고",
    "quantity": 100
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 4.3 전체 재고 목록 조회
```
GET /api/inventories?page=0&size=10&sort=quantity
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "inventoryId": "encrypted_id_1",
        "productName": "LG 그램",
        "warehouseName": "부산 남부 창고",
        "quantity": 50
      },
      {
        "inventoryId": "encrypted_id_2",
        "productName": "삼성 갤럭시 S24",
        "warehouseName": "서울 중앙 창고",
        "quantity": 100
      }
    ],
    "totalElements": 2
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 4.4 상품별 재고 조회
```
GET /api/inventories/by-product/{encryptedProductId}?page=0&size=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "inventoryId": "encrypted_id_1",
        "productName": "삼성 갤럭시 S24",
        "warehouseName": "서울 중앙 창고",
        "quantity": 100
      },
      {
        "inventoryId": "encrypted_id_2",
        "productName": "삼성 갤럭시 S24",
        "warehouseName": "부산 남부 창고",
        "quantity": 50
      }
    ],
    "totalElements": 2
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 4.5 창고별 재고 조회
```
GET /api/inventories/by-warehouse/{encryptedWarehouseId}?page=0&size=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "inventoryId": "encrypted_id_1",
        "productName": "삼성 갤럭시 S24",
        "warehouseName": "서울 중앙 창고",
        "quantity": 100
      },
      {
        "inventoryId": "encrypted_id_2",
        "productName": "LG 그램",
        "warehouseName": "서울 중앙 창고",
        "quantity": 75
      }
    ],
    "totalElements": 2
  },
  "message": null,
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 4.6 재고 입고
```
POST /api/inventories/stock-in
Content-Type: application/json
```

**Request:**
```json
{
  "encryptedInventoryId": "encrypted_inventory_id",
  "quantity": 50
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "재고가 입고되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 4.7 재고 출고
```
POST /api/inventories/stock-out
Content-Type: application/json
```

**Request:**
```json
{
  "encryptedInventoryId": "encrypted_inventory_id",
  "quantity": 30
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": null,
  "message": "재고가 출고되었습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

### 4.8 재고 삭제
```
DELETE /api/inventories/{encryptedId}
```

**Response (204 No Content):**
```
(응답 본문 없음)
```

---

## 5. 에러 응답 예시

### 400 Bad Request (유효성 검증 실패)
```json
{
  "success": false,
  "data": {
    "name": "상품명은 필수입니다.",
    "price": "가격은 양수여야 합니다."
  },
  "message": "입력값이 유효하지 않습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

### 404 Not Found (리소스 없음)
```json
{
  "success": false,
  "data": null,
  "message": "상품을 찾을 수 없습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

### 409 Conflict (데이터 무결성 위반)
```json
{
  "success": false,
  "data": null,
  "message": "연관된 데이터가 존재하여 작업을 수행할 수 없습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "data": null,
  "message": "서버 내부 오류가 발생했습니다.",
  "timestamp": "2025-01-30T10:30:00"
}
```

---

**총 엔드포인트**: 29개 (Product: 8, Manufacturer: 6, Warehouse: 7, Inventory: 8)