package com.hand.spc.pspc_judgement_group.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.pspc_judgement_group.mapper.JudgementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.pspc_judgement_group.dto.Judgement;
import com.hand.spc.pspc_judgement_group.service.IJudgementService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class JudgementServiceImpl extends BaseServiceImpl<Judgement> implements IJudgementService{

    @Autowired
    private JudgementMapper judgementMapper;

    @Override
    public List<Judgement> selectData(IRequest requestContext, Judgement dto, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return judgementMapper.selectData(dto);

    }

    @Override
    public ResponseData validateMustInput(IRequest requestCtx, List<Judgement> dto) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);



        for (Judgement judgement : dto) {
            //新增和修改的时候校验数据唯一性
            if("add".equals(judgement.get__status())){
                Judgement judgementDto = new Judgement();
                judgementDto.setTenantId(judgement.getTenantId());
                judgementDto.setSiteId(judgement.getSiteId());
                judgementDto.setJudgementShortCode(judgement.getJudgementShortCode());
                judgementDto.setJudgementGroupId(judgement.getJudgementGroupId());

                List<Judgement> list = judgementMapper.select(judgementDto);
                if (list != null && list.size() > 0) {
                    responseData.setSuccess(false);
                    responseData.setMessage("数据重复！");
                    return responseData;
                }
            }else if("update".equals(judgement.get__status())){
                Judgement judgementDto = new Judgement();
                judgementDto.setTenantId(judgement.getTenantId());
                judgementDto.setSiteId(judgement.getSiteId());
                judgementDto.setJudgementShortCode(judgement.getJudgementShortCode());
                judgementDto.setJudgementGroupId(judgement.getJudgementGroupId());
                judgementDto.setJudgementId(judgement.getJudgementId());
                long result = judgementMapper.validateUnique(judgementDto);

                if(result > 0){
                    responseData.setSuccess(false);
                    responseData.setMessage("数据重复！");
                    return responseData;
                }
            }
        }
        return responseData;
    }
}