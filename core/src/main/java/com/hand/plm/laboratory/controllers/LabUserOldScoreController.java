package com.hand.plm.laboratory.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.laboratory.dto.LabUserOldScore;
import com.hand.plm.laboratory.service.ILabUserOldScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class LabUserOldScoreController extends BaseController{

    @Autowired
    private ILabUserOldScoreService service;


    @RequestMapping(value = "/plm/lab/user/old/score/query")
    @ResponseBody
    public ResponseData query(LabUserOldScore dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
       // return new ResponseData(service.select(requestContext,dto,page,pageSize));
        return new ResponseData(service.query(dto, page, pageSize));
    }

    @RequestMapping(value = "/plm/lab/user/old/score/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<LabUserOldScore> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/plm/lab/user/old/score/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<LabUserOldScore> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }