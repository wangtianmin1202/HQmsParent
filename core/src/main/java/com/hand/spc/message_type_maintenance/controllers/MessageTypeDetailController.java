package com.hand.spc.message_type_maintenance.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.message_type_maintenance.dto.MessageTypeDetail;
import com.hand.spc.message_type_maintenance.service.IMessageTypeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class MessageTypeDetailController extends BaseController {

    @Autowired
    private IMessageTypeDetailService service;


    @RequestMapping(value = "/pspc/message/type/detail/query")
    @ResponseBody
    public ResponseData query(MessageTypeDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    //头保存方法
    @RequestMapping(value = "/pspc/message/type/detail/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MessageTypeDetail> dto, BindingResult result, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        try {
            return service.submit(requestCtx, dto);
        } catch (Exception ex) {
            ResponseData responseData = new ResponseData();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }
    }

    @RequestMapping(value = "/pspc/message/type/detail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MessageTypeDetail> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

//    //查找有效性
//    @RequestMapping(value = "/pspc/message/type/queryElementStatus")
//    @ResponseBody
//    public ResponseData queryElementStatus(MessageTypeDetail dto, HttpServletRequest request) {
//        IRequest requestCtx = createRequestContext(request);
//        try {
//            return service.queryElementStatus(requestCtx, dto);
//        } catch (Exception ex) {
//            ResponseData responseData = new ResponseData();
//            responseData.setMessage(ex.getMessage());
//            responseData.setSuccess(false);
//            return responseData;
//        }
//    }

    //查找显示类型主题
    @RequestMapping(value = "/pspc/message/type/queryMessageTypeTheme")
    @ResponseBody
    public ResponseData queryMessageType(MessageTypeDetail dto, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        try {
            return service.queryMessageType(requestCtx, dto);
        } catch (Exception ex) {
            ResponseData responseData = new ResponseData();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }
    }

    //查找显示消息内容
    @RequestMapping(value = "/pspc/message/type/queryMessageTypeContent")
    @ResponseBody
    public ResponseData queryMessageTypeContent(MessageTypeDetail dto, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        try {
            return service.queryMessageTypeContent(requestCtx, dto);
        } catch (Exception ex) {
            ResponseData responseData = new ResponseData();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }
    }
}