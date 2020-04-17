package com.hand.hqm.hqm_fqc_inspection_template_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hqm.hqm_fqc_inspection_template_l.dto.FqcInspectionTemplateL;
import com.hand.hqm.hqm_fqc_inspection_template_l.service.IFqcInspectionTemplateLService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class FqcInspectionTemplateLController extends BaseController{

    	@Autowired
        private IFqcInspectionTemplateLService service;

        @Autowired
        IPromptService iPromptService;
        
        /**
         * 
         * @description 查询
         * @author tianmin.wang
         * @date 2019年11月22日 
         * @param dto
         * @param page
         * @param pageSize
         * @param request
         * @return
         */
        @RequestMapping(value = "/hqm/fqc/inspection/template/l/query")
        @ResponseBody
        public ResponseData query(FqcInspectionTemplateL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);
            return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
        }

        /**
         * 
         * @description 更新保存
         * @author tianmin.wang
         * @date 2019年11月22日 
         * @param dto
         * @param result
         * @param request
         * @return
         */
        @RequestMapping(value = "/hqm/fqc/inspection/template/l/submit")
        @ResponseBody
        public ResponseData update(@RequestBody List<FqcInspectionTemplateL> dto, BindingResult result, HttpServletRequest request){
            getValidator().validate(dto, result);
            if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
            }
            for(FqcInspectionTemplateL dtoModel:dto) {
            	if("M".equals(dtoModel.getStandardType())&&(dtoModel.getStandradFrom()==null||dtoModel.getStandradTo()==null||dtoModel.getStandradUom()==null)) {
            		ResponseData responseData = new ResponseData(false);
                    responseData.setMessage("规格类型为计量时的必输项:规格值从/规格至/规格单位");
                    return responseData;
            	}
            	if("M".equals(dtoModel.getStandardType()))
            	{
    	        	if(dtoModel.getStandradFrom()>dtoModel.getStandradTo()) {
    	        	ResponseData responseData = new ResponseData(false);
    	            responseData.setMessage(iPromptService.getPromptDescription(RequestContextUtils.getLocale(request).getLanguage(), "error.standard_from_than_to"));
    	            return responseData;
    	        	}
            	}
            }
            IRequest requestCtx = createRequestContext(request);
            return new ResponseData(service.historynumberUpdate(requestCtx, dto));
        }

        /**
         * 
         * @description 删除
         * @author tianmin.wang
         * @date 2019年11月22日 
         * @param request
         * @param dto
         * @return
         */
        @RequestMapping(value = "/hqm/fqc/inspection/template/l/remove")
        @ResponseBody
        public ResponseData delete(HttpServletRequest request,@RequestBody List<FqcInspectionTemplateL> dto){
            service.batchDelete(dto);
            return new ResponseData();
        }
    }