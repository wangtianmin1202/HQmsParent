package com.hand.plm.laboratory.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.laboratory.dto.LabUser;
import com.hand.plm.laboratory.dto.LabUserOldScore;

public interface ILabUserOldScoreService extends IBaseService<LabUserOldScore>, ProxySelf<ILabUserOldScoreService>{

	ResponseData addOldData(IRequest requestCtx,List<LabUser> users);
	
	List<LabUserOldScore> query(LabUserOldScore dto, int pageNum, int pageSize);
	
}