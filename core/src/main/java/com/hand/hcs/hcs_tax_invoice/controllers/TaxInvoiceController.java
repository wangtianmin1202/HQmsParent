package com.hand.hcs.hcs_tax_invoice.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_tax_invoice.dto.TaxInvoice;
import com.hand.hcs.hcs_tax_invoice.service.ITaxInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class TaxInvoiceController extends BaseController{

    @Autowired
    private ITaxInvoiceService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/tax/invoice/query")
    @ResponseBody
    public ResponseData query(TaxInvoice dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hcs/tax/invoice/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<TaxInvoice> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hcs/tax/invoice/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<TaxInvoice> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 查询今年最大流水号
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/tax/invoice/queryMaxNum")
    @ResponseBody
    public String queryMaxNum(HttpServletRequest request,TaxInvoice dto) {
    	IRequest requestContext = createRequestContext(request);  	
    	String num = service.queryMaxNum(requestContext, dto);
    	return num;
    }
    /**
     * 创建应收发票
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/tax/invoice/createInvoice")
    @ResponseBody
    public ResponseData createInvoice(HttpServletRequest request,TaxInvoice dto) {
    	ResponseData responseData = new ResponseData();
    	try {
    		IRequest requestContext = createRequestContext(request);  	
        	List<TaxInvoice> taxInvoiceList = service.createInvoice(requestContext, dto);
        	responseData.setRows(taxInvoiceList);
        	responseData.setMessage("应收发票创建成功");
    	}catch(DataAccessException e){
    		e.printStackTrace();
    		responseData.setMessage("dataAccess");
    		responseData.setSuccess(false);
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
    	return responseData;
    }
    /**
     * 应收发票编辑 -- 提交
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/tax/invoice/confirm")
    @ResponseBody
    public ResponseData confirm(HttpServletRequest request,@RequestBody List<TaxInvoice> dto) {
    	ResponseData responseData = new ResponseData();
    	try {
    		IRequest requestContext = createRequestContext(request);  	
        	service.confirm(requestContext, dto.get(0));
        	responseData.setMessage("提交成功");
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
    	return responseData;
    }
    /**
     * 删除
     * @param request 请求
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/tax/invoice/changeFlag")
    @ResponseBody
    public ResponseData changeFlag(HttpServletRequest request,@RequestBody List<TaxInvoice> dto) {
    	ResponseData responseData = new ResponseData();
    	try {
    		IRequest requestContext = createRequestContext(request);  	
        	service.changeFlag(requestContext, dto.get(0));
        	responseData.setMessage("删除成功");
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
    	return responseData;
    }
    /**
     *  应收发票查询 拒绝
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/tax/invoice/refused")
    @ResponseBody
    public ResponseData refused(HttpServletRequest request,@RequestBody List<TaxInvoice> dto) {
    	ResponseData responseData = new ResponseData();
    	try {
    		IRequest requestContext = createRequestContext(request);  	
        	service.refused(requestContext, dto);
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
    	return responseData;
    }
    }