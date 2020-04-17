package com.hand.hcs.hcs_delivery_ticket.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DeliveryTicketHController extends BaseController{

    @Autowired
    private IDeliveryTicketHService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/query")
    @ResponseBody
    public ResponseData query(DeliveryTicketH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hcs/delivery/ticket/h/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DeliveryTicketH> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hcs/delivery/ticket/h/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DeliveryTicketH> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 生产供应商当天单号
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/queryMaxNum")
    @ResponseBody
    public String queryMaxNum(HttpServletRequest request,DeliveryTicketH dto){
    	IRequest requestContext = createRequestContext(request);
        return service.queryOrderNum(requestContext, dto);
    }
    /**
     * 送货单编辑主界面查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/queryData")
    @ResponseBody
    public ResponseData queryData(DeliveryTicketH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryData(requestContext,dto,page,pageSize));
    }
    /**
     * 发货
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/delivery")
    @ResponseBody
    public ResponseData delivery(HttpServletRequest request,DeliveryTicketH dto){
    	IRequest requestContext = createRequestContext(request);
        return service.delivery(requestContext, dto);
    }
    /**
     * 取消送货单
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/cancel")
    @ResponseBody
    public ResponseData cancel(HttpServletRequest request,DeliveryTicketH dto){
    	IRequest requestContext = createRequestContext(request);
        return service.cancel(requestContext, dto);
    }
    /**
     * 送货单打印查询
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/printQuery")
    @ResponseBody
    public ResponseData printQuery(HttpServletRequest request,DeliveryTicketH dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData(service.printQuery(requestContext, dto));
        return responseData;
    }
    /**
     * 送货单头查询
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/queryByTicketId")
    @ResponseBody
    public ResponseData queryByTicketId(HttpServletRequest request,DeliveryTicketH dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData(service.queryByTicketId(requestContext, dto));
        return responseData;
    }
    /**
     * 更新打印次数
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/updatePrintTime")
    @ResponseBody
    public ResponseData updatePrintTime(HttpServletRequest request,DeliveryTicketH dto){
    	IRequest requestContext = createRequestContext(request);
        return service.updatePrintTime(requestContext, dto);
    }
    /**
     * 打印校验
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/delivery/ticket/h/checkPrint")
    @ResponseBody
    public ResponseData checkPrint(HttpServletRequest request,DeliveryTicketH dto){
    	IRequest requestContext = createRequestContext(request);
        return service.checkPrint(requestContext, dto);
    }
    }