package com.example.grupo_iot;

import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CorreoElectronico {
    public static void enviarCorreo(String destinatario, String asunto, String contenido) {
        final String usuario = "techbat2023@gmail.com";
        final String contrasena = "nzkrvstxuctidsfu";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(usuario, contrasena);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(contenido);

            Transport.send(message);
            Log.d("Correo", "Correo enviado a " + destinatario);

        } catch (Exception e) {
            Log.e("Correo", "Error al enviar correo a " + destinatario, e);
            e.printStackTrace();
        }
    }
}
