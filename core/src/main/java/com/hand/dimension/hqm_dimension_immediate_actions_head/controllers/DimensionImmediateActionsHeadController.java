package com.hand.dimension.hqm_dimension_immediate_actions_head.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_immediate_actions_head.dto.DimensionImmediateActionsHead;
import com.hand.dimension.hqm_dimension_immediate_actions_head.service.IDimensionImmediateActionsHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DimensionImmediateActionsHeadController extends BaseController{

    @Autowired
    private IDimensionImmediateActionsHeadService service;


    /**
     *  
     * @description 基础查询
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto 查询条件实体
     * @param page 页数
     * @param pageSize 页大小
     * @param request 请求对象
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/8d/immediate/actions/head/query")
    @ResponseBody
    public ResponseData query(DimensionImmediateActionsHead dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }
    /**
     *  
     * @description 删除历史记录查询
     * @author magicor
     * @date 2019年11月21日 
     * @param dto 查询条件实体
     * @param page 页数
     * @param pageSize 页大小
     * @param request 请求对象
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/8d/immediate/actions/head/queryDelete")
    @ResponseBody
    public ResponseData queryDelete(DimensionImmediateActionsHead dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.reSelectDelete(requestContext,dto,page,pageSize));
    }
    /**
     * 
     * @description 请求入口 保存单个数据
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/immediate/actions/head/saveone")
    @ResponseBody
    public ResponseData saveOne(DimensionImmediateActionsHead dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.saveOne(requestContext,dto));
    }
    
    /**
     * 
     * @description 提交临时措施
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/immediate/actions/head/commit")
    @ResponseBody
    public ResponseData commit(DimensionImmediateActionsHead dto, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
		responseData = service.commit(requestContext, dto);
		}catch(Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
    }
    
    /**
     * 
     * @description 删除单个数据
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/immediate/actions/head/deleteone")
    @ResponseBody
    public ResponseData deleteOne(DimensionImmediateActionsHead dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.deleteOne(requestContext,dto));
    }
    
    /**
     * 
     * @description update
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param result
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hqm/8d/immediate/actions/head/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DimensionImmediateActionsHead> dto, BindingResult result, HttpServletRequest request) throws Exception{
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.reBatchUpdate(requestCtx, dto));
    }
    
    /**
     * 
     * @description 有条件的updte入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/immediate/actions/head/resubmit")
    @ResponseBody
    public ResponseData reUpdate(@RequestBody List<DimensionImmediateActionsHead> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        try {
			return new ResponseData(service.reBatchUpdate(requestCtx, dto));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ResponseData responseData = new ResponseData(false);
	        responseData.setMessage(e.getMessage());
	        return responseData;
		}
    }
    /**
     * 
     * @description 数据删除
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/immediate/actions/head/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DimensionImmediateActionsHead> dto){
        service.batchUpdateById(dto);
        return new ResponseData();
    }
    }