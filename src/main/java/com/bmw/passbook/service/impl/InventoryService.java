package com.bmw.passbook.service.impl;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.*;
import com.bmw.passbook.entity.Merchant;
import com.bmw.passbook.mapper.PassTemplateRowMapper;
import com.bmw.passbook.repository.MerchantRepository;
import com.bmw.passbook.service.IInventoryService;
import com.bmw.passbook.service.IUserPassService;
import com.bmw.passbook.utils.HBaseUtil;
import com.bmw.passbook.utils.RowKeyGenUtil;
import lombok.var;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.*;
import java.util.stream.Collectors;

public class InventoryService implements IInventoryService {

    private final MerchantRepository _merchantRepository;
    private final IUserPassService _userPassService;
    public InventoryService(MerchantRepository merchantRepository,IUserPassService userPassService){
        _merchantRepository = merchantRepository;
        _userPassService = userPassService;
    }

    /**
     * 获取库存信息
     * @param userId 用户Id
     * @return
     * @throws Exception
     */
    @Override
    public Response getInventoryInfo(Long userId) throws Exception {
        //获取用户所有的优惠券信息
        Response allUserPass = _userPassService.getUserAllPassInfo(userId);
        List<PassInfo> passInfos = (List<PassInfo>) allUserPass.getData();
        List<PassTemplate> excludeObjects = passInfos.stream().map(PassInfo::getPassTemplate).collect(Collectors.toList());
        List<String> excludeIds = new ArrayList<>();
        excludeObjects.forEach(e->excludeIds.add(RowKeyGenUtil.genPassTemplateRowKey(e)));
        var response = new InventoryResponse(userId,buildPassTemplateInfo(getAvailablePassTemplate(excludeIds)));
        return Response.SUCCESS(response);
    }

    /**
     * 获取系统中可用的优惠券
     * @param excludeIds 需要排除的优惠券Ids
     * @return  {@link PassTemplate}
     */
    private List<PassTemplate> getAvailablePassTemplate(List<String> excludeIds) throws Exception {
        PassTemplateRowMapper mapper = new PassTemplateRowMapper();
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        //limit>0
        filterList.addFilter(new SingleColumnValueFilter(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                CompareFilter.CompareOp.GREATER,
                new LongComparator(0L)
        ));
        //limit=-1
        filterList.addFilter(new SingleColumnValueFilter(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("-1")
        ));
        Scan scan = new Scan();
        scan.setFilter(filterList);
        var table = HBaseUtil.getAdmin().getConnection().getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME));
        var results = table.getScanner(scan);
        List<PassTemplate> passTemplates = new ArrayList<>();
        Date current = new Date();
        for(var result:results){
            var passTemplate = mapper.map(result);
            if(excludeIds.contains(RowKeyGenUtil.genPassTemplateRowKey(passTemplate))){
                continue;
            }
            if(current.getTime()>=passTemplate.getStart().getTime()&&current.getTime()<=passTemplate.getEnd().getTime()){
                passTemplates.add(passTemplate);
            }
        }
        return passTemplates;
    }

    /**
     * 构造优惠券信息
     * @param passTemplates {@link PassTemplate}
     * @return {@link PassTemplateInfo}
     */
    private List<PassTemplateInfo> buildPassTemplateInfo(List<PassTemplate> passTemplates){
        Map<Integer, Merchant> merchantMap = new HashMap<>();
        List<Integer> merchantIds = passTemplates.stream().map(PassTemplate::getId).collect(Collectors.toList());
        List<Merchant> merchants =_merchantRepository.findAllById(merchantIds);
        merchants.forEach(m->merchantMap.put(m.getId(),m));
        List<PassTemplateInfo> data = new ArrayList<>();
        for(var passTemplate : passTemplates){
            Merchant merchant= merchantMap.getOrDefault(passTemplate.getId(),null);
            if(null==merchant){
                continue;
            }
            data.add(new PassTemplateInfo(passTemplate,merchant));
        }
        return data;
    }


}
