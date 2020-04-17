package com.hand.hap.webservice.ws;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.webservice.ws.idto.ContainerInformation;
import com.hand.hap.webservice.ws.idto.DeliveryReceipt;
import com.hand.hap.webservice.ws.idto.FqcBarcodeInfo;
import com.hand.hap.webservice.ws.idto.FqcOutLineInspection;
import com.hand.hap.webservice.ws.idto.MaterialNgInfo;
import com.hand.hap.webservice.ws.idto.PqcProductionInfo;
import com.hand.hap.webservice.ws.idto.ProductionNgInfo;
import com.hand.hap.webservice.ws.idto.ReturnDeliveryExecute;
import com.hand.hap.webservice.ws.idto.SerialDisqualified;
import com.hand.hap.webservice.ws.idto.WorkOperationChange;
import com.hand.hap.webservice.ws.wsi.DataReceptionFromWms;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcs.hcs_delivery_ticket.service.IDeliveryTicketLService;
import com.hand.hcs.hcs_out_barcode.service.IOutBarcodeService;
import com.hand.hcs.hcs_refund_order.service.IRefundOrderLService;
import com.hand.hqm.hqm_fqc_barcode.service.IFqcBarcodeService;
import com.hand.hqm.hqm_mes_ng_matiral.service.IMesNgMatiralService;
import com.hand.hqm.hqm_mes_ng_msg.service.IMesNgMsgService;
import com.hand.hqm.hqm_mes_ng_recorde.service.IMesNgRecordeService;
import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import com.hand.hqm.hqm_qc_task.service.IFqcTaskService;
import com.hand.hqm.hqm_qc_task.service.IPqcTaskService;
import com.hand.itf.itf_container_info.dto.ContainerInfo;
import com.hand.itf.itf_container_info.mapper.ContainerInfoMapper;
import com.hand.itf.itf_delivery_receipt.mapper.DeliveryReceiptMapper;
import com.hand.itf.itf_global_history.dto.GlobalHistory;
import com.hand.itf.itf_return_delivery_execute.mapper.ReturnDeliveryExecuteMapper;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;

/**
 * @author tainmin.wang
 * @version date：2019年11月6日 上午11:28:31
 * 
 */
@WebService(endpointInterface = "com.hand.hap.webservice.ws.wsi.DataReceptionFromWms", serviceName = "DataReceptionFromWms")
public class DataReceptionFromWmsImpl implements DataReceptionFromWms {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	IOutBarcodeService iOutBarcodeService;
	@Autowired
	IDeliveryTicketLService iDeliveryTicketLService;
	@Autowired
	IRefundOrderLService iRefundOrderLService;
	@Autowired
	ContainerInfoMapper containerInfoMapper;// 接口表
	@Autowired
	DeliveryReceiptMapper deliveryReceiptMapper;// 接口表
	@Autowired
	ReturnDeliveryExecuteMapper returnDeliveryExecuteMapper;// 接口表
	@Autowired
	ItemMapper itemMapper;// 接口表
	@Autowired
	IPqcInspectionHService iPqcInspectionHService;
	@Autowired
	IPqcTaskService iPqcTaskService;
	@Autowired
	IMesNgRecordeService iMesNgRecordeService;
	@Autowired
	IMesNgMsgService iMesNgMsgService;
	@Autowired
	IMesNgMatiralService iMesNgMatiralService;
	@Autowired
	IFqcBarcodeService iFqcBarcodeService;
	@Autowired
	IFqcTaskService iFqcTaskService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;

	Logger logger = LoggerFactory.getLogger(DataReceptionFromWmsImpl.class);

	@Override
	public String SRMGlobalFunc(String FuncName, String JsonString) {
		return "";
	}

	public String transferContainer(GlobalHistory gh, String listen, String guid) {// 容器信息接口
		SoapPostUtil.Response response = new SoapPostUtil.Response();
		try {
			ContainerInformation ci = mapper.readValue(listen, ContainerInformation.class);
			ContainerInfo cii = new ContainerInfo();
			cii.setBatchNumber(guid);
			cii.setProcessStatus("N");
			cii.setContainerId(ci.getContainerId());
			cii.setCleanFlag(ci.getCleanFlag());
			containerInfoMapper.insertSelective(cii);
			response = iOutBarcodeService.transferContainer(cii);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult() ? 0 : 1));
		return SoapPostUtil.getStringFromResponse(response);
	}

	public String transferDeliveryReceipt(GlobalHistory gh, String listen, String guid) {// 送货单收货接口
		SoapPostUtil.Response response = new SoapPostUtil.Response();
		try {
			DeliveryReceipt cr = mapper.readValue(listen, DeliveryReceipt.class);
			com.hand.itf.itf_delivery_receipt.dto.DeliveryReceipt dri = new com.hand.itf.itf_delivery_receipt.dto.DeliveryReceipt();
			dri.setDeliveryOrderNo(cr.getDeliveryOrderNo());
			dri.setDeliveryLineNo(cr.getDeliveryLineNo());
			dri.setReceivingQty(cr.getReceivingQty());
			dri.setReceivingDate(cr.getReceivingDate());
			dri.setInspectQty(cr.getInspectQty());
			dri.setQualifiedQty(cr.getQualifiedQty());
			dri.setInspectOk(cr.getInspectOk());
			dri.setBatchNumber(guid);
			dri.setProcessStatus("N");
			deliveryReceiptMapper.insertSelective(dri);
			response = iDeliveryTicketLService.transferDeliveryReceipt(dri);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult() ? 0 : 1));
		return SoapPostUtil.getStringFromResponse(response);
	}

	public String transferReturnDeliveryExecute(GlobalHistory gh, String listen, String guid) {// 退货单执行接口
		SoapPostUtil.Response response = new SoapPostUtil.Response();
		try {
			ReturnDeliveryExecute rdei = mapper.readValue(listen, ReturnDeliveryExecute.class);
			com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute rde = new com.hand.itf.itf_return_delivery_execute.dto.ReturnDeliveryExecute();
			rde.setBatchNumber(guid);
			rde.setProcessStatus("N");
			rde.setReturnOrder(rdei.getReturnOrder());
			rde.setReturnQty(rdei.getReturnQty());
			rde.setReturnLineNo(rdei.getReturnLineNo());
			rde.setReturnDate(rdei.getReturnDate());
			returnDeliveryExecuteMapper.insertSelective(rde);
			response = iRefundOrderLService.transferReturnDeliveryExecute(rde);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult() ? 0 : 1));
		return SoapPostUtil.getStringFromResponse(response);
	}

	/**
	 * 用于工单发起FQC下线报检申请
	 * 
	 * @description FQC下线报检申请 MES
	 * @author tianmin.wang
	 * @date 2020年2月25日
	 * @param gh
	 * @param listen
	 * @param guid
	 * @return
	 */
	public String fqcOutLineInspection(GlobalHistory gh, String listen, String guid) {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();

		FqcOutLineInspection foi;
		try {
			foi = mapper.readValue(listen, FqcOutLineInspection.class);
			response = iFqcTaskService.fqcOutLineInspection(foi);
		} catch (IOException e) {
			response.setResult(false);
			response.setMessage("JSON转换失败:" + e.getMessage());
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult()));
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	/**
	 * 用于传输PQC生产信息
	 * 
	 * @description PQC生产信息接口
	 * @author tianmin.wang
	 * @date 2020年2月25日
	 * @param gh
	 * @param listen
	 * @param guid
	 * @return
	 */
	public String transferPqcProductionInfo(GlobalHistory gh, String listen, String guid) {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			PqcProductionInfo ppi = mapper.readValue(listen, PqcProductionInfo.class);
			response = iPqcInspectionHService.transferPqcProductionInfo(ppi);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult()));
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	/**
	 * 
	 * @description 工单切换
	 * @author tianmin.wang
	 * @date 2020年2月25日
	 * @param gh
	 * @param listen
	 * @param guid
	 * @return
	 */
	public String workOperationChange(GlobalHistory gh, String listen, String guid) {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			WorkOperationChange woc = mapper.readValue(listen, WorkOperationChange.class);
			response = iPqcTaskService.workOperationChange(woc);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult()));
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	/**
	 * 
	 * @description 工位连续不合格数
	 * @author tianmin.wang
	 * @date 2020年2月25日
	 * @param gh
	 * @param listen
	 * @param guid
	 * @return
	 */
	public String serialDisqualified(GlobalHistory gh, String listen, String guid) {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			SerialDisqualified sdf = mapper.readValue(listen, SerialDisqualified.class);
			response = iMesNgRecordeService.serialDisqualified(sdf);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult()));
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	/**
	 * 
	 * @description 产成品不合格信息
	 * @author tianmin.wang
	 * @date 2020年2月25日
	 * @param gh
	 * @param listen
	 * @param guid
	 * @return
	 */
	public String productionNgInfo(GlobalHistory gh, String listen, String guid) {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			ProductionNgInfo pni = mapper.readValue(listen, ProductionNgInfo.class);
			response = iMesNgMsgService.productionNgInfo(pni);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult()));
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	/**
	 * 
	 * @description 原材料不合格信息
	 * @author tianmin.wang
	 * @date 2020年2月25日
	 * @param gh
	 * @param listen
	 * @param guid
	 * @return
	 */
	public String materialNgInfo(GlobalHistory gh, String listen, String guid) {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			MaterialNgInfo mni = mapper.readValue(listen, MaterialNgInfo.class);
			response = iMesNgMatiralService.materialNgInfo(mni);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult()));
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	/**
	 * 
	 * @description FQC 下线sn号
	 * @author tianmin.wang
	 * @date 2020年2月27日
	 * @param gh
	 * @param listen
	 * @param guid
	 * @return
	 */
	public String transferFqcBarcode(GlobalHistory gh, String listen, String guid) {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			FqcBarcodeInfo fbi = mapper.readValue(listen, FqcBarcodeInfo.class);
			response = iFqcBarcodeService.transferFqcBarcode(fbi);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult()));
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	/**
	 * 
	 * @description APS的接口转发
	 * @author tianmin.wang
	 * @date 2020年3月21日
	 * @param gh
	 * @param listen
	 * @param guid
	 * @return
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String procedureAps(GlobalHistory gh, String listen, String guid) {
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			Map<String, String> map = new HashMap();
			map.put("p_function_name", gh.getFuncName());
			map.put("p_json_obj", listen);
			map.put("result", "");
			map.put("x_out_message", "");
			ifInvokeOutboundMapper.apsOperation(map);
			response.setMessage(map.get("x_out_message"));
			logger.info("请求结果:x_out_message>" + map.get("x_out_message") + ">result:" + map.get("result"));
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(String.valueOf(response.getResult()));
		return response.getError_info();
	}

}
