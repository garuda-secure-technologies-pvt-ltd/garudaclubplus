/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MemberDebtBilling.java
 *
 * Created on Jun 23, 2009, 10:11:15 AM
 */
package com.openbravo.pos.clubmang;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.DataParams;
//import com.openbravo.data.loader.Datas;
//import com.openbravo.data.loader.SerializerWriteBasic;
//import com.openbravo.data.loader.SerializerWriteString;
//import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.admin.CardReader;
import com.openbravo.pos.clubmang.MemDebtBillingTableModel.MyAbstractTableModel;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.CardSwipeNotifier;
import com.openbravo.pos.forms.CardSwipeNotifier;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.mant.WaiterInfo;
import com.openbravo.pos.panels.JPanelPrinter;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.PaymentInfo;
//import com.openbravo.pos.payment.PaymentInfoList;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.BillInfo;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.sms.EmailMaster;
import com.openbravo.pos.sms.MemberEmailList;
import com.openbravo.pos.util.JRViewer300;
import com.openbravo.pos.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
//import javax.swing.table.AbstractTableModel;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperPrint;
//import javax.swing.table.TableColumn;
import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService; 
import java.util.concurrent.Executors; 
import java.util.concurrent.ScheduledFuture; 
import java.util.concurrent.TimeUnit; 
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.table.TableColumnModel;
import com.openbravo.pos.customers.memberPhotoInfo;
import com.openbravo.pos.sms.SMSgeneralDBSettings;
import java.text.DecimalFormat;
/**
 *
 * @author swathi
 */
public class MemberDebtBilling extends javax.swing.JPanel implements JPanelView, BeanFactoryApp ,CardSwipeNotifier{

    /** Creates new form MemberDebtBilling */
    private MemDebtBillingTableModel dbmodel;
    private JRViewer300 reportviewer = null;
    private AppView m_App;
    private String id;
    private DataLogicCustomers dlCustomers;
    private CustomerInfo customerInfo;
    private DataLogicFacilities dmang;
    private DataLogicSales m_dlSales;
    private DataLogicSystem dlsystem;
    private TicketParser ttp;
    private boolean alowbilling;
    private MyAbstractTableModel tablemodel;
    private MyAbstractTableModel ctablemodel;
    private double caltotal; 
    private double finalamount;
    private int status;
    private int operator;
    private CaluculateLimit climit;
    private CardReader cr; 
    private SMSgeneralDBSettings smsDBSettings;

  //  private   String BalStr = null;
  DecimalFormat decimalFormat = new DecimalFormat("#0.00");  
  
  private Double FinalAmt=0.00 ;  
     // Double amt = Double.parseDouble(total.getText());
                                     private   Double FinalAmtForPrint=0.00;
    public MemberDebtBilling() {
        initComponents();
    }

    public void init(AppView app) throws BeanFactoryException {
        m_App = app;
        reportviewer = new JRViewer300(null);
        jPanel1.add(reportviewer);
        jPanel1.validate();
        jPanel1.repaint();
        dmang = (DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        smsDBSettings = (SMSgeneralDBSettings) app.getBean("com.openbravo.pos.sms.SMSgeneralDBSettings");
        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dlsystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        ttp = new TicketParser(app.getDeviceTicket(), dlsystem);
        ctotal.setEditable(false);
        finalamt.setEditable(false);
        ToolTipManager.sharedInstance().setInitialDelay(0);

    }
    /*  public void setForMemView(String uid){
    jPanel3.setVisible(false);
    jPanel1.setVisible(false);
    jButton5.setVisible(false);
    jButton3.setVisible(false);
    total.setVisible(false);
    ctotal.setVisible(false);
    jLabel4.setVisible(false);
    jLabel7.setVisible(false);

    try{
    Object[] obj= dmang.getMemberbyID(uid);

    if(obj==null)
    {
    JOptionPane.showMessageDialog(this,AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
    }
    else
    {
    customerInfo=new CustomerInfo(uid);
    customerInfo.setName(obj[1].toString());
    customerInfo.setSearchkey(obj[0].toString());
    mname.setText(obj[1].toString());
    memno.setText(obj[0].toString());
    loadmemdata();
    //customerInfo.setMemType(obj[2]);
    }
    }
    catch(Exception e)
    {
    e.printStackTrace();
    }
    //customerInfo=cinfo;
    }*/

    public String getTitle() {
        return null;
    }

    public void activate() throws BasicException {
        //jButton1.setText("Print");
       startCardReader();
        loadData();
    }

    private void loadData() {
        caltotal = 0;
        status = 0;
        operator = 0;
        customerInfo = null;
        //jButton5.setEnabled(false);
        jTable1.setVisible(false);
        dbmodel = MemDebtBillingTableModel.emptyinstance();

        total.setText("0.0");
        debttotal.setText("0.0");
        finalamt.setText("0.00");
        ctotal.setText("0.0");
        creditamt.setText("0.0");
        memno.setText(null);
        mname.setText(null);
        jTextField3.setText(null);
        jCheckBox1.setSelected(false);
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                 //akshatha:to read a card from card reader without port num
                String cardReaderPortName = m_App.getProperties().getProperty("card.portnumber");
                if (cardReaderPortName == null || cardReaderPortName.trim().length() == 0) {
                    jTextField3.requestFocus();
                    jTextField3.setEditable(true);
                    jButton10.setVisible(true);
                    jLabel8.setVisible(true);
                } else {
                    memno.requestFocus();
                    jTextField3.setEditable(false);
                    jButton10.setVisible(false);
                    jLabel8.setVisible(false);
                }
            }
        });
        //  AppUser user=m_App.getAppUserView().getUser();
        // if(user.getName().toUpperCase().equals("MEMBERS")){
        //   tablemodelmem=dbmodel.getMemDebitTableModel();
        //   jTable1.setModel(tablemodelmem);
        //   ctablemodelmem=dbmodel.getMemCreditTableModel();
        //  jTable2.setModel(ctablemodelmem);
        //   setForMemView(user.getMemid());
        //}else{
        tablemodel = dbmodel.getTableModel();
        tablemodel.settext(total);
        jTable1.setModel(tablemodel);
        ctablemodel = dbmodel.getCreditTableModel();
        ctablemodel.settext(ctotal);
        jTable2.setModel(ctablemodel);
        jButton1.setVisible(true);
    //jButton6.setVisible(false);
    //  }    
   //  Double FinalAmt=0.00 ;  
//      Double amt = Double.parseDouble(total.getText());
//                                  //      Double FinalAmtForPrint=0.00;
//                                        if (customerInfo.getMobile() != null) {
//                                            
//                                            Double DamtTot = Double.parseDouble(debttotal.getText());
//                                            Double CamtTot = Double.parseDouble(creditamt.getText());
//                                            //Double Fimalamt =Double.parseDouble(finalamt.getText());
//                                            
//                                            FinalAmt = CamtTot-DamtTot;
//                                            FinalAmt = FinalAmt +amt;
//                                            FinalAmt = dmang.roundTwoDecimals(FinalAmt);
//                                        //    String BalStr = null;
//                                            FinalAmtForPrint = FinalAmt;
//                                            if(FinalAmt<0){
//                                                FinalAmt = FinalAmt *(-1);
//                                                BalStr = "Balance Rs "+FinalAmt+" DR.";
//                                           //jTextField1.setText(decimalFormat.format(FinalAmt) +"dr.");
//                                            }
//                                            else{
//                                                BalStr = "Balance Rs "+FinalAmt+" CR."; 
//                                              //   jTextField1.setText(decimalFormat.format(FinalAmt) +"cr.");
//                                            } 
////                                             double FinalAmt1 =0.00;
////                                           FinalAmt1=FinalAmt;
//                                          //  jTextField7.setText(decimalFormat.format(GrandTotal1));
//                                           finalamt.setText(BalStr);
//    }  
//                                          finalamt.setText(BalStr);
    }
    /* private class Tablecolumnchange implements PropertyChangeListener{

    public void propertyChange(PropertyChangeEvent evt) {
    int row=jTable1.getSelectedRow();
    if(row>0){
    List<MemDebtBillingTableModel.Facilityline> fac;
    fac=dbmodel.getfacilityline();
    //    String fname=dbmodel.getTableModel().getValueAt(selectedrowno, 0).toString();
    double oamt=fac.get(row).getamountb();
    double namt=fac.get(row).getamount();
    if((Boolean)evt.getNewValue()==true){
    dbmodel.getTableModel().setValueAt(fac.get(row).getamount(), row, 5);
    //  fac.get(row).setAmountb(fac.get(row).getamount());
    //  dbmodel.setfacilityline(dbmodel.getfacilityline());
    jTable1.setModel(dbmodel.getTableModel());

    }else if((Boolean)evt.getNewValue()==false){
    namt=0.0;
    dbmodel.getTableModel().setValueAt(0.0, row, 5);
    jTable1.setModel(dbmodel.getTableModel());
    }
    Double totalamt=Double.parseDouble(total.getText())-oamt+namt;
    total.setText(totalamt.toString());
    }
    }
    }*/

    public boolean deactivate() {
        return true;
    }

    public JComponent getComponent() {
        return this;
    }

    public Object getBean() {
        return this;
    }
   public void execute(String card) {
        card = String.valueOf(m_App.getReader().getVariance() + Double.valueOf(card));

    }
    private void equals() {
        if (operator == 1 && status == 0) {
            caltotal += Double.parseDouble(amtvalue.getText());
        } else if (operator == 2 && status == 0) {
            caltotal -= Double.parseDouble(amtvalue.getText());
        } else if (operator == 3 && status == 0) {
            caltotal *= Double.parseDouble(amtvalue.getText());
        } else if (operator == 0) {
            caltotal = Double.parseDouble(amtvalue.getText());
        }
        amtvalue.setText(String.valueOf(dmang.roundTwoDecimals(caltotal)));

    }



    private void stateTransition(char cTrans) {
        if (cTrans == '\u007f') {
            amtvalue.setText(null);
            caltotal = 0;
            operator = 0;
            opr.setText(" ");
            status = 1;
        } else if (cTrans == '+') {
            equals();
            opr.setText("+");
            amtvalue.setText(String.valueOf(caltotal));
            operator = 1;
            status = 1;
        } else if (cTrans == '-') {
            equals();
            opr.setText("-");
            amtvalue.setText(null);
            amtvalue.setText(String.valueOf(caltotal));
            operator = 2;
            status = 1;
        } else if (cTrans == '*') {
            equals();
            opr.setText("*");
            amtvalue.setText(null);
            amtvalue.setText(String.valueOf(caltotal));
            operator = 3;
            status = 1;
        } else if (cTrans == '=') {
            equals();
            operator = 0;
            caltotal = 0;
            opr.setText(" ");
            status = 1;
        } else {
            if (status == 1) {
                amtvalue.setText(String.valueOf(cTrans));
            } else {
                amtvalue.setText(amtvalue.getText() + cTrans);
            }
            status = 0;
        }
    }

    private Date getdate() {
        Date dnow = new Date();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(dnow.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.MILLISECOND, 00);
        dnow.setTime(cal.getTimeInMillis());
        return dnow;
    }

    private void printTicket(List<MemDebtBillingTableModel.Facilityline> list, String receiptno, String cname, List<PaymentInfo> pinfo, Double amount, List<MemDebtBillingTableModel.Creditline> clist, String memno,Double FinalAmount) {

        String sresource = LookupUtilityImpl.getInstance(null).getDataLogicSystem().getResourceAsXML("Printer.Receipt");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("createdby", m_App.getAppUserView().getUser().getName());
                String x = m_App.getAppUserView().getUser().getRole();
                script.put("role", LookupUtilityImpl.getInstance(null).getRoleMap().get(x).toString());
                script.put("host", m_App.getProperties().getHost());
                script.put("pinfo", pinfo);
                script.put("total", dmang.ConvertDoubleToString(amount));
                script.put(id, amtvalue);
                script.put("cname", cname);
                script.put("cno", memno);
                script.put("date", Formats.TIMESTAMP.formatValue(new Date()));
                script.put("ticket", list);
                script.put("clist", clist);
                script.put("receipt", receiptno);
                String BalStr=null;
                if(FinalAmount<0){
                  FinalAmount = FinalAmount *(-1);
                  BalStr = "Balance Rs "+FinalAmount+" DR.";
                }
                else{
                    BalStr = "Balance Rs "+FinalAmount+" CR.";
                }
                
                script.put("FinalAmount", BalStr);
                
                script.put("eoe", StringUtils.encodeXML("E&OE"));
                ttp.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (Exception e1) {
                new MessageInf(e1).show(this);
            }
        }
    }

    private void load() {
        try {
            total.setText("0.0");
            ctotal.setText("0.0");
            jTable1.setVisible(true);
            String accid = dmang.getCustomerAccountByID(customerInfo.getId());
            dbmodel = MemDebtBillingTableModel.loadInstance(m_App, customerInfo.getId(), accid, dmang);
            tablemodel = dbmodel.getTableModel();
            tablemodel.settext(total);
            jTable1.setModel(tablemodel);
            ctablemodel = dbmodel.getCreditTableModel();
            ctablemodel.settext(ctotal);
            jTable2.setModel(ctablemodel);
            creditamt.setText(dbmodel.getcreditAmount().toString());
            debttotal.setText(dbmodel.getdebtAmount().toString());
            jButton1.setVisible(true);
            jCheckBox1.setSelected(false); 
           // finalamt.setText();
     //        jTextField4.setText(decimalFormat.format(Tax11Total1));
            double d= Double.parseDouble(debttotal.getText());
            double c= Double.parseDouble(creditamt.getText());
              
             double r=0.00;
            
            r=c-d;
              String blr=null;
//              if(r<0){
//                  
//              r=r*(-1);
//             // blr=""+r+" DR.";
//              }
//              else {
//                  blr=""+r+" CR.";
//              }   
                                              if(r<0){
                                                r = r *(-1);
                                            blr=    decimalFormat.format(r);
                                               // blr = "Balance Rs "+r+" DR.";
                                           //jTextField1.setText(decimalFormat.format(FinalAmt) +"dr.");
                                            finalamt.setText(blr+"Dr");
                                            }
                                            else{
                                               // blr = "Balance Rs "+r+" CR."; 
                                                blr=    decimalFormat.format(r);
                                                 finalamt.setText(blr+"Cr");
                                              //   jTextField1.setText(decimalFormat.format(FinalAmt) +"cr.");
                                            } 

   
        } catch (Exception e) {
            e.printStackTrace();
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, "Cannot load the Data", e);
            msg.show(this);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 =  new javax.swing.JTable(){public Component prepareRenderer(TableCellRenderer renderer,
            int rowIndex, int vColIndex) {
            Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                jc.setToolTipText(String.valueOf(getValueAt(rowIndex, vColIndex)));

                if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
                    jc.setBackground(Color.cyan);
                }
                else {
                    jc.setBackground(Color.white);
                }
                if(isCellSelected(rowIndex, vColIndex))
                jc.setBackground(Color.lightGray);
            }
            return c;
        }};
        jLabel6 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        debttotal = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        amtvalue = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        opr = new javax.swing.JLabel();
        jNumberKeys1 = new com.openbravo.beans.JNumberKeys();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 =  new javax.swing.JTable(){public Component prepareRenderer(TableCellRenderer renderer,
            int rowIndex, int vColIndex) {
            Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                jc.setToolTipText(String.valueOf(getValueAt(rowIndex, vColIndex)));

                if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
                    jc.setBackground(Color.cyan);
                }
                else {
                    jc.setBackground(Color.white);
                }
                if(isCellSelected(rowIndex, vColIndex))
                jc.setBackground(Color.lightGray);
            }
            return c;
        }};
        jLabel5 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        creditlabel = new javax.swing.JLabel();
        creditamt = new javax.swing.JTextField();
        ctotal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        finalamt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        oldaccview = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        resetQTlimit_btn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        memno = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        mname = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        btnLoad = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();

        setAutoscrolls(true);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Facility Name", "Due Date", "Debt", "Last Bill Date", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setText("Debit List");

        jLabel3.setText("Debt Total");

        debttotal.setEditable(false);

        jLabel4.setText("Total");

        total.setEditable(false);
        total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(debttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(debttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        amtvalue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton4.setText("Enter");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        opr.setText(" ");

        jNumberKeys1.addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {
            public void keyPerformed(com.openbravo.beans.JNumberEvent evt) {
                jNumberKeys1KeyPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(amtvalue, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(opr, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jNumberKeys1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(235, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(amtvalue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(opr, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jNumberKeys1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(53, Short.MAX_VALUE)))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);

        jLabel5.setText("Available Credit List");

        creditlabel.setText("Total Credit Available");

        creditamt.setEditable(false);
        creditamt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditamtActionPerformed(evt);
            }
        });

        ctotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctotalActionPerformed(evt);
            }
        });

        jLabel7.setText("Total");

        finalamt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalamtActionPerformed(evt);
            }
        });

        jLabel9.setText("Final Amount");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(creditlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(creditamt, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(ctotal, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(finalamt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creditamt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditlabel)
                    .addComponent(ctotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(finalamt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap(575, Short.MAX_VALUE)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setText("Add Account Pay");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setText("Pay");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setText("Reset Cr.limit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        oldaccview.setText("Old Account View");
        oldaccview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oldaccviewActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Select All");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        resetQTlimit_btn.setText("Reset QT. Cr. Limit");
        resetQTlimit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetQTlimit_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox1)
                        .addGap(90, 90, 90))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(resetQTlimit_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(oldaccview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jCheckBox1))
                        .addGap(7, 7, 7)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(oldaccview)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(resetQTlimit_btn))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        memno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memnoActionPerformed(evt);
            }
        });
        memno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                memnoKeyPressed(evt);
            }
        });

        jLabel1.setText("Mem No");

        jLabel2.setText("Member Name");

        mname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnameActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setText("Card No:");

        jTextField3.setMaximumSize(new java.awt.Dimension(6, 20));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        btnLoad.setText("Load");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        jButton7.setText("View Photo");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                    .addComponent(memno))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(mname, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(74, 74, 74))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnLoad))
                            .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(memno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(mname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton7))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTextField1.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void memnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_memnoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyText(evt.getKeyCode()).equals("Enter")) {

            try {
                 Object[] obj = dmang.getMamberbySkey(memno.getText().toUpperCase());

                if (obj == null) {
                    JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                } else {
                    customerInfo = new CustomerInfo(obj[0].toString());
                    customerInfo.setName(obj[1].toString());
                    customerInfo.setSearchkey(memno.getText().toUpperCase());
                    customerInfo.setMobile(String.valueOf(obj[3]));
                    customerInfo.setAccno(obj[4].toString());
                    System.out.println(customerInfo.getAccno());
                    mname.setText(obj[1].toString());
                    load();
                //customerInfo.setMemType(obj[2]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mname.setText(null);
            customerInfo = null;
            if (jTable1.getRowCount() > 0) {
                dbmodel = MemDebtBillingTableModel.emptyinstance();
                jTable1.setModel(dbmodel.getTableModel());
                jTable2.setModel(dbmodel.getCreditTableModel());
                debttotal.setText("0.00");
                creditamt.setText("0.00");
                ctotal.setText("0.00");
                total.setText("0.00"); 
                finalamt.setText("0.00");
            }
        }
    }//GEN-LAST:event_memnoKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        //finder.search(m_oTicket.getCustomer());
        finder.setVisible(true);
        //  CustomerInfoExt customer;
        customerInfo = finder.getSelectedCustomer();
        if (customerInfo != null) {
            try {
                // customer = dlSales.loadCustomerExt(customerInfo.getId());
                mname.setText(customerInfo.toString());
                memno.setText(customerInfo.getSearchkey()); 
                 load();
     
            } catch (Exception e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
                msg.show(this);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try {
            Transaction t;
            t = new Transaction(m_App.getSession()) {
                
                public Object transact() throws BasicException {
                    if (m_App.getAppUserView().getUser().getcashaccount() != null && m_App.getAppUserView().getUser().getchequeaccount() != null) {
                        int rownos = jTable1.getRowCount();
                        boolean allow = true;
                        List<MemDebtBillingTableModel.Facilityline> listF = dbmodel.getfacilityline();
                        //added to correct accountpay
                        for (MemDebtBillingTableModel.Facilityline f : listF) {
                            if (f.getaccid().equals("Account Pay") && Double.parseDouble(ctotal.getText()) > 0) {
                                allow = false;
                            }
                        }
                        if (allow == true) {

                            Double amt = Double.parseDouble(total.getText());
                            CustomerInfoExt cinfo = new CustomerInfoExt(customerInfo.getId());
                            cinfo.setSearchkey(customerInfo.getSearchkey());
                            cinfo.setName(customerInfo.getName());
                            String caccount = "";
                            List<PaymentInfo> pinfo = new ArrayList<PaymentInfo>();
                            String accid;
                            Double payableamt;
                            Double paidamt;
                            JPaymentSelect paymentdialog = JPaymentSelectReceipt.getDialog(JOptionPane.getRootFrame());
                            paymentdialog.init(m_App);
                            Date dnow = getdate();
                            String pref = null;
                            Double bamt1 = 0.0;
                            Double pamt = Double.parseDouble(debttotal.getText()); 
                            
                            Boolean flag = true;
                            boolean bill = true;
                            if (Double.parseDouble(ctotal.getText()) > 0) {
                                alowbilling = false;
                            } else {
                                alowbilling = true;
                            }
                            if (alowbilling == true) {
                                if (amt > 0) {
                                    bill = paymentdialog.showDialog(amt, cinfo, m_App.getAppUserView().getUser().getName(), true);
                                } else {
                                    bill = false;
                                }
                            } else {
                                bill = true;
                                Double camt = 0.0;//Double.parseDouble(creditamt.getText())-Double.parseDouble(total.getText()) ;
                                Double totalamt = Double.parseDouble(total.getText());
                                Double bal = Double.parseDouble(ctotal.getText()) - Double.parseDouble(total.getText());
                                if (bal >= 0) {
                                    List<MemDebtBillingTableModel.Facilityline> listold = dbmodel.getfacilityline();
                                    String temp = null;
                                    for (MemDebtBillingTableModel.Facilityline line : listold) {
                                        if (line.getamountb() > 0) {
                                            if (temp == null) {
                                                temp = line.gettransno() + " # " + line.getamountb();
                                            } else {
                                                temp += " : " + line.gettransno() + " # " + line.getamountb();
                                            }
                                            double amt1 = dmang.roundTwoDecimals(line.getamount() - line.getamountb());
                                            if (amt1 <= 0) {
                                                dmang.updateDebit1(dbmodel.getCreditTransno(), 0.0, line.getaccid());
                                            } else {
                                                dmang.updateDebit(dbmodel.getCreditTransno(), amt1, line.getaccid());
                                            }
                                            int fflag = 0;
                                            try {
                                                Facility factenp = dmang.getFacilitybyID(line.getFacilityId());
                                                if (factenp == null) {
                                                    fflag = 1;
                                                }
                                            } catch (Exception e) {
                                                fflag = 1;
                                            }
                                            if (fflag == 0) {
                                                dmang.setmemberDebt(customerInfo.getId(), line.getFacilityId(), line.getamountb() * -1);
                                            }
                                        }
                                    }
                                    String temoid = dbmodel.getCreditEntry();
                                    if (bal > 0) {
                                        dmang.updatecredit(bal, false, temoid, temp);
                                    } else {
                                        dmang.updatecredit(0.0, true, temoid, temp);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Amount Exceeds the selected credit", null, JOptionPane.WARNING_MESSAGE);
                                    bill = false;
                                }
                                load();
                            }
                            String rnum = "", accjournalid = "";
                            boolean f = true;
                            if (bill) {
                                List<MemDebtBillingTableModel.Facilityline> list = new ArrayList<MemDebtBillingTableModel.Facilityline>();
                                List<MemDebtBillingTableModel.Facilityline> listold = dbmodel.getfacilityline();

                                String tid = UUID.randomUUID().toString();
                                Double cashieramt = amt;
                                if (alowbilling == true) {

                                    pinfo = paymentdialog.getSelectedPayments();
                                    BillInfo ticket = new BillInfo();
                                    ticket.setID(UUID.randomUUID().toString());
                                    ticket.setPayments(pinfo);
                                    ticket.setCustomer(cinfo);
                                    ticket.setCreatedBy(m_App.getAppUserView().getUser().getName());
                                    ticket.setCreatedDate(dnow);
                                    ticket.setActiveCash(m_App.getActiveCashIndex());
                                    //  rnum = m_dlSales.payaccount(ticket, m_App.getInventoryLocation(), false);
                                    rnum = m_dlSales.payaccount(ticket,"1", false);
                                  
                                    if (!(rnum == null || rnum.equals("false"))) {
                                        for (int i = 0; i < rownos; i++) {
                                            Double bamt = 0.0;
                                            accid = dbmodel.getTableModel().getValueAt(i, 8).toString();//account journal id//
                                            payableamt = Double.parseDouble(dbmodel.getTableModel().getValueAt(i, 3).toString());
                                            String fid = null;
                                            try {
                                                fid = dbmodel.getTableModel().getValueAt(i, 11).toString();//
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            caccount = dmang.getCustomerAccountByID(customerInfo.getId());
                                            //  accjournalid=dbmodel.getTableModel().getValueAt(0, 8).toString();
                                            //  pamt += payableamt;
                                            paidamt = Double.parseDouble(dbmodel.getTableModel().getValueAt(i, 5).toString());
                                            if (accid.equals("Account Pay")) {
                                                list.add(listold.get(i));
                                                bamt1 += paidamt;
                                                if (bamt1 != 0.0) {
                                                    flag = false;
                                                }
                                            } else {
                                                bamt = payableamt - paidamt;
                                                if (paidamt > 0) {
                                                    list.add(listold.get(i));
                                                    if (payableamt.toString().equals(paidamt.toString())) {//clear
                                                        f = dmang.updateaccountjournal(accid, dnow, rnum + " # " + paidamt, 0.0, true);
                                                        //if(f=false)
                                                        if (f == false) {
                                                            load();
                                                            throw new BasicException("Error Occured..Please Try again");
                                                        }
                                                        if (pref == null) {
                                                            pref = dbmodel.getTableModel().getValueAt(i, 9).toString();//
                                                        } else {
                                                            pref = pref + " : " + dbmodel.getTableModel().getValueAt(i, 9).toString();// //group all trans no
                                                        }
                                                    } else if (payableamt > paidamt) {
                                                        bamt = dmang.roundTwoDecimals(payableamt - paidamt);
                                                        //ID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE
                                                        dmang.updateaccountjournal1(accid, bamt, rnum + " # " + paidamt, payableamt);
                                                        if (f == false) {
                                                            load();
                                                            throw new BasicException("Error Occured..Please Try again");
                                                        }
                                                        if (pref == null) {
                                                            pref = dbmodel.getTableModel().getValueAt(i, 9).toString();//
                                                        } else {
                                                            pref = pref + " : " + dbmodel.getTableModel().getValueAt(i, 9).toString();// //group all trans no
                                                        }
                                                    }
                                                }
                                            }
                                            int fflag = 0;
                                            try {
                                                Facility factenp = dmang.getFacilitybyID(fid);
                                                if (factenp == null) {
                                                    fflag = 1;
                                                }
                                            } catch (Exception e) {
                                                fflag = 1;
                                            }
                                            if (fflag == 0) {
                                                dmang.setmemberDebt(customerInfo.getId(), fid, paidamt * -1);
                                            }
                                        }
                                        Double chequeamt = 0.0;
                                        
                                        for (PaymentInfo p : pinfo) {
                                            if (p.getName().equals("cheque")) {
                                                chequeamt += p.getTotal();
                                            }
                                        }
                                        
                                        // EDITED BY AAKASH
                                        
                                        Double MagCardAmt = 0.00;
                                        String MagCarAccount = null;
                                        for (PaymentInfo p : pinfo) {
                                            if (p.getName().equals("magcard")) {
                                                Double MagCardAmtTemp = p.getTotal() ;
                                                Double Bankcharges = p.getOtherCharges();
                                                MagCardAmt += (p.getTotal());
                                                String BankAccountID = p.getTrack1();
                                                String Narration1 = "Payment received for bill nos" + pref + ". Payment received vide ref no:  "+p.getTrack3();
                                                Object[] value1 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "D", customerInfo.getSearchkey(), rnum, MagCardAmtTemp, dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(),Narration1 , BankAccountID , 0.0, dnow, true};
                                                dmang.insertintoaccjoutnal1(value1);
                                                
                                                // Object[] valueExt = new Object[]{  UUID.randomUUID().toString() , p.getTrack2() ,ticket.getReceiptRef() ,  p.getTotal() , p.getOtherCharges() ,  p.getTrack3() ,  p.getTrack1() , 1  };
                                                // new PreparedSentence(m_App.getSession(), "INSERT INTO carddetails(ID, BANKID, RECEIPT, AMOUNT,OTHERCHARGES , transactionid , accountid , paymentflag) VALUES (?, ?, ?, ?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING , Datas.INT   })).exec(valueExt);
                                                //String BankChargesActID = null;
                                                // Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE where NAME='Bank Charges Collected Account'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
                                                //if(obj1!=null){
                                                //   if(obj1[0]!=null){
                                                //       BankChargesActID = obj1[0].toString();
                                                //   }
                                                // }
                                                //   String Narration2 = "Payment received for bill nos" + pref + ". Bank Charges received vide ref no:  "+p.getTrack3();
                                                //  Object[] valuer=new Object[]{UUID.randomUUID().toString(),tid,null,dnow,"C",customerInfo.getSearchkey(),rnum, Bankcharges,dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(), Narration2 ,BankChargesActID,0.0,dnow,null,true};
                                                //  dmang.insertintoaccjoutnal2(valuer);
                                                
                                                
                                                
                                            }
                                        }
                                        
                                        
                                        cashieramt = amt - chequeamt - MagCardAmt;
                                        if (chequeamt > 0) {
                                            Object[] value1 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "D", customerInfo.getSearchkey(), rnum, chequeamt, dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Payment received for bill nos" + pref, m_App.getAppUserView().getUser().getchequeaccount(), 0.0, dnow, true};
                                            dmang.insertintoaccjoutnal1(value1);
                                        }
                                        
                                        
                                        
                                        //credit
                                        Object value[] = new Object[]{UUID.randomUUID().toString(), tid, customerInfo.getId(), dnow, "C", "C Receipt", rnum, amt, dnow, flag, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Payment for bill nos " + pref, caccount, bamt1, dnow, pref, true};
                                        dmang.insertintoaccjoutnal2(value);
                                        //debit
                                        if (cashieramt > 0) {
                                            Object[] value1 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "D", customerInfo.getSearchkey(), rnum, cashieramt, dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Payment received for bill nos " + pref, m_App.getAppUserView().getUser().getcashaccount(), 0.0, dnow, true};
                                            dmang.insertintoaccjoutnal1(value1);
                                        }
                                        
//                                        Double FinalAmt=0.00 ;
//                                        Double FinalAmtForPrint=0.00;
                                        if (customerInfo.getMobile() != null) {
    //                                        String smsmsg = "Dear Member,\rRecvd pymt on ur a/c of Rs." + dmang.roundTwoDecimals(amt) + " vide recp no. " + rnum + ".";
                                            
                                            Double DamtTot = Double.parseDouble(debttotal.getText());
                                            Double CamtTot = Double.parseDouble(creditamt.getText());
                                   // Finalamt = Double.parseDouble(finalamt.getText());

                                            FinalAmt = CamtTot-DamtTot;
                                            FinalAmt = FinalAmt +amt;
                                            FinalAmt = dmang.roundTwoDecimals(FinalAmt);
                                          String BalStr = null;
                                          String netBalance = null;
                                            FinalAmtForPrint = FinalAmt;
                                            if(FinalAmt<0){
                                                FinalAmt = FinalAmt *(-1);
                                                BalStr = "Balance Rs "+FinalAmt+" DR.";
                                                netBalance = FinalAmt+" DR.";
                                           //jTextField1.setText(decimalFormat.format(FinalAmt) +"dr.");
                                           // finalamt.setText(BalStr);
                                            }
                                            else{
                                                BalStr = "Balance Rs "+FinalAmt+" CR."; 
                                                netBalance = FinalAmt+" CR.";
                                                // finalamt.setText(BalStr);
                                              //   jTextField1.setText(decimalFormat.format(FinalAmt) +"cr.");
                                            } 
//                                             double FinalAmt1 =0.00;
//                                           FinalAmt1=FinalAmt;
                                          //  jTextField7.setText(decimalFormat.format(GrandTotal1));
                                          // finalamt.setText(BalStr);
                                            
                                          
                                          
                                          
                                            String smsmsg = "Dear Member,\rRecvd pymt on ur a/c "+customerInfo.getSearchkey() + " of Rs." + dmang.roundTwoDecimals(amt) + " vide recp no. " + rnum + ".";

                                            String paymentMethod = "CASH";
                                            for (PaymentInfo p1 : pinfo) {
                                                if (p1.getName().equals("cheque")) {
                                                    smsmsg += "Cheque subjected to realisation. ";
                                                    paymentMethod = "CHEQUE";
                                                    break;
                                                }
                                                
                                                if(p1.getName().equals("cheque")){
                                                    
                                                }
                                            }
                                            smsmsg +=BalStr;
                                            
                                            //smsmsg+="-BUSC";
                                            if (customerInfo.getMobile() != null && customerInfo.getMobile().trim().length() == 10) {
                                                // REMOVING THIS METHOD AS WE ALREADY HAVE ANOTHER IMPLEMENTED 
                                               // dmang.updatetosendMsg(smsmsg, cinfo.getId(), customerInfo.getMobile(), 2);
                                            }
                                            
                                            // SEND NEW SMS
                                            createSMS(SMSgeneralDBSettings.SMS_MEMBER_RECEIPT_BILLING_AND_CASH_MNGT_ID, rnum+"" , dmang.roundTwoDecimals(amt)+"" , customerInfo, paymentMethod, netBalance);
                                            
                                        }
                                        printTicket(list, rnum, cinfo.getName(), pinfo, amt, dbmodel.getCreditList(), cinfo.getSearchkey() , FinalAmtForPrint);
                                        //  JPanelPrinter jpp = new JPanelPrinter(m_App);
                                        
                                        //      AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();
                                        
                                        
                                        
                                        ///////////       ///////////////////////     ////////////////////////////           ///////////       ///////////////////////     ////////////////////////////
                                        
                                        String BMName="Credit Check for billing member";
                                        String BName2 = "Facility for billing member";
                                        Object[] FacObj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE where NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.BOOLEAN})).find(BMName);
                                        
                                        if(FacObj!=null){
                                            
                                            Boolean v5 = (Boolean)FacObj[0];
                                            if(v5){
                                                
                                                
                                                ///////////       ///////////////////////     ////////////////////////////
                                                //For:-CURROPB
                                                String idt = cinfo.getId();
                                                Object[] objchk = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ID FROM billingmember where ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(idt);
                                                if(objchk!=null){
                                                    Double amt1 = amt;
                                                    Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idt);
                                                    String popb=obj[0].toString();
                                                    Double POPB = new Double(popb);
                                                    dmang.roundTwoDecimals(POPB);
                                                    Double COPB = POPB-amt1;
                                                    COPB=dmang.roundTwoDecimals(COPB);
                                                    new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{COPB,idt});
                                                    //For:-OPB
                                                    Object[] objm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((OPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idt);
                                                    //     Object[] objm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ((OBDEBIT+CURDEBIT)-(OBCREDIT+CURCREDIT)) FROM trailbalance where ID=", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idt);
                                                    String popbm=objm[0].toString();
                                                    Double POPBm = new Double(popbm);
                                                    dmang.roundTwoDecimals(POPBm);
                                                    Double OPB = POPBm-amt1;
                                                    OPB=dmang.roundTwoDecimals(OPB);
                                                    new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET OPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{OPB,idt});
                                                    
                                                    for(int i=1;i<=5;i++){
                                                        String idtg = idt+"#Guest "+i;
                                                        Object[] objg = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idtg);
                                                        if(objg!=null){
                                                            Object[] objg1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idtg);
                                                            String popbg=objg1[0].toString();
                                                            Double POPBg = new Double(popbg);
                                                            dmang.roundTwoDecimals(POPBg);
                                                            Double COPBg = POPBg-amt1;
                                                            COPBg=dmang.roundTwoDecimals(COPBg);
                                                            new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{COPBg,idtg});
                                                            new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET OPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{OPB,idtg});
                                                        }
                                                    }
                                                }
                                                ///////////       ///////////////////////     ////////////////////////////
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                            }
                                            else{
                                                
                                                ///////////       ///////////////////////     ////////////////////////////
                                                // donot change billing member amount if other facility is selected instead of all in general table.
                                                
                                                ///////////       ///////////////////////     ////////////////////////////
                                                
                                            }
                                            
                                        }
                                        else{
                                            
                                            
                                            
                                            ///////////       ///////////////////////     ////////////////////////////
                                            //For:-CURROPB
                                            String idt = cinfo.getId();
                                            Object[] objchk = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ID FROM billingmember where ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(idt);
                                            if(objchk!=null){
                                                Double amt1 = amt;
                                                Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idt);
                                                String popb=obj[0].toString();
                                                Double POPB = new Double(popb);
                                                dmang.roundTwoDecimals(POPB);
                                                Double COPB = POPB-amt1;
                                                COPB=dmang.roundTwoDecimals(COPB);
                                                new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{COPB,idt});
                                                //For:-OPB
                                                Object[] objm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((OPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idt);
                                                //     Object[] objm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ((OBDEBIT+CURDEBIT)-(OBCREDIT+CURCREDIT)) FROM trailbalance where ID=", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idt);
                                                String popbm=objm[0].toString();
                                                Double POPBm = new Double(popbm);
                                                dmang.roundTwoDecimals(POPBm);
                                                Double OPB = POPBm-amt1;
                                                OPB=dmang.roundTwoDecimals(OPB);
                                                new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET OPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{OPB,idt});
                                                
                                                for(int i=1;i<=5;i++){
                                                    String idtg = idt+"#Guest "+i;
                                                    Object[] objg = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idtg);
                                                    if(objg!=null){
                                                        Object[] objg1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idtg);
                                                        String popbg=objg1[0].toString();
                                                        Double POPBg = new Double(popbg);
                                                        dmang.roundTwoDecimals(POPBg);
                                                        Double COPBg = POPBg-amt1;
                                                        COPBg=dmang.roundTwoDecimals(COPBg);
                                                        new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{COPBg,idtg});
                                                        new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET OPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{OPB,idtg});
                                                    }
                                                }
                                            }
                                            ///////////       ///////////////////////     ////////////////////////////
                                            
                                            
                                        }
                                        
                                        
                         
                                        
                                        
                                        
                                        
                                        String acc = dmang.getCustomerAccountByID(customerInfo.getId());
                                        dbmodel = MemDebtBillingTableModel.loadInstance(m_App, cinfo.getId(), acc, dmang);
                                        total.setText("0.0");
                                        tablemodel = dbmodel.getTableModel();
                                        tablemodel.settext(total);
                                        jTable1.setModel(tablemodel);
                                        ctablemodel = dbmodel.getCreditTableModel();
                                        ctablemodel.settext(ctotal);
                                        jTable2.setModel(ctablemodel);
                                        creditamt.setText(dbmodel.getcreditAmount().toString());
                                        debttotal.setText(dbmodel.getdebtAmount().toString()); 
                                       // finalamt.setText(BalStr);
                                        load();
                                    } else {
                                        if (rnum.equals("false")) {
                                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please reset the system time or consult your system admin", "Sorry Cannot Create Receipt", JOptionPane.OK_OPTION);
                                        } else {
                                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error", "Error", JOptionPane.OK_OPTION);
                                        }
                                    }
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Cannot create Receipt,Please UnCheck Credit", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Cannot create Receipt", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                    return null;
                }
            };
            t.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    
     public void createSMS(String messageID, String receiptNo, String FacilityAmount, CustomerInfo customerInfo, String paymentMethod, String netBalance)
    {
        String smsString = smsDBSettings.getMessage(messageID);
        if(smsString != null)
        {
            
            smsString = smsString.replace(SMSgeneralDBSettings.SMS_BILL_KEY, receiptNo);
            smsString = smsString.replace(SMSgeneralDBSettings.SMS_DTM_KEY , Formats.TIMESTAMP.formatValue(new Date()));
            smsString = smsString.replace(SMSgeneralDBSettings.SMS_TOT_AMOUNT_KEY , FacilityAmount);
            smsString = smsString.replace(SMSgeneralDBSettings.SMS_FACILITY_KEY , "");
            smsString = smsString.replace(SMSgeneralDBSettings.SMS_WHAREHOUSE_NAME_KEY , "");
            
            if(paymentMethod.equals("CASH")) {
                smsString = smsString.replace(SMSgeneralDBSettings.SMS_CASH_CHEQUE , "cash");
            } else {
                smsString = smsString.replace(SMSgeneralDBSettings.SMS_CASH_CHEQUE , "Cheque subject to realisation");
            }
             
            String x = m_App.getAppUserView().getUser().getRole();
            smsString = smsString.replace(SMSgeneralDBSettings.SMS_ROLE_KEY ,  LookupUtilityImpl.getInstance(null).getRoleMap().get(x).toString());
            
            if(customerInfo != null)
            {
                smsString = smsString.replace(SMSgeneralDBSettings.SMS_MEMBER_NAME_KEY, customerInfo.getName()); 
                smsString = smsString.replace(SMSgeneralDBSettings.SMS_MEMBER_NO_KEY, customerInfo.getSearchkey()); 
            }
            
            smsString = smsString.replace(SMSgeneralDBSettings.SMS_CUST_BAL_BEFORE, netBalance);
            smsString = smsString.replace(SMSgeneralDBSettings.SMS_CUST_BAL_AFTER, "");
           
            if(customerInfo != null && customerInfo.getMobile() != null && customerInfo.getMobile().trim().length() > 0)
            {
                smsDBSettings.insertSMStoActiveMsgTable(smsString, customerInfo.getMobile(), customerInfo.getId());
            }
        }
        
        
    }
    
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int selectedrowno = jTable1.getSelectedRow();
        try {
            if (selectedrowno >= 0) {
                Double amt = Double.parseDouble(amtvalue.getText());
                if (amt > 0) {
                    Double payableamt = Double.parseDouble(dbmodel.getTableModel().getValueAt(selectedrowno, 3).toString());
                    Double paidamt = Double.parseDouble(dbmodel.getTableModel().getValueAt(selectedrowno, 5).toString());

                    String fname = dbmodel.getTableModel().getValueAt(selectedrowno, 0).toString();
                    boolean flag = true;
                    Double damt = Double.parseDouble(total.getText()) + amt;
                    Double camt = dmang.roundTwoDecimals(Double.parseDouble(ctotal.getText()));
                    if (camt > 0 && damt > camt) {
                        if (fname.equals("Account Pay")) {
                            JOptionPane.showMessageDialog(this, "Cannot Process The Request", null, JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Amount Exceeds the selected credit", null, JOptionPane.WARNING_MESSAGE);
                        }
                        flag = false;
                    }
                    if (flag == true) {
                        if (amt <= payableamt || fname.equals("Account Pay")) {
                            dbmodel.getTableModel().setValueAt(false, selectedrowno, 7);
                            dbmodel.getTableModel().setValueAt(amt, selectedrowno, 5);
                            tablemodel = dbmodel.getTableModel();
                            tablemodel.settext(total);
                            jTable1.setModel(tablemodel);
                            Double totalamt = Double.parseDouble(total.getText()) + amt - paidamt;
                            total.setText(String.valueOf(dmang.roundTwoDecimals(totalamt)));

                        } else {
                            JOptionPane.showMessageDialog(this, "The Entered Amount Exceeds the required value", "Sorry", JOptionPane.OK_OPTION);
                        }
                    }
                    amtvalue.setText(null);

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid amount", "Invalid amount", JOptionPane.OK_OPTION);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid amount", "Invalid amount", JOptionPane.OK_OPTION);
            }
        } catch (Exception e) {
            //nomber format
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jNumberKeys1KeyPerformed(com.openbravo.beans.JNumberEvent evt) {//GEN-FIRST:event_jNumberKeys1KeyPerformed
        // TODO add your handling code here:
        stateTransition(evt.getKey());
}//GEN-LAST:event_jNumberKeys1KeyPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        MemDebtBillingTableModel.Facilityline f = new MemDebtBillingTableModel.Facilityline();
        Timestamp tm = new Timestamp(getdate().getTime());
        f.setaccid("Account Pay");
        f.setduedate(tm);
        f.setfname("Account Pay");
        f.setamount(0.00);
        f.setAmountb(0.00);
        f.setNarration("Account Pay on " + tm);

        dbmodel.addfacility(f);
        jTable1.setModel(dbmodel.getTableModel());
        int row = jTable1.getRowCount();
        jTable1.setRowSelectionInterval(row - 1, row - 1);
    }//GEN-LAST:event_jButton5ActionPerformed
//praveen: added to print debt list and credit list...............start

    private JRField[] getFields1() throws JRException, UnsupportedOperationException {
        JRField[] fields = new JRField[12];
        fields[0] = (JRField) new JRBasicField("FacilityName", "fname", java.lang.String.class, "java.lang.String");
        fields[1] = (JRField) new JRBasicField("BilledDate", "date", java.util.Date.class, "java.util.Date");
        fields[2] = (JRField) new JRBasicField("Duedate", "duedate", java.util.Date.class, "java.util.Date");
        fields[3] = (JRField) new JRBasicField("Debt", "amount", java.lang.Double.class, "java.lang.Double");
        fields[4] = (JRField) new JRBasicField("Narration", "narration", java.lang.String.class, "java.lang.String");

        fields[5] = (JRField) new JRBasicField("Org.Amt", "amt", java.lang.Double.class, "java.lang.Double");


        return fields;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // TODO add your handling code here:
        try {
            Map reportparams = new HashMap();
            reportparams.put("companyName", m_App.getSession().getCompanyName());
            reportparams.put("companyAddress", m_App.getSession().getCompanyAddress());
            reportparams.put("memno", memno.getText().toUpperCase());
            reportparams.put("membername", mname.getText().toString());
            reportparams.put("Debttotal", Double.parseDouble(debttotal.getText()));
            reportparams.put("Credittotal", Double.parseDouble(creditamt.getText()));
            DataSourceProvider data1 = new DataSourceProvider(dbmodel.getfacilityline());
            data1.setFields(getFields1());
            DataSourceForMemDebtList ds1 = new DataSourceForMemDebtList(dbmodel.getfacilityline());
            DataSourceForCreditList ds2 = new DataSourceForCreditList(dbmodel.getCreditList());
            DataSourceForMemDebtList ds3 = new DataSourceForMemDebtList(dbmodel.getfacilityline());
            DataSourceForCreditList ds4 = new DataSourceForCreditList(dbmodel.getCreditList());
            reportparams.put("DebitDataSource", ds1);
            reportparams.put("CreditDataSource", ds2);
            if (dbmodel.getfacilityline() != null && dbmodel.getfacilityline().size() > 0) {
                data1.setDataSource(ds3);
            } else {
                data1.setDataSource(ds4);
            }
            //JasperPrint jp = JasperReportNew.runReport(m_App, "./reports/com/openbravo/reports/DebtListReport.jrxml", reportparams, false, data1, true, null);
            JasperPrint jp = JasperReportNew.runReport(m_App, "./reports/com/openbravo/reports/CreditDebitList.jrxml", reportparams, false, data1, true, null);
            reportviewer.loadJasperPrint(jp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void memnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_memnoActionPerformed

    private void creditamtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditamtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_creditamtActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if (customerInfo != null) {
            String memid = customerInfo.getId();
            String accid = customerInfo.getAccno();
            try {
                climit = new CaluculateLimit();
                Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT MEMID,FACID FROM FACILITYLIMITMASTER WHERE MEMID=?",
                        SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING, Datas.STRING,})).find(memid);
                if (obj != null) {
                    climit.calculateFacilityLimitToMemberAndUpdate(m_App.getSession(), memid, accid);
                //JOptionPane.showMessageDialog(null, "caluculated successfully");
                } else {
                    climit.calculateFacilityLimitToMemberAndInsert(m_App.getSession(), memid, accid);
                //JOptionPane.showMessageDialog(this, "caluculated successfully");
                }
                JOptionPane.showMessageDialog(this, "caluculated successfully");
            } catch (BasicException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select Member To caluculate");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void oldaccviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oldaccviewActionPerformed
        // TODO add your handling code here:
        if (customerInfo != null) {
            String searchKey = memno.getText().toUpperCase().toString();
            String name = mname.getText().toString();
            String filePath = m_App.getProperties().getProperty("OldAccountFilePath") + "\\" + searchKey + ".txt";
            System.out.println(filePath);
            try {
                // Open the file that is the first
                // command line parameter
                FileInputStream fstream = new FileInputStream(filePath);
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                StringBuffer str = new StringBuffer("");
                //Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                    // Print the content on the console
                    System.out.println(strLine);
                    str.append(strLine);
                    str.append("\r\n");
                }
                System.out.println(str);
                Details det = Details.getDialog(this, m_App);
                det.showDialog(str);
                //Close the input stream
                in.close();
            } catch (Exception e) {//Catch exception if any
                System.err.println("Error: " + e.getMessage());
                JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Select Member To caluculate");
        }



    }//GEN-LAST:event_oldaccviewActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
       String card = jTextField3.getText();
        
        if (card.length() > 0) {

            try {
                 Object[] obj = dmang.getMamberbycard(card);
                if (obj == null) {
                    JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                } else {
                    customerInfo = new CustomerInfo(obj[0].toString());
                    customerInfo.setSearchkey(obj[1].toString());
                    customerInfo.setName(obj[2].toString());
                    customerInfo.setMobile(String.valueOf(obj[3]));
                    customerInfo.setAccno(obj[4].toString());
                    System.out.println(customerInfo.getAccno());
                    memno.setText(obj[1].toString());
                    mname.setText(obj[2].toString());
                    load();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "swipe a card");
        }
 //akshatha:to read a card from card reader without port num
}//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                
                jTextField3.requestFocus();
                jTextField3.setText(null);
               loadData();
                
               
                
               

            }
        }); //akshatha:to read a card from card reader without port num
}//GEN-LAST:event_jButton10ActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
       String card = cr.getData().toString();
        if (card != null) {
            loadMemberDetails(card);
        }else{
            String cust = jTextField3.getText();
            loadMemberDetails(cust);
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

         
       tablemodel = dbmodel.getTableModel();
       tablemodel.settext(total);
       jTable1.setModel(tablemodel);
       
       int rowcnt=tablemodel.getRowCount();
       //System.out.println("Rowcnt="+rowcnt);
       
       if(jCheckBox1.isSelected()){
           
           for(int i=0; i<rowcnt; i++){

               tablemodel.setValueAt(true, i, 6);
           }
       }else if(jCheckBox1.isSelected()==false){
           for(int i=0; i<rowcnt; i++){

               tablemodel.setValueAt(false, i, 6);
           }
           
       }
       
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void resetQTlimit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetQTlimit_btnActionPerformed
        
         String BMName="Credit Check for billing member";
         String BName2 = "Facility for billing member";
            
            try{
            
                Object[] FacObj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE where NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.BOOLEAN})).find(BMName);

                if(FacObj!=null){

                     Boolean v5 = (Boolean)FacObj[0];
                     if(v5){
                         
                         
                           ///////////       ///////////////////////     ////////////////////////////
                         
                           // if all is selected for billing member restriction .... do nothing .... 
                          JOptionPane.showMessageDialog(this, "Sorry , no facility selected.");
                         
                           ///////////       ///////////////////////     ////////////////////////////
                         
                         
                     }
                     else{
                        
                         
                             Object[] Fac2Obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE where NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(BName2);
                             String FacStrFull  = Fac2Obj[0].toString();
                             String []strarr = FacStrFull.split("#");
                             String FinFacId = strarr[0];
                             
                         
                             Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ACCOUNT FROM CUSTOMERS where ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(customerInfo.getId());
                             String aid = obj[0].toString();
                             
                           //  Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT IFNULL(SUM(A.BALANCEAMOUNT),0.00) FROM ACCOUNTJOURNAL A  WHERE ACCOUNTID=?  AND TRANSREF=? AND TRANSTYPE='D' AND ACTIVE IS TRUE", new SerializerWriteBasic(new Datas[]{Datas.STRING , Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(new Object[]{aid , FinFacId});
                             Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT IFNULL(SUM(A.BALANCEAMOUNT),0.00) FROM ACCOUNTJOURNAL A  WHERE ACCOUNTID=?   AND ACTIVE IS TRUE", new SerializerWriteBasic(new Datas[]{Datas.STRING }), new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(new Object[]{aid });
                           
                           String opbs = obj1[0].toString();
                             Double opb = new Double(opbs);
                             
                             // get pending bills 
                             Object[] PendBillObj = (Object[]) new StaticSentence(m_App.getSession(), "select ifnull(sum(b.amount + b.taxtotal),0.00) from bill b, locations l\n" +
                                                                                                        "where b.paid = 0 and l.facility = ? and l.id = b.warehouse\n" +
                                                                                                        "and b.CUSTOMER = ? ", new SerializerWriteBasic(new Datas[]{Datas.STRING , Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(new Object[]{FinFacId , customerInfo.getId()});
                             String PendBillStr = PendBillObj[0].toString();
                             Double PendBillAmt = new Double(PendBillStr);
                             System.out.println("Facility wise pending bill amt : "+PendBillAmt);
                             

                             // get pending QTs 
                             Object[] PendQTObj = (Object[]) new StaticSentence(m_App.getSession(), "select ifnull(sum(dmultiply*rate),0.00) from qtitems t , qticket q , locations l\n" +
                                                                                                    "where t.parentid = q.id and q.billed=0 and \n" +
                                                                                                    "q.customer=? and l.id = q.warehouse and l.facility=? ", new SerializerWriteBasic(new Datas[]{Datas.STRING , Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(new Object[]{customerInfo.getId() , FinFacId });
                             String PendQTStr = PendQTObj[0].toString();
                             Double PendQTAmt = new Double(PendQTStr);    
                             System.out.println("Facility wise pending QT amt : "+PendQTAmt);

                             
                             Double PendingBillQT =   PendQTAmt+PendBillAmt;
                             Double AllTotal = opb+PendingBillQT;
                             
                             AllTotal = dmang.roundTwoDecimals(AllTotal);
                             
                             
                                 ///////////       ///////////////////////     ////////////////////////////       
                                //For:-CURROPB
                                           
                                            Object[] objchk = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ID FROM billingmember where ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(customerInfo.getId());
                                            if(objchk!=null){
                                                //     Double amt1 = amt;
                                               // Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(customerInfo.getgetId());
                                               // String popb=obj[0].toString();
                                                //   Double POPB = new Double(popb);
                                                 //  dmang.roundTwoDecimals(POPB);
                                                 //  Double COPB = POPB-amt1;
                                                 //  COPB=dmang.roundTwoDecimals(COPB);
                                                  
                                                new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{AllTotal,customerInfo.getId()});
                                 //For:-OPB                 
                                             //  Object[] objm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((OPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(customerInfo.getgetId());
                                          //     Object[] objm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ((OBDEBIT+CURDEBIT)-(OBCREDIT+CURCREDIT)) FROM trailbalance where ID=", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idt);
                                               // String popbm=objm[0].toString();
                                                //   Double POPBm = new Double(popbm);
                                               //    dmang.roundTwoDecimals(POPBm);
                                               //    Double OPB = POPBm-amt1;
                                               //    OPB=dmang.roundTwoDecimals(OPB);
                                                new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET OPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{AllTotal,customerInfo.getId()});

                                               for(int i=1;i<=5;i++){
                                                   String idtg = customerInfo.getId()+"#Guest "+i;
                                                   Object[] objg = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idtg);
                                                   if(objg!=null){
                                                     //  Object[] objg1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(idtg);
                                               // String popbg=objg1[0].toString();
                                               //   Double POPBg = new Double(popbg);
                                               //    dmang.roundTwoDecimals(POPBg);
                                                //   Double COPBg = POPBg-amt1;
                                              //     COPBg=dmang.roundTwoDecimals(COPBg);
                                                       new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{AllTotal,idtg});
                                                       new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET OPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{AllTotal,idtg});
                                                   }
                                               }
                                                JOptionPane.showMessageDialog(this, "Updated Successfully");
                                               
                                            }
                        ///////////       ///////////////////////     ////////////////////////////       
                             
                             
                             
                             
                             
                     }
                     
                }
                else{
                     JOptionPane.showMessageDialog(this, "Error occurred");
                 }
            }
            catch(BasicException e){
                
                
            }
        
        
    }//GEN-LAST:event_resetQTlimit_btnActionPerformed
 public long startSec = 0;
    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
        
        char c = evt.getKeyChar();
        if(jTextField3.getText()!=null){
            int length = jTextField3.getText().trim().length();
            if(length==1){
                startSec = System.currentTimeMillis();
            }
            else if(length>1){
                long Currsec = System.currentTimeMillis();
                long diff = Currsec-startSec;
                if(diff>750){
                    JOptionPane.showMessageDialog(this, "Do not use keyboard. Please swipe card.");
                    jTextField3.setText(null);
                     System.out.println("Time Taken : "+diff);
                }
                if(c==KeyEvent.VK_ENTER){
                    System.out.println("Time Taken : "+diff);
                }
            }
        }
    }//GEN-LAST:event_jTextField3KeyPressed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       if(customerInfo!=null){
           String MemberNo = customerInfo.getSearchkey();
           String MemberName = customerInfo.getName();
           String Cardno = jTextField3.getText();
           
            memberPhotoInfo memList;
            try {
                memList = memberPhotoInfo.getDialog(this, m_App,true,MemberNo,MemberName,Cardno);
                memList.showDialog();
            } catch (BasicException ex) {
                Logger.getLogger(MemberDebtBilling.class.getName()).log(Level.SEVERE, null, ex);
            }
           
       }
       
       else{
           JOptionPane.showMessageDialog(this, "Select Member first !");
       }
        
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void finalamtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalamtActionPerformed
        // TODO add your handling code here: 
       
    }//GEN-LAST:event_finalamtActionPerformed

    private void ctotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctotalActionPerformed

    private void totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalActionPerformed

    private void mnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnameActionPerformed

    private JRField[] getFields() throws JRException, UnsupportedOperationException {
        JRField[] fields = new JRField[6];
        fields[0] = (JRField) new JRBasicField("Perticular", "transref", java.lang.String.class, "java.lang.String");
        fields[1] = (JRField) new JRBasicField("No", "transno", java.lang.String.class, "java.lang.String");
        fields[2] = (JRField) new JRBasicField("Date", "date", java.util.Date.class, "java.util.Date");
        fields[3] = (JRField) new JRBasicField("Narration1", "narration", java.lang.String.class, "java.lang.String");
        fields[4] = (JRField) new JRBasicField("Amount", "amount", java.lang.Double.class, "java.lang.Double");
        fields[5] = (JRField) new JRBasicField("Org.Amt", "amt", java.lang.Double.class, "java.lang.Double");
        return fields;
    }//praveen: added to print debt list and credit list...............finish
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amtvalue;
    private javax.swing.JButton btnLoad;
    private javax.swing.JTextField creditamt;
    private javax.swing.JLabel creditlabel;
    private javax.swing.JTextField ctotal;
    private javax.swing.JTextField debttotal;
    private javax.swing.JTextField finalamt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private com.openbravo.beans.JNumberKeys jNumberKeys1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField memno;
    private javax.swing.JTextField mname;
    private javax.swing.JButton oldaccview;
    private javax.swing.JLabel opr;
    private javax.swing.JButton resetQTlimit_btn;
    private javax.swing.JTextField total;
    // End of variables declaration//GEN-END:variables

    private void loadMemberDetails(String card) {
       
       card = cr.getData();
         String cust = jTextField3.getText();
        if (card.length() > 0) {

            try {
                 Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT C.ID,C.SEARCHKEY,C.NAME,M.CARD,M.ID  FROM CUSTOMERS C,MEMDEPENDENT M WHERE M.MEMNO=C.ID AND M.CARD=?  UNION ALL  SELECT C.ID,C.SEARCHKEY,C.NAME,C.CARD,C.ID  FROM CUSTOMERS C WHERE C.CARD=?",
                        new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING})).find(new Object[]{card, card});
                if (obj == null) {
                    JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                } else {
                     customerInfo = new CustomerInfo(obj[0].toString());
                   customerInfo.setSearchkey(obj[1].toString());
                    customerInfo.setName(obj[2].toString());
                    customerInfo.setMobile(String.valueOf(obj[3]));
                    customerInfo.setAccno(obj[4].toString());
                    System.out.println(customerInfo.getAccno());
                    memno.setText(obj[1].toString());
                    mname.setText(obj[2].toString());
                    load();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(cust.length()>0){
            try {
                 Object[] obj = dmang.getMamberbycard(cust);
                if (obj == null) {
                    JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                } else {
                     customerInfo = new CustomerInfo(obj[0].toString());
                   customerInfo.setSearchkey(obj[1].toString());
                    customerInfo.setName(obj[2].toString());
                    customerInfo.setMobile(String.valueOf(obj[3]));
                    customerInfo.setAccno(obj[4].toString());
                    System.out.println(customerInfo.getAccno());
                    memno.setText(obj[1].toString());
                    mname.setText(obj[2].toString());
                    load();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(this, "swipe a card");
        }
    } //akshatha:to read a card from card reader with port num
    
    public void cardswiped(String custCard) {
        try {
            loadMemberDetails(custCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //akshatha:to read a card from card reader with port num

    public void startCardReader() {
        try {
            String portNumber = m_App.getProperties().getProperty("card.portnumber");
            boolean cardAccessOnlyFlag = false;
            if (m_App.getProperties().getProperty("cardAccessOnly") != null) {
                cardAccessOnlyFlag = Boolean.valueOf(m_App.getProperties().getProperty("cardAccessOnly"));
            }
            cr = new CardReader(portNumber, cardAccessOnlyFlag);
            cr.setCardSwipeNotifier((CardSwipeNotifier) this);
            System.out.println(portNumber);
            cr.ConfigurePort();
        } catch (Exception ex) {
            ex.printStackTrace();
        }




    } //akshatha:to read a card from card reader with port num

    public void cardswiped(WaiterInfo waiter) {
       
    } //akshatha:to read a card from card reader with port num
}
