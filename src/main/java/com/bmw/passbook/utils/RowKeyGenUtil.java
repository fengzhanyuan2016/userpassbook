package com.bmw.passbook.utils;

import com.bmw.passbook.dto.FeedBack;
import com.bmw.passbook.dto.GainPassTemplateRequest;
import com.bmw.passbook.dto.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;


@Slf4j
public class RowKeyGenUtil {
    /**
     * 生成优惠券模板的主键
     * @param passTemplate
     * @return
     */
    public static String genPassTemplateRowKey(PassTemplate passTemplate){
        String passinfo = String.valueOf(passTemplate.getId())+"_"+passTemplate.getTitle();
        String rowkey = DigestUtils.md5DigestAsHex(passinfo.getBytes());
        log.info("GenPassTemplateRowKey:{},{}",passinfo,rowkey);
        return rowkey;
    }

    /**
     * 生成用户优惠卷的主键
     * @param request
     * @return
     */
    public static String genPassRowKey(GainPassTemplateRequest request){
        return new StringBuilder(String.valueOf(request.getUserId())).reverse().toString()+
                (Long.MAX_VALUE-System.currentTimeMillis())+genPassTemplateRowKey(request.getPassTemplate());
    }

    /**
     * 生成用户评论的主键
     * @param feedBack
     * @return
     */
    public static String genFeedbackRowKey(FeedBack feedBack){
        return new StringBuilder(String.valueOf(feedBack.getUserId())).reverse().toString()+(Long.MAX_VALUE-System.currentTimeMillis());
    }





}
