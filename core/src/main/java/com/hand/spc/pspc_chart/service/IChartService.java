package com.hand.spc.pspc_chart.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_chart.dto.Chart;

import java.util.List;

public interface IChartService extends IBaseService<Chart>, ProxySelf<IChartService> {

    /**
     * @param iRequest 基本参数
     * @param chart    限制条件
     * @param page     页数
     * @param pageSize 页数大小
     * @return : java.util.List<com.hand.spc.pspc_chart.dto.Chart>
     * @Description: 基础数据查询
     * @author: ywj
     * @date 2019/8/19 9:54
     * @version 1.0
     */
    List<Chart> queryBaseData(IRequest iRequest, Chart chart, int page, int pageSize);

    /**
     * @param requestCtx 基本参数
     * @param chartList  传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据保存
     * @author: ywj
     * @date 2019/8/19 10:32
     * @version 1.0
     */
    ResponseData saveBaseData(IRequest requestCtx, List<Chart> chartList);

    /**
     * @param requestCtx 基本参数
     * @param chartList  传入参数
     * @return : com.hand.hap.system.dto.ResponseData
     * @Description: 数据删除
     * @author: ywj
     * @date 2019/8/19 19:47
     * @version 1.0
     */
    ResponseData deleteData(IRequest requestCtx, List<Chart> chartList);
}