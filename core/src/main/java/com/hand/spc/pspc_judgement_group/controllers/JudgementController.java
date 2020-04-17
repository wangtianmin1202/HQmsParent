package com.hand.spc.pspc_judgement_group.controllers;

import com.hand.spc.pspc_judgement_group.dto.JudgementGroup;
import com.hand.spc.pspc_judgement_group.mapper.JudgementMapper;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_judgement_group.dto.Judgement;
import com.hand.spc.pspc_judgement_group.service.IJudgementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class JudgementController extends BaseController{

    @Autowired
    private IJudgementService service;
    @Autowired
    private JudgementMapper judgementMapper;


    @RequestMapping(value = "/pspc/judgement/query")
    @ResponseBody
    public ResponseData query(Judgement dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<Judgement> judgementList = service.selectData(requestContext, dto, page, pageSize);
        return new ResponseData(judgementList);
    }

    @RequestMapping(value = "/pspc/judgement/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Judgement> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        //校验数据唯一性
        ResponseData responseData = service.validateMustInput(requestCtx,dto);
        if (!responseData.isSuccess()){
            return responseData;
        }
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/judgement/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Judgement> dto){
        ResponseData responseData = new ResponseData();

        for (Judgement map : dto) {
            int count1 = judgementMapper.selectChartDetailCount(map.getJudgementId());
            int count2 = judgementMapper.selectOocRecordCount(map.getJudgementId());
            int count3 = judgementMapper.selectOocCount(map.getJudgementId());
            if(count1>0){
                responseData.setSuccess(false);
                responseData.setMessage("判异规则组已经被控制图明细表引用，不允许删除！");
                return responseData;
            }
            if(count2>0){
                responseData.setSuccess(false);
                responseData.setMessage("判异规则组已经被OOC记录表引用，不允许删除！");
                return responseData;
            }
            if(count3>0){
                responseData.setSuccess(false);
                responseData.setMessage("判异规则组已经被OOC记录表引用，不允许删除！");
                return responseData;
            }
        }

        service.batchDelete(dto);
        return new ResponseData();
    }
    }