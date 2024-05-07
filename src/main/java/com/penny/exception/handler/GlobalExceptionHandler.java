package com.penny.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理參數驗證失敗的例外，例如 @Valid 驗證不通過時拋出的 MethodArgumentNotValidException。
     * @param exception 驗證失敗的例外
     * @return 返回包含錯誤訊息的 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception) {
        // 取得所有錯誤訊息
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();

        // 將錯誤訊息串流化，轉換為字串格式的錯誤訊息
        String errorMessage = errors.stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        // 若是 FieldError，取得欄位名稱和錯誤訊息
                        return "Field '" + ((FieldError) error).getField() + "': " + error.getDefaultMessage();
                    } else {
                        // 否則取得默認的錯誤訊息
                        return error.getDefaultMessage();
                    }
                })
                // 將所有錯誤訊息用 ";\n" 分隔並串接成最終的錯誤訊息字串
                .collect(Collectors.joining(";\n"));

        // 回傳 HTTP 狀態碼為 BAD_REQUEST 的 ResponseEntity 物件，包含錯誤訊息內容
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
