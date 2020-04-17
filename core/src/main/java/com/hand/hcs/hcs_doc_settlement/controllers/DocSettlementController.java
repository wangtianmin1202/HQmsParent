package com.hand.hcs.hcs_doc_settlement.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;
import com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DocSettlementController extends BaseController{

    @Autowired
    private IDocSettlementService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/doc/settlement/query")
    @ResponseBody
    public ResponseData query(DocSettlement dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hcs/doc/settlement/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DocSettlement> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hcs/doc/settlement/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DocSettlement> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 对账单创建
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/doc/settlement/createSettlement")
    @ResponseBody
    public ResponseData createSettlement(HttpServletRequest request,@RequestBody List<DocSettlement> dto){
        ResponseData responseData = new ResponseData();
        IRequest requestContext = createRequestContext(request);
        try {
        	String docStatementNum = service.createSettlement(requestContext, dto);
        	responseData.setMessage(docStatementNum + "对账单创建成功");
        }catch(Exception e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 对账单明细查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hcs/doc/settlement/queryDetail")
    @ResponseBody
    public ResponseData queryDetail(DocSettlement dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryDetail(requestContext,dto,page,pageSize));
    }
    /**
     * 取消对账单行
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/doc/settlement/cancel")
    @ResponseBody
    public ResponseData cancel(HttpServletRequest request,@RequestBody List<DocSettlement> dto){
        ResponseData responseData = new ResponseData();
        IRequest requestContext = createRequestContext(request);
        try {
        	List<DocSettlement> list = service.cancel(requestContext, dto);
        	responseData.setRows(list);
        	responseData.setMessage("取消成功");
        }catch(Exception e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 添加至已有对账单  确认
     * @param request 请求
     * @param dto 对账单信息
     * @return 对账单集合
     */
    @RequestMapping(value = "/hcs/doc/settlement/confirm")
    @ResponseBody
    public ResponseData confirm(HttpServletRequest request,@RequestBody List<DocSettlement> dto){
        ResponseData responseData = new ResponseData();
        IRequest requestContext = createRequestContext(request);
        try {
        	List<DocSettlement> list = service.confirm(requestContext, dto);
        	responseData.setRows(list);
        }catch(Exception e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 发票明细预览 数据获取
     * @param request 请求
     * @param dto 对账单信息
     * @return 数据源
     */
    @RequestMapping(value = "/hcs/doc/settlement/printQuery")
    @ResponseBody
    public DocSettlement printQuery(HttpServletRequest request,DocSettlement dto){
        DocSettlement docSettlement = new DocSettlement();
        try {
        	docSettlement = service.printQuery(dto);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return docSettlement;
    }
    @RequestMapping(value = "/hcs/doc/settlement/excel/export")
    @ResponseBody
    public void detailExcelExport(HttpServletRequest request,DocSettlement dto,HttpServletResponse response){
        //DocSettlement docSettlement = new DocSettlement();
        try {
        	service.detailExcelExport(dto,request,response);
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    }