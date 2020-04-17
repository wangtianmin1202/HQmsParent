package com.hand.spc.pspc_classify_group.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.constants.SpcConstants;
import com.hand.spc.pspc_ce_parameter_relation.dto.CeParameterRelation;
import com.hand.spc.pspc_ce_parameter_relation.mapper.CeParameterRelationMapper;
import com.hand.spc.pspc_classify_group.dto.ClassifyGroup;
import com.hand.spc.pspc_classify_group.mapper.ClassifyGroupMapper;
import com.hand.spc.pspc_classify_group.service.IClassifyGroupService;
import com.hand.spc.pspc_classify_relation.dto.ClassifyRelation;
import com.hand.spc.pspc_classify_relation.mapper.ClassifyRelationMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class ClassifyGroupServiceImpl extends BaseServiceImpl<ClassifyGroup> implements IClassifyGroupService,SpcConstants {
    @Autowired
    private ClassifyGroupMapper classifyGroupMapper;
    @Autowired
    private ClassifyRelationMapper classifyRelationMapper;
    @Autowired
    private CeParameterRelationMapper ceParameterRelationMapper;
    @Override
    public ResponseData deleteGroupAndLine(IRequest request, ClassifyGroup dto) {
        //删除分类项关系
        ClassifyRelation classifyRelation = new ClassifyRelation();
        classifyRelation.setClassifyGroupId(dto.getClassifyGroupId());
        classifyRelationMapper.delete(classifyRelation);
        //删除控制要素关系
        CeParameterRelation ceParameterRelation = new CeParameterRelation();
        ceParameterRelation.setClassifyGroupId(dto.getClassifyGroupId());
        ceParameterRelationMapper.delete(ceParameterRelation);
        //删除分类组
        classifyGroupMapper.deleteByPrimaryKey(dto);
        return new ResponseData(true);
    }

    @Override
    public ResponseData saveOrUpdate(ClassifyGroup dto) {
        String saveType = dto.getSaveType();
        if(ADD.equals(dto.getSaveType())){
            //判断是否已存在
            ClassifyGroup group1 = new ClassifyGroup();
            group1.setTenantId(dto.getTenantId());
            group1.setSiteId(dto.getSiteId());
            group1.setClassifyGroup(dto.getClassifyGroup());
            List<ClassifyGroup> groups1 = classifyGroupMapper.select(group1);
            if(groups1.size() > 0){
                throw new RuntimeException("分类组已存在");
            }else{
                classifyGroupMapper.insertSelective(dto);
            }

        }


        //修改
        if(EDIT.equals(saveType)){
            dto.setClassifyGroupId(dto.getClassifyGroupId());
            classifyGroupMapper.updateByPrimaryKeySelective(dto);
            return new ResponseData(true);
        }

        //副本保存
        if(COPY_SAVE.equals(saveType)){
            //给新建的分类组存关系
            ClassifyRelation classifyRelation = new ClassifyRelation();
            classifyRelation.setClassifyGroupId(dto.getCopyGroupId());
            //查询原本分类组下的分类项
            List<ClassifyRelation> classifyRelations = classifyRelationMapper.select(classifyRelation);
            //遍历存分类项关系
            ClassifyRelation tempRelation = new ClassifyRelation();
            tempRelation.setTenantId(-1L);
            tempRelation.setSiteId(-1L);
            classifyRelations.stream().forEach(t -> {
                tempRelation.setClassifyGroupId(dto.getClassifyGroupId());
                tempRelation.setClassifyId(t.getClassifyId());
                //判断分类项关系是否存在
                int count = classifyRelationMapper.selectCount(tempRelation);
                if(count > 0){
                    throw new RuntimeException("分类项关系已存在");
                }
                classifyRelationMapper.insertSelective(tempRelation);
            });
            //存控制要素关系
            CeParameterRelation ceParameterRelation = new CeParameterRelation();
            ceParameterRelation.setClassifyGroupId(dto.getCopyGroupId());
            //查询原本分类组下的控制要素关系
            List<CeParameterRelation> ceParameterRelations = ceParameterRelationMapper.select(ceParameterRelation);
            //遍历存控制元素关系
            CeParameterRelation parameterRelation = new CeParameterRelation();
            parameterRelation.setSiteId(-1L);
            parameterRelation.setTenantId(-1L);
            parameterRelation.setClassifyGroupId(dto.getClassifyGroupId()   );
            ceParameterRelations.stream().forEach(t -> {
                parameterRelation.setCeParameterId(t.getCeParameterId());
                //判断控制要素关系是否存在
                int count = ceParameterRelationMapper.selectCount(parameterRelation);
                if(count > 0){
                    throw new RuntimeException("控制要素关系已存在");
                }
                ceParameterRelationMapper.insertSelective(parameterRelation);
            });
            List<ClassifyGroup> groups = Collections.singletonList(dto);
            return new ResponseData(groups);
        }
        return new ResponseData(true);
    }
}