package com.hand.npi.npi_technology.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DocSequence;
import com.hand.hap.system.service.IDocSequenceService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openxmlformats.schemas.drawingml.x2006.main.impl.CTTextUnderlineFillGroupWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_technology.dto.ComposeProduct;
import com.hand.npi.npi_technology.dto.ComposeProductMateriel;
import com.hand.npi.npi_technology.dto.ComposeProductMaterielAttr;
import com.hand.npi.npi_technology.mapper.ComposeProductMapper;
import com.hand.npi.npi_technology.mapper.ComposeProductMaterielAttrMapper;
import com.hand.npi.npi_technology.mapper.ComposeProductMaterielMapper;
import com.hand.npi.npi_technology.service.IComposeProductService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ComposeProductServiceImpl extends BaseServiceImpl<ComposeProduct> implements IComposeProductService{

	
	@Autowired
	private ComposeProductMapper composeProductMapper;
	@Autowired
	private ComposeProductMaterielMapper composeProductMaterielMapper;
	@Autowired
	private ComposeProductMaterielAttrMapper composeProductMaterielAttrMapper;
	@Autowired
	IDocSequenceService iDocSequenceService;
	@Override
	public List<ComposeProduct> addData(IRequest request, List<ComposeProduct> dtos) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		for (ComposeProduct comDto : dtos) {
			DocSequence docSequence = new DocSequence();
			docSequence.setDocType("ZHJ"+time);
			String wpCode = iDocSequenceService.getSequence(request, docSequence, "ZHJ"+time, 4, 1L);
			comDto.setAssItemCode(wpCode);
			composeProductMapper.insertSelective(comDto);
			List<TechnologyWpAction> matList = comDto.getMatList();
			ComposeProductMateriel composeProductMateriel=new ComposeProductMateriel();
			ComposeProductMaterielAttr composeProductMaterielAttr=new ComposeProductMaterielAttr();
			for (TechnologyWpAction technologyWpAction : matList) {
				composeProductMateriel.setAssItemId(comDto.getAssItemId());
				composeProductMateriel.setItemId(technologyWpAction.getItemId());
				composeProductMateriel.setItemDetailVersion(technologyWpAction.getItemDetailVersion());
				composeProductMateriel.setMatType(technologyWpAction.getMatType());
				composeProductMaterielMapper.insertSelective(composeProductMateriel);
				String materielIds = technologyWpAction.getMaterielIds();
				String[] split = materielIds.split(",");
				for (String id : split) {
					composeProductMaterielAttr.setMatAttrId(Float.valueOf(id));
					composeProductMaterielAttr.setAssMatRefId(composeProductMateriel.getAssMatRefId());
					composeProductMaterielAttrMapper.insertSelective(composeProductMaterielAttr);
				}
			}
		}
		return dtos;
	}

}