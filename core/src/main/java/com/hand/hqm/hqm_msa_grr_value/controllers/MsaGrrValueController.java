package com.hand.hqm.hqm_msa_grr_value.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_msa_grr_value.dto.MsaGrrValue;
import com.hand.hqm.hqm_msa_grr_value.service.IMsaGrrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class MsaGrrValueController extends BaseController{

    @Autowired
    private IMsaGrrValueService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/msa/grr/value/query")
    @ResponseBody
    public ResponseData query(MsaGrrValue dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/msa/grr/value/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MsaGrrValue> dto, BindingResult result, HttpServletRequest request){
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
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/msa/grr/value/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MsaGrrValue> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 保存
     * @param dto grr数据集
     * @param request 请求
     * @return
     */
    @RequestMapping(value = "/hqm/msa/grr/value/save")
    @ResponseBody
    public ResponseData submit(@RequestBody List<MsaGrrValue> dto, HttpServletRequest request){
    	ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
        	responseData.setRows(service.submit(requestCtx, dto));
        }catch(Exception e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 求f分布
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/msa/grr/value/finvs")
    @ResponseBody
    public Double finvs(HttpServletRequest request,MsaGrrValue dto){
    	IRequest requestCtx = createRequestContext(request);
        return  service.finvs(requestCtx, dto);
    }
    /**
     * 导入
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/msa/grr/value/upload")
    @ResponseBody
    public ResponseData excelImport(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            service.excelImport(request, requestCtx);
            responseData.setMessage("导入成功");
        } catch (Exception e) {
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * grr删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/msa/grr/value/removeByMsaPlanId")
    @ResponseBody
    public ResponseData removeByMsaPlanId(HttpServletRequest request,MsaGrrValue dto){
    	IRequest requestCtx = createRequestContext(request);
    	service.removeByMsaPlanId(requestCtx,dto);
        return new ResponseData();
    }
    }