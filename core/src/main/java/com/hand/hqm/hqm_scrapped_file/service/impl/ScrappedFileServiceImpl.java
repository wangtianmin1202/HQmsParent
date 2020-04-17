package com.hand.hqm.hqm_scrapped_file.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_qc_files.dto.QcFiles;
import com.hand.hqm.hqm_qc_files.mapper.QcFilesMapper;
import com.hand.hqm.hqm_scrapped_detail.dto.ScrappedDetail;
import com.hand.hqm.hqm_scrapped_file.dto.ScrappedFile;
import com.hand.hqm.hqm_scrapped_file.mapper.ScrappedFileMapper;
import com.hand.hqm.hqm_scrapped_file.service.IScrappedFileService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScrappedFileServiceImpl extends BaseServiceImpl<ScrappedFile> implements IScrappedFileService {
	@Autowired
	ScrappedFileMapper scrappedFileMapper;
	@Autowired
	IScrappedFileService ScrappedFileService;

	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request)
			throws IllegalStateException, IOException {
		Float scrappedLineId = Float.valueOf(request.getParameter("scrappedLineId"));
		String type = request.getParameter("type");
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/scrappedfiles/" + type + "/file_" + scrappedLineId + "/";
		String path = rootPath + endPath;
		if (SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/scrappedfiles/" + type + "/file_" + scrappedLineId + "/";
			path = rootPath + endPath;
		}
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getOriginalFilename());
			MultipartFile forModel = entry.getValue();
			File file = new File(path, forModel.getOriginalFilename());
			// 是否已经存在文件
			ScrappedFile file_s = new ScrappedFile();
			file_s.setScrappedLineId(scrappedLineId);
			file_s.setFileAddress(endPath + entry.getValue().getOriginalFilename());
			ScrappedFileService.insertSelective(requestCtx, file_s);
			responseData.setMessage(entry.getValue().getOriginalFilename());
			if (!file.exists()) {
				forModel.transferTo(file);
			}
			break;
		}
		return responseData;
	}

	@Override
	public List<ScrappedFile> myselect(IRequest requestContext, ScrappedFile dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return scrappedFileMapper.myselect(dto);
	}
}