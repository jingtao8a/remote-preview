package org.jingtao8a.remote_preview.exception;
import org.jingtao8a.remote_preview.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BusinessException extends Exception{
    private ResponseCodeEnum codeEnum;
    private Integer code;
    private String message;
    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }
    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(Throwable e) { super(e); }

    public BusinessException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ResponseCodeEnum getCodeEnum() { return codeEnum; }

    @Override
    public String getMessage() { return message; }

    @Override
    public Throwable fillInStackTrace() { return this; }
}
