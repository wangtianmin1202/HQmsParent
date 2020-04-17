package com.hand.testcode.maincode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.hand.hcs.hcs_doc_statement.service.IDocStatementService;

@Component
public class Test extends Thread{
	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
    private IDocStatementService service;
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat simple = new SimpleDateFormat("yyMMdd");
		String now = simple.format(new Date());
		//System.out.println(now);
		
		String s = "111.000";

		if(s.indexOf(".") > 0){

			//正则表达
			s = s.replaceAll("0+?$", "");//去掉后面无用的零
	
			s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点

		}
		//System.out.println(s);
		
		String taxCode = "2.1%";
		//System.out.println(taxCode.split("%")[0]);
		
		String numStr = "-4_";
		System.out.println(numStr.split("_")[0]);
		System.out.println("_3".split("_")[0]);
		System.out.println("_3".split("_").length);
		float f = Float.parseFloat(numStr.split("_")[0]);
		float f1 = 2f;
		System.out.println(f * f1);
		
		Date dt1 = new Date();
		
		Date dt2 = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt2);
		calendar.add(calendar.DATE, -1);
		dt2 = calendar.getTime();
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(dt2);
		dt2 = format.parse(dateString);
		
		System.out.println(daysBetween(dt1,dt2));
		
	}
	
	private void xcTest() {
		new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	private static int daysBetween(Date smdate,Date bdate) throws ParseException  
    {  
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	smdate=sdf.parse(sdf.format(smdate));
    	bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();  
        cal.setTime(smdate);  
        long time1 = cal.getTimeInMillis();               
        cal.setTime(bdate);  
        long time2 = cal.getTimeInMillis();       
        long between_days=(time2-time1)/(1000*3600*24);
          
       return Integer.parseInt(String.valueOf(between_days));         
    }
}
