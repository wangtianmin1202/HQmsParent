package com.hand.spc.ecr_main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.view.EcrMainV0;
import com.hand.spc.ecr_main.view.EcrMainV1;
import com.hand.spc.ecr_main.view.EcrMainV2;

public interface EcrMainMapper extends Mapper<EcrMain>{

	
	/*
	 * 获取当前流水号 生成对应编码
	 */
public String getNumer(@Param("ruleCode")String ruleCode);

/*
 * 获取申请信息数据
 */
public EcrMainV0 singleOrder(@Param("ecrno")String ecrno,@Param("individuationSql")String individuationSql);

/*
 * 主界面左边基础查询
 */
public List<EcrMainV1>baseQuery(EcrMainV1 dto);

/*
 * 判断所有物料是否只含有唯一的主负责人
 */
public int countUser(@Param("itemIds") List<String> itemIds);
/*
 * 获取具体的职位信息  人员信息
 */
public List<EcrMainV2> getPosition(@Param("itemIds") List<String> itemIds);
/*
 * 更新ecr主负责人信息 
 */
public void saveMainDuty(EcrMainV2 dto);
public void  saveMainDutyHead(EcrMainV2 dto);

/**查询物料最长周期  不分供应商
 * @return
 */
public Long getItemLeadTime(@Param("itemId") String itemId);

/**
 * 根据采购组获取 采购的员工编码
 * @param itemId
 * @return
 */
public String getBuyer(@Param("itemId")String itemId);
}