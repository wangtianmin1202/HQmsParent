package com.hand.hqm.hqm_item_type_tests.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_item_type_tests.dto.Project;

import java.util.List;

public interface ProjectMapper extends Mapper<Project>{

    List<Project> exQuery(Project dto);

}