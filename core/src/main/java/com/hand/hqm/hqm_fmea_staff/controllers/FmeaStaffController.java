package com.hand.hqm.hqm_fmea_staff.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.file_permission.dto.Permission;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea_staff.dto.FmeaStaff;
import com.hand.hqm.hqm_fmea_staff.service.IFmeaStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class FmeaStaffController extends BaseController{

    @Autowired
    private IFmeaStaffService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/fmea/staff/query")
    @ResponseBody
    public ResponseData query(FmeaStaff dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/fmea/staff/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FmeaStaff> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/fmea/staff/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<FmeaStaff> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/fmea/staff/select")
    @ResponseBody
    public ResponseData pselect(FmeaStaff dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    /**
     * 新建
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/fmea/staff/addnew")
    @ResponseBody
    public ResponseData addNewInspection(HttpServletRequest request,@RequestBody List<FmeaStaff> dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
    		responseData =  service.addNewInspection(requestContext, dto);
    	}catch(Exception e) {
    		responseData.setSuccess(false);
   			responseData.setMessage(e.getMessage());
    	}
    	
        return responseData;
    }
    }