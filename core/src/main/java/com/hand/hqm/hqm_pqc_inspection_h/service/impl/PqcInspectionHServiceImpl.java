package com.hand.hqm.hqm_pqc_inspection_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hap.webservice.ws.idto.PqcProductionInfo;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;
import com.hand.hcm.hcm_production_line.mapper.ProductionLineMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_pqc_calendar_a.dto.PqcCalendarA;
import com.hand.hqm.hqm_pqc_calendar_a.service.IPqcCalendarAService;
import com.hand.hqm.hqm_pqc_calendar_b.dto.PqcCalendarB;
import com.hand.hqm.hqm_pqc_calendar_b.service.IPqcCalendarBService;
import com.hand.hqm.hqm_pqc_calendar_c.dto.PqcCalendarC;
import com.hand.hqm.hqm_pqc_calendar_c.service.IPqcCalendarCService;
import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_c.mapper.PqcInspectionCMapper;
import com.hand.hqm.hqm_pqc_inspection_d.dto.PqcInspectionD;
import com.hand.hqm.hqm_pqc_inspection_d.mapper.PqcInspectionDMapper;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.dto.ProcessResult;
import com.hand.hqm.hqm_pqc_inspection_h.mapper.PqcInspectionHMapper;
import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_pqc_inspection_l.mapper.PqcInspectionLMapper;
import com.hand.hqm.hqm_pqc_inspection_l.service.IPqcInspectionLService;
import com.hand.hqm.hqm_qc_task.dto.PqcTask;
import com.hand.hqm.hqm_qc_task.service.impl.FqcTaskServiceImpl;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;
import com.hand.hqm.hqm_qua_ins_time_l.mapper.QuaInsTimeLMapper;
import com.hand.hqm.hqm_stan_op_item_h.mapper.StandardOpItemHMapper;
import com.hand.hqm.hqm_stan_op_item_l.mapper.StandardOpItemLMapper;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;
import com.hand.hqm.hqm_standard_op_ins_h.mapper.StandardOpInspectionHMapper;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;
import com.hand.hqm.hqm_standard_op_ins_l.mapper.StandardOpInspectionLMapper;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcInspectionHServiceImpl extends BaseServiceImpl<PqcInspectionH> implements IPqcInspectionHService {
	@Autowired
	PqcInspectionHMapper pqcInspectionHMapper;
	@Autowired
	PqcInspectionLMapper pqcInspectionLMapper;
	@Autowired
	StandardOpInspectionHMapper standardOpInspectionHMapper;
	@Autowired
	StandardOpItemHMapper standardOpItemHMapper;
	@Autowired
	StandardOpItemLMapper standardOpItemLMapper;
	@Autowired
	PqcInspectionCMapper pqcInspectionCMapper;
	@Autowired
	PqcInspectionDMapper pqcInspectionDMapper;
	@Autowired
	StandardOpInspectionLMapper standardOpInspectionLMapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ProductionLineMapper productionLineMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	QuaInsTimeLMapper quaInsTimeLMapper;
	@Autowired
	IPqcInspectionLService iPqcInspectionLService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	ItemBMapper itemBMapper;
	Logger logger = LoggerFactory.getLogger(FqcTaskServiceImpl.class);

	@Override
	public List<PqcInspectionH> myselect(IRequest requestContext, PqcInspectionH dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return pqcInspectionHMapper.myselect(dto);
	}

	@Autowired
	IPqcCalendarAService iPqcCalendarAService;
	@Autowired
	IPqcCalendarBService iPqcCalendarBService;
	@Autowired
	IPqcCalendarCService iPqcCalendarCService;

	/**
	 * 
	 * @description 更新日历表的数据
	 * @author tianmin.wang
	 * @date 2019年11月26日
	 */
	public void updateCalendar(PqcInspectionH dto, IRequest requestCtx) {
		if (dto.getForkId() == -1f) {
			return;
		}
		switch (dto.getForkType()) {
		case "A":
			PqcCalendarA pa = new PqcCalendarA();
			pa.setCalendarAId(dto.getForkId());
			pa.setLayeredStatus("R");
			pa.setAttribute1(dto.getInspectionNum());
			dto.setSourceType("14");
			iPqcCalendarAService.updateByPrimaryKeySelective(requestCtx, pa);
			break;
		case "B":
			PqcCalendarB pb = new PqcCalendarB();
			pb.setCalendarBId(dto.getForkId());
			pb.setSafetyStatus("R");
			pb.setAttribute1(dto.getInspectionNum());
			dto.setSourceType("13");
			iPqcCalendarBService.updateByPrimaryKeySelective(requestCtx, pb);
			break;
		case "C":
			PqcCalendarC pc = new PqcCalendarC();
			pc.setCalendarCId(dto.getForkId());
			pc.setPwaStatus("R");
			pc.setAttribute1(dto.getInspectionNum());
			dto.setSourceType("5");
			iPqcCalendarCService.updateByPrimaryKeySelective(requestCtx, pc);
			break;
		}
	}

	@Override
	public ResponseData addNewInspection(PqcInspectionH dto, IRequest requestContext, HttpServletRequest request)
			throws Exception {// synchronized
		// TODO 新建PQC检验单

		ObjectMapper mapper = new ObjectMapper();
		List<PqcInspectionL> lineList = new ArrayList<PqcInspectionL>();
		ResponseData responseData = new ResponseData();
		try {
			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<PqcInspectionL>>() {
			});
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
			return responseData;
		}
		String inspectionNum;// 检验单号
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String inspectionNumStart = dto.getPlantCode() + "-" + time.substring(2) + "-";
		// 流水一个检验单号
		inspectionNum = getInspectionNumber(inspectionNumStart);
		dto.setInspectionNum(inspectionNum);
		// 更新日历表的数据 并修改来源类型
		updateCalendar(dto, requestContext);

		PqcInspectionH adder = new PqcInspectionH();
		adder.setInspectionRes("P");
		adder.setInspectionJudge("P");
		adder.setApprovalResult("P");
		adder.setInspectionNum(inspectionNum);
		adder.setInspectionType("PQC");
		adder.setInspectionFrom(dto.getInspectionFrom());
		adder.setInspectionStatus("2");
		adder.setPlantId(dto.getPlantId());
		adder.setProdLineId(dto.getProdLineId());
		adder.setShouldInspectionDate(dto.getShouldInspectionDate());
		adder.setSourceType(dto.getSourceType());
		adder.setInspectorId(dto.getInspectorId());

		pqcInspectionHMapper.insertSelective(adder);
		Float headerId = adder.getHeaderId();
		for (PqcInspectionL line : lineList) {
			PqcInspectionL lineAdder = new PqcInspectionL();
			lineAdder.setHeaderId(headerId);
			lineAdder.setStandardOpId(line.getStandardOpId());
			lineAdder.setWorkstationId(line.getWorkstationId());
			lineAdder.setInspectionStatus(line.getInspectionStatus());
			pqcInspectionLMapper.insertSelective(lineAdder);
			this.createInspectionC(adder, lineAdder, request);
		}
		List<PqcInspectionH> li = new ArrayList<>();
		li.add(adder);
		responseData.setRows(li);
		return responseData;
	}

	/**
	 * 创建C数据
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param pqcInspectionH
	 * @param pqcInspectionL
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<PqcInspectionC> createInspectionC(PqcInspectionH pqcInspectionH, PqcInspectionL pqcInspectionL,
			HttpServletRequest request) throws Exception {
		StandardOpInspectionH queryOpH = new StandardOpInspectionH();
		queryOpH.setPlantId(pqcInspectionH.getPlantId());
		queryOpH.setProdLineId(pqcInspectionH.getProdLineId());
		queryOpH.setStandardOpId(pqcInspectionL.getStandardOpId());
		queryOpH.setWorkstationId(pqcInspectionL.getWorkstationId());
		queryOpH.setSourceType(pqcInspectionH.getSourceType());
		StandardOpInspectionH resOpH = standardOpInspectionHMapper.selectOne(queryOpH);
		if (resOpH == null) {
			throw new Exception(SystemApiMethod.getPromptDescription(request, iPromptService,
					"error.hqm_pqc_inspection_create_01"));
		}
		StandardOpInspectionL queryOpL = new StandardOpInspectionL();
		queryOpL.setHeadId(resOpH.getHeadId());
		List<StandardOpInspectionL> itemLList = new ArrayList<>();
		itemLList = standardOpInspectionLMapper.select(queryOpL);
		List<PqcInspectionC> insCList = new ArrayList<>();
		for (StandardOpInspectionL standardOpItemL : itemLList) {
			PqcInspectionC addorInspectionC = new PqcInspectionC();
			addorInspectionC.setLineId(pqcInspectionL.getLineId());
			addorInspectionC.setAttributeId(standardOpItemL.getAttributeId());
			pqcInspectionCMapper.insertSelective(addorInspectionC);
			insCList.add(addorInspectionC);
		}
		return insCList;

	}

	/**
	 * job入口
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param pqcInspectionH
	 * @param pqcInspectionL
	 * @return
	 * @throws Exception
	 */
	public List<PqcInspectionC> createInspectionCJob(PqcInspectionH pqcInspectionH, PqcInspectionL pqcInspectionL)
			throws Exception {
		// 与正常的生成C的逻辑唯一不同在 queryOpH.setSourceType("12"); 设置了默认值
		StandardOpInspectionH queryOpH = new StandardOpInspectionH();
		queryOpH.setPlantId(pqcInspectionH.getPlantId());
		queryOpH.setProdLineId(pqcInspectionH.getProdLineId());
		queryOpH.setStandardOpId(pqcInspectionL.getStandardOpId());
		queryOpH.setWorkstationId(pqcInspectionL.getWorkstationId());
		queryOpH.setSourceType("12");
		StandardOpInspectionH resOpH = standardOpInspectionHMapper.selectOne(queryOpH);
		if (resOpH == null) {
			throw new Exception(
					SystemApiMethod.getPromptDescription(iPromptService, "error.hqm_pqc_inspection_create_01"));// 未找到检验工序和该来源类型的检验项目
		}
		StandardOpInspectionL queryOpL = new StandardOpInspectionL();
		queryOpL.setHeadId(resOpH.getHeadId());
		List<StandardOpInspectionL> itemLList = new ArrayList<>();
		itemLList = standardOpInspectionLMapper.select(queryOpL);
		List<PqcInspectionC> insCList = new ArrayList<>();
		for (StandardOpInspectionL standardOpItemL : itemLList) {
			PqcInspectionC addorInspectionC = new PqcInspectionC();
			addorInspectionC.setLineId(pqcInspectionL.getLineId());
			addorInspectionC.setAttributeId(standardOpItemL.getAttributeId());
			pqcInspectionCMapper.insertSelective(addorInspectionC);
			insCList.add(addorInspectionC);
		}
		return insCList;

	}

	/**
	 * 获取一个检验单号的流水
	 * 
	 * @param inspectionNumStart
	 * @return
	 */
	public String getInspectionNumber(String inspectionNumStart) {
		Integer count = 1;
		PqcInspectionH sr = new PqcInspectionH();
		sr.setInspectionNum(inspectionNumStart);
		List<PqcInspectionH> list = new ArrayList<PqcInspectionH>();
		list = pqcInspectionHMapper.selectMaxNumber(sr);
		if (list != null && list.size() > 0) {
			String inspectionNum = list.get(0).getInspectionNum();
			String number = inspectionNum.substring(inspectionNum.length() - 5);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%05d", count);
		return inspectionNumStart + str;// 最终检验单号
	}

	@Override
	public ResponseData saveChangeAll(PqcInspectionH dto, IRequest requestContext, HttpServletRequest request)
			throws Exception {
		// TODO 修改的头行一起保存处理
		ResponseData responseData = new ResponseData();
		ObjectMapper mapper = new ObjectMapper();
		List<PqcInspectionL> lineList = new ArrayList<PqcInspectionL>();
		try {
			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<PqcInspectionL>>() {
			});
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
			return responseData;
		}
		String inspectionStatus = "2";
//		if("OK".equals(dto.getInspectionJudge())) {
//			inspectionStatus="5";
//		}else if("NG".equals(dto.getInspectionJudge())) {
//			inspectionStatus="4";
//		}else {
//			
//		}
//		dto.setInspectionStatus(inspectionStatus);
		pqcInspectionHMapper.updateByPrimaryKeySelective(dto);
		for (PqcInspectionL pqcInspectionL : lineList) {
			pqcInspectionL.setHeaderId(dto.getHeaderId());
			// pqcInspectionL.setInspectionStatus(inspectionStatus);
			if (pqcInspectionL.getLineId() == null) {

				pqcInspectionLMapper.insertSelective(pqcInspectionL);
				this.createInspectionC(dto, pqcInspectionL, request);
			} else {
				pqcInspectionLMapper.updateByPrimaryKeySelective(pqcInspectionL);
			}
		}
		return responseData;
	}

	@Override
	public void updateOkNgQty(Float headId) throws Exception {
		// TODO Auto-generated method stub
		List<PqcInspectionL> lineList = new ArrayList<PqcInspectionL>();
		PqcInspectionL ser = new PqcInspectionL();
		ser.setHeaderId(headId);
		lineList = pqcInspectionLMapper.select(ser);
		Float okQty = 0f;
		Float ngQty = 0f;
		for (PqcInspectionL pqcInspectionL : lineList) {
			if (pqcInspectionL.getInspectionResult() != null && "NG".equals(pqcInspectionL.getInspectionResult())) {
				ngQty++;
			} else if (pqcInspectionL.getInspectionResult() != null
					&& "OK".equals(pqcInspectionL.getInspectionResult())) {
				okQty++;
			} else {

			}
		}
		PqcInspectionH update = new PqcInspectionH();
		update.setHeaderId(headId);
		if (ngQty > 0) {
			update.setInspectionJudge("NG");
		} else {
			update.setInspectionJudge("OK");
		}
		update.setOkQty(okQty);
		update.setNgQty(ngQty);
		self().updateByPrimaryKeySelective(null, update);
	}

	@Override
	public void updateOkNgQty(Float headId, String inspectionStatus) throws Exception {
		// TODO Auto-generated method stub
		List<PqcInspectionL> lineList = new ArrayList<PqcInspectionL>();
		PqcInspectionL ser = new PqcInspectionL();
		ser.setHeaderId(headId);
		lineList = pqcInspectionLMapper.select(ser);
		Float okQty = 0f;
		Float ngQty = 0f;
		for (PqcInspectionL pqcInspectionL : lineList) {
			if (pqcInspectionL.getInspectionResult() != null && "NG".equals(pqcInspectionL.getInspectionResult())) {
				ngQty++;
			} else if (pqcInspectionL.getInspectionResult() != null
					&& "OK".equals(pqcInspectionL.getInspectionResult())) {
				okQty++;
			} else {

			}
		}
		PqcInspectionH update = new PqcInspectionH();
		update.setHeaderId(headId);
		if (ngQty > 0) {
			update.setInspectionJudge("NG");
		} else {
			update.setInspectionJudge("OK");
		}
		update.setInspectionStatus(inspectionStatus);
		update.setOkQty(okQty);
		update.setNgQty(ngQty);
		self().updateByPrimaryKeySelective(null, update);
	}

	/**
	 * 获取工厂信息
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param plantId
	 * @return
	 */
	public String getPlantCode(Float plantId) {
		Plant sea = new Plant();
		sea.setPlantId(plantId);
		Plant res = plantMapper.selectOne(sea);
		return res == null ? "" : res.getPlantCode();

	}

	/**
	 * 获取产线信息
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param prodLineId
	 * @return
	 */
	public String getProdLineCode(Float prodLineId) {

		ProductionLine sea = new ProductionLine();
		sea.setProdLineId(prodLineId);
		ProductionLine res = productionLineMapper.selectOne(sea);
		return res == null ? "" : res.getProdLineCode();
	}

	@Override
	public ResponseData addNewInspectionJob(PqcInspectionH dto, List<PqcInspectionL> lineList) throws Exception {
		// TODO 新建PQC检验单
		ResponseData responseData = new ResponseData();
		String inspectionNum;// 检验单号
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String inspectionNumStart = this.getPlantCode(dto.getPlantId()) + "-" + time.substring(2) + "-";
		// 流水一个检验单号
		inspectionNum = getInspectionNumber(inspectionNumStart);
		PqcInspectionH adder = new PqcInspectionH();
		adder.setInspectionNum(inspectionNum);
		adder.setInspectionRes("P");
		adder.setInspectionJudge("P");
		adder.setApprovalResult("P");
		adder.setInspectionType("PQC");
		adder.setInspectionFrom("1");
		adder.setInspectionStatus("2");
		adder.setPlantId(dto.getPlantId());
		adder.setProdLineId(dto.getProdLineId());
		adder.setShouldInspectionDate(new Date());
		adder.setSourceType("12");
		adder.setRemark("定时JOB");
		pqcInspectionHMapper.insertSelective(adder);
		Float headerId = adder.getHeaderId();
		for (PqcInspectionL line : lineList) {
			PqcInspectionL lineAdder = new PqcInspectionL();
			lineAdder.setHeaderId(headerId);
			lineAdder.setStandardOpId(line.getStandardOpId());
			lineAdder.setWorkstationId(line.getWorkstationId());
			lineAdder.setInspectionStatus("0");
			pqcInspectionLMapper.insertSelective(lineAdder);
			this.createInspectionCJob(adder, lineAdder);
		}
		List<PqcInspectionH> li = new ArrayList<>();
		li.add(adder);
		responseData.setRows(li);
		return responseData;
	}

	@Override
	public ResponseData commitItem(PqcInspectionH dto, IRequest requestContext, HttpServletRequest request)
			throws Exception {
		// TODO 执行提交操作
		ResponseData responseData = new ResponseData();
		ObjectMapper mapper = new ObjectMapper();
		List<PqcInspectionL> lineList = new ArrayList<PqcInspectionL>();
		try {
			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<PqcInspectionL>>() {
			});
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage("如果看到此信息请找技术人员:JSON格式转换失败" + ex.getMessage());
			return responseData;
		}
		String inspectionStatus = "4";
		if ("OK".equals(dto.getInspectionJudge())) {
			inspectionStatus = "5";
		} else if ("NG".equals(dto.getInspectionJudge())) {
			inspectionStatus = "4";
		} else {

		}
		dto.setInspectionStatus("4");
		pqcInspectionHMapper.updateByPrimaryKeySelective(dto);
		for (PqcInspectionL pqcInspectionL : lineList) {
			pqcInspectionL.setHeaderId(dto.getHeaderId());
			pqcInspectionL.setInspectionStatus("2");
			if (pqcInspectionL.getLineId() == null) {
				pqcInspectionLMapper.insertSelective(pqcInspectionL);
//						this.createInspectionC(dto,pqcInspectionL,request);
			} else {
				pqcInspectionLMapper.updateByPrimaryKeySelective(pqcInspectionL);
			}
		}
		// 如果满足检验OK则自动审核通过
//				if("5".equals(inspectionStatus)) {
//					dto.setApprovalResult("OK");
//					this.auditItem(dto,requestContext,request);
//				}
		return responseData;

	}

	@Override
	public ResponseData auditItem(PqcInspectionH dto, IRequest requestContext, HttpServletRequest request)
			throws Exception {
		// TODO 执行审核操作
		ResponseData responseData = new ResponseData();
		ObjectMapper mapper = new ObjectMapper();
		List<PqcInspectionL> lineList = new ArrayList<PqcInspectionL>();
		try {
			lineList = mapper.readValue(dto.getLineList(), new TypeReference<List<PqcInspectionL>>() {
			});
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage("JSON格式转换失败" + ex.getMessage());
			return responseData;
		}
		String inspectionStatus = "5";
//				if("OK".equals(dto.getInspectionJudge())) {
//					inspectionStatus="5";
//				}else if("NG".equals(dto.getInspectionJudge())) {
//					inspectionStatus="4";
//				}else {
//					
//				}
		dto.setInspectionStatus(inspectionStatus);
		pqcInspectionHMapper.updateByPrimaryKeySelective(dto);
		for (PqcInspectionL pqcInspectionL : lineList) {
			pqcInspectionL.setHeaderId(dto.getHeaderId());
			pqcInspectionL.setInspectionStatus(inspectionStatus);
			if (pqcInspectionL.getLineId() == null) {

				pqcInspectionLMapper.insertSelective(pqcInspectionL);
//						this.createInspectionC(dto,pqcInspectionL,request);
			}
		}
		return responseData;
	}

	@Override
	public int reBatchDelete(List<PqcInspectionH> list) {
		// TODO 重写的批量删除 包含4个表
		int c = 0;
		for (PqcInspectionH t : list) {
			List<PqcInspectionL> lList;
			PqcInspectionL searchL = new PqcInspectionL();
			searchL.setHeaderId(t.getHeaderId());
			lList = pqcInspectionLMapper.select(searchL);
			for (PqcInspectionL pqcInspectionL : lList) {
				List<PqcInspectionC> cList;
				PqcInspectionC searchC = new PqcInspectionC();
				searchC.setLineId(pqcInspectionL.getLineId());
				cList = pqcInspectionCMapper.select(searchC);
				for (PqcInspectionC pqcInspectionC : cList) {
					List<PqcInspectionD> dList;
					PqcInspectionD searchD = new PqcInspectionD();
					searchD.setConnectId(pqcInspectionC.getConnectId());
					dList = pqcInspectionDMapper.select(searchD);
					for (PqcInspectionD pqcInspectionD : dList) {
						pqcInspectionDMapper.deleteByPrimaryKey(pqcInspectionD);
					}
				}
				pqcInspectionLMapper.deleteByPrimaryKey(pqcInspectionL);
			}
			c += self().deleteByPrimaryKey(t);
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService#
	 * transferPqcProductionInfo(com.hand.hap.webservice.ws.idto.PqcProductionInfo)
	 */
	@Override
	public ResponseSap transferPqcProductionInfo(PqcProductionInfo ppi) throws Exception {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		PqcInspectionH search = new PqcInspectionH();
		search.setInspectionNum(ppi.inspectionNum);
		List<PqcInspectionH> hResult = mapper.select(search);
		PqcInspectionH head = hResult.get(0);
		Float plantId = getPlantIdByCode("CNKE");
		Float itemId = getItemIdByCode(ppi.itemCode, plantId);
		head.setItemId(itemId);
		head.setPlantId(plantId);
		head.setProductionQty(1f);
		head.setSourceOrder(ppi.orderNo);
		head.setItemEdition(ppi.itemVersion);
		ServiceRequest sr = new ServiceRequest();
		sr.setLocale("zh_CN");
		self().operationScan(sr, null, head);
		return response;
	}

	public Float getPlantIdByCode(String plantCode) {
		Plant sear = new Plant();
		sear.setPlantCode(plantCode);
		return plantMapper.select(sear).get(0).getPlantId();

	}

	public Float getItemIdByCode(String itemCode, Float plantId) {
		ItemB search = new ItemB();
		search.setItemCode(itemCode);
		search.setPlantId(plantId);
		return itemBMapper.select(search).get(0).getItemId();

	}

	/**
	 * 工序扫描
	 */
	@Override
	public void operationScan(IRequest requestContext, HttpServletRequest request, PqcInspectionH dto)
			throws Exception {
		// 取所有行
		PqcInspectionL searchLine = new PqcInspectionL();
		searchLine.setHeaderId(dto.getHeaderId());
		List<PqcInspectionL> resultLine = pqcInspectionLMapper.myselect(searchLine);
		for (PqcInspectionL line : resultLine) {
			line.setItemId(String.valueOf(dto.getItemId().intValue()));
			line.setProductionQty(dto.getProductionQty());
			line.setSourceOrder(dto.getSourceOrder());
			iPqcInspectionLService.detailSave(line, request);
		}
		// 更新头表H
		PqcInspectionH update = new PqcInspectionH();
		update.setHeaderId(dto.getHeaderId());
		update.setItemId(dto.getItemId());
		update.setProductionQty(dto.getProductionQty());
		update.setSourceOrder(dto.getSourceOrder());
		update.setProductionBatch(dto.getProductionBatch());
		self().updateByPrimaryKeySelective(requestContext, update);
	}

	@Override
	public List<PqcInspectionH> qmsQuery(IRequest requestContext, PqcInspectionH dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return pqcInspectionHMapper.qmsQuery(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService#
	 * generateByTask(javax.servlet.http.HttpServletRequest,
	 * com.hand.hqm.hqm_qc_task.dto.PqcTask)
	 */
	@Override
	public void generateByTask(HttpServletRequest request, PqcTask pqcTask) {// 检验任务生成检验单
		IRequest iRequest = RequestHelper.createServiceRequest(request);
		PqcInspectionH insert = new PqcInspectionH();
		String inspectionNum;// 检验单号
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String inspectionNumStart = this.getPlantCode(pqcTask.getPlantId()) + "-" + time.substring(2) + "-";
		// 流水一个检验单号
		inspectionNum = getInspectionNumber(inspectionNumStart);
		insert.setPlantId(pqcTask.getPlantId());
		insert.setProdLineId(pqcTask.getProdLineId());
		insert.setInspectorId(
				pqcTask.getInspectorId() == null ? null : String.valueOf(pqcTask.getInspectorId().intValue()));
		insert.setShouldInspectionDate(pqcTask.getCheckDate());
		insert.setSourceType("10");
		insert.setInspectionNum(inspectionNum);
		insert.setInspectionRes("P");
		insert.setInspectionJudge("P");
		insert.setApprovalResult("P");
		insert.setInspectionType("PQC");
		insert.setInspectionFrom("1");
		insert.setInspectionStatus("2");
		self().insertSelective(iRequest, insert);
		/**
		 * 通过hqm_pqc_task表的time_id关联HQM_QUA_INS_TIME_l表取的所有对应的工位数据
		 * 根据order_num排序用工位循环生成对应的PQC检验单行 standard_op_id
		 * 取自HQM_QUA_INS_TIME_l表的standard_op_id workstation_id
		 * 取自HQM_QUA_INS_TIME_l表的workstation_id 其他字段默认为空或取默认值
		 */
		QuaInsTimeL quaInsTimeLSearch = new QuaInsTimeL();
		quaInsTimeLSearch.setTimeId(pqcTask.getTimeId());
		List<QuaInsTimeL> quaList = quaInsTimeLMapper.select(quaInsTimeLSearch);
		if (quaList != null && quaList.size() > 0) {
			quaList = quaList.stream().sorted(Comparator.comparing(QuaInsTimeL::getOrderNumber))
					.collect(Collectors.toList());// 排序
			for (QuaInsTimeL quaInsTimeL : quaList) {
				PqcInspectionL insertL = new PqcInspectionL();
				insertL.setHeaderId(insert.getHeaderId());
				insertL.setStandardOpId(quaInsTimeL.getStandardOpId());
				insertL.setWorkstationId(quaInsTimeL.getWorkstationId());
				insertL.setInspectionStatus("0");
				pqcInspectionLMapper.insertSelective(insertL);
				// this.createInspectionCTask(insert, insertL);
			}
		}
		pqcTask.setInspectionNum(inspectionNum);
	}

	public List<PqcInspectionC> createInspectionCTask(PqcInspectionH pqcInspectionH, PqcInspectionL pqcInspectionL) {
		// 与正常的生成C的逻辑唯一不同在 queryOpH.setSourceType("12"); 设置了默认值
		StandardOpInspectionH queryOpH = new StandardOpInspectionH();
		queryOpH.setPlantId(pqcInspectionH.getPlantId());
		queryOpH.setProdLineId(pqcInspectionH.getProdLineId());
		queryOpH.setStandardOpId(pqcInspectionL.getStandardOpId());
		queryOpH.setWorkstationId(pqcInspectionL.getWorkstationId());
		queryOpH.setSourceType(pqcInspectionH.getSourceType());
		StandardOpInspectionH resOpH = standardOpInspectionHMapper.selectOne(queryOpH);
		if (resOpH == null) {
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(iPromptService, "error.hqm_pqc_inspection_create_01"));// 未找到检验工序和该来源类型的检验项目
		}
		StandardOpInspectionL queryOpL = new StandardOpInspectionL();
		queryOpL.setHeadId(resOpH.getHeadId());
		List<StandardOpInspectionL> itemLList = new ArrayList<>();
		itemLList = standardOpInspectionLMapper.select(queryOpL);
		List<PqcInspectionC> insCList = new ArrayList<>();
		for (StandardOpInspectionL standardOpItemL : itemLList) {
			PqcInspectionC addorInspectionC = new PqcInspectionC();
			addorInspectionC.setLineId(pqcInspectionL.getLineId());
			addorInspectionC.setAttributeId(standardOpItemL.getAttributeId());
			pqcInspectionCMapper.insertSelective(addorInspectionC);
			insCList.add(addorInspectionC);
		}
		return insCList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService#getProcess(
	 * javax.servlet.http.HttpServletRequest, java.lang.Float)
	 */
	@Override
	public void getProcess(HttpServletRequest request, Float headerId) throws Exception {
		PqcInspectionH head = new PqcInspectionH();
		head.setHeaderId(headerId);
		head = mapper.selectByPrimaryKey(head);
		ProductionLine pl = new ProductionLine();
		pl.setProdLineId(head.getProdLineId());
		pl = productionLineMapper.selectByPrimaryKey(pl);
		String line = pl.getProdLineCode();
		PqcInspectionL lineSearch = new PqcInspectionL();
		lineSearch.setHeaderId(headerId);
		List<PqcInspectionL> lineResult = pqcInspectionLMapper.myselect(lineSearch);
		for (PqcInspectionL p : lineResult) {
			String workStation = p.getWorkstationCode();
			webServiceMes(line, workStation);
		}

	}

	public void webServiceMes(String par1, String par2) throws Exception {
		IfInvokeOutbound iio = new IfInvokeOutbound();
		String param = "{" + "\"line\":\"" + par1 + "\"," + "\"workStation\":null," + "\"inspectionNum\":\"" + par2
				+ "\"" + "}";
		ServiceRequest sr = new ServiceRequest();
		sr.setLocale("zh_CN");
		String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "MES_WS_URI");// 获取调用地址
		SoapPostUtil.Response re = SoapPostUtil.ticketSrmToMes(uri, "getProcessQualityInfo", param, iio);
		ifInvokeOutboundMapper.insertSelective(iio);
		logger.info(SoapPostUtil.getStringFromResponse(re));
		if (re.getResult()) {// 成功的结果 执行工序扫描
			ObjectMapper mapper = new ObjectMapper();
			ProcessResult pr = mapper.readValue(re.getMessage(), ProcessResult.class);
			if ("0".equals(pr.result))
				throw new RuntimeException(pr.error_info);
			PqcInspectionH search = new PqcInspectionH();
			search.setInspectionNum(pr.result_info.inspectionNum);
			List<PqcInspectionH> hResult = pqcInspectionHMapper.select(search);
			PqcInspectionH head = hResult.get(0);
			Float plantId = getPlantIdByCode("CNKE");
			Float itemId = getItemIdByCode(pr.result_info.itemCode, plantId);
			head.setItemId(itemId);
			head.setPlantId(plantId);
			head.setProductionQty(1f);
			head.setSourceOrder(pr.result_info.orderNo);
			head.setItemEdition(pr.result_info.itemVersion);
			self().operationScan(sr, null, head);
		} else {
			throw new RuntimeException(re.getMessage());
		}

	}

	@Override
	public void operationGet(HttpServletRequest request, String line, String inspectionNum) throws Exception {
		PqcInspectionH head = new PqcInspectionH();
		head.setInspectionNum(inspectionNum);
		head = mapper.select(head).get(0);
		ProductionLine pl = new ProductionLine();
		pl.setProdLineId(head.getProdLineId());
		pl = productionLineMapper.selectByPrimaryKey(pl);
		// String line = pl.getProdLineCode();
		PqcInspectionL lineSearch = new PqcInspectionL();
		lineSearch.setHeaderId(head.getHeaderId());
		webServiceMes(line, inspectionNum);
	}
}