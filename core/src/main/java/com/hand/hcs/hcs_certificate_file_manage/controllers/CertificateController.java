package com.hand.hcs.hcs_certificate_file_manage.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_certificate_file_manage.dto.Certificate;
import com.hand.hcs.hcs_certificate_file_manage.service.ICertificateService;
import com.hand.wfl.util.ActException;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

@Controller
public class CertificateController extends BaseController {

    @Autowired
    private ICertificateService service;


    @RequestMapping(value = "/hcs/certificate/query")
    @ResponseBody
    public ResponseData query(Certificate dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hcs/certificate/querySupplier")
    @ResponseBody
    public List<Certificate> querySupplier(Certificate dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return service.querySupplier(requestContext, dto);
    }

    @RequestMapping(value = "/hcs/certificate/querySecondCategory")
    @ResponseBody
    public List<Certificate> querySecondCategory(@RequestParam(required = false) Long supplierId,@RequestParam(required = false) String firstCategory, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return service.querySecondCategory(requestContext, supplierId,firstCategory);
    }


    @RequestMapping(value = "/hcs/certificate/queryFirstCategory")
    @ResponseBody
    public List<Certificate> querySecondCategory(@RequestParam(required = false) Long supplierId,
                                       HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return service.queryFirstCategory(requestContext, supplierId);
    }

    @RequestMapping(value = "/hcs/certificate/currentQuery")
    @ResponseBody
    public ResponseData currentQuery(Certificate dto) {
        return new ResponseData(service.currentQuery(dto));
    }

    @RequestMapping(value = "/hcs/certificate/queryMuliSupplier")
    @ResponseBody
    public List<Certificate> queryMuliSupplier(Certificate dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return service.queryMuliSupplier(requestContext, dto);
    }

    @RequestMapping(value = "/hcs/certificate/queryMuliItem")
    @ResponseBody
    public List<Certificate> queryMuliItem(Certificate dto, HttpServletRequest request)  {
        IRequest requestContext = createRequestContext(request);
        return service.queryMuliItem(requestContext, dto);
    }

    @RequestMapping(value = "/hcs/certificate/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Certificate> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hcs/certificate/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Certificate> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }


    @RequestMapping(value = "/hcs/certificate/itemStart")
    @ResponseBody
    public ResponseData itemStart(@RequestBody Certificate dto,HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        return service.itemStart(requestCtx,dto);
    }

    @RequestMapping(value = "/hcs/certificate/supplierStart")
    @ResponseBody
    public ResponseData supplierStart(@RequestBody Certificate dto,HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        return service.supplierStart(requestCtx,dto);
    }

    @RequestMapping(value = "/hcs/certificate/validateData")
    @ResponseBody
    public List<String> validateData(Certificate dto) {
        return service.validData(dto);
    }


    @RequestMapping(value = "/hcs/certificate/approve")
    @ResponseBody
    public ResponseData approve(HttpServletRequest request,@RequestBody Certificate dto) throws ActException, ValidationException {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.approve(requestCtx,dto));
    }

    @RequestMapping(value = "/hcs/certificate/rows/get")
	@ResponseBody
	public ResponseData getAllRows(Certificate dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.getAllRows(requestCtx, dto));
	}

    @RequestMapping(value = "/certificate/file/submit/upload1")
    @ResponseBody
    public ResponseData upload(HttpServletRequest request,
                               @Param("certificateId") Long certificateId,
                               @Param("issueDate") String issueDate,
                               @Param("disabledDate") String disabledDate) throws ValidationException, IOException, ActException {
        IRequest requestCtx = createRequestContext(request);
        return service.submitUpload(requestCtx,request,certificateId,issueDate,disabledDate);
    }

    @RequestMapping(value = "/certificate/file/submit/upload")
    @ResponseBody
    public ResponseData fileUpload(HttpServletRequest request) throws ValidationException {
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            responseData = service.upload(requestCtx,request);
        } catch (IllegalStateException | IOException e) {
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping(value = "/hcs/certificate/update")
    @ResponseBody
    public ResponseData update( @RequestBody  Certificate dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.update(requestCtx, dto);
    }

    @RequestMapping(value = "/hcs/certificate/deleteFile")
    @ResponseBody
    public ResponseData deleteFile(HttpServletRequest request,@RequestBody List<Certificate> dto) {

        IRequest requestCtx = createRequestContext(request);
        service.updateAndDelFile(requestCtx, dto);
        return new ResponseData();
    }
}