package com.hand.dimension.hqm_dimension_improving_actions.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;
import com.hand.dimension.hqm_dimension_improving_actions.service.IDimensionImprovingActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DimensionImprovingActionsController extends BaseController{

    @Autowired
    private IDimensionImprovingActionsService service;


    /**
     * 
     * @description 默认基础查询
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/query")
    @ResponseBody
    public ResponseData query(DimensionImprovingActions dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }
    /**
     * 
     * @description 默认基础查询
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/queryDelete")
    @ResponseBody
    public ResponseData queryDelete(DimensionImprovingActions dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.reSelectDelete(requestContext,dto,page,pageSize));
    }
    
    /**
     * 
     * @description 当前步骤的提交操作
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/commit")
    @ResponseBody
    public ResponseData commit(DimensionImprovingActions dto, HttpServletRequest request) {
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
     * @description 基础查询
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/select")
    @ResponseBody
    public ResponseData select(DimensionImprovingActions dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = "100") int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }
    
    /**
     * 
     * @description 数据更新
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DimensionImprovingActions> dto, BindingResult result, HttpServletRequest request){
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
     * @description 删除
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DimensionImprovingActions> dto){
        service.batchUpdateById(dto);
        return new ResponseData();
    }
    }