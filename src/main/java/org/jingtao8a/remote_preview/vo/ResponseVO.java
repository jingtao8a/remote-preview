package org.jingtao8a.remote_preview.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseVO<T> {
    private String status;
    private Integer code;
    private String info;
    private T data;
}
