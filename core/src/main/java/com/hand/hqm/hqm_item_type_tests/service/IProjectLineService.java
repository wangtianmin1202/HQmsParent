package com.hand.hqm.hqm_item_type_tests.service;

import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_item_type_tests.dto.ProjectLine;

import java.util.List;
import javax.xml.bind.ValidationException;

public interface IProjectLineService extends IBaseService<ProjectLine>, ProxySelf<IProjectLineService>{

    List<ProjectLine> query(IRequest requestCtx, ProjectLine dto);

    List<ProjectLine> add(IRequest requestCtx,ProjectLine dto) throws CodeRuleException, ValidationException;

}