package com.hand.hqm.hqm_iqc_inspection_l.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.dto.ProcessInstanceResponseExt;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_fqc_inspection_d.service.IFqcInspectionDService;
import com.hand.hqm.hqm_iqc_inspection_d.dto.IqcInspectionD;
import com.hand.hqm.hqm_iqc_inspection_d.mapper.IqcInspectionDMapper;
import com.hand.hqm.hqm_iqc_inspection_d.service.IIqcInspectionDService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.mapper.IqcInspectionHMapper;
import com.hand.hqm.hqm_iqc_inspection_h.service.IIqcInspectionHService;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;
import com.hand.hqm.hqm_iqc_inspection_l.mapper.IqcInspectionLMapper;
import com.hand.hqm.hqm_iqc_inspection_l.service.IIqcInspectionLService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.codehaus.jackson.type.TypeReference;

@Service
@Transactional(rollbackFor = Exception.class)
public class IqcInspectionLServiceImpl extends BaseServiceImpl<IqcInspectionL> implements IIqcInspectionLService {

	@Autowired
	IPromptService iPromptService;
	@Autowired
	IqcInspectionLMapper mapper;
	@Autowired
	IqcInspectionDMapper iqcInspectionDMapper;
	@Autowired
	IIqcInspectionDService iIqcInspectionDService;
	@Autowired
	IIqcInspectionHService iIqcInspectionHService;
	@Autowired
	IqcInspectionLMapper iqcInspectionLMapper;
	@Autowired
	IqcInspectionHMapper iqcInspectionHMapper;
	@Autowired
	IActivitiService activitiService;
	@Autowired
	NonconformingOrderMapper nonconformingOrderMapper;
	@Autowired
	IIqcInspectionTemplateLService iIqcInspectionTemplateLService;
	@Autowired
	IFqcInspectionDService iFqcInspectionDService;
	@Override
	public List<IqcInspectionL> reSelect(IRequest requestContext, IqcInspectionL dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	@Override
	public List<IqcInspectionL> reSelectFornon(IRequest requestContext, IqcInspectionL dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelectFornon(dto);
	}

	@Override
	public List<IqcInspectionL> getDetail(IRequest requestCtx, IqcInspectionH dtoh)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		IqcInspectionL searchL = new IqcInspectionL();
		searchL.setHeaderId(dtoh.getHeaderId());
		List<IqcInspectionL> lList = mapper.reSelect(searchL);
		for (IqcInspectionL lDto : lList) {
			IqcInspectionD record = new IqcInspectionD();
			record.setLineId(lDto.getLineId());
			List<IqcInspectionD> dList = iqcInspectionDMapper.select(record);
			lDto.setLineListString(objectMapper.writeValueAsString(dList));
		}
		return lList;
	}

	@Override
	public void saveDetail(IRequest requestCtx, HttpServletRequest request, List<IqcInspectionL> dto) throws Exception {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		IqcInspectionL max = new IqcInspectionL();
		max.setSampleSize(0f);
		for (IqcInspectionL dao : dto) {
			if (dao.getSampleSize() > max.getSampleSize()) {
				max = dao;
			}
		}
		IqcInspectionH update = mapper.readValue(max.getHeaderString(), IqcInspectionH.class);
		saveData(requestCtx, request, update, dto, false);
	}

	@Override
	public ResponseData commitDetail(IRequest requestCtx, HttpServletRequest request, List<IqcInspectionL> dto)
			throws Exception {
		ResponseData responseData = new ResponseData();
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		IqcInspectionL max = new IqcInspectionL();
		max.setSampleSize(0f);
		for (IqcInspectionL dao : dto) {
			if (dao.getSampleSize() > max.getSampleSize()) {
				max = dao;
			}
		}
		List<IqcInspectionD> dList = mapper.readValue(max.getLineListString(),
				new TypeReference<List<IqcInspectionD>>() {
				});
		Float passQuantity = 0f;
		Float ngQuantity = 0f;
		for (IqcInspectionD dao : dList) {
			if ("NG".equals(dao.getSnStatus())) {
				ngQuantity++;
			} else {
				passQuantity++;
			}
		}
		IqcInspectionH update = mapper.readValue(max.getHeaderString(), IqcInspectionH.class);
		update.setDetailStatus("C");
		update.setPassQuantity(passQuantity);
		update.setNgQuantity(ngQuantity);
		update.setInspectionDate(new Date());
		iIqcInspectionHService.updateByPrimaryKeySelective(requestCtx, update);
		saveData(requestCtx, request, update, dto, true);

		if (ngQuantity > 0f) {// 出现不合格,发起流程
			update.setInspectionStatus("5");
			update.setInspectionDate(new Date());
			update.setDetailStatus("C");
			update.setApprovalResult("NG");

			IqcInspectionL iqcInspectionLFind = new IqcInspectionL();
			iqcInspectionLFind.setHeaderId(update.getHeaderId());
			List<IqcInspectionL> iqcInspectionLFindList = iqcInspectionLMapper.select(iqcInspectionLFind);
			iIqcInspectionHService.auditInspection(update, iqcInspectionLFindList, request);

			// 生成不合格单号
			IqcInspectionH iqcInspectionHFind = new IqcInspectionH();
			iqcInspectionHFind.setHeaderId(update.getHeaderId());
			List<IqcInspectionH> iqcInspectionHList = iqcInspectionHMapper.select(iqcInspectionHFind);

			// 生成不合格单号
			String noNum;// 检验单号
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String time = dateFormat.format(date);
			String noNumStart = "NG" + iqcInspectionHList.get(0).getPlantCode() + "-" + time.substring(2) + "-";
			// 流水一个检验单号
			noNum = getNoNumber(noNumStart);

			NonconformingOrder nonconformingOrder = new NonconformingOrder();
			nonconformingOrder.setNoNum(noNum);
			nonconformingOrder.setInspectionType(iqcInspectionHList.get(0).getInspectionType());
			nonconformingOrder.setInspectionId(iqcInspectionHList.get(0).getHeaderId());
			nonconformingOrder.setPlantId(iqcInspectionHList.get(0).getPlantId());
			nonconformingOrder.setItemId(iqcInspectionHList.get(0).getItemId());
			if (iqcInspectionHList.get(0).getSupplierId() != null) {
				nonconformingOrder.setSupplierId(iqcInspectionHList.get(0).getSupplierId());
			}

			nonconformingOrder.setNoStatus("0");

			if (iqcInspectionHList.get(0).getProductionBatch() != null) {
				nonconformingOrder.setProductionBatch(iqcInspectionHList.get(0).getProductionBatch());
			}

			if (iqcInspectionHList.get(0).getReceiveDate() != null) {
				nonconformingOrder.setReceiveDate(iqcInspectionHList.get(0).getReceiveDate());
			}

			nonconformingOrder.setTotalityQty(iqcInspectionHList.get(0).getReceiveQty());
			nonconformingOrder.setSampleSize(iqcInspectionHList.get(0).getSampleSize());

			if (iqcInspectionHList.get(0).getInspectionDate() != null) {
				nonconformingOrder.setInspectionDate(iqcInspectionHList.get(0).getInspectionDate());
			}

			if (iqcInspectionHList.get(0).getInspectorId() != null) {
				nonconformingOrder.setApprovalId(Float.parseFloat(iqcInspectionHList.get(0).getInspectorId()));
			}
			nonconformingOrder.setNoSourceType("1");
			nonconformingOrderMapper.insertSelective(nonconformingOrder);

			// 启动流程
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(String.valueOf(nonconformingOrder.getNoId()));
			ProcessInstanceResponseExt responseExt = new ProcessInstanceResponseExt();

			if ("IQC".equals(iqcInspectionHList.get(0).getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("iqcUnqualityProcess");
				responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx,
						instanceCreateRequest);
			} else if ("FQC".equals(iqcInspectionHList.get(0).getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("fqcUnqualityProcess");
				responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx,
						instanceCreateRequest);
			}

			nonconformingOrder.setProcessInstanceId(responseExt.getId());
			nonconformingOrderMapper.updateByPrimaryKeySelective(nonconformingOrder);

			responseData.setMessage("提交成功，流程已启动");
			return responseData;
		}
		responseData.setMessage("提交成功");
		return responseData;
	}

	public String getNoNumber(String inspectionNumStart) {
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
	 * 明细_D表数据保存 检验记录的提交和保存都会用到此方法
	 * 
	 * @param requestCtx
	 * @param dto
	 * @throws Exception
	 */
	public void saveData(IRequest requestCtx, HttpServletRequest request, IqcInspectionH iqcInspectionH,
			List<IqcInspectionL> dto, boolean isCommit) throws Exception {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		iqcInspectionH.setInspectionJudge("OK");
		iqcInspectionH.setNgQty(0f);
		iqcInspectionH.setOkQty(0f);
		for (IqcInspectionL line : dto) {
			line.setAttributeInspectionRes("OK");
			line.setConformingQty(0f);
			line.setNonConformingQty(0f);
			IqcInspectionL update = new IqcInspectionL();
			update.setRemark(line.getRemark());
			update.setLineId(line.getLineId());
			self().updateByPrimaryKeySelective(requestCtx, update);
			List<IqcInspectionD> dList = mapper.readValue(line.getLineListString(),
					new TypeReference<List<IqcInspectionD>>() {
					});
			for (IqcInspectionD dline : dList) {
				if (line.getStandardType().equals("M") && !StringUtils.isEmpty(dline.getData())
						&& !(SystemApiMethod.getPercision(dline.getData()) == SystemApiMethod
								.getPercision((StringUtils.isEmpty(line.getStandradFrom()) ? line.getStandradTo()
										: line.getStandradFrom())))) {
					throw new Exception(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
							"hqm_iqc_inspection_template_precision_01"));
				}
				if (line.getStandardType().equals("M")) {
					if (!StringUtils.isEmpty(dline.getData()) && ((!StringUtils.isEmpty(line.getStandradFrom())
							&& Float.valueOf(dline.getData()) < Float.valueOf(line.getStandradFrom()))
							|| (!StringUtils.isEmpty(line.getStandradTo())
									&& Float.valueOf(dline.getData()) > Float.valueOf(line.getStandradTo()))

					)) {
						dline.setJudgement("NG");
						line.setAttributeInspectionRes("NG");
						line.setNonConformingQty(line.getNonConformingQty() + 1);
					} else {
						dline.setJudgement("OK");
						line.setConformingQty(line.getConformingQty() + 1);
					}
				} else {
					if (!StringUtils.isEmpty(dline.getData()) && "NG".equals(dline.getData())) {
						dline.setJudgement("NG");
						line.setAttributeInspectionRes("NG");
						line.setNonConformingQty(line.getNonConformingQty() + 1);
					} else {
						dline.setJudgement("OK");
						line.setConformingQty(line.getConformingQty() + 1);
					}
				}
				iIqcInspectionDService.updateByPrimaryKeySelective(requestCtx, dline);
			}
			if ("NG".equals(line.getAttributeInspectionRes())) {
				iqcInspectionH.setInspectionJudge("NG");
				iqcInspectionH.setNgQty(iqcInspectionH.getNgQty() + 1);
			} else {
				iqcInspectionH.setOkQty(iqcInspectionH.getOkQty() + 1);
			}
			// 更新行
			self().updateByPrimaryKeySelective(requestCtx, line);
		}

		// 修改头表的数据和状态
		if (isCommit) {
			if (!"2".equals(iqcInspectionH.getInspectionStatus())
					&& !"3".equals(iqcInspectionH.getInspectionStatus())) {
				System.out.println("iqcInspectionH.getInspectionStatus()" + iqcInspectionH.getInspectionStatus());
				throw new Exception(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"hqm.qc_inspection_submit_02"));// 只能对状态为“2”和“3”的检验单进行提交
			}
			iqcInspectionH.setInspectionStatus("4");
		} else {
			iqcInspectionH.setInspectionStatus("3");
		}
		// 更新头表header
		iIqcInspectionHService.updateByPrimaryKeySelective(requestCtx, iqcInspectionH);

		// 检验结果OK就执行审核
		if ("OK".equals(iqcInspectionH.getInspectionJudge()) && isCommit) {
			iqcInspectionH.setApprovalResult("OK");
			iIqcInspectionHService.auditInspection(iqcInspectionH, dto, request);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_l.service.IIqcInspectionLService#
	 * selectAttribute(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL, int, int)
	 */
	@Override
	public List<IqcInspectionL> selectAttribute(IRequest requestContext, IqcInspectionL dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.selectAttribute(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_l.service.IIqcInspectionLService#
	 * batchAttributeUpdate(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<IqcInspectionL> batchAttributeUpdate(IRequest request, List<IqcInspectionL> list) {
		for (IqcInspectionL t : list) {
			switch (((BaseDTO) t).get__status()) {
			case DTOStatus.ADD:
				self().insertSelectiveAttribute(request, t);
				break;
			case DTOStatus.UPDATE:
				if (useSelectiveUpdate()) {
					self().updateByPrimaryKeySelectiveAttribute(request, t);
				} else {
					self().updateByPrimaryKey(request, t);
				}
				break;
			case DTOStatus.DELETE:
				self().deleteByPrimaryKey(t);
				break;
			default:
				break;
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_l.service.IIqcInspectionLService#
	 * batchAttributeDelete(java.util.List)
	 */
	@Override
	public void batchAttributeDelete(List<IqcInspectionL> dto) {
		dto.forEach(dao -> {
			// 删除本表数据
			self().deleteByPrimaryKey(dao);
			IqcInspectionTemplateL teml = new IqcInspectionTemplateL();
			// 删除模板表数据
			teml.setLineId(dao.getTemplateLineId());
			iIqcInspectionTemplateLService.deleteByPrimaryKey(teml);
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_l.service.IIqcInspectionLService#
	 * updateByPrimaryKeySelectiveAttribute(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL)
	 */
	@Override
	public IqcInspectionL updateByPrimaryKeySelectiveAttribute(IRequest request, IqcInspectionL t) {
		// 更新本表数据
		self().updateByPrimaryKeySelective(request, t);
		// 更新模板表
		IqcInspectionTemplateL update = new IqcInspectionTemplateL();
		update.setLineId(t.getTemplateLineId());
		update.setAttributeId(t.getAttributeId());
		update.setAttributeType(t.getAttributeType());
		update.setStandardType(t.getStandardType());
		update.setSampleWayId(t.getSampleWayId());
		update.setInspectionTool(t.getInspectionTool());
		update.setInspectionMethod(t.getInspectionMethod());
		update.setStandradFrom(t.getStandradFrom());
		update.setStandradTo(t.getStandradTo());
		update.setTextStandrad(t.getTextStandrad());
		update.setQualityCharacterGrade(t.getQualityCharacterGrade());
		iIqcInspectionTemplateLService.updateByPrimaryKeySelective(request, update);
		return t;
	}

	/* (non-Javadoc)
	 * @see com.hand.hqm.hqm_iqc_inspection_l.service.IIqcInspectionLService#insertSelectiveAttribute(com.hand.hap.core.IRequest, com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL)
	 */
	@Override
	public IqcInspectionL insertSelectiveAttribute(IRequest request, IqcInspectionL t) {
		//找头上的抽样数量
		IqcInspectionH search = new IqcInspectionH();
		search.setHeaderId(t.getHeaderId());
		search = iqcInspectionHMapper.selectByPrimaryKey(search);
		t.setSampleSize(search.getSampleSize());
		self().insertSelective(request, t);
		iIqcInspectionDService.batchCreateByInspectionL(request, t);
		return t;
	}
}