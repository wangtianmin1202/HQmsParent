package com.hand.spc.job.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.job.service.ISampleCountCalculationRunSService;
import com.hand.spc.repository.dto.AttachmentGroupR;
import com.hand.spc.repository.dto.AttachmentR;
import com.hand.spc.repository.dto.AttachmentType;
import com.hand.spc.repository.dto.ChartDetailR;
import com.hand.spc.repository.dto.ChartR;
import com.hand.spc.repository.dto.CountCalculationVO;
import com.hand.spc.repository.dto.CountOocR;
import com.hand.spc.repository.dto.CountOocRequestDTO;
import com.hand.spc.repository.dto.CountOocVO;
import com.hand.spc.repository.dto.CountSampleDataWaitR;
import com.hand.spc.repository.dto.CountSampleDataWaitVO;
import com.hand.spc.repository.dto.CountStatisticR;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.JudementParaCountVO;
import com.hand.spc.repository.dto.JudementVO;
import com.hand.spc.repository.dto.JudgementR;
import com.hand.spc.repository.dto.MessageDetailR;
import com.hand.spc.repository.dto.MessageR;
import com.hand.spc.repository.dto.MessageTypeDetailR;
import com.hand.spc.repository.dto.MessageTypeR;
import com.hand.spc.repository.dto.MessageUploadConfigR;
import com.hand.spc.repository.dto.MessageUploadRelR;
import com.hand.spc.repository.dto.Sequence;
import com.hand.spc.repository.mapper.AttachmentTypeMapper;
import com.hand.spc.repository.mapper.CountOocRMapper;
import com.hand.spc.repository.mapper.CountSampleDataWaitRMapper;
import com.hand.spc.repository.mapper.CountStatisticRMapper;
import com.hand.spc.repository.mapper.GetCodeValuesMapper;
import com.hand.spc.repository.mapper.MessageDetailRMapper;
import com.hand.spc.repository.mapper.MessageRMapper;
import com.hand.spc.repository.mapper.MessageUploadRelRMapper;
import com.hand.spc.repository.mapper.SequenceMapper;
import com.hand.spc.repository.service.IAttachmentRService;
import com.hand.spc.repository.service.IChartRService;
import com.hand.spc.repository.service.ICountOocRService;
import com.hand.spc.repository.service.ICountSampleDataRService;
import com.hand.spc.repository.service.ICountStatisticRService;
import com.hand.spc.repository.service.IJudgementRService;
import com.hand.spc.repository.service.IMessageDetailRService;
import com.hand.spc.repository.service.IMessageRService;
import com.hand.spc.repository.service.IMessageTypeRService;
import com.hand.spc.repository.service.IMessageUploadRelRService;
import com.hand.spc.repository.service.ISequenceRService;
import com.hand.spc.temppkg.dto.Temppkgdto;
import com.hand.spc.utils.CommonException;
import com.hand.spc.utils.MtSqlHelper;
import com.hand.spc.utils.RedisSingle;
import com.hand.spc.utils.Utils;
//计数型
@Transactional(rollbackFor = Exception.class)
public class SampleCountCalculationRunSServiceImpl extends BaseServiceImpl<Temppkgdto> implements ISampleCountCalculationRunSService {

	private Logger logger = LoggerFactory.getLogger(SampleCountCalculationSServiceImpl.class);

	@Autowired
	private CountSampleDataWaitRMapper countSampleDataWaitMapper;// 样本数据(计数)预处理
	@Autowired
	private ICountSampleDataRService countSampleDataRepository;// 样本数据(计数)
	@Autowired
	private ICountStatisticRService countStatisticRepository;// 计数型统计量
	@Autowired
    private CountStatisticRMapper countStatisticMapper;
	@Autowired
	private IChartRService chartRepository;// 控制图
	@Autowired
	private ISequenceRService sequenceRepositoty;// 序号表
	@Autowired
	private ICountOocRService countOocRepository;
	@Autowired
    private CountOocRMapper countOocMapper;
	@Autowired
    private IMessageTypeRService messageTypeRepository;
	
	@Autowired
	private IJudgementRService judgementRepository;
	@Autowired
	private IMessageRService messageRepository;
	@Autowired
    private MessageRMapper messageMapper;
	@Autowired
	private IMessageDetailRService messageDetailRepository;
	@Autowired
    private MessageDetailRMapper messageDetailMapper;
	@Autowired
	private IMessageUploadRelRService messageUploadRelRepository;
	@Autowired
    private MessageUploadRelRMapper messageUploadRelMapper;
	@Autowired
	private InterfaceService interfaceService;// 发送接口
	@Autowired
	private SequenceMapper sequenceMapper;
	@Autowired
	private GetCodeValuesMapper getCodeValuesMapper;
	@Autowired
    private JdbcTemplate jdbcTemplate; // modified 20190902
	@Autowired
	private AttachmentTypeMapper attachmentTypeMapper;
	@Autowired
	private IAttachmentRService attachmentRepository;

	@Override
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public void execCountSampleDataWait(CountSampleDataWaitVO countSampleDataWaitVO, String uuid) {
		long startTime = 0L;
		if (CollectionUtils.isNotEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdList())) {
            startTime = System.currentTimeMillis();
            CountSampleDataWaitR countSampleDataWait = new CountSampleDataWaitR();
            countSampleDataWait.setTenantId(countSampleDataWaitVO.getTenantId());
            countSampleDataWait.setSiteId(countSampleDataWaitVO.getSiteId());
            List<Long> countSampleDataWaitList = countSampleDataWaitMapper.selectIsCountData(countSampleDataWaitVO);

            // 预处理样本数据 ->样本数据
            if (CollectionUtils.isNotEmpty(countSampleDataWaitList)) {
                countSampleDataWait.setCountSampleDataWaitIdList(countSampleDataWaitList);
                countSampleDataRepository.insertCountSampleData(countSampleDataWait);

                logger.info(Utils.getLog(uuid, "预处理样本数据预处理,数据大小=" + countSampleDataWaitList.size() + ",耗时="
                                + (System.currentTimeMillis() - startTime) + "ms"));
            }
        }
		if (CollectionUtils.isNotEmpty(countSampleDataWaitVO.getCountSampleDataWaitIdDelList())) {
            startTime = System.currentTimeMillis();
            // 删除预处理样本数据
            countSampleDataWaitMapper.deleteCountSampleDataDelWait(countSampleDataWaitVO);
            logger.info(Utils.getLog(uuid,
                            "删除countSampleDataWaitMapper,数据大小="
                                            + countSampleDataWaitVO.getCountSampleDataWaitIdDelList().size() + ",耗时="
                                            + (System.currentTimeMillis() - startTime) + "ms"));
        }
	}
	
	
	@Transactional(rollbackFor = Exception.class)
    @Override
    public Map<EntityR, CountCalculationVO> batchExecCountStatistic(List<EntityR> entityList, String uuId) {
        Map<EntityR, CountCalculationVO> map = new HashMap<>();
        for (EntityR entity : entityList) {
            if (CollectionUtils.isNotEmpty(entity.getCountSampleDataWaitList())) {
                map.put(entity, execCountStatistic(entity, uuId));
            }
        }
        return map;
    }
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public CountCalculationVO execCountStatistic(EntityR entity, String uuid) {
		/**
         * StringBuilder redisKeyBuilder = new StringBuilder();
         * redisKeyBuilder.append(entity.getTenantId()).append("-").append(entity.getSiteId()).append("-")
         * .append(entity.getEntityCode()).append("-").append(entity.getEntityVersion());
         * 
         * String countStatisticRedisKey = "pspc:count_statistic_data:" + redisKeyBuilder; String
         * countOocRedisKey = "pspc:count_ooc_data:" + redisKeyBuilder;
         **/

        long startTime = System.currentTimeMillis();
        CountCalculationVO countCalculationVO = new CountCalculationVO();
        int dataSize = entity.getCountSampleDataWaitList().size(); // 样本数据预处理(计数)
        logger.info(Utils.getLog(uuid, "实体控制图分组处理 entityCode=" + entity.getEntityCode() + ",entityVersion="
                        + entity.getEntityVersion() + ";预分组数据行数=" + dataSize));

        /**
         * 再Sequence中，记录当前最大组号
         */
        Sequence sequenceQuery = new Sequence();
        sequenceQuery.setTenantId(entity.getTenantId());
        sequenceQuery.setSiteId(entity.getSiteId());
        sequenceQuery.setSequenceCode("COUNTSTATISTIC-" + entity.getEntityCode() + "-" + entity.getEntityVersion());
        Sequence sequence = sequenceMapper.selectOne(sequenceQuery);
        if (sequence == null) {
            sequence = new Sequence();
            sequence.setTenantId(entity.getTenantId());
            sequence.setSiteId(entity.getSiteId());
            sequence.setSequenceCode(sequenceQuery.getSequenceCode());
            sequence.setMaxNum(0L);
        }
        logger.info(Utils.getLog(uuid, "获取实体控制图最大组号,组号=" + sequence.getMaxNum() + ",耗时="
                        + (System.currentTimeMillis() - startTime) + "ms"));
        
        /**
         * 根据实体控制图获取控制图(M) 计数型只计算主图(M)
         */
        startTime = System.currentTimeMillis();
        ChartR chart = chartRepository.queryChartByEntity(entity);
        if (chart == null) {
            throw new CommonException(
                            "找不到该实体控制图下的控制图!实体控制编码:" + entity.getEntityCode() + ",版本:" + entity.getEntityVersion());
        }
        logger.info(Utils.getLog(uuid, "查询控制图,耗时=" + (System.currentTimeMillis() - startTime) + "ms"));

        startTime = System.currentTimeMillis();
        //List<AttachmentType> attachmentTypeList = attachmentTypeMapper.selectAll(); // 原hzero方法   modified 20191023
        List<CodeValue> attachmentTypeList = new ArrayList<CodeValue>();
		attachmentTypeList = getCodeValuesMapper.getCodeValues("PSPC.ATTACHMENT.TYPE");
        AttachmentGroupR attachmentGroup = new AttachmentGroupR();
        attachmentGroup.setTenantId(entity.getTenantId());
        attachmentGroup.setSiteId(entity.getSiteId());
        attachmentGroup.setAttachmentGroupId(entity.getAttachmentGroupId());
        List<AttachmentR> attachmentList = attachmentRepository.getAttachmentByGroup(attachmentGroup);

        StringBuffer attachmentCodes = new StringBuffer();
        StringBuffer attachmentDescriptions = new StringBuffer();
        String desc = "";
        for (AttachmentR attachment : attachmentList) {
            attachmentCodes.append(attachment.getAttachmentType()).append(":").append(attachment.getAttachmentCode()).append(";");
            if (CollectionUtils.isNotEmpty(attachmentTypeList)) {
            	// 原hzero方法   modified 20191023
//                Optional<AttachmentType> optional = attachmentTypeList.stream().filter(t -> t.getAttachmentTypeCode().equals(attachment.getAttachmentType())).findFirst();
//                if (optional.isPresent()) {
//                    desc = optional.get().getAttachmentTypeDesc();
//                }
            	
            	for (CodeValue attachmentType : attachmentTypeList) {
					if (attachmentType.getValue().equals(attachment.getAttachmentType())) {
						desc = attachmentType.getMeaning();
					}
				}
            }
            attachmentDescriptions.append(desc + ":" + attachment.getDescription() + ";");
        }
        logger.info(Utils.getLog(uuid, "获取附着对象信息,耗时=" + (System.currentTimeMillis() - startTime) + "ms"));

        entity.setAttachmentCodes(attachmentCodes.toString());// 附着对象编码,以逗号拼接
        entity.setAttchmentDescriptions(attachmentDescriptions.toString());// 附着对象描述，以逗号拼接

        startTime = System.currentTimeMillis();
        JudementVO judementVo = initJudementVo(entity.getTenantId(), entity.getSiteId(), chart.getChartId());
        logger.info(Utils.getLog(uuid, "查询判异规则，规则数=" + judementVo.getJudgementSize() + ",耗时="
                        + (System.currentTimeMillis() - startTime) + "ms"));

        List<CountStatisticR> countStatisticList = new ArrayList<>();// 最大绘点数范围内
        List<CountStatisticR> countStatisticTempList = new ArrayList<>();// 最大绘点数范围外
        List<CountOocR> existsCountOocList = new ArrayList<>();// 已存在COUNT OOC集合
        CountSampleDataWaitR countSampleDataWait = null; // 预处理样本数据(计数)
        List<CountOocR> countOocList = new ArrayList<>();// COUNT OOC集合
        List<MessageR> messageList = new ArrayList<>();// 消息集合
        List<MessageDetailR> messageDetailList = new ArrayList<>();// 消息明细集合
        List<MessageUploadRelR> messageUploadRelList = new ArrayList<>();// 消息命令集合

        String sampleDatas = "";// 当前分组样本数据拼接字符串 样本数据 + "/" + 样本值
        // 样本结束时间
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");

        if (sequence.getMaxNum() > 0) {
        	/**
             * String countStatisticRedis = redisHelper.strGet(countStatisticRedisKey);
             * 
             * // 注意：P、U需要反推表，Redis存的版本不是最新的，所以需要取表 if ("P".equals(chart.getChartType()) ||
             * "U".equals(chart.getChartType()) || StringUtils.isEmpty(countStatisticRedis)) { //
             * pspc_count_statistic(计数型统计量表) // max_plot_points 最大绘点数 countStatisticList =
             * countStatisticRepository.listCountStatistic(entity.getTenantId(), entity.getSiteId(),
             * entity.getEntityCode(), entity.getEntityVersion(), sequence.getMaxNum(),
             * sequence.getMaxNum() - chart.getMaxPlotPoints() + 1, chart.getChartType());
             * 
             * // 查询当前绘点数范围内已存在OOC集合 if (CollectionUtils.isNotEmpty(countStatisticList)) {
             * List<Long> idList =
             * countStatisticList.stream().map(CountStatistic::getCountSampleDataId)
             * .collect(Collectors.toList()); CountOocRequestDTO countOocRequestDTO = new
             * CountOocRequestDTO(); countOocRequestDTO.setTenantId(entity.getTenantId());
             * countOocRequestDTO.setSiteId(entity.getSiteId());
             * countOocRequestDTO.setCountSampleDataIdList(idList); existsCountOocList =
             * countOocRepository.listCountOocByStatisticList(countOocRequestDTO); } } else {
             * countStatisticList = redisHelper.fromJsonList(countStatisticRedis,
             * CountStatistic.class); String jsonCountOocData =
             * redisHelper.strGet(countOocRedisKey); if (!StringUtils.isEmpty(jsonCountOocData)) {
             * existsCountOocList = redisHelper.fromJsonList(jsonCountOocData, CountOoc.class); } }
             **/
        	
        	// 直接获取数据库的计算结果数据
        	startTime = System.currentTimeMillis();
            countStatisticList = countStatisticRepository.listCountStatistic(entity.getTenantId(), entity.getSiteId(),
                            entity.getEntityCode(), entity.getEntityVersion(), sequence.getMaxNum(),
                            sequence.getMaxNum() - chart.getMaxPlotPoints() + 1, chart.getChartType());

            // 查询当前绘点数范围内已存在OOC集合
            if (CollectionUtils.isNotEmpty(countStatisticList)) {
                List<Long> idList = countStatisticList.stream().map(CountStatisticR::getCountSampleDataId)
                                .collect(Collectors.toList());
                CountOocRequestDTO countOocRequestDTO = new CountOocRequestDTO();
                countOocRequestDTO.setTenantId(entity.getTenantId());
                countOocRequestDTO.setSiteId(entity.getSiteId());
                countOocRequestDTO.setCountSampleDataIdList(idList);
                existsCountOocList = countOocRepository.listCountOocByStatisticList(countOocRequestDTO);
            }
            logger.info(Utils.getLog(uuid, "查询最大绘点数范围的最后计数型统计量数据，数量=" + countStatisticList.size() + ",耗时="
                    + (System.currentTimeMillis() - startTime) + "ms"));
            logger.info(Utils.getLog(uuid, "查询已存在OOC，OOC个数=" + existsCountOocList.size() + ",耗时="
                    + (System.currentTimeMillis() - startTime) + "ms"));
        }

        Long axNum = 0L;// 最大序号
        String groupCode = entity.getEntityCode() + "-" + entity.getEntityVersion() + "-";// 当前最大序号(Message组号)
        for (int i = 0; i < dataSize; i++) {
            countSampleDataWait = entity.getCountSampleDataWaitList().get(i);// 当前计数型统计量
            axNum = sequence.getMaxNum() + i + 1;

            /************************ 计算 ********************/
            CountStatisticR countStatistic =
                    countStatisticCalculate(countSampleDataWait, countStatisticList, entity, chart, axNum);
            countStatistic.setInsStatus("Y");// Y新增

            // 最后一个样本(类型为P&U)，重新计算控制上/下限
            if ((i == dataSize - 1) && ("P".equals(chart.getChartType()) || "U".equals(chart.getChartType()))) {
                DecimalFormat dfPU = new DecimalFormat("#.0000"); // 默认取两位小数
                Double centerLinePU = countStatistic.getCenterLine().doubleValue();// 中心线
                for (CountStatisticR countStatisticPU : countStatisticList) {
                    if ("P".equals(chart.getChartType())) {
                        countStatisticPU.setUpperControlLimit(new BigDecimal(
                                        dfPU.format(centerLinePU + 3 * Math.sqrt((centerLinePU * (1 - centerLinePU)
                                                        / countStatisticPU.getSampleValueCount().doubleValue())))));// 控制上限
                        countStatisticPU.setLowerControlLimit(new BigDecimal(
                                        dfPU.format(centerLinePU - 3 * Math.sqrt((centerLinePU * (1 - centerLinePU)
                                                        / countStatisticPU.getSampleValueCount().doubleValue())))));// 控制下限
                    } else if ("U".equals(chart.getChartType())) {
                        countStatisticPU.setUpperControlLimit(new BigDecimal(dfPU.format(centerLinePU + 3 * Math
                                        .sqrt((centerLinePU / countStatisticPU.getSampleValueCount().doubleValue())))));// 控制上限
                        countStatisticPU.setLowerControlLimit(new BigDecimal(dfPU.format(centerLinePU - 3 * Math
                                        .sqrt((centerLinePU / countStatisticPU.getSampleValueCount().doubleValue())))));// 控制下限
                    }
                }
            }

            sampleDatas = sdf.format(countSampleDataWait.getSampleTime()) + "/" + countSampleDataWait.getSampleValueCount() + ",";
            countStatistic.setSampleDatas(sampleDatas);

            // 主要指标
            if ("nP".equals(chart.getChartType())) {
                countStatistic.setMainStatisticValue(countStatistic.getUnqualifiedQuantity().doubleValue());
            } else if ("P".equals(chart.getChartType())) {
                countStatistic.setMainStatisticValue(countStatistic.getUnqualifiedPercent().doubleValue());
            } else if ("C".equals(chart.getChartType())) {
                countStatistic.setMainStatisticValue(countStatistic.getUnqualifiedQuantity().doubleValue());
            } else if ("U".equals(chart.getChartType())) {
                countStatistic.setMainStatisticValue(countStatistic.getUnqualifiedPercent().doubleValue());
            }

            /**
             * 移动最大绘点数范围内第一个点 这里会将之前的ooc取出来，和新进来的数据进行整体计算，但是已经存在ooc的点要排除掉 我们的宗旨是 点不能有重复的ooc
             */
            if (countStatisticList.size() >= chart.getMaxPlotPoints()) {
                // 移除最大绘点数范围内第一个点对应ooc
                long countSampleDataId = countStatisticList.get(0).getCountSampleDataId();
                int oocCount = existsCountOocList.size();
                for (int j = oocCount - 1; j > -0; j--) {
                    if (existsCountOocList.get(j).getCountSampleDataId().longValue() == countSampleDataId) {
                        existsCountOocList.remove(j);
                    }
                }

                // countStatisticList.remove 两个作用 1.删除当前集合中最大会点数的第一个点 2.获取第一个点，如果是P U图的话，会更新该点的数据
                CountStatisticR deleteStatistic = countStatisticList.remove(0);
                countStatisticTempList.add(deleteStatistic);
            }
            countStatisticList.add(countStatistic);

            /******************** OOC判异 ***********************/
            if (judementVo.getJudgementSize() > 0) {
                CountOocVO countOocVO = oocCalculate(entity, countStatisticList, judementVo, existsCountOocList, chart,
                                groupCode + axNum.toString());

                countOocList.addAll(countOocVO.getCountOocList());
                messageList.addAll(countOocVO.getMessageList());
                messageDetailList.addAll(countOocVO.getMessageDetailList());
                messageUploadRelList.addAll(countOocVO.getMessageUploadRelList());
            }
        }
        logger.info(Utils.getLog(uuid, "样本计算完成，entityCode=" + entity.getEntityCode() + ",entityVersion="
                + entity.getEntityVersion() + ",耗时=" + (System.currentTimeMillis() - startTime) + "ms"));

        countStatisticTempList.addAll(countStatisticList);
        countCalculationVO.setCountStatisticList(countStatisticTempList);

        /**
         * 统计新增的计算结果
         */
        List<CountStatisticR> countStatisticListIns = countStatisticTempList.stream().filter(lovValueDTO -> null == lovValueDTO.getCountStatisticId()).collect(Collectors.toList());
        /**
         * 统计需要更新的计算结果
         */
        List<CountStatisticR> countStatisticListUpd = countStatisticTempList.stream().filter(lovValueDTO -> null != lovValueDTO.getCountStatisticId()).collect(Collectors.toList());
        startTime = System.currentTimeMillis();
        sequence.setMaxNum(sequence.getMaxNum() + countStatisticListIns.size());
        if (sequence.getSequenceId() == null) {
        	sequenceMapper.insertSelective(sequence);
        } else {
        	sequenceMapper.updateByPrimaryKeySelective(sequence);
        }
        logger.info(Utils.getLog(uuid, "保存最大组号，entityCode=" + entity.getEntityCode() + ",entityVersion="
                        + entity.getEntityVersion() + ",耗时=" + (System.currentTimeMillis() - startTime) + "ms"));
        
        /**
         * 保存统计新增的计算结果
         */
        startTime = System.currentTimeMillis();
        countStatisticRepository.batchInsertRows(countStatisticListIns);
        logger.info(Utils.getLog(uuid,
                        "保存新增的计算结果，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                        + entity.getEntityVersion())
                        + ";数量=" + countStatisticListIns.size() + ",耗时=" + (System.currentTimeMillis() - startTime)
                        + "ms");
        
        /**
         * 如果是P或U图，需要保存更新的计算结果
         */
        if (("P".equals(chart.getChartType()) || "U".equals(chart.getChartType()))
                        && CollectionUtils.isNotEmpty(countStatisticListUpd)) {

            startTime = System.currentTimeMillis();
            List<String> sqlList = new ArrayList<>(countStatisticListUpd.size());
            for (CountStatisticR countStatistic : countStatisticListUpd) {
                sqlList.addAll(MtSqlHelper.getUpdateSql(countStatistic));
            }
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            logger.info(Utils.getLog(uuid,
                            "保存更新的计算结果，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";数量=" + countStatisticListUpd.size() + ",耗时=" + (System.currentTimeMillis() - startTime)
                            + "ms");
        }
  
        /**
         * 保存 count OOC
         */
        startTime = System.currentTimeMillis();
        if (CollectionUtils.isNotEmpty(countOocList)) {
            //countOocRepository.batchInsertRows(countOocList);
            for(int k=0;k<countOocList.size();k++) {
            	countOocMapper.insertSelective(countOocList.get(k));
    		}
        }
        logger.info(Utils.getLog(uuid,
                        "保存OOC，entityCode=" + entity.getEntityCode() + ",entityVersion=" + entity.getEntityVersion())
                        + ";OOC数量=" + countOocList.size() + ",耗时=" + (System.currentTimeMillis() - startTime) + "ms");
        
        /**
         * 保存消息明细
         */
        if (CollectionUtils.isNotEmpty(messageList)) {
            startTime = System.currentTimeMillis();
            Date messageCreationDate = new Date();
            messageList.stream().forEach(message -> {
                message.setCreationDate(messageCreationDate);
            });

            //messageRepository.batchInsertRows(messageList);
            for(int k=0;k<messageList.size();k++) {
            	messageMapper.insertSelective(messageList.get(k));
			};
            logger.info(Utils.getLog(uuid,
                            "保存消息头，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";消息头数量=" + messageList.size() + ",耗时=" + (System.currentTimeMillis() - startTime)
                            + "ms");

            startTime = System.currentTimeMillis();
            //messageDetailRepository.batchInsertRows(messageDetailList);
            for(int k=0;k<messageDetailList.size();k++) {
				messageDetailMapper.insertSelective(messageDetailList.get(k));
			};
            logger.info(Utils.getLog(uuid,
                            "保存消息行，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";保存消息行数量=" + messageDetailList.size() + ",耗时=" + (System.currentTimeMillis() - startTime)
                            + "ms");

            countCalculationVO.setMessageList(messageList);
            countCalculationVO.setMessageCreationDate(messageCreationDate);
            countCalculationVO.setMessageType("count");
            /**
             * 保存需要发送的消息
             */
            startTime = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(messageUploadRelList)) {
                //messageUploadRelRepository.batchInsertRows(messageUploadRelList);
            	for(int k=0;k<messageUploadRelList.size();k++) {
            		messageUploadRelMapper.insertSelective(messageUploadRelList.get(k));
    			};
            }
            logger.info(Utils.getLog(uuid,
                            "保存发送消息数据，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";保存发送消息数量=" + messageUploadRelList.size() + ",耗时="
                            + (System.currentTimeMillis() - startTime) + "ms");

        }
        
        
        
        
        return countCalculationVO;

        /**
         * 保存最大绘点数范围内子组数据至redis if (countStatisticList.size() >= chart.getMaxPlotPoints()) { int
         * more = countStatisticList.size() - chart.getMaxPlotPoints().intValue();
         * Iterator<CountStatistic> iterator = countStatisticList.iterator(); int i = 0; while
         * (iterator.hasNext()) { if (i >= more) { break; } iterator.next(); iterator.remove(); i++;
         * } } countStatisticList.stream().forEach(countStatistic ->
         * countStatistic.setInsStatus(null)); redisHelper.strSet(countStatisticRedisKey,
         * redisHelper.toJson(countStatisticList)); logger.info(Utils.getLog(uuid,
         * "保存最大绘点数范围内子组数据到redis，entityCode=" + entity.getEntityCode() + ",entityVersion=" +
         * entity.getEntityVersion()) + ";子组数量=" + countStatisticList.size());
         * 
         * // 保存最大绘点数范围内OOC数据至redis redisHelper.strSet(countOocRedisKey,
         * redisHelper.toJson(existsCountOocList)); logger.info(Utils.getLog(uuid,
         * "保存最大绘点数范围内OOC数据到redis，entityCode=" + entity.getEntityCode() + ",entityVersion=" +
         * entity.getEntityVersion()) + ";OOC数量=" + existsCountOocList.size());
         * 
         * logger.info(Utils.getLog(uuid, "******* end 实体控制图分组处理 entityCode=" +
         * entity.getEntityCode() + ",entityVersion=" + entity.getEntityVersion()));
         **/
	}

	/**
	 * 计算
	 *
	 * @param countSampleDataWait 当前计数型统计量
	 * @param countStatisticList  当前计数型统计量表
	 * @param entity              实体控制图
	 * @param chart               控制图
	 * @return
	 */
	private CountStatisticR countStatisticCalculate(CountSampleDataWaitR countSampleDataWait,
            List<CountStatisticR> countStatisticList, EntityR entity, ChartR chart, Long maxNum) {
		CountStatisticR countStatistic = new CountStatisticR();
        DecimalFormat df = new DecimalFormat("#.0000"); // 默认取两位小数

        Double currUnqualifiedPercent = 0D;// 不合格率/单位缺陷数--当前值
        Double unqualifiedPercentSum = 0D;// 不合格率/单位缺陷数--和
        Double unqualifiedPercentAvg = 0D;// 不合格率/单位缺陷数--平均值

        Double unqualifiedQuantitySum = 0D;// 不合格数--和
        Double unqualifiedQuantityAvg = 0D;// 不合格数--平均值
        Double sampleValueCountSum = 0D;// 样本值--和

        for (ChartDetailR detail : chart.getChartDetailList()) {
            // 控制图明细类型（M主图/S次图）
            if ("M".equals(detail.getChartDetailType())) {
                // countStatistic.setCountStatisticId();//表ID，主键
                countStatistic.setTenantId(entity.getTenantId());// 租户ID
                countStatistic.setSiteId(entity.getSiteId());// 站点ID
                countStatistic.setCountSampleDataId(countSampleDataWait.getCountSampleDataWaitId());// 预处理样本数据ID
                countStatistic.setEntityCode(entity.getEntityCode());// SPC实体控制图
                countStatistic.setEntityVersion(entity.getEntityVersion());// SPC实体控制图版本
                countStatistic.setSubgroupNum(maxNum);// 组号
                countStatistic.setSampleValueCount(countSampleDataWait.getSampleValueCount());// 样本数（计数)
                countStatistic.setUnqualifiedQuantity(countSampleDataWait.getUnqualifiedQuantity());// 不合格数
                countStatistic.setSampleTime(countSampleDataWait.getSampleTime());// 样本时间

                countStatistic.setUpperSpecLimit(detail.getUpperSpecLimit());// 规格上限
                countStatistic.setSpecTarget(detail.getSpecTarget());// 目标值
                countStatistic.setLowerSpecLimit(detail.getLowerSpecLimit());// 规格下限

                // 不合格率（单位缺陷数）=不合格数/样本数
                currUnqualifiedPercent = countStatistic.getUnqualifiedQuantity().doubleValue()
                                / countStatistic.getSampleValueCount().doubleValue();
                countStatistic.setUnqualifiedPercent(new BigDecimal(df.format(currUnqualifiedPercent)));

                // 计算sigma
                // 控制限选项(NULL:无;FIXED:固定;CALCULATION:计算;COEFFICIENT:系数计算;)
                if ("FIXED".equals(detail.getControlLimitUsage())) {
                    countStatistic.setUpperControlLimit(detail.getUpperControlLimit());// 控制上限
                    countStatistic.setCenterLine(detail.getCenterLine());// 中心线
                    countStatistic.setLowerControlLimit(detail.getLowerControlLimit());// 控制下限
                } else if ("CALCULATION".equals(detail.getControlLimitUsage())) {
                    // 计算平均值(如果是第一个点，就是当前样本数)
                    // unqualifiedPercentAvg =
                    // countStatisticList.stream().mapToDouble(CountStatistic::getUnqualifiedPercent).average().getAsDouble();
                    if (countStatisticList.size() > 0) {

                        if ("P".equals(chart.getChartType()) || "U".equals(chart.getChartType())) {
                            sampleValueCountSum = countSampleDataWait.getSampleValueCount().doubleValue()
                                            + countStatisticList.stream().map(CountStatisticR::getSampleValueCount)
                                                            .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
                            unqualifiedQuantitySum = countSampleDataWait.getUnqualifiedQuantity().doubleValue()
                                            + countStatisticList.stream().map(CountStatisticR::getUnqualifiedQuantity)
                                                            .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
                            unqualifiedPercentAvg = unqualifiedQuantitySum / sampleValueCountSum;
                        } else {
                            unqualifiedPercentSum =
                                            countStatisticList.stream().map(CountStatisticR::getUnqualifiedPercent)
                                                            .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue()
                                                            + currUnqualifiedPercent;// 包含当前
                            unqualifiedPercentAvg = unqualifiedPercentSum / (countStatisticList.size() + 1);

                            unqualifiedQuantitySum = countStatisticList.stream()
                                            .map(CountStatisticR::getUnqualifiedQuantity)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue()
                                            + countSampleDataWait.getUnqualifiedQuantity().doubleValue();
                            unqualifiedQuantityAvg = unqualifiedQuantitySum / (countStatisticList.size() + 1);
                        }

                    } else {
                        if ("P".equals(chart.getChartType()) || "U".equals(chart.getChartType())) {
                            sampleValueCountSum = countSampleDataWait.getSampleValueCount().doubleValue();
                            unqualifiedQuantitySum = countSampleDataWait.getUnqualifiedQuantity().doubleValue();
                            unqualifiedPercentAvg = currUnqualifiedPercent;
                        } else {
                            unqualifiedPercentSum = currUnqualifiedPercent;
                            unqualifiedPercentAvg = currUnqualifiedPercent;

                            unqualifiedQuantitySum = countSampleDataWait.getUnqualifiedQuantity().doubleValue();
                            unqualifiedQuantityAvg = countSampleDataWait.getUnqualifiedQuantity().doubleValue();
                        }

                    }

                    // 控制图类型：
                    // XBAR-R:均值-极差图;XBAR-S:均值-标准差图;Me-R:中位数-极差图;X-Rm:单值-移动极差图;
                    // P:不合格率图;nP:不合格品数图;C:不合格数图;U:单位不合格数图;
                    if ("nP".equals(chart.getChartType())) {
                        countStatistic.setCenterLine(new BigDecimal(df.format(
                                        countStatistic.getSampleValueCount().doubleValue() * unqualifiedPercentAvg)));// 中心线
                        countStatistic.setUpperControlLimit(
                                        new BigDecimal(df.format(countStatistic.getCenterLine().doubleValue()
                                                        + 3 * Math.sqrt((countStatistic.getCenterLine().doubleValue()
                                                                        * (1 - unqualifiedPercentAvg))))));// 控制上限
                        countStatistic.setLowerControlLimit(
                                        new BigDecimal(df.format(countStatistic.getCenterLine().doubleValue()
                                                        - 3 * Math.sqrt((countStatistic.getCenterLine().doubleValue()
                                                                        * (1 - unqualifiedPercentAvg))))));// 控制下限
                    } else if ("P".equals(chart.getChartType())) {
                        countStatistic.setCenterLine(
                                        new BigDecimal(df.format(unqualifiedQuantitySum / sampleValueCountSum)));// 中心线
                        countStatistic.setUpperControlLimit(new BigDecimal(df.format(unqualifiedPercentAvg
                                        + 3 * Math.sqrt((unqualifiedPercentAvg * (1 - unqualifiedPercentAvg)
                                                        / countStatistic.getSampleValueCount().doubleValue())))));// 控制上限
                        countStatistic.setLowerControlLimit(new BigDecimal(df.format(unqualifiedPercentAvg
                                        - 3 * Math.sqrt((unqualifiedPercentAvg * (1 - unqualifiedPercentAvg)
                                                        / countStatistic.getSampleValueCount().doubleValue())))));// 控制下限
                    } else if ("C".equals(chart.getChartType())) {
                        countStatistic.setCenterLine(new BigDecimal(df.format(unqualifiedQuantityAvg)));// 中心线
                        countStatistic.setUpperControlLimit(new BigDecimal(
                                        df.format(unqualifiedQuantityAvg + 3 * Math.sqrt(unqualifiedQuantityAvg))));// 控制上限
                        countStatistic.setLowerControlLimit(new BigDecimal(
                                        df.format(unqualifiedQuantityAvg - 3 * Math.sqrt(unqualifiedQuantityAvg))));// 控制下限
                    } else if ("U".equals(chart.getChartType())) {
                        countStatistic.setCenterLine(
                                        new BigDecimal(df.format(unqualifiedQuantitySum / sampleValueCountSum)));// 中心线
                        countStatistic.setUpperControlLimit(new BigDecimal(
                                        df.format(unqualifiedPercentAvg + 3 * Math.sqrt((unqualifiedPercentAvg
                                                        / countStatistic.getSampleValueCount().doubleValue())))));// 控制上限
                        countStatistic.setLowerControlLimit(new BigDecimal(
                                        df.format(unqualifiedPercentAvg - 3 * Math.sqrt((unqualifiedPercentAvg
                                                        / countStatistic.getSampleValueCount().doubleValue())))));// 控制下限
                    } else {
                        throw new CommonException("该" + entity.getEntityCode() + "对应的控制图类型不是计数型!");
                    }
                } else {
                    throw new CommonException("请维护该" + chart.getChartCode() + "的控制限类型!");
                }
            }
        }
        return countStatistic;
	}

	/**
	 * 判异规则集合转换为判异规则对象（36个判异规则属性）
	 *
	 * @param tenantId
	 * @param siteId
	 * @param chartId
	 * @return
	 */
	private JudementVO initJudementVo(Long tenantId, Long siteId, Long chartId) {
		// 获取判异规则
        List<JudgementR> judgementList = judgementRepository.listCountJudegement(tenantId, siteId, chartId);
        JudementVO judementVo = new JudementVO();
        String judgementShortCode = judgementList.stream().map(JudgementR::getJudgementShortCode).distinct().collect(Collectors.joining(","));
        judementVo.setJudgementShortCode(judgementShortCode);
        judementVo.setJudgementSize(judgementList.size());// 判异规则数量

		// hap get lov values
		/*
		 * Plant plant = new Plant(); List<Plant> lovs = new ArrayList<Plant>(); lovs =
		 * (List<Plant>) lovService.selectDatas(null, "LOV_PLANT", plant, 0, 0); for
		 * (Plant plant1 : lovs) { System.out.println(plant1);
		 * System.out.println(plant1.getPlantCode());
		 * System.out.println(plant1.getDescriptions()); }
		 */
		List<CodeValue> messageTypeDescList = new ArrayList<CodeValue>();
		List<CodeValue> judgementDescList = new ArrayList<CodeValue>();
		List<CodeValue> elementThemeList = new ArrayList<CodeValue>();
		List<CodeValue> elementContentList = new ArrayList<CodeValue>();

		if (!judgementList.isEmpty()) { //modified 20190902
			// 消息类型：MESSAGE_TYPE_1:SPC违规等级1、MESSAGE_TYPE_2:SPC违规等级2、MESSAGE_TYPE_3:SPC违规等级3、MESSAGE_TYPE_4:SPC违规等级4、MESSAGE_TYPE_5:SPC违规等级5
			// messageTypeDescList = lovFeignClient.queryLovValue("PSPC.MESSAGE.TYPE", tenantId);
			
			//modified 20191012
			MessageTypeR messageType = new MessageTypeR();
            messageType.setTenantId(tenantId);
            messageType.setSiteId(siteId);
            messageType.setMessageTypeStatus("Y");
            List<MessageTypeR> messageTypeList = messageTypeRepository.select(null, messageType, 0, 0);
            if (!CollectionUtils.isEmpty(messageTypeList)) {
                for(MessageTypeR messageTypeIn:messageTypeList){
                	CodeValue codeValueDTO = new CodeValue();
                	codeValueDTO.setValue(messageTypeIn.getMessageTypeCode());
                	codeValueDTO.setMeaning(messageType.getDescription());
                    messageTypeDescList.add(codeValueDTO);
                }

            }
           
//          原hzero逻辑：20191016
//          judgementDescList = judgementBasisRepository.selectAsLovValue(tenantId, siteId);
//			elementThemeList = messageElementRepository.selectAsLovValue(tenantId, siteId, "THEME");
//			elementContentList = messageElementRepository.selectAsLovValue(tenantId, siteId, "CONTENT");
            
			//messageTypeDescList = getCodeValuesMapper.getCodeValues("PSPC.MESSAGE.TYPE");  modified 20190902

			// 判异准则：
			// judgementDescList = lovFeignClient.queryLovValue("PSPC.JUDGEMENT", tenantId);
			judgementDescList = getCodeValuesMapper.getCodeValues("PSPC.JUDGEMENT");

			// 消息主题元素：
			// elementThemeList = lovFeignClient.queryLovValue("PSPC.MESSAGE.THEME.ELEMENT",
			// tenantId);
			elementThemeList = getCodeValuesMapper.getCodeValues("PSPC.MESSAGE.THEME.ELEMENT");

			// 消息正文元素：
			// elementContentList =
			// lovFeignClient.queryLovValue("PSPC.MESSAGE.CONTENT.ELEMENT", tenantId);
			elementContentList = getCodeValuesMapper.getCodeValues("PSPC.MESSAGE.CONTENT.ELEMENT");
		}

		for (JudgementR judgement : judgementList) {
            // 消息类型描述处理
            judgement.setMessageTypeDesc(messageTypeDescList.stream()
                            .filter(lovValueDTO -> judgement.getMessageTypeCode().equals(lovValueDTO.getValue()))
                            .collect(Collectors.toList()).get(0).getMeaning());

            // 判异规则描述处理
            judgement.setJudgementCodeDesc(judgementDescList.stream()
                            .filter(lovValueDTO -> judgement.getJudgementCode().equals(lovValueDTO.getValue()))
                            .collect(Collectors.toList()).get(0).getMeaning()
                            .replace("#", judgement.getLengthData().toString())
                            .replace("$", judgement.getLimitData().toString()));

            if (judgement.getMessageTypeDetailList() != null) {
                for (MessageTypeDetailR messageTypeDetail : judgement.getMessageTypeDetailList()) {
                    // 元素类别
                    if ("THEME".equals(messageTypeDetail.getElementCategory())) {
                        messageTypeDetail.setElementDescription(elementThemeList.stream()
                                        .filter(lovValueDTO -> messageTypeDetail.getElementCode()
                                                        .equals(lovValueDTO.getValue()))
                                        .collect(Collectors.toList()).get(0).getMeaning());
                    } else if ("CONTENT".equals(messageTypeDetail.getElementCategory())) {
                        messageTypeDetail.setElementDescription(elementContentList.stream()
                                        .filter(lovValueDTO -> messageTypeDetail.getElementCode()
                                                        .equals(lovValueDTO.getValue()))
                                        .collect(Collectors.toList()).get(0).getMeaning());
                    }
                }
            }

            switch (judgement.getJudgementCode() + "-" + judgement.getChartDetailType()) {
                case "RULE1-M":
                    judementVo.setMainRule1(judgement);
                    break;
                case "RULE2-M":
                    judementVo.setMainRule2(judgement);
                    break;
                case "RULE3-M":
                    judementVo.setMainRule3(judgement);
                    break;
                case "RULE4-M":
                    judementVo.setMainRule4(judgement);
                    break;
                case "RULE5-M":
                    judementVo.setMainRule5(judgement);
                    break;
                case "RULE6-M":
                    judementVo.setMainRule6(judgement);
                    break;
                case "RULE7-M":
                    judementVo.setMainRule7(judgement);
                    break;
                case "RULE8-M":
                    judementVo.setMainRule8(judgement);
                    break;
                case "RULE9-M":
                    judementVo.setMainRule9(judgement);
                    break;
                case "RULE10-M":
                    judementVo.setMainRule10(judgement);
                    break;
                case "RULE11-M":
                    judementVo.setMainRule11(judgement);
                    break;
                case "RULE12-M":
                    judementVo.setMainRule12(judgement);
                    break;
                case "RULE13-M":
                    judementVo.setMainRule13(judgement);
                    break;
                case "RULE14-M":
                    judementVo.setMainRule14(judgement);
                    break;
                case "RULE15-M":
                    judementVo.setMainRule15(judgement);
                    break;
                case "RULE16-M":
                    judementVo.setMainRule16(judgement);
                    break;
                case "RULE17-M":
                    judementVo.setMainRule17(judgement);
                    break;
                case "RULE18-M":
                    judementVo.setMainRule18(judgement);
                    break;
                default:
                    break;
            }
        }

		return judementVo;
	}

	/**
	 * OOC判异
	 *
	 * @param entity             实体控制图
	 * @param countStatisticList
	 * @param judementVo         判异规则
	 * @param existsCountOocList
	 * @param chart
	 * @param groupCode          实体控制图最小组号作为Messge组号
	 * @return
	 */
	private CountOocVO oocCalculate(EntityR entity, List<CountStatisticR> countStatisticList, JudementVO judementVo,
            List<CountOocR> existsCountOocList, ChartR chart, String groupCode) {
		CountOocVO oocVO = new CountOocVO();
        List<CountOocR> oocList = new ArrayList<>();
        List<MessageR> messageList = new ArrayList<>();
        List<MessageDetailR> messageDetaillist = new ArrayList<>();
        List<MessageUploadRelR> messageUploadRellist = new ArrayList<>();

        CountStatisticR firstCountStatistic = countStatisticList.get(0);
        CountStatisticR lastCountStatistic = countStatisticList.get(countStatisticList.size() - 1);

        // 实例化参数对象
        JudementParaCountVO paraVo = new JudementParaCountVO();

        int rowNum = 0;
        Double deltaValue = 0D;
        for (CountStatisticR countStatistic : countStatisticList) {
            rowNum++;// 行数

            // *****************主图判异*************************

            // 排除没值和没整体计算的点参与计算 （单值实体控制第一个点）
            if (!ObjectUtils.isEmpty(countStatistic.getMainStatisticValue())) {

                // 规则1 规格上限上方的#个点 Xi大于USL,点必须在规格上限上方 最大绘点数范围内的点全部重新计算OOC
                if (!ObjectUtils.isEmpty(judementVo.getMainRule1())
                                && !ObjectUtils.isEmpty(countStatistic.getUpperSpecLimit())
                                && countStatistic.getMainStatisticValue().doubleValue() > countStatistic
                                                .getUpperSpecLimit().doubleValue()) {
                    paraVo.setMainCountRule1(paraVo.getMainCountRule1() + 1);
                    if (paraVo.getMainCountRule1() >= judementVo.getMainRule1().getLengthData()) {
                        String oocKey = countStatistic.getCountSampleDataId()
                                        + judementVo.getMainRule1().getChartDetailType()
                                        + judementVo.getMainRule1().getJudgementId();
                        if (existsCountOocList.stream()
                                        .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                        + ooc.getJudgementId()).equals(oocKey))
                                        .collect(Collectors.toList()).size() <= 0) {
                            // 当前点违反规则
                            CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule1(),
                                            firstCountStatistic, lastCountStatistic, entity, groupCode);
                            oocList.add(ooc);

                            CountOocR ooc1 = new CountOocR();
                            ooc1.setCountOocId(ooc.getCountOocId());
                            ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                            ooc1.setChartDetailType(ooc.getChartDetailType());
                            ooc1.setJudgementId(ooc.getJudgementId());
                            existsCountOocList.add(ooc1);

                            if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                messageList.add(ooc.getMessage());
                            }
                            if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                messageDetaillist.addAll(ooc.getMessageDetailList());
                            }
                            if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                            }
                        }
                    }
                }
                // 规则2 规格下限下方的#个点 Xi小于LSL,点必须在规格下限下方 最大绘点数范围内的点全部重新计算OOC
                if (!ObjectUtils.isEmpty(judementVo.getMainRule2())
                                && !ObjectUtils.isEmpty(countStatistic.getLowerSpecLimit())
                                && countStatistic.getMainStatisticValue().doubleValue() < countStatistic
                                                .getLowerSpecLimit().doubleValue()) {
                    paraVo.setMainCountRule2(paraVo.getMainCountRule2() + 1);
                    if (paraVo.getMainCountRule2() >= judementVo.getMainRule2().getLengthData()) {
                        String oocKey = countStatistic.getCountSampleDataId()
                                        + judementVo.getMainRule2().getChartDetailType()
                                        + judementVo.getMainRule2().getJudgementId();
                        if (existsCountOocList.stream()
                                        .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                        + ooc.getJudgementId()).equals(oocKey))
                                        .collect(Collectors.toList()).size() <= 0) {
                            CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule2(),
                                            firstCountStatistic, lastCountStatistic, entity, groupCode);
                            oocList.add(ooc);

                            CountOocR ooc1 = new CountOocR();
                            ooc1.setCountOocId(ooc.getCountOocId());
                            ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                            ooc1.setChartDetailType(ooc.getChartDetailType());
                            ooc1.setJudgementId(ooc.getJudgementId());
                            existsCountOocList.add(ooc1);

                            if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                messageList.add(ooc.getMessage());
                            }
                            if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                messageDetaillist.addAll(ooc.getMessageDetailList());
                            }
                            if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                            }
                        }
                    }
                }
                // 规则3 A区上方的#个点 Xi大于UCL,点必须在a区上方 最大绘点数范围内的点全部重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule3()) && countStatistic.getMainStatisticValue()
                                .doubleValue() > countStatistic.getUpperControlLimit().doubleValue()) {
                    paraVo.setMainCountRule3(paraVo.getMainCountRule3() + 1);
                    if (paraVo.getMainCountRule3() >= judementVo.getMainRule3().getLengthData()) {
                        String oocKey = countStatistic.getCountSampleDataId()
                                        + judementVo.getMainRule3().getChartDetailType()
                                        + judementVo.getMainRule3().getJudgementId();
                        if (existsCountOocList.stream()
                                        .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                        + ooc.getJudgementId()).equals(oocKey))
                                        .collect(Collectors.toList()).size() <= 0) {
                            CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule3(),
                                            firstCountStatistic, lastCountStatistic, entity, groupCode);
                            oocList.add(ooc);

                            CountOocR ooc1 = new CountOocR();
                            ooc1.setCountOocId(ooc.getCountOocId());
                            ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                            ooc1.setChartDetailType(ooc.getChartDetailType());
                            ooc1.setJudgementId(ooc.getJudgementId());
                            existsCountOocList.add(ooc1);

                            if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                messageList.add(ooc.getMessage());
                            }
                            if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                messageDetaillist.addAll(ooc.getMessageDetailList());
                            }
                            if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                            }
                        }
                    }
                }
                // 规则4 A区下方的#个点 Xi小于LCL,点必须在a区下方 最大绘点数范围内的点全部重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule4()) && countStatistic.getMainStatisticValue()
                                .doubleValue() < countStatistic.getLowerControlLimit().doubleValue()) {
                    paraVo.setMainCountRule4(paraVo.getMainCountRule4() + 1);
                    if (paraVo.getMainCountRule4() >= judementVo.getMainRule4().getLengthData()) {
                        String oocKey = countStatistic.getCountSampleDataId()
                                        + judementVo.getMainRule4().getChartDetailType()
                                        + judementVo.getMainRule4().getJudgementId();
                        if (existsCountOocList.stream()
                                        .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                        + ooc.getJudgementId()).equals(oocKey))
                                        .collect(Collectors.toList()).size() <= 0) {
                            CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule4(),
                                            firstCountStatistic, lastCountStatistic, entity, groupCode);
                            oocList.add(ooc);

                            CountOocR ooc1 = new CountOocR();
                            ooc1.setCountOocId(ooc.getCountOocId());
                            ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                            ooc1.setChartDetailType(ooc.getChartDetailType());
                            ooc1.setJudgementId(ooc.getJudgementId());
                            existsCountOocList.add(ooc1);

                            if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                messageList.add(ooc.getMessage());
                            }
                            if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                messageDetaillist.addAll(ooc.getMessageDetailList());
                            }
                            if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                            }
                        }
                    }
                }
                // 规则5 上部区域A中或上方的#个连续点 Xi大于等于（CL+2σ） 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule5())) {
                    if (countStatistic.getMainStatisticValue().doubleValue() >= countStatistic.getCenterLine()
                                    .doubleValue()
                                    + 2 * ((countStatistic.getUpperControlLimit().doubleValue()
                                                    - countStatistic.getLowerControlLimit().doubleValue()) / 6)) {
                        paraVo.setMainCountRule5(paraVo.getMainCountRule5() + 1);
                        if (paraVo.getMainCountRule5() >= judementVo.getMainRule5().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule5().getChartDetailType()
                                            + judementVo.getMainRule5().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule5(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    } else {
                        paraVo.setMainCountRule5(0);
                    }
                }
                // 规则6 上部区域B中或上方的#个连续点 Xi大于等于（CL+σ） 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule6())) {
                    if (countStatistic.getMainStatisticValue().doubleValue() >= countStatistic.getCenterLine()
                                    .doubleValue()
                                    + ((countStatistic.getUpperControlLimit().doubleValue()
                                                    - countStatistic.getLowerControlLimit().doubleValue()) / 6)) {
                        paraVo.setMainCountRule6(paraVo.getMainCountRule6() + 1);
                        if (paraVo.getMainCountRule6() >= judementVo.getMainRule6().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule6().getChartDetailType()
                                            + judementVo.getMainRule6().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule6(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    } else {
                        paraVo.setMainCountRule6(0);
                    }
                }

                // 规则7 上部区域C中或上方的#个连续点 Xi大于等于CL 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule7())) {
                    if (countStatistic.getMainStatisticValue().doubleValue() >= countStatistic.getCenterLine()
                                    .doubleValue()) {
                        paraVo.setMainCountRule7(paraVo.getMainCountRule7() + 1);
                        if (paraVo.getMainCountRule7() >= judementVo.getMainRule7().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule7().getChartDetailType()
                                            + judementVo.getMainRule7().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule7(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    } else {
                        paraVo.setMainCountRule7(0);
                    }
                }

                // 规则8 下部区域C中或下方的#个连续点 Xi小于等于CL 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule8())) {
                    if (countStatistic.getMainStatisticValue().doubleValue() <= countStatistic.getCenterLine()
                                    .doubleValue()) {
                        paraVo.setMainCountRule8(paraVo.getMainCountRule8() + 1);
                        if (paraVo.getMainCountRule8() >= judementVo.getMainRule8().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule8().getChartDetailType()
                                            + judementVo.getMainRule8().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule8(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    } else {
                        paraVo.setMainCountRule8(0);
                    }
                }

                // 规则9 下部区域B中或下方的#个连续点 Xi小于等于（CL-σ） 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule9())) {
                    if (countStatistic.getMainStatisticValue().doubleValue() <= countStatistic.getCenterLine()
                                    .doubleValue()
                                    - ((countStatistic.getUpperControlLimit().doubleValue()
                                                    - countStatistic.getLowerControlLimit().doubleValue()) / 6)) {
                        paraVo.setMainCountRule9(paraVo.getMainCountRule9() + 1);
                        if (paraVo.getMainCountRule9() >= judementVo.getMainRule9().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule9().getChartDetailType()
                                            + judementVo.getMainRule9().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule9(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    } else {
                        paraVo.setMainCountRule9(0);
                    }
                }

                // 规则10 下部区域A中或下方的#个连续点 Xi小于等于（CL-2σ） 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule10())) {
                    if (countStatistic.getMainStatisticValue().doubleValue() <= countStatistic.getCenterLine()
                                    .doubleValue()
                                    - 2 * ((countStatistic.getUpperControlLimit().doubleValue()
                                                    - countStatistic.getLowerControlLimit().doubleValue()) / 6)) {
                        paraVo.setMainCountRule10(paraVo.getMainCountRule10() + 1);
                        if (paraVo.getMainCountRule10() >= judementVo.getMainRule10().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule10().getChartDetailType()
                                            + judementVo.getMainRule10().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule10(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    } else {
                        paraVo.setMainCountRule10(0);
                    }
                }

                // 规则11 连续#个点递增或递减 连续递增：Xi-Xi-1>0 ; 连续递减：Xi-Xi-1<0
                if (!ObjectUtils.isEmpty(judementVo.getMainRule11())) {
                    if (rowNum == 1) {
                        paraVo.setMainCountRule11A(1);
                        paraVo.setMainCountRule11B(1);
                    } else {
                        // 递增判断
                        if (countStatisticList.get(rowNum - 1).getMainStatisticValue().doubleValue()
                                        - countStatisticList.get(rowNum - 2).getMainStatisticValue()
                                                        .doubleValue() > 0) {
                            // if
                            // (countStatisticList.get(rowNum).getMainStatisticValue().doubleValue()
                            // - countStatisticList.get(rowNum -
                            // 1).getMainStatisticValue().doubleValue() > 0) {
                            paraVo.setMainCountRule11A(paraVo.getMainCountRule11A() + 1);
                        } else {
                            paraVo.setMainCountRule11A(1);
                        }
                        // 递减判断
                        if (countStatisticList.get(rowNum - 1).getMainStatisticValue().doubleValue()
                                        - countStatisticList.get(rowNum - 2).getMainStatisticValue()
                                                        .doubleValue() < 0) {
                            // if
                            // (countStatisticList.get(rowNum).getMainStatisticValue().doubleValue()
                            // - countStatisticList.get(rowNum -
                            // 1).getMainStatisticValue().doubleValue() < 0) {
                            paraVo.setMainCountRule11B(paraVo.getMainCountRule11B() + 1);
                        } else {
                            paraVo.setMainCountRule11B(1);
                        }

                        if (paraVo.getMainCountRule11A() >= (judementVo.getMainRule11().getLengthData() + 1) || paraVo
                                        .getMainCountRule11B() >= (judementVo.getMainRule11().getLengthData() + 1)) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule11().getChartDetailType()
                                            + judementVo.getMainRule11().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule11(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    }
                }

                // 规则12 连续#个点中相邻点上下交替
                if (!ObjectUtils.isEmpty(judementVo.getMainRule12())) {
                    if (rowNum == 1) {
                        paraVo.setMainCountRule12(1);
                        paraVo.setMainFlagRule12("");
                    } else {
                        deltaValue = countStatisticList.get(rowNum - 1).getMainStatisticValue()
                                        - countStatisticList.get(rowNum - 2).getMainStatisticValue();

                        if (StringUtils.isEmpty(paraVo.getMainFlagRule12())) {
                            // 上一个点位第一个点 上一个符号未定
                            if (deltaValue != 0) {
                                paraVo.setMainCountRule12(paraVo.getMainCountRule12() + 1);
                                if (deltaValue > 0) {
                                    paraVo.setMainFlagRule12("+");
                                } else if (deltaValue < 0) {
                                    paraVo.setMainFlagRule12("-");
                                }
                            } else {
                                paraVo.setMainCountRule12(1);
                                paraVo.setMainFlagRule12("");
                            }
                        } else if (deltaValue == 0D) {
                            // 当前点和上一个点值相等
                            paraVo.setMainCountRule12(1);
                            paraVo.setMainFlagRule12("");
                        } else if (deltaValue > 0) {
                            // 当前点比上一个点大
                            if ("-".equals(paraVo.getMainFlagRule12())) {
                                paraVo.setMainCountRule12(paraVo.getMainCountRule12() + 1);
                            } else {
                                paraVo.setMainCountRule12(2);
                            }
                            paraVo.setMainFlagRule12("+");
                        } else if (deltaValue < 0) {
                            // 当前点比上一个点小
                            if ("+".equals(paraVo.getMainFlagRule12())) {
                                paraVo.setMainCountRule12(paraVo.getMainCountRule12() + 1);
                            } else {
                                paraVo.setMainCountRule12(2);
                            }
                            paraVo.setMainFlagRule12("-");
                        }

                        if (paraVo.getMainCountRule12() >= judementVo.getMainRule12().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule12().getChartDetailType()
                                            + judementVo.getMainRule12().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule12(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    }
                }

                // 规则13 连续#个点中有$个点落在上部区域C以外 Xi>CL+σ时，判断前面的#个点中大于CL+σ的个数，个数大于等于
                // 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule13())) {
                    if (paraVo.getMainListRule13().size() >= judementVo.getMainRule13().getLengthData()) {
                        paraVo.getMainListRule13().remove(0);
                    }
                    paraVo.getMainListRule13().add(countStatistic);
                    if (paraVo.getMainListRule13().size() >= judementVo.getMainRule13().getLengthData()) {
                        Double value = countStatistic.getCenterLine().doubleValue()
                                        + ((countStatistic.getUpperControlLimit().doubleValue()
                                                        - countStatistic.getLowerControlLimit().doubleValue()) / 6);
                        if ((paraVo.getMainListRule13().stream()
                                        .filter(sampleSubgroup1 -> sampleSubgroup1.getMainStatisticValue() > value)
                                        .collect(Collectors.toList())).size() >= judementVo.getMainRule13()
                                                        .getLimitData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule13().getChartDetailType()
                                            + judementVo.getMainRule13().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule13(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    }
                }

                // 规则14 连续#个点中有$个点落在下部区域C以外 Xi<CL-σ时，判断前面的#个点中小于CL-σ的个数，个数大于等于$
                // 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule14())) {
                    if (paraVo.getMainListRule14().size() >= judementVo.getMainRule14().getLengthData()) {
                        paraVo.getMainListRule14().remove(0);
                    }
                    paraVo.getMainListRule14().add(countStatistic);
                    if (paraVo.getMainListRule14().size() >= judementVo.getMainRule14().getLengthData()) {
                        Double value = countStatistic.getCenterLine().doubleValue()
                                        - ((countStatistic.getUpperControlLimit().doubleValue()
                                                        - countStatistic.getLowerControlLimit().doubleValue()) / 6);
                        if ((paraVo.getMainListRule14().stream()
                                        .filter(sampleSubgroup1 -> sampleSubgroup1.getMainStatisticValue() < value)
                                        .collect(Collectors.toList())).size() >= judementVo.getMainRule14()
                                                        .getLimitData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule14().getChartDetailType()
                                            + judementVo.getMainRule14().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule14(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    }
                }

                // 规则15 连续#个点中有$个点落在上部区域B以外
                // Xi>CL+2σ时，判断前面的#个点中大于CL+2σ的个数，个数大于等于$ 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule15())) {
                    if (paraVo.getMainListRule15().size() >= judementVo.getMainRule15().getLengthData()) {
                        paraVo.getMainListRule15().remove(0);
                    }
                    paraVo.getMainListRule15().add(countStatistic);
                    if (paraVo.getMainListRule15().size() >= judementVo.getMainRule15().getLengthData()) {
                        Double value = countStatistic.getCenterLine().doubleValue()
                                        + 2 * ((countStatistic.getUpperControlLimit().doubleValue()
                                                        - countStatistic.getLowerControlLimit().doubleValue()) / 6);
                        if ((paraVo.getMainListRule15().stream()
                                        .filter(sampleSubgroup1 -> sampleSubgroup1.getMainStatisticValue() > value)
                                        .collect(Collectors.toList())).size() >= judementVo.getMainRule15()
                                                        .getLimitData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule15().getChartDetailType()
                                            + judementVo.getMainRule15().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule15(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    }
                }

                // 规则16 连续#个点中有$个点落在下部区域B以外
                // Xi<CL-2σ时，判断前面的#个点中小于CL-2σ的个数，个数大于等于$ 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule16())) {
                    if (paraVo.getMainListRule16().size() >= judementVo.getMainRule16().getLengthData()) {
                        paraVo.getMainListRule16().remove(0);
                    }
                    paraVo.getMainListRule16().add(countStatistic);
                    if (paraVo.getMainListRule16().size() >= judementVo.getMainRule16().getLengthData()) {
                        Double value = countStatistic.getCenterLine().doubleValue()
                                        - 2 * ((countStatistic.getUpperControlLimit().doubleValue()
                                                        - countStatistic.getLowerControlLimit().doubleValue()) / 6);
                        if ((paraVo.getMainListRule16().stream()
                                        .filter(sampleSubgroup1 -> sampleSubgroup1.getMainStatisticValue() < value)
                                        .collect(Collectors.toList())).size() >= judementVo.getMainRule16()
                                                        .getLimitData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule16().getChartDetailType()
                                            + judementVo.getMainRule16().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule16(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    }
                }

                // 规则17 连续#个点在C区中心线上下 CL-σ<Xi<CL+σ 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule17())) {
                    if (countStatistic.getMainStatisticValue().doubleValue() < countStatistic.getCenterLine()
                                    .doubleValue()
                                    + ((countStatistic.getUpperControlLimit().doubleValue()
                                                    - countStatistic.getLowerControlLimit().doubleValue()) / 6)
                                    && countStatistic.getMainStatisticValue().doubleValue() > countStatistic
                                                    .getCenterLine().doubleValue()
                                                    - ((countStatistic.getUpperControlLimit().doubleValue()
                                                                    - countStatistic.getLowerControlLimit()
                                                                                    .doubleValue())
                                                                    / 6)) {
                        paraVo.setMainCountRule17(paraVo.getMainCountRule17() + 1);
                        if (paraVo.getMainCountRule17() >= judementVo.getMainRule17().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule17().getChartDetailType()
                                            + judementVo.getMainRule17().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule17(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    } else {
                        paraVo.setMainCountRule17(0);
                    }
                }

                // 规则18 连续#个点落在中心线两侧且无一在C区内 Xi>CL+σ或Xi<CL-σ 最大绘点数范围内的点重新计算
                if (!ObjectUtils.isEmpty(judementVo.getMainRule18())) {
                    if (countStatistic.getMainStatisticValue().doubleValue() > countStatistic.getCenterLine()
                                    .doubleValue()
                                    + ((countStatistic.getUpperControlLimit().doubleValue()
                                                    - countStatistic.getLowerControlLimit().doubleValue()) / 6)
                                    || countStatistic.getMainStatisticValue().doubleValue() < countStatistic
                                                    .getCenterLine().doubleValue()
                                                    - ((countStatistic.getUpperControlLimit().doubleValue()
                                                                    - countStatistic.getLowerControlLimit()
                                                                                    .doubleValue())
                                                                    / 6)) {
                        paraVo.setMainCountRule18(paraVo.getMainCountRule18() + 1);
                        if (paraVo.getMainCountRule18() >= judementVo.getMainRule18().getLengthData()) {
                            String oocKey = countStatistic.getCountSampleDataId()
                                            + judementVo.getMainRule18().getChartDetailType()
                                            + judementVo.getMainRule18().getJudgementId();
                            if (existsCountOocList.stream()
                                            .filter(ooc -> (ooc.getCountSampleDataId() + ooc.getChartDetailType()
                                                            + ooc.getJudgementId()).equals(oocKey))
                                            .collect(Collectors.toList()).size() <= 0) {
                                CountOocR ooc = initOoc(countStatistic, chart, judementVo.getMainRule18(),
                                                firstCountStatistic, lastCountStatistic, entity, groupCode);
                                oocList.add(ooc);

                                CountOocR ooc1 = new CountOocR();
                                ooc1.setCountOocId(ooc.getCountOocId());
                                ooc1.setCountSampleDataId(ooc.getCountSampleDataId());
                                ooc1.setChartDetailType(ooc.getChartDetailType());
                                ooc1.setJudgementId(ooc.getJudgementId());
                                existsCountOocList.add(ooc1);

                                if (!ObjectUtils.isEmpty(ooc.getMessage())) {
                                    messageList.add(ooc.getMessage());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageDetailList())) {
                                    messageDetaillist.addAll(ooc.getMessageDetailList());
                                }
                                if (!ObjectUtils.isEmpty(ooc.getMessageUploadRelList())) {
                                    messageUploadRellist.addAll(ooc.getMessageUploadRelList());
                                }
                            }
                        }
                    } else {
                        paraVo.setMainCountRule18(0);
                    }
                }

            }
        }

        oocVO.setCountOocList(oocList);
        oocVO.setMessageList(messageList);
        oocVO.setMessageDetailList(messageDetaillist);
        oocVO.setMessageUploadRelList(messageUploadRellist);

        return oocVO;
	}

	/**
	 * 实例化OOC对象
	 *
	 * @param countStatistic
	 * @param chart
	 * @param judgement
	 * @param firstCountStatistic
	 * @param lastCountStatistic
	 * @param entity
	 * @param groupCode           实体控制图最小组号作为Messge组号
	 * @return
	 */
	private CountOocR initOoc(CountStatisticR countStatistic, ChartR chart, JudgementR judgement,
            CountStatisticR firstCountStatistic, CountStatisticR lastCountStatistic, EntityR entity, String groupCode) {
        CountOocR countOoc = new CountOocR();
        countOoc.setCountOocId(countStatistic.getTenantId() + "," + countStatistic.getSiteId() + ","
                        + countStatistic.getCountSampleDataId() + "," + judgement.getJudgementId() + ","
                        + judgement.getChartDetailType()+","+countStatistic.getEntityCode()+","+countStatistic.getEntityVersion());
        countOoc.setOocStatus("UNPROCESSED");// 状态
        countOoc.setTenantId(countStatistic.getTenantId());
        countOoc.setSiteId(countStatistic.getSiteId());
        countOoc.setCountSampleDataId(countStatistic.getCountSampleDataId());// 样本数据ID
        countOoc.setEntityCode(countStatistic.getEntityCode());// SPC实体控制图
        countOoc.setEntityVersion(countStatistic.getEntityVersion());// 实体控制图版本

        countOoc.setMaxPlotPoints(chart.getMaxPlotPoints());// 绘点数
        countOoc.setTickLabelX(chart.getxTickLabel());// X轴刻度
        // 获取控制图明细
        ChartDetailR chartDetail = new ChartDetailR();
        for (ChartDetailR detail : chart.getChartDetailList()) {
            if (detail.getChartDetailType().equals(judgement.getChartDetailType())) {
                chartDetail = detail;
                break;
            }
        }
        countOoc.setAxisLabelX(chartDetail.getxAxisLabel());// X轴标签
        countOoc.setAxisLabelY(chartDetail.getyAxisLabel());// Y轴标签

        countOoc.setSpecTarget(chartDetail.getSpecTarget());// 目标值
        countOoc.setUpperControlLimit(countStatistic.getUpperControlLimit());// 控制上限
        countOoc.setCenterLine(countStatistic.getCenterLine());// 中心线
        countOoc.setLowerControlLimit(countStatistic.getLowerControlLimit());// 控制下限
        countOoc.setUpperSpecLimit(countStatistic.getUpperSpecLimit());// 规格上线
        countOoc.setLowerSpecLimit(countStatistic.getLowerSpecLimit());// 规格下线

        countOoc.setJudgementId(judgement.getJudgementId());// 规则行ID
        countOoc.setChartDetailType(judgement.getChartDetailType());// 主图/次图
        countOoc.setFirstSubgroupNum(firstCountStatistic.getSubgroupNum());// 开始组号
        countOoc.setLastSubgroupNum(lastCountStatistic.getSubgroupNum());// 结束组号

        // 生成消息
        if (judgement.getMessageTypeDetailList() != null && judgement.getMessageTypeDetailList().size() > 0) {
            MessageR message = new MessageR();
            message.setMessageId(countOoc.getCountOocId());
            message.setTenantId(countOoc.getTenantId());
            message.setSiteId(countOoc.getSiteId());
            message.setOocId(countOoc.getCountOocId());
            message.setEntityCode(countOoc.getEntityCode());// 实体控制图编码
            message.setEntityVersion(countOoc.getEntityVersion());// 实体控制图版本
            message.setGroupCode(groupCode);// 分组(实体控制图为单位)
            message.setMessageStatus("N");// 消息状态

            // 消息命令(只新增关系表pspc_message_upload_rel)
            List<MessageUploadRelR> messageUploadConfigList = new ArrayList<MessageUploadRelR>();
            if (judgement.getMessageUploadConfigList() != null) {
                for (MessageUploadConfigR messageUploadConfig : judgement.getMessageUploadConfigList()) {
                    MessageUploadRelR messageUploadRel = new MessageUploadRelR();
                    messageUploadRel.setUploadConfigId(messageUploadConfig.getUploadConfigId());
                    messageUploadRel.setMessageId(message.getMessageId());
                    messageUploadRel.setTenantId(message.getTenantId());
                    messageUploadRel.setSiteId(message.getSiteId());
                    messageUploadRel.setStatus("N");

                    messageUploadConfigList.add(messageUploadRel);
                }
            }

            List<MessageDetailR> messageDetailList = new ArrayList<MessageDetailR>();
            for (MessageTypeDetailR messageTypeDetail : judgement.getMessageTypeDetailList()) {
                MessageDetailR messageDetail = new MessageDetailR();
                messageDetail.setMessageId(message.getMessageId());
                messageDetail.setTenantId(message.getTenantId());
                messageDetail.setSiteId(message.getSiteId());
                messageDetail.setEntityCode(message.getEntityCode());// 实体控制图编码
                messageDetail.setEntityVersion(message.getEntityVersion());// 实体控制图版本
                messageDetail.setElementCategory(messageTypeDetail.getElementCategory());// 元素类别
                messageDetail.setElementCode(messageTypeDetail.getElementCode());// 元素编码
                messageDetail.setElementDescription(messageTypeDetail.getElementDescription());// 元素描述
                messageDetail.setElementStatus(messageTypeDetail.getElementStatus());// 元素状态(Y/N)

                if ("Y".equals(messageTypeDetail.getElementStatus())) {
                    if ("MESSAGE_TYPE".equals(messageTypeDetail.getElementCode())) {
                        // 消息类型
                        messageDetail.setElementValueCode(judgement.getMessageTypeCode());
                        messageDetail.setElementValueDescription(judgement.getMessageTypeDesc());
                    } else if ("ATTACHMENT_GROUP".equals(messageTypeDetail.getElementCode())) {
                        // 附着对象组
                        messageDetail.setElementValueCode(entity.getAttachmentCodes());
                        messageDetail.setElementValueDescription(entity.getAttchmentDescriptions());
                    } else if ("CE_GROUP".equals(messageTypeDetail.getElementCode())) {
                        // 控制要素组
                        messageDetail.setElementValueCode(entity.getCeGroup());
                        messageDetail.setElementValueDescription(entity.getCeGroupDescription());
                    } else if ("CE_PARAMTER".equals(messageTypeDetail.getElementCode())) {
                        // 控制要素
                        messageDetail.setElementValueCode(entity.getCeParameter());
                        messageDetail.setElementValueDescription(entity.getCeParameterName());
                    } else if ("ENTITY".equals(messageTypeDetail.getElementCode())) {
                        // 实体控制图
                        messageDetail.setElementValueCode(entity.getEntityCode());
                        messageDetail.setElementValueDescription(entity.getDescription());
                    } else if ("CHART".equals(messageTypeDetail.getElementCode())) {
                        // 控制图
                        messageDetail.setElementValueCode(chart.getChartCode());
                        messageDetail.setElementValueDescription(chart.getDescription());
                    } else if ("CHART_TITLE".equals(messageTypeDetail.getElementCode())) {
                        // 控制图标题
                        messageDetail.setElementValueCode(chart.getChartTitle());
                        messageDetail.setElementValueDescription(chart.getChartTitle());
                    } else if ("MAX_PLOT_POINTS".equals(messageTypeDetail.getElementCode())) {
                        // 最大绘点数
                        messageDetail.setElementValueCode(chart.getMaxPlotPoints().toString());
                        messageDetail.setElementValueDescription(chart.getMaxPlotPoints().toString());
                    } else if ("SUBGROUP_SIZE".equals(messageTypeDetail.getElementCode())) {
                        // 子组大小
                        messageDetail.setElementValueCode(chart.getSubgroupSize().toString());
                        messageDetail.setElementValueDescription(chart.getSubgroupSize().toString());
                    } else if ("JUDGEMENT".equals(messageTypeDetail.getElementCode())) {
                        // OOC判异规则
                        messageDetail.setElementValueCode(judgement.getJudgementCode());
                        messageDetail.setElementValueDescription(judgement.getJudgementCodeDesc());
                    } else if ("SAMPLE_DATA".equals(messageTypeDetail.getElementCode())) {
                        // 样本数据
                        messageDetail.setElementValueCode(countStatistic.getSampleDatas());
                        messageDetail.setElementValueDescription(countStatistic.getSampleDatas());
                    } else if ("CONTROL_LIMIT".equals(messageTypeDetail.getElementCode())) {
                        //
                        messageDetail.setElementValueCode("UCL：" + countOoc.getUpperControlLimit() + "，CL："
                                        + countOoc.getCenterLine() + "，LCL：" + countOoc.getLowerControlLimit());
                        messageDetail.setElementValueDescription("控制上限：" + countOoc.getUpperControlLimit() + "，中心线："
                                        + countOoc.getCenterLine() + "，控制下限：" + countOoc.getLowerControlLimit());
                    } else if ("SPEC_LIMIT".equals(messageTypeDetail.getElementCode())) {
                        // 规格限
                        messageDetail.setElementValueCode("USL：" + countOoc.getUpperSpecLimit() + "，ST："
                                        + countOoc.getSpecTarget() + "，LSL：" + countOoc.getLowerSpecLimit());
                        messageDetail.setElementValueDescription("规格上限：" + countOoc.getUpperSpecLimit() + "，目标值："
                                        + countOoc.getSpecTarget() + "，规格下限：" + countOoc.getLowerSpecLimit());
                    } else if ("SAMPLE_START_TIME".equals(messageTypeDetail.getElementCode())) {
                        // 样本开始时间
                        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
                        messageDetail.setElementValueCode(sdf.format(firstCountStatistic.getSampleTime()));
                        messageDetail.setElementValueDescription(sdf.format(firstCountStatistic.getSampleTime()));
                    } else if ("SAMPLE_END_TIME".equals(messageTypeDetail.getElementCode())) {
                        // 样本结束时间
                        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
                        messageDetail.setElementValueCode(sdf.format(lastCountStatistic.getSampleTime()));
                        messageDetail.setElementValueDescription(sdf.format(lastCountStatistic.getSampleTime()));
                    } else if ("SUBGROUP_NUMBER".equals(messageTypeDetail.getElementCode())) {
                        // 样本组号
                        messageDetail.setElementValueCode(countStatistic.getSubgroupNum().toString());
                        messageDetail.setElementValueDescription(countStatistic.getSubgroupNum().toString());
                    } else if ("JUDGEMENT_SHORT_CODE".equals(messageTypeDetail.getElementCode())) {
                        // 编码简称
                        messageDetail.setElementValueCode(judgement.getJudgementShortCode());
                        messageDetail.setElementValueDescription(judgement.getJudgementShortCode());
                    }

                }

                messageDetailList.add(messageDetail);
            }
            message.setMessageDetailList(messageDetailList);
            countOoc.setMessage(message);
            countOoc.setMessageDetailList(messageDetailList);
            countOoc.setMessageUploadRelList(messageUploadConfigList);
        }
        return countOoc;
    }
}
