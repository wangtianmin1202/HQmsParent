package com.hand.hqm.hqm_use_detail.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_scrapped_detail.dto.ScrappedDetail;
import com.hand.hqm.hqm_use_detail.dto.UseDetail;
import com.hand.hqm.hqm_use_detail.service.IUseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class UseDetailController extends BaseController{

    @Autowired
    private IUseDetailService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	 
    @RequestMapping(value = "/hqm/use/detail/query")
    @ResponseBody
    public ResponseData query(UseDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/use/detail/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<UseDetail> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/use/detail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<UseDetail> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 新建明细行表
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/use/detail/addline")
   	@ResponseBody
   	public ResponseData addline(UseDetail dto, HttpServletRequest request) {
   		ResponseData responseData=new ResponseData();
   		IRequest requestContext = createRequestContext(request);
   		try {
   		responseData = service.addline(dto,requestContext,request);
   		}
   		catch(Exception e){
   			responseData.setSuccess(false);
   			responseData.setMessage(e.getMessage());
   		}
   		return responseData;
   	}
    
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/use/detail/select")
    @ResponseBody
    public ResponseData myselect(UseDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    /**
     * 样品页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/use/detail/selectforsample")
    @ResponseBody
    public ResponseData selectforsample(UseDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectforsample(requestContext,dto,page,pageSize));
    }
    /**
     * 删除行表
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/use/detail/delete/row")
   	@ResponseBody
   	public ResponseData deleteRow(UseDetail dto, HttpServletRequest request) {
   		ResponseData responseData = new ResponseData(true);
   		try {
   			service.deleteRow(dto);
   		} catch (Exception e) {
   			e.printStackTrace();
   			responseData.setMessage(e.getMessage());
   			responseData.setSuccess(false);
   		}
   		return responseData;
   	}
    }