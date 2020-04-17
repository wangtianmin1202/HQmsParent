package com.hand.utils.promptUtils.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.utils.promptUtils.dto.CreateReportFile;
import com.hand.utils.promptUtils.service.IPromptUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lkj
 * @Date: 2018/6/19 11:01
 * @E-mail: kejin.liu@hand-china.com
 * @Description: 多语言的导入导出
 */
@Controller
public class PromptUtilController extends BaseController {
    @Autowired
    private IPromptUtilService service;

    @RequestMapping(value = "/exportExcelTableColumnsAndColumnsDesc")
    public void exportExcelTableColumns(HttpServletRequest request, HttpServletResponse response, String tableName) throws Exception {
        /**
         *
         * 功能描述: 查询表列名和注释并且导出
         *
         * @auther:lkj
         * @date:2018/6/19 上午11:49
         * @param:[request, response, tableName]
         * @return:void
         *
         */
        IRequest requestCtx = createRequestContext(request);
        service.selectTableColumn(tableName,requestCtx,request,response);
    }

    @RequestMapping("/importPromptData")
    @ResponseBody
    public ResponseData importExcel(HttpServletRequest request, HttpServletResponse response){
        /**
         *
         * 功能描述: 描述维护数据导入
         *
         * @auther:lkj
         * @date:2018/6/19 下午8:19
         * @param:[request, response]
         * @return:com.hand.hap.system.dto.ResponseData
         *
         */

        ResponseData responseData = null;
        try {
            responseData = service.importPromptData(request,response,createRequestContext(request));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping("/importCreateTable")
    @ResponseBody
    public ResponseData importCreateTable(HttpServletRequest request, HttpServletResponse response){
        /**
         *
         * 功能描述: excelUtil 建表
         *
         * @auther:lkj
         * @date:2018/7/11 下午7:22
         * @param:[request, response]
         * @return:com.hand.hap.system.dto.ResponseData
         *
         */

        ResponseData responseData = null;
        try {
            responseData = service.importCreateTable(request,response,createRequestContext(request));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping("/createReportFile")
    @ResponseBody
    public ResponseData createReportFile(HttpServletRequest request, CreateReportFile dto){
        /**
         *
         * 功能描述: 创建报表文件
         *
         * @auther:lkj
         * @date:2018/9/3 上午11:48
         * @param:[request, response]
         * @return:com.hand.hap.system.dto.ResponseData
         *
         */
        IRequest iRequest = createRequestContext(request);
        ResponseData responseData = null;
        try {
            responseData = service.createReportFile(iRequest,dto);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

}
