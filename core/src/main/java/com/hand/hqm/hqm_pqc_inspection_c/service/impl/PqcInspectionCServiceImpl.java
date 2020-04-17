package com.hand.hqm.hqm_pqc_inspection_c.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_c.mapper.PqcInspectionCMapper;
import com.hand.hqm.hqm_pqc_inspection_c.service.IPqcInspectionCService;
import com.hand.hqm.hqm_pqc_inspection_d.dto.PqcInspectionD;
import com.hand.hqm.hqm_pqc_inspection_d.mapper.PqcInspectionDMapper;
import com.hand.hqm.hqm_pqc_inspection_d.service.IPqcInspectionDService;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.mapper.PqcInspectionHMapper;
import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_pqc_inspection_l.mapper.PqcInspectionLMapper;
import com.hand.hqm.hqm_pqc_inspection_l.service.IPqcInspectionLService;

import org.apache.commons.lang3.StringUtils;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcInspectionCServiceImpl extends BaseServiceImpl<PqcInspectionC> implements IPqcInspectionCService {
	@Autowired
	PqcInspectionCMapper mapper;
	@Autowired
	PqcInspectionDMapper pqcInspectionDMapper;
	@Autowired
	PqcInspectionLMapper pqcInspectionLMapper;
	@Autowired
	PqcInspectionHMapper pqcInspectionHMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IPqcInspectionHService iPqcInspectionHService;
	@Autowired
	IPqcInspectionLService iPqcInspectionLService;
	@Autowired
	IPqcInspectionDService iPqcInspectionDService;

	@Override
	public List<PqcInspectionC> myselect(IRequest requestContext, PqcInspectionC dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		// TODO Auto-generated method stub
		return mapper.myselect(dto);
	}

	@Override
	public List<PqcInspectionC> detailSave(IRequest requestCtx, HttpServletRequest request, List<PqcInspectionC> dto)
			throws Exception {
		// TODO _C表的明细处理及保存
		Float ngCount = 0f;
		for (PqcInspectionC pqcInspectionC : dto) {
			PqcInspectionD sear = new PqcInspectionD();
			sear.setConnectId(pqcInspectionC.getConnectId());
			List<PqcInspectionD> dList = pqcInspectionDMapper.select(sear);
			for (PqcInspectionD pd : dList) {
				if (StringUtils.isEmpty(pd.getData())) {
					throw new Exception(SystemApiMethod.getPromptDescription(request, iPromptService,
							"hqm_iqc_inspection_inpute_precision_01"));
				}
			}
			mapper.updateByPrimaryKeySelective(pqcInspectionC);
			if ("NG".equals(pqcInspectionC.getAttributeInspectionRes())) {
				ngCount++;
			}
		}
		// 设置对应行L表为不合格NG状态
		PqcInspectionL updaterPqcInspectionL = new PqcInspectionL();
		updaterPqcInspectionL.setLineId(dto.get(0).getLineId());
		updaterPqcInspectionL.setInspectionResult(ngCount == 0 ? "OK" : "NG");
		pqcInspectionLMapper.updateByPrimaryKeySelective(updaterPqcInspectionL);
		PqcInspectionL searchL = new PqcInspectionL();
		searchL.setLineId(dto.get(0).getLineId());
		searchL = pqcInspectionLMapper.selectByPrimaryKey(searchL);
		iPqcInspectionHService.updateOkNgQty(searchL.getHeaderId());
		return dto;
	}

	/**
	 * D表数据保存
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param connectId
	 * @param orderNum
	 * @param data
	 * @param pqcInspectionC
	 */
	public void saveInspectionD(Float connectId, String orderNum, String data, PqcInspectionC pqcInspectionC) {
		PqcInspectionD search = new PqcInspectionD();
		search.setConnectId(connectId);
		search.setOrderNum(orderNum);
		List<PqcInspectionD> li = new ArrayList<PqcInspectionD>();
		String judgement = new String();
		if ("M".equals(pqcInspectionC.getStandardType())) {
			// M型需要依照规格值从和规格值至计算judgement
			if (Float.valueOf(data).floatValue() < Float.valueOf(pqcInspectionC.getStandradFrom())
					|| Float.valueOf(data).floatValue() > Float.valueOf(pqcInspectionC.getStandradTo())) {
				judgement = "NG";
			} else {
				judgement = "OK";
			}
		} else {
			judgement = data;
		}
		li = pqcInspectionDMapper.select(search);
		if (li.size() == 0) {
			PqcInspectionD insert = new PqcInspectionD();
			insert.setConnectId(connectId);
			insert.setOrderNum(orderNum);
			insert.setData(data);
			insert.setJudgement(judgement);
			pqcInspectionDMapper.insertSelective(insert);
		} else {
			PqcInspectionD updater = li.get(0);
			updater.setData(data);
			updater.setJudgement(judgement);
			pqcInspectionDMapper.updateByPrimaryKeySelective(updater);
		}
	}

	@Override
	public List<PqcInspectionC> selectfornon(IRequest requestContext, PqcInspectionC dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.selectfornon(dto);
	}

	@Override
	public List<PqcInspectionC> getDetail(IRequest requestCtx, PqcInspectionL dtoh)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		PqcInspectionC searchL = new PqcInspectionC();
		searchL.setLineId(dtoh.getLineId());
		List<PqcInspectionC> lList = mapper.myselect(searchL);
		for (PqcInspectionC lDto : lList) {
			PqcInspectionD record = new PqcInspectionD();
			record.setConnectId(lDto.getConnectId());
			List<PqcInspectionD> dList = pqcInspectionDMapper.select(record);
			lDto.setLineListString(objectMapper.writeValueAsString(dList));
		}
		return lList;
	}

	@Override
	public void saveDetail(IRequest requestCtx, List<PqcInspectionC> dto) throws Exception {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		PqcInspectionC max = new PqcInspectionC();
		max.setSampleQty(0f);
		for (PqcInspectionC dao : dto) {
			if (dao.getSampleQty() > max.getSampleQty()) {
				max = dao;
			}
		}
		PqcInspectionL update = pqcInspectionLMapper.selectByPrimaryKey(max.getLineId());
		saveData(requestCtx, update, dto, false);
	}

	@Override
	public void commitDetail(IRequest requestCtx, List<PqcInspectionC> dto) throws Exception {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
		PqcInspectionC max = new PqcInspectionC();
		max.setSampleQty(0f);
		for (PqcInspectionC dao : dto) {
			if (dao.getSampleQty() > max.getSampleQty()) {
				max = dao;
			}
		}
		List<PqcInspectionD> dList = mapper.readValue(max.getLineListString(),
				new TypeReference<List<PqcInspectionD>>() {
				});
		Float passQuantity = 0f;
		Float ngQuantity = 0f;
		for (PqcInspectionD dao : dList) {
			if ("NG".equals(dao.getSnStatus())) {
				ngQuantity++;
			} else {
				passQuantity++;
			}
		}
		PqcInspectionL update = pqcInspectionLMapper.selectByPrimaryKey(max.getLineId());
		update.setDetailStatus("C");
		update.setPassQuantity(passQuantity);
		update.setNgQuantity(ngQuantity);
		iPqcInspectionLService.updateByPrimaryKeySelective(requestCtx, update);
		saveData(requestCtx, update, dto, true);
	}

	/**
	 * 明细_D表数据保存 检验记录的提交和保存都会用到此方法
	 * 
	 * @param requestCtx
	 * @param dto
	 * @throws Exception
	 */
	public void saveData(IRequest requestCtx, PqcInspectionL pqcInspectionL, List<PqcInspectionC> dto, boolean isCommit)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();// 定义 org.codehaus.jackson
//		PqcInspectionH pqcInspectionH  = pqcInspectionHMapper.selectByPrimaryKey(pqcInspectionL.getHeaderId());
//		pqcInspectionH.setInspectionJudge("OK");
//		pqcInspectionH.setNgQty(0f);
//		pqcInspectionH.setOkQty(0f);
		for (PqcInspectionC line : dto) {
			line.setAttributeInspectionRes("OK");
			line.setConformingQty(0f);
			line.setNonConformingQty(0f);
			PqcInspectionC update = new PqcInspectionC();
			update.setRemark(line.getRemark());
			update.setLineId(line.getLineId());
			self().updateByPrimaryKeySelective(requestCtx, update);
			List<PqcInspectionD> dList = mapper.readValue(line.getLineListString(),
					new TypeReference<List<PqcInspectionD>>() {
					});
			for (PqcInspectionD dline : dList) {
				if (line.getStandardType().equals("M") && !StringUtils.isEmpty(dline.getData())
						&& !(SystemApiMethod.getPercision(dline.getData()) == line.getPrecision().intValue())) {
					throw new Exception(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
							"hqm_iqc_inspection_template_precision_01"));
				}
				if (line.getStandardType().equals("M")) {
					if (!StringUtils.isEmpty(dline.getData())
							&& ((Float.valueOf(dline.getData())) < Float.valueOf(line.getStandradFrom())
									|| (Float.valueOf(dline.getData())) > Float.valueOf(line.getStandradTo()))) {
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
				iPqcInspectionDService.updateByPrimaryKeySelective(requestCtx, dline);
			}
			if ("NG".equals(line.getAttributeInspectionRes())) {
				pqcInspectionL.setInspectionResult("NG");
//				pqcInspectionL.setNgQty(pqcInspectionL.getNgQty() + 1);
			} else {
//				pqcInspectionL.setOkQty(pqcInspectionL.getOkQty() + 1);
			}
			// 更新行
			self().updateByPrimaryKeySelective(requestCtx, line);
		}

		// 修改头表的数据和状态
		if (isCommit) {
			if (!"2".equals(pqcInspectionL.getInspectionStatus())
					&& !"3".equals(pqcInspectionL.getInspectionStatus())) {
				throw new Exception(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"hqm.qc_inspection_submit_02"));// 只能对状态为“2”和“3”的检验单进行提交
			}
			pqcInspectionL.setInspectionStatus("4");
		} else {
			pqcInspectionL.setInspectionStatus("3");
		}
		// 更新头表l表
		iPqcInspectionLService.updateByPrimaryKeySelective(requestCtx, pqcInspectionL);
		iPqcInspectionHService.updateOkNgQty(pqcInspectionL.getHeaderId());

		/**
		 * added by wtm 20191226 提交执行后 更新检验单头表的approval_result字段为头表inspection_judge的状态
		 * 更新检验单状态为 5
		 */
		PqcInspectionH hSearch = new PqcInspectionH();
		hSearch.setHeaderId(pqcInspectionL.getHeaderId());
		hSearch = pqcInspectionHMapper.selectByPrimaryKey(hSearch);
		hSearch.setApprovalResult(hSearch.getInspectionJudge());
		hSearch.setInspectionStatus("5");
		pqcInspectionHMapper.updateByPrimaryKeySelective(hSearch);

	}
}