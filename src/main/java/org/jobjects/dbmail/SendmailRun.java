package org.jobjects.dbmail;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang3.StringUtils;

public class SendmailRun extends Thread {
  private Logger LOGGER = Logger.getLogger(SendmailRun.class.getCanonicalName());

  private SendmailBean sendmail;

  public SendmailRun(SendmailBean sendmail) {
    this.sendmail=sendmail;
  }

  public void run() {
    StringBuilder logMsg = new StringBuilder();
    logMsg.append("FROM [" + sendmail.getFrom() + "]"+SystemUtils.LINE_SEPARATOR);
    logMsg.append("TO [" + sendmail.getAdressesTo() + "]"+SystemUtils.LINE_SEPARATOR);
    logMsg.append("CC [" + sendmail.getAdressesCC() + "]"+SystemUtils.LINE_SEPARATOR);
    logMsg.append("SUBJECT [" + sendmail.getSubject() + "]"+SystemUtils.LINE_SEPARATOR);
    logMsg.append("BODY [" + sendmail.getBody() + "]"+SystemUtils.LINE_SEPARATOR);
    logMsg.append("."+SystemUtils.LINE_SEPARATOR);
    LOGGER.log(Level.INFO,logMsg.toString());

    Properties props = System.getProperties();

    try {
      // host= "smtp.google.com";
      // port= "25";
      props.put("mail.smtp.host", sendmail.getHost());
      props.put("mail.smtp.port", sendmail.getPort());
      props.put("mail.transport.protocol", "smtp");

      Session session = Session.getDefaultInstance(props, null);
      session.setDebug(false);

      MimeMessage msg = new MimeMessage(session);
      Multipart mp = new MimeMultipart();
      if (sendmail.getFrom() != null) {
        msg.setFrom(new InternetAddress(sendmail.getFrom()));
      }

      String[] adressesTos = Tools.list2Array(sendmail.getAdressesTo());
      {
        InternetAddress[] address = new InternetAddress[adressesTos.length];
        for (int _i = 0; _i < adressesTos.length; _i++) {
          address[_i] = new InternetAddress(adressesTos[_i]);
        }
        msg.setRecipients(Message.RecipientType.TO, address);
      }

      if (sendmail.getAdressesCC() != null) {
        String[] adresses = Tools.list2Array(sendmail.getAdressesCC());
        InternetAddress[] netAdresses = new InternetAddress[adresses.length];
        for (int _i = 0; _i < adresses.length; _i++) {
          netAdresses[_i] = new InternetAddress(adresses[_i]);
        }
        msg.setRecipients(Message.RecipientType.CC, netAdresses);
      }

      if (sendmail.getAdressesBCC() != null) {
        String[] adresses = Tools.list2Array(sendmail.getAdressesBCC());
        InternetAddress[] netAdresses = new InternetAddress[adresses.length];
        for (int _i = 0; _i < adresses.length; _i++) {
          netAdresses[_i] = new InternetAddress(adresses[_i]);
        }
        msg.setRecipients(Message.RecipientType.BCC, netAdresses);
      }

      if (sendmail.getSubject() != null) {
        msg.setSubject(sendmail.getSubject());
      }

      // corps du message
      if(StringUtils.isNotEmpty(sendmail.getBody())) {
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setContent(sendmail.getBody(),sendmail.getBodyType().getText());
        mbp1.setText(sendmail.getBody(), "UTF-8");
        mp.addBodyPart(mbp1);
      }

      // piéce jointe
      if (sendmail.getFilenames() != null) {
        for (File filename : sendmail.getFilenames()) {
          MimeBodyPart mbp = new MimeBodyPart();
          FileDataSource fds = new FileDataSource(filename);
          mbp.setDataHandler(new DataHandler(fds));
          mbp.setFileName(fds.getName());
          mp.addBodyPart(mbp);
        }
      }

      msg.setContent(mp);
      msg.setSentDate(new java.util.Date());

      if (sendmail.getSaveMailInDir() != null) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
        String date = df.format(new Date(System.currentTimeMillis()));
        FileOutputStream fos = new FileOutputStream(File.createTempFile("mail-" + date + "-", ".asc", new File(sendmail.getSaveMailInDir())));
        msg.writeTo(fos);
        fos.close();
        fos = null;
      }

      Transport.send(msg);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }

  // ----------------------------------------------------------------------------

}
