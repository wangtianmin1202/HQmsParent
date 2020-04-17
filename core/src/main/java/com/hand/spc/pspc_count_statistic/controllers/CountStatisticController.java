package com.hand.spc.pspc_count_statistic.controllers;

import com.hand.hap.mybatis.common.Criteria;
import com.hand.hap.mybatis.common.query.Comparison;
import com.hand.hap.mybatis.common.query.WhereField;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.service.IEntityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_count_statistic.dto.CountStatistic;
import com.hand.spc.pspc_count_statistic.service.ICountStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CountStatisticController extends BaseController{

    @Autowired
    private ICountStatisticService service;

    @Autowired
    private IEntityService iEntityService;

    @RequestMapping(value = "/pspc/count/statistic/query")
    @ResponseBody
    public ResponseData query(CountStatistic dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/count/statistic/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<CountStatistic> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/pspc/count/statistic/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<CountStatistic> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/pspc/count/statistic/query/report")
    @ResponseBody
    public ResponseData queryReport(@RequestBody CountStatistic dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        /**
         * @Description //TODO 柏拉图展示查询
         * @Author leizhe
         * @Date 15:58 2019/8/19
         * @Param
         * @return com.hand.hap.system.dto.ResponseData
         **/
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = null;
        try {
            responseData = service.queryReport(requestContext, dto, page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseData;
    }


    @RequestMapping(value = "/pspc/count/statistic/entry/query/ui")
    @ResponseBody
    public ResponseData query(Entity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {

        /**
         * @Description //TODO   实体控制图 LOV
         * @Author leizhe
         * @Date 15:59 2019/8/20
         * @Param
         * @return com.hand.hap.system.dto.ResponseData
         **/

        IRequest requestContext = createRequestContext(request);
        //模糊查询
        Criteria criteria = new Criteria(dto);
        criteria.where(new WhereField(Entity.FIELD_ENTITY_CODE, Comparison.LIKE)
        );
        List<Entity> entities = iEntityService.selectOptions(requestContext, dto, criteria, page, pageSize);
        return new ResponseData(entities);
    }



    @RequestMapping(value = "/pspc/count/statistic/exportExcel/ui")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,
                            CountStatistic dto,String img){
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = service.queryReport(requestContext, dto, 1, 1);
        List<CountStatistic> rows = (List<CountStatistic>) responseData.getRows();
        service.exportExcel(dto,createRequestContext(request),request,response,rows,img);
    }










}