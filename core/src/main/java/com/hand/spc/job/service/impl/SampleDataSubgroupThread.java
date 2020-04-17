package com.hand.spc.job.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.hand.spc.job.service.ISampleSubgroupSService;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.SampleDataWaitR;
import com.hand.spc.repository.dto.SampleGroupDataVO;
import com.hand.spc.repository.dto.SubGroupCalcResultVO;
import com.hand.spc.repository.service.IEntityRService;
import com.hand.spc.repository.service.ISampleDataWaitRService;
import com.hand.spc.utils.RedisSingle;
import com.hand.spc.utils.Utils;
import com.hand.spc.utils.components.SpringContextUtil;
import com.hand.spc.utils.components.WebSocketHandler;

//计量型
/**
 * 样本数据分组
 */
public class SampleDataSubgroupThread  implements Callable<Map<EntityR, SubGroupCalcResultVO>> {

	private Logger logger = LoggerFactory.getLogger(SampleCalculationSServiceImpl.class);
	private String uuid;
	private StringBuilder logMessage;
	private SampleGroupDataVO sampleGroupDataVO;
	private ISampleDataWaitRService sampleDataWaitRepository; // 样本数据预处理
	private RedisSingle redisHelper;
	private WebSocketHandler webSocketHandler;
	private ISampleSubgroupSService sampleSubgroupService;
	private IEntityRService entityRepository;

	public SampleDataSubgroupThread(String uuid, StringBuilder logMessage, SampleGroupDataVO sampleGroupDataVO) {
        this.uuid = uuid;
        this.logMessage = logMessage;
        this.sampleGroupDataVO = sampleGroupDataVO;
    }

	@Override
    public Map<EntityR, SubGroupCalcResultVO> call() throws Exception {
        this.uuid = this.uuid + ":" + Thread.currentThread().getId();
        Long start = System.currentTimeMillis();

        getSpringBean();
        String redisKey = "pspc:sample_data_calculate_flag:" + sampleGroupDataVO.getTenantId() + "-"
                        + sampleGroupDataVO.getSiteId() + "-" + sampleGroupDataVO.getAttachmentGroupId() + "-"
                        + sampleGroupDataVO.getCeGroupId() + "-" + sampleGroupDataVO.getCeParameterId();

        Map<EntityR, SubGroupCalcResultVO> result = new HashMap<EntityR, SubGroupCalcResultVO>();
        try {
            result = getSampleDataWaits();
            logger.info(Utils.getLog(uuid, "***************分组计算耗时:" + (System.currentTimeMillis() - start) + "ms"));
        } catch (Exception ex) {
            logger.error("getSampleDataWaits Error. {}", ex.getMessage());
        } finally {
            redisHelper.strSet(redisKey, "COMPLETED");
        }
        return result;
    }

	private Map<EntityR, SubGroupCalcResultVO> getSampleDataWaits() {
        List<SampleDataWaitR> sampleDataWaitCurrentList = new ArrayList<>();// 计算中的集合
        List<SampleDataWaitR> sampleDataWaitDelList = new ArrayList<>();
        List<SampleDataWaitR> sampleDataWaitInsList = new ArrayList<>();
        
        //List<SampleDataWaitR> sampleDataWaitList = sampleDataWaitRepository.listExtractSampleDataWait(sampleGroupDataVO); //modified 20191101
        String whereInSql = this.GetWhereInValuesSql("psdw.sample_data_wait_id",sampleGroupDataVO.getSampleDataWaitIdList(),800);
        List<SampleDataWaitR> sampleDataWaitList = sampleDataWaitRepository.listExtractSampleDataWaitModified(whereInSql);
        
        

        // 数据过滤
        if (CollectionUtils.isNotEmpty(sampleGroupDataVO.getDataAccessConfigurationList())) {
            logger.info(Utils.getLog(uuid, "**** 当前分组数据进行筛选  ****"));

            sampleGroupDataVO.setSampleDataWaitIdList(null);// 清空集合
            sampleGroupDataVO.setSampleDataWaitIdDelList(null);

            if (CollectionUtils.isNotEmpty(sampleDataWaitList)) {
                /**
                 * 数据过滤上下限(有可能为空)
                 */
                filterByLimit(sampleDataWaitList, sampleDataWaitCurrentList, sampleDataWaitDelList);

                /**
                 * 筛选数据
                 */
                if (sampleDataWaitList != null && sampleDataWaitList.size() > 0) {
                    filterByEffectiveType(sampleDataWaitList, sampleDataWaitCurrentList, sampleDataWaitDelList,
                                    sampleDataWaitInsList);
                }
            }

            // 计算集合
            if (CollectionUtils.isNotEmpty(sampleDataWaitInsList)) {
                sampleGroupDataVO.setSampleDataWaitIdList(sampleDataWaitInsList.stream()
                                .map(countSampleDataWait -> countSampleDataWait.getSampleDataWaitId())
                                .collect(Collectors.toList()));
                sampleGroupDataVO.setSampleDataWaitInsList(sampleDataWaitInsList);
            }

            // 删除集合
            if (CollectionUtils.isNotEmpty(sampleDataWaitDelList)) {
                sampleGroupDataVO.setSampleDataWaitIdDelList(sampleDataWaitDelList.stream()
                                .map(countSampleDataWait -> countSampleDataWait.getSampleDataWaitId())
                                .collect(Collectors.toList()));
            }

            int insCount = CollectionUtils.isEmpty(sampleGroupDataVO.getSampleDataWaitInsList()) ? 0
                            : sampleGroupDataVO.getSampleDataWaitInsList().size();
            int delCount = CollectionUtils.isEmpty(sampleGroupDataVO.getSampleDataWaitIdDelList()) ? 0
                            : sampleGroupDataVO.getSampleDataWaitIdDelList().size();
            logger.info(Utils.getLog(uuid, "**** 当前分组需要进行计算的数据有" + insCount + "个"));
            logger.info(Utils.getLog(uuid, "**** 当前分组需要删除的数据有" + delCount + "个"));
        } else {
            logger.info(Utils.getLog(uuid, "**** 当前分组数据不进行筛选 ****"));

            sampleGroupDataVO.setSampleDataWaitInsList(sampleDataWaitList);
            sampleGroupDataVO.setSampleDataWaitIdDelList(sampleGroupDataVO.getSampleDataWaitIdList());

            int insCount = CollectionUtils.isEmpty(sampleGroupDataVO.getSampleDataWaitInsList()) ? 0
                            : sampleGroupDataVO.getSampleDataWaitInsList().size();
            int delCount = CollectionUtils.isEmpty(sampleGroupDataVO.getSampleDataWaitIdDelList()) ? 0
                            : sampleGroupDataVO.getSampleDataWaitIdDelList().size();

            logger.info(Utils.getLog(uuid, "**** 当前分组需要进行计算的数据有" + insCount + "个"));
            logger.info(Utils.getLog(uuid, "**** 当前分组需要删除的数据有" + delCount + "个"));
        }

        if (CollectionUtils.isNotEmpty(sampleGroupDataVO.getSampleDataWaitInsList())
                        || CollectionUtils.isEmpty(sampleGroupDataVO.getSampleDataWaitIdDelList())) {
            String redisKey = "pspc:sample_data_calculate_flag:" + sampleGroupDataVO.getTenantId() + "-"
                            + sampleGroupDataVO.getSiteId() + "-" + sampleGroupDataVO.getAttachmentGroupId() + "-"
                            + sampleGroupDataVO.getCeGroupId() + "-" + sampleGroupDataVO.getCeParameterId();
            String redisKeyStatus = redisHelper.strGet(redisKey);

            logger.info(Utils.getLog(uuid, "**** RUNNING redisKeyStatus is " + redisKeyStatus));

            if (StringUtils.isEmpty(redisKeyStatus) || redisKeyStatus.equals("COMPLETED")) {
                logger.info(Utils.getLog(uuid, "**** RUNNING redisKey is " + redisKey + " RUNNING"));
                redisHelper.strSet(redisKey, "RUNNING");
                return buildSampleSubgroupWait();
            } else {
                logger.info(Utils.getLog(uuid, "**** RUNNING 分组计算运行中" + logMessage));
                return Collections.emptyMap();
            }
        }

        return Collections.emptyMap();
    }

	private void getSpringBean() {
		if (null == redisHelper) {
			redisHelper = RedisSingle.getInstance();
		}
		if (null == webSocketHandler) {
			webSocketHandler = (WebSocketHandler) SpringContextUtil.getSpringBean("WebSocketHandlerBean");
		}
		if (null == entityRepository) {
			entityRepository = (IEntityRService) SpringContextUtil.getSpringBean("EntityRServiceImplBean");
		}
		if (null == sampleDataWaitRepository) {
			sampleDataWaitRepository = (ISampleDataWaitRService) SpringContextUtil.getSpringBean("SampleDataWaitRServiceImplBean");
		}
		if (null == sampleSubgroupService) {
			sampleSubgroupService = (ISampleSubgroupSService) SpringContextUtil.getSpringBean("SampleSubgroupSServiceImplBean");
		}

	}

	private void filterByEffectiveType(List<SampleDataWaitR> sampleDataWaitList,
			List<SampleDataWaitR> sampleDataWaitCurrentList, List<SampleDataWaitR> sampleDataWaitDelList,
			List<SampleDataWaitR> sampleDataWaitInsList) {
		String effectiveType = sampleGroupDataVO.getDataAccessConfigurationList().get(0).getEffectiveType();// 有效类型
		String samplingPosition = sampleGroupDataVO.getDataAccessConfigurationList().get(0).getSamplingPosition();// 抽样取值位置
		// (1)等距抽样
		if ("ISOMETRIC".equals(effectiveType)) {
			filterByIsometric(sampleDataWaitList, sampleDataWaitDelList, sampleDataWaitInsList, samplingPosition);
		}
		// (2)时间抽样
		else if ("TIME".equals(effectiveType)) {
			filterByTime(sampleDataWaitList, sampleDataWaitCurrentList, sampleDataWaitDelList, sampleDataWaitInsList,
					samplingPosition);
		} else {
			sampleDataWaitInsList.addAll(sampleDataWaitList);
			sampleDataWaitDelList.addAll(sampleDataWaitList);
		}
	}

	private void filterByTime(List<SampleDataWaitR> sampleDataWaitList, List<SampleDataWaitR> sampleDataWaitCurrentList,
			List<SampleDataWaitR> sampleDataWaitDelList, List<SampleDataWaitR> sampleDataWaitInsList,
			String samplingPosition) {
		int timeSampling = sampleGroupDataVO.getDataAccessConfigurationList().get(0).getTimeSampling().intValue(); // 时间抽样(S秒)

		int inxCount = sampleDataWaitList.size() - 1;// 集合最大下标
		if (Utils.differentMillisecond(sampleDataWaitList.get(0).getSampleTime(),
				sampleDataWaitList.get(inxCount).getSampleTime()) >= timeSampling) {

			Date dateStart = null;// 计算开始(时间开始)
			Date dateEnd = null;// 计算开始(时间结束)
			sampleDataWaitCurrentList.clear();// 清除
			for (int i = 1; i <= inxCount; i++) {
				// 开始时间(默认为第一笔，其后是上次结束时间)
				if (dateStart == null) {
					dateStart = sampleDataWaitList.get(0).getSampleTime();
					sampleDataWaitCurrentList.add(sampleDataWaitList.get(0));// 累计
				}
				dateEnd = sampleDataWaitList.get(i).getSampleTime();
				sampleDataWaitCurrentList.add(sampleDataWaitList.get(i));// 累计

				// 分组校验
				if (Utils.differentMillisecond(dateStart, dateEnd) >= timeSampling - 1) {
					// 前部
					if ("FRONT".equals(samplingPosition)) {
						sampleDataWaitInsList.add(sampleDataWaitCurrentList.get(0));
					}
					// 中部
					else if ("MIDDLE".equals(samplingPosition)) {
						if ((sampleDataWaitCurrentList.size() % 2) != 0) { // 奇数
							sampleDataWaitInsList.add(sampleDataWaitCurrentList.get(sampleDataWaitCurrentList.size() / 2));
						} else {
							// 偶数取偏上一笔
							sampleDataWaitInsList.add(sampleDataWaitCurrentList.get((sampleDataWaitCurrentList.size() / 2) - 1));
						}
					}
					// 后部
					else if ("BEHIND".equals(samplingPosition)) {
						sampleDataWaitInsList.add(sampleDataWaitCurrentList.get(sampleDataWaitCurrentList.size() - 1));
					}

					sampleDataWaitDelList.addAll(sampleDataWaitCurrentList);

					dateStart = dateEnd;// 重新开始
					sampleDataWaitCurrentList.clear();
				}
			}
		}
	}

	private void filterByIsometric(List<SampleDataWaitR> sampleDataWaitList, List<SampleDataWaitR> sampleDataWaitDelList,
			List<SampleDataWaitR> sampleDataWaitInsList, String samplingPosition) {
		int isometricSampling = sampleGroupDataVO.getDataAccessConfigurationList().get(0).getIsometricSampling()
				.intValue(); // 等距抽样

		int groupCount = sampleDataWaitList.size() / isometricSampling;// 分组数量(取整)
		int indexStart = 0;// 计算开始(集合开始下标)
		int indexEnd = 0;// 计算结束(集合结束下标)
		// int indxMiddle = groupCount / 2;//计算(集合中间下标(取整)：注意奇偶数)

		for (int i = 0; i < groupCount; i++) {
			indexStart = i * isometricSampling;// 计算开始(集合开始下标)
			indexEnd = indexStart + isometricSampling - 1;// 计算结束(集合结束下标)

			// 前部
			if ("FRONT".equals(samplingPosition)) {
				sampleDataWaitInsList.add(sampleDataWaitList.get(indexStart));
			}
			// 中部
			else if ("MIDDLE".equals(samplingPosition)) {
				if ((isometricSampling % 2) != 0) { // 奇数
					sampleDataWaitInsList.add(sampleDataWaitList.get(indexStart + isometricSampling / 2));
				} else {
					// 偶数取偏上一笔
					sampleDataWaitInsList.add(sampleDataWaitList.get(indexStart + isometricSampling / 2 - 1));
				}
			}
			// 后部
			else if ("BEHIND".equals(samplingPosition)) {
				sampleDataWaitInsList.add(sampleDataWaitList.get(indexEnd));
			}

			// 添加待删除集合
			for (int k = indexStart; k <= indexEnd; k++) {
				sampleDataWaitDelList.add(sampleDataWaitList.get(k));
			}
		}
	}

	private void filterByLimit(List<SampleDataWaitR> sampleDataWaitList, List<SampleDataWaitR> sampleDataWaitCurrentList,
			List<SampleDataWaitR> sampleDataWaitDelList) {
		if (sampleGroupDataVO.getDataAccessConfigurationList().get(0).getDataUpperLimit() != null) {
			// 待删除(异常：样本值 '大于' 数据过滤上限)
			sampleDataWaitCurrentList = sampleDataWaitList.stream()
					.filter(lovValueDTO -> lovValueDTO.getSampleValue().doubleValue() > sampleGroupDataVO
							.getDataAccessConfigurationList().get(0).getDataUpperLimit().doubleValue())
					.collect(Collectors.toList());

			sampleDataWaitDelList.addAll(sampleDataWaitCurrentList);// 移到待删除集合
			sampleDataWaitList.removeAll(sampleDataWaitCurrentList);// 去掉异常集合
		}

		// 数据过滤下限(有可能为空)
		if (sampleGroupDataVO.getDataAccessConfigurationList().get(0).getDataLowerLimit() != null) {
			// 待删除(异常：样本值 '小于' 数据过滤下限)
			sampleDataWaitCurrentList = sampleDataWaitList.stream()
					.filter(lovValueDTO -> lovValueDTO.getSampleValue().doubleValue() < sampleGroupDataVO
							.getDataAccessConfigurationList().get(0).getDataLowerLimit().doubleValue())
					.collect(Collectors.toList());

			sampleDataWaitDelList.addAll(sampleDataWaitCurrentList);// 移到待删除集合
			sampleDataWaitList.removeAll(sampleDataWaitCurrentList);// 去掉异常集合
		}
	}

	public Map<EntityR, SubGroupCalcResultVO> buildSampleSubgroupWait() {
		long begin = System.currentTimeMillis();

		sampleSubgroupService.sampleDataSubgroupWait(sampleGroupDataVO, uuid);

		logger.info(Utils.getLog(uuid, "***************样本数据预分组用时=" + (System.currentTimeMillis() - begin) + "ms"));

		if (CollectionUtils.isNotEmpty(sampleGroupDataVO.getSampleDataWaitIdList())) {
			begin = System.currentTimeMillis();
			SampleGroupDataVO sampleGroupDataVOForQuery = new SampleGroupDataVO();
			sampleGroupDataVOForQuery.setTenantId(sampleGroupDataVO.getTenantId());
			sampleGroupDataVOForQuery.setSiteId(sampleGroupDataVO.getSiteId());
			sampleGroupDataVOForQuery.setAttachmentGroupId(sampleGroupDataVO.getAttachmentGroupId());
			sampleGroupDataVOForQuery.setCeGroupId(sampleGroupDataVO.getCeGroupId());
			sampleGroupDataVOForQuery.setCeParameterId(sampleGroupDataVO.getCeParameterId());
			List<EntityR> entityList = entityRepository.listSubgroupWaitData(sampleGroupDataVOForQuery);
			String entityCodes = entityList.stream().map(EntityR::getEntityCode).collect(Collectors.joining(","));// 问题记录：数据的写入或取出有问题   entity里面sampleSubgroupWaitList数据顺序与excel导入顺序不一致  20191030 
			logger.info(Utils.getLog(uuid, "获取实体控制图，实体控制图数量= " + entityList.size() + ",实体控制编码=" + entityCodes));

			Map<EntityR, SubGroupCalcResultVO> result = sampleSubgroupService.sampleDataSubgroupList(entityList, uuid);

			logger.info(Utils.getLog(uuid, "***************样本数据分组计算耗时=" + (System.currentTimeMillis() - begin) + "ms"));

			// 释放内存
			sampleGroupDataVO = null;

			if (CollectionUtils.isNotEmpty(entityList) && MapUtils.isNotEmpty(result)) {
				begin = System.currentTimeMillis();
				/**
				 * 拼接所有的计算完成实体控制图的entitycode，推送拼接后的实体控制图code给前端
				 */
				Map<EntityR, Integer> filterMap = new HashMap<EntityR, Integer>(result.size());
				for (Map.Entry<EntityR, SubGroupCalcResultVO> entry : result.entrySet()) {
					if (entry.getValue().getSubgroupStatisticList().size() > 0) {
						filterMap.put(entry.getKey(), entry.getValue().getSubgroupStatisticList().size());
					}
				}

				if (MapUtils.isNotEmpty(filterMap)) {
					try {
						webSocketHandler.sendMessage(uuid, filterMap);
						logger.info(Utils.getLog(uuid,
								"***************发送websocket耗时=" + (System.currentTimeMillis() - begin) + "ms"));
					} catch (Exception ex) {
						logger.error(Utils.getLog(uuid, "websocket send message error:" + ex.getMessage()));
					}
				}
			}
			return result;
		}
		return Collections.emptyMap();
	}
	
	/**
     * 获取where in语句
     *
     * @param column      字段名
     * @param values      值集合
     * @param num         数量
     * @return where in语句
     */
	
   public String GetWhereInValuesSql(String column, List<Long> values,int num) {
        // sql语句
        String sql = "(";
        // 值的个数
        int valueSize = values.size();
        // 批次数
        int batchSize = valueSize / num + (valueSize % num == 0 ? 0 : 1);
        for (int i = 0; i < batchSize; i++) {
            if (i > 0) {
                sql += ") or ";
            }
            sql += column+" in (";
            for (int j = i * num; ( j < (i + 1) * num) && j < valueSize; j++) {
                if (j > i * num) {
                    sql += ",";
                }
                sql += "'" + values.get(j) + "'";
            }
        }
        sql += "))";
        return sql;
    };
	
}
