package com.hand.hqm.hqm_qc_files.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_qc_files.dto.QcFiles;
import com.hand.hqm.hqm_qc_files.service.IQcFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.List;

    @Controller
    public class QcFilesController extends BaseController{

    @Autowired
    private IQcFilesService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/qc/files/query")
    @ResponseBody
    public ResponseData query(QcFiles dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/qc/files/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<QcFiles> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/qc/files/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<QcFiles> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/hqm/qc/files/deleteDataAndFile")
    @ResponseBody
    public ResponseData deleteDataAndFile(HttpServletRequest request,@RequestBody List<QcFiles> dto){
        service.deleteDataAndFile(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/hqm/qc/files/upload")
	@ResponseBody
	public ResponseData fileUpload(HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			responseData = service.fileUpload(requestCtx, request);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}

		return responseData;
	}
    }