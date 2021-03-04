package com.mafgwo.prototype.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常切面
 *
 * @author chenxiaoqi
 * @since 2020/10/24
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException e) {
        log.error("异常信息", e);
        Map<String, String> returnMap = new HashMap<>(1);
        returnMap.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnMap);
    }

}
