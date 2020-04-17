package com.hand.spc.pspc_judgement_group.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_judgement_group.dto.Judgement;
import com.hand.spc.pspc_judgement_group.mapper.JudgementGroupMapper;
import com.hand.spc.pspc_judgement_group.mapper.JudgementMapper;
import com.hand.spc.pspc_judgement_group.service.IJudgementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_judgement_group.dto.JudgementGroup;
import com.hand.spc.pspc_judgement_group.service.IJudgementGroupService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class JudgementGroupServiceImpl extends BaseServiceImpl<JudgementGroup> implements IJudgementGroupService {

    @Autowired
    private IJudgementGroupService iJudgementGroupService;
    @Autowired
    private IJudgementService iJudgementService;
    @Autowired
    private JudgementGroupMapper judgementGroupMapper;
    @Autowired
    private JudgementMapper judgementMapper;

    /**
     * @return com.hand.hap.system.dto.ResponseData
     * @Description 根据传进来的拷贝id去保存对应行表信息
     * @author hch
     * @date 2019/8/14 16:35
     * @Param [requestCtx, dto]
     * @version 1.0
     */
    @Override
    public ResponseData copyData(IRequest requestCtx, List<JudgementGroup> dto) {
        ResponseData responseData = new ResponseData();

        for (int i = 0; i < dto.size(); i++) {
            //判断如果状态是否更新，是更新则走更新逻辑。不是则走新增逻辑
            if ("update".equals(dto.get(i).get__status())) {
                JudgementGroup judgementGroup = new JudgementGroup();
                judgementGroup.setJudgementGroupCode(dto.get(i).getJudgementGroupCode());
                long result = judgementGroupMapper.validateUnique(judgementGroup);
                if (result > 0) {
                    responseData.setSuccess(false);
                    responseData.setMessage("数据重复！");
                    return responseData;
                }
                iJudgementGroupService.updateByPrimaryKeySelective(requestCtx, dto.get(i));
            } else {
                //先拷贝行表数据
                Judgement judgement = new Judgement();
                //定义规则组复制id
                Float judgementGroupCopyId = null;
                if (dto.get(i).getJudgementGroupCopy() != null && !"".equals(dto.get(i).getJudgementGroupCopy())) {
                    judgementGroupCopyId = Float.valueOf(dto.get(i).getJudgementGroupCopy());
                }
                judgement.setJudgementGroupId(judgementGroupCopyId);
                List<Judgement> judgementList = judgementMapper.select(judgement);

                JudgementGroup judgementGroup = new JudgementGroup();
                judgementGroup.setJudgementGroupCode(dto.get(i).getJudgementGroupCode());
                List<JudgementGroup> list = judgementGroupMapper.select(judgementGroup);
                if (list != null && list.size() > 0) {
                    responseData.setSuccess(false);
                    responseData.setMessage("数据重复！");
                    return responseData;
                }

                //因为前台新增后就所有只读，所以不用判断更新状态的逻辑，只要新增表就行
                iJudgementGroupService.insertSelective(requestCtx, dto.get(i));
                //新增之后的主键ID（这里的dto.getJudgementGroupId()不再是传进来的id，因为调用insertSelective之后会修改dto的这个值）
                Float judgementGroupId = dto.get(i).getJudgementGroupId();

                //如果是规则组复制的新增才走新增行表逻辑，不是则只新增头
                if (judgementGroupCopyId != null) {
                    //新增头表之后根据新的头表id插入拷贝的行表数据
                    for (int j = 0; j < judgementList.size(); j++) {
                        Judgement judgementNew = new Judgement();
                        judgementNew.setJudgementGroupId(judgementGroupId);
                        judgementNew.setTenantId(judgementList.get(j).getTenantId());
                        judgementNew.setSiteId(judgementList.get(j).getSiteId());
                        judgementNew.setJudgementShortCode(judgementList.get(j).getJudgementShortCode());
                        judgementNew.setJudgementCode(judgementList.get(j).getJudgementCode());
                        judgementNew.setLimitData(judgementList.get(j).getLimitData());
                        judgementNew.setLengthData(judgementList.get(j).getLengthData());
                        judgementNew.setMessageTypeCode(judgementList.get(j).getMessageTypeCode());
                        iJudgementService.insertSelective(requestCtx, judgementNew);
                    }
                }
            }

        }
        responseData.setSuccess(true);
        responseData.setMessage("保存成功！");
        return responseData;
    }


    /**
    * @Description 判异规则组维护前台查询
    * @author hch
    * @date 2019/8/28 15:35
    * @Param [requestContext, dto, page, pageSize]
    * @return java.util.List<com.hand.spc.pspc_judgement_group.dto.JudgementGroup>
    * @version 1.0
    */
    @Override
    public List<JudgementGroup> MySelect(IRequest requestContext, JudgementGroup dto, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return judgementGroupMapper.selectDate(dto);
    }


}