package org.jobjects.dbmail;

import java.io.File;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class SendMail {

  public static void main(String[] args) {
    String adresseFromValue = null;
    String adressesTOValue = null;
    String subjectValue = null;
    String bodyValue = null;
    String filesValue = null;
    BodyTypeEnum bodytypeValue = null;
    String dirValue = null;
    String adressesCCValue = null;
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
    options.addOption("c", "copy", true, "Destinataires du mail en copie séparé par des ','.");
    options.addOption("h", "host", true, "Nom du serveur (smtp.gmail.com).");
    options.addOption("p", "port", true, "Port du serveur (25).");

    CommandLineParser parser = new PosixParser();
    try {

      CommandLine line = parser.parse(options, args);

      adresseFromValue = line.getOptionValue("f");
      adressesTOValue = line.getOptionValue("a");
      subjectValue = line.getOptionValue("s");
      bodyValue = line.getOptionValue("b");
      filesValue = line.getOptionValue("F");
      if (line.getOptionValue("e") == null) {
        bodytypeValue = BodyTypeEnum.TEXT_PLAIN;
      } else {
        if (!(line.getOptionValue("e").equals("HTML") || line.getOptionValue("e").equals("TEXT"))) {
          System.err.println("Error : encode=" + line.getOptionValue("e"));
          formatter.printHelp(cmdLineSyntax, header, options, footer);
          System.exit(1);
        } else {
          if (line.getOptionValue("e").equals("HTML")) {
            bodytypeValue = BodyTypeEnum.TEXT_HTML;
          } else {
            bodytypeValue = BodyTypeEnum.TEXT_PLAIN;
          }
        }
      }
      dirValue = line.getOptionValue("d");
      adressesCCValue = line.getOptionValue("c");
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

    SendmailBean sendmailBean = new SendmailBean();

    sendmailBean.setFrom(adresseFromValue);
    sendmailBean.setAdressesTo(adressesTOValue);
    sendmailBean.setAdressesCC(adressesCCValue);
    sendmailBean.setSubject(subjectValue);
    sendmailBean.setBody(bodyValue);
    sendmailBean.setBodytype(bodytypeValue);

    sendmailBean.setHost(hostValue);
    sendmailBean.setPort(portValue);
    sendmailBean.setSaveMailInDir(dirValue);
    
    SendmailRun sendmailRun =new SendmailRun (sendmailBean);
    sendmailRun.run();    
  }

}
