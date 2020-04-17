package com.hand.npi.npi_technology.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.dto.EbomMain;

public interface EbomDetailMapper extends Mapper<EbomDetail>{
	List<EbomDetail> qeuryEBomPart (EbomMain dto);
	List<EbomDetail> checkMatQty (@Param("skuCode") String skuCode,@Param("matNumber") String matNumber);
}