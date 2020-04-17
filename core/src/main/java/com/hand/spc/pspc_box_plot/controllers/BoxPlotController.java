package com.hand.spc.pspc_box_plot.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_box_plot.service.IBoxPlotService;
import com.hand.spc.pspc_box_plot.view.BoxPlotVO;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.spc.pspc_entity.service.IEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class BoxPlotController extends BaseController{

    @Autowired
    private IBoxPlotService service;

    @RequestMapping(value = "/pspc/box/plot/query/ui")
    @ResponseBody
    public ResponseData queryScatterPlot(@RequestBody List<BoxPlotVO> dto, HttpServletRequest request) {
        /**
         * @Description //TODO  箱线图查询
         * @Author leizhe
         * @Date 17:09 2019/8/26
         * @Param
         * @return com.hand.hap.system.dto.ResponseData
         **/
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            // responseData = service.queryScatterPlot(requestContext, dto);

            responseData = service.queryBoxPlot(responseData,requestContext, dto);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }



}