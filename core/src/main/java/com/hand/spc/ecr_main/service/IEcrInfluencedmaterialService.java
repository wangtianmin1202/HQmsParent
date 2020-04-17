package com.hand.spc.ecr_main.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.view.EcrApsV0;
import com.hand.spc.ecr_main.view.EcrMainV0;
import com.hand.spc.ecr_main.view.EcrMaterialResultV0;
import com.hand.spc.ecr_main.view.EcrMaterialV0;

public interface IEcrInfluencedmaterialService extends IBaseService<EcrInfluencedmaterial>, ProxySelf<IEcrInfluencedmaterialService>{

	/*关联单号查询物料属性*/
	public List<EcrMaterialV0> basequery(EcrInfluencedmaterial dto) ;
	
	/*根据物料属性带出对应的产品类别*/
	public List<EcrMaterialV0> detailProductquery(EcrInfluencedmaterial dto) ;
	/*
	 * 主负责人维护进入界面的数据获取
	 */
	public List<EcrMainV0> dutySingleOrder(IRequest iRequest,EcrMain dto);
	/*
	 * 结果查询函数  仅查询用没有处理逻辑 2020年3月5日11:59:54
	 */
	public List<EcrMaterialResultV0> getItemResult(IRequest iRequest,EcrMain dto);
	/*
	 * 物料管控保存数据自动判断 如果物料清单下所有的库存状态都是已完成 则进行计算， 否则保存数据
	 */
	public ResponseData saveItemControl(IRequest iRequest,List<EcrMaterialV0> dtos);
	/*
	 *   更新最佳方案
	 * */
	public void updateState(IRequest iRequest,EcrItemResult dto);	
	
	/*
	 * 将对应计划数据存入临时表进行物料管控计算
	 */
	public List<EcrApsV0> apsTmpData(List<String> itemIds);
}