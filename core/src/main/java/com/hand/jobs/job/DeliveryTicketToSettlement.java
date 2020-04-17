package com.hand.jobs.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.job.AbstractJob;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketL;
import com.hand.hcs.hcs_delivery_ticket.mapper.DeliveryTicketLMapper;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketLService;
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;
import com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService;

/**
 * 送货单结算Job
 * 
 * @author KOCDZG1
 *
 */
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class DeliveryTicketToSettlement extends AbstractJob implements ITask {

	@Autowired
	private DeliveryTicketLMapper mapper;

	@Autowired
	private IDeliveryTicketLService seliveryTicketLService;

	@Autowired
	private IDocSettlementService docSettlementService;

	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		// TODO Auto-generated method stub
		ticketSettlementMain();
	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		ticketSettlementMain();
	}

	/**
	 * job入口
	 */
	private void ticketSettlementMain() {
		// 获取数据源
		List<DeliveryTicketL> deliveryTicketLList = mapper.deliveryTicketJobQuery();

		if (deliveryTicketLList != null && deliveryTicketLList.size() > 0) {
			for (DeliveryTicketL deliveryTicketL : deliveryTicketLList) {
				// 生成结算单据
				createDocSettlement(deliveryTicketL);

				// 更新送货单行表中结算单据生成标识
				deliveryTicketL.setSettlementFlag("Y");
				seliveryTicketLService.updateByPrimaryKeySelective(null, deliveryTicketL);
			}
		}
	}

	/**
	 * 生成结算单据
	 * 
	 * @param deliveryTicketL
	 * @return
	 */
	private DocSettlement createDocSettlement(DeliveryTicketL deliveryTicketL) {
		// 生成结算单据号
		DocSettlement settlement = new DocSettlement();
		settlement.setSupplierId(deliveryTicketL.getSupplierId());
		int num = docSettlementService.queryMaxNum(settlement);
		num++;
		// 序列号
		String numStr = String.format("%04d", num);
		// 年月日：yyyy
		SimpleDateFormat simple = new SimpleDateFormat("yyMM");
		String now = simple.format(new Date());
		String docSettlementNum = deliveryTicketL.getSupplierCode() + now + numStr;

		DocSettlement docSettlement = new DocSettlement();
		docSettlement.setDocSettlementNum(docSettlementNum);
		docSettlement.setDocType("D");
		if (deliveryTicketL.getReceiveDate() == null) {
			throw new RuntimeException(
					"TicketLineNum(送货单行号)：" + deliveryTicketL.getTicketLineNum() + ",ReceiveDate(接收日期) is not exist");
		}
		docSettlement.setAccountDate(deliveryTicketL.getReceiveDate());
		docSettlement.setSettlementStatus("U");
		docSettlement.setItemId(deliveryTicketL.getItemId() != null ? deliveryTicketL.getItemId() : null);
		docSettlement.setPlantId(deliveryTicketL.getPlantId() != null ? deliveryTicketL.getPlantId() : null);
		docSettlement.setItemVersion(deliveryTicketL.getItemVersion() != null ? deliveryTicketL.getItemVersion() : null);
		docSettlement.setDocQty(deliveryTicketL.getReceiveQuantity() != null ? deliveryTicketL.getReceiveQuantity() : null);
		docSettlement.setItemUom(deliveryTicketL.getUomCode() != null ? deliveryTicketL.getUomCode() : null);
		docSettlement.setRelDocNumH(deliveryTicketL.getTicketNumber() != null ? deliveryTicketL.getTicketNumber() : null);
		docSettlement.setRelDocNumL(deliveryTicketL.getTicketLineNum() != null ? replacePonit(deliveryTicketL.getTicketLineNum() + "")  : null);
		docSettlement.setRelPoNumH(deliveryTicketL.getPoNumber() != null ? deliveryTicketL.getPoNumber() : null);
		docSettlement.setRelPoNumL(deliveryTicketL.getLineNum() != null ? replacePonit(deliveryTicketL.getLineNum()) : null);
		docSettlement.setSupplierId(deliveryTicketL.getSupplierId() != null ? deliveryTicketL.getSupplierId() : null);
		docSettlement.setUnitPrice(deliveryTicketL.getUnitPrice() != null ? deliveryTicketL.getUnitPrice() : null);
		docSettlement.setPriceUnit(deliveryTicketL.getPriceUnit() != null ? deliveryTicketL.getPriceUnit() : null);
		docSettlement.setActualAmount(deliveryTicketL.getActualAmount() != null ? deliveryTicketL.getActualAmount() : null);
		docSettlement.setAgentFullName(deliveryTicketL.getPurchaseGroup() != null ? deliveryTicketL.getPurchaseGroup() : null);
		docSettlement.setTaxCode(deliveryTicketL.getTaxRate() != null ? deliveryTicketL.getTaxRate() : null);
		docSettlement.setCurrency(deliveryTicketL.getCurrency() != null ? deliveryTicketL.getCurrency() : null);
		// 生成结算单据
		docSettlement = docSettlementService.insertSelective(null, docSettlement);
		return docSettlement;
	}
	/**
	 * 去除小数点以及后面无用的零
	 * @param num 数字
	 * @return 处理后结果
	 */
	private String replacePonit(String num) {

		if(num.indexOf(".") > 0){
			//正则表达
			num = num.replaceAll("0+?$", "");//去掉后面无用的零
			num = num.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
		}
		return num;
	}
}
