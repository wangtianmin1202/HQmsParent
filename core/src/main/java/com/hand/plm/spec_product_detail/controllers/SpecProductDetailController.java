package com.hand.plm.spec_product_detail.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.plm.plm_product_func_attr_draft.dto.ProductFuncAttrDraft;
import com.hand.plm.spec_product_detail.dto.SpecProductDetail;
import com.hand.plm.spec_product_detail.mapper.SpecProductDetailMapper;
import com.hand.plm.spec_product_detail.service.ISpecProductDetailService;

import org.apache.ibatis.annotations.Param;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SpecProductDetailController extends BaseController{

	    @Autowired
	    private ISpecProductDetailService service;
	
		@RequestMapping(value = "/plm/spec/product/detail/query")
		@ResponseBody
		public ResponseData query(SpecProductDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
				@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
			IRequest requestContext = createRequestContext(request);
			return new ResponseData(service.select(requestContext, dto, page, pageSize));
		}
	
	    @RequestMapping(value = "/plm/spec/product/detail/queryAll")
	    @ResponseBody
	    public ResponseData queryAll(SpecProductDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.queryAll(requestContext,dto,page,pageSize));
	    }
	
	    @RequestMapping(value = "/plm/spec/product/detail/submit")
	    @ResponseBody
	    public ResponseData update(@RequestBody List<SpecProductDetail> dto, BindingResult result, HttpServletRequest request){
	        IRequest requestCtx = createRequestContext(request);
	        return new ResponseData(service.batchUpdateDetail(requestCtx, dto));
	    }
	
	    @RequestMapping(value = "/plm/spec/product/detail/remove")
	    @ResponseBody
	    public ResponseData delete(HttpServletRequest request,@RequestBody List<SpecProductDetail> dto){
	        service.batchDelete(dto);
	        return new ResponseData();
	    }
	    
	    @RequestMapping(value = "/plm/spec/product/detail/queryTreeDatas")
	    @ResponseBody
	    public ResponseData queryTreeDatas(HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.selectTreeDatas(requestContext));
	    }
	    
		@RequestMapping({ "/plm/spec/product/detail/exportCheckData" })
	    @ResponseBody
	    public ResponseData exportCheckData(SpecProductDetail condition, HttpServletRequest request, HttpServletResponse response) {
	        IRequest requestContext = createRequestContext(request);
	        service.exportData(requestContext,condition, request, response);
	        return new ResponseData();
	    }
		
		@RequestMapping(value = "/plm/spec/product/detail/updateSpecLineIdStatus")
	    @ResponseBody
	    public ResponseData updateSpecLineIdStatus(HttpServletRequest request,@RequestBody List<SpecProductDetail> dto){
			IRequest requestContext = createRequestContext(request);
	        service.updateSpecLineIdStatus(requestContext,dto);
	        return new ResponseData();
	    }
		
		@RequestMapping(value = "/plm/spec/product/detail/queryTreeChild")
	    @ResponseBody
	    public ResponseData selectTreeChild(HttpServletRequest request,@RequestBody List<SpecProductDetail> dto){
			IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.selectTreeChild(requestContext,dto));
	    }
		
		@RequestMapping(value = "/plm/spec/product/detail/queryLevelInfo")
	    @ResponseBody
	    public ResponseData selectLevelInfo(HttpServletRequest request, @RequestParam(value="levelNum", required = false) Long levelNum, @RequestParam(value="parentSpecId", required = false) Long parentSpecId){
			IRequest requestContext = createRequestContext(request);
			return new ResponseData(service.selectLevelInfo(requestContext,levelNum,parentSpecId));
	    }
		
		 @RequestMapping(value = "/plm/spec/product/detail/queryAllPendding")
		    @ResponseBody
		    public ResponseData queryAllPendding(@RequestParam(required = false) String changeType, HttpServletRequest request) {
		        IRequest requestContext = createRequestContext(request);
		        return new ResponseData(service.queryAllPendding(requestContext,changeType));
		    }
		 
		 @RequestMapping(value = "/plm/spec/product/detail/queryParentTree")
		    @ResponseBody
		    public ResponseData selectParentTree(HttpServletRequest request) {
		        IRequest requestContext = createRequestContext(request);
		        return new ResponseData(service.selectParentTree(requestContext));
		    }
		 
		 @RequestMapping(value = "/plm/spec/product/detail/hisCount")
		    @ResponseBody
		    public ResponseData hisCount(HttpServletRequest request) {
		        IRequest requestContext = createRequestContext(request);
		        ResponseData responseData  = new ResponseData();
		        responseData.setTotal(service.hisCount(requestContext));
		        return responseData;
		    }
		 
		 @RequestMapping(value = "/plm/spec/product/detail/changeSubmit")
		    @ResponseBody
		    public ResponseData updateChanges(@RequestBody List<SpecProductDetail> dto, BindingResult result, HttpServletRequest request){
		        IRequest requestCtx = createRequestContext(request);
		        service.batchUpdateSpecChange(requestCtx, dto);
		        return new ResponseData();
		    }
    }
    