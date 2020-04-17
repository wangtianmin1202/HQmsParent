package com.hand.spc.ecr_main.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrQtp;
import com.hand.spc.ecr_main.service.IEcrQtpService;
import com.hand.spc.ecr_main.view.EcrQtpV0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * description
 *
 * @author KOCDZX0 2020/03/12 1:52 PM
 */
@Controller
public class EcrQtpController extends BaseController {

    @Autowired
    private IEcrQtpService ecrQtpService;

    @RequestMapping(value = "/hpm/ecr/qtp/query")
    @ResponseBody
    public ResponseData query(HttpServletRequest request,
    		EcrQtpV0 dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(ecrQtpService.eqQuery(requestCtx,dto));
    }

}
