package com.bmw.passbook.mapper;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.PassTemplate;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.ParseException;

public class PassTemplateRowMapper {
    private static byte[] FAMILY_B = Constants.PassTemplateTable.FAMILY_B.getBytes();
    private static byte[] ID = Constants.PassTemplateTable.ID.getBytes();
    private static byte[] TITLE = Constants.PassTemplateTable.TITLE.getBytes();
    private static byte[] SUMMARY = Constants.PassTemplateTable.SUMMARY.getBytes();
    private static byte[] DESC = Constants.PassTemplateTable.DESC.getBytes();
    private static byte[] HAS_TOKEN = Constants.PassTemplateTable.HAS_TOKEN.getBytes();
    private static byte[] BACKGROUND = Constants.PassTemplateTable.BACKGROUND.getBytes();
    private static byte[] FAMILY_C = Constants.PassTemplateTable.FAMILY_C.getBytes();
    private static byte[] LIMIT = Constants.PassTemplateTable.LIMIT.getBytes();
    private static byte[] START = Constants.PassTemplateTable.START.getBytes();
    private static byte[] END = Constants.PassTemplateTable.END.getBytes();

    public PassTemplate map(Result result) throws ParseException {
        if(result.isEmpty()){
            return null;
        }
        PassTemplate template = new PassTemplate();
        template.setId(Bytes.toInt(result.getValue(FAMILY_B,ID)));
        template.setTitle(Bytes.toString(result.getValue(FAMILY_B,TITLE)));
        template.setSummary(Bytes.toString(result.getValue(FAMILY_B,SUMMARY)));
        template.setDesc(Bytes.toString(result.getValue(FAMILY_B,DESC)));
        template.setHasToken(Bytes.toBoolean(result.getValue(FAMILY_B,HAS_TOKEN)));
        template.setBackground(Bytes.toInt(result.getValue(FAMILY_B,BACKGROUND)));
        template.setLimit(Bytes.toLong(result.getValue(FAMILY_C,LIMIT)));

        String[] patterns = new String[]{"yyyy-MM-dd"};
        template.setStart(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C,START)),patterns));
        template.setEnd(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C,END)),patterns));
        return template;


    }


}
