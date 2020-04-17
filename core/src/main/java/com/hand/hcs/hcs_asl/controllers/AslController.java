package com.hand.hcs.hcs_asl.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_asl.dto.Asl;
import com.hand.hcs.hcs_asl.service.IAslService;
import com.hand.hcs.hcs_asl_control.dto.AslControl;
import com.hand.hcs.hcs_asl_control.service.IAslControlService;
import com.hand.hqm.hqm_asl_iqc_control.dto.AslIqcControl;
import com.hand.hqm.hqm_asl_iqc_control.service.IAslIqcControlService;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;
import com.hand.hqm.hqm_supp_item_exemption.service.ISuppItemExemptionService;

    @Controller
    public class AslController extends BaseController{

    @Autowired
    private IAslService service;
    
    @Autowired
    private ISuppItemExemptionService suppItemExemptionService;//iqc免检service
    
    @Autowired
    private IAslIqcControlService aslIqcControlService;//物料启用设置service

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/asl/query")
    @ResponseBody
    public ResponseData query(Asl dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }
    
    
    //iqc免检批量维护弹窗查询
    @RequestMapping(value = "/hcs/asl/IQCMJselectDatas")
    @ResponseBody
    public ResponseData query1(Asl dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectDatas(requestContext,dto,page,pageSize));
    }
    
    //iqc免检批量维护保存
    @RequestMapping(value = "/hcs/asl/IQCMJsubmit")
    @ResponseBody
    public ResponseData IQCMJsubmit(@RequestBody List<SuppItemExemption> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(suppItemExemptionService.batchUpdate(requestCtx, dto));
    }
    
    
    //物料启用批量维护弹窗查询
    @RequestMapping(value = "/hcs/asl/IQCControlSelectDatas")
    @ResponseBody
    public ResponseData IQCControlSelectDatas(Asl dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.IQCControlSelectDatas(requestContext,dto,page,pageSize));
    }
        
   //物料启用批量维护保存
    @RequestMapping(value = "/hcs/asl/iqccontrolsubmit")
    @ResponseBody
    public ResponseData IQCControlsubmit(@RequestBody List<AslIqcControl> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(aslIqcControlService.batchTablesUpdate(requestCtx, dto));
    }
  
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hcs/asl/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Asl> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hcs/asl/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Asl> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 到货提前期更新
     * @param dto 更新数据
     * @param result 结果参数
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/asl/controlUpdate")
    @ResponseBody
    public ResponseData controlUpdate(@RequestBody List<Asl> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.controlUpdate(requestCtx, dto));
    }
    }