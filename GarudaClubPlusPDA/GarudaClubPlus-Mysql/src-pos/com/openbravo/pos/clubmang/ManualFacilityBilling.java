/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ManualFacilityBilling.java
 *
 * Created on Jan 19, 2009, 3:04:44 PM
 */

package com.openbravo.pos.clubmang;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.clubmang.MemberDependent;
import com.openbravo.pos.customers.CustomerInfo;
//import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.LookupUtilityImpl;
//import com.openbravo.pos.payment.JPaymentSelect;
//import com.openbravo.pos.payment.JPaymentSelectReceipt;
//import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
//import com.openbravo.pos.sales.BillInfo;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TaxInfo;
import java.awt.event.KeyEvent;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author swathi
 */
public class ManualFacilityBilling extends javax.swing.JPanel implements JPanelView,BeanFactoryApp {

    private DataLogicFacilities dmang;
    private CustomerInfo customerInfo;
    private DataLogicCustomers dlCustomers;
    private ComboBoxValModel fmodel;
    private ComboBoxValModel dmodel;
    private AppView m_App;
    private Date sdate;
    private Date lbdate;
    private Date edate;
    private Facility f=null;
    private Periodicity p=null;
    private TaxesLogic taxeslogic;
    private DataLogicSales m_dlSales;
    private DataLogicSystem dlsystem;
    private TicketParser ttp;
    private String servicetaxacc;
    private double taxrate;
    private double total;
    private double taxtotal;
    private FacilityLogic flogic;
    private ManualFacilityBillingTableModel mfmodel;
    List<ManualFacilityBillingTableModel.Receiptline> list;
    private String billnum;
    private double totalamt=0.0,totaltax=0.0;
    /** Creates new form ManualFacilityBilling */
    public ManualFacilityBilling() {
        initComponents();

    }
     public String getTitle() {
        return null;
    }
    public void init(AppView app) throws BeanFactoryException {
         m_App=app;
         dmang=(DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
         dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
         m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
         dlsystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
         ttp = new TicketParser(app.getDeviceTicket(), dlsystem);
         UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
    }
    public void activate() throws BasicException {
       memno.setText(null);
       mname.setText(null);
       taxeslogic = new TaxesLogic(m_dlSales.getTaxList().list());
       mfmodel=ManualFacilityBillingTableModel.emptyinstance();
       jTable1.setModel(mfmodel.getTableModel());
       dmodel=new ComboBoxValModel();
       fmodel=new ComboBoxValModel();
       facilitylist.setModel(fmodel);
       dependent.setModel(dmodel);
       dependent.setSelectedIndex(-1);
       billnum=dmang.getNextBillno("Manual Facility Billing Seq");
       customerInfo=null;
       servicetaxacc=null;
       loaddata();
    }
    private void loaddata() throws BasicException{
       flogic=new FacilityLogic(dmang);
       jRadioButton1.setSelected(true);
       jLabel11.setVisible(false);
       prevbillno.setVisible(false);
       sdate=new Date();
       lbdate=new Date();
       edate=new Date();
       jButton1.setEnabled(false);
       jLabel9.setVisible(false);
       facilitylist.setSelectedIndex(-1);
       periodtype.setText(null);
       fdate.setText(null);
       nperiod.setText(null);
       todate.setText(null);
       rate.setText(null);
       amount.setText(null);
       jLabel11.setVisible(true);
       prevbillno.setVisible(true);
       prevbillno.setText(null);
       java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                memno.requestFocus();
            }
        });
       
       jLabel12.setVisible(false);
       dependent.setVisible(false);
       total=0;
       taxrate=0;
       taxtotal=0;
       
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
    private Date calculateEndDate(Periodicity p,Date d,int no){
      Date ed=new Date();
      ed.setTime(d.getTime());
      Calendar cal=Calendar.getInstance();
      cal.setTimeInMillis(ed.getTime());
      if(p.gettype().equals("Months")){
        cal.add(Calendar.MONTH, p.getno()*no);
        if(p.getdoj()==false)
              cal.set(Calendar.DATE, Integer.parseInt(p.getdate()));
      }else if(p.gettype().equals("Days")){
              cal.add(Calendar.DATE,  p.getno()*no);
      }else if(p.gettype().equals("Years")){
              cal.add(Calendar.YEAR,  p.getno()*no);
              if(p.getdoj()==false){
                 cal.set(Calendar.DATE, Integer.parseInt(p.getdate()));
                  cal.set(Calendar.MONTH, Integer.parseInt(p.getmonthname()));
              }
      }else if(p.gettype().equals("Quaterly")){
            for(int i=0;i<no;i++){
               if(0 <= cal.get(Calendar.MONTH) && cal.get(Calendar.MONTH) <=2){
                  if(p.getdoj()==false){
                   cal.set(Calendar.DATE, Integer.parseInt(p.getdate()));
                  }
                  cal.set(Calendar.MONTH, 3);
               }else if(3 <= cal.get(Calendar.MONTH) && cal.get(Calendar.MONTH) <=5){
                 if(p.getdoj()==false){
                  cal.set(Calendar.DATE, Integer.parseInt(p.getdate()));
                 }
                  cal.set(Calendar.MONTH, 6);
               }else if(6 <= cal.get(Calendar.MONTH) && cal.get(Calendar.MONTH) <=8){
                   if(p.getdoj()==false){
                     cal.set(Calendar.DATE, Integer.parseInt(p.getdate()));
                   }
                  cal.set(Calendar.MONTH, 9);
               }else if(9 <= cal.get(Calendar.MONTH) && cal.get(Calendar.MONTH) <=11){
                  if(p.getdoj()==false){
                   cal.set(Calendar.DATE, Integer.parseInt(p.getdate()));
                  }
                  cal.set(Calendar.MONTH, 0);
                  cal.add(Calendar.YEAR, 1);
               }
              }
            }
            ed.setTime(cal.getTimeInMillis());
             todate.setText(Formats.DATE.formatValue(ed));
             return ed;
         }

     private Date getdate(){
        Date d=new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        d.setTime(cal.getTimeInMillis());
        return d;
     }

   private double calculateaccurateAmt(Date sdate,Date edate ){
     Calendar caltemp1=Calendar.getInstance();
     Calendar caltemp2=Calendar.getInstance();
     caltemp1.setTimeInMillis(sdate.getTime());
     caltemp2.setTimeInMillis(edate.getTime());
     long difMil1 = caltemp2.getTimeInMillis() - caltemp1.getTimeInMillis();
     int days1 = (int)((difMil1 + 12 * 3600000L) / (24 * 3600000L));//no of days between sdate and edate
     caltemp1.set(Calendar.DATE, caltemp2.get(Calendar.DATE));
     long difMil = caltemp2.getTimeInMillis() - caltemp1.getTimeInMillis();
     int days = (int)((difMil + 12 * 3600000L) / (24 * 3600000L));//total days b/w sdate's month to edate's month
     return  f.getramt() * days1/days;
   }

    private void printTicket(String billnum,Double amt,List<ManualFacilityBillingTableModel.Receiptline> list) throws ScriptException {

        String sresource = LookupUtilityImpl.getInstance(null).getDataLogicSystem().getResourceAsXML("Printer.FacilityBilling");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try{
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("createdby", m_App.getAppUserView().getUser().getName());
                String x = m_App.getAppUserView().getUser().getRole();
                script.put("role", LookupUtilityImpl.getInstance(null).getRoleMap().get(x).toString());
                script.put("host", m_App.getProperties().getHost());
                script.put("total", String.valueOf(amt));
                script.put("mname", customerInfo.getName());
                script.put("mid", customerInfo.getSearchkey());
                script.put("date", Formats.TIMESTAMP.formatValue(new Date()));
                script.put("ticket", list);
                script.put("bnum", billnum);
                ttp.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
            catch (Exception e1) {
                new MessageInf(e1).show(this);
            }
        }
    }
    private void getServiceTaxacc() throws BasicException{
       servicetaxacc=null;
       Object stacc=new StaticSentence(m_App.getSession()
                       , "SELECT VALUE FROM GENERALTABLE WHERE NAME = ? "
                       , SerializerWriteString.INSTANCE
                       ,SerializerReadString.INSTANCE).find("Service Tax Account");
           if(stacc!=null)
               servicetaxacc=stacc.toString();
    }
    private void TaxDetail() throws BasicException{
       taxrate=0.0;
       if(f.getservicetax()!=null){
          TaxCategoryInfo tinfo=(TaxCategoryInfo)m_dlSales.getTaxCategoryByid(f.getservicetax());
          TaxInfo tax = taxeslogic.getTaxInfo(tinfo);
          taxrate=tax.getRate();
       }
    }
    private void setenddate(int num,int billabledate){
        Date d=flogic.calculateEndDate1(sdate, p, billabledate, num);
        edate.setTime(d.getTime());
        todate.setText(Formats.DATE.formatValue(edate));
       // double amt=0;
        if(p.getdoj()==false && p.getaccurate()==true){
              total= calculateaccurateAmt(sdate, edate);
              taxtotal=Math.floor(total*taxrate);
              total+=taxtotal;
              amount.setText(String.valueOf(dmang.roundTwoDecimals(total)));
        }else{
              total=f.getramt()*num;
              taxtotal=Math.floor(total*taxrate);
              amount.setText(String.valueOf(dmang.roundTwoDecimals(total+taxtotal)));
        }
    }
    private void incrementDate(Date d){
       Calendar cal=Calendar.getInstance();
       cal.setTimeInMillis(d.getTime());
       cal.set(Calendar.HOUR_OF_DAY, 00);
       cal.set(Calendar.MINUTE, 00);
       cal.set(Calendar.SECOND, 00);
       cal.set(Calendar.MILLISECOND, 00);
       cal.add(Calendar.DATE, 1);
       d.setTime(cal.getTimeInMillis());
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
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        memno = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        facilitylist = new javax.swing.JComboBox();
        fdate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        nperiod = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        todate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        mname = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        periodtype = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        amount = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        rate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        prevbillno = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        dependent = new javax.swing.JComboBox();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        jLabel2.setText("Mem No");

        memno.setNextFocusableComponent(jRadioButton1);
        memno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                memnoKeyPressed(evt);
            }
        });

        jLabel1.setText("Facility");

        facilitylist.setNextFocusableComponent(nperiod);
        facilitylist.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                facilitylistItemStateChanged(evt);
            }
        });

        fdate.setEditable(false);

        jLabel4.setText("From date");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        jButton1.setNextFocusableComponent(nperiod);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("No. Of Period");

        nperiod.setNextFocusableComponent(jButton3);
        nperiod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nperiodKeyReleased(evt);
            }
        });

        jLabel6.setText("To date");

        todate.setEditable(false);

        jLabel3.setText("Mem Name");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel7.setText("Period Type");

        periodtype.setEditable(false);

        jLabel8.setText("Amount");

        amount.setEditable(false);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/greenled.png"))); // NOI18N
        jLabel9.setToolTipText("Please press enter for the changes to apply");

        jLabel10.setText("Rate");

        rate.setEditable(false);

        jLabel11.setText("Previous Bill No");

        prevbillno.setEditable(false);

        jLabel12.setText("Dependent");

        dependent.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dependentItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Member");
        jRadioButton1.setNextFocusableComponent(jRadioButton2);
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Dependent Member");
        jRadioButton2.setNextFocusableComponent(dependent);
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });

        jLabel13.setText("Type");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(prevbillno, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                            .addComponent(todate, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(nperiod, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel9)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(facilitylist, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(fdate, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(2, 2, 2)))
                                .addGap(87, 87, 87)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(memno, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(76, 76, 76)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(44, 44, 44)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dependent, 0, 188, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(amount)
                                .addComponent(rate, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                            .addComponent(periodtype, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mname, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(memno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(mname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton4))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jLabel13)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(facilitylist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(dependent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7)
                                    .addComponent(periodtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(rate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(nperiod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(todate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(prevbillno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))))
        );

        jButton3.setText("Bill");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

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
        jScrollPane1.setViewportView(jTable1);

        jLabel14.setText("List");

        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        remove.setText("Remove");
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(451, 451, 451))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(remove, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(add, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(add)
                        .addGap(2, 2, 2)
                        .addComponent(remove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void memnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_memnoKeyPressed
        // TODO add your handling code here:
        try{
        String skey=null;
         if(evt.getKeyText(evt.getKeyCode()).equals("Enter")){
            
                skey=memno.getText();
               Object[] obj= dmang.getMamberbySkey(memno.getText().toUpperCase());
            if(obj==null)
            {
                JOptionPane.showMessageDialog(this,AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                activate();
                memno.setText(skey);
                customerInfo=new CustomerInfo(obj[0].toString());
                customerInfo.setName(obj[1].toString());
                customerInfo.setMobile(String.valueOf(obj[3]));
                mname.setText(obj[1].toString());
                List<Facility> flist=dmang.getmemberscurrentfacilities(customerInfo.getId());
                fmodel=new ComboBoxValModel(flist);
                facilitylist.setModel(fmodel);
            }
           
        }else{
           if(mname.getText().length()>0){
             mname.setText(null);
             customerInfo=null;
             activate();
           }
        }
      }
      catch(Exception e){
              e.printStackTrace();
      }
    }//GEN-LAST:event_memnoKeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        finder.setVisible(true);
        try {
            activate();
        } catch (BasicException ex) {
           ex.printStackTrace();
        }
        customerInfo = finder.getSelectedCustomer();
        if (customerInfo != null) {
            try {
                
                mname.setText(customerInfo.toString());
                memno.setText(customerInfo.getSearchkey());
                List<Facility> flist=dmang.getmemberscurrentfacilities(customerInfo.getId());
                fmodel=new ComboBoxValModel(flist);
                facilitylist.setModel(fmodel);
            } catch (Exception e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
                msg.show(this);
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed
/*
 *  jLabel9.setVisible(false);
          int num=Integer.parseInt(nperiod.getText());
          Calendar cal=Calendar.getInstance();
          cal.setTimeInMillis(sdate.getTime());
          int billabledate=cal.get(Calendar.DATE);
          if(num>0){
           setenddate(num,billabledate);
          }
          else{
            nperiod.setText("1");
            setenddate(1,billabledate);
 * */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
          Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(fdate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendar(this, date);
        if (date != null) {
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            Calendar cals=Calendar.getInstance();
            cals.setTime(lbdate);
            if(cal.after(cals)==true){
                JOptionPane.showMessageDialog(this, "Please select a date previous to the specified date", null, JOptionPane.OK_OPTION);
                fdate.setText(Formats.DATE.formatValue(sdate));
            }else{
          if(p!=null){
             nperiod.setText("1");
            cal.setTimeInMillis(date.getTime());
            int billabledate=cal.get(Calendar.DATE);
            if(p.getdoj()==false){
              cal.setTimeInMillis(flogic.getBillableDate(p, billabledate, cal).getTimeInMillis());
            }
            date.setTime(cal.getTimeInMillis());
            sdate.setTime(date.getTime());
            billabledate=cal.get(Calendar.DATE);
            setenddate(1,billabledate);
            fdate.setText(Formats.DATE.formatValue(date));
            sdate.setTime(date.getTime());
               }
            }
     
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void facilitylistItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_facilitylistItemStateChanged
        // TODO add your handling code here:
        try{
         if(facilitylist.getSelectedIndex()!=-1){
        //  if(dependent.isVisible()==false){
          f=(Facility)facilitylist.getSelectedItem();
          flogic=new FacilityLogic(dmang);
          TaxDetail();
          if(jRadioButton1.isSelected()==true){
            Date d=new Date();
          if(f.getrperiod()!=null){
            p=dmang.getPerioicitybyid(f.getrperiod());
            periodtype.setText(p.getName());
            Object[] obj=(Object[])new StaticSentence(m_App.getSession()
                    , "SELECT LBILLDATE,SDATE,BILLREF,FACMANGREF FROM MEMFACILITYUSAGE WHERE MEMNO=? AND FACILITYTYPE=?  AND USERID IS NULL AND ACTIVE=TRUE "
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
                    ,new SerializerReadBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING,Datas.STRING})).find(new Object[]{customerInfo.getId(),f.getid()});
            if(obj[3]==null){
            if(obj[0]==null){
               if(obj[1]!=null){
                 d=(Date)(obj[1]);
                 fdate.setText(Formats.DATE.formatValue(d));
               }
            }else{
                 d=(Date)(obj[0]);
                 fdate.setText(Formats.DATE.formatValue(d));
            }
            if(obj[2]!=null)
                prevbillno.setText(obj[2].toString());
            else
                prevbillno.setText(null);
            
            sdate.setTime(d.getTime());
            lbdate.setTime(d.getTime());
            rate.setText(String.valueOf(dmang.roundTwoDecimals(f.getramt())));
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(lbdate.getTime());
            cal.add(Calendar.DATE, 1);
            int billabledate=cal.get(Calendar.DATE);
            if(p.getdoj()==false){
              cal.setTimeInMillis(flogic.getBillableDate(p, billabledate, cal).getTimeInMillis());
            }
            lbdate.setTime(cal.getTimeInMillis());
            sdate.setTime(cal.getTimeInMillis());
            nperiod.setText("1");
            billabledate=cal.get(Calendar.DATE);
            setenddate(1,billabledate);
            fdate.setText(Formats.DATE.formatValue(lbdate));
            jButton1.setEnabled(true);
            }else{
                facilitylist.setSelectedIndex(-1);
                JOptionPane.showMessageDialog(null, "There is a pending request for this facility", "Cannot Bill this facility", JOptionPane.ERROR_MESSAGE);
            }
           }else{
               fdate.setText(null);
               todate.setText(null);
               periodtype.setText(null);
               rate.setText(null);
               amount.setText(null);
               nperiod.setText(null);
               prevbillno.setText(null);
           }
         }else{
             if(facilitylist.getSelectedIndex()!=-1){
               List dlist=dmang.getmembersDependentsUsingfacility(customerInfo.getId(), f.getid());
               dmodel=new ComboBoxValModel(dlist);
               dependent.setModel(dmodel);
             }
         }
      //   }
         }
        }catch(Exception e){
          e.printStackTrace();
        }
    }//GEN-LAST:event_facilitylistItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
   try{
       list=mfmodel.getReceiptlist();
       totaltax=0.0;
       totalamt=0.0;
       for(ManualFacilityBillingTableModel.Receiptline line:list){
            totaltax += line.getTaxamt();
            totalamt += line.getAmount();
          }

   Transaction t=new Transaction(m_App.getSession()){
        public Object transact() throws BasicException {
      if( customerInfo!= null && jLabel9.isVisible()==false && totalamt+totaltax >0){
          int dblen=0;
          String dbname="";
          try{
          dbname=m_App.getSession().getDatabaseName();
          dblen=dbname.length();
          }catch(Exception e){

          }
          Date date=getdate();
          AppUser user=m_App.getAppUserView().getUser();
          String cacc=dmang.getCustomerAccountByID(customerInfo.getId());
          String tid=UUID.randomUUID().toString();
          
          String narration=null;
          String smstemp1=null,smstemp2=null;
          String smsmsg="Dear Member,\rUr a/c with us has been debited by "+dmang.ConvertDoubleToString(totalamt+totaltax)+" ";
          //String smstemp="Dear Member,\r Your a/c with us has been debited by "+dmang.roundTwoDecimals(totalamt+totaltax)+" ";
          smstemp1=smsmsg;
          //smstemp2=smsmsg;
          int i=2;
          int j=0;
         // String smstemp3=null;
          Map<String,GeneralSettingInfo> gs=LookupUtilityImpl.getInstance(null).getGeneralSettingsMap();
          GeneralSettingInfo sinfo=gs.get("Datestart");
          GeneralSettingInfo einfo=gs.get("Dateend");
          String prevFySubIncome=gs.get("prevFySubIncome").getValue();
          String nextFySubIncome=gs.get("nextFySubIncome").getValue();
          Date fsdate=(Date)Formats.DATE.parseValue(sinfo.getValue());
          Date fedate=(Date)Formats.DATE.parseValue(einfo.getValue());
          Calendar fscal=Calendar.getInstance();//financial year sdate
          fscal.setTime(fsdate);
          Calendar fecal=Calendar.getInstance();//financial year edate
          fecal.setTime(fedate);
          for(ManualFacilityBillingTableModel.Receiptline line:list){
              FacilityInfo ftmp=LookupUtilityImpl.getInstance(null).getFacilityMap().get(line.getFacilityid());
              String smstemp3=smstemp1;
              String smstemp5=null;
              if(smstemp1!=null && j==0){
                  smstemp1+="("+ftmp.getSMSForm()+" Rs."+dmang.ConvertDoubleToString(line.getAmount())+" due on "+Formats.DATE.formatValue(line.getDuedate());
                  smstemp5=ftmp.getSMSForm()+" Rs."+dmang.ConvertDoubleToString(line.getAmount())+" due on "+Formats.DATE.formatValue(line.getDuedate());
              }else if(smstemp1!=null && j!=0){
                  smstemp1+=","+ftmp.getSMSForm()+" Rs."+dmang.ConvertDoubleToString(line.getAmount())+" due on "+Formats.DATE.formatValue(line.getDuedate());
                  smstemp5=ftmp.getSMSForm()+" Rs."+dmang.ConvertDoubleToString(line.getAmount())+" due on "+Formats.DATE.formatValue(line.getDuedate());
              }else{
                  smstemp1=ftmp.getSMSForm()+" Rs."+dmang.ConvertDoubleToString(line.getAmount())+" due on "+Formats.DATE.formatValue(line.getDuedate());
                  smstemp5=ftmp.getSMSForm()+" Rs."+dmang.ConvertDoubleToString(line.getAmount())+" due on "+Formats.DATE.formatValue(line.getDuedate());
                  if(smstemp1.length()>155){
                    smstemp1=null;
                  }
              }
              j++;
               System.out.println("temp1 length"+smstemp1.length());
              if(smstemp1!=null && smstemp1.length()>160-(9) && i-1==list.size()){
                 if(smstemp2==null)
                     smstemp2=smstemp3;
                 else
                     smstemp2+=smstemp3;
                 System.out.println("temp1 length"+smstemp1.length());
                 System.out.println("temp3 length"+smstemp3.length());
                 smstemp1="#MSG"+i+" "+smstemp5;
                 i++;
              }else if(smstemp1!=null && smstemp1.length()>160-8-" cntd..-".length() && i-1!=list.size()){
                 if(smstemp2==null)
                     smstemp2=smstemp3+" cntd..";
                 else
                     smstemp2+=smstemp3+" cntd..";
                 System.out.println("temp1 length"+smstemp1.length());
                 System.out.println("temp3 length"+smstemp3.length());
                 smstemp1="#MSG"+i+" "+smstemp5;
                 i++;
              }
              // totaltax += line.getTaxamt();
             double amt=line.getAmount();
             double amtwithoutTax=amt-line.getTaxamt();
             
             Calendar rscal=Calendar.getInstance();
             Calendar recal=Calendar.getInstance();
             rscal.setTimeInMillis(line.getSdate().getTime());
             recal.setTimeInMillis(line.getEdate().getTime());
             String fnarrationbegin=null;
             String fnarrationend=null;
             int pLen=0;
             while(rscal.before(recal)){
                     pLen++;
                     rscal.add(Calendar.MONTH, 1);
             }
             double pamt=amtwithoutTax/pLen;// value for 1 month
             if(rscal.before(fscal)){
                 int cnt=0;
                 while(rscal.before(fscal)){
                     cnt++;
                     rscal.add(Calendar.MONTH, 1);
                 }
                 double prevamt=pamt*cnt;
                 amtwithoutTax=amtwithoutTax-prevamt;
                 Calendar caltemp=Calendar.getInstance();
                 caltemp.setTimeInMillis(fscal.getTimeInMillis());
                 caltemp.add(Calendar.DATE, -1);
                 fnarrationbegin="Renewal fees Period: "+Formats.DATE.formatValue(line.getSdate());
                 if(line.getUserId()!=null)
                   narration="Renewal fees Period: "+Formats.DATE.formatValue(line.getSdate())+" to "+Formats.DATE.formatValue(caltemp.getTime())+"- "+line.getUserName();
                 else
                   narration="Renewal fees Period: "+Formats.DATE.formatValue(line.getSdate())+" to "+Formats.DATE.formatValue(caltemp.getTime());
                 Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,new Date(),"C",line.getFacilityid(),billnum,prevamt,date,true,user.getName(),m_App.getProperties().getHost(),narration,prevFySubIncome,0.0,true};
                 dmang.insertintoaccjoutnal(value);
             }if(!recal.before(fecal)){
                 int cnt=0;
                 Calendar caltemp1=Calendar.getInstance();//copy of financial year end
                 caltemp1.setTimeInMillis(fecal.getTimeInMillis());
                 while(caltemp1.before(recal)){
                     cnt++;
                     caltemp1.add(Calendar.MONTH, 1);
                     caltemp1.set(Calendar.DATE, rscal.getActualMaximum(Calendar.DATE));
                 }

                 double advamt=pamt*cnt;
                 amtwithoutTax=amtwithoutTax-advamt;
                 Calendar caltemp=Calendar.getInstance();
                 caltemp.setTimeInMillis(fecal.getTimeInMillis());
                 caltemp.add(Calendar.DATE, 1);
                 if(line.getUserId()!=null){
                    fnarrationend=" to "+Formats.DATE.formatValue(line.getSdate())+"- "+line.getUserName();
                    narration="Renewal fees Period: "+Formats.DATE.formatValue(caltemp.getTime())+" to "+Formats.DATE.formatValue(line.getEdate())+"- "+line.getUserName();
                 }else{
                    fnarrationend=" to "+Formats.DATE.formatValue(line.getSdate());
                    narration="Renewal fees Period: "+Formats.DATE.formatValue(caltemp.getTime())+" to "+Formats.DATE.formatValue(line.getEdate());
                 }
                 Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,new Date(),"C",line.getFacilityid(),billnum,advamt,date,true,user.getName(),m_App.getProperties().getHost(),narration,nextFySubIncome,0.0,true};
                 dmang.insertintoaccjoutnal(value);
             }
             //totalamt += amt;
             if(fnarrationbegin==null)
                 fnarrationbegin="Renewal fees Period: "+Formats.DATE.formatValue(line.getSdate());
             if(line.getUserId()!=null){
                 narration="Renewal fees Period: "+Formats.DATE.formatValue(line.getSdate())+" to "+Formats.DATE.formatValue(line.getEdate())+"- "+line.getUserName();
                 if(fnarrationend==null)
                     fnarrationend=" to "+Formats.DATE.formatValue(line.getEdate())+"- "+line.getUserName();
             }else{
                 narration="Renewal fees Period: "+Formats.DATE.formatValue(line.getSdate())+" to "+Formats.DATE.formatValue(line.getEdate());
                 if(fnarrationend==null)
                     fnarrationend=" to "+Formats.DATE.formatValue(line.getEdate());
             }
             
             Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,new Date(),"C",line.getFacilityid(),billnum,amtwithoutTax,date,true,user.getName(),m_App.getProperties().getHost(),fnarrationbegin+fnarrationend,line.getFacilityAccount(),0.0,true};
             dmang.insertintoaccjoutnal(value);
             //if(amt>0){
             Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,customerInfo.getId(),new Date(),"D",line.getFacilityid(),billnum,amt,line.getDuedate(),false,user.getName(),m_App.getProperties().getHost(),narration,cacc,amt,true};
             dmang.insertintoaccjoutnal(value1);
             //}
             Calendar calp=Calendar.getInstance();
             calp.setTimeInMillis(line.getEdate().getTime());
             Calendar callb=Calendar.getInstance();
             callb.setTimeInMillis(line.getlBilldate().getTime());
             if(calp.after(callb)==true){
                String userid=line.getUserId();

            // if(dependent.getSelectedIndex()!=-1 && dependent.isVisible()){
            //   MemberDependent md=(MemberDependent)dependent.getSelectedItem();
           //    userid=md.getID();
            // }
                if(userid!=null){
                  Object[] value2=new Object[]{line.getEdate(),new Date(),billnum,customerInfo.getId(),line.getFacilityid(),userid};
                  dmang.UpdateMemberFacilityUsage(value2);
                }else{
                  Object[] value2=new Object[]{line.getEdate(),new Date(),billnum,customerInfo.getId(),line.getFacilityid()};
                  dmang.UpdateMemberFacilityUsage1(value2);
                }
            }
             dmang.setmemberDebt(customerInfo.getId(), line.getFacilityid(), amt);
          }
         if(smstemp1!=null){
             if(smstemp2!=null)
             smstemp2+=smstemp1+" bill no "+billnum;
             else
                 smstemp2=smstemp1+" bill no "+billnum;
         }else{
              if(smstemp2!=null)
             smstemp2+=" bill no "+billnum;
         }
           if(totaltax>0){
               getServiceTaxacc();
              if(servicetaxacc!=null){
                Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,new Date(),"C",f.getid(),billnum,totaltax,date,true,user.getName(),m_App.getProperties().getHost(),"Service tax on renewalfee",servicetaxacc,0.0,true};
                dmang.insertintoaccjoutnal(value);
            }
           }
        
            dmang.updateBillNum("Manual Facility Billing Seq");
            if(customerInfo.getMobile()!=null && customerInfo.getMobile().trim().length()==10)
               dmang.updatetosendMsg(smstemp2, customerInfo.getId(), customerInfo.getMobile(),2);
           try{
             printTicket(billnum,totalamt,list);
            }catch(Exception e){
            }
            activate();
           // jLabel11.setVisible(true);
           // prevbillno.setVisible(true);
           // prevbillno.setText(rnum);

        }else{
           //exceeds
           JOptionPane.showMessageDialog(null,"Please Fill the form completely",null , JOptionPane.WARNING_MESSAGE);
        }
         return null;
        }
       };
        t.execute();
        }catch(Exception e){
          e.printStackTrace();
           MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, "Error"  , e);
           msg.show(this);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void nperiodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nperiodKeyReleased
        // TODO add your handling code here:
       try{
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
          jLabel9.setVisible(false);
          int num=Integer.parseInt(nperiod.getText());
          Calendar cal=Calendar.getInstance();
          cal.setTimeInMillis(sdate.getTime());
          int billabledate=cal.get(Calendar.DATE);
          if(num>0){
           setenddate(num,billabledate);
          }
          else{
            nperiod.setText("1");
            setenddate(1,billabledate);
          /*  Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(sdate.getTime());
            int billabledate=cal.get(Calendar.DATE);
            edate.setTime(flogic.calculateEndDate1(sdate, p, billabledate, num).getTime());
            todate.setText(Formats.DATE.formatValue(edate));
            if(p.getdoj()==false && p.getaccurate()==true){
              double amt= calculateaccurateAmt(sdate, edate);
              amount.setText(String.valueOf(dmang.roundTwoDecimals(amt)));
            }else
             amount.setText(f.getramt().toString());*/
          }
        }else{
          jLabel9.setVisible(true);
        }
       }catch(NumberFormatException e){
          JOptionPane.showMessageDialog(this, "Please enter a vaild number", "Invalid Number", JOptionPane.OK_OPTION);
       }catch(Exception e){
         e.printStackTrace();
       }
    }//GEN-LAST:event_nperiodKeyReleased

    private void dependentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dependentItemStateChanged
        // TODO add your handling code here:
        try{
        if(f!=null && customerInfo!=null && dependent.getSelectedIndex()!=-1){
            flogic=new FacilityLogic(dmang);
            MemberDependent md=(MemberDependent)dependent.getSelectedItem();
            Date d=new Date();
            f=(Facility)facilitylist.getSelectedItem();
            p=dmang.getPerioicitybyid(f.getrperiod());
            periodtype.setText(p.getName());
            Object[] obj=(Object[])new StaticSentence(m_App.getSession()
                    , "SELECT LBILLDATE,SDATE,BILLREF,FACMANGREF FROM MEMFACILITYUSAGE WHERE MEMNO=? AND FACILITYTYPE=? AND USERID=? AND ACTIVE=TRUE "
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING})
                    ,new SerializerReadBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING,Datas.STRING})).find(new Object[]{customerInfo.getId(),f.getid(),md.getID()});
            if(obj[3]==null){
            if(obj[0]==null){
               if(obj[1]!=null){
                 d=(Date)(obj[1]);
                 incrementDate(d);
                 fdate.setText(Formats.DATE.formatValue(d));
                 //Formats.TIMESTAMP.parseValue
               }
            }else{
                 d=(Date)(obj[0]);
                 incrementDate(d);
                 fdate.setText(Formats.DATE.formatValue(d));

            }
            
            if(obj[2]!=null)
              prevbillno.setText(obj[2].toString());
            else
                prevbillno.setText(null);
            sdate.setTime(d.getTime());
            lbdate.setTime(d.getTime());
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(lbdate.getTime());
            int billabledate=cal.get(Calendar.DATE);
            if(p.getdoj()==false){
              cal.setTimeInMillis(flogic.getBillableDate(p, billabledate, cal).getTimeInMillis());
            }
            lbdate.setTime(cal.getTimeInMillis());
            billabledate=cal.get(Calendar.DATE);
            setenddate(1,billabledate);
            fdate.setText(Formats.DATE.formatValue(lbdate));
            nperiod.setText("1");
            rate.setText(f.getramt().toString());
           /*  double amt=0.0;
            if(p.getdoj()==false && p.getaccurate()==true){
                fdate.setText(Formats.DATE.formatValue(sdate));
                   amt= calculateaccurateAmt(sdate, edate);
                   amt +=(amt*taxrate);
                   amount.setText(String.valueOf(dmang.roundTwoDecimals(amt)));
                   todate.setText(Formats.DATE.formatValue(edate));
            }else{
                 fdate.setText(Formats.DATE.formatValue(sdate));
                    amt=f.getramt()+(f.getramt()*taxrate);
                    amount.setText(String.valueOf(amt));
                    todate.setText(Formats.DATE.formatValue(edate));
            }*/
         /*  if(p.getdoj()==false && p.getaccurate()==true){
              
              double amt= calculateaccurateAmt(sdate, edate);
              amount.setText(String.valueOf(dmang.roundTwoDecimals(amt)));
              
           }else{
              fdate.setText(Formats.DATE.formatValue(lbdate));
              amount.setText(f.getramt().toString());
              todate.setText(Formats.DATE.formatValue(edate));
           }*/
           jButton1.setEnabled(true);
         }else{
             dependent.setSelectedIndex(-1);
                JOptionPane.showMessageDialog(null, "There is a pending request for this facility", "Cannot Bill this facility for the dependent", JOptionPane.ERROR_MESSAGE);
         }
        }
        }catch(Exception e){
          e.printStackTrace();
        }
    }//GEN-LAST:event_dependentItemStateChanged

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        // TODO add your handling code here:
        try{
            int flag=0;
            if(jRadioButton1.isSelected()==true){
                if(facilitylist.getSelectedIndex()!=-1)
                    flag=1;
            }else if(jRadioButton2.isSelected()==true){
               if(facilitylist.getSelectedIndex()!=-1 && dependent.getSelectedIndex() != -1)
                    flag=2;
            }
          if(flag>0){
           ManualFacilityBillingTableModel.Receiptline line= mfmodel.getReceiptline();
           Date date=getdate();
           Date duedate=new Date();
           int noperiod=Integer.parseInt(nperiod.getText());
           Calendar caltemp=Calendar.getInstance();
           caltemp.setTimeInMillis((new Date()).getTime());
           caltemp.set(Calendar.DATE, 1);
           if(f.getdueperiod()!=null){
            DebtTypeTableModel.DebtTypeline dueperiod=dmang.getDebtTypebyid(f.getdueperiod());
           // duedate.setTime(flogic.getDueDate(dueperiod, flogic.getTemp()).getTime());
            duedate.setTime(flogic.getDueDate(dueperiod, caltemp.getTime()).getTime());
           }
           line.setDuedate(duedate);
           line.setNoOfPeriod(noperiod);
          // line.setBillNum(rnum);
           line.setSdate((Date)Formats.TIMESTAMP.parseValue(fdate.getText()));
           line.setEdate((Date)Formats.TIMESTAMP.parseValue(todate.getText()));
           line.setlBilldate(lbdate);
           line.setFacilityAccount(f.getRenewalacc());
           line.setFacilityId(f.getid());
           line.setFacilityName(f.getName());
           line.setAmount(total+taxtotal);
           line.setTaxamt(taxtotal);
           if(dependent.getSelectedIndex()!=-1){
               MemberDependent md=(MemberDependent)dependent.getSelectedItem();
               line.setUserid(md.getID());
               line.setuserName(md.getName());
             }else{
                line.setUserid(null);
                line.setuserName(customerInfo.getName());
             }
         //  dmang.updatebillno(f.getid());
           mfmodel.addReceiptLine(line);
           loaddata();
           jTable1.setModel(mfmodel.getTableModel());
          }
       /*   //  String tid=UUID.randomUUID().toString();
          //  double rate1=Double.parseDouble(rate.getText());
            //AppUser user=m_App.getAppUserView().getUser();
          //  String narration="Renewal fees Period:"+fdate.getText()+" to "+todate.getText();
           // Double amt=(Double)Formats.DOUBLE.parseValue(amount.getText());
            
           // String rnum=dmang.getnewbillno(f.getid());
           // String cacc=dmang.getCustomerAccountByID(customerInfo.getId());
            //ID,TID,MEMID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,ACTIVE
            double amt=total;
            String tid=UUID.randomUUID().toString();
            if(servicetaxacc!=null){
                Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,date,"C",f.getid(),rnum,taxtotal,date,true,user.getName(),m_App.getProperties().getHost(),"Service tax on renewalfee for "+f.getName(),servicetaxacc,0.0,true};
                dmang.insertintoaccjoutnal(value);
                amt=dmang.roundTwoDecimals(total+taxtotal);
            }
            Object[] value=new Object[]{UUID.randomUUID().toString(),tid,null,date,"C",f.getid(),rnum,total,date,true,user.getName(),m_App.getProperties().getHost(),narration,f.getRenewalacc(),0.0,true};
            dmang.insertintoaccjoutnal(value);
            Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,customerInfo.getId(),date,"D",f.getid(),rnum,amt,duedate,false,user.getName(),m_App.getProperties().getHost(),narration,cacc,amt,true};
            dmang.insertintoaccjoutnal(value1);
            Calendar calp=Calendar.getInstance();
            calp.setTimeInMillis(edate.getTime());
            Calendar callb=Calendar.getInstance();
            callb.setTimeInMillis(lbdate.getTime());
            if(calp.after(callb)==true){
                String userid=null;
             if(dependent.getSelectedIndex()!=-1){
               MemberDependent md=(MemberDependent)dependent.getSelectedItem();
               userid=md.getID();
             }
                if(userid!=null){
                  Object[] value2=new Object[]{edate,new Date(),rnum,customerInfo.getId(),f.getid(),userid};
                  dmang.UpdateMemberFacilityUsage(value2);
                }else{
                  Object[] value2=new Object[]{edate,new Date(),rnum,customerInfo.getId(),f.getid()};
                  dmang.UpdateMemberFacilityUsage1(value2);
                }
            }
            dmang.updatebillno(f.getid());
            dmang.setmemberDebt(customerInfo.getId(), f.getid(), total);
            try{
            printTicket(rnum,narration,amt,noperiod,rate1);
            }catch(Exception e){
            }
            activate();
            jLabel11.setVisible(true);
            prevbillno.setVisible(true);
            prevbillno.setText(rnum);*/


        }catch(Exception e){
          e.printStackTrace();
        }
    }//GEN-LAST:event_addActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        int row=jTable1.getSelectedRow();
        if(row>=0){
          mfmodel.RemoveReceiptLine(row);
          jTable1.setModel(mfmodel.getTableModel());
        }
}//GEN-LAST:event_removeActionPerformed

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged
        try{
        if(jRadioButton1.isSelected()){
           jLabel12.setVisible(false);
           dependent.setVisible(false);
           dmodel=new ComboBoxValModel();
           dependent.setModel(dmodel);
           List<Facility> flist=null;
           if(customerInfo!=null)
           flist=dmang.getmemberscurrentfacilities(customerInfo.getId());
           dmodel=new ComboBoxValModel();
           dependent.setModel(dmodel);
           if(flist!=null)
               fmodel=new ComboBoxValModel(flist);
           else
               fmodel=new ComboBoxValModel();
           facilitylist.setModel(fmodel);

        }
        }catch(Exception e){
          e.printStackTrace();
        }
    }//GEN-LAST:event_jRadioButton1ItemStateChanged

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged
      try{
        if(jRadioButton2.isSelected()){
           jLabel12.setVisible(true);
           dependent.setVisible(true);
           List<Facility> flist=dmang.getmembersDependentscurrentfacilities(customerInfo.getId());
           if(flist!=null)
               fmodel=new ComboBoxValModel(flist);
           else
               fmodel=new ComboBoxValModel();
           facilitylist.setModel(fmodel);
        }
        }catch(Exception e){
          e.printStackTrace();
        }
    }//GEN-LAST:event_jRadioButton2ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JTextField amount;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox dependent;
    private javax.swing.JComboBox facilitylist;
    private javax.swing.JTextField fdate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField memno;
    private javax.swing.JTextField mname;
    private javax.swing.JTextField nperiod;
    private javax.swing.JTextField periodtype;
    private javax.swing.JTextField prevbillno;
    private javax.swing.JTextField rate;
    private javax.swing.JButton remove;
    private javax.swing.JTextField todate;
    // End of variables declaration//GEN-END:variables



}
