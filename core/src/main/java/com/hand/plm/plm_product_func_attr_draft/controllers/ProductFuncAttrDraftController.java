package com.hand.plm.plm_product_func_attr_draft.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.google.common.base.Throwables;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.plm_product_func_attr_draft.dto.ProductFuncAttrDraft;
import com.hand.plm.plm_product_func_attr_draft.service.IProductFuncAttrDraftService;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ProductFuncAttrDraftController extends BaseController {

	@Autowired
	private IProductFuncAttrDraftService service;

	@RequestMapping(value = "/plm/product/func/attr/draft/query")
	@ResponseBody
	public ResponseData query(ProductFuncAttrDraft dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		dto.setCreatedBy(requestContext.getUserId());
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/plm/product/func/attr/draft/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ProductFuncAttrDraft> dto, BindingResult result,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			getValidator().validate(dto, result);
			if (result.hasErrors()) {
				responseData.setSuccess(false);
				responseData.setMessage(getErrorMessage(result, request));
				return responseData;
			}
			for (ProductFuncAttrDraft draft : dto) {
				// 校验产品和功能属性的目录层级关系
				if (!service.checkTreeLevelIsExist(requestCtx, draft.getProduct(), draft.getProductFunc())) {
					responseData.setSuccess(false);
					responseData.setMessage("【" + draft.getProductFunc() + "】不在【" + draft.getProduct() + "】结构目录下，请检查！");
					return responseData;
				}
				// 校验当新增数据是否已经在草稿表里，且状态为DRAFT，如果有，则更新，不新增
				if (StringUtils.isNotEmpty(draft.getDetailId()) && "add".equals(draft.get__status())) {
					String kid = service.checkDetailDataIsExist(requestCtx, draft.getDetailId());
					if (StringUtils.isNotEmpty(kid)) {
						draft.setKid(kid);
						draft.set__status("update");
					}
				}
			}

			List<ProductFuncAttrDraft> resultList = service.batchUpdate(requestCtx, dto);
			responseData.setRows(resultList);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
		} catch (RuntimeException ex) {
			responseData.setSuccess(false);
			responseData.setMessage(ex.getMessage());
		}
		return responseData;
	}

	@RequestMapping(value = "/plm/product/func/attr/draft/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ProductFuncAttrDraft> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 草稿表数据提交审批
	 * 
	 * @param kidList 草稿表KID集合
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/plm/product/func/attr/draft/submit/approve")
	@ResponseBody
	public ResponseData submitApprove(@RequestBody List<String> kidList, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData.setSuccess(true);
			responseData.setMessage("成功");
			responseData.setRows(service.submitApprove(requestContext, kidList));
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage(Throwables.getRootCause(ex).getMessage());
		}
		return responseData;
	}
}