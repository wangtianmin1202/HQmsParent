package com.hand.hqm.hqm_nonconforming_order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fqc_inspection_d.dto.FqcInspectionD;
import com.hand.hqm.hqm_fqc_inspection_d.mapper.FqcInspectionDMapper;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService;
import com.hand.hqm.hqm_fqc_inspection_h.service.impl.FqcInspectionHServiceImpl;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_fqc_inspection_l.mapper.FqcInspectionLMapper;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.mapper.IqcInspectionHMapper;
import com.hand.hqm.hqm_iqc_inspection_h.service.IIqcInspectionHService;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.idto.FqcInpectionDealMethod;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;
import com.hand.hqm.hqm_nonconforming_order.service.INonconformingOrderService;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;
import com.hand.hqm.hqm_qc_task.mapper.FqcTaskMapper;
import com.hand.hqm.hqm_qc_task.mapper.IqcTaskMapper;
import com.hand.hqm.hqm_qc_task.service.IFqcTaskService;
import com.hand.hqm.hqm_qc_task.service.IIqcTaskService;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContextUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class NonconformingOrderServiceImpl extends BaseServiceImpl<NonconformingOrder>
		implements INonconformingOrderService {
	@Autowired
	NonconformingOrderMapper nonconformingOrderMapper;
	@Autowired
	IqcInspectionHMapper iqcInspectionHMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IqcTaskMapper iqcTaskMapper;
	@Autowired
	IIqcTaskService iIqcTaskService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;
	@Autowired
	FqcInspectionHMapper fqcInspectionHMapper;
	@Autowired
	FqcInspectionLMapper fqcInspectionLMapper;
	@Autowired
	FqcInspectionDMapper fqcInspectionDMapper;
	@Autowired
	FqcTaskMapper fqcTaskMapper;
	@Autowired
	IFqcTaskService iFqcTaskService;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	private IActivitiService activitiService;
	@Autowired
	IIqcInspectionHService iIqcInspectionHService;
	@Autowired
	IFqcInspectionHService iFqcInspectionHService;
	Logger logger = LoggerFactory.getLogger(FqcInspectionHServiceImpl.class);

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@Override
	public List<NonconformingOrder> myselect(IRequest requestContext, NonconformingOrder dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return nonconformingOrderMapper.myselect(dto);
	}

	/**
	 * 新建
	 * 
	 * @description
	 * 
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@Override
	public ResponseData addNewInspection(NonconformingOrder dto, IRequest requestCtx, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		if (dto.getPlantId() == null || dto.getNoSourceType() == null || dto.getInspectionType() == null) {
			// 检验必输项
			responseData.setSuccess(false);
			responseData.setMessage(getMessageByPromptCode(request, "必输项未填写完整"));
			return responseData;
		}
		if (dto.getNoNum() == null) {
			NonconformingOrder head = new NonconformingOrder();
			if (dto.getInspectionId() == null) {

			} else {
				head.setInspectionId(dto.getInspectionId());
				head = nonconformingOrderMapper.selectOne(head);
				if (head == null) {
				} else {
					responseData.setSuccess(false);
					responseData.setMessage(getMessageByPromptCode(request, "error.hqm_nonconforming_order_create_02"));
					return responseData;
				}
			}
		}

		// 计算不合格率
		/*
		 * float sum =dto.getSampleSize(); float ngQty =dto.getNonconformingQty();
		 * 
		 * String rate; if(dto.getSampleSize()==null||dto.getNonconformingQty()==null) {
		 * rate= null; } rate= String.valueOf(ngQty/sum);
		 * dto.setNonconformingRate(rate);
		 */

		// 根据单号查询数据库中是否存在
		// List<NonconformingOrder> data_have = nonconformingOrderMapper.myselect(dto);
		if (dto.getNoNum() == null) {

			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String inspectionNum;// 检验单号
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String time = dateFormat.format(date);
			String inspectionNumStart = "NG" + dto.getPlantCode() + "-" + time.substring(2) + "-";
			// 流水一个检验单号
			inspectionNum = getInspectionNumber(inspectionNumStart);
			dto.setNoNum(inspectionNum);

			// 新增一条数据
			nonconformingOrderMapper.insertSelective(dto);
			List<NonconformingOrder> data_have = nonconformingOrderMapper.myselect(dto);
			responseData.setSuccess(true);
			responseData.setRows(data_have);

			// 发起流程
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(String.valueOf(dto.getNoId()));
			if ("IQC".equals(dto.getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("iqcUnqualityProcess");
				activitiService.startProcess(requestCtx, instanceCreateRequest);
			} else if ("FQC".equals(dto.getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("fqcUnqualityProcess");
				activitiService.startProcess(requestCtx, instanceCreateRequest);
			}

			return responseData;
		} else {
			// 如果不为空
			nonconformingOrderMapper.updateByPrimaryKeySelective(dto);
			List<NonconformingOrder> data_have = nonconformingOrderMapper.myselect(dto);
			responseData.setSuccess(true);
			responseData.setRows(data_have);
			return responseData;
		}
	}

	/**
	 * 更新
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@Override
	public ResponseData updateInspection(NonconformingOrder dto, IRequest requestCtx, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();

		/**
		 * 添加一个新的修改 customerCheck() added by wtm 20191225
		 */
		customerCheck(requestCtx, dto);

		if (dto.getPlantId() == null || dto.getNoSourceType() == null || dto.getInspectionType() == null) {
			// 检验必输项
			responseData.setSuccess(false);
			responseData.setMessage(getMessageByPromptCode(request, "必输项未填写完整"));
			return responseData;
		}
		nonconformingOrderMapper.updateByPrimaryKeySelective(dto);
		return new ResponseData();
	}

	/**
	 * @description 保存时的校验
	 * @author tianmin.wang
	 * @date 2019年12月25日
	 * @param requestCtx
	 * @param dto
	 */
	private void customerCheck(IRequest requestCtx, NonconformingOrder dto) {
		// TODO 新增校验

		if (dto.getInspectionType() != null && dto.getInspectionType().equals("IQC")
				&& StringUtil.isEmpty(dto.getIssueSourceType())) {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
					"error.hqm_nonconforming_create_101"));// IQC类型时 问题来源必输
		}
		if ("2".equals(dto.getTopIssueSourceType())) { // 如果为类型判定 保存后不修改不合格处理单据的状态
			return;
		}
		if ("1".equals(dto.getTopIssueSourceType())) {
			if (StringUtil.isEmpty(dto.getDealMethod())) {
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"error.hqm_nonconforming_create_1001"));// SQE判定处理方法必输
			}
		}
		if ("0".equals(dto.getNoStatus())) {
			if ("IQC".equals(dto.getInspectionType())) {
				// 若为IQC，判断是否类型判定，类型判定不更新状态
				NonconformingOrder nonconformingOrder = new NonconformingOrder();
				nonconformingOrder.setNoId(dto.getNoId());
				List<NonconformingOrder> nonconformingOrderList = nonconformingOrderMapper.select(nonconformingOrder);
				if (nonconformingOrderList != null && nonconformingOrderList.size() > 0) {
					if (nonconformingOrderList.get(0).getIssueSourceType() != null
							&& !"".equals(nonconformingOrderList.get(0).getIssueSourceType())) {
						dto.setNoStatus("1");
					}
				}
			} else {
				dto.setNoStatus("1");
			}
		} else if ("3".equals(dto.getNoStatus())) {
			dto.setNoStatus("1");
		} else {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
					"error.hqm_nonconforming_create_04"));
		}
	}

	/**
	 * 发布
	 * 
	 * @param dto     操作数据集
	 * @param request 请求
	 * @return 操作结果
	 */
	@Override
	public ResponseData publish(NonconformingOrder dto, IRequest requestCtx, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		if (dto.getNoStatus() == "1") {
			// 检验必输项
			responseData.setSuccess(false);
			responseData.setMessage(getMessageByPromptCode(request, "已发布的不合格处理单不允许再发布"));
			return responseData;
		}
		dto.setNoStatus("1");
		nonconformingOrderMapper.updateByPrimaryKeySelective(dto);
		return new ResponseData();
	}

	public String getInspectionNumber(String inspectionNumStart) {
		Integer count = 1;
		NonconformingOrder sr = new NonconformingOrder();
		sr.setNoNum(inspectionNumStart);
		List<NonconformingOrder> list = new ArrayList<NonconformingOrder>();
		list = nonconformingOrderMapper.selectMaxNumber(sr);
		if (list != null && list.size() > 0) {
			String NoNum = list.get(0).getNoNum();
			String number = NoNum.substring(NoNum.length() - 5);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%05d", count);
		return inspectionNumStart + str;// 最终检验单号
	}

	/**
	 * 获取code对应的维护信息
	 * 
	 * @param request
	 * @param code
	 * @return
	 */
	public String getMessageByPromptCode(HttpServletRequest request, String code) {
		String returnString = "first info";
		try {
			returnString = iPromptService.getPromptDescription(
					RequestContextUtils.getLocale(request).getLanguage().equals("zh") ? "zh_CN" : "en_GB", code);
		} catch (Exception e) {
			returnString = "get code " + code + " error" + e.getMessage();
		}
		if (returnString == null || returnString.equals("")) {
			returnString = code;
		}
		return returnString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_nonconforming_order.service.INonconformingOrderService#audit
	 * (com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public void audit(IRequest requestCtx, List<NonconformingOrder> list, HttpServletRequest request) {
		// TODO 审核
		for (NonconformingOrder dto : list) {
			switch (dto.getInspectionType()) {
			case "IQC":
				auditIqc(requestCtx, dto, request);
				break;
			case "FQC":
				auditFqc(requestCtx, dto, request);
				break;
			case "OQC":
				break;
			case "PQC":
				break;
			}
		}
	}

	public void auditFqc(IRequest requestCtx, NonconformingOrder dto, HttpServletRequest request) {
		fqcTaskCreat(requestCtx, request, dto);
		FqcInspectionH fqcSearch = new FqcInspectionH();
		fqcSearch.setInspectionNum(dto.getInspectionCode());
		List<FqcInspectionH> res = fqcInspectionHMapper.select(fqcSearch);
		FqcInspectionL fqclSearch = new FqcInspectionL();
		fqclSearch.setHeaderId(res.get(0).getHeaderId());
		iFqcInspectionHService.chooseInspectionType(res.get(0), fqcInspectionLMapper.select(fqclSearch));
		pushToMes(dto);
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月3日
	 * @param requestCtx
	 * @param request
	 * @param dto
	 */
	private void fqcTaskCreat(IRequest requestCtx, HttpServletRequest request, NonconformingOrder dto) {
		if (!dto.getNoStatus().equals("1")) {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
					"error.hqm_nonconforming_create_01"));// 不合格单处理状态不为已发布，不允许审核
		}
		dto.setNoStatus("2");
		self().updateByPrimaryKeySelective(requestCtx, dto);
		/*
		 * 检验单号关联HQM_FQC_TASK 更新HQM_IQC_TASK的DEAL_METHOD为不合格处理单的DEAL_METHOD
		 */
		FqcInspectionH fqcHSearch = new FqcInspectionH();
		fqcHSearch.setHeaderId(dto.getInspectionId());
		List<FqcInspectionH> fqcHResult = fqcInspectionHMapper.select(fqcHSearch);
		if (fqcHResult != null && fqcHResult.size() > 0) {
			FqcTask fqcTaskSearch = new FqcTask();
			fqcTaskSearch.setInspectionNum(fqcHResult.get(0).getInspectionNum());
			List<FqcTask> fqcTaskResult = fqcTaskMapper.select(fqcTaskSearch);
			if (fqcTaskResult != null && fqcTaskResult.size() > 0) {
				fqcTaskResult.forEach(p -> {
					p.setDealMethod(dto.getDealMethod());
					iFqcTaskService.updateByPrimaryKeySelective(requestCtx, p);
				});
			}
		}
		/**
		 * 若DEAL_METHOD为 3 4 则往HQM_FQC_TASK新增一条数据
		 */
		if (StringUtil.isNotEmpty(dto.getDealMethod())
				&& ("4".equals(dto.getDealMethod()) || "3".equals(dto.getDealMethod()))) {
			// 找关联的FQC
			FqcInspectionH fqchSearch = new FqcInspectionH();
			fqchSearch.setHeaderId(dto.getInspectionId());
			fqchSearch = fqcInspectionHMapper.selectByPrimaryKey(fqchSearch);
			FqcTask insert = new FqcTask();
			insert.setPlantId(dto.getPlantId());
			insert.setSourceOrder(fqchSearch.getSourceOrder());
			insert.setSourceType("6");
			FqcTask fqcTaskEarlist = getFqcTaskEarlistBySourceOrder(fqchSearch.getSourceOrder());
			if (fqcTaskEarlist == null)
				return;
			insert.setProdLineId(fqcTaskEarlist.getProdLineId());
			insert.setItemId(fqcTaskEarlist.getItemId());
			insert.setItemVersion(fqcTaskEarlist.getItemVersion());
			insert.setSpreading(fqcTaskEarlist.getSpreading());
			insert.setProductionBatch(fqcTaskEarlist.getProductionBatch());
			insert.setInspectionType("FQC");
			insert.setProductQty(fqcTaskEarlist.getProductQty());
			insert.setProductDate(fqcTaskEarlist.getProductDate());
			insert.setRelOrder(fqcTaskEarlist.getInspectionNum());
			insert.setRecheckSampleWay(dto.getRecheckSampleWay());
			insert.setTaskFrom(dto.getDealMethod());
			iFqcTaskService.insertSelective(requestCtx, insert);
			try {
				iFqcTaskService.createFqc(insert, request);
				iFqcTaskService.updateByPrimaryKeySelective(requestCtx, insert);
			} catch (Exception e) {
				logger.warn("FQC creat error");
			}
		}
	}

	/**
	 * @description 获取最早的当前来源的数据
	 * @author tianmin.wang
	 * @date 2019年12月24日
	 * @param sourceOrder
	 * @return
	 */
	private FqcTask getFqcTaskEarlistBySourceOrder(String sourceOrder) {
		// TODO Auto-generated method stub
		List<FqcTask> res = fqcTaskMapper.getFqcTaskEarlistBySourceOrder(sourceOrder);
		if (res == null || res.size() == 0) {
			return null;
		}
		return res.get(0);
	}

	/**
	 * 
	 * @description fqcInpectionDealMethod
	 * @author tianmin.wang
	 * @date 2020年2月24日
	 * @param dto
	 */
	@Override
	public void pushToMes(NonconformingOrder dto) {
		FqcInpectionDealMethod idm = new FqcInpectionDealMethod();
		idm.plantCode = dto.getPlantCode();
		FqcInspectionH fihSearch = new FqcInspectionH();
		fihSearch.setInspectionNum(dto.getInspectionCode());
		List<FqcInspectionH> hResult = fqcInspectionHMapper.select(fihSearch);
		if (hResult == null || hResult.size() == 0)
			return;
		idm.orderNo = hResult.get(0).getSourceOrder();
		idm.inspectionNum = dto.getInspectionCode();
		idm.method = dto.getDealMethod();
		if ("1".equals(dto.getDealMethod())) {
			idm.reworkType = "1";
		} else {
			idm.reworkType = "2";
			List<FqcInspectionD> max = new ArrayList<FqcInspectionD>();
			FqcInspectionL lSearch = new FqcInspectionL();
			lSearch.setHeaderId(hResult.get(0).getHeaderId());
			List<FqcInspectionL> lineResult = fqcInspectionLMapper.select(lSearch);
			for (FqcInspectionL line : lineResult) {
				FqcInspectionD dataSearch = new FqcInspectionD();
				dataSearch.setLineId(line.getLineId());
				List<FqcInspectionD> dataResult = fqcInspectionDMapper.select(dataSearch);
				if (dataResult != null && dataResult.size() > max.size())
					max = dataResult;
			}
			idm.serialNumber = StringUtils.join(max.stream().filter(p -> "NG".equals(p.getSnStatus()))
					.map(FqcInspectionD::getSerialNumber).collect(Collectors.toList()), ",");

		}
		taskExecutor.execute(() -> {
			IfInvokeOutbound iio = new IfInvokeOutbound();
			ObjectMapper obj = new ObjectMapper();
			try {
				String param = obj.writeValueAsString(idm);
				ServiceRequest sr = new ServiceRequest();
				sr.setLocale("zh_CN");
				String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "MES_WS_URI");// 获取调用地址
				SoapPostUtil.Response re = SoapPostUtil.ticketSrmToMes(uri, "getInspectionDealMethod", param, iio);
				ifInvokeOutboundMapper.insertSelective(iio);
				logger.info(SoapPostUtil.getStringFromResponse(re));
			} catch (Exception e) {
				iio.setResponseContent(e.getMessage());
				iio.setResponseCode("E");
				ifInvokeOutboundMapper.insertSelective(iio);
				logger.warn(e.getMessage());
			}
		});
	}

	/**
	 * 
	 * @description IQC类型的不合格单据审核
	 * @author tianmin.wang
	 * @date 2019年12月24日
	 * @param requestCtx
	 * @param dto
	 */
	public void auditIqc(IRequest requestCtx, NonconformingOrder dto, HttpServletRequest request) {
		if (!dto.getNoStatus().equals("1")) {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
					"error.hqm_nonconforming_create_01"));// 不合格单处理状态不为已发布，不允许审核
		}
		dto.setNoStatus("2");
		self().updateByPrimaryKeySelective(requestCtx, dto);
		/*
		 * 检验单号关联HQM_IQC_TASK 更新HQM_IQC_TASK的DEAL_METHOD为不合格处理单的DEAL_METHOD
		 */
		IqcInspectionH iqcHSearch = new IqcInspectionH();
		iqcHSearch.setHeaderId(dto.getInspectionId());
		List<IqcInspectionH> iqcHResult = iqcInspectionHMapper.select(iqcHSearch);
		if (iqcHResult != null && iqcHResult.size() > 0) {
			IqcTask iqcTaskSearch = new IqcTask();
			iqcTaskSearch.setInspectionNum(iqcHResult.get(0).getInspectionNum());
			List<IqcTask> iqcTaskResult = iqcTaskMapper.select(iqcTaskSearch);
			if (iqcTaskResult != null && iqcTaskResult.size() > 0) {
				iqcTaskResult.forEach(p -> {
					p.setDealMethod(dto.getDealMethod());
					iIqcTaskService.updateByPrimaryKeySelective(requestCtx, p);
				});
			}
		}
		/**
		 * 若DEAL_METHOD为 4 则往HQM_IQC_TASK新增一条数据
		 */
		if (StringUtil.isNotEmpty(dto.getDealMethod()) && "4".equals(dto.getDealMethod())) {
			IqcInspectionH iqchSearch = new IqcInspectionH();
			iqchSearch.setHeaderId(dto.getInspectionId());
			iqchSearch = iqcInspectionHMapper.selectByPrimaryKey(iqchSearch);
			IqcTask insert = new IqcTask();
			insert.setPlantId(dto.getPlantId());
			insert.setSourceOrder(iqchSearch.getSourceOrder());
			insert.setSourceType("0");

			IqcTask iqcTaskEarlist = getIqcTaskEarlistBySourceOrder(iqchSearch.getSourceOrder());
			if (iqcTaskEarlist == null) {
				return;
			}
			insert.setItemId(iqcTaskEarlist.getItemId());
			insert.setItemVersion(iqcTaskEarlist.getItemVersion());
			insert.setSpreading(iqcTaskEarlist.getSpreading());
			insert.setProductionBatch(iqcTaskEarlist.getProductionBatch());
			insert.setSupplierId(iqcTaskEarlist.getSupplierId());
			insert.setSupplierSiteId(iqcTaskEarlist.getSupplierSiteId());
			insert.setInspectionType("IQC");
			insert.setReceiveQty(iqcTaskEarlist.getReceiveQty());
			insert.setReceiveDate(iqcTaskEarlist.getReceiveDate());
			insert.setPackQty(iqcTaskEarlist.getPackQty());
			insert.setPacketInfo(iqcTaskEarlist.getPacketInfo());
			insert.setPoNumber(iqcTaskEarlist.getPoNumber());
			insert.setRecipientNum(iqcTaskEarlist.getRecipientNum());
			insert.setRecheckSampleWay(dto.getRecheckSampleWay());
			iIqcTaskService.insertSelective(requestCtx, insert);
			// IQC审核 发送到 WMS
			iIqcInspectionHService.conclusionToWMS(iqcHResult.get(0));
			try {
				iIqcTaskService.createIqc(insert, request); // 生成报告的逻辑
				iIqcTaskService.updateByPrimaryKeySelective(requestCtx, insert);
			} catch (Exception e) {
				logger.warn("IQC creat error");
			}
		}
	}

	/**
	 * @description 获取最早的当前来源的数据
	 * @author tianmin.wang
	 * @date 2019年12月24日
	 * @param sourceOrder
	 * @return
	 */
	private IqcTask getIqcTaskEarlistBySourceOrder(String sourceOrder) {
		// TODO Auto-generated method stub
		List<IqcTask> res = iqcTaskMapper.getIqcTaskEarlistBySourceOrder(sourceOrder);
		if (res == null || res.size() == 0) {
			return null;
		}
		return res.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_nonconforming_order.service.INonconformingOrderService#
	 * refuse(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public void refuse(IRequest requestCtx, List<NonconformingOrder> list) {
		// TODO 拒绝操作
		for (NonconformingOrder dto : list) {
			if (!dto.getNoStatus().equals("1")) {
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"error.hqm_nonconforming_create_02"));// 不合格单处理状态不为已发布，不允许拒绝
			}
			dto.setNoStatus("3");
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}
	}
}