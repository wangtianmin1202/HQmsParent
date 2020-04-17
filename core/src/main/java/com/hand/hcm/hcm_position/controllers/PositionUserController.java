package com.hand.hcm.hcm_position.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_position.dto.PositionUser;
import com.hand.hcm.hcm_position.service.IPositionUserService;
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
 * @author KOCDZX0 2020/03/11 10:04 AM
 */
@Controller
public class PositionUserController extends BaseController {

    @Autowired
    private IPositionUserService userService;

    @RequestMapping(value = "/hcm/position/user/query")
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
                              PositionUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(userService.query(requestCtx,dto,page,pageSize));
    }

    @RequestMapping(value = "/hcm/position/user/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PositionUser> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(userService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcm/position/user/deleteById")
    @ResponseBody
    public ResponseData deleteById(HttpServletRequest request,Long kid){
        PositionUser pu=new PositionUser();
        pu.setKid(kid);
        userService.deleteByPrimaryKey(pu);
        return new ResponseData();
    }

    @RequestMapping(value = "/hcm/position/user/add")
    @ResponseBody
    public ResponseData add(@RequestBody PositionUser dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(userService.add(requestCtx, dto));
    }

}
