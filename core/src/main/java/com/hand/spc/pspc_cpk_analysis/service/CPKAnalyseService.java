package com.hand.spc.pspc_cpk_analysis.service;

import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseResDTO;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseReqDTO;

public interface CPKAnalyseService {

    /**
     * @Author han.zhang
     * @Description CPK查询
     * @Date 14:46 2019/9/18
     * @Param [requestDTO]
     */
    CPKAnalyseResDTO listCPK(CPKAnalyseReqDTO requestDTO);

}
