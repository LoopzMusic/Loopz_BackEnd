package dev.trier.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String remetente;


    public String enviarEmailHtml(String destinatario, String assunto, String mensagemHtml) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagemHtml, true);
            javaMailSender.send(mimeMessage);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao enviar email em HTML para " + destinatario + ": " + e.getMessage();
        }
    }


    @Async
    public CompletableFuture<String> enviarEmailHtmlAsync(String destinatario, String assunto, String mensagemHtml) {
        String resultado = enviarEmailHtml(destinatario, assunto, mensagemHtml);
        return CompletableFuture.completedFuture(resultado);
    }

}
