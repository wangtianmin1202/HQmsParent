package com.hand.spc.pspc_cpk_analysis.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_cpk_analysis.dto.CPKAnalyseReqDTO;
import com.hand.spc.pspc_cpk_analysis.service.CPKAnalyseService;
import com.hand.spc.pspc_entity.dto.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
/**
 * @Author han.zhang
 * @Description CPK分析控制层
 * @Date 9:03 2019/9/19
 * @Param
 */
@Controller
public class CPKAnalyseController extends BaseController {
    @Autowired
    private CPKAnalyseService cpkAnalyseService;
    /**
     * @Author han.zhang
     * @Description 查询CPK数据
     * @Date 14:42 2019/9/18
     * @Param [request, dto]
     */
    @RequestMapping(value = "/pspc/data/get/cpk/data")
    @ResponseBody
    public ResponseData remove(HttpServletRequest request, CPKAnalyseReqDTO dto) {
        ResponseData responseData = new ResponseData();
        try {
            responseData.setRows(Collections.singletonList(cpkAnalyseService.listCPK(dto)));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }
}
