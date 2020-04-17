package com.hand.spc.pspc_attachment_relation.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;
import com.hand.spc.pspc_attachment_group.service.IAttachmentGroupService;
import com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation;
import com.hand.spc.pspc_attachment_relation.service.IAttachmentRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AttachmentRelationController extends BaseController {

    @Autowired
    private IAttachmentRelationService service;


    @RequestMapping(value = "/pspc/attachment/relation/query")
    @ResponseBody
    public ResponseData query(AttachmentRelation dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     *
     * @Description 保存附着对象组与附着对象关系
     *
     * @author yuchao.wang
     * @date 2019/8/10 14:59
     * @param dto
     * @param result
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/attachment/relation/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<AttachmentRelation> dto, BindingResult result, HttpServletRequest request){
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            responseData.setSuccess(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }

        try {
            responseData = service.saveAttachmentRelation(requestCtx, dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @RequestMapping(value = "/pspc/attachment/relation/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AttachmentRelation> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     *
     * @Description 删除附着对象组与附着对象关系
     *
     * @author yuchao.wang
     * @date 2019/8/10 14:59
     * @param dto
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/attachment/relation/delete")
    @ResponseBody
    public ResponseData deleteRelation(HttpServletRequest request, @RequestBody List<AttachmentRelation> dto){
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);

        try {
            service.deleteRelation(requestCtx, dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
}