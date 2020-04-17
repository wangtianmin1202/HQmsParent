package com.hand.spc.pspc_ooc.controllers;

import com.hand.hap.mybatis.common.Criteria;
import com.hand.spc.pspc_ooc.view.OocReportVO;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_ooc.dto.Ooc;
import com.hand.spc.pspc_ooc.service.IOocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class OocController extends BaseController{

    @Autowired
    private IOocService service;


    @RequestMapping(value = "/pspc/ooc/query")
    @ResponseBody
    public ResponseData query(Ooc dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/ooc/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Ooc> dto, BindingResult result, HttpServletRequest request){
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
     * @Author chengpunan
     * @Description 保存并修改状态
     * @Date 下午10:18 2019/8/30
     * @Param [dto, result, request]
     **/
    @RequestMapping(value = "/pspc/ooc/submit/change/status")
    @ResponseBody
    public ResponseData updateAndChangeStatus(@RequestBody List<Ooc> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.saveAndChangeStatus(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/ooc/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Ooc> dto){
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
    @RequestMapping(value = "/pspc/ooc/query/report")
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
    @RequestMapping(value = "/pspc/ooc/query/report/edit")
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
    @RequestMapping(value = "/pspc/ooc/save/report")
    @ResponseBody
    public ResponseData saveForOocReport(@RequestBody Ooc dto, BindingResult result, HttpServletRequest request){
        IRequest requestCtx = createRequestContext(request);

        //构造更新字段信息
        Criteria criteria = new Criteria();
        criteria.update(Ooc.FIELD_CLASSIFY_GROUP_ID, Ooc.FIELD_CLASSIFY_ID, Ooc.FIELD_OOC_STATUS, Ooc.FIELD_REMARK);

        service.updateByPrimaryKeyOptions(requestCtx, dto, criteria);
        return new ResponseData();
    }

    /**
     * @Author han.zhang
     * @Description 生成8d报告
     * @Date 上午9:18 2019/10/17
     * @Param [dto, page, pageSize, request]
     **/
    @RequestMapping(value = "/pspc/ooc/create/eightd/report")
    @ResponseBody
    public ResponseData queryOocReport(@RequestBody List<OocReportVO> dtos, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData = service.createDReport(requestContext,dtos);
            responseData.setMessage("生成成功");
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
}