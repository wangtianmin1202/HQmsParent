package com.hand.npi.npi_technology.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.service.ITechnologySpecService;
import com.hand.wfl.util.DropDownListDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class TechnologySpecController extends BaseController{

    @Autowired
    private ITechnologySpecService service;


    @RequestMapping(value = "/npi/technology/spec/query")
    @ResponseBody
    public ResponseData query(TechnologySpec dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/npi/technology/spec/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<TechnologySpec> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.submitData(requestCtx, dto));
    }
    
    /**
	 * @description 修改测试动作后执行保存
	 * @author likai 2020.03.20
	 * @param request
	 * @param list
	 * @return
	 */
    @RequestMapping(value = "/npi/technology/spec/submitTest")
    @ResponseBody
    public ResponseData updateTest(@RequestBody List<TechnologySpec> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.submitTestData(requestCtx, dto));
    }

    @RequestMapping(value = "/npi/technology/spec/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<TechnologySpec> dto){
        service.delData(dto);
        return new ResponseData();
    }
    
    
    @RequestMapping(value = "/npi/technology/spec/add")
    @ResponseBody
    public ResponseData add(@RequestBody TechnologySpec dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.addNewTechnologySpec(dto, requestCtx, request);
    }
    
    //标准动作下拉列表
    @RequestMapping(value = "/npi/technology/queryStandardActionName")
    @ResponseBody
    public List<DropDownListDto> queryStandardActionName(@RequestParam(required = false) String standardActionName){
    	return service.queryStandardActionName(standardActionName);
    }
    
    /**
     * @author likai 2020.03.20
     * 测试动作下拉列表
     * @param standardActionName
     * @return
     */
    @RequestMapping(value = "/npi/technology/queryTestActionName")
    @ResponseBody
    public List<DropDownListDto> queryTestActionName(@RequestParam(required = false) String standardActionName){
    	return service.queryTestActionName(standardActionName);
    }
    
    //辅助动作下拉列表
    @RequestMapping(value = "/npi/technology/queryAuxiliaryActionName")
    @ResponseBody
    public List<DropDownListDto> queryauxiliaryActionName(@RequestParam(required = false) String standardActionName){
    	return service.queryAuxiliaryActionName(standardActionName);
    }
    
    @RequestMapping(value = "/npi/technology/specList/query")
    @ResponseBody
    public ResponseData querySpecList(TechnologySpec dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.querySpecData(dto, page, pageSize, requestContext, request));
    }
    @RequestMapping(value = "/npi/technology/specList/info")
    @ResponseBody
    public ResponseData querySpecListInfo(TechnologySpec dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.querySpecInfo(dto, page, pageSize, requestContext, request));
    }
    
    @RequestMapping(value = "/npi/technology/queryMaterielName")
    @ResponseBody
    public List<DropDownListDto> queryMaterielName(@RequestParam(required = false) String materielName){
    	return service.queryMaterielName(materielName);
    }
    
    @RequestMapping(value = "/npi/technology/queryCharacteristicName")
    @ResponseBody
    public List<DropDownListDto> queryCharacteristicName(@RequestParam(required = false) String characteristicName){
    	return service.queryCharacteristicName(characteristicName);
    }
    
    //2020年2月27日14:12:14
    //保存标准动作的头行信息
    @RequestMapping(value = "/npi/technology/spec/addData")
    @ResponseBody
    public ResponseData addData(@RequestBody List<TechnologySpec> dtos, BindingResult result, HttpServletRequest request){
        getValidator().validate(dtos, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.addData(dtos, requestCtx, request);
    }
    
    /**
	 * @description 保存测试动作要求
	 * @author likai 2020.03.20
	 * @param dtos
	 * @param requestCtx
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "/npi/technology/spec/addTestData")
    @ResponseBody
    public ResponseData addTestData(@RequestBody List<TechnologySpec> dtos, BindingResult result, HttpServletRequest request){
        getValidator().validate(dtos, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.addTestData(dtos, requestCtx, request);
    }
    
    @RequestMapping(value = "/npi/technology/specList/queryHisData")
    @ResponseBody
    public ResponseData queryHisData(TechnologySpec dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryHisData(dto, page, pageSize, requestContext, request));
    }
    
    /**
	 * @description 执行审批，启动流程
	 * @author likai 2020.03.21
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
    @RequestMapping(value = "/npi/technology/spec/audit")
	@ResponseBody
    public ResponseData audit(@RequestBody List<TechnologySpec> dtos, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.audit(dtos, requestCtx);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
    
    /**
	 * @description 删除数据
	 * @author likai 2020.04.01
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
    @RequestMapping(value = "/npi/technology/spec/deleteData")
	@ResponseBody
    public ResponseData deleteData(@RequestBody List<TechnologySpec> dtos, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.deleteData(dtos, requestCtx);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
    
    }