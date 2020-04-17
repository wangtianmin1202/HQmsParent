package com.hand.spc.pspc_ce_relationship.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_ce_relationship.dto.CeRelationship;
import com.hand.spc.pspc_ce_relationship.mapper.CeRelationshipMapper;
import com.hand.spc.pspc_ce_relationship.service.ICeRelationshipService;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CeRelationshipServiceImpl extends BaseServiceImpl<CeRelationship> implements ICeRelationshipService {

    @Autowired
    private CeRelationshipMapper ceRelationshipMapper;

    @Autowired
    private EntityMapper entityMapper;

    /**
     *
     * @Description 删除控制要素与控制要素组关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 23:20
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData deleteRelationship(List<CeRelationship> dto) throws Exception {
        ResponseData responseData = new ResponseData();

        for (CeRelationship ceRelationship:dto) {
            //先查询在实体表中是否有引用
            Entity entity = new Entity();
            entity.setCeParameterId(ceRelationship.getCeParameterId());
            entity.setCeGroupId(ceRelationship.getCeGroupId());
            List<Entity> entities = entityMapper.selectEntityInfo(entity);
            if(entities != null && !entities.isEmpty()) {
                throw new Exception("控制要素[" + ceRelationship.getCeParameterName() + "]已维护实体控制图关系，无法删除");
            }

            //再执行删除
            ceRelationshipMapper.delete(ceRelationship);
        }
        return responseData;
    }

    /**
     *
     * @Description 更新控制要素与控制要素组关系
     *
     * @author yuchao.wang
     * @date 2019/9/5 14:13
     * @param requestCtx
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @Override
    public ResponseData updateRelationship(IRequest requestCtx, List<CeRelationship> dto) {
        ResponseData responseData = new ResponseData();

        //校验唯一性
        for (CeRelationship ceRelationship : dto) {
            if("add".equals(ceRelationship.get__status()) || "update".equals(ceRelationship.get__status())) {
                CeRelationship relationship = ceRelationshipMapper.selectRelationshipForUniqueCheck(ceRelationship);
                if(relationship != null){
                    responseData.setSuccess(false);
                    responseData.setMessage("控制要素[" + ceRelationship.getCeParameterName() + "]重复");
                    return responseData;
                }
            }
        }

        responseData.setRows(self().batchUpdate(requestCtx, dto));
        return responseData;
    }
}