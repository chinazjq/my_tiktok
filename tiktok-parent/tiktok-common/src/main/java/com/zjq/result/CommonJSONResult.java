package com.zjq.result;

/**
 * 自定义数据返回统一封装实体
 */
public class CommonJSONResult {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    private String ok;    // 不使用

    public static CommonJSONResult build(Integer status, String msg, Object data) {
        return new CommonJSONResult(status, msg, data);
    }

    public static CommonJSONResult build(Integer status, String msg, Object data, String ok) {
        return new CommonJSONResult(status, msg, data, ok);
    }

    public static CommonJSONResult ok(Object data) {
        return new CommonJSONResult(data);
    }

    public static CommonJSONResult ok() {
        return new CommonJSONResult(null);
    }

    public static CommonJSONResult errorMsg(String msg) {
        return new CommonJSONResult(500, msg, null);
    }

    public static CommonJSONResult errorUserTicket(String msg) {
        return new CommonJSONResult(557, msg, null);
    }

    public static CommonJSONResult errorMap(Object data) {
        return new CommonJSONResult(501, "error", data);
    }

    public static CommonJSONResult errorTokenMsg(String msg) {
        return new CommonJSONResult(502, msg, null);
    }

    public static CommonJSONResult errorException(String msg) {
        return new CommonJSONResult(555, msg, null);
    }

    public static CommonJSONResult errorUserQQ(String msg) {
        return new CommonJSONResult(556, msg, null);
    }

    public CommonJSONResult() {

    }

    public CommonJSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public CommonJSONResult(Integer status, String msg, Object data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public CommonJSONResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

}
