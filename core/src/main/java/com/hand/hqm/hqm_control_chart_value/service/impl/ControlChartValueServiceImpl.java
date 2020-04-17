package com.hand.hqm.hqm_control_chart_value.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_control_chart_value.dto.ControlChartValue;
import com.hand.hqm.hqm_control_chart_value.service.IControlChartValueService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ControlChartValueServiceImpl extends BaseServiceImpl<ControlChartValue> implements IControlChartValueService{

}