package com.hand.spc.ecr_main.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.service.IEcrApproveService;
import com.hand.spc.ecr_main.view.EcrApproveV1;
import com.hand.spc.ecr_main.view.EcrApproveV2;
import com.hand.spc.ecr_main.view.EcrApproveV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * description
 *
 * @author KOCDZX0 2020/03/09 4:33 PM
 */
@Controller
public class EcrApproveController extends BaseController {

    @Autowired
    private IEcrApproveService ecrApproveService;


    @RequestMapping(value = "/hpm/ecr/approve/basequery")
    @ResponseBody
    public ResponseData approve1Query(HttpServletRequest request, EcrApproveV1 dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(ecrApproveService.approve1Query(requestCtx, dto));
    }


    @RequestMapping(value = "/hpm/ecr/approve/itemquery")
    @ResponseBody
    public ResponseData approve2Query(HttpServletRequest request, EcrApproveV2 dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(ecrApproveService.approve2Query(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/approve/skuquery")
    @ResponseBody
    public ResponseData approve3Query(EcrApproveV3 dto) {
        return new ResponseData(ecrApproveService.approve3Query(dto));
    }
}
