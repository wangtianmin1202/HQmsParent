package com.hand.spc.job.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.job.service.ISampleCalculationSService;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.SampleGroupDataVO;
import com.hand.spc.repository.dto.SubGroupCalcResultVO;
import com.hand.spc.repository.service.ISampleDataWaitRService;
import com.hand.spc.temppkg.dto.Temppkgdto;
import com.hand.spc.utils.Utils;

//计量型

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleCalculationSServiceImpl extends BaseServiceImpl<Temppkgdto> implements ISampleCalculationSService {

	private Logger logger = LoggerFactory.getLogger(SampleCalculationSServiceImpl.class);

	@Autowired
	private ISampleDataWaitRService sampleDataWaitRepository; // 样本数据预处理
	
	@Autowired
    private InterfaceService interfaceService;// 发送接口

	private ExecutorService cachedService = new ThreadPoolExecutor(100, 1000, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

	@Override
	public void sampleDataSubgroupCalculation(Long tenantId, Long siteId) {
		String uuid = UUID.randomUUID().toString();// 获取UUID 追溯同一进程日志
		Long begin = System.currentTimeMillis();

		logger.info(
				Utils.getLog(uuid, "******** START 样本数据分组计算 teandId=" + tenantId + ",siteId=" + siteId + "********"));

		// 获取预处理样本数据 按 “租户”，“站点”，“附着对象组ID”，“控制要素组ID”，“控制要素ID” 分组
		List<SampleGroupDataVO> sampleGroupDataVOList = sampleDataWaitRepository.listSampleGroupData(tenantId, siteId);

		if (CollectionUtils.isNotEmpty(sampleGroupDataVOList)) {
            logger.info(Utils.getLog(uuid, "****** 分组大小 =" + sampleGroupDataVOList.size()));

            List<Future<Map<EntityR, SubGroupCalcResultVO>>> resultList = new ArrayList<Future<Map<EntityR, SubGroupCalcResultVO>>>();
            for (SampleGroupDataVO sampleGroupDataVO : sampleGroupDataVOList) {
                StringBuilder logMessage = new StringBuilder();// log日志
                logMessage.append("TenantId=").append(sampleGroupDataVO.getTenantId()).append(",SiteId=")
                                .append(sampleGroupDataVO.getSiteId()).append(",AttachmentGroupId=")
                                .append(sampleGroupDataVO.getAttachmentGroupId()).append(",CeGroupId=")
                                .append(sampleGroupDataVO.getCeGroupId()).append(",CeParameterId=")
                                .append(sampleGroupDataVO.getCeParameterId());

                SampleDataSubgroupThread sampleCalculationThread =  new SampleDataSubgroupThread(uuid, logMessage, sampleGroupDataVO);
                resultList.add(cachedService.submit(sampleCalculationThread));
            }

            Map<EntityR, SubGroupCalcResultVO> totalMap = new HashMap<EntityR, SubGroupCalcResultVO>();
            for (Future<Map<EntityR, SubGroupCalcResultVO>> future : resultList) {
                try {
                    Map<EntityR, SubGroupCalcResultVO> tmpMap = future.get();
                    if (MapUtils.isNotEmpty(tmpMap)) {
                        for (Map.Entry<EntityR, SubGroupCalcResultVO> entry : tmpMap.entrySet()) {
                            if (CollectionUtils.isNotEmpty(entry.getValue().getMessageList())) {
                                totalMap.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.info(Utils.getLog(uuid, "获取计算结果信息失败：" + e.getMessage()));
                }
            }
            logger.info("***************计算总计用时:" + (System.currentTimeMillis() - begin) + "ms");

            for (Map.Entry<EntityR, SubGroupCalcResultVO> entry : totalMap.entrySet()) {
                MessageThread messageThread = new MessageThread(uuid, entry.getKey(), entry.getValue(), interfaceService);
                cachedService.submit(messageThread);
            }
            logger.info("***************发送邮件***************");
        }

	}
}
