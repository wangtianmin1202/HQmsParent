package com.hand.dimension.hqm_dimension_upload_files.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles;
import com.hand.dimension.hqm_dimension_upload_files.mapper.DimensionUploadFilesMapper;
import com.hand.dimension.hqm_dimension_upload_files.service.IDimensionUploadFilesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionUploadFilesServiceImpl extends BaseServiceImpl<DimensionUploadFiles>
		implements IDimensionUploadFilesService {
	@Autowired
	DimensionUploadFilesMapper mapper;

	@Override
	public int reBatchDelete(List<DimensionUploadFiles> list) {
		// TODO 重写的删除
		String rootPath = "/apps/hap/resource";
		if (SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
		}
		int c = 0;
		for (DimensionUploadFiles t : list) {
			c += self().deleteByPrimaryKey(t);
			File file = new File(rootPath + t.getFileUrl());
			if (file.exists()) {
				file.delete();
			}
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.dimension.hqm_dimension_upload_files.service.
	 * IDimensionUploadFilesService#fileUpload(com.hand.hap.core.IRequest,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();
		Float actionId = Float.valueOf(request.getParameter("actionId"));
		Float orderId = Float.valueOf(request.getParameter("orderId"));
		String type = request.getParameter("type");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String endPath = "/demension/improving_prevention/file" + actionId.intValue() + type + "/";
		String path = SystemApiMethod.getFileFolder() + endPath;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
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
				// TODO 文件保存异常
				responseData.setMessage(e.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}
			if (newFileFlag) {
				DimensionUploadFiles dimensionUploadFiles = new DimensionUploadFiles();
				dimensionUploadFiles.setOrderId(Float.valueOf(orderId));
				dimensionUploadFiles.setActionId(actionId);
				if (type.equals("S7")) {
					dimensionUploadFiles.setStep(7f);
				} else if (type.equals("S5")) {
					dimensionUploadFiles.setStep(5f);
				}
				dimensionUploadFiles.setFileUrl(endPath + entry.getValue().getOriginalFilename());
				dimensionUploadFiles.setType(type);
				self().insertSelective(requestCtx, dimensionUploadFiles);
			}
		}
		return responseData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.dimension.hqm_dimension_upload_files.service.
	 * IDimensionUploadFilesService#reSelect(com.hand.hap.core.IRequest,
	 * com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles, int,
	 * int)
	 */
	@Override
	public List<DimensionUploadFiles> reSelect(IRequest requestContext, DimensionUploadFiles dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

}