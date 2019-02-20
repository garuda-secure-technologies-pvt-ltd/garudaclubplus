

package com.openbravo.pos.ticket;

import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.basic.BasicException;
import com.openbravo.format.Formats;
import com.openbravo.pos.inventory.TaxCategoryInfo;

public class ProductInfo implements SerializableRead /* , Externalizable, SerializableWrite */ {
    
    protected String m_ID;
    protected String m_sRef;
    protected String m_sCode;
    protected String m_sName;
    protected boolean m_bCom;
    protected boolean m_bScale;
    protected String m_sCategoryID;
    
    protected TaxCategoryInfo taxcategory;
    protected double m_dPriceBuy;
    protected double m_dPriceSell;
    protected String m_sprcategory;
    protected String m_sunittype;
    protected Double stockvolume;
    protected String account;
    protected String saccount;
    
    /** Creates new ProductInfo */
    public ProductInfo() {
        m_ID = null;
        m_sRef = "0000";
        m_sCode = "0000";
        m_sName = null;
        m_bCom = false;
        m_bScale = false;
        m_sCategoryID = null;
        taxcategory = null;
        m_dPriceBuy = 0.0;
        m_dPriceSell = 0.0;
        stockvolume=0.0;
        account=null;
        saccount=null;
      //  m_sunittype=null;
    }
    
    public void readValues(DataRead dr) throws BasicException {
        m_ID = dr.getString(1);
        m_sRef = dr.getString(2);
        m_sCode = dr.getString(3);
        m_sName = dr.getString(4);
        m_bCom = dr.getBoolean(5).booleanValue();
        m_bScale = dr.getBoolean(6).booleanValue();
        m_dPriceBuy = dr.getDouble(7).doubleValue();
        m_dPriceSell = dr.getDouble(8).doubleValue();
        taxcategory = new TaxCategoryInfo(dr.getString(9), dr.getString(10));    
        m_sCategoryID = dr.getString(11);

    }
   
    public final String getID() {
        return m_ID;
    }
    
    public final void setID(String id) {
        m_ID = id;
    }
    
    public final String getReference(){
        return m_sRef;
    }
    public final void setReference(String sRef){
        m_sRef = sRef;
    }    
    public final String getCode(){
        return m_sCode;
    }
    public final void setCode(String sCode){
        m_sCode = sCode;
    }
    public final String getName() {            
        return m_sName;
    }
    public final void setName(String sName){            
        m_sName = sName;
    }
    public final boolean isCom() {            
        return m_bCom;
    }
    public final void setCom(boolean bValue){            
        m_bCom = bValue;
    }
    public final boolean isScale() {            
        return m_bScale;
    }
    public final void setScale(boolean bValue){            
        m_bScale = bValue;
    }
    public final String getCategoryID() {
        return m_sCategoryID;
    }
    public final void setCategoryID(String sCategoryID) {
        m_sCategoryID = sCategoryID;
    }
    public final String getPRCategory() {
        return m_sprcategory;
    }
    public final void setUnitType(String unittype) {
        m_sunittype = unittype;
    }
     public final String getStockvolume() {
         if(stockvolume==null)
             return "0";
         else
        return stockvolume.toString();
     }
     public final String getAccount(){
       return account;
     }
     public final void setAccount(String acc){
       account=acc;
     }
     public final String getsAccount(){
       return saccount;
     }
     public final void setsAccount(String acc){
        saccount=acc;
     }
    public final void setStockvolume(String stockvolume) {
        this.stockvolume = Double.parseDouble(stockvolume);
    }
     public final String getUnitType() {
        return m_sunittype;
    }
    public final void setPRCategory(String sprcategory) {
        m_sprcategory = sprcategory;
    }
    public final void setTaxCategoryInfo(TaxCategoryInfo taxcat) {
        taxcategory = taxcat;
    }
    public final TaxCategoryInfo getTaxCategoryInfo() {
        return taxcategory;
    }
    public final String getTaxCategoryID() {
        return taxcategory == null ? null : taxcategory.getID();
    }   
    public final String getTaxCategoryName() {
        return taxcategory == null ? null : taxcategory.getName();
    }
//    public final double getTaxRate() {
//        return m_TaxInfo == null ? 0.0 : m_TaxInfo.getRate();
//    }
    public final double getPriceBuy(){
        return m_dPriceBuy;
    }    
    public final void setPriceBuy(double dPrice) {
        m_dPriceBuy = dPrice;
    }        
    public final double getPriceSell(){        
        return m_dPriceSell;
    }
    public final void setPriceSell(double dPrice) {        
        m_dPriceSell = dPrice;
    }      
    public final double getPriceSellTax(TaxInfo tax) {
        return m_dPriceSell * (1.0 + tax.getRate());
    }    
    public String printPriceSell() {
        return Formats.CURRENCY.formatValue(new Double(getPriceSell()));
    }    
    public String printPriceSellTax(TaxInfo tax) {
        return Formats.CURRENCY.formatValue(new Double(getPriceSellTax(tax)));
    }     
    
    @Override
    public final String toString() {
        return m_sRef + " - " + m_sName;
    }
}