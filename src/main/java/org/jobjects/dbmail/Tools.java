package org.jobjects.dbmail;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Tools {
  private static Logger LOGGER = Logger.getLogger(Tools.class.getCanonicalName());

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
          LOGGER.log(Level.SEVERE, "addresses=" + addresses, ae);
        }
      }
      returnValue = (String[]) ll.toArray(new String[ll.size()]);
    }
    return returnValue;
  }

}
