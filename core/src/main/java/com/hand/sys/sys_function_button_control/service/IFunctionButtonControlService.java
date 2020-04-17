package com.hand.sys.sys_function_button_control.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_item.service.IItemService;
import com.hand.sys.sys_function_button_control.dto.FunctionButtonControl;

import java.util.List;

public interface IFunctionButtonControlService extends IBaseService<FunctionButtonControl>, ProxySelf<IFunctionButtonControlService> {

    /**
     * 根据function id查询按钮列
     * @param id
     * @return List集合
     */
    public List<FunctionButtonControl> getButtonById(IRequest iRequest,Long id, int pageNum, int pageSize);

    /**
     * 根据function code查询按钮列
     * @param codeUrl 请求URL
     * @return List集合
     */
    public List<FunctionButtonControl> getButtonByFunctionCode(String codeUrl);

}
