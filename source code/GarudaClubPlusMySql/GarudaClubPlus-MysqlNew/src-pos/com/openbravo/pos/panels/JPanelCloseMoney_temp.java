

package com.openbravo.pos.panels;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import java.awt.*;
import java.text.ParseException;
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
//import com.openbravo.pos.admin.DetailsFetcher;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.clubmang.DebtTypeTableModel;
import com.openbravo.pos.clubmang.Facility;
//import com.openbravo.pos.clubmang.Periodicity;
//import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.clubmang.GeneralSettingInfo;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
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
import com.openbravo.pos.util.StringUtils;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author adrianromero
 */
public class JPanelCloseMoney_temp extends JPanel implements JPanelView, BeanFactoryApp {
    
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private DataLogicFacilities dlfac;
    private PaymentsModel m_PaymentsToClose = null;   
    private Date date;
    private TicketParser m_TTP;
    
    /** Creates new form JPanelCloseMoney */
    public JPanelCloseMoney_temp() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException {
        
        m_App = app;        
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        dlfac=(DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        m_jTicketTable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {new FormatsPayment(), Formats.CURRENCY}));
        m_jTicketTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));
        m_jTicketTable.getTableHeader().setReorderingAllowed(false);         
        m_jTicketTable.setRowHeight(25);
        m_jTicketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);         
        jButton3.setText("Detail");
        jButton4.setText("Detail");
        jButton5.setText("Detail");
        jButton6.setText("Detail");
        jButton7.setText("Detail");
        jButton8.setText("Detail");
        unr_text.setEditable(false);
        paymentout.setEditable(false);
        jTextField1.setEditable(false);
        jButton9.setText("Detail");
      /*  m_jotherincometable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.TIMESTAMP,Formats.STRING,Formats.STRING, Formats.CURRENCY}));
        m_jotherincometable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane5.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));
        m_jotherincometable.getTableHeader().setReorderingAllowed(false);
        m_jotherincometable.setRowHeight(25);
        m_jotherincometable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

         m_jRdTable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.TIMESTAMP,Formats.STRING,Formats.STRING,Formats.STRING, Formats.CURRENCY,Formats.STRING}));
        m_jRdTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       jScrollPane2.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));
        m_jRdTable.getTableHeader().setReorderingAllowed(false);
        m_jRdTable.setRowHeight(25);
        m_jRdTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

         debtrdetail.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.TIMESTAMP,Formats.STRING,Formats.STRING,Formats.STRING, Formats.CURRENCY,Formats.STRING}));
         debtrdetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       jScrollPane4.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));
         debtrdetail.getTableHeader().setReorderingAllowed(false);
         debtrdetail.setRowHeight(25);
         debtrdetail.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

          debtctable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.TIMESTAMP,Formats.STRING,Formats.STRING, Formats.CURRENCY}));
        debtctable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       jScrollPane3.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));
        debtctable.getTableHeader().setReorderingAllowed(false);
        debtctable.setRowHeight(25);
        debtctable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);*/
     //   DetailsFetcher df=new DetailsFetcher();
     //   df.accessConnector();
        cashinsum.setText(null);
        receipttotal.setText(null);
        debtctotal.setText(null);
        debtraised.setText(null);
        otherincometotal.setText(null);
        balance.setText(null);
     /*   jTextField1.setVisible(false);
        jTextField2.setVisible(false);
        jLabel9.setVisible(false);
         jLabel10.setVisible(false);*/
    }
    
    public Object getBean() {
        return this;
    }
    
    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.CloseTPV");
    }    
    
    public void activate() throws BasicException {
        loadData();
    }   
    
    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate   
        return true;
    }  
    
    private void loadData() throws BasicException {
        
        // Reset
        m_jSequence.setText(null);
       // m_jMinDate.setText(null);
        m_jMaxDate.setText(null);
        jButton1.setEnabled(false);
          cashinsum.setText(null);
        receipttotal.setText(null);
        debtctotal.setText(null);
        debtraised.setText(null);
        otherincometotal.setText(null);
        balance.setText(null);
      /*  jTextField1.setText(null);
        jTextField2.setText(null);*/
      //  m_jCount.setText(null); // AppLocal.getIntString("label.noticketstoclose");
    /*    m_jCash.setText(null);
         jTextField1.setText(null);
         jTextField2.setText(null);
         jTextField3.setText(null);
      //  m_jSales.setText(null);
      //  m_jSalesSubtotal.setText(null);
      //  m_jSalesTaxes.setText(null);
        m_jSalesTotal.setText(null);*/
        
        m_jTicketTable.setModel(new DefaultTableModel());
     //   m_jotherincometable.setModel(new DefaultTableModel());
            
        // LoadData
        date =new Date();
        m_PaymentsToClose = PaymentsModel.loadInstance(m_App,date);
        
        // Populate Data
        m_jSequence.setText(m_PaymentsToClose.printSequence());
      //  m_jMinDate.setText(m_PaymentsToClose.printDateStart());
        m_jMaxDate.setText(m_PaymentsToClose.printDateEnd());
        
     //   if (m_PaymentsToClose.getPayments() != 0 || m_PaymentsToClose.getSales() != 0) {

            jButton1.setEnabled(true);
              cashinsum.setText(m_PaymentsToClose.printincash());
              receipttotal.setText(m_PaymentsToClose.printreceiptTotalsum());
              debtctotal.setText(m_PaymentsToClose.printdebtcTotal());
              debtraised.setText(m_PaymentsToClose.printdebtrTotal());
              otherincometotal.setText(m_PaymentsToClose.getotherIncomeTotal());
              //otherincometotal.setText(m_PaymentsToClose.getGuestFee());
              balance.setText(m_PaymentsToClose.printbalance());
              paymentout.setText(m_PaymentsToClose.printpayTotal());
              jTable1.setModel(m_PaymentsToClose.getCounterModel());
              unr_text.setText(String.valueOf(m_PaymentsToClose.getUNClosedReceiptsTotal()));
           //   PaymentsModel.CounterTotals ct=new PaymentsModel.CounterTotals();
           /*   if(m_PaymentsToClose.getcountercount()==1)
              {
                  jTextField1.setVisible(true);

                  jTextField1.setText(m_PaymentsToClose.printReceiptTotal(0));
                 //  jTextField2.setVisible(false);
                   jLabel9.setVisible(true);
                   jLabel9.setText(m_PaymentsToClose.printReceiptCounter(0));

              } else if(m_PaymentsToClose.getcountercount()==2)
              {
                   jTextField1.setVisible(true);
                   jTextField2.setVisible(true);
                    jTextField1.setText(m_PaymentsToClose.printReceiptTotal(0));
                     jTextField2.setText(m_PaymentsToClose.printReceiptTotal(1));
                   jLabel9.setVisible(true);
                   jLabel10.setVisible(true);
                    jLabel9.setText(m_PaymentsToClose.printReceiptCounter(0));
                     jLabel10.setText(m_PaymentsToClose.printReceiptCounter(1));
              }*/
         //   m_jCount.setText(m_PaymentsToClose.printPayments());
       /*     m_jCash.setText(m_PaymentsToClose.printreceiptTotal());
            
          //  m_jSales.setText(m_PaymentsToClose.printSales());
         //   m_jSalesSubtotal.setText(m_PaymentsToClose.printSalesBase());
         //   m_jSalesTaxes.setText(m_PaymentsToClose.printSalesTaxes());
            m_jSalesTotal.setText(m_PaymentsToClose.printpayTotal());
            jTextField1.setText(m_PaymentsToClose.printreceiptTotal());
            jTextField2.setText(m_PaymentsToClose.printincash());
            jTextField3.setText(m_PaymentsToClose.printbalance());*/

      //  }
        
        m_jTicketTable.setModel(m_PaymentsToClose.getPaymentsModel1());
                
        TableColumnModel jColumns = m_jTicketTable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(150);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(100);
        jColumns.getColumn(1).setResizable(false);
        
     /*   m_jotherincometable.setModel(m_PaymentsToClose.getOtherIncomeTableModel());
        
        jColumns = m_jotherincometable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(150);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(100);
        jColumns.getColumn(1).setResizable(false);

        m_jRdTable.setModel(m_PaymentsToClose.getRdetailsModel());

        jColumns = m_jRdTable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(80);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(60);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(60);
        jColumns.getColumn(2).setResizable(false);
        jColumns.getColumn(3).setPreferredWidth(100);
        jColumns.getColumn(3).setResizable(false);
         jColumns.getColumn(4).setPreferredWidth(100);
        jColumns.getColumn(4).setResizable(false);
         jColumns.getColumn(5).setPreferredWidth(100);
        jColumns.getColumn(5).setResizable(false);

          debtrdetail.setModel(m_PaymentsToClose.getRdetails1Model());

        jColumns = debtrdetail.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(80);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(60);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(60);
        jColumns.getColumn(2).setResizable(false);
        jColumns.getColumn(3).setPreferredWidth(100);
        jColumns.getColumn(3).setResizable(false);
         jColumns.getColumn(4).setPreferredWidth(100);
        jColumns.getColumn(4).setResizable(false);
         jColumns.getColumn(5).setPreferredWidth(100);
        jColumns.getColumn(5).setResizable(false);

         debtctable.setModel(m_PaymentsToClose.getDebtraisedModel());

        jColumns = debtctable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(80);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(60);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(60);
        jColumns.getColumn(2).setResizable(false);
        jColumns.getColumn(3).setPreferredWidth(100);
        jColumns.getColumn(3).setResizable(false);*/
    }   
    
    private void printPayments(String remark) {
        
        String sresource = m_dlSystem.getResourceAsXML("Printer.CloseCash");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                boolean flag=false;
                if(m_App.getAppUserView().getUser().getRole().equals("")){
                    flag=true;
                }
                String displayName = m_App.getAppUserView().getUser().getName();
               // m_PaymentsToClose.getChequeDetailLines().size();
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("payments", m_PaymentsToClose);
                script.put("remark",StringUtils.encodeXML(remark) );
                script.put("cashier", flag);
                script.put("displayname", displayName);
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

    private class FormatsPayment extends Formats {
        protected String formatValueInt(Object value) {
            return AppLocal.getIntString("transpayment." + (String) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return value;
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }         
    }    
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        m_jSequence = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jMaxDate = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jTicketTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cashinsum = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        receipttotal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        debtctotal = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        debtraised = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        otherincometotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        balance = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        paymentout = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        unr_text = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        remark_text = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setAutoscrolls(true);
        jPanel1.setLayout(null);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.datestitle"))); // NOI18N
        jPanel4.setLayout(null);

        jLabel11.setText(AppLocal.getIntString("label.sequence")); // NOI18N
        jPanel4.add(jLabel11);
        jLabel11.setBounds(20, 20, 140, 14);
        jLabel11.getAccessibleContext().setAccessibleName(null);

        m_jSequence.setEditable(false);
        m_jSequence.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel4.add(m_jSequence);
        m_jSequence.setBounds(80, 20, 50, 20);

        jLabel3.setText(AppLocal.getIntString("Label.EndDate")); // NOI18N
        jPanel4.add(jLabel3);
        jLabel3.setBounds(190, 20, 140, 14);
        jLabel3.getAccessibleContext().setAccessibleName(null);

        m_jMaxDate.setEditable(false);
        m_jMaxDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel4.add(m_jMaxDate);
        m_jMaxDate.setBounds(470, 20, 160, 20);
        jPanel4.add(jPanel6);
        jPanel6.setBounds(330, 0, 10, 10);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(10, 0, 650, 50);

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel2.setAutoscrolls(true);
        jPanel2.setLayout(null);

        m_jTicketTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        m_jTicketTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(m_jTicketTable);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(10, 20, 350, 90);

        jLabel1.setText("Payment Details :");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(10, 4, 100, 14);

        jLabel8.setText("Cashin (A) :");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(10, 140, 150, 14);

        cashinsum.setEditable(false);
        jPanel2.add(cashinsum);
        cashinsum.setBounds(180, 140, 130, 20);

        jLabel12.setText("Receipt Total (B):");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(10, 180, 160, 14);

        receipttotal.setEditable(false);
        jPanel2.add(receipttotal);
        receipttotal.setBounds(180, 180, 130, 20);

        jLabel13.setText("Debt Collection Total (C):");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 220, 160, 14);

        debtctotal.setEditable(false);
        jPanel2.add(debtctotal);
        debtctotal.setBounds(180, 220, 130, 20);

        jLabel14.setText("Debt Raised Total (E):");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(10, 300, 160, 14);

        debtraised.setEditable(false);
        jPanel2.add(debtraised);
        debtraised.setBounds(180, 300, 130, 20);

        jLabel15.setText("Other Income (D) :");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(10, 260, 160, 14);

        otherincometotal.setEditable(false);
        jPanel2.add(otherincometotal);
        otherincometotal.setBounds(180, 260, 130, 20);

        jLabel16.setText("Balance (A+B+C+D-E) :");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(10, 390, 160, 14);

        balance.setEditable(false);
        jPanel2.add(balance);
        balance.setBounds(180, 390, 150, 20);

        jLabel17.setText("Cheque Details :");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(460, 180, 100, 14);

        jButton2.setText("Cheque Detail");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);
        jButton2.setBounds(580, 180, 120, 23);

        jLabel18.setText("Counter wise Total");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(390, 0, 140, 14);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(390, 20, 350, 90);

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);
        jButton3.setBounds(360, 180, 90, 23);

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);
        jButton4.setBounds(360, 220, 90, 23);

        jButton5.setText("jButton5");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);
        jButton5.setBounds(360, 260, 90, 23);

        jButton6.setText("jButton6");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);
        jButton6.setBounds(360, 300, 90, 23);

        jLabel19.setText("Payments/cash out ");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(10, 340, 100, 14);
        jPanel2.add(paymentout);
        paymentout.setBounds(180, 340, 130, 20);

        jButton7.setText("jButton7");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7);
        jButton7.setBounds(360, 340, 90, 23);

        jLabel4.setText("UnClosed Receipts");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(460, 220, 110, 14);
        jPanel2.add(unr_text);
        unr_text.setBounds(580, 220, 120, 20);

        jButton8.setText("jButton8");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton8);
        jButton8.setBounds(670, 250, 73, 23);

        remark_text.setColumns(20);
        remark_text.setRows(5);
        jScrollPane4.setViewportView(remark_text);

        jPanel2.add(jScrollPane4);
        jScrollPane4.setBounds(10, 450, 700, 96);

        jLabel5.setText("Remark");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(10, 430, 150, 14);

        jLabel2.setText("Pending Credit Conf");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(460, 280, 110, 14);
        jPanel2.add(jTextField1);
        jTextField1.setBounds(580, 280, 120, 20);

        jButton9.setText("jButton9");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton9);
        jButton9.setBounds(670, 310, 73, 23);

        jScrollPane1.setViewportView(jPanel2);

        jPanel3.add(jScrollPane1);

        jScrollPane7.setViewportView(jPanel3);

        jPanel1.add(jScrollPane7);
        jScrollPane7.setBounds(10, 60, 760, 560);

        jButton1.setText("Close Cash");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(670, 30, 110, 23);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

/*  public static String now() {
    java.util.Calendar cal = java.util.Calendar.getInstance();
   java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT_NOW);
    return sdf.format(cal.getTime());

  }*/

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try{
     Transaction t = new Transaction(m_App.getSession()) {
       public Object transact() throws BasicException {
        int flag=0;
        LookupUtilityImpl.getInstance(null).reloadGeneralSettingsMap();
        GeneralSettingInfo gcardaAccinfo=LookupUtilityImpl.getInstance(null).getGeneralSettingsMap().get("Cards Room Collection Account");
        //Cards Room Control Account
        GeneralSettingInfo gcardCtrlAccinfo=LookupUtilityImpl.getInstance(null).getGeneralSettingsMap().get("Cards Room Control Account");
        int res = JOptionPane.showConfirmDialog(null, AppLocal.getIntString("message.wannaclosecash"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            DecimalFormat format = new DecimalFormat("############################.##");
            double debittotal=m_PaymentsToClose.getbalance()-m_PaymentsToClose.getdebtcTotal()+m_PaymentsToClose.getDebtRaised();
            double credittotal=m_PaymentsToClose.getReceipttotalsum()+m_PaymentsToClose.getotherIncomeTotal1();
            
            debittotal = Double.parseDouble(format.format(debittotal));
            credittotal = Double.parseDouble(format.format(credittotal));
            
            if(debittotal==credittotal){   //Arun
             Date dNow = new Date();
             dNow.setTime(date.getTime());
            String username=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName();
            String remark=null;
            remark=remark_text.getText().trim();
            remark=StringUtils.encodeXML(remark);
           // try {
                  if (m_App.getActiveCashDateEnd() == null) {
               // {
                    new StaticSentence(m_App.getSession()
                            //praveen:changed user to user_
                        , "UPDATE CLOSEDCASH SET DATEEND = ?,REMARK=?,HOST = ?,HOSTSEQUENCE=? WHERE   USER_ = ? AND HOSTSEQUENCE<0"
                        , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.STRING,Datas.STRING,Datas.INT, Datas.STRING}))
                        .exec(new Object[] {dNow,remark,m_App.getProperties().getHost(),m_PaymentsToClose.getSequence() , m_App.getAppUserView().getUser().getId()});
                }
                m_App.setActiveCash(UUID.randomUUID().toString(),-1, dNow, null);

                // creamos la caja activa
              //  m_dlSystem.execInsertCash(
                //        new Object[] {m_App.getActiveCashIndex(),  -1,m_App.getProperties().getHost(), m_App.getActiveCashDateStart(), m_App.getActiveCashDateEnd()});
                  new StaticSentence(m_App.getSession()
                        , "UPDATE PEOPLE SET CLOSECASHTIME = ?,OPENCASHTIME=? , LOGINTIME=? WHERE NAME = ?"
                        , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP,Datas.NULL,Datas.NULL, Datas.STRING }))
                        .exec(new Object[] {dNow,null,null,username});
                // ponemos la fecha de fin
                m_PaymentsToClose.setDateEnd(dNow);
                // Imprimimos el miniinforme

               
                 String tid=UUID.randomUUID().toString();
                 String narration="Close cash on "+m_PaymentsToClose.getDateEnd();
                if( m_PaymentsToClose.getbalance()-m_PaymentsToClose.getdebtcTotal()-m_PaymentsToClose.getOChequeAmount()>0){
                 Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,dNow,"D","Close cash",m_PaymentsToClose.getSequence()+"", m_PaymentsToClose.getbalance()-m_PaymentsToClose.getdebtcTotal()-m_PaymentsToClose.getOChequeAmount(),dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration,m_App.getAppUserView().getUser().getcashaccount(),0.0,dNow,null,true};
                 dlfac.insertintoaccjoutnal2(value);
                }
                if(m_PaymentsToClose.getOChequeAmount()>0){
                  Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,null,dNow,"D","Close cash",m_PaymentsToClose.getSequence()+"", m_PaymentsToClose.getOChequeAmount(),dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration,m_App.getAppUserView().getUser().getchequeaccount(),0.0,dNow,null,true};
                  dlfac.insertintoaccjoutnal2(value1);
                }
                java.util.List<PaymentsModel.RdetailsLine1> debtr=m_PaymentsToClose.getRdetalLines1();
               //warehouse changes - start
                for(PaymentsModel.RdetailsLine1 r:debtr){
//                  String s1="RA";
//                  String s2="BA";
              //   if(r.getbillid().startsWith(s2))
                 {
                Facility barfac=dlfac.getFacilitybyID(r.getFacilityId());
                 DebtTypeTableModel.DebtTypeline periodtype=dlfac.getDebtTypebyid(barfac.getdueperiod());
                 int ptype=4;
                 if(periodtype.getperiod().equals("Days"))
                     ptype=1;
                 else if(periodtype.getperiod().equals("Months"))
                     ptype=2;
                 else if(periodtype.getperiod().equals("Years"))
                     ptype=3;
                    Date d=new Date();
                    d.setTime(date.getTime());
                    d.setTime(r.getbDate().getTime());
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(d.getTime());
                    switch(ptype){
                        case 1:  cal.add(Calendar.DATE, periodtype.getNum());
                                 break;
                        case 2: cal.add(Calendar.MONTH, periodtype.getNum());
                                 break;
                        case 3: cal.add(Calendar.YEAR, periodtype.getNum());
                                 break;
                        case 4: break;
                  }
                     d.setTime(cal.getTimeInMillis());
                   //  Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,r.getCid(),r.getbDate(),"D",r.getFacilityId(),r.getrId(), r.getrTotal(),d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"for bill no "+r.getbillid()+" billed on "+r.printBDate(),r.getAccount(),r.getrTotal(),null,null,true};
                    //praveen:changed date field ----from billed date to closecash date
                     Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,r.getCid(),dNow,"D",r.getFacilityId(),r.getrId(), r.getrTotal(),d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"for bill no "+r.getbillid()+" billed on "+r.printBDate(),r.getAccount(),r.getrTotal(),null,null,true};
                    dlfac.insertintoaccjoutnal2(valuer);
                    dlfac.setmemberDebt(r.getCid(), "1", r.getrTotal());//bar facility
                }

//                 else if(r.getbillid().startsWith(s1)){
//                 Facility Resfac=dlfac.getFacilitybyID("2");
//                 DebtTypeTableModel.DebtTypeline periodtype1=dlfac.getDebtTypebyid(Resfac.getdueperiod());
//                 int ptype1=4;
//                 if(periodtype1.getperiod().equals("Days"))
//                     ptype1=1;
//                 else if(periodtype1.getperiod().equals("Months"))
//                     ptype1=2;
//                 else if(periodtype1.getperiod().equals("Years"))
//                     ptype1=3;
//                     Date d=new Date();
//                    d.setTime(date.getTime());
//                    d.setTime(r.getbDate().getTime());
//                    Calendar cal=Calendar.getInstance();
//                    cal.setTimeInMillis(d.getTime());
//                    switch(ptype1){
//                        case 1:  cal.add(Calendar.DATE, periodtype1.getNum());
//                                 break;
//                        case 2: cal.add(Calendar.MONTH, periodtype1.getNum());
//                                 break;
//                        case 3: cal.add(Calendar.YEAR, periodtype1.getNum());
//                                 break;
//                        case 4: break;
//                  }
//                     d.setTime(cal.getTimeInMillis());
//                    Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,r.getCid(),r.getbDate(),"D","2",r.getrId(), r.getrTotal(),d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"for bill no "+r.getbillid()+" billed on "+r.printBDate(),r.getAccount(),r.getrTotal(),null,null,true};
//                    dlfac.insertintoaccjoutnal2(valuer);
//                    dlfac.setmemberDebt(r.getCid(), "2", r.getrTotal());//bar facility
//                 }
                }
                //warehouse changes - end
               /* java.util.List<PaymentsModel.RdetailsLine1> debtr=m_PaymentsToClose.getRdetalLines1();
               
                Facility barfac=dlfac.getFacilitybyID("1");
                 DebtTypeTableModel.DebtTypeline periodtype=dlfac.getDebtTypebyid(barfac.getdueperiod());
                 int ptype=4;
                 if(periodtype.getperiod().equals("Days"))
                     ptype=1;
                 else if(periodtype.getperiod().equals("Months"))
                     ptype=2;
                 else if(periodtype.getperiod().equals("Years"))
                     ptype=3;
                 for(PaymentsModel.RdetailsLine1 r:debtr){
                    Date d=new Date();
                    d.setTime(date.getTime());
                    d.setTime(r.getbDate().getTime());
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(d.getTime());
                    switch(ptype){
                        case 1:  cal.add(Calendar.DATE, periodtype.getNum());
                                 break;
                        case 2: cal.add(Calendar.MONTH, periodtype.getNum());
                                 break;
                        case 3: cal.add(Calendar.YEAR, periodtype.getNum());
                                 break;
                        case 4: break;
                  }
                     d.setTime(cal.getTimeInMillis());

                    Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,r.getCid(),r.getbDate(),"D","1",r.getrId(), r.getrTotal(),d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"for bill no "+r.getbillid()+" billed on "+r.printBDate(),r.getAccount(),r.getrTotal(),null,null,true};
                    dlfac.insertintoaccjoutnal2(valuer);
                    dlfac.setmemberDebt(r.getCid(), "1", r.getrTotal());//bar facility
                }
                 Facility Resfac=dlfac.getFacilitybyID("2");
                 DebtTypeTableModel.DebtTypeline periodtype1=dlfac.getDebtTypebyid(Resfac.getdueperiod());
                 int ptype1=4;
                 if(periodtype1.getperiod().equals("Days"))
                     ptype1=1;
                 else if(periodtype1.getperiod().equals("Months"))
                     ptype1=2;
                 else if(periodtype1.getperiod().equals("Years"))
                     ptype1=3;
                 for(PaymentsModel.RdetailsLine1 r:debtr){
                    Date d=new Date();
                    d.setTime(date.getTime());
                    d.setTime(r.getbDate().getTime());
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(d.getTime());
                    switch(ptype1){
                        case 1:  cal.add(Calendar.DATE, periodtype1.getNum());
                                 break;
                        case 2: cal.add(Calendar.MONTH, periodtype1.getNum());
                                 break;
                        case 3: cal.add(Calendar.YEAR, periodtype1.getNum());
                                 break;
                        case 4: break;
                  }
                     d.setTime(cal.getTimeInMillis());
                    Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,r.getCid(),r.getbDate(),"D","2",r.getrId(), r.getrTotal(),d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"for bill no "+r.getbillid()+" billed on "+r.printBDate(),r.getAccount(),r.getrTotal(),null,null,true};
                    dlfac.insertintoaccjoutnal2(valuer);
                    dlfac.setmemberDebt(r.getCid(), "2", r.getrTotal());//bar facility
                }*/
                 java.util.List<PaymentsModel.OtherIncomeTotalLine> OIncome=m_PaymentsToClose.getOtherIncomeTotalLines();
                for(PaymentsModel.OtherIncomeTotalLine o:OIncome){
                    String accid=o.getPtype();
                    String[] accs=accid.split("#");
                    for(int i=0;i<accs.length;i++){
                     Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,null,dNow,"C",o.getDesc(),m_PaymentsToClose.getSequence()+"", Double.valueOf(accs[i+1]),dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration+" other income",accs[i],0.0,dNow,null,true};
                    //ID,TID,MEMID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,PAYMENTREF
                     dlfac.insertintoaccjoutnal2(valuer);
                     i++;
                    }
                }
                //Guest cat changes start
                if(m_PaymentsToClose.getGuestFee()>0){
                    java.util.List<PaymentsModel.GuestReceiptdetailsLine> guestDetails=m_PaymentsToClose.getGuestReceiptDetails();
                    for(PaymentsModel.GuestReceiptdetailsLine line:guestDetails){
                        Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,null,dNow,"C","Close cash Guest Fees -" + line.getName(),m_PaymentsToClose.getSequence()+"", line.getAmount(),dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration+" other income",line.getAccount(),0.0,dNow,null,true};
                        //ID,TID,MEMID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,PAYMENTREF
                        dlfac.insertintoaccjoutnal2(valuer);
                    }
                }
                //Guest cat changes end
                if(m_PaymentsToClose.getCardsRoomIncome()>0){
                    if(gcardaAccinfo!=null && gcardaAccinfo.getValue()!=null){
                  Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,null,dNow,"C","Close cash Cards Room Income",m_PaymentsToClose.getSequence()+"", m_PaymentsToClose.getCardsRoomIncome(),dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration+" other income",gcardaAccinfo.getValue(),0.0,dNow,null,true};
                    //ID,TID,MEMID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,PAYMENTREF
                    dlfac.insertintoaccjoutnal2(valuer);
                    }
                }
                double amt=m_PaymentsToClose.getIssuedTokenamount()-m_PaymentsToClose.getReceivedTokenAmount();
                if(amt!=0 || amt!=-0){
                      String tid1=UUID.randomUUID().toString();
                      if(amt>0){
                         Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid1,null,dNow,"D","Cards Token",m_PaymentsToClose.getSequence()+"", amt,dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Extra tokens issued",m_App.getAppUserView().getUser().getcashaccount(),0.0,dNow,null,true};
                    //ID,TID,MEMID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,PAYMENTREF
                         dlfac.insertintoaccjoutnal2(valuer);
                          valuer=new Object[]{UUID.randomUUID().toString(),tid1,null,dNow,"C","Cards Token",m_PaymentsToClose.getSequence()+"", amt,dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Extra tokens issued",gcardCtrlAccinfo.getValue(),0.0,dNow,null,true};
                    //ID,TID,MEMID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,PAYMENTREF
                          dlfac.insertintoaccjoutnal2(valuer);
                      }else{
                           Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid1,null,dNow,"C","Cards Token",m_PaymentsToClose.getSequence()+"", amt*-1,dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Extra tokens returned",m_App.getAppUserView().getUser().getcashaccount(),0.0,dNow,null,true};
                    //ID,TID,MEMID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,PAYMENTREF
                         dlfac.insertintoaccjoutnal2(valuer);
                         valuer=new Object[]{UUID.randomUUID().toString(),tid1,null,dNow,"D","Cards Token",m_PaymentsToClose.getSequence()+"",amt*-1,dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Extra tokens returned",gcardCtrlAccinfo.getValue(),0.0,dNow,null,true};
                    //ID,TID,MEMID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE,PAYMENTREF
                          dlfac.insertintoaccjoutnal2(valuer);
                      }
                }
                //warehouse changes - start
                if(m_PaymentsToClose.getcountercount()>0){
             for(PaymentsModel.WarehouseTotals whTotals : m_PaymentsToClose.getWareHouseTotals()){
              //  if(m_PaymentsToClose.printReceiptCounter(k).equals("Counter role")){
                     Object[] value2=new Object[]{UUID.randomUUID().toString(),tid,null,dNow,"C","Close cash",m_PaymentsToClose.getSequence()+"", whTotals.getamount(),dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration+" restaurant",whTotals.getCustomerCurrentAccount(),0.0,dNow,null,true};
                     dlfac.insertintoaccjoutnal2(value2);
                
               }
                }
                //warehouse changes - end
                /*
                 *
                  new PreparedSentence(m_App.getSession()
               , "INSERT INTO QTICKET_ARV(ID,CUSTOMER,PLACE,WAITER,FLOOR,PRCATEGORY,BILLED,BILLREF,CREATEDBY,CRDATE,REASON)  SELECT Q.ID,Q.CUSTOMER,Q.PLACE,Q.WAITER,Q.FLOOR,Q.PRCATEGORY,Q.BILLED,Q.BILLREF,Q.CREATEDBY,Q.CRDATE,Q.REASON FROM QTICKET Q WHERE Q.BILLED=TRUE AND  Q.CREATEDBY IN (SELECT NAME FROM PEOPLE WHERE ROLE=? ) AND Q.CRDATE=? "
               , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP})).exec(new Object[]{m_App.getAppUserView().getUser().getRole(),date});

               new PreparedSentence(m_App.getSession()
               , "INSERT INTO QTITEMS_ARV (ID,LINE,PARENTID,PRODUCT,DMULTIPLY,RATE,ATTRIBUTES) SELECT Q.ID,Q.LINE,Q.PARENTID,Q.PRODUCT,Q.DMULTIPLY,Q.RATE,Q.ATTRIBUTES FROM QTITEMS Q,QTICKET QT WHERE QT.BILLED=TRUE AND  QT.CREATEDBY IN (SELECT NAME FROM PEOPLE WHERE ROLE=? ) AND QT.CRDATE=?  AND QT.ID=Q.PARENTID  "
               , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP})).exec(new Object[]{m_App.getAppUserView().getUser().getRole(),date});

              new PreparedSentence(m_App.getSession()
               , "DELETE FROM QTICKET WHERE  QTICKET.BILLED=TRUE AND QTICKET.CRDATE < ? AND QTICKET.CREATEDBY IN (SELECT NAME FROM PEOPLE WHERE ROLE=? ) "
               , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{date,m_App.getAppUserView().getUser().getRole()});
               new PreparedSentence(m_App.getSession()
                        , "UPDATE BILL B SET CLOSESALESEQ=? WHERE CLOSESALESEQ IS NULL AND CREATEDDATE < ? AND CREATEDBY IN (SELECT NAME FROM PEOPLE WHERE ROLE=? )"
                        , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{user.getRole()+" : "+m_salesmodel.getSequence(),date,m_App.getAppUserView().getUser().getRole()});
                new PreparedSentence(m_App.getSession()
                       , "INSERT INTO BILL(ID,CUSTOMER,PLACE,WAITER,FLOOR,AMOUNT,CREATEDBY,CREATEDDATE,PAID,RECEIPT,TAXTOTAL,CLOSESALESEQ) SELECT B.ID,B.CUSTOMER,B.PLACE,B.WAITER,B.FLOOR,B.AMOUNT,B.CREATEDBY,B.CREATEDDATE,B.PAID,B.RECEIPT,B.TAXTOTAL,B.CLOSESALESEQ FROM BILL B WHERE  B.CLOSESALESEQ IS NOT NULL AND B.RECEIPT NOT IN (SELECT ID FROM RECEIPTS ) "
                       , null).exec();
               new PreparedSentence(m_App.getSession()
                       , "INSERT INTO BILLITEM(ID,LINE,PARENTID,PRODUCT,DMULTIPLY,ATTRIBUTES,RATE,TOTAL) SELECT BI.ID,BI.LINE,BI.PARENTID,BI.PRODUCT,BI.DMULTIPLY,BI.ATTRIBUTES,BI.RATE,BI.TOTAL FROM BILLITEM BI,BILL B WHERE  B.CLOSESALESEQ IS NOT NULL AND B.RECEIPT NOT IN (SELECT ID FROM RECEIPTS ) "
                       , null).exec();
               new PreparedSentence(m_App.getSession()
                       , "DELETE FROM BILL WHERE  BILL.RECEIPT IS NOT NULL AND BILL.RECEIPT NOT IN (SELECT ID FROM RECEIPT )"
                       , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{date,m_App.getAppUserView().getUser().getName()});
                 * */
                //   Object[] value3=new Object[]{UUID.randomUUID().toString(),null,dNow,"C","Close cash","1", m_PaymentsToClose.getdebtcTotal(),dNow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),narration+" debt collected","debt collection account",0.0,dNow,null};
                //   dlfac.insertintoaccjoutnal2(value3);
                /* " SELECT RECEIPTS.DATENEW,RECEIPTS.ID,BILL.ID,BILL.CUSTOMER,SUM(BILL.AMOUNT + BILL.TAXTOTAL),ROLES.NAME "+
                " FROM RECEIPTS,BILL,PEOPLE P,PEOPLE P1,ROLES "+
                " WHERE BILL.RECEIPT=RECEIPTS.ID AND BILL.CREATEDBY=P.NAME AND P.ROLE=ROLES.ID  AND RECEIPTS.RUSER = ? AND RECEIPTS.RUSER=P1.NAME AND  RECEIPTS.DATENEW > P1.OPENCASHTIME AND P1.CLOSECASHTIME IS NULL  "+
               " GROUP BY RECEIPTS.ID,RECEIPTS.DATENEW,BILL.CUSTOMER,ROLES.NAME ORDER BY ROLES.NAME"*/
                new PreparedSentence(m_App.getSession()
                        , "UPDATE TOKENLOG SET CLOSECASHREF=? WHERE CLOSECASHREF IS NULL AND CREATEDBY=?"
                        , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})).exec(new Object[]{m_App.getAppUserView().getUser().getName()+" : "+m_PaymentsToClose.printSequence(),m_App.getAppUserView().getUser().getName()});
               // new PreparedSentence(m_App.getSession()
                new PreparedSentence(m_App.getSession()
                        , "UPDATE RECEIPTS SET CLOSECASHSEQ=? WHERE CLOSECASHSEQ IS NULL AND DATENEW < ? AND RUSER=?"
                        , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{m_PaymentsToClose.printHost()+" : "+m_PaymentsToClose.printSequence(),date,m_App.getAppUserView().getUser().getName()});
                //praveen:confirmer changes---satrt
                new PreparedSentence(m_App.getSession()
                       , "INSERT INTO RECEIPTS_ARV (ID,DATENEW,ATTRIBUTES,RUSER,PAYMENTREF,DESC_,CLOSECASHSEQ,CONFIRMER) SELECT R.ID,R.DATENEW,R.ATTRIBUTES,R.RUSER,R.PAYMENTREF,R.DESC_,R.CLOSECASHSEQ,R.CONFIRMER FROM RECEIPTS R,BILL B WHERE  R.CLOSECASHSEQ IS NOT NULL AND B.CLOSESALESEQ IS NOT NULL AND B.RECEIPT=R.ID"
                       , null).exec();
                 new PreparedSentence(m_App.getSession()
                       , "INSERT INTO RECEIPTS_ARV (ID,DATENEW,ATTRIBUTES,RUSER,PAYMENTREF,DESC_,CLOSECASHSEQ,CONFIRMER) SELECT R.ID,R.DATENEW,R.ATTRIBUTES,R.RUSER,R.PAYMENTREF,R.DESC_,R.CLOSECASHSEQ,R.CONFIRMER FROM RECEIPTS R  WHERE  R.CLOSECASHSEQ IS NOT NULL AND  R.ID NOT IN (SELECT RECEIPT FROM BILL) "
                       , null).exec();
                 //praveen:confirmer changes---end
                 new PreparedSentence(m_App.getSession()
                       , "INSERT INTO PAYMENTS_ARV (ID,RECEIPT,PAYMENT,TOTAL,PUSER,PTIME,CUSTOMER) SELECT P.ID,P.RECEIPT,P.PAYMENT,P.TOTAL,P.PUSER,P.PTIME,P.CUSTOMER FROM RECEIPTS R,BILL B,PAYMENTS P WHERE  R.CLOSECASHSEQ IS NOT NULL  AND R.ID=P.RECEIPT AND B.CLOSESALESEQ IS NOT NULL AND B.RECEIPT=R.ID "
                       , null).exec();
                      new PreparedSentence(m_App.getSession()
                       , "INSERT INTO PAYMENTS_ARV (ID,RECEIPT,PAYMENT,TOTAL,PUSER,PTIME,CUSTOMER) SELECT P.ID,P.RECEIPT,P.PAYMENT,P.TOTAL,P.PUSER,P.PTIME,P.CUSTOMER FROM RECEIPTS R,PAYMENTS P WHERE  R.CLOSECASHSEQ IS NOT NULL  AND R.ID=P.RECEIPT AND  R.ID NOT IN (SELECT RECEIPT FROM BILL) "
                       , null).exec();
               //praveen:initiator changes---satrt
                      new PreparedSentence(m_App.getSession()
                       , "INSERT INTO BILL_ARV(ID,CUSTOMER,PLACE,WAITER,FLOOR,AMOUNT,CREATEDBY,CREATEDDATE,PAID,RECEIPT,TAXTOTAL,CLOSESALESEQ,WAREHOUSE,INITIATOR) SELECT B.ID,B.CUSTOMER,B.PLACE,B.WAITER,B.FLOOR,B.AMOUNT,B.CREATEDBY,B.CREATEDDATE,B.PAID,B.RECEIPT,B.TAXTOTAL,B.CLOSESALESEQ,B.WAREHOUSE,B.INITIATOR FROM BILL B,RECEIPTS R WHERE  R.CLOSECASHSEQ IS NOT NULL AND B.CLOSESALESEQ IS NOT NULL AND R.ID=B.RECEIPT"
                       , null).exec();
               new PreparedSentence(m_App.getSession()
                       , "INSERT INTO BILLITEM_ARV(ID,LINE,PARENTID,PRODUCT,DMULTIPLY,ATTRIBUTES,RATE,TOTAL,Tax1ID,Tax1,Tax2ID,T2_Cas,Tax2,Tax3ID,T3_Cas,Tax3) SELECT BI.ID,BI.LINE,BI.PARENTID,BI.PRODUCT,BI.DMULTIPLY,BI.ATTRIBUTES,BI.RATE,BI.TOTAL,BI.Tax1ID,BI.Tax1,BI.Tax2ID,BI.T2_Cas,BI.Tax2,BI.Tax3ID,BI.T3_Cas,BI.Tax3 FROM BILLITEM BI,BILL B,RECEIPTS R WHERE  R.CLOSECASHSEQ IS NOT NULL AND B.CLOSESALESEQ IS NOT NULL AND R.ID=B.RECEIPT AND B.ID=BI.PARENTID"
                       , null).exec();
                new PreparedSentence(m_App.getSession()
               , "INSERT INTO QTICKET_ARV(ID,CUSTOMER,PLACE,WAITER,FLOOR,PRCATEGORY,BILLED,BILLREF,CREATEDBY,CRDATE,REASON,INITIATOR)  SELECT Q.ID,Q.CUSTOMER,Q.PLACE,Q.WAITER,Q.FLOOR,Q.PRCATEGORY,Q.BILLED,Q.BILLREF,Q.CREATEDBY,Q.CRDATE,Q.REASON,Q.INITIATOR FROM QTICKET Q,BILL B,RECEIPTS R WHERE Q.BILLED=TRUE AND B.CLOSESALESEQ IS NOT NULL  AND B.ID=Q.BILLREF AND R.CLOSECASHSEQ IS NOT NULL AND R.ID=B.RECEIPT "
               , null).exec();
                //praveen:initiator changes---end

               new PreparedSentence(m_App.getSession()
               , "INSERT INTO QTITEMS_ARV (ID,LINE,PARENTID,PRODUCT,DMULTIPLY,RATE,ATTRIBUTES) SELECT Q.ID,Q.LINE,Q.PARENTID,Q.PRODUCT,Q.DMULTIPLY,Q.RATE,Q.ATTRIBUTES FROM QTITEMS Q,QTICKET QT,BILL B,RECEIPTS R WHERE QT.BILLED=TRUE  AND QT.ID=Q.PARENTID AND B.CLOSESALESEQ IS NOT NULL  AND B.ID=QT.BILLREF AND R.CLOSECASHSEQ IS NOT NULL AND R.ID=B.RECEIPT "
               , null).exec();
               new PreparedSentence(m_App.getSession()
                       , "DELETE FROM RECEIPTS   WHERE CLOSECASHSEQ IS NOT NULL AND ID IN (SELECT RECEIPT FROM BILL WHERE CLOSESALESEQ IS NOT NULL) "
                       , null).exec();
                new PreparedSentence(m_App.getSession()
                       , "DELETE FROM RECEIPTS   WHERE CLOSECASHSEQ IS NOT NULL AND  ID NOT IN (SELECT RECEIPT FROM BILL) "
                       , null).exec();
                 printPayments(remark);
                 printPayments(remark);
                // Mostramos el mensaje
                //JOptionPane.showMessageDialog(null, AppLocal.getIntString("message.closecashok"), AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);
                loadData();
                JPrincipalApp.m_approot.closeAppView();
            }else{
               JOptionPane.showMessageDialog(null, "Debit is not equal to credit","Cannot Close Cash", JOptionPane.ERROR_MESSAGE);
            }
        }

           return null;
         }
         };
         t.execute();
        }catch(Exception e){
               MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
                e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        ChequeDetail cd=ChequeDetail.getDialog(this, m_PaymentsToClose);
        try{
            boolean flag=  cd.showDialog(m_PaymentsToClose.getPaymentsModel2(),false,"Details Of Payment Made");
        }catch(Exception e){
            e.printStackTrace();
        }
}//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        ChequeDetail cd=ChequeDetail.getDialog(this, m_PaymentsToClose);
        try{
            boolean flag=  cd.showDialog(m_PaymentsToClose.getRdetails1Model(),false,"Details OF Debt Raised");
        }catch(Exception e){
            e.printStackTrace();
        }
}//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        ChequeDetail cd=ChequeDetail.getDialog(this, m_PaymentsToClose);
        try{
            boolean flag=  cd.showDialog(m_PaymentsToClose.getOtherIncomeTableModel(),true,"Details Of Other Income");
        }catch(Exception e){
            e.printStackTrace();
        }
}//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        ChequeDetail cd=ChequeDetail.getDialog(this, m_PaymentsToClose);
        try{
            boolean flag=  cd.showDialog(m_PaymentsToClose.getDebtraisedModel(),true,"Details Of Debt Collected");
        }catch(Exception e){
            e.printStackTrace();
        }
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        ChequeDetail cd=ChequeDetail.getDialog(this, m_PaymentsToClose);
        try{
            boolean flag=  cd.showDialog(m_PaymentsToClose.getRdetailsModel(),false,"Details Of Receipt");
        }catch(Exception e){
            e.printStackTrace();
        }
}//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        ChequeDetail cd=ChequeDetail.getDialog(this, m_PaymentsToClose);
        try{
            boolean flag=  cd.showDialog(m_PaymentsToClose.getChequeDetailModel(),true,"Details Of Cheque");
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        ChequeDetail cd=ChequeDetail.getDialog(this, m_PaymentsToClose);
        try{
            boolean flag=  cd.showDialog(m_PaymentsToClose.UCReceiptTableModel(),false,"Details Of Unclosed Receipts");
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
         ChequeDetail cd=ChequeDetail.getDialog(this, m_PaymentsToClose);
        try{
            cd.showDialog(m_PaymentsToClose.PendingCrConfTableModel(),false,"Details Of Pending Credit Confirmation");
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton9ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField balance;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField cashinsum;
    private javax.swing.JTextField debtctotal;
    private javax.swing.JTextField debtraised;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField m_jMaxDate;
    private javax.swing.JTextField m_jSequence;
    private javax.swing.JTable m_jTicketTable;
    private javax.swing.JTextField otherincometotal;
    private javax.swing.JTextField paymentout;
    private javax.swing.JTextField receipttotal;
    private javax.swing.JTextArea remark_text;
    private javax.swing.JTextField unr_text;
    // End of variables declaration//GEN-END:variables
    
}
