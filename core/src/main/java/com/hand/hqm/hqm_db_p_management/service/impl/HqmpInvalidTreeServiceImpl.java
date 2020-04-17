package com.hand.hqm.hqm_db_p_management.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalidTree;
import com.hand.hqm.hqm_db_p_management.dto.HqmpdbMenuItem;
import com.hand.hqm.hqm_db_p_management.mapper.HqmpInvalidTreeMapper;
import com.hand.hqm.hqm_db_p_management.service.IHqmpInvalidTreeService;

@Service
@Transactional(rollbackFor = Exception.class)
public class HqmpInvalidTreeServiceImpl extends BaseServiceImpl<HQMPInvalidTree> implements IHqmpInvalidTreeService {
	@Autowired
	private HqmpInvalidTreeMapper hqmpInvalidTreeMapper;

	/**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:40 2019/8/19
	 * @Param [requestContext, dto]
	 */
	@Override
	public List<HqmpdbMenuItem> queryTreeData(IRequest requestContext, HQMPInvalidTree dto) {
		// 查询根数据
		List<HQMPInvalidTree> hQMInvalids = hqmpInvalidTreeMapper.selectParentInvalid(dto);
		// 查询下层数据
		List<HqmpdbMenuItem> menuItems = castToMenuItem(hQMInvalids);
		return menuItems;
	}

	/**
	 * @Author han.zhang
	 * @Description 更新或者保存附着对象
	 * @Date 19:40 2019/8/19
	 * @Param [requestCtx, dto]
	 */
	@Override
	public ResponseData updateOrAdd(IRequest requestCtx, HQMPInvalidTree dto) {
		// HQMPInvalidTree hQMInvalid = hqmpInvalidTreeMapper.selectByPrimaryKey(dto);
		ResponseData responseData = new ResponseData();
		// id没有是新增
		if (null == dto.getInvalidId()) {// 新插入值
			if (dto.getParentInvalidId() != null) {// 有父级id
				HQMPInvalidTree hQMInvalidTree = new HQMPInvalidTree();
				hQMInvalidTree.setInvalidId(dto.getParentInvalidId());
				hQMInvalidTree = hqmpInvalidTreeMapper.selectByPrimaryKey(hQMInvalidTree);
				if (hQMInvalidTree.getParentInvalidId() != null) {
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
				hqmpInvalidTreeMapper.insertSelective(dto);
				return responseData;
			} else {
				return responseData;
			}

		} else {
			// 校验
			checkInvalid(dto, dto.getRanks(), responseData);
			if (responseData.isSuccess()) {
				// 更新
				hqmpInvalidTreeMapper.updateByPrimaryKeySelective(dto);
				return responseData;
			} else {
				return responseData;
			}
		}
	}

	/**
	 * @Author han.zhang
	 * @Description 删除附着对象及其后代
	 * @Date 11:49 2019/8/20
	 * @Param [dto]
	 */
	@Override
	public void deleteRow(HQMPInvalidTree dto) {

		// 查询后代所有需要删除的附着对象
		List<HQMPInvalidTree> delList = new ArrayList<>();
		delList.add(dto);
		queryDownAttachment(delList, Collections.singletonList(dto));
		// 删除
		self().batchDelete(delList);
	}


	/**
	 * @Author han.zhang
	 * @Description 将list转换成目录菜单
	 * @Date 11:52 2019/8/19
	 * @Param [spcAttachments]
	 */
	private List<HqmpdbMenuItem> castToMenuItem(List<HQMPInvalidTree> hQMInvalids) {
		// 根
		List<HqmpdbMenuItem> menuItems = new ArrayList<>();
		hQMInvalids.stream().forEach(hQMInvalid -> {
			if (hQMInvalid.getParentInvalidId() == null) {
				menuItems.add(createMenuItem(hQMInvalid));
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
	private HqmpdbMenuItem createMenuItem(HQMPInvalidTree hQMInvalid) {
		HqmpdbMenuItem menu = new HqmpdbMenuItem();
		menu.setFunctionCode(hQMInvalid.getInvalidName());
		menu.setText(hQMInvalid.getInvalidName());
		menu.setType(hQMInvalid.getInvalidName());
		menu.setId(hQMInvalid.getInvalidId());
		menu.setInvalidId(hQMInvalid.getInvalidId());
		menu.setParentInvalidId(hQMInvalid.getParentInvalidId());
		menu.setRanks(hQMInvalid.getRanks());
		menu.setInvalidName(hQMInvalid.getInvalidName());
		menu.setRangeName(hQMInvalid.getRangeName());
		menu.setInvalidConsequence(hQMInvalid.getInvalidConsequence());
		menu.setSerious(hQMInvalid.getSerious());
		menu.setSpecialCharacterType(hQMInvalid.getSpecialCharacterType());
		menu.setInvalidReason(hQMInvalid.getInvalidReason());
		menu.setPreventMeasure(hQMInvalid.getPreventMeasure());
		menu.setDetectMeasure(hQMInvalid.getDetectMeasure());
		menu.setOccurrence(hQMInvalid.getOccurrence());
		menu.setDetection(hQMInvalid.getDetection());
		menu.setRpn(hQMInvalid.getRpn());
		return menu;
	}

	/**
	 * @Author han.zhang
	 * @Description 有子元素则添加
	 * @Date 13:51 2019/8/19
	 * @Param [menuItem]
	 */
	private void additem(HqmpdbMenuItem menuItem) {
		// 定义子菜单
		List<HqmpdbMenuItem> children = new ArrayList<>();
		// 查询子
		HQMPInvalidTree hQMInvalid = new HQMPInvalidTree();
		hQMInvalid.setParentInvalidId(menuItem.getInvalidId());
		List<HQMPInvalidTree> hQMInvalids = hqmpInvalidTreeMapper.selectInvalidByParent(hQMInvalid);
		// 添加子
		hQMInvalids.stream().forEach(item -> {
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
	public void queryDownAttachment(List<HQMPInvalidTree> delList, List<HQMPInvalidTree> dtos) {
		dtos.stream().forEach(dto -> {
			HQMPInvalidTree parnethQMInvalid = new HQMPInvalidTree();
			parnethQMInvalid.setParentInvalidId(dto.getInvalidId());
			List<HQMPInvalidTree> hQMInvalids = hqmpInvalidTreeMapper.select(parnethQMInvalid);
			if (hQMInvalids.size() > 0) {
				delList.addAll(hQMInvalids);
				queryDownAttachment(delList, hQMInvalids);
			}
		});
	}

	/**
	 * @Description 校验结构不重复 同一结构下功能不重复 同一功能下失效不重复
	 * @Param [dtos]
	 */
	public void checkInvalid(HQMPInvalidTree dto, Long ranks, ResponseData responseData) {
		if (ranks == 1&&dto.getParentInvalidId()==null) {
			// 结构校验
			int count1 = hqmpInvalidTreeMapper.checkStructure(dto);
			if (count1 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("工序不能重复！");

			}
		} else if (ranks == 2||(ranks == 1&&dto.getParentInvalidId()!=null)) {
			// 功能校验
			int count2 = hqmpInvalidTreeMapper.checkFunctionAndInvalid(dto);
			if (count2 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("同一工序下要求不能重复！");
			}
		} /*else {
			// 失效校验
			int count3 = hqmpInvalidTreeMapper.checkFunctionAndInvalid(dto);
			if (count3 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("同一要求下失效不能重复！");
			}
		}*/
	}
}