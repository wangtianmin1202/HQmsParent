package com.hand.hcs.hcs_barcode_relation.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_barcode_relation.dto.BarcodeRelation;
import com.hand.hcs.hcs_barcode_relation.service.IBarcodeRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class BarcodeRelationController extends BaseController{

    @Autowired
    private IBarcodeRelationService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/barcode/relation/query")
    @ResponseBody
    public ResponseData query(BarcodeRelation dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hcs/barcode/relation/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<BarcodeRelation> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hcs/barcode/relation/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<BarcodeRelation> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 确认绑定
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/barcode/relation/confirmBind")
    @ResponseBody
    public ResponseData confirmBind(HttpServletRequest request,BarcodeRelation dto){
    	IRequest requestCtx = createRequestContext(request);
        return service.confirmBind(requestCtx, dto);
    }
    }