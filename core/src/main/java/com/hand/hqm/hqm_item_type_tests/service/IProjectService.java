package com.hand.hqm.hqm_item_type_tests.service;

import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_item_type_tests.dto.Project;

import java.util.List;
import javax.xml.bind.ValidationException;

public interface IProjectService extends IBaseService<Project>, ProxySelf<IProjectService>{

    List<Project> add(IRequest requestCtx,Project dto) throws CodeRuleException, ValidationException;

    List<Project> exQuery(IRequest requestCtx,Project dto);

    String getProjectCode(IRequest request,String start,String mid) throws CodeRuleException;

}