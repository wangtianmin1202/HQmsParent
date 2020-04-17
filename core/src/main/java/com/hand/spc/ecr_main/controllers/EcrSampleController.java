package com.hand.spc.ecr_main.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrSample;
import com.hand.spc.ecr_main.service.IEcrSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Controller
public class EcrSampleController extends BaseController {

    @Autowired
    private IEcrSampleService service;


    @RequestMapping(value = "/hpm/ecr/sample/query")
    @ResponseBody
    public ResponseData query(EcrSample dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/sample/querybs")
    @ResponseBody
    public ResponseData bsquery(EcrSample dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.baseQuery(dto, page, pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/sample/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrSample> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/sample/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<EcrSample> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hpm/ecr/sample/upload")
    @ResponseBody
    public ResponseData fileUpload(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            responseData = service.fileUpload(requestCtx, request);
        } catch (IllegalStateException | IOException e) {
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping(value = "/hpm/ecr/sample/delfile")
    @ResponseBody
    public ResponseData updaeDataAndDelFile(HttpServletRequest request, @RequestBody List<EcrSample> dto) {
        IRequest requestCtx = createRequestContext(request);
        service.updateAndDelFile(requestCtx, dto);
        return new ResponseData();
    }
}