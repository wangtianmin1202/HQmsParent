package com.hand.hqm.hqm_item_type_tests.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_item_type_tests.dto.ProjectLine;

import java.util.List;

public interface ProjectLineMapper extends Mapper<ProjectLine>{

    List<ProjectLine> plQuery(ProjectLine dto);

}