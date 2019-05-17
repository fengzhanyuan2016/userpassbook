package com.bmw.passbook.service.impl;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.constant.PassStatus;
import com.bmw.passbook.dto.Pass;
import com.bmw.passbook.dto.PassInfo;
import com.bmw.passbook.dto.PassTemplate;
import com.bmw.passbook.dto.Response;
import com.bmw.passbook.entity.Merchant;
import com.bmw.passbook.mapper.PassRowMapper;
import com.bmw.passbook.mapper.PassTemplateRowMapper;
import com.bmw.passbook.repository.MerchantRepository;
import com.bmw.passbook.service.IUserPassService;
import com.bmw.passbook.utils.HBaseUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserPassService implements IUserPassService {

    private MerchantRepository _merchantRepository;

    public UserPassService(MerchantRepository merchantRepository) {
        _merchantRepository = merchantRepository;
    }

    @Override
    public Response getUserPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId,PassStatus.UNUSED);
    }

    @Override
    public Response getUserUsedPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId,PassStatus.USED);
    }

    @Override
    public Response getUserAllPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId,PassStatus.ALL);
    }

    /**
     *
     * @param pass
     * @return
     */
    @Override
    public Response userUsePass(Pass pass) throws Exception {
        var mapper = new PassRowMapper();
        var rowPrefix = new StringBuilder(String.valueOf(pass.getUserId())).reverse().toString().getBytes();
        Scan scan = new Scan();
        List<Filter> filters = new ArrayList<>();
        filters.add(new PrefixFilter(rowPrefix));
        filters.add(new SingleColumnValueFilter(Constants.PassTable.FAMILY_I.getBytes(),Constants.PassTable.TEMPLATE_ID.getBytes(), CompareFilter.CompareOp.EQUAL,pass.getTemplateId().getBytes()));
        filters.add(new SingleColumnValueFilter(Constants.PassTable.FAMILY_I.getBytes(),Constants.PassTable.CON_DATE.getBytes(), CompareFilter.CompareOp.EQUAL,Bytes.toBytes("-1")));
        scan.setFilter(new FilterList(filters));
        var table = HBaseUtil.getAdmin().getConnection().getTable(TableName.valueOf(Constants.PassTable.TABLE_NAME));
        var results = table.getScanner(scan);

        List<Pass> passes = new ArrayList<>();
        for(var result :results){
            var item = mapper.map(result);
            passes.add(item);
        }
        if(passes.size()!=1){
            return Response.ERROR("UserPassERROR");
        }
        //修改数据库
        Put put = new Put(passes.get(0).getRowKey().getBytes());
        put.addColumn(Constants.PassTable.FAMILY_I.getBytes(),Constants.PassTable.CON_DATE.getBytes(),Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())));
        table.put(put);
        return Response.SUCCESS("success");
    }

    /**
     * 根据优惠卷状态获取优惠卷信息
     **/
    private Response getPassInfoByStatus(Long userId, PassStatus status) throws Exception {
        var mapper = new PassRowMapper();
        var rowPrefix = new StringBuilder(String.valueOf(userId)).reverse().toString();
        byte[] b_rowPrefix = Bytes.toBytes(rowPrefix);
        CompareFilter.CompareOp compareOption = status == PassStatus.UNUSED ? CompareFilter.CompareOp.EQUAL : CompareFilter.CompareOp.NOT_EQUAL;
        Scan scan = new Scan();
        List<Filter> filters = new ArrayList<>();
        //获取特定用户的优惠卷
        filters.add(new PrefixFilter(b_rowPrefix));
        //找到未使用的优惠卷
        if (status != PassStatus.ALL) {
            filters.add(new SingleColumnValueFilter(Constants.PassTable.FAMILY_I.getBytes(), Constants.PassTable.CON_DATE.getBytes(), compareOption, Bytes.toBytes("-1")));
        }
        scan.setFilter(new FilterList(filters));
        var admin = HBaseUtil.getAdmin();
        var results = admin.getConnection().getTable(TableName.valueOf(Constants.PassTable.TABLE_NAME)).getScanner(scan);
        List<Pass> passes = new ArrayList<>();
        for (var result : results) {
            var pass = mapper.map(result);
            passes.add(pass);
        }
        var passTemplateMap = buildPassTemplateMap(passes);
        var merchantMap = buildMerchantMap(new ArrayList<>(passTemplateMap.values()));

        List<PassInfo> data = new ArrayList<>();
        for (Pass pass : passes) {
            PassTemplate passTemplate = passTemplateMap.getOrDefault(pass.getTemplateId(), null);
            if (null == passTemplate) {
                continue;
            }
            Merchant merchant = merchantMap.getOrDefault(passTemplate.getId(), null);
            if (null == merchant) {
                continue;
            }
            data.add(new PassInfo(pass, passTemplate, merchant));
        }
        return Response.SUCCESS(data);
    }


    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passes) throws IOException, ParseException {
        String[] patterns = new String[]{"yyyy-MM-dd"};
        List<String> templateIds = passes.stream().map(
                Pass::getTemplateId
        ).collect(Collectors.toList());

        List<Get> templateGets = new ArrayList<>(templateIds.size());
        templateIds.forEach(t -> templateGets.add(new Get(Bytes.toBytes(t))));

        var admin = HBaseUtil.getAdmin();
        var table = admin.getConnection().getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME));
        Result[] results = table.get(templateGets);

        //構造Template
        Map<String, PassTemplate> templateId2Object = new HashMap<>();
        var mapper = new PassTemplateRowMapper();
        for (Result result : results) {
            var passTemplate = mapper.map(result);
            templateId2Object.put(Bytes.toString(result.getRow()), passTemplate);
        }
        return templateId2Object;
    }

    private Map<Integer, Merchant> buildMerchantMap(List<PassTemplate> passTemplates) {
        Map<Integer, Merchant> map = new HashMap<>();
        var merchantIds = passTemplates.stream().map(PassTemplate::getId).collect(Collectors.toList());
        var merchants = _merchantRepository.findAllById(merchantIds);
        merchants.forEach(m -> map.put(m.getId(), m));
        return map;
    }


}
