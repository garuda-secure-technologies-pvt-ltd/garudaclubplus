/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.Accounts;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.format.Formats;
import java.util.Date;
//import com.openbravo.data.loader.SerializerRead;

/**
 *
 * @author swathi
 */
public class CompanyInfo implements SerializableRead {
    private String name;
    private String id;
    private String desc;
    private Date date;
    private String path;
    private String url;
    private String address;
    public void readValues(DataRead dr) throws BasicException {
       id=dr.getString(1);
       name=dr.getString(2);
       desc=dr.getString(3);
       path=dr.getString(4);
       date=(Date)Formats.DATE.parseValue(Formats.DATE.formatValue(dr.getTimestamp(5)));
       url=dr.getString(6);
       address=dr.getString(7);
    }
    public String getName(){
      return name;
    }
    public String getId(){
       return id;
    }
    public String getDesc(){
       return desc;
    }
    public Date getDate(){
       return date;
    }
    public String getUrl(){
       return url;
    }

    public String getAddress(){
       return address;
    }
}
