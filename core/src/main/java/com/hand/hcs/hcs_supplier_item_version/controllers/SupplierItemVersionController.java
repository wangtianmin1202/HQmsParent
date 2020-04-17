package com.hand.hcs.hcs_supplier_item_version.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_supplier_item_version.dto.SupplierItemVersion;
import com.hand.hcs.hcs_supplier_item_version.service.ISupplierItemVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.text.MessageFormat;
import java.util.List;

    @Controller
    public class SupplierItemVersionController extends BaseController{

    @Autowired
    private ISupplierItemVersionService service;
    @Autowired
	private IPromptService iPromptService;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/supplier/item/version/query")
    @ResponseBody
    public ResponseData query(SupplierItemVersion dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hcs/supplier/item/version/select")
    @ResponseBody
    public ResponseData select(SupplierItemVersion dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hcs/supplier/item/version/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SupplierItemVersion> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        for(SupplierItemVersion version :dto) {   
        	if("add".equals(version.get__status())) {        		
        		SupplierItemVersion supplierItemVersion = new SupplierItemVersion();
        		supplierItemVersion.setEnableFlag("Y");
        		supplierItemVersion.setItemVersion(version.getItemVersion());
        		supplierItemVersion.setItemId(version.getItemId());
        		List<SupplierItemVersion> supplierItemVersionList = service.select(requestCtx, supplierItemVersion, 0, 0);
        		if(supplierItemVersionList != null && supplierItemVersionList.size() >= 1) {
        			String msg = SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1097");
        			ResponseData responseData = new ResponseData(false);
        	        responseData.setMessage(msg);
        	        return responseData;
        		}
        	}
        }
        
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/supplier/item/version/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SupplierItemVersion> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 失效
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/supplier/item/version/changeFlag")
    @ResponseBody
    public ResponseData changeFlag(HttpServletRequest request,@RequestBody List<SupplierItemVersion> dto){
    	IRequest requestCtx = createRequestContext(request);
    	service.changeFlag(requestCtx,dto);
        return new ResponseData();
    }
    /**
     * 修改主版本
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/supplier/item/version/changeMainVersion")
    @ResponseBody
    public ResponseData changeMainVersion(HttpServletRequest request,SupplierItemVersion dto){
    	IRequest requestCtx = createRequestContext(request);
    	service.changeMainVersion(requestCtx,dto);
        return new ResponseData();
    }
    }