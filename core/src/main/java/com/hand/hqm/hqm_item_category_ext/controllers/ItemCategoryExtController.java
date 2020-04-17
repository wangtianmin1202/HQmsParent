package com.hand.hqm.hqm_item_category_ext.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hqm.hqm_item_category_ext.dto.ItemCategoryExt;
import com.hand.hqm.hqm_item_category_ext.service.IItemCategoryExtService;
import com.hand.hqm.hqm_sample_size_code_letter.dto.SampleSizeCodeLetter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
	/*
	 * created by tianmin.wang on 2019/7/9.
	 */
    @Controller
    public class ItemCategoryExtController extends BaseController{

    @Autowired
    private IItemCategoryExtService service;


    /**
     * 基础查询
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/item/category/ext/query")
    @ResponseBody
    public ResponseData query(ItemCategoryExt dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    
    /**
     * 分组查询
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/item/category/ext/groupquery")
    @ResponseBody
    public ResponseData groupquery(ItemCategoryExt dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectGroup(requestContext,dto,page,pageSize));
    }
    
    /**
     * 
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/item/category/ext/categoryquery")
    @ResponseBody
    public ResponseData categoryquery(ItemCategoryExt dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.categoryquery(requestContext,dto));
    }
    
    /**
     * 保存
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/item/category/ext/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ItemCategoryExt> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestContext = createRequestContext(request);
        for(ItemCategoryExt d : dto) {
//        	if(d.get__status()!=null&&!d.get__status().equals(DTOStatus.ADD)&&!d.get__status().equals(DTOStatus.UPDATE)&&!d.get__status().equals(DTOStatus.DELETE)) {
//        		continue;
//        	}
//        	else if(d.get__status()==null) {
//        		continue;
//        	}
//        	else {
//        		
//        	}
//        	ItemCategoryExt search = new ItemCategoryExt();
//        	search.setItemCategory(d.getItemCategory());
//        	search.setOrderCode(d.getOrderCode());
//        	search.setEnableFlag("Y");
//        	List<ItemCategoryExt> list = new ArrayList<>();
//        	list = service.select(requestContext, search, 1, 1);
//        	if(list.size()>0) {
//        		ResponseData responseData = new ResponseData(false);
//                responseData.setMessage("排序码"+d.getOrderCode()+"/物料组"+d.getItemCategory()+"/在有效记录中已存在");
//                return responseData;
//        	};
        	if(d.getKid() == null||d.getKid().equals("")) {
        		d.set__status(DTOStatus.ADD);
        	}
        }
        
        List<ItemCategoryExt> rows = service.batchUpdate(requestContext, dto);
        return new ResponseData(rows);
    }

    /**
     * 删除
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/item/category/ext/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ItemCategoryExt> dto){
        service.reBatchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 批量删除
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/item/category/ext/removebatch")
    @ResponseBody
    public ResponseData deleteBatch(HttpServletRequest request,@RequestBody List<ItemCategoryExt> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
//    @Autowired
//    IPromptService iPromptService;
//    public String getMessage(HttpServletRequest request,String promptCode) {
//		return iPromptService.getPromptDescription(RequestContextUtils.getLocale(request).getLanguage(), promptCode);
//    }
    }