package com.hand.spc.job.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.hand.spc.job.service.ISampleCountCalculationRunSService;
import com.hand.spc.repository.dto.CountCalculationVO;
import com.hand.spc.repository.dto.CountSampleDataWaitR;
import com.hand.spc.repository.dto.CountSampleDataWaitVO;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.service.ICountSampleDataWaitRService;
import com.hand.spc.repository.service.IEntityRService;
import com.hand.spc.utils.RedisSingle;
import com.hand.spc.utils.Utils;
import com.hand.spc.utils.components.SpringContextUtil;
import com.hand.spc.utils.components.WebSocketHandler;

//计数型
/**
 * 本数据(计数)分组
 */
public class SampleCountCalculationThread implements Callable<Map<EntityR, CountCalculationVO>> {
    private Logger logger = LoggerFactory.getLogger(SampleCountCalculationSServiceImpl.class);

    private CountSampleDataWaitVO countSampleDataWaitVO; //样本数据预处理(计数)
    private String uuid;
    private StringBuilder logMessage;
    private RedisSingle redisHelper;
    private ISampleCountCalculationRunSService sampleCountCalculationRunService;
    private IEntityRService entityRepository;//实体控制图
    private WebSocketHandler webSocketHandler;	
    private ICountSampleDataWaitRService countSampleDataWaitRepository; //样本数据(计数)预处理
    
	
    public SampleCountCalculationThread(String uuid, StringBuilder logMessage, CountSampleDataWaitVO countSampleDataWaitVO) {
    	this.uuid = uuid;
    	this.logMessage = logMessage;
    	this.countSampleDataWaitVO = countSampleDataWaitVO;
    }

    @Override
    public Map<EntityR, CountCalculationVO> call() throws Exception {
        this.uuid = uuid + ":" + Thread.currentThread().getId();
        Long start = System.currentTimeMillis();
        getSpringBean();

        Map<EntityR, CountCalculationVO> result = new HashMap<>();
        String redisKey = "pspc:sample_data_calculate_flag:" + logMessage;
        try {
            result = getSampleDataWaits();
            logger.info(Utils.getLog(uuid, "***************分组计算耗时:" + (System.currentTimeMillis() - start) + "ms"));
        } catch (Exception ex) {
            logger.error("getSampleDataWaits Error: {}", ex.getMessage());
        } finally {
            redisHelper.strSet(redisKey, "COMPLETED");
        }
        return result;
    }
	
    private Map<EntityR, CountCalculationVO> buildSampleSubgroupWait() {
        Map<EntityR, CountCalculationVO> result = processData();

        if (MapUtils.isNotEmpty(result)) {
            long begin = System.currentTimeMillis();

            Map<EntityR, Integer> filterMap = new HashMap<>(result.size());
            for (Map.Entry<EntityR, CountCalculationVO> entry : result.entrySet()) {
                if (entry.getValue().getCountStatisticList().size() > 0) {
                    filterMap.put(entry.getKey(), entry.getValue().getCountStatisticList().size());
                }
            }

            if (MapUtils.isNotEmpty(filterMap)) {
                try {
                    //webSocketHandler.sendMessage(uuid, filterMap);
                    logger.info(Utils.getLog(uuid,
                                    "***************发送websocket耗时=" + (System.currentTimeMillis() - begin) + "ms"));
                } catch (Exception ex) {
                    logger.error(Utils.getLog(uuid, "websocket send message error:" + ex.getMessage()));
                }
            }
        }
        return result;
    }
    
    private void getSpringBean() {
		if (null == redisHelper) {
			redisHelper = RedisSingle.getInstance();//modified by jy 20191012
		}
		if (null == webSocketHandler) {
			webSocketHandler = (WebSocketHandler) SpringContextUtil.getSpringBean("WebSocketHandlerBean");
		}
		if (null == entityRepository) {
			entityRepository = (IEntityRService) SpringContextUtil.getSpringBean("EntityRServiceImplBean");
		}
		if (null == countSampleDataWaitRepository) {
			countSampleDataWaitRepository = (ICountSampleDataWaitRService) SpringContextUtil.getSpringBean("CountSampleDataWaitRServiceImplBean");
		}
		if (null == sampleCountCalculationRunService) {
			sampleCountCalculationRunService = (ISampleCountCalculationRunSService) SpringContextUtil.getSpringBean("SampleCountCalculationRunSServiceImplBean");
		}
	}
	
    private Map<EntityR, CountCalculationVO> getSampleDataWaits() {
        List<CountSampleDataWaitR> countSampleDataWaitCurrentList = new ArrayList<>();
        List<CountSampleDataWaitR> countSampleDataWaitDelList = new ArrayList<>();
        List<CountSampleDataWaitR> countSampleDataWaitInsList = new ArrayList<>();
        Map<EntityR, CountCalculationVO> result = new HashMap<>();

        if (countSampleDataWaitVO != null) {
        	//List<CountSampleDataWaitR> countSampleDataWaitList = countSampleDataWaitRepository.listExtractCountSampleDataWait(countSampleDataWaitVO);  modified 20191025
        	String whereInSql = this.GetWhereInValuesSql("pcsdw.count_sample_data_wait_id",countSampleDataWaitVO.getCountSampleDataWaitIdList(),800);
        	List<CountSampleDataWaitR> countSampleDataWaitList = countSampleDataWaitRepository.listExtractCountSampleDataWaitModified(whereInSql);

            if (CollectionUtils.isNotEmpty(countSampleDataWaitList)) {
                if (CollectionUtils.isNotEmpty(countSampleDataWaitVO.getDataAccessConfigurationList())) {
                    logger.info(Utils.getLog(uuid, "**** 当前数据进行筛选  ****"));

                    countSampleDataWaitVO.setCountSampleDataWaitIdList(null);// 清空集合
                    countSampleDataWaitVO.setCountSampleDataWaitIdDelList(null);

                    filterByLimit(countSampleDataWaitCurrentList, countSampleDataWaitDelList, countSampleDataWaitList);

                    if (CollectionUtils.isNotEmpty(countSampleDataWaitList)) {
                        filterByEffectiveType(countSampleDataWaitCurrentList, countSampleDataWaitDelList,
                                        countSampleDataWaitInsList, countSampleDataWaitList);
                    }


                    if (CollectionUtils.isNotEmpty(countSampleDataWaitInsList)) {
                        countSampleDataWaitVO.setCountSampleDataWaitIdList(countSampleDataWaitInsList.stream()
                                        .map(countSampleDataWait -> countSampleDataWait.getCountSampleDataWaitId())
                                        .collect(Collectors.toList()));
                    }

                    if (CollectionUtils.isNotEmpty(countSampleDataWaitDelList)) {
                        countSampleDataWaitVO.setCountSampleDataWaitIdDelList(countSampleDataWaitDelList.stream()
                                        .map(countSampleDataWait -> countSampleDataWait.getCountSampleDataWaitId())
                                        .collect(Collectors.toList()));
                    }

                    int insCount = CollectionUtils.isEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdList()) ? 0
                                    : countSampleDataWaitVO.getCountSampleDataWaitIdList().size();
                    int delCount = CollectionUtils.isEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdDelList()) ? 0
                                    : countSampleDataWaitVO.getCountSampleDataWaitIdDelList().size();

                    logger.info(Utils.getLog(uuid, "当前需要进行计算的数据有:" + insCount + "个"));
                    logger.info(Utils.getLog(uuid, "当前需要删除的数据有:" + delCount + "个"));
                } else {
                    logger.info(Utils.getLog(uuid, "当前数据不进行筛选"));

                    countSampleDataWaitVO.setCountSampleDataWaitIdList(countSampleDataWaitList.stream()
                                    .map(CountSampleDataWaitR::getCountSampleDataWaitId).collect(Collectors.toList()));
                    countSampleDataWaitVO.setCountSampleDataWaitIdDelList(
                                    countSampleDataWaitVO.getCountSampleDataWaitIdList());

                    int insCount = CollectionUtils.isEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdList()) ? 0
                                    : countSampleDataWaitVO.getCountSampleDataWaitIdList().size();
                    int delCount = CollectionUtils.isEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdDelList()) ? 0
                                    : countSampleDataWaitVO.getCountSampleDataWaitIdDelList().size();

                    logger.info(Utils.getLog(uuid, "当前需要进行计算的数据有:" + insCount + "个"));
                    logger.info(Utils.getLog(uuid, "当前需要删除的数据有:" + delCount + "个"));
                }

                if (CollectionUtils.isNotEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdList()) || CollectionUtils
                                .isNotEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdDelList())) {

                    String redisKey = "pspc:sample_data_calculate_flag:" + logMessage;
                    String redisKeyStatus = redisHelper.strGet(redisKey);
                    logger.info(Utils.getLog(uuid, "**** RUNNING redisKeyStatus is " + redisKeyStatus));
                    if (StringUtils.isEmpty(redisKeyStatus) || redisKeyStatus.equals("COMPLETED")) {
                        redisHelper.strSet(redisKey, "RUNNING");
                        logger.info(Utils.getLog(uuid, "**** RUNNING redisKey is " + redisKey + " RUNNING"));
                        result = buildSampleSubgroupWait();
                    } else {
                        logger.info(Utils.getLog(uuid, "**** RUNNING 计算运行中 " + logMessage));
                    }
                    logger.info(Utils.getLog(uuid, "*** Thread End 单个计算 " + logMessage));
                }
            }
        }
        return result;
    }

	private void filterByEffectiveType(List<CountSampleDataWaitR> countSampleDataWaitCurrentList,
			List<CountSampleDataWaitR> countSampleDataWaitDelList, List<CountSampleDataWaitR> countSampleDataWaitInsList,
			List<CountSampleDataWaitR> countSampleDataWaitList) {
		String effectiveType = countSampleDataWaitVO.getDataAccessConfigurationList().get(0).getEffectiveType();// 有效类型
		String samplingPosition = countSampleDataWaitVO.getDataAccessConfigurationList().get(0).getSamplingPosition();// 抽样取值位置
		// (1)等距抽样
		if ("ISOMETRIC".equals(effectiveType)) {
			filterByIsometric(countSampleDataWaitVO, samplingPosition, countSampleDataWaitDelList,
					countSampleDataWaitInsList, countSampleDataWaitList);
		}
		// (2)时间抽样
		else if ("TIME".equals(effectiveType)) {
			filterByTime(countSampleDataWaitVO, samplingPosition, countSampleDataWaitCurrentList,
					countSampleDataWaitDelList, countSampleDataWaitInsList, countSampleDataWaitList);
		} else {
			countSampleDataWaitInsList.addAll(countSampleDataWaitList);
			countSampleDataWaitDelList.addAll(countSampleDataWaitList);
		}
	}
	
	private void filterByTime(CountSampleDataWaitVO countSampleDataWaitVO, String samplingPosition,
			List<CountSampleDataWaitR> countSampleDataWaitCurrentList,
			List<CountSampleDataWaitR> countSampleDataWaitDelList, List<CountSampleDataWaitR> countSampleDataWaitInsList,
			List<CountSampleDataWaitR> countSampleDataWaitList) {
		int timeSampling = countSampleDataWaitVO.getDataAccessConfigurationList().get(0).getTimeSampling().intValue(); // 时间抽样(S秒)

		int inxCount = countSampleDataWaitList.size() - 1;// 集合最大下标
		if (Utils.differentMillisecond(countSampleDataWaitList.get(0).getSampleTime(),
				countSampleDataWaitList.get(inxCount).getSampleTime()) >= timeSampling) {

			Date dateStart = null;// 计算开始(时间开始)
			Date dateEnd = null;// 计算开始(时间结束)
			countSampleDataWaitCurrentList.clear();// 清除
			for (int i = 1; i <= inxCount; i++) {
				// 开始时间(默认为第一笔，其后是上次结束时间)
				if (dateStart == null) {
					dateStart = countSampleDataWaitList.get(0).getSampleTime();

					countSampleDataWaitCurrentList.add(countSampleDataWaitList.get(0));// 累计
				}

				dateEnd = countSampleDataWaitList.get(i).getSampleTime();

				countSampleDataWaitCurrentList.add(countSampleDataWaitList.get(i));// 累计

				// 分组校验
				if (Utils.differentMillisecond(dateStart, dateEnd) >= timeSampling - 1) {
					// 前部
					if ("FRONT".equals(samplingPosition)) {
						countSampleDataWaitInsList.add(countSampleDataWaitCurrentList.get(0));
					}
					// 中部
					else if ("MIDDLE".equals(samplingPosition)) {
						if ((countSampleDataWaitCurrentList.size() % 2) != 0) { // 奇数
							countSampleDataWaitInsList
									.add(countSampleDataWaitCurrentList.get(countSampleDataWaitCurrentList.size() / 2));
						} else {
							// 偶数取偏上一笔
							countSampleDataWaitInsList.add(countSampleDataWaitCurrentList
									.get((countSampleDataWaitCurrentList.size() / 2) - 1));
						}
					}
					// 后部
					else if ("BEHIND".equals(samplingPosition)) {
						countSampleDataWaitInsList
								.add(countSampleDataWaitCurrentList.get(countSampleDataWaitCurrentList.size() - 1));
					}

					countSampleDataWaitDelList.addAll(countSampleDataWaitCurrentList);

					dateStart = dateEnd;// 重新开始
					countSampleDataWaitCurrentList.clear();
				}
			}
		}
	}
	
	private void filterByIsometric(CountSampleDataWaitVO countSampleDataWaitVO, String samplingPosition,
			List<CountSampleDataWaitR> countSampleDataWaitDelList, List<CountSampleDataWaitR> countSampleDataWaitInsList,
			List<CountSampleDataWaitR> countSampleDataWaitList) {
		int isometricSampling = countSampleDataWaitVO.getDataAccessConfigurationList().get(0).getIsometricSampling()
				.intValue(); // 等距抽样

		if (countSampleDataWaitList.size() >= isometricSampling) {
			int groupCount = countSampleDataWaitList.size() / isometricSampling;// 分组数量(取整)
			int indxStart = 0;// 计算开始(集合开始下标)
			int indxEnd = 0;// 计算结束(集合结束下标)
			// int indxMiddle = groupCount / 2;// 计算(集合中间下标(取整)：注意奇偶数)

			for (int i = 0; i < groupCount; i++) {
				indxStart = i * isometricSampling;// 计算开始(集合开始下标)
				indxEnd = indxStart + isometricSampling - 1;// 计算结束(集合结束下标)

				// 前部
				if ("FRONT".equals(samplingPosition)) {
					countSampleDataWaitInsList.add(countSampleDataWaitList.get(indxStart));
				}
				// 中部
				else if ("MIDDLE".equals(samplingPosition)) {
					if ((isometricSampling % 2) != 0) { // 奇数
						countSampleDataWaitInsList.add(countSampleDataWaitList.get(indxStart + isometricSampling / 2));
					} else {
						// 偶数取偏上一笔
						countSampleDataWaitInsList
								.add(countSampleDataWaitList.get(indxStart + isometricSampling / 2 - 1));
					}
				}
				// 后部
				else if ("BEHIND".equals(samplingPosition)) {
					countSampleDataWaitInsList.add(countSampleDataWaitList.get(indxEnd));
				}

				// 添加待删除集合
				for (int k = indxStart; k <= indxEnd; k++) {
					countSampleDataWaitDelList.add(countSampleDataWaitList.get(k));
				}
			}
		}
	}
	
	private void filterByLimit(List<CountSampleDataWaitR> countSampleDataWaitCurrentList,
			List<CountSampleDataWaitR> countSampleDataWaitDelList, List<CountSampleDataWaitR> countSampleDataWaitList) {
		// 数据过滤上限(有可能为空)
		if (countSampleDataWaitVO.getDataAccessConfigurationList().get(0).getDataUpperLimit() != null) {
			// 待删除(异常：样本值 '大于' 数据过滤上限)
			countSampleDataWaitCurrentList = countSampleDataWaitList.stream()
					.filter(lovValueDTO -> lovValueDTO.getSampleValueCount().doubleValue() > countSampleDataWaitVO
							.getDataAccessConfigurationList().get(0).getDataUpperLimit().doubleValue())
					.collect(Collectors.toList());

			countSampleDataWaitDelList.addAll(countSampleDataWaitCurrentList);// 移到待删除集合
			countSampleDataWaitList.removeAll(countSampleDataWaitCurrentList);// 去掉异常集合
		}

		// 数据过滤下限(有可能为空)
		if (countSampleDataWaitVO.getDataAccessConfigurationList().get(0).getDataLowerLimit() != null) {
			// 待删除(异常：样本值 '小于' 数据过滤下限)
			countSampleDataWaitCurrentList = countSampleDataWaitList.stream()
					.filter(lovValueDTO -> lovValueDTO.getSampleValueCount().doubleValue() < countSampleDataWaitVO
							.getDataAccessConfigurationList().get(0).getDataLowerLimit().doubleValue())
					.collect(Collectors.toList());

			countSampleDataWaitDelList.addAll(countSampleDataWaitCurrentList);// 移到待删除集合
			countSampleDataWaitList.removeAll(countSampleDataWaitCurrentList);// 去掉异常集合
		}
	}
	
	private Map<EntityR, CountCalculationVO> processData() {
        long startTime = 0L;
        Map<EntityR, CountCalculationVO> result = new HashMap<>();

        if (CollectionUtils.isNotEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdList())) {
            startTime = System.currentTimeMillis();

            /**
             * 再数据计算之前，排除掉技术数据中，计量类型的数据
             */
            //List<Long> countSampleDataWaitIdList = countSampleDataWaitRepository.selectIsCountData(countSampleDataWaitVO);  //modified 20191025
            String whereInSql = this.GetWhereInValuesSql("sdw.count_sample_data_wait_id",countSampleDataWaitVO.getCountSampleDataWaitIdList(),800);
            List<Long> countSampleDataWaitIdList = countSampleDataWaitRepository.selectIsCountDataModified(whereInSql);

            /**
             * 如果数据都是计量型的，或者没有匹配到的，则全部将待处理的数据放到待删除的基合中
             */
            if (CollectionUtils.isEmpty(countSampleDataWaitIdList)) {
                countSampleDataWaitVO.getCountSampleDataWaitIdDelList()
                                .addAll(countSampleDataWaitVO.getCountSampleDataWaitIdList());
            }
            countSampleDataWaitVO.setCountSampleDataWaitIdList(countSampleDataWaitIdList);
            logger.info(Utils.getLog(uuid, "排除计量数据，耗时=" + (System.currentTimeMillis() - startTime) + "ms"));

            startTime = System.currentTimeMillis();
            List<EntityR> entityList = entityRepository.listCountSampleDataWait(countSampleDataWaitVO);
            String entityCodes = entityList.stream().map(EntityR::getEntityCode).collect(Collectors.joining(","));
            logger.info(Utils.getLog(uuid, "获取实体控制图，实体控制图数量= " + entityList.size() + ",编码=" + entityCodes + ",耗时="
                            + (System.currentTimeMillis() - startTime) + "ms"));

            startTime = System.currentTimeMillis();
            result = sampleCountCalculationRunService.batchExecCountStatistic(entityList, uuid);
            logger.info(Utils.getLog(uuid, "*************实体控制图计算耗时：" + (System.currentTimeMillis() - startTime) + "ms"));
        }

        /**
         * 样本数据预处理
         */
        if (CollectionUtils.isNotEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdDelList())
                        || CollectionUtils.isNotEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdList())) {
            startTime = System.currentTimeMillis();
            sampleCountCalculationRunService.execCountSampleDataWait(countSampleDataWaitVO, uuid);
            logger.info(Utils.getLog(uuid,  "*************样本数据预处理耗时：" + (System.currentTimeMillis() - startTime) + "ms"));
        }
        return result;
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
	
    
    /*public void main(String[] args){
    	List<Long> lists = new  ArrayList<>();
    	for(Long i = 0L;i<100L;i++) {
    		lists.add(i);
    	}
    	String string = GetWhereInValuesSql("suu.user_id",lists,50 );
    	System.out.println(string);
    }*/
	
}
