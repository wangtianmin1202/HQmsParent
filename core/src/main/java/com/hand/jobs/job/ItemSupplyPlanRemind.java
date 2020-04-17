package com.hand.jobs.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.job.AbstractJob;
import com.hand.hap.mail.dto.MessageTemplate;
import com.hand.hap.mail.mapper.MessageTemplateMapper;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;
import com.hand.hap.util.MailUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;
import com.hand.hcs.hcs_supply_plan.dto.SupplyPlan;
import com.hand.hcs.hcs_supply_plan.mapper.SupplyPlanMapper;

/**
 * @author tainmin.wang
 * @version date：2019年11月1日 下午3:31:05 物料发运提醒定时job 每天早上7点
 */
public class ItemSupplyPlanRemind extends AbstractJob implements ITask {

	@Autowired
	SupplyPlanMapper supplyPlanMapper;
	@Autowired
	SuppliersMapper suppliersMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	private MessageTemplateMapper messageTemplateMapper;
	@Autowired
	private ICodeService iCodeService;

	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		self();
	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		self();
	}

	public void self() {
		ServiceRequest sr = new ServiceRequest();
		sr.setLocale("zh_CN");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String forecastDeliveryTime = sdf.format(new Date());
		SupplyPlan supSearch = new SupplyPlan();
		supSearch.setForecastDeliveryTime(forecastDeliveryTime);
		List<SupplyPlan> searchResult = supplyPlanMapper.selectForecastDelivery(supSearch);
		// 先分组
		Map<Float, List<SupplyPlan>> groupMap = searchResult.stream().collect(Collectors.groupingBy(p -> {
			return p.getSupplierId();
		}));
		for (Map.Entry<Float, List<SupplyPlan>> entry : groupMap.entrySet()) {
			List<SupplyPlan> result = entry.getValue().stream()
					.sorted(Comparator.comparing(SupplyPlan::getItemCode).thenComparing(SupplyPlan::getRequireTime))
					.collect(Collectors.toList());
			generatorExcelAndSendMail(sr, result, entry.getKey());
		}
	}

	public void generatorExcelAndSendMail(IRequest requestCtx, List<SupplyPlan> result, Float supplierId) {
		if (result == null || result.size() < 1) {
			return;
		}
		MessageTemplate mt = getMailTemplate("SRM_SHIP_LIST_MAIL_REMIND");
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
		names.add("今日发运清单.xlsx");

		MailUtil.sendExcelMail(to, null, mt.getSubject(), mt.getContent(), excel, names, mt.getUserName(), null);
		excel.stream().forEach(t -> {
			try {
				t.close();
			} catch (IOException e) {

			}
		});
	}

	public XSSFWorkbook generatorExcel(IRequest r, List<SupplyPlan> result) {
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
		sheet1.setColumnWidth(10, 6000);
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
		row1.createCell(7).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.supplierdeliverytime")));
		row1.createCell(8).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.supplierdeliveryqty")));
		row1.createCell(9).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.suppliercode")));
		row1.createCell(10).setCellValue(helper.createRichTextString(meaning(r, "supplyplan.suppliername")));
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).getSupplierDeliveryTime() == null)
				continue;
			Row row = sheet1.createRow(i + 1);
			row.createCell(0).setCellValue(result.get(i).getSupplyPlanNum());
			row.createCell(1).setCellValue(result.get(i).getSupplyPlanLineNum());
			row.createCell(2).setCellValue(result.get(i).getItemCode());
			row.createCell(3).setCellValue(result.get(i).getItemDescriptions());
			row.createCell(4).setCellValue(iCodeService.getCodeMeaningByValue(r, "HCS_PURCHASE_ITEM_TYPE", result.get(i).getPurchaseType()));
			row.createCell(5).setCellValue(sdf.format(result.get(i).getRequireTime()));
			row.createCell(6).setCellValue(result.get(i).getRequireQty());
			row.createCell(7).setCellValue(sdf.format(result.get(i).getSupplierDeliveryTime()));
			row.createCell(8).setCellValue(result.get(i).getSupplierDeliveryQty());
			row.createCell(9).setCellValue(result.get(i).getSupplierCode());
			row.createCell(10).setCellValue(result.get(i).getSupplierName());
		}

		return xlsFile;
	}

	public String meaning(IRequest requestCtx, String code) {
		return SystemApiMethod.getPromptDescription(requestCtx, iPromptService, code);
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
}
