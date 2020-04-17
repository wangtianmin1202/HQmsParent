 package com.hand.hqm.hqm_sample_size_code_letter.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sample_size_code_letter.dto.SampleSizeCodeLetter;
import com.hand.hqm.hqm_sample_size_code_letter.service.ISampleSizeCodeLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;
/*
 * created by tianmin.wang on 2019/7/14.
 */
@Controller
public class SampleSizeCodeLetterController extends BaseController {

    @Autowired
    private ISampleSizeCodeLetterService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/size/code/letter/query")
    @ResponseBody
    public ResponseData query(SampleSizeCodeLetter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/size/code/letter/myselect")
    @ResponseBody
    public ResponseData myquery(SampleSizeCodeLetter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext, dto, page, pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/sample/size/code/letter/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleSizeCodeLetter> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        List<SampleSizeCodeLetter> list = service.select(null, new SampleSizeCodeLetter(), 1, 100);
        for (SampleSizeCodeLetter t : dto) {
            if (!(t.getLotSizeFrom() < t.getLotSizeTo())) {
                ResponseData responseData = new ResponseData(false);
                responseData.setMessage("上限" + t.getLotSizeFrom() + "下限" + t.getLotSizeTo() + "已存在");
                return responseData;
            }
            for (SampleSizeCodeLetter only : list) {
                if((t.getLotSizeFrom()>=only.getLotSizeFrom()&&t.getLotSizeFrom()<=only.getLotSizeTo())||
                		(t.getLotSizeTo()>=only.getLotSizeFrom()&&t.getLotSizeTo()<=only.getLotSizeTo())){
                    ResponseData responseData = new ResponseData(false);
                    responseData.setMessage("上限" + t.getLotSizeFrom() + "下限" + t.getLotSizeTo() + "已存在于:上限"+only.getLotSizeFrom()+"下限"+only.getLotSizeTo());
                    return responseData;
                }
            }
        }

        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.LevelUpdate(requestCtx, dto));
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/sample/size/code/letter/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<SampleSizeCodeLetter> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/hqm/sample/size/code/letter/standard")
    @ResponseBody
    public ResponseData lookUpStandard(HttpServletRequest request) {
    	List<SampleSizeCodeLetter> list = service.selectSandardCode();
        return new ResponseData(list);
    }
    @RequestMapping(value = "/hqm/sample/size/code/letter/codelevel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseData lookUpCodeLevel(HttpServletRequest request,@RequestParam String value ) {
    	SampleSizeCodeLetter search = new SampleSizeCodeLetter();
    	search.setValue(value);
    	List<SampleSizeCodeLetter> list = service.selectCodeLevel(search);
        return new ResponseData(list);
    }
}