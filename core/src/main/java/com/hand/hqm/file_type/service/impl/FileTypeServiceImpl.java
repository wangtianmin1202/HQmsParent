package com.hand.hqm.file_type.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.file_type.dto.FileType;
import com.hand.hqm.file_type.mapper.FileTypeMapper;
import com.hand.hqm.file_type.service.IFileTypeService;
import com.hand.wfl.util.DropDownListDto;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileTypeServiceImpl extends BaseServiceImpl<FileType> implements IFileTypeService{

	@Autowired
	private FileTypeMapper fileTypeMapper;
	
	@Override
	public List<FileType> queryByCondition(IRequest requestContext, FileType dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return fileTypeMapper.queryByCondition(dto);
	}

	
	@Override
	public ResponseData add(IRequest requestCtx, FileType dto) {
		ResponseData responseData = new ResponseData();
		try {
			if ("add".equals(dto.get__status())) {
				self().insertSelective(requestCtx, dto);
			} else if ("update".equals(dto.get__status())) {
				self().updateByPrimaryKey(requestCtx, dto);
			}
			//校验名称
			FileType queryName = new FileType();
			queryName.setFileTypeName(dto.getFileTypeName());
			List<FileType> queryNameList = fileTypeMapper.queryByCondition(queryName);
			if (queryNameList != null && queryNameList.size() > 1) {
				throw new NullPointerException();
			}
			//校验代码
			FileType queryCode = new FileType();
			queryCode.setFileTypeCode(dto.getFileTypeCode());
			List<FileType> queryCodeList = fileTypeMapper.queryByCondition(queryCode);
			if (queryCodeList != null && queryCodeList.size() > 1) {
				throw new ClassNotFoundException();
			}
		} catch (NullPointerException e1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
			responseData.setSuccess(false);
			responseData.setMessage("已存在同名的文件类型名称！");
			//return responseData;
		} catch (ClassNotFoundException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
			responseData.setSuccess(false);
			responseData.setMessage("已存在同名的文件类型代码！");
			//return responseData;
		} 
		return responseData;
		
		/*FileType queryName = new FileType();
		FileType queryCode = new FileType();
		if ("add".equals(dto.get__status())) {
			queryName.setFileTypeName(dto.getFileTypeName());
			List<FileType> queryNameList = fileTypeMapper.queryByCondition(queryName);
			if (queryNameList.isEmpty()) {
				queryCode.setFileTypeCode(dto.getFileTypeCode());
				List<FileType> queryCodeList = fileTypeMapper.queryByCondition(queryCode);
				if (queryCodeList.isEmpty()) {
					self().insertSelective(requestCtx, dto);
				} else {
					responseData.setSuccess(false);
					responseData.setMessage("已存在同名的文件类型代码！");
				}
			} else {
				responseData.setSuccess(false);
				responseData.setMessage("已存在同名的文件类型名称！");
			}
		} else if ("update".equals(dto.get__status())) {
			queryName.setFileTypeName(dto.getFileTypeName());
			List<FileType> queryNameList = fileTypeMapper.queryByCondition(queryName);
			if (queryNameList.isEmpty()) {
				queryCode.setFileTypeCode(dto.getFileTypeCode());
				List<FileType> queryCodeList = fileTypeMapper.queryByCondition(queryCode);
				if (queryCodeList.isEmpty()) {
					self().insertSelective(requestCtx, dto);
				} else {
					responseData.setSuccess(false);
					responseData.setMessage("已存在同名的文件类型代码！");
				}
			} else if (queryNameList != null && queryNameList.size() == 1) {
				
			} {
				responseData.setSuccess(false);
				responseData.setMessage("已存在同名的文件类型名称！");
			}
			self().updateByPrimaryKey(requestCtx, dto);
		}*/
		
	}


	
	@Override
	public List<DropDownListDto> queryFileTypeName(String fileTypeName) {
		return fileTypeMapper.queryFileTypeName(fileTypeName);
	}

}