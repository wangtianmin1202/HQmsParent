package com.hand.hqm.hqm_dfmea_detail.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalidTree;
import com.hand.hqm.hqm_db_p_management.service.IHqmpInvalidTreeService;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_dfmea_detail.service.IDfmeaDetailService;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

    @Controller
    public class DfmeaDetailController extends BaseController{

    @Autowired
    private IDfmeaDetailService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/dfmea/detail/query")
    @ResponseBody
    public ResponseData query(DfmeaDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/dfmea/detail/query/condition")
    @ResponseBody
    public ResponseData queryCondition(DfmeaDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.queryCondition(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/dfmea/detail/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DfmeaDetail> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/dfmea/detail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DfmeaDetail> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
  
	/**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:39 2019/8/19
	 * @Param [dto, page, pageSize, request]
	 */
	@RequestMapping(value = "/hqm/dfmea/query/tree/data")
	@ResponseBody
	public ResponseData queryTreeData(DfmeaDetail dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryTreeData(requestContext, dto));
	}
	
	/**
     * 查询打印sql
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
   
	 @RequestMapping(value = "/hqm/dfmea/query/print")
	    @ResponseBody
	    public ResponseData queryprintData(DfmeaDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.queryprintData(requestContext,dto,page,pageSize));
	    }
	 /**
	     * 查询打印头表sql
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 
	 @RequestMapping(value = "/hqm/dfmea/print/header/query")
	    @ResponseBody
	    public DfmeaDetail query_header(@RequestParam Float kid,HttpServletRequest request) {
	        return  service.queryHeaderData(kid);//select(requestContext,dto,page,pageSize)
	    }
	/**
	 * @Author han.zhang
	 * @Description 附着对象更新、新增
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@RequestMapping(value = "/hqm/dfmea/save/or/update")
	@ResponseBody
	public ResponseData updateOrSave(DfmeaDetail dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			responseData = service.updateOrAdd(requestCtx, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.toString());
			responseData.setSuccess(false);
		}
		return responseData;
	}

	/**
	 * @Author han.zhang
	 * @Description 根据主键删除附着对象
	 * @Date 10:51 2019/8/20
	 * @Param [dto, request]
	 */
	@RequestMapping(value = "/hqm/dfmea/delete/row")
	@ResponseBody
	public ResponseData deleteRow(DfmeaDetail dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData(true);
		try {
			service.deleteRow(dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}
	  /**
     * 提交
     * @param request
     * @param dfmea 数据集合
     * @return 结果集
     */
	 @RequestMapping(value = "/hqm/dfmea/confirm")
	    @ResponseBody
	    public ResponseData confirm(HttpServletRequest request,@RequestParam("list") List<Float> dto){
	    	ResponseData responseData = new ResponseData();
	    	IRequest requestCtx = createRequestContext(request);
	    	try{
	    		responseData= service.confirm(requestCtx, dto);
	        	
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		responseData.setSuccess(false);
	    		responseData.setMessage("提交失败");
	    	}
	        return responseData;
	    }
	 /**
	     * 提交
	     * @param request
	     * @param dfmea获取数据
	     * @return 结果集
	     */
	   @RequestMapping(value = "/hqm/dfmea/getmessage")
	    @ResponseBody
	    public ResponseData getdata(HttpServletRequest request,@RequestParam("list") List<Long> dto){
	    	ResponseData responseData = new ResponseData();
	    	IRequest requestCtx = createRequestContext(request);
	    	try{
	    		responseData= service.getdata(requestCtx, dto);
	        	
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		responseData.setSuccess(false);
	    		responseData.setMessage("提交失败");
	    	}
	        return responseData;
	    }
	   /**
	     * 页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	    @RequestMapping(value = "/hqm/dfmea/detail/select")
	    @ResponseBody
	    public ResponseData select(DfmeaDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
	    }
	    
	    /**
	     * 
	     * @description 明细excel导入
	     * @author tianmin.wang
	     * @date 2019年12月9日 
	     * @param request
	     * @return
	     * @throws Throwable
	     */
	    @RequestMapping(value = "/hqm/dfmea/detail/excelimport/upload")
		@ResponseBody
		public ResponseData stExcelImport(HttpServletRequest request) throws Throwable {
			ResponseData responseData = new ResponseData();
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			IRequest requestCtx = createRequestContext(request);
			// 获取文件map集合
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			List<DfmeaDetail> returnList = new ArrayList<DfmeaDetail>();
			for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
				MultipartFile forModel = entry.getValue();
				try {
					returnList.addAll(service.detailExcelImport(request,requestCtx, forModel));
				} catch (RuntimeException e) {
					responseData = new ResponseData(false);
					responseData.setMessage(e.getMessage());
					return responseData;
				}
			}
			responseData.setRows(returnList);
			return responseData;
		}
	    /**
		 * 
		 * @description excel导出
		 * @author tianmin.wang
		 * @date 2019年11月22日 
		 * @param dto
		 * @param request
		 * @param response
		 * @throws Throwable
		 */
		@RequestMapping(value = "/hqm/dfmea/detail/exportexcel")
		@ResponseBody
		public void exportExcel(@RequestParam String fmeaId, HttpServletRequest request, HttpServletResponse response ) throws Throwable {
			service.exportExcel(Float.valueOf(fmeaId),request,response);
		}
    }