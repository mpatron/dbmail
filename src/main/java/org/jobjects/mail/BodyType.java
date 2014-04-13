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

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Mickael Patron
 * @version 2.0
 */
public class BodyType implements Comparable<BodyType> {

  public final static BodyType TEXT_PLAIN = new BodyType(BodyType.text_plain);

  public final static BodyType TEXT_HTML  = new BodyType(BodyType.text_html);

  private final static String  text_plain = "text/plain";

  private final static String  text_html  = "text/html";

  private String               value      = null;

  private BodyType(String type) {
    value = type;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return value;
  }

  /**
   * Returns the value.
   * 
   * @return String
   */
  public String getValue() {
    return value;
  }

  /**
   * @see java.lang.Object#equals(Object)
   */
  public boolean equals(Object object) {
    if (object == this) { return true; }
    if (!(object instanceof BodyType)) { return false; }
    BodyType rhs = (BodyType) object;
    return new EqualsBuilder().append(this.value, rhs.value).isEquals();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return new HashCodeBuilder(-865361163, -1547940145).append(this.value).toHashCode();
  }

  /**
   * @see java.lang.Comparable#compareTo(Object)
   */
  public int compareTo(BodyType object) {
    return new CompareToBuilder().append(this.value, object.value).toComparison();
  }
}