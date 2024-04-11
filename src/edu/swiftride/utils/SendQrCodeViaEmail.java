/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.swiftride.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.ImageView;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static edu.swiftride.utils.CreatePdf.createPDF;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.transform.dom.DOMSource;
/**
 *
 * @author skann
 */
public class SendQrCodeViaEmail {
    private static String verificationCode;
    private static String email;
     public static void sendQrCode(String filename,String email,BufferedImage bufferedImage) throws AddressException, MessagingException {
        try {
            //  InternetAddress[] toAddresse = { new InternetAddress(tfemail.getText())};
            // generate verification code
            
            // send verification code via email
            
            String to = email;
            String subject = "Votre Qrcode";
            String message = "Merci pour votre fidélité avec notre application !";
            
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            
            String from = "swiftride2023@gmail.com";
            String password = "mkokjmqjrihgumfy";
            
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });
            
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
                    msg.setContent("<html> <body style=\"background-color: #e1d2b8;\">\n" +
"  <div style=\"text-align: center\">"+
"        <table>\n" +
"        <tr><td></td>\n" +
"        <td><h4 style=\"color:#2e006c\">Bonjour, </h4></td></tr>\n" +
"        </table>\n" +
"        <p style=\"color:#2e006c\"> Cher Client,</p>\n" +
"        <p style=\"color:#2e006c\">\"Votre qr code est dans le fichier pdf ci-dessous "
                  +"</p>"+

          "</div>"+
"    </body>\n" +
"</html>","text/html");
             
               DataSource source = new FileDataSource(createPDF(filename,bufferedImage));
                msg.setDataHandler(new DataHandler(source));
               msg.setFileName(createPDF(filename,bufferedImage).getName());
            Transport.send(msg);
                       // createPDF(filename,bufferedImage).deleteOnExit();
    deletefile(createPDF(filename,bufferedImage));
        } catch (Exception ex) {
                   System.out.println(ex.getMessage());         
        }

    }
     private static void deletefile(File file){
         file.delete();
     }


}
