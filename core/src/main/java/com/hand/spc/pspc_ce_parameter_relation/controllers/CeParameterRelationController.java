package com.hand.spc.pspc_ce_parameter_relation.controllers;

import com.hand.spc.pspc_classify_relation.dto.ClassifyRelation;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_ce_parameter_relation.dto.CeParameterRelation;
import com.hand.spc.pspc_ce_parameter_relation.service.ICeParameterRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class CeParameterRelationController extends BaseController{

    @Autowired
    private ICeParameterRelationService service;


    @RequestMapping(value = "/pspc/ce/parameter/relation/query")
    @ResponseBody
    public ResponseData query(CeParameterRelation dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/ce/parameter/relation/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CeParameterRelation> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/ce/parameter/relation/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CeParameterRelation> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @Author han.zhang
     * @Description 分类组下的控制要素删除
     * @Date 16:21 2019/8/8
     * @Param [request, dto]
     */
    @RequestMapping(value = "/pspc/classify/relation/delete/ce/paramter")
    @ResponseBody
    public ResponseData deleteCeParamter(HttpServletRequest request,CeParameterRelation dto){
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