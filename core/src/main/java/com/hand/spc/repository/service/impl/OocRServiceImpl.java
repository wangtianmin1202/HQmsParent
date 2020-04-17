package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.OocR;
import com.hand.spc.repository.dto.OocRequestDTO;
import com.hand.spc.repository.dto.OocResponseDTO;
import com.hand.spc.repository.mapper.OocRMapper;
import com.hand.spc.repository.service.IOocRService;
import com.hand.spc.utils.Page;

@Service
@Transactional(rollbackFor = Exception.class)
public class OocRServiceImpl extends BaseServiceImpl<OocR> implements IOocRService {

    @Autowired
    private OocRMapper oocMapper;

    @Override
    public Page<OocResponseDTO> listOOC(OocRequestDTO requestDTO, PageRequest pageRequest) {
        return null;  // 20190813 PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(), () -> oocMapper.listOOC(requestDTO));
    }

    @Override
    public List<OocResponseDTO> listSeOoc(OocRequestDTO requestDTO) {
        return oocMapper.listOOC(requestDTO);
    }

    @Override
    public List<OocR> listOocBySampleSubgroupIdList(OocRequestDTO requestDTO) {
        return oocMapper.listOocBySampleSubgroupIdList(requestDTO);
    }

    @Override
    public int batchInsertRows(List<OocR> oocList) {
        return oocMapper.batchInsertRows(oocList);
    }
}
