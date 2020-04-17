package com.hand.spc.pspc_chart.controllers;

import com.hand.spc.pspc_entity.service.IEntityService;
import com.hand.spc.pspc_ooc.dto.Ooc;
import com.hand.spc.pspc_ooc.service.IOocService;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_chart.dto.Chart;
import com.hand.spc.pspc_chart.service.IChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class ChartController extends BaseController {

    @Autowired
    private IChartService service;

    @Autowired
    private IOocService oocService;


    @RequestMapping(value = "/pspc/chart/query")
    @ResponseBody
    public ResponseData query(Chart dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestBody String a,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryBaseData(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/pspc/chart/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Chart> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.saveBaseData(requestCtx, dto);
    }

    /**
     * @param request 基本参数
     * @param dto     限制条件
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据删除
     * @author: ywj
     * @date 2019/8/20 11:05
     * @version 1.0
     */
    @RequestMapping(value = "/pspc/chart/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Chart> dto) {

        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {

            responseData = service.deleteData(requestCtx, dto);

        } catch (Exception e) {

            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }

        return responseData;

    }
}