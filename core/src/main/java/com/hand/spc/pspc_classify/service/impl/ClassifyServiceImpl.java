package com.hand.spc.pspc_classify.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.constants.SpcConstants;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.pspc_classify.mapper.ClassifyMapper;
import com.hand.spc.pspc_classify.service.IClassifyService;
import com.hand.spc.pspc_classify_relation.dto.ClassifyRelation;
import com.hand.spc.pspc_classify_relation.mapper.ClassifyRelationMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class ClassifyServiceImpl extends BaseServiceImpl<Classify> implements IClassifyService,SpcConstants {
    @Autowired
    private ClassifyRelationMapper classifyRelationMapper;
    @Autowired
    private ClassifyMapper classifyMapper;

    /**
     * @Author han.zhang
     * @Description 根据分类组id查询分类项
     * @Date 9:52 2019/8/7
     * @Param [requestContext, dto, page, pageSize]
     */
    @Override
    public ResponseData selectClassify(IRequest requestContext, Classify dto, int page, int pageSize) {
        ClassifyRelation classifyRelation = new ClassifyRelation();
        classifyRelation.setClassifyGroupId(dto.getClassifyGroupId());
        //查询分类关系
        List<ClassifyRelation> classifyRelations = classifyRelationMapper.select(classifyRelation);

        List<Classify> classifyList = new ArrayList<>();
        classifyRelations.stream().forEach(item -> {
            Classify classify = new Classify();
            classify.setClassifyId(item.getClassifyId());
            Classify classify1 = classifyMapper.selectByPrimaryKey(classify);
            if(null != classify1){
                classify1.setClassifyRelationId(item.getClassifyRelationId());
                classifyList.add(classify1);
            }
        });
        List<Classify> classifies = classifyList.stream()
                .skip((page-1) * pageSize)
                .limit(pageSize).collect(Collectors.toList());

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setRows(classifies);
        responseData.setTotal((long) classifyRelations.size());
        return responseData;
    }
    /**
     * @Author han.zhang
     * @Description 分类项新增或保存
     * @Date 20:41 2019/8/7
     * @Param [requestContext, dto]
     */
    @Override
    public ResponseData saveAndUpdateClassify(IRequest requestContext, Classify dto) {
        //新增分类项
        if(ADD.equals(dto.getOperateStatus())){
            Classify classify = new Classify();
            classify.setClassifyCode(dto.getClassifyCode());
            int count = classifyMapper.selectCount(classify);
            if(count > 0){
                throw new RuntimeException("分类项已存在");
            }
            //新增分类项
            classifyMapper.insertSelective(dto);
        }
        //更新
        if(EDIT.equals(dto.getOperateStatus())){
            //更新分类项
            classifyMapper.updateByPrimaryKeySelective(dto);
        }
        //新增关系并更新
        if(ADD_EDIT.equals(dto.getOperateStatus())){
            //更新分类项
            classifyMapper.updateByPrimaryKeySelective(dto);
            //查询分类项关系是否存在
            ClassifyRelation classifyRelation = new ClassifyRelation();
            classifyRelation.setTenantId(-1L);
            classifyRelation.setSiteId(-1L);
            classifyRelation.setClassifyId(dto.getClassifyId());
            classifyRelation.setClassifyGroupId(dto.getClassifyGroupId());
            List<ClassifyRelation> relations = classifyRelationMapper.select(classifyRelation);
            if(relations.size() > 0){
                throw new RuntimeException("分类项关系已存在");
            }
            classifyRelationMapper.insertSelective(classifyRelation);
        }
        return new ResponseData(true);
    }

    @Override
    public ResponseData getClassifyByParameterId(IRequest requestContext, Classify dto) {
        return new ResponseData(classifyMapper.queryByParamterId(dto.getCeParameterId()));
    }
}