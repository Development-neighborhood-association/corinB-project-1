# WMS API 명세서

WMS (Warehouse Management System) REST API 명세서입니다.

## 목차

1. [개요](#개요)
2. [공통 사항](#공통-사항)
3. [상품 관리 API](#상품-관리-api)
4. [제조사 관리 API](#제조사-관리-api)
5. [창고 관리 API](#창고-관리-api)
6. [재고 관리 API](#재고-관리-api)

---

## 개요

### Base URL
```
http://localhost:8080/api
```

### 기술 스택
- **Framework**: Spring Boot 3.5.7
- **Database**: MySQL 8.x (Port 3307)
- **Response Format**: JSON
- **Character Encoding**: UTF-8

---

## 공통 사항

### 표준 응답 형식

모든 API 응답은 `ApiResponse<T>` 래퍼 객체를 사용합니다.

```json
{
  "success": true,
  "data": { /* 실제 데이터 */ },
  "message": "성공 메시지",
  "timestamp": "2025-01-15T10:30:00"
}
```

**필드 설명:**
- `success` (boolean): 요청 성공 여부
- `data` (T): 응답 데이터 (제네릭 타입)
- `message` (string): 응답 메시지 (선택적)
- `timestamp` (datetime): 응답 생성 시간

### ID 암호화

보안을 위해 모든 엔티티 ID는 AES 암호화된 문자열로 제공됩니다.

**예시:**
```json
{
  "productId": "ZW5jcnlwdGVkSWRTdHJpbmc=",  // 암호화된 ID
  "name": "샘플 상품"
}
```

### 페이징

목록 조회 API는 Spring Data JPA 페이징을 사용합니다.

**요청 파라미터:**
- `page`: 페이지 번호 (0부터 시작, 기본값: 0)
- `size`: 페이지 크기 (기본값: 10)
- `sort`: 정렬 기준 (예: `name,asc` 또는 `price,desc`)

**응답 형식:**
```json
{
  "success": true,
  "data": {
    "content": [ /* 데이터 배열 */ ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": { "sorted": true, "unsorted": false }
    },
    "totalElements": 100,
    "totalPages": 10,
    "last": false,
    "first": true,
    "size": 10,
    "number": 0
  }
}
```

### HTTP 상태 코드

| 코드 | 의미 | 사용 시점 |
|------|------|-----------|
| 200 OK | 성공 | 조회, 수정 성공 |
| 201 Created | 생성 완료 | 리소스 생성 성공 |
| 204 No Content | 성공 (본문 없음) | 삭제 성공 |
| 400 Bad Request | 잘못된 요청 | 유효성 검증 실패 |
| 404 Not Found | 리소스 없음 | 해당 ID의 리소스가 없음 |
| 500 Internal Server Error | 서버 오류 | 서버 내부 에러 |

### 에러 응답

에러 발생 시 응답 형식:

```json
{
  "success": false,
  "data": null,
  "message": "에러 메시지",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

## 상품 관리 API

Base URL: `/api/products`

### 1. 상품 생성

**POST** `/api/products`

**설명:** 새로운 상품을 등록합니다.

**요청 본문:**
```json
{
  "name": "갤럭시 S24",
  "price": 1200000.0,
  "description": "삼성 플래그십 스마트폰",
  "encryptedManufacturerId": "ZW5jcnlwdGVkTWFudWZhY3R1cmVySWQ="
}
```

**필드 검증:**
- `name`: 필수, 공백 불가
- `price`: 필수, 0보다 큰 값
- `description`: 선택
- `encryptedManufacturerId`: 필수, 암호화된 제조사 ID

**응답:** `201 Created`
```json
{
  "success": true,
  "data": {
    "productId": "ZW5jcnlwdGVkUHJvZHVjdElk",
    "name": "갤럭시 S24",
    "price": 1200000.0,
    "description": "삼성 플래그십 스마트폰",
    "manufacturerId": "ZW5jcnlwdGVkTWFudWZhY3R1cmVySWQ=",
    "manufacturerName": "삼성전자",
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  },
  "message": "상품이 생성되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

### 2. 상품 상세 조회

**GET** `/api/products/{encryptedId}`

**설명:** 특정 상품의 상세 정보를 조회합니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 상품 ID

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "productId": "ZW5jcnlwdGVkUHJvZHVjdElk",
    "name": "갤럭시 S24",
    "price": 1200000.0,
    "description": "삼성 플래그십 스마트폰",
    "manufacturerId": "ZW5jcnlwdGVkTWFudWZhY3R1cmVySWQ=",
    "manufacturerName": "삼성전자",
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  }
}
```

---

### 3. 상품 목록 조회

**GET** `/api/products`

**설명:** 전체 상품 목록을 페이징하여 조회합니다.

**쿼리 파라미터:**
- `page`: 페이지 번호 (기본값: 0)
- `size`: 페이지 크기 (기본값: 10)
- `sort`: 정렬 기준 (기본값: `name`)

**예시 요청:**
```
GET /api/products?page=0&size=10&sort=name,asc
```

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productId": "ZW5jcnlwdGVkUHJvZHVjdElk",
        "name": "갤럭시 S24",
        "price": 1200000.0,
        "manufacturerName": "삼성전자"
      }
    ],
    "pageable": { /* 페이징 정보 */ },
    "totalElements": 100,
    "totalPages": 10
  }
}
```

---

### 4. 상품명으로 검색

**GET** `/api/products/search`

**설명:** 상품명으로 부분 일치 검색합니다.

**쿼리 파라미터:**
- `name`: 검색할 상품명 (필수)
- `page`: 페이지 번호
- `size`: 페이지 크기
- `sort`: 정렬 기준 (기본값: `name`)

**예시 요청:**
```
GET /api/products/search?name=갤럭시&page=0&size=10
```

**응답:** `200 OK` (목록 조회와 동일한 형식)

---

### 5. 제조사명으로 상품 검색

**GET** `/api/products/search/by-manufacturer`

**설명:** 제조사명으로 상품을 검색합니다.

**쿼리 파라미터:**
- `manufacturer`: 검색할 제조사명 (필수)
- `page`: 페이지 번호
- `size`: 페이지 크기
- `sort`: 정렬 기준

**예시 요청:**
```
GET /api/products/search/by-manufacturer?manufacturer=삼성&page=0&size=10
```

**응답:** `200 OK` (목록 조회와 동일한 형식)

---

### 6. 가격 범위로 검색

**GET** `/api/products/search/by-price-range`

**설명:** 가격 범위 내의 상품을 검색합니다.

**쿼리 파라미터:**
- `minPrice`: 최소 가격 (필수)
- `maxPrice`: 최대 가격 (필수)
- `page`: 페이지 번호
- `size`: 페이지 크기
- `sort`: 정렬 기준

**예시 요청:**
```
GET /api/products/search/by-price-range?minPrice=1000000&maxPrice=2000000&page=0&size=10
```

**응답:** `200 OK` (목록 조회와 동일한 형식)

---

### 7. 상품 정보 수정

**PUT** `/api/products/{encryptedId}`

**설명:** 상품 정보를 수정합니다. 모든 필드는 선택적입니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 상품 ID

**요청 본문:**
```json
{
  "name": "갤럭시 S24 Ultra",
  "price": 1500000.0,
  "description": "프리미엄 플래그십 모델",
  "encryptedManufacturerId": "ZW5jcnlwdGVkTWFudWZhY3R1cmVySWQ="
}
```

**참고:** 모든 필드는 선택적입니다. 제공된 필드만 업데이트됩니다.

**응답:** `200 OK`
```json
{
  "success": true,
  "data": null,
  "message": "상품 정보가 수정되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

### 8. 상품 삭제

**DELETE** `/api/products/{encryptedId}`

**설명:** 상품을 삭제합니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 상품 ID

**응답:** `204 No Content`

**에러:**
- 재고가 존재하는 경우: `400 Bad Request`
```json
{
  "success": false,
  "message": "보관중인 재고가 아직 존재합니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

## 제조사 관리 API

Base URL: `/api/manufacturers`

### 1. 제조사 생성

**POST** `/api/manufacturers`

**설명:** 새로운 제조사를 등록합니다.

**요청 본문:**
```json
{
  "companyName": "삼성전자",
  "email": "contact@samsung.com",
  "contact": "02-1234-5678",
  "location": "서울특별시 강남구"
}
```

**필드 검증:**
- `companyName`: 필수, 공백 불가
- `email`: 필수, 이메일 형식
- `contact`: 필수, 전화번호 형식 (예: 02-1234-5678, 010-1234-5678)
- `location`: 필수, 공백 불가

**응답:** `201 Created`
```json
{
  "success": true,
  "data": {
    "manufacturerId": "ZW5jcnlwdGVkTWFudWZhY3R1cmVySWQ=",
    "companyName": "삼성전자",
    "email": "contact@samsung.com",
    "contact": "02-1234-5678",
    "location": "서울특별시 강남구",
    "products": [],
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  },
  "message": "제조사가 생성되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

### 2. 제조사 상세 조회

**GET** `/api/manufacturers/{encryptedId}`

**설명:** 특정 제조사의 상세 정보를 조회합니다. 생산 상품 목록도 포함됩니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 제조사 ID

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "manufacturerId": "ZW5jcnlwdGVkTWFudWZhY3R1cmVySWQ=",
    "companyName": "삼성전자",
    "email": "contact@samsung.com",
    "contact": "02-1234-5678",
    "location": "서울특별시 강남구",
    "products": [
      {
        "productId": "ZW5jcnlwdGVkUHJvZHVjdElk",
        "name": "갤럭시 S24",
        "price": 1200000.0
      }
    ],
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  }
}
```

---

### 3. 제조사 목록 조회

**GET** `/api/manufacturers`

**설명:** 전체 제조사 목록을 페이징하여 조회합니다.

**쿼리 파라미터:**
- `page`: 페이지 번호 (기본값: 0)
- `size`: 페이지 크기 (기본값: 10)
- `sort`: 정렬 기준 (기본값: `companyName`)

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "manufacturerId": "ZW5jcnlwdGVkTWFudWZhY3R1cmVySWQ=",
        "companyName": "삼성전자",
        "email": "contact@samsung.com",
        "contact": "02-1234-5678",
        "location": "서울특별시 강남구"
      }
    ],
    "pageable": { /* 페이징 정보 */ },
    "totalElements": 50,
    "totalPages": 5
  }
}
```

---

### 4. 회사명으로 검색

**GET** `/api/manufacturers/search`

**설명:** 회사명으로 부분 일치 검색합니다.

**쿼리 파라미터:**
- `companyName`: 검색할 회사명 (필수)
- `page`, `size`, `sort`: 페이징 파라미터

**예시 요청:**
```
GET /api/manufacturers/search?companyName=삼성&page=0&size=10
```

**응답:** `200 OK` (목록 조회와 동일한 형식)

---

### 5. 제조사 정보 수정

**PUT** `/api/manufacturers/{encryptedId}`

**설명:** 제조사 정보를 수정합니다. 모든 필드는 선택적입니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 제조사 ID

**요청 본문:**
```json
{
  "companyName": "삼성전자 주식회사",
  "email": "newcontact@samsung.com",
  "contact": "02-9999-8888",
  "location": "서울특별시 서초구"
}
```

**응답:** `200 OK`
```json
{
  "success": true,
  "data": null,
  "message": "제조사 정보가 수정되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

### 6. 제조사 삭제

**DELETE** `/api/manufacturers/{encryptedId}`

**설명:** 제조사를 삭제합니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 제조사 ID

**응답:** `204 No Content`

**에러:**
- 생산 중인 상품이 있는 경우: `400 Bad Request`

---

## 창고 관리 API

Base URL: `/api/warehouses`

### 1. 창고 생성

**POST** `/api/warehouses`

**설명:** 새로운 창고를 등록합니다.

**요청 본문:**
```json
{
  "name": "서울 중앙 물류센터",
  "location": "서울특별시 송파구",
  "contact": "02-3333-4444"
}
```

**필드 검증:**
- `name`: 필수, 공백 불가
- `location`: 필수, 공백 불가
- `contact`: 선택, 전화번호 형식 (예: 02-1234-5678)

**응답:** `201 Created`
```json
{
  "success": true,
  "data": {
    "warehouseId": "ZW5jcnlwdGVkV2FyZWhvdXNlSWQ=",
    "name": "서울 중앙 물류센터",
    "location": "서울특별시 송파구",
    "contact": "02-3333-4444",
    "inventories": [],
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  },
  "message": "창고가 생성되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

### 2. 창고 상세 조회

**GET** `/api/warehouses/{encryptedId}`

**설명:** 특정 창고의 상세 정보를 조회합니다. 보관 중인 상품 목록도 포함됩니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 창고 ID

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "warehouseId": "ZW5jcnlwdGVkV2FyZWhvdXNlSWQ=",
    "name": "서울 중앙 물류센터",
    "location": "서울특별시 송파구",
    "contact": "02-3333-4444",
    "inventories": [
      {
        "inventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQ=",
        "productName": "갤럭시 S24",
        "quantity": 100
      }
    ],
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  }
}
```

---

### 3. 창고 목록 조회

**GET** `/api/warehouses`

**설명:** 전체 창고 목록을 페이징하여 조회합니다.

**쿼리 파라미터:**
- `page`: 페이지 번호 (기본값: 0)
- `size`: 페이지 크기 (기본값: 10)
- `sort`: 정렬 기준 (기본값: `name`)

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "warehouseId": "ZW5jcnlwdGVkV2FyZWhvdXNlSWQ=",
        "name": "서울 중앙 물류센터",
        "location": "서울특별시 송파구",
        "contact": "02-3333-4444"
      }
    ],
    "pageable": { /* 페이징 정보 */ },
    "totalElements": 20,
    "totalPages": 2
  }
}
```

---

### 4. 창고명으로 검색

**GET** `/api/warehouses/search`

**설명:** 창고명으로 부분 일치 검색합니다.

**쿼리 파라미터:**
- `name`: 검색할 창고명 (필수)
- `page`, `size`, `sort`: 페이징 파라미터

**예시 요청:**
```
GET /api/warehouses/search?name=서울&page=0&size=10
```

**응답:** `200 OK` (목록 조회와 동일한 형식)

---

### 5. 위치로 검색

**GET** `/api/warehouses/search/by-location`

**설명:** 위치로 부분 일치 검색합니다.

**쿼리 파라미터:**
- `location`: 검색할 위치 (필수)
- `page`, `size`, `sort`: 페이징 파라미터

**예시 요청:**
```
GET /api/warehouses/search/by-location?location=송파구&page=0&size=10
```

**응답:** `200 OK` (목록 조회와 동일한 형식)

---

### 6. 창고 정보 수정

**PUT** `/api/warehouses/{encryptedId}`

**설명:** 창고 정보를 수정합니다. 모든 필드는 선택적입니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 창고 ID

**요청 본문:**
```json
{
  "name": "서울 신규 물류센터",
  "location": "서울특별시 강동구",
  "contact": "02-5555-6666"
}
```

**응답:** `200 OK`
```json
{
  "success": true,
  "data": null,
  "message": "창고 정보가 수정되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

### 7. 창고 삭제

**DELETE** `/api/warehouses/{encryptedId}`

**설명:** 창고를 삭제합니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 창고 ID

**응답:** `204 No Content`

**에러:**
- 보관 중인 재고가 있는 경우: `400 Bad Request`

---

## 재고 관리 API

Base URL: `/api/inventories`

### 1. 재고 등록

**POST** `/api/inventories`

**설명:** 새로운 재고를 등록합니다 (특정 창고에 특정 상품을 등록).

**요청 본문:**
```json
{
  "encryptedProductId": "ZW5jcnlwdGVkUHJvZHVjdElk",
  "encryptedWarehouseId": "ZW5jcnlwdGVkV2FyZWhvdXNlSWQ=",
  "quantity": 100
}
```

**필드 검증:**
- `encryptedProductId`: 필수, 암호화된 상품 ID
- `encryptedWarehouseId`: 필수, 암호화된 창고 ID
- `quantity`: 필수, 0 이상의 정수

**응답:** `201 Created`
```json
{
  "success": true,
  "data": {
    "inventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQ=",
    "productId": "ZW5jcnlwdGVkUHJvZHVjdElk",
    "productName": "갤럭시 S24",
    "warehouseId": "ZW5jcnlwdGVkV2FyZWhvdXNlSWQ=",
    "warehouseName": "서울 중앙 물류센터",
    "quantity": 100,
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  },
  "message": "재고가 등록되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

---

### 2. 재고 상세 조회

**GET** `/api/inventories/{encryptedId}`

**설명:** 특정 재고의 상세 정보를 조회합니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 재고 ID

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "inventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQ=",
    "productId": "ZW5jcnlwdGVkUHJvZHVjdElk",
    "productName": "갤럭시 S24",
    "warehouseId": "ZW5jcnlwdGVkV2FyZWhvdXNlSWQ=",
    "warehouseName": "서울 중앙 물류센터",
    "quantity": 100,
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  }
}
```

---

### 3. 전체 재고 목록 조회

**GET** `/api/inventories`

**설명:** 전체 재고 목록을 페이징하여 조회합니다.

**쿼리 파라미터:**
- `page`: 페이지 번호 (기본값: 0)
- `size`: 페이지 크기 (기본값: 10)
- `sort`: 정렬 기준 (기본값: `quantity`)

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "inventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQ=",
        "productName": "갤럭시 S24",
        "warehouseName": "서울 중앙 물류센터",
        "quantity": 100
      }
    ],
    "pageable": { /* 페이징 정보 */ },
    "totalElements": 200,
    "totalPages": 20
  }
}
```

---

### 4. 상품별 재고 조회

**GET** `/api/inventories/by-product/{encryptedProductId}`

**설명:** 특정 상품의 재고를 모든 창고에 대해 조회합니다.

**경로 파라미터:**
- `encryptedProductId`: 암호화된 상품 ID

**쿼리 파라미터:**
- `page`, `size`, `sort`: 페이징 파라미터 (기본 정렬: `quantity`)

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "inventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQx",
        "productName": "갤럭시 S24",
        "warehouseName": "서울 중앙 물류센터",
        "quantity": 100
      },
      {
        "inventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQy",
        "productName": "갤럭시 S24",
        "warehouseName": "부산 물류센터",
        "quantity": 50
      }
    ],
    "totalElements": 2
  }
}
```

---

### 5. 창고별 재고 조회

**GET** `/api/inventories/by-warehouse/{encryptedWarehouseId}`

**설명:** 특정 창고의 모든 상품 재고를 조회합니다.

**경로 파라미터:**
- `encryptedWarehouseId`: 암호화된 창고 ID

**쿼리 파라미터:**
- `page`, `size`, `sort`: 페이징 파라미터 (기본 정렬: `quantity`)

**응답:** `200 OK`
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "inventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQx",
        "productName": "갤럭시 S24",
        "warehouseName": "서울 중앙 물류센터",
        "quantity": 100
      },
      {
        "inventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQy",
        "productName": "아이폰 15",
        "warehouseName": "서울 중앙 물류센터",
        "quantity": 80
      }
    ],
    "totalElements": 50
  }
}
```

---

### 6. 재고 입고

**POST** `/api/inventories/stock-in`

**설명:** 기존 재고에 수량을 추가합니다 (입고 처리).

**요청 본문:**
```json
{
  "encryptedInventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQ=",
  "quantity": 50,
  "reason": "정기 입고"
}
```

**필드 검증:**
- `encryptedInventoryId`: 필수, 암호화된 재고 ID
- `quantity`: 필수, 0보다 큰 정수
- `reason`: 선택, 입고 사유

**응답:** `200 OK`
```json
{
  "success": true,
  "data": null,
  "message": "재고가 입고되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

**처리 결과:**
- 기존 수량: 100
- 입고 수량: 50
- 최종 수량: 150

---

### 7. 재고 출고

**POST** `/api/inventories/stock-out`

**설명:** 기존 재고에서 수량을 차감합니다 (출고 처리).

**요청 본문:**
```json
{
  "encryptedInventoryId": "ZW5jcnlwdGVkSW52ZW50b3J5SWQ=",
  "quantity": 30,
  "reason": "판매 출고"
}
```

**필드 검증:**
- `encryptedInventoryId`: 필수, 암호화된 재고 ID
- `quantity`: 필수, 0보다 큰 정수
- `reason`: 선택, 출고 사유

**응답:** `200 OK`
```json
{
  "success": true,
  "data": null,
  "message": "재고가 출고되었습니다.",
  "timestamp": "2025-01-15T10:30:00"
}
```

**에러:**
- 재고 부족 시: `400 Bad Request`
```json
{
  "success": false,
  "message": "재고가 부족합니다. 현재 재고: 20, 요청 수량: 30",
  "timestamp": "2025-01-15T10:30:00"
}
```

**처리 결과:**
- 기존 수량: 150
- 출고 수량: 30
- 최종 수량: 120

---

### 8. 재고 삭제

**DELETE** `/api/inventories/{encryptedId}`

**설명:** 재고 항목을 삭제합니다.

**경로 파라미터:**
- `encryptedId`: 암호화된 재고 ID

**응답:** `204 No Content`

---

## 부록

### 전화번호 형식

전화번호는 다음 형식을 지원합니다:
- 서울 지역번호: `02-XXXX-XXXX`
- 기타 지역번호: `0XX-XXX-XXXX` 또는 `0XX-XXXX-XXXX`
- 휴대전화: `010-XXXX-XXXX`

**정규식:** `^\\d{2,3}-\\d{3,4}-\\d{4}$`

### 유니크 제약조건

**Products 테이블:**
- (name, price, manufacturer_id) 조합은 유일해야 함
- 같은 제조사가 같은 이름과 가격의 상품을 중복 등록할 수 없음

**Inventory 테이블:**
- (product_id, warehouse_id) 조합은 유일해야 함
- 같은 상품이 같은 창고에 중복 등록될 수 없음
- 동일 상품-창고 조합에 대해서는 입고/출고로만 수량 조정

---

**문서 버전:** 1.0
**최종 수정일:** 2025-10-25
**작성자:** 백종현