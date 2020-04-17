package com.hand.spc.pspc_attachment.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_attachment.dto.MenuItem;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;
import com.hand.spc.pspc_attachment.mapper.SpcAttachmentMapper;
import com.hand.spc.pspc_attachment.service.ISpcAttachmentService;
import com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation;
import com.hand.spc.pspc_attachment_relation.mapper.AttachmentRelationMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class SpcAttachmentServiceImpl extends BaseServiceImpl<SpcAttachment> implements ISpcAttachmentService {
    @Autowired
    private SpcAttachmentMapper spcAttachmentMapper;
    @Autowired
    private AttachmentRelationMapper attachmentRelationMapper;

    /**
     *
     * @Description 根据附着对象组ID查找附着对象ID
     *
     * @author yuchao.wang
     * @date 2019/8/9 16:45
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_attachment.dto.SpcAttachment>
     *
     */
    @Override
    public List<SpcAttachment> selectByCroupId(IRequest requestContext, SpcAttachment dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        MenuItem menuItem = new MenuItem();
        return spcAttachmentMapper.selectAttachmentsByCroupId(dto);
    }

    /**
     * @Author han.zhang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:40 2019/8/19
     * @Param [requestContext, dto]
     */
    @Override
    public List<MenuItem> queryTreeData(IRequest requestContext, SpcAttachment dto) {
        //查询根数据
        List<SpcAttachment> spcAttachments = spcAttachmentMapper.selectParentAttachment(dto);
        //查询下层数据
        List<MenuItem> menuItems = castToMenuItem(spcAttachments);
        return menuItems;
    }

    /**
     * @Author han.zhang
     * @Description 更新或者保存附着对象
     * @Date 19:40 2019/8/19
     * @Param [requestCtx, dto]
     */
    @Override
    public ResponseData updateOrAdd(IRequest requestCtx, SpcAttachment dto) {
        //id没有是新增
        if(null == dto.getAttachmentId()){
            SpcAttachment attachment = new SpcAttachment();
            attachment.setAttachmentCode(dto.getAttachmentCode());
            int count = spcAttachmentMapper.selectCount(attachment);
            if(count > 0){
                throw new RuntimeException("附着对象已存在");
            }
            //新增
            spcAttachmentMapper.insertSelective(dto);
        }else{
            //根据有没有修改父层级附着对象来判断是更新还是新增
            SpcAttachment spcAttachment = spcAttachmentMapper.selectByPrimaryKey(dto);
            if(spcAttachment.getParentAttachmentId().equals(dto.getParentAttachmentId())){
                //更新
                spcAttachmentMapper.updateByPrimaryKeySelective(dto);
            }else{
                SpcAttachment targetAttachment = new SpcAttachment();
                BeanUtils.copyProperties(spcAttachment,targetAttachment);
                targetAttachment.setAttachmentId(null);
                targetAttachment.setAttachmentCode(dto.getAttachmentCode());
                targetAttachment.setAttachmentType(dto.getAttachmentType());
                targetAttachment.setDescription(dto.getDescription());
                targetAttachment.setParentAttachmentId(dto.getParentAttachmentId());
                spcAttachmentMapper.insertSelective(targetAttachment);
            }

        }
        return new ResponseData(true);
    }

    /**
     * @Author han.zhang
     * @Description 删除附着对象及其后代
     * @Date 11:49 2019/8/20
     * @Param [dto]
     */
    @Override
    public void deleteRow(SpcAttachment dto) {

        //查询后代所有需要删除的附着对象
        List<SpcAttachment> delList = new ArrayList<>();
        delList.add(dto);
        queryDownAttachment(delList,Collections.singletonList(dto));
        //校验
        checkAttachmentDel(delList);
        //删除
        self().batchDelete(delList);
    }

    /**
     * @Author han.zhang
     * @Description 将list转换成目录菜单
     * @Date 11:52 2019/8/19
     * @Param [spcAttachments]
     */
    private List<MenuItem> castToMenuItem(List<SpcAttachment> spcAttachments){
        //根
        List<MenuItem> menuItems = new ArrayList<>();
        spcAttachments.stream()
                .forEach(spcAttachment -> {
                    if (spcAttachment.getParentAttachmentId() == null) {
                        menuItems.add(createMenuItem(spcAttachment));
                    }
                });
        //添加子
        menuItems.stream().forEach(item -> {
            additem(item);
        });
        return menuItems;
    }

    /**
     * @Author han.zhang
     * @Description 将SpcAttachment对象转换成菜单
     * @Date 13:50 2019/8/19
     * @Param [attachment]
     */
    private MenuItem createMenuItem(SpcAttachment attachment){
        MenuItem menu = new MenuItem();
        menu.setFunctionCode(attachment.getAttachmentCode());
        menu.setText(attachment.getDescription());
        menu.setType(attachment.getAttachmentType());
        menu.setId(attachment.getAttachmentId());
        return menu;
    }

    /**
     * @Author han.zhang
     * @Description 有子元素则添加
     * @Date 13:51 2019/8/19
     * @Param [menuItem]
     */
    private void additem(MenuItem menuItem){
        //定义子菜单
        List<MenuItem> children = new ArrayList<>();
        //查询子
        SpcAttachment spcAttachment = new SpcAttachment();
        spcAttachment.setParentAttachmentId(menuItem.getId());
        List<SpcAttachment> spcAttachments = spcAttachmentMapper.select(spcAttachment);
        //添加子
        spcAttachments.stream().forEach(item -> {
            children.add(createMenuItem(item));
        });
        //设定子菜单
        if(children.size() > 0){
            menuItem.setChildren(children);
            //递归，有子 继续添加
            children.stream().forEach(childrenItem -> {
                additem(childrenItem);
            });
        }
        menuItem.setChildren(children);
    }

    /**
     * @Author han.zhang
     * @Description 查询后代子附着对象
     * @Date 11:52 2019/8/20
     * @Param
     */
    public void queryDownAttachment(List<SpcAttachment> delList,List<SpcAttachment> dtos){
        dtos.stream().forEach(dto ->{
            SpcAttachment parnetAttachment = new SpcAttachment();
            parnetAttachment.setParentAttachmentId(dto.getAttachmentId());
            List<SpcAttachment> spcAttachments = spcAttachmentMapper.select(parnetAttachment);
            if(spcAttachments.size() > 0){
                delList.addAll(spcAttachments);
                queryDownAttachment(delList,spcAttachments);
            }
        });
    }

    /**
     * @Author han.zhang
     * @Description 校验附着对象删除
     * @Date 12:25 2019/8/20
     * @Param [dtos]
     */
    public void checkAttachmentDel(List<SpcAttachment> dtos){
        dtos.forEach(dto -> {
            AttachmentRelation attachmentRelation = new AttachmentRelation();
            attachmentRelation.setAttachmentId(dto.getAttachmentId());
            int count = attachmentRelationMapper.selectCount(attachmentRelation);
            if(count > 0){
                throw new RuntimeException("该附着对象或下级附着对象已有实体图/样本数据，无法删除。附着对象："+dto.getAttachmentCode());
            }
        });
    }


}