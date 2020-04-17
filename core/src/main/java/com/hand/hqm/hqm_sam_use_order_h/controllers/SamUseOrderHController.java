package com.hand.hqm.hqm_sam_use_order_h.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;
import com.hand.hqm.hqm_sam_use_order_h.service.ISamUseOrderHService;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;
import com.hand.hqm.hqm_sample.dto.Sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SamUseOrderHController extends BaseController{

    @Autowired
    private ISamUseOrderHService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sam/use/order/h/query")
    @ResponseBody
    public ResponseData query(SamUseOrderH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/sam/use/order/h/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SamUseOrderH> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/sam/use/order/h/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SamUseOrderH> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 行表数据查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sam/use/order/h/select")
    @ResponseBody
    public ResponseData select(SamUseOrderH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    /**
     * 根据主键查询头表数据
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
     @RequestMapping(value = "/hqm/sam/use/order/h/queryByHeadKid")
     @ResponseBody
     public ResponseData queryByTicketId(HttpServletRequest request,SamUseOrderH dto){
     	IRequest requestContext = createRequestContext(request);
     	ResponseData responseData = new ResponseData(service.queryByTicketId(requestContext, dto));
         return responseData;
     }
     /**
      * 保存头表数据
      * @param dto 查询内容
      * @param request 请求
      * @return 结果集
      */
     @RequestMapping(value = "/hqm/sam/use/order/h/saveHead")
     @ResponseBody
     public ResponseData saveLine(HttpServletRequest request,@RequestBody List<SamUseOrderH> dto){
     	IRequest requestContext = createRequestContext(request);
     	ResponseData responseData = new ResponseData();
     	try {
     		service.saveHead(requestContext, dto);
     		responseData.setMessage("操作成功");
     	}catch(Exception e) {
     		e.printStackTrace();
     		responseData.setMessage(e.getMessage());
     		responseData.setSuccess(false);
     	}
     	
         return responseData;
     }
    }