package com.hand.itf.itf_global_history.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.itf.itf_global_history.dto.GlobalHistory;
import com.hand.itf.itf_global_history.service.IGlobalHistoryService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class GlobalHistoryServiceImpl extends BaseServiceImpl<GlobalHistory> implements IGlobalHistoryService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.itf.itf_global_history.service.IGlobalHistoryService#itfInsert(com.
	 * hand.itf.itf_global_history.dto.GlobalHistory)
	 */
	@Override
	public String itfInsert(GlobalHistory gh) {
		// TODO 插入
		mapper.insertSelective(gh);
		return gh.getMessageStore();
	}

}