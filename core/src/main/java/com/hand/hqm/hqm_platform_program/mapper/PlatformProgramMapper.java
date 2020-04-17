package com.hand.hqm.hqm_platform_program.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_platform_program.dto.PlatformProgram;

public interface PlatformProgramMapper extends Mapper<PlatformProgram>{

	List<PlatformProgram> reSelect(PlatformProgram dto);
}