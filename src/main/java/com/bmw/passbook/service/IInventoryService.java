package com.bmw.passbook.service;

import com.bmw.passbook.dto.Response;

public interface IInventoryService {
    Response getInventoryInfo(Long userId) throws Exception;



}
