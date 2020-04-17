package com.hand.spc.utils;

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
import java.util.Properties;

/**
 * @Author: lkj
 * @Date: 2018/8/9 09:33
 * @E-mail: kejin.liu@hand-china.com
 * @Description: 邮件发送工具
 */
@Component
public class MailUtil {
    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    /**
     * 邮箱服务器smtp.126.com
     */
    private static final String HOST = "mailhost.kohlerco.com";//smtp.partner.outlook.cn
    /**
     * 发件人 mesdba@uabsbattery.com
     */
    private static final String USERNAME = "IMP@kohler.com.cn";//mesdba@uabsbattery.com
    /**
     * 发件人授权码Abcd1234 邮箱密码Abcd#1234
     */
    private static final String PASSWORD = "";//Abcd#321

    /**
     * 功能描述: 邮件发送，只发送文本
     *
     * @auther: lkj
     * @date: 2018/8/9 上午11:46
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
     * @author: Benjamin
     * @date: 2019/1/14 17:07
     * @description: taskSendMail
     * @version 0.1
     */
    public void taskSendMail(String to, String subject, String html) throws InterruptedException {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
        taskExecutor.execute(() -> {
            logger.info(Thread.currentThread().getName());
            MailUtil.sendMail(to, subject, html);
        });
        Thread.sleep(1000);
    }

}
