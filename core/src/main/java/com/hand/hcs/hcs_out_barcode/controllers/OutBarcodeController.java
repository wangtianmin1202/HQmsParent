package com.hand.hcs.hcs_out_barcode.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;
import com.hand.hcs.hcs_out_barcode.service.IOutBarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;
import java.util.Map;

    @Controller
    public class OutBarcodeController extends BaseController{

    @Autowired
    private IOutBarcodeService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hcs/out/barcode/query")
    @ResponseBody
    public ResponseData query(OutBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hcs/out/barcode/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OutBarcode> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<OutBarcode> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     *  生成条码号
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/queryMaxNum")
    @ResponseBody
    public String queryMaxNum(HttpServletRequest request,OutBarcode dto){
    	IRequest requestContext = createRequestContext(request);
        return service.getobarcode(requestContext, dto);
    }
    @RequestMapping(value = "/hcs/out/barcode/getMaxNum")
    @ResponseBody
    public Map<String,String> getMaxNum(HttpServletRequest request,OutBarcode dto){
        return service.getMaxNum( dto);
    }
    /**
     * 新建外箱条码
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/addInfo")
    @ResponseBody
    public ResponseData addInfo(HttpServletRequest request,OutBarcode dto){
    	IRequest requestContext = createRequestContext(request);
        return service.addInfo(requestContext, dto);
    }
    /**
     * 外箱条码打印
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/printQuery")
    @ResponseBody
    public ResponseData printQuery(HttpServletRequest request,@RequestBody List<OutBarcode> dto){
    	IRequest requestContext = createRequestContext(request);
        return service.printQuery(requestContext,dto);
    }
    /**
     * 外箱条码 失效
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/changeFlag")
    @ResponseBody
    public ResponseData changeFlag(HttpServletRequest request,@RequestBody List<OutBarcode> dto){
    	IRequest requestContext = createRequestContext(request);
        return service.changeFlag(requestContext,dto);
    }
    
    @RequestMapping(value = "/hcs/out/barcode/bindQuery")
    @ResponseBody
    public ResponseData bindQuery(OutBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.bindQuery(requestContext,dto,page,pageSize));
    }
    /**
     * 确认绑定校验
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/checkBind")
    @ResponseBody
    public ResponseData checkBind(HttpServletRequest request, @RequestBody List<SmallBarcode> dto){
    	IRequest requestContext = createRequestContext(request);
        return service.checkBind(requestContext,dto);
    }
    /**
     * 绑定容器查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/outBindQuery")
    @ResponseBody
    public ResponseData outBindQuery(OutBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.outBindQuery(requestContext,dto,page,pageSize));
    }
    /**
     * 绑定校验
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/bindValidator")
    @ResponseBody
    public ResponseData bindValidator(HttpServletRequest request, @RequestBody List<OutBarcode> dto){
    	IRequest requestContext = createRequestContext(request);
        return service.bindValidator(requestContext,dto);
    }
    /**
     * 解绑
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/unBind")
    @ResponseBody
    public ResponseData unBind(HttpServletRequest request, @RequestBody List<OutBarcode> dto){
    	IRequest requestContext = createRequestContext(request);
        return service.unBind(requestContext, dto);
    }
    /**
     * 详情查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/queryDetail")
    @ResponseBody
    public ResponseData queryDetail(OutBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryDetail(requestContext,dto,page,pageSize));
    }
    /**
     * 绑定查询（left）
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/bindQueryLeft")
    @ResponseBody
    public ResponseData bindQueryLeft(OutBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.bindQueryLeft(requestContext,dto,page,pageSize));
    }
    /**
     * 已绑定页面查询（right）
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hcs/out/barcode/queryRight")
    @ResponseBody
    public ResponseData queryRight(OutBarcode dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryRight(requestContext,dto,page,pageSize));
    }
    /**
     * 绑定
     * @param request 请求
     * @param dto 绑定信息
     * @return 响应体
     */
    @RequestMapping(value = "/hcs/out/barcode/bind")
    @ResponseBody
    public ResponseData bind(HttpServletRequest request, @RequestBody List<OutBarcode> dto){
    	IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.bind(requestContext,dto));
    }
    /**
     * 更新打印次数
     * @param request 请求
     * @param dto 打印信息
     * @return 响应体
     */
    @RequestMapping(value = "/hcs/out/barcode/updatePrintTime")
    @ResponseBody
    public ResponseData updatePrintTime(HttpServletRequest request, @RequestBody List<OutBarcode> dto){
    	IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.updatePrintTime(requestContext,dto));
    }
    }