package com.hand.plm.plm_prod_design_standard_detail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.plm_prod_design_standard_detail.dto.ProdDesignStandardDetail;
import com.hand.plm.plm_prod_design_standard_detail.view.ProdDesignStandardDetailVO;

public interface ProdDesignStandardDetailMapper extends Mapper<ProdDesignStandardDetail> {
	List<ProdDesignStandardDetailVO> queryAll(ProdDesignStandardDetailVO vo);
	
	Long getInvalidDetailCount(@Param("detailId") String detailId);
}