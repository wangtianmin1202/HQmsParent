package com.hand.hqm.hqm_item_type_tests.service.impl;

import com.google.common.collect.Lists;
import com.hand.hap.code.rule.dto.CodeRulesHeader;
import com.hand.hap.code.rule.dto.CodeRulesLine;
import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.mapper.CodeRulesHeaderMapper;
import com.hand.hap.code.rule.service.ICodeRulesHeaderService;
import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_item_type_tests.dto.Project;
import com.hand.hqm.hqm_item_type_tests.mapper.ProjectLineMapper;
import com.hand.hqm.hqm_item_type_tests.mapper.ProjectMapper;
import com.hand.utils.CodeUtils.service.ICodeUtilsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_item_type_tests.dto.ProjectLine;
import com.hand.hqm.hqm_item_type_tests.service.IProjectLineService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import javax.xml.bind.ValidationException;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectLineServiceImpl extends BaseServiceImpl<ProjectLine> implements IProjectLineService{

    @Autowired
    private ProjectLineMapper lineMapper;

    @Autowired
    private CodeRulesHeaderMapper codeRulesHeaderMapper;

    @Autowired
    private ICodeRulesHeaderService codeRulesHeaderService;

    @Autowired
    private ICodeUtilsService codeUtilsService;

    @Autowired
    private ISysCodeRuleProcessService ruleProcessService;

    public final static String RESET_FREQUENCY_YEAR = "YEAR";

    @Autowired
    private ProjectMapper projectMapper;


    @Override
    public List<ProjectLine> query(IRequest requestCtx, ProjectLine dto) {
        return lineMapper.plQuery(dto);
    }

    @Override
    public List<ProjectLine> add(IRequest requestCtx, ProjectLine dto) throws CodeRuleException, ValidationException {
        ProjectLine proLine = new ProjectLine();
        proLine.setProjectId(dto.getProjectId());
        List<ProjectLine> projectLines =lineMapper.select(proLine);
        Project project = projectMapper.selectByPrimaryKey(dto.getProjectId());
        if (DTOStatus.ADD.equals(dto.get__status())) {
            if(projectLines.stream().anyMatch(
                    line->line.getSkuCode().equals(dto.getSkuCode()))){
                throw new ValidationException("当前项目已存在相同SKU型号，请重新输入！");
            }
            String subcode = this.getSubcode(requestCtx, project.getProjectCode());
            if(StringUtils.isEmpty(subcode)){
                throw new ValidationException("子代号生成错误！");
            }
            dto.setSubcode(ruleProcessService.getRuleCode(subcode) );
            self().insertSelective(requestCtx, dto);
        } else {
            if(projectLines.stream()
                    .filter(line->line.getProjectLineId().equals(dto.getProjectLineId()))
                    .anyMatch(line->line.getSkuCode().equals(dto.getSkuCode()))){
                throw new ValidationException("当前项目已存在相同SKU型号，请重新输入！");
            }
            self().updateByPrimaryKeySelective(requestCtx, dto);
        }
        return Arrays.asList(dto);
    }


    private String getSubcode(IRequest request, String ruleCode){
        CodeRulesHeader rulesHeader = new CodeRulesHeader();
        rulesHeader.setRuleCode(ruleCode);
        // 不存在该编码规则时创建
        List<CodeRulesHeader> headers = codeRulesHeaderMapper.select(rulesHeader);
        if (!headers.isEmpty()) {
            return ruleCode;
        } else {
            return createProjectLineRule(request, ruleCode);
        }
    }

    private String createProjectLineRule(IRequest iRequest,String ruleCode){
        CodeRulesHeader rulesHeader = new CodeRulesHeader();
        rulesHeader.setRuleCode(ruleCode);
        rulesHeader.setRuleName("项目子代号");
        rulesHeader.setDescription("项目子代号");
        rulesHeader.setEnableFlag("Y");
        List<CodeRulesLine> rulesLines = Lists.newArrayListWithCapacity(4);
        rulesHeader.setLines(rulesLines);
        //设置常量
        rulesLines.add(codeUtilsService.createConstantCodeRuleLine(1L, ruleCode+"-"));
        // 流水号的长度调整
        rulesLines.add(codeUtilsService.createSequenceCodeRuleLine(2L, 1L, 1L,0L, RESET_FREQUENCY_YEAR));
        return codeRulesHeaderService.createCodeRule(rulesHeader).getRuleCode();
    }
}