package com.bmw.passbook.service;

import com.bmw.passbook.dto.Pass;
import com.bmw.passbook.dto.Response;

public interface IUserPassService {
    //获取用户个人可以优惠卷信息
    Response getUserPassInfo(Long userId) throws Exception;
    Response getUserUsedPassInfo(Long userId) throws Exception;
    Response getUserAllPassInfo(Long userId) throws Exception;
    Response userUsePass(Pass pass);

}
