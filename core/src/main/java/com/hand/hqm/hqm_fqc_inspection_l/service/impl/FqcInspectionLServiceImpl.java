package com.hand.hqm.hqm_fqc_inspection_l.service.impl;

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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_fqc_inspection_d.dto.FqcInspectionD;
import com.hand.hqm.hqm_fqc_inspection_d.mapper.FqcInspectionDMapper;
import com.hand.hqm.hqm_fqc_inspection_d.service.IFqcInspectionDService;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_fqc_inspection_l.mapper.FqcInspectionLMapper;
import com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;
import com.hand.hqm.hqm_nonconforming_order.service.impl.NonconformingOrderServiceImpl;

import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcInspectionLServiceImpl extends BaseServiceImpl<FqcInspectionL> implements IFqcInspectionLService {

	@Autowired
	private FqcInspectionLMapper mapper;
	@Autowired
	private FqcInspectionDMapper fqcInspectionDMapper;
	@Autowired
	private IFqcInspectionDService iFqcInspectionDService;
	@Autowired
	private IFqcInspectionHService iFqcInspectionHService;
	@Autowired
	private IPromptService iPromptService;
	@Autowired
	FqcInspectionLMapper fqcInspectionLMapper;
	@Autowired
	FqcInspectionHMapper fqcInspectionHMapper;
	@Autowired
	private IActivitiService activitiService;
	@Autowired
	NonconformingOrderMapper nonconformingOrderMapper;
	@Autowired
	IIqcInspectionTemplateLService iIqcInspectionTemplateLService;

	@Override
	public List<FqcInspectionL> query(IRequest requestContext, FqcInspectionL dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public List<FqcInspectionL> queryfornon(IRequest requestContext, FqcInspectionL dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.queryfornon(dto);
	}

	@Override
	public List<FqcInspectionL> getDetail(IRequest requestCtx, FqcInspectionH dtoh)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		FqcInspectionL searchL = new FqcInspectionL();
		searchL.setHeaderId(dtoh.getHeaderId());
		searchL.setIsLaboratory(dtoh.getIsLaboratory());
		List<FqcInspectionL> lList = mapper.query(searchL);
		for (FqcInspectionL lDto : lList) {
			FqcInspectionD record = new FqcInspectionD();
			record.setLineId(lDto.getLineId());
			List<FqcInspectionD> dList = fqcInspectionDMapper.select(record);
			lDto.setLineListString(objectMapper.writeValueAsString(dList));
		}
		return lList;
	}

	@Override
	public void saveDetail(IRequest requestCtx, HttpServletRequest request, List<FqcInspectionL> dto) throws Exception {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		FqcInspectionL max = new FqcInspectionL();
		max.setSampleQty(0f);
		for (FqcInspectionL dao : dto) {
			if (dao.getSampleQty() > max.getSampleQty()) {
				max = dao;
			}
		}
		FqcInspectionH update = mapper.readValue(max.getHeaderString(), FqcInspectionH.class);
		saveData(requestCtx, request, update, dto, false);
	}

	@Override
	public ResponseData commitDetail(IRequest requestCtx, HttpServletRequest request, List<FqcInspectionL> dto)
			throws Exception {
		ResponseData responseData = new ResponseData();
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		FqcInspectionL max = new FqcInspectionL();
		max.setSampleQty(0f);
		for (FqcInspectionL dao : dto) {
			if (dao.getSampleQty() > max.getSampleQty()) {
				max = dao;
			}
		}
		List<FqcInspectionD> dList = mapper.readValue(max.getLineListString(),
				new TypeReference<List<FqcInspectionD>>() {
				});
		Float passQuantity = 0f;
		Float ngQuantity = 0f;
		for (FqcInspectionD dao : dList) {
			if ("NG".equals(dao.getSnStatus())) {
				ngQuantity++;
			} else {
				passQuantity++;
			}
		}
		FqcInspectionH update = mapper.readValue(max.getHeaderString(), FqcInspectionH.class);
		update.setDetailStatus("C");
		update.setPassQuantity(passQuantity);
		update.setNgQuantity(ngQuantity);
		update.setInspectionDate(new Date());
		iFqcInspectionHService.updateByPrimaryKeySelective(requestCtx, update);
		saveData(requestCtx, request, update, dto, true);

		if (ngQuantity > 0f) {
			update.setInspectionStatus("5");
			update.setInspectionDate(new Date());
			update.setDetailStatus("C");
			update.setApprovalResult("NG");

			FqcInspectionL fqcInspectionLFind = new FqcInspectionL();
			fqcInspectionLFind.setHeaderId(update.getHeaderId());
			List<FqcInspectionL> fqcInspectionLFindList = fqcInspectionLMapper.select(fqcInspectionLFind);
			iFqcInspectionHService.auditInspection(update, fqcInspectionLFindList, request);

			FqcInspectionH fqcInspectionHFind = new FqcInspectionH();
			fqcInspectionHFind.setHeaderId(update.getHeaderId());
			List<FqcInspectionH> fqcInspectionHList = fqcInspectionHMapper.select(fqcInspectionHFind);
			// 生成不合格单号
			String noNum;// 检验单号
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String time = dateFormat.format(date);
			String noNumStart = "NG" + fqcInspectionHList.get(0).getPlantCode() + "-" + time.substring(2) + "-";
			// 流水一个检验单号
			noNum = getNoNumber(noNumStart);

			NonconformingOrder nonconformingOrder = new NonconformingOrder();
			nonconformingOrder.setNoNum(noNum);
			nonconformingOrder.setInspectionType(fqcInspectionHList.get(0).getInspectionType());
			nonconformingOrder.setInspectionId(fqcInspectionHList.get(0).getHeaderId());
			nonconformingOrder.setPlantId(fqcInspectionHList.get(0).getPlantId());
			nonconformingOrder.setItemId(fqcInspectionHList.get(0).getItemId());
			nonconformingOrder.setNoStatus("0");
			if (fqcInspectionHList.get(0).getProductionBatch() != null) {
				nonconformingOrder.setProductionBatch(fqcInspectionHList.get(0).getProductionBatch());
			}

			if (fqcInspectionHList.get(0).getProduceDate() != null) {
				nonconformingOrder.setReceiveDate(fqcInspectionHList.get(0).getProduceDate());
			}

			nonconformingOrder.setTotalityQty(fqcInspectionHList.get(0).getProduceQty());
			nonconformingOrder.setSampleSize(fqcInspectionHList.get(0).getSampleQty());

			if (fqcInspectionHList.get(0).getInspectionDate() != null) {
				nonconformingOrder.setInspectionDate(fqcInspectionHList.get(0).getInspectionDate());
			}

			if (fqcInspectionHList.get(0).getInspectorId() != null) {
				nonconformingOrder.setApprovalId(fqcInspectionHList.get(0).getInspectorId());
			}
			nonconformingOrder.setNoSourceType("1");
			nonconformingOrderMapper.insertSelective(nonconformingOrder);

			// 启动流程
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(String.valueOf(nonconformingOrder.getNoId()));
			ProcessInstanceResponseExt responseExt = new ProcessInstanceResponseExt();

			if ("IQC".equals(fqcInspectionHList.get(0).getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("iqcUnqualityProcess");
				responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx,
						instanceCreateRequest);
			} else if ("FQC".equals(fqcInspectionHList.get(0).getInspectionType())) {
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
	 * 
	 * @description 明细_D表数据保存 检验记录的提交和保存都会用到此方法
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param requestCtx
	 * @param fqcInspectionH
	 * @param dto
	 * @param isCommit
	 * @throws Exception
	 */
	public void saveData(IRequest requestCtx, HttpServletRequest request, FqcInspectionH fqcInspectionH,
			List<FqcInspectionL> dto, boolean isCommit) throws Exception {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		fqcInspectionH.setInspectionJudge("OK");
		fqcInspectionH.setNgQty(0f);
		fqcInspectionH.setOkQty(0f);
		for (FqcInspectionL line : dto) {
			line.setAttributeInspectionRes("OK");
			line.setConformingQty(0f);
			line.setNonConformingQty(0f);
			FqcInspectionL update = new FqcInspectionL();
			update.setRemark(line.getRemark());
			update.setLineId(line.getLineId());
			self().updateByPrimaryKeySelective(requestCtx, update);
			List<FqcInspectionD> dList = mapper.readValue(line.getLineListString(),
					new TypeReference<List<FqcInspectionD>>() {
					});
			for (FqcInspectionD dline : dList) {
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
				iFqcInspectionDService.updateByPrimaryKeySelective(requestCtx, dline);
			}
			if ("NG".equals(line.getAttributeInspectionRes())) {
				fqcInspectionH.setInspectionJudge("NG");
				fqcInspectionH.setNgQty(fqcInspectionH.getNgQty() + 1);
			} else {
				fqcInspectionH.setOkQty(fqcInspectionH.getOkQty() + 1);
			}
			// 更新行
			self().updateByPrimaryKeySelective(requestCtx, line);
		}

		// 修改头表的数据和状态
		if (isCommit) {
			if (!"2".equals(fqcInspectionH.getInspectionStatus())
					&& !"3".equals(fqcInspectionH.getInspectionStatus())) {
				throw new Exception(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"hqm.qc_inspection_submit_02"));// 只能对状态为“2”和“3”的检验单进行提交
			}

			/**
			 * added by wtm 20191220 当用户出现两个NG 的sn及以上而且选择了提交 InspectionJudge("NG")
			 */

			if (fqcInspectionH.getForceCommit().equals("Y")) {
				fqcInspectionH.setInspectionJudge("NG");
			}

			fqcInspectionH.setInspectionStatus("4");

		} else {
			fqcInspectionH.setInspectionStatus("3");
		}
		// 更新头表header
		iFqcInspectionHService.updateByPrimaryKeySelective(requestCtx, fqcInspectionH);
		// wtm 20200218 执行自动审核
		if ("OK".equals(fqcInspectionH.getInspectionJudge()) && isCommit) {
			fqcInspectionH.setApprovalResult("OK");
			iFqcInspectionHService.auditInspection(fqcInspectionH, dto, request);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService#
	 * batchAttributeDelete(java.util.List)
	 */
	@Override
	public void batchAttributeDelete(List<FqcInspectionL> dto) {
		// TODO 检验项修改的删除
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
	 * @see com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService#
	 * batchAttributeUpdate(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<FqcInspectionL> batchAttributeUpdate(IRequest request, List<FqcInspectionL> list) {
		// TODO 检验项修改的更新
		for (FqcInspectionL t : list) {
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
	 * @see com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService#
	 * selectAttribute(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL, int, int)
	 */
	@Override
	public List<FqcInspectionL> selectAttribute(IRequest requestContext, FqcInspectionL dto, int page, int pageSize) {
		// TODO 检验项修改的查询
		PageHelper.startPage(page, pageSize);
		return mapper.selectAttribute(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService#
	 * insertSelectiveAttribute(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL)
	 */
	@Override
	public FqcInspectionL insertSelectiveAttribute(IRequest request, FqcInspectionL t) {
		// 找头上的抽样数量
		FqcInspectionH search = new FqcInspectionH();
		search.setHeaderId(t.getHeaderId());
		search = fqcInspectionHMapper.selectByPrimaryKey(search);
		t.setSampleQty(search.getSampleQty());
		self().insertSelective(request, t);
		iFqcInspectionDService.batchCreateByInspectionL(request, t);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService#
	 * updateByPrimaryKeySelectiveAttribute(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL)
	 */
	@Override
	public FqcInspectionL updateByPrimaryKeySelectiveAttribute(IRequest request, FqcInspectionL t) {
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
}