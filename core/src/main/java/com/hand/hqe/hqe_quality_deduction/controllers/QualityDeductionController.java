package com.hand.hqe.hqe_quality_deduction.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqe.hqe_quality_deduction.dto.QualityDeduction;
import com.hand.hqe.hqe_quality_deduction.service.IQualityDeductionService;
import com.hand.hqm.hqm_qc_files.dto.QcFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

    @Controller
    public class QualityDeductionController extends BaseController{

    @Autowired
    private IQualityDeductionService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqe/quality/deduction/query")
    @ResponseBody
    public ResponseData query(QualityDeduction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqe/quality/deduction/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<QualityDeduction> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
//        for(QualityDeduction qualityDeduction : dto) {
//        	if("add".equals(qualityDeduction.get__status())) {
//        		//获取扣款单号
//        		int num = service.queryMaxNum(qualityDeduction);
//        		num++;
//        		// 序列号
//        		String numStr = String.format("%03d", num);
//        		// 年月日：yyyy
//        		SimpleDateFormat simple = new SimpleDateFormat("yyMM");
//        		String now = simple.format(new Date());
//        		String qualityDeductionNum = qualityDeduction.getSupplierCode() + now + numStr;
//        		
//        		qualityDeduction.setQualityDeductionNum(qualityDeductionNum);
//        	}
//        }
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqe/quality/deduction/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<QualityDeduction> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     *  生成结算单据
     * @param request 请求
     * @param dto 质量扣款单据集合
     * @return 响应体
     */
    @RequestMapping(value = "/hqe/quality/deduction/createSettlement")
    @ResponseBody
    public ResponseData createSettlement(HttpServletRequest request,@RequestBody List<QualityDeduction> dto){
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
        	responseData.setRows(service.createSettlement(requestCtx,dto));
        }catch(Exception e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     *  质量扣款单据录入： 取消
     * @param request 请求
     * @param dto 质量扣款单据集合
     * @return 响应结果
     */
    @RequestMapping(value = "/hqe/quality/deduction/cancel")
    @ResponseBody
    public ResponseData cancel(HttpServletRequest request,@RequestBody List<QualityDeduction> dto){
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
        	responseData.setRows(service.cancel(requestCtx,dto));
        }catch(Exception e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 上传
     * @param request 请求
     * @return
     */
    @RequestMapping(value = "/hqe/quality/deduction/upload")
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
    /**
     * 删除文件
     * @param request 请求
     * @param dto 质量扣款单据集合
     * @return
     */
    @RequestMapping(value = "/hqe/quality/updaeDataAndDelFile")
    @ResponseBody
    public ResponseData updaeDataAndDelFile(HttpServletRequest request,@RequestBody List<QualityDeduction> dto){
    	IRequest requestCtx = createRequestContext(request);
    	service.updaeDataAndDelFile(requestCtx, dto);
        return new ResponseData();
    }
    }