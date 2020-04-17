	package com.hand.hcs.hcs_doc_adjustment.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_doc_adjustment.dto.DocAdjustment;
import com.hand.hcs.hcs_doc_adjustment.service.IDocAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DocAdjustmentController extends BaseController{

    @Autowired
    private IDocAdjustmentService service;

    /**
     * 调整行查询
     * @param dto 查询参数
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/doc/adjustment/query")
    @ResponseBody
    public ResponseData query(DocAdjustment dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }
    /**
     * 应收发票明细 调整行保存
     * @param dto 调整行明细信息
     * @param result 结果参数
     * @param request	请求
     * @return 更新结果
     */
    @RequestMapping(value = "/hcs/doc/adjustment/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DocAdjustment> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
        	responseData.setRows(service.update(requestCtx, dto));
        	responseData.setMessage("保存成功");
        }catch(Exception e) {
        	e.printStackTrace();
        	responseData.setSuccess(false);
        	responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
    /**
     * 删除
     * @param request 请求
     * @param dto 删除信息
     * @return 
     */
    @RequestMapping(value = "/hcs/doc/adjustment/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DocAdjustment> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 查询最大行号
     * @param request 请求
     * @param dto
     * @return 最大行号
     */
    @RequestMapping(value = "/hcs/doc/adjustment/queryMaxNumByInvoicId")
    @ResponseBody
    public Integer queryMaxNumByInvoicId(HttpServletRequest request,DocAdjustment dto){
    	IRequest requestContext = createRequestContext(request);  	
        return service.queryMaxNumByInvoicId(requestContext,dto);
    }
    }