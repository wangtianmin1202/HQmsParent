package com.hand.hqm.hqm_db_management.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_db_management.dto.HQMFunction;
import com.hand.hqm.hqm_db_management.dto.HQMInvalidTree;
import com.hand.hqm.hqm_db_management.dto.HQMStructure;
import com.hand.hqm.hqm_db_management.dto.HqmdbMenuItem;
import com.hand.hqm.hqm_db_management.mapper.HQMFunctionMapper;
import com.hand.hqm.hqm_db_management.mapper.HQMStructureMapper;
import com.hand.hqm.hqm_db_management.mapper.HqmInvalidTreeMapper;
import com.hand.hqm.hqm_db_management.service.IHQMFunctionService;
import com.hand.hqm.hqm_db_management.service.IHQMStructureService;
import com.hand.hqm.hqm_db_management.service.IHqmInvalidTreeService;


@Service
@Transactional(rollbackFor = Exception.class)
public class HqmInvalidTreeServiceImpl extends BaseServiceImpl<HQMInvalidTree> implements IHqmInvalidTreeService {
	@Autowired
	private HqmInvalidTreeMapper hqmInvalidTreeMapper;

	@Autowired
	private IHQMStructureService hQMStructureService;

	@Autowired
	private IHQMFunctionService hQMFunctionService;

	@Autowired
	private HQMStructureMapper hQMStructureMapper;

	@Autowired
	private HQMFunctionMapper hQMFunctionMapper;

	/**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:40 2019/8/19
	 * @Param [requestContext, dto]
	 */
	@Override
	public List<HqmdbMenuItem> queryTreeData(IRequest requestContext, HQMInvalidTree dto) {
		// 查询根数据
		List<HQMInvalidTree> hQMInvalids = hqmInvalidTreeMapper.selectParentInvalid(dto);
		// 查询下层数据
		List<HqmdbMenuItem> menuItems = castToMenuItem(hQMInvalids);
		return menuItems;
	}

	/**
	 * @Author han.zhang
	 * @Description 更新或者保存附着对象
	 * @Date 19:40 2019/8/19
	 * @Param [requestCtx, dto]
	 */
	@Override
	public ResponseData updateOrAdd(IRequest requestCtx, HQMInvalidTree dto) {
		// HQMInvalidTree hQMInvalid = hqmInvalidTreeMapper.selectByPrimaryKey(dto);
		ResponseData responseData = new ResponseData();
		// id没有是新增
		if (null == dto.getInvalidId()) {// 新插入值
			if (dto.getParentInvalidId() != null) {// 有父级id
				HQMInvalidTree hQMInvalidTree = new HQMInvalidTree();
				hQMInvalidTree.setInvalidId(dto.getParentInvalidId());
				hQMInvalidTree = hqmInvalidTreeMapper.selectByPrimaryKey(hQMInvalidTree);
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
				hqmInvalidTreeMapper.insertSelective(dto);
				return responseData;
			} else {
				return responseData;
			}

		} else {
			// 校验
			checkInvalid(dto, dto.getRanks(), responseData);
			if (responseData.isSuccess()) {
				// 更新
				hqmInvalidTreeMapper.updateByPrimaryKeySelective(dto);
				return responseData;
			} else {
				return responseData;
			}
		}
		// List<HQMInvalidTree> hQMInvalids = new ArrayList<HQMInvalidTree>();
		// hQMInvalids.add(dto);
		// save(requestCtx, hQMInvalids)

	}

	public List<HQMInvalidTree> save(IRequest requestCtx, List<HQMInvalidTree> list) {
		List<HQMInvalidTree> hQMInvalids = new ArrayList<HQMInvalidTree>();
		for (int i = 0; i < list.size(); i++) {
			// 保存结构表
			HQMStructure hQMStructureq = new HQMStructure();
			hQMStructureq = hQMStructureMapper.structureNamecount(list.get(i).getStructureName());
			if (hQMStructureq != null) {// 判断结构表里是否已有此结构
				list.get(i).setStructureId(hQMStructureq.getStructureId());
			} else {
				HQMStructure hQMStructurei = new HQMStructure();
				hQMStructurei.setStructureName(list.get(i).getStructureName());
				if ("add".equals(list.get(i).get__status())) {
					hQMStructurei = hQMStructureService.insertSelective(requestCtx, hQMStructurei);
					list.get(i).setStructureId(hQMStructurei.getStructureId());
				} else {
					hQMStructurei.setStructureId(list.get(i).getStructureId());
					hQMStructureService.updateByPrimaryKeySelective(requestCtx, hQMStructurei);
				}
			}

			// 保存功能表
			HQMFunction hQMFunctionq = new HQMFunction();
			hQMFunctionq = hQMFunctionMapper.functionNamecount(list.get(i).getFunctionName());
			if (hQMFunctionq != null) {// 判断功能表里是否已有此功能
				list.get(i).setFunctionId(hQMFunctionq.getFunctionId());
			} else {
				HQMFunction hQMFunctioni = new HQMFunction();
				hQMFunctioni.setFunctionName(list.get(i).getFunctionName());
				if ("add".equals(list.get(i).get__status())) {
					hQMFunctioni = hQMFunctionService.insertSelective(requestCtx, hQMFunctioni);
					list.get(i).setFunctionId(hQMFunctioni.getFunctionId());

					HQMFunction hQMFunctionii = new HQMFunction();
					hQMFunctionii.setFunctionId(hQMFunctioni.getFunctionId());
					hQMFunctionii.setStructureId(list.get(i).getStructureId());
					hQMFunctionService.updateByPrimaryKeySelective(requestCtx, hQMFunctionii);
				} else {
					hQMFunctioni.setFunctionId(list.get(i).getFunctionId());
					hQMFunctionService.updateByPrimaryKey(requestCtx, hQMFunctioni);
				}
			}
		}

		// 保存失效表
		hQMInvalids = self().batchUpdate(requestCtx, list);

		// 回写功能表 更新功能关联的失效id
		for (int j = 0; j < hQMInvalids.size(); j++) {
			HQMFunction hQMFunctionii = new HQMFunction();
			hQMFunctionii.setInvalidId(hQMInvalids.get(j).getInvalidId());
			hQMFunctionii.setFunctionId(list.get(j).getFunctionId());
			hQMFunctionService.updateByPrimaryKeySelective(requestCtx, hQMFunctionii);
		}

		return hQMInvalids;
	}

	/**
	 * @Author han.zhang
	 * @Description 删除附着对象及其后代
	 * @Date 11:49 2019/8/20
	 * @Param [dto]
	 */
	@Override
	public void deleteRow(HQMInvalidTree dto) {

		// 查询后代所有需要删除的附着对象
		List<HQMInvalidTree> delList = new ArrayList<>();
		delList.add(dto);
		queryDownAttachment(delList, Collections.singletonList(dto));
		// 删除
		self().batchDelete(delList);

		// delete(delList);
	}

	public int delete(List<HQMInvalidTree> list) {
		for (int i = 0; i < list.size(); i++) {

			// 删除结构表
			HQMStructure hQMStructure = new HQMStructure();
			hQMStructure.setStructureId(list.get(i).getStructureId());
			hQMStructureService.deleteByPrimaryKey(hQMStructure);

			// 删除功能表
			HQMFunction hQMFunction = new HQMFunction();
			hQMFunction.setFunctionId(list.get(i).getFunctionId());
			hQMFunctionService.deleteByPrimaryKey(hQMFunction);
		}
		int re = 0;
		re = self().batchDelete(list);
		return re;
	}

	/**
	 * @Author han.zhang
	 * @Description 将list转换成目录菜单
	 * @Date 11:52 2019/8/19
	 * @Param [spcAttachments]
	 */
	private List<HqmdbMenuItem> castToMenuItem(List<HQMInvalidTree> hQMInvalids) {
		// 根
		List<HqmdbMenuItem> menuItems = new ArrayList<>();
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
	private HqmdbMenuItem createMenuItem(HQMInvalidTree hQMInvalid) {
		HqmdbMenuItem menu = new HqmdbMenuItem();
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
	private void additem(HqmdbMenuItem menuItem) {
		// 定义子菜单
		List<HqmdbMenuItem> children = new ArrayList<>();
		// 查询子
		HQMInvalidTree hQMInvalid = new HQMInvalidTree();
		hQMInvalid.setParentInvalidId(menuItem.getInvalidId());
		List<HQMInvalidTree> hQMInvalids = hqmInvalidTreeMapper.selectInvalidByParent(hQMInvalid);
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
	public void queryDownAttachment(List<HQMInvalidTree> delList, List<HQMInvalidTree> dtos) {
		dtos.stream().forEach(dto -> {
			HQMInvalidTree parnethQMInvalid = new HQMInvalidTree();
			parnethQMInvalid.setParentInvalidId(dto.getInvalidId());
			List<HQMInvalidTree> hQMInvalids = hqmInvalidTreeMapper.select(parnethQMInvalid);
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
	public void checkInvalid(HQMInvalidTree dto, Long ranks, ResponseData responseData) {
		if (ranks == 1&&dto.getParentInvalidId()==null) {
			// 结构校验
			int count1 = hqmInvalidTreeMapper.checkStructure(dto);
			if (count1 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("结构不能重复！");

			}
		} else if (ranks == 2||(ranks == 1&&dto.getParentInvalidId()!=null)) {
			// 功能校验
			int count2 = hqmInvalidTreeMapper.checkFunctionAndInvalid(dto);
			if (count2 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("同一结构下功能不能重复！");
			}
		} /*else {
			// 失效校验
			int count3 = hqmInvalidTreeMapper.checkFunctionAndInvalid(dto);
			if (count3 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("同一功能下失效不能重复！");
			}
		}*/
	}
}