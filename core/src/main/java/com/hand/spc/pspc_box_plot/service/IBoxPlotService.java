package com.hand.spc.pspc_box_plot.service;



import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_box_plot.view.BoxPlotVO;

import java.util.List;

public interface IBoxPlotService{


    //箱线图查询
    ResponseData queryBoxPlot(ResponseData responseData, IRequest requestContext, List<BoxPlotVO> dto);

}