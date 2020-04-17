package com.hand.spc.ecr_main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrRfq;


public interface EcrRfqMapper extends Mapper<EcrRfq>{

	/**
	 * -查询改善方案信息
	 * @param ecrno
	 * @param itemIds 
	 * @return
	 */
	List<EcrRfq> selectSolution(@Param("ecrno") String ecrno,@Param("list") List<String> itemIds);
	
	/**
	 * -查询 RFQ 信息
	 * @param dto
	 * @return
	 */
	List<EcrRfq> selectData(EcrRfq dto);
	
	/**
	 * -失效 RFQ 数据
	 * @param ecrno
	 * @param itemId
	 */
	void updateRfqInvalide(@Param("ecrno") String ecrno,@Param("itemId")  String itemId);

	/**
	 * -流程：技术文件 查询
	 * @param dto
	 * @return
	 */
	List<EcrRfq> selectTaskFile(EcrRfq dto);
	
	
	/**
	 * -流程：技术文件 保存
	 */
	void updateTaskFile(EcrRfq dto);
	
	
	/**
	 * -流程：技术文件 保存-head
	 */
	void updateTaskFileHead(EcrRfq file);
	
	/**
	 * -流程：技术文件 查询当前版本
	 * @param itemId
	 */
	String selectFileVersion(String itemId);

	/**
	 * -查询产品品类
	 * @param string
	 * @return
	 */
	List<String> selectCatogry(String string);

	/**
	 * -查询新品老品
	 * @param ecrno
	 * @return
	 */
	List<String> selectMainType(String ecrno);

	/**
	 *-根据物料 id 查询物料 code
	 * @param itemId
	 * @return
	 */
	List<String> selectItemCode(String itemId);
	

	/**
	 * -流程：QTP 查询
	 */
	List<EcrRfq> selectTaskQtp(EcrRfq dto);
	
	
	/**
	 * -流程：QTP 保存
	 */
	void updateTaskQtp(EcrRfq dto);
	
	/**
	 * -流程：VTP 查询
	 */
	List<EcrRfq> selectTaskVtp(EcrRfq dto);
	
	
	/**
	 * -流程：VTP 保存
	 */
	void updateTaskVtp(EcrRfq dto);
	
	/**
	 * TODO 待定
	 * -流程：PCI 查询
	 */
	List<EcrRfq> selectTaskPci(EcrRfq dto);
	
	
	/**
	 * -流程：PCI 保存
	 */
	void updateTaskPci(EcrRfq dto);
	
	/**
	 * -流程：RFQ 查询
	 */
	List<EcrRfq> selectTaskRfq(EcrRfq dto);
	
	
	/**
	 * -流程：RFQ 保存
	 */
	void updateTaskRfq(EcrRfq dto);

}