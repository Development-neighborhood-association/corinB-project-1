# WMS (Warehouse Management System)

![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=flat-square&logo=spring-boot)
![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=flat-square&logo=mysql)
![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?style=flat-square&logo=gradle)
![Status](https://img.shields.io/badge/Status-Completed-success?style=flat-square)

물류창고의 상품 재고를 관리하기 위한 Spring Boot 기반 REST API 애플리케이션

## 📋 목차

1. [프로젝트 소개](#-프로젝트-소개)
2. [기술 스택](#-기술-스택)
3. [시작하기](#-시작하기)
4. [API 문서](#-api-문서)
5. [개발 현황](#-개발-현황)
6. [도메인 모델](#-도메인-모델)
7. [구현된 기능](#-구현된-기능)
8. [보안 기능](#-보안-기능)
9. [프로젝트 구조](#-프로젝트-구조)
10. [개발 가이드](#-개발-가이드)
11. [문서](#-문서)

---

## 🎯 프로젝트 소개

WMS(Warehouse Management System)는 물류창고의 상품과 재고를 효율적으로 관리하기 위한 REST API 기반 백엔드 시스템입니다.

### 주요 특징

✨ **완전한 CRUD API** - 상품, 제조사, 창고, 재고에 대한 전체 생명주기 관리
🔐 **보안 강화** - AES 암호화를 통한 엔티티 ID 보호
📊 **고급 검색** - 다양한 조건을 활용한 필터링 및 페이징 지원
⚡ **성능 최적화** - N+1 문제 해결 및 효율적인 쿼리 설계
🧪 **높은 테스트 커버리지** - 모든 서비스 레이어에 대한 단위 테스트
📝 **표준화된 응답** - 일관된 API 응답 형식

## 🛠 기술 스택

### Backend
- **Java 21** - LTS 버전
- **Spring Boot 3.5.7** - 최신 Spring Boot 프레임워크
- **Spring Data JPA** - ORM 및 데이터 액세스 계층
- **Hibernate** - JPA 구현체
- **Lombok** - 보일러플레이트 코드 최소화

### Frontend
- **Thymeleaf** - 서버 사이드 템플릿 엔진

### Database
- **MySQL 8.x** - 관계형 데이터베이스

### Build Tool
- **Gradle 8.x** - 빌드 자동화 도구

## 🚀 시작하기

### 사전 요구사항

- Java 21 이상
- MySQL 8.x
- Gradle 8.x (또는 포함된 Gradle Wrapper 사용)

### 데이터베이스 설정

1. MySQL 서버 시작 (포트 3307)
2. 데이터베이스 및 사용자 생성:

```sql
CREATE DATABASE wms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'wms_user'@'localhost' IDENTIFIED BY 'wms1234';
GRANT ALL PRIVILEGES ON wms_db.* TO 'wms_user'@'localhost';
FLUSH PRIVILEGES;
```

3. 스키마 생성 (DDL 모드가 `validate`이므로 사전에 스키마 필요)

### 빌드 및 실행

```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun

# 클린 빌드
./gradlew clean build
```

### 테스트 실행

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "org.example.wms.service.ProductServiceTest"

# 특정 테스트 메서드 실행
./gradlew test --tests "org.example.wms.service.ProductServiceTest.createProduct"
```

### 애플리케이션 접속

애플리케이션 실행 후:
- **API Base URL**: `http://localhost:8080/api`
- **Swagger UI**: (구현 예정)

---

## 📚 API 문서

### REST API 엔드포인트

#### 1. 상품 관리 (`/api/products`)
- `POST /api/products` - 상품 생성
- `GET /api/products/{id}` - 상품 상세 조회
- `GET /api/products` - 상품 목록 조회 (페이징)
- `GET /api/products/search?name={name}` - 상품명 검색
- `GET /api/products/search/by-manufacturer?manufacturer={name}` - 제조사명으로 상품 검색
- `GET /api/products/search/by-price-range?minPrice={min}&maxPrice={max}` - 가격 범위 검색
- `PUT /api/products/{id}` - 상품 정보 수정
- `DELETE /api/products/{id}` - 상품 삭제

#### 2. 제조사 관리 (`/api/manufacturers`)
- `POST /api/manufacturers` - 제조사 생성
- `GET /api/manufacturers/{id}` - 제조사 상세 조회 (생산 상품 포함)
- `GET /api/manufacturers` - 제조사 목록 조회 (페이징)
- `GET /api/manufacturers/search?companyName={name}` - 회사명 검색
- `PUT /api/manufacturers/{id}` - 제조사 정보 수정
- `DELETE /api/manufacturers/{id}` - 제조사 삭제

#### 3. 창고 관리 (`/api/warehouses`)
- `POST /api/warehouses` - 창고 생성
- `GET /api/warehouses/{id}` - 창고 상세 조회 (보관 상품 포함)
- `GET /api/warehouses` - 창고 목록 조회 (페이징)
- `GET /api/warehouses/search?name={name}` - 창고명 검색
- `GET /api/warehouses/search/by-location?location={location}` - 위치 검색
- `PUT /api/warehouses/{id}` - 창고 정보 수정
- `DELETE /api/warehouses/{id}` - 창고 삭제

#### 4. 재고 관리 (`/api/inventories`)
- `POST /api/inventories` - 재고 등록
- `GET /api/inventories/{id}` - 재고 상세 조회
- `GET /api/inventories` - 전체 재고 목록 조회 (페이징)
- `GET /api/inventories/by-product/{productId}` - 상품별 재고 조회
- `GET /api/inventories/by-warehouse/{warehouseId}` - 창고별 재고 조회
- `POST /api/inventories/stock-in` - 재고 입고 (수량 증가)
- `POST /api/inventories/stock-out` - 재고 출고 (수량 감소)
- `DELETE /api/inventories/{id}` - 재고 삭제

### 표준 응답 형식

```json
{
  "success": true,
  "data": { /* 실제 데이터 */ },
  "message": "성공 메시지",
  "timestamp": "2025-01-15T10:30:00"
}
```

**자세한 API 명세는 [API_SPECIFICATION.md](./API_SPECIFICATION.md)를 참조하세요.**

---

## ✅ 개발 현황

### 완료된 구성 요소

#### ✅ Domain Layer (Entity)
- `ProductEntity` - 상품 엔티티
- `ManufacturerEntity` - 제조사 엔티티
- `WarehouseEntity` - 물류창고 엔티티
- `InventoryEntity` - 재고 엔티티

#### ✅ Repository Layer
- `ProductRepository` - 상품 데이터 액세스 (N+1 방지, 커스텀 쿼리)
- `ManufacturerRepository` - 제조사 데이터 액세스
- `WarehouseRepository` - 창고 데이터 액세스
- `InventoryRepository` - 재고 데이터 액세스

#### ✅ Service Layer
- `ProductService` - 상품 비즈니스 로직
- `ManufacturerService` - 제조사 비즈니스 로직
- `WarehouseService` - 창고 비즈니스 로직
- `InventoryService` - 재고 비즈니스 로직

#### ✅ DTO Layer
- `dto/info` - 상세 정보 DTO (ProductInfoDTO, ManufacturerInfoDTO 등)
- `dto/list` - 목록 조회 DTO (ProductListDTO, WarehouseListDTO 등)
- `dto/crud` - 생성/수정 요청 DTO (ProductCreateRequest, StockInRequest 등)
- `ApiResponse` - 표준 API 응답 래퍼

#### ✅ Presentation Layer (Controller)
- `ProductController` - 상품 관리 REST API
- `ManufactureController` - 제조사 관리 REST API
- `WarehouseController` - 창고 관리 REST API
- `InventoryController` - 재고 관리 REST API
- `GlobalExceptionHandler` - 전역 예외 처리

#### ✅ Utility & Configuration
- `IdEncryptionUtil` - AES 기반 ID 암호화/복호화
- `AppConfig` - 애플리케이션 설정

#### ✅ Test Layer
- Service Layer 단위 테스트 (100% 완료)
- Utility 단위 테스트

### 프로젝트 완료 상태

🎉 **모든 계층 구현 완료**
- ✅ Domain Layer
- ✅ Repository Layer
- ✅ Service Layer
- ✅ DTO Layer
- ✅ Controller Layer (REST API)
- ✅ Utility & Configuration
- ✅ Test Layer

## 📊 도메인 모델

### 엔티티 관계도

```
Manufacturer (제조사) 1 ──────< * Product (상품)
                                      |
                                      *
                                      |
                              Inventory (재고)
                                      |
                                      *
                                      |
Warehouse (물류창고) 1 ──────< *
```

### 핵심 엔티티

#### 1. Product (상품)
- 상품의 기본 정보 관리
- 제조사와 N:1 관계
- 재고와 양방향 1:N 관계
- 유니크 제약조건: (상품명, 가격, 제조사) 조합

#### 2. Manufacturer (제조사)
- 제조사 정보 관리
- 상품과 양방향 1:N 관계
- 회사명, 위치, 연락처, 이메일 정보

#### 3. Warehouse (물류창고)
- 물류창고 위치 및 정보 관리
- 재고와 양방향 1:N 관계
- 창고명, 위치, 연락처 정보

#### 4. Inventory (재고)
- 상품-창고 간 재고 수량 관리
- 상품과 N:1 관계
- 창고와 N:1 관계
- 유니크 제약조건: (상품 ID, 창고 ID) 조합
- 비즈니스 메서드: `addQuantity()`, `removeQuantity()`

## 🎯 구현된 기능

### 상품 관리 (ProductService)
- ✅ 상품 생성 - `createProduct(ProductCreateRequest)`
- ✅ 상품 상세 조회 - `getProduct(String encryptedId)`
- ✅ 상품 목록 조회 (페이징) - `getAllProducts(Pageable)`
- ✅ 상품명으로 검색 - `searchByName(String name, Pageable)`
- ✅ 제조사명으로 검색 - `searchByManufacturerName(String manufacturerName, Pageable)`
- ✅ 가격 범위로 검색 - `searchByPriceRange(Double minPrice, Double maxPrice, Pageable)`
- ✅ 상품 정보 수정 - `updateProduct(String encryptedId, ProductUpdateRequest)`
- ✅ 상품 삭제 - `deleteProduct(String encryptedId)`

### 제조사 관리 (ManufacturerService)
- ✅ 제조사 생성 - `createManufacturer(ManufacturerCreateRequest)`
- ✅ 제조사 상세 조회 (생산 상품 목록 포함) - `getManufacturer(String encryptedId)`
- ✅ 제조사 목록 조회 (페이징) - `getAllManufacturers(Pageable)`
- ✅ 회사명으로 검색 - `searchByCompanyName(String companyName, Pageable)`
- ✅ 제조사 정보 수정 - `updateManufacturer(String encryptedId, ManufacturerUpdateRequest)`
- ✅ 제조사 삭제 - `deleteManufacturer(String encryptedId)`

### 물류창고 관리 (WarehouseService)
- ✅ 창고 생성 - `createWarehouse(WarehouseCreateRequest)`
- ✅ 창고 상세 조회 (보관 상품 목록 포함) - `getWarehouse(String encryptedId)`
- ✅ 창고 목록 조회 (페이징) - `getAllWarehouses(Pageable)`
- ✅ 창고명으로 검색 - `searchByName(String name, Pageable)`
- ✅ 위치로 검색 - `searchByLocation(String location, Pageable)`
- ✅ 창고 정보 수정 - `updateWarehouse(String encryptedId, WarehouseUpdateRequest)`
- ✅ 창고 삭제 - `deleteWarehouse(String encryptedId)`

### 재고 관리 (InventoryService)
- ✅ 초기 재고 등록 - `createInventory(InventoryCreateRequest)`
- ✅ 재고 상세 조회 - `getInventory(String encryptedId)`
- ✅ 전체 재고 목록 조회 (페이징) - `getAllInventories(Pageable)`
- ✅ 상품별 재고 조회 - `getInventoriesByProduct(String encryptedProductId, Pageable)`
- ✅ 창고별 재고 조회 - `getInventoriesByWarehouse(String encryptedWarehouseId, Pageable)`
- ✅ 재고 입고 (수량 증가) - `stockIn(StockInRequest)`
- ✅ 재고 출고 (수량 감소) - `stockOut(StockOutRequest)`
- ✅ 재고 삭제 - `deleteInventory(String encryptedId)`

## 🔐 보안 기능

### ID 암호화
실제 데이터베이스 ID를 클라이언트에 노출하지 않기 위해 AES 암호화 적용:

- **IdEncryptionUtil**: Long 타입 ID ↔ 암호화된 문자열 변환
- **암호화 알고리즘**: AES-128/192/256 (키 길이에 따라)
- **인코딩**: Base64 URL-safe encoding
- **DTO 패턴**:
  - `ProductDtoForList.of(entity, idEncryptionUtil)` - 암호화된 ID 사용
  - `ProductDtoForList.ofWithoutEncryption(entity)` - 일반 ID 사용 (테스트용)

#### 설정 방법

`application.yaml`:
```yaml
app:
  encryption:
    secret-key: "MySecretKey12345"  # 16, 24, 또는 32 바이트
```

⚠️ **주의**: 프로덕션 환경에서는 환경변수로 관리 필요

자세한 사용법은 [ID_ENCRYPTION_USAGE.md](./ID_ENCRYPTION_USAGE.md) 참조

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/org/example/wms/
│   │   ├── entity/          # 엔티티 클래스 (Domain Layer)
│   │   │   ├── ProductEntity.java
│   │   │   ├── ManufacturerEntity.java
│   │   │   ├── WarehouseEntity.java
│   │   │   └── InventoryEntity.java
│   │   ├── repository/      # 데이터 액세스 레이어 (Repository Layer)
│   │   │   ├── ProductRepository.java
│   │   │   ├── ManufacturerRepository.java
│   │   │   ├── WarehouseRepository.java
│   │   │   └── InventoryRepository.java
│   │   ├── service/         # 비즈니스 로직 레이어 (Service Layer)
│   │   │   ├── ProductService.java
│   │   │   ├── ManufacturerService.java
│   │   │   ├── WarehouseService.java
│   │   │   └── InventoryService.java
│   │   ├── dto/             # 데이터 전송 객체 (DTO Layer)
│   │   │   ├── info/        # 상세 정보 DTO
│   │   │   │   ├── ProductInfoDTO.java
│   │   │   │   ├── ManufacturerInfoDTO.java
│   │   │   │   ├── WarehouseInfoDTO.java
│   │   │   │   └── InventoryInfoDTO.java
│   │   │   ├── list/        # 목록 조회 DTO
│   │   │   │   ├── ProductListDTO.java
│   │   │   │   ├── ManufacturerListDTO.java
│   │   │   │   ├── WarehouseListDTO.java
│   │   │   │   └── InventoryListDTO.java
│   │   │   └── crud/        # 생성/수정 요청 DTO
│   │   │       ├── ProductCreateRequest.java
│   │   │       ├── ProductUpdateRequest.java
│   │   │       ├── ManufacturerCreateRequest.java
│   │   │       ├── ManufacturerUpdateRequest.java
│   │   │       ├── WarehouseCreateRequest.java
│   │   │       ├── WarehouseUpdateRequest.java
│   │   │       ├── InventoryCreateRequest.java
│   │   │       ├── StockInRequest.java
│   │   │       └── StockOutRequest.java
│   │   ├── controller/      # REST API 컨트롤러
│   │   │   ├── ProductController.java
│   │   │   ├── ManufactureController.java
│   │   │   ├── WarehouseController.java
│   │   │   └── InventoryController.java
│   │   ├── exception/       # 예외 처리
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── util/            # 유틸리티 클래스
│   │   │   └── IdEncryptionUtil.java
│   │   ├── config/          # 설정 클래스
│   │   │   └── AppConfig.java
│   │   └── WmsApplication.java
│   └── resources/
│       └── application.yaml  # 애플리케이션 설정
└── test/                    # 테스트 코드
    └── java/org/example/wms/
        ├── service/         # Service Layer 테스트
        │   ├── ProductServiceTest.java
        │   ├── ManufacturerServiceTest.java
        │   ├── WarehouseServiceTest.java
        │   └── InventoryServiceTest.java
        └── util/
            └── IdEncryptionUtilTest.java
```

## 📖 개발 가이드

### 엔티티 작성 규칙

```java
@Entity
@Table(name = "table_name")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "entityId")  // ID 필드로 동등성 비교
public class SampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entityId;

    // 타임스탬프 자동 관리
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 컬렉션은 Builder.Default로 초기화
    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<ChildEntity> children = new ArrayList<>();
}
```

### Repository 작성 규칙

```java
public interface SampleRepository extends JpaRepository<SampleEntity, Long> {

    // N+1 문제 방지: EntityGraph 사용
    @EntityGraph(attributePaths = {"relatedEntity"})
    Optional<SampleEntity> findById(Long id);

    // Native SQL은 상수로 정의
    String FIND_BY_NAME_SQL = """
        SELECT * FROM table_name
        WHERE name LIKE CONCAT('%', :name, '%')
        """;

    @Query(value = FIND_BY_NAME_SQL, nativeQuery = true)
    Page<SampleEntity> findByNameContaining(@Param("name") String name, Pageable pageable);
}
```

### DTO 작성 규칙

```java
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SampleDTO {
    private String encryptedId;  // 암호화된 ID
    private String name;

    // 정적 팩토리 메서드 - 암호화 버전
    public static SampleDTO of(SampleEntity entity, IdEncryptionUtil util) {
        return SampleDTO.builder()
                .encryptedId(util.encrypt(entity.getEntityId()))
                .name(entity.getName())
                .build();
    }

    // 정적 팩토리 메서드 - 비암호화 버전 (테스트/내부용)
    public static SampleDTO ofWithoutEncryption(SampleEntity entity) {
        return SampleDTO.builder()
                .encryptedId(entity.getEntityId().toString())
                .name(entity.getName())
                .build();
    }
}
```

### Service Layer 작성 규칙

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 클래스 레벨: 읽기 전용
public class SampleService {

    private final SampleRepository repository;
    private final IdEncryptionUtil idEncryptionUtil;

    /**
     * 생성 (트랜잭션 필요)
     */
    @Transactional
    public SampleInfoDTO create(SampleCreateRequest request) {
        SampleEntity entity = SampleEntity.builder()
                .name(request.getName())
                .build();

        SampleEntity saved = repository.saveAndFlush(entity);
        return SampleInfoDTO.of(saved, idEncryptionUtil);
    }

    /**
     * 조회 (읽기 전용 트랜잭션)
     */
    public SampleInfoDTO get(String encryptedId) {
        Long id = idEncryptionUtil.decrypt(encryptedId);
        SampleEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리소스를 찾을 수 없습니다."));
        return SampleInfoDTO.of(entity, idEncryptionUtil);
    }

    /**
     * 수정 (트랜잭션 필요)
     */
    @Transactional
    public void update(String encryptedId, SampleUpdateRequest request) {
        Long id = idEncryptionUtil.decrypt(encryptedId);
        SampleEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리소스를 찾을 수 없습니다."));

        // Dirty Checking을 활용한 업데이트
        if (request.getName() != null) {
            entity.setName(request.getName());
        }
    }
}
```

### 주요 개발 원칙

1. **Lazy Loading**: 모든 연관관계는 `FetchType.LAZY` 사용
2. **N+1 방지**: `@EntityGraph` 적극 활용
3. **타임스탬프 자동화**: `@CreationTimestamp`, `@UpdateTimestamp` 사용
4. **빌더 패턴**: Lombok `@Builder` 사용, 컬렉션은 `@Builder.Default`로 초기화
5. **유니크 제약조건**: `@UniqueConstraint`에 명시적 이름 부여
6. **ID 암호화**: 클라이언트 노출 DTO는 암호화된 ID 사용
7. **트랜잭션 관리**: 클래스 레벨 `@Transactional(readOnly = true)`, 쓰기 메서드는 `@Transactional`
8. **예외 처리**: `IllegalArgumentException` (리소스 없음), `DataIntegrityViolationException` (무결성 위반)

---

## 📝 문서

프로젝트와 관련된 상세 문서는 아래를 참조하세요:

### API 문서
- **[API_SPECIFICATION.md](./API_SPECIFICATION.md)** - 전체 REST API 명세서
  - 모든 엔드포인트 상세 설명
  - 요청/응답 예시
  - HTTP 상태 코드
  - 에러 처리 가이드

### 보안 문서
- **[ID_ENCRYPTION_USAGE.md](./ID_ENCRYPTION_USAGE.md)** - ID 암호화 사용 가이드
  - IdEncryptionUtil 사용법
  - 환경별 키 관리
  - DTO 변환 패턴

---

**개발 환경**: Java 21 | Spring Boot 3.5.7 | MySQL 8.x | Gradle 8.x

**프로젝트 완료일**: 2025년 10월 30일