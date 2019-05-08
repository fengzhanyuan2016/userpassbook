package com.bmw.passbook.service;

import com.bmw.passbook.dto.PassTemplate;

public interface IHBasePassService {
    boolean dropPassTemplateToHBase(PassTemplate template);
}
