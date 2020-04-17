package com.hand.hcs.hcs_supply_plan.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.mail.dto.MessageTemplate;
import com.hand.hap.mail.mapper.MessageTemplateMapper;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.MailUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketL;
import com.hand.hcs.hcs_delivery_ticket.mapper.DeliveryTicketLMapper;
import com.hand.hcs.hcs_po_line_locations.dto.PoLineLocations;
import com.hand.hcs.hcs_po_line_locations.mapper.PoLineLocationsMapper;
import com.hand.hcs.hcs_po_line_locations.service.IPoLineLocationsService;
import com.hand.hcs.hcs_po_lines.dto.PoLines;
import com.hand.hcs.hcs_po_lines.mapper.PoLinesMapper;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;
import com.hand.hcs.hcs_supply_demand.mapper.SupplyDemandMapper;
import com.hand.hcs.hcs_supply_demand.service.ISupplyDemandService;
import com.hand.hcs.hcs_supply_plan.dto.SupplyPlan;
import com.hand.hcs.hcs_supply_plan.mapper.SupplyPlanMapper;
import com.hand.hcs.hcs_supply_plan.service.ISupplyPlanService;
import com.hand.srm.srm_user_purchase_group.dto.UserPurchaseGroup;
import com.hand.srm.srm_user_purchase_group.mapper.UserPurchaseGroupMapper;
import com.hand.sys.sys_user.dto.UserSys;
import com.hand.sys.sys_user.mapper.UserSysMapper;
import jodd.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplyPlanServiceImpl extends BaseServiceImpl<SupplyPlan> implements ISupplyPlanService {

	@Autowired
	ICodeService iCodeService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	SuppliersMapper suppliersMapper;
	@Autowired
	ISupplyDemandService iSupplyDemandService;
	@Autowired
	SupplyDemandMapper supplyDemandMapper;
	@Autowired
	SupplyPlanMapper mapper;
	@Autowired
	PoLinesMapper poLinesMapper;
	@Autowired
	DeliveryTicketLMapper deliveryTicketLMapper;
	@Autowired
	IPoLineLocationsService iPoLineLocationsService;
	@Autowired
	UserPurchaseGroupMapper userPurchaseGroupMapper;
	@Autowired
	UserSysMapper userSysMapper;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	PoLineLocationsMapper poLineLocationsMapper;
	@Autowired
	private MessageTemplateMapper messageTemplateMapper;
	private Logger logger = LoggerFactory.getLogger(SupplyPlanServiceImpl.class);

	@Override
	public List<SupplyPlan> reSelect(IRequest requestContext, SupplyPlan dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		if(!StringUtils.isEmpty(dto.getSortname())) {
			dto.setSortname(SystemApiMethod.camelToUnderline(dto.getSortname(), 1));
		}
		return mapper.reSelect(dto);
	}

	@Override
	public List<SupplyPlan> reBatchUpdate(IRequest request, List<SupplyPlan> list) {
		int thisBatchCount = 0;
		for (SupplyPlan t : list) {
			switch (((BaseDTO) t).get__status()) {
			case DTOStatus.ADD:
				if (StringUtils.isEmpty(t.getSupplyPlanNum())) {
					t.setSupplyPlanNum(getMaxSupplyPlanNum(t.getItemId(), t.getPlantId()));
				}
				if (StringUtils.isEmpty(t.getSupplyPlanLineNum())) {
					thisBatchCount++;
					t.setSupplyPlanLineNum(String.valueOf(thisBatchCount));
				}
				self().insertSelective(request, t);
				break;
			case DTOStatus.UPDATE:
				if (useSelectiveUpdate()) {
					self().updateByPrimaryKeySelective(request, t);
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
		for (SupplyPlan sp : list) {
			List<SupplyPlan> res = mapper.selectSumSupplierDeliveryQty(sp);// 求供应商数量之和A,需求数量B,实际需发运数量C A需小于B与C的大值,不满足就报错
			if (res.get(0)
					.getSupplierDeliveryQty() > (res.get(0).getRequireQty() > res.get(0).getRealityShipQty()
							? res.get(0).getRequireQty()
							: res.get(0).getRealityShipQty())) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(request, iPromptService, "error.srm_purchase_1042"));
			}
		}
		return list;
	}

	@Override
	public List<SupplyPlan> maxLineNumSelect(IRequest requestContext, SupplyPlan dto) {
		dto.setSupplyPlanLineNum(dto.getSupplyPlanLineNum().split("-")[0]);
		List<SupplyPlan> selectList = mapper.maxLineNumSelect(dto);
		List<SupplyPlan> resli = new ArrayList<SupplyPlan>();
		resli.add(new SupplyPlan());
		int max = 0;
		for (SupplyPlan su : selectList) {
			if (su.getSupplyPlanLineNum().split("-")[0].length() != dto.getSupplyPlanLineNum().split("-")[0].length()) {
				continue;
			}
			if (su.getSupplyPlanLineNum().split("-").length > 1) {
				max = max < Integer.parseInt(su.getSupplyPlanLineNum().split("-")[1])
						? Integer.parseInt(su.getSupplyPlanLineNum().split("-")[1])
						: max;
			}
		}
		resli.get(0).setSupplyPlanLineNum(String.valueOf(max));
		return resli;
	}

	@Override
	public synchronized List<SupplyDemand> jitExcelImport(IRequest requestCtx, MultipartFile forModel)
			throws RuntimeException, IOException {

		List<SupplyDemand> returnList = jitExcelImport(requestCtx, forModel.getInputStream());
		return returnList;
	}

	/**
	 * 
	 * @description jit excel数据解析
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param inputStream
	 * @return
	 * @throws RuntimeException
	 * @throws IOException
	 */
	public List<SupplyDemand> jitExcelImport(IRequest requestCtx, InputStream inputStream)
			throws RuntimeException, IOException {
		Sheet sheet;
		XSSFWorkbook workBook;
		List<SupplyDemand> totalList = new ArrayList<SupplyDemand>();
		workBook = new XSSFWorkbook(inputStream);
		StringBuilder totalMessage = new StringBuilder();// 全局错误信息
		int countTotalError = 0;// 全局错误数量
		sheet = workBook.getSheetAt(0);
		// 默认工厂
		String defaultPlantCode = iCodeService.getCodeValuesByCode(requestCtx, "HQM_DEFAULT_PLANT").get(0).getMeaning();
		Plant searchPlant = new Plant();
		searchPlant.setPlantCode(defaultPlantCode);
		Float defaultPlantId = plantMapper.select(searchPlant).get(0).getPlantId();// 默认ID

		Row shiftRow = sheet.getRow(0);// 白班夜班标识行
		Row titleRow = sheet.getRow(1);// 标题日期标识行

		// 供货需求批号
		String supplyDemandNum = getMaxSupplyPlanNumJIT();

		// 获取一次错误信息
		String itemErrorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"error.srm_purchase_1033");
		String supplierErrorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"error.srm_purchase_1034");
		String requireQtyErrorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"error.srm_purchase_1038");

		List<Date> dateList;
		dateList = getJitDateList(requestCtx, shiftRow, titleRow);
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			// i=2,从第3行开始
			Row row = sheet.getRow(i);
			if (row.getCell(9) == null) {
				continue;
			} else {
				row.getCell(9).setCellType(CellType.STRING);
				if (!"Supply".toLowerCase().equals(row.getCell(9).getStringCellValue().toLowerCase())) {
					continue;
				}
			}
			SupplyDemand rowModel = new SupplyDemand();
			// 行解析

			// D(3)列物料解析
			if (row.getCell(3) == null) {
				totalMessage.append(MessageFormat.format(itemErrorMessage, i + 1, ""));
				countTotalError++;
			} else {
				row.getCell(3).setCellType(CellType.STRING);

				if (StringUtil.isEmpty(row.getCell(3).getStringCellValue())) {
					totalMessage.append(MessageFormat.format(itemErrorMessage, i + 1, ""));
					countTotalError++;
				} else {
					ItemB itemSearch = new ItemB();
					itemSearch.setItemCode(row.getCell(3).getStringCellValue());
					itemSearch.setPlantId(defaultPlantId);
					List<ItemB> itemBList = itemBMapper.select(itemSearch);
					if (itemBList == null || itemBList.size() == 0) {
						totalMessage.append(
								MessageFormat.format(itemErrorMessage, i + 1, row.getCell(3).getStringCellValue()));
						countTotalError++;
					} else {
						if (!"JIT".equals(itemBList.get(0).getPurchaseType())
								&& !"LARGE".equals(itemBList.get(0).getPurchaseType())) {
							continue;
						}
						rowModel.setItemId(itemBList.get(0).getItemId());
						rowModel.setPlantId(defaultPlantId);
						rowModel.setPurchaseType(itemBList.get(0).getPurchaseType());
					}
				}
			}
			// H(7)列供应商解析
			if (row.getCell(7) == null) {
				totalMessage.append(MessageFormat.format(supplierErrorMessage, i + 1, ""));
				countTotalError++;
			} else {
				Suppliers suppliersSearch = new Suppliers();
				row.getCell(7).setCellType(CellType.STRING);
				if (StringUtil.isEmpty(row.getCell(7).getStringCellValue())) {
					totalMessage.append(MessageFormat.format(supplierErrorMessage, i + 1, ""));
					countTotalError++;
				} else {
					suppliersSearch.setSupplierCode(row.getCell(7).getStringCellValue());
					List<Suppliers> suppliersList = suppliersMapper.select(suppliersSearch);
					if (suppliersList == null || suppliersList.size() < 1) {
						totalMessage.append(
								MessageFormat.format(supplierErrorMessage, i + 1, row.getCell(7).getStringCellValue()));
						countTotalError++;
					} else {
						rowModel.setSupplierId(suppliersList.get(0).getSupplierId());
					}
				}
			}
			rowModel.setSupplyDemandNum(supplyDemandNum);
			rowModel.setSupplyPlanLineNum(Float.valueOf(String.valueOf(i)));
			rowModel.setSourceType("EXCEL");
			rowModel.setSupplyPlanFlag("N");

			for (int j = 12; j < titleRow.getLastCellNum(); j++) {
				SupplyDemand rowMainModel = new SupplyDemand();
				rowMainModel.setSupplyDemandNum(rowModel.getSupplyDemandNum());
				rowMainModel.setItemId(rowModel.getItemId());
				rowMainModel.setPlantId(rowModel.getPlantId());
				rowMainModel.setPurchaseType(rowModel.getPurchaseType());
				rowMainModel.setSupplierId(rowModel.getSupplierId());
				rowMainModel.setSourceType(rowModel.getSourceType());
				rowMainModel.setSupplyPlanFlag(rowModel.getSupplyPlanFlag());
				rowMainModel.setSupplyPlanLineNum(Float.valueOf(String.valueOf((i - 2) * 12 + j - 11)));
				rowMainModel.setRequireTime(dateList.get(j - 12));
				if (row.getCell(j) == null) {
					continue;
				}
				row.getCell(j).setCellType(CellType.STRING);
				if (StringUtils.isEmpty(row.getCell(j).getStringCellValue())
						|| "0".equals(row.getCell(j).getStringCellValue())) {
					continue;
				}
				try {
					rowMainModel.setRequireQty(Float.valueOf(row.getCell(j).getStringCellValue()));
				} catch (Exception e) {
					totalMessage.append(MessageFormat.format(requireQtyErrorMessage, i + 1, j));
					countTotalError++;
				}
				totalList.add(rowMainModel);
			}
		}
		try {
			workBook.close();
		} catch (Exception e) {

		}
		if (countTotalError > 0) {
			throw new RuntimeException(totalMessage.toString());
		} else {
			for (SupplyDemand supplyDemand : totalList) {
				iSupplyDemandService.insertSelective(requestCtx, supplyDemand);
			}
		}
		generateJitPlanData(requestCtx, supplyDemandNum, totalList);
		return totalList;
	}

	/**
	 * 生成jit计划
	 * 
	 * @param requestCtx
	 * @param supplyDemandNum
	 * @param totalList
	 */
	public void generateJitPlanData(IRequest requestCtx, String supplyDemandNum, List<SupplyDemand> totalList) {
		SupplyDemand searchDemand = new SupplyDemand();
		searchDemand.setSupplyDemandNum(supplyDemandNum);
		List<SupplyDemand> leadTimeList = supplyDemandMapper.selectLeadTime(searchDemand);
		SupplyPlan searchPlan = new SupplyPlan();
		List<SupplyPlan> leadTimePlanList = mapper.selectLeadTime(searchPlan);
		for (SupplyPlan supplyPlan : leadTimePlanList) {
			supplyPlan.setStatus("C");
			self().updateByPrimaryKeySelective(requestCtx, supplyPlan);
		}
		List<SupplyPlan> insertPlanList = new ArrayList<SupplyPlan>();
		int countIns = 0;
		for (SupplyDemand supplyDemand : leadTimeList) {
			countIns++;
			SupplyPlan insert = new SupplyPlan();
			insert.setSupplyPlanNum(supplyDemandNum);
			insert.setSupplyPlanLineNum(String.valueOf(countIns));
			insert.setItemId(supplyDemand.getItemId());
			insert.setPlantId(supplyDemand.getPlantId());
			insert.setPurchaseType(supplyDemand.getPurchaseType());
			insert.setSupplierId(supplyDemand.getSupplierId());
			insert.setRequireTime(supplyDemand.getRequireTime());
			insert.setRequireQty(supplyDemand.getRequireQty());
			insert.setStatus("N");
			insert.setShipPlanFlag("N");
			insert.setSourceType("E");
			insertPlanList.add(insert);
			SupplyDemand upda = new SupplyDemand();
			upda.setSupplyDemandId(supplyDemand.getSupplyDemandId());
			upda.setSupplyPlanFlag("Y");
			supplyDemandMapper.updateByPrimaryKeySelective(upda);
		}
		for (SupplyPlan supplyPlan : insertPlanList) {
			self().insertSelective(requestCtx, supplyPlan);
		}
		stjitMail(requestCtx, insertPlanList);
	}

	/**
	 * 获取导入的Excel中所有白班和夜班对应的日期
	 * 
	 * @param requestCtx
	 * @param shiftRow
	 * @param titleRow
	 * @return
	 * @throws RuntimeException
	 */
	private List<Date> getJitDateList(IRequest requestCtx, Row shiftRow, Row titleRow) throws RuntimeException {

		String requireTimeErrorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"error.srm_purchase_1037");
		List<Date> returnList = new ArrayList<Date>();
		String shiftNightTime = iCodeService.getCodeMeaningByValue(requestCtx, "SRM_SUPPLY_DEMAND_TIME", "N");// 晚班时间
		String shiftDayTime = iCodeService.getCodeMeaningByValue(requestCtx, "SRM_SUPPLY_DEMAND_TIME", "D");// 白班时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy/MM/dd");
		for (int k = 12; k < titleRow.getLastCellNum(); k++) {
			try {
				if (titleRow.getCell(k) == null) {
					throw new RuntimeException(MessageFormat.format(requireTimeErrorMessage, 2, k + 1));
				}
				if ("D".equals(shiftRow.getCell(k).getStringCellValue().substring(0, 1))) {// 白班
					returnList.add(sdf.parse(sdfr.format(titleRow.getCell(k).getDateCellValue()) + shiftDayTime));
				} else {
					returnList.add(sdf.parse(sdfr.format(titleRow.getCell(k).getDateCellValue()) + shiftNightTime));
				}
			} catch (Exception e) {
				throw new RuntimeException(MessageFormat.format(requireTimeErrorMessage, 2, k + 1));
			}

		}
		return returnList;
	}

	/**
	 * st jit 组织数据发送邮件的入口 不管邮件是否发送成功 上面的操作依旧保存
	 * 
	 * @param requestCtx
	 * @param insertPlanList
	 */
	public void stjitMail(IRequest requestCtx, List<SupplyPlan> insertPlanList) {
		// taskExecutor
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		SupplyPlan search = new SupplyPlan();
		for (SupplyPlan sp : insertPlanList) {
//			search.setSupplyPlanId(sp.getSupplyPlanId());
//			sp = mapper.reSelect(search).get(0);
			sp.setMonday("WK" + sdf.format(SystemApiMethod.getThisWeekMonday(sp.getRequireTime())));
		}
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// 先分组
					loadItemPlantSupplier(insertPlanList);

					Map<Float, List<SupplyPlan>> groupMap = insertPlanList.stream().collect(Collectors.groupingBy(p -> {
						return p.getSupplierId();
					}));
					for (Map.Entry<Float, List<SupplyPlan>> entry : groupMap.entrySet()) {
//						List<SupplyPlan> result = entry.getValue().stream().sorted(
//								Comparator.comparing(SupplyPlan::getItemCode).thenComparing(SupplyPlan::getRequireTime))
//								.collect(Collectors.toList());
						List<SupplyPlan> result = entry.getValue().stream().sorted(
								Comparator.comparing(SupplyPlan::getItemCode).thenComparing(SupplyPlan::getRequireTime))
								.collect(Collectors.toList());
						generatorExcelAndSendMail(requestCtx, result, entry.getKey());
					}
				} catch (Exception e) {
					logger.error("邮件发送产生了错误:" + e.getMessage());
				}
			}
		});
	}

	/**
	 * 
	 * @description 获取数据的物料 工厂 供应商 编码 描述相关的信息
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param insertPlanList
	 */
	public void loadItemPlantSupplier(List<SupplyPlan> insertPlanList) {
		for (SupplyPlan supply : insertPlanList) {
			ItemB items = new ItemB();
			items.setItemId(supply.getItemId());
			items.setPlantId(supply.getPlantId());
			items = itemBMapper.reSelect(items).get(0);
			supply.setItemCode(items.getItemCode());
			supply.setItemDescriptions(items.getItemDescriptions());
			Suppliers sus = new Suppliers();
			sus.setSupplierId(supply.getSupplierId());
			sus = suppliersMapper.selectOne(sus);
			supply.setSupplierCode(sus.getSupplierCode());
			supply.setSupplierName(sus.getSupplierName());
		}
	}

	/**
	 * 
	 * @description 生成供货计划操作 发送邮件
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param result
	 * @param supplierId
	 */
	public void generatorExcelAndSendMail(IRequest requestCtx, List<SupplyPlan> result, Float supplierId) {
		MessageTemplate mt = getMailTemplate("SRM_NEW_SUPPLY_PLAN_CONFIRM");
		String to = "";// 收件人邮箱
		Suppliers suppliersSearch = new Suppliers();
		suppliersSearch.setSupplierId(supplierId);
		List<Suppliers> suList = suppliersMapper.query(suppliersSearch);
		if (suList == null || suList.size() < 1) {
			return;
		} else {
			for (Suppliers suu : suList) {
				to = to + suu.getEmail() + ",";
			}
			to = to.substring(0, to.length() - 1);

		}
		XSSFWorkbook xlsFile = generatorExcel(requestCtx, result); // create a workbook
		List<XSSFWorkbook> excel = new ArrayList<XSSFWorkbook>();
		excel.add(xlsFile);
		List<String> names = new ArrayList<String>();
		names.add("新供货计划确认_" + result.get(0).getSupplyPlanNum() + ".xlsx");

		MailUtil.sendExcelMail(to, null, MessageFormat.format(mt.getSubject(), result.get(0).getSupplyPlanNum()),
				mt.getContent(), excel, names, mt.getUserName(), null);
		excel.stream().forEach(t -> {
			try {
				t.close();
			} catch (IOException e) {

			}
		});
	}

	/**
	 * 
	 * @description 把传入的数据组织成excel文件对象
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param r
	 * @param result
	 * @return
	 */
	public XSSFWorkbook generatorExcel(IRequest r, List<SupplyPlan> result) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		XSSFWorkbook xlsFile = new XSSFWorkbook();
		CreationHelper helper = xlsFile.getCreationHelper();
		Sheet sheet1 = xlsFile.createSheet("sheeet1"); // add a sheet to your workbook
		sheet1.setColumnWidth(0, 4000);
		sheet1.setColumnWidth(1, 1000);
		sheet1.setColumnWidth(2, 2000);
		sheet1.setColumnWidth(3, 3000);
		sheet1.setColumnWidth(4, 4000);
		sheet1.setColumnWidth(5, 3000);
		sheet1.setColumnWidth(6, 5000);
		sheet1.setColumnWidth(7, 2000);
		sheet1.setColumnWidth(8, 3000);
		sheet1.setColumnWidth(9, 3000);
		sheet1.setColumnWidth(10, 6000);
		Row row1 = sheet1.createRow(0);
//		sheet1.setColumnWidth(0, 25 * 256);
//		sheet1.setColumnWidth(1, 30 * 256);
		row1.createCell(0).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.supplyplannum")));
		row1.createCell(1).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.upplyplanlinenum")));
		row1.createCell(2).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.monday")));
		row1.createCell(3).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.itemid")));
		row1.createCell(4).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.itemdescriptions")));
		row1.createCell(5).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.purchasetype")));
		row1.createCell(6).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.requiretime")));
		row1.createCell(7).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.requireqty")));
		row1.createCell(8).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.realityshipqty")));
		row1.createCell(9).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.suppliercode")));
		row1.createCell(10).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.suppliername")));

		for (int i = 0; i < result.size(); i++) {
			Row row = sheet1.createRow((i + 1));
			row.createCell(0).setCellValue(result.get(i).getSupplyPlanNum());
			row.createCell(1).setCellValue(result.get(i).getSupplyPlanLineNum());
			row.createCell(2).setCellValue(result.get(i).getMonday());
			row.createCell(3).setCellValue(result.get(i).getItemCode());
			row.createCell(4).setCellValue(result.get(i).getItemDescriptions());
			row.createCell(5).setCellValue(
					iCodeService.getCodeMeaningByValue(r, "HCS_PURCHASE_ITEM_TYPE", result.get(i).getPurchaseType()));
			row.createCell(6).setCellValue(sdf.format(result.get(i).getRequireTime()));
			row.createCell(7).setCellValue(result.get(i).getRequireQty());
			row.createCell(8).setCellValue(result.get(i).getRealityShipQty());
			row.createCell(9).setCellValue(result.get(i).getSupplierCode());
			row.createCell(10).setCellValue(result.get(i).getSupplierName());
		}
		return xlsFile;
	}

	/**
	 * 
	 * @description 通过code获取快速编码描述meaning
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param code
	 * @return
	 */
	public String meaning(IRequest requestCtx, String code) {
		return SystemApiMethod.getPromptDescription(requestCtx, iPromptService, code);
	}

	@Override
	public synchronized List<SupplyDemand> stExcelImport(IRequest requestCtx, MultipartFile forModel)
			throws RuntimeException, IOException {
		List<SupplyDemand> returnList = stExcelImport(requestCtx, forModel.getInputStream());
		return returnList;
	}

	/**
	 * 
	 * @description st文件导入 开始
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param inputStream 文件流
	 * @return
	 * @throws RuntimeException
	 * @throws IOException
	 */
	public List<SupplyDemand> stExcelImport(IRequest requestCtx, InputStream inputStream)
			throws RuntimeException, IOException {
		Sheet sheet;
		XSSFWorkbook workBook;
		List<SupplyDemand> totalList = new ArrayList<SupplyDemand>();
		workBook = new XSSFWorkbook(inputStream);
		StringBuilder totalMessage = new StringBuilder();// 全局错误信息
		int countTotalError = 0;// 全局错误数量
		// 默认工厂
		String defaultPlantCode = iCodeService.getCodeValuesByCode(requestCtx, "HQM_DEFAULT_PLANT").get(0).getMeaning();// 默认编码
		Plant searchPlant = new Plant();
		searchPlant.setPlantCode(defaultPlantCode);
		Float defaultPlantId = plantMapper.select(searchPlant).get(0).getPlantId();// 默认ID
		// 获取一次错误信息
		String itemErrorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"error.srm_purchase_1033");
		String supplierErrorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"error.srm_purchase_1034");
		String requireTimeErrorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"error.srm_purchase_1035");
		String requireQtyErrorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"error.srm_purchase_1036");
		// 供货需求批号
		String supplyDemandNum = getMaxSupplyPlanNumST();
		sheet = workBook.getSheetAt(0);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			// i=1,从第二行开始
			Row row = sheet.getRow(i);
			SupplyDemand rowModel = new SupplyDemand();
			// 行解析
			// A(0)列物料解析
			ItemB itemSearch = new ItemB();
			if (row.getCell(0) == null) {
				totalMessage.append(MessageFormat.format(itemErrorMessage, i + 1, ""));
				countTotalError++;
			} else {
				row.getCell(0).setCellType(CellType.STRING);
				if (StringUtil.isEmpty(row.getCell(0).getStringCellValue())) {
					totalMessage.append(MessageFormat.format(itemErrorMessage, i + 1, ""));
					countTotalError++;
				} else {
					itemSearch.setItemCode(row.getCell(0).getStringCellValue());
					itemSearch.setPlantId(defaultPlantId);
					List<ItemB> itemBList = itemBMapper.select(itemSearch);
					if (itemBList == null || itemBList.size() < 1) {
						totalMessage.append(
								MessageFormat.format(itemErrorMessage, i + 1, row.getCell(0).getStringCellValue()));
						countTotalError++;
					} else {
						if ("OTHERS".equals(itemBList.get(0).getPurchaseType())) {
							rowModel.setItemId(itemBList.get(0).getItemId());
							rowModel.setPlantId(defaultPlantId);
							rowModel.setPurchaseType("OTHERS");
						} else {
							if (StringUtil.isEmpty(itemBList.get(0).getPurchaseType())) {
								totalMessage.append(MessageFormat.format(itemErrorMessage, i + 1,
										row.getCell(0).getStringCellValue()));
								countTotalError++;
							} else {
								continue;
							}
						}
					}
				}
			}
			// E(4)列供应商解析
			Suppliers suppliersSearch = new Suppliers();
			if (row.getCell(4) == null) {
				totalMessage.append(MessageFormat.format(supplierErrorMessage, i + 1, ""));
				countTotalError++;
			} else {
				row.getCell(4).setCellType(CellType.STRING);
				if (StringUtil.isEmpty(row.getCell(4).getStringCellValue())) {
					totalMessage.append(MessageFormat.format(supplierErrorMessage, i + 1, ""));
					countTotalError++;
				} else {
					suppliersSearch.setSupplierCode(row.getCell(4).getStringCellValue());
					List<Suppliers> suppliersList = suppliersMapper.select(suppliersSearch);
					if (suppliersList == null || suppliersList.size() == 0) {
						totalMessage.append(
								MessageFormat.format(supplierErrorMessage, i + 1, row.getCell(4).getStringCellValue()));
						countTotalError++;
					} else {
						rowModel.setSupplierId(suppliersList.get(0).getSupplierId());
					}
				}
			}
			// K(10)需求时间
			try {
				rowModel.setRequireTime(row.getCell(10).getDateCellValue());
				if (row.getCell(10).getDateCellValue() == null) {
					throw new Exception();
				}
			} catch (Exception e) {
				totalMessage.append(
						MessageFormat.format(requireTimeErrorMessage, i + 1, row.getCell(10).getStringCellValue()));
				countTotalError++;
			}
			// L(11)需求数量
			try {
				if (row.getCell(11) == null) {
					totalMessage.append(MessageFormat.format(requireQtyErrorMessage, i + 1, ""));
					countTotalError++;
				} else {
					row.getCell(11).setCellType(CellType.STRING);
					if (StringUtil.isEmpty(row.getCell(11).getStringCellValue())) {
						totalMessage.append(MessageFormat.format(requireQtyErrorMessage, i + 1, ""));
						countTotalError++;
					} else {
						rowModel.setRequireQty(Float.valueOf(row.getCell(11).getStringCellValue()));
					}
				}
			} catch (Exception e) {
				totalMessage.append(
						MessageFormat.format(requireQtyErrorMessage, i + 1, row.getCell(11).getStringCellValue()));
				countTotalError++;
			}
			// M(12)QA数量
			try {
				if (row.getCell(12) == null) {
					rowModel.setPresentQa(0f);
				} else {
					row.getCell(12).setCellType(CellType.STRING);
					if (StringUtil.isEmpty(row.getCell(12).getStringCellValue())) {
						rowModel.setPresentQa(0f);
					} else {
						rowModel.setPresentQa(Float.valueOf(row.getCell(12).getStringCellValue()));
					}
				}
			} catch (Exception e) {
				rowModel.setPresentQa(0f);
			}
			// O(14) 安全库存
			if (row.getCell(14) == null) {
				rowModel.setSafetyStockValue(0f);
			} else {
				row.getCell(14).setCellType(CellType.STRING);
				if (StringUtil.isEmpty(row.getCell(11).getStringCellValue())) {
					rowModel.setSafetyStockValue(0f);
				} else {
					try {
						rowModel.setSafetyStockValue(Float.valueOf(row.getCell(14).getStringCellValue()));
					} catch (Exception e) {
						rowModel.setSafetyStockValue(0f);
					}
				}
			}
			rowModel.setSupplyDemandNum(supplyDemandNum);
			rowModel.setSupplyPlanLineNum(Float.valueOf(String.valueOf(i)));
			rowModel.setSourceType("EXCEL");
			rowModel.setSupplyPlanFlag("N");
			rowModel.setPurchaseType("OTHERS");
			totalList.add(rowModel);
		}
		try {
			workBook.close();
		} catch (Exception e) {

		}
		if (countTotalError > 0) {
			throw new RuntimeException(totalMessage.toString());
		} else {
			for (SupplyDemand supplyDemand : totalList) {
				iSupplyDemandService.insertSelective(requestCtx, supplyDemand);
			}
		}
		try {
			generateStPlanData(requestCtx, supplyDemandNum, totalList);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}

		return totalList;
	}

	/**
	 * 
	 * @param requestCtx
	 * @param supplyDemandNum 本次导入的批次号/供货需求编号
	 * @param totalList
	 * @throws ParseException
	 */
	public void generateStPlanData(IRequest requestCtx, String supplyDemandNum, List<SupplyDemand> totalList)
			throws ParseException {

		// wtm 20200323 将系统中已存在的 新建 状态的供货计划 取消为 取消状态
		cancelNewCreat(requestCtx);

		String week = iCodeService.getCodeValuesByCode(requestCtx, "SRM_OTHERS_DEMAND_TIME").get(0).getValue();// 普通物料交期配置
																												// 周几
		String time = iCodeService.getCodeValuesByCode(requestCtx, "SRM_OTHERS_DEMAND_TIME").get(0).getMeaning();// 普通物料交期配置
																													// 几点
		SupplyDemand search = new SupplyDemand();
		search.setSupplyDemandNum(supplyDemandNum);
		search.setWeek(week);
		/**
		 * 供货需求按周汇总 RequireWeeks 值为 导入周值
		 */
		List<SupplyDemand> weeksList = supplyDemandMapper.selectWeeksGroup(search);
		List<SupplyPlan> insertPlanList = new ArrayList<SupplyPlan>();
		List<SupplyPlan> updatePlanList = new ArrayList<SupplyPlan>();
		for (SupplyDemand supplyDemand : weeksList) {
			SupplyPlan searchPlan = new SupplyPlan();
			searchPlan.setPlantId(supplyDemand.getPlantId());
			searchPlan.setItemId(supplyDemand.getItemId());
			searchPlan.setSupplierId(supplyDemand.getSupplierId());
			searchPlan.setRequireWeeks(supplyDemand.getRequireWeeks());
			supplyDemand.setSupplyDemandNum(supplyDemandNum);
			List<SupplyPlan> resultPlans = mapper.existsSelect(searchPlan);
			/**
			 * int count = 0; 保证 同 item plant supplier week 只新增一条
			 */
			int count = 0;
			if (resultPlans != null && resultPlans.size() > 0) {
				for (SupplyPlan supplyPlan : resultPlans) {
					if (supplyPlan.getStatus() != null) {
						SupplyPlan updateDto = new SupplyPlan();
						switch (supplyPlan.getStatus()) {
						case "N":
							updateDto.setSupplyPlanId(supplyPlan.getSupplyPlanId());
							updateDto.setStatus("C");
							updatePlanList.add(updateDto);
							if (count != 1) {
								insertPlanList.add(
										getSupplyPlanBySupplyDemand(supplyPlan, supplyDemand, time, insertPlanList));
								count++;
							}
//							self().updateByPrimaryKeySelective(requestCtx, updateDto);
							break;
						case "A":
							updateDto.setSupplyPlanId(supplyPlan.getSupplyPlanId());
							updateDto.setStatus("C");
							updatePlanList.add(updateDto);
							if (count != 1) {
								insertPlanList.add(
										getSupplyPlanBySupplyDemand(supplyPlan, supplyDemand, time, insertPlanList));
								count++;
							}
//							self().updateByPrimaryKeySelective(requestCtx, updateDto);
							break;
						case "C":
							if (count != 1) {
								insertPlanList.add(
										getSupplyPlanBySupplyDemand(supplyPlan, supplyDemand, time, insertPlanList));
								count++;
							}
							break;
						case "S":
							if (count != 1) {
								insertPlanList.add(
										getSupplyPlanBySupplyDemand(supplyPlan, supplyDemand, time, insertPlanList));
								count++;
							}
							break;
						}
					}
				}
			} else {
				insertPlanList.add(getSupplyPlanBySupplyDemand(null, supplyDemand, time, insertPlanList));
			}
		}
		// 更新和插入操作
		for (SupplyPlan sp : updatePlanList) {
			self().updateByPrimaryKeySelective(requestCtx, sp);
		}

		for (SupplyPlan ins : insertPlanList) {
			self().insertSelective(requestCtx, ins);
		}
		/**
		 * add by wtm 20200113 生成供货计划按钮的逻辑 迁移到 st导入这里
		 */
		safeQa(requestCtx, insertPlanList);
		// 供货计划标识supply_plan_flag改为Y
		for (SupplyDemand supplyDemand : totalList) {
			SupplyDemand upda = new SupplyDemand();
			upda.setSupplyDemandId(supplyDemand.getSupplyDemandId());
			upda.setSupplyPlanFlag("Y");
			supplyDemandMapper.updateByPrimaryKeySelective(upda);
		}
		stjitMail(requestCtx, insertPlanList);
	}

	/**
	 * 取消已存在的所有供货计划
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月23日
	 * @param requestCtx
	 */
	public void cancelNewCreat(IRequest requestCtx) {
		SupplyPlan search = new SupplyPlan();
		search.setStatus("N");
		List<SupplyPlan> result = mapper.reSelect(search);
		result.stream().filter(p -> "OTHERS".equals(p.getPurchaseTypeItem())).forEach(p -> {
			p.setStatus("C");
			self().updateByPrimaryKeySelective(requestCtx, p);
		});
	}

	/**
	 * 
	 * @description 通过供货需求SupplyDemand得到供货计划SupplyPlan
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param supplyDemand
	 * @param time
	 * @param list
	 * @return
	 * @throws ParseException
	 */
	public SupplyPlan getSupplyPlanBySupplyDemand(SupplyPlan supplyPlan, SupplyDemand supplyDemand, String time,
			List<SupplyPlan> list) throws ParseException {
		SupplyPlan insertDto = new SupplyPlan();
		insertDto.setSupplyPlanNum(supplyDemand.getSupplyDemandNum());
		insertDto.setSupplyPlanLineNum(String.valueOf(list.size() + 1));
		insertDto.setItemId(supplyDemand.getItemId());
		insertDto.setPlantId(supplyDemand.getPlantId());
		insertDto.setSupplierId(supplyDemand.getSupplierId());
		insertDto.setPurchaseType(supplyDemand.getPurchaseType());
		SimpleDateFormat sdfday = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat sdftime = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
		insertDto.setRequireTime(sdftime.parse(sdfday.format(supplyDemand.getRequireWeek()) + time));
		insertDto.setRequireQty(supplyDemand.getRequireQty());
		insertDto.setStatus("N");
		insertDto.setSourceType("E");
		insertDto.setShipPlanFlag("N");
		return insertDto;

	}

	/**
	 * 
	 * @description 流水一个新增的批次号
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param itemId
	 * @param plantId
	 * @return
	 */
	public String getMaxSupplyPlanNum(Float itemId, Float plantId) {
		ItemB search = new ItemB();
		search.setItemId(itemId);
		search.setPlantId(plantId);
		List<ItemB> itemList = itemBMapper.select(search);
		String start = "";
		if ("OTHERS".equals(itemList.get(0).getPurchaseType())) {
			start = "ST";
		} else {
			start = "JIT";
		}
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String supplyPlanNumStart = start + time;
		// 流水一个批号
		Integer count = 1;
		SupplyPlan sr = new SupplyPlan();
		sr.setSupplyPlanNum(supplyPlanNumStart);
		List<SupplyPlan> list = new ArrayList<SupplyPlan>();
		list = mapper.selectMaxSupplyPlanNum(sr);
		if (list != null && list.size() > 0) {
			String supplyDemandNum = list.get(0).getSupplyPlanNum();
			String number = supplyDemandNum.substring(supplyDemandNum.length() - 2);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%02d", count);
		return supplyPlanNumStart + str;// 最终批号
	}

	/**
	 * 
	 * @description 流水一个上传批次号 从 demand 表中取最大
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @return
	 */
	public String getMaxSupplyDemandNumST() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String supplyDemandNumStart = "ST" + time;
		// 流水一个供货需求批号
		Integer count = 1;
		SupplyDemand sr = new SupplyDemand();
		sr.setSupplyDemandNum(supplyDemandNumStart);
		List<SupplyDemand> list = new ArrayList<SupplyDemand>();
		list = supplyDemandMapper.selectMaxSupplyDemandNum(sr);
		if (list != null && list.size() > 0) {
			String supplyDemandNum = list.get(0).getSupplyDemandNum();
			String number = supplyDemandNum.substring(supplyDemandNum.length() - 2);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%02d", count);
		return supplyDemandNumStart + str;// 最终供货需求批号
	}

	/**
	 * 
	 * @description 流水一个上传批次号 plan表中取最大
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @return
	 */
	public String getMaxSupplyPlanNumST() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String supplyDemandNumStart = "ST" + time;
		// 流水一个供货需求批号
		Integer count = 1;
		SupplyPlan sr = new SupplyPlan();
		sr.setSupplyPlanNum(supplyDemandNumStart);
		List<SupplyPlan> list = new ArrayList<SupplyPlan>();
		list = mapper.selectMaxSupplyPlanNum(sr);
		if (list != null && list.size() > 0) {
			String supplyDemandNum = list.get(0).getSupplyPlanNum();
			String number = supplyDemandNum.substring(supplyDemandNum.length() - 2);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%02d", count);
		return supplyDemandNumStart + str;// 最终供货需求批号
	}

	/**
	 * 
	 * @description 流水一个上传批次号 plan表中取最大
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @return
	 */
	public String getMaxSupplyPlanNumJIT() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String supplyDemandNumStart = "JIT" + time;
		// 流水一个供货需求批号
		Integer count = 1;
		SupplyPlan sr = new SupplyPlan();
		sr.setSupplyPlanNum(supplyDemandNumStart);
		List<SupplyPlan> list = new ArrayList<SupplyPlan>();
		list = mapper.selectMaxSupplyPlanNum(sr);
		if (list != null && list.size() > 0) {
			String supplyDemandNum = list.get(0).getSupplyPlanNum();
			String number = supplyDemandNum.substring(supplyDemandNum.length() - 2);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%02d", count);
		return supplyDemandNumStart + str;// 最终供货需求批号
	}

	/**
	 * jit类型流水号
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @return
	 */
	public String getMaxSupplyDemandNumJIT() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String supplyDemandNumStart = "JIT" + time;
		// 流水一个供货需求批号
		Integer count = 1;
		SupplyDemand sr = new SupplyDemand();
		sr.setSupplyDemandNum(supplyDemandNumStart);
		List<SupplyDemand> list = new ArrayList<SupplyDemand>();
		list = supplyDemandMapper.selectMaxSupplyDemandNum(sr);
		if (list != null && list.size() > 0) {
			String supplyDemandNum = list.get(0).getSupplyDemandNum();
			String number = supplyDemandNum.substring(supplyDemandNum.length() - 2);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%02d", count);
		return supplyDemandNumStart + str;// 最终供货需求批号
	}

	@Override
	public void generate(IRequest requestCtx, List<SupplyPlan> list) {// 生成发运计划 按钮 入口数据处理及校验

		if (list.stream().filter(p -> "N".equals(p.getCanShip())).count() > 0) {
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1043"));//
		}
		if (list.stream().filter(p -> "N".equals(p.getStatus())).count() > 0) {
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1074"));// 必须全部为已确认才能生成
		}
		if (list.stream().filter(p -> "Y".equals(p.getShipPlanFlag())).count() > 0) {
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1073"));// 已发运不能发运
		}
		if (list.stream().filter(p -> p.getSupplyPlanId() == null).count() > 0) {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "存在未保存的数据"));
		}
		// safeQa(requestCtx, list);
		// 分组 item plant supplier supplyplannum求和 RealityShipQty或SupplierDeliveryQty
		Map<String, IntSummaryStatistics> collect = list.stream().collect(Collectors.groupingBy(p -> {
			return String.valueOf(p.getItemId().intValue()) + "-" + String.valueOf(p.getPlantId().intValue()) + "-"
					+ String.valueOf(p.getSupplierId().intValue()) + "-" + p.getSupplyPlanNum();
		}, Collectors.summarizingInt(p -> {
			return p.getSupplierDeliveryQty().intValue();
		})));
		List<SupplyPlan> groupList = new ArrayList<SupplyPlan>();
		StringBuilder totalMessage = new StringBuilder();// 全局错误信息
		int countTotalError = 0;// 全局错误数量
		for (Entry<String, IntSummaryStatistics> entry : collect.entrySet()) {
			SupplyPlan supplyPlan = new SupplyPlan();
			supplyPlan.setItemId(Float.valueOf(entry.getKey().split("-")[0]));
			supplyPlan.setPlantId(Float.valueOf(entry.getKey().split("-")[1]));
			supplyPlan.setSupplierId(Float.valueOf(entry.getKey().split("-")[2]));
			supplyPlan.setSupplyPlanNum(entry.getKey().split("-")[3]);
			supplyPlan.setSupplierDeliveryQty(Float.valueOf(String.valueOf(entry.getValue().getSum())));
			PoLines sear = new PoLines();
			sear.setSupplierId(supplyPlan.getSupplierId());
			sear.setItemId(supplyPlan.getItemId());
			sear.setPlantId(supplyPlan.getPlantId());
			/**
			 * 改了下求值逻辑 20191223
			 */
			List<PoLines> poLines = poLinesMapper.selectCanShipQty(sear);
			supplyPlan.setCanShipQty(poLines == null || poLines.size() == 0 ? 0f : poLines.get(0).getQuantity());// 可发运数量获取
			groupList.add(supplyPlan);
			if (supplyPlan.getSupplierDeliveryQty().intValue() > supplyPlan.getCanShipQty().intValue()) {
				countTotalError++;
				totalMessage.append(getIPSMessage(requestCtx, supplyPlan));// 获取物料 工厂 供应商的相关信息
			}
		}
		if (countTotalError > 0) {
			throw new RuntimeException(totalMessage.toString());
		}
		generateEnd(requestCtx, list);
	}

	/**
	 * 
	 * @description 生成发运计划数据校验通过后执行的 生成po_location
	 * @author tianmin.wang
	 * @date 2019年11月13日
	 * @param requestCtx
	 * @param list       已经构建的发运计划
	 */
	public void generateEnd(IRequest requestCtx, List<SupplyPlan> list) {
		list = list.stream().sorted(Comparator.comparing(SupplyPlan::getSupplierDeliveryTime))
				.collect(Collectors.toList());
//		list = list.stream().sorted(
//				Comparator.comparing(p->p.getSupplierDeliveryTime()))
//				.collect(Collectors.toList());
		for (SupplyPlan sup : list) {
			// Float sq = "Y".equals(sup.getOtherFactorFlag()) ? sup.getRealityShipQty() :
			// sup.getSupplierDeliveryQty();
			Float sq = sup.getSupplierDeliveryQty();// 交货数量
			PoLines sear = new PoLines();
			sear.setSupplierId(sup.getSupplierId());
			sear.setItemId(sup.getItemId());
			sear.setPlantId(sup.getPlantId());
			List<PoLines> poLines = poLinesMapper.selectCanShipData(sear);
			int linesCount = 0;
			for (PoLines pol : poLines) {
				linesCount++;
				if (pol.getQuantity() <= 0) {
					continue;
				}
				if ((sq - nvl(pol.getQuantity())) <= 0) {
					generatePoLocation(requestCtx, sq, pol, sup, linesCount);
					break;
				} else {
					sq = sq - pol.getQuantity();
					generatePoLocation(requestCtx, nvl(pol.getQuantity()), pol, sup, linesCount);
					continue;
				}
			}
			SupplyPlan suUp = new SupplyPlan();
			suUp.setSupplyPlanId(sup.getSupplyPlanId());
			suUp.setShipPlanFlag("Y");
			suUp.setStatus("S");
			self().updateByPrimaryKeySelective(requestCtx, suUp);
		}
	}

	/**
	 * 在poLocation表中组建并插入数据
	 * 
	 * @param requestCtx
	 * @param quantity
	 * @param pol
	 * @param sup
	 * @param linesCount
	 */
	public void generatePoLocation(IRequest requestCtx, Float quantity, PoLines pol, SupplyPlan sup, int linesCount) {
		// iPoLineLocationsService
		PoLineLocations insert = new PoLineLocations();
		insert.setPoHeaderId(pol.getPoHeaderId());
		insert.setPoLineId(pol.getPoLineId());
		insert.setShipmentNum(getNewPoLocationShipmentNum(pol.getPoHeaderId(), pol.getPoLineId()));
		insert.setShipmentStatus("A");
		insert.setShipToOrganizationId(pol.getPlantId());
		insert.setUnitMeasLookupCode(pol.getPrimaryUom());
		insert.setQuantity(quantity);// 生成的数量
		insert.setCanceledFlag("N");
		insert.setClosedCode("N");
		insert.setClosedFlag("N");
		insert.setApprovedFlag("Y");
		insert.setConfirmFlag("Y");
		insert.setConfirmTime(new Date());
		insert.setConfirmBy(Float.valueOf(String.valueOf(requestCtx.getUserId())));
		insert.setQuantityReceived(0f);
		insert.setQuantityAccepted(0f);
		insert.setQuantityRejected(0f);
		insert.setQuantityBilled(0f);
		insert.setQuantityCancelled(0f);
		insert.setAheadOfTime(0f);
		insert.setDelayDays(0f);
		insert.setPromisedDate(sup.getSupplierDeliveryTime());
		insert.setNeedByDate(sup.getRequireTime());
		insert.setConsignedFlag("N");
		insert.setLineLocationSupplierDesc(sup.getSupplierRemarks());
		insert.setSupplyPlanId(sup.getSupplyPlanId());
		iPoLineLocationsService.insertSelective(requestCtx, insert);
	}

	/**
	 * 
	 * @description 获取当前行对应的location中ShipmentNum最大的值+1
	 * @author tianmin.wang
	 * @date 2019年11月15日
	 * @param poHeaderId
	 * @param poLineId
	 * @return
	 */
	public Float getNewPoLocationShipmentNum(Float poHeaderId, Float poLineId) {
		return poLineLocationsMapper.getNewPoLocationShipmentNum(poHeaderId, poLineId);
	}

	/**
	 * 
	 * @description 传入内容是否为空
	 * @author tianmin.wang
	 * @date 2019年11月15日
	 * @param f
	 * @return
	 */
	public float nvl(Float f) {
		return f == null ? 0f : f;
	}

	public void safeQa(IRequest requestCtx, List<SupplyPlan> list) {
		// 分组求最小
		Map<String, Optional<SupplyPlan>> collectDeliveryTime = list.stream()
				.filter(p -> p.getPurchaseType().equals("OTHERS")).collect(Collectors.groupingBy(p -> {
					return String.valueOf(p.getItemId().intValue()) + "-" + String.valueOf(p.getPlantId().intValue())
							+ "-" + String.valueOf(p.getSupplierId().intValue()) + "-" + p.getSupplyPlanNum();
				}, Collectors.minBy(Comparator.comparing(SupplyPlan::getRequireTime))));

		for (Entry<String, Optional<SupplyPlan>> entry : collectDeliveryTime.entrySet()) {
			SupplyPlan supplyPlan = new SupplyPlan();
			supplyPlan.setItemId(Float.valueOf(entry.getKey().split("-")[0]));
			supplyPlan.setPlantId(Float.valueOf(entry.getKey().split("-")[1]));
			supplyPlan.setSupplierId(Float.valueOf(entry.getKey().split("-")[2]));
			supplyPlan.setSupplyPlanNum(entry.getKey().split("-")[3]);
			List<SupplyPlan> otherResult = mapper.otherFactorFlagSelect(supplyPlan);

			/**
			 * 若存在已考虑安全库存在途QA的供货计划other_factor_flag为Y 将供应商交货数量supplier_delivery_qty更新至
			 * 实际需发运数量 reality_ship_qty 后再执行; 若不存在 对勾选数据中供应商交期 supplier_delivery_time
			 * 靠前的供货计划 进行以下处理 otherFactoryDo 后再执行
			 */
			if (otherResult == null || otherResult.size() == 0) {// 需求时间靠前OTHERS的供货计划，进行以下处理
				otherFactoryDo(requestCtx, entry.getValue().get());
				List<SupplyPlan> others = list.stream()
						.filter(p -> p.getItemId().intValue() == entry.getValue().get().getItemId().intValue()
								&& p.getPlantId().intValue() == entry.getValue().get().getPlantId().intValue()
								&& p.getSupplierId().intValue() == entry.getValue().get().getSupplierId().intValue()
								&& p.getSupplyPlanNum().equals(entry.getValue().get().getSupplyPlanNum())
								&& p.getSupplyPlanId().intValue() != entry.getValue().get().getSupplyPlanId()
										.intValue())
						.collect(Collectors.toList());
				others.stream().forEach(p -> {
					SupplyPlan supplyPlanUpdator = new SupplyPlan();
					supplyPlanUpdator.setSupplyPlanId(p.getSupplyPlanId());
					supplyPlanUpdator.setRealityShipQty(p.getRequireQty());
					p.setRealityShipQty(p.getRequireQty());
					mapper.updateByPrimaryKeySelective(supplyPlanUpdator);
				});
			}

		}
	}

	/**
	 * otherFactoryFlag标记为Y的计算某些需要的字段值 并更新到表
	 * 
	 * @param requestCtx
	 * @param supplyPlan
	 */
	public void otherFactoryDo(IRequest requestCtx, SupplyPlan supplyPlan) {
		SupplyPlan update = new SupplyPlan();
		update.setSupplyPlanId(supplyPlan.getSupplyPlanId());
		update.setOtherFactorFlag("Y");
		// 获取安全库存
//		ItemB itemBSearch = new ItemB();
//		itemBSearch.setItemId(supplyPlan.getItemId());
//		itemBSearch.setPlantId(supplyPlan.getPlantId());
//		List<ItemB> itemResult = itemBMapper.select(itemBSearch);
		SupplyDemand sdSearch = new SupplyDemand();
		sdSearch.setItemId(supplyPlan.getItemId());
		sdSearch.setSupplierId(supplyPlan.getSupplierId());
		sdSearch.setSupplyDemandNum(supplyPlan.getSupplyPlanNum());
		List<SupplyDemand> sdResult = supplyDemandMapper.select(sdSearch);
		update.setSafetyStockValue(sdResult.get(0).getSafetyStockValue());
//		update.setSafetyStockValue(
//				itemResult == null || itemResult.size() < 1 || itemResult.get(0).getSafetyStockValue() == null ? 0f
//						: itemResult.get(0).getSafetyStockValue());
		// 未收货数量
		DeliveryTicketL ticketLSearch = new DeliveryTicketL();
		ticketLSearch.setItemId(supplyPlan.getItemId());
		ticketLSearch.setPlantId(supplyPlan.getPlantId());
		ticketLSearch.setSupplierId(supplyPlan.getSupplierId());
		List<DeliveryTicketL> ticketResult = deliveryTicketLMapper.supplyPlanSumSelect(ticketLSearch);
		update.setShipQuantity(ticketResult == null || ticketResult.size() < 1 ? 0f
				: ticketResult.get(0).getShipQuantity() == null ? 0f : ticketResult.get(0).getShipQuantity());
		// 报检中数量
//		update.setPresentQa(
//				ticketResult == null || ticketResult.size() < 1 || ticketResult.get(0).getQuarantineQty() == null ? 0f
//						: ticketResult.get(0).getQuarantineQty());
		update.setPresentQa(sdResult.get(0).getPresentQa());
		// 实际需发运数量
		update.setRealityShipQty(supplyPlan.getRequireQty() + update.getSafetyStockValue() - update.getShipQuantity()
				- update.getPresentQa());
		update.setRealityShipQty(
				update.getRealityShipQty().floatValue() < 0 ? 0f : update.getRealityShipQty().floatValue());

		self().updateByPrimaryKeySelective(requestCtx, update);
		supplyPlan.setOtherFactorFlag("Y");
		supplyPlan.setShipQuantity(update.getShipQuantity());
		supplyPlan.setPresentQa(update.getPresentQa());
		supplyPlan.setRealityShipQty(update.getRealityShipQty());
	}

	/**
	 * 获取物料 工厂 供应商的相关信息
	 */
	public String getIPSMessage(IRequest requestCtx, SupplyPlan supplyPlan) {

		ItemB itemSearch = new ItemB();
		itemSearch.setItemId(supplyPlan.getItemId());
		itemSearch.setPlantId(supplyPlan.getPlantId());
		Suppliers suppliersSearch = new Suppliers();
		suppliersSearch.setSupplierId(supplyPlan.getSupplierId());
		String itemCode = itemBMapper.select(itemSearch).get(0).getItemCode();
		String supplierName = suppliersMapper.select(suppliersSearch).get(0).getSupplierCode();
		String endString = MessageFormat.format(
				SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1048"), itemCode,
				supplierName);
		return endString;
	}

	/**
	 * 取消操作
	 */
	@Override
	public void cancel(IRequest requestCtx, List<SupplyPlan> list) {
		// TODO Auto-generated method stub
		for (SupplyPlan sup : list) {
			if (!"N".equals(sup.getStatus())) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1045"));
			}
			if (sup.getSupplyPlanId() == null) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "存在未保存的数据"));
			}
			SupplyPlan suUp = new SupplyPlan();
			suUp.setSupplyPlanId(sup.getSupplyPlanId());
			suUp.setStatus("C");
			self().updateByPrimaryKeySelective(requestCtx, suUp);
		}

	}

	/**
	 * 确认操作
	 */
	@Override
	public void confirm(IRequest requestCtx, List<SupplyPlan> list) {
		// TODO Auto-generated method stub
		UserSys us = new UserSys();
		us.setUserId(Float.valueOf(String.valueOf(requestCtx.getUserId())));
		List<UserSys> usList = userSysMapper.select(us);
		Float supplierId = usList.get(0).getSupplierId();
		Float needConfirmCount = mapper.selectNeedConfirm(supplierId);
//		if (needConfirmCount.intValue() != list.size()) {
//			throw new RuntimeException(
//					SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1046"));
//		}
		for (SupplyPlan sup : list) {
			if (sup.getSupplyPlanId() == null) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "存在未保存的数据"));
			}
			SupplyPlan suUp = new SupplyPlan();
			suUp.setSupplyPlanId(sup.getSupplyPlanId());
			suUp.setStatus("A");
			self().updateByPrimaryKeySelective(requestCtx, suUp);
		}
		commitMail(requestCtx, list);
	}

	/**
	 * 确认操作发邮件
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param requestCtx
	 * @param insertPlanList
	 */
	public void commitMail(IRequest requestCtx, List<SupplyPlan> insertPlanList) {
		// taskExecutor
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// 先分组
					Map<String, List<SupplyPlan>> groupMap = insertPlanList.stream()
							.collect(Collectors.groupingBy(p -> {
								return StringUtils.isEmpty(p.getPurchaseGroup()) ? "" : p.getPurchaseGroup();
							}));
					for (Map.Entry<String, List<SupplyPlan>> entry : groupMap.entrySet()) {
						List<SupplyPlan> result = entry.getValue().stream().sorted(
								Comparator.comparing(SupplyPlan::getItemCode).thenComparing(SupplyPlan::getRequireTime))
								.collect(Collectors.toList());
						if (StringUtils.isEmpty(entry.getKey())) {
							return;
						}
						generatorConfirmExcelAndSendMail(requestCtx, result, entry.getKey());
					}
				} catch (Exception e) {
					logger.error("邮件发送产生了错误:" + e.getMessage());
				}
			}
		});
	}

	/**
	 * 生成提交数据的excel并发送邮件
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param requestCtx
	 * @param result
	 * @param purchaseGroup
	 */
	public void generatorConfirmExcelAndSendMail(IRequest requestCtx, List<SupplyPlan> result, String purchaseGroup) {
		MessageTemplate mt = getMailTemplate("SRM_SUPPLY_PLAN_CONFIRMED");
		String supplierName = "";
		UserSys suppliersSearch = new UserSys();
		suppliersSearch.setUserId(Float.valueOf(String.valueOf(requestCtx.getUserId())));
		List<UserSys> sup = userSysMapper.selectSupplierName(suppliersSearch);
		if (sup != null && sup.size() >= 1) {
			supplierName = sup.get(0).getSupplierName();
		}

		String to = "";// 收件人邮箱
		UserPurchaseGroup userPurchaseGroup = new UserPurchaseGroup();
		userPurchaseGroup.setPurchaseGroupCode(purchaseGroup);
		List<UserPurchaseGroup> suList = userPurchaseGroupMapper.query(userPurchaseGroup);
		if (suList == null || suList.size() < 1) {
			return;
		} else {
			to = suList.stream().map(UserPurchaseGroup::getEmail).reduce((p1, p2) -> {
				return p1 + "," + p2;
			}).get();
		}
		XSSFWorkbook xlsFile = generatorConfirmExcel(requestCtx, result); // create a workbook
		List<XSSFWorkbook> excel = new ArrayList<XSSFWorkbook>();
		excel.add(xlsFile);
		List<String> names = new ArrayList<String>();
		names.add(supplierName + "供货计划确认情况反馈.xlsx");

		MailUtil.sendExcelMail(to, null, MessageFormat.format(mt.getSubject(), supplierName), mt.getContent(), excel,
				names, mt.getUserName(), null);
		excel.stream().forEach(t -> {
			try {
				t.close();
			} catch (IOException e) {

			}
		});
	}

	public MessageTemplate getMailTemplate(String templateCode) {
		MessageTemplate mtSearch = new MessageTemplate();
		mtSearch.setTemplateCode(templateCode);
		List<MessageTemplate> res = messageTemplateMapper.select(mtSearch);
		if (res == null || res.size() == 0) {
			throw new RuntimeException("没有找到邮件模板" + templateCode);
		}
		return res.get(0);
	}

	/**
	 * 生成提交的excel
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param r
	 * @param result
	 * @return
	 */
	public XSSFWorkbook generatorConfirmExcel(IRequest r, List<SupplyPlan> result) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		XSSFWorkbook xlsFile = new XSSFWorkbook();
		CreationHelper helper = xlsFile.getCreationHelper();
		Sheet sheet1 = xlsFile.createSheet("sheeet1"); // add a sheet to your workbook
		sheet1.setColumnWidth(0, 4000);
		sheet1.setColumnWidth(1, 1000);
		sheet1.setColumnWidth(2, 4000);
		sheet1.setColumnWidth(3, 4000);
		sheet1.setColumnWidth(4, 3000);
		sheet1.setColumnWidth(5, 5000);
		sheet1.setColumnWidth(6, 2000);
		sheet1.setColumnWidth(7, 5000);
		sheet1.setColumnWidth(8, 3000);
		sheet1.setColumnWidth(9, 3000);
		sheet1.setColumnWidth(10, 4000);
		sheet1.setColumnWidth(11, 3000);
		sheet1.setColumnWidth(12, 3000);
		Row row1 = sheet1.createRow(0);
//		sheet1.setColumnWidth(0, 25 * 256);
//		sheet1.setColumnWidth(1, 30 * 256);
		row1.createCell(0).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.supplyplannum")));
		row1.createCell(1).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.upplyplanlinenum")));
		row1.createCell(2).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.itemid")));
		row1.createCell(3).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.itemdescriptions")));
		row1.createCell(4).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.purchasetype")));
		row1.createCell(5).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.requiretime")));
		row1.createCell(6).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.requireqty")));
		row1.createCell(7).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.suppliercode")));
		row1.createCell(8).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.suppliername")));
		row1.createCell(7).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.supplierdeliverytime")));
		row1.createCell(8).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.supplierdeliveryqty")));
		row1.createCell(9).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.suppliercode")));
		row1.createCell(10).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.suppliername")));
		row1.createCell(11).setCellValue(helper.createRichTextString("交期不满足"));
		row1.createCell(12).setCellValue(helper.createRichTextString("数量不满足"));
		for (int i = 0; i < result.size(); i++) {
			Row row = sheet1.createRow(i + 1);
			row.createCell(0).setCellValue(result.get(i).getSupplyPlanNum());
			row.createCell(1).setCellValue(result.get(i).getSupplyPlanLineNum());
			row.createCell(2).setCellValue(result.get(i).getItemCode());
			row.createCell(3).setCellValue(result.get(i).getItemDescriptions());
			row.createCell(4).setCellValue(
					iCodeService.getCodeMeaningByValue(r, "HCS_PURCHASE_ITEM_TYPE", result.get(i).getPurchaseType()));
			row.createCell(5).setCellValue(sdf.format(result.get(i).getRequireTime()));
			row.createCell(6).setCellValue(result.get(i).getRequireQty());
			row.createCell(7).setCellValue(sdf.format(result.get(i).getSupplierDeliveryTime()));
			row.createCell(8).setCellValue(result.get(i).getSupplierDeliveryQty());
			row.createCell(9).setCellValue(result.get(i).getSupplierCode());
			row.createCell(10).setCellValue(result.get(i).getSupplierName());
			if (result.get(i).getSupplierDeliveryTime().after(result.get(i).getRequireTime())) {
				row.createCell(11).setCellValue("是");
			}
//			if (result.get(i).getSupplierDeliveryQty().floatValue() < result.get(i).getRequireQty().floatValue()) {
//				row.createCell(12).setCellValue("是");
//			}
			Float supplierDeliveryQty = mapper.getSupplierDeliveryQtyHaveSplit(result.get(i));
			if (supplierDeliveryQty < result.get(i).getRequireQty().floatValue()) {
				row.createCell(12).setCellValue("是");
			}
		}

		return xlsFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hcs.hcs_supply_plan.service.ISupplyPlanService#
	 * getSumSupplierdeliveryQty(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer getSumSupplierdeliveryQty(String supplyPlanNum, String supplyPlanLineNum) {
		// TODO Auto-generated method stub
		return mapper.getSumSupplierdeliveryQty(supplyPlanNum, supplyPlanLineNum);
	}
}