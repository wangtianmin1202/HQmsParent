package com.hand.hqm.hqm_use_detail_after.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_scrapped_detail_after.dto.ScrappedDetailAfter;
import com.hand.hqm.hqm_use_detail_after.dto.UseDetailAfter;
import com.hand.hqm.hqm_use_detail_after.service.IUseDetailAfterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class UseDetailAfterController extends BaseController{

    @Autowired
    private IUseDetailAfterService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/use/detail/after/query")
    @ResponseBody
    public ResponseData query(UseDetailAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/use/detail/after/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<UseDetailAfter> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/use/detail/after/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<UseDetailAfter> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/hqm/use/detail/after/addone")
    @ResponseBody
    public ResponseData addOne(UseDetailAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request){
    	IRequest requestCtx = createRequestContext(request);
        service.addOne(requestCtx,dto);
        return new ResponseData();
    }
    }