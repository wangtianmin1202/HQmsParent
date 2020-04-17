package com.hand.spc.pspc_attachment_group.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;
import com.hand.spc.pspc_attachment.mapper.SpcAttachmentMapper;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;
import com.hand.spc.pspc_attachment_group.mapper.AttachmentGroupMapper;
import com.hand.spc.pspc_attachment_group.service.IAttachmentGroupService;
import com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation;
import com.hand.spc.pspc_attachment_relation.mapper.AttachmentRelationMapper;
import com.hand.spc.pspc_ce_relationship.dto.CeRelationship;
import com.hand.spc.pspc_entity.dto.Entity;
import com.hand.spc.pspc_entity.mapper.EntityMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AttachmentGroupServiceImpl extends BaseServiceImpl<AttachmentGroup> implements IAttachmentGroupService {

    @Autowired
    private AttachmentGroupMapper attachmentGroupMapper;

    @Autowired
    private AttachmentRelationMapper  attachmentRelationMapper;

    @Autowired
    private EntityMapper entityMapper;


    /**
     *
     * @Description 查询附着要素组及明细
     *
     * @author yuchao.wang
     * @date 2019/8/9 17:53
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_attachment_group.dto.AttachmentGroup>
     *
     */
    @Override
    public List<AttachmentGroup> queryGroupDetail(IRequest requestContext, AttachmentGroup dto, int page, int pageSize) {
        //分页查询当前附着要素组
        PageHelper.startPage(page, pageSize);
        List<AttachmentGroup> attachmentGroupList = attachmentGroupMapper.queryAttachmentGroup(dto);

        return attachmentGroupList;
    }

    /**
     *
     * @Description 删除附着对象组与控制要素组关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 23:41
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData deleteRelationship(List<AttachmentGroup> dto) throws Exception {
        ResponseData responseData = new ResponseData();

        for (AttachmentGroup attachmentGroup:dto) {
            //先查询在实体表中是否有引用
            Entity entity = new Entity();
            entity.setAttachmentGroupId(attachmentGroup.getAttachmentGroupId());
            List<Entity> entities = entityMapper.selectEntityInfo(entity);
            if(entities != null && !entities.isEmpty()) {
                throw new Exception("附着对象组[" + attachmentGroup.getAttachmentGroupDetail()
                        + "]已维护实体控制图关系，无法删除");
            }

            //删除附着对象组
            attachmentGroupMapper.delete(attachmentGroup);

            //删除附着对象组与附着对象关系
            List<AttachmentRelation> attachmentRelationList = attachmentRelationMapper
                    .selectRelationByCroupId(attachmentGroup.getAttachmentGroupId());
            for (AttachmentRelation attachmentRelation : attachmentRelationList) {
                attachmentRelationMapper.delete(attachmentRelation);
            }
        }
        return responseData;
    }

    @Override
    public ResponseData addAttachGroup(IRequest requestCtx, List<SpcAttachment> dtos) {
        StringBuilder sb = new StringBuilder();
        dtos.forEach(dto -> {
            sb.append(dto.getInnerMap().get("type")+":"+dto.getInnerMap().get("functionCode")+";");
        });
        AttachmentGroup attachmentGroup = new AttachmentGroup();
        attachmentGroup.setCeGroupId(dtos.get(0).getCeGroupId());
        attachmentGroup.setAttachmentGroupDescription(sb.toString());
        self().insertSelective(requestCtx,attachmentGroup);
        return new ResponseData();
    }
}