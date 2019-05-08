package com.bmw.passbook.constant;

public enum PassStatus {
    UNUSED(1,""),USED(2,""),ALL(3,"");
    private Integer code;
    private String desc;
    PassStatus(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode(){
        return  this.code;
    }
    public String getDesc(){
        return  this.desc;
    }

}
