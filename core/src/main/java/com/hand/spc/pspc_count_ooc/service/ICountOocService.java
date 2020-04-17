package com.hand.spc.pspc_count_ooc.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_count_ooc.dto.CountOoc;
import com.hand.spc.pspc_ooc.view.OocReportVO;

import java.util.List;

public interface ICountOocService extends IBaseService<CountOoc>, ProxySelf<ICountOocService>{

    /**
     *
     * @Description OOC报表查询
     *
     * @author yuchao.wang
     * @date 2019/8/29 22:10
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ooc.view.OocReportVO>
     *
     */
    List<OocReportVO> queryOocReport(IRequest requestContext, OocReportVO dto, int page, int pageSize);

    /**
     * @Author han.zhang
     * @Description 更新时将状态改为PROCESSED
     * @Date 18:50 2019/9/2
     * @Param [requestCtx, dto]
     */
    List<CountOoc> updateAndSave(IRequest requestCtx, List<CountOoc> dto);

    /**
     * @Author han.zhang
     * @Description 查询计数OOC数据几对应的规则编码
     * @Date 16:14 2019/9/3
     * @Param [requestContext, dto, page, pageSize]
     */
    List<CountOoc> selectCountOoc(IRequest requestContext, CountOoc dto);
}