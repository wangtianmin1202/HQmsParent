package com.hand.spc.pspc_count_statistic.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_count_statistic.dto.CountStatistic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ICountStatisticService extends IBaseService<CountStatistic>, ProxySelf<ICountStatisticService>{


    // 柏拉图展示查询
    ResponseData queryReport(IRequest requestContext, CountStatistic dto, int page, int pageSize);

    //导出功能
    void exportExcel(CountStatistic dto, IRequest requestContext, HttpServletRequest request, HttpServletResponse response,List<CountStatistic> rows,String img);

}