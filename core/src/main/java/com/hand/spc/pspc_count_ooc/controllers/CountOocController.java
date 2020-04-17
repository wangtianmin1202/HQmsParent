package com.hand.spc.pspc_count_ooc.controllers;

import com.hand.hap.mybatis.common.Criteria;
import com.hand.spc.pspc_ooc.view.OocReportVO;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_count_ooc.dto.CountOoc;
import com.hand.spc.pspc_count_ooc.service.ICountOocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class CountOocController extends BaseController{

    @Autowired
    private ICountOocService service;


    @RequestMapping(value = "/pspc/count/ooc/query")
    @ResponseBody
    public ResponseData query(CountOoc dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /**
     * @Author han.zhang
     * @Description 查询计数OOC数据几对应的规则编码
     * @Date 16:13 2019/9/3
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/count/ooc/query/count/judge")
    @ResponseBody
    public ResponseData queryCountOoc(CountOoc dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectCountOoc(requestContext,dto));
    }

    @RequestMapping(value = "/pspc/count/ooc/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CountOoc> dto, BindingResult result, HttpServletRequest request){
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
     * @Author han.zhang
     * @Description 查询并更新状态
     * @Date 18:49 2019/9/2
     * @Param [dto, result, request]
     */
    @RequestMapping(value = "/pspc/count/ooc/submit/and/save")
    @ResponseBody
    public ResponseData updateAndSave(@RequestBody List<CountOoc> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.updateAndSave(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/count/ooc/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CountOoc> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     *
     * @Description OOC报表查询
     *
     * @author yuchao.wang
     * @date 2019/8/29 22:10
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/count/ooc/query/report")
    @ResponseBody
    public ResponseData queryOocReport(OocReportVO dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryOocReport(requestContext,dto,page,pageSize));
    }

    /**
     *
     * @Description OOC报表编辑查询
     *
     * @author yuchao.wang
     * @date 2019/8/29 22:10
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/count/ooc/query/report/edit")
    @ResponseBody
    public ResponseData queryOocReportForEdit(@RequestBody OocReportVO dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryOocReport(requestContext,dto,page,pageSize));
    }

    /**
     *
     * @Description OOC报表编辑
     *
     * @author yuchao.wang
     * @date 2019/8/30 19:31
     * @param dto
     * @param result
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/count/ooc/save/report")
    @ResponseBody
    public ResponseData saveForOocReport(@RequestBody CountOoc dto, BindingResult result, HttpServletRequest request){
        IRequest requestCtx = createRequestContext(request);

        //构造更新字段信息
        Criteria criteria = new Criteria();
        criteria.update(CountOoc.FIELD_CLASSIFY_GROUP_ID, CountOoc.FIELD_CLASSIFY_ID, CountOoc.FIELD_OOC_STATUS, CountOoc.FIELD_REMARK);

        service.updateByPrimaryKeyOptions(requestCtx, dto, criteria);
        return new ResponseData();
    }
}