package com.hand.spc.message_type_maintenance.controllers;

import com.hand.spc.message_type_maintenance.mapper.MessageTypeUploadRelMapper;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.message_type_maintenance.dto.MessageTypeUploadRel;
import com.hand.spc.message_type_maintenance.service.IMessageTypeUploadRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class MessageTypeUploadRelController extends BaseController{

    @Autowired
    private IMessageTypeUploadRelService service;
    @Autowired
    private MessageTypeUploadRelMapper messageTypeUploadRelMapper;
    @Autowired
    private IMessageTypeUploadRelService iMessageTypeUploadRelService;


    @RequestMapping(value = "/pspc/message/type/upload/rel/query/ui")
    @ResponseBody
    public ResponseData query(MessageTypeUploadRel dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectData(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/message/type/upload/rel/submit/ui")
    @ResponseBody
    public ResponseData update(@RequestBody List<MessageTypeUploadRel> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        //校验行表数据唯一性
        ResponseData responseData = service.validateUnique(requestCtx,dto);
        if(!responseData.isSuccess()){
            return responseData;
        }
        List<MessageTypeUploadRel> list = service.batchUpdate(requestCtx, dto);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/pspc/message/type/upload/rel/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MessageTypeUploadRel> dto){
        ResponseData responseData = new ResponseData();
        //删除头的时候删除对应的行
        for(MessageTypeUploadRel map : dto){
            MessageTypeUploadRel messageTypeUploadRel = new MessageTypeUploadRel();
            messageTypeUploadRel.setMessageTypeId(map.getMessageTypeId());
            messageTypeUploadRel.setUploadConfigId(map.getUploadConfigId());

            //通过mapper的select方法能通过唯一性索引找到唯一一条数据，再从唯一一条数据找主键id
            List<MessageTypeUploadRel> messageTypeUploadRels = messageTypeUploadRelMapper.select(messageTypeUploadRel);
            //找到主键以后删除即可
            MessageTypeUploadRel messageTypeUploadRelDelete = new MessageTypeUploadRel();
            messageTypeUploadRelDelete.setRelId(messageTypeUploadRels.get(0).getRelId());
            iMessageTypeUploadRelService.deleteByPrimaryKey(messageTypeUploadRelDelete);
            responseData.setSuccess(true);
        }
        return new ResponseData();
    }
    }