package com.hand.spc.pspc_ce_parameter.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_ce_parameter.dto.CeParameter;
import com.hand.spc.pspc_ce_parameter.service.ICeParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

@Controller
public class CeParameterController extends BaseController{

    @Autowired
    private ICeParameterService service;


    @RequestMapping(value = "/pspc/ce/parameter/query")
    @ResponseBody
    public ResponseData query(CeParameter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<CeParameter> ceParameterList = service.selectCeParameterSelective(requestContext,dto,page,pageSize);
        return new ResponseData(ceParameterList);
    }

    /**
     *
     * @Description 根据控制要素组ID查询相关控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/9 14:26
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/ce/parameter/query/by/groupId")
    @ResponseBody
    public ResponseData queryByCroupId(CeParameter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<CeParameter> ceParameterList = service.selectCeParameterByGroupId(requestContext, dto, page, pageSize);
        return new ResponseData(ceParameterList);
    }

    /**
     * @Author han.zhang
     * @Description 分类组下的控制要素查询
     * @Date 14:02 2019/8/7
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/ce/parameter/query/ce/parameter")
    @ResponseBody
    public ResponseData queryCeParameter(CeParameter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try{
            responseData = service.selectCeParameter(requestContext,dto,page,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @RequestMapping(value = "/pspc/ce/parameter/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CeParameter> dto, BindingResult result, HttpServletRequest request){
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
     * @Description 控制要素编辑
     *
     * @author yuchao.wang
     * @date 2019/8/9 14:23
     * @param dto
     * @param result
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/ce/parameter/update/by/id")
    @ResponseBody
    public ResponseData updateById(@RequestBody CeParameter dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }

        //判断是新增还是更新
        /*for(int i=0; i< dto.size(); i++) {
            if(dto.get(i) != null)
                if(dto.get(i).getCeParameterId() != null)
                    dto.get(i).set__status("update");
                else
                    dto.get(i).set__status("add");
        }*/
        dto.set__status("update");

        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData(Collections.singletonList(service.updateByPrimaryKeySelective(requestCtx, dto)));
        return responseData;
    }

    @RequestMapping(value = "/pspc/ce/parameter/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CeParameter> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @Author han.zhang
     * @Description 控制要素新增或者更新
     * @Date 20:35 2019/8/7
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/classify/add/update/ce/parameter")
    @ResponseBody
    public ResponseData saveAndUpdateCeParameter(CeParameter dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData = service.saveAndUpdateClassify(requestContext, dto);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.toString());
        }
        return responseData;
    }

    /**
     *
     * @Description 删除控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/10 15:10
     * @param request
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/ce/parameter/delete")
    @ResponseBody
    public ResponseData deleteCeParameter(HttpServletRequest request,@RequestBody List<CeParameter> dto){
        ResponseData responseData = new ResponseData();

        try {
            responseData = service.deleteCeParameter(dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
}