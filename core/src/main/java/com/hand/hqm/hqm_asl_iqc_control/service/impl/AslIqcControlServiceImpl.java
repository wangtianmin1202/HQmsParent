package com.hand.hqm.hqm_asl_iqc_control.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_asl_iqc_control.mapper.AslIqcControlMapper;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;
import com.hand.hqm.hqm_switch_score.mapper.SwitchScoreMapper;
import com.hand.hqm.hqm_switch_score.service.ISwitchScoreService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_asl_iqc_control.dto.AslIqcControl;
import com.hand.hqm.hqm_asl_iqc_control.service.IAslIqcControlService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AslIqcControlServiceImpl extends BaseServiceImpl<AslIqcControl> implements IAslIqcControlService{
    @Autowired
    private AslIqcControlMapper aslIqcControlMapper;
    @Autowired
    private SwitchScoreMapper switchScoreMapper;
    @Autowired
    private ISwitchScoreService iSwitchScoreService;
    @Override
    public List<AslIqcControl> myselect(IRequest requestContext, AslIqcControl dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return aslIqcControlMapper.myselect(dto);
    }

    @Override
    public List<AslIqcControl> batchTablesUpdate(IRequest request,@StdWho List<AslIqcControl> list) {
//        IBaseService<AslIqcControl> self = ((IBaseService<AslIqcControl>) AopContext.currentProxy());
        for (AslIqcControl t : list) {
            SwitchScore switchScore = new SwitchScore();
            switchScore.setItemId(t.getItemId());
            switchScore.setPlantId(t.getPlantId());
            switchScore.setConsecutiveConformingLot(t.getConsecutiveConformingLot());
            switchScore.setNonnconformingLot(t.getNonnconformingLot());
            switchScore.setSupplierId(t.getSupplierId());
            switchScore.setSupplierSitId(t.getSupplierSitId());
            switchScore.setSamplePlanTypeNext("N");
            switchScore.setSamplePlanTypeNow("N");
            switchScore.setChangeFlag(t.getChangeFlag());
            switchScore.setSwitchScore(0f);
            switch (((BaseDTO) t).get__status()) {
                case DTOStatus.ADD:
                    self().insertSelective(request, t);
                    iSwitchScoreService.self().insertSelective(request,switchScore);
                    t.setScoreId(switchScore.getScoreId());
                    break;
                case DTOStatus.UPDATE:
                    switchScore.setScoreId(t.getScoreId());
                    if (useSelectiveUpdate()) {
                        self().updateByPrimaryKeySelective(request, t);
                        iSwitchScoreService.self().updateByPrimaryKeySelective(request,switchScore);
                    } else {
                        self().updateByPrimaryKey(request, t);
                        iSwitchScoreService.self().updateByPrimaryKey(request,switchScore);
                    }
                    break;
                case DTOStatus.DELETE:
                    switchScore.setScoreId(t.getScoreId());
                    self().deleteByPrimaryKey(t);
                    iSwitchScoreService.self().deleteByPrimaryKey(switchScore);
                    break;
                default:
                    break;
            }
        }
        return list;
    }
}