package com.qyh.demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qyh.demo.enums.CodeEnum;
import lombok.Data;

import java.io.Serializable;

import static com.qyh.demo.enums.CodeEnum.SUCCESSFUL;


/**
 * ResponseResult
 * <p>
 * Create By cary 2012692013@qq.com
 * 2018-07-12 13:41
 **/
@Data
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 5935352669960485205L;
    @JsonProperty("code")
    private int code;
    private String msg;
    private T data;
    public ResponseResult() {
    }

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    private static final ResponseResult EMPTY_SUCCESS = of(SUCCESSFUL.codeOf(), SUCCESSFUL.msgOf(), null);
    private static final ResponseResult EMPTY_ERROR = of(CodeEnum.ERROR.codeOf(), CodeEnum.ERROR.msgOf(), null);
    private static final ResponseResult ERR_UNDEFINED = of(CodeEnum.UNDEFINED.codeOf(),  CodeEnum.UNDEFINED.msgOf(), null);

    private static<T> ResponseResult<T> of(Integer code,  String msg, T data) {
        return new ResponseResult<>(code, msg, data);
    }

    public static ResponseResult success() {
        return EMPTY_SUCCESS;
    }

    public static ResponseResult success(Integer code, String message) {
        return of(code, message, null);
    }

    public static<T> ResponseResult success(String message, T data){return of(SUCCESSFUL.codeOf(),message,data);}

    public static<T> ResponseResult<T> success(T data) {
        return of(SUCCESSFUL.codeOf(), SUCCESSFUL.msgOf(), data);
    }

    public static ResponseResult undefined() {
        return ERR_UNDEFINED;
    }

    public static ResponseResult error(CodeEnum code) {
        return of(code.codeOf(), null, code.msgOf());
    }

    public static ResponseResult error(Integer code, String message) {
        return of(code, message, null);
    }

    public static ResponseResult error( String message) {
        return of(CodeEnum.FAIL.codeOf(), message, null);
    }

    public static ResponseResult error() {
        return of(CodeEnum.FAIL.codeOf(), "参数未传", null);
    }

    public static ResponseResult update(Integer i){
        if (i<1)
            return error(400,"操作失败,请刷新后重试");
        return success(200,"操作成功");
    }

    public static<T> ResponseResult update(Integer i,T successData){
        if (i<1)
            return error(400,"操作失败,请刷新后重试");
        return success(successData);
    }
}
