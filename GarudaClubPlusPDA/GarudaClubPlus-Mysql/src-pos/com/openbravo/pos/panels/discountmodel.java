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
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.ticket.ProductInfo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author swathi
 */
public class discountmodel {
   private List<discountline> dis;
    private final static String[] DISCOUNTHEADERS = {"label.Discountid","label.Discustomer","label.Disname","label.Disproduct","label.disqty","label.disrate","label.disamt","label.disreason","label.disauthorised"};
  private discountmodel()
   {
   }

  public static discountmodel emptyinstance()
  {
      discountmodel d=new discountmodel();
      d.dis=new ArrayList<discountline>();
      return d;
  }
  public static discountmodel loadInstance(AppView app) throws BasicException {
      discountmodel d = new discountmodel();
     List dlist = new StaticSentence(app.getSession()
                ,"SELECT D.QTITEMID,D.CUSTOMER_ID,D.USER_ID,D.PRODUCT_ID,D.QTY,D.RATE,D.AMOUNT,D.REASON,D.AUTHORISED,D.ID"
                +" FROM DISCOUNTLIST D WHERE D.AUTHORISED IS NULL "

                ,SerializerWriteString.INSTANCE
              ,new SerializerReadClass( discountmodel.discountline.class )).list();
     if(dlist==null)
     {
         d.dis=new ArrayList<discountline>();
     }
     else
     {
         d.dis=dlist;
     }

     return d;

  }
  public List<discountline> getdiscountline()
     {
         return dis;
     }
  public AbstractTableModel getdiscountTableModel() {
        return new AbstractTableModel() {
            @Override
            public String getColumnName(int column) {
                return AppLocal.getIntString(DISCOUNTHEADERS[column]);
            }
            public int getRowCount() {
                return dis.size();
            }
            public int getColumnCount() {

                return DISCOUNTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                discountline l = dis.get(row);

                switch (column) {

                    case 0 : return l.getid();
                case 1: return l.getcustomername();
                case 2: return l.getuserid();
                case 3: return l.getproductName();
                case 4:
                       return l.getquantity();
                case 5 : return l.getrate();
                case 6 : return l.getamount();
                case 7: return l.getreason();
                case 8: return l.getauthorised();
                case 9 :return l.getqid();
                case 10 :return l.getproductid();
                default: return null;
                }
            }
        };
    }


public static class discountline implements SerializableRead{
    private String customername;
    private String userid;
    private String productid;
    private int qty;
    private Double rate;
    private Double amount;
    private String reason;
    private String authorised;
    private String id;
    private String qid;
    public void readValues(DataRead dr) throws BasicException
    {

        id=dr.getString(1);
        customername=dr.getString(2);
        userid=dr.getString(3);
        productid=dr.getString(4);
        qty=dr.getInt(5);
        rate=dr.getDouble(6);
        amount=dr.getDouble(7);
        reason=dr.getString(8);
        authorised=dr.getString(9);
        qid=dr.getString(10);
    }
    public String getid() {
     return id;
    }
    public String getqid(){
     return qid;
    }

    public String getcustomername()
    {
        return customername;
    }
    public String getuserid()
    {
        return userid;
    }
    public String getproductName()
    {
          ProductInfo pInfo = LookupUtilityImpl.getInstance(null).getProductsMap().get(productid);
        return pInfo.getName();
    }
     public String getproductid()
    {

        return productid;
    }
    public int getquantity()
    {
        return qty;
    }
    public Double getrate()
    {
        return rate;
    }
    public Double getamount()
    {
        return amount;
    }
    public String getreason()
    {
        return reason;
    }
    public String getauthorised()
    {
        return authorised;
    }
     public String printcustomername()
    {
        return customername;
    }
    public String printuserid()
    {
        return userid;
    }
    public String printproductid()
    {
        return productid;
    }
    public String printquantity()
    {
        return Formats.INT.formatValue(qty);
    }
    public String printrate()
    {
        return Formats.CURRENCY.formatValue(rate);
    }
    public String printamount()
    {
        return Formats.CURRENCY.formatValue(amount);
    }
    public String printreason()
    {
        return reason;
    }
    public String printauthorised()
    {
        return authorised;
    }

}
}
