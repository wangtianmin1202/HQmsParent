package com.hand.hqm.hqm_mes_ng_recorde_line.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_mes_ng_recorde_line.dto.MesNgRecordeLine;
import com.hand.hqm.hqm_mes_ng_recorde_line.service.IMesNgRecordeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class MesNgRecordeLineController extends BaseController{

    @Autowired
    private IMesNgRecordeLineService service;


    @RequestMapping(value = "/hqm/mes/ng/recorde/line/query")
    @ResponseBody
    public ResponseData query(MesNgRecordeLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/mes/ng/recorde/line/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MesNgRecordeLine> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/mes/ng/recorde/line/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MesNgRecordeLine> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }