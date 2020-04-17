package com.hand.dimension.hqm_dimension_team_assembled.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;
import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;
import com.hand.dimension.hqm_dimension_team_assembled.service.IDimensionTeamAssembledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;
import java.util.stream.Collectors;

    @Controller
    public class DimensionTeamAssembledController extends BaseController{

    @Autowired
    private IDimensionTeamAssembledService service;


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
    @RequestMapping(value = "/hqm/8d/team/assembled/query")
    @ResponseBody
    public ResponseData query(DimensionTeamAssembled dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
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
    @RequestMapping(value = "/hqm/8d/team/assembled/select")
    @ResponseBody
    public ResponseData select(DimensionTeamAssembled dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    /**
     * 
     * @description 多选界面数据查询
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/team/assembled/lovselect")
    @ResponseBody
    public ResponseData selectLov(DimensionTeamAssembled dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectMulLov(requestContext,dto,page,pageSize));
    }
    /**
     * 
     * @description 保存数据
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/team/assembled/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DimensionTeamAssembled> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        try {
        	ResponseData responseData = new ResponseData();
			responseData.setRows(service.batchUpdateRe(requestCtx, dto));
			return responseData;
		}catch(DataRetrievalFailureException e) {
			ResponseData responseData = new ResponseData(false);
	        responseData.setMessage("违反唯一性");
	        return responseData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ResponseData responseData = new ResponseData(false);
			if(e.getMessage().contains("nested")) {
				responseData.setMessage("人员不能相同");
			}else {
				responseData.setMessage(e.getMessage());
			}
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
    @RequestMapping(value = "/hqm/8d/team/assembled/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DimensionTeamAssembled> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 
     * @description 提交步骤
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/team/assembled/commit")
    @ResponseBody
    public ResponseData commit(HttpServletRequest request,@RequestBody List<DimensionTeamAssembled> dto){
    	IRequest requestCtx = createRequestContext(request);
    	ResponseData re = new ResponseData();
    	try {
    		re = service.commit(requestCtx,dto);
    	}catch(Exception e) {
    		re.setSuccess(false);
    		re.setMessage(e.getMessage());
    	}
        return re;
    }
    
    /**
     * 
     * @description 保存成员
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/team/assembled/saveMenber")
   	@ResponseBody
   	public ResponseData saveMenber(DimensionTeamAssembled dto, HttpServletRequest request) {
   		ResponseData responseData=new ResponseData();
   		IRequest requestContext = createRequestContext(request);
   		try {
   		responseData = service.saveMenber(dto,requestContext,request);
   		}
   		catch(Exception e){
   			responseData.setSuccess(false);
   			responseData.setMessage(e.getMessage());
   		}
   		return responseData;
   	}
    }