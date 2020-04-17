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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.job.service.ISampleCountCalculationSService;
import com.hand.spc.repository.dto.CountCalculationVO;
import com.hand.spc.repository.dto.CountSampleDataWaitVO;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.service.ICountSampleDataWaitRService;
import com.hand.spc.temppkg.dto.Temppkgdto;
import com.hand.spc.utils.Utils;


//计数型
@Service
@Transactional(rollbackFor = Exception.class)
public class SampleCountCalculationSServiceImpl extends BaseServiceImpl<Temppkgdto> implements ISampleCountCalculationSService {
    private Logger logger = LoggerFactory.getLogger(SampleCountCalculationSServiceImpl.class);
    @Autowired
    private InterfaceService interfaceService;// 发送接口
    @Autowired
    private ICountSampleDataWaitRService countSampleDataWaitRepository; //样本数据(计数)预处理
    
    private ExecutorService exec = new ThreadPoolExecutor(100, 1000, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    @Override
    public void sampleCountCalculation(Long tenantId, Long siteId) {
        String uuid = UUID.randomUUID().toString();// 获取UUID 追溯同一进程日志
        Long begin = System.currentTimeMillis();
        logger.info(Utils.getLog(uuid, "******** START 样本数据(计数)计算 teandId=" + tenantId + ",siteId=" + siteId + "********"));

        // 获取预处理样本数据 按 “租户”，“站点”：“附着对象组ID”，“控制要素组ID”，“控制要素ID”
        List<CountSampleDataWaitVO> countSampleDataWaitVOList =  countSampleDataWaitRepository.listCountSampleDataWait(tenantId, siteId);

        if (CollectionUtils.isNotEmpty(countSampleDataWaitVOList)) {
            logger.info(Utils.getLog(uuid, "****** 样本大小 =" + countSampleDataWaitVOList.size()));

            List<Future<Map<EntityR, CountCalculationVO>>> resultList = new ArrayList<Future<Map<EntityR, CountCalculationVO>>>();
            for (CountSampleDataWaitVO countSampleDataWaitVO : countSampleDataWaitVOList) {
                StringBuilder logMessage = new StringBuilder();
                logMessage.append("TenantId=").append(countSampleDataWaitVO.getTenantId()).append(",SiteId=")
                                .append(countSampleDataWaitVO.getSiteId()).append(",AttachmentGroupId=")
                                .append(countSampleDataWaitVO.getAttachmentGroupId()).append(",CeGroupId=")
                                .append(countSampleDataWaitVO.getCeGroupId()).append(",CeParameterId=")
                                .append(countSampleDataWaitVO.getCeParameterId());

                SampleCountCalculationThread sampleCountCalculationThread =  new SampleCountCalculationThread(uuid, logMessage, countSampleDataWaitVO);
                resultList.add(exec.submit(sampleCountCalculationThread));
            }

            Map<EntityR, CountCalculationVO> totalMap = new HashMap<EntityR, CountCalculationVO>();
            for (Future<Map<EntityR, CountCalculationVO>> future : resultList) {
                try {
                    Map<EntityR, CountCalculationVO> tmpMap = future.get();
                    if (MapUtils.isNotEmpty(tmpMap)) {
                        for (Map.Entry<EntityR, CountCalculationVO> entry : tmpMap.entrySet()) {
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

            for (Map.Entry<EntityR, CountCalculationVO> entry : totalMap.entrySet()) {
                MessageThread messageThread =  new MessageThread(uuid, entry.getKey(), entry.getValue(), interfaceService);
                exec.submit(messageThread);
            }
            logger.info("***************发送邮件***************");
        }
    }
}
