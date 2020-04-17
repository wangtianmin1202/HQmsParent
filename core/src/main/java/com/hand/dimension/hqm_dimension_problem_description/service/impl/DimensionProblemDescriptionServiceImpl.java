package com.hand.dimension.hqm_dimension_problem_description.service.impl;

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
import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import com.hand.dimension.hqm_dimension_problem_description.dto.DimensionProblemDescription;
import com.hand.dimension.hqm_dimension_problem_description.mapper.DimensionProblemDescriptionMapper;
import com.hand.dimension.hqm_dimension_problem_description.service.IDimensionProblemDescriptionService;
import com.hand.dimension.hqm_dimension_step.mapper.DimensionStepMapper;
import com.hand.dimension.hqm_dimension_step.service.IDimensionStepService;
import com.hand.dimension.hqm_dimension_team_assembled.dto.DimensionTeamAssembled;
import com.hand.dimension.hqm_dimension_team_assembled.mapper.DimensionTeamAssembledMapper;
import com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles;
import com.hand.dimension.hqm_dimension_upload_files.service.IDimensionUploadFilesService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionProblemDescriptionServiceImpl extends BaseServiceImpl<DimensionProblemDescription>
		implements IDimensionProblemDescriptionService {
	@Autowired
	DimensionProblemDescriptionMapper mapper;
	@Autowired
	IDimensionOrderService iDimensionOrderService;
	@Autowired
	IDimensionStepService iDimensionStepService;
	@Autowired
	DimensionStepMapper dimensionStepMapper;
	@Autowired
	IDimensionUploadFilesService iDimensionUploadFilesService;
	@Autowired
    DimensionTeamAssembledMapper dimensionTeamAssembledMapper;
	@Override
	public List<DimensionProblemDescription> reSelect(DimensionProblemDescription dto) {
		// TODO 重写的查询方法
		return mapper.reSelect(dto);
	}

	@Override
	public List<DimensionProblemDescription> saveDetail(IRequest requestContext, DimensionProblemDescription dto)
			throws Exception {
		List<DimensionProblemDescription> returnList = new ArrayList<>();
		// TODO 操作保存
		if (dto.getProblemId() == null) {
			dto = self().insertSelective(requestContext, dto);
			if(dto.getProblemLevel()!=null)
			{
				if(dto.getProblemLevel().equals("A"))
				{
					DimensionTeamAssembled dimensionTeamAssembled =new DimensionTeamAssembled();
					dimensionTeamAssembled.setMemberId(Float.valueOf("1"));
					dimensionTeamAssembled.setOrderId(dto.getOrderId());
					dimensionTeamAssembledMapper.insertSelective(dimensionTeamAssembled);	
				}
			}
			returnList.add(dto);
		} else {
			self().updateByPrimaryKeySelective(requestContext, dto);
			returnList.add(dto);
		}
		iDimensionOrderService.changeStepStatus(requestContext, dto.getOrderId(), 2);
		return returnList;
	}

	@Override
	public ResponseData commitDetail(IRequest request, DimensionProblemDescription dto) throws Exception {
		// TODO 操作提交
		ResponseData responseData = new ResponseData();
		if (dto.getProblemId() == null) {
			responseData.setSuccess(false);
			responseData.setMessage("数据未保存");
			return responseData;
		}
		iDimensionOrderService.changeStep(request, dto.getOrderId(), 2, 3);
		return responseData;
	}

	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) {
		// TODO 文件上传
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();
		String orderId = String.valueOf(Float.valueOf(request.getParameter("orderId")).intValue());
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String endPath = "/demension/problemdescription/file" + orderId + "/";
		String path = SystemApiMethod.getFileFolder() + endPath;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		List<InspectionAttribute> returnList = new ArrayList<InspectionAttribute>();
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
				dimensionUploadFiles.setStep(2f);
				dimensionUploadFiles.setFileUrl(endPath + entry.getValue().getOriginalFilename());
				dimensionUploadFiles.setType("S2");
				iDimensionUploadFilesService.insertSelective(requestCtx, dimensionUploadFiles);
			}
		}
		return responseData;
	}

	@Override
	public List<DimensionProblemDescription> selectDimensionByItem(DimensionProblemDescription dto) {
		// TODO Auto-generated method stub
		return mapper.sameItemDimension(dto);
	}

	@Override
	public List<DimensionProblemDescription> selectDimensionByItemGroupCount(DimensionProblemDescription dto) {
		// TODO Auto-generated method stub
		return mapper.sameItemDimensionNgCodeGroup(dto);
	}
}