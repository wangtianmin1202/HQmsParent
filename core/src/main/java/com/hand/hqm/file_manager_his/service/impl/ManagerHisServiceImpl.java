package com.hand.hqm.file_manager_his.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.file_manager.mapper.ManagerMapper;
import com.hand.hqm.file_manager_classify.dto.ManagerClassify;
import com.hand.hqm.file_manager_his.dto.ManagerHis;
import com.hand.hqm.file_manager_his.mapper.ManagerHisMapper;
import com.hand.hqm.file_manager_his.service.IManagerHisService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class ManagerHisServiceImpl extends BaseServiceImpl<ManagerHis> implements IManagerHisService{
	@Autowired
	ManagerHisMapper managerHisMapper ;
	@Autowired
	ManagerMapper managerMapper ;
	@Override
	    public List<ManagerHis> myselect(IRequest requestContext, ManagerHis dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return managerHisMapper.myselect(dto);
		 }
	
	// TODO 文件上传
    @Override
	public ResponseData fileUpload(IRequest requestCtx,HttpServletRequest request) {
		
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();
    	
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    	
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		//先往主表中插值 以获取id
		Manager dimensionUploadFiles = new Manager();
		dimensionUploadFiles =managerMapper.selectByPrimaryKey(Float.valueOf(request.getParameter("fileId")));
		//获取最新版本
		String version =dimensionUploadFiles.getEdition();
		String str ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int ii = str.indexOf(version);
		String fir = String.valueOf(str.charAt(ii+1)) ;
        //更新业务表	
		dimensionUploadFiles.setRemark(request.getParameter("remark"));	
		dimensionUploadFiles.setExpirationDate(stringToDate(request.getParameter("expirationDate")));
		dimensionUploadFiles.setEdition(fir);		
		managerMapper.updateByPrimaryKeySelective(dimensionUploadFiles);
		
		//服务器路径
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/filemanager/file"+dimensionUploadFiles.getFileId()+"/";
		String path = rootPath + endPath;
		if(SystemApiMethod.getOsType().equals("window")) {
			//本地路径
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/problemdescription/file"+dimensionUploadFiles.getFileId()+dimensionUploadFiles.getEdition()+"/";
			path = rootPath + endPath;
		}
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		/*List<InspectionAttribute> returnList = new ArrayList<InspectionAttribute>();*/
		for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getOriginalFilename());
			MultipartFile forModel = entry.getValue();
			File file = new File(path,forModel.getOriginalFilename());
			boolean newFileFlag = true;
			if(file.exists()) {
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
			if(newFileFlag) {
				
				ManagerHis managerHiss = new ManagerHis();
				//将之前的历史置于失效
				managerHiss.setFileId(Float.valueOf(request.getParameter("fileId")));
				managerHiss.setExpirationDate(stringToDate(request.getParameter("expirationDate")));
				List<ManagerHis> manager = managerHisMapper.select(managerHiss);
				for(ManagerHis managerH :manager)
				{
					managerH.setEnableFlag("N");
					managerHisMapper.updateByPrimaryKeySelective(managerH);		
				}
				ManagerHis managerHis = new ManagerHis();
				managerHis.setFileNum(dimensionUploadFiles.getFileNum());
				managerHis.setFileTitle(dimensionUploadFiles.getFileTitle());
				managerHis.setFileUrl(endPath + entry.getValue().getOriginalFilename());
				managerHis.setEdition(fir);	
				managerHis.setUploadDate(new Date());
				managerHis.setEnableFlag("Y");				
				managerHis.setFileId(Float.valueOf(request.getParameter("fileId")));
				managerHisMapper.insertSelective(managerHis);			   	
			}
		}
		return responseData;
	}
    
    private Date stringToDate (String sting) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date ts= null;//创建Date对象
    	try {
    	ts = sdf.parse(sting);//转换为Date类型 这里要抛一个异常
    	} catch (Exception e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    	} 
        return ts;
    } 
    
}