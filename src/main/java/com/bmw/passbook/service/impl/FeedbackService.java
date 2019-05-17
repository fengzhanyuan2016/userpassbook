package com.bmw.passbook.service.impl;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.FeedBack;
import com.bmw.passbook.dto.Response;
import com.bmw.passbook.mapper.FeedbackRowMapper;
import com.bmw.passbook.service.IFeedbackService;
import com.bmw.passbook.utils.HBaseUtil;
import com.bmw.passbook.utils.RowKeyGenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class FeedbackService implements IFeedbackService {

    @Override
    public Response createFeedback(FeedBack feedback) {
        String rowKey = RowKeyGenUtil.genFeedbackRowKey(feedback);
        var i_map = new HashMap<String,String>();
        i_map.put(Constants.FeedbackTable.TEMPLATE_ID,feedback.getTemplateId());
        i_map.put(Constants.FeedbackTable.COMMENT,feedback.getComment());
        i_map.put(Constants.FeedbackTable.TYPE,feedback.getType());
        i_map.put(Constants.FeedbackTable.USER_ID,feedback.getUserId().toString());
        HBaseUtil.add(Constants.FeedbackTable.TABLE_NAME,rowKey,Constants.FeedbackTable.FAMILY_I,i_map);
        return  Response.SUCCESS(feedback);
    }

    @Override
    public Response getFeedback(Long userId) {
        FeedbackRowMapper mapper = new FeedbackRowMapper();
        var reverseId = new StringBuilder(String.valueOf(userId)).reverse().toString();
        var results = HBaseUtil.scan(Constants.FeedbackTable.TABLE_NAME,reverseId);
        List<FeedBack> feedBacks = new ArrayList<>();
        for(Result result :results){
            var feedback = mapper.map(result);
            feedBacks.add(feedback);
        }
        return Response.SUCCESS(feedBacks);
    }


}
