package com.hand.spc.pspc_ce_relationship.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_ce_relationship.dto.CeRelationship;
import com.hand.spc.pspc_ce_relationship.service.ICeRelationshipService;
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
public class CeRelationshipController extends BaseController {

    @Autowired
    private ICeRelationshipService service;


    @RequestMapping(value = "/pspc/ce/relationship/query")
    @ResponseBody
    public ResponseData query(CeRelationship dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/ce/relationship/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CeRelationship> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.updateRelationship(requestCtx, dto);
    }

    @RequestMapping(value = "/pspc/ce/relationship/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<CeRelationship> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     *
     * @Description 删除控制要素与控制要素组关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 23:06
     * @param request
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/ce/relationship/delete")
    @ResponseBody
    public ResponseData deleteRelationship(HttpServletRequest request, @RequestBody List<CeRelationship> dto){
        ResponseData responseData = new ResponseData();

        try {
            responseData = service.deleteRelationship(dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
}