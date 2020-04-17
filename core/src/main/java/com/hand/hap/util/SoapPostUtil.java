package com.hand.hap.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;

/**
 * soap调用
 * 
 * @author tainmin.wang
 * @version date：2019年11月5日 下午4:48:46
 * 
 */
public class SoapPostUtil {

	private static ObjectMapper mapper = new ObjectMapper();
	private static SAXReader reader = new SAXReader();

	/**
	 * @description wms接口调用
	 * @author tianmin.wang
	 * @date 2019年12月3日
	 * @param post
	 * @param iio
	 * @return
	 */
	public static Response ticketSrmToWms(String param, IfInvokeOutbound iio, String uri) {
		iio.setOutType("T");// ticketSrmToWms
		Map<String, String> map = new HashMap<String, String>();
		String soapRequestData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">"
				+ "   <soapenv:Header>" + "      <tem:WMSSoapHeader>" + "         <!--Optional:-->"
				+ "         <tem:UserKey>SRM:123456</tem:UserKey>" + "      </tem:WMSSoapHeader>"
				+ "   </soapenv:Header>" + "   <soapenv:Body>" + "      <tem:WMSGlobalFunc>"
				+ "         <!--Optional:-->" + "         <tem:FuncName>" + "SRM_DeliveryLabel"
				+ "</tem:FuncName>" + "         <!--Optional:-->" + "         <tem:JsonString>" + param
				+ "</tem:JsonString>" + "      </tem:WMSGlobalFunc>" + "   </soapenv:Body>"
				+ "</soapenv:Envelope>";
		iio.setRequestParameter(soapRequestData);
		try {
			String method = uri;// 比如 http://cnsbx034:7005/WMS_Provide_PS/WMSServicePipelineProxyService
			iio.setInterfaceName("SRM_DeliveryLabel");
			iio.setInterfaceUrl(method);
			PostMethod postMethod = new PostMethod(method);
			byte[] b = soapRequestData.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is, b.length, "application/soap+xml;charset=utf-8;");
			postMethod.setRequestHeader("SOAPAction", "");
			postMethod.setRequestEntity(re);
			HttpClient httpClient = new HttpClient();
			Integer statusCode = httpClient.executeMethod(postMethod);
			byte[] responseBody = postMethod.getResponseBody();
			Document document = reader.read(new ByteArrayInputStream(responseBody));
			iio.setRequestStatus(postMethod.getStatusText().length() > 10 ? "E" : postMethod.getStatusText());
			iio.setResponseContent(new String(responseBody));
			Map<String, String> nodeMap = new HashMap<String, String>();
			getChildNodes(document.getRootElement(), nodeMap);
			iio.setResponseCode("success");
			String resString = nodeMap.get("WMSGlobalFuncResult");
			return new Response(true, resString);
		} catch (Exception e) {
			iio.setResponseCode("E");
			return new Response(false, e.getMessage());
		}
	}

	/**
	 * @description wms接口调用
	 * @author tianmin.wang
	 * @date 2019年12月3日
	 * @param post
	 * @param iio
	 * @return
	 */
	public static Response ticketSrmToWms(String funcName, String param, IfInvokeOutbound iio, String uri) {
		iio.setOutType("T");// ticketSrmToWms
		String soapRequestData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">"
				+ "   <soapenv:Header>" + "      <tem:WMSSoapHeader>" + "         <!--Optional:-->"
				+ "         <tem:UserKey>SRM:123456</tem:UserKey>" + "      </tem:WMSSoapHeader>"
				+ "   </soapenv:Header>" + "   <soapenv:Body>" + "      <tem:WMSGlobalFunc>"
				+ "         <!--Optional:-->" + "         <tem:FuncName>" + funcName + "</tem:FuncName>"
				+ "         <!--Optional:-->" + "         <tem:JsonString>" + param + "</tem:JsonString>"
				+ "      </tem:WMSGlobalFunc>" + "   </soapenv:Body>" + "</soapenv:Envelope>";
		iio.setRequestParameter(soapRequestData);
		try {
			String method = uri;// 比如 http://cnsbx034:7005/WMS_Provide_PS/WMSServicePipelineProxyService
			iio.setInterfaceName(funcName);
			iio.setInterfaceUrl(method);
			PostMethod postMethod = new PostMethod(method);
			byte[] b = soapRequestData.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is, b.length, "application/soap+xml;charset=utf-8;");
			postMethod.setRequestHeader("SOAPAction", "");
			postMethod.setRequestEntity(re);
			HttpClient httpClient = new HttpClient();
			Integer statusCode = httpClient.executeMethod(postMethod);
			byte[] responseBody = postMethod.getResponseBody();
			Document document = reader.read(new ByteArrayInputStream(responseBody));
			iio.setRequestStatus(postMethod.getStatusText().length() > 10 ? "E" : postMethod.getStatusText());
			iio.setResponseContent(new String(responseBody));
			Map<String, String> nodeMap = new HashMap<String, String>();
			getChildNodes(document.getRootElement(), nodeMap);
			iio.setResponseCode("success");
			String resString = nodeMap.get("WMSGlobalFuncResult");
			return new Response(true, resString);
		} catch (Exception e) {
			iio.setResponseCode("E");
			return new Response(false, e.getMessage());
		}
	}

	/**
	 * 
	 * @description mes接口调用
	 * @author tianmin.wang
	 * @date 2020年2月21日
	 * @param uri              地址
	 * @param functionName     方法名称
	 * @param param            数据字符
	 * @param ifInvokeOutbound 记录量
	 * @return
	 */
	public static Response ticketSrmToMes(String uri, String functionName, String param,
			IfInvokeOutbound ifInvokeOutbound) {
		ifInvokeOutbound.setOutType("T");// ticketSrmToWms
		String soapRequestData =
		"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.dev.wis.com\">" + 
		"   <soapenv:Header/>" + 
		"   <soapenv:Body>" + 
		"      <ser:MES_GlobalFunction>" + 
		"         <ser:funcName>" + functionName + "</ser:funcName>" + 
		"         <ser:jsonString>" + param + "</ser:jsonString>" + 
		"      </ser:MES_GlobalFunction>" + 
		"   </soapenv:Body>" + 
		"</soapenv:Envelope>";
		ifInvokeOutbound.setRequestParameter(soapRequestData);
		try {
			String method = uri;// 比如 http://cnsbx034:7005/WMS_Provide_PS/WMSServicePipelineProxyService
			ifInvokeOutbound.setInterfaceName(functionName);
			ifInvokeOutbound.setInterfaceUrl(method);
			PostMethod postMethod = new PostMethod(method);
			byte[] b = soapRequestData.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is, b.length, "text/xml;charset=utf-8");
			postMethod.setRequestHeader("SOAPAction", "");
			postMethod.setRequestEntity(re);
			HttpClient httpClient = new HttpClient();
			Integer statusCode = httpClient.executeMethod(postMethod);
			byte[] responseBody = postMethod.getResponseBody();
			Document document = reader.read(new ByteArrayInputStream(responseBody));
			ifInvokeOutbound
					.setRequestStatus(postMethod.getStatusText().length() > 10 ? "E" : postMethod.getStatusText());
			ifInvokeOutbound.setResponseContent(new String(responseBody));
			Map<String, String> nodeMap = new HashMap<String, String>();
			getChildNodes(document.getRootElement(), nodeMap);
			ifInvokeOutbound.setResponseCode("success");
			String resString = nodeMap.get("out");
			return new Response(true, resString);
		} catch (Exception e) {
			ifInvokeOutbound.setResponseCode("E");
			return new Response(false, e.getMessage());
		}
	}

	private static void getChildNodes(Element elem, Map<String, String> nodeMap) {
		Iterator<Node> it = elem.nodeIterator();
		while (it.hasNext()) {
			Node node = it.next();
			if (node instanceof Element) {
				Element e1 = (Element) node;
				nodeMap.put(e1.getName(), e1.getText());
				getChildNodes(e1, nodeMap);
			}
		}
	}

	public static String getStringFromResponse(Response response) {
		try {
			return mapper.writeValueAsString(response);
		} catch (IOException e) {
			return "{\"result\":" + response.getResult().toString() + ",\"message\":\"" + response.getMessage() + "\"}";
		}

	}

	public static String getStringFromResponseSap(ResponseSap response) {
		if (response.getResult() == 1) {
			response.setError_info("1 is success");
		}
		try {
			return mapper.writeValueAsString(response);
		} catch (IOException e) {
			return "{\"result\":" + response.getResult().toString() + ",\"error_code\":\"" + response.getError_code()
					+ "\",\"error_info\":\"" + response.getError_info() + "\"}";
		}

	}

	public static Response getResponseFromString(String response)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(response, Response.class);
	}

	/**
	 * sap交互1:成功 0:失败
	 * 
	 * @author wtm
	 *
	 */
	public static class ResponseSap {
		//
		private Integer result;
		private String error_code;

		private String error_info;

		public String getError_code() {
			return error_code;
		}

		public void setError_code(String error_code) {
			this.error_code = error_code;
		}

		public String getError_info() {
			return error_info;
		}

		public void setError_info(String error_info) {
			this.error_info = error_info;
		}

		public ResponseSap() {
			this.result = 1;
		}

		public ResponseSap(Boolean success, String message) {
			this.result = success ? 1 : 0;
			this.error_info = message;
		}

		public Integer getResult() {
			return result;
		}

		public void setResult(Boolean result) {
			this.result = result ? 1 : 0;
		}

		public void setMessage(String message) {
			this.error_info = message;
		}

	}

	/**
	 * wms交互
	 * 
	 * @author wtm
	 *
	 */
	public static class Response {
		private Boolean result;
		private String message;

		public Response() {
			this.result = true;
		}

		public Response(Boolean success, String message) {
			this.result = success;
			this.message = message;
		}

		public Boolean getResult() {
			return result;
		}

		public void setResult(Boolean result) {
			this.result = result;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}
