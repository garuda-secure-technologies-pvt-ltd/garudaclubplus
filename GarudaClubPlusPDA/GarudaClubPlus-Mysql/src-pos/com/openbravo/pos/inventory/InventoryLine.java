

package com.openbravo.pos.inventory;

import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.ticket.ProductInfoExt;

/**
 *
 * @author adrianromero
 */
public class InventoryLine {
    
    private double m_dMultiply;    
    private double m_dPrice;
    
    private String m_sProdID;
    private String m_sProdName;
    private String unittype;
    private String stockvolume;
    private int index;
 
    /** Creates a new instance of InventoryLine */
    public InventoryLine()
    {
         m_sProdID ="";
        m_sProdName = "";
        m_dMultiply = 0.0;
        m_dPrice = 0.0;
        unittype="";
        stockvolume="";
    }

    public InventoryLine(InventoryLine il)
    {
         m_sProdID =il.getProductID();
        m_sProdName = il.getProductName();
        m_dMultiply = il.getMultiply();
        m_dPrice = 0.0;
        unittype=il.getUnittype();
        stockvolume=il.getstockvl();
    }
    public InventoryLine(ProductInfoExt oProduct) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = 1.0;
        m_dPrice = 0.0;
        unittype=oProduct.getUnitType();
        stockvolume=oProduct.getStockvolume();
    }
    
    public InventoryLine(ProductInfoExt oProduct, double dpor, Double dprice,int index) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = dpor;
        m_dPrice = 0.0;
       this.unittype=oProduct.getUnitType();
       stockvolume=oProduct.getStockvolume();
       this.index=index;
    }
    public int getIndex(){
        return index;
    }
    public String getstockvl()
    {
        return stockvolume;
    }
    public String getProductID() {
        return m_sProdID;
    }    
    
    public String getProductName() {
        return m_sProdName;
    } 
    public void setProductName(String sValue) {
        if (m_sProdID == null) {
            m_sProdName = sValue;
        }
    }
    public double getMultiply() {
        return m_dMultiply;
    }
    
    public void setMultiply(double dValue) {
        m_dMultiply = dValue;
    }
    
    public double getPrice() {
        return m_dPrice;
    }
    
    public void setPrice(double dValue) {
        m_dPrice = dValue;
    }    
    
    public double getSubValue() {
        return m_dMultiply * m_dPrice;
    }
    
    public String printName() {
        return m_sProdName;
    }
    public String getUnittype()
    {
        try{
         Object[] obj1=(Object[]) new StaticSentence(LookupUtilityImpl.getInstance(null).getAppView().getSession(),
                "SELECT NAME FROM UNIT WHERE ID = ? "
                , SerializerWriteString.INSTANCE
                ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(unittype);
         if(obj1[0]!=null)
        return obj1[0].toString();
         else
             return " ";
        }
        catch(Exception e)
        {
            return "";
        }
    }

    public String printPrice() {
        if (m_dMultiply == 1.0) {
            return "";
        } else {
            return Formats.CURRENCY.formatValue(new Double(getPrice()));
        }
    }
    
    public String printMultiply() {
        return Formats.DOUBLE.formatValue(new Double(m_dMultiply));
    }
    
    public String printSubValue() {
        return Formats.CURRENCY.formatValue(new Double(getSubValue()));
    }
    
}
