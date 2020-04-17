package com.hand.hqm.hqm_iqc_inspection_template_l_his.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_iqc_inspection_template_l_his.dto.IqcInspectionTemplateLHis;
import com.hand.hqm.hqm_iqc_inspection_template_l_his.service.IIqcInspectionTemplateLHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class IqcInspectionTemplateLHisController extends BaseController{

    @Autowired
    private IIqcInspectionTemplateLHisService service;


    @RequestMapping(value = "/hqm/iqc/inspection/template/l/his/query")
    @ResponseBody
    public ResponseData query(IqcInspectionTemplateLHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/iqc/inspection/template/l/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<IqcInspectionTemplateLHis> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/iqc/inspection/template/l/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<IqcInspectionTemplateLHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }