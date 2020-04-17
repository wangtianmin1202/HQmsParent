package com.hand.hqm.hqm_fqc_inspection_template_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_item_category.mapper.ItemCategoryMapper;

import java.util.Date;
import java.util.List;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fqc_inspection_template_h.dto.FqcInspectionTemplateH;
import com.hand.hqm.hqm_fqc_inspection_template_h.service.IFqcInspectionTemplateHService;
import com.hand.hqm.hqm_fqc_inspection_template_l.dto.FqcInspectionTemplateL;
import com.hand.hqm.hqm_fqc_inspection_template_l.mapper.FqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_fqc_inspection_template_l.service.IFqcInspectionTemplateLService;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_item_category_ext.dto.ItemCategoryExt;
import com.hand.hqm.hqm_item_category_ext.mapper.ItemCategoryExtMapper;
import com.hand.hqm.hqm_fqc_inspection_template_h.mapper.FqcInspectionTemplateHMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcInspectionTemplateHServiceImpl extends BaseServiceImpl<FqcInspectionTemplateH> implements IFqcInspectionTemplateHService{
	@Autowired
	FqcInspectionTemplateHMapper fqcInspectionTemplateHMapper;
	@Autowired
	FqcInspectionTemplateLMapper fqcInspectionTemplateLMapper;
	@Autowired
	IFqcInspectionTemplateLService iFqcInspectionTemplateLService;
	@Autowired
	ItemCategoryMapper itemCategoryMapper;
	@Autowired
	ItemCategoryExtMapper itemCategoryExtMapper;
	@Autowired
	InspectionAttributeMapper inspectionAttributeMapper;
	@Override
	public List<FqcInspectionTemplateH> myselect(IRequest requestContext,FqcInspectionTemplateH dto,int page, int pageSize){
		PageHelper.startPage(page, pageSize);
        return fqcInspectionTemplateHMapper.myselect(dto);
	}
	@Override
	public List<FqcInspectionTemplateH> versionNumberBatchUpdate(IRequest request,
			@StdWho List<FqcInspectionTemplateH> list) {
        for (FqcInspectionTemplateH t : list) {
            switch (((BaseDTO) t).get__status()) {
            case DTOStatus.ADD:
                self().insertSelective(request, t);
                this.createTemplateL(t);
                break;
            case DTOStatus.UPDATE:
                if (useSelectiveUpdate()) {
                    self().updateByPrimaryKeySelective(request, t);
                } else {
                    self().updateByPrimaryKey(request, t);
                }
                break;
            case DTOStatus.DELETE:
                self().deleteByPrimaryKey(t);
                break;
            default:
                break;
            }
        }
        return list;
	}
	public void createTemplateL(FqcInspectionTemplateH templateH) {
		ItemCategoryExt categoryQuery = new ItemCategoryExt();
		categoryQuery.setItemCategory(templateH.getItemCategory());
		categoryQuery.setSourceType(templateH.getSourceType());
		List<ItemCategoryExt> categoryList = itemCategoryExtMapper.select(categoryQuery);
		if(categoryList==null)return;
		Float orderCode = 0f;
		for(ItemCategoryExt itemCategoryExt : categoryList) {
			orderCode++;
			InspectionAttribute  inspectionAttributeSearch = new InspectionAttribute();
			inspectionAttributeSearch.setAttributeId(itemCategoryExt.getAttributeId());
			InspectionAttribute  inspectionAttributeResult = inspectionAttributeMapper.selectByPrimaryKey(inspectionAttributeSearch);
			if(inspectionAttributeResult == null)continue;
			FqcInspectionTemplateL addTemplateL = new FqcInspectionTemplateL();
			addTemplateL.setHeaderId(templateH.getHeaderId());
			addTemplateL.setEnableType("D");
			addTemplateL.setOrderCode(orderCode);
			addTemplateL.setEnableTime(new Date());
			addTemplateL.setAttributeId(inspectionAttributeResult.getAttributeId());
			fqcInspectionTemplateLMapper.insertSelective(addTemplateL);
			
		}
	}
	@Override
	public void updateStatus(IRequest requestCtx, List<FqcInspectionTemplateH> dto) {
		for(FqcInspectionTemplateH in : dto) {
			FqcInspectionTemplateH search = new FqcInspectionTemplateH();
			search.setHeaderId(in.getHeaderId());
			search = fqcInspectionTemplateHMapper.selectOne(search);
			FqcInspectionTemplateH updateDto = new FqcInspectionTemplateH();
			updateDto.setHeaderId(search.getHeaderId());
			updateDto.setHistoryNum(search.getHistoryNum()+1);
			updateDto.setStatus("2");
			self().updateByPrimaryKeySelective(requestCtx, updateDto);
		}
		
	}
	@Override
	public int reBatchDelete(List<FqcInspectionTemplateH> list) {
        int c = 0;
        for (FqcInspectionTemplateH t : list) {
        	FqcInspectionTemplateL search = new FqcInspectionTemplateL();
        	search.setHeaderId(t.getHeaderId());
        	List<FqcInspectionTemplateL> lineList = fqcInspectionTemplateLMapper.select(search);
        	iFqcInspectionTemplateLService.batchDelete(lineList);
            c += self().deleteByPrimaryKey(t);
        }
        return c;
		
	}

}