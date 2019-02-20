/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.clubmang;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.pos.Accounts.AdditionalEntry;
import com.openbravo.pos.Accounts.PurchaseVoucherline;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.LookupUtilityImpl;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author swathi
 */
public class PurchaseJournalTable {
    private static String[] HEADERS={"Item","Quantity","Rate","Amount","Taxcat","Tax","Total Amount"};
    private static String[] HEADERS1={"Item","Amount"};
    private List<PurchaseVoucherline> pvoucher=new ArrayList<PurchaseVoucherline>();
    private List<AdditionalEntry> addtional=new ArrayList<AdditionalEntry>();
     public static PurchaseJournalTable loadInstance(){
          PurchaseJournalTable p=new PurchaseJournalTable();
          p.pvoucher=new ArrayList<PurchaseVoucherline>();
          p.addtional=new ArrayList<AdditionalEntry>();
          return p;
     }
     public List<PurchaseVoucherline> getlist(){
       return pvoucher;
     }
     public void setlist( List<PurchaseVoucherline> list){
       pvoucher.addAll(list);
     }
     public void setadditionalEntrylist( List<AdditionalEntry> list){
       addtional.addAll(list);
     }
      public List<AdditionalEntry> getadditionalEntrylist(){
       return addtional;
     }
public abstract class AbstractTableModelExt extends DefaultTableModel{
     public void setValueAt1(Object aValue, int row, int column) {

     }

}
public AbstractTableModel getTableModel() {
        return new AbstractTableModel()  {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class,java.lang.Double.class,java.lang.Double.class,java.lang.String.class, java.lang.Double.class,java.lang.Double.class
            };
            @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public String getColumnName(int column) {
                return HEADERS[column];
            }
            @Override
            public int getRowCount() {
                return pvoucher.size();
            }
            @Override
            public int getColumnCount() {

                return HEADERS.length;
            }
             boolean[] canEdit = new boolean [] {
                true, false, false, true, false,false,false
            };

           
           @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }

         //   @Override
         /*   public void setValueAt(Object aValue, int row, int column) {
                try{
                AppView m_App=LookupUtilityImpl.getInstance(null).getAppView();
                DataLogicFacilities dlfac=(DataLogicFacilities) m_App.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
                List acclist = dlfac.getsubAccounts(aValue.toString().toUpperCase());
                }catch(Exception e){

                }
            }*/
            @Override
           public void setValueAt(Object aValue, int row, int column) {
              if(row>=0){
               PurchaseVoucherline l = pvoucher.get(row);
                switch (column) {

                case 0:  l.setitem(aValue);
                          fireTableDataChanged();
                         break;
                case 1:  l.setQty(aValue);
                          fireTableDataChanged();
                         break;
                case 2:  l.setRate(aValue);
                          fireTableDataChanged();
                         break;
                case 3:  l.setamount(aValue);
                          fireTableDataChanged();
                         break;
                case 4:  l.setTaxcat(aValue);
                          fireTableDataChanged();
                         break;
                case 5:  l.setTax(aValue);
                          fireTableDataChanged();
                         break;
                case 6:  l.setTotalAmount(aValue);
                         fireTableDataChanged();
                         break;
               case 7:  l.setpdttaxvalue(aValue);
                         fireTableDataChanged();
                           break;
               case 8:  l.setAccount(String.valueOf(aValue));
                         fireTableDataChanged();
                         break;
               case 9:  l.setitemid(aValue);
                        fireTableDataChanged();
                          break;
                            case 10:  l.setTaxcatId(String.valueOf(aValue));
                        fireTableDataChanged();
                          break;
                default:  break ;
                }
              }
            }
            @Override
            public Object getValueAt(int row, int column) {
                PurchaseVoucherline l = pvoucher.get(row);

                switch (column) {

                case 0: return l.getitem();
                case 1: return l.getQty();
                case 2: return l.getRate();
                case 3: return l.getamount();
                case 4: return l.getTaxcat();
                case 5: return l.getTax();
                case 6:return l.getTotalAmount();
                case 7 :return l.getpdttaxvalue();
                case 8:return l.getAccount();
                case 9:return l.getitemid();
                default: return null;
                }
            }
        };
    }

public AbstractTableModel getTableModelforAdditionalEntries() {
        return new AbstractTableModel()  {
            Class[] types = new Class [] {
                java.lang.String.class,java.lang.Double.class
            };
            @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public String getColumnName(int column) {
                return HEADERS1[column];
            }
            @Override
            public int getRowCount() {
                return addtional.size();
            }
            @Override
            public int getColumnCount() {

                return HEADERS1.length;
            }
             boolean[] canEdit = new boolean [] {
                true ,false
            };
           @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            @Override
           public void setValueAt(Object aValue, int row, int column) {
               AdditionalEntry l = addtional.get(row);
                switch (column) {
                
                case 0:  l.setname(aValue);
                       //  fireTableCellUpdated(row, 0);
                         break;
                case 1:  l.setamount(aValue);
                         // fireTableCellUpdated(row, 1);
                         break;
                case 2:  l.setAccount(aValue);
                         break;
                default:  break ;
                }

            }
            @Override
            public Object getValueAt(int row, int column) {
                AdditionalEntry l = addtional.get(row);
                switch (column) {
               // case 0: return l.getAccount();
                case 0: return l.getname();
                case 1: return l.getamount();
                case 2: return l.getAccount();
                default: return null;
                }
            }
        };
    }

/*public static class AdditionalEntry implements SerializableRead{
       private String name;
       private double amount=0.0;
       private String account;
       private String parentskey;
       private String transtype;
       private String narration;
     /*   public AdditionalEntry(){
          account=new javax.swing.JComboBox();
          amount=0.0;
        }//
        public void readValues(DataRead dr) throws BasicException {
            name=dr.getString(1);
            amount=dr.getDouble(2);
            account=dr.getString(3);
            parentskey=dr.getString(4);
            transtype=dr.getString(5);
            narration=dr.getString(6);
        }
        public String getNarration(){
          return narration;
        }
        public String getTransType(){
          return transtype;
        }
        public String getParentSearchKey(){
           return parentskey;
        }
        public String getname(){
          return name;
        }
        public void setname(Object name){
         this.name=name.toString();
        }
        public double getamount(){
          return amount;
        }
        public void setamount(Object amt){
          amount=Double.parseDouble(amt.toString());
        }
        public String getAccount(){
          return account;
        }
        public void setAccount(Object acc){
          account=acc.toString();
        }

}*/
/*public static class PurchaseVoucherline implements SerializableRead{
    private String item;
    private int qty;
    private double rate=0;
    private double amount=0;
    private String taxcat;
    private double tax=0;
    private double totalAmount=0;
    private double pdttaxvalue=0;
    private String account;
    private String itemid;


    public void readValues(DataRead dr) throws BasicException
    {
       /* item=dr.getString(1);
        qty=dr.getInt(2);
        rate=dr.getDouble(3);
        amount=dr.getDouble(4);
        taxcat=dr.getString(5);
        tax=dr.getDouble(6);
        totalAmount=dr.getDouble(7);
        pdttaxvalue=dr.getDouble(8);
        account=dr.getString(9);
        itemid=dr.getString(10);//
        item=dr.getString(1);
        qty=dr.getInt(2);
        rate=dr.getDouble(3);
        amount=dr.getDouble(4);
        taxcat=null;
        tax=dr.getDouble(5);
        totalAmount=amount+tax;
        pdttaxvalue=tax/qty;
        account=dr.getString(6);
        itemid=dr.getString(7);
   }
    public String getAccount(){
      return account;
    }
    public void setAccount(String acc){
     account=acc;
    }
    public Double getpdttaxvalue(){
      return pdttaxvalue;
    }
    public void setpdttaxvalue(Object tax){
      pdttaxvalue=Double.valueOf(tax.toString());
    }
    public String getitem(){
      return item;
    }
    public String getitemid(){
      return itemid;
    }

    public int getQty(){
       return qty;
    }

    public void setitem( Object name){
        item=name.toString();
    }
    public void setitemid( Object id){
        itemid=id.toString();
    }
     public void setQty( Object Qty){
        qty=Integer.valueOf(Qty.toString());
    }

    public double getRate(){
      return rate;
    }
    public void setRate(Object Rate){
       rate=Double.valueOf(Rate.toString());
    }

    public double getamount(){
      return amount;
    }
    public void setamount(Object amt){
     amount=Double.valueOf(amt.toString());
    }
   public String getTaxcat(){
     return taxcat;
   }
   public void setTaxcat(Object taxcatname){
    taxcat=taxcatname.toString();
   }
   public double getTax(){
    return tax;
   }
    public void setTax(Object taxamt){
      tax=Double.valueOf(taxamt.toString());;
    }
    public double getTotalAmount(){
      return totalAmount;
    }
    public void setTotalAmount(Object amt){
     totalAmount=Double.valueOf(amt.toString());
    }
 }*/
}
