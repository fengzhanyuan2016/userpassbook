package com.bmw.passbook.controller;

import com.bmw.passbook.dto.*;
import com.bmw.passbook.log.LogConstants;
import com.bmw.passbook.log.LogGenerator;
import com.bmw.passbook.service.*;
import com.bmw.passbook.service.impl.HBasePassService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/passbook")
public class PassbookController {
    private IUserPassService _userPassService;
    private IInventoryService _iInventoryService;
    private IGainPassTemplateService _gainPassTemplateService;
    private IFeedbackService _feedbackService;
    private HttpServletRequest _request;
    private IHBasePassService _hbasePassService;
    public PassbookController(IUserPassService userPassService,IHBasePassService hBasePassService, IInventoryService iInventoryService, IGainPassTemplateService gainPassTemplateService, IFeedbackService feedbackService, HttpServletRequest request){
        _userPassService = userPassService;
        _iInventoryService = iInventoryService;
        _feedbackService = feedbackService;
        _gainPassTemplateService = gainPassTemplateService;
        _hbasePassService = hBasePassService;
        _request = request;
    }

    /**
     * 获取用户的优惠券信息
     * @param userId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("userpassinfo")
    Response userPassInfo(Long userId) throws Exception{
        LogGenerator.genLog(_request,userId, LogConstants.ActionName.USER_PASS_INFO,null);
        return _userPassService.getUserPassInfo(userId);
    }

    /**
     * 获取用户已经使用的优惠券
     * @param userId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("userusedpassinfo")
    Response userUsedPassInfo(Long userId) throws  Exception{
        LogGenerator.genLog(_request,userId, LogConstants.ActionName.USER_USED_PASS_INFO,null);
        return _userPassService.getUserUsedPassInfo(userId);
    }

    /***
     * 用户使用优惠券
     * @param pass
     * @return
     */
    @ResponseBody
    @PostMapping("userusepass")
    Response userUsePass(Pass pass) throws Exception{
        LogGenerator.genLog(_request,pass.getUserId(), LogConstants.ActionName.USER_USE_PASS,null);
        return _userPassService.userUsePass(pass);
    }

    /**
     * 获取库存信息
     * @param userId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/inventoryinfo")
    Response inventoryInfo(Long userId) throws Exception{
        LogGenerator.genLog(_request,userId, LogConstants.ActionName.INVENTORY_INFO,null);
        return  _iInventoryService.getInventoryInfo(userId);
    }

    /**
     * 用户领取优惠券
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/gainpasstemplate")
    Response gainPassTemplate(@RequestBody GainPassTemplateRequest request) throws Exception{
        LogGenerator.genLog(_request,request.getUserId(), LogConstants.ActionName.GAIN_PASS_TEMPLATE,request);
        return _gainPassTemplateService.gainPassTemplate(request);
    }

    /**
     * 用户创建评论
     * @param feedBack
     * @return
     */
    @ResponseBody
    @PostMapping("/createfeedback")
    Response createFeedback(FeedBack feedBack){
        LogGenerator.genLog(_request,feedBack.getUserId(), LogConstants.ActionName.CREATE_FEEDBACK,feedBack);
        return _feedbackService.createFeedback(feedBack);
    }

    /**
     * 用户获取评论信息
     * @param userId
     * @return
     */
    @ResponseBody
    @PostMapping("/getfeedback")
    Response getFeedback(Long userId){
        LogGenerator.genLog(_request,userId,LogConstants.ActionName.GET_FEEDBACK,null);
        return _feedbackService.getFeedback(userId);
    }


    /**
     * 模拟商户分发优惠券
     * @return
     */
    @ResponseBody
    @PostMapping("/dropTemplate")
    Response dropTemplate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        PassTemplate passTemplate = new PassTemplate();
        passTemplate.setBackground(2);
        passTemplate.setDesc("描述");
        passTemplate.setStart(new Date());
        passTemplate.setEnd(calendar.getTime());
        passTemplate.setHasToken(false);
        passTemplate.setLimit(100L);
        passTemplate.setId(1);
        passTemplate.setSummary("简介");
        passTemplate.setTitle("标题");
        boolean result = _hbasePassService.dropPassTemplateToHBase(passTemplate);
        return new Response().SUCCESS(result);


    }




}
