/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PurchaseJournal.java
 *
 * Created on Apr 7, 2009, 4:52:00 PM
 */

package com.openbravo.pos.clubmang;

import com.openbravo.pos.Accounts.AccountMaster;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.Accounts.AdditionalEntry;
import com.openbravo.pos.Accounts.PurchaseVoucherline;
import com.openbravo.pos.catalog.CatalogSelector;
import com.openbravo.pos.catalog.JCatalog;
import com.openbravo.pos.catalog.JCatalog1;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.LookupUtilityImpl;
//import com.openbravo.pos.sales.JPanelButtons;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
//import java.awt.Container;
import java.awt.Dimension;
//import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
//import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
//import java.awt.event.InputMethodEvent;
//import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
//import javax.swing.event.CellEditorListener;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.TableModelEvent;
//import javax.swing.event.TableModelListener;
//import javax.swing.table.AbstractTableModel;
//import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
//import javax.swing.table.TableModel;

/**
 *
 * @author swathi
 */
public  class PurchaseJournal extends javax.swing.JPanel implements JPanelView,BeanFactoryApp,KeyListener
{
     private CatalogSelector m_cat;
     private DataLogicSales m_dlSales;
     private ComboBoxValModel cModel;
     private ComboBoxValModel vModel;
     private ComboBoxValModel cashaccModel;
     private PurchaseJournalTable ptableModel;
     private int mode;//0=new creation mode,1=edit mode
     private List<PurchaseVoucherline> oldplist;
     private List<AdditionalEntry> oldalist;
     private TaxesLogic taxeslogic;
     private Component component;
     private DataLogicFacilities dlfac;
     private KeyboardFocusManager kfm ;
     private String celldata=null;
     private AppView m_App;
     private String oldID;
     private Date oldDate;
     private String oldTransno;
     private Object[] oldValues;
     private JDialog dialog;
    public PurchaseJournal() {
        initComponents();

    }

     public void init(AppView app) throws BeanFactoryException {
         m_App=app;
         m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
         dlfac=(DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
         m_cat = new JCatalog(m_dlSales);
         m_cat.getComponent().setPreferredSize(new Dimension(0, 245));
         m_cat.addActionListener(new CatalogListener());
         component=m_cat.getComponent();
         jPanel2.add(component, BorderLayout.CENTER);
         addKeyListener(this);
         mode=0;
         oldID=null;
     //   this.addKeyListener(new KeyAdapter() {
       //     @Override
       //  public void keyReleased(java.awt.event.KeyEvent evt) {
               // formkeyReleased(evt);
        //    }});
    }
    
    public String getTitle() {
       return null;
    }
    public void keyPressed(java.awt.event.KeyEvent evt) {
               // formkeyReleased(evt);
    }
    public void keyTyped(java.awt.event.KeyEvent evt) {
               // formkeyReleased(evt);
    }
    public void keyReleased(java.awt.event.KeyEvent evt) {
         if(evt.getKeyCode()==KeyEvent.VK_F1){
               Date datenow;
        try {
            datenow = (Date) Formats.TIMESTAMP.parseValue(date.getText());
        } catch (BasicException e1) {
            datenow = null;
        }
        datenow = JCalendarDialog.showCalendar(this, datenow);
        if (datenow != null) {
            date.setText(Formats.TIMESTAMP.formatValue(datenow));
        }
         }
     }
     private void reset() throws BasicException{
          ptableModel=PurchaseJournalTable.loadInstance();
         // plist=ptableModel.getlist();
          PurchaseVoucherline pelement=new PurchaseVoucherline();
          ptableModel.getlist().add(pelement);
          jPanel1.setVisible(false);
          jTable1.setModel(ptableModel.getTableModel());
          Date d=new Date();
          Calendar cal=Calendar.getInstance();
          cal.setTime(d);
          cal.set(Calendar.HOUR_OF_DAY,24);
          cal.set(Calendar.MINUTE,59);
          cal.set(Calendar.SECOND, 59);
          cal.set(Calendar.MILLISECOND, 59);
          Calendar scal=Calendar.getInstance();
          scal.setTime(d);
          scal.set(Calendar.HOUR_OF_DAY,00);
          scal.set(Calendar.MINUTE,00);
          scal.set(Calendar.SECOND, 00);
          scal.set(Calendar.MILLISECOND, 00);
          Timestamp t=new Timestamp(d.getTime());
          String[] dstring=t.toString().split(" ");
          // String[] dstring= t.toString();
          date.setText(Formats.TIMESTAMP.formatValue(d));
          jLabel2.setText(dlfac.getPurchasetransactionnum(scal.getTime(),cal.getTime() ));
          warehouse.setSelectedIndex(0);
          List<CategoryInfo> cinfolist=new ArrayList();
          cinfolist.add((CategoryInfo)warehouse.getSelectedItem());
          m_cat.loadCatalog(cinfolist);
          amtTotal.setText("0.0");
          amountTotal.setText("0.0");
          grandtotal.setText("0.0");
          qtytotal.setText("0.0");
          jPanel2.setVisible(true);
          jLabel9.setVisible(true);
          amountTotal.setVisible(true);
          grandtotal.setVisible(true);
          jScrollPane3.setVisible(false);
          jPanel1.revalidate();
          invoiceno.setText(null);
          delivarychallan.setText(null);
          cashaccount.setSelectedIndex(-1);
        // cashaccount.showPopup();
          docref.setText(null);
          //warehouse.setSelectedIndex(-1);
          //alist=ptableModel.getadditionalEntrylist();
          AdditionalEntry aelement=new AdditionalEntry();
          ptableModel.getadditionalEntrylist().add(aelement);
          jTable3.setModel(ptableModel.getTableModelforAdditionalEntries());
          jLabel11.setVisible(true);
          jTable1.setRowSelectionInterval(0,0);
          TableColumn col = jTable3.getColumnModel().getColumn(0);
          col.setPreferredWidth(300);
          jTable3.getColumnModel().getColumn(1).setPreferredWidth(80);
          col.setCellEditor(new MyTableCellEditor());
     }
    public void activate() throws BasicException {
     //   jPanel1.setVisible(false);
        qtytotal.setEditable(false);
        amtTotal.setEditable(false);
        amountTotal.setEditable(false);
        grandtotal.setEditable(false);
        cModel=new ComboBoxValModel(m_dlSales.getMainWarehouses());
        vModel=new ComboBoxValModel(dlfac.getVendorList());
        cashaccModel=new ComboBoxValModel(dlfac.getCashAndBanksubAccounts(""));
        cashaccount.setModel(cashaccModel);
        vendor.setModel(vModel);
        warehouse.setModel(cModel);
       // warehouse.setSelectedIndex(0);
       // List<CategoryInfo> cinfolist=new ArrayList();
      //  cinfolist.add((CategoryInfo)warehouse.getSelectedItem());
      //  m_cat.loadCatalog(cinfolist);
        taxeslogic = new TaxesLogic(m_dlSales.getTaxList().list());
        vendor.getEditor().getEditorComponent().addKeyListener(new MyKeyListener());
       // if(flag)
        reset();
       // else
       //     ;
        jTable1.setBackground(Color.WHITE);
        TableColumnModel jColumns = jTable1.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(200);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(60);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(60);
        jColumns.getColumn(2).setResizable(false);
        jColumns.getColumn(3).setPreferredWidth(60);
        jColumns.getColumn(3).setResizable(false);
        jColumns.getColumn(4).setPreferredWidth(60);
        jColumns.getColumn(4).setResizable(false);
        jColumns.getColumn(5).setPreferredWidth(60);
        jColumns.getColumn(5).setResizable(false);
        jColumns.getColumn(6).setPreferredWidth(60);
        jColumns.getColumn(6).setResizable(false);
      //   TableColumn col = jTable3.getColumnModel().getColumn(0);
       //  col.setPreferredWidth(300);
       //  jTable3.getColumnModel().getColumn(1).setPreferredWidth(80);
       //  col.setCellEditor(new MyTableCellEditor());
   //     amtTotal.setText("0.0");
   //     qtytotal.setText("0.0");
   //     jPanel2.setVisible(true);
  //      jLabel9.setVisible(false);
  //      amountTotal.setVisible(false);
  //      grandtotal.setVisible(false);
  //       jList1.setVisible(false);
        //jPanel1.setVisible(false);
    //   TableColumn tcolumn=jTable1.getColumnModel().getColumn(2);
   //     tcolumn.setCellEditor(new javax.swing.JComboBox());
        //jTable1.getCellEditor().
     //  jTable1.getModel().addTableModelListener(this);

     //   jTable1.getColumnModel().getColumn(0).getc
    /*   KeyboardFocusManager kfm =
      KeyboardFocusManager.getCurrentKeyboardFocusManager();
    kfm.addKeyEventDispatcher(
      new KeyEventDispatcher() {
        public boolean dispatchKeyEvent(KeyEvent e) {
            String del=""+e.getKeyCode();
            if(e.getKeyCode() == KeyEvent.VK_DOWN) return true;
          return false;
        }});*/
     /* jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
           jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
               public void keyPressed(java.awt.event.KeyEvent evt) {
                   jTableKeyPressed(evt);
               }
           });*/
    

    }
    public void setValue(Object[] values,List<PurchaseVoucherline> purlist,List<AdditionalEntry> adlList,JDialog dialog){
        this.dialog=dialog;
            mode=1;
        oldValues=values;
        vModel.setSelectedKey(values[0]);
        cModel.setSelectedKey(values[1]);
        invoiceno.setText(values[2].toString());
        date.setText(Formats.TIMESTAMP.formatValue(values[3]));
        oldDate=(Date)values[3];
        oldTransno=values[6].toString();
        docref.setText(values[4].toString());
        delivarychallan.setText(values[5].toString());
        jLabel2.setText(values[6].toString());

        //cashaccount.set;
        boolean cashpayment=false;
        if(values[7]!=null){
            String cashacc=values[7].toString();
            cashaccModel.setSelectedKey(cashacc);
            jRadioButton1.setSelected(true);
            jRadioButton2.setSelected(false);
        }else{
            jRadioButton2.setSelected(true);
            jRadioButton1.setSelected(false);
        }
           // cashpayment=Boolean.parseBoolean(values[7].toString());
        oldID=values[8].toString();
      /*  if(cashpayment){
            jRadioButton1.setSelected(true);
            jRadioButton2.setSelected(false);
        }else{
            jRadioButton2.setSelected(true);
            jRadioButton1.setSelected(false);
        }*/

        //plist=ptableModel.getlist();
        ptableModel.getlist().clear();
        ptableModel.getlist().addAll(purlist);
        PurchaseVoucherline vline=new PurchaseVoucherline();
        ptableModel.getlist().add(vline);
        //alist=ptableModel.getadditionalEntrylist();
        ptableModel.getadditionalEntrylist().clear();
        ptableModel.getadditionalEntrylist().addAll(adlList);
        AdditionalEntry aline=new AdditionalEntry();
        ptableModel.getadditionalEntrylist().add(aline);
        oldplist=new ArrayList<PurchaseVoucherline>();
        for(PurchaseVoucherline line:ptableModel.getlist()){
            oldplist.add(line.getCopy());
        }
        //oldplist.addAll(purlist);
        vline=new PurchaseVoucherline();
        oldplist.add(vline);
        oldalist=new ArrayList<AdditionalEntry>();
        for(AdditionalEntry line:ptableModel.getadditionalEntrylist()){
            oldalist.add(line.getCopy());
        }
        //oldalist.addAll(adlList);
        aline=new AdditionalEntry();
        oldalist.add(aline);
        double qty=0.0,amount=0.0,addlamount=0.0;
        for(PurchaseVoucherline line:ptableModel.getlist()){
             amount+=line.getTotalAmount();
             qty+=line.getQty();
            // taxamount+=line.getTax();
        }
        for(AdditionalEntry line:ptableModel.getadditionalEntrylist()){
             addlamount+=line.getamount();
        }
        qtytotal.setText(String.valueOf(qty));
        amtTotal.setText(dlfac.ConvertDoubleToString(amount));
        amountTotal.setText(dlfac.ConvertDoubleToString(addlamount));
        grandtotal.setText(dlfac.ConvertDoubleToString(amount+addlamount));
        jTable1.setModel(ptableModel.getTableModel());
        jTable3.setModel(ptableModel.getTableModelforAdditionalEntries());
        jTable1.setRowSelectionInterval(0,0);
          TableColumn col = jTable3.getColumnModel().getColumn(0);
          col.setPreferredWidth(300);
          jTable3.getColumnModel().getColumn(1).setPreferredWidth(80);
          col.setCellEditor(new MyTableCellEditor());
    }
     private boolean checkforValidity(Date d){
          try{
          Map<String,GeneralSettingInfo> gs=LookupUtilityImpl.getInstance(null).getGeneralSettingsMap();
          GeneralSettingInfo sinfo=gs.get("Datestart");
          GeneralSettingInfo einfo=gs.get("Dateend");
          //String prevFySubIncome=gs.get("prevFySubIncome").getValue();
         // String nextFySubIncome=gs.get("nextFySubIncome").getValue();
          Date fsdate=(Date)Formats.DATE.parseValue(sinfo.getValue());
          Date fedate=(Date)Formats.DATE.parseValue(einfo.getValue());
          Calendar fscal=Calendar.getInstance();//financial year sdate
          fscal.setTime(fsdate);
          Calendar fecal=Calendar.getInstance();//financial year edate
          fecal.setTime(fedate);
          Calendar caltemp=Calendar.getInstance();
          caltemp.setTime(d);
          if(!caltemp.before(fscal) && !caltemp.after(fecal)){
             return true;
          }else
              return false;
          }catch(Exception e){
              return false;
          }
      }
     class MyKeyListener extends KeyAdapter {
        public void keyReleased(KeyEvent evt) {
           // JComboBox cb = (JComboBox)evt.getSource();

            // Get pressed character
          //  char ch = evt.getKeyChar();

            // If not a printable character, return
          //  if (ch != KeyEvent.CHAR_UNDEFINED) {
                vendor.showPopup();
          //  }
        }
     }
    private void cellKeyPressed(java.awt.event.KeyEvent evt,Object text) throws BasicException{
      if(evt.getKeyCode()==KeyEvent.VK_ENTER){
          int row=jTable3.getSelectedRow();
           jTable3.getModel().setValueAt(jList1.getSelectedValue().toString(), row, 0);
            AccountMaster acc=(AccountMaster)jList1.getSelectedValue();
           String id=acc.getid();
          jTable3.getModel().setValueAt(id, row, 2);
          // jList1.setVisible(false);
          jScrollPane3.setVisible(false);
          jPanel1.revalidate();
          //jScrollPane1.setVisible(false);
           jTable3.setModel(ptableModel.getTableModelforAdditionalEntries());
           TableColumn col = jTable3.getColumnModel().getColumn(0);
           col.setCellEditor(new MyTableCellEditor());
           jTable3.setRowSelectionInterval(row, row);
      }else{
            celldata=text.toString();
            List acclist = dlfac.getsubAccounts(text.toString().toUpperCase());
            jList1.removeAll();
            jList1.setModel(new ItemsListModel(acclist));
            jList1.setSelectedIndex(0);
            jList1.setVisible(true);
            jScrollPane3.setVisible(true);
            jPanel1.revalidate();
           // jScrollPane1.setVisible(true);
        //    jScrollPane3.setVisible(true);
        //    jPanel1.validate();
           // jPanel1.repaint();
            
             if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
                 java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    jList1.requestFocus();
                }
            });
             }
      }
    }
     public class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor,KeyListener {
        // This is the component that will handle the editing of the cell value
        JComponent component = new JTextField();

        // This method is called when a cell value is edited by the user.
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int rowIndex, int vColIndex) {
            // 'value' is value contained in the cell located at (rowIndex, vColIndex)
            component.addKeyListener(this);
            if (isSelected) {
                // cell (and perhaps other cells) are selected
                jTable1.clearSelection();
            }

            // Configure the component with the specified value
            ((JTextField)component).setText((String)value);

            // Return the configured component
            return component;
        }

        // This method is called when editing is completed.
        // It must return the new value to be stored in the cell.
        public Object getCellEditorValue() {
            return ((JTextField)component).getText();
        }

        public void keyTyped(KeyEvent e) {
            
        }

        public void keyPressed(KeyEvent e) {
           
        }

        public void keyReleased(KeyEvent e) {
            try{
            cellKeyPressed(e,getCellEditorValue());
            }catch(Exception e1){
              e1.printStackTrace();
            }
        }
    }
 

    public boolean deactivate() {
       return true;
    }

    public JComponent getComponent() {
        return this;
    }

    public Object getBean() {
        return this;
    }

   
     private class ItemsListModel extends AbstractListModel {
        private java.util.List items;
        public ItemsListModel(java.util.List items) {
            this.items = items;
        }
        public int getSize() {
            return items.size();
        }
        public Object getElementAt(int i) {
            return items.get(i);
        }


    }

     private class CatalogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          // incProduct(1.0, (ProductInfoExt) e.getSource());
            int row=jTable1.getSelectedRow();
            int column=jTable1.getSelectedColumn();
            ProductInfoExt pinfo=(ProductInfoExt)e.getSource();
            if(pinfo.getAccount()!=null){
            ptableModel.getTableModel().setValueAt(pinfo.getName(), row, 0);
            ptableModel.getTableModel().setValueAt(pinfo.getTaxCategoryName(), row, 4);
            ptableModel.getTableModel().setValueAt(pinfo.getPriceSell(), row, 2);
            ptableModel.getTableModel().setValueAt(pinfo.getAccount(), row, 8);
            TaxInfo tax = taxeslogic.getTaxInfo(pinfo.getTaxCategoryInfo());
         //   pinfo.getPriceSellTax(tax);
            ptableModel.getTableModel().setValueAt(tax.getRate(), row, 7);
            ptableModel.getTableModel().setValueAt(pinfo.getID(), row, 9);//tax value for i qty
             ptableModel.getTableModel().setValueAt(tax.getId(), row, 10);
            /*List<PurchaseVoucherline> plist=ptableModel.getlist();
           if((jTable1.getRowCount()-2)==row){
              PurchaseVoucherline pelement=new PurchaseVoucherline();
              plist.add(pelement);
            }*/
           jTable1.setModel(ptableModel.getTableModel());
          // jTable1.setRowSelectionAllowed(true);
           jTable1.setRowSelectionInterval(row, row);
            }else{
              JOptionPane.showMessageDialog(null, "Please Specify a Purchase Account to the product", null, JOptionPane.OK_OPTION);
            }
        // jTable1.se
         //jTable1.getModel().addTableModelListener(this);
        }
    }
      private void stateTransition(char cTrans) {

        if (cTrans == '\u007f') {
           jTextField1.setText(null);
        } else if (cTrans == '+' || cTrans == '-') {

        }
        else if (cTrans == ' ' || cTrans == '=') {

        } else {

            jTextField1.setText( jTextField1.getText() + cTrans);
        }
    }
   
    private boolean isAlpha(String s){
       s = s.toUpperCase();
       for (int i = 0; i < s.length(); i ++){
           int c = (int) s.charAt(i);
           if ((c < 65 || c > 90) && (c<47 || c>58) )
                  return false;
       }
       return true;
    }

  private void updatePurchaseJournalMainStockDiary(Date d,Vendor v,String id,double total) throws BasicException{
        String createdby=m_App.getAppUserView().getUser().getName();  
        Object[] values=new Object[]{id,jLabel2.getText(),invoiceno.getText(),delivarychallan.getText(),docref.getText(),createdby,d,v.getKey(),total,cModel.getSelectedKey()};
        new PreparedSentence(m_App.getSession()
                    , "INSERT INTO PURCHASEJOURNALMAIN (ID,TNO,INVOICENO,DELIVERYCHALLAN,DOCUMENTREF,CREATEDBY,CRDATE,VENDOR,TOTAL,WAREHOUSE) VALUES(?,?,?,?,?,?,?,?,?,?)"
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING,  Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,Datas.STRING,Datas.TIMESTAMP,Datas.STRING,Datas.DOUBLE,Datas.STRING})).exec(values);

    }
    private void updatePurchaseJournalnStockDiary(Date d,PurchaseVoucherline p,int qty,Vendor v,String id) throws BasicException{
        //for(PurchaseJournalTable.PurchaseVoucherline p:plist){
        
            String createdby=m_App.getAppUserView().getUser().getName();
            Object[] values=new Object[]{UUID.randomUUID().toString(),p.getitemid(),qty,p.getRate(),p.getamount(),p.getTax(),id};
            new PreparedSentence(m_App.getSession()
                    , "INSERT INTO PURCHASEJOURNAL (ID,ITEM,QTY,RATE,TOTAL,TAXTOTAL,PARENT) VALUES(?,?,?,?,?,?,?)"
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.INT, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING})).exec(values);
            /*
             *Object[] values=new Object[]{UUID.randomUUID().toString(),p.getitemid(),qty,p.getRate(),p.getTotalAmount(),jLabel2.getText(),invoiceno.getText(),delivarychallan.getText(),docref.getText(),createdby,d,v.getKey()};

             new PreparedSentence(m_App.getSession()
                    , "INSERT INTO PURCHASEJOURNAL (ID,ITEM,QTY,RATE,TOTAL,TNO,INVOICENO,DELIVERYCHALLAN,DOCUMENTREF,CREATEDBY,CRDATE,VENDOR) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.INT, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,Datas.STRING,Datas.TIMESTAMP,Datas.STRING})).exec(values);

             * */
        CategoryInfo cinfo=(CategoryInfo)warehouse.getSelectedItem();
       // string qty=Integer.toString(p.getQty());
        Object[] param=new Object[]{UUID.randomUUID().toString(),d,1,cinfo.getID(),p.getitemid(),Double.valueOf(Integer.toString(qty)),0.0,createdby,jLabel2.getText()};
          new PreparedSentence(m_App.getSession()
                    , "INSERT INTO STOCKDIARY (ID,DATENEW,REASON1,LOCATION1,PRODUCT1,UNITS1,PRICE1,CREATEDBY,RNO) VALUES(?,?,?,?,?,?,?,?,?)"
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.INT, Datas.STRING,  Datas.STRING,Datas.DOUBLE, Datas.DOUBLE, Datas.STRING,Datas.STRING})).exec(param);
  //  }(
          CategoryInfo catinfo=(CategoryInfo)warehouse.getSelectedItem();
          m_dlSales.updateStockVolume2(Double.parseDouble(String.valueOf(qty)), p.getitemid(),catinfo.getID());

    }
    private Date getDate(String date) throws BasicException{
       Date d=(Date)Formats.TIMESTAMP.parseValue(date);
       Calendar cal=GregorianCalendar.getInstance();
       cal.set(Calendar.HOUR_OF_DAY, 00);
       cal.set(Calendar.MINUTE, 00);
       cal.set(Calendar.SECOND, 00);
       cal.set(Calendar.MILLISECOND, 00);
       return d;
    }

    private boolean isEdited(){
        boolean edited=false;
        for(PurchaseVoucherline line:ptableModel.getlist()){
            boolean flag=true;
            if(line.getitem()!=null){
            for(PurchaseVoucherline oline:oldplist){
                if(line.getQty()==oline.getQty() && line.getTax()==oline.getTax() && line.getamount()==oline.getamount() && line.getitemid().equals(oline.getitemid())){
                     oldplist.remove(oline);
                     flag=false;
                     break;
                }
            }
            }else
                flag=false;
            if(flag){
              edited=true;
              return edited;
            }
        }
        for(AdditionalEntry line:ptableModel.getadditionalEntrylist()){
            boolean flag=true;
           if(line.getname()!=null){
            for(AdditionalEntry oline:oldalist){
                if(line.getAccount().equals(oline.getAccount()) && line.getamount()==oline.getamount()){
                     oldalist.remove(oline);
                     flag=false;
                     break;
                }
            }
            }else
                flag=false;
            if(flag){
              edited=true;
              return edited;
            }
        }
        if(vModel.getSelectedKey()!=null && !vModel.getSelectedKey().equals(oldValues[0]))
            edited=true;
        else if(cModel.getSelectedKey()!=null && !cModel.getSelectedKey().equals(oldValues[1]))
            edited=true;
        else if(!invoiceno.getText().equals(oldValues[2]))
            edited=true;
        else if(!Formats.TIMESTAMP.formatValue(oldValues[3]).equals(date.getText()))
            edited=true;
        else if(!docref.getText().equals(oldValues[4]))
            edited=true;
        else if(!delivarychallan.getText().equals(oldValues[5]))
            edited=true;
        else if(!jLabel2.getText().equals(oldValues[6]))
            edited=true;
        else if(oldValues[7]==null && jRadioButton1.isSelected()){
            edited=true;
        } else if(oldValues[7]!=null && jRadioButton2.isSelected()){
            edited=true;
        } else if(oldValues[7]!=null && !cashaccModel.getSelectedKey().equals(oldValues[7])){
            edited=true;
        }


        return edited;
    }

 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        qtytotal = new javax.swing.JTextField();
        amtTotal = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        amountTotal = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jNumberKeys1 = new com.openbravo.beans.JNumberKeys();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        vendor = new javax.swing.JComboBox();
        invoiceno = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        docref = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        warehouse = new javax.swing.JComboBox();
        date = new javax.swing.JTextField();
        cashaccount = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        delivarychallan = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        grandtotal = new javax.swing.JTextField();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel8.setText("Total");

        jPanel2.setLayout(new java.awt.BorderLayout());

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable3.setShowHorizontalLines(false);
        jTable3.setShowVerticalLines(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable3);

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jList1KeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
        );

        jLabel9.setText("Total Amount");

        amountTotal.setText("0.0");

        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jNumberKeys1.addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {
            public void keyPerformed(com.openbravo.beans.JNumberEvent evt) {
                jNumberKeys1KeyPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 12));

        jButton2.setText("Enter");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Addl. Charge");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jNumberKeys1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jNumberKeys1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jLabel1.setText("Name");

        jLabel3.setText("Invoice No");

        vendor.setEditable(true);

        jLabel6.setText("Document Ref");

        jLabel7.setText("Warehouse");

        jLabel5.setText("Date");

        jLabel4.setText("Delivary Challan");

        warehouse.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                warehouseItemStateChanged(evt);
            }
        });

        jLabel10.setText("Cash Accounts");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Cash");
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Credit");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel12.setText("Purchase NO :");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(vendor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(invoiceno, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(docref, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(warehouse, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cashaccount, javax.swing.GroupLayout.Alignment.LEADING, 0, 178, Short.MAX_VALUE)
                            .addComponent(delivarychallan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(vendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7)
                            .addComponent(warehouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(invoiceno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(docref, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4)
                            .addComponent(delivarychallan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jLabel10)
                            .addComponent(cashaccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton2)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jButton1)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jLabel11.setText("Grand Total");

        grandtotal.setText("0.0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(qtytotal, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(amtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(226, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(grandtotal)
                    .addComponent(amountTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addGap(315, 315, 315))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, 0, 757, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(qtytotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(amtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(amountTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grandtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                    .addComponent(jButton4))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void warehouseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_warehouseItemStateChanged
        // TODO add your handling code here:
        try{
            if(warehouse.getSelectedIndex()!=-1){
            //  reset();
             List<CategoryInfo> cinfolist=new ArrayList();
             m_cat.loadCatalog(cinfolist);
             cinfolist.add((CategoryInfo)warehouse.getSelectedItem());
              m_cat.loadCatalog(cinfolist);
              if(ptableModel!=null){
              ptableModel.getlist().clear();
              ptableModel.getlist().add(new PurchaseVoucherline());
              jTable1.setModel(ptableModel.getTableModel());
              grandtotal.setText("0.0");
              amountTotal.setText("0.0");
              amtTotal.setText("0.0");
              qtytotal.setText("0");
            }
            }
        }catch(Exception e){
         e.printStackTrace();
        }
    }//GEN-LAST:event_warehouseItemStateChanged

    private void jNumberKeys1KeyPerformed(com.openbravo.beans.JNumberEvent evt) {//GEN-FIRST:event_jNumberKeys1KeyPerformed
        // TODO add your handling code here:
         stateTransition(evt.getKey());
    }//GEN-LAST:event_jNumberKeys1KeyPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        DecimalFormat dFormat = new DecimalFormat("#.##");
      try{
         int row=jTable1.getSelectedRow();
         int row1=jTable3.getSelectedRow();
         if(row>=0 && row1>=0){
            JOptionPane.showMessageDialog(this, "Please select only one table", "Cannot insert", JOptionPane.OK_OPTION);
         }else if(row>=0){

         int column=jTable1.getSelectedColumn();
       //  if(column==1){
          try{
        if(jTextField1.getText().length()>0){
        if(column==2){   //Arun
            Double qty=Double.valueOf(ptableModel.getTableModel().getValueAt(row,1).toString());
            //qty = Double.parseDouble(dFormat.format(qty));
            Double rate=dlfac.roundTwoDecimals(Double.valueOf(jTextField1.getText()).doubleValue());
             //rate = Double.parseDouble(dFormat.format(rate));
            Double Amount=dlfac.roundTwoDecimals(rate*qty);
             //Amount = Double.parseDouble(dFormat.format(Amount));

            ptableModel.getTableModel().setValueAt(Amount, row, 3);
            ptableModel.getTableModel().setValueAt(rate, row, 2);
            Double taxvalue=Double.valueOf(ptableModel.getTableModel().getValueAt(row,7).toString());
            Double taxtotal=dlfac.roundTwoDecimals(taxvalue * qty * rate);

             //taxvalue = Double.parseDouble(dFormat.format(taxvalue));
             //taxtotal = Double.parseDouble(dFormat.format(taxtotal));
            
            ptableModel.getTableModel().setValueAt(taxtotal, row, 5);
            ptableModel.getTableModel().setValueAt(dlfac.roundTwoDecimals(taxtotal+Amount), row, 6);
            jTable1.setModel(ptableModel.getTableModel());
            if(jTable1.getRowCount()-1>row)
             jTable1.setRowSelectionInterval(row+1, row+1);
            else
            jTable1.setRowSelectionInterval(row, row);
            jTextField1.setText(null);
        }else if(column==6){  //Arun
            double qty=Double.parseDouble(ptableModel.getTableModel().getValueAt(row,1).toString());
            qty = Double.parseDouble(dFormat.format(qty));
            double amt=0.0,rate=0.0;
            if(qty>0){
              amt=dlfac.roundTwoDecimals(Double.parseDouble(jTextField1.getText()));
              amt = Double.parseDouble(dFormat.format(amt));

              Double taxvalue=Double.valueOf(ptableModel.getTableModel().getValueAt(row,7).toString());
              Double taxtotal=dlfac.roundTwoDecimals(taxvalue * amt);
              rate=dlfac.roundTwoDecimals((amt-taxtotal)/qty);
              rate = Double.parseDouble(dFormat.format(rate));

              ptableModel.getTableModel().setValueAt(rate, row, 2);
               ptableModel.getTableModel().setValueAt(dlfac.roundTwoDecimals(amt-taxtotal), row, 3);
               ptableModel.getTableModel().setValueAt(dlfac.roundTwoDecimals(amt), row, 6);
               ptableModel.getTableModel().setValueAt(dlfac.roundTwoDecimals(taxtotal), row, 5);
              jTable1.setModel(ptableModel.getTableModel());
              if(jTable1.getRowCount()-1>row)
               jTable1.setRowSelectionInterval(row+1, row+1);
              else
              jTable1.setRowSelectionInterval(row, row);
              jTextField1.setText(null);
            }
        }else{
         Double qty=Double.valueOf(jTextField1.getText());
         Double rate= Double.valueOf(ptableModel.getTableModel().getValueAt(row,2).toString());
         Double taxvalue=Double.valueOf(ptableModel.getTableModel().getValueAt(row,7).toString());
         Double taxtotal=dlfac.roundTwoDecimals(taxvalue * qty *rate);
         Double Amount=dlfac.roundTwoDecimals(rate * qty);
       //  qtytotal.setText(String.valueOf(Double.valueOf(qtytotal.getText())+qty));
        // amtTotal.setText(String.valueOf(Double.valueOf(amtTotal.getText())+Amount));
         ptableModel.getTableModel().setValueAt(Amount, row, 3);
         ptableModel.getTableModel().setValueAt(taxtotal, row, 5);
         ptableModel.getTableModel().setValueAt(dlfac.roundTwoDecimals(taxtotal+Amount), row, 6);
         ptableModel.getTableModel().setValueAt(Integer.valueOf(jTextField1.getText()), row, 1);
        // jTable1.setModel(ptableModel.getTableModel());
        // jTable1.getModel().addTableModelListener(this);
       /*  int qtytemp=0;
         Double amttemp=0.0;
         for(int i=0;i<plist.size();i++){
            if(plist.get(i).getamount()!=null){
                qtytemp+=plist.get(i).getQty();
                amttemp+=plist.get(i).getamount();
            }
         }
         qtytotal.setText(String.valueOf(qtytemp));
         amtTotal.setText(String.valueOf(amttemp));*/
        //  if((jTable1.getRowCount()-2)==row){
      //   PurchaseJournalTable.PurchaseVoucherline pelement=new PurchaseJournalTable.PurchaseVoucherline();
       //   plist.add(pelement);}else
           if(jTable1.getRowCount()-1==row){
              PurchaseVoucherline pelement=new PurchaseVoucherline();
              ptableModel.getlist().add(pelement);
          }
         jTable1.setModel(ptableModel.getTableModel());
         jTable1.setRowSelectionInterval(row+1, row+1);
         jTextField1.setText(null);
          }
          int qtytemp=0;
         Double amttemp=0.0;
         for(int i=0;i<ptableModel.getlist().size();i++){
            //if(plist.get(i).getamount()!=null){
                qtytemp+=ptableModel.getlist().get(i).getQty();
                amttemp+=ptableModel.getlist().get(i).getamount()+ptableModel.getlist().get(i).getTax();
           // }
         }
         qtytotal.setText(String.valueOf(qtytemp));
         amtTotal.setText(String.valueOf(dlfac.roundTwoDecimals(amttemp)));
         if(grandtotal.isVisible()==true){
           grandtotal.setText(String.valueOf(dlfac.roundTwoDecimals(Double.parseDouble(amtTotal.getText())+Double.parseDouble(amountTotal.getText()))));
         }
          }
         } catch(NumberFormatException e){
           JOptionPane.showMessageDialog(this, "Invalid Number.Quantity Cannot be a decimal value", "Error",JOptionPane.WARNING_MESSAGE);
           e.printStackTrace();
         }
         catch(Exception e){
           JOptionPane.showMessageDialog(this, "Please Select a product", "Error",JOptionPane.WARNING_MESSAGE);
           e.printStackTrace();
         }
         }else if(row1>=0){
             if(jTextField1.getText().length()>0){
              jTable3.getModel().setValueAt(Double.valueOf(jTextField1.getText()), row1, 1);
              if((jTable3.getRowCount()-1)==row1){
                AdditionalEntry aelement=new AdditionalEntry();
                ptableModel.getadditionalEntrylist().add(aelement);
              }
             jTable3.setModel(ptableModel.getTableModelforAdditionalEntries());
             TableColumn col = jTable3.getColumnModel().getColumn(0);
             col.setCellEditor(new MyTableCellEditor());
            
            Double amt=0.0;
            for(int i=0;i<ptableModel.getadditionalEntrylist().size();i++){
              //  if(ptableModel.getadditionalEntrylist().get(i).getamount()!=null)
                   amt+=ptableModel.getadditionalEntrylist().get(i).getamount();
            }
            amountTotal.setText(String.valueOf(dlfac.roundTwoDecimals(amt)));
            grandtotal.setText(String.valueOf(dlfac.roundTwoDecimals(Double.parseDouble(amtTotal.getText())+amt)));
            jTextField1.setText(null);
            jTable3.setRowSelectionInterval(row1+1, row1+1);
           }
         }
      }catch(Exception e1){
        JOptionPane.showMessageDialog(this, "Invalid Number", "Error", JOptionPane.OK_OPTION);
        e1.printStackTrace();
      }
    }//GEN-LAST:event_jButton2ActionPerformed
  //   private javax.swing.JList list;
  //   javax.swing.JTable jTable2;
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(jPanel1.isVisible()==false){
          jLabel11.setVisible(true);
         jPanel2.setVisible(false);
         jPanel1.setVisible(true);
         jLabel9.setVisible(true);
         amountTotal.setVisible(true);
         grandtotal.setVisible(true);
         jTable3.setRowSelectionInterval(ptableModel.getadditionalEntrylist().size()-1,ptableModel.getadditionalEntrylist().size()-1);
        }else{
            jPanel1.setVisible(false);
            jPanel2.setVisible(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jList1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
          int row=jTable3.getSelectedRow();
          jTable3.getModel().setValueAt(jList1.getSelectedValue().toString(), row, 0);
          AccountMaster acc=(AccountMaster)jList1.getSelectedValue();
           String id=acc.getid();
          jTable3.getModel().setValueAt(id, row, 2);
         // jList1.setVisible(false);
          jScrollPane3.setVisible(false);
         jPanel1.revalidate();
       /*   if((jTable3.getRowCount()-2)==row){
          PurchaseJournalTable.AdditionalEntry aelement=new PurchaseJournalTable.AdditionalEntry();
            alist.add(aelement);
          }*/
          jTable3.setModel(ptableModel.getTableModelforAdditionalEntries());
          TableColumn col = jTable3.getColumnModel().getColumn(0);
          col.setCellEditor(new MyTableCellEditor());
          jTable3.setRowSelectionInterval(row, row);
        }
    }//GEN-LAST:event_jList1KeyPressed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
         int row=jTable3.getSelectedRow();
      //  int column=jTable3.getSelectedColumn();
        jTable3.getModel().setValueAt(jList1.getSelectedValue().toString(), row, 0);
         AccountMaster acc=(AccountMaster)jList1.getSelectedValue();
         String id=acc.getid();
          jTable3.getModel().setValueAt(id, row, 2);
       //  jList1.setVisible(false);
         //jScrollPane1.setVisible(false);
         jScrollPane3.setVisible(false);
         jPanel1.revalidate();
       /*  if((jTable3.getRowCount()-2)==row){
          PurchaseJournalTable.AdditionalEntry aelement=new PurchaseJournalTable.AdditionalEntry();
          alist.add(aelement);
         }*/
        jTable3.setModel(ptableModel.getTableModelforAdditionalEntries());
          TableColumn col = jTable3.getColumnModel().getColumn(0);
         col.setCellEditor(new MyTableCellEditor());
         jTable3.setRowSelectionInterval(row, row);
       // celldata=null;
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if(vendor.getSelectedIndex()!=-1 && invoiceno.getText().length()>0 && docref.getText().length()>0 && date.getText().length()>0 && delivarychallan.getText().length()>0){
         try{
            Date d=(Date)Formats.DATE.parseValue(date.getText());
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(d.getTime());
            Calendar fy_scal=Calendar.getInstance();
            Calendar fy_ecal=Calendar.getInstance();
            Map<String,GeneralSettingInfo> gsinfo=LookupUtilityImpl.getInstance(m_App).getGeneralSettingsMap();
            Date fy_sdate=(Date)Formats.DATE.parseValue(gsinfo.get("Datestart").getValue());
            Date fy_edate=(Date)Formats.DATE.parseValue(gsinfo.get("Dateend").getValue());
            fy_ecal.setTimeInMillis(fy_edate.getTime());
            fy_ecal.add(Calendar.DATE, 1);
            fy_scal.setTimeInMillis(fy_sdate.getTime());
            if(cal.before(fy_scal) || !cal.before(fy_ecal)){
               JOptionPane.showMessageDialog(this, "Please check the date.The Specified Date does not come under the present financial year", "Sorry Cannot Save", JOptionPane.WARNING_MESSAGE);
            }else{
              boolean flag1=false;
              if(mode==1){
                  flag1=isEdited();

              }else
                  flag1=true;
       if(flag1==true){
            Transaction t=new Transaction(m_App.getSession()) {
                    @Override
            protected Object transact() throws BasicException {
             Date d=new Date();
             String tid=UUID.randomUUID().toString();
             Date pdate=(Date)Formats.DATE.parseValue(date.getText());
            String transref="Purchase Journal";
            double amt=0.0;
            Vendor v=(Vendor)vendor.getSelectedItem();
            if(grandtotal.isVisible()==true)
                amt=dlfac.roundTwoDecimals(Double.valueOf(grandtotal.getText()));   //Arun
            else
                amt=dlfac.roundTwoDecimals(Double.valueOf(amtTotal.getText()));
           /* String cashiercashacc=m_App.getAppUserView().getUser().getcashaccount();
            String cashierchequeacc=m_App.getAppUserView().getUser().getchequeaccount();
            List cashieracc=dlfac.getUsersCashAndChequeAccount();
            cashieracc.remove(cashiercashacc);
            cashieracc.remove(cashierchequeacc);*/
            if(mode==1){
                //Update Purchase journal
                new PreparedSentence(m_App.getSession(), "UPDATE PURCHASEJOURNALMAIN SET DEACTDATE=?,DEACTBY=?,DEACTREF=? WHERE ID=? "
                                    , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.STRING,Datas.STRING,Datas.STRING})).exec(new Object[]{new Date(),m_App.getAppUserView().getUser().getId(),tid,oldID});
               //update accountjournal
                new PreparedSentence(m_App.getSession(), "UPDATE ACCOUNTJOURNAL SET ACTIVE=?,DEACTDATE=?,DEACTBY=? WHERE TID=? "
                                    , new SerializerWriteBasic(new Datas[]{Datas.BOOLEAN,Datas.TIMESTAMP,Datas.STRING,Datas.STRING})).exec(new Object[]{false,new Date(),m_App.getAppUserView().getUser().getName(),oldID});
                //update ajperiodtotals
                List<Object[]> objList=new PreparedSentence(m_App.getSession(), "SELECT ACCOUNTID,AMOUNT,TRANSTYPE,DATE FROM ACCOUNTJOURNAL WHERE TID=?"
                                     , SerializerWriteString.INSTANCE,new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.DOUBLE,Datas.STRING,Datas.TIMESTAMP})).list(oldID);
           for(Object[] obj:objList){
               if(obj!=null){
                    Date d1=(Date)obj[3];
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(d1.getTime());
                    cal.set(Calendar.HOUR_OF_DAY, 00);
                    cal.set(Calendar.MINUTE, 00);
                    cal.set(Calendar.SECOND, 00);
                    cal.set(Calendar.MILLISECOND, 00);
                    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                    d1.setTime(cal.getTimeInMillis());
                    if(obj[2].toString().equals("C")){
                        new PreparedSentence(m_App.getSession(), "UPDATE AJPERIODTOTALS SET CURCREDIT=(CURCREDIT-?) where ACCOUNTID=? AND EDATE=? "
                                , new SerializerWriteBasic(new Datas[]{Datas.DOUBLE,Datas.STRING,Datas.TIMESTAMP})).exec(new Object[]{obj[1],obj[0],d1});
                        new PreparedSentence(m_App.getSession(), "UPDATE TRAILBALANCE SET CURCREDIT=(CURCREDIT-?) where ACCOUNTID=?  "
                                , new SerializerWriteBasic(new Datas[]{Datas.DOUBLE,Datas.STRING})).exec(new Object[]{obj[1],obj[0]});
                    }else{
                         new PreparedSentence(m_App.getSession(), "UPDATE AJPERIODTOTALS SET CURDEBIT=(CURDEBIT-?) where ACCOUNTID=? AND EDATE=? "
                                , new SerializerWriteBasic(new Datas[]{Datas.DOUBLE,Datas.STRING,Datas.TIMESTAMP})).exec(new Object[]{obj[1],obj[0],d1});
                         new PreparedSentence(m_App.getSession(), "UPDATE TRAILBALANCE SET CURDEBIT=(CURDEBIT-?) where ACCOUNTID=?  "
                                , new SerializerWriteBasic(new Datas[]{Datas.DOUBLE,Datas.STRING})).exec(new Object[]{obj[1],obj[0]});
                    }
               }
           }
           new PreparedSentence(m_App.getSession(), "DELETE FROM STOCKDIARY WHERE DATENEW=? AND RNO=?"
                                     , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{oldDate,oldTransno});
           for(PurchaseVoucherline line:oldplist){
                new PreparedSentence(m_App.getSession(), "UPDATE STOCKCURRENT SET UNITS=(UNITS-?) WHERE PRODUCT=? "
                                      , new SerializerWriteBasic(new Datas[]{Datas.DOUBLE,Datas.STRING})).exec(new Object[]{Double.valueOf(String.valueOf(line.getQty())),line.getitemid()});
           }
        }
        if(jRadioButton1.isSelected()==true){
            AccountMaster acc=(AccountMaster)cashaccount.getSelectedItem();
            Object[] value=new Object[]{UUID.randomUUID().toString(),tid, pdate,"C",transref,jLabel2.getText(),amt,d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Purchase",acc.getid(),amt,d,d,true,oldID};
            dlfac.insertintoaccjoutnal6(value);
           /*  if( cashieracc.contains(acc.getid())){
                  new PreparedSentence(m_App.getSession()
                      , "INSERT INTO GENERALTABLE (ID,NAME,VALUE,CREATEDBY) VALUES (?,?,?,?)"
                      , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING})
                      ).exec(new Object[]{UUID.randomUUID().toString(),acc.getid(),tid,m_App.getAppUserView().getUser().getName()});
             }*/
        }else if(jRadioButton2.isSelected()==true){
            //ID,TID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,DATEOFENTRY,ACTIVE
            Object[] value=new Object[]{UUID.randomUUID().toString(),tid,pdate,"C",transref,jLabel2.getText(),amt,d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Purchase",v.getAccount(),amt,d,d,true,oldID};
            dlfac.insertintoaccjoutnal6(value);
        }
       // updatePurchaseJournalnStockDiary(d);
       // updateStockDiary(d);
       
        List<PurchaseVoucherline> purchaselist=ptableModel.getlist();
        int i=0;
        int size=purchaselist.size();
        updatePurchaseJournalMainStockDiary(pdate,  v, tid, Double.valueOf(amtTotal.getText()));
       // while(i<size-1){
        for(PurchaseVoucherline p:purchaselist){
          //  PurchaseJournalTable.PurchaseVoucherline p=purchaselist.get(i);
          //  purchaselist.remove(p);
            size=size-1;
            Double amount=p.getamount();
            int qty=p.getQty();
           // List<PurchaseJournalTable.PurchaseVoucherline> purchaselisttemp=new ArrayList<PurchaseJournalTable.PurchaseVoucherline>();
           // purchaselisttemp.addAll(purchaselist);
            /*for(PurchaseJournalTable.PurchaseVoucherline ptemp:purchaselisttemp){
                Boolean flag=p.getitem().equals(ptemp.getitem());
               if(flag){
                 purchaselist.remove(ptemp);
                 size=size-1;
                 amount+=ptemp.getTotalAmount();
                 qty+=ptemp.getQty();
               }
            }*/
           // purchaselist.
            double taxamount=p.getTax();
            String taxComment="";
            if(taxamount>0){
                String taxaccount=dlfac.getAccountForTaxID(p.getTaxcatId());
                if(taxaccount!=null){
                 Object[] value=new Object[]{UUID.randomUUID().toString(),tid,pdate,"D",transref,jLabel2.getText(),taxamount,d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Purchase of "+p.getitem(),taxaccount,taxamount,d,d,true,oldID};
                   dlfac.insertintoaccjoutnal6(value);
                }else{
                    taxComment=" Inclusive Of Tax";
                    JOptionPane.showMessageDialog(null, "Account is not specified to "+ p.getTaxcat()+"Category. tax value will be added to the product account");
                    amount+=taxamount;
                }
            }


          if(p.getAccount()!=null ){
             // try{
                   updatePurchaseJournalnStockDiary(pdate,p,qty,v,tid);
                   amount=dlfac.roundTwoDecimals(amount);
                   Object[] value=new Object[]{UUID.randomUUID().toString(),tid,pdate,"D",transref,jLabel2.getText(),amount,d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Purchase of "+p.getitem()+taxComment,p.getAccount(),amount,d,d,true,oldID};
                   dlfac.insertintoaccjoutnal6(value);
            //  }catch(Exception e){
            //   e.printStackTrace();
           //   }
          }else if(p.getitem()!=null){
             throw new BasicException();
          }
        }
        double amt1=0;
        for(int j=0;j<ptableModel.getadditionalEntrylist().size()-1;j++){
           // try{
            if(ptableModel.getadditionalEntrylist().get(j).getAccount()!=null && ptableModel.getadditionalEntrylist().get(j).getAccount().length()>0 && ptableModel.getadditionalEntrylist().get(j).getamount()>0){
              double amount1=dlfac.roundTwoDecimals(ptableModel.getadditionalEntrylist().get(j).getamount());
              Object[] value=new Object[]{UUID.randomUUID().toString(),tid,pdate,"D",transref,jLabel2.getText(),amount1,d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Additional charges",ptableModel.getadditionalEntrylist().get(j).getAccount(),amount1,d,d,true,oldID};
              dlfac.insertintoaccjoutnal6(value);
              amt1+=ptableModel.getadditionalEntrylist().get(j).getamount();
            }
          //  }catch(Exception e){
         //     e.printStackTrace();
         //   }
        }
       /* if(alist.size()>0 && amt1>0){
         Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,Formats.TIMESTAMP.parseValue(date.getText()),"C","Trans ref",jLabel2.getText(),amt,d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Narration",m_App.getAppUserView().getUser().getcashaccount(),0.0,d,d};
         dlfac.insertintoaccjoutnal3(value1);
        }*/
           if(mode!=1)
               reset();

           //else
            //   setVisible(false);
            return null;
           }
          };
          t.execute();
         }else{
            JOptionPane.showMessageDialog(null, "No Changes was found", "Not Saved", JOptionPane.WARNING_MESSAGE);
         }
         }
         }catch(Exception e){
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "Please Enter Correct Values", "Error", JOptionPane.ERROR_MESSAGE);
        }
        }else{
           JOptionPane.showMessageDialog(this, "Please Fill The Form", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
        }
      if(mode==1)
       dialog.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try{
          Date datenow;
        try {
            datenow = (Date) Formats.TIMESTAMP.parseValue(date.getText());
        } catch (BasicException e) {
            datenow = null;
        }
        datenow = JCalendarDialog.showCalendar(this, datenow);
        if (datenow != null) {
           // LookupUtilityImpl.getInstance(null).reloadGeneralSettingsMap();
             boolean status=checkforValidity(datenow);
            if(status==false){
                datenow=new Date();
                JOptionPane.showMessageDialog(this, "Please select a date within this financial year", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            LookupUtilityImpl.getInstance(null).getGeneralSettingsMap().get("");
              Timestamp t=new Timestamp(datenow.getTime());
              String[] dstring=t.toString().split(" ");
                       Calendar cal=Calendar.getInstance();
          cal.setTime(datenow);
          cal.set(Calendar.HOUR_OF_DAY,24);
          cal.set(Calendar.MINUTE,59);
          cal.set(Calendar.SECOND, 59);
          cal.set(Calendar.MILLISECOND, 59);
          Calendar scal=Calendar.getInstance();
          scal.setTime(datenow);
          scal.set(Calendar.HOUR_OF_DAY,00);
          scal.set(Calendar.MINUTE,00);
          scal.set(Calendar.SECOND, 00);
          scal.set(Calendar.MILLISECOND, 00);

           jLabel2.setText(dlfac.getPurchasetransactionnum(cal.getTime(),scal.getTime()));
            date.setText(Formats.TIMESTAMP.formatValue(datenow));
        }
        }catch(Exception e){
         e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged
        if(jRadioButton1.isSelected()){
          jLabel10.setVisible(true);
          cashaccount.setVisible(true);
        }else{
          jLabel10.setVisible(false);
          cashaccount.setVisible(false);
        }
    }//GEN-LAST:event_jRadioButton1ItemStateChanged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        jTable3.clearSelection();

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        jTable1.clearSelection();
    }//GEN-LAST:event_jTable3MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amountTotal;
    private javax.swing.JTextField amtTotal;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cashaccount;
    private javax.swing.JTextField date;
    private javax.swing.JTextField delivarychallan;
    private javax.swing.JTextField docref;
    private javax.swing.JTextField grandtotal;
    private javax.swing.JTextField invoiceno;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private com.openbravo.beans.JNumberKeys jNumberKeys1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField qtytotal;
    private javax.swing.JComboBox vendor;
    private javax.swing.JComboBox warehouse;
    // End of variables declaration//GEN-END:variables



}
