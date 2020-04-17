package com.hand.hqm.hqm_fqc_inspection_template_h.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_fqc_inspection_template_h.dto.FqcInspectionTemplateH;
import com.hand.hqm.hqm_fqc_inspection_template_h.service.IFqcInspectionTemplateHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;
	/*
	 * created by tianmin.wang on 2019/7/29
	 */
    @Controller
    public class FqcInspectionTemplateHController extends BaseController{

    @Autowired
    private IFqcInspectionTemplateHService service;


    /**
     * 
     * @description 查询
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/fqc/inspection/template/h/query")
    @ResponseBody
    public ResponseData query(FqcInspectionTemplateH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }

    /**
     * 
     * @description 保存
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/fqc/inspection/template/h/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FqcInspectionTemplateH> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.versionNumberBatchUpdate(requestCtx, dto));
    }
    /*
     * @param null
     * @return
     * @author tianmin.wang
     * @date 2019/7/29
     * @description  发布功能，修改status字段
     **/
    @RequestMapping(value = "/hqm/fqc/inspection/template/h/issue")
    @ResponseBody
    public ResponseData operationIssue(@RequestBody List<FqcInspectionTemplateH> dto, HttpServletRequest request){
    	IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
        service.updateStatus(requestCtx, dto);
        }
        catch (Exception e) {
        	responseData.setSuccess(false);
        	responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
    
    /**
     * 
     * @description 删除
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/fqc/inspection/template/h/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<FqcInspectionTemplateH> dto){
        service.reBatchDelete(dto);
        return new ResponseData();
    }
    }