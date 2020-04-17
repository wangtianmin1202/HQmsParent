package com.hand.npi.npi_technology.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.npi.npi_technology.dto.EbomMain;

public interface EbomMainMapper extends Mapper<EbomMain>{
	EbomMain queryNewEBomVersion(EbomMain dto);
}