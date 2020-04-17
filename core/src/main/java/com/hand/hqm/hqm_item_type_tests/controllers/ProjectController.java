package com.hand.hqm.hqm_item_type_tests.controllers;

import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_item_type_tests.dto.Project;
import com.hand.hqm.hqm_item_type_tests.service.IProjectService;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
public class ProjectController extends BaseController {

    @Autowired
    private IProjectService service;

    @Autowired
    ISysCodeRuleProcessService codeRuleProcessService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/hpm/project/query")
    @ResponseBody
    public ResponseData query(Project dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hpm/project/exquery")
    @ResponseBody
    public ResponseData exQuery(Project dto ,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.exQuery(requestContext,dto));
    }

    @RequestMapping(value = "/hpm/project/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Project> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/project/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Project> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hpm/project/add")
    @ResponseBody
    public ResponseData add(@RequestBody Project dto, BindingResult result, HttpServletRequest request) throws CodeRuleException, ValidationException {
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