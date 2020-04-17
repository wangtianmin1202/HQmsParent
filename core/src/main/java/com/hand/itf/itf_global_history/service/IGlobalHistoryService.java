package com.hand.itf.itf_global_history.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.itf.itf_global_history.dto.GlobalHistory;

public interface IGlobalHistoryService extends IBaseService<GlobalHistory>, ProxySelf<IGlobalHistoryService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月25日 
	 * @param gh
	 * @return
	 */
	String itfInsert(GlobalHistory gh);

}