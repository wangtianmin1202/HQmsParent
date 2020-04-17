package com.hand.hcm.hcm_item.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

import com.hand.hap.system.controllers.BaseController;
import com.hand.custom.components.EchoWebSocketHandler;
import com.hand.hap.attachment.exception.AttachmentException;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.service.IItemService;
import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;
import com.hand.hqm.hqm_qua_ins_time_l.mapper.QuaInsTimeLMapper;
import com.hand.hqm.hqm_qua_ins_time_l.service.IQuaInsTimeLService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController extends BaseController {

	@Autowired
	private IItemService service;

	@Autowired
	QuaInsTimeLMapper quaInsTimeLMapper;
	@Autowired
	IQuaInsTimeLService iQuaInsTimeLService;
	@Autowired
	IPqcInspectionHService iPqcInspectionHService;
	Logger logger = LoggerFactory.getLogger(ItemController.class);

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hcm/item/query")
	@ResponseBody
	public ResponseData query(Item dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hcm/item/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<Item> dto, BindingResult result, HttpServletRequest request) {
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
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcm/item/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<Item> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hcm/item/function/{variable}/test")
	@ResponseBody
	public String functionTest(@PathVariable String variable, HttpServletRequest request) throws IOException {
		ResponseData responseData = new ResponseData();
//		EchoWebSocketHandler.sendMessage("11");
		String var = variable;
		responseData.setMessage("<h1>SB</h1>");
		return responseData.getMessage();
	}

	@RequestMapping(value = "/hcm/item/function/test/upload")
	@ResponseBody
	public ResponseData upload(HttpServletRequest request, @RequestParam MultipartFile[] files,
			@RequestParam String par1, @RequestParam String par2) {
		logger.info("par1：" + par1);
		logger.info("par2：" + par2);
		for (MultipartFile mf : files) {
			logger.info("收到文件：" + mf.getOriginalFilename());
		}
//		MultipartHttpServletRequest mul = (MultipartHttpServletRequest) request;
//		Map<String, MultipartFile> fileMap = mul.getFileMap();
//		logger.info("par1：" + par1);
//		logger.info("par2：" + par2);
//		for (Map.Entry<String, MultipartFile> multipart : fileMap.entrySet()) {
//			logger.info("收到文件：" + multipart.getValue().getOriginalFilename());
//		}
		return new ResponseData();
	}

	// /give/me/a/pdf
	@RequestMapping(value = "/give/me/a/pdf")
	@ResponseBody
	public void give(HttpServletRequest request, HttpServletResponse response) throws Exception {
		IRequest requestContext = createRequestContext(request);
		File file = new File("D:/u01/hap/font-awesome.pdf");
		if (file.exists()) {
			response.addHeader("content-disposition",
					"inline;filename=\"" + URLEncoder.encode("font-awesome.pdf","UTF-8") + "\";");//inline 打开 attachment 下载
			response.setContentType("application/pdf;charset=UTF-8;");
			response.setHeader("accept-ranges", "bytes");
			int fileLength = (int) file.length();
			response.setContentLength(fileLength);
			if (fileLength > 0) {
				writeFileToResp(response, file);
			}
		} else {

		}
	}

	private void writeFileToResp(HttpServletResponse response, File file) throws Exception {
		byte[] buf = new byte[1024];
		try (InputStream inStream = new FileInputStream(file);
				ServletOutputStream outputStream = response.getOutputStream()) {
			int readLength;
			while (((readLength = inStream.read(buf)) != -1)) {
				outputStream.write(buf, 0, readLength);
			}
			outputStream.flush();

		}
	}

	public boolean isShiftNow(Float prodLineId, String shiftCode, Float plantId) {
		// 判断是否符合当前班次范围
		QuaInsTimeL search = new QuaInsTimeL();
		search.setProdLineId(prodLineId);
		search.setShiftCode(shiftCode);
		search.setPlantId(plantId);
		List<QuaInsTimeL> resultList = quaInsTimeLMapper.shiftNowQuery(search);
		if (resultList == null || resultList.size() == 0) {
			return false;
		}
		return true;
	}
}