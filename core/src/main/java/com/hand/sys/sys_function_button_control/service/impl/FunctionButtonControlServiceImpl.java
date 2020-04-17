package com.hand.sys.sys_function_button_control.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.function.dto.Function;
import com.hand.hap.function.service.impl.FunctionServiceImpl;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.sys.sys_function_button_control.dto.FunctionButtonControl;
import com.hand.sys.sys_function_button_control.mapper.FunctionButtonControlMapper;
import com.hand.sys.sys_function_button_control.service.IFunctionButtonControlService;
import org.apache.fop.fo.expr.FunctionBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FunctionButtonControlServiceImpl extends BaseServiceImpl<FunctionButtonControl> implements IFunctionButtonControlService {

    @Autowired
    FunctionButtonControlMapper functionButtonControlMapper;

    @Override
    public List<FunctionButtonControl> getButtonById(IRequest iRequest,Long id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return functionButtonControlMapper.getButtonById(id);
    }

    @Override
    public List<FunctionButtonControl> getButtonByFunctionCode(String codeUrl) {
        String code=null;
        try {
            String str1 = codeUrl.substring(0, codeUrl.indexOf("functionCode="));
            code = codeUrl.substring(str1.length() + 13, codeUrl.length());
        }
        catch (Exception e){
            code=null;
        }
        return functionButtonControlMapper.getButtonByFunctionCode(code);
    }
}
