package com.hand.spc.message_type_maintenance.controllers;

import com.hand.spc.message_type_maintenance.dto.MessageTypeDetail;
import com.hand.spc.message_type_maintenance.mapper.MessageTypeMapper;
import com.hand.spc.pspc_data_access_configuration.dto.DataAccessConfiguration;
import com.hand.spc.pspc_judgement_group.dto.JudgementGroup;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.message_type_maintenance.dto.MessageType;
import com.hand.spc.message_type_maintenance.service.IMessageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class MessageTypeController extends BaseController {

    @Autowired
    private IMessageTypeService service;
    @Autowired
    private MessageTypeMapper messageTypeMapper;


    @RequestMapping(value = "/pspc/message/type/query")
    @ResponseBody
    public ResponseData query(MessageType dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.mySelect(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/pspc/message/type/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MessageType> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/message/type/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MessageType> dto) {
        ResponseData responseData = new ResponseData();
        for (MessageType map : dto) {
            int count1 = messageTypeMapper.selectTypeCodeCount(map.getMessageTypeCode());
            if(count1>0){
                responseData.setSuccess(false);
                responseData.setMessage("此消息类型已被判异规则组引用，不允许删除！");
                return responseData;
            }
        }

        service.batchDelete(dto);
        return new ResponseData();
    }

    //查找有效性
    @RequestMapping(value = "/pspc/message/type/queryElementStatus")
    @ResponseBody
    public ResponseData queryElementStatus(MessageType dto, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        try {
            return service.queryElementStatus(requestCtx, dto);
        } catch (Exception ex) {
            ResponseData responseData = new ResponseData();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }
    }


    //查找显示类型主题
//    @RequestMapping(value = "/pspc/message/type/queryMessageType")
//    @ResponseBody
//    public ResponseData queryMessageType(MessageType dto, HttpServletRequest request) {
//        IRequest requestCtx = createRequestContext(request);
//        try {
//            return service.queryMessageType(requestCtx, dto);
//        } catch (Exception ex) {
//            ResponseData responseData = new ResponseData();
//            responseData.setMessage(ex.getMessage());
//            responseData.setSuccess(false);
//            return responseData;
//        }
//    }

}