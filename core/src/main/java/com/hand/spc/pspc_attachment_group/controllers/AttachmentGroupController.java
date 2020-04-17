package com.hand.spc.pspc_attachment_group.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;
import com.hand.spc.pspc_attachment_group.service.IAttachmentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AttachmentGroupController extends BaseController {

    @Autowired
    private IAttachmentGroupService service;


    @RequestMapping(value = "/pspc/attachment/group/query")
    @ResponseBody
    public ResponseData query(AttachmentGroup dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     *
     * @Description 查询附着要素组及明细
     *
     * @author yuchao.wang
     * @date 2019/8/9 17:51
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/attachment/group/detail/query")
    @ResponseBody
    public ResponseData queryGroupDetail(AttachmentGroup dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<AttachmentGroup> attachmentGroupList = service.queryGroupDetail(requestContext, dto, page, pageSize);
        return new ResponseData(attachmentGroupList);
    }

    @RequestMapping(value = "/pspc/attachment/group/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<AttachmentGroup> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/attachment/group/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AttachmentGroup> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     *
     * @Description 删除附着对象组与控制要素组关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 23:06
     * @param request
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/attachment/group/relationship/delete")
    @ResponseBody
    public ResponseData deleteRelationship(HttpServletRequest request, @RequestBody List<AttachmentGroup> dto){
        ResponseData responseData = new ResponseData();

        try {
            responseData = service.deleteRelationship(dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * @Author han.zhang
     * @Description //TODO 控制要素组维护中新增 附着对象组
     * @Date 下午3:03 2019/10/18
     * @Param [dto, result, request]
     **/
    @RequestMapping(value = "/pspc/control/add/attachment/group/add")
    @ResponseBody
    public ResponseData addAttachGroup(@RequestBody List<SpcAttachment> dtos, HttpServletRequest request){
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData = service.addAttachGroup(requestCtx, dtos);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
}