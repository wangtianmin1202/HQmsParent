package com.hand.testcode.maincode;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;

import com.hand.hap.util.MailUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;

/**
 * @author tainmin.wang
 * @version date：2019年7月19日 上午11:30:05
 * 
 */
import org.codehaus.jackson.type.TypeReference;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.codehaus.jackson.map.ObjectMapper;

public class CodeTest {
	static SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
	static SAXReader reader = new SAXReader();
	public static void main(String[] args) throws Exception {

	}
	
	
	public static void ticketSrmToMes() {
		String soapRequestData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:qms=\"http://qmsservice.dev.wis.com\">" + 
				"   <soapenv:Header/>" + 
				"   <soapenv:Body>" + 
				"      <qms:MESGlobalFunc>" + 
				"         <qms:funcName>getOqcSampleQty</qms:funcName>" + 
				"         <qms:jsonString>{\"plantCode\":\"CNKE\",\"inspectionNum\":\"CNKE-OQC-200321-00001\",\"itemCode\":\"1242198\",\"itemVersion\":\"V10\",\"spreading\":\"V10\",\"lotNumber\":\"20200313A\",\"sampleQty\":\"2\"}</qms:jsonString>" + 
				"      </qms:MESGlobalFunc>" + 
				"   </soapenv:Body>" + 
				"</soapenv:Envelope>";
		try {
			String method = "http://cnsbw007:8087/QualityService/QualityModuleService";// 比如 http://cnsbx034:7005/WMS_Provide_PS/WMSServicePipelineProxyService
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
			System.out.print(new String(responseBody));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sysPasswordPattern() {
		Pattern PASSWORD_PATTERN_DIGITS_AND_CASE_LETTERS = Pattern
				.compile("(?!^\\d+$)(?!^[a-z]+$)(?!^[A-Z]+$)(?!^[\\dA-Z]+$)(?!^[\\da-z]+$)(?!^[a-zA-Z]+$)[0-9a-zA-Z]+");
		System.out.print(PASSWORD_PATTERN_DIGITS_AND_CASE_LETTERS.matcher("aA0@").matches());
	}

	public static void printException() {
		try {
			throw new RuntimeException("1111");
		} catch (Exception e) {
			System.out.println(11111111);
		}
	}

	public static List<Date> getDayListOfMonth(Date date) throws ParseException {
		List<Date> list = new ArrayList<Date>();
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		int year = aCalendar.get(Calendar.YEAR);// 年份
		int month = aCalendar.get(Calendar.MONTH) + 1;// 月份
		int day = aCalendar.getActualMaximum(Calendar.DATE);
		for (int i = 1; i <= day; i++) {
			String aDate = String.valueOf(year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", i);
			System.out.println(aDate);
			list.add(sdf.parse(aDate));
		}
		return list;
	}

	public static void mailTest() throws InterruptedException, IOException {
		XSSFWorkbook xlsFile = new XSSFWorkbook(); // create a workbook
		CreationHelper helper = xlsFile.getCreationHelper();
		OutputStream file = null;
		FileOutputStream fos = new FileOutputStream("");
		Sheet sheet1 = xlsFile.createSheet("title"); // add a sheet to your workbook
		org.apache.poi.ss.usermodel.Row row1 = sheet1.createRow((short) 0);
		sheet1.setColumnWidth(0, 25 * 256);
		sheet1.setColumnWidth(1, 30 * 256);
		row1.createCell(0).setCellValue(helper.createRichTextString("cell0"));
		row1.createCell(1).setCellValue(helper.createRichTextString("cell1"));
		row1.createCell(2).setCellValue(helper.createRichTextString("cell2"));
		List<XSSFWorkbook> exList = new ArrayList<XSSFWorkbook>();
		exList.add(xlsFile);
		List<String> name = new ArrayList<String>();
		name.add("中文测试.xlsx");
		String content = "<p>content</p>";
//		MailUtil.sendExcelMail("tianmin.wang@hand-china.com", null, "测试", content, exList, name);
		xlsFile.write(fos);
		try {
			xlsFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getPercision(String par) {
		System.out.println(par.contains("."));
		System.out.println(par.split(".").length);

		if (par.contains(".") && (par.split("\\.")).length > 1) {
			return par.split("\\.")[1].length();
		} else {
			return 0;
		}
	}

	public static void test4() {
		String str = "[{\"innerMap\":{},\"__id\":null,\"__status\":null,\"__tls\":{},\"sortname\":null,\"sortorder\":null,\"_token\":\"f913716f6b31acd6ac69244081c8dfb6\",\"requestId\":-1,\"programId\":-1,\"objectVersionNumber\":1,\"createdBy\":10011,\"creationDate\":1571052369000,\"lastUpdatedBy\":10011,\"lastUpdateDate\":1571052369000,\"lastUpdateLogin\":10011,\"attributeCategory\":null,\"attribute1\":null,\"attribute2\":null,\"attribute3\":null,\"attribute4\":null,\"attribute5\":null,\"attribute6\":null,\"attribute7\":null,\"attribute8\":null,\"attribute9\":null,\"attribute10\":null,\"attribute11\":null,\"attribute12\":null,\"attribute13\":null,\"attribute14\":null,\"attribute15\":null,\"dataId\":642.0,\"lineId\":384.0,\"orderNum\":\"0\",\"data\":null,\"remark\":null,\"ngDescription\":null,\"judgement\":null,\"inspectionType\":null,\"solveWay\":null,\"approvalDes\":null,\"serialNumber\":null,\"snStatus\":null},{\"innerMap\":{},\"__id\":null,\"__status\":null,\"__tls\":{},\"sortname\":null,\"sortorder\":null,\"_token\":\"1c8d30ad7d9bf9bd91d3a90c3aefc85f\",\"requestId\":-1,\"programId\":-1,\"objectVersionNumber\":1,\"createdBy\":10011,\"creationDate\":1571052369000,\"lastUpdatedBy\":10011,\"lastUpdateDate\":1571052369000,\"lastUpdateLogin\":10011,\"attributeCategory\":null,\"attribute1\":null,\"attribute2\":null,\"attribute3\":null,\"attribute4\":null,\"attribute5\":null,\"attribute6\":null,\"attribute7\":null,\"attribute8\":null,\"attribute9\":null,\"attribute10\":null,\"attribute11\":null,\"attribute12\":null,\"attribute13\":null,\"attribute14\":null,\"attribute15\":null,\"dataId\":643.0,\"lineId\":384.0,\"orderNum\":\"1\",\"data\":null,\"remark\":null,\"ngDescription\":null,\"judgement\":null,\"inspectionType\":null,\"solveWay\":null,\"approvalDes\":null,\"serialNumber\":null,\"snStatus\":null},{\"innerMap\":{},\"__id\":null,\"__status\":null,\"__tls\":{},\"sortname\":null,\"sortorder\":null,\"_token\":\"7ea9cf68cf9bb67929bd622dc2014677\",\"requestId\":-1,\"programId\":-1,\"objectVersionNumber\":1,\"createdBy\":10011,\"creationDate\":1571052369000,\"lastUpdatedBy\":10011,\"lastUpdateDate\":1571052369000,\"lastUpdateLogin\":10011,\"attributeCategory\":null,\"attribute1\":null,\"attribute2\":null,\"attribute3\":null,\"attribute4\":null,\"attribute5\":null,\"attribute6\":null,\"attribute7\":null,\"attribute8\":null,\"attribute9\":null,\"attribute10\":null,\"attribute11\":null,\"attribute12\":null,\"attribute13\":null,\"attribute14\":null,\"attribute15\":null,\"dataId\":644.0,\"lineId\":384.0,\"orderNum\":\"2\",\"data\":null,\"remark\":null,\"ngDescription\":null,\"judgement\":null,\"inspectionType\":null,\"solveWay\":null,\"approvalDes\":null,\"serialNumber\":null,\"snStatus\":null}]";
		StringBuilder s1 = new StringBuilder();
		String s2 = MessageFormat.format("{0},{1}", "0000", "111111111");
		System.out.println(s2);
	}

	public static void test3() throws JsonGenerationException, JsonMappingException, IOException {
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 4; i++) {
			Map<String, String> hsMap = new LinkedHashMap<String, String>();
			for (int j = 0; j < 5; j++) {
				hsMap.put(String.valueOf(j) + "A", String.valueOf(j));
			}
			listMap.add(hsMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(System.currentTimeMillis());
		String ujson = mapper.writeValueAsString(listMap);
		System.out.println(System.currentTimeMillis());
		System.out.println(ujson);
	}

	public static void test2() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		Student stu = new CodeTest().new Student();
//		Method[] Methods = Student.class.getMethods();
//		String[] ss = { "zhou" };
//		for (Method me : Methods) {
//			if (me.getName().startsWith("set")) {
//				me.invoke(stu, ss);
//			}
//		}
//		System.out.println(stu.getFamily());
	}

	public static void test1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date data1;
		Date data2;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, 1);
		data1 = calendar.getTime();
		calendar.add(Calendar.MONTH, 2);
		data2 = calendar.getTime();
		System.out.println(new Date());
		System.out.println(sdf.format(data1));
		System.out.println(sdf.format(data2));
	}

	public static Date getPlanTime(Float timeLimit) {
		Date currentDate = new Date();
		if (timeLimit == null)
			return null;
		// 计算计划完成时间
		Date returnDate = new Date(currentDate.getTime() + (timeLimit.intValue()) * 60 * 60 * 1000);
		return returnDate;
	}

	public static void jsonTest() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
//		Classes cla = new Classes();
//		cla.setName("娃娃哈");
//		cla.setSex("bob");
//		cla.setAge("88");
//		List<Student> stus = new ArrayList<Student>();
//		/**
//		 * [{"family":"Afamily"},{"family":"Bfamily"},{"family":"Cfamily"}]
//		 */
//		Student stu1 = new Student();
//		stu1.setFamily("Afamily");
//		stus.add(stu1);
//		Student stu2 = new Student();
//		stu2.setFamily("Bfamily");
//		stus.add(stu2);
//		Student stu3 = new Student();
//		stu3.setFamily("Cfamily");
//		stus.add(stu3);
//		cla.setStudents(stus);
//
//		String json = mapper.writeValueAsString(stus);
//		System.out.println(json);

		Classes clb = mapper.readValue("{\"family\":\"Bfamily\",\"hamily\":\"hfamily\"}", new TypeReference<Classes>() {
		});
//
		System.out.println(clb);

	}

	public static void jsonTest1() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		UpcaseCla uc = mapper.readValue(
				"{\r\n" + "  \"TICKET_LINE\": \"12345\",\r\n" + "  \"TICKET_LINE_NUMBER\": \"45678\"\r\n" + "}",
				UpcaseCla.class);

		System.out.println(uc.TICKET_LINE);
		System.out.println(uc.TICKET_LINE_NUMBER);

	}

	public static class UpcaseCla {
		public String TICKET_LINE;
		public String TICKET_LINE_NUMBER;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Classes {

		private String name;

		private String sex;

		private String age;

		private List<Student> students;

		public List<Student> getStudents() {
			return students;
		}

		public void setStudents(List<Student> students) {
			this.students = students;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}
	}

	public static class Student {

		private String family;

		public String getFamily() {
			return family;
		}

		public void setFamily(String family) {
			this.family = family;
		}
	}

}
