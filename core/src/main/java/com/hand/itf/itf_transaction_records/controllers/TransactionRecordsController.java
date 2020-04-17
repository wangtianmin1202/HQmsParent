package com.hand.itf.itf_transaction_records.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.itf.itf_transaction_records.dto.TransactionRecords;
import com.hand.itf.itf_transaction_records.service.ITransactionRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

@Controller
public class TransactionRecordsController extends BaseController {

    @Autowired
    private ITransactionRecordsService service;


    @RequestMapping(value = "/itf/transaction/records/query")
    @ResponseBody
    public ResponseData query(TransactionRecords dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<TransactionRecords> transactionRecordsList = service.queryTransactionRecords(requestContext, dto, page, pageSize);
        return new ResponseData(transactionRecordsList);
    }

    @RequestMapping(value = "/itf/transaction/records/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<TransactionRecords> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/itf/transaction/records/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<TransactionRecords> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/itf/transaction/records/resend")
    @ResponseBody
    public ResponseData resend(HttpServletRequest request, float id,@RequestParam(defaultValue = DEFAULT_PAGE) int page,@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestContext = createRequestContext(request);
        service.transactionResend(requestContext,id,page,pageSize);
        return new ResponseData();
    }

    @RequestMapping(value = "/itf/transaction/records/detail")
    @ResponseBody
    public String showDetail(HttpServletRequest request, float id) {
        IRequest requestContext = createRequestContext(request);
        String res=service.transactionDetail(requestContext,id);
        return res;
    }
}