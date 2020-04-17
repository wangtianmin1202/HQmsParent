package com.hand.utils.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @Author: jhb
 * @Date: 2019/9/16 
 * @Description: 邮件发送工具
 */
@Component
public class MailUtil {
    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    /**
     * 邮箱服务器 smtp.126.com
     */
    private static final String HOST = "smtp.126.com";
    /**
     * 发件人 hap_dev@126.com
     */
    private static final String USERNAME = "hap_dev@126.com";
    /**
     * 发件人邮箱密码 hapdev11
     */
    private static final String PASSWORD = "hapdev11";

    /**
     * 功能描述: 邮件发送，只发送文本
     *
     * @auther: jhb
     * @date: 2019/9/16 
     * @param: [to 收件人, subject 主题, html 内容]
     */
    public static boolean sendMail(String to, String subject, String html) {
        boolean result;
        Transport ts = null;
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            Properties prop = new Properties();
            prop.put("mail.host", HOST);
            prop.put("mail.transport.protocol", "smtp");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);
            prop.put("mail.smtp.socketFactory.fallback", "false");
            //使用JavaMail发送邮件的5个步骤
            //1、创建session
            Session session = Session.getInstance(prop);
            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(true);
            //2、通过session得到transport对象
            ts = session.getTransport();
            //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
            ts.connect(HOST, USERNAME, PASSWORD);
            //4、创建邮件
            //创建邮件对象
            MimeMessage message = new MimeMessage(session);
            //指明邮件的发件人
            message.setFrom(new InternetAddress(USERNAME));
            //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //邮件的标题
            message.setSubject(subject);
            //邮件的文本内容
            message.setContent(html, "text/html;charset=UTF-8");
            //返回创建好的邮件对象
            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            logger.info("SendMail Success.>" + to);
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        return result;
    }

    /**
     * use spring ThreadPoolTaskExecutor to send asynchronous mail request
     *
     * @param to      邮件接收人
     * @param subject 邮件主题
     * @param html    邮件内容
     * @author: jhb
     * @description: taskSendMail
     * @version 0.1
     */
    public void taskSendMail(String to, String subject, String html) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
        taskExecutor.execute(() -> {
            logger.info(Thread.currentThread().getName());
            MailUtil.sendMail(to, subject, html);
        });
    }


    /**
     *
     * @Description 获取带表格的邮件Html
     *
     * @author 
     * @date 
     * @param title
     * @param tableWidth
     * @param tableHead
     * @param tableBody
     * @return java.lang.String
     *
     */
    public String getTable(String title, int tableWidth, ArrayList<String> tableHead, ArrayList<ArrayList<String>> tableBody) {
        String html = getHtml(title);
        StringBuilder table = new StringBuilder();

        //拼接表格样式
        table.append("  <style type=\"text/css\">");
        table.append("   table {");
        table.append("    margin: 10px 0 30px 0;");
        table.append("    font-size: 13px;");
        table.append("   }");
        table.append("   table caption {");
        table.append("    text-align:left;");
        table.append("   }");
        table.append("   table tr th {");
        table.append("    background: #cef;");
        table.append("    color: #000;");
        table.append("    padding: 7px 4px;");
        table.append("    text-align: left;");
        table.append("   }");
        table.append("   table tr td {");
        table.append("    padding: 7px 4px;");
        table.append("    text-align: left;");
        table.append("   }");
        table.append("  </style>");
        html = html.replace("<style></style>", table.toString());

        //拼接表格
        table.delete(0,table.length());
        table.append("  <h2>" + title + "<h2/>");
        table.append("  <table cellspacing=\"0px\" rules=\"all\" border=\"1px solid #000\" style=\"width:" + tableWidth + "px;\">");
        //表头
        table.append("   <tr>");
        for (int i = 0; i < tableHead.size(); i++) {
            table.append("    <th>" + tableHead.get(i) + "</th>");
        }
        table.append("   </tr>");
        //表格内容
        for (int i = 0; i < tableBody.size(); i++) {
            table.append("   <tr>");
            for (int j = 0; j < tableBody.get(i).size(); j++) {
                table.append("    <td>" + tableBody.get(i).get(j) + "</td>");
            }
            table.append("   </tr>");
        }
        table.append("  </table> ");
        html = html.replace("<table></table>", table.toString());

        return html;
    }

    /**
     *
     * @Description 获取HTML页面基本代码
     *
     * @author 
     * @date 
     * @param title
     * @return java.lang.String
     *
     */
    public static String getHtml(String title) {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append(" <head>");
        html.append("  <title>" + title + "</title>");
        html.append("  <style></style>");
        html.append(" </head>");
        html.append(" <body>");
        html.append("  <div id=\"beginText\"></div>");
        html.append("  <table></table>");
        html.append("  <div id=\"endText\"></div>");
        html.append(" </body>");
        html.append("</html>");
        return html.toString();
    }

}
