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

/**
 * Servicio de envío de correos electrónicos.
 * <p>
 * Esta clase maneja el envío de correos electrónicos para diferentes propósitos como la recuperación de contraseñas
 * y el envío de credenciales de usuario. Utiliza {@link JavaMailSender} para la creación y envío de los mensajes,
 * y {@link TemplateEngine} para procesar las plantillas de correo electrónico con los datos proporcionados.
 * </p>
 *
 * @see JavaMailSender
 * @see TemplateEngine
 */

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.host}")
    private String username;

    /**
     * Constructor de la clase {@link EmailService}.
     *
     * @param mailSender El objeto que envía los correos electrónicos.
     * @param templateEngine El motor de plantillas utilizado para procesar los correos electrónicos.
     */

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Envía un correo electrónico para la recuperación de la contraseña.
     * <p>
     * Este método envía un correo electrónico con un código de recuperación de contraseña a la dirección de correo
     * especificada.
     * </p>
     *
     * @param to La dirección de correo electrónico a la que se enviará el mensaje.
     * @param code El código de recuperación de la contraseña.
     */

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

    /**
     * Envía un correo electrónico con las credenciales de usuario.
     * <p>
     * Este método envía un correo electrónico con el nombre de usuario y la contraseña proporcionados al usuario
     * especificado.
     * </p>
     *
     * @param to La dirección de correo electrónico a la que se enviará el mensaje.
     * @param username El nombre de usuario del destinatario.
     * @param password La contraseña del destinatario.
     * @return {@code true} si el correo se envió correctamente, {@code false} si ocurrió un error.
     */

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