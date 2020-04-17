package com.hand.spc.temppkg.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.repository.dto.Sequence;
import com.hand.spc.repository.mapper.SequenceMapper;
import com.hand.spc.repository.service.ICountStatisticRService;
import com.hand.spc.repository.service.ISequenceRService;
import com.hand.spc.temppkg.dto.Temppkgdto;
import com.hand.spc.temppkg.mapper.TemppkgMapper;
import com.hand.spc.temppkg.service.ITemppkgService;

    @Controller
    public class TemppkgController extends BaseController{

    @Autowired
    private ITemppkgService service;
    
    @Autowired
	private TemppkgMapper temppkgMapper;
    
    @Autowired
	private SequenceMapper  sequenceMapper;
    
    @Autowired
	private ISequenceRService sequenceRepositoty;// 序号表
    
    @Autowired
	private ICountStatisticRService countStatisticRepository;// 计数型统计量


    @RequestMapping(value = "/spc/temp/pkg/query")
    @ResponseBody
    public ResponseData query(Temppkgdto dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        //dto= TemppkgMapper.selectOne(dto);
        Sequence sequence = new Sequence();
        sequence.setSequenceCode("dsssss");
        sequence.setTenantId(1L);
        sequence.setSiteId(1L);
        //sequenceRepositoty.insertSelective(null,sequence);
        sequenceMapper.selectOne(sequence);
        
        //自定义批量插入测试
        /*CountStatistic countStatistic = new CountStatistic();
        countStatistic.setTenantId(1L);
        countStatistic.setSiteId(1L);
        countStatistic.setCountSampleDataId(1L);
        countStatistic.setEntityCode("ssss");
        countStatistic.setEntityVersion("sss");
        countStatistic.setSubgroupNum(1L);
        
        List<CountStatistic> countStatisticListIns = new ArrayList<CountStatistic>();
        countStatisticListIns.add(countStatistic);
        countStatisticListIns.add(countStatistic);
        countStatisticListIns.add(countStatistic);
        countStatisticRepository.batchInsertRows(countStatisticListIns);*/
        
        
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/spc/temp/pkg/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Temppkgdto> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        
        //service.batchUpdate(requestCtx, dto);
        
        service.batchUpdate(null, dto);
        
        /*for (Temppkg t : dto) {
        switch (((BaseDTO) t).get__status()) {
        case "add":
        	TemppkgMapper.insertSelective(t);
            break;
        case "update":
            	TemppkgMapper.updateByPrimaryKeySelective(t);
            	TemppkgMapper.updateByPrimaryKey(t);
            break;
        case "delete":
        	TemppkgMapper.deleteByPrimaryKey(t);
            break;
        default:
            break;
        }
	}*/
	
	
	
	/*for (Temppkg t : dto) {
        switch (((BaseDTO) t).get__status()) {
        case "add":
        	service.insertSelective(null,t);
            break;
        case "update":
        	service.updateByPrimaryKeySelective(null,t);
        	//service.updateByPrimaryKey(null,t);
            break;
        case "delete":
        	service.deleteByPrimaryKey(t);
            break;
        default:
            break;
        }
	}*/
        return new ResponseData();
    }

    @RequestMapping(value = "/spc/temp/pkg/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Temppkgdto> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }