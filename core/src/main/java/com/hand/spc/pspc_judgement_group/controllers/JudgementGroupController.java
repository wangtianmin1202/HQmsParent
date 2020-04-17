package com.hand.spc.pspc_judgement_group.controllers;

import com.hand.spc.pspc_judgement_group.dto.Judgement;
import com.hand.spc.pspc_judgement_group.mapper.JudgementGroupMapper;
import com.hand.spc.pspc_judgement_group.service.IJudgementService;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_judgement_group.dto.JudgementGroup;
import com.hand.spc.pspc_judgement_group.service.IJudgementGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class JudgementGroupController extends BaseController {

    @Autowired
    private IJudgementGroupService service;
    @Autowired
    private IJudgementService iJudgementService;
    @Autowired
    private JudgementGroupMapper judgementGroupMapper;


    @RequestMapping(value = "/pspc/judgement/group/query")
    @ResponseBody
    public ResponseData query(JudgementGroup dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.MySelect(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/pspc/judgement/group/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<JudgementGroup> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        //根据传进来的拷贝id去保存对应行表信息
        try {
             return service.copyData(requestCtx,dto);
        } catch (Exception ex) {
            ResponseData responseData=new ResponseData();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
            return responseData;
        }
    }

    @RequestMapping(value = "/pspc/judgement/group/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<JudgementGroup> dto) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();

        //删除头的时候删除对应的行
        for (JudgementGroup map : dto) {
            if("N".equals(map.getStandard())){
                responseData.setSuccess(false);
                responseData.setMessage("标准判异规则不允许删除！");
                return responseData;
            }

            //若判异规则组已经被其它表引用，则不可删除，通过判异规则组ID关联相关表
          int count1 = judgementGroupMapper.selectEntityCount(map.getJudgementGroupId());
          int count2 = judgementGroupMapper.selectChartDetailCount(map.getJudgementGroupId());
          int count3 = judgementGroupMapper.selectOocRecordCount(map.getJudgementGroupId());
          int count4 = judgementGroupMapper.selectOocCount(map.getJudgementGroupId());
          if(count1>0){
              responseData.setSuccess(false);
              responseData.setMessage("判异规则组已经被SPC实体控制图引用，不允许删除！");
              return responseData;
          }
          if(count2>0){
                responseData.setSuccess(false);
                responseData.setMessage("判异规则组已经被控制图明细表引用，不允许删除！");
                return responseData;
          }
          if(count3>0){
                responseData.setSuccess(false);
                responseData.setMessage("判异规则组已经被OOC记录表引用，不允许删除！");
                return responseData;
          }
            if(count4>0){
                responseData.setSuccess(false);
                responseData.setMessage("判异规则组已经被OOC记录表引用，不允许删除！");
                return responseData;
            }

            Judgement judgement = new Judgement();
            judgement.setJudgementGroupId(map.getJudgementGroupId());
            List<Judgement> judgementList = iJudgementService.select(requestCtx, judgement, 1, 9999);
            for (Judgement l : judgementList) {
                iJudgementService.deleteByPrimaryKey(l);//对你注入的字段全部更新,为Null不忽略更新
            }
        }

        service.batchDelete(dto);

        responseData.setSuccess(true);
        responseData.setMessage("删除成功！");
        return responseData;

    }
}