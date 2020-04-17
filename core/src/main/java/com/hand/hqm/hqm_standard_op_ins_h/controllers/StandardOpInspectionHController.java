package com.hand.hqm.hqm_standard_op_ins_h.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;
import com.hand.hqm.hqm_standard_op_ins_h.service.IStandardOpInspectionHService;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class StandardOpInspectionHController extends BaseController{

    @Autowired
    private IStandardOpInspectionHService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/standard/op/inspection/h/query")
    @ResponseBody
    public ResponseData query(StandardOpInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    @RequestMapping(value = "/hqm/standard/op/inspection/h/save")
    @ResponseBody
    public ResponseData save(StandardOpInspectionH dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.save(requestContext,dto));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/standard/op/inspection/h/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<StandardOpInspectionH> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/standard/op/inspection/h/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<StandardOpInspectionH> dto){
        service.reBatchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/hqm/standard/op/inspection/h/select")
    @ResponseBody
    public ResponseData select(StandardOpInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    

    }