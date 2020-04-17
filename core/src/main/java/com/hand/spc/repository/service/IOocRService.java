package com.hand.spc.repository.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.OocR;
import com.hand.spc.repository.dto.OocRequestDTO;
import com.hand.spc.repository.dto.OocResponseDTO;
import com.hand.spc.utils.Page;

public interface IOocRService extends IBaseService<OocR>, ProxySelf<IOocRService>{

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
