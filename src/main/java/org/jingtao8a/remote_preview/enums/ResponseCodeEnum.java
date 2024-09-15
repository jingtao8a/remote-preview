package org.jingtao8a.remote_preview.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCodeEnum {
    CODE_200(200, "请求成功"),
    CODE_404(404, "请求地址不存在"),
    CODE_500(500, "服务器返回错误，请联系管理员"),
    CODE_600(600, "请求参数错误"),
    CODE_601(601, "信息已经存在"),
    CODE_700(700, "请求文件不存在"),
    CODE_701(701, "请求文件转码失败"),
    CODE_702(702, "请求文件转码中");

    private Integer code;
    private String msg;
}
