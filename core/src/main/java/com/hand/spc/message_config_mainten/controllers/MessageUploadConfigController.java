package com.hand.spc.message_config_mainten.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.message_config_mainten.dto.MessageUploadConfig;
import com.hand.spc.message_config_mainten.service.IMessageUploadConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class MessageUploadConfigController extends BaseController{

    @Autowired
    private IMessageUploadConfigService service;


    @RequestMapping(value = "/pspc/message/upload/config/query")
    @ResponseBody
    public ResponseData query(MessageUploadConfig dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryData(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/message/upload/config/submit")
    @ResponseBody
    public ResponseData update(MessageUploadConfig dto, HttpServletRequest request){

        IRequest requestCtx = createRequestContext(request);
        try {
            return service.update(requestCtx, dto);
        }catch (Exception ex){
            ResponseData responseData=new ResponseData();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }

    }

    @RequestMapping(value = "/pspc/message/upload/config/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MessageUploadConfig> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }