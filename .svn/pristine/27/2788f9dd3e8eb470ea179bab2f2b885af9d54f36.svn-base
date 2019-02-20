/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.clubmang;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author swathi
 */
public class FacilitytableModel {
      private List<Facilityline> fac;
      private int flag;
    private final static String[] FACILITYHEADERS = {"name","Joining Amt","JFee Account","Renewal Amt","Period","RFee Account","Usage Amt","Period","UFee Account","Entrance Check","Due Period","Member Type","Debt max","Type","Created by","Created Date"};
    private final static String[] FACILITYHEADERS1 = {"name","Joining Amt","JFee Account","Renewal Amt","Period","RFee Account","Usage Amt","Period","UFee Account","Entrance Check","Due Period","Member Type","Debt max","Type","Created by","Created Date","Deactivated By","Deativated Date"};
  private FacilitytableModel()
   {
   }

  public static FacilitytableModel emptyinstance()
  {
      FacilitytableModel d=new FacilitytableModel();
      d.fac=new ArrayList<Facilityline>();
      return d;
  }
  public static FacilitytableModel loadInstance(AppView app,int flag) throws BasicException {
      FacilitytableModel d = new FacilitytableModel();
      d.flag=flag;
     List dlist;
     if(flag==1){
             dlist= new StaticSentence(app.getSession()                 //praveen:changed type to type_
                ,"SELECT F.ID,F.NAME,F.JOINAMT,CASE WHEN F.JOINAMT<1 THEN NULL ELSE (SELECT A.NAME FROM ACCOUNTMASTER A WHERE A.ID=F.JFEEACCOUNT) END,CASE WHEN F.RENEWALAMT<1 THEN NULL ELSE (SELECT A.NAME FROM ACCOUNTMASTER A WHERE A.ID=F.RENEWALACC) END,CASE WHEN F.USAGEAMT<1 THEN NULL ELSE (SELECT A.NAME FROM ACCOUNTMASTER A WHERE A.ID=F.USAGEACC) END,F.RENEWALAMT,CASE WHEN F.RPERIODICITY IS NOT NULL  THEN  (SELECT P.NAME FROM PERIODICITY P WHERE P.ID=F.RPERIODICITY) ELSE NULL END ,F.USAGEAMT,CASE WHEN F.UPERIODICITY IS NOT NULL  THEN  (SELECT P.NAME FROM PERIODICITY P WHERE P.ID=F.UPERIODICITY) ELSE NULL END,F.ENTRANCECONTROL,F.CREATEDBY,F.CRDATE,CASE WHEN F.MEMTYPE='ALL' THEN 'ALL' ELSE (SELECT M.NAME FROM MEMTYPE M WHERE F.MEMTYPE=M.ID) END,F.DEBTMAX,F.DEACTIVATEDBY,F.DEACTIVATEDDATE,F.TYPE_,D.NAME "
                +" FROM FACILITY F,DEBTTYPE D WHERE D.ID=F.DUEPERIOD ORDER BY F.NAME"
              ,SerializerWriteString.INSTANCE
              ,new SerializerReadClass( FacilitytableModel.Facilityline.class )).list();
     }else{
          dlist= new StaticSentence(app.getSession()
                ,"SELECT F.ID,F.NAME,F.JOINAMT,CASE WHEN F.JOINAMT<1 THEN NULL ELSE (SELECT A.NAME FROM ACCOUNTMASTER A WHERE A.ID=F.JFEEACCOUNT) END,CASE WHEN F.RENEWALAMT<1 THEN NULL ELSE (SELECT A.NAME FROM ACCOUNTMASTER A WHERE A.ID=F.RENEWALACC) END,CASE WHEN F.USAGEAMT<1 THEN NULL ELSE (SELECT A.NAME FROM ACCOUNTMASTER A WHERE A.ID=F.USAGEACC) END,F.RENEWALAMT,CASE WHEN F.RPERIODICITY IS NOT NULL  THEN  (SELECT P.NAME FROM PERIODICITY P WHERE P.ID=F.RPERIODICITY) ELSE NULL END ,F.USAGEAMT,CASE WHEN F.UPERIODICITY IS NOT NULL  THEN  (SELECT P.NAME FROM PERIODICITY P WHERE P.ID=F.UPERIODICITY) ELSE NULL END,F.ENTRANCECONTROL,F.CREATEDBY,F.CRDATE,CASE WHEN F.MEMTYPE='ALL' THEN 'ALL' ELSE (SELECT M.NAME FROM MEMTYPE M WHERE F.MEMTYPE=M.ID) END,F.DEBTMAX,F.DEACTIVATEDBY,F.DEACTIVATEDDATE,F.TYPE_,D.NAME "
                +" FROM FACILITY F,DEBTTYPE D WHERE   D.ID=F.DUEPERIOD ORDER BY F.NAME"
              ,SerializerWriteString.INSTANCE
              ,new SerializerReadClass( FacilitytableModel.Facilityline.class )).list();
     }
     if(dlist==null)
     {
         d.fac=new ArrayList<Facilityline>();
     }
     else
     {
         d.fac=dlist;
     }

     return d;

  }
  
  public List<Facilityline> getfacilityline()
     {
         return fac;
     }
  public AbstractTableModel getTableModel() {
        return new AbstractTableModel() {
            @Override
            public String getColumnName(int column) {
                if(flag==1)
                return AppLocal.getIntString(FACILITYHEADERS[column]);
                else
                     return AppLocal.getIntString(FACILITYHEADERS1[column]);
            }
            public int getRowCount() {
                return fac.size();
            }
            public int getColumnCount() {
              if(flag==1)
                  return FACILITYHEADERS.length;
              else
                  return FACILITYHEADERS1.length;
            }
            public Object getValueAt(int row, int column) {
                Facilityline l = fac.get(row);

                switch (column) {
                  case 0 : return l.getname();
                  case 1: return l.getjamt();
                  case 2: return l.getJaccid();
                  case 3: return l.getramt();
                  case 4: return l.getrperiod();
                  case 5: return l.getRaccid();
                  case 6: return l.getuamt();
                  case 7: return l.getuperiod();
                  case 8: return l.getUaccid();
                  case 9: return l.getecheck();
                  case 10: return l.getDuePeriod();
                  case 11: return l.getMemType();
                  case 12: return l.getMaxDebt();
                  case 13: return l.getType();
                  case 14: return l.getcrby();
                  case 15: return l.getdate();
                  case 16: return l.getDeactivatedby();
                  case 17: return l.getDeactivatedDate();
                  
                  case 18: return l.getid();

                default: return null;
                }
            }
        };
    }
  


public static class Facilityline implements SerializableRead{
    private String id;
    private String name;
    private Double jamt;
    private Double ramt ;
    private Double uamt;
    private String rperiod;
    private String uperiod;
    private String createdby;
    private Timestamp crdate;
    private boolean echeck;
    private String deactivatedby;
    private Timestamp deactivateddate;
    private String memtype;
    private Double maxdebt;
    private String type;
    private String jaccid;
    private String raccid;
    private String uaccid;
    private String dueperiodname;
    
    public void readValues(DataRead dr) throws BasicException
    {

        id=dr.getString(1);
        name=dr.getString(2);
        jamt=dr.getDouble(3);
        jaccid=dr.getString(4);
        raccid=dr.getString(5);
        uaccid=dr.getString(6);
        ramt=dr.getDouble(7);
        rperiod=dr.getString(8);
        uamt=dr.getDouble(9);
        uperiod=dr.getString(10);
        echeck=dr.getBoolean(11);
        createdby=dr.getString(12);
        crdate=dr.getTimestamp(13);
        memtype=dr.getString(14);
        maxdebt=dr.getDouble(15);
        deactivatedby=dr.getString(16);
        deactivateddate=dr.getTimestamp(17);
        type=dr.getString(18);
        dueperiodname=dr.getString(19);
    }
    public String getDuePeriod(){
       return dueperiodname;
    }
    public String getType(){
      return type;
    }
    public String getJaccid(){
      return jaccid;
    }
     public String getRaccid(){
      return raccid;
    }
    public String getUaccid(){
      return uaccid;
    }
    public String getMemType(){
      return memtype;
    }
    public Double getMaxDebt(){
      return maxdebt;
    }

    public String getid() {
     return id;
    }
    public String getDeactivatedby(){
     return deactivatedby;
    }
    public Timestamp getDeactivatedDate(){
        return deactivateddate;
    }
    public boolean getecheck(){
       return echeck;
    }
    public String getcrby(){
        return createdby;
    }
    public Timestamp getdate(){
      return crdate;
    }
    public String getname(){
       return name;
    }
    public Double getjamt()
    {
        return jamt;
    }
    public Double getramt()
    {
        return ramt;
    }
    public Double getuamt()
    {
        
        return uamt;
    }
    public String getrperiod(){
      return rperiod;
    }
    public String getuperiod()
    {
        return uperiod;
    }
 }

}
