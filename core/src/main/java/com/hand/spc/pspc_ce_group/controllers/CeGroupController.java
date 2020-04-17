package com.hand.spc.pspc_ce_group.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_ce_group.dto.CeGroup;
import com.hand.spc.pspc_ce_group.service.ICeGroupService;
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
public class CeGroupController extends BaseController {

    @Autowired
    private ICeGroupService service;


    @RequestMapping(value = "/pspc/ce/group/query")
    @ResponseBody
    public ResponseData query(CeGroup dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<CeGroup> ceGroupList = service.selectCeGroup(requestContext,dto,page,pageSize);
        return new ResponseData(ceGroupList);
    }

    @RequestMapping(value = "/pspc/ce/group/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CeGroup> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }

        //如果是控制要素组功能前台更新
        if(dto != null && !dto.isEmpty()){
            for(int i=0; i<dto.size(); i++){
                if(dto.get(i).isFrontUpdate()){
                    dto.get(i).set__status("update");
                }
            }
        }

        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/ce/group/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<CeGroup> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     *
     * @Description 控制要素组副本保存
     *
     * @author yuchao.wang
     * @date 2019/8/9 16:08
     * @param dto
     * @param result
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/ce/group/copy/save")
    @ResponseBody
    public ResponseData copySave(@RequestBody CeGroup dto, BindingResult result, HttpServletRequest request){
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();

        try {
            responseData = service.copySave(requestCtx, dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }


    /**
     *
     * @Description 删除控制要素组及其关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 23:06
     * @param request
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/ce/group/delete")
    @ResponseBody
    public ResponseData deleteCeGroupAndRelationship(HttpServletRequest request, @RequestBody CeGroup dto){
        ResponseData responseData = new ResponseData();

        try {
            responseData = service.deleteCeGroupAndRelationship(dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
}