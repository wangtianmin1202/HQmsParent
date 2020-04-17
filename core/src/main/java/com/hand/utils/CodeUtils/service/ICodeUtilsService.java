package com.hand.utils.CodeUtils.service;

import com.hand.hap.code.rule.dto.CodeRulesLine;
import com.hand.hap.code.rule.exception.CodeRuleException;

import java.util.Map;

/**
 * description
 *
 * @author KOCDZX0 2020/03/16 3:35 PM
 */
public interface ICodeUtilsService {
    CodeRulesLine createConstantCodeRuleLine(Long sequence, String code);

    CodeRulesLine createCodeRuleLineForAdd();

    CodeRulesLine createSequenceCodeRuleLine(Long sequence, Long seqLength, Long startValue, String reset);

    CodeRulesLine createSequenceCodeRuleLine(Long sequence, Long seqLength, Long startValue, Long currentValue, String reset);

    CodeRulesLine createDateCodeRuleLine(Long sequence, String dateMask);

    String getNum(String ruleCode, String startCode) throws CodeRuleException;

    String getNum(String ruleCode, String startCode, Map<String, String> map) throws CodeRuleException;

    boolean hasCode(String ruleCode);
}
