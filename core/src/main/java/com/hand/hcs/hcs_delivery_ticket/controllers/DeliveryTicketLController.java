package com.hand.hcs.hcs_delivery_ticket.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketL;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class DeliveryTicketLController extends BaseController {

	@Autowired
	private IDeliveryTicketLService service;

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/query")
	@ResponseBody
	public ResponseData query(DeliveryTicketL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.query(requestContext, dto, page, pageSize));
	}

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<DeliveryTicketL> dto, BindingResult result,
			HttpServletRequest request) {
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
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<DeliveryTicketL> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 送货单编辑
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/saveHeadLine")
	@ResponseBody
	public ResponseData saveHeadLine(HttpServletRequest request, @RequestBody List<DeliveryTicketL> dto) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData = service.saveHeadLine(requestContext, dto);
			// responseData.setMessage("保存送货单成功，单号为：" + dto.get(0).getTicketNumber());
		} catch (RuntimeException e) {
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
			return responseData;
		} catch (Exception e) {
			throw e;
		}

		return responseData;
	}

	/**
	 * 送货单头查询行信息
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/queryByHeadId")
	@ResponseBody
	public ResponseData queryByHeadId(HttpServletRequest request, DeliveryTicketL dto) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			List<DeliveryTicketL> lineList = service.queryByHeadId(requestContext, dto);
			responseData.setRows(lineList);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}

		return responseData;
	}

	/**
	 * 取消送货单行
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/cancelLine")
	@ResponseBody
	public ResponseData cancelLine(HttpServletRequest request, @RequestBody List<DeliveryTicketL> dto) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData = service.cancelLine(requestContext, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}

		return responseData;
	}

	/**
	 * 送货单打印行查询
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/printQueryLine")
	@ResponseBody
	public ResponseData printQueryLine(HttpServletRequest request, DeliveryTicketL dto) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			List<DeliveryTicketL> lineList = service.printQueryLine(requestContext, dto);
			responseData.setRows(lineList);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}

		return responseData;
	}

	/**
	 * 采购订单-按明细查询-查询已接收数量
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/queryReceiveQty")
	@ResponseBody
	public Long queryReceiveQty(HttpServletRequest request, DeliveryTicketL dto) {
		IRequest requestContext = createRequestContext(request);
		return service.queryReceiveQty(requestContext, dto);
	}

	/**
	 * 送货单查询-明细
	 * 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/queryHeadLine")
	@ResponseBody
	public ResponseData queryHeadLine(DeliveryTicketL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryHeadLine(requestContext, dto, page, pageSize));
	}

	/**
	 * 采购订单发运明细 关闭按钮校验查询
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/queryLocationCount")
	@ResponseBody
	public ResponseData queryLocationCount(HttpServletRequest request, DeliveryTicketL dto) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData = service.queryLocationCount(requestContext, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}

		return responseData;
	}

	/**
	 * 日期格式化
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 供货执行情况查询
	 * 
	 * @param dto      查询条件
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hcs/delivery/ticket/l/planReport")
	@ResponseBody
	public ResponseData planReport(DeliveryTicketL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData.setRows(service.planReport(requestContext, dto));
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}
}