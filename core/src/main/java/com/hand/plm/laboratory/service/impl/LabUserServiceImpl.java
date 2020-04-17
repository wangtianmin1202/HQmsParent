package com.hand.plm.laboratory.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.plm.laboratory.dto.LabUser;
import com.hand.plm.laboratory.dto.LabUserFile;
import com.hand.plm.laboratory.dto.LabUserPost;
import com.hand.plm.laboratory.mapper.LabUserMapper;
import com.hand.plm.laboratory.service.ILabUserFileService;
import com.hand.plm.laboratory.service.ILabUserPostService;
import com.hand.plm.laboratory.service.ILabUserService;

import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hand.plm.spec_product_detail.util.controllers.ExportExcelUtil;
import com.hand.plm.spec_product_detail.util.dto.FormTextDto;
import com.hand.plm.spec_product_detail.util.dto.FormTitleDto;

@Service
@Transactional(rollbackFor = Exception.class)
public class LabUserServiceImpl extends BaseServiceImpl<LabUser> implements ILabUserService{

	@Autowired
	private LabUserMapper userMapper;
	@Autowired
	private ILabUserPostService userPostService;
	@Autowired
	private ILabUserFileService userFileService;
	
	@Autowired
	private ICodeService codeService;
	
	@Override
	public List<LabUser> getUserInfoList(int pageNum, int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		return userMapper.getUserInfo();
	}
	
	@Override
	public LabUser createUser(IRequest requestContext,LabUser dto){
		if(!dto.getLabUserId().equals(-1f)) {
			dto = self().updateByPrimaryKeySelective(requestContext, dto);
		}else {
			dto.setLabUserId(null);
			dto = self().insertSelective(requestContext, dto);
		}
		
		return dto;
	}
	 
	@Override
	public ResponseData deleteUser(IRequest requestContext,List<LabUser> dto) {
		if(!dto.isEmpty()) {
			//删除从表post
			LabUserPost post = new LabUserPost();
	    	post.setLabUserId(dto.get(0).getLabUserId());
	    	List<LabUserPost> postList = userPostService.select(requestContext, post, 1, 100);
	    	userPostService.batchDelete(postList);
	    	
	    	//删除从表file
	    	LabUserFile file = new LabUserFile();
	    	file.setLabUserId(dto.get(0).getLabUserId());
	    	List<LabUserFile> fileList = userFileService.select(requestContext, file, 1, 100);
	    	userFileService.batchDelete(fileList);
	    	
	    	//删除主表
	    	self().batchDelete(dto);
		}
		
    	return new ResponseData();
	}
	
	
	
	private final String sheetTitle = "实验室资源";
	private final String[] title = {"姓名","专业","学历","质量职责","工作描述","评分","好评率","及时完成率","岗位"};
	
	
	@Override
	public List<LabUser> getExcelUserData(IRequest requestContext){
		List<LabUser> userList = userMapper.getExcelUserDatas();
		
		List<CodeValue> codeList = codeService.selectCodeValuesByCodeName(requestContext, "LAB_USER_EDUCATION");
		List<CodeValue> codeList2 = codeService.selectCodeValuesByCodeName(requestContext, "LAB_USER_POST");
		
		List<LabUser> objList = new ArrayList<LabUser>();		
		for(LabUser user:userList) {		

			for(CodeValue cv:codeList) {
				if(cv.getValue().equals(user.getEducation())) {
					user.setEducation(cv.getMeaning());
				}
			}

			for(CodeValue cv2:codeList2) {
				if(cv2.getValue().equals(user.getPost1())) {
					user.setPost1(cv2.getMeaning());
				}
			}

		}

		return userList;
	}
	
	
	
	
	@Override
	public void excelExport(IRequest requestContext,HttpServletResponse response,HttpServletRequest request) throws Exception {
		
		String fileName = "实验室资源";
        //sheet名
        String sheetName = "实验室资源sheet";
 
        List<LabUser> userLists = getExcelUserData(requestContext);

        
        String sheetName1 = "sheet";

        //开始设置标题,第一个参数是在excel显示的标题,第二个参数是对应的dto中的属性值,第三个dto是这个标题在excel中的坐标
        FormTitleDto sheet1Title1 = new FormTitleDto("姓名", "userName","0,1,0,0");
        FormTitleDto sheet1Title0 = new FormTitleDto("专业", "major","0,1,1,1");
        FormTitleDto sheet1Title2 = new FormTitleDto("学历", "education","0,1,2,2");
        FormTitleDto sheet1Title3 = new FormTitleDto("质量职责", "quality","0,1,3,3");
        FormTitleDto sheet1Title4 = new FormTitleDto("工作描述", "jobDesc","0,1,4,4");
        FormTitleDto sheet1Title5 = new FormTitleDto("评分", "abilityScore","0,1,5,5");
        FormTitleDto sheet1Title6 = new FormTitleDto("好评率(%)", "praiseRate","0,1,6,6");
        FormTitleDto sheet1Title7 = new FormTitleDto("及时完成率(%)", "finishTimeRate","0,1,7,7");
        FormTitleDto sheet1Title8 = new FormTitleDto("岗位", "post1","0,1,8,8");
  

        //用来存放标题
        List<FormTitleDto> sheet1FormTitleDtolist = new ArrayList<FormTitleDto>();
        sheet1FormTitleDtolist.add(sheet1Title0);
        sheet1FormTitleDtolist.add(sheet1Title1);
        sheet1FormTitleDtolist.add(sheet1Title2);
        sheet1FormTitleDtolist.add(sheet1Title3);
        sheet1FormTitleDtolist.add(sheet1Title4);
        sheet1FormTitleDtolist.add(sheet1Title5);
        sheet1FormTitleDtolist.add(sheet1Title6);
        sheet1FormTitleDtolist.add(sheet1Title7);
        sheet1FormTitleDtolist.add(sheet1Title8);


        //这个用来设置我们文本内容的样式
        FormTextDto sheet1FormTextDto = new FormTextDto();

        //将sheet1的数据存入list中
        Map sheet1Map = new HashMap();


        //这是sheet名字
        sheet1Map.put("sheetName", sheetName1);
        //存入标题
        sheet1Map.put("formTitle", sheet1FormTitleDtolist);
        //存入字体样式
        sheet1Map.put("textStyle", sheet1FormTextDto);
        //存入要导出的数据
        sheet1Map.put("data", userLists);

        List saveSheetList = new ArrayList();
        saveSheetList.add(sheet1Map);

        //调用导出多sheet工具类导出excel
        ExportExcelUtil.startDownLoadForSheets(request, response, saveSheetList, "实验室资源");


	}
	
	
	
	
	
}