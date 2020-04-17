package com.hand.spc.pspc_sample_data.controllers;

import com.hand.spc.pspc_sample_data.view.SampleDataQueryVO;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_sample_data.dto.SampleData;
import com.hand.spc.pspc_sample_data.service.ISampleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleDataController extends BaseController{

    @Autowired
    private ISampleDataService service;


    @RequestMapping(value = "/pspc/sample/data/query")
    @ResponseBody
    public ResponseData query(SampleDataQueryVO dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);
            return new ResponseData(service.queryBaseData(requestContext,dto,page,pageSize));
        }

    @RequestMapping(value = "/pspc/sample/data/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleDataQueryVO> dto, HttpServletRequest request){
        IRequest requestCtx = createRequestContext(request);
        return service.saveBaseData(requestCtx, dto);
    }

    @RequestMapping(value = "/pspc/sample/data/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SampleData> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

        @RequestMapping(value = "/pspc/sample/dataimportExcel")
        @ResponseBody
        public ResponseData importFirstExcel(HttpServletRequest request,@RequestParam("ceParameterId") Long ceParameterId,
                                             @RequestParam("ceGroupId") Long ceGroupId,
                                             @RequestParam("attachmentGroupId") Long attachmentGroupId){
            IRequest requestContext = createRequestContext(request);
            ResponseData responseData = new ResponseData();
            try {
                responseData = service.importExcel(requestContext, request,ceGroupId,ceParameterId,attachmentGroupId);
            }catch (Exception e){
                e.printStackTrace();
                responseData.setMessage(e.getMessage());
                responseData.setSuccess(false);
            }
            return responseData;
        }
    }