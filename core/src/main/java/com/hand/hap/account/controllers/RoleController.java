package com.hand.hap.account.controllers;

import com.hand.hap.account.dto.Role;
import com.hand.hap.account.dto.RoleExt;
import com.hand.hap.account.service.IRoleService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.exception.BaseException;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色控制器.
 *
 * @author shengyang.zhou@hand-china.com
 * @date 2016/6/9
 */
@RestController
@RequestMapping(value = {"/sys/role", "/api/sys/role"})
public class RoleController extends BaseController {

    @Autowired
    @Qualifier("roleServiceImpl")
    private IRoleService roleService;
    /**
     * 查询用户角色表中不存在的角色
     * @param request 请求
     * @param roleExt 角色扩展信息
     * @param page 页码
     * @param pagesize 页大小
     * @return 查询结果解
     */
    @RequestMapping(value = "/queryRoleNotUserRole", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseData queryRoleNotUserRoles(HttpServletRequest request, RoleExt roleExt,
                                              @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(roleService.selectRoleNotUserRoles(requestContext, roleExt, page, pagesize));
    }
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseData queryRoles(Role role, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(roleService.selectRoles(requestContext, role, page, pagesize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @PostMapping(value = "/submit")
    public ResponseData submitRole(@RequestBody List<Role> roles, BindingResult result,
                                   HttpServletRequest request) throws BaseException {
        getValidator().validate(roles, result);
        if (result.hasErrors()) {
            ResponseData rd = new ResponseData(false);
            rd.setMessage(getErrorMessage(result, request));
            return rd;
        }
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(roleService.batchUpdate(requestContext, roles));
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @PostMapping(value = "/remove")
    public ResponseData removeRole(HttpServletRequest request, @RequestBody List<Role> roles) throws BaseException {
        roleService.batchDelete(roles);
        return new ResponseData();
    }
    /**
     * 查询角色
     * @param role 角色
     * @param page 页码
     * @param pagesize 页大小
     * @param request 请求
     * @return 查询结果集
     */
    @RequestMapping(value = "/select", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseData select(Role role, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(roleService.select(requestContext, role, page, pagesize));
    }
}
