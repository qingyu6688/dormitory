package com.example.service.impl;

import com.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件服务实现类
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("【TechFlow】密码重置验证码");
            
            // HTML邮件内容
            String content = buildEmailContent(code);
            helper.setText(content, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送失败", e);
        }
    }
    
    /**
     * 构建邮件HTML内容
     */
    private String buildEmailContent(String code) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                "        .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
                "        .header { text-align: center; padding-bottom: 20px; border-bottom: 2px solid #2563EB; }" +
                "        .header h1 { color: #2563EB; margin: 0; }" +
                "        .content { padding: 30px 0; }" +
                "        .code-box { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; font-size: 32px; font-weight: bold; text-align: center; padding: 20px; border-radius: 8px; letter-spacing: 8px; margin: 20px 0; }" +
                "        .tips { background-color: #f0f9ff; border-left: 4px solid #2563EB; padding: 15px; margin: 20px 0; color: #1e40af; }" +
                "        .footer { text-align: center; padding-top: 20px; border-top: 1px solid #e5e7eb; color: #6b7280; font-size: 14px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>🔐 TechFlow 密码重置</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>尊敬的用户，您好！</p>" +
                "            <p>您正在进行密码重置操作，您的验证码是：</p>" +
                "            <div class='code-box'>" + code + "</div>" +
                "            <div class='tips'>" +
                "                <strong>⚠️ 安全提示：</strong><br>" +
                "                • 验证码有效期为 <strong>5分钟</strong><br>" +
                "                • 请勿将验证码告知他人<br>" +
                "                • 如非本人操作，请忽略此邮件" +
                "            </div>" +
                "            <p>如有任何疑问，请联系我们的客服团队。</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>此邮件由系统自动发送，请勿直接回复</p>" +
                "            <p>&copy; 2025 TechFlow Team. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
