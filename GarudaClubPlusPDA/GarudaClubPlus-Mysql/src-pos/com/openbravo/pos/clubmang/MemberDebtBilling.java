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

import RMI.Compute1;
import RMI.ComputePi;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
//import com.openbravo.data.loader.Datas;
//import com.openbravo.data.loader.SerializerWriteBasic;
//import com.openbravo.data.loader.SerializerWriteString;
//import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.clubmang.MemDebtBillingTableModel.MyAbstractTableModel;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.customers.CustomerInfoExt;
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
import com.openbravo.pos.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
//import javax.swing.table.AbstractTableModel;
import javax.swing.ToolTipManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
//import javax.swing.table.TableColumn;
//import javax.swing.table.TableColumnModel;

/**
 *
 * @author swathi
 */
public class MemberDebtBilling extends javax.swing.JPanel implements JPanelView,BeanFactoryApp {

    /** Creates new form MemberDebtBilling */
    private MemDebtBillingTableModel dbmodel;
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
    private int status;
    private int operator;
    public MemberDebtBilling() {
        initComponents();
    }

     public void init(AppView app) throws BeanFactoryException {
        m_App=app;
        dmang=(DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dlsystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        ttp = new TicketParser(app.getDeviceTicket(), dlsystem);
        ctotal.setEditable(false);
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
       loadData();
    }
    private void loadData(){
        caltotal=0;
        status=0;
        operator=0;
        customerInfo=null;
        //jButton5.setEnabled(false);
        jTable1.setVisible(false);
        dbmodel=MemDebtBillingTableModel.emptyinstance();
        
        total.setText("0.0");
        debttotal.setText("0.0");
        ctotal.setText("0.0");
        creditamt.setText("0.0");
        memno.setText(null);
        mname.setText(null);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                memno.requestFocus();
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
            tablemodel=dbmodel.getTableModel();
            tablemodel.settext(total);
            jTable1.setModel(tablemodel);
            ctablemodel=dbmodel.getCreditTableModel();
            ctablemodel.settext(ctotal);
            jTable2.setModel(ctablemodel);
      //  }
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
   private void equals(){
      if(operator==1 && status==0)
             caltotal +=Double.parseDouble(amtvalue.getText());
      else if(operator==2 && status==0)
             caltotal -=Double.parseDouble(amtvalue.getText());
      else if(operator==3 && status==0)
             caltotal *=Double.parseDouble(amtvalue.getText());
      else if(operator==0 )
          caltotal =Double.parseDouble(amtvalue.getText());
       amtvalue.setText(String.valueOf(dmang.roundTwoDecimals(caltotal)));

   }
   private void stateTransition(char cTrans) {
        if (cTrans == '\u007f') {
           amtvalue.setText(null);
           caltotal=0;
           operator=0;
           opr.setText(" ");
           status= 1;
        } else if (cTrans == '+' ) {
           equals();
           opr.setText("+");
           amtvalue.setText(String.valueOf(caltotal));
           operator =1;
           status=1;
        } else if (cTrans == '-'){
             equals();
             opr.setText("-");
             amtvalue.setText(null);
             amtvalue.setText(String.valueOf(caltotal));
             operator= 2;
             status=1;
        } else if (cTrans == '*'){
             equals();
             opr.setText("*");
             amtvalue.setText(null);
             amtvalue.setText(String.valueOf(caltotal));
             operator= 3;
             status=1;
        }
        else if ( cTrans == '=') {
            equals();
            operator=0;
            caltotal=0;
            opr.setText(" ");
            status=1;
        } else {
             if(status==1)
                 amtvalue.setText( String.valueOf(cTrans));
            else
                 amtvalue.setText( amtvalue.getText() + cTrans);
             status=0;
        }
    }
   private Date getdate(){
          Date dnow=new Date();
          Calendar cal=GregorianCalendar.getInstance();
          cal.setTimeInMillis(dnow.getTime());
          cal.set(Calendar.HOUR_OF_DAY, 00);
          cal.set(Calendar.SECOND, 00);
          cal.set(Calendar.MINUTE, 00);
          cal.set(Calendar.MILLISECOND, 00);
          dnow.setTime(cal.getTimeInMillis());
          return dnow;
   }

    private void printTicket(List<MemDebtBillingTableModel.Facilityline> list,String receiptno,String cname,List<PaymentInfo> pinfo,Double amount,List<MemDebtBillingTableModel.Creditline> clist,String memno) {

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
                script.put("eoe", StringUtils.encodeXML("E&OE"));
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
    private void load(){
         try{
            total.setText("0.0");
            ctotal.setText("0.0");
            jTable1.setVisible(true);
            String accid=dmang.getCustomerAccountByID(customerInfo.getId());
            dbmodel=MemDebtBillingTableModel.loadInstance(m_App,customerInfo.getId(),accid,dmang);
            tablemodel=dbmodel.getTableModel();
            tablemodel.settext(total);
            jTable1.setModel(tablemodel);
            ctablemodel=dbmodel.getCreditTableModel();
            ctablemodel.settext(ctotal);
            jTable2.setModel(ctablemodel);
            creditamt.setText(dbmodel.getcreditAmount().toString());
            debttotal.setText(dbmodel.getdebtAmount().toString());
        }

        catch(Exception e){
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
        jPanel1 = new javax.swing.JPanel();
        jNumberKeys1 = new com.openbravo.beans.JNumberKeys();
        amtvalue = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        opr = new javax.swing.JLabel();
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
        jPanel3 = new javax.swing.JPanel();
        memno = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        mname = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
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
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setAutoscrolls(true);

        jNumberKeys1.addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {
            public void keyPerformed(com.openbravo.beans.JNumberEvent evt) {
                jNumberKeys1KeyPerformed(evt);
            }
        });

        amtvalue.setFont(new java.awt.Font("Tahoma", 0, 12));

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButton4.setText("Enter");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        opr.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(amtvalue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(opr, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jNumberKeys1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jNumberKeys1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(amtvalue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(opr, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                .addContainerGap())
        );

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
        jTable1.setRowHeight(18);
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setText("Debit List");

        jLabel3.setText("Debt Total");

        debttotal.setEditable(false);

        jLabel4.setText("Total");

        total.setEditable(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(7, 7, 7)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        memno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                memnoKeyPressed(evt);
            }
        });

        jLabel1.setText("Mem No");

        jLabel2.setText("Member Name");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(memno, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mname, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(65, 65, 65))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(memno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(mname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(23, Short.MAX_VALUE))
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
        jTable2.setRowHeight(18);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);

        jLabel5.setText("Available Credit List");

        creditlabel.setText("Total Credit Available");

        creditamt.setEditable(false);

        jLabel7.setText("Total");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(creditlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(creditamt, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(ctotal, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
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
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(432, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jButton5))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void memnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_memnoKeyPressed
        // TODO add your handling code here:
          if(evt.getKeyText(evt.getKeyCode()).equals("Enter")){

            try{
               Object[] obj= dmang.getMamberbySkey(memno.getText().toUpperCase());

            if(obj==null)
            {
                JOptionPane.showMessageDialog(this,AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
            }
            else
            {
               customerInfo=new CustomerInfo(obj[0].toString());
               customerInfo.setName(obj[1].toString());
               customerInfo.setSearchkey(memno.getText().toUpperCase());
               customerInfo.setMobile(String.valueOf(obj[3]));
               mname.setText(obj[1].toString());
               load();
               //customerInfo.setMemType(obj[2]);
            }
            }
          catch(Exception e)
          {
          }
        }else{
            mname.setText(null);
            customerInfo=null;
            if(jTable1.getRowCount()>0){
                dbmodel=MemDebtBillingTableModel.emptyinstance();
                jTable1.setModel(dbmodel.getTableModel());
                jTable2.setModel(dbmodel.getCreditTableModel());
                debttotal.setText("0.00");
                creditamt.setText("0.00");
                ctotal.setText("0.00");
                total.setText("0.00");
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
        try{
          Transaction t=new Transaction(m_App.getSession()){
          public Object transact() throws BasicException {
         if(m_App.getAppUserView().getUser().getcashaccount()!=null && m_App.getAppUserView().getUser().getchequeaccount()!=null ){
         int rownos= jTable1.getRowCount();
                        
         Double amt=Double.parseDouble(total.getText());
         CustomerInfoExt cinfo=new CustomerInfoExt(customerInfo.getId());
         cinfo.setSearchkey(customerInfo.getSearchkey());
         cinfo.setName(customerInfo.getName());
         String caccount="";
         List<PaymentInfo> pinfo=new ArrayList<PaymentInfo>();
         String accid;
         Double payableamt;
         Double paidamt;
          JPaymentSelect paymentdialog =JPaymentSelectReceipt.getDialog(JOptionPane.getRootFrame());
          paymentdialog.init(m_App);
          Date dnow= getdate();
          String pref=null;
          Double bamt1=0.0;
          Double pamt=Double.parseDouble(debttotal.getText());
          Boolean flag=true;
          boolean bill=true;
          if(Double.parseDouble(ctotal.getText())>0)
              alowbilling=false;
          else
              alowbilling=true;
          if(alowbilling==true){
              if(amt>0)
                   bill= paymentdialog.showDialog(amt, cinfo, m_App.getAppUserView().getUser().getName(),true);
              else
                   bill=false;
          }else{
                 bill=true;
                 Double camt= 0.0;//Double.parseDouble(creditamt.getText())-Double.parseDouble(total.getText()) ;
                 Double totalamt=Double.parseDouble(total.getText());
                 Double bal=Double.parseDouble(ctotal.getText())-Double.parseDouble(total.getText());
                 if(bal>=0){
                 List<MemDebtBillingTableModel.Facilityline> listold=dbmodel.getfacilityline();
                 String temp=null;
                 for(MemDebtBillingTableModel.Facilityline line:listold){
                     if(line.getamountb()>0){
                       if(temp==null)
                        temp =line.gettransno()+" # "+line.getamountb();
                       else
                        temp+=" : "+line.gettransno()+" # "+line.getamountb();
                       double amt1=dmang.roundTwoDecimals(line.getamount()-line.getamountb());
                       if(amt1<=0)
                           dmang.updateDebit1(dbmodel.getCreditTransno(),0.0,line.getaccid());
                       else
                           dmang.updateDebit(dbmodel.getCreditTransno(),amt1,line.getaccid());
                       int fflag=0;
                   try{
                       Facility factenp=dmang.getFacilitybyID(line.getFacilityId());
                       if(factenp==null)
                          fflag=1;
                    }catch(Exception e){
                         fflag=1;
                    }
                    if(fflag==0)
                        dmang.setmemberDebt(customerInfo.getId(),line.getFacilityId() , line.getamountb()*-1);
                     }
                 }
                 String temoid=dbmodel.getCreditEntry();
                 if(bal>0)
                 dmang.updatecredit(bal,false,temoid,temp);
                 else
                  dmang.updatecredit(0.0,true,temoid,temp);
                 }else{
                     JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Amount Exceeds the selected credit", null, JOptionPane.WARNING_MESSAGE);
                     bill=false;
                 }
                load();
          }
          String rnum="", accjournalid="";
          boolean f=true;
          if(bill){
              List<MemDebtBillingTableModel.Facilityline> list=new ArrayList<MemDebtBillingTableModel.Facilityline>();
              List<MemDebtBillingTableModel.Facilityline> listold=dbmodel.getfacilityline();
             
             String tid=UUID.randomUUID().toString();
             Double cashieramt=amt;
             if(alowbilling==true){
             
              pinfo=paymentdialog.getSelectedPayments();
             BillInfo ticket=new BillInfo();
             ticket.setID(UUID.randomUUID().toString());
             ticket.setPayments(pinfo);
             ticket.setCustomer(cinfo);
             ticket.setCreatedBy(m_App.getAppUserView().getUser().getName());
             ticket.setCreatedDate(dnow);
             ticket.setActiveCash(m_App.getActiveCashIndex());
             rnum=m_dlSales.payaccount(ticket, m_App.getInventoryLocation(),false);
             if(!(rnum==null || rnum.equals("false"))){
                 for(int i=0;i<rownos;i++){
               Double bamt=0.0;
               accid=dbmodel.getTableModel().getValueAt(i, 8).toString();//account journal id//
               payableamt=Double.parseDouble(dbmodel.getTableModel().getValueAt(i, 3).toString());
               String fid=null;
               try{
                  fid=dbmodel.getTableModel().getValueAt(i, 11).toString();//
               }catch(Exception e){
                  e.printStackTrace();
               }
               caccount=dmang.getCustomerAccountByID(customerInfo.getId());
             //  accjournalid=dbmodel.getTableModel().getValueAt(0, 8).toString();
             //  pamt += payableamt;
               paidamt=Double.parseDouble(dbmodel.getTableModel().getValueAt(i, 5).toString());
               if(accid.equals("Account Pay")){
                   list.add(listold.get(i));
                   bamt1+=paidamt;
                if(bamt1!=0.0)
                 flag=false;
               }
               else{
                bamt=payableamt-paidamt;
                if(paidamt>0){
                   list.add(listold.get(i));
                if(payableamt.toString().equals(paidamt.toString())){//clear
                    f=dmang.updateaccountjournal(accid, dnow, rnum+" # "+paidamt,0.0,true);
                   //if(f=false)
                    if(f==false){
                        load();
                        throw new BasicException("Error Occured..Please Try again");
                    }
                if(pref==null)
                   pref=dbmodel.getTableModel().getValueAt(i, 9).toString();//
                else
                   pref=pref+" : "+dbmodel.getTableModel().getValueAt(i, 9).toString();// //group all trans no
               }else
               if(payableamt > paidamt){
                   bamt=dmang.roundTwoDecimals(payableamt-paidamt);
                   //ID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE
                   dmang.updateaccountjournal1(accid, bamt,rnum+" # "+paidamt,payableamt);
                   if(f==false){
                        load();
                        throw new BasicException("Error Occured..Please Try again");
                   }
                 if(pref==null)
                   pref=dbmodel.getTableModel().getValueAt(i, 9).toString();//
                 else
                   pref=pref+" : "+dbmodel.getTableModel().getValueAt(i, 9).toString();// //group all trans no
               }
               }
               }
               int fflag=0;
               try{
                 Facility factenp=dmang.getFacilitybyID(fid);
                 if(factenp==null)
                     fflag=1;
               }catch(Exception e){
                 fflag=1;
               }
               if(fflag==0)
               dmang.setmemberDebt(customerInfo.getId(),fid , paidamt*-1);
             }
             Double chequeamt=0.0;
             for(PaymentInfo p :pinfo){
              if(p.getName().equals("cheque")){
                chequeamt +=p.getTotal();
              }
             }
             cashieramt=amt-chequeamt;
             if(chequeamt>0){
                 Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,dnow,"D",customerInfo.getSearchkey(),rnum,chequeamt,dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Payment received for bill nos" + pref,m_App.getAppUserView().getUser().getchequeaccount(),0.0,dnow,true};
                 dmang.insertintoaccjoutnal1(value1);
              }
                 //credit
                 Object value[]=new Object[]{UUID.randomUUID().toString(),tid,customerInfo.getId(),dnow,"C","C Receipt",rnum,amt,dnow,flag,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Payment for bill nos " + pref,caccount,bamt1,dnow,pref,true};
                 dmang.insertintoaccjoutnal2(value);
                 //debit
                 if(cashieramt>0){
                  Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,dnow,"D",customerInfo.getSearchkey(),rnum,cashieramt,dnow,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Payment received for bill nos " + pref,m_App.getAppUserView().getUser().getcashaccount(),0.0,dnow,true};
                  dmang.insertintoaccjoutnal1(value1);
                 }
                 if(customerInfo.getMobile()!=null){
                 String smsmsg="Dear Member,\rRecvd pymt on ur a/c of Rs."+dmang.roundTwoDecimals(amt)+" vide recp no. "+rnum+".";
                 
                 for(PaymentInfo p1:pinfo){
                   if(p1.getName().equals("cheque")){
                       smsmsg+="Cheque subjected to realisation";
                       break;
                   }
                 }
                 //smsmsg+="-BUSC";
                 if(customerInfo.getMobile()!=null && customerInfo.getMobile().trim().length()==10)
                 dmang.updatetosendMsg(smsmsg, cinfo.getId(), customerInfo.getMobile(),2);
                 }
                 printTicket(list,rnum,cinfo.getName(),pinfo,amt,dbmodel.getCreditList(),cinfo.getSearchkey());
                 String acc=dmang.getCustomerAccountByID(customerInfo.getId());
                 dbmodel=MemDebtBillingTableModel.loadInstance(m_App, cinfo.getId(),acc,dmang);
                 total.setText("0.0");
                 tablemodel=dbmodel.getTableModel();
                 tablemodel.settext(total);
                 jTable1.setModel(tablemodel);
                 ctablemodel=dbmodel.getCreditTableModel();
                 ctablemodel.settext(ctotal);
                 jTable2.setModel(ctablemodel);
                 creditamt.setText(dbmodel.getcreditAmount().toString());
                 debttotal.setText(dbmodel.getdebtAmount().toString());
                  load();
             }else{
             if(rnum.equals("false")){
                  JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please reset the system time or consult your system admin", "Sorry Cannot Create Receipt", JOptionPane.OK_OPTION);
             }else
                 JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Error", "Error", JOptionPane.OK_OPTION);
             }
            }
          }
         }else{
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Cannot create Receipt", "Error", JOptionPane.WARNING_MESSAGE);
         }
         return null;
        }
    };
    t.execute();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int selectedrowno=jTable1.getSelectedRow();
        try{
          if(selectedrowno>=0){
            Double payableamt=Double.parseDouble(dbmodel.getTableModel().getValueAt(selectedrowno, 3).toString());
            Double paidamt=Double.parseDouble(dbmodel.getTableModel().getValueAt(selectedrowno, 5).toString());
            Double amt=Double.parseDouble(amtvalue.getText());
            String fname=dbmodel.getTableModel().getValueAt(selectedrowno, 0).toString();
            boolean flag=true;
             Double damt=Double.parseDouble(total.getText())+amt;
              Double camt=dmang.roundTwoDecimals(Double.parseDouble(ctotal.getText()));
              if(camt>0 && damt>camt){
                  if(fname.equals("Account Pay")){
                     JOptionPane.showMessageDialog(this, "Cannot Process The Request", null, JOptionPane.WARNING_MESSAGE);
                  }else
                  JOptionPane.showMessageDialog(this, "Amount Exceeds the selected credit", null, JOptionPane.WARNING_MESSAGE);
                  flag=false;
              }
            if(flag==true){
            if(amt<=payableamt || fname.equals("Account Pay")){
                dbmodel.getTableModel().setValueAt(false, selectedrowno, 7);
                dbmodel.getTableModel().setValueAt(amt, selectedrowno, 5);
                tablemodel=dbmodel.getTableModel();
                 tablemodel.settext(total);
                 jTable1.setModel(tablemodel);
                Double totalamt=Double.parseDouble(total.getText())+amt-paidamt;
                total.setText(String.valueOf(dmang.roundTwoDecimals(totalamt)));
               
            }
            else
                JOptionPane.showMessageDialog(this,"The Entered Amount Exceeds the required value", "Sorry", JOptionPane.OK_OPTION);
            }
            amtvalue.setText(null);

          }
        }
        catch(Exception e){
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
        MemDebtBillingTableModel.Facilityline f=new MemDebtBillingTableModel.Facilityline();
        Timestamp tm=new Timestamp(getdate().getTime());
        f.setaccid("Account Pay");
        f.setduedate(tm);
        f.setfname("Account Pay");
        f.setamount(0.00);
        f.setAmountb(0.00);
        f.setNarration("Account Pay on "+tm);

        dbmodel.addfacility(f);
        jTable1.setModel(dbmodel.getTableModel());
        int row=jTable1.getRowCount();
        jTable1.setRowSelectionInterval(row-1, row-1);
    }//GEN-LAST:event_jButton5ActionPerformed




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amtvalue;
    private javax.swing.JTextField creditamt;
    private javax.swing.JLabel creditlabel;
    private javax.swing.JTextField ctotal;
    private javax.swing.JTextField debttotal;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JTextField memno;
    private javax.swing.JTextField mname;
    private javax.swing.JLabel opr;
    private javax.swing.JTextField total;
    // End of variables declaration//GEN-END:variables

}
