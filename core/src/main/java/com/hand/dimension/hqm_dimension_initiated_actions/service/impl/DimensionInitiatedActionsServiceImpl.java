package com.hand.dimension.hqm_dimension_initiated_actions.service.impl;

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
import com.hand.dimension.hqm_dimension_initiated_actions.dto.DimensionInitiatedActions;
import com.hand.dimension.hqm_dimension_initiated_actions.mapper.DimensionInitiatedActionsMapper;
import com.hand.dimension.hqm_dimension_initiated_actions.service.IDimensionInitiatedActionsService;
import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles;
import com.hand.dimension.hqm_dimension_upload_files.mapper.DimensionUploadFilesMapper;
import com.hand.dimension.hqm_dimension_upload_files.service.IDimensionUploadFilesService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionInitiatedActionsServiceImpl extends BaseServiceImpl<DimensionInitiatedActions> implements IDimensionInitiatedActionsService{
	@Autowired
	DimensionInitiatedActionsMapper mapper;
	@Autowired
	DimensionUploadFilesMapper dimensionUploadFilesMapper;
	@Autowired
	IDimensionUploadFilesService iDimensionUploadFilesService;
	@Autowired
	IDimensionOrderService iDimensionOrderService;
	@Override
	public List<DimensionInitiatedActions> reSelect(IRequest requestContext, DimensionInitiatedActions dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}
	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException {
		// TODO 文件上载
				String orderId = String.valueOf(Float.valueOf(request.getParameter("orderId")).intValue());;
				String actionId = String.valueOf(Float.valueOf(request.getParameter("actionId")).intValue());
				ResponseData responseData = new ResponseData();
				List<String> rows = new ArrayList<String>();
		    	
		    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

				// 获取文件map集合
				Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
				String rootPath = "/apps/hap/resource";
				String endPath = "/demension/initiatedactions/file_"+orderId+"_"+actionId+"/";
				String path = rootPath + endPath;
				if(SystemApiMethod.getOsType().equals("window")) {
					rootPath = "C:/apps/hap/resource";
					endPath = "/demension/initiatedactions/file_"+orderId+"_"+actionId+"/";
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
					DimensionUploadFiles searchFile = new DimensionUploadFiles();
					searchFile.setOrderId(Float.valueOf(orderId));
					searchFile.setActionId(Float.valueOf(actionId));
					searchFile.setStep(6f);
					searchFile.setType("S6");
					List<DimensionUploadFiles> resultFileList = dimensionUploadFilesMapper.select(searchFile);				
					if(resultFileList == null || resultFileList.size() == 0) {
						//执行新增
						forModel.transferTo(file);
						DimensionUploadFiles dimensionUploadFiles = new DimensionUploadFiles();
						dimensionUploadFiles.setOrderId(Float.valueOf(orderId));
						dimensionUploadFiles.setActionId(Float.valueOf(actionId));
						dimensionUploadFiles.setStep(6f);
						dimensionUploadFiles.setFileUrl(endPath + entry.getValue().getOriginalFilename());
						dimensionUploadFiles.setType("S6");
						iDimensionUploadFilesService.insertSelective(requestCtx, dimensionUploadFiles);
					}else {
						//删除已存在并执行更改
						File haveFile = new File(rootPath+resultFileList.get(0).getFileUrl());
						if(haveFile.exists()) {
							file.delete();
						}
						forModel.transferTo(file);
						//执行更新操作
						DimensionUploadFiles update = new DimensionUploadFiles();
						update.setFileId(resultFileList.get(0).getFileId());
						update.setFileUrl(endPath + entry.getValue().getOriginalFilename());
						iDimensionUploadFilesService.updateByPrimaryKeySelective(requestCtx, update);
					}
					responseData.setMessage(entry.getValue().getOriginalFilename());
					break;
				}
				return responseData;
	}
	@Override
	public ResponseData commit(IRequest requestContext, DimensionInitiatedActions dto) throws Exception {
		ResponseData responseData = new ResponseData();
		iDimensionOrderService.changeStep(requestContext,dto.getOrderId(),6,7);
		return responseData;
	}
	@Override
	public List<DimensionInitiatedActions> reBatchUpdate(IRequest requestCtx, List<DimensionInitiatedActions> dto) throws Exception {
		// TODO Auto-generated method stub
		self().batchUpdate(requestCtx, dto);
		iDimensionOrderService.changeStepStatus(requestCtx, dto.get(0).getOrderId(), 6);
		return dto; 
	}

}