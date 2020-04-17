package com.hand.plm.laboratory.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.laboratory.dto.LabUser;
import com.hand.plm.laboratory.dto.LabUserFile;
import com.hand.plm.laboratory.dto.LabUserOldScore;
import com.hand.plm.laboratory.dto.LabUserPost;
import com.hand.plm.laboratory.service.ILabUserFileService;
import com.hand.plm.laboratory.service.ILabUserOldScoreService;
import com.hand.plm.laboratory.service.ILabUserPostService;
import com.hand.plm.laboratory.service.ILabUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class LabUserController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(LabUserController.class);
    	
    @Autowired
    private ILabUserService service;
    @Autowired
    private ILabUserPostService postService;
    @Autowired
    private ILabUserFileService fileService;
    @Autowired
    private ILabUserOldScoreService oldService;


    @RequestMapping(value = "/plm/lab/user/query")
    @ResponseBody
    public ResponseData query(LabUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
        //return new ResponseData(service.getUserInfoList(page,pageSize));
    }

    @RequestMapping(value = "/plm/lab/user/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<LabUser> dto, BindingResult result, HttpServletRequest request){
        /*getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }*/
    	// 修改评分，将历史数据保存到历史表中
        IRequest requestCtx = createRequestContext(request);
        
        return oldService.addOldData(requestCtx, dto);
    }

    @RequestMapping(value = "/plm/lab/user/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<LabUser> dto){
    	IRequest requestCtx = createRequestContext(request);
        service.deleteUser(requestCtx, dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/plm/lab/user/create")
    @ResponseBody
    public ResponseData create(@RequestBody LabUser dto, HttpServletRequest request){
        
        IRequest requestCtx = createRequestContext(request);
        
        Float labUserId = dto.getLabUserId();
        LabUser user = service.createUser(requestCtx, dto);
        
        List<LabUser> labUserList = new ArrayList<LabUser>();
        labUserList.add(user);
        dto.setLabUserId(user.getLabUserId());
        // 插入岗位数据到从表
        postService.createUserPost(dto, requestCtx, labUserId);
        //插入附件表(-1,新增用户数据)
        if(labUserId.equals(-1f)) {
        	List<LabUserFile> userFileList = new ArrayList<LabUserFile>();
            for(String file:dto.getFilesList()) {
            	LabUserFile userFile = new LabUserFile();
            	userFile.setFilePath(file);
                userFile.setLabUserId(user.getLabUserId());
                userFile.setFileName(file.substring(file.lastIndexOf('/')+1));
                userFile.setFileDate(new Date());
                userFile.set__status("add");
                userFileList.add(userFile);
            }
            fileService.batchUpdate(requestCtx, userFileList);
        }
        
        return new ResponseData(labUserList);
    }
    
    
    
    @RequestMapping(value = "/plm/lab/user/exportExcel2")
    @ResponseBody
    public ResponseData exportExcel2(HttpServletRequest request,HttpServletResponse response){
        
        IRequest requestCtx = createRequestContext(request);
        try {
			service.excelExport(requestCtx,response,request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return new ResponseData();
    }
        
    
}