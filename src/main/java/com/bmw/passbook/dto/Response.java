package com.bmw.passbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Integer errorCode;
    private String errorMsg;
    private Object data;

    public Response(Object data) {
        this.errorCode = 0;
        this.data = data;
    }

    public static Response SUCCESS(Object data){
        return new Response(data);
    }

    public static Response ERROR(String msg){
        var response = new Response();
        response.setErrorCode(-1);
        response.setErrorMsg(msg);
        return response;
    }
}
