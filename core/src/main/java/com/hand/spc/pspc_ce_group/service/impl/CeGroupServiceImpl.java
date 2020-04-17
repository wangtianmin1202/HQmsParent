package com.hand.spc.pspc_ce_group.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;
import com.hand.spc.pspc_attachment_group.mapper.AttachmentGroupMapper;
import com.hand.spc.pspc_attachment_group.service.IAttachmentGroupService;
import com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation;
import com.hand.spc.pspc_attachment_relation.mapper.AttachmentRelationMapper;
import com.hand.spc.pspc_attachment_relation.service.IAttachmentRelationService;
import com.hand.spc.pspc_ce_group.dto.CeGroup;
import com.hand.spc.pspc_ce_group.mapper.CeGroupMapper;
import com.hand.spc.pspc_ce_group.service.ICeGroupService;
import com.hand.spc.pspc_ce_parameter.dto.CeParameter;
import com.hand.spc.pspc_ce_parameter.mapper.CeParameterMapper;
import com.hand.spc.pspc_ce_relationship.dto.CeRelationship;
import com.hand.spc.pspc_ce_relationship.mapper.CeRelationshipMapper;
import com.hand.spc.pspc_ce_relationship.service.ICeRelationshipService;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.mapper.EntityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Caret;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CeGroupServiceImpl extends BaseServiceImpl<CeGroup> implements ICeGroupService{

    @Autowired
    private CeGroupMapper ceGroupMapper;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private CeParameterMapper ceParameterMapper;

    @Autowired
    private CeRelationshipMapper ceRelationshipMapper;

    @Autowired
    private AttachmentGroupMapper attachmentGroupMapper;

    @Autowired
    private AttachmentRelationMapper attachmentRelationMapper;

    @Autowired
    private ICeRelationshipService ceRelationshipService;

    @Autowired
    private IAttachmentGroupService attachmentGroupService;

    @Autowired
    private IAttachmentRelationService attachmentRelationService;


    /**
     *
     * @Description 控制要素组副本保存
     *
     * @author yuchao.wang
     * @date 2019/8/9 16:08
     * @param requestCtx
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData copySave(IRequest requestCtx, CeGroup dto) {
        ResponseData responseData = new ResponseData();
        AttachmentGroup attachmentGroup = new AttachmentGroup();
        Long oldCeGroupId = dto.getOldCeGroupId();
        Long ceGroupId = dto.getCeGroupId();

        //查询当前控制要素组是否存在关系
        List<CeRelationship> ceRelationships = ceRelationshipMapper.selectRelationshipByGroupId(ceGroupId);
        if(ceRelationships != null && !ceRelationships.isEmpty()) {
            responseData.setSuccess(false);
            responseData.setMessage("该控制要素组已经维护控制要素，不允许保存");
            return responseData;
        }
        attachmentGroup.setCeGroupId(ceGroupId);
        List<AttachmentGroup> attachmentGroups = attachmentGroupMapper.queryAttachmentGroup(attachmentGroup);
        if(attachmentGroups != null && !attachmentGroups.isEmpty()) {
            responseData.setSuccess(false);
            responseData.setMessage("该控制要素组已经维护附着对象组，不允许保存");
            return responseData;
        }

        //查询原控制要素关系信息
        List<CeRelationship> ceRelationshipList = ceRelationshipMapper.selectRelationshipByGroupId(oldCeGroupId);
        if(ceRelationshipList != null && !ceRelationshipList.isEmpty()) {
            for(int i=0; i<ceRelationshipList.size(); i++){
                //更新字段
                ceRelationshipList.get(i).setCeRelationshipId(null);
                ceRelationshipList.get(i).setCeGroupId(ceGroupId);

                //保存新的控制要素关系
                ceRelationshipService.insertSelective(requestCtx, ceRelationshipList.get(i));
            }
        }

        //查询原附着对象组信息
        attachmentGroup = new AttachmentGroup();
        attachmentGroup.setCeGroupId(oldCeGroupId);
        List<AttachmentGroup> attachmentGroupList = attachmentGroupMapper.queryAttachmentGroup(attachmentGroup);
        if(attachmentGroupList != null && !attachmentGroupList.isEmpty()) {
            for(int i=0; i<attachmentGroupList.size(); i++){
                attachmentGroup = attachmentGroupList.get(i);
                //查找附着对象关系
                List<AttachmentRelation> attachmentRelationList = attachmentRelationMapper.selectRelationByCroupId(attachmentGroup.getAttachmentGroupId());

                //更新附着对象组字段
                attachmentGroup.setAttachmentGroupId(null);
                attachmentGroup.setCeGroupId(ceGroupId);

                //保存新的附着对象组信息，返回新的组ID
                attachmentGroupService.insertSelective(requestCtx, attachmentGroup);

                //重新保存附着对象组和附着对象关系
                if(attachmentRelationList != null && !attachmentRelationList.isEmpty()){
                    for(int j=0; j<attachmentRelationList.size(); j++){
                        //更新字段
                        attachmentRelationList.get(j).setAttachmentGroupId(attachmentGroup.getAttachmentGroupId());
                        attachmentRelationList.get(j).setAttachmentRelationId(null);

                        //保存新的附着对象关系
                        attachmentRelationService.insertSelective(requestCtx, attachmentRelationList.get(j));
                    }
                }
            }
        }

        return responseData;
    }

    /**
     *
     * @Description 删除控制要素组及其关系
     *
     * @author yuchao.wang
     * @date 2019/8/10 23:54
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData deleteCeGroupAndRelationship(CeGroup dto) throws Exception {
        ResponseData responseData = new ResponseData();

        //先查询在实体表中是否有引用
        Entity entity = new Entity();
        entity.setCeGroupId(dto.getCeGroupId());
        List<Entity> entities = entityMapper.selectEntityInfo(entity);
        if(entities != null && !entities.isEmpty()) {
            throw new Exception("附着对象组[" + dto.getDescription() + "]已维护实体控制图关系，无法删除");
        }

        //删除控制要素组和控制要素关系
        CeParameter ceParameter = new CeParameter();
        ceParameter.setCeGroupId(dto.getCeGroupId());
        List<CeRelationship> ceRelationshipList = ceRelationshipMapper.selectRelationshipByGroupId(dto.getCeGroupId());
        if(ceRelationshipList != null && !ceRelationshipList.isEmpty()) {
            ceRelationshipService.deleteRelationship(ceRelationshipList);
        }

        //删除控制要素组和附着对象组关系
        AttachmentGroup attachmentGroup = new AttachmentGroup();
        attachmentGroup.setCeGroupId(dto.getCeGroupId());
        List<AttachmentGroup> attachmentGroupList = attachmentGroupMapper.queryAttachmentGroup(attachmentGroup);
        if(attachmentGroupList != null && !attachmentGroupList.isEmpty()) {
            attachmentGroupService.deleteRelationship(attachmentGroupList);
        }

        //再执行删除
        ceGroupMapper.delete(dto);

        return responseData;
    }

    /**
     *
     * @Description 模糊查询控制要素组
     *
     * @author yuchao.wang
     * @date 2019/8/21 22:10
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ce_group.dto.CeGroup>
     *
     */
    @Override
    public List<CeGroup> selectCeGroup(IRequest requestContext, CeGroup dto, int page, int pageSize) {
        //分页查询当前附着要素组
        PageHelper.startPage(page, pageSize);
        List<CeGroup> ceGroupList = ceGroupMapper.selectCeGroup(dto);

        return ceGroupList;
    }
}