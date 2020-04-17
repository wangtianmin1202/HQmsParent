package com.hand.spc.ecr_main.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrTechnicalFileHeader;
import com.hand.spc.ecr_main.service.IEcrTechnicalFileHeaderService;
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
public class EcrTechnicalFileHeaderController extends BaseController {

    @Autowired
    private IEcrTechnicalFileHeaderService service;


    @RequestMapping(value = "/hpm/ecr/technical/file/header/query")
    @ResponseBody
    public ResponseData query(EcrTechnicalFileHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/technical/file/header/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrTechnicalFileHeader> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/technical/file/header/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<EcrTechnicalFileHeader> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}