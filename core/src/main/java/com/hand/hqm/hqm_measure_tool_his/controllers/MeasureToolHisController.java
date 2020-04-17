package com.hand.hqm.hqm_measure_tool_his.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHis;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHisVO;
import com.hand.hqm.hqm_measure_tool_his.service.IMeasureToolHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class MeasureToolHisController extends BaseController{

    @Autowired
    private IMeasureToolHisService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/measure/tool/his/query")
    @ResponseBody
    public ResponseData query(MeasureToolHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }
    @RequestMapping(value = "/hqm/measure/tool/his/queryCheckType")
    @ResponseBody
    public List<MeasureToolHisVO> queryCheckType(MeasureToolHis dto , HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return service.queryCheckType(requestContext,dto);
    }
    @RequestMapping(value = "/hqm/measure/tool/his/queryCheckTypeGrid")
    @ResponseBody
    public ResponseData queryCheckTypeGrid(MeasureToolHis dto , @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.queryCheckTypeGrid(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/hqm/measure/tool/his/queryDepartmentUsage")
    @ResponseBody
    public List<MeasureToolHisVO> queryDepartmentUsage(MeasureToolHis dto , HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return service.queryDepartmentUsage(requestContext,dto);
    }
    @RequestMapping(value = "/hqm/measure/tool/his/queryDepartmentUsageGrid")
    @ResponseBody
    public ResponseData queryDepartmentUsageGrid(MeasureToolHis dto , @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.queryDepartmentUsageGrid(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/measure/tool/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MeasureToolHis> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/measure/tool/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MeasureToolHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }