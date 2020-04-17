package com.hand.spc.pspc_entity.controllers;

import com.hand.spc.pspc_entity.dto.ChartShowVO;
import com.hand.spc.pspc_entity.view.ParametricComparisonVO;
import com.hand.spc.pspc_entity.view.ScatterPlotVO;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.service.IEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class EntityController extends BaseController{

    @Autowired
    private IEntityService service;


    @RequestMapping(value = "/pspc/entity/query")
    @ResponseBody
    public ResponseData query(Entity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/pspc/entity/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Entity> dto, BindingResult result, HttpServletRequest request){

//        getValidator().validate(dto, result);
//        if (result.hasErrors()) {
//        ResponseData responseData = new ResponseData(false);
//        responseData.setMessage(getErrorMessage(result, request));
//        return responseData;
//        }
        IRequest requestCtx = createRequestContext(request);
        return service.saveBaseData(requestCtx, dto);
    }

    @RequestMapping(value = "/pspc/entity/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Entity> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @Author han.zhang
     * @Description 查询图形展示的数据
     * @Date 16:07 2019/8/21
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/entity/query/chart/show")
    @ResponseBody
    public ResponseData query(ChartShowVO dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try{
            responseData.setRows(service.selectChartShow(dto));
            responseData.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setMessage(e.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    /**
     * @Author han.zhang
     * @Description 查询正态检验表格数据以及数据点
     * @Date 16:07 2019/8/21
     * @Param [dto, page, pageSize, request]
     */
    @RequestMapping(value = "/pspc/entity/query/normal/cali/data")
    @ResponseBody
    public ResponseData queryNormalCali(ChartShowVO dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            responseData.setRows(service.selectNormalCalibrationData(dto));
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @RequestMapping(value = "/pspc/entity/query/new")
    @ResponseBody
    public ResponseData queryData(Entity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData(true);
        try {
            responseData.setRows(service.selectData(requestContext, dto, page, pageSize));
        }catch (Exception e){
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }

    /**
     *
     * @Description 散点图查询
     *
     * @author yuchao.wang
     * @date 2019/8/23 11:26
     * @param dto
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/entity/query/scatter/plot")
    @ResponseBody
    public ResponseData queryScatterPlot(@RequestBody List<ScatterPlotVO> dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();

        try {
            responseData = service.queryScatterPlot(requestContext, dto);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     *
     * @Description 散点图导出Excel
     *
     * @author yuchao.wang
     * @date 2019/8/26 21:26
     * @param request
     * @param response
     * @param img 矩阵散点图-可为空
     * @param img1 散点图-可为空
     * @param thead 表头 半角逗号分隔
     * @param tbody 表数据 井号分隔行，半角逗号分隔列
     * @return
     *
     */
    @RequestMapping(value = "/pspc/entity/export/excel/scatter/plot")
    @ResponseBody
    public void exportExcelScatterPlot(HttpServletRequest request, HttpServletResponse response,
                                               String img, String img1, String thead, String tbody) {

        service.exportExcelScatterPlot(request, response, img, img1, thead, tbody);
    }

    /**
     *
     * @Description 多参数对比图查询
     *
     * @author yuchao.wang
     * @date 2019/8/27 15:26
     * @param dto
     * @param request
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @RequestMapping(value = "/pspc/entity/query/parametric/comparison")
    @ResponseBody
    public ResponseData queryParametricComparisonChart(@RequestBody List<ParametricComparisonVO> dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();

        try {
            responseData = service.queryParametricComparisonChart(requestContext, dto);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     *
     * @Description 导出Excel,适用于多参数对比图、箱线图等一个图一个表格的导出
     *
     * @author yuchao.wang
     * @date 2019/8/28 15:26
     * @param request
     * @param response
     * @param img 多参数对比图-可为空
     * @param thead 表头 半角逗号分隔
     * @param tbody 表数据 井号分隔行，半角逗号分隔列
     * @param title 导出文件名前缀
     * @param exportFileName 导出文件名前缀-支持中文
     * @return
     *
     */

    @RequestMapping(value = "/pspc/entity/export/excel/single/graph")
    @ResponseBody
    public void exportExcelSingleGraph(HttpServletRequest request, HttpServletResponse response,
                                       String img, String thead, String tbody, String title, String exportFileName,int rowEnd,int colEnd) {

        service.exportExcelSingleGraph(request, response, img, thead, tbody, title, exportFileName,rowEnd,colEnd);
    }

    /**
     * @Author han.zhang
     * @Description 图形展示的导出
     * @Date 下午5:30 2019/10/18
     * @Param [request, response, img, thead, tbody, title, exportFileName, rowEnd, colEnd]
     **/
    @RequestMapping(value = "/pspc/entity/export/excel/single/graph/chart/show")
    @ResponseBody
    public void exportExcelSingleGraphChartShow(HttpServletRequest request, HttpServletResponse response,
                                       String img, String thead, String tbody, String title, String exportFileName,int rowEnd,int colEnd) {

        service.exportExcelSingleGraphChartShow(request, response, img, thead, tbody, title, exportFileName,rowEnd,colEnd);
    }

}