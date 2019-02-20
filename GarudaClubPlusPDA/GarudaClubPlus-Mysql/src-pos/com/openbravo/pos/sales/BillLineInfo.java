/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.sales;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.ticket.ProductInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
//import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;
/**
 *
 * @author swathi
 */
public class BillLineInfo implements SerializableWrite, SerializableRead, Serializable{

    private String m_id;
    private String m_parentid;
    private ProductInfo m_product;
    private int m_multiply;
    private double m_rate;
    private double m_amount;
    private int m_iLine;
    private Properties attributes = new Properties();
    private transient TaxInfo tax;
    private boolean disStatus=false;;
    public BillLineInfo(String id, String parentid, ProductInfo product, int multiply, double rate,double amount) {
        m_id=id;
        m_parentid = parentid;
        setProduct(product);
        m_multiply = multiply;
        m_rate = rate;
        m_iLine = -1;
        m_amount = amount;
       }//or init???

    public BillLineInfo() {
        m_id = UUID.randomUUID().toString();
        disStatus=false;
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_id);
        dp.setInt(2, m_iLine);
        dp.setString(3, m_parentid);
        dp.setString(4, m_product.getID());
        dp.setInt(5, m_multiply);
        dp.setDouble(6, m_rate);
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            attributes.storeToXML(o, AppLocal.APP_NAME, "UTF-8");
            dp.setBytes(7, o.toByteArray());
        } catch (IOException e) {
            dp.setBytes(7, null);
        }
        dp.setDouble(8, m_amount);
    }

    public void readValues(DataRead dr) throws BasicException {
        m_id = dr.getString(1);
         m_iLine = dr.getInt(2);
         m_parentid = dr.getString(3);
         m_product = LookupUtilityImpl.getInstance(null).getProductsMap().get(dr.getString(4));
         m_multiply = dr.getInt(5);
         m_rate = dr.getDouble(6);
        attributes = new Properties();
        try {
            byte[] img = dr.getBytes(7);
            if (img != null) {
                attributes.loadFromXML(new ByteArrayInputStream(img));
            }
            m_amount=dr.getDouble(8);
        } catch (IOException e) {
        }
    }

    public BillLineInfo copybill() {
        BillLineInfo t = new BillLineInfo();
        t.m_id = m_id;
        t.m_iLine = m_iLine;
        t.m_parentid = m_parentid;
        t.m_product = m_product;
        t.m_multiply = m_multiply;
        t.m_rate = m_rate;
        t.attributes = (Properties) attributes.clone();
        t.m_amount=m_amount;
        return t;
    }
   public boolean getDiscountStatus(){
     return disStatus;
   }
   public void setDiscountStatus(boolean status){
     disStatus=status;
   }
    public void setAmount(Double amt)
    {
        m_amount=amt;
    }
    public double getamount()
    {
       return m_amount;
    }
    public String getID() {
        return m_id;
    }

    public void setID(String value) {
        m_id = value;
    }
    public String getParentid() {
        return m_parentid;
    }

    public void setParentid(String value) {
        m_parentid = value;
    }

    private String getProductID() {
        return m_product.getID();
    }

    public ProductInfo getProduct() {
        return m_product;
    }

   

    private void refreshAttributes() {
        attributes.setProperty("product.name", m_product.getName());
        attributes.setProperty("product.com", m_product.isCom() ? "true" : "false");
        attributes.setProperty("product.taxcategoryid", m_product.getTaxCategoryID());
        if (m_product.getCategoryID() != null) {
            attributes.setProperty("product.categoryid", m_product.getCategoryID());
        }
    }

    public void setProduct(ProductInfo value) {
        m_product = value;
        refreshAttributes();
    }

    public int getMultiply() {
        return m_multiply;
    }

    public void setMultiply(int value){
        m_multiply = value;
    }

    public double getRate() {
        return m_rate;
    }

    public void setRate(double value) {
        m_rate = value;
    }

    void setTicket(BillInfo parentid, int line) {
        m_parentid = parentid.getID();
        m_iLine = line;
        if (tax == null) {
            tax = LookupUtilityImpl.getInstance(null).getTaxInfo(getProduct().getTaxCategoryInfo(), parentid.getCustomer());
        }
    }

    public TaxInfo getTaxInfo() {
        return tax;
    }

    public double getSubValue() {
      //  return m_rate * m_multiply;
        return m_amount;
    }

    public double getTaxRate() {
        return tax == null ? 0.0 : tax.getRate();
    }

    public double getTax() {
        return m_amount * getTaxRate();
    }

    public String getProperty(String key) {
        return attributes.getProperty(key);
    }

    public String getProperty(String key, String defaultvalue) {
        return attributes.getProperty(key, defaultvalue);
    }

    public void setProperty(String key, String value) {
        attributes.setProperty(key, value);
    }

    public Properties getProperties() {
        return attributes;
    }

    public double getValue() {
        return m_rate * (1.0 + getTaxRate());
    }



    //For printing
    public String getProductName() {
        return attributes.getProperty("product.name");
    }

    public boolean isProductCom() {
        return "true".equals(attributes.getProperty("product.com"));
    }

    public String getProductTaxCategoryID() {
        return (attributes.getProperty("product.taxcategoryid"));
    }

    public String getProductCategoryID() {
        return (attributes.getProperty("product.categoryid"));
    }
    public String printName() {
        if(attributes.getProperty("product.name")==null)
            return StringUtils.encodeXML(m_product.getName());
        else
         return StringUtils.encodeXML(attributes.getProperty("product.name"));
    }

    public String printMultiply() {
        return Formats.DOUBLE.formatValue(getMultiply());
    }

    public String printPrice() {
        return Formats.CURRENCY.formatValue(getRate());
    }

    public String printPriceTax() {
        return Formats.CURRENCY.formatValue(getTaxRate());
    }

    public String printTax() {
        return Formats.CURRENCY.formatValue(getTax());
    }

    public String printTaxRate() {
        return Formats.PERCENT.formatValue(getTaxRate());
    }

    public String printSubValue() {
        return Formats.CURRENCY.formatValue(getSubValue());
    }

    public String printAmount() {
        return Formats.CURRENCY.formatValue(getamount());
    }

    public String printValue() {
        return Formats.CURRENCY.formatValue(getValue());
    }

 /*   public String printBilledQty(){

        double temp=getamount()/getRate();
        if(((int)temp)==getMultiply() || (getamount()%getRate())!=0.0)
            return "";
        else
        return " B("+Formats.DOUBLE.formatValue(temp)+")";
    }
    public Double getBilledQty(){
        double temp=getamount()/getRate();
        return temp;
    }

    public String printBilledPrice(){
        double temp=getamount()/getMultiply();
        if((temp)==getRate() || (getamount()%getMultiply())!=0.0)
            return "";
        else
        return " B("+Formats.DOUBLE.formatValue(temp)+")";
    }
    public Double getBilledPrice(){
        double temp=getamount()/getMultiply();
        return temp;
    }*/

}
