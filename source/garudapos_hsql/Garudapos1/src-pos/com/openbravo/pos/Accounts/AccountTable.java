/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.Accounts;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author swathi
 */
public class AccountTable {
      private  List<AccountLine> acc;
    private final static String[] HEADERS = {"Name","Description","Search key","Level","Parent","Opening Bal","Date of O Bal"};
  public AccountTable()
  {
  }

  public static AccountTable emptyinstance()
  {
      AccountTable d=new AccountTable();
      d.acc=new ArrayList<AccountLine>();
      return d;
  }
  public static AccountTable loadInstance(AppView app,String searchkey) throws BasicException{
      AccountTable d = new AccountTable();
      d.acc=new ArrayList<AccountLine>();
      Session s=app.getSession();
       List<AccountLine>   acclist2= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE FROM ACCOUNTMASTER A WHERE  A.LEVEL='S' AND A.PARENT= ?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(searchkey);
               List<AccountLine> headinglist= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='D' AND A.PARENT=?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(searchkey);
               d.acc.addAll(acclist2);
             for(int j=0;j<headinglist.size();j++){
                 AccountLine acch=headinglist.get(j);
                List<AccountLine> acclist1= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='S' AND A.PARENT= ?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(acch.getSearchkey());
               List<AccountLine> headinglist1= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='D' AND A.PARENT=?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(acch.getSearchkey());
               d.acc.add(acch);
               d.acc.addAll(acclist1);
               headinglist.addAll(headinglist1);
             }
    
     return d;
  }

    public static AccountTable loadInstanceName(AppView app,String searchkey) throws BasicException{
      AccountTable d = new AccountTable();
      d.acc=new ArrayList<AccountLine>();
      Session s=app.getSession();
       List<AccountLine>   acclist2= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE FROM ACCOUNTMASTER A WHERE  A.LEVEL='S' AND A.PARENT= ?  ORDER BY A.NAME"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(searchkey);
               List<AccountLine> headinglist= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='D' AND A.PARENT=?  ORDER BY A.NAME"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(searchkey);
               d.acc.addAll(acclist2);
             for(int j=0;j<headinglist.size();j++){
                 AccountLine acch=headinglist.get(j);
                List<AccountLine> acclist1= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='S' AND A.PARENT= ?  ORDER BY A.NAME"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(acch.getSearchkey());
               List<AccountLine> headinglist1= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='D' AND A.PARENT=?  ORDER BY A.NAME"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(acch.getSearchkey());
               d.acc.add(acch);
               d.acc.addAll(acclist1);
               headinglist.addAll(headinglist1);
             }

     return d;
  }
    
   public static AccountTable loadInstanceM(AppView app,String searchkey) throws BasicException{
      AccountTable d = new AccountTable();
      d.acc=new ArrayList<AccountLine>();
      Session s=app.getSession();
        List<AccountLine>   acclist3= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE FROM ACCOUNTMASTER A WHERE  A.LEVEL='C' AND A.PARENT= ?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(searchkey);
        for(int i=0;i<acclist3.size();i++){
            AccountLine acc1=acclist3.get(i);
              List<AccountLine>   acclist2= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE FROM ACCOUNTMASTER A WHERE  A.LEVEL='S' AND A.PARENT= ?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(acc1.getSearchkey());
               List<AccountLine> headinglist= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='D' AND A.PARENT=?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(acc1.getSearchkey());
               d.acc.add(acc1);
               d.acc.addAll(acclist2);
             for(int j=0;j<headinglist.size();j++){
                 AccountLine acch=headinglist.get(j);
                List<AccountLine> acclist1= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='S' AND A.PARENT= ?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(acch.getSearchkey());
               List<AccountLine> headinglist1= new StaticSentence(s
                           , "SELECT A.NAME,A.DESCRIPTION,A.SEARCHKEY,A.LEVEL,A.PARENT,A.OPENINGBALANCE,DATEOFOPENINGBAL,A.ID,A.EDITABLE  FROM ACCOUNTMASTER A WHERE  A.LEVEL='D' AND A.PARENT=?  ORDER BY A.SEARCHKEY"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountLine.class)).list(acch.getSearchkey());
               d.acc.add(acch);
               d.acc.addAll(acclist1);
               headinglist.addAll(headinglist1);
             }
        }
     return d;
  }

  public List<AccountLine> getAccountline()
     {
         return acc;
     }
  public DefaultTableModel getTableModel() {
        return new DefaultTableModel() {


            @Override
            public String getColumnName(int column) {
                return AppLocal.getIntString(HEADERS[column]);
            }
            public int getRowCount() {
                return acc.size();
            }
            public int getColumnCount() {

                return HEADERS.length;
            }
             boolean[] canEdit = new boolean [] {
                false, false, false, false, false,false,false,false
            };


           @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            public Object getValueAt(int row, int column) {
                AccountLine l = acc.get(row);

                switch (column) {
                case 0: return l.getname();
                case 1: return l.getdesc();
                case 2: return l.getSearchkey();
                case 3: return l.getLevel();
                case 4: return l.getparent();
                case 5: return l.getOpeningBal();
                case 6: return l.getobdate();
                case 7: return l.getid();
               
               
                default: return null;
                }
            }
        };
    }
public static class AccountLine implements SerializableRead,IKeyed{
    private String id;
    private String name;
   private String level;
    private String searchkey;
    private String parent;
    private Double openingbalance;
    private Timestamp dateofopeningbal;
    private String description;
    private boolean editable;


    public void readValues(DataRead dr) throws BasicException
    {

        id=dr.getString(8);
        name=dr.getString(1);
        description=dr.getString(2);
        searchkey=dr.getString(3);
        level=dr.getString(4);
        parent=dr.getString(5);
        openingbalance=dr.getDouble(6);
        dateofopeningbal=dr.getTimestamp(7);
        editable=dr.getBoolean(9);
        
   }
    public String getdesc(){
      return description;
    }
    public String getSearchkey(){
      return searchkey;
    }
     public String getLevel(){
      return level;
    }
    public String getparent(){
      return parent;
    }
    public Double getOpeningBal(){
      return openingbalance;
    }
    public String getid() {
     return id;
    }
   public String getname(){
     return name;
   }
    public Timestamp getobdate(){
        return dateofopeningbal;
    }
    public boolean isEditable(){
      return editable;
    }
  
    public Object getKey() {
            return id;
        }
    @Override
    public String toString(){
       return name;
    }

 }

}
