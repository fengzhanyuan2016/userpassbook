package com.bmw.passbook.mapper;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.FeedBack;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class FeedbackRowMapper {
    private static byte[] FAMILY_I = Constants.FeedbackTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = Constants.FeedbackTable.USER_ID.getBytes();
    private static byte[] TYPE = Constants.FeedbackTable.TYPE.getBytes();
    private static byte[] TEMPLATE_ID = Constants.FeedbackTable.TEMPLATE_ID.getBytes();
    private static byte[] COMMENT = Constants.FeedbackTable.COMMENT.getBytes();
    public FeedBack map(Result result){
        if(result.isEmpty()){
            return null;
        }
        FeedBack feedBack = new FeedBack();
        feedBack.setUserId(Bytes.toLong(result.getValue(FAMILY_I,USER_ID)));
        feedBack.setTemplateId(Bytes.toString(result.getValue(FAMILY_I,TEMPLATE_ID)));
        feedBack.setType(Bytes.toString(result.getValue(FAMILY_I,TYPE)));
        feedBack.setComment(Bytes.toString(result.getValue(FAMILY_I,COMMENT)));
        return  feedBack;
    }
}
