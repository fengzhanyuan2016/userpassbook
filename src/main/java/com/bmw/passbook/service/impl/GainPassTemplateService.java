package com.bmw.passbook.service.impl;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.GainPassTemplateRequest;
import com.bmw.passbook.dto.Response;
import com.bmw.passbook.service.IGainPassTemplateService;
import com.bmw.passbook.utils.HBaseUtil;
import com.bmw.passbook.utils.RowKeyGenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

@Slf4j
public class GainPassTemplateService implements IGainPassTemplateService {
    private StringRedisTemplate _redisTemplate;
    public GainPassTemplateService(StringRedisTemplate redisTemplate){
        _redisTemplate = redisTemplate;
    }
    @Override
    public Response gainPassTemplate(GainPassTemplateRequest request) throws Exception {
        return null;
    }

    /**
     * 给用户添加优惠卷
     * @param request
     * @param merchantId 商户Id
     * @param passTemplateId 优惠卷模板Id
     * @return
     */
    private boolean addPassForUser(GainPassTemplateRequest request,Integer merchantId,String passTemplateId) throws IOException {
        Put put = new Put(Bytes.toBytes(RowKeyGenUtil.genPassRowKey(request)));
        put.addColumn(Bytes.toBytes(Constants.PassTable.FAMILY_I),Bytes.toBytes(Constants.PassTable.USER_ID),Bytes.toBytes(request.getUserId()));
        put.addColumn(Bytes.toBytes(Constants.PassTable.FAMILY_I),Bytes.toBytes(Constants.PassTable.TEMPLATE_ID),Bytes.toBytes(passTemplateId));
        var admin= HBaseUtil.getAdmin();
        var table= admin.getConnection().getTable(TableName.valueOf(Constants.PassTable.TABLE_NAME));
        if(request.getPassTemplate().getHasToken()){
            String token = _redisTemplate.opsForSet().pop(passTemplateId);
            if(null==token){
                log.error("Token not exist {}",passTemplateId);
                return false;
            }
            recordTokenToFile(merchantId,passTemplateId,token);
            put.addColumn(Bytes.toBytes(Constants.PassTable.FAMILY_I),Bytes.toBytes(Constants.PassTable.TOKEN),Bytes.toBytes(token));
        }else{
            put.addColumn(Bytes.toBytes(Constants.PassTable.FAMILY_I),Bytes.toBytes(Constants.PassTable.TOKEN),Bytes.toBytes("-1"));
        }
        put.addColumn(Bytes.toBytes(Constants.PassTable.FAMILY_I),Bytes.toBytes(Constants.PassTable.ASSIGNED_DATE),Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())));
        put.addColumn(Bytes.toBytes(Constants.PassTable.FAMILY_I),Bytes.toBytes(Constants.PassTable.CON_DATE),Bytes.toBytes("-1"));
        table.put(put);
        return true;
    }

    private void recordTokenToFile(Integer merchantsId,String passTemplatedId,String token) throws IOException {
        Files.write(Paths.get(Constants.TOKEN_DIR,String.valueOf(merchantsId)+passTemplatedId+Constants.USED_TOKEN_SUHHIX),(token+"\n").getBytes(), StandardOpenOption.APPEND);
    }



}
