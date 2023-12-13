package com.example.inkzone.service;



import com.example.inkzone.model.dto.view.UserViewModel;


import javax.mail.MessagingException;



public interface EmailService {


    public void sendEmail(String toEmail, String subject, String body);

    public void sendEmailToMultipleRecipients(String [] toEmails, String subject, String body);



    public void sendRegistrationEmail(UserViewModel userEmailViewModel);




    public void sendForgottenPasswordEmail(String email, String resetPasswordLink) throws MessagingException;
}
