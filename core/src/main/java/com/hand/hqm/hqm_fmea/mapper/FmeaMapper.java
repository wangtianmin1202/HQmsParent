package com.hand.hqm.hqm_fmea.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;

public interface FmeaMapper extends Mapper<Fmea>{
	/**
     * 查询DFMEA 行信息
     * @param dto list 操作数据集          
     * @return 操作结果
     */
	List<Fmea> dmyselect(Fmea dto);
	/**
     * 查询PFMEA 行信息
     * @param dto list 操作数据集
     * @param request 请求
     * @return 操作结果
     */
    List<Fmea> selectMaxNumberD(Fmea dto);
    /**
     * 查询PFMEA 行信息
     * @param dto list 操作数据集
     * @param request 请求
     * @return 操作结果
     */
    List<Fmea> pmyselect(Fmea dto);
    /**
     * 查询当天流水号的最大后坠
     * @param dto list 操作数据集
     * @param request 请求
     * @return 操作结果
     */
    List<Fmea> selectMaxNumberP(Fmea dto);
    /**
     * 查询DFMEA版本
     * @param dto list 操作数据集
     * @param request 请求
     * @return 操作结果
     */
    List<Fmea> DselectV(Fmea dto);
    /**
     * 查询PFMEA版本
     * @param dto list 操作数据集
     * @param request 请求
     * @return 操作结果
     */
    List<Fmea> PselectV(Fmea dto);
    
	Fmea selectFmeaByLevelId(Long levelId);
}