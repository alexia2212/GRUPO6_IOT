package com.example.grupo_iot;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailSender {

    public static void sendEmail(String subject, String userEmail, String message, Context context) {
        new SendEmailTask(subject, userEmail, message, context).execute();
    }
    private static class SendEmailTask extends AsyncTask<Void, Void, Void> {
        private String subject;
        private String userEmail;
        private String message;
        private Context context;
        public SendEmailTask(String subject, String userEmail, String message,Context context) {
            this.subject = subject;
            this.userEmail = userEmail;
            this.message = message;
            this.context = context;
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("msg-test", "Ingreso al send email");

            if (context == null) {
                Log.e("msg-test", "Contexto nulo en SendEmailTask");
                return null;
            }
            //final String subject = "Registro Exitoso";
            //final String message = "Bienvenido a la familia Telebat, gracias por tu registro pronto estaremos enviando una confirmación para que puedas acceder a la aplicación.";
            final String senderEmail = "techbatapp@gmail.com";
            final String senderPassword = "dnfauxduwvblpzle";

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

                mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(userEmail));
                mimeMessage.setSubject(subject);

                // Cuerpo del mensaje con formato HTML
                MimeMultipart multipart = new MimeMultipart();

                // Parte del texto
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(message, "utf-8", "html");

                // Agregar las partes al cuerpo del mensaje
                multipart.addBodyPart(textPart);

                // Establecer el contenido del mensaje
                mimeMessage.setContent(multipart);

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