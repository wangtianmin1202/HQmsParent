package com.hand.hqm.hqm_pfmea_level.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;
import com.hand.hqm.hqm_pfmea_level.service.IPfmeaLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PfmeaLevelController extends BaseController{

    @Autowired
    private IPfmeaLevelService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/pfmea/level/query")
    @ResponseBody
    public ResponseData query(PfmeaLevel dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/pfmea/level/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PfmeaLevel> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/pfmea/level/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PfmeaLevel> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 页面查询
     * @param dto 层级查询
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/pfmea/level/query/tree/data")
	@ResponseBody
	public ResponseData queryTreeData(PfmeaLevel dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryTreeData(requestContext, dto));
	}
    /**
     * 新增，保存数据
     * @param dto 层级查询
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/pfmea/level/save/or/update")
	@ResponseBody
	public ResponseData updateOrSave(PfmeaLevel dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			responseData = service.updateOrAdd(requestCtx, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.toString());
			responseData.setSuccess(false);
		}
		return responseData;
	}
    /**
     * 新增保存
     * @param dto 层级查询
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/pfmea/level/save/or/updateD")
  	@ResponseBody
  	public ResponseData updateOrSave_D(PfmeaLevel dto, HttpServletRequest request) {
  		ResponseData responseData = new ResponseData();
  		IRequest requestCtx = createRequestContext(request);
  		try {
  			responseData = service.updateOrAdd_D(requestCtx, dto);
  		} catch (Exception e) {
  			e.printStackTrace();
  			responseData.setMessage(e.toString());
  			responseData.setSuccess(false);
  		}
  		return responseData;
  	}
    /**
     * 新增保存(D)
     * @param dto 层级查询
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/pfmea/level/check")
   	@ResponseBody
   	public ResponseData publish(PfmeaLevel dto, HttpServletRequest request) {
   		ResponseData responseData=new ResponseData();
   		IRequest requestContext = createRequestContext(request);
   		try {
   		responseData = service.publish(dto,requestContext,request);
   		}
   		catch(Exception e){
   			responseData.setSuccess(false);
   			responseData.setMessage(e.getMessage());
   		}
   		return responseData;
   	}
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */  
	@RequestMapping(value = "/hqm/pfmea/level/delete/row")
	@ResponseBody
	public ResponseData deleteRow(PfmeaLevel dto, HttpServletRequest request) {
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