package com.hand.plm.plm_prod_design_standard_draft.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.plm_prod_design_standard_draft.dto.ProdDesignStandardDraft;
import com.hand.plm.plm_prod_design_standard_draft.service.IProdDesignStandardDraftService;
import com.hand.plm.plm_prod_design_standard_draft.view.ProdDesignStandardDraftVO;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ProdDesignStandardDraftController extends BaseController {

	@Autowired
	private IProdDesignStandardDraftService service;

	@RequestMapping(value = "/plm/prod/design/standard/draft/query")
	@ResponseBody
	public ResponseData query(ProdDesignStandardDraftVO dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryNew(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/plm/prod/design/standard/draft/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ProdDesignStandardDraftVO> vo, BindingResult result,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		List<ProdDesignStandardDraft> dtoList = new ArrayList<>();

		for (ProdDesignStandardDraftVO draft : vo) {
			if (StringUtils.isNotEmpty(draft.getDetailId()) && "add".equals(draft.get__status())) {
				String kid = service.checkDetailDataIsExist(requestCtx, draft.getDetailId());
				if (StringUtils.isNotEmpty(kid)) {
					draft.setKid(kid);
					draft.set__status("update");
				}
			}
			ProdDesignStandardDraft dto = new ProdDesignStandardDraft();
			BeanUtils.copyProperties(draft, dto);
			if("add".equals(dto.get__status())) {
				dto.setCreatedBy(requestCtx.getUserId());
				dto.setCreationDate(new Date());
			}
			
			dtoList.add(dto);
		}

		getValidator().validate(dtoList, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}

		return new ResponseData(service.batchUpdate(requestCtx, dtoList));
	}

	@RequestMapping(value = "/plm/prod/design/standard/draft/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ProdDesignStandardDraft> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/plm/prod/design/standard/draft/update/query")
	@ResponseBody
	public ResponseData updateQuery(ProdDesignStandardDraftVO dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.updateQuery(requestContext, dto));
	}

}