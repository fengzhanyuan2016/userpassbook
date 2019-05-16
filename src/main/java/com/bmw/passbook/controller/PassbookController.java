package com.bmw.passbook.controller;

import com.bmw.passbook.dto.FeedBack;
import com.bmw.passbook.dto.GainPassTemplateRequest;
import com.bmw.passbook.dto.Pass;
import com.bmw.passbook.dto.Response;
import com.bmw.passbook.log.LogConstants;
import com.bmw.passbook.log.LogGenerator;
import com.bmw.passbook.service.IFeedbackService;
import com.bmw.passbook.service.IGainPassTemplateService;
import com.bmw.passbook.service.IInventoryService;
import com.bmw.passbook.service.IUserPassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/passbook")
public class PassbookController {
    private IUserPassService _userPassService;
    private IInventoryService _iInventoryService;
    private IGainPassTemplateService _gainPassTemplateService;
    private IFeedbackService _feedbackService;
    private HttpServletRequest _request;
    public PassbookController(IUserPassService userPassService, IInventoryService iInventoryService, IGainPassTemplateService gainPassTemplateService, IFeedbackService feedbackService, HttpServletRequest request){
        _userPassService = userPassService;
        _iInventoryService = iInventoryService;
        _feedbackService = feedbackService;
        _gainPassTemplateService = gainPassTemplateService;
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
        LogGenerator.genLog(_request,pass.getUserId(), LogConstants.ActionName.USER_USED_PASS_INFO,null);
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
    @GetMapping("/gainpasstemplate")
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
        LogGenerator.genLog(_request,feedBack.getUserId(), LogConstants.ActionName.GAIN_PASS_TEMPLATE,feedBack);
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


}
