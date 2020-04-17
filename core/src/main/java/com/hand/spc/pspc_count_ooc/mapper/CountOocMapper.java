package com.hand.spc.pspc_count_ooc.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_count_ooc.dto.CountOoc;
import com.hand.spc.pspc_ooc.view.OocReportVO;

import java.util.List;

public interface CountOocMapper extends Mapper<CountOoc>{

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

    /**
     * @Author han.zhang
     * @Description 查询OOC数据及对应编码
     * @Date 15:09 2019/9/2
     * @Param [countOoc]
     */
    List<CountOoc> selectCountOocJudge(CountOoc countOoc);
}