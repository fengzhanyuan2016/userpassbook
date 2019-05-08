package com.bmw.passbook.constant;

public enum FeedBackType {
    PASS(1,"针对优惠卷的评论"),
    APP(2,"针对卡包的评论");

    private Integer code;
    private String desc;
    FeedBackType(Integer code,String desc){
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
