package com.hand.spc.pspc_chart.controllers;

import com.hand.spc.pspc_chart.view.ChartDetailSaveVO;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_chart.service.IChartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class ChartDetailController extends BaseController {

    @Autowired
    private IChartDetailService service;


    @RequestMapping(value = "/pspc/chart/detail/query")
    @ResponseBody
    public ResponseData query(ChartDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/pspc/chart/detail/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ChartDetail> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/chart/detail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ChartDetail> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }


    /**
     * @param dto     限制条件
     * @param request 基本参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据查询 通过头Id
     * @author: ywj
     * @date 2019/8/19 11:03
     * @version 1.0
     */
    @RequestMapping(value = "/pspc/chart/detail/queryDataByChartId")
    @ResponseBody
    public ResponseData queryDataByChartId(ChartDetail dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);

        ResponseData responseData = new ResponseData();
        try {

            responseData = service.queryBaseDataByChartId(requestContext, dto);

        } catch (Exception e) {

            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }

        return responseData;

    }

    /**
     * @param dto     传入参数
     * @param request 基本参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywj
     * @date 2019/8/19 14:17
     * @version 1.0
     */
    @RequestMapping(value = "/pspc/chart/detail/saveData")
    @ResponseBody
    public ResponseData update(@RequestBody ChartDetailSaveVO dto, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {

            responseData = service.saveData(requestCtx, dto);

        } catch (Exception e) {

            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }
}