package com.hand.hap.util;

import com.github.pagehelper.StringUtil;
import com.sun.mail.util.MailSSLSocketFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: tianmin.wang@hand-china.com
 * @Date: 2019/10/25
 * @Description: 邮件发送工具
 */

public class MailUtil {
	private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

	/**
	 * 邮箱服务器
	 */
	private static final String HOST = "mailhost.kohlerco.com";// smtp.partner.outlook.cn
	/**
	 * 发件人
	 */
	private static final String USERNAME = "HAP-SRM@kohler.com.cn";// mesdba@uabsbattery.com
	/**
	 * 发件人授权码Abcd1234 邮箱密码Abcd#1234
	 */
	private static final String PASSWORD = "Abcd#321";// Abcd#321

	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月30日 
	 * @param to 接收人
	 * @param cc 抄送
	 * @param subject 主题
	 * @param content 内容明细
	 * @param excel 文件
	 * @param names 文件名
	 * @param userName 发送人 如果为空则默认  HAP-SRM@kohler.com.cn
	 * @param contentHeader 内容 头
	 * @return
	 */
	public static boolean sendExcelMail(String to, String cc, String subject, String content, List<XSSFWorkbook> excel,
			List<String> names, String userName, String contentHeader) {
		// 属性对象
		boolean result;
		try {
			if (StringUtil.isEmpty(to)) {
				logger.info("to is null cancel send");
				return true;
			}
			Properties properties = properties();
			// 环境信息
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 在session中设置账户信息，Transport发送邮件时会使用W
					return new PasswordAuthentication(USERNAME, PASSWORD);
				}
			});
			session.setDebug(true);
			// 邮件
			MimeMessage msg = new MimeMessage(session);
			// 设置主题
			//MimeUtility.encodeWord(subject, "UTF-8", "Q");
			msg.setSubject(subject);
			// 发件人，注意中文的处理
			msg.setFrom(new InternetAddress(userName == null || userName.length() == 0 ? USERNAME : userName));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			if (!StringUtil.isEmpty(cc)) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			}
			// 整封邮件的MINE消息体
			MimeMultipart msgMultipart = new MimeMultipart("mixed");// 混合的组合关系
			// 设置邮件的MINE消息体
			msg.setContent(msgMultipart);
			// 装载附件
			if (excel != null && names != null) {
				for (int i = 0; i < excel.size(); i++) {
					MimeBodyPart attch = new MimeBodyPart(); // 附件
					msgMultipart.addBodyPart(attch); // 将附件添加到MIME消息体中
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					excel.get(i).write(bos); // write excel data to a byte array
					bos.close();
					ByteArrayDataSource dataSource = new ByteArrayDataSource(bos.toByteArray(),
							"application/vnd.ms-excel");
//                ByteArrayDataSource dataSource = new ByteArrayDataSource(bytes[i], "text/data"); //数据源
					attch.setDataHandler(new DataHandler(dataSource));
					attch.setFileName(MimeUtility.encodeText(names.get(i)));
				}
			}

			// html代码部分
			MimeBodyPart htmlPart = new MimeBodyPart();
			msgMultipart.addBodyPart(htmlPart);
			// html代码
//			htmlPart.setContent(contentHeader == null || contentHeader.length() == 0 ? "<h1>系统自动发送,请勿回复!!!</h1>"
//					: contentHeader + content, "text/html;charset=utf-8");
			htmlPart.setContent(content, "text/html;charset=utf-8");
			// 发送邮件
			Transport.send(msg, msg.getAllRecipients());
			logger.info("SendMail Success.>" + to);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * @param to      收件人 地址以 逗号 , 隔开
	 * @param cc      抄送人 地址以 逗号 , 隔开
	 * @param subject 主题
	 * @param content 内容
	 * @param excel   附件
	 * @param names   附件名
	 * @return
	 */
	public static boolean sendExcelMail(String to, String cc, String subject, String content, List<XSSFWorkbook> excel,
			List<String> names) {
		// 属性对象
		boolean result;
		try {
			if (StringUtil.isEmpty(to)) {
				logger.info("to is null cancel send");
				return true;
			}
			Properties properties = properties();
			// 环境信息
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 在session中设置账户信息，Transport发送邮件时会使用W
					return new PasswordAuthentication(USERNAME, PASSWORD);
				}
			});
			session.setDebug(true);
			// 邮件
			MimeMessage msg = new MimeMessage(session);
			// 设置主题
			msg.setSubject(subject);
			// 发件人，注意中文的处理
			msg.setFrom(new InternetAddress(USERNAME));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			if (!StringUtil.isEmpty(cc)) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			}
			// 整封邮件的MINE消息体
			MimeMultipart msgMultipart = new MimeMultipart("mixed");// 混合的组合关系
			// 设置邮件的MINE消息体
			msg.setContent(msgMultipart);
			// 装载附件
			if (excel != null && names != null) {
				for (int i = 0; i < excel.size(); i++) {
					MimeBodyPart attch = new MimeBodyPart(); // 附件
					msgMultipart.addBodyPart(attch); // 将附件添加到MIME消息体中
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					excel.get(i).write(bos); // write excel data to a byte array
					bos.close();
					ByteArrayDataSource dataSource = new ByteArrayDataSource(bos.toByteArray(),
							"application/vnd.ms-excel");
//                ByteArrayDataSource dataSource = new ByteArrayDataSource(bytes[i], "text/data"); //数据源
					attch.setDataHandler(new DataHandler(dataSource));
					attch.setFileName(MimeUtility.encodeText(names.get(i)));
				}
			}

			// html代码部分
			MimeBodyPart htmlPart = new MimeBodyPart();
			msgMultipart.addBodyPart(htmlPart);
			// html代码
			htmlPart.setContent("<h1>系统自动发送,请勿回复!!!</h1>" + content, "text/html;charset=utf-8");
			// 发送邮件
			Transport.send(msg, msg.getAllRecipients());
			logger.info("SendMail Success.>" + to);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		return result;
	}
	
	/**
	 * 
	 * @param to      收件人 地址以 逗号 , 隔开
	 * @param cc      抄送人 地址以 逗号 , 隔开
	 * @param subject 主题
	 * @param content 内容
	 * @return
	 */
	public static boolean sendExcelMail(String to, String cc, String subject, String content) {
		// 属性对象
		boolean result;
		try {
			if (StringUtil.isEmpty(to)) {
				logger.info("to is null cancel send");
				return true;
			}
			Properties properties = properties();
			// 环境信息
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 在session中设置账户信息，Transport发送邮件时会使用W
					return new PasswordAuthentication(USERNAME, PASSWORD);
				}
			});
			session.setDebug(true);
			// 邮件
			MimeMessage msg = new MimeMessage(session);
			// 设置主题
			msg.setSubject(subject);
			// 发件人，注意中文的处理
			msg.setFrom(new InternetAddress(USERNAME));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			if (!StringUtil.isEmpty(cc)) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			}
			// 整封邮件的MINE消息体
			MimeMultipart msgMultipart = new MimeMultipart("mixed");// 混合的组合关系
			// 设置邮件的MINE消息体
			msg.setContent(msgMultipart);

			// html代码部分
			MimeBodyPart htmlPart = new MimeBodyPart();
			msgMultipart.addBodyPart(htmlPart);
			// html代码
			htmlPart.setContent("<h1>系统自动发送,请勿回复!!!</h1>" + content, "text/html;charset=utf-8");
			// 发送邮件
			Transport.send(msg, msg.getAllRecipients());
			logger.info("SendMail Success.>" + to);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		return result;
	}

	protected static Properties properties() throws GeneralSecurityException {
		// 属性对象
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		Properties properties = new Properties();
		properties.put("mail.host", HOST);
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.socketFactory", sf);
		properties.put("mail.smtp.socketFactory.fallback", "false");
		return properties;
	}
}
