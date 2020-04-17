package com.hand.hcs.hcs_supplier_onhand_quantities.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_supplier_onhand_quantities.dto.SupplierOnhandQuantities;
import com.hand.hcs.hcs_supplier_onhand_quantities.service.ISupplierOnhandQuantitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.util.List;

    @Controller
    public class SupplierOnhandQuantitiesController extends BaseController{

    @Autowired
    private ISupplierOnhandQuantitiesService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/query")
    @ResponseBody
    public ResponseData query(SupplierOnhandQuantities dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/select")
    @ResponseBody
    public ResponseData select(SupplierOnhandQuantities dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SupplierOnhandQuantities> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        for(SupplierOnhandQuantities data : dto) {
        	if(data.getItemId() == null) {
        		ResponseData responseData = new ResponseData(false);
                responseData.setMessage("物料编码必输");
                return responseData;
        	}
        }
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
        	responseData.setRows(service.batchUpdate(requestCtx, dto));
        	responseData.setMessage("保存成功");
        }catch(DataAccessException e) {
        	e.printStackTrace();
        	responseData.setMessage("此工厂下已存在该版本物料的库存数据，不能重复新增");
        	responseData.setSuccess(false);
        }catch(Exception e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SupplierOnhandQuantities> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 查询用户对应供应商
     * @param request
     * @return
     */
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/querySupplier")
    @ResponseBody
    public ResponseData querySupplier(HttpServletRequest request){
    	IRequest requestContext = createRequestContext(request);

        return service.querySupplier(requestContext);
    }
    
    /**
     * 页面查询
     * @param dto 查询内容(树形结构)
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/queryTree")
    @ResponseBody
    public ResponseData queryTree(SupplierOnhandQuantities dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryTree(requestContext,dto,page,pageSize));
    }
    /**
     * 更新
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/updateData")
    @ResponseBody
    public ResponseData updateData(HttpServletRequest request,@RequestBody List<SupplierOnhandQuantities> dto){
    	IRequest requestContext = createRequestContext(request);
    	service.updateData(requestContext, dto);
        return new ResponseData();
    }
    /**
     * 新增采购件
     * @param request 请求
     * @param dto 采购件信息
     * @return
     */
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/addData")
    @ResponseBody
    public ResponseData addData(HttpServletRequest request,SupplierOnhandQuantities dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
			service.addData(requestContext, dto);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
        return responseData;
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/supplier/onhand/quantities/deleteData")
    @ResponseBody
    public ResponseData deleteData(HttpServletRequest request,SupplierOnhandQuantities dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
			service.deleteData(requestContext, dto);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
        return responseData;
    }
    }