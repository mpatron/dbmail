/**
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
package org.jobjects.dbmail;

/**
 * @author Mickael
 * 
 */
public enum BodyTypeEnum {
  TEXT_PLAIN("text/plain"), TEXT_HTML("text/html");

  private BodyTypeEnum(String text) {
    this.text = text;
  }

  private String text;

  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text
   *          the text to set
   */
  public void setText(String text) {
    this.text = text;
  }

}
