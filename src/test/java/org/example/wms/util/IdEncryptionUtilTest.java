package org.example.wms.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdEncryptionUtilTest {

    private IdEncryptionUtil encryptionUtil;

    @BeforeEach
    void setUp() {
        // 16바이트 키로 테스트
        encryptionUtil = new IdEncryptionUtil("MySecretKey12345");
    }

    @Test
    @DisplayName("ID 암호화 및 복호화가 정상 동작한다")
    void testEncryptAndDecrypt() {
        // Given
        Long originalId = 12345L;

        // When
        String encrypted = encryptionUtil.encrypt(originalId);
        Long decrypted = encryptionUtil.decrypt(encrypted);

        // Then
        assertNotNull(encrypted);
        assertNotEquals(originalId.toString(), encrypted);
        assertEquals(originalId, decrypted);
    }

    @Test
    @DisplayName("같은 ID를 암호화하면 항상 같은 결과가 나온다")
    void testEncryptionConsistency() {
        // Given
        Long id = 100L;

        // When
        String encrypted1 = encryptionUtil.encrypt(id);
        String encrypted2 = encryptionUtil.encrypt(id);

        // Then
        assertEquals(encrypted1, encrypted2);
    }

    @Test
    @DisplayName("다른 ID는 다른 암호화 결과를 생성한다")
    void testDifferentIdsProduceDifferentEncryption() {
        // Given
        Long id1 = 100L;
        Long id2 = 200L;

        // When
        String encrypted1 = encryptionUtil.encrypt(id1);
        String encrypted2 = encryptionUtil.encrypt(id2);

        // Then
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    @DisplayName("null ID를 암호화하면 null을 반환한다")
    void testEncryptNull() {
        // When
        String encrypted = encryptionUtil.encrypt(null);

        // Then
        assertNull(encrypted);
    }

    @Test
    @DisplayName("null 문자열을 복호화하면 null을 반환한다")
    void testDecryptNull() {
        // When
        Long decrypted = encryptionUtil.decrypt(null);

        // Then
        assertNull(decrypted);
    }

    @Test
    @DisplayName("빈 문자열을 복호화하면 null을 반환한다")
    void testDecryptEmptyString() {
        // When
        Long decrypted = encryptionUtil.decrypt("");

        // Then
        assertNull(decrypted);
    }

    @Test
    @DisplayName("잘못된 암호화 문자열을 복호화하면 예외가 발생한다")
    void testDecryptInvalidString() {
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            encryptionUtil.decrypt("invalid-encrypted-string");
        });
    }

    @Test
    @DisplayName("유효한 암호화 ID는 검증을 통과한다")
    void testIsValidWithValidId() {
        // Given
        Long id = 999L;
        String encrypted = encryptionUtil.encrypt(id);

        // When
        boolean valid = encryptionUtil.isValid(encrypted);

        // Then
        assertTrue(valid);
    }

    @Test
    @DisplayName("잘못된 암호화 ID는 검증을 통과하지 못한다")
    void testIsValidWithInvalidId() {
        // When
        boolean valid = encryptionUtil.isValid("invalid-id");

        // Then
        assertFalse(valid);
    }

    @Test
    @DisplayName("큰 숫자 ID도 정상적으로 암호화/복호화된다")
    void testEncryptLargeNumber() {
        // Given
        Long largeId = 999999999999L;

        // When
        String encrypted = encryptionUtil.encrypt(largeId);
        Long decrypted = encryptionUtil.decrypt(encrypted);

        // Then
        assertEquals(largeId, decrypted);
    }
}