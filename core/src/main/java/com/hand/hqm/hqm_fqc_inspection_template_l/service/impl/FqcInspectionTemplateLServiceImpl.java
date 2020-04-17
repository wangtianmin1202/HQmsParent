package com.hand.hqm.hqm_fqc_inspection_template_l.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fqc_inspection_template_l.dto.FqcInspectionTemplateL;
import com.hand.hqm.hqm_fqc_inspection_template_l.service.IFqcInspectionTemplateLService;
import com.hand.hqm.hqm_fqc_inspection_template_l.mapper.FqcInspectionTemplateLMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcInspectionTemplateLServiceImpl extends BaseServiceImpl<FqcInspectionTemplateL> implements IFqcInspectionTemplateLService{
	@Autowired
	FqcInspectionTemplateLMapper fqcInspectionTemplateLMapper;
	
	@Override
	public List<FqcInspectionTemplateL> myselect(IRequest requestContext, FqcInspectionTemplateL dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return fqcInspectionTemplateLMapper.myselect(dto);
	}
	@Override
	public List<FqcInspectionTemplateL> historynumberUpdate(IRequest request, @StdWho List<FqcInspectionTemplateL> list) {
//        IBaseService<FqcInspectionTemplateL> self = ((IBaseService<FqcInspectionTemplateL>) AopContext.currentProxy());
        for (FqcInspectionTemplateL t : list) {
            switch (((BaseDTO) t).get__status()) {
            case DTOStatus.ADD:
                self().insertSelective(request, t);
                break;
            case DTOStatus.UPDATE:
                if (useSelectiveUpdate()) {
                	t.setHistoryNum(t.getHistoryNum()+1);
                    self().updateByPrimaryKeySelective(request, t);
                } else {
                	t.setHistoryNum(t.getHistoryNum()+1);
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
}