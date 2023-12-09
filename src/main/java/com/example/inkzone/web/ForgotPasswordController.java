package com.example.inkzone.web;

import com.example.inkzone.model.dto.view.UserViewModel;
import com.example.inkzone.service.EmailService;
import com.example.inkzone.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.aspectj.apache.bcel.classfile.Utility;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    private final UserService userService;
    private final EmailService emailService;

    public ForgotPasswordController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgotPassword";

    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
            String resetPasswordLink = "http://localhost:8080/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgotPassword";
    }

    public void sendEmail(String email, String resetPasswordLink)   throws MessagingException, UnsupportedEncodingException {
        emailService.sendForgottenPasswordEmail(email, resetPasswordLink);

    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        UserViewModel user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid Token");

        }

        return "resetPassword";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        UserViewModel userViewModel = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (userViewModel == null) {
            model.addAttribute("message", "Invalid Token");

        } else {
            userService.updatePassword(userViewModel, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "resetPassword";
    }

}
