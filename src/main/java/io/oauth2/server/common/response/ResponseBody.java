package io.oauth2.server.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseBody {

    private int state;
    private String massage;
    private Object data;
    private Object code;

    @Builder
    public ResponseBody(ErrorCode errorCode, Object data){
        this.state = errorCode.getState();
        this.massage = errorCode.getMessage();
        this.code = errorCode.getCode();
        this.data = data;
    }
}
