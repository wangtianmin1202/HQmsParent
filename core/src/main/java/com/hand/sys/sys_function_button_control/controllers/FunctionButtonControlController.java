package com.hand.sys.sys_function_button_control.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.function.dto.Function;
import com.hand.hap.function.service.IFunctionService;
import com.hand.hap.function.service.impl.FunctionServiceImpl;
import com.hand.hap.system.controllers.BaseController;
import com.hand.spc.utils.ResponseData;
import com.hand.sys.sys_function_button_control.dto.FunctionButtonControl;
import com.hand.sys.sys_function_button_control.service.IFunctionButtonControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

@Controller
public class FunctionButtonControlController extends BaseController {

    @Autowired
    private IFunctionButtonControlService service;

    @Autowired
    IFunctionService functionService;

    private Long functionId;

    @RequestMapping(value = "/function/button/control/query")
    @ResponseBody
    public ResponseData query(FunctionButtonControl dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }


    @RequestMapping(value = "/function/button/control/querybyid")
    @ResponseBody
    public ResponseData queryById(Long id, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        functionId=id;
        ResponseData responseData=new ResponseData();
        List<FunctionButtonControl> functionButtonControlList=service.getButtonById(requestContext,id,page,pageSize);
        responseData.setTotal(functionButtonControlList.size());
        responseData.setRows(functionButtonControlList);
        responseData.setSuccess(true);
        return responseData;
    }

    @RequestMapping(value = "/function/button/control/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FunctionButtonControl> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        for (FunctionButtonControl fbc :dto
             ) {
            fbc.setFunctionId(functionId);
        }
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        Function function=new Function();
        function.setFunctionId(dto.get(0).getFunctionId());
        Function selectFunction=functionService.selectByPrimaryKey(requestCtx,function);

        if(selectFunction !=null) {
            for (FunctionButtonControl fbc : dto
            ) {
                fbc.setFunctionCode(selectFunction.getFunctionCode());
                fbc.setFunctionName(selectFunction.getFunctionName());
            }
        }
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/function/button/control/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<FunctionButtonControl> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/function/button/control/getButtonByFunctionCode")
    @ResponseBody
    public ResponseData getButtonByFunctionCode(String codeUrl){
        return new ResponseData(service.getButtonByFunctionCode(codeUrl));
    }

}
