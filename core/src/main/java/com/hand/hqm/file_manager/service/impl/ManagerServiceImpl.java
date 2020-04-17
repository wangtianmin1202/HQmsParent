package com.hand.hqm.file_manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.file_manager.mapper.ManagerMapper;
import com.hand.hqm.file_manager.service.IManagerService;
import com.hand.hqm.file_manager_classify.dto.ManagerClassify;
import com.hand.hqm.file_manager_classify.mapper.ManagerClassifyMapper;
import com.hand.hqm.file_manager_classify.service.IManagerClassifyService;
import com.hand.hqm.file_manager_his.dto.ManagerHis;
import com.hand.hqm.file_manager_his.mapper.ManagerHisMapper;
import com.hand.hqm.file_manager_his.service.IManagerHisService;
import com.hand.hqm.file_upload.dto.FileUpload;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class ManagerServiceImpl extends BaseServiceImpl<Manager> implements IManagerService{
	
	
	 @Autowired
	 ManagerMapper managerMapper ;
	 @Autowired
	 IManagerService  ManagerService ;
	 @Autowired 
	 ManagerClassifyMapper managerClassifyMapper;
	 @Autowired 
	 IManagerClassifyService ManagerClassifyService;
	 @Autowired 
	 ManagerHisMapper managerHisMapper;
	 @Autowired
	 IManagerHisService ManagerHisService;
	  /**
      *  新增文件
    * @param dto 文件表集合
    * @param request 请求
    * @return 请求结果
    */
	 
	 public ResponseData addNew(Manager dto,IRequest requestCtx, HttpServletRequest request) {  
    	ResponseData  responseData=new ResponseData();
   	
    	Manager head =new Manager();
    	Manager receive =new Manager();
		head.setFileNum(dto.getFileNum());
		List<Manager> receives=managerMapper.select(head);
		if(receives.size()==0)
		{   
		/*	String str = dto.getClassifyIds();
	        String[] strs=str.split(",");*/
	   /*  for(int j=0;j<strs.length;j++){*/
			receive.setFileNum(dto.getFileNum());
			receive.setFileTitle(dto.getFileTitle());
			receive.setEnableFlag("Y");
			receive.setEdition("A");
			receive.setRemark(dto.getRemark());
			ManagerService.insertSelective(requestCtx,receive);
			responseData.setSuccess(true);
			responseData.setMessage("新建成功");
			return responseData;
		/*}*/
		}else
		{
			responseData.setSuccess(false);
			responseData.setMessage("文件编号已存在");
			return responseData;
					
		}
		
 } 
	/* @Override
		public List<MenuItem> queryTreeData(IRequest requestContext, Classify dto) {
			// 查询根数据
			List<Classify> classify = classifyMapper.selectParentInvalid(dto);
			// 查询下层数据
			List<MenuItem> menuItems = castToMenuItem(classify);
			return menuItems;
		}
    */
	    /**
	     *  查询文件表
	   * @param dto 文件表集合
	   * @param request 请求
	   * @return 请求结果
	   */
    @Override
    public List<Manager> myselect(IRequest requestContext, Manager dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return managerMapper.myselect(dto);
	 }
    /**
     *  文件上传
   * 
   * @param request 请求
   * @return 请求结果
   */
 // TODO 文件上传
    @Override
	public ResponseData fileUpload(IRequest requestCtx,HttpServletRequest request) {
		
		ResponseData responseData = new ResponseData();
		List<String> rows = new ArrayList<String>();
    	
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    	
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		//校验编号
		Manager manager= new Manager();
		manager.setFileNum(request.getParameter("fileNum"));
		List<Manager> ms =managerMapper.select(manager);
		if(ms.size()>0)
		{
			responseData.setMessage("该文件编号已存在");
			responseData.setSuccess(false);
			return responseData;
		}	
		//先往主表中插值 以获取id
		Manager dimensionUploadFiles = new Manager();
		dimensionUploadFiles.setFileNum(request.getParameter("fileNum"));
		dimensionUploadFiles.setFileTitle(request.getParameter("fileTitle"));
		dimensionUploadFiles.setRemark(request.getParameter("remark"));
		dimensionUploadFiles.setExpirationDate(stringToDate(request.getParameter("expirationDate")));
		dimensionUploadFiles.setEdition("A");	
		dimensionUploadFiles.setEnableFlag("Y");
		
		self().insertSelective(requestCtx, dimensionUploadFiles);
		
		//服务器路径
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/filemanager/file_"+dimensionUploadFiles.getFileId()+"_"+dimensionUploadFiles.getEdition()+"/";//注意不要多空格！
		String path = rootPath + endPath;
		if(SystemApiMethod.getOsType().equals("window")) {
			//本地路径
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/filemanager/file_"+dimensionUploadFiles.getFileId()+"_"+dimensionUploadFiles.getEdition()+"/";
			path = rootPath + endPath;
		}
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		/*List<InspectionAttribute> returnList = new ArrayList<InspectionAttribute>();*/
		if(fileMap.size()>1)
		{
			responseData.setMessage("一次只能上传一个文件");
			responseData.setSuccess(false);
			return responseData;
			
		}
		for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			rows.add(entry.getValue().getOriginalFilename());
			MultipartFile forModel = entry.getValue();
			if(forModel.getOriginalFilename()==null)
			{
				responseData.setMessage("请选择上传文件");
				responseData.setSuccess(false);
				return responseData;
				
				
			}
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
				
				String isd =request.getParameter("classifyIds");
				String[]  strs=isd.split(",");
				
				ManagerHis managerHis = new ManagerHis();
				
				managerHis.setFileNum(request.getParameter("fileNum"));
				managerHis.setFileTitle(request.getParameter("fileTitle"));
				managerHis.setExpirationDate(stringToDate(request.getParameter("expirationDate")));
				managerHis.setFileUrl(endPath + entry.getValue().getOriginalFilename());
				managerHis.setEdition("A");	
				managerHis.setUploadDate(new Date());
				managerHis.setEnableFlag("Y");	
				managerHis.setFileId(dimensionUploadFiles.getFileId());
				ManagerHisService.insertSelective(requestCtx,managerHis);
				
				for(int j=0;j<strs.length;j++)
				{
					ManagerClassify managerClassify =new ManagerClassify();
					managerClassify.setFileId(dimensionUploadFiles.getFileId());
					managerClassify.setClassifyId(Float.valueOf(strs[j].toString()));
				   
					ManagerClassifyService.insertSelective(requestCtx,managerClassify);
				}
							   	
			}
		}
		responseData.setSuccess(true);
		responseData.setMessage(String.valueOf(dimensionUploadFiles.getFileId()));
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
	
	@Override
	public ResponseData activityUpload(IRequest requestCtx, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		String documentUrl = "";
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		//校验上传的文件数量
		if(fileMap.size()>1){
			responseData.setMessage("一次只能上传一个文件");
			responseData.setSuccess(false);
			return responseData;
		}
		//List<String> rows = new ArrayList<String>();
    	
		
		//获取主键最大值，+1得当前需要保存的文件的id
		Float maxFileId = managerMapper.selectMaxFileId() + 1;
		
		//服务器路径
		String rootPath = "/apps/hap/resource";
		String endPath = "/demension/filemanager/file_"+maxFileId+"_A"+"/";//注意不要多空格！
		String path = rootPath + endPath;
		if(SystemApiMethod.getOsType().equals("window")) {
			//本地路径
			rootPath = "C:/apps/hap/resource";
			endPath = "/demension/filemanager/file_"+maxFileId+"_A"+"/";
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
			} catch (IllegalStateException | IOException e) {
				// TODO 文件保存异常
				responseData.setMessage(e.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}
		}
		Manager dimensionUploadFiles = new Manager();
		dimensionUploadFiles.setEdition("A");	
		//失效状态为失效，只有当流程审批通过后，将其变为有效
		dimensionUploadFiles.setEnableFlag("N");
		dimensionUploadFiles.setFileUrl(documentUrl);
		dimensionUploadFiles = self().insertSelective(requestCtx, dimensionUploadFiles);
		//封装返回结果
		/*FileUpload fileUpload = new FileUpload();
		fileUpload.setDocumentUrl(documentUrl);
		fileUpload.setFileId(dimensionUploadFiles.getFileId());
		responseData.setRows(Arrays.asList(fileUpload));*/
		responseData.setRows(Arrays.asList(dimensionUploadFiles));
		responseData.setSuccess(true);
		return responseData;
	}
	
	@Override
	public List<Manager> queryFileUpload(IRequest requestContext, FileUpload dto) {
		List<Manager> list = new ArrayList<>();
		if (dto.getFileIdList() != null && dto.getFileIdList() != "") {
			String[] fileIdStr = dto.getFileIdList().split(",");
			for(int i = 0; i < fileIdStr.length; i++) {
				Manager manager = new Manager();
				manager.setFileId(Float.parseFloat(fileIdStr[i]));
				manager = self().selectByPrimaryKey(requestContext, manager);
				list.add(manager);
			}
		}
		return list;
	} 
    
}