package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrItemReport;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.view.EcrItemReportDetailV0;
import com.hand.spc.ecr_main.view.EcrItemReportV0;
import com.hand.spc.ecr_main.view.EcrItemReportV2;

public interface IEcrItemReportService extends IBaseService<EcrItemReport>, ProxySelf<IEcrItemReportService>{

	/**
	 * 	主界面查询服务
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<EcrItemReportV0> reportQuery(EcrItemReport dto,int page,int pageSize);
	
	/**
	 *  行表查询
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<EcrItemReportDetailV0> reportDetailQuery(EcrItemReport dto,int page,int pageSize);
	
	/**
	 * 物料变更明细
	 * @param supplierId
	 * @param itemId
	 * @return
	 */
	public EcrItemReportV2 getEcrOnhand(String supplierId,String itemId);
	
	
	/**
	 * 库存明细检查进度数据生成
	 * @param ecrno
	 */
	public void checkEcrProcess(IRequest iRequest, String ecrno);
}