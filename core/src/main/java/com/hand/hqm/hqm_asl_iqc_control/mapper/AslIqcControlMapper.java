package com.hand.hqm.hqm_asl_iqc_control.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_asl_iqc_control.dto.AslIqcControl;

import java.util.List;

public interface AslIqcControlMapper extends Mapper<AslIqcControl>{

    List<AslIqcControl> myselect(AslIqcControl dto);
}