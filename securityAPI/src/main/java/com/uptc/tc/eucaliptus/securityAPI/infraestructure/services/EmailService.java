package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.host}")
    private String username;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmailPasswordRecovery(String to, int code) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("code", code);
            context.setVariables(model);
            String htmlText = templateEngine.process("emailPasswordRecovery-template", context);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña");
            helper.setText(htmlText, true);

            ClassPathResource imageResource = new ClassPathResource("static/images/logo2.png");
            helper.addInline("logo2.png", imageResource);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmailCredentials(String to, String username, String password) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("password", password);
            context.setVariables(model);
            String htmlText = templateEngine.process("sendCredentials-template", context);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject("Bienvenido a Eucaliptus");
            helper.setText(htmlText, true);

            ClassPathResource imageResource = new ClassPathResource("static/images/logo2.png");
            helper.addInline("logo2.png", imageResource);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
