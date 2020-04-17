/**
 * 
 */
package com.hand.hap.webservice.ws.idto;

import java.util.List;

/**
 * @author tainmin.wang
 * @version date：2020年2月25日 下午1:42:18
 * 
 */

public class SerialDisqualified {
	public String taskType;
	public String taskNo;
	public String plantCode;
	public String line;
	public String workStation;
	public String eventTime;
	public List<SerialDisqualifiedLine> recordLines;
}
