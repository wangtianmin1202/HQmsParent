package com.hand.spc.ecr_main.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrDetail;
import com.hand.spc.ecr_main.service.IEcrDetailService;

@Controller
public class EcrDetailController extends BaseController {

    @Autowired
    private IEcrDetailService ecrDetailService;

    @RequestMapping(value = "/hpm/ecr/ecrdetail/query")
    @ResponseBody
    public ResponseData detailQuery(HttpServletRequest request,
                                    EcrDetail dto,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                    @RequestParam(defaultValue = DEFAULT_PAGE) int page) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(ecrDetailService.inventoryDetailsQuery(requestCtx, dto, page, pageSize));

    }

    @RequestMapping(value = "/hpm/ecr/ecrdetail/edit")
    @ResponseBody
    public ResponseData update(@RequestBody EcrDetail dto, HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
            responseData = ecrDetailService.update(requestCtx, dto);
        } catch (Exception e) {
            responseData.setMessage(e.toString());
            responseData.setSuccess(false);
        }
        return responseData;
    }


    @RequestMapping(value = "/hpm/ecr/ecrstock/query")
    @ResponseBody
    public ResponseData detailQuery(Long itemId) {
        return new ResponseData(ecrDetailService.stockInfoQuery(itemId));
    }
}
