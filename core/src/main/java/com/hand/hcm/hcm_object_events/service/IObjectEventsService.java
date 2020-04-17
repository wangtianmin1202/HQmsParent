package com.hand.hcm.hcm_object_events.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;

public interface IObjectEventsService extends IBaseService<ObjectEvents>, ProxySelf<IObjectEventsService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月4日 
	 * @param requestContext
	 * @param dto
	 * @param tableName
	 * @return
	 */
	List<ObjectEvents> query(IRequest requestContext, ObjectEvents dto, String tableName);

}