package com.hand.spc.pspc_classify_group.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_classify_group.dto.ClassifyGroup;
import com.hand.spc.pspc_classify_group.service.IClassifyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ClassifyGroupController extends BaseController{

    @Autowired
    private IClassifyGroupService service;


    @RequestMapping(value = "/pspc/classify/group/query")
    @ResponseBody
    public ResponseData query(ClassifyGroup dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/classify/group/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ClassifyGroup> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/classify/group/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ClassifyGroup> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @Author han.zhang
     * @Description 分类组保存
     * @Date 10:24 2019/8/7
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/classify/group/save/classify/group")
    @ResponseBody
    public ResponseData classifyGroupSave(ClassifyGroup dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData = service.saveOrUpdate(dto);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * @Author han.zhang
     * @Description 删除分类组及其关系
     * @Date 17:17 2019/8/8
     * @Param [dto, request]
     */
    @RequestMapping(value = "/pspc/classify/group/delete/group/relation")
    @ResponseBody
    public ResponseData deleteGroupRealation(ClassifyGroup dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData = service.deleteGroupAndLine(requestContext,dto);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return new ResponseData();
    }
}