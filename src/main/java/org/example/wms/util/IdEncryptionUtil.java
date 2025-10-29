package org.example.wms.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 엔티티 ID 암호화/복호화 유틸리티
 * AES 대칭키 암호화를 사용하여 클라이언트에 노출되는 ID를 안전하게 처리
 */
@Component
public class IdEncryptionUtil {

    private static final String ALGORITHM = "AES";
    private final SecretKeySpec secretKey;

    public IdEncryptionUtil(@Value("${app.encryption.secret-key}") String secretKeyString) {
        // 16, 24, 32 바이트 키만 허용 (AES-128, AES-192, AES-256)
        byte[] key = secretKeyString.getBytes(StandardCharsets.UTF_8);
        if (key.length != 16 && key.length != 24 && key.length != 32) {
            throw new IllegalArgumentException(
                    "암호화 키는 16, 24, 또는 32 바이트여야 합니다. 현재: " + key.length + " 바이트"
            );
        }
        this.secretKey = new SecretKeySpec(key, ALGORITHM);
    }

    /**
     * Long ID를 암호화된 문자열로 변환
     * @param id 엔티티 ID
     * @return Base64로 인코딩된 암호화 문자열
     */
    public String encrypt(Long id) {
        if (id == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(id.toString().getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("ID 암호화 실패: " + id, e);
        }
    }

    /**
     * 암호화된 문자열을 Long ID로 복호화
     * @param encryptedId Base64로 인코딩된 암호화 문자열
     * @return 원본 ID
     */
    public Long decrypt(String encryptedId) {
        if (encryptedId == null || encryptedId.isEmpty()) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getUrlDecoder().decode(encryptedId);
            byte[] decrypted = cipher.doFinal(decoded);
            String decryptedString = new String(decrypted, StandardCharsets.UTF_8);
            return Long.parseLong(decryptedString);
        } catch (Exception e) {
            throw new RuntimeException("ID 복호화 실패: " + encryptedId, e);
        }
    }

    /**
     * 암호화된 ID의 유효성 검증
     * @param encryptedId 검증할 암호화 ID
     * @return 유효하면 true
     */
    public boolean isValid(String encryptedId) {
        try {
            Long id = decrypt(encryptedId);
            return id != null && id > 0;
        } catch (Exception e) {
            return false;
        }
    }
}