package com.hand.hqm.hqm_scrapped_file_after.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.controllers.BaseController;
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
import com.hand.hqm.hqm_scrapped_file_after.dto.ScrappedFileAfter;
import com.hand.hqm.hqm_scrapped_file_after.mapper.ScrappedFileAfterMapper;
import com.hand.hqm.hqm_scrapped_file_after.service.IScrappedFileAfterService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScrappedFileAfterServiceImpl extends BaseServiceImpl<ScrappedFileAfter>
		implements IScrappedFileAfterService {
	@Autowired
	ScrappedFileAfterMapper mapper;

	@Override
	public List<ScrappedFileAfter> reSelect(IRequest requestContext, ScrappedFileAfter dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}
	
	@Override
	public List<ScrappedFileAfter> fileUp(HttpServletRequest request) {
		// TODO Auto-generated method stub
		IRequest requestContext = RequestHelper.createServiceRequest(request);
		List<String> rows = new ArrayList<String>();
		String scrappedLineId = String.valueOf(Float.valueOf(request.getParameter("scrappedLineId")).intValue());
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String endPath = "/demension/scrappedfileafter/file" + scrappedLineId + "/";
		String path = SystemApiMethod.getFileFolder() + endPath;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		List<ScrappedFileAfter> returnList = new ArrayList<ScrappedFileAfter>();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getOriginalFilename());
			MultipartFile forModel = entry.getValue();
			File file = new File(path, forModel.getOriginalFilename());
			boolean newFileFlag = true;
			if (file.exists()) {
				file.delete();
				newFileFlag = false;
			}
			try {
				forModel.transferTo(file);

			} catch (IllegalStateException | IOException e) {
			}
			if (newFileFlag) {
				ScrappedFileAfter addor = new ScrappedFileAfter();
				addor.setScrappedLineId(Float.valueOf(scrappedLineId));
				addor.setFileAddress(endPath + entry.getValue().getOriginalFilename());
				self().insertSelective(requestContext, addor);
				returnList.add(addor);
			}
		}
		return returnList;
	}

	@Override
	public int reBatchDelete(List<ScrappedFileAfter> dto) {
		String rootPath = SystemApiMethod.getFileFolder();
		int c = 0;
		for (ScrappedFileAfter t : dto) {
			c += self().deleteByPrimaryKey(t);
			File file = new File(rootPath + t.getFileAddress());
			if (file.exists()) {
				file.delete();
			}
		}
		return c;
	}

	

}