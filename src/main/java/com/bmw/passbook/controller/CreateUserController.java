package com.bmw.passbook.controller;

import com.bmw.passbook.dto.Response;
import com.bmw.passbook.dto.User;
import com.bmw.passbook.log.LogConstants;
import com.bmw.passbook.log.LogGenerator;
import com.bmw.passbook.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class CreateUserController {
    private IUserService _userService;
    private final HttpServletRequest httpServletRequest;
    public CreateUserController(IUserService userService,HttpServletRequest request){
        this._userService = userService;
        this.httpServletRequest = request;
    }

    @PostMapping("/passbook")
    Response createUser(@RequestBody User user) throws Exception{
        LogGenerator.genLog(httpServletRequest,-1L, LogConstants.ActionName.CREATE_USER,user);
        return _userService.createUser(user);
    }


}
