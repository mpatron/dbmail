/*
Copyright 2004 Mickaël Patron

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.jobjects.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class principale avec fonction main. La fonction main trappe les variables de
 * la JVM :<br>
 * <ul>
 * <li>[-f | --from] from.</li>
 * <li>[-a | --adresses] liste des addresses séparé par des ','.</li>
 * <li>[-s | --sujet] sujet.</li>
 * <li>[-b | --body] fichier contenant le body.</li>
 * <li>[-F | --files] fichier attacher séparé par des ','.</li>
 * <li>[-e | --encode] [ HTML | TEXT ] Defini si le body est du HTML
 * ('text/html') ou du texte ('text/plain').</li>
 * <li>[-d | --dir] Répertoire de sauvegarde de mail. Ne sauve les mails que si
 * le parametre est present.</li>
 * <li>[-h | --host] Nom du serveur (smtp.gmail.com).</li>
 * <li>[-p | --port] Port du serveur (25).</li>
 * </ul>
 * 
 * @author Mickael Patron
 * @version 2.0
 */

public final class SendMail extends Thread {

  private Log      log        = LogFactory.getLog(getClass());

  private String   from       = null;

  private String[] addressStr = null;

  private String   body       = null;

  private String   subject    = null;

  private File[]   filenames  = null;

  private BodyType bodytype   = null;

  private String   dir        = null;

  private String   copy       = null;

  private String   host;

  private String   port;

  public SendMail() {
  }

  /**
   * Returns the host.
   * 
   * @return String
   */
  public String getHost() {
    return host;
  }

  /**
   * Returns the port.
   * 
   * @return String
   */
  public String getPort() {
    return port;
  }

  /**
   * Sets the host.
   * 
   * @param host
   *          The host to set
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * Sets the port.
   * 
   * @param port
   *          The port to set
   */
  public void setPort(String port) {
    this.port = port;
  }

  /**
   * Returns the dir.
   * 
   * @return String
   */
  public String getDir() {
    return dir;
  }

  /**
   * Sets the dir. Si le dir est affecté SendMail enregistre le mail dans ce
   * répertoire au format mail-yyyyMMdd-HHmmss-SSS-xxxxxx.asc. Où xxxxxx est un
   * numéro unique.
   * 
   * @param dir
   *          The dir to set
   */
  public void setDir(String dir) {
    this.dir = dir;
  }

  /**
   * Returns the copy. Destinataires du mail en copie caché séparé par des ','.
   * 
   * @return String
   */
  public String getCopy() {
    return copy;
  }

  /**
   * Sets the copy. Destinataires du mail en copie caché séparé par des ','.
   * 
   * @param copy
   *          The copy to set
   */
  public void setCopy(String copy) {
    this.copy = copy;
  }

  /**
   * Envoi des mails à un ou plusieurs destinataires au format HTML. Ce mail
   * pourra contenir un ou plusieurs fichiers joints. Cette méthode est
   * indépendante. Le séparateur entre les destinataires peut être : une virgule
   * ",", un espace " " ou un point virgule ";".
   * 
   * @param from
   *          expéditeur du message
   * @param addressStr
   *          liste des adresses des destinataires du message.
   * @param subject
   *          sujet du message
   * @param body
   *          corps du message
   * @param filenames
   *          nom du fichier attaché ou <code>null</code> si aucun fichier
   *          attaché
   * @param bodytype
   *          Defini si le body est du HTML ("text/html") ou texte
   *          ("text/plain")
   */
  public SendMail(String from, String[] addressStr, String subject, String body, File[] filenames, BodyType bodytype) {
    super();
    this.from = from;
    this.addressStr = addressStr;
    this.subject = subject;
    this.body = body;
    this.filenames = filenames;
    this.bodytype = bodytype;
  }

  public void run() {
    log.debug("from[" + from + "]");
    log.debug("addressStr[" + addressStr[0] + "]");
    log.debug("body[" + body + "]");
    log.debug("subject[" + subject + "]");

    Properties props = System.getProperties();

    try {
      //host= "smtp.google.com";
      //port= "25";
      props.put("mail.smtp.host", getHost());
      props.put("mail.smtp.port", getPort());
      props.put("mail.transport.protocol", "smtp");

      Session session = Session.getDefaultInstance(props, null);
      session.setDebug(false);

      MimeMessage msg = new MimeMessage(session);
      Multipart mp = new MimeMultipart();
      if (from != null) {
        msg.setFrom(new InternetAddress(from));
      }

      InternetAddress[] address = new InternetAddress[addressStr.length];
      for (int _i = 0; _i < addressStr.length; _i++) {
        address[_i] = new InternetAddress(addressStr[_i]);
      }
      msg.setRecipients(Message.RecipientType.TO, address);

      String[] addressStrBCC = list2Array(copy);
      if (addressStrBCC != null) {
        InternetAddress[] addressBCC = new InternetAddress[addressStrBCC.length];
        for (int _i = 0; _i < addressStrBCC.length; _i++) {
          addressBCC[_i] = new InternetAddress(addressStrBCC[_i]);
        }
        msg.setRecipients(Message.RecipientType.BCC, addressBCC);
      }

      if (subject != null) {
        msg.setSubject(subject);
      }

      //corps du message

      if ((body != null) && (bodytype != null)) {
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setContent(body, bodytype.getValue());
        mp.addBodyPart(mbp1);
        mbp1.setText(body, "UTF-8");
      }
      /*
       * if (body != null) { MimeBodyPart mbp1= new MimeBodyPart(); if (content !=
       * null) { mbp1.setContent(body, content); } //mbp1.setText(body,
       * encoding); mp.addBodyPart(mbp1); }
       */

      //piéce jointe
      if (filenames != null) {
        for (int i = 0; i < filenames.length; i++) {
          if (filenames[i] != null) {
            MimeBodyPart mbp = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(filenames[i]);
            mbp.setDataHandler(new DataHandler(fds));
            mbp.setFileName(fds.getName());
            mp.addBodyPart(mbp);
          }
        }
      }

      msg.setContent(mp);
      msg.setSentDate(new java.util.Date());

      if (dir != null) {
        try {
          SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
          String date = df.format(new Date(System.currentTimeMillis()));
          FileOutputStream fos = new FileOutputStream(File.createTempFile("mail-" + date + "-", ".asc", new File(dir)));
          msg.writeTo(fos);
          fos.close();
          fos = null;
        } catch (Exception e) {
          log.error("", e);
        }
      }

      Transport.send(msg);
    } catch (SendFailedException e) {
      log.error("Error while sending mail.", e);
    } catch (MessagingException mex) {
      log.error("Error while sending mail.", mex);
    }
  }

  //----------------------------------------------------------------------------

  /**
   * Method list2Array. Trasforme un chaine "a,b,c" en un tableau {"a", "b",
   * "c"}.
   * 
   * @param addresses
   * @return String[] retour null si addresses est null ou si il y a une erreur.
   */
  public static String[] list2Array(String addresses) {
    String[] returnValue = null;
    if (addresses != null) {
      StringTokenizer st_a = new StringTokenizer(addresses, ",");
      List<String> ll = new LinkedList<String>();
      while (st_a.hasMoreTokens()) {
        String chaine = st_a.nextToken();
        try {
          InternetAddress.parse(chaine);
          ll.add(chaine);
        } catch (AddressException ae) {
          System.err.println(ae.getMessage() + " : " + chaine);
        }
      }
      returnValue = (String[]) ll.toArray(new String[ll.size()]);
    }
    return returnValue;
  }

  /**
   * Method main.
   * 
   * @param args
   */
  //----------------------------------------------------------------------------
  public static void main(String[] args) {

    String fromValue = null;
    String adressesValue = null;
    String subjectValue = null;
    String bodyValue = null;
    String filesValue = null;
    BodyType bodytypeValue = null;
    String dirValue = null;
    String copyValue = null;
    String hostValue = null;
    Integer portValue = null;

    HelpFormatter formatter = new HelpFormatter();
    String cmdLineSyntax = "$JAVA_HOME/bin/java " + SendMail.class.getName();
    String header = "JSendmail : emission de mail.";
    String footer = "Copyright à 2004 Mickaël Patron. All Rights Reserved";

    Options options = new Options();
    options.addOption("f", "from", true, "From.");
    options.addOption("a", "adresses", true, "Liste des addresses séparé par des ','.");
    options.addOption("s", "subject", true, "Sujet.");
    options.addOption("b", "body", true, "Chaine du body.");
    options.addOption("F", "files", true, "Fichier(s) attache(s) séparé par des ','.");
    options.addOption("e",
        "encode",
        true,
        "[ HTML | TEXT ] Defini si le body est du HTML ('text/html') ou  du texte ('text/plain').");
    options.addOption("d",
        "dir",
        true,
        "Répertoire de sauvegarde de mail. Ne sauve les mails que si le parametre est present.");
    options.addOption("c", "copy", true, "Destinataires du mail en copie caché  séparé par des ','.");
    options.addOption("h", "host", true, "Nom du serveur (smtp.gmail.com).");
    options.addOption("p", "port", true, "Port du serveur (25).");

    CommandLineParser parser = new PosixParser();
    try {

      CommandLine line = parser.parse(options, args);

      fromValue = line.getOptionValue("f");
      adressesValue = line.getOptionValue("a");
      subjectValue = line.getOptionValue("s");
      bodyValue = line.getOptionValue("b");
      filesValue = line.getOptionValue("F");
      if (line.getOptionValue("e") == null) {
        bodytypeValue = BodyType.TEXT_PLAIN;
      } else {
        if (!(line.getOptionValue("e").equals("HTML") || line.getOptionValue("e").equals("TEXT"))) {
          System.err.println("Error : encode=" + line.getOptionValue("e"));
          formatter.printHelp(cmdLineSyntax, header, options, footer);
          System.exit(1);
        } else {
          if (line.getOptionValue("e").equals("HTML")) {
            bodytypeValue = BodyType.TEXT_HTML;
          } else {
            bodytypeValue = BodyType.TEXT_PLAIN;
          }
        }
      }
      dirValue = line.getOptionValue("d");
      copyValue = line.getOptionValue("c");
      hostValue = line.getOptionValue("h");
      if (line.getOptionValue("p") == null) {
        portValue = new Integer(25);
      } else {
        try {
          portValue = Integer.decode(line.getOptionValue("p"));
        } catch (NumberFormatException nfe) {
          System.err.println("Error : port=" + line.getOptionValue("p"));
          formatter.printHelp(cmdLineSyntax, header, options, footer);
          System.exit(1);
        }
      }

      if (!(line.hasOption("a") && line.hasOption("h"))) {
        System.out.println("L'option 'a' et 'h' sont obligatoire.");
        formatter.printHelp(cmdLineSyntax, header, options, footer);
        System.exit(1);
      }

    } catch (org.apache.commons.cli.ParseException pe) {
      System.err.println(pe.getMessage());
      formatter.printHelp(cmdLineSyntax, header, options, footer);
      System.exit(1);
    }

    File[] filenames = null;
    if (filesValue != null) {
      StringTokenizer st_e = new StringTokenizer(filesValue, ",");
      filenames = new File[st_e.countTokens()];
      int i_e = 0;
      while (st_e.hasMoreTokens()) {
        filenames[i_e++] = new File(st_e.nextToken());
      }
    }

    //SendMail sm= new SendMail(fromValue, addressStr, subjectValue, bodyValue,
    // filenames, bodytypeValue);
    SendMail sm = new SendMail(fromValue, list2Array(adressesValue), subjectValue, bodyValue, filenames, bodytypeValue);
    sm.setHost(hostValue);
    sm.setPort(portValue.toString());
    sm.setDir(dirValue);
    sm.setCopy(copyValue);
    sm.run();

  }

}