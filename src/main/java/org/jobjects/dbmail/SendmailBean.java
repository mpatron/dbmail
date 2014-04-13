package org.jobjects.dbmail;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

public class SendmailBean {

  public SendmailBean() {
  }

  private String from = null;

  private String adressesTo = null;

  private String adressesCC = null;

  private String adressesBCC = null;

  private String body = null;

  private String subject = null;

  private File[] filenames = null;

  private BodyTypeEnum bodyType = BodyTypeEnum.TEXT_PLAIN;

  private String saveMailInDir = SystemUtils.JAVA_IO_TMPDIR;

  private String host = "smtp.google.com";

  private int port = 25;

  /**
   * @return the from
   */
  public String getFrom() {
    return from;
  }

  /**
   * @param from
   *          the from to set
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * @return the addressStr
   */
  public String getAdressesTo() {
    return adressesTo;
  }

  /**
   * @param addressStr
   *          the addressStr to set
   */
  public void setAdressesTo(String adresseTo) {
    this.adressesTo = adresseTo;
  }

  /**
   * @return the adressesBCC
   */
  public String getAdressesBCC() {
    return adressesBCC;
  }

  /**
   * @param adressesBCC
   *          the adressesBCC to set
   */
  public void setAdressesBCC(String adressesBCC) {
    this.adressesBCC = adressesBCC;
  }

  /**
   * @return the body
   */
  public String getBody() {
    return body;
  }

  /**
   * @param body
   *          the body to set
   */
  public void setBody(String body) {
    this.body = body;
  }

  /**
   * @return the subject
   */
  public String getSubject() {
    return subject;
  }

  /**
   * @param subject
   *          the subject to set
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * @return the filenames
   */
  public File[] getFilenames() {
    return filenames;
  }

  /**
   * @param filenames
   *          the filenames to set
   */
  public void setFilenames(File[] filenames) {
    this.filenames = filenames;
  }

  /**
   * @return the bodytype
   */
  public BodyTypeEnum getBodyType() {
    return bodyType;
  }

  /**
   * @param bodytype
   *          the bodytype to set
   */
  public void setBodytype(BodyTypeEnum bodyType) {
    this.bodyType = bodyType;
  }

  /**
   * @return the saveMailInDir
   */
  public String getSaveMailInDir() {
    return saveMailInDir;
  }

  /**
   * @param saveMailInDir
   *          the saveMailInDir to set
   */
  public void setSaveMailInDir(String dir) {
    this.saveMailInDir = dir;
  }

  /**
   * @return the adressesCC
   */
  public String getAdressesCC() {
    return adressesCC;
  }

  /**
   * @param adressesCC
   *          the adressesCC to set
   */
  public void setAdressesCC(String copy) {
    this.adressesCC = copy;
  }

  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * @param host
   *          the host to set
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * @param port
   *          the port to set
   */
  public void setPort(int port) {
    this.port = port;
  }

}
