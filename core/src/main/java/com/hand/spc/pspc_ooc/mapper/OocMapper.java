package com.hand.spc.pspc_ooc.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_ooc.dto.Ooc;
import com.hand.spc.pspc_ooc.view.OocReportVO;

import java.util.List;

public interface OocMapper extends Mapper<Ooc>{
    List<Ooc> selectOocJudge(Ooc dto);

    /**
     *
     * @Description OOC报表查询
     *
     * @author yuchao.wang
     * @date 2019/8/29 22:12
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_ooc.view.OocReportVO>
     *
     */
    List<OocReportVO> queryOocReport(OocReportVO dto);
}