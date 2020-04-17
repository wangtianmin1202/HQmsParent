package com.hand.hcs.hcs_certificate_file_manage.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_certificate_file_manage.dto.CertificateHistory;
import com.hand.hcs.hcs_certificate_file_manage.service.ICertificateHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CertificateHistoryController extends BaseController {

    @Autowired
    private ICertificateHistoryService service;


    @RequestMapping(value = "/hcs/certificate/history/query")
    @ResponseBody
    public ResponseData query(CertificateHistory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hcs/certificate/history/listQuery")
    @ResponseBody
    public ResponseData listQuery(CertificateHistory dto, HttpServletRequest request,@RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.listQuery(requestContext, dto,page,pageSize));
    }

    @RequestMapping(value = "/hcs/certificate/history/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CertificateHistory> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcs/certificate/history/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<CertificateHistory> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}