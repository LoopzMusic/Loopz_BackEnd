package dev.trier.ecommerce.service.emailCredencialService;

import dev.trier.ecommerce.model.EmailCredenciais.EmailCredencialModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailCredencialService emailCredencialService; // injeta o serviço

    public String enviarEmailHtml(String destinatario, String assunto, String mensagemHtml) {
        try {

            EmailCredencialModel creds = emailCredencialService.buscarCredenciais(1L);


            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(587);
            mailSender.setUsername(creds.getEmail());
            mailSender.setPassword(creds.getPassword());

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");


            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(creds.getEmail());
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagemHtml, true);


            mailSender.send(mimeMessage);

            return "Email enviado com sucesso!";
        } catch (Exception e) {
            return "Erro ao enviar email: " + e.getMessage();
        }
    }

    @Async
    public CompletableFuture<String> enviarEmailHtmlAsync(String destinatario, String assunto, String mensagemHtml) {
        String resultado = enviarEmailHtml(destinatario, assunto, mensagemHtml);
        return CompletableFuture.completedFuture(resultado);
    }
}
