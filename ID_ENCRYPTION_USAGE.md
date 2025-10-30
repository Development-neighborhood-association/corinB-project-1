# ID 암호화/복호화 사용 가이드

## 개요

엔티티의 실제 ID를 클라이언트에 노출하지 않기 위해 AES 암호화를 사용합니다.

## 구성 요소

### 1. IdEncryptionUtil
- AES 대칭키 암호화를 사용한 ID 암호화/복호화 유틸리티
- Spring Bean으로 등록되어 있어 의존성 주입으로 사용 가능

### 2. application.yaml 설정
```yaml
app:
  encryption:
    secret-key: "MySecretKey12345"  # 16, 24, 또는 32 바이트
```

**⚠️ 주의사항:**
- 프로덕션 환경에서는 환경변수나 외부 설정 파일로 키를 관리하세요
- 키는 절대 Git에 커밋하지 마세요
- 키를 변경하면 기존 암호화된 ID는 복호화할 수 없습니다

## 사용 방법

### 서비스 레이어에서 사용

```java
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final IdEncryptionUtil idEncryptionUtil;

    /**
     * 상품 목록 조회 (암호화된 ID 포함)
     */
    public Page<ProductDtoForList> getProductList(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> ProductDtoForList.of(product, idEncryptionUtil));
    }

    /**
     * 암호화된 ID로 상품 조회
     */
    public ProductInfoDTO getProduct(String encryptedId) {
        // 1. 암호화된 ID를 복호화
        Long productId = idEncryptionUtil.decrypt(encryptedId);

        // 2. 실제 ID로 조회
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        return ProductInfoDTO.of(product);
    }

    /**
     * 상품 수정
     */
    public void updateProduct(String encryptedId, ProductUpdateRequest request) {
        Long productId = idEncryptionUtil.decrypt(encryptedId);

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 수정 로직
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productRepository.save(product);
    }

    /**
     * 상품 삭제
     */
    public void deleteProduct(String encryptedId) {
        Long productId = idEncryptionUtil.decrypt(encryptedId);
        productRepository.deleteById(productId);
    }
}
```

### 컨트롤러에서 사용

```java
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final IdEncryptionUtil idEncryptionUtil;

    /**
     * 상품 목록 조회
     * Response: [{"productId": "암호화된문자열", "name": "제품명", ...}]
     */
    @GetMapping
    public Page<ProductDtoForList> getProducts(Pageable pageable) {
        return productService.getProductList(pageable);
    }

    /**
     * 상품 상세 조회
     * URL: /api/products/{encryptedId}
     */
    @GetMapping("/{encryptedId}")
    public ProductInfoDTO getProduct(@PathVariable String encryptedId) {
        // ID 유효성 검증
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }

        return productService.getProduct(encryptedId);
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{encryptedId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable String encryptedId,
            @RequestBody @Valid ProductUpdateRequest request) {

        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }

        productService.updateProduct(encryptedId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{encryptedId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String encryptedId) {
        if (!idEncryptionUtil.isValid(encryptedId)) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }

        productService.deleteProduct(encryptedId);
        return ResponseEntity.noContent().build();
    }
}
```

### Thymeleaf 템플릿에서 사용

```html
<!-- 상품 목록 -->
<table>
    <thead>
        <tr>
            <th>상품명</th>
            <th>가격</th>
            <th>제조사</th>
            <th>액션</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="product : ${products}">
            <td th:text="${product.name}">상품명</td>
            <td th:text="${product.price}">가격</td>
            <td th:text="${product.manufacturer}">제조사</td>
            <td>
                <!-- 암호화된 ID를 URL에 사용 -->
                <a th:href="@{/products/{id}(id=${product.productId})}">상세보기</a>
                <a th:href="@{/products/{id}/edit(id=${product.productId})}">수정</a>
            </td>
        </tr>
    </tbody>
</table>
```

### JavaScript에서 사용

```javascript
// 상품 상세 조회
async function getProduct(encryptedId) {
    const response = await fetch(`/api/products/${encryptedId}`);
    const product = await response.json();
    console.log(product);
}

// 상품 삭제
async function deleteProduct(encryptedId) {
    const response = await fetch(`/api/products/${encryptedId}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        alert('삭제되었습니다.');
    }
}
```

## 장점

1. **보안 강화**: 실제 데이터베이스 ID를 클라이언트에 노출하지 않음
2. **ID 예측 방지**: 순차적인 ID 패턴을 숨겨 무작위 접근 방지
3. **URL 난독화**: URL이 의미 없는 문자열로 표시됨

## 단점 및 고려사항

1. **성능**: 암호화/복호화 연산 비용 발생 (하지만 매우 작음)
2. **URL 길이**: 암호화된 ID가 숫자 ID보다 길어짐
3. **캐싱**: 같은 ID는 항상 같은 암호화 값을 생성하므로 캐싱 가능
4. **키 관리**: 암호화 키 변경 시 기존 URL이 모두 무효화됨

## 테스트

테스트 코드는 `IdEncryptionUtilTest.java` 참조:

```bash
# 테스트 실행
./gradlew test --tests "org.example.wms.util.IdEncryptionUtilTest"
```

## 환경별 키 관리

### 개발 환경
```yaml
# application-dev.yaml
app:
  encryption:
    secret-key: "MyDevSecretKey16"
```

### 프로덕션 환경 (환경변수 사용)
```yaml
# application-prod.yaml
app:
  encryption:
    secret-key: ${ENCRYPTION_SECRET_KEY}
```

```bash
# 환경변수 설정
export ENCRYPTION_SECRET_KEY="ProductionKey123456"
```

## 추가 엔티티에 적용

다른 엔티티(Manufacturer, Warehouse, Inventory)에도 동일한 패턴 적용 가능:

```java
// ManufacturerDtoForList
public static ManufacturerDtoForList of(ManufacturerEntity manufacturer, IdEncryptionUtil util) {
    return ManufacturerDtoForList.builder()
            .manufacturerId(util.encrypt(manufacturer.getManufacturerId()))
            .companyName(manufacturer.getCompanyName())
            .build();
}
```