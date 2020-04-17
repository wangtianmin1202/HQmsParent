package com.hand.hqm.hqm_control_plan.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeaturesTree;
import com.hand.hqm.hqm_control_plan.service.IControlPlanFeaturesTreeService;
import com.hand.hqm.hqm_fmea.dto.Fmea;

@Controller
public class ControlPlanFeaturesTreeController extends BaseController {
	
	@Autowired
	private IControlPlanFeaturesTreeService service;

	/**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:39 2019/8/19
	 * @Param [dto, page, pageSize, request]
	 */
	@RequestMapping(value = "/hdm/features/query/tree/data")
	@ResponseBody
	public ResponseData queryTreeData(ControlPlanFeaturesTree dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryTreeData(requestContext, dto));
	}

	/**
	 * @Author han.zhang
	 * @Description 附着对象更新、新增
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@RequestMapping(value = "/hdm/features/save/or/update")
	@ResponseBody
	public ResponseData updateOrSave(ControlPlanFeaturesTree dto, HttpServletRequest request) {
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
	 * @Author han.zhang
	 * @Description 根据主键删除附着对象
	 * @Date 10:51 2019/8/20
	 * @Param [dto, request]
	 */
	@RequestMapping(value = "/hdm/features/delete/row")
	@ResponseBody
	public ResponseData deleteRow(ControlPlanFeaturesTree dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData(true);
		try {
			IRequest requestCtx = createRequestContext(request);
			service.deleteRow(requestCtx,dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}
	/**
	 * @Author magicor
	 * @Description 根据主键还原数据
	 * @Date 10:51 2019/9/12
	 * @Param [dto, request]
	 */
	@RequestMapping(value = "/hdm/features/restore/row")
	@ResponseBody
	public ResponseData restoreRow(ControlPlanFeaturesTree dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData(true);
		try {
			IRequest requestCtx = createRequestContext(request);
			service.restoreRow(requestCtx,dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}
	@RequestMapping(value = "/hdm/features/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ControlPlanFeaturesTree> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        responseData.setRows(service.changeDatas(requestCtx, dto));
        responseData.setMessage("操作成功");
        return responseData;
    }
	@RequestMapping(value = "/hdm/features/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ControlPlanFeaturesTree> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
}