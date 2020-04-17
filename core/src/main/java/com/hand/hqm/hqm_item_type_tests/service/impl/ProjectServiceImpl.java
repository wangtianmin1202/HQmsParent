package com.hand.hqm.hqm_item_type_tests.service.impl;

import com.google.common.collect.Lists;
import com.hand.hap.code.rule.dto.CodeRulesHeader;
import com.hand.hap.code.rule.dto.CodeRulesLine;
import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.mapper.CodeRulesHeaderMapper;
import com.hand.hap.code.rule.service.ICodeRulesHeaderService;
import com.hand.hap.core.IRequest;
import com.hand.hap.hr.dto.HrOrgUnit;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_item_type_tests.dto.Project;
import com.hand.hqm.hqm_item_type_tests.mapper.ProjectMapper;
import com.hand.hqm.hqm_item_type_tests.service.IProjectService;
import com.hand.utils.CodeUtils.service.ICodeUtilsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.ValidationException;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectServiceImpl extends BaseServiceImpl<Project> implements IProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ICodeUtilsService codeUtilsService;

    @Autowired
    public CodeRulesHeaderMapper codeRulesHeaderMapper;

    @Autowired
    public ICodeRulesHeaderService codeRulesHeaderService;

    public final static String RESET_FREQUENCY_YEAR = "YEAR";
    private final static String PROJECT_NUM="PROJECT_NUM";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Project> add(IRequest requestCtx, Project dto) throws CodeRuleException, ValidationException {
        Calendar instance = Calendar.getInstance();
        if (DTOStatus.ADD.equals(dto.get__status())) {
            String projectCode = this.getProjectCode(requestCtx, dto.getProjectType(),String.valueOf(instance.get(Calendar.YEAR)).substring(2,4));
            if (StringUtils.isEmpty(projectCode)) {
                throw new ValidationException("项目编码生成错误！");
            }
            dto.setProjectCode(projectCode);
            self().insertSelective(requestCtx, dto);
        } else if (DTOStatus.UPDATE.equals(dto.get__status())) {
            self().updateByPrimaryKeySelective(requestCtx, dto);
        }
        return Arrays.asList(dto);
    }

    @Override
    public List<Project> exQuery(IRequest requestCtx, Project dto) {
        return projectMapper.exQuery(dto);
    }

    public String getProjectCode(IRequest iRequest, String start,String  mid) throws CodeRuleException {
        StringBuffer projectNum = new StringBuffer(PROJECT_NUM);
        /**
         * 获取信息拼接
         */
        if (StringUtils.isNotBlank(mid) && StringUtils.isNotBlank(start)) {
            projectNum.append(start);
            projectNum.append(mid);

            // 不存在，尝试创建编码规则
            if (!codeUtilsService.hasCode(projectNum.toString())) {
                CodeRulesHeader codeRulesHeader = new CodeRulesHeader();
                //构建对应的规则Code头
                codeRulesHeader.setRuleCode(projectNum.toString());
                codeRulesHeader.setDescription("项目编码");
                codeRulesHeader.setEnableFlag("Y");
                codeRulesHeader.setRuleName("项目编码");
                //构建对应规则Code行
                List<CodeRulesLine> rulesLines = Lists.newArrayListWithExpectedSize(4);
                rulesLines.add(codeUtilsService.createConstantCodeRuleLine(1L, start));
                rulesLines.add(codeUtilsService.createConstantCodeRuleLine(2L, mid));
                rulesLines.add(codeUtilsService.createSequenceCodeRuleLine(3L, 4L, 0000L, RESET_FREQUENCY_YEAR));
                //将对应的list 添加到规则头上
                codeRulesHeader.setLines(rulesLines);
                //创建并激活编码规则
                codeRulesHeaderService.createCodeRule(codeRulesHeader);
            }
        }
        return codeUtilsService.getNum(projectNum.toString(), PROJECT_NUM);
    }



}