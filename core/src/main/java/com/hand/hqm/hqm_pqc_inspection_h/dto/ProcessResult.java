package com.hand.hqm.hqm_pqc_inspection_h.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * WMS库存重验信息
 * @author kai.li
 * @version date:2020年2月25日09:07:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessResult {

	public String result;
	public String error_code;
	public String error_info;
	public ProcessResultInfo result_info;
}
