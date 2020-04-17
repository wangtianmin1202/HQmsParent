package com.hand.hcs.hcs_asl.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_asl.dto.Asl;

public interface IAslService extends IBaseService<Asl>, ProxySelf<IAslService>{
	
	/**
	 * 合格物料查询
	 * @param requestContext 请求上下文
	 * @param asl 查询条件
	 * @param page 页码
	 * @param pageSize 页大小
	 * @return 查询结果集
	 */
	List<Asl> query(IRequest requestContext, Asl asl, int page, int pageSize);
	
	List<Asl> selectDatas(IRequest requestContext, Asl asl, int page, int pageSize);
	
	List<Asl> IQCControlSelectDatas(IRequest requestContext, Asl asl, int page, int pageSize);
	/**
	 * 到货提前期更新
	 * @param requestContext 请求上下文
	 * @param asl 更新数据
	 * @return 更新结果
	 */
	List<Asl> controlUpdate(IRequest requestContext, List<Asl> asl);
}