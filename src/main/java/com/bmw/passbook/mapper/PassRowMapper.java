package com.bmw.passbook.mapper;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.Pass;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.ParseException;

public class PassRowMapper {
    private static byte[] FAMILY_I = Constants.PassTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = Constants.PassTable.USER_ID.getBytes();
    private static byte[] TEMPLATE_ID = Constants.PassTable.TEMPLATE_ID.getBytes();
    private static byte[] TOKEN = Constants.PassTable.TOKEN.getBytes();
    private static byte[] ASSIGNED_DATE = Constants.PassTable.ASSIGNED_DATE.getBytes();
    private static byte[] CON_DATE = Constants.PassTable.CON_DATE.getBytes();

    public Pass map(Result result) throws ParseException {
        if(result.isEmpty()){
            return  null;
        }
        Pass pass = new Pass();
        pass.setRowKey(Bytes.toString(result.getRow()));
        pass.setUserId(Bytes.toLong(result.getValue(FAMILY_I,USER_ID)));
        pass.setTemplateId(Bytes.toString(result.getValue(FAMILY_I,TEMPLATE_ID)));
        pass.setToken(Bytes.toString(result.getValue(FAMILY_I,TOKEN)));
        String[] patterns = new String[]{"yyyy-MM-dd"};
        pass.setAssignedDate(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_I,ASSIGNED_DATE)),patterns));
        pass.setConDate(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_I,CON_DATE)),patterns));
        return pass;
    }


}
