package com.hand.plm.laboratory.service;


import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.laboratory.dto.LabUser;
import com.hand.plm.laboratory.dto.LabUserPost;

public interface ILabUserPostService extends IBaseService<LabUserPost>, ProxySelf<ILabUserPostService>{
	
	void createUserPost(LabUser dto,IRequest requestContext,Float labUserId);

}