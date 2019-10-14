package com.example.demo.error;

public class ErrorInfo<T> {

    public static final Integer SUCCESS = 200;
    public static final Integer ERROR = 100;

    // 錯誤信息
    private Integer code;
    // 錯誤碼
    private String message;
    private String uri;
    private T data;

    public static Integer getSUCCESS() {
        return SUCCESS;
    }

    public static Integer getERROR() {
        return ERROR;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
