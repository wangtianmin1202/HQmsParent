package com.hand.hqm.hqm_d2_v_value.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_d2_v_value.dto.D2VValue;
import com.hand.hqm.hqm_d2_v_value.service.ID2VValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class D2VValueController extends BaseController{

    @Autowired
    private ID2VValueService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/d2/v/value/query")
    @ResponseBody
    public ResponseData query(D2VValue dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/d2/v/value/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<D2VValue> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/d2/v/value/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<D2VValue> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 求临界值
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/d2/v/value/getTinvs")
    @ResponseBody
    public Double getTinvs(HttpServletRequest request,D2VValue dto){
    	double tinv = -1;
    	try {
    		tinv = service.getTinv(dto);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
        
        return tinv;
    }
    }