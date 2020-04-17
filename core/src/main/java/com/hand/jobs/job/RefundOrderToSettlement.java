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
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;
import com.hand.hcs.hcs_doc_settlement.service.IDocSettlementService;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderH;
import com.hand.hcs.hcs_refund_order.dto.RefundOrderL;
import com.hand.hcs.hcs_refund_order.mapper.RefundOrderHMapper;
import com.hand.hcs.hcs_refund_order.mapper.RefundOrderLMapper;
import com.hand.hcs.hcs_refund_order.service.IRefundOrderLService;

/**
 * 退货单结算Job
 * 
 * @author KOCDZG1
 *
 */
@DisallowConcurrentExecution
@Transactional(rollbackFor = Exception.class)
public class RefundOrderToSettlement extends AbstractJob implements ITask {

	@Autowired
	private IDocSettlementService docSettlementService;
	@Autowired
	private RefundOrderLMapper mapper;
	@Autowired
	private RefundOrderHMapper refundOrderHMapper;
	@Autowired
	private IRefundOrderLService refundOrderLService;

	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		// TODO Auto-generated method stub
		RefundOrderToSettlementMain();
	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		RefundOrderToSettlementMain();
	}

	/**
	 * Job入口
	 */
	private void RefundOrderToSettlementMain() {
		// 获取数据源
		List<RefundOrderL> refundOrderLList = mapper.refundOrderJobQuery();
		if (refundOrderLList != null && refundOrderLList.size() > 0) {
			for (RefundOrderL refundOrderL : refundOrderLList) {
				// 生成结算单据
				createDocSettlement(refundOrderL);

				// 更新送货单行表中结算单据生成标识
				refundOrderL.setSettlementFlag("Y");
				refundOrderL.setRoLineStatus("C");
				refundOrderLService.updateByPrimaryKeySelective(null, refundOrderL);
				RefundOrderL relsearch = new RefundOrderL();
				relsearch.setRoHeaderId(refundOrderL.getRoHeaderId());
				List<RefundOrderL> result = mapper.select(relsearch);
				if (result != null && result.stream().filter(p -> {
					return !p.getRoLineStatus().equals("C") && !p.getRoLineStatus().equals("R");
				}).count() == 0) {
					RefundOrderH rfh = new RefundOrderH();
					rfh.setRoHeaderId(refundOrderL.getRoHeaderId());
					rfh.setRefundOrderStatus("C");
					refundOrderHMapper.updateByPrimaryKeySelective(rfh);
				}
			}
		}
	}

	/**
	 * 生成结算单据
	 * 
	 * @param refundOrderL
	 * @return
	 */
	private DocSettlement createDocSettlement(RefundOrderL refundOrderL) {
		// 生成结算单据号
		DocSettlement settlement = new DocSettlement();
		settlement.setSupplierId(refundOrderL.getSupplierId());
		int num = docSettlementService.queryMaxNum(settlement);
		num++;
		// 序列号
		String numStr = String.format("%04d", num);
		// 年月日：yyyy
		SimpleDateFormat simple = new SimpleDateFormat("yyMM");
		String now = simple.format(new Date());
		String docSettlementNum = refundOrderL.getSupplierCode() + now + numStr;

		DocSettlement docSettlement = new DocSettlement();
		docSettlement.setDocSettlementNum(docSettlementNum);
		docSettlement.setDocType("R");
		if (refundOrderL.getRefundedDate() == null) {
			throw new RuntimeException(
					"RoLineNum(退货单行号)：" + refundOrderL.getRoLineNum() + ",RefundedDate(a退货日期) is not exist");
		}

		docSettlement.setAccountDate(refundOrderL.getRefundedDate());
		docSettlement.setSettlementStatus("U");
		docSettlement.setItemId(refundOrderL.getItemId() != null ? refundOrderL.getItemId() : null);
		docSettlement.setPlantId(refundOrderL.getPlantId() != null ? refundOrderL.getPlantId() : null);
		docSettlement.setItemVersion(refundOrderL.getItemVersion() != null ? refundOrderL.getItemVersion() : null);
		docSettlement.setDocQty(refundOrderL.getQuantityRefunded() != null ? refundOrderL.getQuantityRefunded() : null);
		docSettlement.setItemUom(refundOrderL.getUnitCode() != null ? refundOrderL.getUnitCode() : null);
		docSettlement.setRelPoNumH(refundOrderL.getRefundOrderNum() != null ? refundOrderL.getRefundOrderNum() : null);
		docSettlement
				.setRelPoNumL(refundOrderL.getRoLineNum() != null ? replacePonit(refundOrderL.getRoLineNum()) : null);
		docSettlement.setSupplierId(refundOrderL.getSupplierId() != null ? refundOrderL.getSupplierId() : null);
		docSettlement.setUnitPrice(refundOrderL.getUnitPrice() != null ? refundOrderL.getUnitPrice() : null);
		docSettlement.setPriceUnit(refundOrderL.getPriceUnit() != null ? refundOrderL.getPriceUnit() : null);
		docSettlement
				.setActualAmount((refundOrderL.getActualAmount() != null ? refundOrderL.getActualAmount() * -1 : null));
		docSettlement
				.setAgentFullName(refundOrderL.getPurchaseGroup() != null ? refundOrderL.getPurchaseGroup() : null);
		docSettlement.setTaxCode(refundOrderL.getTaxRate() != null ? refundOrderL.getTaxRate() : null);
		docSettlement.setCurrency(refundOrderL.getCurrency() != null ? refundOrderL.getCurrency() : null);
		// 生成结算单据
		docSettlement = docSettlementService.insertSelective(null, docSettlement);
		return docSettlement;
	}

	/**
	 * 去除小数点以及后面无用的零
	 * 
	 * @param num 数字
	 * @return 处理后结果
	 */
	private String replacePonit(float num) {
		String s = num + "";
		if (s.indexOf(".") > 0) {
			// 正则表达
			s = s.replaceAll("0+?$", "");// 去掉后面无用的零
			s = s.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
		}
		return s;
	}
}
