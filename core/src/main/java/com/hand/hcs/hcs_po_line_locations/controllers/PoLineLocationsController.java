package com.hand.hcs.hcs_po_line_locations.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_po_line_locations.dto.PoLineLocations;
import com.hand.hcs.hcs_po_line_locations.service.IPoLineLocationsService;
import com.hand.hcs.hcs_po_lines.dto.Receive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PoLineLocationsController extends BaseController{

    @Autowired
    private IPoLineLocationsService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/po/line/locations/query")
    @ResponseBody
    public ResponseData query(PoLineLocations dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryPoLineLocations(requestContext,dto,page,pageSize));
    }
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/po/line/locations/queryPoLineDetail")
    @ResponseBody
    public ResponseData queryPoLineDetail(PoLineLocations dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryPoLineDetail(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hcs/po/line/locations/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PoLineLocations> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hcs/po/line/locations/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PoLineLocations> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 保存
     * @param dto 数据集
     * @param request 请求
     * @return 数据返回对象
     */
    @RequestMapping(value = "/hcs/po/lines/locations/saveInfo")
    @ResponseBody
    public ResponseData saveInfo(@RequestBody List<PoLineLocations> dto, HttpServletRequest request){
       
        IRequest requestCtx = createRequestContext(request);
        service.saveInfo(requestCtx, dto);
        ResponseData responseData = new ResponseData();
        responseData.setMessage("成功");
        return responseData;
    }
    /**
     * 发运计划查询 关闭
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/po/line/locations/close")
    @ResponseBody
    public ResponseData close(HttpServletRequest request,@RequestBody List<PoLineLocations> dto){
    	IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.close(requestCtx,dto));
    }
    /**
     *  采购订单发运明细查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
     @RequestMapping(value = "/hcs/po/line/locations/queryLocationDetail")
     @ResponseBody
     public ResponseData queryLocationDetail(PoLineLocations dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
         IRequest requestContext = createRequestContext(request);
         return new ResponseData(service.queryLocationDetail(requestContext,dto,page,pageSize));
     }
    }