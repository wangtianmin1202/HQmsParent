package com.hand.spc.repository.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.CountStatisticR;
import com.hand.spc.repository.dto.CountStatisticVO;
import com.hand.spc.repository.dto.PlatoCountVO;
import com.hand.spc.repository.dto.SeCountPointDataDTO;
import com.hand.spc.repository.dto.SeRequestDTO;
import com.hand.spc.repository.mapper.CountStatisticRMapper;
import com.hand.spc.repository.service.ICountStatisticRService;

/**
 * 计数型统计量表 资源库实现
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:01:57
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CountStatisticRServiceImpl extends BaseServiceImpl<CountStatisticR> implements ICountStatisticRService {

    @Autowired
    private CountStatisticRMapper countStatisticMapper;

    @Override
    public List<CountStatisticR> listCountStatistic(Long tenantId, Long siteId, String entityCode, String entityVersion, Long maxSubgroupNum, Long minSubgroupNum, String chartType) {
        return countStatisticMapper.listCountStatistic(tenantId, siteId, entityCode, entityVersion, maxSubgroupNum, minSubgroupNum, chartType);
    }

    @Override
    public int batchInsertRows(List<CountStatisticR> countStatisticList) {
        return countStatisticMapper.batchInsertRows(countStatisticList);
    }

    @Override
    public Long queryMaxSubgroupNum(SeRequestDTO requestDTO) {
        return countStatisticMapper.queryMaxSubgroupNum(requestDTO);
    }

    @Override
    public List<PlatoCountVO> listPlatoCount(CountStatisticVO countStatisticVO) {
        return countStatisticMapper.listPlatoCount(countStatisticVO);
    }

    @Override
    public List<SeCountPointDataDTO> listSECount(CountStatisticVO countStatisticVO) {
        return countStatisticMapper.listSECount(countStatisticVO);
    }

	@Override
	public void batchUpdateByPrimaryKeySelective(List<CountStatisticR> countStatisticListUpd) {
		
		self().batchUpdate(null, countStatisticListUpd);
		/*Iterator<CountStatistic> var4=countStatisticListUpd.iterator();		
			while(var4.hasNext()) {
	            countStatisticMapper.updateByPrimaryKeySelective(var4.next());
	        }*/
		
	}
	
	/*@SuppressWarnings("unused")
	private int batchDml(List<T> list, Function<T,Integer> dmlCommend) {
		if(CollectionUtils.isEmpty(list)) {
			return 0;
		}else {
			int dealCount =0;
			
			Object obj;
			for(Iterator<T> var1=list.iterator();var1.hasNext();dealCount+=(Integer)dmlCommend.apply((T) obj)) {
				obj=var1.hasNext();
			}
			return dealCount;
		}
	}*/
}
