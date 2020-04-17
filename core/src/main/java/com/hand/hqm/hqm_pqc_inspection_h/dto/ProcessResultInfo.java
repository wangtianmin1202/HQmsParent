package com.hand.hqm.hqm_pqc_inspection_h.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * WMS库存重验信息
 * @author kai.li
 * @version date:2020年2月25日09:07:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessResultInfo {
	public String line;
	public String workStation;
	public String inspectionNum;
	public String orderNo;
	public String itemCode;
	public String itemVersion;
}
