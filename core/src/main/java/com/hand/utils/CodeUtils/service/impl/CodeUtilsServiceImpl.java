package com.hand.utils.CodeUtils.service.impl;

import com.hand.hap.code.rule.dto.CodeRulesHeader;
import com.hand.hap.code.rule.dto.CodeRulesLine;
import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.mapper.CodeRulesHeaderMapper;
import com.hand.hap.code.rule.mapper.CodeRulesLineMapper;
import com.hand.hap.code.rule.service.ICodeRulesHeaderService;
import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.utils.CodeUtils.service.ICodeUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * description 编码规则自定义处理
 *
 * @author KOCDZX0 2020/03/16 3:35 PM
 */
@Service
public class CodeUtilsServiceImpl implements ICodeUtilsService {
    @Autowired
    public CodeRulesHeaderMapper codeRulesHeaderMapper;

    @Autowired
    public ICodeRulesHeaderService codeRulesHeaderService;

    @Autowired
    private ISysCodeRuleProcessService sysCodeRuleProcessService;

    @Autowired
    private CodeRulesLineMapper codeRulesLineMapper;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 重置频率
     */
    public final static String RESET_FREQUENCY_NEVER = "NEVER";
    public final static String RESET_FREQUENCY_YEAR = "YEAR";
    public final static String RESET_FREQUENCY_QUARTER = "QUARTER";
    public final static String RESET_FREQUENCY_MONTH = "MONTH";



    /**
     * 创建常量对象
     *
     * @param sequence 规则位置
     * @param code     常量值
     * @return
     */
    public CodeRulesLine createConstantCodeRuleLine(Long sequence, String code) {
        CodeRulesLine codeRulesLine = createCodeRuleLineForAdd();
        //常量
        codeRulesLine.setFiledType("CONSTANT");
        codeRulesLine.setFiledValue(code);
        codeRulesLine.setFieldSequence(sequence);
        return codeRulesLine;
    }

    /**
     * 创建一个可以用来新建的CodeRuleLine
     *
     * @return
     */
    public CodeRulesLine createCodeRuleLineForAdd() {
        CodeRulesLine codeRulesLine = new CodeRulesLine();
        codeRulesLine.setFiledValue("");
        codeRulesLine.setDateMask("");
        codeRulesLine.set__status("add");
        return codeRulesLine;
    }

    /**
     * 创建序列类型行规则
     *
     * @param sequence   规则位置
     * @param seqLength  长度
     * @param startValue 起始值
     * @param reset      重复频率
     * @return
     */
    public CodeRulesLine createSequenceCodeRuleLine(Long sequence, Long seqLength, Long startValue, String reset) {
        CodeRulesLine codeRulesLine = createCodeRuleLineForAdd();
        //序列
        codeRulesLine.setFiledType("SEQUENCE");
        codeRulesLine.setFieldSequence(sequence);
        codeRulesLine.setSeqLength(seqLength);
        codeRulesLine.setStartValue(startValue);
        //当前值
        codeRulesLine.setCurrentValue(1L);
        //重置频率
        codeRulesLine.setResetFrequency(reset);
        return codeRulesLine;
    }

    /**
     * 序列 自定义当前值
     *
     * @param sequence
     * @param seqLength
     * @param startValue
     * @param reset
     * @param currentValue
     * @return
     */
    public CodeRulesLine createSequenceCodeRuleLine(Long sequence, Long seqLength, Long startValue, Long currentValue, String reset) {
        CodeRulesLine codeRulesLine = createCodeRuleLineForAdd();
        //序列
        codeRulesLine.setFiledType("SEQUENCE");
        codeRulesLine.setFieldSequence(sequence);
        codeRulesLine.setSeqLength(seqLength);
        codeRulesLine.setStartValue(startValue);
        //当前值
        codeRulesLine.setCurrentValue(currentValue);
        //重置频率
        codeRulesLine.setResetFrequency(reset);
        return codeRulesLine;
    }

    /**
     * 创建日期类型行规则
     *
     * @param sequence 规则位置
     * @param dateMask 日期掩码
     * @return
     */
    public CodeRulesLine createDateCodeRuleLine(Long sequence, String dateMask) {
        CodeRulesLine codeRulesLine = createCodeRuleLineForAdd();
        //日期
        codeRulesLine.setFiledType("DATE");
        codeRulesLine.setFieldSequence(sequence);
        //格式-支持正则-需要在快码里维护对应的值
        codeRulesLine.setDateMask(dateMask);
        return codeRulesLine;
    }

    /**
     * 尝试根据编码规则获得编码
     *
     * @param ruleCode  首位获取Code
     * @param startCode 补位获取Code
     * @return 生成的流水编码
     * @throws CodeRuleException 编码规则不存在异常
     */
    @Override
    public String getNum(String ruleCode, String startCode) throws CodeRuleException {
        try {
            return sysCodeRuleProcessService.getRuleCode(ruleCode);
        } catch (CodeRuleException e) {
            try {
                return sysCodeRuleProcessService.getRuleCode(startCode);
            } catch (CodeRuleException e1) {
                throw e1;
            }
        }
    }

    /**
     * 尝试根据编码规则获得编码（含参版）
     *
     * @param ruleCode  首位获取Code
     * @param startCode 补位获取Code
     * @param map       需要填充的参数
     * @return 生成的流水编码
     * @throws CodeRuleException 编码规则不存在异常
     */
    @Override
    public String getNum(String ruleCode, String startCode, Map<String, String> map) throws CodeRuleException {
        try {
            return sysCodeRuleProcessService.getRuleCode(ruleCode, map);
        } catch (Exception e) {
            try {
                return sysCodeRuleProcessService.getRuleCode(startCode, map);
            } catch (Exception e1) {
                throw e1;
            }
        }
    }

    /**
     * 输入编码规则Code，判断是否存在启用的符合规则的编码规则
     *
     * @param ruleCode
     * @return true 存在，false不存在
     */
    @Override
    public boolean hasCode(String ruleCode) {
        CodeRulesHeader codeRulesHeader = new CodeRulesHeader();
        codeRulesHeader.setRuleCode(ruleCode);
        List<CodeRulesHeader> select = codeRulesHeaderMapper.select(codeRulesHeader);
        if (select != null && select.size() > 0) {
            for (CodeRulesHeader header : select) {
                if (!"Y".equalsIgnoreCase(header.getEnableFlag())) {
                    CodeRulesLine codeRulesLine = new CodeRulesLine();
                    codeRulesLine.setHeaderId(header.getHeaderId());
                    header.setLines(codeRulesLineMapper.select(codeRulesLine));
                    codeRulesHeaderService.updateCodeRule(header);
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
