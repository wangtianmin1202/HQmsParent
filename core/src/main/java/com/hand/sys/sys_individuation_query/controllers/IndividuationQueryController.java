package com.hand.sys.sys_individuation_query.controllers;

import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.sys.sys_individuation_query.dto.IndividuationVO;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.sys.sys_individuation_query.dto.IndividuationQuery;
import com.hand.sys.sys_individuation_query.service.IIndividuationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class IndividuationQueryController extends BaseController {

    @Autowired
    private IIndividuationQueryService service;
    @Autowired
    private ICodeService codeService;


    @RequestMapping(value = "/sys/individuation/query/query")
    @ResponseBody
    public ResponseData query(IndividuationQuery dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/sys/individuation/query/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<IndividuationQuery> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/sys/individuation/query/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<IndividuationQuery> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/sys/individuation/query/query/function")
    @ResponseBody
    public ResponseData queryByFunction(HttpServletRequest request,String code) {
        //url=request.getHeader("Referer");
        return new ResponseData(service.selectColumnByFunction(code));
    }

    @RequestMapping(value = "/sys/individuation/query/query/code")
    @ResponseBody
    public ResponseData queryByCode(HttpServletRequest request, String tag) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(codeService.selectCodeValuesByCodeNameTag(requestCtx,"INDIVIDUATION_OPERATION",tag));
    }

    @RequestMapping(value = "/sys/individuation/query/query/save")
    @ResponseBody
    public ResponseData saveData(IndividuationVO vo,HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        service.saveTemplate(requestCtx,vo);
        return new ResponseData();
    }

    @RequestMapping(value = "/sys/individuation/query/query/onquery")
    @ResponseBody
    public String queryData(IndividuationVO vo,HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        String sql=service.queryData(requestCtx,vo);
        return sql;
    }

    @RequestMapping(value = "/sys/individuation/query/query/header")
    @ResponseBody
    public String getHeader(HttpServletRequest request) {
        String code;
        String url=request.getHeader("Referer");

        try {
            String str1 = url.substring(0, url.indexOf("?functionCode="));
            code = url.substring(str1.length() + 14, url.length());
        }
        catch (Exception e){
            code=null;
        }

        return code;
    }

}