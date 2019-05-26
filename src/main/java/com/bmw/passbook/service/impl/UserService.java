package com.bmw.passbook.service.impl;

import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.Response;
import com.bmw.passbook.dto.User;
import com.bmw.passbook.service.IUserService;
import com.bmw.passbook.utils.HBaseUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class UserService implements IUserService {
    private StringRedisTemplate _redisTemplate;
    public UserService(StringRedisTemplate redisTemplate) {
        this._redisTemplate = redisTemplate;
    }

    @Override
    public Response createUser(User user) {

        Long curCount = _redisTemplate.opsForValue().increment(Constants.USE_COUNT_REDIS_KEY);
        Long userId = genUserId(curCount);

        var b_map = new HashMap<String,String>();
        b_map.put(Constants.UserTable.NAME,user.getBaseInfo().getName());
        b_map.put(Constants.UserTable.AGE,user.getBaseInfo().getAge().toString());
        b_map.put(Constants.UserTable.SEX,user.getBaseInfo().getSex());
        HBaseUtil.add(Constants.UserTable.TABLE_NAME, userId.toString(),Constants.UserTable.FAMILY_B,b_map);

        var o_map = new HashMap<String,String>();
        o_map.put(Constants.UserTable.PHONE,user.getOtherInfo().getPhone());
        o_map.put(Constants.UserTable.ADDRESS,user.getOtherInfo().getAddress());
        HBaseUtil.add(Constants.UserTable.TABLE_NAME, userId.toString(),Constants.UserTable.FAMILY_O,o_map);
        return new Response(user);
    }

    private Long genUserId(Long frefix){
        String suffix = RandomStringUtils.randomNumeric(5);
        return Long.valueOf(frefix+suffix);
    }

}
