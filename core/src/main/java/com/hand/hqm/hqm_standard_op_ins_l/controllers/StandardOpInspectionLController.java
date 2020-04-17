package com.hand.hqm.hqm_standard_op_ins_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;
import com.hand.hqm.hqm_standard_op_ins_l.service.IStandardOpInspectionLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class StandardOpInspectionLController extends BaseController{

    @Autowired
    private IStandardOpInspectionLService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/standard/op/inspection/l/query")
    @ResponseBody
    public ResponseData query(StandardOpInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/standard/op/inspection/l/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<StandardOpInspectionL> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/standard/op/inspection/l/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<StandardOpInspectionL> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/hqm/standard/op/inspection/l/saveHeadLine")
    @ResponseBody
    public ResponseData saveHeadLine(HttpServletRequest request,@RequestBody List<StandardOpInspectionL> dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
//    		service.saveHeadLine(requestContext, dto);
    		responseData.setRows(service.saveHeadLine(requestContext, dto));
    	}catch(Exception e) {
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
    	
        return responseData;
    }
    
    
    @RequestMapping(value = "/hqm/standard/op/inspection/l/select")
    @ResponseBody
    public ResponseData select(StandardOpInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    }