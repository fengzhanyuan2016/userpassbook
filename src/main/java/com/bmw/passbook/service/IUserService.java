package com.bmw.passbook.service;

import com.bmw.passbook.dto.Response;
import com.bmw.passbook.dto.User;

public interface IUserService {
    Response createUser(User user);
}
