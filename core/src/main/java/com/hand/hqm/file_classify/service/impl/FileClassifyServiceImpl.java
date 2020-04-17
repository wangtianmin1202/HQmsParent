package com.hand.hqm.file_classify.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.file_classify.dto.Classify;
import com.hand.hqm.file_classify.dto.MenuItem;
import com.hand.hqm.file_classify.mapper.FileClassifyMapper;
import com.hand.hqm.file_classify.service.IFileClassifyService;
import com.hand.hqm.file_manager.dto.Manager;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileClassifyServiceImpl extends BaseServiceImpl<Classify> implements IFileClassifyService {
	@Autowired
	private FileClassifyMapper classifyMapper;

	/**
	 * @Author jiang ruifu
	 * @Description 更新或者保存附着对象
	 * @Date 19:40 2019/8/30
	 * @Param [requestCtx, dto]
	 */
	@Override
	public ResponseData updateOrAdd(IRequest requestCtx, Classify dto) {

		ResponseData responseData = new ResponseData();
		// 根据主键判断更新还是新增
		if (dto.getClassifyId() == null) {// 新增

			Classify classify_c = new Classify();
			classify_c.setClassifyCode(dto.getClassifyCode());
			// 校验 唯一性
			List<Classify> classify_l = classifyMapper.select(classify_c);
			if (classify_l.size() > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("文件夹名称不可重复");
				return responseData;
			} else {
				self().insertSelective(requestCtx, dto);
			}
		} else {// 更新
			if (null == dto.getParentClassifyId()) {
				// 如果是第一层 需要把所有子层的有效性更新(查询语句)
				List<Classify> classifys = classifyMapper.myselect(dto);
				Classify classify = new Classify();
				classify.setClassifyCode(dto.getClassifyCode());
				classify.setClassifyDescriptions(dto.getClassifyDescriptions());
				classify.setClassifyId(dto.getClassifyId());
				classify.setEnableFlag(dto.getEnableFlag());
				self().updateByPrimaryKeySelective(requestCtx, classify);
				for (Classify dao : classifys) {
					Classify classify_u = new Classify();
					classify_u.setEnableFlag(dto.getEnableFlag());
					classify_u.setClassifyId(dao.getClassifyId());
					self().updateByPrimaryKeySelective(requestCtx, classify_u);
				}
			} else {
				Classify classify_u = new Classify();
				classify_u.setClassifyCode(dto.getClassifyCode());
				classify_u.setClassifyDescriptions(dto.getClassifyDescriptions());
				classify_u.setClassifyId(dto.getClassifyId());
				classify_u.setEnableFlag(dto.getEnableFlag());
				self().updateByPrimaryKeySelective(requestCtx, classify_u);
			}
		}
		responseData.setSuccess(true);
		responseData.setMessage("保存成功");
		return responseData;
	}

	@Override
	public List<MenuItem> queryTreeData(IRequest requestContext, Classify dto) {

		String flag = dto.getEnableFlag();
		if (dto.getClassifyId() != null) {
			List<Classify> classify_s = classifyMapper.myselectforleaf(dto);
			dto.setClassifyCode(classify_s.get(0).getClassifyCode());
			dto.setClassifyId(classify_s.get(0).getClassifyId());
		} else {
		}
		// 查询根数据
		List<Classify> classify = classifyMapper.selectParentInvalid(dto);
		// 查询下层数据
		List<MenuItem> menuItems = castToMenuItem(classify, flag);
		return menuItems;
	}

	private List<MenuItem> castToMenuItem(List<Classify> classifys, String flag) {
		// 根
		List<MenuItem> menuItems = new ArrayList<>();
		classifys.stream().forEach(classify -> {
			if (classify.getParentClassifyId() == null) {
				menuItems.add(createMenuItem(classify));
			}
		});
		// 添加子
		menuItems.stream().forEach(item -> {
			additem(item, flag);
		});
		return menuItems;
	}

	private MenuItem createMenuItem(Classify classifys) {
		MenuItem menu = new MenuItem();
		menu.setFunctionCode(classifys.getClassifyCode());
		menu.setText(classifys.getClassifyCode());
		menu.setType(classifys.getClassifyCode());
		menu.setId(classifys.getClassifyId());
		menu.setClassifyId(classifys.getClassifyId());
		menu.setParentClassifyId(classifys.getParentClassifyId());
		menu.setClassifyDescriptions(classifys.getClassifyDescriptions());
		menu.setMainClassifyCode(classifys.getMainClassifyCode());
		menu.setEnableFlag(classifys.getEnableFlag());
		menu.setClassifyCode(classifys.getClassifyCode());
		return menu;
	}

	private void additem(MenuItem menuItem, String flag) {
		// 定义子菜单
		List<MenuItem> children = new ArrayList<>();
		// 查询子
		Classify classify = new Classify();
		classify.setParentClassifyId(menuItem.getClassifyId());
		classify.setEnableFlag(flag);
		List<Classify> classifys = classifyMapper.selectInvalidByParent(classify);
		// 添加子
		classifys.stream().forEach(item -> {
			children.add(createMenuItem(item));
		});
		// 设定子菜单
		if (children.size() > 0) {
			menuItem.setChildren(children);
			// 递归，有子 继续添加

			children.stream().forEach(childrenItem -> {
				additem(childrenItem, flag);

			});

		}
		menuItem.setChildren(children);
	}

	/**
	 * @Author han.zhang
	 * @Description 查询第一层级数据
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@Override
	public List<Classify> myselect1(IRequest requestContext, Classify dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return classifyMapper.myselectone(dto);
	}

	/**
	 * @Author han.zhang
	 * @Description 查询第二层级数据
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@Override
	public List<Classify> myselect2(IRequest requestContext, Classify dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return classifyMapper.myselecttwo(dto);
	}

	/**
	 * @Author han.zhang
	 * @Description 查询第三层级数据
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@Override
	public List<Classify> myselect3(IRequest requestContext, Classify dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return classifyMapper.myselectthr(dto);
	}
}
