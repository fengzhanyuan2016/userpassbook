package com.bmw.passbook.service.impl;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.PassTemplate;
import com.bmw.passbook.service.IHBasePassService;
import com.bmw.passbook.utils.HBaseUtil;
import com.bmw.passbook.utils.RowKeyGenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**
 * 从kafka接收优惠券模板并存储的Hbase中
 */
@Slf4j
@Service
public class HBasePassService implements IHBasePassService {

    @Override
    public boolean dropPassTemplateToHBase(PassTemplate template) {
        if(null==template){
            return false;
        }
        String rowKey = RowKeyGenUtil.genPassTemplateRowKey(template);
        //添加数据
        var b_map = new HashMap<String,String>();
        b_map.put(Constants.PassTemplateTable.ID,template.getId().toString());
        b_map.put(Constants.PassTemplateTable.TITLE,template.getTitle());
        b_map.put(Constants.PassTemplateTable.SUMMARY,template.getSummary());
        b_map.put(Constants.PassTemplateTable.DESC,template.getDesc());
        b_map.put(Constants.PassTemplateTable.HAS_TOKEN,template.getHasToken().toString());
        b_map.put(Constants.PassTemplateTable.BACKGROUND,template.getBackground().toString());
        HBaseUtil.add(Constants.PassTemplateTable.TABLE_NAME,rowKey,Constants.PassTemplateTable.FAMILY_B,b_map);
        var c_map = new HashMap<String,String>();
        b_map.put(Constants.PassTemplateTable.LIMIT,template.getLimit().toString());
        b_map.put(Constants.PassTemplateTable.START,template.getStart().toString());
        b_map.put(Constants.PassTemplateTable.END,template.getEnd().toString());
        HBaseUtil.add(Constants.PassTemplateTable.TABLE_NAME,rowKey,Constants.PassTemplateTable.FAMILY_C,c_map);
        return true;
    }
}
