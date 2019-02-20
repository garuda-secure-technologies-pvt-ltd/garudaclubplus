/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */////

package com.openbravo.pos.clubmang;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author swathi
 */
public class Facility implements SerializableRead, SerializableWrite, IKeyed {
     private String ID;
     private String Name;
    // private boolean joinfee;
     private Double joinamt;
   //  private boolean renewalfee;
     private Double renewalamt;
     private String rperiod;
  //   private boolean usagefee;
     private Double usageamt;
     private String uperiod;
     private boolean echeck;
     private String dueperiod;
     private String jfeeaccount;
     private String renewalacc;
     private String usageacc;
     private String memtype;
     private Double debtmax;
     private String type;
     private String staxcat;
     private int appto;
   //  private Boolean billbfusage;//bill before usage

     public void readValues(DataRead dr) throws BasicException {
         ID = dr.getString(1);
         Name = dr.getString(2);
      //   joinfee=dr.getBoolean(3);
         joinamt=dr.getDouble(3);
     //    renewalfee=dr.getBoolean(5);
         renewalamt=dr.getDouble(4);
         rperiod=dr.getString(5);
    //     usagefee=dr.getBoolean(8);
         usageamt=dr.getDouble(6);
         uperiod=dr.getString(7);
         echeck=dr.getBoolean(8);
         dueperiod=dr.getString(9);
         jfeeaccount=dr.getString(10);
         renewalacc=dr.getString(11);
         usageacc=dr.getString(12);
         memtype=dr.getString(13);
         debtmax=dr.getDouble(14);
         type=dr.getString(15);
         staxcat=dr.getString(16);
         appto=dr.getInt(17);
      //   billbfusage=dr.getBoolean(20);
     }
     public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, ID);
        dp.setString(2, Name);
     }
     public String getservicetax(){
      return staxcat;
     }
     public void setID(String id){
        ID=id;
     }
     public void setName(String name){
        Name=name;
     }
    // public Boolean getBillBfUsage(){
    //  return billbfusage;
   //  }
     public String getType(){
       return type;
     }
     public String getMemtype(){
      return memtype;
     }
     public Double getdebtMax(){
       return debtmax;
     }
     public String getRenewalacc(){
       return renewalacc;
     }
     public String getUsageacc(){
       return usageacc;
     }
     public String getJoinfeeAccount(){
       return jfeeaccount;
     }
     public String getdueperiod(){
       return dueperiod;
     }
      public Object getKey() {
        return ID;
     }
     public String getid(){
         return ID;
     }
     public String getName(){
        return Name;
     }
 //  public boolean getjfee(){
 //        return joinfee;
 //  }
     public Double getjamt(){
        return joinamt;
    }
 //     public boolean getrfee(){
 //       return renewalfee;
 //    }
     public Double getramt(){
        return renewalamt;
     }
     public String getrperiod(){
        return rperiod;
     }
   //  public boolean getufee(){
   //     return usagefee;
  //   }
     public Double getuamt(){
        return usageamt;
     }
     public String getuperiod(){
        return uperiod;
     }
     public boolean getecheck(){
        return echeck;
     }

     public String toString(){
        return Name;
     }
}
