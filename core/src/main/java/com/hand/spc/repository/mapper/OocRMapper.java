package com.hand.spc.repository.mapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.OocR;
import com.hand.spc.repository.dto.OocRequestDTO;
import com.hand.spc.repository.dto.OocResponseDTO;

public interface OocRMapper extends Mapper<OocR> {

    /**
     * OOC查询
     *
     * @param requestDTO
     * @return
     */
    public List<OocResponseDTO> listOOC(OocRequestDTO requestDTO);
    
    /**
     * OOC查询
     *
     * @param requestDTO
     * @return
     */
    public Page<OocResponseDTO> listOOC(OocRequestDTO requestDTO, PageRequest pageRequest);
    
    /**
     * SE查询 根据分组ID集合查询OOC
     *
     * @param requestDTO
     * @return
     */
    public List<OocResponseDTO> listSeOoc(OocRequestDTO requestDTO);

    /**
     * 根据子组ID集合获取OOC集合
     *
     * @param requestDTO
     * @return
     */
    public List<OocR> listOocBySampleSubgroupIdList(OocRequestDTO requestDTO);

    /**
     * 批量保存OOC
     *
     * @param oocList
     * @return
     */
    public int batchInsertRows(List<OocR> oocList);
    
}
