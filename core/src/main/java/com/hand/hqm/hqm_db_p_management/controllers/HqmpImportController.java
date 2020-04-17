package com.hand.hqm.hqm_db_p_management.controllers;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_db_p_management.service.IHQMPInvalidService;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;

@Controller
public class HqmpImportController extends BaseController {

	@Autowired
	private IHQMPInvalidService service;

	/**
	 * excel数据导入
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/hqmp/db/excelimport")
    @ResponseBody
    public ResponseData excelImport(HttpServletRequest request){
		IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	//MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;   写法1 报错:  cannot be cast to org.springframework.web.multipart.MultipartHttpServletRequest
    	
    	//写法2 不报错
    	MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
    	MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<InspectionAttribute> returnList = new ArrayList<InspectionAttribute>();
		for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			try {
				responseData=service.inputDataFromExcel(request,requestContext,forModel.getInputStream());
			} catch (Exception e1) {
				// TODO 解析异常
				responseData.setMessage(e1.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}
		}
		responseData.setRows(returnList);
        return responseData;
    }
	
	/**
	 * 文件下载
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/hqmp/db/downloadfile")
	@ResponseBody
    public void exportVehicleInfo(HttpServletRequest req, HttpServletResponse resp,@RequestParam String fileName, @RequestParam String filePath) {
        DataInputStream in = null;
        OutputStream out = null;
        try{
            resp.reset();// 清空输出流
            String resultFileName = fileName + System.currentTimeMillis() + ".xls";
            resultFileName = URLEncoder.encode(resultFileName,"UTF-8");  
            resp.setCharacterEncoding("UTF-8");  
            resp.setHeader("Content-disposition", "attachment; filename=" + resultFileName);// 设定输出文件头
            resp.setContentType("application/msexcel");// 定义输出类型
            //输入流：本地文件路径
            in = new DataInputStream(
                    new FileInputStream(new File(filePath + fileName)));  //+".xlsx"
            //输出流
            out = resp.getOutputStream();
            //输出文件
            int bytes = 0;
            byte[] bufferOut = new byte[1024];  
            while ((bytes = in.read(bufferOut)) != -1) {  
                out.write(bufferOut, 0, bytes);  
            }
        } catch(Exception e){
            e.printStackTrace();
            resp.reset();
            try {
                OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");  
                String data = "<script language='javascript'>alert(\"\\u64cd\\u4f5c\\u5f02\\u5e38\\uff01\");</script>";
                writer.write(data); 
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
}