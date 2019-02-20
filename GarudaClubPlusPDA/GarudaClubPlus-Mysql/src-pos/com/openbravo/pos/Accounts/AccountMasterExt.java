/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.Accounts;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

/**
 *
 * @author swathi
 */
public class AccountMasterExt extends AccountMaster{

   
    private String type;
    private String sign;
    private String max;
    private boolean editable;
    @Override
    public void readValues(DataRead dr) throws BasicException {
        id=dr.getString(1);
        Name=dr.getString(2);
        Searchkey=dr.getString(3);
        type=dr.getString(4);
        sign=dr.getString(5);
        parent=dr.getString(7);
        max=dr.getString(6);
        editable=dr.getBoolean(8);
    }
   
    public String getmax(){
      return max;
    }
    public boolean isEditable(){
       return editable;
    }
     public String gettype(){
     return type;
    }
     public String getsign(){
     return sign;
    }
    
  
  //  public void toString(){

  //  }
}
