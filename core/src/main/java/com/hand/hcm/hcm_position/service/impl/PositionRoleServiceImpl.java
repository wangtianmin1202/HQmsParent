package com.hand.hcm.hcm_position.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_position.dto.PositionCategory;
import com.hand.hcm.hcm_position.dto.PositionRole;
import com.hand.hcm.hcm_position.mapper.PositionCategoryMapper;
import com.hand.hcm.hcm_position.mapper.PositionRoleMapper;
import com.hand.hcm.hcm_position.service.IPositionCategoryService;
import com.hand.hcm.hcm_position.service.IPositionRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 9:27 AM
 */
@Service
public class PositionRoleServiceImpl extends BaseServiceImpl<PositionRole> implements IPositionRoleService {

    @Autowired
    private PositionRoleMapper roleMapper;
    @Autowired
    private PositionCategoryMapper positionCategoryMapper;
    @Autowired
    private IPositionCategoryService positionCategoryService;


    @Override
    public List<PositionRole> query(PositionRole dto, IRequest requestContext, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return roleMapper.prQuery(dto);
    }

    @Override
    public List<PositionRole> add(IRequest requestCtx, PositionRole dto) {
        if ("add".equals(dto.get__status())) {
            self().insertSelective(requestCtx, dto);
            //插入数据到职位与三级分类关系表

            //根据三级分类id查询一级与二级id
            PositionCategory category = positionCategoryMapper.query(dto.getCategoryId());

            PositionCategory positionCategory = new PositionCategory();
            positionCategory.setPositionId(dto.getKid());
            positionCategory.setCategoryFirstId(category.getCategoryFirstId());
            positionCategory.setCategorySecondId(category.getCategorySecondId());
            positionCategory.setCategoryThirdId(category.getCategoryThirdId());

            positionCategoryService.insert(requestCtx, positionCategory);
        } else if ("update".equals(dto.get__status())) {
            self().updateByPrimaryKeySelective(requestCtx, dto);
        }
        return Arrays.asList(dto);
    }
}
