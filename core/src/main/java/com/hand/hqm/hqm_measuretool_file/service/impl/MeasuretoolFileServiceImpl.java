package com.hand.hqm.hqm_measuretool_file.service.impl;

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
import com.hand.hqm.hqm_measuretool_file.dto.MeasuretoolFile;
import com.hand.hqm.hqm_measuretool_file.mapper.MeasuretoolFileMapper;
import com.hand.hqm.hqm_measuretool_file.service.IMeasuretoolFileService;
import com.hand.hqm.hqm_qc_files.dto.QcFiles;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeasuretoolFileServiceImpl extends BaseServiceImpl<MeasuretoolFile> implements IMeasuretoolFileService{
	
	@Autowired
	private MeasuretoolFileMapper mapper;
	
	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request)
			throws IllegalStateException, IOException {
		String measureToolNum = request.getParameter("measureToolNum");
		String type = request.getParameter("type");
		String measuretoolId = String.valueOf(Float.valueOf(request.getParameter("measuretoolId")).intValue());
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();
    	
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/measureTools/"+ type +"/file_"+measureToolNum+"/";
		String path = rootPath + endPath;
		if(SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/measureTools/"+ type +"/file_"+measureToolNum+"/";
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
			MeasuretoolFile measuretoolFile = new MeasuretoolFile();
			measuretoolFile.setMeasuretoolId(Float.valueOf(measuretoolId));
			measuretoolFile.setMeasureToolFileType(type);
			measuretoolFile.setFileAddress(endPath + entry.getValue().getOriginalFilename());

			List<MeasuretoolFile> measuretoolFileList = mapper.select(measuretoolFile);				
			if(measuretoolFileList == null || measuretoolFileList.size() == 0) {
				//执行新增
				forModel.transferTo(file);
				
				MeasuretoolFile files = new MeasuretoolFile();
				files.setMeasuretoolId(Float.valueOf(measuretoolId));
				files.setMeasureToolFileType(type);
				files.setFileAddress(endPath + entry.getValue().getOriginalFilename());

				self().insertSelective(requestCtx, files);
			}else {
				//删除已存在并执行更改
				File haveFile = new File(rootPath+measuretoolFileList.get(0).getFileAddress());
				if(haveFile.exists()) {
					file.delete();
				}
				forModel.transferTo(file);
				//执行更新操作
				MeasuretoolFile files = new MeasuretoolFile();
				files.setMsaFileId(measuretoolFileList.get(0).getMsaFileId());
				files.setFileAddress(endPath + entry.getValue().getOriginalFilename());
				self().updateByPrimaryKeySelective(requestCtx, files);
			}
			responseData.setMessage(entry.getValue().getOriginalFilename());
			break;
		}
		return responseData;
	}

	@Override
	public List<MeasuretoolFile> query(IRequest requestContext, MeasuretoolFile dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public int deleteDataAndFile(List<MeasuretoolFile> dto) {
		String rootPath = "/apps/hap/resource";
		if (SystemApiMethod.getOsType().equals("window")) {
			rootPath = "C:/apps/hap/resource";
		}
		int c = 0;
		for (MeasuretoolFile t : dto) {
			c += self().deleteByPrimaryKey(t);
			File file = new File(rootPath + t.getFileAddress());
			if (file.exists()) {
				file.delete();
			}
		}
		return c;
	}

}