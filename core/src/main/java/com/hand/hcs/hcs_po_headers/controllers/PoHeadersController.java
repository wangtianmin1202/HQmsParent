package com.hand.hcs.hcs_po_headers.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_po_headers.dto.PoHeaders;
import com.hand.hcs.hcs_po_headers.service.IPoHeadersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PoHeadersController extends BaseController{

    @Autowired
    private IPoHeadersService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/po/headers/query")
    @ResponseBody
    public ResponseData query(PoHeaders dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hcs/po/headers/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PoHeaders> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hcs/po/headers/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PoHeaders> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
      * 采购订单确认
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/po/headers/confirm")
    @ResponseBody
    public ResponseData confirm(HttpServletRequest request,@RequestBody List<PoHeaders> dto){
    	ResponseData responseData = new ResponseData();
    	try{
    		IRequest requestContext = createRequestContext(request);
        	responseData.setRows(service.confirm(requestContext, dto));
        	responseData.setMessage("确认成功");
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setSuccess(false);
    		responseData.setMessage("确认失败");
    	}
        return responseData;
    }
    /**
     * 校验发运行
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/po/headers/checkLine")
    @ResponseBody
    public ResponseData checkLine(HttpServletRequest request,@RequestBody List<PoHeaders> dto){
    	ResponseData responseData = new ResponseData();
    	try{
    		IRequest requestContext = createRequestContext(request);
        	responseData.setRows(service.checkLine(requestContext, dto));
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setSuccess(false);
    	}
        return responseData;
    }
    }