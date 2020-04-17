package com.hand.hqm.hqm_fmea.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea.service.IFmeaService;
import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class FmeaController extends BaseController{

    @Autowired
    private IFmeaService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/fmea/query")
    @ResponseBody
    public ResponseData query(Fmea dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/fmea/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Fmea> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/fmea/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Fmea> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 保存PFMEA 行信息
     * @param dto list 操作数据集
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/pfmea/save")
    @ResponseBody
    public ResponseData psaveLine(HttpServletRequest request,@RequestBody List<Fmea> dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
    		responseData=service.psave(requestContext, dto);
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
    	
        return responseData;
    }
    /**
     * 保存DFMEA 行信息
     * @param dto list 操作数据集
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/dfmea/save")
    @ResponseBody
    public ResponseData dsaveLine(HttpServletRequest request,@RequestBody List<Fmea> dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
    		responseData=service.dsave(requestContext, dto);
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
    	
        return responseData;
    }
    /**
     * PFMEA页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/pfmea/select")
    @ResponseBody
    public ResponseData pselect(Fmea dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.pmyselect(requestContext,dto,page,pageSize));
    }
    /**
     * 新增控制计划数据
     * @param dto  操作数据集
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/pfmea/level/contralPlan/add")
    @ResponseBody
    public ResponseData addContralPlan(Fmea dto, HttpServletRequest request) {
    	ResponseData responseData = new ResponseData();
    	IRequest requestCtx = createRequestContext(request);
    	try {
    		responseData = service.addContralPlan(requestCtx, dto);
    	} catch (Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.toString());
    		responseData.setSuccess(false);
    	}
    	return responseData;
    }
    /**
     * DFMEA页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/dfmea/select")
    @ResponseBody
    public ResponseData dselect(Fmea dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.dmyselect(requestContext,dto,page,pageSize));
    }
    
    /**
     * PFMEA页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/dfmea/selectversionP")
    @ResponseBody
    public ResponseData PselectV(Fmea dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.PselectV(requestContext,dto,page,pageSize));
    }
    /**
     * DFMEA页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/dfmea/selectversionD")
    @ResponseBody
    public ResponseData DselectV(Fmea dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.DselectV(requestContext,dto,page,pageSize));
    }
    }