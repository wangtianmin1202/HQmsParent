/*package com.hand.spc.repository.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CeParameter;
import com.hand.spc.repository.dto.CeParameterRelation;
import com.hand.spc.repository.dto.Classify;
import com.hand.spc.repository.dto.ClassifyGroup;
import com.hand.spc.repository.dto.ClassifyRelation;
import com.hand.spc.repository.mapper.CeParameterMapper;
import com.hand.spc.repository.mapper.CeParameterRelationMapper;
import com.hand.spc.repository.mapper.ClassifyGroupMapper;
import com.hand.spc.repository.mapper.ClassifyMapper;
import com.hand.spc.repository.mapper.ClassifyRelationMapper;
import com.hand.spc.repository.service.IClassifyRService;
import com.hand.spc.utils.CommonException;

@Service
public class ClassifyRServiceImpl extends BaseServiceImpl<Classify> implements IClassifyRService {

    @Autowired
    private ClassifyGroupMapper classifyGroupRepository; //分类组
    @Autowired
    private ClassifyMapper classifyRepository; //分类项
    @Autowired
    private ClassifyRelationMapper classifyRelationRepository;//分类项与分类组关系
    @Autowired
    private CeParameterMapper ceParameterRepository;//控制要素
    @Autowired
    private CeParameterRelationMapper ceParameterRelationRepository;//控制要素与分类组关系

    //@Autowired
    //private OocRepository oocRepository; //OOC表
    //@Autowired
    //private OocHisRepository oocHisRepository; //OOC历史表

    private String ErrorMessage = ""; //报错消息

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassifyGroup saveClassifyGroup(ClassifyGroup classifyGroup) {

        if (classifyGroup.get__status() != null && classifyGroup.get__status().equals("create")) {

            Long oldClassifyGroupId=classifyGroup.getClassifyGroupId();
            Long siteId=classifyGroup.getSiteId();
            Long tenantId=classifyGroup.getTenantId();
            classifyGroup.setClassifyGroupId(null);
            classifyGroupRepository.insert(classifyGroup);
            Long newClassfyGroupId=classifyGroup.getClassifyGroupId();

            ClassifyRelation classFyRelation =new ClassifyRelation();
            classFyRelation.setClassifyGroupId(oldClassifyGroupId);
            classFyRelation.setTenantId(tenantId);
            classFyRelation.setSiteId(siteId);
            List<ClassifyRelation>classFyRelationList= classifyRelationRepository.select(classFyRelation);
            List<ClassifyRelation>classFyRelationCloneList= new  ArrayList<ClassifyRelation>();

            for(ClassifyRelation r:classFyRelationList){
                ClassifyRelation newClassifyRelation=new ClassifyRelation();
                BeanUtils.copyProperties(r, newClassifyRelation);
                newClassifyRelation.setClassifyRelationId(null);
                newClassifyRelation.setClassifyGroupId(newClassfyGroupId);
                classFyRelationCloneList.add(newClassifyRelation);
            }

            CeParameterRelation ceParameterRelation=new CeParameterRelation();
            ceParameterRelation.setClassifyGroupId(oldClassifyGroupId);
            ceParameterRelation.setSiteId(siteId);
            ceParameterRelation.setTenantId(tenantId);

            List<CeParameterRelation>ceParameterRelationList=ceParameterRelationRepository.select(ceParameterRelation);
            List<CeParameterRelation>ceParameterRelationCloneList= new  ArrayList<CeParameterRelation>();


            for(CeParameterRelation r:ceParameterRelationList){
                CeParameterRelation  newCeParameterRelation=new CeParameterRelation();
                BeanUtils.copyProperties(r, newCeParameterRelation);
                newCeParameterRelation.setRelationId(null);
                newCeParameterRelation.setClassifyGroupId(newClassfyGroupId);
                ceParameterRelationCloneList.add(newCeParameterRelation);
            }
            for(int i=0;i<classFyRelationCloneList.size();i++) {
            classifyRelationRepository.insert(classFyRelationCloneList.get(i));
            };
            //classifyRelationRepository.batchInsert(classFyRelationCloneList);
            for(int i=0;i<ceParameterRelationCloneList.size();i++) {
            ceParameterRelationRepository.insert(ceParameterRelationCloneList.get(i));
            };
            //ceParameterRelationRepository.batchInsert(ceParameterRelationCloneList);

        } else if (classifyGroup.get__status() != null && classifyGroup.get__status().equals("update")) {
            classifyGroupRepository.updateByPrimaryKey(classifyGroup);
        }
        return classifyGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassifyGroup delClassifyGroup(ClassifyGroup classifyGroup) {
        //if (!checkExists(classifyGroup)) {
        //    throw new CommonException(ErrorMessage);
        //}

        //删除控制要素组
        if (classifyGroup.getClassifyGroupId() != null) {
            classifyGroupRepository.deleteByPrimaryKey(classifyGroup);

            //删除分类项与分类组关系
            ClassifyRelation classifyRelation = new ClassifyRelation();
            classifyRelation.setTenantId(classifyGroup.getTenantId());
            classifyRelation.setSiteId(classifyGroup.getSiteId());
            classifyRelation.setClassifyGroupId(classifyGroup.getClassifyGroupId());
            classifyRelationRepository.delete(classifyRelation);

            //删除控制要素与分类组关系
            CeParameterRelation ceParameterRelation = new CeParameterRelation();
            ceParameterRelation.setTenantId(classifyGroup.getTenantId());
            ceParameterRelation.setSiteId(classifyGroup.getSiteId());
            ceParameterRelation.setClassifyGroupId(classifyGroup.getClassifyGroupId());
            ceParameterRelationRepository.delete(ceParameterRelation);
        }

        return classifyGroup;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassifyGroup saveClassify(ClassifyGroup classifyGroup) {

        //保存分类项
        if (classifyGroup.getClassifyList() != null && classifyGroup.getClassifyList().size() > 0) {

            for (Classify classify : classifyGroup.getClassifyList()) {
                ClassifyRelation classifyRelation = new ClassifyRelation();
                List<ClassifyRelation> classifyRelationList = new ArrayList<ClassifyRelation>();

                if (classifyGroup.get__status() != null && classifyGroup.get__status().equals("create") && classify.getClassifyId() == null) {
                    //校验“分类项”是否已存在
                    Classify classifyExists = new Classify();
                    classifyExists.setTenantId(classifyGroup.getTenantId());
                    classifyExists.setSiteId(classifyGroup.getSiteId());
                    classifyExists.setClassifyCode(classify.getClassifyCode());
                    List<Classify> classifyExistsList = classifyRepository.select(classifyExists);
                    if (classifyExistsList.size() > 0) {
                        throw new CommonException("数据库中已经存在该数据");//数据库中已存在该数据，请检查
                    }

                    //新增分类项
                    classify.setTenantId(classifyGroup.getTenantId());
                    classify.setSiteId(classifyGroup.getSiteId());
                    classifyRepository.insert(classify);

                    //新增分类项与分类组关系
                    classifyRelation.setTenantId(classifyGroup.getTenantId());
                    classifyRelation.setSiteId(classifyGroup.getSiteId());
                    classifyRelation.setClassifyId(classify.getClassifyId());
                    classifyRelation.setClassifyGroupId(classifyGroup.getClassifyGroupId());
                    classifyRelation.setSequence(classify.getSequence());
                    classifyRelationRepository.insert(classifyRelation);
                    //} else if (classifyGroup.get_status() != null && classifyGroup.get_status().name().equals("update")) {
                } else {
                    //修改分类项
                    classify.setTenantId(classifyGroup.getTenantId());
                    classify.setSiteId(classifyGroup.getSiteId());
                    classifyRepository.updateByPrimaryKey(classify);

                    //保存控制要素关系  1、关系存在,修改顺序  2、关系不存在,新增
                    classifyRelation.setTenantId(classifyGroup.getTenantId());
                    classifyRelation.setSiteId(classifyGroup.getSiteId());
                    classifyRelation.setClassifyId(classify.getClassifyId());
                    classifyRelation.setClassifyGroupId(classifyGroup.getClassifyGroupId());

                    classifyRelationList = classifyRelationRepository.select(classifyRelation);
                    if (classifyRelationList.size() > 0) {
                        classifyRelation.setSequence(classify.getSequence());
                        classifyRelation.setClassifyRelationId(classifyRelationList.get(0).getClassifyRelationId());
                        classifyRelation.setObjectVersionNumber(classifyRelationList.get(0).getObjectVersionNumber());
                        classifyRelationRepository.updateByPrimaryKey(classifyRelation);
                    } else {
                        classifyRelation.setSequence(classify.getSequence());
                        classifyRelationRepository.insert(classifyRelation);
                    }
                }
            }
        }

        return classifyGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassifyGroup delClassify(ClassifyGroup classifyGroup) {
        //if (!checkExists(classifyGroup)) {
        //    throw new CommonException(ErrorMessage);
        //}

        //删除分类项与分类组关系
        if (classifyGroup.getClassifyList() != null && classifyGroup.getClassifyList().size() > 0) {
            for (Classify classify : classifyGroup.getClassifyList()) {
                ClassifyRelation classifyRelation = new ClassifyRelation();
                classifyRelation.setTenantId(classifyGroup.getTenantId());
                classifyRelation.setSiteId(classifyGroup.getSiteId());
                classifyRelation.setClassifyId(classify.getClassifyId());
                classifyRelation.setClassifyGroupId(classifyGroup.getClassifyGroupId());
                classifyRelationRepository.delete(classifyRelation);
            }
        }
        return classifyGroup;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassifyGroup saveCeParameter(ClassifyGroup classifyGroup) {

        //保存分类项
        if (classifyGroup.getCeParameterList() != null && classifyGroup.getCeParameterList().size() > 0) {

            for (CeParameter ceParameter : classifyGroup.getCeParameterList()) {
                CeParameterRelation ceParameterRelation = new CeParameterRelation();
                List<CeParameterRelation> ceParameterRelationList = new ArrayList<CeParameterRelation>();

                if (classifyGroup.get__status() != null && classifyGroup.get__status().equals("create") && ceParameter.getCeParameterId() == null) {
                    //校验“控制要素”是否已存在
                    CeParameter ceParameterExists = new CeParameter();
                    ceParameterExists.setTenantId(classifyGroup.getTenantId());
                    ceParameterExists.setSiteId(classifyGroup.getSiteId());
                    ceParameterExists.setCeParameter(ceParameter.getCeParameter());
                    List<CeParameter> ceParameterExistsList = ceParameterRepository.select(ceParameterExists);
                    if (ceParameterExistsList.size() > 0) {
                        throw new CommonException("数据库中已经存在该数据");//数据库中已存在该数据，请检查
                    }

                    //新增控制要素
                    ceParameter.setSiteId(classifyGroup.getSiteId());
                    ceParameter.setTenantId(classifyGroup.getTenantId());
                    ceParameterRepository.insert(ceParameter);

                    //新增控制要素与分类组关系
                    ceParameterRelation.setTenantId(classifyGroup.getTenantId());
                    ceParameterRelation.setSiteId(classifyGroup.getSiteId());
                    ceParameterRelation.setCeParameterId(ceParameter.getCeParameterId());
                    ceParameterRelation.setClassifyGroupId(classifyGroup.getClassifyGroupId());
                    ceParameterRelationRepository.insert(ceParameterRelation);
                    //} else if (classifyGroup.get_status() != null && classifyGroup.get_status().name().equals("update")) {
                } else if (classifyGroup.get__status() != null && classifyGroup.get__status().equals("update")) {
                    //修改分类项
                    ceParameter.setTenantId(classifyGroup.getTenantId());
                    ceParameter.setSiteId(classifyGroup.getSiteId());
                    ceParameterRepository.updateByPrimaryKey(ceParameter);

                    //保存控制要素关系  1、关系存在,修改顺序  2、关系不存在,新增
                    ceParameterRelation.setTenantId(classifyGroup.getTenantId());
                    ceParameterRelation.setSiteId(classifyGroup.getSiteId());
                    ceParameterRelation.setCeParameterId(ceParameter.getCeParameterId());
                    ceParameterRelation.setClassifyGroupId(classifyGroup.getClassifyGroupId());

                    ceParameterRelationList = ceParameterRelationRepository.select(ceParameterRelation);
                    if (ceParameterRelationList.size() > 0) {
                        ceParameterRelation.setRelationId(ceParameterRelationList.get(0).getRelationId());
                        ceParameterRelation.setObjectVersionNumber(ceParameterRelationList.get(0).getObjectVersionNumber());
                        ceParameterRelationRepository.updateByPrimaryKey(ceParameterRelation);
                    } else {
                        ceParameterRelationRepository.insert(ceParameterRelation);
                    }
                }
            }
        }

        return classifyGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassifyGroup delCeParameter(ClassifyGroup classifyGroup) {
        //if (!checkExists(classifyGroup)) {
        //    throw new CommonException(ErrorMessage);
        //}

        //删除分类项与分类组关系
        if (classifyGroup.getCeParameterList() != null && classifyGroup.getCeParameterList().size() > 0) {
            for (CeParameter classify : classifyGroup.getCeParameterList()) {
                CeParameterRelation ceParameterRelation = new CeParameterRelation();
                ceParameterRelation.setTenantId(classify.getTenantId());
                ceParameterRelation.setSiteId(classify.getSiteId());
                ceParameterRelation.setCeParameterId(classify.getCeParameterId());
                ceParameterRelationRepository.delete(ceParameterRelation);
            }
        }
        return classifyGroup;
    }
}
*/