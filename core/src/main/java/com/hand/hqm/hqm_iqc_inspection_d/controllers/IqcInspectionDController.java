package com.hand.hqm.hqm_iqc_inspection_d.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_iqc_inspection_d.dto.IqcInspectionD;
import com.hand.hqm.hqm_iqc_inspection_d.service.IIqcInspectionDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class IqcInspectionDController extends BaseController{

    @Autowired
    private IIqcInspectionDService service;


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
    @RequestMapping(value = "/hqm/iqc/inspection/d/query")
    @ResponseBody
    public ResponseData query(IqcInspectionD dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     * 
     * @description 保存
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/iqc/inspection/d/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<IqcInspectionD> dto, BindingResult result, HttpServletRequest request){
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
     * 
     * @description 删除
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/iqc/inspection/d/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<IqcInspectionD> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }