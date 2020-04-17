package com.hand.hap.account.controllers;

import com.hand.hap.account.dto.User;
import com.hand.hap.account.exception.UserException;
import com.hand.hap.account.service.IRoleService;
import com.hand.hap.account.service.IUserInfoService;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.exception.BaseException;
import com.hand.hap.function.dto.ResourceItemAssign;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 用户控制器.
 *
 * @author njq.niu@hand-china.com
 * @date 2016/1/29
 *
 */
@RestController
@RequestMapping(value = { "/sys/user", "/api/sys/user" })
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IUserInfoService userInfoService;

	/**
	 * 修改用户权限组件分配.
	 * 
	 * @param request
	 * @param resourceItemAssignList
	 * @param userId
	 * @param functionId
	 * @return
	 */
	@PostMapping(value = "/submitResourceItems")
	public ResponseData submitResourceItems(HttpServletRequest request,
			@RequestBody List<ResourceItemAssign> resourceItemAssignList, @RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long functionId) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(
				userService.updateResourceItemAssign(requestContext, resourceItemAssignList, userId, functionId));
	}

	/**
	 * 根据用户ID和功能ID删除权限组件.
	 * 
	 * @param userId     用户id
	 * @param functionId 功能id
	 * @return
	 */
	@PostMapping(value = "/deleteResourceItems")
	public ResponseData removeResourceItems(@RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long functionId) {
		userService.deleteResourceItems(userId, functionId);
		return new ResponseData();
	}

	/**
	 * 查询用户权限组件菜单
	 * 
	 * @param request
	 * @param userId
	 * @param functionId
	 * @return
	 */
	@PostMapping(value = "/queryResourceItems")
	public ResponseData queryResourceItems(HttpServletRequest request, @RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long functionId) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(userService.queryResourceItems(requestContext, userId, functionId));
	}

	/**
	 * 查询用户功能菜单.
	 * 
	 * @param request
	 * @param userId
	 * @return
	 */
	@PostMapping(value = "/queryFunction")
	public ResponseData queryFunction(HttpServletRequest request, @RequestParam(required = false) Long userId) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(userService.queryFunction(requestContext, userId));
	}

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@PostMapping(value = "/query")
	public ResponseData queryUsers(HttpServletRequest request, @ModelAttribute User user,
			@RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize) {
		IRequest iRequest = createRequestContext(request);
		return new ResponseData(userService.selectUsers(iRequest, user, page, pagesize));
	}

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@PostMapping(value = "/submit")
	public ResponseData submitUsers(@RequestBody List<User> users, BindingResult result, HttpServletRequest request)
			throws BaseException {
		getValidator().validate(users, result);
		if (result.hasErrors()) {
			ResponseData rd = new ResponseData(false);
			rd.setMessage(getErrorMessage(result, request));
			return rd;
		}
		IRequest requestCtx = createRequestContext(request);
		// userService.updateUserBasic(users);
		users.forEach(p -> {
			User user = new User();
			user.setUserId(p.getUserId());
			user.setUserName(p.getUserName());
			user.setUserType(p.getUserType());
			user.setStartActiveDate(p.getStartActiveDate());
			user.setEndActiveDate(p.getEndActiveDate());
			userService.updateByPrimaryKeySelective(requestCtx, user);
		});
		return new ResponseData(userService.batchUpdate(createRequestContext(request), users));
		// return new ResponseData();
	}

	/**
	 * 更新用户信息
	 * 
	 * @param request 请求
	 * @param user    用户信息
	 * @return
	 * @throws BaseException
	 */
	@PostMapping(value = "/update")
	public ResponseData updateUserInfo(HttpServletRequest request, @RequestBody User user) throws BaseException {
		IRequest iRequest = createRequestContext(request);
		userInfoService.update(iRequest, user);
		return new ResponseData(Collections.singletonList(user));
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@PostMapping(value = "/remove")
	public ResponseData remove(@RequestBody List<User> users) throws BaseException {
		userService.batchDelete(users);
		return new ResponseData(users);
	}

	/**
	 * 查询用户所有启用状态的角色.
	 * 
	 * @param request
	 * @param userId
	 * @return
	 */
	@PostMapping(value = "/{userId}/roles")
	public ResponseData queryUserAndRoles(HttpServletRequest request, @PathVariable Long userId) {
		IRequest iRequest = createRequestContext(request);
		ResponseData rd = new ResponseData();
		User user = new User();
		user.setUserId(userId);
		rd.setRows(roleService.selectRolesByUser(iRequest, user));
		return rd;
	}

	/**
	 * 用户管理修改密码.
	 * 
	 * @param request
	 * @param password
	 * @param passwordAgain
	 * @param userId
	 * @return
	 * @throws UserException
	 */
	@PostMapping(value = "/password/reset")
	public ResponseData updatePassword(HttpServletRequest request, String password, String passwordAgain, Long userId)
			throws UserException {
		IRequest iRequest = createRequestContext(request);
		User user = new User();
		user.setUserId(userId);
		user.setPassword(password);
		userService.resetPassword(iRequest, user, passwordAgain);
		return new ResponseData(true);
	}

	/**
	 * 修改当前用户密码
	 * 
	 * @param request
	 * @param oldPwd
	 * @param newPwd
	 * @param newPwdAgain
	 * @return
	 * @throws UserException
	 */
	@PostMapping(value = "/password/update")
	public ResponseData updatePassword(HttpServletRequest request, String oldPwd, String newPwd, String newPwdAgain)
			throws UserException {
		IRequest iRequest = createRequestContext(request);
		userService.updateOwnerPassword(iRequest, oldPwd, newPwd, newPwdAgain);
		return new ResponseData(true);
	}

	/**
	 * 获取用户角色合并标识
	 * 
	 * @param request
	 * @param users
	 * @return
	 * @throws BaseException
	 */
	@PostMapping(value = "/queryUserRoleMerge")
	public User queryUserRoleMerge(HttpServletRequest request, User users) throws BaseException {
		return userService.queryUserRoleMerge();
	}
}
