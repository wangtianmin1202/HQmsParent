package com.hand.spc.repository.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hand.hap.mybatis.common.Criteria;
import com.hand.hap.mybatis.common.query.Comparison;
import com.hand.hap.mybatis.common.query.WhereField;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.constants.Constants;
import com.hand.spc.repository.dto.ErrorMessageR;
import com.hand.spc.repository.mapper.ErrorMessageRMapper;
import com.hand.spc.repository.service.IErrorMessageRService;
import com.hand.spc.utils.MtException;
import com.hand.spc.utils.ReplaceUtil;

/**
 *  资源库实现
 *
 * @author linjie.shi@hand-china.com 2019-08-12 10:13:01
 */
@Component
public class ErrorMessageRServiceImpl extends BaseServiceImpl<ErrorMessageR> implements IErrorMessageRService {


    @Autowired
    private ErrorMessageRMapper errorMessageMapper;


    @Override
    public String getErrorMessageWithModule(Long tenantId, String code, String module, String... args) {
        // 先查询redis中是否加载了该消息编码
        // String key = code + "-" + module;
        ErrorMessageR mtErrorMessage = null;

        // 如果redis查询结果为空，则查询MySql数据
        mtErrorMessage = new ErrorMessageR();
        mtErrorMessage.setTenantId(tenantId);
        mtErrorMessage.setModule(module);
        mtErrorMessage.setMessageCode(code);
        mtErrorMessage = errorMessageMapper.selectOne(mtErrorMessage);

        if (mtErrorMessage == null) {
            return code + "";
        }

        String message = mtErrorMessage.getMessage();
        if (StringUtils.isEmpty(message)) {
            return code + "";
        }

        if (ArrayUtils.isNotEmpty(args)) {
            message = ReplaceUtil.replace(message, args);
        }
        return message;
    }


    @Override
    public List<String> messageLimitMessageCodeQuery(Long tenantId, String module, String message) {
        if (StringUtils.isEmpty(module)) {
            throw new MtException("MT_GENERAL_0001", getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                    Constants.GENERAL, "module", "【API:messageLimitMessageCodeQuery】"));
        }
        if (StringUtils.isEmpty(message)) {
            throw new MtException("MT_GENERAL_0001", getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                    Constants.GENERAL, "message", "【API:messageLimitMessageCodeQuery】"));
        }

        ErrorMessageR mtErrorMessage = new ErrorMessageR();
        mtErrorMessage.setTenantId(tenantId);
        mtErrorMessage.setModule(module);
        mtErrorMessage.setMessage(message);
        Criteria criteria = new Criteria(mtErrorMessage);
        List<WhereField> whereFields = new ArrayList<>();
        whereFields.add(new WhereField(ErrorMessageR.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(ErrorMessageR.FIELD_MODULE, Comparison.EQUAL));
        whereFields.add(new WhereField(ErrorMessageR.FIELD_MESSAGE, Comparison.LIKE));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<ErrorMessageR> mtErrorMessages = this.errorMessageMapper.selectOptions(mtErrorMessage, criteria);
        if (CollectionUtils.isEmpty(mtErrorMessages)) {
            return Collections.emptyList();
        }

        return mtErrorMessages.stream().map(ErrorMessageR::getMessageCode).distinct().collect(toList());
    }

    @Override
    public String messageCodeLimitMessageGet(Long tenantId, String module, String messageCode) {
        if (StringUtils.isEmpty(module)) {
            throw new MtException("MT_GENERAL_0001", getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                    Constants.GENERAL, "module", "【API:messageCodeLimitMessageGet】"));
        }
        if (StringUtils.isEmpty(messageCode)) {
            throw new MtException("MT_GENERAL_0001", getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                    Constants.GENERAL, "messageCode", "【API:messageCodeLimitMessageGet】"));
        }

        ErrorMessageR mtErrorMessage = new ErrorMessageR();
        mtErrorMessage.setTenantId(tenantId);
        mtErrorMessage.setModule(module);
        mtErrorMessage.setMessageCode(messageCode);
        mtErrorMessage = errorMessageMapper.selectOne(mtErrorMessage);

        if (mtErrorMessage == null) {
            return "";
        }

        return mtErrorMessage.getMessage();
    }
  
}
