package com.hand.spc.pspc_chart.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_chart.dto.ChartDetail;
import com.hand.spc.pspc_chart.view.ChartDetailSaveVO;

public interface IChartDetailService extends IBaseService<ChartDetail>, ProxySelf<IChartDetailService> {

    /**
     * @param requestCtx  基本参数
     * @param chartDetail 限制条件
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据查询
     * @author: ywj
     * @date 2019/8/19 11:01
     * @version 1.0
     */
    ResponseData queryBaseDataByChartId(IRequest requestCtx, ChartDetail chartDetail);

    /**
     * @param requestCtx        基本参数
     * @param chartDetailSaveVO 传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywj
     * @date 2019/8/19 14:18
     * @version 1.0
     */
    ResponseData saveData(IRequest requestCtx, ChartDetailSaveVO chartDetailSaveVO);

}