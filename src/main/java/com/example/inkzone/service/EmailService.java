package com.example.inkzone.service;

import java.time.LocalDate;
import java.util.Locale;

import com.example.inkzone.model.dto.view.UserViewModel;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("inkzoneservice@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        javaMailSender.send(message);
        System.out.println("Message sent successfully");

    }

    public void sendEmailToMultipleRecipients(String [] toEmails, String subject, String body){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom("inkzoneservice@gmail.com");
            mimeMessageHelper.setTo(toEmails);
            mimeMessageHelper.setSubject(subject + LocalDate.now().toString());
            mimeMessageHelper.setText(generateTaskEmailText(body), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Message sent successfully");
    }

    private String generateTaskEmailText(String body) {
        Context ctx = new Context();
        ctx.setLocale(Locale.getDefault());

        ctx.setVariable("body", body);

        return templateEngine.process("task-email", ctx);
    }

    public void sendRegistrationEmail(UserViewModel userEmailViewModel) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom("inkzoneservice@gmail.com");
            mimeMessageHelper.setTo(userEmailViewModel.getEmail());
            mimeMessageHelper.setSubject("Welcome to the Inkzone Family!");
            mimeMessageHelper.setText(generateEmailText(userEmailViewModel), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }



    private String generateEmailText(UserViewModel userEmailViewModel) {
        Context ctx = new Context();
        ctx.setLocale(Locale.getDefault());
        String userFullName = userEmailViewModel.getFirstName() + " " + userEmailViewModel.getLastName();
        ctx.setVariable("user", userFullName);

        return templateEngine.process("email-template", ctx);
    }


    public void sendForgottenPasswordEmail(String email, String resetPasswordLink) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("inkzoneservice@gmail.com");
        helper.setTo(email);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
    }
}
