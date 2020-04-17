package com.hand.spc.pspc_ooc.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_ooc.dto.Ooc;
import com.hand.spc.pspc_ooc.view.OocReportVO;

import java.util.List;

public interface IOocService extends IBaseService<Ooc>, ProxySelf<IOocService>{
    /**
     * @Author han.zhang
     * @Description 更新时更新状态
     * @Date 下午10:24 2019/8/30
     * @Param [requestCtx, dto]
     **/
    List<Ooc> saveAndChangeStatus(IRequest requestCtx, List<Ooc> dto);

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
     * @Description 生成8D报告
     * @Date 上午9:35 2019/10/17
     * @Param [requestContext, dtos]
     **/
    ResponseData createDReport(IRequest requestContext, List<OocReportVO> dtos);
}