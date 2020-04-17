package com.hand.hqm.hqm_msa_plan_line.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_msa_plan_line.dto.MsaPlanLine;
import com.hand.hqm.hqm_msa_plan_line.service.IMsaPlanLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class MsaPlanLineController extends BaseController{

    @Autowired
    private IMsaPlanLineService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/msa/plan/line/query")
    @ResponseBody
    public ResponseData query(MsaPlanLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/msa/plan/line/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MsaPlanLine> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/msa/plan/line/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MsaPlanLine> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     *  更新msa结果
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/msa/plan/line/updateResult")
    @ResponseBody
    public ResponseData updateResult(MsaPlanLine dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        service.updateResult(requestContext,dto);
        return new ResponseData();
    }
    /**
     * 更改分析日期
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/msa/plan/line/updateAnalyzeData")
    @ResponseBody
    public ResponseData updateAnalyzeData(MsaPlanLine dto, HttpServletRequest request) {
    	ResponseData responseData = new ResponseData();
        IRequest requestContext = createRequestContext(request);
        
        responseData.setRows(service.updateAnalyzeData(requestContext,dto));
        return responseData;
    }
    }