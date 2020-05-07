package com.qyh.demo.enums;

/**
 * CodeEnum
 * <p>
 * Create By cary 2012692013@qq.com
 * 2018-07-12 13:18
 **/
public enum CodeEnum {

    //HTTP
    NO_HANDLER(404, "请求不存在"),
    SUCCESSFUL(200, "请求成功"),
    FAIL(888,"请求失败"),
    ERROR(403, "请求失败"),
    OPT_NO_SUPPORT(400, "当前状态不允许操作"),
    NO_AUTH(401,"登陆未授权"),
    ILLEGAL(411,"请求不合法"),
    UNDEFINED(0xFFFF, "未知异常"),

    // 文件上传
    UPLOAD_IMG_ERROR(0xB001,"图片上传失败"),
    UPLOAD_FILE_ERROR(0xB002,"文件上传失败");

    private Integer code;
    private String msg;

    CodeEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer codeOf(){
        return this.code;
    }

    public String msgOf(){
        return this.msg;
    }

    public static CodeEnum get(int value) {
        for (CodeEnum code : values()) {
            if (code.codeOf() == value)
                return code;
        }
        return null;
    }
}
