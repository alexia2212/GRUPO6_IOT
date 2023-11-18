package com.example.grupo_iot;
import android.os.AsyncTask;
import android.util.Log;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public static void sendEmail(String userEmail) {
        new SendEmailTask(userEmail).execute();
    }


    private static class SendEmailTask extends AsyncTask<Void, Void, Void> {
        private String userEmail;

        public SendEmailTask(String userEmail) {
            this.userEmail = userEmail;
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("msg-test", "Ingreso al send email");

            final String subject = "Registro Exitoso";
            final String message = "Bienvenido a la familia Telebat, gracias por tu registro pronto estaremos enviando una confirmación para que puedas acceder a la aplicación.";
            final String senderEmail = "techbat2023@gmail.com";
            final String senderPassword = "nzkrvstxuctidsfu";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "*");
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            Log.d("msg-test", "Se configuró la sesión de email");

            try {
                Log.d("msg-test", "Entrar al try");

                MimeMessage mimeMessage = new MimeMessage(session);

                // Establecer dirección de correo electrónico del remitente
                //mimeMessage.setFrom(new InternetAddress(senderEmail));
                // Establecer dirección de correo electrónico del destinatario
                mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(userEmail));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(message);
                Log.d("msg-test", "Antes de envío de correo");
                Transport.send(mimeMessage);

            } catch (MessagingException e) {
                Log.d("msg-test", "Excepción en envío de correo: " + e);
                throw new RuntimeException("Error al enviar el correo electrónico", e);
            }
            return null;
        }
    }
}