
package com.openbravo.pos.inventory;

import com.openbravo.data.loader.Session;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.Accounts.AccountMaster;
import com.openbravo.pos.Accounts.AccountMasterExt;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.sales.TaxesLogic;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author adrianromero
 */
public class ProductsEditor extends JPanel implements EditorRecord {
       
    private SentenceList m_sentcat;
    private SentenceList m_sentprcat;
    private ComboBoxValModel m_CategoryModel;
    private ComboBoxValModel m_prModel;

    private SentenceList taxcatsent;
    private ComboBoxValModel taxcatmodel;  
    
    private SentenceList taxsent;
    private TaxesLogic taxeslogic;
    
    private ComboBoxValModel m_CodetypeModel;
    
     private SentenceList m_sentunit;
     private ComboBoxValModel m_UnitModel;
     private ComboBoxValModel accountModel;
     private ComboBoxValModel saccountModel;//sales account model
    private Object m_id;
    private Object unittype;
    private boolean reportlock = false;
    private Session s;
    private DataLogicFacilities dlfac;
    private DataLogicSales m_dlSales;
    
    /** Creates new form JEditProduct */
    public ProductsEditor(DataLogicSales dlSales, DirtyManager dirty,AppView app) {
        initComponents();
         dlfac=(DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate") ;
         m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        // The taxes sentence
        taxsent = dlSales.getTaxList();
        // The categories model
        m_sentcat = dlSales.getCategoriesList();
        m_CategoryModel = new ComboBoxValModel();

        //print category model
        m_sentprcat = dlSales.getprCategoriesList();
        m_prModel = new ComboBoxValModel();
        
        // The taxes model
        taxcatsent = dlSales.getTaxCategoriesList();
        taxcatmodel = new ComboBoxValModel();

        m_sentunit=dlSales.getUnitList();
         m_UnitModel = new ComboBoxValModel();
        
        m_CodetypeModel = new ComboBoxValModel();
        m_CodetypeModel.add(null);
        m_CodetypeModel.add(CodeType.EAN13);
        m_CodetypeModel.add(CodeType.CODE128);
        m_jCodetype.setModel(m_CodetypeModel);
        m_jCodetype.setVisible(false);
        accountModel=new ComboBoxValModel();
        purchaseaccount.addActionListener(dirty);
        saccountModel=new ComboBoxValModel();
        salesaccount.addActionListener(dirty);
        m_jRef.getDocument().addDocumentListener(dirty);
        m_jCode.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jComment.addActionListener(dirty);
        m_jScale.addActionListener(dirty);
        m_jCategory.addActionListener(dirty);
        m_jprcategory.addActionListener(dirty);
        m_jTax.addActionListener(dirty);
        m_jPriceBuy.getDocument().addDocumentListener(dirty);
        m_jPriceSell.getDocument().addDocumentListener(dirty);
        m_jImage.addPropertyChangeListener("image", dirty);
        m_jstockcost.getDocument().addDocumentListener(dirty);
        m_jstockvolume.getDocument().addDocumentListener(dirty);
        m_jInCatalog.addActionListener(dirty);
        m_jCatalogOrder.getDocument().addDocumentListener(dirty);
        txtAttributes.getDocument().addDocumentListener(dirty);
        unitname.getDocument().addDocumentListener(dirty);
        unitlist.addActionListener(dirty);

        FieldsManager fm = new FieldsManager();
        m_jPriceBuy.getDocument().addDocumentListener(fm);
        m_jPriceSell.getDocument().addDocumentListener(fm);
        m_jTax.addActionListener(fm);
        
        m_jPriceSellTax.getDocument().addDocumentListener(new PriceTaxManager());
        m_jmargin.getDocument().addDocumentListener(new MarginManager());
      //  unittype=unitlist.getSelectedItem().toString();
        writeValueEOF();
    }
    
    public void activate() throws BasicException {
        
        // Load the taxes logic
        taxeslogic = new TaxesLogic(taxsent.list());        
        
        m_CategoryModel = new ComboBoxValModel(m_sentcat.list());
        m_prModel=new ComboBoxValModel(m_sentprcat.list());
        m_jCategory.setModel(m_CategoryModel);
        m_jprcategory.setModel(m_prModel);
        taxcatmodel = new ComboBoxValModel(taxcatsent.list());
        m_jTax.setModel(taxcatmodel);
        m_UnitModel=new ComboBoxValModel(m_sentunit.list());
        unitlist.setModel(m_UnitModel);
        List<AccountMasterExt> acclist=dlfac.getaccounts();
        List<AccountMasterExt> sacclist=new ArrayList<AccountMasterExt>();
        sacclist.addAll(acclist);
        accountModel=new ComboBoxValModel(acclist);
        purchaseaccount.setModel(accountModel);
        saccountModel=new ComboBoxValModel(sacclist);
        salesaccount.setModel(saccountModel);
      //    unittype=unitlist.getSelectedItem().toString();
      /*  AppView m_App=LookupUtilityImpl.getInstance(null).getAppView();
          List<PdtUnitInfo> prinfo=   new StaticSentence(m_App.getSession()
                        , "SELECT ID,NAME FROM UNIT "
                       ,null
                       ,new SerializerReadClass(PdtUnitInfo.class)).list();
           if(prinfo!=null)
        {
       String[] strings=new String[prinfo.size()];
      int i=0;
            for(PdtUnitInfo p: prinfo)
            {
                strings[i]=p.getName();
                i++;
            }
      unitlist.setModel(new javax.swing.DefaultComboBoxModel(strings));
           }*/

    }
    
    public void writeValueEOF() {
        
        reportlock = true;
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
        m_id = null;
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
        m_UnitModel.setSelectedKey(null);
        m_prModel.setSelectedKey(null);
        taxcatmodel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        m_jPriceSell.setText(null);            
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jInCatalog.setSelected(false);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        reportlock = false;
        unitname.setText(null);
        unitlist.setEnabled(false);
        unitname.setEnabled(false);
        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
        m_jprcategory.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);
        unittype=null;
        accountModel.setSelectedKey(null);
        purchaseaccount.setVisible(false);
        saccountModel.setSelectedKey(null);
        salesaccount.setVisible(false);
        calculateMargin();
        calculatePriceSellTax();
    }
    public void writeValueInsert() {
       
        reportlock = true;
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
        m_id = null;
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
        m_UnitModel.setSelectedKey(null);
        m_prModel.setSelectedKey(null);
        taxcatmodel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        m_jPriceSell.setText(null);            
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jInCatalog.setSelected(true);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        reportlock = false;
        
        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jprcategory.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true); 
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true); 
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(true);
         unitname.setText(null);
        unitlist.setEnabled(true);
        unitname.setEnabled(true);
        unittype=null;
        accountModel.setSelectedKey(null);
        purchaseaccount.setVisible(true);
        saccountModel.setSelectedKey(null);
        salesaccount.setVisible(true);
        calculateMargin();
        calculatePriceSellTax();
   }
    public void writeValueDelete(Object value) {
        
        reportlock = true;       
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]) + " " + AppLocal.getIntString("label.recorddeleted"));
        m_id = myprod[0];
        m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(myprod[7]));            
        m_CategoryModel.setSelectedKey(myprod[8]);

        taxcatmodel.setSelectedKey(myprod[9]);
        m_jImage.setImage((BufferedImage) myprod[10]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[11]));
        
         //  unittype=unitlist.getSelectedItem().toString();
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[12]));
        m_jInCatalog.setSelected(((Boolean)myprod[13]).booleanValue());
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[14]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[15]));
        m_prModel.setSelectedKey(myprod[16]);
        unittype=  m_UnitModel.getElementByKey(Formats.STRING.formatValue(myprod[17]));
         m_UnitModel.setSelectedKey(Formats.STRING.formatValue(myprod[17]));
     /*   try{
         if(myprod[17]!=null)
         {
           AppView m_App=LookupUtilityImpl.getInstance(null).getAppView();
         Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT NAME FROM UNIT WHERE ID = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(myprod[17].toString());
            if(obj1!=null && obj1[0]!=null)
         m_UnitModel.setSelectedKey(Formats.STRING.formatValue(obj1[0]));
         }
         }
         catch(Exception e)
         {
         }*/
         accountModel.setSelectedKey(myprod[18]);
        purchaseaccount.setVisible(false);
        saccountModel.setSelectedKey(myprod[19]);
        salesaccount.setVisible(false);
        txtAttributes.setCaretPosition(0);
        reportlock = false;
        
        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
        m_jprcategory.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);
        unitname.setText(null);
        unitname.setEnabled(false);
        unitname.setEnabled(false);
        unittype=null;
        calculateMargin();
        calculatePriceSellTax();
       
    }    
    private String unitkey;
    public void writeValueEdit(Object value) {
        
        reportlock = true;
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]));
        m_id = myprod[0];
        pid=myprod[0];
        m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(myprod[7]));            
        m_CategoryModel.setSelectedKey(myprod[8]);
        taxcatmodel.setSelectedKey(myprod[9]);
        m_jImage.setImage((BufferedImage) myprod[10]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[11]));
         unitkey=Formats.STRING.formatValue(myprod[17]);
       unittype=  m_UnitModel.getElementByKey(unitkey);
        
         m_UnitModel.setSelectedKey(unitkey);
        //  unittype=unitlist.getSelectedItem().toString();
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[12]));
        m_jInCatalog.setSelected(((Boolean)myprod[13]).booleanValue());
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[14]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[15]));
         m_prModel.setSelectedKey(myprod[16]);
     /*   try{
         if(myprod[17]!=null)
         {
           AppView m_App=LookupUtilityImpl.getInstance(null).getAppView();
         Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT NAME FROM UNIT WHERE ID = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(myprod[17].toString());
            if(obj1!=null && obj1[0]!=null)
         m_UnitModel.setSelectedKey(Formats.STRING.formatValue(obj1[0]));
         }
         }
         catch(Exception e)
         {
         }*/
         accountModel.setSelectedKey(myprod[18]);
        purchaseaccount.setVisible(true);
        saccountModel.setSelectedKey(myprod[19]);
        salesaccount.setVisible(true);
        txtAttributes.setCaretPosition(0);
        reportlock = false;
        
        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jprcategory.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true); 
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true);
        m_jCatalogOrder.setEnabled(m_jInCatalog.isSelected());  
        txtAttributes.setEnabled(true);
        unitname.setEnabled(true);
        unitlist.setEnabled(true);
         unitname.setText(null);
        calculateMargin();
        calculatePriceSellTax();
        
    }
  private Object pid;
    public Object createValue() throws BasicException {
        
        Object[] myprod = new Object[21];
       // pid=m_id == null ? UUID.randomUUID().toString() : m_id;
        myprod[0] =  m_id == null ? UUID.randomUUID().toString() : m_id;
        myprod[1] = m_jRef.getText();
        myprod[2] = m_jCode.getText();
        myprod[3] = m_jName.getText();
        myprod[4] = Boolean.valueOf(m_jComment.isSelected());
        myprod[5] = Boolean.valueOf(m_jScale.isSelected());
        myprod[6] = Formats.CURRENCY.parseValue(m_jPriceBuy.getText());
        myprod[7] = Formats.CURRENCY.parseValue(m_jPriceSell.getText());
        myprod[8] = m_CategoryModel.getSelectedKey();
        myprod[9] = taxcatmodel.getSelectedKey();
        myprod[10] = m_jImage.getImage();
        myprod[11] = Formats.CURRENCY.parseValue(m_jstockcost.getText());
        myprod[12] = Formats.DOUBLE.parseValue(m_jstockvolume.getText());
        myprod[13] = Boolean.valueOf(m_jInCatalog.isSelected());       
        myprod[14] = Formats.INT.parseValue(m_jCatalogOrder.getText()); 
        myprod[15] = Formats.BYTEA.parseValue(txtAttributes.getText());
        myprod[16] = m_prModel.getSelectedKey();
        int flag=0;
    String unitid="";
    final String unitid1;
        if(m_UnitModel.getSelectedKey()==null && unitname.getText()!=null)
        {
            flag=1;
            unitid1=UUID.randomUUID().toString();
             SentenceExec unitInsert = new PreparedSentence(LookupUtilityImpl.getInstance(null).getAppView().getSession()
                                   ,"INSERT INTO UNIT (ID, NAME) VALUES (?, ?)"
                                   ,SerializerWriteParams.INSTANCE);
                           unitInsert.exec(new DataParams() {
                               public void writeValues() throws BasicException {
                                   setString(1,unitid1);
                                   setString(2,unitname.getText() );
                               }
                           });
                           unitid=unitid1;


        }

           if(flag==0)
           {
                AppView m_App=LookupUtilityImpl.getInstance(null).getAppView();
         Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT ID FROM UNIT WHERE NAME = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(m_UnitModel.getSelectedItem().toString());
            if(obj1!=null && obj1[0]!=null)
               myprod[17]=obj1[0].toString();

           }
          else
          {
                myprod[17]=unitid;
                 m_UnitModel.setSelectedKey(Formats.STRING.formatValue(unitid));
                  unitname.setEnabled(true);
          }
           myprod[18]=accountModel.getSelectedKey();
           myprod[19]=saccountModel.getSelectedKey();
           String parentcat=String.valueOf(m_CategoryModel.getSelectedKey());
           String pcat=null;
           while(!parentcat.equals("null")){
              pcat=parentcat;
              parentcat=String.valueOf(m_dlSales.getParentCategory(pcat));
           }
           myprod[20]=pcat;
        return myprod;
    }    
    
    public Component getComponent() {
        return this;
    }
    
    private void calculateMargin() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dPriceSell = readCurrency(m_jPriceSell.getText());

            if (dPriceBuy == null || dPriceSell == null) {
                m_jmargin.setText(null);
            } else {
                m_jmargin.setText(Formats.PERCENT.formatValue(new Double(dPriceSell.doubleValue() / dPriceBuy.doubleValue() - 1.0)));
            }    
            reportlock = false;
        }
    }
    
    private void calculatePriceSellTax() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceSell = readCurrency(m_jPriceSell.getText());
            
            if (dPriceSell == null) {
                m_jPriceSellTax.setText(null);
            } else {               
                double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem());
                m_jPriceSellTax.setText(Formats.CURRENCY.formatValue(new Double(dPriceSell.doubleValue() * (1.0 + dTaxRate))));
            }            
            reportlock = false;
        }
    }
    
    private void calculatePriceSellfromMargin() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dMargin = readPercent(m_jmargin.getText());  
            
            if (dMargin == null || dPriceBuy == null) {
                m_jPriceSell.setText(null);
            } else {
                m_jPriceSell.setText(Formats.CURRENCY.formatValue(new Double(dPriceBuy.doubleValue() * (1.0 + dMargin.doubleValue()))));
            }
            reportlock = false;
        }
      
    }
    
    private void calculatePriceSellfromPST() {
        
        if (!reportlock) {
            reportlock = true;
                    
            Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());  

            if (dPriceSellTax == null) {
                m_jPriceSell.setText(null);
            } else {
                double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem()); 
                m_jPriceSell.setText(Formats.CURRENCY.formatValue(new Double(dPriceSellTax.doubleValue() / (1.0 + dTaxRate))));
            }
            reportlock = false;
        }    
    }

    private class FieldsManager implements DocumentListener, ActionListener {
        public void changedUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
        }
        public void insertUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
        }    
        public void removeUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
        }         
        public void actionPerformed(ActionEvent e) {
            calculateMargin();
            calculatePriceSellTax();
        }
    }

    private class PriceTaxManager implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
        }
        public void insertUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
        }    
        public void removeUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
        }         
    }
    
    private class MarginManager implements DocumentListener  {
        public void changedUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
        }
        public void insertUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
        }    
        public void removeUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
        }         
    }
    
    private final static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
        
    private final static Double readPercent(String sValue) {
        try {
            return (Double) Formats.PERCENT.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }

//    private static class MyListData extends javax.swing.AbstractListModel {
//        
//        private java.util.List m_data;
//        
//        public MyListData(java.util.List data) {
//            m_data = data;
//        }
//        
//        public Object getElementAt(int index) {
//            return m_data.get(index);
//        }
//        
//        public int getSize() {
//            return m_data.size();
//        } 
//    } 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        m_jRef = new javax.swing.JTextField();
        m_jName = new javax.swing.JTextField();
        m_jTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        m_jCode = new javax.swing.JTextField();
        m_jImage = new com.openbravo.data.gui.JImageEditor();
        jLabel3 = new javax.swing.JLabel();
        m_jPriceBuy = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jPriceSell = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        m_jTax = new javax.swing.JComboBox();
        m_jmargin = new javax.swing.JTextField();
        m_jPriceSellTax = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        m_jCodetype = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        m_jprcategory = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        m_jstockcost = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        m_jstockvolume = new javax.swing.JTextField();
        m_jScale = new javax.swing.JCheckBox();
        m_jComment = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        m_jCatalogOrder = new javax.swing.JTextField();
        m_jInCatalog = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        unitlist = new javax.swing.JComboBox();
        unitname = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        purchaseaccount = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        salesaccount = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAttributes = new javax.swing.JTextArea();

        setLayout(null);

        jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(10, 50, 80, 14);

        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(180, 50, 70, 14);
        add(m_jRef);
        m_jRef.setBounds(90, 50, 70, 20);
        add(m_jName);
        m_jName.setBounds(250, 50, 220, 20);

        m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18));
        add(m_jTitle);
        m_jTitle.setBounds(10, 10, 320, 30);

        jPanel1.setLayout(null);

        jLabel6.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
        jPanel1.add(jLabel6);
        jLabel6.setBounds(10, 20, 150, 14);
        jPanel1.add(m_jCode);
        m_jCode.setBounds(160, 20, 170, 20);
        jPanel1.add(m_jImage);
        m_jImage.setBounds(340, 20, 200, 180);

        jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 50, 150, 14);

        m_jPriceBuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceBuy);
        m_jPriceBuy.setBounds(160, 50, 80, 20);

        jLabel4.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 80, 150, 14);

        m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSell);
        m_jPriceSell.setBounds(160, 80, 80, 20);

        jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 170, 150, 14);
        jPanel1.add(m_jCategory);
        m_jCategory.setBounds(160, 170, 170, 20);

        jLabel7.setText(AppLocal.getIntString("label.taxcategory")); // NOI18N
        jPanel1.add(jLabel7);
        jLabel7.setBounds(10, 140, 150, 20);
        jPanel1.add(m_jTax);
        m_jTax.setBounds(160, 140, 170, 20);

        m_jmargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jmargin);
        m_jmargin.setBounds(250, 80, 80, 20);

        m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSellTax);
        m_jPriceSellTax.setBounds(160, 110, 80, 20);

        jLabel16.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N
        jPanel1.add(jLabel16);
        jLabel16.setBounds(10, 110, 150, 14);
        jPanel1.add(m_jCodetype);
        m_jCodetype.setBounds(250, 40, 80, 20);

        jLabel13.setText("Print Category");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(10, 200, 80, 14);

        jPanel1.add(m_jprcategory);
        m_jprcategory.setBounds(160, 200, 170, 20);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), jPanel1); // NOI18N

        jPanel2.setLayout(null);

        jLabel9.setText(AppLocal.getIntString("label.prodstockcost")); // NOI18N
        jPanel2.add(jLabel9);
        jLabel9.setBounds(10, 20, 150, 14);

        m_jstockcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockcost);
        m_jstockcost.setBounds(160, 20, 80, 20);

        jLabel10.setText(AppLocal.getIntString("label.prodstockvol")); // NOI18N
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 50, 150, 14);

        m_jstockvolume.setEditable(false);
        m_jstockvolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockvolume);
        m_jstockvolume.setBounds(160, 50, 80, 20);
        jPanel2.add(m_jScale);
        m_jScale.setBounds(160, 140, 80, 21);
        jPanel2.add(m_jComment);
        m_jComment.setBounds(160, 110, 80, 21);

        jLabel18.setText(AppLocal.getIntString("label.prodorder")); // NOI18N
        jPanel2.add(jLabel18);
        jLabel18.setBounds(250, 80, 60, 14);

        m_jCatalogOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jCatalogOrder);
        m_jCatalogOrder.setBounds(310, 80, 80, 20);

        m_jInCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jInCatalogActionPerformed(evt);
            }
        });
        jPanel2.add(m_jInCatalog);
        m_jInCatalog.setBounds(160, 80, 50, 21);

        jLabel8.setText(AppLocal.getIntString("label.prodincatalog")); // NOI18N
        jPanel2.add(jLabel8);
        jLabel8.setBounds(10, 80, 150, 14);

        jLabel11.setText(AppLocal.getIntString("label.prodaux")); // NOI18N
        jPanel2.add(jLabel11);
        jLabel11.setBounds(10, 110, 150, 14);

        jLabel12.setText(AppLocal.getIntString("label.prodscale")); // NOI18N
        jPanel2.add(jLabel12);
        jLabel12.setBounds(10, 140, 150, 14);

        jLabel14.setText("Unit :");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(250, 120, 50, 14);

        unitlist.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                unitlistItemStateChanged(evt);
            }
        });
        jPanel2.add(unitlist);
        unitlist.setBounds(310, 120, 120, 20);
        jPanel2.add(unitname);
        unitname.setBounds(310, 150, 120, 20);

        jLabel15.setText("Purchase Account");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(10, 190, 100, 14);

        jPanel2.add(purchaseaccount);
        purchaseaccount.setBounds(160, 190, 210, 20);

        jLabel17.setText("Sales Account");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(10, 220, 120, 14);

        jPanel2.add(salesaccount);
        salesaccount.setBounds(160, 220, 210, 20);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodstock"), jPanel2); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtAttributes.setFont(new java.awt.Font("DialogInput", 0, 12));
        jScrollPane1.setViewportView(txtAttributes);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(AppLocal.getIntString("label.attributes"), jPanel3); // NOI18N

        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 90, 560, 280);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jInCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jInCatalogActionPerformed
 
        if (m_jInCatalog.isSelected()) {
            m_jCatalogOrder.setEnabled(true);   
        } else {
            m_jCatalogOrder.setEnabled(false);   
            m_jCatalogOrder.setText(null);   
        }

    }//GEN-LAST:event_m_jInCatalogActionPerformed

    private void unitlistItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_unitlistItemStateChanged
        // TODO add your handling code here:
       Object sel=unitlist.getSelectedItem();
         if(unittype!=null&& sel !=null)
        {
            if(!unittype.toString().equals(sel.toString()) && m_jstockvolume.getText()!=null)
            {
               // if(m_jstockvolume!=null)
                try{
                  Object[] obj2=(Object[])   new StaticSentence(LookupUtilityImpl.getInstance(null).getAppView().getSession()
                        , "SELECT ID FROM QTITEMS WHERE PRODUCT = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(pid);



               if(obj2 !=null )
               {
                   JOptionPane.showMessageDialog(this, "Cannot change the unittype", "Error", JOptionPane.OK_OPTION);
                   m_UnitModel.setSelectedKey(unitkey);
                 //  unitlist.setSelectedIndex(unitkey);
               }
                   }
                catch(Exception e){
                e.printStackTrace();
                }
      // if(o!=null)
            }
        }
    }//GEN-LAST:event_unitlistItemStateChanged

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField m_jCatalogOrder;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JTextField m_jCode;
    private javax.swing.JComboBox m_jCodetype;
    private javax.swing.JCheckBox m_jComment;
    private com.openbravo.data.gui.JImageEditor m_jImage;
    private javax.swing.JCheckBox m_jInCatalog;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jPriceBuy;
    private javax.swing.JTextField m_jPriceSell;
    private javax.swing.JTextField m_jPriceSellTax;
    private javax.swing.JTextField m_jRef;
    private javax.swing.JCheckBox m_jScale;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JLabel m_jTitle;
    private javax.swing.JTextField m_jmargin;
    private javax.swing.JComboBox m_jprcategory;
    private javax.swing.JTextField m_jstockcost;
    private javax.swing.JTextField m_jstockvolume;
    private javax.swing.JComboBox purchaseaccount;
    private javax.swing.JComboBox salesaccount;
    private javax.swing.JTextArea txtAttributes;
    private javax.swing.JComboBox unitlist;
    private javax.swing.JTextField unitname;
    // End of variables declaration//GEN-END:variables
    
}
