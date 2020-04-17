package com.hand.dimension.hqm_dimension_improving_actions_ev.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_improving_actions_ev.dto.DimensionImprovingActionsEv;
import com.hand.dimension.hqm_dimension_improving_actions_ev.service.IDimensionImprovingActionsEvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class DimensionImprovingActionsEvController extends BaseController{

    @Autowired
    private IDimensionImprovingActionsEvService service;


    /**
     * 
     * @description 基础查询
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/ev/query")
    @ResponseBody
    public ResponseData query(DimensionImprovingActionsEv dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     * 
     * @description 更新数据的提交
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/ev/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<DimensionImprovingActionsEv> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 
     * @description 删除
     * @author tianmin.wang
     * @date 2019年11月21日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/8d/improving/actions/ev/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<DimensionImprovingActionsEv> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }