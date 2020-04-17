package com.hand.spc.pspc_attachment_relation.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;
import com.hand.spc.pspc_attachment.mapper.SpcAttachmentMapper;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;
import com.hand.spc.pspc_attachment_group.service.IAttachmentGroupService;
import com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation;
import com.hand.spc.pspc_attachment_relation.service.IAttachmentRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AttachmentRelationServiceImpl extends BaseServiceImpl<AttachmentRelation> implements IAttachmentRelationService {

    @Autowired
    private IAttachmentGroupService attachmentGroupService;

    @Autowired
    private SpcAttachmentMapper spcAttachmentMapper;

    /**
     *
     * @Description 保存附着对象组与附着对象关系
     *
     * @author yuchao.wang
     * @date 2019/8/10 14:59
     * @param requestCtx
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData saveAttachmentRelation(IRequest requestCtx, List<AttachmentRelation> dto) {
        ResponseData responseData = new ResponseData();

        if(dto != null && !dto.isEmpty()) {
            //判断附着对象组是否维护
            if(dto.get(0).getAttachmentGroupId() == null) {
                AttachmentGroup attachmentGroup = new AttachmentGroup();
                attachmentGroup.setCeGroupId(dto.get(0).getCeGroupId());
                attachmentGroupService.insertSelective(requestCtx, attachmentGroup);

                //添加新增标识 附着对象组ID
                for (int i = 0; i < dto.size(); i++) {
                    dto.get(i).set__status("add");
                    dto.get(i).setAttachmentGroupId(attachmentGroup.getAttachmentGroupId());
                }
            } else {
                //添加新增标识
                for (int i = 0; i < dto.size(); i++) {
                    dto.get(i).set__status("add");
                }
            }
            //先插入附着对象关系
            responseData.setRows(self().batchUpdate(requestCtx, dto));

            //更新附着对象组描述
            updateAttachmentGroupDescription(requestCtx, dto.get(0).getAttachmentGroupId());
        }

        return responseData;
    }

    /**
     *
     * @Description 删除附着对象组与附着对象关系
     *
     * @author yuchao.wang
     * @date 2019/8/10 14:59
     * @param requestCtx
     * @param dto
     * @return
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRelation(IRequest requestCtx, List<AttachmentRelation> dto) {
        if(dto != null && !dto.isEmpty()) {
            //删除附着要素
            self().batchDelete(dto);

            //更新附着对象组描述
            updateAttachmentGroupDescription(requestCtx, dto.get(0).getAttachmentGroupId());
        }
    }

    /**
     *
     * @Description 更新附着对象组描述
     *
     * @author yuchao.wang
     * @date 2019/8/10 14:59
     * @param requestCtx
     * @param attachmentGroupId
     * @return
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAttachmentGroupDescription(IRequest requestCtx, Long attachmentGroupId){
        AttachmentGroup attachmentGroup = new AttachmentGroup();
        attachmentGroup.setAttachmentGroupId(attachmentGroupId);
        attachmentGroup.setAttachmentGroupDescription("");

        //查询附着要素
        SpcAttachment attachment = new SpcAttachment();
        attachment.setAttachmentGroupId(attachmentGroupId);
        List<SpcAttachment> attachmentList = spcAttachmentMapper.selectAttachmentsByCroupId(attachment);

        //拼接明细并更新
        if(attachmentList != null && !attachmentList.isEmpty()){
            StringBuilder attachmentDetail = new StringBuilder();
            for(int j=0; j<attachmentList.size(); j++){
                attachmentDetail.append(attachmentList.get(j).getAttachmentType()).append(":")
                        .append(attachmentList.get(j).getAttachmentCode()).append(";");
            }
            attachmentGroup.setAttachmentGroupDescription(attachmentDetail.toString());
        }

        //更新描述
        attachmentGroupService.updateByPrimaryKeySelective(requestCtx, attachmentGroup);
    }
}