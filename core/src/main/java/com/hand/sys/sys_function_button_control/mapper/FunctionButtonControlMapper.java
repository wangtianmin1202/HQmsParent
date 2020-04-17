package com.hand.sys.sys_function_button_control.mapper;

import com.hand.hap.mybatis.common.BaseMapper;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.sys.sys_function_button_control.dto.FunctionButtonControl;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FunctionButtonControlMapper extends Mapper<FunctionButtonControl> {

    public List<FunctionButtonControl> getButtonById(@Param("id") Long id);

    public List<FunctionButtonControl> getButtonByFunctionCode(@Param("code") String code);
}
