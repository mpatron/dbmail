/**
 * 
 */
package org.jobjects.dbmail;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

/**
 * @author Mickael
 *
 */
public class ToolsTest {
  private static Logger LOGGER = Logger.getLogger(Tools.class.getCanonicalName());
  /**
   * Test method for {@link org.jobjects.dbmail.Tools#list2Array(java.lang.String)}.
   */
  @Test
  public void testList2Array() {
    LOGGER.log(Level.INFO, "testList2Array"+SystemUtils.JAVA_IO_TMPDIR);
    String addresses="toto";    
    Tools.list2Array(addresses);
  }

}
