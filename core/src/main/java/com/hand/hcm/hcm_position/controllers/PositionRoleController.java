package com.hand.hcm.hcm_position.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.hr.mapper.PositionMapper;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_position.dto.PositionRole;
import com.hand.hcm.hcm_position.dto.PositionUser;
import com.hand.hcm.hcm_position.mapper.PositionCategoryMapper;
import com.hand.hcm.hcm_position.mapper.PositionUserMapper;
import com.hand.hcm.hcm_position.service.IPositionRoleService;
import com.hand.hcm.hcm_position.service.IPositionUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 9:30 AM
 */
@Controller
public class PositionRoleController extends BaseController {

    @Autowired
    private IPositionRoleService roleService;

    @Autowired
    private IPositionUserService userService;

    @Autowired
    private PositionUserMapper userMapper;

    @Autowired
    private PositionCategoryMapper categoryMapper;

    @RequestMapping(value = "/hcm/position/role/query")
    @ResponseBody
    public ResponseData query(PositionRole dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                              HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(roleService.query(dto,requestContext,page,pageSize));
    }

    @RequestMapping(value = "/hcm/position/role/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PositionRole> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(roleService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcm/position/role/deleteById")
    @ResponseBody
    public ResponseData deleteById(HttpServletRequest request,Long kid){
        PositionRole pr=new PositionRole();
        pr.setKid(kid);
        roleService.deleteByPrimaryKey(pr);

        //删除职位的同时删掉相应的人员
        PositionUser user = new PositionUser();
        user.setPositionId(kid);
        List<PositionUser> select = userMapper.select(user);
        if(CollectionUtils.isNotEmpty(select)){
            userService.batchDelete(select);
        }

        //删除职位的同时删掉与三级分类的关系
        categoryMapper.deleteByPositionId(kid);

        return new ResponseData();
    }

    @RequestMapping(value = "/hcm/position/role/add")
    @ResponseBody
    public ResponseData add(@RequestBody PositionRole dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(roleService.add(requestCtx, dto));
    }
}
