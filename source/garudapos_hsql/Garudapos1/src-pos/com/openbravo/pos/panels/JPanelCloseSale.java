

package com.openbravo.pos.panels;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import java.awt.*;
//import java.text.ParseException;
import javax.swing.*;
import java.util.Date;
import java.util.UUID;
import javax.swing.table.*;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.TableRendererBasic;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SerializerReadBasic;
//import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPrincipalApp;
//import com.openbravo.pos.forms.JRootApp;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.forms.LookupUtilityImpl;
//import com.openbravo.pos.forms.StartPOS;
//import com.openbravo.pos.panels.Jpendingremarkedit;

//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author adrianromero
 */
public class JPanelCloseSale extends JPanel implements JPanelView, BeanFactoryApp {
    
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private DataLogicSales m_dlSales;
    
    private SalessModel m_salesmodel = null;
    
    private TicketParser m_TTP;
    private String String;
    private DataLogicFacilities dlfac;
    private Date date;
    /** Creates new form JPanelCloseMoney */
    public JPanelCloseSale() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException {
        
        m_App = app;        
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
       // m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        dlfac=(DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);

        m_jTicketTable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.STRING, Formats.STRING, Formats.CURRENCY, Formats.CURRENCY, Formats.CURRENCY}));
        m_jTicketTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollTableTicket.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));       
        m_jTicketTable.getTableHeader().setReorderingAllowed(false);         
        m_jTicketTable.setRowHeight(25);
        m_jTicketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);         
        
        m_jsalestable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.STRING, Formats.INT, Formats.CURRENCY, Formats.CURRENCY}));
        m_jsalestable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollSales.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));       
        m_jsalestable.getTableHeader().setReorderingAllowed(false);         
        m_jsalestable.setRowHeight(25);
        m_jsalestable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        m_jQtTable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.STRING, Formats.STRING,Formats.STRING, Formats.DOUBLE,Formats.TIMESTAMP,Formats.STRING}));
        m_jQtTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));
        m_jQtTable.getTableHeader().setReorderingAllowed(false);
        m_jQtTable.setRowHeight(25);
        m_jQtTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public Object getBean() {
        return this;
    }
    
    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.CloseSale");
    }    
    
    public void activate() throws BasicException {
        loadData();
    }   
    
    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate   
        return true;
    }
  /*  public void loaddata2() throws BasicException{
      //  init( app);
    loadData();
    }*/
    
    private void loadData() throws BasicException {
        
        // Reset
        date=new Date();
        m_jSequence.setText(null);
        m_jMinDate.setText(null);
        m_jMaxDate.setText(null);
        m_jCloseCash.setEnabled(false);
       jTextField1.setText(null); // AppLocal.getIntString("label.noticketstoclose");
        jTextField2.setText(null);
        jTextField4.setText(null);
//        jTextField3.setText(null);
        m_jSalesSubtotal.setText(null);
        m_jSalesTaxes.setText(null);
        m_jSalesTotal.setText(null);
        
        m_jTicketTable.setModel(new DefaultTableModel());
        m_jsalestable.setModel(new DefaultTableModel());
         m_jQtTable.setModel(new DefaultTableModel());
            
        // LoadData
        m_salesmodel = SalessModel.loadInstance(m_App,date);
        
        // Populate Data
        m_jSequence.setText(m_salesmodel.printSequence());
        m_jMinDate.setText(m_salesmodel.printDateStart());
        m_jMaxDate.setText(m_salesmodel.printDateEnd());
        
        

            m_jCloseCash.setEnabled(true);

          jTextField1.setText(m_salesmodel.printsubbill());
          jTextField2.setText(m_salesmodel.printsubtax());
          jTextField3.setText(m_salesmodel.printTotals());
          m_jSalesSubtotal.setText(m_salesmodel.printpdtamount());
          m_jSalesTaxes.setText(m_salesmodel.printtaxamt());
          m_jSalesTotal.setText(m_salesmodel.printtotalamt());
          jTextField4.setText(m_salesmodel.printPendingamt());

         //  m_jCash.setText(m_salesmodel.printPaymentsTotal());
            
        //    m_jSales.setText(m_salesmodel.printSales());
         //   m_jSalesSubtotal.setText(m_salesmodel.printSalesBase());
         //   m_jSalesTaxes.setText(m_salesmodel.printSalesTaxes());
        //    m_jSalesTotal.setText(m_salesmodel.printSalesTotal());
                  
        
        m_jTicketTable.setModel(m_salesmodel.getSalesModel());
                
        TableColumnModel jColumns = m_jTicketTable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(60);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(200);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(80);
        jColumns.getColumn(2).setResizable(false);
        jColumns.getColumn(3).setPreferredWidth(80);
        jColumns.getColumn(3).setResizable(false);
        jColumns.getColumn(4).setPreferredWidth(80);
        jColumns.getColumn(4).setResizable(false);
        
        m_jsalestable.setModel(m_salesmodel.getProductsModel());

        jColumns = m_jsalestable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(200);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(50);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(80);
        jColumns.getColumn(2).setResizable(false);
        jColumns.getColumn(3).setPreferredWidth(80);
        jColumns.getColumn(3).setResizable(false);

         m_jQtTable.setModel(m_salesmodel.getQtTableModel());

        jColumns = m_jQtTable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(60);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(200);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(80);
        jColumns.getColumn(2).setResizable(false);
        jColumns.getColumn(3).setPreferredWidth(80);
        jColumns.getColumn(3).setResizable(false);
        jColumns.getColumn(4).setPreferredWidth(80);
        jColumns.getColumn(4).setResizable(false);
         jColumns.getColumn(5).setPreferredWidth(80);
        jColumns.getColumn(5).setResizable(false);
        
         m_jCloseCash.setEnabled(false);
        if(m_salesmodel.getTotals()>0)
            m_jCloseCash.setEnabled(true);
        
    }   
    
    private void printSales() {
        
        String sresource = m_dlSystem.getResourceAsXML("Printer.CloseSale");
      
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("sales", m_salesmodel);
             //   Object obj=script.eval(sresource).toString();
                int temp=0;//used to indicate whether bar counter or restaurant counter
                boolean flag=m_App.getAppUserView().getUser().hasPermission("res counter");
                if(flag==true){
                    temp=1;
                    script.put("name", "Reataurant");

                }else
                    script.put("name", "Bar");
               // flag=m_App.getAppUserView().getUser().hasPermission("bar counter");
               // if(flag==true){
              //
              //  }
              //      temp=2;
                script.put("flag", temp);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
        }
    }

    private void updatestockdiarydis() throws BasicException{
      /*  java.util.List<SalessModel.discountlist> li=m_salesmodel.getdisQTLineStk();
        for(SalessModel.discountlist d1: li){
         Object[] obj1=m_dlSales.getlocationFromStkCurrent(d1.printproduct());
          if(obj1!=null){
               // m_dlSales.updateStockVolume1(qty1, product);

          SentenceExec sent=  m_dlSales.getStockDiaryInsertinpdt();
          Date d=new Date();
          //-8 indicates discount accepted
          Object[] values= new Object[]{UUID.randomUUID().toString(),d,-8,obj1[0].toString(),d1.printproduct(),d1.getqty(),0.0,"Bill Discount",d1.printcreatedby()};
          sent.exec(values);
          }
        }*/
        java.util.List<SalessModel.discountlist> l=m_salesmodel.getdisBillLineStk();
        for(SalessModel.discountlist d2: l){
         Object[] obj1=m_dlSales.getlocationFromStkCurrent(d2.printproduct());
          if(obj1!=null){
               // m_dlSales.updateStockVolume1(qty1, product);

          SentenceExec sent=  m_dlSales.getStockDiaryInsertinpdt();
          Date d=date;
          //-8 indicates discount accepted
          Object[] values= new Object[]{UUID.randomUUID().toString(),d,-8,obj1[0].toString(),d2.printproduct(),(d2.getqty()),0.0,"Bill Discount",d2.printcreatedby()};
          sent.exec(values);
          }
        }

    }
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        m_jSequence = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        m_jMinDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jMaxDate = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        m_jScrollTableTicket = new javax.swing.JScrollPane();
        m_jTicketTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        m_jSalesTotal = new javax.swing.JTextField();
        m_jScrollSales = new javax.swing.JScrollPane();
        m_jsalestable = new javax.swing.JTable();
        m_jSalesTaxes = new javax.swing.JTextField();
        m_jSalesSubtotal = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel8 = new javax.swing.JLabel();
        m_jCloseCash2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jCloseCash = new javax.swing.JButton();
         m_jQtTable = new javax.swing.JTable(); 

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(null);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.datestitle"))); // NOI18N
        jPanel4.setLayout(null);

        jLabel11.setText(AppLocal.getIntString("label.sequence")); // NOI18N
        jPanel4.add(jLabel11);
        jLabel11.setBounds(20, 20, 140, 14);

        m_jSequence.setEditable(false);
        m_jSequence.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel4.add(m_jSequence);
        m_jSequence.setBounds(160, 20, 160, 20);

        jLabel2.setText(AppLocal.getIntString("Label.StartDate")); // NOI18N
        jPanel4.add(jLabel2);
        jLabel2.setBounds(20, 50, 140, 14);

        m_jMinDate.setEditable(false);
        m_jMinDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jMinDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jMinDateActionPerformed(evt);
            }
        });
        jPanel4.add(m_jMinDate);
        m_jMinDate.setBounds(160, 50, 160, 20);

        jLabel3.setText(AppLocal.getIntString("Label.EndDate")); // NOI18N
        jPanel4.add(jLabel3);
        jLabel3.setBounds(20, 80, 140, 14);

        m_jMaxDate.setEditable(false);
        m_jMaxDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel4.add(m_jMaxDate);
        m_jMaxDate.setBounds(160, 80, 160, 20);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(10, 10, 620, 120);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.paymentstitle"))); // NOI18N
        jPanel5.setLayout(null);

        m_jTicketTable.setFocusable(false);
        m_jTicketTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jTicketTable.setRequestFocusEnabled(false);
        m_jTicketTable.setShowVerticalLines(false);
        m_jScrollTableTicket.setViewportView(m_jTicketTable);

        jPanel5.add(m_jScrollTableTicket);
        m_jScrollTableTicket.setBounds(20, 20, 490, 130);

        jLabel1.setText("Amount Total");
        jPanel5.add(jLabel1);
        jLabel1.setBounds(520, 20, 70, 14);

        jTextField1.setEditable(false);
        jPanel5.add(jTextField1);
        jTextField1.setBounds(590, 20, 100, 20);

        jLabel4.setText("Tax Total");
        jPanel5.add(jLabel4);
        jLabel4.setBounds(520, 50, 70, 14);

        jTextField2.setEditable(false);
        jPanel5.add(jTextField2);
        jTextField2.setBounds(590, 50, 100, 20);

        jLabel5.setText("Total");
        jPanel5.add(jLabel5);
        jLabel5.setBounds(520, 80, 60, 14);

        jTextField3.setEditable(false);
        jPanel5.add(jTextField3);
        jTextField3.setBounds(590, 80, 100, 20);

        jPanel1.add(jPanel5);
        jPanel5.setBounds(10, 130, 740, 160);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.salestitle"))); // NOI18N
        jPanel6.setLayout(null);

        m_jSalesTotal.setEditable(false);
        m_jSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel6.add(m_jSalesTotal);
        m_jSalesTotal.setBounds(590, 100, 100, 20);

        m_jsalestable.setFocusable(false);
        m_jsalestable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jsalestable.setRequestFocusEnabled(false);
        m_jsalestable.setShowVerticalLines(false);
        m_jScrollSales.setViewportView(m_jsalestable);


        jPanel6.add(m_jScrollSales);
        m_jScrollSales.setBounds(20, 30, 490, 130);

	   m_jQtTable.setFocusable(false);
        m_jQtTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jQtTable.setRequestFocusEnabled(false);
        m_jQtTable.setShowVerticalLines(false);
        jScrollPane1.setViewportView(m_jQtTable);


        m_jSalesTaxes.setEditable(false);
        m_jSalesTaxes.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel6.add(m_jSalesTaxes);
        m_jSalesTaxes.setBounds(590, 70, 100, 20);

        m_jSalesSubtotal.setEditable(false);
        m_jSalesSubtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel6.add(m_jSalesSubtotal);
        m_jSalesSubtotal.setBounds(590, 40, 100, 20);

        jLabel6.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N
        jPanel6.add(jLabel6);
        jLabel6.setBounds(520, 40, 90, 14);

        jLabel12.setText(AppLocal.getIntString("label.taxcash")); // NOI18N
        jPanel6.add(jLabel12);
        jLabel12.setBounds(520, 70, 90, 14);

        jLabel7.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
        jPanel6.add(jLabel7);
        jLabel7.setBounds(520, 100, 90, 14);

        jPanel1.add(jPanel6);
        jPanel6.setBounds(10, 290, 740, 180);
        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(30, 490, 490, 80);

        jLabel8.setText("Pending QT");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(30, 470, 70, 14);

        m_jCloseCash2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line.png"))); // NOI18N
        m_jCloseCash2.setText(AppLocal.getIntString("Button.CloseCash")); // NOI18N
        m_jCloseCash2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseCash2ActionPerformed(evt);
            }
        });
        jPanel1.add(m_jCloseCash2);
        m_jCloseCash2.setBounds(530, 540, 50, 30);

        jLabel9.setText("Pending QT Total :");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(530, 480, 130, 14);

        jTextField4.setEditable(false);
        jPanel1.add(jTextField4);
        jTextField4.setBounds(530, 500, 120, 20);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        m_jCloseCash.setText(AppLocal.getIntString("Button.CloseCash")); // NOI18N
        m_jCloseCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseCashActionPerformed(evt);
            }
        });
        jPanel2.add(m_jCloseCash);

        jPanel3.add(jPanel2, java.awt.BorderLayout.LINE_END);

        add(jPanel3, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jCloseCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseCashActionPerformed
        // TODO add your handling code here:
        try{
       Transaction t = new Transaction(m_App.getSession()) {
       public Object transact() throws BasicException {
        String tempbool="false";
        int count=0;
        String tempstr;
        Date dNow = new Date();
        dNow.setTime(date.getTime());
        AppView appv=LookupUtilityImpl.getInstance(null).getAppView();
       // try{
            Object[] obj=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT COUNT(ID) FROM QTICKET WHERE  BILLED = FALSE AND CREATEDBY = ? AND REASON IS NULL "
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName());
                       
        if(obj[0]== null || obj==null)
            count=0;
        else{
           tempstr=obj[0].toString();
            count=Integer.parseInt(tempstr);
        }
       // }
      // catch(BasicException be)
      //  {
     //   }
        if(count>0)
        {
             JOptionPane.showMessageDialog(null,AppLocal.getIntString("message.qtreasonwarning"), AppLocal.getIntString("message.qtreasontitle"), JOptionPane.WARNING_MESSAGE);
        }
        else
        {
        int res = JOptionPane.showConfirmDialog(null, AppLocal.getIntString("message.wannaclosesale"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            
           
            String username=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName();
     //    java.sql.Timestamp dNowtemp=(java.sql.Timestamp)System.nanoTime();
            int count1=2;
           // try {
                // Cerramos la caja si esta pendiente de cerrar.
                
               Object[] con =(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT COUNT(*) FROM PEOPLE WHERE LOGINTIME IS NOT NULL AND ROLE = ?"
                        ,  SerializerWriteString.INSTANCE
                        , new SerializerReadBasic(new Datas[] {Datas.INT}))
                        .find(m_App.getAppUserView().getUser().getRole());
                  if(con!=null && con[0]!=null)
                      count1=Integer.parseInt(con[0].toString());
                  else
                      count1=2;
              //  } catch (BasicException e) {
             //   MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
             //   msg.show(null);
             //  }
               if(count1>1)
               {
                   JOptionPane.showMessageDialog(null, "Please Close Other Terminals", "Cannot Close Sale", JOptionPane.OK_OPTION);
          
               }
               else
               {
              //   try {
                     new StaticSentence(m_App.getSession()
                        , "UPDATE PEOPLE SET CLOSESALE = ? , LOGINTIME=?  WHERE ROLE = ?"
                        , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP,Datas.NULL,Datas.STRING }))
                        .exec(new Object[] {dNow,null, m_App.getAppUserView().getUser().getRole()});
               
              
                m_salesmodel.setDateEnd(dNow);
                
                AppUser user = m_App.getAppUserView().getUser();
                user.setCloseSaleTime(dNow);
               // m_dlSystem.updateUser(user);

                 //ID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,PAYMENTREF
              // AppUser suser=dlfac.getnewuserdetail(dlsys, sender);
                String narration="close sale from "+m_salesmodel.getDateStart()+" to "+m_salesmodel.getDateEnd();
                Date dnow=new Date();
                dnow.setTime(date.getTime());
                //bar
               // AppUser appuser=m_App.getAppUserView().getUser();
                String tid=UUID.randomUUID().toString();
               if(user.hasPermission("bar counter")){
                 Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"D","Close Sale",m_salesmodel.getSequence()+"", m_salesmodel.getTotal(),dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration,"3",0.0,dnow,null,true};
                 dlfac.insertintoaccjoutnal2(value);
                 java.util.List<SalessModel.ProductsAccountLine> list=m_salesmodel.getprodutsAccountwiseTotal();
                 Double amt=0.0;
                 for(SalessModel.ProductsAccountLine pacc:list){
                    if(pacc.getAccount()!=null){
                      Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"C","Close Sale",m_salesmodel.getSequence()+"",pacc.getAmount(),dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration,pacc.getAccount(),0.0,dnow,null,true};
                      dlfac.insertintoaccjoutnal2(value1);
                      amt+=pacc.getAmount();
                    }
                 }
                 if(amt!=m_salesmodel.getTotal()){
                  Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"C","Close Sale",m_salesmodel.getSequence()+"", m_salesmodel.getTotal()-amt,dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration,dlfac.getaccountidByName("Bar sales"),0.0,dnow,null,true};
                  dlfac.insertintoaccjoutnal2(value1);
                 }
               }
               else if(user.hasPermission("res counter")){
                  Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"D","Close Sale",m_salesmodel.getSequence()+"", m_salesmodel.getTotal(),dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration,"4",0.0,dnow,null,true};
                  dlfac.insertintoaccjoutnal2(value);
                   java.util.List<SalessModel.ProductsAccountLine> list=m_salesmodel.getprodutsAccountwiseTotal();
                Double amt=0.0;
                for(SalessModel.ProductsAccountLine pacc:list){
                    if(pacc.getAccount()!=null){
                     Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"C","Close Sale",m_salesmodel.getSequence()+"",pacc.getAmount(),dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration,pacc.getAccount(),0.0,dnow,null,true};
                     dlfac.insertintoaccjoutnal2(value1);
                     amt+=pacc.getAmount();
                    }
                }
                if(amt!=m_salesmodel.getTotal()){
                  Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"C","Close Sale",m_salesmodel.getSequence()+"", m_salesmodel.getTotal()-amt,dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration,"7",0.0,dnow,null,true};
                  dlfac.insertintoaccjoutnal2(value1);
                }
               
               Object[] obj1=(Object[])new StaticSentence(m_App.getSession()
                       , "SELECT VALUE,(SELECT VALUE FROM GENERALTABLE WHERE NAME = ?) FROM GENERALTABLE WHERE NAME = ?"
                       , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
                       ,new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING})).find(new Object[]{"Canteen Levy Account","Canteen Levy"});
               Double lpercentage=Double.parseDouble(obj1[0].toString());
               String narration1= "Canteen levy "+lpercentage+"% of the total sales";
               Double lamt=Double.valueOf(String.valueOf(dlfac.roundTwoDecimals((lpercentage * m_salesmodel.getTotal())/100)));
                if(lamt>0){
                  Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"D","Close Sale",m_salesmodel.getSequence()+"", lamt,dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration1 ,"7",0.0,dnow,null,true};
                  dlfac.insertintoaccjoutnal2(value1);
                  Object[] value2=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"C","Close Sale",m_salesmodel.getSequence()+"", lamt,dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration1 ,obj1[1],0.0,dnow,null,true};
                  dlfac.insertintoaccjoutnal2(value2);
                }
               }

               
                updatestockdiarydis();
                boolean flag=user.hasPermission("StockCheckNotRequired");
                if(flag==false){
                 String location=null;
                 java.util.List<SalessModel.ProductsLine> pdtlist= m_salesmodel.getProductLine();
                  Map<String,String> catwid=new HashMap<String,String>();
                 for(int i=0;i<pdtlist.size();i++){
                  SalessModel.ProductsLine pdtinfo=pdtlist.get(i);
                  location=catwid.get(pdtinfo.getpdtcategory());
                  if(location==null){
                   Object parentid=pdtinfo.getpdtcategory();
                   Object[] catdetail=new Object[3];
                  while(parentid!=null)
                   {
                      catdetail=  m_dlSales.getParentCategories(parentid.toString());
                      parentid=catdetail[0];
                   }
                 location=catdetail[2].toString();
                 catwid.put(pdtinfo.getpdtcategory(),location);
                  }
                 String id=UUID.randomUUID().toString();
                 Double tempv=0.0;
                 Integer temp1=pdtinfo.getqty() * -1;
                 Object[] values = new Object[] {id,user.getName(),dNow,-1,location,pdtinfo.getpdtid(),temp1.doubleValue(),tempv,"QT"};
                 Datas[] datas = new Datas[] {Datas.STRING,Datas.STRING,Datas.TIMESTAMP,Datas.INT,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING};
                 new PreparedSentence(m_App.getSession()
                , "INSERT INTO STOCKDIARY (ID,CREATEDBY,DATENEW,REASON1,LOCATION1,PRODUCT1,UNITS1,PRICE1,RNO) VALUES (?,?,?,?,?,?,?,?,?) "
                , new SerializerWriteBasicExt(datas, new int[] {0,1, 2, 3, 4, 5, 6, 7,8})).exec(values);
                 }
                }
                m_salesmodel.getSequence();
                new PreparedSentence(m_App.getSession()
                , "INSERT INTO CLOSEDSALE (ID,SEQUENCE,DATESTART,DATEEND,USER,AMOUNT,ROLE) VALUES (?,?,?,?,?,?,?) "
                , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.INT,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING,Datas.DOUBLE,Datas.STRING})).exec(new Object[]{UUID.randomUUID().toString(),m_salesmodel.getSequence(),m_salesmodel.getDateStart(),dNow,m_App.getAppUserView().getUser().getId(),m_salesmodel.getTotal(),m_App.getAppUserView().getUser().getRole()});
                 new PreparedSentence(m_App.getSession()
                        , "UPDATE BILL B SET CLOSESALESEQ=? WHERE CLOSESALESEQ IS NULL AND CREATEDDATE <= ? AND CREATEDBY IN (SELECT NAME FROM PEOPLE WHERE ROLE=? )"
                        , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{user.getRole()+" : "+m_salesmodel.getSequence(),date,m_App.getAppUserView().getUser().getRole()});
                  new PreparedSentence(m_App.getSession()
                       , "INSERT INTO RECEIPTS_ARV (ID,DATENEW,ATTRIBUTES,RUSER,PAYMENTREF,DESC,CLOSECASHSEQ) SELECT R.ID,R.DATENEW,R.ATTRIBUTES,R.RUSER,R.PAYMENTREF,R.DESC,R.CLOSECASHSEQ FROM RECEIPTS R,BILL B WHERE  R.CLOSECASHSEQ IS NOT NULL AND B.CLOSESALESEQ IS NOT NULL AND B.RECEIPT=R.ID "
                       , null).exec();
                 new PreparedSentence(m_App.getSession()
                       , "INSERT INTO RECEIPTS_ARV (ID,DATENEW,ATTRIBUTES,RUSER,PAYMENTREF,DESC,CLOSECASHSEQ) SELECT R.ID,R.DATENEW,R.ATTRIBUTES,R.RUSER,R.PAYMENTREF,R.DESC,R.CLOSECASHSEQ FROM RECEIPTS R  WHERE  R.CLOSECASHSEQ IS NOT NULL AND  R.ID NOT IN (SELECT RECEIPT FROM BILL) "
                       , null).exec();
                   new PreparedSentence(m_App.getSession()
                       , "INSERT INTO PAYMENTS_ARV (ID,RECEIPT,PAYMENT,TOTAL,PUSER,PTIME,CUSTOMER) SELECT P.ID,P.RECEIPT,P.PAYMENT,P.TOTAL,P.PUSER,P.PTIME,P.CUSTOMER FROM RECEIPTS R,BILL B,PAYMENTS P WHERE  R.CLOSECASHSEQ IS NOT NULL  AND R.ID=P.RECEIPT AND B.CLOSESALESEQ IS NOT NULL AND B.RECEIPT=R.ID "
                       , null).exec();
                      new PreparedSentence(m_App.getSession()
                       , "INSERT INTO PAYMENTS_ARV (ID,RECEIPT,PAYMENT,TOTAL,PUSER,PTIME,CUSTOMER) SELECT P.ID,P.RECEIPT,P.PAYMENT,P.TOTAL,P.PUSER,P.PTIME,P.CUSTOMER FROM RECEIPTS R,PAYMENTS P WHERE  R.CLOSECASHSEQ IS NOT NULL  AND R.ID=P.RECEIPT AND  R.ID NOT IN (SELECT RECEIPT FROM BILL) "
                       , null).exec();
               new PreparedSentence(m_App.getSession()
                       , "INSERT INTO BILL_ARV(ID,CUSTOMER,PLACE,WAITER,FLOOR,AMOUNT,CREATEDBY,CREATEDDATE,PAID,RECEIPT,TAXTOTAL,CLOSESALESEQ) SELECT B.ID,B.CUSTOMER,B.PLACE,B.WAITER,B.FLOOR,B.AMOUNT,B.CREATEDBY,B.CREATEDDATE,B.PAID,B.RECEIPT,B.TAXTOTAL,B.CLOSESALESEQ FROM BILL B,RECEIPTS R WHERE  R.CLOSECASHSEQ IS NOT NULL AND B.CLOSESALESEQ IS NOT NULL AND R.ID=B.RECEIPT"
                       , null).exec();
               new PreparedSentence(m_App.getSession()
                       , "INSERT INTO BILLITEM_ARV(ID,LINE,PARENTID,PRODUCT,DMULTIPLY,ATTRIBUTES,RATE,TOTAL) SELECT BI.ID,BI.LINE,BI.PARENTID,BI.PRODUCT,BI.DMULTIPLY,BI.ATTRIBUTES,BI.RATE,BI.TOTAL FROM BILLITEM BI,BILL B,RECEIPTS R WHERE  R.CLOSECASHSEQ IS NOT NULL AND B.CLOSESALESEQ IS NOT NULL AND R.ID=B.RECEIPT AND B.ID=BI.PARENTID"
                       , null).exec();
                new PreparedSentence(m_App.getSession()
               , "INSERT INTO QTICKET_ARV(ID,CUSTOMER,PLACE,WAITER,FLOOR,PRCATEGORY,BILLED,BILLREF,CREATEDBY,CRDATE,REASON)  SELECT Q.ID,Q.CUSTOMER,Q.PLACE,Q.WAITER,Q.FLOOR,Q.PRCATEGORY,Q.BILLED,Q.BILLREF,Q.CREATEDBY,Q.CRDATE,Q.REASON FROM QTICKET Q,BILL B,RECEIPTS R WHERE Q.BILLED=TRUE AND B.CLOSESALESEQ IS NOT NULL  AND B.ID=Q.BILLREF AND R.CLOSECASHSEQ IS NOT NULL AND R.ID=B.RECEIPT "
               , null).exec();

               new PreparedSentence(m_App.getSession()
               , "INSERT INTO QTITEMS_ARV (ID,LINE,PARENTID,PRODUCT,DMULTIPLY,RATE,ATTRIBUTES) SELECT Q.ID,Q.LINE,Q.PARENTID,Q.PRODUCT,Q.DMULTIPLY,Q.RATE,Q.ATTRIBUTES FROM QTITEMS Q,QTICKET QT,BILL B,RECEIPTS R WHERE QT.BILLED=TRUE  AND QT.ID=Q.PARENTID AND B.CLOSESALESEQ IS NOT NULL  AND B.ID=QT.BILLREF AND R.CLOSECASHSEQ IS NOT NULL AND R.ID=B.RECEIPT "
               , null).exec();
               new PreparedSentence(m_App.getSession()
                       , "DELETE FROM RECEIPTS R  WHERE R.CLOSECASHSEQ IS NOT NULL AND R.ID IN (SELECT RECEIPT FROM BILL WHERE CLOSESALESEQ IS NOT NULL) "
                       , null).exec();
                new PreparedSentence(m_App.getSession()
                       , "DELETE FROM RECEIPTS R  WHERE R.CLOSECASHSEQ IS NOT NULL AND  R.ID NOT IN (SELECT RECEIPT FROM BILL)"
                       , null).exec();

             /*  new PreparedSentence(m_App.getSession()
                        , "UPDATE BILL B SET CLOSESALESEQ=? WHERE CLOSESALESEQ IS NULL AND CREATEDDATE < ? AND CREATEDBY IN (SELECT NAME FROM PEOPLE WHERE ROLE=? )"
                        , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{user.getRole()+" : "+m_salesmodel.getSequence(),date,m_App.getAppUserView().getUser().getRole()});
               */
               printSales();
              //  printSales();
              //   out.close();
                // Mostramos el mensaje
                JOptionPane.showMessageDialog(null, "Press Ok Once the printing is complete", AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);
        
                 new StaticSentence(m_App.getSession()
                        , "UPDATE PEOPLE SET CLOSESALE = ? , LOGINTIME=?  WHERE ROLE = ?"
                        , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP,Datas.NULL ,Datas.STRING }))
                        .exec(new Object[] {dNow,null, m_App.getAppUserView().getUser().getRole()});
                 
                
          //  } catch (Exception e) {
          //      MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("label.noticketstoclose"), e);
          //      msg.show(null);
          //      e.printStackTrace();
         //   }
            // JPrincipalApp app= new JPrincipalApp();
             JPrincipalApp.m_approot.closeAppView();
        }
        }
      }
              return null;
         }
         };
         t.execute();
        }catch(Exception e){
           MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("label.noticketstoclose"), e);
           msg.show(this);
           e.printStackTrace();
        }
    }//GEN-LAST:event_m_jCloseCashActionPerformed

    private void m_jMinDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jMinDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jMinDateActionPerformed

    private void m_jCloseCash2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseCash2ActionPerformed
        // TODO add your handling code here:
        int row=m_jQtTable.getSelectedRow();
      String temp= (String)  m_salesmodel.getQtTableModel().getValueAt(row, 0);
       if (row < 0){
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
          String tempreason=(String) m_salesmodel.getQtTableModel().getValueAt(row, 5);

            Jpendingremarkedit.showMessage( this,m_App, tempreason,temp);
                // se ha modificado la linea

            
        }

    }//GEN-LAST:event_m_jCloseCash2ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton m_jCloseCash;
    private javax.swing.JButton m_jCloseCash2;
    private javax.swing.JTextField m_jMaxDate;
    private javax.swing.JTextField m_jMinDate;
    private javax.swing.JTextField m_jSalesSubtotal;
    private javax.swing.JTextField m_jSalesTaxes;
    private javax.swing.JTextField m_jSalesTotal;
    private javax.swing.JScrollPane m_jScrollSales;
    private javax.swing.JScrollPane m_jScrollTableTicket;
    private javax.swing.JTextField m_jSequence;
    private javax.swing.JTable m_jTicketTable;
    private javax.swing.JTable m_jsalestable;
     private javax.swing.JTable m_jQtTable;

    // End of variables declaration//GEN-END:variables
    
}
