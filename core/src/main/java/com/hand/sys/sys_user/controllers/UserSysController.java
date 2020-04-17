package com.hand.sys.sys_user.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.hr.dto.Employee;
import com.hand.hap.hr.service.IEmployeeService;
import com.hand.hap.system.dto.ResponseData;
import com.hand.sys.sys_user.dto.UserSys;
import com.hand.sys.sys_user.service.IUserSysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

    @Controller
    public class UserSysController extends BaseController{

    @Autowired
    private IUserSysService service;

    @Autowired
    private IEmployeeService employeeService;

    @RequestMapping(value = "/user/sys/query")
    @ResponseBody
    public ResponseData query(UserSys dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/user/sys/srm/query")
    @ResponseBody
    public ResponseData srmQuery(UserSys dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.srmUserTypeSelect(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/user/sys/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<UserSys> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/user/sys/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<UserSys> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/user/sys/queryEmployee")
    @ResponseBody
    public ResponseData queryEmployee(UserSys dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        dto = service.selectByPrimaryKey(requestContext, dto);
        if (dto.getEmployeeId() != null) {
			Employee employee = new Employee();
			employee.setEmployeeId(dto.getEmployeeId().longValue());
			employee = employeeService.selectByPrimaryKey(requestContext, employee);
			return new ResponseData(Arrays.asList(employee));
		}
        return new ResponseData(false);
    }
    }