package com.hand.spc.pspc_attachment.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;
import com.hand.spc.pspc_attachment.service.ISpcAttachmentService;
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
public class SpcAttachmentController extends BaseController {

    @Autowired
    private ISpcAttachmentService service;


    @RequestMapping(value = "/pspc/attachment/query")
    @ResponseBody
    public List<SpcAttachment> query(SpcAttachment dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return service.select(requestContext,dto,page,pageSize);
    }

    /**
     * @Author han.zhang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:39 2019/8/19
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/attachment/query/tree/data")
    @ResponseBody
    public ResponseData queryTreeData(SpcAttachment dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryTreeData(requestContext, dto));
    }

    @RequestMapping(value = "/pspc/attachment/query/by/group")
    @ResponseBody
    public ResponseData queryByCroupId(SpcAttachment dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<SpcAttachment> attachmentList = service.selectByCroupId(requestContext, dto, page, pageSize);
        return new ResponseData(attachmentList);
    }

    @RequestMapping(value = "/pspc/attachment/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SpcAttachment> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/attachment/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<SpcAttachment> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @Author han.zhang
     * @Description 附着对象更新、新增
     * @Date 18:10 2019/8/19
     * @Param [dto, result, request]
     */
    @RequestMapping(value = "/pspc/attachment/save/or/update")
    @ResponseBody
    public ResponseData updateOrSave(SpcAttachment dto, HttpServletRequest request){
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            responseData = service.updateOrAdd(requestCtx, dto);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    /**
     * @Author han.zhang
     * @Description 根据主键删除附着对象
     * @Date 10:51 2019/8/20
     * @Param [dto, request]
     */
    @RequestMapping(value = "/pspc/attachment/delete/row")
    @ResponseBody
    public ResponseData deleteRow(SpcAttachment dto, HttpServletRequest request){
        ResponseData responseData = new ResponseData(true);
        try {
            service.deleteRow(dto);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }
}