package cn.magic.backgroundmanagement.utils;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通用返回结果封装类
 */
@Data
@Accessors(chain = true) // 支持链式调用
public class R {

    private Integer code;    // 状态码，例如：200成功，500失败
    private String message;  // 提示信息
    private Boolean success; // 操作是否成功
    private Object data;     // 返回数据

    // 私有构造器，统一使用静态方法创建
    private R() {}

    // ----------- 静态工厂方法 -----------
    public static R ok() {
        return new R()
                .setCode(200)
                .setSuccess(true)
                .setMessage("操作成功");
    }

    public static R ok(Object data) {
        return new R()
                .setCode(200)
                .setSuccess(true)
                .setMessage("操作成功")
                .setData(data);
    }

    public static R ok(String message, Object data) {
        return new R()
                .setCode(200)
                .setSuccess(true)
                .setMessage(message)
                .setData(data);
    }

    public static R error() {
        return new R()
                .setCode(500)
                .setSuccess(false)
                .setMessage("操作失败");
    }

    public static R error(String message) {
        return new R()
                .setCode(500)
                .setSuccess(false)
                .setMessage(message);
    }

    // ----------- 链式调用支持 -----------
    public R code(Integer code) {
        this.code = code;
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public R data(Object data) {
        this.data = data;
        return this;
    }
}
