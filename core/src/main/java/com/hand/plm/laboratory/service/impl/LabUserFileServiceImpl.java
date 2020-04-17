package com.hand.plm.laboratory.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.file_manager.dto.Manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.hand.plm.laboratory.dto.LabUser;
import com.hand.plm.laboratory.dto.LabUserFile;
import com.hand.plm.laboratory.service.ILabUserFileService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class LabUserFileServiceImpl extends BaseServiceImpl<LabUserFile> implements ILabUserFileService{
	
	
	@Override
	public ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request,Float labUserId){
		ResponseData responseData = new ResponseData();
		String documentUrl = "";
		String documentName = "";
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		//校验上传的文件数量
		if(fileMap.size()>1){
			responseData.setMessage("一次只能上传一个文件");
			responseData.setSuccess(false);
			return responseData;
		}

		
		//服务器路径
		String rootPath = "/apps/hap/resource/demension";
		String endPath = "/lab_manager/";//注意不要多空格！
		String path = rootPath + endPath;
		if(SystemApiMethod.getOsType().equals("window")) {
			//本地路径
			rootPath = "C:/apps/hap/resource/demension";
			endPath = "/lab_manager/";
			path = rootPath + endPath;
		}
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			if(forModel.getOriginalFilename()==null){
				responseData.setMessage("请选择上传文件");
				responseData.setSuccess(false);
				return responseData;
			}
			File file = new File(path,forModel.getOriginalFilename());
			/*boolean newFileFlag = true;
			if(file.exists()) {
				file.delete();
				newFileFlag = false;
			}*/
			try {
				forModel.transferTo(file);
				documentUrl = endPath + entry.getValue().getOriginalFilename();
				documentName = entry.getValue().getOriginalFilename();
			} catch (IllegalStateException | IOException e) {
				// TODO 文件保存异常
				responseData.setMessage(e.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}
		}
		
		LabUserFile userFiles = new LabUserFile();
		userFiles.setFilePath(documentUrl);
		userFiles.setFileName(documentName);
		// 新增用户没有办法获取用户labUserid,故只能在点击保存的时候存入从表数据
		// labUserid=-1是在新增用户的时候的值，没法获取用户ID，故跳过保存附件从表数据，其他值是在查看附件的时候进行操作的，进行保存从表数据
		if(labUserId!=-1) {
			userFiles.setLabUserId(labUserId);
			userFiles = self().insertSelective(requestCtx, userFiles);
		}
		
		//封装返回结果
		responseData.setRows(Arrays.asList(userFiles));
		responseData.setSuccess(true);
		return responseData;
	}
	
	
	
	// 删除文件
	@Override
	public ResponseData deleteFile(String filePath) {
		ResponseData responseData = new ResponseData(false);
		//服务器路径
		String rootPath = "/apps/hap/resource/demension";
		if(SystemApiMethod.getOsType().equals("window")) {
			//本地路径
			rootPath = "C:/apps/hap/resource/demension";
		}
		File file = new File(rootPath+filePath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        responseData.setSuccess(true); 
	    }  
		
		return responseData;
		
	}
	

	
}