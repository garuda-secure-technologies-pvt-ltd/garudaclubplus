/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.adminUtils;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author user
 */
public class MemberInfo  implements SerializableRead, SerializableWrite {
    String mid;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
    public void readValues(DataRead dr) throws BasicException {
        mid = dr.getString(1);
        
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, mid);
        
    }


}
