package org.example.wms.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.wms.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 모든 Controller에서 발생하는 예외를 일관된 형식으로 처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * IllegalArgumentException 처리
     * 리소스를 찾을 수 없거나 잘못된 인자가 전달된 경우
     *
     * @param e 예외
     * @return 404 NOT_FOUND 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException 발생: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage() != null ? e.getMessage() : "리소스를 찾을 수 없습니다."));
    }

    /**
     * DataIntegrityViolationException 처리
     * 데이터베이스 제약 조건 위반 (외래키, 유니크 제약 등)
     *
     * @param e 예외
     * @return 409 CONFLICT 응답
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("DataIntegrityViolationException 발생: {}", e.getMessage());

        // 사용자 친화적인 메시지 제공
        String message = "데이터 무결성 위반";
        if (e.getMessage() != null) {
            if (e.getMessage().contains("foreign key constraint")) {
                message = "연관된 데이터가 존재하여 작업을 수행할 수 없습니다.";
            } else if (e.getMessage().contains("unique constraint") || e.getMessage().contains("Duplicate entry")) {
                message = "중복된 데이터가 존재합니다.";
            }
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(message));
    }

    /**
     * MethodArgumentNotValidException 처리
     * Request Body의 유효성 검증 실패 (@Valid 검증 실패)
     *
     * @param e 예외
     * @return 400 BAD_REQUEST 응답 (필드별 에러 메시지 포함)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException 발생: {}", e.getMessage());

        // 필드별 에러 메시지 수집
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .data(errors)
                        .message("입력값이 유효하지 않습니다.")
                        .build());
    }

    /**
     * 예상치 못한 모든 예외 처리
     *
     * @param e 예외
     * @return 500 INTERNAL_SERVER_ERROR 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("예상치 못한 예외 발생", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("서버 내부 오류가 발생했습니다."));
    }
}