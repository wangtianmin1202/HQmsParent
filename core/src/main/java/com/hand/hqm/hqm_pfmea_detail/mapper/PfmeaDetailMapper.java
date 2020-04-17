package com.hand.hqm.hqm_pfmea_detail.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_pfmea_detail.dto.PfmeaDetail;

public interface PfmeaDetailMapper extends Mapper<PfmeaDetail>{

	 /**
    * @Author han.zhang
    * @Description 查询根对象
    * @Date 14:08 2019/8/19
    * @Param []
    * @param dto
    */
   List<PfmeaDetail> selectParentInvalid(PfmeaDetail dto);
   
   /**
    * @Author han.zhang
    * @Description 根据父级查询子集
    * @Date 14:08 2019/8/19
    * @Param []
    * @param dto
    */
   List<PfmeaDetail> selectInvalidByParent(PfmeaDetail dto);
   
   int checkStructure(PfmeaDetail record);
   
   int checkFunctionAndInvalid(PfmeaDetail record);
   /**
    * @Author ruifu.jiang
    * @Description 页面查询
    * @Date 19:40 2019/8/26
    * @Param [requestCtx, dto,page,pagesize]
    */
   List<PfmeaDetail> myselect(PfmeaDetail dto);
   /**
    * @Author ruifu.jiang
    * @Description 获取打印数据
    * @Date 19:40 2019/8/26
    * @Param [requestCtx, dto,page,pagesize]
    */
   List<PfmeaDetail> queryprintData(PfmeaDetail dto);
   
   List<PfmeaDetail> commitSelect(PfmeaDetail dto);
   
   List<PfmeaDetail> Selectbyparentbranch(PfmeaDetail dto);
   /**
    * @Author ruifu.jiang
    * @Description 查询头表数据
    * @Date 19:40 2019/8/26
    * @Param [kid]
    */
   PfmeaDetail queryHeaderData(Float kid);
   
   Long getparentIdsec(PfmeaDetail dto);
   
   Long getparentIdthd(PfmeaDetail dto);
   
   Long getparentIdfromthd(PfmeaDetail dto);

   List<PfmeaDetail> selectCondition(PfmeaDetail dto);

	/**
	 * @Description:修改
	 * @param dto
	 */
	void updatePfmeaDetail(PfmeaDetail dto);
}