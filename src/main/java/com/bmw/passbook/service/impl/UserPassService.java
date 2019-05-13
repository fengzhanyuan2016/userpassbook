package com.bmw.passbook.service.impl;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.constant.PassStatus;
import com.bmw.passbook.dto.Pass;
import com.bmw.passbook.dto.PassTemplate;
import com.bmw.passbook.dto.Response;
import com.bmw.passbook.entity.Merchant;
import com.bmw.passbook.mapper.PassTemplateRowMapper;
import com.bmw.passbook.repository.MerchantRepository;
import com.bmw.passbook.service.IUserPassService;
import com.bmw.passbook.utils.HBaseUtil;
import lombok.var;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserPassService implements IUserPassService {

    private MerchantRepository _merchantRepository;
    public UserPassService(MerchantRepository merchantRepository){
        _merchantRepository = merchantRepository;
    }

    @Override
    public Response getUserPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public Response getUserUsedPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public Response getUserAllPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public Response userUsePass(Pass pass) {
        return null;
    }

    /**
    * 根据优惠卷状态获取优惠卷信息
    * */
    private Response getPassInfoByStatus(Long userId, PassStatus status){
        var rowPrefix = new StringBuilder(String.valueOf(userId)).reverse().toString();
        byte[] b_rowPrefix = Bytes.toBytes(rowPrefix);
        CompareFilter.CompareOp compareOption = status == PassStatus.UNUSED?CompareFilter.CompareOp.EQUAL:CompareFilter.CompareOp.NOT_EQUAL;
        Scan scan = new Scan();
        //获取特定用户的优惠卷
        scan.setFilter(new PrefixFilter(b_rowPrefix));

        //找到未使用的优惠卷
        if(status != PassStatus.ALL){
            scan.setFilter(new SingleColumnValueFilter(Constants.PassTable.FAMILY_I.getBytes(),Constants.PassTable.CON_DATE.getBytes(),compareOption,Bytes.toBytes("-1")));
        }

        return null;
    }


    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passes) throws IOException, ParseException {
        String[] patterns = new String[]{"yyyy-MM-dd"};
        List<String> templateIds = passes.stream().map(
                Pass::getTemplateId
        ).collect(Collectors.toList());

        List<Get> templateGets = new ArrayList<>(templateIds.size());
        templateIds.forEach(t->templateGets.add(new Get(Bytes.toBytes(t))));

        var admin = HBaseUtil.getAdmin();
        var table =  admin.getConnection().getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME));
        Result[] results= table.get(templateGets);

        //構造Template
        Map<String,PassTemplate> templateId2Object = new HashMap<>();
        var mapper = new PassTemplateRowMapper();
        for(Result result : results){
            var passTemplate = mapper.map(result);
            templateId2Object.put(Bytes.toString(result.getRow()),passTemplate);
        }
        return templateId2Object;
    }

    private Map<Integer,Merchant> buildMerchantMap(List<PassTemplate> passTemplates){
        Map<Integer,Merchant> map = new HashMap<>();
        var merchantIds = passTemplates.stream().map(PassTemplate::getId).collect(Collectors.toList());
        var merchants = _merchantRepository.findAllById(merchantIds);
        merchants.forEach(m->map.put(m.getId(),m));
        return map;
    }





}
