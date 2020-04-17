package com.hand.hqm.hqm_msa_plan.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class MsaPlanController extends BaseController{

    @Autowired
    private IMsaPlanService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/msa/plan/query")
    @ResponseBody
    public ResponseData query(MsaPlan dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/msa/plan/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MsaPlan> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.update(requestCtx, dto));
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/msa/plan/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MsaPlan> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * msa计划制定 取消
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/msa/plan/cancel")
    @ResponseBody
    public ResponseData cancel(HttpServletRequest request,@RequestBody List<MsaPlan> dto){
    	IRequest requestCtx = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
    		service.cancel(requestCtx,dto);
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
        return responseData;
    }
    /**
     * msa计划执行 完成
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/msa/plan/complete")
    @ResponseBody
    public ResponseData complete(HttpServletRequest request,@RequestBody List<MsaPlan> dto){
    	IRequest requestCtx = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
    		service.complete(requestCtx,dto);
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setMessage(e.getMessage());
    		responseData.setSuccess(false);
    	}
        return responseData;
    }
    }