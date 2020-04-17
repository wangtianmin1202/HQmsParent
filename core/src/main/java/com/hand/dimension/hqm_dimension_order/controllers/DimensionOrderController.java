package com.hand.dimension.hqm_dimension_order.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.custom.annotation.LogCustom;
import com.hand.dimension.hqm_dimension_order.dto.DimensionOrder;
import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
	/**
	 * 
	 * @author tianmin.wang
	 * 所有8D相关
	 */
    @Controller
    public class DimensionOrderController extends BaseController{

    @Autowired
    private IDimensionOrderService service;


    /**
     * 
     * @description 基础查询入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/query")
    @ResponseBody
    public ResponseData query(DimensionOrder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 
     * @description 基础查询入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/select")
    @ResponseBody
    public ResponseData select(DimensionOrder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reselect(requestContext,dto,page,pageSize));
    }
    /**
     * 
     * @description 数据更新保存提交
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DimensionOrder> dto, BindingResult result, HttpServletRequest request){
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
     * 
     * @description 关闭操作入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/close")
    @ResponseBody
    public ResponseData close(HttpServletRequest request,@RequestBody List<DimensionOrder> dto){
    	ResponseData responseData = new ResponseData();
    	IRequest requestCtx = createRequestContext(request);
    	try {
    		service.batchClose(request,requestCtx,dto);
        }catch(Exception e) {
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    
    /**
     * 
     * @description 提交入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/commit")
    @ResponseBody
    public ResponseData commit(HttpServletRequest request,DimensionOrder dto){
    	ResponseData responseData = new ResponseData();
    	IRequest requestCtx = createRequestContext(request);
    	try {
    		service.commit(request,requestCtx,dto);
        }catch(Exception e) {
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    
    /**
     * 
     * @description 单个数据的保存入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/saveone")
    @ResponseBody
    public ResponseData saveOne(HttpServletRequest request,DimensionOrder dto){
    	ResponseData responseData = new ResponseData();
    	IRequest requestCtx = createRequestContext(request);
    	try {
    		service.saveOne(request,requestCtx,dto);
        }catch(Exception e) {
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    
    /**
     * 
     * @description 删除入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DimensionOrder> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 新建界面的保存
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/addnewsave")
    @ResponseBody
    public ResponseData addNewSave(DimensionOrder dto,HttpServletRequest request){
    	ResponseData responseData = new ResponseData();
    	IRequest requestCtx = createRequestContext(request);
    	try {
    		List<DimensionOrder> li = new ArrayList<>();
    		li.add(service.addNew(requestCtx,dto));
    		responseData.setRows(li);
    	}catch(Exception e) {
    		responseData.setSuccess(false);
    		responseData.setMessage(e.getMessage());
    		return responseData;
    	}
        return responseData;
    }
    /**
     * F|O QC发起8D入口
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/fosponsor")
    @ResponseBody
    public ResponseData foSponsor(FqcInspectionH dto,HttpServletRequest request){
    	ResponseData responseData = new ResponseData();
    	IRequest requestCtx = createRequestContext(request);
    	try {
			responseData = service.foSponsor(request,requestCtx,dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
        return responseData;
    }
    /**
     * IQC发起8D入口
     * @param request
     * @param dto
     * @return
     */
    //@LogCustom
    @RequestMapping(value = "/hqm/8d/order/isponsor")
    @ResponseBody
    public ResponseData iSponsor(IqcInspectionH dto,HttpServletRequest request){
    	ResponseData responseData = new ResponseData();
    	IRequest requestCtx = createRequestContext(request);
    	try {
			responseData = service.iSponsor(request,requestCtx,dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
        return responseData;
    }
    /**
     * PQC发起8D入口
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/order/psponsor")
    @ResponseBody
    public ResponseData pSponsor(PqcInspectionH dto,HttpServletRequest request){
    	ResponseData responseData = new ResponseData();
    	IRequest requestCtx = createRequestContext(request);
    	try {
			responseData = service.pSponsor(request,requestCtx,dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
        return responseData;
    }
    }