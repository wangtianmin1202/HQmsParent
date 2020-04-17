package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CountOocR;
import com.hand.spc.repository.dto.CountOocRequestDTO;
import com.hand.spc.repository.dto.CountOocResponseDTO;
import com.hand.spc.repository.mapper.CountOocRMapper;
import com.hand.spc.repository.service.ICountOocRService;

/**
 * OOC(计数) 资源库实现
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:01:40
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CountOocRServiceImpl extends BaseServiceImpl<CountOocR> implements ICountOocRService {
    @Autowired
    private CountOocRMapper countOocMapper;

    @Override
    public List<CountOocR> listCountOocByStatisticList(CountOocRequestDTO countOocRequestDTO) {
        return countOocMapper.listCountOocByStatisticList(countOocRequestDTO);
    }

    @Override
    public int batchInsertRows(List<CountOocR> countOocList) {
        return countOocMapper.batchInsertRows(countOocList);
    }

    @Override
    public List<CountOocResponseDTO> listCountOOC(CountOocRequestDTO countOocRequestDTO) {
        return countOocMapper.listCountOOC(countOocRequestDTO);
    }

    @Override
    public List<CountOocResponseDTO> CountOOCById(CountOocRequestDTO countOocRequestDTO) {
        return countOocMapper.CountOOCById(countOocRequestDTO);
    }
}
