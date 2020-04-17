package com.hand.spc.repository.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CountOocR;
import com.hand.spc.repository.dto.CountOocRequestDTO;
import com.hand.spc.repository.dto.CountOocResponseDTO;
import com.hand.spc.repository.dto.OocR;
import com.hand.spc.repository.dto.OocRequestDTO;
import com.hand.spc.repository.dto.OocResponseDTO;
import com.hand.spc.repository.dto.OocUpdateDto;

public interface IOocService extends IBaseService<OocR>,ProxySelf<IOocService> {

    /**
     * OOC查询
     *
     * @param requestDTO
     * @return
     */
    public Page<OocResponseDTO> listOOC(OocRequestDTO requestDTO, PageRequest pageRequest);

    /**
     * OOC修改
     *
     * @param oocUpdateDto
     * @return
     */
    public OocR updateOOC(OocUpdateDto oocUpdateDto);

    /**
     * 计数型OOC查询
     * @param countOocRequestDTO
     * @return
     */
    List<CountOocResponseDTO> listCountOocByStatisticList(CountOocRequestDTO countOocRequestDTO);

    /**
     * 计数型OOC修改
     * @param countOoc
     * @return
     */
    CountOocR CountupdateOoc(CountOocR countOoc);
}
