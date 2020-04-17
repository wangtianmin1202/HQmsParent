package com.hand.hqm.file_permission.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.file_classify.dto.Classify;
import com.hand.hqm.file_permission.dto.Permission;
import com.hand.hqm.file_permission.mapper.PermissionMapper;
import com.hand.hqm.file_permission.service.IPermissionService;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements IPermissionService{
	 @Autowired
	 PermissionMapper permissionMapper; 
	
	@Override

	    public List<Permission> myselect(IRequest requestContext, Permission dto, int page, int pageSize) {
		 
	        PageHelper.startPage(page, pageSize);
	        return  permissionMapper.myselect(dto);
		 }
	
	@Override

    public List<Permission> myselectline(IRequest requestContext, Permission dto, int page, int pageSize) {
	 
        PageHelper.startPage(page, pageSize);
        return  permissionMapper.myselectline(dto);
	 }
	
	/*权限校验*/
	@Override
	public ResponseData check(IRequest requestCtx, Permission dto) {
		
		//第一次查询：查找当前文件对应的 权限类型下 权限表中有无数据，若没有数据 ，所有人都有权限查看
		Permission permissionS1 =new Permission();
		permissionS1.setPermissionCode(dto.getPermissionCode());
		permissionS1.setFileId(dto.getFileId());		
		List<Permission> r1 =permissionMapper.selectlcheckone(permissionS1);	
		if(r1.size()>0)
		{
			//如果存在当前权限表中是否存在 当前权限类型 并且 角色id 不为空,且为当前， userid为空的数据，若存在，有权限，返回成功；如果不存在，进入第三步
			Permission permissionS2 =new Permission();
			permissionS2.setPermissionCode(dto.getPermissionCode());
			permissionS2.setFileId(dto.getFileId());
			List<Permission> r2 =permissionMapper.selectlchecktwo(permissionS2);
		     if(r2.size()==0)
		     {
		    	 //第三次： 查询当前用户id 是否存在于当前文件，当前权限类别下的 数据中
		    	 Permission permissionS3 =new Permission();
		    	 permissionS3.setPermissionCode(dto.getPermissionCode());
		    	 permissionS3.setFileId(dto.getFileId());
		    	 permissionS3.setUserId((float)requestCtx.getUserId());
		    	 List<Permission> r3 =permissionMapper.selectlcheckthree(permissionS3);
		    	 if(r3.size()>0)	    		 
		    	 {
		    	   return new ResponseData();  
		    	 }
		    	 else
		    	 {
		    		  ResponseData responseData =new ResponseData();
		    		  responseData.setSuccess(false);
		    		  responseData.setMessage("您的权限不足:::::::::");
		    		  return responseData;
		    		 
		    	 } 
		    	 
		     }
		     else
		     {
		    	 //判断 所获取的角色和当前id的关系在角色用户表中是否存在
		    	 for(Permission p :r2)
		    	 {
		    		 Permission p_role =new Permission();
		    		 p_role.setPermissionCode(dto.getPermissionCode());
		    		 p_role.setUserId((float)requestCtx.getUserId());
		    		 p_role.setCharacterId(p.getCharacterId());
		    		 
		    		 List<Permission> pp = permissionMapper.checkUserRole(p_role);
		    		 if(pp.size()==0)
		    		 {
		    			 Permission permissionS3 =new Permission();
				    	 permissionS3.setPermissionCode(dto.getPermissionCode());
				    	 permissionS3.setFileId(dto.getFileId());
				    	 permissionS3.setUserId((float)requestCtx.getUserId());
				    	 List<Permission> r3 =permissionMapper.selectlcheckthree(permissionS3);
				    	 if(r3.size()>0)	    		 
				    	 {
				    	      return new ResponseData();  
				    	 }
				    	 else
				    	 {
				    		  ResponseData responseData =new ResponseData();
				    		  responseData.setSuccess(false);
				    		  responseData.setMessage("您的权限不足:::::::::");
				    		  return responseData;
				    		 
				    	 } 		 
		    		 }
		    		 else
		    		 {
		    			 return new ResponseData(); 
		    			 
		    		 } 
    		 
		    	 }
		     }	
		}	
		else {	
			//符合第一种情况，有权限
		    return new ResponseData();
	     }
		return null;
	}
	
	
	@Override
	public ResponseData  addNewInspection (IRequest requestCtx,List<Permission> dto) {
		
		for(Permission row:dto)
		{	
		ResponseData responseData = new ResponseData();
		Permission permission = new Permission();
		List<Permission>   p1;
		if(row.getUserId()==null)
		{	
		
		permission.setPermissionCode(row.getPermissionCode());
		permission.setCharacterId(row.getCharacterId());
		permission.setFileId(row.getFileId());	
		
		   p1= permissionMapper.checkcountwithNull(permission);

			if( p1.size() > 0)
			{
				responseData.setSuccess(false);
				responseData.setMessage("数据重复，请勿重复添加");
				return responseData;
			}
			else 
			{
				Permission permission1 = new Permission();
				permission1.setPermissionCode(row.getPermissionCode());
				permission1.setUserId(row.getUserId());
				permission1.setCharacterId(row.getCharacterId());
				permission1.setFileId(row.getFileId());
				permissionMapper.insertSelective(permission1);
				return  responseData;
			}
		}
		else
		{
			permission.setPermissionCode(row.getPermissionCode());
			permission.setCharacterId(row.getCharacterId());
			permission.setFileId(row.getFileId());	
		    permission.setUserId(row.getUserId());
		    p1=permissionMapper.checkcount(permission);

			if( p1.size() > 0)
			{
				responseData.setSuccess(false);
				responseData.setMessage("数据重复，请勿重复添加");
				return responseData;
			}
			else 
			{
				Permission permission1 = new Permission();
				permission1.setPermissionCode(row.getPermissionCode());
				permission1.setUserId(row.getUserId());
				permission1.setCharacterId(row.getCharacterId());
				permission1.setFileId(row.getFileId());
				permissionMapper.insertSelective(permission1);
				return  responseData;
			}
		}
		
		
		
				
		
		
		}
		return null;	
	}
}
