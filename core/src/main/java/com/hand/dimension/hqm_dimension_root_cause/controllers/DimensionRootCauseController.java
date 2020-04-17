package com.hand.dimension.hqm_dimension_root_cause.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_problem_description.dto.DimensionProblemDescription;
import com.hand.dimension.hqm_dimension_root_cause.dto.DimensionRootCause;
import com.hand.dimension.hqm_dimension_root_cause.service.IDimensionRootCauseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DimensionRootCauseController extends BaseController{

    @Autowired
    private IDimensionRootCauseService service;


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
    @RequestMapping(value = "/hqm/8d/root/cause/query")
    @ResponseBody
    public ResponseData query(DimensionRootCause dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    
    /**
     * 
     * @description 保存单个数据入口
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/root/cause/saveone")
    @ResponseBody
    public ResponseData saveOne(DimensionRootCause dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        try {
			return new ResponseData(service.saveOne(requestContext,dto));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ResponseData responseData = new ResponseData(false);
	        responseData.setMessage(e.getMessage());
	        return responseData;
		}
    }
    
    /**
     * 
     * @description 保存修改数据
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/root/cause/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DimensionRootCause> dto, BindingResult result, HttpServletRequest request){
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
     * @description 删除
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/root/cause/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DimensionRootCause> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 
     * @description 提交步骤
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/root/cause/commit")
	@ResponseBody
	public ResponseData commit(DimensionRootCause dto, HttpServletRequest request) {
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
    }