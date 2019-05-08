package com.bmw.passbook.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.servlet.http.HttpServletRequest;
@Slf4j
public class LogGenerator {
    public static void genLog(HttpServletRequest request,Long userId,String action,Object info){
        var loginfo = JSON.toJSONString(new LogObject(action,userId,System.currentTimeMillis(),request.getRemoteAddr(),info));
        log.info(loginfo);
    }
}
