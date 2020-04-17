package com.hand.hqm.hqm_pqc_inspection_h.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PqcInspectionHController extends BaseController{

    @Autowired
    private IPqcInspectionHService service;


    /**
     * 基础查询
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/pqc/inspection/h/query")
    @ResponseBody
    public ResponseData query(PqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    
    /**
     * 综合查询
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/pqc/inspection/h/qmsQuery")
    @ResponseBody
    public ResponseData qmsQuery(PqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.qmsQuery(requestContext,dto,page,pageSize));
    }
    
    /**
     * 基础查询
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/pqc/inspection/h/select")
    @ResponseBody
    public ResponseData select(PqcInspectionH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    
    /**
     * 保存
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/pqc/inspection/h/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PqcInspectionH> dto, BindingResult result, HttpServletRequest request){
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
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/pqc/inspection/h/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PqcInspectionH> dto){
        service.reBatchDelete(dto);
        return new ResponseData();
    }
    /**
	 * tianmin.wang
	 * 检验单管理-新建PQC巡验单
	 * @param dto
	 * @param request
	 * @return 
	 * 20190726
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/h/addnew")
	@ResponseBody
	public ResponseData addNewInspection(PqcInspectionH dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		
		IRequest requestContext = createRequestContext(request);
		
		try {
		responseData = service.addNewInspection(dto,requestContext,request);
		}
		catch(Exception e){
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
	
	/**
	 * 保存更改
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/h/savechange")
	@ResponseBody
	public ResponseData saveChange(PqcInspectionH dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		
		IRequest requestContext = createRequestContext(request);
		
		try {
		responseData = service.saveChangeAll(dto,requestContext,request);
		}
		catch(Exception e){
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
	
	
	/**
	 * 提交
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/h/commit")
	@ResponseBody
	public ResponseData commit(PqcInspectionH dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestContext = createRequestContext(request);
		try {
		responseData = service.commitItem(dto,requestContext,request);
		}
		catch(Exception e){
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
	
	/**
	 * 审核
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/h/audit")
	@ResponseBody
	public ResponseData audit(PqcInspectionH dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		
		IRequest requestContext = createRequestContext(request);
		
		try {
		responseData = service.auditItem(dto,requestContext,request);
		}
		catch(Exception e){
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
	/**
	 * 工序扫描
	 * @param dto
	 * @param request
	 * @return
	 * @throws Throwable 
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/h/operationscan")
    @ResponseBody
    public ResponseData operationScan(PqcInspectionH dto, HttpServletRequest request) throws Throwable {
        IRequest requestContext = createRequestContext(request);
        try {
        	service.operationScan(requestContext,request,dto);
        }catch(RuntimeException e) {
        	ResponseData re = new ResponseData(false);
        	re.setMessage(e.getMessage());
        	return re;
        }catch(Exception e) {
        	throw new Throwable(e.getMessage());
        }
        
        return new ResponseData();
    }
	/**
	 * 
	 * @description 工序获取
	 * @author tianmin.wang
	 * @date 2020年2月21日 
	 * @param dto
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/pqc/inspection/h/get/process")
    @ResponseBody
    public ResponseData getProcess(@RequestParam Float headerId, HttpServletRequest request) throws Throwable {
        try {
        	service.getProcess(request,headerId);
        }catch(RuntimeException e) {
        	ResponseData re = new ResponseData(false);
        	re.setMessage(e.getMessage());
        	return re;
        }catch(Exception e) {
        	throw new Throwable(e.getMessage());
        }
        
        return new ResponseData();
	}
	@RequestMapping(value = "/hqm/pqc/inspection/h/operationget")
    @ResponseBody
    public ResponseData operationGet(@RequestParam String line,@RequestParam String inspectionNum, HttpServletRequest request) throws Throwable {
        try {
        	service.operationGet(request,line,inspectionNum);
        }catch(RuntimeException e) {
        	ResponseData re = new ResponseData(false);
        	re.setMessage(e.getMessage());
        	return re;
        }catch(Exception e) {
        	throw new Throwable(e.getMessage());
        }
        return new ResponseData();
	}
    }