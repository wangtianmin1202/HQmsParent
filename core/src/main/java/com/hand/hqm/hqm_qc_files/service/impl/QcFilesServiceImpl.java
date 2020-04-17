package com.hand.hqm.hqm_qc_files.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles;
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
import com.hand.hqm.hqm_qc_files.service.IQcFilesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class QcFilesServiceImpl extends BaseServiceImpl<QcFiles> implements IQcFilesService{
	
	@Autowired
	private QcFilesMapper mapper; 
	
	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request)
			throws IllegalStateException, IOException {
		String inspectionNum = request.getParameter("inspectionNum");
		String inspectionType = request.getParameter("inspectionType");
		String headerId = String.valueOf(Float.valueOf(request.getParameter("headerId")).intValue());
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();
    	
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/qcfiles/"+ inspectionType +"/file_"+inspectionNum+"/";
		String path = rootPath + endPath;
		if(SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/qcfiles/"+ inspectionType +"/file_"+inspectionNum+"/";
			path = rootPath + endPath;
		}
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getOriginalFilename());
			MultipartFile forModel = entry.getValue();
			File file = new File(path,forModel.getOriginalFilename());
			//是否已经存在文件
			QcFiles qcFiles = new QcFiles();
			qcFiles.setHeaderId(Float.valueOf(headerId));
			qcFiles.setInspectionType(inspectionType);
			qcFiles.setFileUrl(endPath + entry.getValue().getOriginalFilename());

			List<QcFiles> qcFilesList = mapper.select(qcFiles);				
			if(qcFilesList == null || qcFilesList.size() == 0) {
				//执行新增
				forModel.transferTo(file);
				QcFiles files = new QcFiles();
				files.setHeaderId(Float.valueOf(headerId));
				files.setInspectionType(inspectionType);
				files.setFileUrl(endPath + entry.getValue().getOriginalFilename());
				self().insertSelective(requestCtx, files);
			}else {
				//删除已存在并执行更改
				File haveFile = new File(rootPath+qcFilesList.get(0).getFileUrl());
				if(haveFile.exists()) {
					file.delete();
				}
				forModel.transferTo(file);
				//执行更新操作
				QcFiles files = new QcFiles();
				files.setFileId(qcFilesList.get(0).getFileId());
				files.setFileUrl(endPath + entry.getValue().getOriginalFilename());
				self().updateByPrimaryKeySelective(requestCtx, files);
			}
			responseData.setMessage(entry.getValue().getOriginalFilename());
			break;
		}
		return responseData;
	}

	@Override
	public List<QcFiles> query(IRequest requestContext, QcFiles dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public int deleteDataAndFile(List<QcFiles> dto) {
		String rootPath = "/apps/hap/resource";
		if (SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
		}
		int c = 0;
		for (QcFiles t : dto) {
			c += self().deleteByPrimaryKey(t);
			File file = new File(rootPath + t.getFileUrl());
			if (file.exists()) {
				file.delete();
			}
		}
		return c;
	}

}