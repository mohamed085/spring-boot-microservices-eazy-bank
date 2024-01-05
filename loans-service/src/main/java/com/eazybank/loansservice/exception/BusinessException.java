package com.eazybank.loansservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private String message;
    private String errorCode;
    private HttpStatus httpStatus;

    public static RuntimeException buildNotFound(String resourceName, String fieldName, String fieldValue) {
        return BusinessException
                .builder()
                .message(String.format("%s not found with the given input data %s : '%s'", resourceName, fieldName, fieldValue))
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }
}
