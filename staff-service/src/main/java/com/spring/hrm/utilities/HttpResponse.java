package com.spring.hrm.utilities;

import java.time.LocalDateTime;
import static com.spring.hrm.utilities.AppResponseStatus.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter @Setter
public class HttpResponse<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    private LocalDateTime timeStamp;
    private String statusCode;
    private String statusMessage;
    private T data;


    private HttpResponse(AppResponseStatus status, T data) {
        this.timeStamp = LocalDateTime.now();
        this.statusCode = status.getCode();
        // this.statusMessage = status.getMessage();
        this.data = data;
    }
    private HttpResponse(AppResponseStatus status) {
        this.timeStamp = LocalDateTime.now();
        this.statusCode = status.getCode();
        // this.statusMessage = status.getMessage();
    }
    private HttpResponse(String code, String message) {
        this.timeStamp = LocalDateTime.now();
        this.statusCode = code;
        this.statusMessage = message;
    }

    private HttpResponse(){}

    public static <T> HttpResponse<T> ok(T data) {
        return new HttpResponse<T>(APP_200,  data);
    }
    public static <T> HttpResponse<T> created() {
        return new HttpResponse<T>(APP_201);
    }
    public static <T> HttpResponse<T> noContent() {
        return new HttpResponse<T>(APP_204);
    }
    public static <T> HttpResponse<T> error(AppResponseStatus status) {
        return new HttpResponse<T>(status);
    }
    public static <T> HttpResponse<T> error(String code, String message) {
        return new HttpResponse<T>(code, message);
    }
}
