package com.hand.hqm.hqm_control_plan.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeaturesMenuItem;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeaturesTree;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanProcess;
import com.hand.hqm.hqm_control_plan.mapper.ControlPlanFeaturesTreeMapper;
import com.hand.hqm.hqm_control_plan.mapper.ControlPlanProcessMapper;
import com.hand.hqm.hqm_control_plan.service.IControlPlanFeaturesTreeService;
import com.hand.hqm.hqm_control_plan.service.IControlPlanProcessService;
import com.hand.hqm.hqm_db_management.dto.HQMFunction;
import com.hand.hqm.hqm_db_management.dto.HQMStructure;

import oracle.net.aso.e;

@Service
@Transactional(rollbackFor = Exception.class)
public class ControlPlanFeaturesTreeServiceImpl extends BaseServiceImpl<ControlPlanFeaturesTree>
		implements IControlPlanFeaturesTreeService {
	@Autowired
	private ControlPlanFeaturesTreeMapper controlPlanFeaturesTreeMapper;
	
	@Autowired
	private IControlPlanFeaturesTreeService service;
	
	@Autowired
	private ControlPlanProcessMapper controlPlanProcessMapper;
	
	@Autowired
	private IControlPlanProcessService controlPlanProcessService;

	/**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:40 2019/8/19
	 * @Param [requestContext, dto]
	 */
	@Override
	public List<ControlPlanFeaturesMenuItem> queryTreeData(IRequest requestContext, ControlPlanFeaturesTree dto) {
		String enableFlag = dto.getEnableFlag();
		if (StringUtils.isEmpty(enableFlag)) {
			dto.setEnableFlag("Y");
		}
		// 查询根数据
		List<ControlPlanFeaturesTree> CPFeatures = controlPlanFeaturesTreeMapper.selectParentFeatures(dto);
		// 查询下层数据
		List<ControlPlanFeaturesMenuItem> menuItems = castToMenuItem(CPFeatures);

		if (("N").equals(enableFlag)) {
			// 查询根数据
			CPFeatures = controlPlanFeaturesTreeMapper.selectFeaturesByChild(dto);
			CPFeatures.stream().filter(predicate -> {
				predicate.setEnableFlag("N");
				return true;
			}).collect(Collectors.toList());
			// 查询下层数据
			menuItems = castToMenuItem(CPFeatures);
		}

		return menuItems;
	}

	/**
	 * @Author han.zhang
	 * @Description 更新或者保存附着对象
	 * @Date 19:40 2019/8/19
	 * @Param [requestCtx, dto]
	 */
	@Override
	public ResponseData updateOrAdd(IRequest requestCtx, ControlPlanFeaturesTree dto) {
		// ControlPlanFeaturesTree CPFeature =
		// controlPlanFeaturesTreeMapper.selectByPrimaryKey(dto);
		List<ControlPlanFeaturesTree> hQMInvalids = new ArrayList<ControlPlanFeaturesTree>();
		hQMInvalids.add(dto);
		ResponseData responseData = new ResponseData();
		// id没有是新增
		if (null == dto.getFeaturesId()) {// 新插入值
			dto.set__status("add");
			if (dto.getParentFeaturesId() != null) {// 有父级id
				ControlPlanFeaturesTree CPFeatureTree = new ControlPlanFeaturesTree();
				CPFeatureTree.setFeaturesId(dto.getParentFeaturesId());
				CPFeatureTree = controlPlanFeaturesTreeMapper.selectByPrimaryKey(CPFeatureTree);
				if (CPFeatureTree.getParentFeaturesId() != null) {
					dto.setRanks(3L);
				} else {
					dto.setRanks(2L);
				}

			} else {
				dto.setRanks(1L);
			}
			// 校验
			checkInvalid(dto, dto.getRanks(), responseData);
			if (responseData.isSuccess()) {
				// 新增
				//service.insertSelective(requestCtx,dto);
				save(requestCtx, hQMInvalids);
				return responseData;
			} else {
				return responseData;
			}

		} else {
			dto.set__status("update");
			// 校验
//			checkInvalid(dto, dto.getRanks(), responseData);
			if (responseData.isSuccess()) {
				// 更新
				//service.updateByPrimaryKeySelective(requestCtx,dto);
				save(requestCtx, hQMInvalids);
				return responseData;
			} else {
				return responseData;
			}
		}

	}
	
	
	public List<ControlPlanFeaturesTree> save(IRequest requestCtx, List<ControlPlanFeaturesTree> list) {
		List<ControlPlanFeaturesTree> controlPlanFeaturesTrees = new ArrayList<ControlPlanFeaturesTree>();
		for (int i = 0; i < list.size(); i++) {
			// 保存过程表
			ControlPlanProcess controlPlanProcess = new ControlPlanProcess();
			controlPlanProcess = controlPlanProcessMapper.processNamecount(list.get(i).getFeaturesName());
			if (controlPlanProcess != null) {// 判断过程表里是否已有此名称
				list.get(i).setProcessId(controlPlanProcess.getProcessId());
			} else {
				ControlPlanProcess controlPlanProcessi = new ControlPlanProcess();
				controlPlanProcessi.setProcessName(list.get(i).getFeaturesName());
				if ("add".equals(list.get(i).get__status())) {
					controlPlanProcessi = controlPlanProcessService.insertSelective(requestCtx, controlPlanProcessi);
					list.get(i).setProcessId(controlPlanProcessi.getProcessId());
				} else {
					controlPlanProcessi.setProcessId(list.get(i).getProcessId());
					controlPlanProcessService.updateByPrimaryKey(requestCtx, controlPlanProcessi);
				}
			}
		}

		// 保存特征表
		controlPlanFeaturesTrees = self().batchUpdate(requestCtx, list);
		return controlPlanFeaturesTrees;
	}

	/**
	 * @Author han.zhang
	 * @Description 删除附着对象及其后代
	 * @Date 11:49 2019/8/20
	 * @Param [dto]
	 */
	@Override
	public void deleteRow(IRequest request,ControlPlanFeaturesTree dto) {

		// 查询后代所有需要删除的附着对象
		List<ControlPlanFeaturesTree> delList = new ArrayList<>();
		dto.setEnableFlag("N");
		dto.set__status("update");
		delList.add(dto);
		queryDownAttachment(delList, Collections.singletonList(dto),dto.getEnableFlag());
		
		//删除过程表
		for (int i = 0; i < delList.size(); i++) {

			// 删除结构表
			ControlPlanProcess controlPlanProcess = new ControlPlanProcess();
			controlPlanProcess.setProcessId(delList.get(i).getProcessId());
			controlPlanProcessService.deleteByPrimaryKey(controlPlanProcess);

		}
		// 修改
		self().batchUpdate(request, delList);
	}
	/**
	 * @Author magicor
	 * @Description 还原附着对象及其后代
	 * @Date 11:00 2019/9/12
	 * @Param [dto]
	 */
	@Override
	public void restoreRow(IRequest request,ControlPlanFeaturesTree dto) {
		ControlPlanFeaturesTree cTree = controlPlanFeaturesTreeMapper.selectByPrimaryKey(dto);
		Long parentFeaturesId = cTree.getParentFeaturesId();
		if (parentFeaturesId != null) {
			ControlPlanFeaturesTree treeHead = controlPlanFeaturesTreeMapper.selectByPrimaryKey(parentFeaturesId);
			treeHead.setEnableFlag("Y");
			controlPlanFeaturesTreeMapper.updateByPrimaryKeySelective(treeHead);
		}
		// 查询后代所有需要还原的附着对象
		List<ControlPlanFeaturesTree> delList = new ArrayList<>();
		dto.setEnableFlag("Y");
		dto.set__status("update");
		delList.add(dto);
		queryDownAttachment(delList, Collections.singletonList(dto),dto.getEnableFlag());
		
		//删除过程表
//		for (int i = 0; i < delList.size(); i++) {
//			
//			// 删除结构表
//			ControlPlanProcess controlPlanProcess = new ControlPlanProcess();
//			controlPlanProcess.setProcessId(delList.get(i).getProcessId());
//			controlPlanProcessService.deleteByPrimaryKey(controlPlanProcess);
//			
//		}
		// 修改还原
		self().batchUpdate(request, delList);
	}

	/**
	 * @Author han.zhang
	 * @Description 将list转换成目录菜单
	 * @Date 11:52 2019/8/19
	 * @Param [spcAttachments]
	 */
	private List<ControlPlanFeaturesMenuItem> castToMenuItem(List<ControlPlanFeaturesTree> CPFeatures) {
		// 根
		List<ControlPlanFeaturesMenuItem> menuItems = new ArrayList<>();
		CPFeatures.stream().forEach(CPFeature -> {
			if (CPFeature.getParentFeaturesId() == null) {
				menuItems.add(createMenuItem(CPFeature));
			}
		});
		// 添加子
		menuItems.stream().forEach(item -> {
			additem(item);
		});
		return menuItems;
	}

	/**
	 * @Author han.zhang
	 * @Description 将SpcAttachment对象转换成菜单
	 * @Date 13:50 2019/8/19
	 * @Param [attachment]
	 */
	private ControlPlanFeaturesMenuItem createMenuItem(ControlPlanFeaturesTree CPFeature) {
		ControlPlanFeaturesMenuItem menu = new ControlPlanFeaturesMenuItem();
		menu.setFunctionCode(CPFeature.getFeaturesName());
		menu.setText(CPFeature.getFeaturesName());
		menu.setType(CPFeature.getFeaturesName());
		menu.setId(CPFeature.getFeaturesId());
		menu.set_token(CPFeature.get_token());
		menu.setFeature(CPFeature.getFeature());
		menu.setEnableFlag(CPFeature.getEnableFlag());
		menu.setFeaturesId(CPFeature.getFeaturesId());
		menu.setParentFeaturesId(CPFeature.getParentFeaturesId());
		menu.setControlPlanId(CPFeature.getControlPlanId());
		menu.setRanks(CPFeature.getRanks());
		menu.setFeaturesName(CPFeature.getFeaturesName());
		menu.setFeaturesContent(CPFeature.getFeaturesContent());
		menu.setFeaturesType(CPFeature.getFeaturesType());
		menu.setEquipment(CPFeature.getEquipment());
		menu.setSpecialCharacterType(CPFeature.getSpecialCharacterType());
		menu.setStandrad(CPFeature.getStandrad());
		menu.setDetectionEquipment(CPFeature.getDetectionEquipment());
		menu.setSampleSize(CPFeature.getSampleSize());
		menu.setDetectionFrequency(CPFeature.getDetectionFrequency());
		menu.setControlMethod(CPFeature.getControlMethod());
		menu.setGrR(CPFeature.getGrR());
		menu.setProcessCapability(CPFeature.getProcessCapability());
		menu.setReactionPlan(CPFeature.getReactionPlan());
		menu.setObjVNumber(CPFeature.getObjectVersionNumber());
		menu.setProcessId(CPFeature.getProcessId());
		return menu;
	}

	/**
	 * @Author han.zhang
	 * @Description 有子元素则添加
	 * @Date 13:51 2019/8/19
	 * @Param [menuItem]
	 */
	private void additem(ControlPlanFeaturesMenuItem menuItem) {
		// 定义子菜单
		List<ControlPlanFeaturesMenuItem> children = new ArrayList<>();
		// 查询子
		ControlPlanFeaturesTree CPFeature = new ControlPlanFeaturesTree();
		CPFeature.setParentFeaturesId(menuItem.getFeaturesId());
		CPFeature.setEnableFlag(menuItem.getEnableFlag());
		List<ControlPlanFeaturesTree> CPFeatures = controlPlanFeaturesTreeMapper.selectFeaturesByParent(CPFeature);
		// 添加子
		CPFeatures.stream().forEach(item -> {
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
	 * @Author han.zhang
	 * @Description 查询后代子附着对象
	 * @Date 11:52 2019/8/20
	 * @Param
	 */
	public void queryDownAttachment(List<ControlPlanFeaturesTree> delList, List<ControlPlanFeaturesTree> dtos,String enableFlag) {
		dtos.stream().forEach(dto -> {
			ControlPlanFeaturesTree parnetCPFeature = new ControlPlanFeaturesTree();
			parnetCPFeature.setParentFeaturesId(dto.getFeaturesId());
			List<ControlPlanFeaturesTree> CPFeatures = controlPlanFeaturesTreeMapper.select(parnetCPFeature);
			if (CPFeatures.size() > 0) {
				CPFeatures.stream().filter(e -> {
					e.setEnableFlag(enableFlag);
					e.set__status("update");
					return true;
				}).collect(Collectors.toList());
				delList.addAll(CPFeatures);
				queryDownAttachment(delList, CPFeatures,enableFlag);
			}
		});
	}

	/**
	 * @Description 校验结构不重复 同一结构下功能不重复 同一功能下失效不重复
	 * @Param [dtos]
	 */
	public void checkInvalid(ControlPlanFeaturesTree dto, Long ranks, ResponseData responseData) {
		if (ranks == 1 && dto.getParentFeaturesId() == null) {
			// 过程校验
			int count1 = controlPlanFeaturesTreeMapper.checkProcess(dto);
			if (count1 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("过程不能重复！");

			}
		} else {
			// 特性校验
			/*int count3 = controlPlanFeaturesTreeMapper.checkFeatures(dto);
			if (count3 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("同一过程下特性不能重复！");
			}*/
		}
	}

	@Override
	public List<ControlPlanFeaturesTree> changeDatas(IRequest requestContext,
			List<ControlPlanFeaturesTree> dto) {
		if(dto.get(0).getFeaturesId() == null) {
			//add
			for(ControlPlanFeaturesTree controlPlanFeaturesTree : dto) {
				long controlPlanId = controlPlanFeaturesTree.getControlPlanId();
				//特性
				ControlPlanProcess controlPlanProcess = new ControlPlanProcess();
				controlPlanProcess.setProcessName(controlPlanFeaturesTree.getFeaturesName());
				controlPlanProcess = controlPlanProcessService.insertSelective(requestContext, controlPlanProcess);
				
				ControlPlanFeaturesTree head = new ControlPlanFeaturesTree();
				head.setFeaturesId(controlPlanFeaturesTree.getParentId());
				head = controlPlanFeaturesTreeMapper.selectByPrimaryKey(head);
				//过程
				controlPlanFeaturesTree.setControlPlanId(controlPlanId);
				controlPlanFeaturesTree.setProcessId(controlPlanProcess.getProcessId());
				controlPlanFeaturesTree.setParentFeaturesId(controlPlanFeaturesTree.getParentId());
				self().insertSelective(requestContext, controlPlanFeaturesTree);
			}
		}else {
			self().batchUpdate(requestContext, dto);
		}
		return dto;
	}
}