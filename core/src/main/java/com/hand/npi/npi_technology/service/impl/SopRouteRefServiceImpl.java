package com.hand.npi.npi_technology.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.attachment.dto.SysFile;
import com.hand.hap.attachment.mapper.SysFileMapper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.file_manager.mapper.ManagerMapper;
import com.hand.hqm.file_manager_classify.dto.ManagerClassify;
import com.hand.hqm.file_manager_his.dto.ManagerHis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.SopInfo;
import com.hand.npi.npi_technology.dto.SopRouteRef;
import com.hand.npi.npi_technology.mapper.SopInfoMapper;
import com.hand.npi.npi_technology.mapper.SopRouteRefMapper;
import com.hand.npi.npi_technology.mapper.TechnologySparePartDetailsMapper;
import com.hand.npi.npi_technology.service.ISopRouteRefService;

import oracle.net.aso.d;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class SopRouteRefServiceImpl extends BaseServiceImpl<SopRouteRef> implements ISopRouteRefService {

	@Autowired
	private SopRouteRefMapper sopRouteRefMapper;
	@Autowired
	private SopInfoMapper sopInfoMapper;
	@Autowired
	SysFileMapper sysFileMapper;

	@Override
	public List<SopRouteRef> queryData(IRequest request, SopRouteRef dto, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<SopRouteRef> dataQuery = sopRouteRefMapper.dataQuery(dto);
		return dataQuery;
	}

	@Override
	public List<SopRouteRef> hisQuery(IRequest request, SopRouteRef dto, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<SopRouteRef> dataQuery = sopRouteRefMapper.hisQuery(dto);
		return dataQuery;
	}

	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) {
		// 文件上传 返回文件id
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		SysFile sysFile = new SysFile();
		sysFile.setFileName(request.getParameter("fileNum"));
		sysFileMapper.insertSelective(sysFile);
		// 服务器路径
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/filemanager/file_sop" + sysFile.getFileId() + "/";// 注意不要多空格！
		String path = rootPath + endPath;
		if (SystemApiMethod.getOsType().equals("window")) {
			// 本地路径
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/filemanager/file_sop" + sysFile.getFileId() + "/";
			path = rootPath + endPath;
		}
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		sysFile.setFilePath(path);
		sysFileMapper.updateByPrimaryKey(sysFile);
		/*
		 * List<InspectionAttribute> returnList = new ArrayList<InspectionAttribute>();
		 */
		if (fileMap.size() > 1) {
			responseData.setMessage("一次只能上传一个文件");
			responseData.setSuccess(false);
			return responseData;

		}
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getOriginalFilename());
			MultipartFile forModel = entry.getValue();
			if (forModel.getOriginalFilename() == null) {
				responseData.setMessage("请选择上传文件");
				responseData.setSuccess(false);
				return responseData;

			}
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
		}
		responseData.setSuccess(true);
		responseData.setMessage(String.valueOf(sysFile.getFileId()));
		return responseData;
	}

	@Override
	public ResponseData sopChange(IRequest requestCtx, SopRouteRef dto) {
		// 变更业务逻辑：1.新增一条sop的数据，作为基础数据 2.将原来的数据的aftersop修改并新增一条最新版本的数据 
		//todo 开启审批流
		ResponseData responseData = new ResponseData();
		SopRouteRef dtoOld= new SopRouteRef();
		dtoOld.setRefId(dto.getRefId());
		dto.setRefId(null);
		
		SopInfo sopinfo=new SopInfo();
		sopinfo.setSopCode("SOPNO"+sopInfoMapper.getCodeSeq());
		sopinfo.setSopName(dto.getBeforeSopName());
		sopinfo.setFileId(Float.valueOf(dto.getFileId()));
		sopInfoMapper.insertSelective(sopinfo);
		
		dtoOld.setRouteId(dto.getRouteId());
		dtoOld.setAfterSopId(sopinfo.getSopId());
		sopRouteRefMapper.updateByPrimaryKeySelective(dtoOld);

		dto.setSopId(sopinfo.getSopId());
		sopRouteRefMapper.insertSelective(dto);

		responseData.setSuccess(true);
		return responseData;
	}

}