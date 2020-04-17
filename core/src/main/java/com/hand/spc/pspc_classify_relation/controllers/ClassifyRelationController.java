package com.hand.spc.pspc_classify_relation.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_classify_relation.dto.ClassifyRelation;
import com.hand.spc.pspc_classify_relation.service.IClassifyRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ClassifyRelationController extends BaseController{

    @Autowired
    private IClassifyRelationService service;


    @RequestMapping(value = "/pspc/classify/relation/query")
    @ResponseBody
    public ResponseData query(ClassifyRelation dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/classify/relation/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ClassifyRelation> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/classify/relation/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ClassifyRelation> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @Author han.zhang
     * @Description 分类组下的分类项删除
     * @Date 16:21 2019/8/8
     * @Param [request, dto]
     */
    @RequestMapping(value = "/pspc/classify/relation/delete/classify")
    @ResponseBody
    public ResponseData deleteClassify(HttpServletRequest request,ClassifyRelation dto){
        ResponseData responseData = new ResponseData();
        try{
            service.deleteByPrimaryKey(dto);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }
}