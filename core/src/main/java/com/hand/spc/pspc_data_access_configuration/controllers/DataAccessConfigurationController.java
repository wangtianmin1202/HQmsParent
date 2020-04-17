package com.hand.spc.pspc_data_access_configuration.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_data_access_configuration.dto.DataAccessConfiguration;
import com.hand.spc.pspc_data_access_configuration.service.IDataAccessConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class DataAccessConfigurationController extends BaseController {

    @Autowired
    private IDataAccessConfigurationService service;


    @RequestMapping(value = "/pspc/data/access/configuration/query")
    @ResponseBody
    public ResponseData query(DataAccessConfiguration dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectData(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/pspc/data/access/configuration/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DataAccessConfiguration> dto, BindingResult result, HttpServletRequest request) {
        //加@RequestBody的原因是后台要用List集合接收，所以前台的contentType类型为: "application/json", 所以相应的后台也要加上@RequestBody。如果只保存一条数据，就不用list，也就不用这个注解了
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        //校验数据（过滤下限不能超过过滤上限，唯一性校验）
        ResponseData responseData = service.validate(requestCtx,dto);
        if (!responseData.isSuccess()){
            return responseData;
        }
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    //手写保存方法
    @RequestMapping(value = "/pspc/data/access/configuration/submit/ui")
    @ResponseBody
    public ResponseData updateAndSubmit(DataAccessConfiguration dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        try {
            return service.updateAndSubmit(requestCtx, dto);
        } catch (Exception ex) {
            ResponseData responseData = new ResponseData();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }
    }

    @RequestMapping(value = "/pspc/data/access/configuration/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<DataAccessConfiguration> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    //手写删除方法
    @RequestMapping(value = "/pspc/data/access/configuration/remove/ui")
    @ResponseBody
    public ResponseData remove(HttpServletRequest request, DataAccessConfiguration dto) {
        try {
            return service.remove(dto);
        } catch (Exception ex) {
            ResponseData responseData = new ResponseData();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }
    }

}