package com.bmw.passbook.service;

import com.bmw.passbook.dto.GainPassTemplateRequest;
import com.bmw.passbook.dto.Response;

/*
*
* */
public interface IGainPassTemplateService {
    Response gainPassTemplate(GainPassTemplateRequest request) throws Exception;
}
