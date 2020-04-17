package com.hand.plm.spec_product_detail.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.OldCellRecord;
import org.apache.regexp.recompile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.plm.spec_product_detail.constant.PlmSpecApprovalStatus;
import com.hand.plm.spec_product_detail.dto.SpecChangeOrder;
import com.hand.plm.spec_product_detail.dto.SpecProductDetail;
import com.hand.plm.spec_product_detail.mapper.SpecProductDetailMapper;
import com.hand.plm.spec_product_detail.service.ISpecChangeOrderService;
import com.hand.plm.spec_product_detail.service.ISpecProductDetailService;
import com.hand.plm.spec_product_detail.service.ISpecProductPenddingService;
import com.hand.plm.spec_product_detail.util.controllers.ExportExcelUtil;
import com.hand.plm.spec_product_detail.util.dto.FormTextDto;
import com.hand.plm.spec_product_detail.util.dto.FormTitleDto;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SpecProductDetailServiceImpl extends BaseServiceImpl<SpecProductDetail> implements ISpecProductDetailService{
	@Autowired
	private SpecProductDetailMapper productDetailMapper;
	
	@Autowired
	private ISpecChangeOrderService changeOrderService;
	
	@Autowired
	private ISpecProductDetailService productDetailService;
	
	@Autowired
	private ISpecProductPenddingService productPenddingService;
	
	@Override
	public List<SpecProductDetail> selectTreeDatas(IRequest requestContext) {
		return productDetailMapper.selectTreeDatas();
	}
	
	@Override
	public List<SpecProductDetail> queryAll(IRequest requestContext,SpecProductDetail condition, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<String> specDesList = new ArrayList<>();
		List<String> specLineIdList = new ArrayList<>();
		List<SpecProductDetail> productDetailList = new ArrayList<>();
		//修改复制时查询
		if(condition.getSpecLineIds() != null) {
			specLineIdList = Arrays.asList(condition.getSpecLineIds().split(";")); 
		/*	specLineIdList = stringToLongList(condition.getSpecLineIds());*/
		}
		if("动态加载".equals(condition.getChangeType()) && condition.getSpecId() != null) {
			specLineIdList = productDetailMapper.queryLastSpecId(condition.getSpecId());
			if(specLineIdList.size() <= 0) {
				//若是最子代则直接用specLineId查询
				specLineIdList.add(String.valueOf(condition.getSpecLineId()));
			}
		}
		productDetailList = productDetailMapper.queryAll(condition.getTmp(), specLineIdList);
		for (SpecProductDetail specProductDetail : productDetailList) {
			specDesList = Arrays.asList(specProductDetail.getSpecDescription().split(";"));
			specProductDetail.setSpecDescription01(specDesList.get(0));
			specProductDetail.setSpecDescription02(specDesList.get(1));
			specProductDetail.setSpecDescription03(specDesList.get(2));
			specProductDetail.setSpecDescription04(specDesList.get(3));
		}
		return productDetailList;
	}
	

    @Override
    public void exportData(IRequest iRequest, SpecProductDetail dto, HttpServletRequest request, HttpServletResponse response) {
        List<SpecProductDetail> specAlls = new ArrayList<>();
        specAlls = queryAll(iRequest,dto,1,9999);
        
        String sheetName1 = "SPEC基础数据库";

        //开始设置标题,第一个参数是在excel显示的标题,第二个参数是对应的dto中的属性值,第三个dto是这个标题在excel中的坐标
        FormTitleDto sheet1Title1 = new FormTitleDto("产品品类", "specDescription01","0,1,0,0");
        FormTitleDto sheet1Title0 = new FormTitleDto("功能大类", "specDescription02",(short)8000,"0,1,1,1");
        FormTitleDto sheet1Title2 = new FormTitleDto("功能小类", "specDescription03","0,1,2,2");
        FormTitleDto sheet1Title3 = new FormTitleDto("功能/分类", "specDescription04","0,1,3,3");
        FormTitleDto sheet1Title4 = new FormTitleDto("要求明细", "demandMsg",(short)9200,"0,1,4,4");
        FormTitleDto sheet1Title5 = new FormTitleDto("特性分类", "attachment",(short)6200,"0,1,5,5");
        FormTitleDto sheet1Title6 = new FormTitleDto("标准/测试方法", "stdTestMethod",(short)6200,"0,1,6,6");
        FormTitleDto sheet1Title7 = new FormTitleDto("Spec代码", "specCode",(short)6200,"0,1,7,7");
        FormTitleDto sheet1Title8 = new FormTitleDto("下限", "minValue","0,1,8,8");
        FormTitleDto sheet1Title9 = new FormTitleDto("上限", "maxValue","0,1,9,9");
        FormTitleDto sheet1Title10 = new FormTitleDto("单位", "uom","0,1,10,10");
        FormTitleDto sheet1Title11 = new FormTitleDto("审批状态", "approvalStatus","0,1,11,11");
        FormTitleDto sheet1Title12 = new FormTitleDto("版本", "version","0,1,12,12");

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
        sheet1FormTitleDtolist.add(sheet1Title9);
        sheet1FormTitleDtolist.add(sheet1Title10);
        sheet1FormTitleDtolist.add(sheet1Title11);
        sheet1FormTitleDtolist.add(sheet1Title12);

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
        sheet1Map.put("data", specAlls);

        List saveSheetList = new ArrayList();
        saveSheetList.add(sheet1Map);

        //调用导出多sheet工具类导出excel
        ExportExcelUtil.startDownLoadForSheets(request, response, saveSheetList, "SPEC基础数据库");
    }
    
/*    *//**
     * String字符串转成List<Long>数据格式
     * @param strArr
     * @return
     *//*
    private List<Long> stringToLongList(String strArr) {
        return Arrays.stream(strArr.split(","))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
    }*/
    
    @Override
    public void updateSpecLineIdStatus(IRequest iRequest, List<SpecProductDetail> details){
    	//1.更新明细表中的status 审批中
    	productDetailMapper.updateSpecLineIdStatus(details);
    	//2.同时，查询出当条数据，插入变更的头行表
    	batchUpdateSpecChange(iRequest,details);
    }
    
    /**
     * 根据明细数据去生成变更头行表、复制一条到变更头行表（抽成一个方法）
     * @param iRequest
     * @param details
     */
    @Override
    public void batchUpdateSpecChange(IRequest iRequest, List<SpecProductDetail> details){
    	//插入变更的头行表
    	//变更头表：(存在问题，这种情况只满足同一层级、4级下，不同数据的一起变更，不满足跨层级变更)
    	SpecChangeOrder changeOrder = new SpecChangeOrder();
		//变更头表信息
		changeOrder.setProductName(details.get(0).getSpecDescription01());
		changeOrder.setSpecClause(details.get(0).getSpecDescription04());
		changeOrder.setChangeType(details.get(0).getChangeType());
		changeOrder.setApprovalStatus(PlmSpecApprovalStatus.DRAFT_VALUE);
		changeOrder.setChangeOrderCode(changeOrderService.getChangeOrderCode());
		changeOrder = changeOrderService.insertSelective(iRequest, changeOrder);
    	for (SpecProductDetail specProductDetail : details) {
    		//通过lineId去复制一条明细数据到变更行表里
    		if(specProductDetail.getKid() != null) {
    			productDetailMapper.insertSpecChangeLinePen(changeOrder.getChangeOrderId(), specProductDetail.getSpecLineId());
    		}else{
    			productDetailMapper.insertSpecChangeLineDdt(changeOrder.getChangeOrderId(), specProductDetail.getSpecLineId());
    		}
    		
		}
    }
    
    @Override//拿到数据存草稿表（需要拿到树结构id，然后存入草稿表明细数据）
    public List<SpecProductDetail> batchUpdateDetail(IRequest iRequest, List<SpecProductDetail> details){
    	for (SpecProductDetail specProductDetail : details) {
    		if(specProductDetail.getSpecLineId() == null) {
    			specProductDetail.setSpecLineId(productDetailMapper.selectKey());
    		}
    		//判断是否为第一次修改
			SpecProductDetail sProductDetail = new SpecProductDetail();
			List<SpecProductDetail> list = new ArrayList<>();
			sProductDetail.setSpecLineId(specProductDetail.getSpecLineId());
			list =productDetailMapper.select(sProductDetail);
			
			if(list.size() > 0) {
				//多次修改只新增一条数据
				specProductDetail.set__status("update");
			}
    		
		}
    	
    	//新增、复制-》新增一条到草稿表（抽成一个方法）
    	details = productDetailService.batchUpdate(iRequest, details);
    	return details;
    }
    
    @Override
    public List<SpecProductDetail> selectTreeChild(IRequest iRequest, List<SpecProductDetail> details){
    	return productDetailMapper.selectTreeChild(details.get(0).getSpecId());
    }
    
    @Override
    public List<SpecProductDetail> selectLevelInfo(IRequest iRequest, Long levelNum, Long parentSpecId){
    	List<SpecProductDetail> productDetail = new ArrayList<>();
    	if(parentSpecId != null && !parentSpecId.equals(0L)) {
    		productDetail = productDetailMapper.selectLevelInfoById(parentSpecId);
    	}else {
    		productDetail = productDetailMapper.selectLevelInfo(levelNum);
    	}
    	return productDetail;
    }
    
    @Override
	public List<SpecProductDetail> queryAllPendding(IRequest iRequest, String changeType) {
    	//查询草稿数据
    	List<SpecProductDetail> productDetailList = new ArrayList<>();
    	List<String> specDesList = new ArrayList<>();
    	if("修改".equals(changeType)) {
    		productDetailList = productDetailMapper.queryAllPendding();
    	}else if ("废止".equals(changeType)) {
    		productDetailList = productDetailMapper.queryAllHis();
		}
		
		for (SpecProductDetail specProductDetail : productDetailList) {
			specDesList = Arrays.asList(specProductDetail.getSpecDescription().split(";"));
			specProductDetail.setSpecDescription01(specDesList.get(0));
			specProductDetail.setSpecDescription02(specDesList.get(1));
			specProductDetail.setSpecDescription03(specDesList.get(2));
			specProductDetail.setSpecDescription04(specDesList.get(3));
		}
		return productDetailList;
	}
    
    
    @Override
	public List<SpecProductDetail> selectParentTree(IRequest iRequest) {
		return productDetailMapper.selectParentTree();
	}
    
    @Override
   	public Long hisCount(IRequest iRequest) {
   		return productDetailMapper.hisCount();
   	}
}


