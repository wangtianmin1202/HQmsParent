package com.hand.utils.recording_changes_basic_data.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.utils.recording_changes_basic_data.dto.ChangesBasicData;
import com.hand.utils.recording_changes_basic_data.service.IChangesBasicDataService;

@Controller
public class ChangesBasicDataController extends BaseController {

    @Autowired
    private IChangesBasicDataService service;


    @RequestMapping(value = "/recording/changes/basic/data/query")
    @ResponseBody
    public ResponseData query(ChangesBasicData dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        pageSize = 10000000;
        List<ChangesBasicData> select = service.select(requestContext, dto, page, pageSize);
        return new ResponseData(select);
    }
    
    @RequestMapping(value = "/recording/changes/basic/data/query2")
    @ResponseBody
    public ResponseData query2(ChangesBasicData dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws ParseException {
        IRequest requestContext = createRequestContext(request);
        pageSize = 10000000;
        //DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");   
        //dto.setLastUpdateDate(format1.parse(dto.getAttribute1().toString()));
        List<ChangesBasicData> select = service.selectChangeDatas(dto);
        return new ResponseData(select);
    }

}