package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrSolutionMain;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.view.EcrResultMsgV0;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV0;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV1;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV2;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV4;

/**
 * @author KOCE3C3
 *
 */
public interface IEcrSolutionSkuService extends IBaseService<EcrSolutionSku>, ProxySelf<IEcrSolutionSkuService>{	 
		/**
		 * 改善方案头部查询
		 * @param iRequest
		 * @param dto
		 * @param page
		 * @param pageSize
		 * @return
		 */
		public List<EcrSolutionSkuV0> getHeadSku(IRequest iRequest, EcrMain dto, int page, int pageSize);
	 
		/**
		 * 改善方案头部查询
		 * @param iRequest
		 * @param dto
		 * @param page
		 * @param pageSize
		 * @return
		 */
		public List<EcrSolutionSkuV1> getLineSku(IRequest iRequest, EcrSolutionSku dto, int page, int pageSize);	
		/**
		 * 前台数据保存逻辑
		 * @param iRequest
		 * @param dto
		 * @return
		 */
		public ResponseData saveResult(IRequest iRequest,EcrSolutionSkuV2 dto);
	 
		
		/**
		 *  根据ecr编码和物料id 修改skuItem数据
		 * @param dto
		 */
		public void updateByEcrnoAndItemId(EcrSolutionSku dto);
		 
		/**
		 * 根据ecr编码和物料id 修改skuItem数据
		 * @param dto
		 */
		public void updateByEcrnoAndSkuId(EcrSolutionSku dto) ;
	 
		/**
		 * 更新年用量 HPM_ITEM_RESULT
		 * @param iRequest
		 * @param dto
		 */
		public void updateEcrYearQty(IRequest iRequest, EcrSolutionSku dto);
		 
		/**
		 *  方案提交后修改状态 触发审批流
		 * @param iRequest
		 * @param dto
		 * @return
		 */
		public ResponseData commitSolution(IRequest iRequest,EcrSolutionMain dto) ;
		
		
		 
		/**
		 * 生成对应技术文件数据（计数任务）
		 * @param iRequest
		 * @param dto
		 * @return
		 */
		public EcrResultMsgV0 saveTechData(IRequest iRequest,EcrSolutionMain dto) ; 
		 
		/**
		 * 改善方案物料选择下拉菜单
		 * @param dto
		 * @return
		 */
		public List<EcrSolutionSkuV1> getSolutionList(EcrMain dto);
		
		/**
		 * 选择物料后下拉的明细
		 * @param dto
		 * @return
		 */
		public List<EcrSolutionSkuV1> getDetailRfq(EcrInfluencedmaterial dto);
		
		/**确认报价界面
		 * @param dto
		 * @return
		 */
		public List<EcrSolutionSkuV1> getRfqCommitList(EcrMain dto);
		
		/**
		 * 选择物料自动提交工作流
		 * @param iRequest
		 * @param dto
		 * @return
		 */
		public ResponseData rfqCommitProcess(IRequest iRequest,EcrSolutionSkuV4 dto);
}

