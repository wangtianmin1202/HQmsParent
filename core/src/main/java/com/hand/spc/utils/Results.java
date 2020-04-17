package com.hand.spc.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unchecked")
public class Results {

    private Results() {}

    @SuppressWarnings("rawtypes")
    private static final ResponseEntity NO_CONTENT = new ResponseEntity<>(HttpStatus.NO_CONTENT);
    @SuppressWarnings("rawtypes")
    private static final ResponseEntity ERROR = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    @SuppressWarnings("rawtypes")
    private static final ResponseEntity INVALID = new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    public static <T> ResponseEntity<T> success(T data) {
        if (data == null) {
            return NO_CONTENT;
        }
        return ResponseEntity.ok(data);
    }

    public static <T> ResponseEntity<T> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    public static <T> ResponseEntity<T> success() {
        return NO_CONTENT;
    }

    public static <T> ResponseEntity<T> invalid() {
        return INVALID;
    }

    public static <T> ResponseEntity<T> invalid(T data) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
    }

    public static <T> ResponseEntity<T> error() {
        return ERROR;
    }

    public static <T> ResponseEntity<T> error(T data) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
    }

    public static <T> ResponseEntity<T> newResult(int code) {
        return ResponseEntity.status(HttpStatus.valueOf(code)).build();
    }

    public static <T> ResponseEntity<T> newResult(int code, T data) {
        return ResponseEntity.status(HttpStatus.valueOf(code)).body(data);
    }

}
