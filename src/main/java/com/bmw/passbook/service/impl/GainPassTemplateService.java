package com.bmw.passbook.service.impl;

import com.bmw.passbook.dto.GainPassTemplateRequest;
import com.bmw.passbook.dto.Response;
import com.bmw.passbook.service.IGainPassTemplateService;

public class GainPassTemplateService implements IGainPassTemplateService {
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
    private boolean addPassForUser(GainPassTemplateRequest request,Integer merchantId,String passTemplateId){

        return true;
    }


}
