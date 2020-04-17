package com.hand.hqm.hqm_item_type_tests.controllers;

import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_item_type_tests.dto.ProjectLine;
import com.hand.hqm.hqm_item_type_tests.service.IProjectLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

@Controller
public class ProjectLineController extends BaseController {

    @Autowired
    private IProjectLineService service;


    @RequestMapping(value = "/hpm/project/line/query")
    @ResponseBody
    public ResponseData query(ProjectLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hpm/project/line/exquery")
    @ResponseBody
    public ResponseData expandQuery(ProjectLine dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto));
    }

    @RequestMapping(value = "/hpm/project/line/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ProjectLine> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/project/line/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ProjectLine> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hpm/project/line/add")
    @ResponseBody
    public ResponseData add(@RequestBody ProjectLine dto, BindingResult result, HttpServletRequest request) throws CodeRuleException, ValidationException {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.add(requestCtx, dto));
    }
}