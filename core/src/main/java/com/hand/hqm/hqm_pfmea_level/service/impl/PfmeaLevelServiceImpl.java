package com.hand.hqm.hqm_pfmea_level.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea.mapper.FmeaMapper;
import com.hand.hqm.hqm_fmea.service.IFmeaService;
import com.hand.hqm.hqm_fmea.service.impl.FmeaServiceImpl;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_pfmea_level.dto.HqmLevelMenuItem;
import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;
import com.hand.hqm.hqm_pfmea_level.mapper.PfmeaLevelMapper;
import com.hand.hqm.hqm_pfmea_level.service.IPfmeaLevelService;
import com.thoughtworks.xstream.mapper.Mapper.Null;

import oracle.net.aso.e;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PfmeaLevelServiceImpl extends BaseServiceImpl<PfmeaLevel> implements IPfmeaLevelService{
	@Autowired
	private PfmeaLevelMapper  pfmeaLevelMapper;
	 @Autowired
	 FmeaMapper fmeaMapper;
	 /**
	     * @Author ruifu.jiang
	     * @Description 查询附着对象层级维护 树形图数据
	     * @Date 11:40 2019/8/26
	     * @Param [requestContext, dto]
	     */
	@Override
	public List<HqmLevelMenuItem> queryTreeData(IRequest requestContext, PfmeaLevel dto) {
		// 查询根数据
		List<PfmeaLevel> pfmeaLevel = pfmeaLevelMapper.selectParentInvalid(dto);
		// 查询下层数据
		List<HqmLevelMenuItem> menuItems = castToMenuItem(pfmeaLevel);
		return menuItems;
	}
	
	private List<HqmLevelMenuItem> castToMenuItem(List<PfmeaLevel> pfmeaLevel) {
		// 根
		List<HqmLevelMenuItem> menuItems = new ArrayList<>();
		pfmeaLevel.stream().forEach(dfmea -> {
			if (dfmea.getParentLevelId() == null) {
				menuItems.add(createMenuItem(dfmea));
			}
		});
		// 添加子
		menuItems.stream().forEach(item -> {
			additem(item);
		});
		return menuItems;
	}
	/**
     * 删除
     * @param dto 层级查询
     * @return 结果集
     */
	@Override
	public void deleteRow(PfmeaLevel dto) {

		// 查询后代所有需要删除的附着对象
		List<PfmeaLevel> delList = new ArrayList<>();
		delList.add(dto);
		queryDownAttachment(delList, Collections.singletonList(dto));
		// 删除
		self().batchDelete(delList);
	}
	
	public void queryDownAttachment(List<PfmeaLevel> delList, List<PfmeaLevel> dtos) {
		dtos.stream().forEach(dto -> {
			PfmeaLevel parnethQMInvalid = new PfmeaLevel();
			parnethQMInvalid.setParentLevelId(dto.getLevelId());
			parnethQMInvalid.setLevelType(dto.getLevelType());
			List<PfmeaLevel> hQMInvalids = pfmeaLevelMapper.select(parnethQMInvalid);
			if (hQMInvalids.size() > 0) {
				delList.addAll(hQMInvalids);
				queryDownAttachment(delList, hQMInvalids);
			}
		});
	}
	
	private HqmLevelMenuItem createMenuItem(PfmeaLevel pfmeaLevel) {
		HqmLevelMenuItem menu = new HqmLevelMenuItem();
		menu.setFunctionCode(pfmeaLevel.getLevelCode());
		menu.setText(pfmeaLevel.getLevelCode());
		menu.setType(pfmeaLevel.getLevelCode());
		menu.setLevelCode(pfmeaLevel.getLevelCode());
		menu.setId(pfmeaLevel.getLevelId());
		menu.setLevelId(pfmeaLevel.getLevelId());
		menu.setParentLevelId(pfmeaLevel.getParentLevelId());
		menu.setFmeaLevel (pfmeaLevel.getFmeaLevel());
		menu.setDescription(pfmeaLevel.getDescription()); 
		menu.setLevelType(pfmeaLevel.getLevelType());
		menu.setLevelPfmeaType(pfmeaLevel.getLevelPfmeaType());
		//menu.setRangeName(dfmeaDetail.getRangeName());		
		return menu;
	}
	
	private void additem(HqmLevelMenuItem menuItem) {
		// 定义子菜单
		List<HqmLevelMenuItem> children = new ArrayList<>();
		// 查询子
		PfmeaLevel pfmeaLevel = new PfmeaLevel();
		pfmeaLevel.setParentLevelId(menuItem.getLevelId());
		pfmeaLevel.setLevelType(menuItem.getLevelType());
		List<PfmeaLevel> pfmeaLevels = pfmeaLevelMapper.selectInvalidByParent(pfmeaLevel);
		// 添加子
		pfmeaLevels.stream().forEach(item -> {
			children.add(createMenuItem(item));
		});
		// 设定子菜单
		if (children.size() > 0) {
			menuItem.setChildren(children);
			// 递归，有子 继续添加

			children.stream().forEach(childrenItem -> {
				additem(childrenItem);

			});

		}
		menuItem.setChildren(children);
	}
	/**
     * 新增保存(P)
     * @param dto 层级查询
     * @param request 请求
     * @return 结果集
     */
	@Override
	public ResponseData updateOrAdd(IRequest requestCtx, PfmeaLevel dto) {
		// HQMPInvalidTree hQMInvalid = hqmpInvalidTreeMapper.selectByPrimaryKey(dto);
		ResponseData responseData = new ResponseData();
		//顶层编号唯一
		List<PfmeaLevel> list = pfmeaLevelMapper.selectFmeaLevelByLevelCode(dto.getLevelCode());
		if (list != null && list.size() > 0) {
			responseData.setSuccess(false);
			responseData.setMessage("顶层编号名称已存在");
			return responseData;
		}
		// id没有是新增
		if (null == dto.getLevelId()) {// 新插入值
			if (dto.getParentLevelId() != null) {// 有父级id 51
				PfmeaLevel pfmeaLevel = new PfmeaLevel();
				pfmeaLevel.setLevelId(dto.getParentLevelId());
				pfmeaLevel = pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel);
				if (pfmeaLevel.getParentLevelId() != null) {	//49			
					PfmeaLevel pfmeaLevel_1 = new PfmeaLevel();
					pfmeaLevel_1.setLevelId(pfmeaLevel.getParentLevelId());
					pfmeaLevel_1 = pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_1);
					if (pfmeaLevel_1.getParentLevelId() != null) { //44
					    dto.setFmeaLevel(4L);
					}else
					{
						dto.setFmeaLevel(3L);
					}
				} else {
					dto.setFmeaLevel(2L);
				} 
			} else {
				dto.setFmeaLevel(1L);					
			}
		 			
				// 新增
				pfmeaLevelMapper.insertSelective(dto);
				return responseData;			 

		  }else {		 
			
				// 更新pfmeaLevel表同时更新Fmea表
				pfmeaLevelMapper.updateByPrimaryKeySelective(dto);
				//根据LevelId查询Fmea
				Fmea fmea = new Fmea();
				fmea.setLevelId(dto.getLevelId());

				//调用自定义方法
				List<Fmea> fmeas = fmeaService.pmyselect(requestCtx, fmea, 0, 0);
				
				this.getFmeaCode(dto , fmeas ,fmeaMapper,requestCtx);

				return responseData;
		}
	}
	/**
     * 新增保存(D)
     * @param dto 层级查询
     * @param request 请求
     * @return 结果集
     */
	@Autowired
	private IFmeaService fmeaService;
	
	/**
	 * @Description: 查询层级数据并修改levelCode
	 * @param pfmeaLevel
	 * @param fmea
	 * @param fmeaMapper
	 * @return 
	 */
	public void getFmeaCode(PfmeaLevel pfmeaLevel ,List<Fmea> list ,FmeaMapper fmeaMapper ,IRequest requestContext) {
		//编号
		String levelCode = pfmeaLevel.getLevelCode();
		//层级
		Long fmeaLevel = pfmeaLevel.getFmeaLevel();
		//描述
		String description = pfmeaLevel.getDescription();
		
		//FMEA数据修改
		if (list != null && list.size() > 0) {
			list.forEach(e -> {	
				//项目编号
				StringBuilder fmeaCode = new StringBuilder();
				String[] split = e.getFmeaCode().trim().split("-");
				int num = split.length;
				Long size = new Long((long)num);
				
				//更新本层项目名称
				if (fmeaLevel.equals(size)) {
					//项目名称
					e.setFmeaName(description);
				}
				//更新FMEA项目编号      
				for (int i = 0; i < split.length; i++) {
					//替换数组的指定位置
					if (i == fmeaLevel - 1) {
						split[i] = levelCode;
					}
					fmeaCode = fmeaCode.append(split[i]).append('-');
				}
				
				//赋值，去掉最后一个"-"
				e.setFmeaCode(fmeaCode.toString().substring(0, fmeaCode.length() - 1 ));

				fmeaMapper.updateByPrimaryKeySelective(e);
			});
		}				
	}
	
	@Override
	public ResponseData updateOrAdd_D(IRequest requestCtx, PfmeaLevel dto) {
		// HQMPInvalidTree hQMInvalid = hqmpInvalidTreeMapper.selectByPrimaryKey(dto);
		ResponseData responseData = new ResponseData();
		//顶层编号唯一
		List<PfmeaLevel> list = pfmeaLevelMapper.selectFmeaLevelByLevelCode(dto.getLevelCode());
		if (list != null && list.size() > 0) {
			responseData.setSuccess(false);
			responseData.setMessage("顶层编号名称已存在");
			return responseData;
		}
		// id没有是新增
		if (null == dto.getLevelId()) {// 新插入值
			if (dto.getParentLevelId() != null) {// 有父级id
				PfmeaLevel pfmeaLevel = new PfmeaLevel();
				pfmeaLevel.setLevelId(dto.getParentLevelId());
				pfmeaLevel = pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel);
				if (pfmeaLevel.getParentLevelId() != null) {				
					PfmeaLevel pfmeaLevel_1 = new PfmeaLevel();
					pfmeaLevel_1.setLevelId(pfmeaLevel.getParentLevelId());
					pfmeaLevel_1 = pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_1);
					if (pfmeaLevel_1.getParentLevelId() != null) {
						PfmeaLevel pfmeaLevel_2 = new PfmeaLevel();
						pfmeaLevel_2.setLevelId(pfmeaLevel_1.getParentLevelId());
						pfmeaLevel_2 = pfmeaLevelMapper.selectByPrimaryKey(pfmeaLevel_2);
						if (pfmeaLevel_2.getParentLevelId() != null) {
							 dto.setFmeaLevel(5L);
						}else {
							 dto.setFmeaLevel(4L);
						}	   
					}else
					{
						dto.setFmeaLevel(3L);
					}
				} else {
					dto.setFmeaLevel(2L);
				} 
			} else {				
				dto.setFmeaLevel(1L);
			}
		 			
				// 新增
				pfmeaLevelMapper.insertSelective(dto);
				return responseData;			 

		  }else {		 
			
				// 更新pfmeaLevel表同时更新Fmea表
				pfmeaLevelMapper.updateByPrimaryKeySelective(dto);
				
				//根据LevelId查询Fmea
				Fmea fmea = new Fmea();
				fmea.setLevelId(dto.getLevelId());

				//调用自定义方法
				List<Fmea> fmeas = fmeaService.dmyselect(requestCtx, fmea, 0, 0);
				
				this.getFmeaCode(dto , fmeas ,fmeaMapper,requestCtx);

				return responseData;

		}
	}
	/**
     * 提交
     * @param dto 层级查询
     * @param request 请求
     * @param requestCtx 请求
     * @return 结果集
     */
	public ResponseData publish(PfmeaLevel dto, IRequest requestCtx, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		Fmea head = new Fmea();
		head.setLevelId(dto.getLevelId());
		int i= fmeaMapper.selectCount(head);
		if(i==1)
		{
			responseData.setSuccess(false);
			responseData.setMessage("已存在（待定）");
			return responseData;
		}
		
		return responseData;
	}
}