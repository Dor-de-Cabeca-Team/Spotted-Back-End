package com.Rede_Social.Service.Email;

import com.Rede_Social.Entity.EmailEntity;
import com.Rede_Social.Entity.Enum.StatusEmail;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.Email.EmailSendException;
import com.Rede_Social.Repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${EmailFrom}")
    private String emailFrom;

    @Value("${EmailURL}")
    private String emailURL;

    public EmailEntity criarEmail(UserEntity user) {
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setOwnerRef(user.getUuid());
        emailEntity.setEmailFrom(this.emailFrom);
        emailEntity.setEmailTo(user.getEmail());
        emailEntity.setSubject("Bem-vindo(a) ao Spotted! 🎉");

        String hash = generateHash(user.getNome(), user.getEmail());

        String validationLink = "http://"+emailURL+"/email-validador?idUser=" + user.getUuid() + "&hash=" + hash;

        emailEntity.setText("Olá " + user.getNome() + ",\n\n" +
                "Estamos muito felizes em tê-lo(a) como parte da nossa comunidade! Para completar o seu cadastro e ativar a sua conta, por favor clique no link abaixo:\n\n" +
                validationLink + "\n\n" +
                "Se precisar de qualquer coisa, não hesite em nos contatar.\n\n" +
                "Atenciosamente,\n" +
                "Equipe Dor de Cabeça");

        return emailEntity;
    }

    @Async
    public void enviaEmail(EmailEntity emailEntity) {
        emailEntity.setSendDateEmail(LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailEntity.getEmailFrom());
            message.setTo(emailEntity.getEmailTo().split("\\s*,\\s*"));
            message.setSubject(emailEntity.getSubject());
            message.setText(emailEntity.getText());
            mailSender.send(message);

            emailEntity.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e) {
            emailEntity.setStatusEmail(StatusEmail.ERROR);
            throw new EmailSendException("Erro ao enviar o e-mail para: " + emailEntity.getEmailTo(), e);
        } finally {
            emailRepository.save(emailEntity);
        }
    }

    public static String generateHash(String nome, String email) {
        try {
            String concat = nome + email;

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = md.digest(concat.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public EmailEntity criarEmailRedefinicaoSenha(UserEntity user, String token) {
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setOwnerRef(user.getUuid());
        emailEntity.setEmailFrom(this.emailFrom);
        emailEntity.setEmailTo(user.getEmail());
        emailEntity.setSubject("Redefinição de Senha - Spotted");

        String resetLink = "http://"+emailURL+"/reset-password?token=" + token; // Altere o URL para o frontend correto

        emailEntity.setText("Olá " + user.getNome() + ",\n\n" +
                "Recebemos uma solicitação para redefinir sua senha. Caso você tenha feito essa solicitação, clique no link abaixo para redefinir sua senha:\n\n" +
                resetLink + "\n\n" +
                "Se você não solicitou a redefinição de senha, ignore este e-mail. O link expirará em 30 minutos.\n\n" +
                "Atenciosamente,\n" +
                "Equipe Dor de Cabeça");

        return emailEntity;
    }
}