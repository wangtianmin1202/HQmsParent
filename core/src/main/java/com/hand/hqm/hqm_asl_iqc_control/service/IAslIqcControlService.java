package com.hand.hqm.hqm_asl_iqc_control.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_asl_iqc_control.dto.AslIqcControl;

import java.util.List;

public interface IAslIqcControlService extends IBaseService<AslIqcControl>, ProxySelf<IAslIqcControlService>{

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
    List<AslIqcControl>  myselect(IRequest requestContext,AslIqcControl dto,int page, int pageSize);

    /**
     * 
     * @description 批量更新
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param requestCtx
     * @param dto
     * @return
     */
    List<AslIqcControl> batchTablesUpdate(IRequest requestCtx, List<AslIqcControl> dto);
}