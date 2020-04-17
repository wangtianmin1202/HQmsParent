package com.hand.spc.pspc_ce_parameter.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.constants.SpcConstants;
import com.hand.spc.pspc_ce_parameter.dto.CeParameter;
import com.hand.spc.pspc_ce_parameter.mapper.CeParameterMapper;
import com.hand.spc.pspc_ce_parameter.service.ICeParameterService;
import com.hand.spc.pspc_ce_parameter_relation.dto.CeParameterRelation;
import com.hand.spc.pspc_ce_parameter_relation.mapper.CeParameterRelationMapper;
import com.hand.spc.pspc_ce_relationship.dto.CeRelationship;
import com.hand.spc.pspc_ce_relationship.mapper.CeRelationshipMapper;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.mapper.EntityMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class CeParameterServiceImpl extends BaseServiceImpl<CeParameter> implements ICeParameterService,SpcConstants {

    @Autowired
    private CeParameterRelationMapper ceParameterRelationMapper;

    @Autowired
    private CeParameterMapper ceParameterMapper;

    @Autowired
    private CeRelationshipMapper ceRelationshipMapper;

    @Autowired
    private EntityMapper entityMapper;


    @Override
    public ResponseData selectCeParameter(IRequest requestContext, CeParameter dto, int page, int pageSize) {
        CeParameterRelation ceParameterRelation = new CeParameterRelation();
        ceParameterRelation.setClassifyGroupId(dto.getClassifyGroupId());
        //查询控制要素
        List<CeParameterRelation> classifyRelations = ceParameterRelationMapper.select(ceParameterRelation);

        List<CeParameter> ceParameterList = new ArrayList<>();
        classifyRelations.stream().forEach(item -> {
            CeParameter ceParameter = new CeParameter();
            ceParameter.setCeParameterId(item.getCeParameterId());
            ceParameter = ceParameterMapper.selectByPrimaryKey(ceParameter);
            if(null != ceParameter){
                ceParameter.setRelationId(item.getRelationId());
                ceParameterList.add(ceParameter);
            }
        });

        List<CeParameter> ceParameters = ceParameterList.stream()
                .skip((page-1) * pageSize)
                .limit(pageSize).collect(Collectors.toList());

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setRows(ceParameters);
        responseData.setTotal((long) classifyRelations.size());

        return responseData;
    }

    /**
     *
     * @Description 根据条件查询控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/9 17:05
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ce_parameter.dto.CeParameter>
     *
     */
    @Override
    public List<CeParameter> selectCeParameterByGroupId(IRequest requestContext, CeParameter dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ceParameterMapper.selectCeParameterByGroupId(dto);
    }

    /**
     *
     * @Description 删除控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/10 15:11
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData deleteCeParameter(List<CeParameter> dto) throws Exception {
        ResponseData responseData = new ResponseData();

        for (CeParameter ceParameter:dto) {
            //先查询在实体表中是否有引用
            Entity entity = new Entity();
            entity.setCeParameterId(ceParameter.getCeParameterId());
            List<Entity> entities = entityMapper.selectEntityInfo(entity);
            if(entities != null && !entities.isEmpty()) {
                throw new Exception("控制要素[" + ceParameter.getCeParameterName() + "]已维护实体控制图关系，无法删除");
            }

            //在查询在关系表中是否有引用
            CeRelationship ceRelationship = new CeRelationship();
            ceRelationship.setCeParameterId(ceParameter.getCeParameterId());
            List<CeRelationship> select = ceRelationshipMapper.select(ceRelationship);
            if(select != null && !select.isEmpty()){
                throw new Exception("其他控制要素组引用此控制要素[" + ceParameter.getCeParameterName() + "]，无法删除");
            }

            //再执行删除
            ceParameterMapper.delete(ceParameter);
        }
        return responseData;
    }

    /**
     *
     * @Description 模糊查询控制要素
     *
     * @author yuchao.wang
     * @date 2019/8/21 22:41
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ce_parameter.dto.CeParameter>
     *
     */
    @Override
    public List<CeParameter> selectCeParameterSelective(IRequest requestContext, CeParameter dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ceParameterMapper.selectCeParameterSelective(dto);
    }

    @Override
    public ResponseData saveAndUpdateClassify(IRequest requestContext, CeParameter dto) {
        CeParameter ceParameter = new CeParameter();
        ceParameter.setSiteId(dto.getSiteId());
        ceParameter.setTenantId(dto.getTenantId());
        ceParameter.setCeParameter(dto.getCeParameter());
        //更新
        ceParameterMapper.updateByPrimaryKeySelective(dto);
        //新增控制要素关系
        CeParameterRelation ceParameterRelation = new CeParameterRelation();
        ceParameterRelation.setCeParameterId(dto.getCeParameterId());
        ceParameterRelation.setClassifyGroupId(dto.getClassifyGroupId());
        int count = ceParameterRelationMapper.selectCount(ceParameterRelation);
        if(count > 0){
            throw new RuntimeException("该分类组下此控制要素已存在");
        }
        ceParameterRelation.setTenantId(-1L);
        ceParameterRelation.setSiteId(-1L);
        ceParameterRelationMapper.insertSelective(ceParameterRelation);
        return new ResponseData(true);
    }
}