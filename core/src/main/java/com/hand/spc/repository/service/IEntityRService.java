package com.hand.spc.repository.service;


import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CountSampleDataWaitVO;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.EntityByChartVO;
import com.hand.spc.repository.dto.SampleGroupDataVO;
import com.hand.spc.utils.Page;

public interface IEntityRService extends IBaseService<EntityR>, ProxySelf<EntityR> {

    /**
     * 查询待分组数据
     *
     * @param sampleGroupDataVO
     * @return
     */
    public List<EntityR> listSubgroupWaitData(SampleGroupDataVO sampleGroupDataVO);

    /**
     * 查询 样本数据预处理(计数)
     *
     * @param countSampleDataWaitVO
     * @return
     */
    public List<EntityR> listCountSampleDataWait(CountSampleDataWaitVO countSampleDataWaitVO);

    //查询实体控制图
    public Page<EntityR> queryEntity(EntityR entity);

    //查询 实体控制图&控制图关联
    public List<EntityByChartVO> queryEntityByChart(EntityR entity);
}
