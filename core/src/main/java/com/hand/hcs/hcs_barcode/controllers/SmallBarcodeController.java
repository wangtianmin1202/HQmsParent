package com.hand.hcs.hcs_barcode.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_barcode.service.ISmallBarcodeService;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class SmallBarcodeController extends BaseController {

	@Autowired
	private ISmallBarcodeService service;

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hcs/small/barcode/query")
	@ResponseBody
	public ResponseData query(SmallBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.query(requestContext, dto, page, pageSize));// select(requestContext,dto,page,pageSize)
	}

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hcs/small/barcode/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<SmallBarcode> dto, BindingResult result, HttpServletRequest request) {
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
	 * 
	 * @param request 请求
	 * @param dto     删除数据
	 * @return
	 */
	@RequestMapping(value = "/hcs/small/barcode/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<SmallBarcode> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 保存小箱打印数据
	 */
	@RequestMapping(value = "/hcs/small/barcode/savesmall")
	@ResponseBody
	public ResponseData addNewInspection(SmallBarcode dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		service.insertsmall(dto, requestContext, request);
		return new ResponseData();
	}

	/**
	 * 失效
	 * 
	 * @param request 请求
	 * @param dto     失效数据
	 * @return
	 */
	@RequestMapping(value = "/hcs/small/barcode/changeFlag")
	@ResponseBody
	public ResponseData changeFlag(HttpServletRequest request, @RequestBody List<SmallBarcode> dto) {
		IRequest requestContext = createRequestContext(request);
		service.changeFlag(requestContext, dto);
		return new ResponseData();
	}

	/**
	 * 关联小箱条码
	 * 
	 * @param dto      查询条件
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 查询结果集
	 */
	@RequestMapping(value = "/hcs/small/barcode/querySmallBarcode")
	@ResponseBody
	public ResponseData querySmallBarcode(SmallBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.querySmallBarcode(requestContext, dto, page, pageSize));
	}

	/**
	 * 关联小箱条码 完成按钮
	 * 
	 * @param request 请求
	 * @param dto     小箱条码集合
	 * @return 返回结果
	 */
	@RequestMapping(value = "/hcs/small/barcode/saveInfo")
	@ResponseBody
	public ResponseData saveInfo(HttpServletRequest request, @RequestBody List<SmallBarcode> dto) {
		IRequest requestContext = createRequestContext(request);
		return service.saveInfo(requestContext, dto);
	}

	/**
	 * 更新打印次数
	 * 
	 * @param request 请求
	 * @param dto     打印对象集合
	 * @return
	 */
	@RequestMapping(value = "/hcs/small/barcode/updatePrintTime")
	@ResponseBody
	public ResponseData updatePrintTime(HttpServletRequest request, @RequestBody List<SmallBarcode> dto) {
		ResponseData responseData = new ResponseData();
		IRequest requestContext = createRequestContext(request);
		service.updatePrintTime(requestContext, dto);
		return responseData;
	}

	/**
	 * 获取最大序列号
	 * 
	 * @param request 请求
	 * @param dto     查询条件
	 * @return 最大序列号
	 */
	@RequestMapping(value = "/hcs/small/barcode/queryMaxNum")
	@ResponseBody
	public String queryMaxNum(HttpServletRequest request, SmallBarcode dto) {
		IRequest requestContext = createRequestContext(request);
		return service.getsbarcode(requestContext, dto);
	}

	/**
	 * 解除绑定
	 * 
	 * @param request 请求
	 * @param dto     解绑数据
	 * @return
	 */
	@RequestMapping(value = "/hcs/small/barcode/unBind")
	@ResponseBody
	public ResponseData unBind(HttpServletRequest request, @RequestBody List<SmallBarcode> dto) {
		ResponseData responseData = new ResponseData();
		IRequest requestContext = createRequestContext(request);
		try {
			service.unBind(requestContext, dto);
		} catch (RuntimeException e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
			return responseData;
		} catch (Exception e) {
			throw e;
		}
		return responseData;
	}

	/**
	 * 确认绑定
	 * 
	 * @param request 请求
	 * @param dto     绑定信息
	 * @return
	 */
	@RequestMapping(value = "/hcs/small/barcode/confirmBind")
	@ResponseBody
	public ResponseData confirmBind(HttpServletRequest request, @RequestBody List<SmallBarcode> dto) {
		ResponseData responseData = new ResponseData();
		IRequest requestContext = createRequestContext(request);
		service.confirmBind(requestContext, dto);
		return responseData;
	}

	/**
	 * 日期格式化
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}