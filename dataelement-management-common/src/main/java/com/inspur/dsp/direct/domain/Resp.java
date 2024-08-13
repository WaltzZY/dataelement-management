package com.inspur.dsp.direct.domain;

import com.inspur.dsp.direct.enums.RespCode;
import lombok.Data;

/**
 * 统一返回类
 * @param <T>
 */
@Data
public class Resp<T> {

    /**
     * 返回消息
     */
    private String msg;
    /**
     * 返回代码
     */
    private String code;
    /**
     * 返回数据
     */
    private T data;

    private Resp() {}

    private Resp(String msg, RespCode respCode, T data) {
        this.msg = msg;
        this.code = respCode.getState();
        this.data = data;
    }

    private Resp(String msg, String code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public static <T> Resp<T> success(T data) {
        return new Resp<>(RespCode.OK.getStateInfo(), RespCode.OK, data);
    }

    public static <T> Resp<T> success(String message, T data) {
        return new Resp<>(message, RespCode.OK, data);
    }

    public static <T> Resp<T> success() {
        return new Resp<>(RespCode.OK.getStateInfo(), RespCode.OK, null);
    }

    public static <T> Resp<T> fail() {
        return new Resp<>(RespCode.BAD_REQUEST.getStateInfo(), RespCode.BAD_REQUEST, null);
    }

    public static <T> Resp<T> fail(String message) {
        return new Resp<>(message, RespCode.BAD_REQUEST, null);
    }

    public static <T> Resp<T> fail(String message, RespCode respCode) {
        return new Resp<>(message, respCode, null);
    }

    public static <T> Resp<T> fail(RespCode respCode) {
        return new Resp<>(respCode.getStateInfo(), respCode, null);
    }

    /**
     * 功能描述,自定义异常返回使用,传入异常信息枚举回显信息,异常信息枚举代码
     * @param message 异常信息枚举回显信息
     * @param code    异常信息枚举代码
     * @param <T>
     * @return
     */
    public static <T> Resp<T> fail(String message, String code) {
        return new Resp<>(message, code, null);
    }
}
