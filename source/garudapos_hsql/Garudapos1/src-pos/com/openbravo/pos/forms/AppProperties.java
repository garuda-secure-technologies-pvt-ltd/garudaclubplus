
package com.openbravo.pos.forms;

import java.io.File;

/**
 *
 * @author adrianromero
 */
public interface AppProperties {

    public File getConfigFile(); 
    public String getHost();    
    public String getProperty(String sKey); // Config property.
}
