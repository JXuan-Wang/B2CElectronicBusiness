package org.example.system.common.exception;


import lombok.Data;
import org.example.system.model.vo.common.ResultCodeEnum;

@Data
public class MyException extends RuntimeException{
    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public MyException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
        this.code=resultCodeEnum.getCode();
        this.message=resultCodeEnum.getMessage();
    }
}
