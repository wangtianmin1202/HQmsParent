package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.CountOocR;
import com.hand.spc.repository.dto.CountOocRequestDTO;
import com.hand.spc.repository.dto.CountOocResponseDTO;

/**
 * OOC(计数)资源库
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:01:40
 */
public interface ICountOocRService extends IBaseService<CountOocR>, ProxySelf<ICountOocRService> {
    /**
     * 根据子组ID集合获取COUNT OOC集合
     *
     * @param countOocRequestDTO
     * @return
     */
    public List<CountOocR> listCountOocByStatisticList(CountOocRequestDTO countOocRequestDTO);

    /**
     * 批量保存COUNT OOC
     *
     * @param countOocList
     * @return
     */
    public int batchInsertRows(List<CountOocR> countOocList);

    /**
     * Count OOC查询
     *
     * @param countOocRequestDTO
     * @return
     */
    public List<CountOocResponseDTO> listCountOOC(CountOocRequestDTO countOocRequestDTO);

    /**
     * 通过样本数据id获取ooc和判异规则
     * @param countOocRequestDTO
     * @return
     */
    public List<CountOocResponseDTO> CountOOCById(CountOocRequestDTO countOocRequestDTO);
}
