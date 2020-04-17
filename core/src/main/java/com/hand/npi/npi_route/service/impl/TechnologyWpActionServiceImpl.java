package com.hand.npi.npi_route.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_item_b.dto.ItemB;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.regexp.recompile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_route.dto.TechnologyWpActionEquipDetail;
import com.hand.npi.npi_route.mapper.TechnologyWpActionEquipDetailMapper;
import com.hand.npi.npi_route.mapper.TechnologyWpActionMapper;
import com.hand.npi.npi_route.service.ITechnologyWpActionService;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.dto.EbomMain;
import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;
import com.hand.npi.npi_technology.dto.TechnologySpecMatDetail;
import com.hand.npi.npi_technology.mapper.EbomDetailMapper;
import com.hand.npi.npi_technology.mapper.EbomMainMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecDetailMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMatDetailMapper;

import oracle.net.aso.i;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyWpActionServiceImpl extends BaseServiceImpl<TechnologyWpAction>
		implements ITechnologyWpActionService {

	@Autowired
	TechnologyWpActionMapper technologyWpActionMapper;
	@Autowired
	private TechnologySpecMatDetailMapper technologySpecMatDetailMapper;
	@Autowired
	private TechnologySpecDetailMapper technologySpecDetailMapper;
	@Autowired
	EbomMainMapper ebomMainMapper;
	@Autowired
	EbomDetailMapper ebomDetailMapper;
	@Autowired
	TechnologyWpActionEquipDetailMapper technologyWpActionEquipDetailMapper;

	@Override
	public List<TechnologyWpAction> queryWpactionList(TechnologyWpAction dto) {
		return technologyWpActionMapper.queryWpactionList(dto);
	}

	@Override
	public List<TechnologyWpAction> queryData(IRequest request, TechnologyWpAction condition, int pageNum,
			int pageSize) {
		//工艺路径页面 查询某个工序的工艺动作
		PageHelper.startPage(pageNum, pageSize);
		List<TechnologyWpAction> select = technologyWpActionMapper.queryData(condition);
		return select;
	}

	@Override
	public List<EbomDetail> queryItemLov(IRequest request, EbomMain dto, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		EbomMain eVersion = ebomMainMapper.queryNewEBomVersion(dto);
		PageHelper.startPage(pageNum, pageSize);
		List<EbomDetail> qeuryEBomPart = ebomDetailMapper.qeuryEBomPart(eVersion);
		//List<ItemB> queryItemLov = technologyWpActionMapper.queryItemLov(dto);
		return qeuryEBomPart;
	}

	@Override
	public List<TechnologySparePartDetails> queryMatAttrLov(IRequest request, TechnologyWpAction dto, int pageNum,
			int pageSize) {
		// 通过物料/组合件来获取 对应的物料属性
		// 物料的属性 从物料分类属性里面找 组合件的从组合件物料属性表里面找 属性ID总是关联 NPI_TECHNOLOGY_SPARE_PART_DETAILS表
		// 因为组件的物料属性来源于组成他的物料的物料属性
		String materielType = dto.getMatType();
		List<TechnologySparePartDetails> matAttrByMat = Lists.newLinkedList();
		if ("mat".equals(materielType)) {
			// 前台选的是物料
			matAttrByMat = technologyWpActionMapper.getMatAttrByMat(dto);
		} else {
			// 前台选的是组件
			matAttrByMat = technologyWpActionMapper.getMatAttrByComp(dto);
		}
		return matAttrByMat;
	}

	@Override
	public List<TechnologySpecDetail> checkMatAttr(String materielIds, String id) {
		// 判断这个标准动作的物料属性和勾选的物料属性是否相等
		//首先获取到有所有物料属性的 标准动作编码
		List<TechnologySpec> strActionNumberByMat = technologyWpActionMapper.getStrActionNumberByMat(materielIds);
		if (strActionNumberByMat == null || strActionNumberByMat.size() == 0) {
			return null;
		}else {
			String actionNumber = strActionNumberByMat.get(0).getStandActionId();
			if (id.equals(actionNumber)) {
				TechnologySpecDetail dto=new TechnologySpecDetail();
				dto.setSpecId(strActionNumberByMat.get(0).getSpecId());
				List<TechnologySpecDetail> select = technologySpecDetailMapper.select(dto);
				return select;
			}else {
				return null;
			}
		}
		/*String[] split = materielIds.split(",");
		TechnologySpecMatDetail te=new TechnologySpecMatDetail();
		te.setSpecId(id);
		List<TechnologySpecMatDetail> select = technologySpecMatDetailMapper.select(te);
		List<String> matList = Arrays.asList(split);
		LinkedList specList= new LinkedList();
		for (TechnologySpecMatDetail technologySpecMatDetail : select) {
			specList.add(technologySpecMatDetail.getMatId().toString());
		}
		matList.sort(Comparator.comparing(String::hashCode));  
		specList.sort(Comparator.comparing(String::hashCode));
		boolean equals = matList.toString().equals(specList.toString());
		if (equals) {
			TechnologySpecDetail selectByPrimaryKey = technologySpecDetailMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		}*/
	}

	@Override
	public List<TechnologyWpAction> queryActionInfo(IRequest request, TechnologyWpAction condition, int pageNum,
			int pageSize) {
		// 首先获取所有的工艺动作 然后获取他的属性集合 
		PageHelper.startPage(pageNum, pageSize);
		List<TechnologyWpAction> select = technologyWpActionMapper.selectInfo(condition);
		TechnologyWpActionEquipDetail equipDetail=new TechnologyWpActionEquipDetail();
		for (TechnologyWpAction dto : select) {
			Map queryMaterielInfo = technologyWpActionMapper.queryMaterielInfo(dto);
			String materielNames = String.valueOf(queryMaterielInfo.get("MATERIELNAME"));
			String materielIds = String.valueOf(queryMaterielInfo.get("MATERIELIDS"));
			dto.setMaterielIds(materielIds);
			dto.setMatAttribute(materielNames);
			equipDetail.setWpActionId(dto.getWpAuxId());
			//查询出他的工装设备
			List<TechnologyWpActionEquipDetail> select2 = technologyWpActionEquipDetailMapper.selectInfoAux(equipDetail);
			dto.setEquipList(select2);
		}
		return select;
	}

}