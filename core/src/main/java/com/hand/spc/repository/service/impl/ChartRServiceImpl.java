package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.ChartR;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.mapper.ChartRMapper;
import com.hand.spc.repository.service.IChartRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChartRServiceImpl extends BaseServiceImpl<ChartR> implements IChartRService {

    @Autowired
    public ChartRMapper chartMapper;

    @Override
    public ChartR selectByChartCode(ChartR chart) {
        List<ChartR> c = chartMapper.selectByChartCode(chart);
        if(c.size() >0){
            return c.get(0);
        }else {
            ChartR chart1 = new ChartR();
            return chart1;
        }
    }

    /**
     * 根据实体控制图获取控制
     *
     * @param entity
     * @return
     */
    public ChartR queryChartByEntity(EntityR entity) {
        return chartMapper.queryChartByEntity(entity);
    }
}
