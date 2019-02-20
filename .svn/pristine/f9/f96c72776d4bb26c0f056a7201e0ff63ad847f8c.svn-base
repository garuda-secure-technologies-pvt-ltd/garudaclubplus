/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author swathi
 */
public class StockTransferTable {

    private List<Transferline> dis;
    private List<Transferline> dis1;
    private final static String[] DISCOUNTHEADERS = {"Trans No","Date","Product1","Quantity1","Unit Type1","Product2","Quantity2","Unit Type2","Created By","Received By"};
  private StockTransferTable()
   {
   }

  public static StockTransferTable emptyinstance()
  {
      StockTransferTable st=new StockTransferTable();
      st.dis=new ArrayList<Transferline>();
      st.dis1=new ArrayList<Transferline>();
      return st;
  }
  public static StockTransferTable loadInstance(AppView app) throws BasicException {
      StockTransferTable st = new StockTransferTable();
     List dlist = new StaticSentence(app.getSession()
                ,"SELECT P.NAME,U.NAME,S.DATENEW,S.UNITS,PDT.NAME,U1.NAME,S.UNITS1,S.NUM,S.CREATEDBY,RECEIVEDBY,S.LOCATION,S.LOCATION1,S.ID "
                +"FROM STOCKDIARY S,PRODUCTS P,PRODUCTS PDT,UNIT U,UNIT U1 WHERE S.PRODUCT=P.ID AND P.UNITTYPE=U.ID AND S.PRODUCT1=PDT.ID AND PDT.UNITTYPE=U1.ID AND NUM IS NOT NULL AND RECEIVEDBY IS NULL ORDER BY NUM "

                ,SerializerWriteString.INSTANCE
              ,new SerializerReadClass( StockTransferTable.Transferline.class )).list();
     if(dlist==null)
     {
         st.dis=new ArrayList<Transferline>();
     }
     else
     {
         st.dis=dlist;
     }

        List dlist1 = new StaticSentence(app.getSession()
                ,"SELECT P.NAME,U.NAME,S.DATENEW,S.UNITS,PDT.NAME,U1.NAME,S.UNITS1,S.NUM,S.CREATEDBY,RECEIVEDBY,S.LOCATION,S.LOCATION1,S.ID "
                +"FROM STOCKDIARY S,PRODUCTS P,PRODUCTS PDT,UNIT U,UNIT U1 WHERE P.ID=S.PRODUCT AND U.ID=P.UNITTYPE AND PDT.ID=S.PRODUCT1 AND U1.ID=PDT.UNITTYPE AND NUM IS NOT NULL AND RECEIVEDBY IS NOT NULL ORDER BY NUM "

                ,SerializerWriteString.INSTANCE
              ,new SerializerReadClass( StockTransferTable.Transferline.class )).list();
     if(dlist1==null)
     {
         st.dis1=new ArrayList<Transferline>();
     }
     else{
            st.dis1=dlist1;
            Date d=new Date();
            Timestamp today=new Timestamp(d.getTime());
            String[] tdate=today.toString().split(" ");
            for(int i=0;i<st.dis1.size();i++)
            {
            Timestamp pdtdate=st.dis1.get(i).getDate();
             String[] pdate=pdtdate.toString().split(" ");
             if(pdate[0].equals(tdate[0]))
             {
                 st.dis.add(st.dis1.get(i));
             }
            }
     }

     return st;

  }
  public List<Transferline> getdiscountline()
     {
         return dis;
     }
  public AbstractTableModel getdiscountTableModel() {
        return new AbstractTableModel() {
            @Override
            public String getColumnName(int column) {
               // return AppLocal.getIntString(DISCOUNTHEADERS[column]);
                //sanjeev commented above line and added below line
                 return DISCOUNTHEADERS[column];
            }
            public int getRowCount() {
                return dis.size();
            }
            public int getColumnCount() {

                return DISCOUNTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                Transferline l = dis.get(row);

                switch (column) {
                    case 0 : return l.getNum();
                    case 1: return l.getDate();

                    case 2 : return l.getproduct1();
                    case 3: return l.getqty1();
                    case 4: return l.getunit1();
                     case 5 : return l.getproduct2();
                    case 6: return l.getqty2();
                    case 7: return l.getunit2();
                    case 8: return l.getCreatedby();
                    case 9: return l.getreceivedby();
                    case 10:return l.getlocation();
                    case 11:return l.getlocation1();
                    case 12:return l.getid();
                default: return null;
                }
            }
        };
    }


public static class Transferline implements SerializableRead{
    private String product1;
    private String unittype1;
    private Double num;
    private Timestamp date;
    private Double qty1;
    private String product2;
    private String unittype2;
     private Double qty2;
     private String createdby;
     private String receivedby;
     private String location;
     private String location1;
     private String id;
    public void readValues(DataRead dr) throws BasicException
    {

        product1=dr.getString(1);
        unittype1=dr.getString(2);
       // num=dr.getDouble(3);
        date=dr.getTimestamp(3);
        qty1=dr.getDouble(4);
        product2=dr.getString(5);
        unittype2=dr.getString(6);
        qty2=dr.getDouble(7);
        num=dr.getDouble(8);
        createdby=dr.getString(9);
         receivedby=dr.getString(10);
         location=dr.getString(11);
         location1=dr.getString(12);
         id=dr.getString(13);

    }
    public String getid(){
        return id;
    }
    public String getlocation()
    {
        return location;
    }
     public String getlocation1()
    {
        return location1;
    }
    public String getreceivedby()
    {
        return receivedby;
    }
    public String getproduct1() {
     return product1;
    }

    public String getunit1()
    {
        return unittype1;
    }

    public Double getqty1()
    {
        return qty1;
    }
     public String getproduct2() {
     return product2;
    }

    public String getunit2()
    {
        return unittype2;
    }

    public Double getqty2()
    {
        return qty2;
    }
    public Double getNum()
    {
        return  num;
    }
    public Timestamp getDate()
    {
        return date;
    }
    public String getCreatedby()
    {
        return createdby;
    }

    }

}
