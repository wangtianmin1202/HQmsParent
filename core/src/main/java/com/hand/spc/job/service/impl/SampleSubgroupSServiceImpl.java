package com.hand.spc.job.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.job.service.ISampleSubgroupSService;
import com.hand.spc.repository.dto.AttachmentGroupR;
import com.hand.spc.repository.dto.AttachmentR;
import com.hand.spc.repository.dto.AttachmentType;
import com.hand.spc.repository.dto.ChartDetailR;
import com.hand.spc.repository.dto.ChartR;
import com.hand.spc.repository.dto.CoefficientR;
import com.hand.spc.repository.dto.EntiretyStatisticR;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.JudementParaVO;
import com.hand.spc.repository.dto.JudementVO;
import com.hand.spc.repository.dto.JudgementR;
import com.hand.spc.repository.dto.MessageDetailR;
import com.hand.spc.repository.dto.MessageR;
import com.hand.spc.repository.dto.MessageTypeDetailR;
import com.hand.spc.repository.dto.MessageTypeR;
import com.hand.spc.repository.dto.MessageUploadConfigR;
import com.hand.spc.repository.dto.MessageUploadRelR;
import com.hand.spc.repository.dto.OocR;
import com.hand.spc.repository.dto.OocRequestDTO;
import com.hand.spc.repository.dto.OocVO;
import com.hand.spc.repository.dto.SampleDataR;
import com.hand.spc.repository.dto.SampleDataWaitR;
import com.hand.spc.repository.dto.SampleGroupDataVO;
import com.hand.spc.repository.dto.SampleSubgroupR;
import com.hand.spc.repository.dto.SampleSubgroupRelationR;
import com.hand.spc.repository.dto.SampleSubgroupWaitR;
import com.hand.spc.repository.dto.Sequence;
import com.hand.spc.repository.dto.SubGroupCalcResultVO;
import com.hand.spc.repository.dto.SubgroupStatisticR;
import com.hand.spc.repository.mapper.AttachmentTypeMapper;
import com.hand.spc.repository.mapper.CoefficientRMapper;
import com.hand.spc.repository.mapper.EntiretyStatisticRMapper;
import com.hand.spc.repository.mapper.GetCodeValuesMapper;
import com.hand.spc.repository.mapper.MessageDetailRMapper;
import com.hand.spc.repository.mapper.MessageRMapper;
import com.hand.spc.repository.mapper.MessageUploadRelRMapper;
import com.hand.spc.repository.mapper.OocRMapper;
import com.hand.spc.repository.mapper.SampleSubgroupRMapper;
import com.hand.spc.repository.mapper.SampleSubgroupRelationRMapper;
import com.hand.spc.repository.mapper.SampleSubgroupWaitRMapper;
import com.hand.spc.repository.mapper.SequenceMapper;
import com.hand.spc.repository.mapper.SubgroupStatisticRMapper;
import com.hand.spc.repository.service.IAttachmentRService;
import com.hand.spc.repository.service.IChartRService;
import com.hand.spc.repository.service.ICoefficientRService;
import com.hand.spc.repository.service.IEntiretyStatisticRService;
import com.hand.spc.repository.service.IErrorMessageRService;
import com.hand.spc.repository.service.IJudgementRService;
import com.hand.spc.repository.service.IMessageDetailRService;
import com.hand.spc.repository.service.IMessageRService;
import com.hand.spc.repository.service.IMessageTypeRService;
import com.hand.spc.repository.service.IMessageUploadRelRService;
import com.hand.spc.repository.service.IOocRService;
import com.hand.spc.repository.service.ISampleDataRService;
import com.hand.spc.repository.service.ISampleDataWaitRService;
import com.hand.spc.repository.service.ISampleSubgroupRService;
import com.hand.spc.repository.service.ISampleSubgroupRelationRService;
import com.hand.spc.repository.service.ISampleSubgroupWaitRService;
import com.hand.spc.repository.service.ISequenceRService;
import com.hand.spc.repository.service.ISubgroupStatisticRService;
import com.hand.spc.utils.CommonException;
import com.hand.spc.utils.MtException;
import com.hand.spc.utils.MtSqlHelper;
import com.hand.spc.utils.RedisSingle;
import com.hand.spc.utils.Utils;

//计量型
@Transactional(rollbackFor = Exception.class)
public class SampleSubgroupSServiceImpl extends BaseServiceImpl<EntityR> implements ISampleSubgroupSService {

	private Logger logger = LoggerFactory.getLogger(SampleCalculationSServiceImpl.class);

	@Autowired
	private ISampleSubgroupRService subgroupRepository;
	@Autowired
	private SampleSubgroupRMapper sampleSubgroupRMapper;
	@Autowired
	private ISampleSubgroupRelationRService relationRepository;
	@Autowired
	private SampleSubgroupRelationRMapper sampleSubgroupRelationRMapper;
	@Autowired
	private ISequenceRService sequenceRepository;
	@Autowired
	private ISubgroupStatisticRService statisticRepository;	
	@Autowired
	private SubgroupStatisticRMapper subgroupStatisticRMapper;
	@Autowired
	private ISampleSubgroupWaitRService sampleSubgroupWaitRepositoty;
	@Autowired
	private SampleSubgroupWaitRMapper sampleSubgroupWaitRMapper;
	@Autowired
	private ISampleSubgroupWaitRService sampleSubgroupWaitRepository;
	@Autowired
	private IEntiretyStatisticRService entiretyStatisticRepository;
	@Autowired
	private EntiretyStatisticRMapper entiretyStatisticRMapper;
	@Autowired
	private IJudgementRService judgementRepository;
	@Autowired
	private IOocRService oocRepository;
	@Autowired
	private OocRMapper oocRMapper;
	@Autowired
	private IChartRService chartRepository;
	@Autowired
	private IAttachmentRService attachmentRepository;
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
	private ICoefficientRService coefficientRepository;
	@Autowired
	private ISampleDataRService sampleDataRepository;
	@Autowired
	private ISampleDataWaitRService sampleDataWaitRepository;
	@Autowired
	InterfaceService interfaceService;// 发送接口

	@Autowired
	private CoefficientRMapper coefficientMapper;

	@Autowired
	private SequenceMapper sequenceMapper;

	@Autowired
	private GetCodeValuesMapper getCodeValuesMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate; // modified 20190903
	@Autowired
	private IMessageTypeRService messageTypeRepository; // modified 20190903
	@Autowired
	private IErrorMessageRService errorMessageRepository; // modified 20190903

	@Autowired
	private AttachmentTypeMapper attachmentTypeMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public void sampleDataSubgroupWait(SampleGroupDataVO sampleGroupDataVO, String uuid) {
        int batchSize = 20000;
        long startTime = 0L;

        if (CollectionUtils.isNotEmpty(sampleGroupDataVO.getSampleDataWaitIdList())) {
            /**
             * 获取样本预处理表数据
             */
            startTime = System.currentTimeMillis();
            SampleDataWaitR sampleDataWait = new SampleDataWaitR();
            sampleDataWait.setSampleDataWaitIdList(sampleGroupDataVO.getSampleDataWaitIdList());
            sampleDataWait.setTenantId(sampleGroupDataVO.getTenantId());
            sampleDataWait.setSiteId(sampleGroupDataVO.getSiteId());
            //List<SampleSubgroupWaitR> sampleSubgroupWaitList = sampleSubgroupWaitRepositoty.selectSubgroupWaitData(sampleDataWait); //modified 20191101
            
            String whereInSql = this.GetWhereInValuesSql("sdw.sample_data_wait_id",sampleDataWait.getSampleDataWaitIdList(),800);
            List<SampleSubgroupWaitR> sampleSubgroupWaitList = sampleSubgroupWaitRepositoty.selectSubgroupWaitDataModified(whereInSql);

            logger.info(Utils.getLog(uuid, "获取样本数据:" + sampleSubgroupWaitList.size() + "个," + (System.currentTimeMillis() - startTime) + "ms"));

            startTime = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(sampleSubgroupWaitList)) {
                /**
                 * 保存样本预处理数据
                 */
                List<SampleSubgroupWaitR> tempSampleSubgroupWaitList;
                int sizeClassify = sampleSubgroupWaitList.size();
                int countClassify = sizeClassify / batchSize;
                for (int i = 0; i <= countClassify; i++) {
                    int start = i * batchSize;
                    int end = countClassify > i ? start + batchSize : start + sizeClassify % batchSize;
                    if (start < end) {
                        tempSampleSubgroupWaitList = sampleSubgroupWaitList.subList(start, end);
                        for(int k=0 ;k<tempSampleSubgroupWaitList.size();k++) {
                        	sampleSubgroupWaitRMapper.insertSelective(tempSampleSubgroupWaitList.get(k));
                        }
                        //sampleSubgroupWaitRepositoty.batchInsertSampleSubgroupWait(tempSampleSubgroupWaitList);  modified 20191029
                    }
                }
            }
            logger.info(Utils.getLog(uuid, "The size of sampleSubgroupWaitSqlList is " + sampleSubgroupWaitList.size()
                            + ",time is " + (System.currentTimeMillis() - startTime) + "ms"));

            if (CollectionUtils.isNotEmpty(sampleGroupDataVO.getSampleDataWaitInsList())) {
                /**
                 * 保存样本数据
                 */
                startTime = System.currentTimeMillis();
                final List<SampleDataR> sampleDataList =
                                new ArrayList<>(sampleGroupDataVO.getSampleDataWaitInsList().size());
                sampleGroupDataVO.getSampleDataWaitInsList().stream().forEach(c -> {
                    SampleDataR sampleData = new SampleDataR();
                    BeanUtils.copyProperties(c, sampleData);
                    sampleData.setSampleDataId(c.getSampleDataWaitId());
                    sampleDataList.add(sampleData);
                });

                List<SampleDataR> tempSampleData;
                int sizeSampleData = sampleDataList.size();
                int countSampleData = sizeSampleData / batchSize;
                for (int i = 0; i <= countSampleData; i++) {
                    int start = i * batchSize;
                    int end = countSampleData > i ? start + batchSize : start + sizeSampleData % batchSize;
                    if (start < end) {
                        tempSampleData = sampleDataList.subList(start, end);
                        sampleDataRepository.batchInsertSampleData(tempSampleData);
                    }
                }
                logger.info(Utils.getLog(uuid, "The size of sampleDataList is " + sampleDataList.size() + ",time is "
                                + (System.currentTimeMillis() - startTime) + "ms"));
            }
        }

        if (CollectionUtils.isNotEmpty(sampleGroupDataVO.getSampleDataWaitIdDelList())) {
            startTime = System.currentTimeMillis();
            List<Long> sampleDataWaitIdDelList = sampleGroupDataVO.getSampleDataWaitIdDelList();

            List<Long> tempSampleData;
            int size = sampleDataWaitIdDelList.size();
            int countSampleData = size / batchSize;
            for (int i = 0; i <= countSampleData; i++) {
                int start = i * batchSize;
                int end = countSampleData > i ? start + batchSize : start + size % batchSize;
                if (start < end) {
                    tempSampleData = sampleDataWaitIdDelList.subList(start, end);
                  //sampleDataWaitRepository.batchDeleteSampleDataWaitByIds(tempSampleData);
                    
                    for(int k=0;k<tempSampleData.size();k++) {
                    	SampleDataWaitR sampleDataWait = new SampleDataWaitR();
                    	sampleDataWait.setSampleDataWaitId(tempSampleData.get(k));
                    	sampleDataWaitRepository.deleteByPrimaryKey(sampleDataWait);
                    } 
                    
                }
            }
            logger.info(Utils.getLog(uuid, "The size of sampleDataWaitIdDelList is " + sampleDataWaitIdDelList.size()
                            + ",time is " + (System.currentTimeMillis() - startTime) + "ms"));
        }
    }


	@Override
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public SubGroupCalcResultVO sampleDataSubgroup(EntityR entity, String uuid) {
		/**
         * String subgroupRedisKey = entity.getSubgroupRedisKey(); String oocRedisKey =
         * entity.getOocRedisKey();
         **/
        SubGroupCalcResultVO subGroupCalcResultVO = new SubGroupCalcResultVO();
        int dataSize = entity.getSampleSubgroupWaitList().size();
        
		if (null != entity.getSubgroupSize()) {
			/**
             * 获取序列
             */
            long startTime = System.currentTimeMillis();
            Sequence sequenceQuery = new Sequence(entity.getTenantId(), entity.getSiteId(),
                            "SUBGROUP-" + entity.getEntityCode() + "-" + entity.getEntityVersion(), null);
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
             * 根据实体控制图获取控制图
             */
            startTime = System.currentTimeMillis();
            ChartR chart = chartRepository.queryChartByEntity(entity);
            if (chart == null) {
                throw new CommonException(
                                "找不到该实体控制图下的控制图!实体控制编码:" + entity.getEntityCode() + ",版本:" + entity.getEntityVersion());
            }
            logger.info(Utils.getLog(uuid, "查询控制图,耗时=" + (System.currentTimeMillis() - startTime) + "ms"));

            /**
             * 获取系数(控制图：控制限选项(COEFFICIENT:系数计算)
             */
            CoefficientR coefficient = new CoefficientR();
            if (chart.getChartDetailList().stream()
                            .filter(chartDetail -> chartDetail.getControlLimitUsage().equals("COEFFICIENT"))
                            .collect(Collectors.toList()).size() > 0) {
                coefficient.setSubgroupSize(chart.getSubgroupSize());
                coefficient = coefficientMapper.selectOne(coefficient);
                if (coefficient == null) {
                    logger.error("The subgroup size is {}. Correspondence coefficient does not exist",
                                    chart.getSubgroupSize());
                    throw new MtException("PSPC_ERROR_47",
                                    errorMessageRepository.getErrorMessageWithModule(entity.getTenantId(),
                                                    "PSPC_ERROR_47", "GENERAL", String.valueOf(chart.getSubgroupSize()),
                                                    "【API:sampleDataSubgroup】"));
                }
            }
            
            startTime = System.currentTimeMillis();
			//List<AttachmentType> attachmentTypeList = attachmentTypeMapper.selectAll();// 原hzero方法   modified 20191018
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
//					for (AttachmentType attachmentType : attachmentTypeList) {  // 原hzero方法   modified 20191018
//						if (attachmentType.getAttachmentTypeCode().equals(attachment.getAttachmentType())) {
//							desc = attachmentType.getAttachmentTypeDesc();
//						}
//					}
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


            /**
             * 获取实体控制图对应判异规则
             */
            startTime = System.currentTimeMillis();
            JudementVO judementVo = initJudementVo(entity.getTenantId(), entity.getSiteId(), chart.getChartId());
            logger.info(Utils.getLog(uuid, "查询判异规则，规则数=" + judementVo.getJudgementSize() + ",耗时="
                            + (System.currentTimeMillis() - startTime) + "ms"));

            List<SampleSubgroupR> currentSubgroupDataList = new ArrayList<SampleSubgroupR>();// 最大绘点数范围内子组数据集合
            List<OocR> existsOocList = new ArrayList<OocR>();// 已存在OOC集合

            SampleSubgroupWaitR sampleSubgroupWait = null;// 样本待分组
            List<SampleSubgroupR> sampleSubgroupList = new ArrayList<SampleSubgroupR>();// 分组数据集合
            List<SampleSubgroupRelationR> subgroupRelationList = new ArrayList<SampleSubgroupRelationR>();// 样本分组关系集合
            List<SampleSubgroupWaitR> currentSampleDataList = new ArrayList<SampleSubgroupWaitR>();// 当前分组样本数据集合
            List<Long> deleteIdList = new ArrayList<>();// 已分组完成待分组样本数据ID集合

            SubgroupStatisticR subgroupStatistic = null;// 子组计算数据
            List<SubgroupStatisticR> subgroupStatisticList = new ArrayList<SubgroupStatisticR>();// 子组计算集合
            List<EntiretyStatisticR> entiretyStatisticList = new ArrayList<EntiretyStatisticR>();// 整体计算集合

            List<OocR> oocList = new ArrayList<>();// OOC集合
            List<MessageR> messageList = new ArrayList<MessageR>();// 消息集合
            List<MessageDetailR> messageDetailList = new ArrayList<MessageDetailR>();// 消息明细集合
            List<MessageUploadRelR> messageUploadRelList = new ArrayList<MessageUploadRelR>();// 消息命令集合

			if (sequence.getMaxNum() > 0) {
				/**
				 * // 获取当前实体控制图已分组完成分组数据 优先取redis 取不到再取数据库 String jsonSubgroupData =
				 * redisHelper.strGet(subgroupRedisKey); if
				 * (StringUtils.isEmpty(jsonSubgroupData)) { //
				 * pspc_sample_subgroup(样本数据分组表)、pspc_subgroup_statistic(子组统计指标表) //
				 * chart_type控制图类型(XBAR-R:均值-极差图;XBAR-S:均值-标准差图;Me-R:中位数-极差图;X-Rm:单值-移动极差图;P:不合格率图;nP:不合格品数图;C:不合格数图;U:单位不合格数图;)
				 * // max_plot_points 最大绘点数 currentSubgroupDataList =
				 * subgroupRepository.queryPreSubgroupStatistic(entity.getTenantId(),
				 * entity.getSiteId(), entity.getEntityCode(), entity.getEntityVersion(),
				 * sequence.getMaxNum(), sequence.getMaxNum() - chart.getMaxPlotPoints() + 1,
				 * chart.getChartType());
				 * 
				 * // 查询当前绘点数范围内已存在OOC集合 if (currentSubgroupDataList.size() > 0) { List<Long>
				 * idList = currentSubgroupDataList.stream() .map(sampleSubgroup ->
				 * sampleSubgroup.getSampleSubgroupId()) .collect(Collectors.toList());
				 * OocRequestDTO requestDTO = new OocRequestDTO();
				 * requestDTO.setTenantId(entity.getTenantId());
				 * requestDTO.setSiteId(entity.getSiteId());
				 * requestDTO.setSubgroupIdList(idList); existsOocList =
				 * oocRepository.listOocBySampleSubgroupIdList(requestDTO); } } else {
				 * currentSubgroupDataList = redisHelper.fromJsonList(jsonSubgroupData,
				 * SampleSubgroup.class); String jsonOocData = redisHelper.strGet(oocRedisKey);
				 * if (!StringUtils.isEmpty(jsonOocData)) { existsOocList =
				 * redisHelper.fromJsonList(jsonOocData, Ooc.class); } }
				 * 
				 * if (currentSubgroupDataList.size() > chart.getMaxPlotPoints()) { int more =
				 * currentSubgroupDataList.size() - chart.getMaxPlotPoints().intValue();
				 * Iterator<SampleSubgroup> iterator = currentSubgroupDataList.iterator(); int i
				 * = 0; while (iterator.hasNext()) { if (i >= more) { break; } iterator.next();
				 * iterator.remove(); i++; } }
				 **/

				startTime = System.currentTimeMillis();
                currentSubgroupDataList = subgroupRepository.queryPreSubgroupStatistic(entity.getTenantId(),
                                entity.getSiteId(), entity.getEntityCode(), entity.getEntityVersion(),
                                sequence.getMaxNum(), sequence.getMaxNum() - chart.getMaxPlotPoints() + 1,
                                chart.getChartType());

             // 查询当前绘点数范围内已存在OOC集合
                if (currentSubgroupDataList.size() > 0) {
                    List<Long> idList = currentSubgroupDataList.stream()
                                    .map(sampleSubgroup -> sampleSubgroup.getSampleSubgroupId())
                                    .collect(Collectors.toList());
                    OocRequestDTO requestDTO = new OocRequestDTO();
                    requestDTO.setTenantId(entity.getTenantId());
                    requestDTO.setSiteId(entity.getSiteId());
                    requestDTO.setSubgroupIdList(idList);
                    existsOocList = oocRepository.listOocBySampleSubgroupIdList(requestDTO);
                }

                logger.info(Utils.getLog(uuid, "查询最大绘点数范围的最后子组数据，子组数量=" + currentSubgroupDataList.size() + ",耗时="
                        + (System.currentTimeMillis() - startTime) + "ms"));
                logger.info(Utils.getLog(uuid, "查询已存在OOC，OOC个数=" + existsOocList.size() + ",耗时="
                        + (System.currentTimeMillis() - startTime) + "ms"));
			}

			int oocCount = 0;
            Double max = 0D;// 最大值
            Double min = 0D;// 最小值
            Double sum = 0D;// 合计
            String sampleDatas = "";// 当前分组样本数据拼接字符串 样本数据 + "/" + 样本值
            // 样本结束时间
            SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");

            Double preSubgroupValue = 0D;
            boolean subgroupFlag = true;// 是否新增分组标识

            startTime = System.currentTimeMillis();

			for (int i = 0; i < dataSize; i++) {
				sampleSubgroupWait = entity.getSampleSubgroupWaitList().get(i);// 当前预分组数据
				// 新增标识为Y时 “新增子组” 或 “退出分组”
                if (subgroupFlag) {
                    if (dataSize - i < entity.getSubgroupSize()) {// 子组大小
                        break;
                    } else {
                        // 当前实体控制图组号+1
                        sequence.setMaxNum(sequence.getMaxNum() + 1);
                        // 新增子组数据
                        SampleSubgroupR sampleSubgroup = new SampleSubgroupR();
                        sampleSubgroup.setSampleSubgroupId(sampleSubgroupWait.getSampleSubgroupWaitId());// 分组数据ID
                                                                                                         // =
                                                                                                         // 预分组数据第一个ID
                        sampleSubgroup.setTenantId(entity.getTenantId());
                        sampleSubgroup.setSiteId(entity.getSiteId());
                        sampleSubgroup.setEntityCode(entity.getEntityCode());
                        sampleSubgroup.setEntityVersion((entity.getEntityVersion()));
                        sampleSubgroup.setSubgroupSize(entity.getSubgroupSize());
                        sampleSubgroup.setExistNum(0L);// 已分组数
                        sampleSubgroup.setSampleCalculateStatus("OOC");// 计算状态
                                                                       // 已整体计算
                        sampleSubgroup.setSampleModified("N");// 是否修改
                        sampleSubgroup.setSubgroupNum(sequence.getMaxNum());// 组号
                        // 添加分组数据至集合
                        sampleSubgroupList.add(sampleSubgroup);

                        // 新增状态改为FALSE
                        subgroupFlag = false;

                        // 计算相关变量初始化
                        currentSampleDataList.clear();
                        max = sampleSubgroupWait.getSampleValue().doubleValue();// 样本数据
                        min = sampleSubgroupWait.getSampleValue().doubleValue();
                        sum = 0D;
                        sampleDatas = "";
                    }
                }

                /**
                 * 分组关系数据处理
                 */
                SampleSubgroupRelationR subgroupRelation = new SampleSubgroupRelationR();
                subgroupRelation.setTenantId(entity.getTenantId());
                subgroupRelation.setSiteId(entity.getSiteId());
                subgroupRelation.setEntityCode(entity.getEntityCode());
                subgroupRelation.setEntityVersion(entity.getEntityVersion());
                subgroupRelation.setSampleSubgroupId(
                                sampleSubgroupList.get(sampleSubgroupList.size() - 1).getSampleSubgroupId());// 分组ID
                subgroupRelation.setSampleDataId(sampleSubgroupWait.getSampleDataId());// 样本数据ID
                subgroupRelation.setSampleValue(sampleSubgroupWait.getSampleValue());// 样本值
                subgroupRelationList.add(subgroupRelation);

                /**
                 * 处理当前分组数据 已分配个数 样本时间
                 */
                sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                .setExistNum(sampleSubgroupList.get(sampleSubgroupList.size() - 1).getExistNum() + 1);// 累加样本分组已分组个数
                sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                .setSampleSubgroupTime(sampleSubgroupWait.getSampleTime());// 样本时间

                if (sampleSubgroupWait.getSampleValue().doubleValue() > max.doubleValue()) {
                    max = sampleSubgroupWait.getSampleValue().doubleValue();// 最大样本值
                }
                if (sampleSubgroupWait.getSampleValue().doubleValue() < min.doubleValue()) {
                    min = sampleSubgroupWait.getSampleValue().doubleValue();// 最小样本值
                }
                sum += sampleSubgroupWait.getSampleValue().doubleValue();// 样本值累加
                sampleDatas += sdf.format(sampleSubgroupWait.getSampleTime()) + "/"
                                + sampleSubgroupWait.getSampleValue() + ",";

                currentSampleDataList.add(sampleSubgroupWait);
                deleteIdList.add(sampleSubgroupWait.getSampleSubgroupWaitId());

                // 判断最新一个分组是否完成 完成->修改新增状态为Y 子组计算
                if (sampleSubgroupList.get(sampleSubgroupList.size() - 1).getExistNum().longValue() == entity
                                .getSubgroupSize().longValue()) {
                    subgroupFlag = true;

                    /********** 子组计算处理 ************************/
                    if (currentSubgroupDataList.size() > 0) {
                        preSubgroupValue = currentSubgroupDataList.get(currentSubgroupDataList.size() - 1)
                                        .getSubgroupBar();// 移动极差需要
                    } else {
                        preSubgroupValue = null;
                    }
                    subgroupStatistic = subgroupStatisticCalculate(entity, currentSampleDataList, max, min, sum,
                                    sampleSubgroupList.get(sampleSubgroupList.size() - 1).getSampleSubgroupId(),
                                    preSubgroupValue);
                    subgroupStatisticList.add(subgroupStatistic);

                    // 移动最大绘点数范围内第一个点
                    if (currentSubgroupDataList.size() >= chart.getMaxPlotPoints()) {
                        // 移除最大绘点数范围内第一个点对应ooc
                        long sampleSubgroupId = currentSubgroupDataList.get(0).getSampleSubgroupId();
                        oocCount = existsOocList.size();
                        for (int j = oocCount - 1; j > -0; j--) {
                            if (existsOocList.get(j).getSampleSubgroupId().longValue() == sampleSubgroupId) {
                                existsOocList.remove(j);
                            }
                        }
                        currentSubgroupDataList.remove(0);
                    }

                    // 主要指标 和 次要指标赋值
                    if ("XBAR-R".equals(chart.getChartType())) {
                        sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                        .setMainStatisticValue(subgroupStatistic.getSubgroupBar().doubleValue());
                        sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                        .setSecondStatisticValue(subgroupStatistic.getSubgroupR().doubleValue());
                    } else if ("XBAR-S".equals(chart.getChartType())) {
                        sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                        .setMainStatisticValue(subgroupStatistic.getSubgroupBar().doubleValue());
                        sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                        .setSecondStatisticValue(subgroupStatistic.getSubgroupSigma().doubleValue());
                    } else if ("Me-R".equals(chart.getChartType())) {
                        sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                        .setMainStatisticValue(subgroupStatistic.getSubgroupMe().doubleValue());
                        sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                        .setSecondStatisticValue(subgroupStatistic.getSubgroupR().doubleValue());
                    } else if ("X-Rm".equals(chart.getChartType())) {
                        sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                        .setMainStatisticValue(subgroupStatistic.getSubgroupBar().doubleValue());
                        if (subgroupStatistic.getSubgroupRm() != null) {
                            sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                            .setSecondStatisticValue(subgroupStatistic.getSubgroupRm().doubleValue());
                        }
                    }
                    sampleSubgroupList.get(sampleSubgroupList.size() - 1)
                                    .setSubgroupBar(subgroupStatistic.getSubgroupBar().doubleValue());

                    // 当前分组 样本数据赋值
                    sampleSubgroupList.get(sampleSubgroupList.size() - 1).setSampleDatas(sampleDatas);

                    // 当前分组点添加到最大绘点数集合中
                    currentSubgroupDataList.add(sampleSubgroupList.get(sampleSubgroupList.size() - 1));

                    /************************ 整体计算 ********************/
                    List<EntiretyStatisticR> entStatisticList =
                                    entiretyStatisticCalculate(currentSubgroupDataList, chart, coefficient);
                    entiretyStatisticList.addAll(entStatisticList);

                    /******************** OOC判异 ***********************/
                    if (judementVo.getJudgementSize() > 0) {
                        OocVO oocVO = oocCalculate(entity, currentSubgroupDataList, judementVo, entStatisticList,
                                        existsOocList, chart);

                        oocList.addAll(oocVO.getOocList());
                        messageList.addAll(oocVO.getMessageList());
                        messageDetailList.addAll(oocVO.getMessageDetailList());
                        messageUploadRelList.addAll(oocVO.getMessageUploadRelList());
                    }
                }
			}
			logger.info(Utils.getLog(uuid, "样本计算完成，entityCode=" + entity.getEntityCode() + ",entityVersion="
                    + entity.getEntityVersion() + ",耗时=" + (System.currentTimeMillis() - startTime) + "ms"));
						
			/**
             * 批量保存分组数据 pspc_sample_subgroup
             */
            int batchSize = 15000;
            startTime = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(sampleSubgroupList)) {
                List<SampleSubgroupR> sampleSubgroupListCopy = sampleSubgroupList.stream().collect(Collectors.toList());
                List<SampleSubgroupR> tempSampleSubgroupList;
                int sizeClassify = sampleSubgroupListCopy.size();
                int countClassify = sizeClassify / batchSize;
                for (int i = 0; i <= countClassify; i++) {
                    int start = i * batchSize;
                    int end = countClassify > i ? start + batchSize : start + sizeClassify % batchSize;
                    if (start < end) {
                        tempSampleSubgroupList = sampleSubgroupListCopy.subList(start, end);
                        //subgroupRepository.batchInsertRows(tempSampleSubgroupList);
                        for(int k=0;k<tempSampleSubgroupList.size();k++) {
							 sampleSubgroupRMapper.insertSelective(tempSampleSubgroupList.get(k));
						}
                    }
                }
            }
            logger.info(Utils.getLog(uuid,
                            "保存分组数据，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";分组数量：" + sampleSubgroupList.size() + ",耗时=" + (System.currentTimeMillis() - startTime)
                            + "ms");
			
			/**
             * 批量保存分组关系数据
             */
            startTime = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(subgroupRelationList)) {
                List<SampleSubgroupRelationR> subgroupRelationListCopy =
                                subgroupRelationList.stream().collect(Collectors.toList());
                List<SampleSubgroupRelationR> tempSubgroupRelationList;
                int sizeRelation = subgroupRelationListCopy.size();
                int countRelation = sizeRelation / batchSize;
                for (int i = 0; i <= countRelation; i++) {
                    int start = i * batchSize;
                    int end = countRelation > i ? start + batchSize : start + sizeRelation % batchSize;
                    if (start < end) {
                        tempSubgroupRelationList = subgroupRelationListCopy.subList(start, end);
                        //relationRepository.batchInsertRows(tempSubgroupRelationList);
                        for(int k=0;k<tempSubgroupRelationList.size();k++) {
							sampleSubgroupRelationRMapper.insertSelective(tempSubgroupRelationList.get(k));
						}
                    }
                }
            }
            logger.info(Utils.getLog(uuid,
                            "保存分组关系，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";分组关系数量：" + subgroupRelationList.size() + ",耗时="
                            + (System.currentTimeMillis() - startTime) + "ms");
			
			
			/**
			 * 修改实体控制图最大组号
			 */
			startTime = System.currentTimeMillis();
			if (sequence.getSequenceId() == null) {
				sequenceMapper.insertSelective(sequence);
			} else {
				sequenceMapper.updateByPrimaryKeySelective(sequence);
			}
			logger.info(Utils.getLog(uuid, "保存最大组号，entityCode=" + entity.getEntityCode() + ",entityVersion="
                    + entity.getEntityVersion() + ",耗时=" + (System.currentTimeMillis() - startTime) + "ms"));

			/**
             * 批量保存子组计算数据 pspc_subgroup_statistic
             */
            startTime = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(subgroupStatisticList)) {
                List<SubgroupStatisticR> tempSubgroupStatisticList;
                int sizeStatistic = subgroupStatisticList.size();
                int countStatistic = sizeStatistic / batchSize;
                for (int i = 0; i <= countStatistic; i++) {
                    int start = i * batchSize;
                    int end = countStatistic > i ? start + batchSize : start + sizeStatistic % batchSize;
                    if (start < end) {
                        tempSubgroupStatisticList = subgroupStatisticList.subList(start, end);
                        //statisticRepository.batchInsertRows(tempSubgroupStatisticList);
                        for(int k=0;k<tempSubgroupStatisticList.size();k++) {
							subgroupStatisticRMapper.insertSelective(tempSubgroupStatisticList.get(k));
						}
                    }
                }
            }
            logger.info(Utils.getLog(uuid,
                            "保存子组计算，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";子组统计数量=" + subgroupStatisticList.size() + ",耗时="
                            + (System.currentTimeMillis() - startTime) + "ms");
						
			/**
             * 批量保存整体计算数据 pspc_entirety_statistic
             */
            startTime = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(entiretyStatisticList)) {
                List<EntiretyStatisticR> tempEntiretyStatisticList;
                int sizeEntiretyStatistic = entiretyStatisticList.size();
                int countEntiretyStatistic = sizeEntiretyStatistic / batchSize;
                for (int i = 0; i <= countEntiretyStatistic; i++) {
                    int start = i * batchSize;
                    int end = countEntiretyStatistic > i ? start + batchSize
                                    : start + sizeEntiretyStatistic % batchSize;
                    if (start < end) {
                        tempEntiretyStatisticList = entiretyStatisticList.subList(start, end);
                        //entiretyStatisticRepository.batchInsertRows(tempEntiretyStatisticList);
                        for(int k=0;k<tempEntiretyStatisticList.size();k++) {
							entiretyStatisticRMapper.insertSelective(tempEntiretyStatisticList.get(k));
						}
                    }
                }
            }
            logger.info(Utils.getLog(uuid,
                            "保存整体计算，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";整体统计数量=" + entiretyStatisticList.size() + ",耗时="
                            + (System.currentTimeMillis() - startTime) + "ms");	
			/**
             * 批量保存OOC pspc_ooc
             */
            startTime = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(oocList)) {
                List<OocR> tempOocList;
                int sizeOoc = oocList.size();
                int countOoc = sizeOoc / batchSize;
                for (int i = 0; i <= countOoc; i++) {
                    int start = i * batchSize;
                    int end = countOoc > i ? start + batchSize : start + sizeOoc % batchSize;
                    if (start < end) {
                        tempOocList = oocList.subList(start, end);
                        //oocRepository.batchInsertRows(tempOocList);
                        for(int k=0;k<tempOocList.size();k++) {
							oocRMapper.insertSelective(tempOocList.get(k));
						}
                    }
                }
            }
            logger.info(Utils.getLog(uuid,
                            "保存OOC，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";OOC数量=" + oocList.size() + ",耗时=" + (System.currentTimeMillis() - startTime) + "ms");
			
			

			// 批量保存消息 消息和消息明细
			// pspc_message、pspc_message_detail
			if (CollectionUtils.isNotEmpty(messageList)) {
				Date messageCreationDate = new Date();

				messageList.stream().forEach(message -> {
					message.setCreationDate(messageCreationDate);
				});

				List<MessageR> tempMessageList;
				int sizeMessage = messageList.size();
				int countMessage = sizeMessage / batchSize;

				startTime = System.currentTimeMillis();			
				for (int i = 0; i <= countMessage; i++) {
                    int start = i * batchSize;
                    int end = countMessage > i ? start + batchSize : start + sizeMessage % batchSize;
                    if (start < end) {
                        tempMessageList = messageList.subList(start, end);
                        //messageRepository.batchInsertRows(tempMessageList);
                        for(int k=0;k<tempMessageList.size();k++) {
							messageMapper.insertSelective(tempMessageList.get(k));
						}
                    }
                }
                logger.info(Utils.getLog(uuid,
                                "保存消息头，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                                + entity.getEntityVersion())
                                + ";消息头数量=" + messageList.size() + ",耗时=" + (System.currentTimeMillis() - startTime)
                                + "ms");
				
				startTime = System.currentTimeMillis();
				if (CollectionUtils.isNotEmpty(messageDetailList)) {
					List<MessageDetailR> tempMessageDetailList;
					int sizeMessageDetailList = messageDetailList.size();
					int countMessageDetailList = sizeMessageDetailList / batchSize;
					for (int i = 0; i <= countMessageDetailList; i++) {
                        int start = i * batchSize;
                        int end = countMessageDetailList > i ? start + batchSize
                                        : start + sizeMessageDetailList % batchSize;
                        if (start < end) {
                            tempMessageDetailList = messageDetailList.subList(start, end);
                            //messageDetailRepository.batchInsertRows(tempMessageDetailList);
                            for(int k=0;k<tempMessageDetailList.size();k++) {
								messageDetailMapper.insertSelective(tempMessageDetailList.get(k));
							}
                        }
                    }
				}
				logger.info(Utils.getLog(uuid,
                        "保存消息行，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                        + entity.getEntityVersion())
                        + ";保存消息行数量=" + messageDetailList.size() + ",耗时="
                        + (System.currentTimeMillis() - startTime) + "ms");
		
				// 批量保存消息 消息
                if (CollectionUtils.isNotEmpty(messageUploadRelList)) {
                    startTime = System.currentTimeMillis();
                    List<MessageUploadRelR> tempMessageUploadRel;
                    int sizeMessageUploadRel = messageUploadRelList.size();
                    int countMessageUploadRel = sizeMessageUploadRel / batchSize;
                    for (int i = 0; i <= countMessageUploadRel; i++) {
                        int start = i * batchSize;
                        int end = countMessageUploadRel > i ? start + batchSize
                                        : start + sizeMessageUploadRel % batchSize;
                        if (start < end) {
                            tempMessageUploadRel = messageUploadRelList.subList(start, end);
                            //messageUploadRelRepository.batchInsertRows(tempMessageUploadRel);
                            for(int k=0;k<tempMessageUploadRel.size();k++) {
								messageUploadRelMapper.insertSelective(tempMessageUploadRel.get(k));
							}
                        }
                    }
                    logger.info(Utils.getLog(uuid,
                                    "保存发送消息数据，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                                    + entity.getEntityVersion())
                                    + ";保存发送消息数量=" + messageUploadRelList.size() + ",耗时="
                                    + (System.currentTimeMillis() - startTime) + "ms");

                    /**
                     * startTime = System.currentTimeMillis(); try {
                     * interfaceService.batchDealMessage(messageList, entity, "sample",
                     * messageCreationDate); } catch (Exception e) { logger.info(Utils.getLog(uuid,
                     * "发送消息失败,原因=" + e.getMessage())); } logger.info(Utils.getLog(uuid, "发送消息,耗时="
                     * + (System.currentTimeMillis() - startTime) + "ms"));
                     **/
                    subGroupCalcResultVO.setMessageList(messageList);
                    subGroupCalcResultVO.setMessageType("sample");
                    subGroupCalcResultVO.setMessageCreationDate(messageCreationDate);
                }
			}
			
			
			
			// 批量保存消息 消息和消息明细
			// pspc_message、pspc_message_detail
//			if (messageList.size() > 0) {
//				List<String> messageSqlList = new ArrayList<>();
//				for (MessageR c : messageList) {
//					messageSqlList.addAll(MtSqlHelper.getInsertSql(c));
//				}
//				logger.info("批量保存消息:" + subgroupStatisticList.size());
//				this.jdbcTemplate.batchUpdate(messageSqlList.toArray(new String[messageSqlList.size()]));
//				logger.info(Utils.getLog(uuid, "end 保存消息头，entityCode=" + entity.getEntityCode() + ",entityVersion="
//						+ entity.getEntityVersion()) + ";消息头数量=" + messageList.size());
//
//				List<String> messageDetaiSqllList = new ArrayList<>();
//				for (MessageDetailR c : messageDetailList) {
//					messageDetaiSqllList.addAll(MtSqlHelper.getInsertSql(c));
//				}
//				this.jdbcTemplate.batchUpdate(messageDetaiSqllList.toArray(new String[messageDetaiSqllList.size()]));
//				logger.info(Utils.getLog(uuid, "end 保存消息行，entityCode=" + entity.getEntityCode() + ",entityVersion="
//						+ entity.getEntityVersion()) + ";保存消息行数量=" + messageDetailList.size());
//			}
//
//			// 批量保存消息 消息
//			if (messageUploadRelList.size() > 0) {
//				List<String> messageUploadRelSqlList = new ArrayList<>();
//				for (MessageUploadRelR c : messageUploadRelList) {
//					messageUploadRelSqlList.addAll(MtSqlHelper.getInsertSql(c));
//				}
//				this.jdbcTemplate.batchUpdate(messageUploadRelSqlList.toArray(new String[messageUploadRelSqlList.size()]));
//
//				// 調用發送消息
//				try { // 消息集合
//					for (MessageR message : messageList) {
//						//interfaceService.uploadMessage(message.getSiteId(), message.getTenantId(), message.getGroupCode(), entity, "sample", messageCreationDate);
//					}
//				} catch (Exception e) {
//					// 消息发送失败不做任何处理
//				}
//			}

			/**
             * 批量删除预分组数据（已完成分组部分） pspc_sample_subgroup_wait
             */
            startTime = System.currentTimeMillis();
            SampleSubgroupWaitR deleteDto = new SampleSubgroupWaitR();
            deleteDto.setTenantId(entity.getTenantId());
            deleteDto.setSiteId(entity.getSiteId());
            deleteDto.setSampleSubgroupWaitIdList(deleteIdList);
            //sampleSubgroupWaitRepositoty.deleteSubgroupWaitDataByIdList(deleteDto); modified 20191101
            
            for(int k=0;k<deleteIdList.size();k++) {
            	SampleSubgroupWaitR sampleSubgroupWaitR = new SampleSubgroupWaitR();
            	sampleSubgroupWaitR.setSampleSubgroupWaitId(deleteIdList.get(k));
            	sampleSubgroupWaitRepositoty.deleteByPrimaryKey(sampleSubgroupWaitR);
            } 
            
            logger.info(Utils.getLog(uuid,
                            "删除预分组数据，entityCode=" + entity.getEntityCode() + ",entityVersion="
                                            + entity.getEntityVersion())
                            + ";删除行数=" + deleteIdList.size() + ",耗时=" + (System.currentTimeMillis() - startTime)
                            + "ms");

            subGroupCalcResultVO.setSubgroupStatisticList(subgroupStatisticList);

            /**
             * // 保存最大绘点数范围内子组数据至redis // 最大绘点数范围内子组数据先删后增 redisHelper.strSet(subgroupRedisKey,
             * redisHelper.toJson(currentSubgroupDataList)); logger.info(Utils .getLog(uuid, "end
             * 保存最大绘点数范围内子组数据到redis，entityCode=" + entity.getEntityCode() + ",entityVersion=" +
             * entity.getEntityVersion()) + ";子组数量=" + currentSubgroupDataList.size());
             * 
             * // 保存最大绘点数范围内OOC数据至redis redisHelper.strSet(oocRedisKey,
             * redisHelper.toJson(existsOocList)); logger.info(Utils .getLog(uuid, "end
             * 保存最大绘点数范围内OOC数据到redis，entityCode=" + entity.getEntityCode() + ",entityVersion=" +
             * entity.getEntityVersion()) + ";OOC数量=" + existsOocList.size());
             * logger.info(Utils.getLog(uuid, "******* end 实体控制图分组处理 entityCode=" +
             * entity.getEntityCode() + ",entityVersion=" + entity.getEntityVersion()));
             **/
        }

        return subGroupCalcResultVO;
	}

	@Override
    public Map<EntityR, SubGroupCalcResultVO> sampleDataSubgroupList(List<EntityR> entityList, String uuid) {
        Map<EntityR, SubGroupCalcResultVO> map = new HashMap<EntityR, SubGroupCalcResultVO>();
        for (EntityR entity : entityList) {
            int dataSize = entity.getSampleSubgroupWaitList().size();
            if (dataSize < entity.getSubgroupSize()) {
                continue;
            }
            String subgroupRedisKey = "pspc:entity_subgroup_data:" + entity.getTenantId() + "-" + entity.getSiteId()
                            + "-" + entity.getEntityCode() + "-" + entity.getEntityVersion();
            String oocRedisKey = "pspc:entity_ooc_data:" + entity.getTenantId() + "-" + entity.getSiteId() + "-"
                            + entity.getEntityCode() + "-" + entity.getEntityVersion();
            entity.setSubgroupRedisKey(subgroupRedisKey);
            entity.setOocRedisKey(oocRedisKey);
            map.put(entity, sampleDataSubgroup(entity, uuid));
        }
        return map;
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
        List<JudgementR> judgementList = judgementRepository.listJudegement(tenantId, siteId, chartId);

        JudementVO judementVo = new JudementVO();

        String judgementShortCode = judgementList.stream().map(JudgementR::getJudgementShortCode).distinct()
                        .collect(Collectors.joining(","));

        judementVo.setJudgementShortCode(judgementShortCode);
        judementVo.setJudgementSize(judgementList.size());

		List<CodeValue> messageTypeDescList = new ArrayList<CodeValue>();
		List<CodeValue> judgementDescList = new ArrayList<CodeValue>();
		List<CodeValue> elementThemeList = new ArrayList<CodeValue>();
		List<CodeValue> elementContentList = new ArrayList<CodeValue>();
		if (!judgementList.isEmpty()) { // modified 20190903
			MessageTypeR messageType = new MessageTypeR(); // modified 20190903
			messageType.setTenantId(tenantId);
			messageType.setSiteId(siteId);
			messageType.setMessageTypeStatus("Y");
			List<MessageTypeR> messageTypeList = messageTypeRepository.select(null, messageType, 0, 0);
			if (!CollectionUtils.isEmpty(messageTypeList)) {
				for (MessageTypeR messageTypeIn : messageTypeList) {
					CodeValue codeValueDTO = new CodeValue();
					codeValueDTO.setValue(messageTypeIn.getMessageTypeCode());
					codeValueDTO.setMeaning(messageType.getDescription());
					messageTypeDescList.add(codeValueDTO);
				}

			}

//          原hzero逻辑：
//			judgementDescList = judgementBasisRepository.selectAsLovValue(tenantId, siteId);
//			elementThemeList = messageElementRepository.selectAsLovValue(tenantId, siteId, "THEME");
//			elementContentList = messageElementRepository.selectAsLovValue(tenantId, siteId, "CONTENT");

			// 消息类型：MESSAGE_TYPE_1:SPC违规等级1、MESSAGE_TYPE_2:SPC违规等级2、MESSAGE_TYPE_3:SPC违规等级3、MESSAGE_TYPE_4:SPC违规等级4、MESSAGE_TYPE_5:SPC违规等级5
			// messageTypeDescList = getCodeValuesMapper.getCodeValues("PSPC.MESSAGE.TYPE");
			// 判异准则：
			judgementDescList = getCodeValuesMapper.getCodeValues("PSPC.JUDGEMENT");
			// 消息主题元素：
			elementThemeList = getCodeValuesMapper.getCodeValues("PSPC.MESSAGE.THEME.ELEMENT");
			// 消息正文元素：
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

            if (!judgement.getMessageTypeDetailList().isEmpty()) {
                for (MessageTypeDetailR messageTypeDetail : judgement.getMessageTypeDetailList()) {
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
                case "RULE1-S":
                    judementVo.setSecondRule1(judgement);
                    break;
                case "RULE2-M":
                    judementVo.setMainRule2(judgement);
                    break;
                case "RULE2-S":
                    judementVo.setSecondRule2(judgement);
                    break;
                case "RULE3-M":
                    judementVo.setMainRule3(judgement);
                    break;
                case "RULE3-S":
                    judementVo.setSecondRule3(judgement);
                    break;
                case "RULE4-M":
                    judementVo.setMainRule4(judgement);
                    break;
                case "RULE4-S":
                    judementVo.setSecondRule4(judgement);
                    break;
                case "RULE5-M":
                    judementVo.setMainRule5(judgement);
                    break;
                case "RULE5-S":
                    judementVo.setSecondRule5(judgement);
                    break;
                case "RULE6-M":
                    judementVo.setMainRule6(judgement);
                    break;
                case "RULE6-S":
                    judementVo.setSecondRule6(judgement);
                    break;
                case "RULE7-M":
                    judementVo.setMainRule7(judgement);
                    break;
                case "RULE7-S":
                    judementVo.setSecondRule7(judgement);
                    break;
                case "RULE8-M":
                    judementVo.setMainRule8(judgement);
                    break;
                case "RULE8-S":
                    judementVo.setSecondRule8(judgement);
                    break;
                case "RULE9-M":
                    judementVo.setMainRule9(judgement);
                    break;
                case "RULE9-S":
                    judementVo.setSecondRule9(judgement);
                    break;
                case "RULE10-M":
                    judementVo.setMainRule10(judgement);
                    break;
                case "RULE10-S":
                    judementVo.setSecondRule10(judgement);
                    break;
                case "RULE11-M":
                    judementVo.setMainRule11(judgement);
                    break;
                case "RULE11-S":
                    judementVo.setSecondRule11(judgement);
                    break;
                case "RULE12-M":
                    judementVo.setMainRule12(judgement);
                    break;
                case "RULE12-S":
                    judementVo.setSecondRule12(judgement);
                    break;
                case "RULE13-M":
                    judementVo.setMainRule13(judgement);
                    break;
                case "RULE13-S":
                    judementVo.setSecondRule13(judgement);
                    break;
                case "RULE14-M":
                    judementVo.setMainRule14(judgement);
                    break;
                case "RULE14-S":
                    judementVo.setSecondRule14(judgement);
                    break;
                case "RULE15-M":
                    judementVo.setMainRule15(judgement);
                    break;
                case "RULE15-S":
                    judementVo.setSecondRule15(judgement);
                    break;
                case "RULE16-M":
                    judementVo.setMainRule16(judgement);
                    break;
                case "RULE16-S":
                    judementVo.setSecondRule16(judgement);
                    break;
                case "RULE17-M":
                    judementVo.setMainRule17(judgement);
                    break;
                case "RULE17-S":
                    judementVo.setSecondRule17(judgement);
                    break;
                case "RULE18-M":
                    judementVo.setMainRule18(judgement);
                    break;
                case "RULE18-S":
                    judementVo.setSecondRule18(judgement);
                    break;
                default:
                    break;
            }
        }
		return judementVo;
	}

	// modified 20190903-20191022begin
	/**
	 * 子组计算
	 *
	 * @param entity                 实体控制图
	 * @param sampleSubgroupWaitList 当前分组对应样本数据集合
	 * @param max                    最大值
	 * @param min                    最小值
	 * @param sum                    合计
	 * @param sampleSubgroupId       分组ID
	 * @param preSubgroupValue       上一个点平均值 用于单值分组移动极差计算
	 * @return
	 */
	private SubgroupStatisticR subgroupStatisticCalculate(EntityR entity,
			List<SampleSubgroupWaitR> sampleSubgroupWaitList, Double max, Double min, Double sum, Long sampleSubgroupId,
			Double preSubgroupValue) {

		double subgroupAvg;// 子组计算--平均值
        double subgroupMe;// 子组计算--中位数

        SubgroupStatisticR subgroupStatistic = new SubgroupStatisticR();
        subgroupStatistic.setSampleSubgroupId(sampleSubgroupId);
        subgroupStatistic.setTenantId(entity.getTenantId());
        subgroupStatistic.setSiteId(entity.getSiteId());
        subgroupStatistic.setEntityCode(entity.getEntityCode());
        subgroupStatistic.setEntityVersion(entity.getEntityVersion());

        if (entity.getSubgroupSize() == 1) {
            // 平均值
            subgroupStatistic.setSubgroupBar(sampleSubgroupWaitList.get(0).getSampleValue());
            // 极差
            subgroupStatistic.setSubgroupR(BigDecimal.valueOf(0));
            // 组内最大值
            subgroupStatistic.setSubgroupMax(sampleSubgroupWaitList.get(0).getSampleValue());
            // 组内最小值
            subgroupStatistic.setSubgroupMin(sampleSubgroupWaitList.get(0).getSampleValue());
            // 标准差
            subgroupStatistic.setSubgroupSigma(BigDecimal.valueOf(0));
            // 中位数
            subgroupStatistic.setSubgroupMe(sampleSubgroupWaitList.get(0).getSampleValue());

            // 移动极差
            if (!ObjectUtils.isEmpty(preSubgroupValue)) {
                subgroupStatistic.setSubgroupRm(BigDecimal.valueOf(Math
                                .abs(sampleSubgroupWaitList.get(0).getSampleValue().doubleValue() - preSubgroupValue)));
            }
        } else {
            subgroupAvg = sum / sampleSubgroupWaitList.size();

            // 平均值
            subgroupStatistic.setSubgroupBar(BigDecimal.valueOf(subgroupAvg));
            // 极差
            subgroupStatistic.setSubgroupR(BigDecimal.valueOf(max - min));
            // 组内最大值
            subgroupStatistic.setSubgroupMax(BigDecimal.valueOf(max));
            // 组内最小值
            subgroupStatistic.setSubgroupMin(BigDecimal.valueOf(min));
            // 中位数
            if (sampleSubgroupWaitList.size() % 2 == 0) {
                subgroupMe = (sampleSubgroupWaitList.get(sampleSubgroupWaitList.size() / 2 - 1).getSampleValue()
                                .doubleValue()
                                + sampleSubgroupWaitList.get(sampleSubgroupWaitList.size() / 2).getSampleValue()
                                                .doubleValue())
                                / 2;
            } else {
                subgroupMe = sampleSubgroupWaitList.get(sampleSubgroupWaitList.size() / 2).getSampleValue()
                                .doubleValue();
            }
            subgroupStatistic.setSubgroupMe(BigDecimal.valueOf(subgroupMe));
            // 标准差
            double sumSigma = 0D;
            for (SampleSubgroupWaitR sampleData : sampleSubgroupWaitList) {
                sumSigma += Math.pow((sampleData.getSampleValue().doubleValue() - subgroupAvg), 2);
            }

            subgroupStatistic.setSubgroupSigma(
                            BigDecimal.valueOf(Math.sqrt(sumSigma / (sampleSubgroupWaitList.size() - 1))));
        }

        return subgroupStatistic;
	}
	// modified 20190903-20191022 end

	// modified 20190903 begin
	/**
	 * 整体计算
	 *
	 * @param sampleSubgroupList 当前最大绘点数范围内分组数据
	 * @param chart              控制图
	 * @return
	 */
	private List<EntiretyStatisticR> entiretyStatisticCalculate(List<SampleSubgroupR> sampleSubgroupList, ChartR chart,
			CoefficientR coefficient) {

		Double entiretyBar = 0D;// 整体计算--平均值
        Double entiretyMainBar;// 整体计算--主图平均值
        Double entiretySecondBar = 0D;// 整体计算--次图平均值
        double entiretySigma = 0D;// 整体计算--SIGMA
        double entiretySum;// 整体计算--合计
        int entiretyCount;// 整体计算--计数器
        Double entiretyStatisticValue = 0D;// 整体计算--指标值

        List<EntiretyStatisticR> entiretyStatisticList = new ArrayList<>();

        // 计算平均值
        // 主图平均值
        if (sampleSubgroupList.size() == 1) {
            entiretyMainBar = sampleSubgroupList.get(0).getMainStatisticValue();
        } else {
            entiretyMainBar = sampleSubgroupList.stream().mapToDouble(
                            value -> value.getMainStatisticValue() == null ? 0.0D : value.getMainStatisticValue())
                            .average().getAsDouble();
        }
        // 次图平均值
        if (sampleSubgroupList.size() == 1) {
            entiretySecondBar = sampleSubgroupList.get(0).getSecondStatisticValue();
        } else {
            entiretySecondBar = null;
            List<SampleSubgroupR> sList = sampleSubgroupList.stream()
                            .filter(sampleSubgroup -> sampleSubgroup.getSecondStatisticValue() != null)
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(sList)) {
                entiretySecondBar = sList.stream().mapToDouble(value -> value.getSecondStatisticValue() == null ? 0.0D
                                : value.getSecondStatisticValue()).average().getAsDouble();
            }
        }

        for (ChartDetailR detail : chart.getChartDetailList()) {

            if ("M".equals(detail.getChartDetailType())) {
                entiretyBar = entiretyMainBar;
            } else if ("S".equals(detail.getChartDetailType())) {
                entiretyBar = entiretySecondBar;
            }

            // 排除子组大小为1 时第一个点移动极差值为NULL
            if (!ObjectUtils.isEmpty(entiretyBar)) {
                EntiretyStatisticR entiretyStatistic = new EntiretyStatisticR();
                entiretyStatistic.setTenantId(sampleSubgroupList.get(sampleSubgroupList.size() - 1).getTenantId());
                entiretyStatistic.setSiteId(sampleSubgroupList.get(sampleSubgroupList.size() - 1).getSiteId());
                entiretyStatistic.setSampleSubgroupId(
                                sampleSubgroupList.get(sampleSubgroupList.size() - 1).getSampleSubgroupId());
                entiretyStatistic.setEntityCode(sampleSubgroupList.get(sampleSubgroupList.size() - 1).getEntityCode());
                entiretyStatistic.setEntityVersion(
                                sampleSubgroupList.get(sampleSubgroupList.size() - 1).getEntityVersion());
                entiretyStatistic.setChartDetailType(detail.getChartDetailType());

                // 平均值
                entiretyStatistic.setEntiretyBar(BigDecimal.valueOf(entiretyBar));
                // 控制限类型
                entiretyStatistic.setControlLimitUsage(detail.getControlLimitUsage());
                // 规格限
                entiretyStatistic.setEntiretyUsl(detail.getUpperSpecLimit());
                entiretyStatistic.setEntiretyLsl(detail.getLowerSpecLimit());

                // 计算sigma
                if ("FIXED".equals(detail.getControlLimitUsage())) {
                    // 计算SIGMA (UCL-LCL)/6
                    entiretySigma = (detail.getUpperControlLimit().doubleValue()
                                    - detail.getLowerControlLimit().doubleValue()) / 6;

                    entiretyStatistic.setEntiretyUcl(BigDecimal.valueOf(detail.getUpperControlLimit().doubleValue()));
                    entiretyStatistic.setEntiretyCl(BigDecimal.valueOf(detail.getCenterLine().doubleValue()));
                    entiretyStatistic.setEntiretyLcl(BigDecimal.valueOf(detail.getLowerControlLimit().doubleValue()));

                } else if ("CALCULATION".equals(detail.getControlLimitUsage())) {
                    // 计算SIGMA
                    entiretySum = 0D;
                    entiretyCount = 0;
                    for (SampleSubgroupR dto : sampleSubgroupList) {
                        if ("M".equals(detail.getChartDetailType())) {
                            entiretyStatisticValue = dto.getMainStatisticValue();
                        } else if ("S".equals(detail.getChartDetailType())) {
                            entiretyStatisticValue = dto.getSecondStatisticValue();
                        }
                        if (entiretyStatisticValue != null) {
                            entiretySum += Math.pow((entiretyStatisticValue.doubleValue() - entiretyBar), 2);
                            entiretyCount++;
                        }
                    }
                    // 次图SIGMA
                    if (entiretyCount > 1) {
                        entiretySigma = Math.sqrt(entiretySum / (entiretyCount - 1));
                    } else {
                        entiretySigma = 0D;
                    }

                    entiretyStatistic.setEntiretyUcl(BigDecimal.valueOf(entiretyBar + 3 * entiretySigma));
                    entiretyStatistic.setEntiretyCl(BigDecimal.valueOf(entiretyBar));
                    entiretyStatistic.setEntiretyLcl(BigDecimal.valueOf(entiretyBar - 3 * entiretySigma));
                } else if ("COEFFICIENT".equals(detail.getControlLimitUsage())) {
                    switch (chart.getChartType() + "-" + detail.getChartDetailType()) {
                        case "XBAR-R-M":
                            entiretyStatistic.setEntiretyUcl(BigDecimal.valueOf(
                                            entiretyMainBar + coefficient.getA2().doubleValue() * entiretySecondBar));
                            entiretyStatistic.setEntiretyLcl(BigDecimal.valueOf(
                                            entiretyMainBar - coefficient.getA2().doubleValue() * entiretySecondBar));
                            break;
                        case "XBAR-R-S":
                            entiretyStatistic.setEntiretyUcl(
                                            BigDecimal.valueOf(coefficient.getD4().doubleValue() * entiretySecondBar));
                            entiretyStatistic.setEntiretyLcl(
                                            BigDecimal.valueOf(coefficient.getD3().doubleValue() * entiretySecondBar));
                            break;
                        case "XBAR-S-M":
                            entiretyStatistic.setEntiretyUcl(BigDecimal.valueOf(
                                            entiretyMainBar + coefficient.getA3().doubleValue() * entiretySecondBar));
                            entiretyStatistic.setEntiretyLcl(BigDecimal.valueOf(
                                            entiretyMainBar - coefficient.getA3().doubleValue() * entiretySecondBar));
                            break;
                        case "XBAR-S-S":
                            entiretyStatistic.setEntiretyUcl(
                                            BigDecimal.valueOf(coefficient.getB4().doubleValue() * entiretySecondBar));
                            entiretyStatistic.setEntiretyLcl(
                                            BigDecimal.valueOf(coefficient.getB3().doubleValue() * entiretySecondBar));
                            break;
                        case "Me-R-M":
                            entiretyStatistic.setEntiretyUcl(BigDecimal.valueOf(
                                            entiretyMainBar + coefficient.getA4().doubleValue() * entiretySecondBar));
                            entiretyStatistic.setEntiretyLcl(BigDecimal.valueOf(
                                            entiretyMainBar - coefficient.getA4().doubleValue() * entiretySecondBar));
                            break;
                        case "Me-R-S":
                            entiretyStatistic.setEntiretyUcl(
                                            BigDecimal.valueOf(coefficient.getD4().doubleValue() * entiretySecondBar));
                            entiretyStatistic.setEntiretyLcl(
                                            BigDecimal.valueOf(coefficient.getD3().doubleValue() * entiretySecondBar));
                            break;
                        case "X-Rm-S":
                            if (entiretySecondBar == null) {
                                entiretySecondBar = 0D;
                            }
                            entiretyStatistic.setEntiretyUcl(
                                            BigDecimal.valueOf(coefficient.getD4().doubleValue() * entiretySecondBar));
                            entiretyStatistic.setEntiretyLcl(
                                            BigDecimal.valueOf(coefficient.getD3().doubleValue() * entiretySecondBar));

                            break;
                        case "X-Rm-M":
                            if (entiretySecondBar == null) {
                                entiretySecondBar = 0D;
                            }
                            entiretyStatistic.setEntiretyUcl(BigDecimal.valueOf(
                                            entiretyMainBar + coefficient.getE2().doubleValue() * entiretySecondBar));
                            entiretyStatistic.setEntiretyLcl(BigDecimal.valueOf(
                                            entiretyMainBar - coefficient.getE2().doubleValue() * entiretySecondBar));
                            break;
                        default:
                            break;
                    }
                    entiretyStatistic.setEntiretyCl(BigDecimal.valueOf(entiretyBar));

                    entiretySigma = (entiretyStatistic.getEntiretyUcl().doubleValue()
                                    - entiretyStatistic.getEntiretyLcl().doubleValue()) / 6;
                } else {
                    throw new CommonException("请维护该" + chart.getChartCode() + "的控制限类型!");

                }

                entiretyStatistic.setEntiretySigma(BigDecimal.valueOf(entiretySigma));

                entiretyStatisticList.add(entiretyStatistic);
            }
        }
        return entiretyStatisticList;
	}

	// modified 20190903 end
	/**
	 * OOC判异
	 *
	 * @param entity
	 * @param sampleSubgroupList
	 * @param judementVo
	 * @param entiretyStatisticList
	 * @param existsOocList
	 * @param chart
	 * @return
	 */
	private OocVO oocCalculate(EntityR entity, List<SampleSubgroupR> sampleSubgroupList, JudementVO judementVo,
			List<EntiretyStatisticR> entiretyStatisticList, List<OocR> existsOocList, ChartR chart) {
		OocVO oocVO = new OocVO();
		Set<OocR> oocList = new HashSet<>();
		List<MessageR> messageList = new ArrayList<>();
		List<MessageDetailR> messageDetaillist = new ArrayList<>();
		List<MessageUploadRelR> messageUploadRellist = new ArrayList<>();

		EntiretyStatisticR mainStatistic = new EntiretyStatisticR();// 主图整体计算指标
		EntiretyStatisticR secondStatistic = new EntiretyStatisticR();// 次图整体计算指标
		for (EntiretyStatisticR entiretyStatistic : entiretyStatisticList) {
			if ("M".equals(entiretyStatistic.getChartDetailType())) {
				mainStatistic = entiretyStatistic;
			} else if ("S".equals(entiretyStatistic.getChartDetailType())) {
				secondStatistic = entiretyStatistic;
			}
		}

		SampleSubgroupR firstSampleSubgroup = sampleSubgroupList.get(0);
		SampleSubgroupR lastSampleSubgroup = sampleSubgroupList.get(sampleSubgroupList.size() - 1);

// 实例化参数对象
		JudementParaVO paraVo = new JudementParaVO();

		int rowNum = 0;
		Double deltaValue = 0D;
		String groupCode = entity.getEntityCode() + "-" + entity.getEntityVersion() + "-";// 当前最大序号(Message组号)
		for (SampleSubgroupR sampleSubgroup : sampleSubgroupList) {
			rowNum++;// 行数

			// *****************主图判异*************************

			// 排除没值和没整体计算的点参与计算 （单值实体控制第一个点）
			if (!ObjectUtils.isEmpty(sampleSubgroup.getMainStatisticValue()) && !ObjectUtils.isEmpty(mainStatistic)) {

				// 规则1 规格上限上方的#个点 Xi大于USL,点必须在规格上限上方 最大绘点数范围内的点全部重新计算OOC
				if (!ObjectUtils.isEmpty(judementVo.getMainRule1())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyUsl()) && sampleSubgroup
								.getMainStatisticValue().doubleValue() > mainStatistic.getEntiretyUsl().doubleValue()) {
					paraVo.setMainCountRule1(paraVo.getMainCountRule1() + 1);
					if (paraVo.getMainCountRule1() >= judementVo.getMainRule1().getLengthData()) {
						String oocKey = sampleSubgroup.getSampleSubgroupId()
								+ judementVo.getMainRule1().getChartDetailType()
								+ judementVo.getMainRule1().getJudgementId();
						if (existsOocList.stream()
								.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
										+ ooc.getJudgementId()).equals(oocKey))
								.collect(Collectors.toList()).size() <= 0) {
							// 当前点违反规则
							OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule1(), mainStatistic,
									firstSampleSubgroup, lastSampleSubgroup, entity,
									groupCode + sampleSubgroup.getSubgroupNum());
							if (!oocList.contains(ooc)) {
								oocList.add(ooc);
							}

							OocR ooc1 = new OocR();
							ooc1.setOocId(ooc.getOocId());
							ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
							ooc1.setChartDetailType(ooc.getChartDetailType());
							ooc1.setJudgementId(ooc.getJudgementId());
							existsOocList.add(ooc1);

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
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyLsl()) && sampleSubgroup
								.getMainStatisticValue().doubleValue() < mainStatistic.getEntiretyLsl().doubleValue()) {
					paraVo.setMainCountRule2(paraVo.getMainCountRule2() + 1);
					if (paraVo.getMainCountRule2() >= judementVo.getMainRule2().getLengthData()) {
						String oocKey = sampleSubgroup.getSampleSubgroupId()
								+ judementVo.getMainRule2().getChartDetailType()
								+ judementVo.getMainRule2().getJudgementId();
						if (existsOocList.stream()
								.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
										+ ooc.getJudgementId()).equals(oocKey))
								.collect(Collectors.toList()).size() <= 0) {
							OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule2(), mainStatistic,
									firstSampleSubgroup, lastSampleSubgroup, entity,
									groupCode + sampleSubgroup.getSubgroupNum());
							if (!oocList.contains(ooc)) {
								oocList.add(ooc);
							}

							OocR ooc1 = new OocR();
							ooc1.setOocId(ooc.getOocId());
							ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
							ooc1.setChartDetailType(ooc.getChartDetailType());
							ooc1.setJudgementId(ooc.getJudgementId());
							existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule3())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyUcl()) && sampleSubgroup
								.getMainStatisticValue().doubleValue() > mainStatistic.getEntiretyUcl().doubleValue()) {
					paraVo.setMainCountRule3(paraVo.getMainCountRule3() + 1);
					if (paraVo.getMainCountRule3() >= judementVo.getMainRule3().getLengthData()) {
						String oocKey = sampleSubgroup.getSampleSubgroupId()
								+ judementVo.getMainRule3().getChartDetailType()
								+ judementVo.getMainRule3().getJudgementId();
						if (existsOocList.stream()
								.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
										+ ooc.getJudgementId()).equals(oocKey))
								.collect(Collectors.toList()).size() <= 0) {
							OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule3(), mainStatistic,
									firstSampleSubgroup, lastSampleSubgroup, entity,
									groupCode + sampleSubgroup.getSubgroupNum());
							if (!oocList.contains(ooc)) {
								oocList.add(ooc);
							}

							OocR ooc1 = new OocR();
							ooc1.setOocId(ooc.getOocId());
							ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
							ooc1.setChartDetailType(ooc.getChartDetailType());
							ooc1.setJudgementId(ooc.getJudgementId());
							existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule4())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyLcl()) && sampleSubgroup
								.getMainStatisticValue().doubleValue() < mainStatistic.getEntiretyLcl().doubleValue()) {
					paraVo.setMainCountRule4(paraVo.getMainCountRule4() + 1);
					if (paraVo.getMainCountRule4() >= judementVo.getMainRule4().getLengthData()) {
						String oocKey = sampleSubgroup.getSampleSubgroupId()
								+ judementVo.getMainRule4().getChartDetailType()
								+ judementVo.getMainRule4().getJudgementId();
						if (existsOocList.stream()
								.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
										+ ooc.getJudgementId()).equals(oocKey))
								.collect(Collectors.toList()).size() <= 0) {
							OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule4(), mainStatistic,
									firstSampleSubgroup, lastSampleSubgroup, entity,
									groupCode + sampleSubgroup.getSubgroupNum());
							if (!oocList.contains(ooc)) {
								oocList.add(ooc);
							}

							OocR ooc1 = new OocR();
							ooc1.setOocId(ooc.getOocId());
							ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
							ooc1.setChartDetailType(ooc.getChartDetailType());
							ooc1.setJudgementId(ooc.getJudgementId());
							existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule5())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getMainStatisticValue()
							.doubleValue() >= mainStatistic.getEntiretyCl().doubleValue()
									+ 2 * mainStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setMainCountRule5(paraVo.getMainCountRule5() + 1);
						if (paraVo.getMainCountRule5() >= judementVo.getMainRule5().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule5().getChartDetailType()
									+ judementVo.getMainRule5().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule5(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule6())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getMainStatisticValue()
							.doubleValue() >= mainStatistic.getEntiretyCl().doubleValue()
									+ mainStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setMainCountRule6(paraVo.getMainCountRule6() + 1);
						if (paraVo.getMainCountRule6() >= judementVo.getMainRule6().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule6().getChartDetailType()
									+ judementVo.getMainRule6().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule6(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule7())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getMainStatisticValue().doubleValue() >= mainStatistic.getEntiretyCl()
							.doubleValue()) {
						paraVo.setMainCountRule7(paraVo.getMainCountRule7() + 1);
						if (paraVo.getMainCountRule7() >= judementVo.getMainRule7().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule7().getChartDetailType()
									+ judementVo.getMainRule7().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule7(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule8())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getMainStatisticValue().doubleValue() <= mainStatistic.getEntiretyCl()
							.doubleValue()) {
						paraVo.setMainCountRule8(paraVo.getMainCountRule8() + 1);
						if (paraVo.getMainCountRule8() >= judementVo.getMainRule8().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule8().getChartDetailType()
									+ judementVo.getMainRule8().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule8(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule9())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getMainStatisticValue()
							.doubleValue() <= mainStatistic.getEntiretyCl().doubleValue()
									- mainStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setMainCountRule9(paraVo.getMainCountRule9() + 1);
						if (paraVo.getMainCountRule9() >= judementVo.getMainRule9().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule9().getChartDetailType()
									+ judementVo.getMainRule9().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule9(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule10())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getMainStatisticValue()
							.doubleValue() <= mainStatistic.getEntiretyCl().doubleValue()
									- 2 * mainStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setMainCountRule10(paraVo.getMainCountRule10() + 1);
						if (paraVo.getMainCountRule10() >= judementVo.getMainRule10().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule10().getChartDetailType()
									+ judementVo.getMainRule10().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule10(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						if (sampleSubgroupList.get(rowNum - 1).getMainStatisticValue().doubleValue()
								- sampleSubgroupList.get(rowNum - 2).getMainStatisticValue().doubleValue() > 0) {
							// if
							// (sampleSubgroupList.get(rowNum).getMainStatisticValue().doubleValue()
							// - sampleSubgroupList.get(rowNum -
							// 1).getMainStatisticValue().doubleValue() > 0) {

							paraVo.setMainCountRule11A(paraVo.getMainCountRule11A() + 1);
						} else {
							paraVo.setMainCountRule11A(1);
						}
						// 递减判断
						if (sampleSubgroupList.get(rowNum - 1).getMainStatisticValue().doubleValue()
								- sampleSubgroupList.get(rowNum - 2).getMainStatisticValue().doubleValue() < 0) {
							// if (sampleSubgroupList.get(rowNum
							// ).getMainStatisticValue().doubleValue() -
							// sampleSubgroupList.get(rowNum -
							// 1).getMainStatisticValue().doubleValue() < 0) {
							paraVo.setMainCountRule11B(paraVo.getMainCountRule11B() + 1);
						} else {
							paraVo.setMainCountRule11B(1);
						}

						if (paraVo.getMainCountRule11A() >= (judementVo.getMainRule11().getLengthData() + 1)
								|| paraVo.getMainCountRule11B() >= (judementVo.getMainRule11().getLengthData() + 1)) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule11().getChartDetailType()
									+ judementVo.getMainRule11().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule11(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						deltaValue = sampleSubgroupList.get(rowNum - 1).getMainStatisticValue()
								- sampleSubgroupList.get(rowNum - 2).getMainStatisticValue();

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
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule12().getChartDetailType()
									+ judementVo.getMainRule12().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule12(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
					paraVo.getMainListRule13().add(sampleSubgroup);
					if (paraVo.getMainListRule13().size() >= judementVo.getMainRule13().getLengthData()) {
						Double value = mainStatistic.getEntiretyCl().doubleValue()
								+ mainStatistic.getEntiretySigma().doubleValue();
						if ((paraVo.getMainListRule13().stream()
								.filter(sampleSubgroup1 -> sampleSubgroup1.getMainStatisticValue() > value)
								.collect(Collectors.toList())).size() >= judementVo.getMainRule13().getLimitData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule13().getChartDetailType()
									+ judementVo.getMainRule13().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule13(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
					paraVo.getMainListRule14().add(sampleSubgroup);
					if (paraVo.getMainListRule14().size() >= judementVo.getMainRule14().getLengthData()) {
						Double value = mainStatistic.getEntiretyCl().doubleValue()
								- mainStatistic.getEntiretySigma().doubleValue();
						if ((paraVo.getMainListRule14().stream()
								.filter(sampleSubgroup1 -> sampleSubgroup1.getMainStatisticValue() < value)
								.collect(Collectors.toList())).size() >= judementVo.getMainRule14().getLimitData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule14().getChartDetailType()
									+ judementVo.getMainRule14().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule14(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
					paraVo.getMainListRule15().add(sampleSubgroup);
					if (paraVo.getMainListRule15().size() >= judementVo.getMainRule15().getLengthData()) {
						Double value = mainStatistic.getEntiretyCl().doubleValue()
								+ 2 * mainStatistic.getEntiretySigma().doubleValue();
						if ((paraVo.getMainListRule15().stream()
								.filter(sampleSubgroup1 -> sampleSubgroup1.getMainStatisticValue() > value)
								.collect(Collectors.toList())).size() >= judementVo.getMainRule15().getLimitData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule15().getChartDetailType()
									+ judementVo.getMainRule15().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule15(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
					paraVo.getMainListRule16().add(sampleSubgroup);
					if (paraVo.getMainListRule16().size() >= judementVo.getMainRule16().getLengthData()) {
						Double value = mainStatistic.getEntiretyCl().doubleValue()
								- 2 * mainStatistic.getEntiretySigma().doubleValue();
						if ((paraVo.getMainListRule16().stream()
								.filter(sampleSubgroup1 -> sampleSubgroup1.getMainStatisticValue() < value)
								.collect(Collectors.toList())).size() >= judementVo.getMainRule16().getLimitData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule16().getChartDetailType()
									+ judementVo.getMainRule16().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule16(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule17())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getMainStatisticValue()
							.doubleValue() < mainStatistic.getEntiretyCl().doubleValue()
									+ mainStatistic.getEntiretySigma().doubleValue()
							&& sampleSubgroup.getMainStatisticValue()
									.doubleValue() > mainStatistic.getEntiretyCl().doubleValue()
											- mainStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setMainCountRule17(paraVo.getMainCountRule17() + 1);
						if (paraVo.getMainCountRule17() >= judementVo.getMainRule17().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule17().getChartDetailType()
									+ judementVo.getMainRule17().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule17(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getMainRule18())
						&& !ObjectUtils.isEmpty(mainStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getMainStatisticValue()
							.doubleValue() > mainStatistic.getEntiretyCl().doubleValue()
									+ mainStatistic.getEntiretySigma().doubleValue()
							|| sampleSubgroup.getMainStatisticValue()
									.doubleValue() < mainStatistic.getEntiretyCl().doubleValue()
											- mainStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setMainCountRule18(paraVo.getMainCountRule18() + 1);
						if (paraVo.getMainCountRule18() >= judementVo.getMainRule18().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getMainRule18().getChartDetailType()
									+ judementVo.getMainRule18().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getMainRule18(), mainStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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

			// *****************次图判异**************************

			// 排除没值和没整体计算的点参与计算 （单值实体控制第一个点）
			if (!ObjectUtils.isEmpty(sampleSubgroup.getSecondStatisticValue())
					&& !ObjectUtils.isEmpty(secondStatistic)) {

				// 规则1 规格上限上方的#个点 Xi大于规格上限,点必须在规格上限上方 最大绘点数范围内的点全部重新计算OOC
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule1())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyUsl())
						&& sampleSubgroup.getSecondStatisticValue().doubleValue() > secondStatistic.getEntiretyUsl()
								.doubleValue()) {
					paraVo.setSecondCountRule1(paraVo.getSecondCountRule1() + 1);
					if (paraVo.getSecondCountRule1() >= judementVo.getSecondRule1().getLengthData()) {
						String oocKey = sampleSubgroup.getSampleSubgroupId()
								+ judementVo.getSecondRule1().getChartDetailType()
								+ judementVo.getSecondRule1().getJudgementId();
						if (existsOocList.stream()
								.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
										+ ooc.getJudgementId()).equals(oocKey))
								.collect(Collectors.toList()).size() <= 0) {
							// 当前点违反规则
							OocR ooc = new OocR();
							ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule1(), secondStatistic,
									firstSampleSubgroup, lastSampleSubgroup, entity,
									groupCode + sampleSubgroup.getSubgroupNum());
							if (!oocList.contains(ooc)) {
								oocList.add(ooc);
							}

							OocR ooc1 = new OocR();
							ooc1.setOocId(ooc.getOocId());
							ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
							ooc1.setChartDetailType(ooc.getChartDetailType());
							ooc1.setJudgementId(ooc.getJudgementId());
							existsOocList.add(ooc1);

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
				// 规则2 规格下限下方的#个点 Xi小于规格下限,点必须在规格下限下方 最大绘点数范围内的点全部重新计算OOC
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule2())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyLsl())
						&& sampleSubgroup.getSecondStatisticValue().doubleValue() < secondStatistic.getEntiretyLsl()
								.doubleValue()) {
					paraVo.setSecondCountRule2(paraVo.getSecondCountRule2() + 1);
					if (paraVo.getSecondCountRule2() >= judementVo.getSecondRule2().getLengthData()) {
						String oocKey = sampleSubgroup.getSampleSubgroupId()
								+ judementVo.getSecondRule2().getChartDetailType()
								+ judementVo.getSecondRule2().getJudgementId();
						if (existsOocList.stream()
								.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
										+ ooc.getJudgementId()).equals(oocKey))
								.collect(Collectors.toList()).size() <= 0) {
							OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule2(), secondStatistic,
									firstSampleSubgroup, lastSampleSubgroup, entity,
									groupCode + sampleSubgroup.getSubgroupNum());
							if (!oocList.contains(ooc)) {
								oocList.add(ooc);
							}

							OocR ooc1 = new OocR();
							ooc1.setOocId(ooc.getOocId());
							ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
							ooc1.setChartDetailType(ooc.getChartDetailType());
							ooc1.setJudgementId(ooc.getJudgementId());
							existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule3())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyUcl())
						&& sampleSubgroup.getSecondStatisticValue().doubleValue() > secondStatistic.getEntiretyUcl()
								.doubleValue()) {
					paraVo.setSecondCountRule3(paraVo.getSecondCountRule3() + 1);
					if (paraVo.getSecondCountRule3() >= judementVo.getSecondRule3().getLengthData()) {
						String oocKey = sampleSubgroup.getSampleSubgroupId()
								+ judementVo.getSecondRule3().getChartDetailType()
								+ judementVo.getSecondRule3().getJudgementId();
						if (existsOocList.stream()
								.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
										+ ooc.getJudgementId()).equals(oocKey))
								.collect(Collectors.toList()).size() <= 0) {
							OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule3(), secondStatistic,
									firstSampleSubgroup, lastSampleSubgroup, entity,
									groupCode + sampleSubgroup.getSubgroupNum());
							if (!oocList.contains(ooc)) {
								oocList.add(ooc);
							}

							OocR ooc1 = new OocR();
							ooc1.setOocId(ooc.getOocId());
							ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
							ooc1.setChartDetailType(ooc.getChartDetailType());
							ooc1.setJudgementId(ooc.getJudgementId());
							existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule4())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyLcl())
						&& sampleSubgroup.getSecondStatisticValue().doubleValue() < secondStatistic.getEntiretyLcl()
								.doubleValue()) {
					paraVo.setSecondCountRule4(paraVo.getSecondCountRule4() + 1);
					if (paraVo.getSecondCountRule4() >= judementVo.getSecondRule4().getLengthData()) {
						String oocKey = sampleSubgroup.getSampleSubgroupId()
								+ judementVo.getSecondRule4().getChartDetailType()
								+ judementVo.getSecondRule4().getJudgementId();
						if (existsOocList.stream()
								.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
										+ ooc.getJudgementId()).equals(oocKey))
								.collect(Collectors.toList()).size() <= 0) {
							OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule4(), secondStatistic,
									firstSampleSubgroup, lastSampleSubgroup, entity,
									groupCode + sampleSubgroup.getSubgroupNum());
							if (!oocList.contains(ooc)) {
								oocList.add(ooc);
							}

							OocR ooc1 = new OocR();
							ooc1.setOocId(ooc.getOocId());
							ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
							ooc1.setChartDetailType(ooc.getChartDetailType());
							ooc1.setJudgementId(ooc.getJudgementId());
							existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule5())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getSecondStatisticValue()
							.doubleValue() >= secondStatistic.getEntiretyCl().doubleValue()
									+ 2 * secondStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setSecondCountRule5(paraVo.getSecondCountRule5() + 1);
						if (paraVo.getSecondCountRule5() >= judementVo.getSecondRule5().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule5().getChartDetailType()
									+ judementVo.getSecondRule5().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule5(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule6())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getSecondStatisticValue()
							.doubleValue() >= secondStatistic.getEntiretyCl().doubleValue()
									+ secondStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setSecondCountRule6(paraVo.getSecondCountRule6() + 1);
						if (paraVo.getSecondCountRule6() >= judementVo.getSecondRule6().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule6().getChartDetailType()
									+ judementVo.getSecondRule6().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule6(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						paraVo.setSecondCountRule6(0);
					}
				}

				// 规则7 上部区域C中或上方的#个连续点 Xi大于等于CL 最大绘点数范围内的点重新计算
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule7())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getSecondStatisticValue().doubleValue() >= secondStatistic.getEntiretyCl()
							.doubleValue()) {
						paraVo.setSecondCountRule7(paraVo.getSecondCountRule7() + 1);
						if (paraVo.getSecondCountRule7() >= judementVo.getSecondRule7().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule7().getChartDetailType()
									+ judementVo.getSecondRule7().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule7(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						paraVo.setSecondCountRule7(0);
					}
				}

				// 规则8 下部区域C中或下方的#个连续点 Xi小于等于CL 最大绘点数范围内的点重新计算
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule8())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getSecondStatisticValue().doubleValue() <= secondStatistic.getEntiretyCl()
							.doubleValue()) {
						paraVo.setSecondCountRule8(paraVo.getSecondCountRule8() + 1);
						if (paraVo.getSecondCountRule8() >= judementVo.getSecondRule8().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule8().getChartDetailType()
									+ judementVo.getSecondRule8().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule8(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						paraVo.setSecondCountRule8(0);
					}
				}

				// 规则9 下部区域B中或下方的#个连续点 Xi小于等于（CL-σ） 最大绘点数范围内的点重新计算
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule9())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getSecondStatisticValue()
							.doubleValue() <= secondStatistic.getEntiretyCl().doubleValue()
									- secondStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setSecondCountRule9(paraVo.getSecondCountRule9() + 1);
						if (paraVo.getSecondCountRule9() >= judementVo.getSecondRule9().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule9().getChartDetailType()
									+ judementVo.getSecondRule9().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule9(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						paraVo.setSecondCountRule9(0);
					}
				}

				// 规则10 下部区域A中或下方的#个连续点 Xi小于等于（CL-2σ） 最大绘点数范围内的点重新计算
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule10())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getSecondStatisticValue()
							.doubleValue() <= secondStatistic.getEntiretyCl().doubleValue()
									- 2 * secondStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setSecondCountRule10(paraVo.getSecondCountRule10() + 1);
						if (paraVo.getSecondCountRule10() >= judementVo.getSecondRule10().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule10().getChartDetailType()
									+ judementVo.getSecondRule10().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule10(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						paraVo.setSecondCountRule10(0);
					}
				}

				if (rowNum == 2 && ObjectUtils.isEmpty(sampleSubgroupList.get(rowNum - 2).getSecondStatisticValue())) {
					continue;
				}

				// 规则11 连续#个点递增或递减 连续递增：Xi-Xi-1>0 ; 连续递减：Xi-Xi-1<0
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule11())) {
					// 考虑单值移动极差第一个点没值的情况
					if (rowNum == 1 || (rowNum == 2 && sampleSubgroupList.get(1).getSecondStatisticValue() == null)) {
						paraVo.setSecondCountRule11A(1);
						paraVo.setSecondCountRule11B(1);
					} else {
						// 递增判断
						if (sampleSubgroupList.get(rowNum - 1).getSecondStatisticValue().doubleValue()
								- sampleSubgroupList.get(rowNum - 2).getSecondStatisticValue().doubleValue() > 0) {
							paraVo.setSecondCountRule11A(paraVo.getSecondCountRule11A() + 1);
						} else {
							paraVo.setSecondCountRule11A(1);
						}
						// 递减判断
						if (sampleSubgroupList.get(rowNum - 1).getSecondStatisticValue().doubleValue()
								- sampleSubgroupList.get(rowNum - 2).getSecondStatisticValue().doubleValue() < 0) {
							paraVo.setSecondCountRule11B(paraVo.getSecondCountRule11B() + 1);
						} else {
							paraVo.setSecondCountRule11B(1);
						}

						if (paraVo.getSecondCountRule11A() >= judementVo.getSecondRule11().getLengthData()
								|| paraVo.getSecondCountRule11B() >= judementVo.getSecondRule11().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule11().getChartDetailType()
									+ judementVo.getSecondRule11().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule11(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule12())) {
					if (rowNum == 1 || (rowNum == 2 && sampleSubgroupList.get(1).getSecondStatisticValue() == null)) {
						paraVo.setSecondCountRule12(1);
						paraVo.setSecondFlagRule12("");
					} else {
						deltaValue = sampleSubgroupList.get(rowNum - 1).getSecondStatisticValue()
								- sampleSubgroupList.get(rowNum - 2).getSecondStatisticValue();

						if (StringUtils.isEmpty(paraVo.getSecondFlagRule12())) {
							// 上一个点位第一个点 上一个符号未定
							if (deltaValue != 0) {
								paraVo.setSecondCountRule12(paraVo.getSecondCountRule12() + 1);
								if (deltaValue > 0) {
									paraVo.setSecondFlagRule12("+");
								} else if (deltaValue < 0) {
									paraVo.setSecondFlagRule12("-");
								}
							} else {
								paraVo.setSecondCountRule12(1);
								paraVo.setSecondFlagRule12("");
							}
						} else if (deltaValue == 0D) {
							// 当前点和上一个点值相等
							paraVo.setSecondCountRule12(1);
							paraVo.setSecondFlagRule12("");
						} else if (deltaValue > 0) {
							// 当前点比上一个点大
							if ("-".equals(paraVo.getSecondFlagRule12())) {
								paraVo.setSecondCountRule12(paraVo.getSecondCountRule12() + 1);
							} else {
								paraVo.setSecondCountRule12(2);
							}
							paraVo.setSecondFlagRule12("+");
						} else if (deltaValue < 0) {
							// 当前点比上一个点小
							if ("+".equals(paraVo.getSecondFlagRule12())) {
								paraVo.setSecondCountRule12(paraVo.getSecondCountRule12() + 1);
							} else {
								paraVo.setSecondCountRule12(2);
							}
							paraVo.setSecondFlagRule12("-");
						}

						if (paraVo.getSecondCountRule12() >= judementVo.getSecondRule12().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule12().getChartDetailType()
									+ judementVo.getSecondRule12().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule12(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule13())) {
					if (paraVo.getSecondListRule13().size() >= judementVo.getSecondRule13().getLengthData()) {
						paraVo.getSecondListRule13().remove(0);
					}
					paraVo.getSecondListRule13().add(sampleSubgroup);
					if (paraVo.getSecondListRule13().size() >= judementVo.getSecondRule13().getLengthData()) {
						Double value = secondStatistic.getEntiretyCl().doubleValue()
								+ secondStatistic.getEntiretySigma().doubleValue();
						if ((paraVo.getSecondListRule13().stream()
								.filter(sampleSubgroup1 -> sampleSubgroup1.getSecondStatisticValue() > value)
								.collect(Collectors.toList())).size() >= judementVo.getSecondRule13().getLimitData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule13().getChartDetailType()
									+ judementVo.getSecondRule13().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule13(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule14())) {
					if (paraVo.getSecondListRule14().size() >= judementVo.getSecondRule14().getLengthData()) {
						paraVo.getSecondListRule14().remove(0);
					}
					paraVo.getSecondListRule14().add(sampleSubgroup);
					if (paraVo.getSecondListRule14().size() >= judementVo.getSecondRule14().getLengthData()) {
						Double value = secondStatistic.getEntiretyCl().doubleValue()
								- secondStatistic.getEntiretySigma().doubleValue();
						if ((paraVo.getSecondListRule14().stream()
								.filter(sampleSubgroup1 -> sampleSubgroup1.getSecondStatisticValue() < value)
								.collect(Collectors.toList())).size() >= judementVo.getSecondRule14().getLimitData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule14().getChartDetailType()
									+ judementVo.getSecondRule14().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule14(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule15())) {
					if (paraVo.getSecondListRule15().size() >= judementVo.getSecondRule15().getLengthData()) {
						paraVo.getSecondListRule15().remove(0);
					}
					paraVo.getSecondListRule15().add(sampleSubgroup);
					if (paraVo.getSecondListRule15().size() >= judementVo.getSecondRule15().getLengthData()) {
						Double value = secondStatistic.getEntiretyCl().doubleValue()
								+ 2 * secondStatistic.getEntiretySigma().doubleValue();
						if ((paraVo.getSecondListRule15().stream()
								.filter(sampleSubgroup1 -> sampleSubgroup1.getSecondStatisticValue() > value)
								.collect(Collectors.toList())).size() >= judementVo.getSecondRule15().getLimitData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule15().getChartDetailType()
									+ judementVo.getSecondRule15().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule15(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule16())) {
					if (paraVo.getSecondListRule16().size() >= judementVo.getSecondRule16().getLengthData()) {
						paraVo.getSecondListRule16().remove(0);
					}
					paraVo.getSecondListRule16().add(sampleSubgroup);
					if (paraVo.getSecondListRule16().size() >= judementVo.getSecondRule16().getLengthData()) {
						Double value = secondStatistic.getEntiretyCl().doubleValue()
								- 2 * secondStatistic.getEntiretySigma().doubleValue();
						if ((paraVo.getSecondListRule16().stream()
								.filter(sampleSubgroup1 -> sampleSubgroup1.getSecondStatisticValue() < value)
								.collect(Collectors.toList())).size() >= judementVo.getSecondRule16().getLimitData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule16().getChartDetailType()
									+ judementVo.getSecondRule16().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule16(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule17())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getSecondStatisticValue()
							.doubleValue() < secondStatistic.getEntiretyCl().doubleValue()
									+ secondStatistic.getEntiretySigma().doubleValue()
							&& sampleSubgroup.getSecondStatisticValue()
									.doubleValue() > secondStatistic.getEntiretyCl().doubleValue()
											- secondStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setSecondCountRule17(paraVo.getSecondCountRule17() + 1);
						if (paraVo.getSecondCountRule17() >= judementVo.getSecondRule17().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule17().getChartDetailType()
									+ judementVo.getSecondRule17().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule17(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						paraVo.setSecondCountRule17(0);
					}
				}

				// 规则18 连续#个点落在中心线两侧且无一在C区内 Xi>CL+σ或Xi<CL-σ 最大绘点数范围内的点重新计算
				if (!ObjectUtils.isEmpty(judementVo.getSecondRule18())
						&& !ObjectUtils.isEmpty(secondStatistic.getEntiretyCl())) {
					if (sampleSubgroup.getSecondStatisticValue()
							.doubleValue() > secondStatistic.getEntiretyCl().doubleValue()
									+ secondStatistic.getEntiretySigma().doubleValue()
							|| sampleSubgroup.getSecondStatisticValue()
									.doubleValue() < secondStatistic.getEntiretyCl().doubleValue()
											- secondStatistic.getEntiretySigma().doubleValue()) {
						paraVo.setSecondCountRule18(paraVo.getSecondCountRule18() + 1);
						if (paraVo.getSecondCountRule18() >= judementVo.getSecondRule18().getLengthData()) {
							String oocKey = sampleSubgroup.getSampleSubgroupId()
									+ judementVo.getSecondRule18().getChartDetailType()
									+ judementVo.getSecondRule18().getJudgementId();
							if (existsOocList.stream()
									.filter(ooc -> (ooc.getSampleSubgroupId() + ooc.getChartDetailType()
											+ ooc.getJudgementId()).equals(oocKey))
									.collect(Collectors.toList()).size() <= 0) {
								OocR ooc = initOoc(sampleSubgroup, chart, judementVo.getSecondRule18(), secondStatistic,
										firstSampleSubgroup, lastSampleSubgroup, entity,
										groupCode + sampleSubgroup.getSubgroupNum());
								if (!oocList.contains(ooc)) {
									oocList.add(ooc);
								}

								OocR ooc1 = new OocR();
								ooc1.setOocId(ooc.getOocId());
								ooc1.setSampleSubgroupId(ooc.getSampleSubgroupId());
								ooc1.setChartDetailType(ooc.getChartDetailType());
								ooc1.setJudgementId(ooc.getJudgementId());
								existsOocList.add(ooc1);

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
						paraVo.setSecondCountRule18(0);
					}
				}
			}
		}

		oocVO.setOocList(new ArrayList<>(oocList));
		oocVO.setMessageList(messageList);
		oocVO.setMessageDetailList(messageDetaillist);
		oocVO.setMessageUploadRelList(messageUploadRellist);

		return oocVO;
	}

	/**
	 * 实例化OOC对象
	 *
	 * @param sampleSubgroup
	 * @param chart
	 * @param judgement
	 * @param entiretyStatistic
	 * @param firstSampleSubgroup
	 * @param lastSampleSubgroup
	 * @param entity
	 * @return
	 */
	private OocR initOoc(SampleSubgroupR sampleSubgroup, ChartR chart, JudgementR judgement,
			EntiretyStatisticR entiretyStatistic, SampleSubgroupR firstSampleSubgroup, SampleSubgroupR lastSampleSubgroup,
			EntityR entity, String groupCode) {
		OocR ooc = new OocR();
		ooc.setOocId(sampleSubgroup.getTenantId() + "," + sampleSubgroup.getSiteId() + ","
				+ sampleSubgroup.getSampleSubgroupId() + "," + judgement.getJudgementId() + ","
				+ judgement.getChartDetailType() + "," + sampleSubgroup.getEntityCode() + ","
				+ sampleSubgroup.getEntityVersion());
		ooc.setOocStatus("UNPROCESSED");
		ooc.setTenantId(sampleSubgroup.getTenantId());
		ooc.setSiteId(sampleSubgroup.getSiteId());
		ooc.setSampleSubgroupId(sampleSubgroup.getSampleSubgroupId());
		ooc.setEntityCode(sampleSubgroup.getEntityCode());
		ooc.setEntityVersion(sampleSubgroup.getEntityVersion());

		ooc.setMaxPlotPoints(chart.getMaxPlotPoints());
		ooc.setTickLabelX(chart.getxTickLabel());
		// 获取控制图明细
		ChartDetailR chartDetail = new ChartDetailR();
		for (ChartDetailR detail : chart.getChartDetailList()) {
			if (detail.getChartDetailType().equals(judgement.getChartDetailType())) {
				chartDetail = detail;
				break;
			}
		}
		ooc.setAxisLabelX(chartDetail.getxAxisLabel());
		ooc.setAxisLabelY(chartDetail.getyAxisLabel());

		ooc.setSpecTarget(chartDetail.getSpecTarget());
		ooc.setUpperControlLimit(entiretyStatistic.getEntiretyUcl());
		ooc.setCenterLine(entiretyStatistic.getEntiretyCl());
		ooc.setLowerControlLimit(entiretyStatistic.getEntiretyLcl());
		ooc.setUpperSpecLimit(entiretyStatistic.getEntiretyUsl());
		ooc.setLowerSpecLimit(entiretyStatistic.getEntiretyLsl());

		ooc.setJudgementId(judgement.getJudgementId());
		ooc.setChartDetailType(judgement.getChartDetailType());
		ooc.setFirstSubgroupNum(firstSampleSubgroup.getSubgroupNum());
		ooc.setLastSubgroupNum(lastSampleSubgroup.getSubgroupNum());

		// 生成消息
		if (judgement.getMessageTypeDetailList() != null && judgement.getMessageTypeDetailList().size() > 0) {
			MessageR message = new MessageR();
			message.setMessageId(ooc.getOocId());
			message.setTenantId(ooc.getTenantId());
			message.setSiteId(ooc.getSiteId());
			message.setOocId(ooc.getOocId());
			message.setEntityCode(ooc.getEntityCode());
			message.setEntityVersion(ooc.getEntityVersion());
			message.setGroupCode(groupCode);// 分组(实体控制图为单位)
			message.setMessageStatus("N");

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
				messageDetail.setEntityCode(message.getEntityCode());
				messageDetail.setEntityVersion(message.getEntityVersion());
				messageDetail.setElementCategory(messageTypeDetail.getElementCategory());
				messageDetail.setElementCode(messageTypeDetail.getElementCode());
				messageDetail.setElementDescription(messageTypeDetail.getElementDescription());
				messageDetail.setElementStatus(messageTypeDetail.getElementStatus());

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
						messageDetail.setElementValueCode(sampleSubgroup.getSampleDatas());
						messageDetail.setElementValueDescription(sampleSubgroup.getSampleDatas());
					} else if ("CONTROL_LIMIT".equals(messageTypeDetail.getElementCode())) {
						// 控制限
						messageDetail.setElementValueCode("UCL：" + ooc.getUpperControlLimit() + "，CL："
								+ ooc.getCenterLine() + "，LCL：" + ooc.getLowerControlLimit());
						messageDetail.setElementValueDescription("控制上限：" + ooc.getUpperControlLimit() + "，中心线："
								+ ooc.getCenterLine() + "，控制下限：" + ooc.getLowerControlLimit());
					} else if ("SPEC_LIMIT".equals(messageTypeDetail.getElementCode())) {
						// 规格限
						messageDetail.setElementValueCode("USL：" + ooc.getUpperSpecLimit() + "，ST："
								+ ooc.getSpecTarget() + "，LSL：" + ooc.getLowerSpecLimit());
						messageDetail.setElementValueDescription("规格上限：" + ooc.getUpperSpecLimit() + "，目标值："
								+ ooc.getSpecTarget() + "，规格下限：" + ooc.getLowerSpecLimit());
					} else if ("SAMPLE_START_TIME".equals(messageTypeDetail.getElementCode())) {
						// 样本开始时间
						SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
						messageDetail.setElementValueCode(sdf.format(firstSampleSubgroup.getSampleSubgroupTime()));
						messageDetail
								.setElementValueDescription(sdf.format(firstSampleSubgroup.getSampleSubgroupTime()));
					} else if ("SAMPLE_END_TIME".equals(messageTypeDetail.getElementCode())) {
						// 样本结束时间
						SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
						messageDetail.setElementValueCode(sdf.format(lastSampleSubgroup.getSampleSubgroupTime()));
						messageDetail
								.setElementValueDescription(sdf.format(lastSampleSubgroup.getSampleSubgroupTime()));
					} else if ("SUBGROUP_NUMBER".equals(messageTypeDetail.getElementCode())) {
						// 样本组号
						messageDetail.setElementValueCode(judgement.getMessageTypeCode());
						messageDetail.setElementValueDescription(sampleSubgroup.getSubgroupNum().toString());
					} else if ("JUDGEMENT_SHORT_CODE".equals(messageTypeDetail.getElementCode())) {
						// 编码简称
						messageDetail.setElementValueCode(judgement.getJudgementShortCode());
						messageDetail.setElementValueDescription(judgement.getJudgementShortCode());
					}

				}

				messageDetailList.add(messageDetail);
			}
			message.setMessageDetailList(messageDetailList);

			ooc.setMessage(message);
			ooc.setMessageDetailList(messageDetailList);
			ooc.setMessageUploadRelList(messageUploadConfigList);
		}

		return ooc;
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
