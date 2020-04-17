package com.hand.hcm.hcm_object_events.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;

public interface ObjectEventsMapper extends Mapper<ObjectEvents> {

	/**
	 * @description 检验员供应商关系
	 * @author tianmin.wang
	 * @date 2020年3月4日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectSupplierInspectorRel(ObjectEvents dto);

	/**
	 * @description 成品平台项目组
	 * @author tianmin.wang
	 * @date 2020年3月4日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectPlatformProgram(ObjectEvents dto);

	/**
	 * @description 库存重检周期
	 * @author tianmin.wang
	 * @date 2020年3月5日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectItemControl(ObjectEvents dto);

	/**
	 * @description PQC巡检预警
	 * @author tianmin.wang
	 * @date 2020年3月5日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectPqcWarning(ObjectEvents dto);

	/**
	 * @description 检验项目维护
	 * @author tianmin.wang
	 * @date 2020年3月5日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectInspectionAttribute(ObjectEvents dto);

	/**
	 * @description SKU与平台
	 * @author tianmin.wang
	 * @date 2020年3月5日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectOnlineSkuRule(ObjectEvents dto);

	/**
	 * @description 项目与SKU关系
	 * @author tianmin.wang
	 * @date 2020年3月6日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectProgramSkuRel(ObjectEvents dto);

	/**
	 * @description 原材料免检清单
	 * @author tianmin.wang
	 * @date 2020年3月6日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectSuppItemExemption(ObjectEvents dto);

	/**
	 * @description FQC抽样方案转移
	 * @author tianmin.wang
	 * @date 2020年3月6日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectFqcSampleSwitch(ObjectEvents dto);

	/**
	 * @description IQC抽样方案转移
	 * @author tianmin.wang
	 * @date 2020年3月6日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectSampleSwitchRule(ObjectEvents dto);

	/**
	 * @description IFQC检验单模板
	 * @author tianmin.wang
	 * @date 2020年3月9日
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectInspectionTemplate(ObjectEvents dto);

	/**
	 * @description PQC检验单模板
	 * @author tianmin.wang
	 * @date 2020年3月11日 
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectStandardOpItem(ObjectEvents dto);

	/**
	 * 获取历史记录
	 * @author kai.li
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> getHisContent(ObjectEvents dto);
	
	/**
	 * 获取历史记录 PQC
	 * @author kai.li
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> getHisContentPqc(ObjectEvents dto);

	/**
	 * @description 型式试验
	 * @author tianmin.wang
	 * @date 2020年3月21日 
	 * @param dto
	 * @return
	 */
	List<ObjectEvents> selectItemTypeTests(ObjectEvents dto);
}