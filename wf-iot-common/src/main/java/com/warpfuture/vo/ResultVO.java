package com.warpfuture.vo;

import com.warpfuture.constant.ResponseCode;
import com.warpfuture.constant.ResponseMsg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Created by fido on 2018/4/13. 统一的返回结果
 */
@Data
@ToString
public class ResultVO<T> {
    private Integer code; // 状态码
    private String message; // 描述信息
    private T data; // 数据

    public ResultVO<T> fail(String errMsg){
        this.code = ResponseCode.FAIL;
        this.message = errMsg;
        return this;
    }

    public ResultVO<T> success(){
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMsg.SUCCESS;
        return this;
    }

    public ResultVO<T> success(T data){
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMsg.SUCCESS;
        this.data = data;
        return this;
    }

    public boolean isSuccess(){
        return this.code.equals(ResponseCode.SUCCESS);
    }

    public ResultVO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultVO() {
    }
}
