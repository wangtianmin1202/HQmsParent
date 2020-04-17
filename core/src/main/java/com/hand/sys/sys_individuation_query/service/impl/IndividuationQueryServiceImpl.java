package com.hand.sys.sys_individuation_query.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.sys.sys_individuation_query.dto.IndividuationVO;
import com.hand.sys.sys_individuation_query.mapper.IndividuationQueryMapper;
import com.hand.sys.sys_individuation_template.dto.IndividuationTemplate;
import com.hand.sys.sys_individuation_template.service.IIndividuationTemplateService;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.sys.sys_individuation_query.dto.IndividuationQuery;
import com.hand.sys.sys_individuation_query.service.IIndividuationQueryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class IndividuationQueryServiceImpl extends BaseServiceImpl<IndividuationQuery> implements IIndividuationQueryService {

    @Autowired
    IndividuationQueryMapper individuationQueryMapper;
    @Autowired
    IIndividuationTemplateService templateLService;

    private String templateValue;

    @Override
    public List<IndividuationQuery> selectColumnByFunction(String code) {
        List<IndividuationQuery> individuationQueryList = individuationQueryMapper.selectColumnByFunction(code);
        return individuationQueryList;
    }

    @Override
    public void saveTemplate(IRequest request, IndividuationVO vo) {
        if (StringUtils.isNotEmpty(vo.toString())) {

            String sql = getSql(vo);
            String condition = setCondition(vo);

            IndividuationTemplate individuationTemplate = new IndividuationTemplate();
            individuationTemplate.setTemplateName(vo.getTemplateName());
            individuationTemplate.setTemplateUser(Float.parseFloat(request.getUserId().toString()));
            individuationTemplate.setFunctionCode(vo.getFunctionCode());
            List<IndividuationTemplate> templateList = templateLService.select(request, individuationTemplate, 1, 100);

            individuationTemplate.setTemplateValue(condition);
            try {
                if (templateList.size() > 0) {
                    templateList.get(0).setTemplateSql(sql);
                    templateList.get(0).setFunctionCode(vo.getFunctionCode());
                    templateLService.updateByPrimaryKey(request, templateList.get(0));
                } else {
                    individuationTemplate.setTemplateSql(sql);
                    individuationTemplate.setFunctionCode(vo.getFunctionCode());
                    templateLService.insert(request, individuationTemplate);
                }
            } catch (Exception e) {
                System.out.println("SB：" + e.toString());
            }
        }
    }

    @Override
    public String queryData(IRequest request, IndividuationVO vo) {
        return getSql(vo);
    }

    /**
     * 获取查询SQL
     *
     * @param vo
     * @return
     */
    private String getSql(IndividuationVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("AND (");

        sql.append(" ((" + getChildSql(vo.getColumnCode1(), vo.getOperationCode1(), vo.getValue1()) + ") ");
        sql.append(" " + vo.getLogicCode1() + " ");
        sql.append(" (" + getChildSql(vo.getColumnCode2(), vo.getOperationCode2(), vo.getValue2()) + ") ");
        sql.append(" " + vo.getLogicCode2() + " ");
        sql.append(" (" + getChildSql(vo.getColumnCode3(), vo.getOperationCode3(), vo.getValue3()) + ") ");
        sql.append(" " + vo.getLogicCode3() + " ");
        sql.append(" (" + getChildSql(vo.getColumnCode4(), vo.getOperationCode4(), vo.getValue4()) + ") ");

        sql.append(") " + vo.getLogicCode4() + " (");

        sql.append(" (" + getChildSql(vo.getColumnCode5(), vo.getOperationCode5(), vo.getValue5()) + ") ");
        sql.append(" " + vo.getLogicCode5() + " ");
        sql.append(" (" + getChildSql(vo.getColumnCode6(), vo.getOperationCode6(), vo.getValue6()) + ") ");
        sql.append(" " + vo.getLogicCode6() + " ");
        sql.append(" (" + getChildSql(vo.getColumnCode7(), vo.getOperationCode7(), vo.getValue7()) + ") ");
        sql.append(" " + vo.getLogicCode7() + " ");
        sql.append(" (" + getChildSql(vo.getColumnCode8(), vo.getOperationCode8(), vo.getValue8()) + ")) ");

        sql.append(")");

        return sql.toString();
    }

    /**
     * 获取子条件查询SQL
     *
     * @param columnCode    列名
     * @param operationCode 运算符
     * @param value         值
     * @return
     */
    private String getChildSql(String columnCode, String operationCode, String value) {
        StringBuilder sql = new StringBuilder();

        if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(columnCode) && StringUtils.isNotEmpty(operationCode)) {
            IndividuationQuery individuationQuery = individuationQueryMapper.selectByPrimaryKey(columnCode);

            if (StringUtils.isNotEmpty(individuationQuery.toString())) {
                if (individuationQuery.getColumnType().equals("DATE")) {
                    sql.append(individuationQuery.getColumnCode() + " " + operationCode + " =to_date('" + value + "',yyyy-mm-dd hh24:mi:ss) ");
                } else if (individuationQuery.getColumnType().equals("TEXT")) {
                    if (operationCode.toUpperCase().equals("LIKE") || operationCode.toUpperCase().equals("NOT LIKE")) {
                        sql.append(individuationQuery.getColumnCode() + " " + operationCode + " '%" + value + "%' ");
                    } else {
                        sql.append(individuationQuery.getColumnCode() + " " + operationCode + " '" + value + "' ");
                    }
                } else {
                    sql.append(individuationQuery.getColumnCode() + " " + operationCode + " " + value + " ");
                }
            } else {
                sql.append(" 1=1 ");
            }
        } else {
            sql.append(" 1=1 ");
        }

        return sql.toString();
    }

    /**
     * 查询条件以字符串以逗号拼接
     *
     * @param vo
     * @return
     */
    private String setCondition(IndividuationVO vo) {
        StringBuilder condition = new StringBuilder();

        IndividuationQuery individuationQuery = individuationQueryMapper.selectByPrimaryKey(vo.getColumnCode1());
        condition.append(individuationQuery.getColumnCode());
        condition.append(",");
        condition.append(vo.getOperationCode1());
        condition.append(",");
        condition.append(vo.getValue1());
        condition.append(",");

        condition.append(vo.getLogicCode1());
        condition.append(",");
        individuationQuery = individuationQueryMapper.selectByPrimaryKey(vo.getColumnCode2());
        condition.append(individuationQuery.getColumnCode());
        condition.append(",");
        condition.append(vo.getOperationCode2());
        condition.append(",");
        condition.append(vo.getValue2());
        condition.append(",");

        condition.append(vo.getLogicCode2());
        condition.append(",");
        individuationQuery = individuationQueryMapper.selectByPrimaryKey(vo.getColumnCode3());
        condition.append(individuationQuery.getColumnCode());
        condition.append(",");
        condition.append(vo.getOperationCode3());
        condition.append(",");
        condition.append(vo.getValue3());
        condition.append(",");

        condition.append(vo.getLogicCode3());
        condition.append(",");
        individuationQuery = individuationQueryMapper.selectByPrimaryKey(vo.getColumnCode4());
        condition.append(individuationQuery.getColumnCode());
        condition.append(",");
        condition.append(vo.getOperationCode4());
        condition.append(",");
        condition.append(vo.getValue4());
        condition.append(",");

        condition.append(vo.getLogicCode4());
        condition.append(",");
        individuationQuery = individuationQueryMapper.selectByPrimaryKey(vo.getColumnCode5());
        condition.append(individuationQuery.getColumnCode());
        condition.append(",");
        condition.append(vo.getOperationCode5());
        condition.append(",");
        condition.append(vo.getValue5());
        condition.append(",");

        condition.append(vo.getLogicCode5());
        condition.append(",");
        individuationQuery = individuationQueryMapper.selectByPrimaryKey(vo.getColumnCode6());
        condition.append(individuationQuery.getColumnCode());
        condition.append(",");
        condition.append(vo.getOperationCode6());
        condition.append(",");
        condition.append(vo.getValue6());
        condition.append(",");

        condition.append(vo.getLogicCode6());
        condition.append(",");
        individuationQuery = individuationQueryMapper.selectByPrimaryKey(vo.getColumnCode7());
        condition.append(individuationQuery.getColumnCode());
        condition.append(",");
        condition.append(vo.getOperationCode7());
        condition.append(",");
        condition.append(vo.getValue7());
        condition.append(",");

        condition.append(vo.getLogicCode7());
        condition.append(",");
        individuationQuery = individuationQueryMapper.selectByPrimaryKey(vo.getColumnCode8());
        condition.append(individuationQuery.getColumnCode());
        condition.append(",");
        condition.append(vo.getOperationCode8());
        condition.append(",");
        condition.append(vo.getValue8());


        return condition.toString();
    }

}