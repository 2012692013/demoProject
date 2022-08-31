package com.qyh.demo.base.third.mail;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
/**
 * @Description:    邮件发送工具类
 * @CreateDate:     2019年02月19日 17:48:55
 * @author qiuyuehao
 */
public class SendMail {
    //http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256
    private static final String username = "XXX@qq.com";
    private static final String password = "fspzxalxwhtudgfa";//fspzxalxwhtudgfa//服务密码
    private Authenticator auth = null;
    private MimeMessage mimeMessage =null;
    private Properties pros = null;
    private Multipart multipart = null;
    private BodyPart bodypart= null;
    /**
     * 初始化账号密码并验证
     * 创建MimeMessage对象
     * 发送邮件必须的步骤:1
     * @param username
     * @param password
     */
   /* public SendMail(String username,String password){
        this.username = username;
        this.password = password;
    } */

    public SendMail() {
        super();
    }


    /**
     * 初始化MimeMessage对象
     * 发送邮件必须的步骤:3
     */
    public void initMessage(){
        this.auth = new Email_Autherticator();
        Session session = Session.getDefaultInstance(pros,auth);
        session.setDebug(true); //设置获取 debug 信息
        mimeMessage = new MimeMessage(session);
    }
    /**
     * 设置email系统参数
     * 接收一个map集合key为string类型，值为String
     * 发送邮件必须的步骤:2
     * @param map
     */
    public void setPros(Map<String,String> map){
        pros = new Properties();
        for(Map.Entry<String,String> entry:map.entrySet()){
            pros.setProperty(entry.getKey(), entry.getValue());
        }
    }
    /**
     * 验证账号密码
     * 发送邮件必须的步骤
     * @author qiuyuehao
     *
     */
    public class Email_Autherticator extends Authenticator
    {
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }
    }
    /**
     * 设置发送邮件的基本参数(去除繁琐的邮件设置)
     * @param sub 设置邮件主题
     * @param text 设置邮件文本内容
     * @param rec 设置邮件接收人
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void setDefaultMessagePros(String sub,String text,String rec) throws MessagingException, UnsupportedEncodingException{
        mimeMessage.setSubject(sub);
        mimeMessage.setText(text);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(rec));
        mimeMessage.setSentDate(new Date());
        mimeMessage.setFrom(new InternetAddress(username,username));
    }
    /**
     * 设置主题
     * @param subject
     * @throws MessagingException
     */
    public void  setSubject(String subject) throws MessagingException{
        mimeMessage.setSubject(subject);
    }
    /**
     * 设置日期
     * @param date
     * @throws MessagingException
     */
    public void  setDate(Date date) throws MessagingException{
        mimeMessage.setSentDate(new Date());
    }
    /**
     * 设置邮件文本内容
     * @param text
     * @throws MessagingException
     */
    public void setText(String text) throws MessagingException{
        mimeMessage.setText(text);
    }
    /**
     * 设置邮件头部
     * @param arg0
     * @param arg1
     * @throws MessagingException
     */
    public void setHeader(String arg0,String arg1) throws MessagingException{
        mimeMessage.setHeader(arg0, arg1);
    }
    /**
     * 设置邮件接收人地址 <单人发送>
     * @param recipient
     * @throws MessagingException
     */
    public void setRecipient(String recipient) throws MessagingException{
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    }
    /**
     * 设置邮件接收人地址 <多人发送>
     * @param list
     * @throws MessagingException
     * @throws AddressException
     */
    public String setRecipients(List<String> recs) throws AddressException, MessagingException{
        if(recs.isEmpty()){
            return "接收人地址为空!";
        }
        for(String str:recs){
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(str));
        }
        return "加入接收人地址成功!";
    }
    /**
     * 设置邮件接收人地址 <多人发送>
     * @param StringBuffer<parms,parms2,parms.....>
     * @throws MessagingException
     * @throws AddressException
     */
    @SuppressWarnings("static-access")
    public String setRecipients(StringBuffer sb) throws AddressException, MessagingException{
        if(sb==null||"".equals(sb)){
            return "字符串数据为空!";
        }
        Address []address = new InternetAddress().parse(sb.toString());
        mimeMessage.addRecipients(Message.RecipientType.TO, address);
        return "收件人加入成功";
    }
    /**
     * 设置邮件发送人的名字
     * @param from
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public void setFrom(String from) throws UnsupportedEncodingException, MessagingException{
        mimeMessage.setFrom(new InternetAddress(username,from));
    }
    /**
     * 发送邮件<单人发送>
     * return 是否发送成功
     * @throws MessagingException
     */
    public String sendMessage() throws MessagingException{
        Transport.send(mimeMessage);
        return "success";
    }
    /**
     * 设置附件
     * @param file 发送文件的路径
     */
    public void setMultipart(String file) throws MessagingException, IOException{
        if(multipart==null){
            multipart = new MimeMultipart();
        }
        multipart.addBodyPart(writeFiles(file));
        mimeMessage.setContent(multipart);
    }
    /**
     * 设置附件<添加多附件>
     * @param fileList<接收List集合>
     * @throws MessagingException
     * @throws IOException
     */
    public void setMultiparts(List<String> fileList) throws MessagingException, IOException{
        if(multipart==null){
            multipart = new MimeMultipart();
        }
        for(String s:fileList){
            multipart.addBodyPart(writeFiles(s));
        }
        mimeMessage.setContent(multipart);
    }
    /**
     * 发送文本内容，设置编码方式
     * <方法与发送附件配套使用>
     * <发送普通的文本内容请使用setText()方法>
     * @param s
     * @param type
     * @throws MessagingException
     */
    public void setContent(String s,String type) throws MessagingException{
        if(multipart==null){
            multipart = new MimeMultipart();
        }
        bodypart = new MimeBodyPart();
        bodypart.setContent(s, type);
        multipart.addBodyPart(bodypart);
        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();
    }
    /**
     * 读取附件
     * @param filePath
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public BodyPart writeFiles(String filePath)throws IOException, MessagingException{
        File file = new File(filePath);
        if(!file.exists()){
            throw new IOException("文件不存在!请确定文件路径是否正确");
        }
        bodypart = new MimeBodyPart();
        DataSource dataSource = new FileDataSource(file);
        bodypart.setDataHandler(new DataHandler(dataSource));
        //文件名要加入编码，不然出现乱码
        bodypart.setFileName(MimeUtility.encodeText(file.getName()));
        return bodypart;
    }

    /**
     * 发送邮件消息（文本内容）
     * @param list 发件人
     * @param content 发送内容
     * @throws IOException
     * @throws MessagingException
     * 2018年8月6日
     * @author qiuyuehao
     */
    public static void sendMessageEmail(List<String> list,String content)throws IOException, MessagingException{
        Map<String,String> map= new HashMap<String,String>();
        SendMail mail = new SendMail();
        map.put("mail.smtp.host", "smtp.qq.com");
        map.put("mail.smtp.auth", "true");
        map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        map.put("mail.smtp.port", "465");
        map.put("mail.smtp.socketFactory.port", "465");
        mail.setPros(map);
        mail.initMessage();
        /*List<String> list = new ArrayList<String>();
        list.add("2524536977@qq.com");*/
        mail.setRecipients(list);
        mail.setSubject("邮件通知消息");//标题
        mail.setDate(new Date());
        mail.setFrom("律保保平台");
        mail.setContent(content, "text/html; charset=UTF-8");
        System.out.println(mail.sendMessage());//https://blog.csdn.net/Coding_One/article/details/51354456

    }

    /**
     * 发送邮件消息（文本内容和一个附件）
     * @param list 发件人
     * @param content 发送内容
     * @param path 文件路径
     * @throws IOException
     * @throws MessagingException
     * 2018年8月6日
     * @author qiuyuehao
     */
    public static void sendMessageEmailAndAccessory(List<String> list,String content,String path)throws IOException, MessagingException{
        Map<String,String> map= new HashMap<String,String>();
        SendMail mail = new SendMail();
        map.put("mail.smtp.host", "smtp.qq.com");
        map.put("mail.smtp.auth", "true");
        map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        map.put("mail.smtp.port", "465");
        map.put("mail.smtp.socketFactory.port", "465");
        mail.setPros(map);
        mail.initMessage();
        /*List<String> list = new ArrayList<String>();
        list.add("2524536977@qq.com");*/
        mail.setRecipients(list);
        mail.setSubject("测试邮箱");
        mail.setDate(new Date());
        mail.setFrom("律保保");
        mail.setMultipart(path);//"D:你你你.txt"
        mail.setContent(content, "text/html; charset=UTF-8");
        System.out.println(mail.sendMessage());

    }
    /**
     * 发送邮件消息（文本内容和多个附件）
     * @param list
     * @param content
     * @param path
     * @throws IOException
     * @throws MessagingException
     * 2018年8月6日
     * @author qiuyuehao
     */
    public static void sendMessageEmailAndAccessorys(List<String> list,String content,List<String> fileList)throws IOException, MessagingException{
        Map<String,String> map= new HashMap<String,String>();
        //SendMail mail = new SendMail("2934404317@qq.com","pjlqzlmxrrwwdejj");
        SendMail mail = new SendMail();
        map.put("mail.smtp.host", "smtp.qq.com");
        map.put("mail.smtp.auth", "true");
        map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        map.put("mail.smtp.port", "465");
        map.put("mail.smtp.socketFactory.port", "465");
        mail.setPros(map);
        mail.initMessage();
        mail.setRecipients(list);
        mail.setSubject("测试邮箱");
        mail.setDate(new Date());
        mail.setFrom("律保保");
        mail.setMultiparts(fileList);
        mail.setContent(content, "text/html; charset=UTF-8");
        System.out.println(mail.sendMessage());

    }


}
