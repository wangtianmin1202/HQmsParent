package com.hand.hqm.hqm_pqc_inspection_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_pqc_inspection_l.service.IPqcInspectionLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PqcInspectionLController extends BaseController{

    @Autowired
    private IPqcInspectionLService service;


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
    @RequestMapping(value = "/hqm/pqc/inspection/l/query")
    @ResponseBody
    public ResponseData query(PqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
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
    @RequestMapping(value = "/hqm/pqc/inspection/l/select")
    @ResponseBody
    public ResponseData select(PqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    

    @RequestMapping(value = "/hqm/pqc/inspection/l/selectfornon")
    @ResponseBody
    public ResponseData selectfornon(PqcInspectionL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectfornon(requestContext,dto,page,pageSize));
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
    @RequestMapping(value = "/hqm/pqc/inspection/l/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PqcInspectionL> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/pqc/inspection/l/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PqcInspectionL> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 明细编辑保存
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/pqc/inspection/l/detaileditsave")
    @ResponseBody
    public ResponseData detailSave(PqcInspectionL dto,HttpServletRequest request){
    	ResponseData responseData = new ResponseData();
    	try{
    		responseData = service.detailSave(dto,request);
    	}catch(Exception e) {
    		responseData.setSuccess(false);
    		responseData.setMessage(e.getMessage());
    		
    	}
        return responseData;
    }
    }