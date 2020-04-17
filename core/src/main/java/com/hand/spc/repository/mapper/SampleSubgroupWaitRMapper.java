package com.hand.spc.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.SampleDataWaitR;
import com.hand.spc.repository.dto.SampleSubgroupWaitR;

public interface SampleSubgroupWaitRMapper extends Mapper<SampleSubgroupWaitR> {

    /**
     * 新增SE对应历史样本数据处理
     *
     * @param entity
     * @return
     */
    public int insertOldSubgroupWaitData(EntityR entity);

    /**
     * 新增SE对应样本数据至 样本数据待处理
     *
     * @param sampleDataWait
     * @return
     */
    public int insertSubgroupWaitData(SampleDataWaitR sampleDataWait);

    /**
     * 根据预分组数据ID集合删除数据
     *
     * @param sampleSubgroupWait
     * @return
     */
    public int deleteSubgroupWaitDataByIdList(SampleSubgroupWaitR sampleSubgroupWait);
    
    List<SampleSubgroupWaitR> selectSubgroupWaitData(SampleDataWaitR sampleDataWait);// modified 20190903  <!--与xml里面方法参数类型不一致 暂无影响 -->
    List<SampleSubgroupWaitR> selectSubgroupWaitDataModified(@Param("whereInSql") String whereInSql);// modified 20190903
    
    int batchInsertSampleSubgroupWait(@Param("sampleSubgroupWaitList")List<SampleSubgroupWaitR> sampleSubgroupWaitList);
}
