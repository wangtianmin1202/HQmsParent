package com.hand.spc.ecr_main.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.service.IEcrVtpService;
import com.hand.spc.ecr_main.view.EcrVtpVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description
 *
 * @author KOCDZX0 2020/03/09 11:48 AM
 */
@Controller
public class EcrVtpController extends BaseController {

    @Autowired
    private IEcrVtpService vtpService;

    @RequestMapping(value = "/hpm/ecr/vtp/query")
    @ResponseBody
    public ResponseData query(EcrVtpVO vo){
        return new ResponseData(vtpService.listQuery(vo));
    }

}
