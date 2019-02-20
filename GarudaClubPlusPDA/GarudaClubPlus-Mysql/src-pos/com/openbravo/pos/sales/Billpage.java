/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Billpage.java
 *
 * Created on Dec 11, 2008, 3:41:18 PM
 */

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
//import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.mant.PlacesInfo;
import com.openbravo.pos.mant.WaiterInfo;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.restaurant.JIntroPageRest;
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurant;
import com.openbravo.pos.sales.restaurant.discountPage;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.util.StringUtils;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author swathi
 */
public class Billpage extends javax.swing.JDialog {

    private static final String PERMISSION_PAYMENT = "payment";

    private BillItemTableModel billtablemodel;
    private List<BillLineInfo> blist;
    private BillLogic blogic;
    private BillLogicApply bla;
    private DataLogicSales dlSales;
    private BillInfo binfo;
    private boolean resultok = false;
    private BillTaxesLogic taxeslogic;
    private ListKeyed taxcollection;
    private CustomerInfo customertemp;
    private JPaymentSelect paymentdialogreceipt;
    private JPaymentSelect paymentdialogrefund;
     private DataLogicSystem dlSystem;
     private String cname;
    // private Frame frame;

   // private JIntroPageRest m_restaurant;
    private TicketParser m_TTP;

    /** Creates new form Billpage */

    public Billpage(Frame parent, DataLogicSales dlSales, BillLogicApply bla, CustomerInfo customer) {

        super(parent, true);

        this.bla = bla;
        this.blogic = LookupUtilityImpl.getInstance(null).getDataLogicBill();
        this.customertemp=customer;
        this.dlSales = dlSales;
        resultok = bla == null;
    }

    public Billpage(Dialog parent, DataLogicSales dlSales, BillLogicApply bla, CustomerInfo customer) {
        super(parent, true);
        // frame=parent;
        this.bla = bla;
        this.blogic = LookupUtilityImpl.getInstance(null).getDataLogicBill();
        this.dlSales = dlSales;
         this.customertemp=customer;
        resultok = bla == null;
    }

    protected static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

    public static Billpage getDialog(Component parent, DataLogicSales dlSales, BillLogicApply bla, CustomerInfo customer) {

        Window window = getWindow(parent);

        Billpage mybilllogic;

        if (window instanceof Frame) {
            mybilllogic = new Billpage((Frame) window, dlSales, bla, customer);
        } else {
            mybilllogic = new Billpage((Dialog) window, dlSales, bla, customer );
        }

        return mybilllogic;
    }


    public void init(BillInfo binfo) throws BasicException {
        //binfo.get
        this.binfo = binfo;
        blist = binfo.getLines();
        if (blist == null) {
            blist = blogic.getBillLineList(binfo.getID());
        }

        List<TaxInfo> taxlist = dlSales.getTaxList().list();
        taxeslogic = new BillTaxesLogic(taxlist);
        taxcollection = new ListKeyed<TaxInfo>(taxlist);

        AppView app = LookupUtilityImpl.getInstance(null).getAppView();
         dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");

        m_TTP = new TicketParser(app.getDeviceTicket(), LookupUtilityImpl.getInstance(null).getDataLogicSystem());

        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(app);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this);
        paymentdialogrefund.init(app);


        initComponents();
         jBtnPay.setEnabled(LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission(PERMISSION_PAYMENT));
        billtablemodel = new BillItemTableModel(blist);
        jTable1.setModel(billtablemodel);

        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(40);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(2).setMaxWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(3).setMaxWidth(80);
        


//        jTextField1.setText(binfo.getID());
        jTextField2.setText(binfo.getCustomer().getSearchkey());
        cname=binfo.getCustomerId();
        Date date = new Date();
        jTextField3.setText(""+date);
       // jButton4.setVisible(false);
        boolean perm=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission(PERMISSION_PAYMENT);
        jButton4.setEnabled(JIntroPageRest.dflag);
        jButton1.setEnabled(!JIntroPageRest.dflag && !perm);
        jButton2.setEnabled(JIntroPageRest.dflag);
       // boolean f=;
        if(LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission("bar counter"))
        {
        jButton6.setEnabled(JIntroPageRest.dflag &&  LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission("bar counter"));
        }
        else
        {
        jButton6.setEnabled(JIntroPageRest.dflag &&  LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission("res counter"));
        }
        jBtnPay.setEnabled(perm);
        jButton5.setEnabled(!JIntroPageRest.dflag && !perm);
        //Remove the below comment if you want to give debit permission only to bar counter
        //jButton5.setEnabled(!JIntroPageRest.dflag && LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission("bar counter"));
        String[] arr =binfo.getCustomerId().split("#");
        if(arr.length>1){
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        }
        //jButton1.setVisible(!perm);
        try {
            jButton1.setIcon(new ImageIcon(ImageIO.read(Billpage.class.getResourceAsStream("/com/openbravo/images/cash.png"))));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //if(perm==true){
        
        //}
        if(jButton6.isEnabled()){
           try{
         Object[] obj=(Object[])   new StaticSentence(app.getSession()
                        , "SELECT ID,NAME,APPPASSWORD,CARD,ROLE,IMAGE,LOGINTIME,CLOSECASHTIME,OPENCASHTIME,CLOSESALE,OPENSALE FROM PEOPLE WHERE NAME=?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] {Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.IMAGE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP})).find(binfo.getCreatedBy());

         AppUser appuser=new AppUser(obj[0].toString(), obj[1].toString(),obj[4].toString());
         appuser.fillPermissions(dlSystem);
         if(LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission("bar counter"))
        {
        jButton6.setEnabled(appuser.hasPermission("bar counter"));
         }
         else
         {
        jButton6.setEnabled(appuser.hasPermission("res counter"));
        }
        // jButton6.setEnabled(appuser.hasPermission("bar counter"));
        // jButton6.setEnabled(appuser.hasPermission("res counter"));


      }
      catch(Exception e){
          e.printStackTrace();
         //  MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosave"), e);
        //    msg.show(this);
      }
        }
        try {
            loadBill();
        } catch (BasicException e) {
            new MessageInf(e);
        }

    }

    public boolean showDialog() throws BasicException {
       // init(binfo);
        setVisible(true);
        return resultok;
    }
    private void loadBill() throws BasicException {
        if (binfo.getLines() == null) {
            List<BillLineInfo> billList = blogic.getBillLineList(binfo.getID());
            blist = new ArrayList<BillLineInfo>(billList.size());
            for (Iterator<BillLineInfo> it = billList.iterator(); it.hasNext();) {
                BillLineInfo billLineInfo = it.next();
                if (billLineInfo != null)
                blist.add(billLineInfo);
            }
        }
    }

    private boolean closeTicket(BillInfo ticket, Object ticketext) throws BasicException {

        jBtnPay.setEnabled(false);
       if(!ticket.isPaid()) {
        boolean resultok = false;
        //jBtnPay.setEnabled(false);
        AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();



        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {

            // reset the payment info
            taxeslogic.calculateTaxes(ticket);
            ticket.resetPayments();

            // Muestro el total
            printTicket("Printer.TicketTotal", ticket, ticketext);


            // Select the Payments information
            JPaymentSelect paymentdialog = ticket.getTotal() >= 0.0
                    ? paymentdialogreceipt
                    : paymentdialogrefund;
            paymentdialog.setPrintSelected(true); //Print always
              boolean flag=true;
            if (paymentdialog.showDialog(ticket.getTotal(), ticket.getCustomer(),ticket.getCreatedBy(),true)) {

                // assign the payments selected and calculate taxes.
                ticket.setPayments(paymentdialog.getSelectedPayments());

                // Asigno los valores definitivos del ticket...
                ticket.setActiveCash(m_App.getActiveCashIndex());
               
                // Save the receipt and assign a receipt number
                try {
                    flag = dlSales.payBill(ticket, m_App.getInventoryLocation());
                } catch (Exception eData) {
                    MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
                    msg.show(this);
                }

                // Print receipt.
                if(flag==true){
                 // String smsmsg="Dear Member,\rYour a/c with us has been debited by Rs."+Formats.ConvertDoubleToString(ticket.getTotal())+" bill no "+ticket.printId()+" for bar usage."+"Thank u for using our facility";
                  printTicket(paymentdialog.isPrintSelected()
                        ? "Printer.Ticket_1"
                        : "Printer.Ticket2", ticket, ticketext);
                resultok = true;
                }else{
                    resultok=false;
                }
            }

            // reset the payment info
            ticket.resetTaxes();
            ticket.resetPayments();
        }

        // cancelled the ticket.total script
        // or canceled the payment dialog
        // or canceled the ticket.close script
        
       }
       return resultok;
    }

    private void printTicket(String sresourcename, BillInfo ticket, Object ticketext) {

        String sresource = LookupUtilityImpl.getInstance(null).getDataLogicSystem().getResourceAsXML(sresourcename);
        String receiptdate;

        String waitername;
        String table;
        int debt1=0;
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
           
                Date date=new Date();
                receiptdate=date.toString();
               
                 String sign="";
                 String temp="";
                 try{
               List<PaymentInfo> pi=ticket.getPayments();
               for(int i=0;i<pi.size();i++)
               {
                   temp=pi.get(i).getName();
                   if(pi.get(i).getName().equals("debt"))
                   {
                       debt1=1;
                       sign="sign";
                   }
               }
                 }
                 catch(Exception e)
                 {
                 }
                  AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();
           /*  String bank=   pi.get.get(0).getChequeDetails().getBank();
              for(PaymentInfo p:pi)
               {
                 String bank=  p.getChequeDetails().getChequeno().getBank();
               }*/
                //ticket.resetPayments();
                  try {
               
                 WaiterInfo w=LookupUtilityImpl.getInstance(null).getWaiterMap().get(ticket.getWaiter());
                    waitername=w.getName();
                     Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT NAME FROM PLACES WHERE ID=?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(ticket.getPlace());

                    if(obj1== null)
                       table="";
                        else{
                          table=obj1[0].toString();
                          }
                 // PlacesInfo p= LookupUtilityImpl.getInstance(null).getPlacesMap().get(ticket.getPlace());

                //  table=p.getName();
       //  try{
                 

                    Object[] obj2=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT ID,NAME,ROLE FROM PEOPLE WHERE NAME=?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] {Datas.STRING,Datas.STRING,Datas.STRING})).find(ticket.getCreatedBy());
       /*if(obj[5]==null)
           obj[5]=new Icon();*/
                 AppUser appuser=new AppUser(obj2[0].toString(), obj2[1].toString(),obj2[2].toString());
                appuser.fillPermissions(dlSystem);
                boolean flag1= appuser.hasPermission("bar counter");

               //  ticket.getWaiter();
                  boolean flag= m_App.getAppUserView().getUser().hasPermission("bar counter");
                  boolean crconf=false;
                  if(ticketext.equals("cerditconf"))
                      crconf=true;
                  taxeslogic.calculateTaxes(ticket);
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
                script.put("ticket", ticket);
                script.put("place", table);
                  script.put("flag", flag);
                   script.put("flag1", flag1);
                 script.put("waiter", waitername);
                script.put("date", receiptdate);
                script.put("sign", sign);
                script.put("createdby", m_App.getAppUserView().getUser().getName());
                script.put("eoe", StringUtils.encodeXML("E&OE"));
                script.put("crconf", crconf);
              //  script.put("bank", bank);
              /*  if(debt1==1)
                {
                m_TTP.printTicket(script.eval(sresource).toString());
                }*/
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
            catch (BasicException e) {
                new MessageInf(e).show(this);
            }
        }
    }
    private int checkForBillDiscount(String billid){
         int flag1=0,tempint=0;
        String temp;
        try{

            String bid= billid;
            AppView app = LookupUtilityImpl.getInstance(null).getAppView();
            Object obj[]=(Object[]) new StaticSentence(app.getSession()
                    ,"SELECT COUNT(BILLID) FROM REVERSEDBILL WHERE BILLID = ? AND AUTHORISED IS NULL GROUP BY BILLID "
                    ,SerializerWriteString.INSTANCE
                    ,new SerializerReadBasic(new Datas[]{Datas.INT})).find(bid);
            //temp
             if(obj!=null )
              {
                temp=obj[0].toString();
                tempint=Integer.parseInt(temp);
                if(tempint > 0)
                {
                     JOptionPane.showMessageDialog(this,AppLocal.getIntString("Discount Requst under processing"), AppLocal.getIntString("Cannot Process"), JOptionPane.WARNING_MESSAGE);
                    flag1=1;
                    // break;
                }
              }

           }
           catch(BasicException e)
           {
               flag1=1;
               e.printStackTrace();

           }
        return flag1;
    }
    private boolean saveBill(String type) {
        if (bla != null && !resultok) {
            try {
            boolean flag= bla.saveBill(type);
                resultok = true;
                return flag;
            } catch (Exception e) {
                new MessageInf(e).show(this);
            }
        }
        return false;
    }

  /*   private boolean validateDiscount(List<BillLineInfo> bill, BillLineInfo discountbline) {
        int quantity = discountbline.getMultiply();
        for (BillLineInfo blInfo : bill) {
            if (blInfo.getProduct().equals(discountbline.getProduct())) {
                quantity += blInfo.getMultiply();
            }
        }

        if (quantity < 0) {
            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.toomuchdiscount")).show(this);
            return false;
        }

        return true;
    }*/
  private void refreshItemModel() {
        if (jTable1 == null) {
            return;
        }
        int i = jTable1.getSelectedRow();

         billtablemodel = new BillItemTableModel(blist);
        jTable1.setModel(billtablemodel);

        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(40);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(2).setMaxWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(3).setMaxWidth(80);

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jBtnPay = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BILL");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Sl No", "Product", "Qty", "Rate", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Member ID");

        jTextField2.setEditable(false);

        jLabel3.setText("Date");

        jTextField3.setEditable(false);

        jButton5.setText("Debit Bill");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/apply.png"))); // NOI18N
        jButton1.setText("Cash Bill");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        jButton3.setText("Cancel");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jBtnPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/cash.png"))); // NOI18N
        jBtnPay.setText("Pay");
        jBtnPay.setEnabled(false);
        jBtnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPayActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/ark216.png"))); // NOI18N
        jButton2.setText("Reprint");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("Discount");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setText("Convert to Debit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jBtnPay, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnPay)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      int flag1=checkForBillDiscount(binfo.getID());
       if(flag1==0){
         boolean flag= saveBill("Cash");
         if(flag==true){
         printTicket("Printer.Ticket", binfo, binfo.getPlace());
         dispose();
         AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();
         try{
            String name=customertemp.getName();
            String idt=customertemp.getId();
            new StaticSentence(m_App.getSession()
                        , " DELETE FROM SHAREDTICKETS  WHERE CID = ? AND NAME = ? AND COUNTER=?"
                        , new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING,Datas.STRING}))
                        .exec(new Object[] {customertemp.getId(),customertemp.getSearchkey(),m_App.getAppUserView().getUser().getRole()});

         }
         catch(Exception e)
         {
         }
           JTicketsBagRestaurant.m_rest.newTicket();
       }
       }
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jBtnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPayActionPerformed
        int flag=checkForBillDiscount(binfo.getID());
        if(flag==0){
      //  saveBill();
            resultok=true;
       // if (resultok) {
            try {
                //jBtnPay.setEnabled(false);
                closeTicket(binfo, binfo.getPlace());
                dispose();
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }
      //  }
        }
    }//GEN-LAST:event_jBtnPayActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.reprintbill"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (res == JOptionPane.YES_OPTION) {
                printTicket("Printer.Ticket", binfo, binfo.getPlace());
        
       }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String crby=binfo.getCreatedBy();
        String auser=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName();
        if(auser.equals(crby)){
        if(jTable1.getSelectedRowCount()==1){
        int row=jTable1.getSelectedRow();
        BillLineInfo temp= blist.get(row);
        BillLineInfo bline=new BillLineInfo();
        bline.setProduct(temp.getProduct());
        bline.setParentid(temp.getParentid());

        int limit=temp.getMultiply();
       // Frame frame=new JFra();
        if(limit>0){
        discountPage discount = discountPage.getDialog(this, blist, bline, limit);
         boolean resultok = false;
                try {
                    resultok = discount.showDialog(cname);
                } catch(BasicException e) {
                    new MessageInf(e).show(this);
                }
        }else{
           JOptionPane.showMessageDialog(this, "Discount Request cannot be processed", "Discount Failed", JOptionPane.OK_OPTION);
        }
        }else {
           if( JOptionPane.showConfirmDialog(this, "Do you want to Discount the entire bill", "", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
           {
               int count=0;
                AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();
            try{
                  Object obj[]=(Object[])  new StaticSentence(m_App.getSession()
                        , "SELECT COUNT(*) FROM REVERSEDBILL WHERE BILLID = ?  GROUP BY BILLID  "
                        ,SerializerWriteString.INSTANCE
                ,new SerializerReadBasic(new Datas[]{Datas.INT})).find(binfo.getID());
                  if(obj!=null){
                     count=Integer.parseInt(obj[0].toString());
                  }
                  if(count==0){
                 Date dnow=new Date();
                  discountPage discount = discountPage.getDialog(this, blist, new BillLineInfo(), 0);
           
                 String reason=discount.showDialogreason();
              if(reason!=null){
               for(BillLineInfo bli:blist)
               {
                    //Double a=0;

                    Integer temp=bli.getMultiply();
                    if(temp>0){
                    Double qty=Double.parseDouble(temp.toString());
                    Double rate=bli.getRate();
                    Object[] value=new Object[]{UUID.randomUUID().toString(),bli.getParentid(),cname,bli.getProduct().getID(),(qty * -1),rate ,reason,m_App.getAppUserView().getUser().getName(),dnow};
                     new PreparedSentence(m_App.getSession()
                    , "INSERT INTO REVERSEDBILL (ID, BILLID ,CUSTOMER, PRODUCT, QTY, RATE,REASON,CREATEDBY,CRDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.STRING,Datas.TIMESTAMP})).exec(value);
                    }
               }
              }else
                  JOptionPane.showMessageDialog(this, "Cannot Disount", "", JOptionPane.OK_OPTION);

                  }
                  else{
                      JOptionPane.showMessageDialog(this, "Cannot Request for Disount.Request has already been sent", "", JOptionPane.OK_OPTION);
                  }
              }catch(Exception e)
                {
                     e.printStackTrace();
                }
           }else
            JOptionPane.showMessageDialog(this, "Please select one product", "Error", JOptionPane.OK_OPTION);
        }
        }else{
            JOptionPane.showMessageDialog(this, "Only the bill creator can discount it", "Error", JOptionPane.OK_OPTION);
        }
         //if(resultok)
       /*  if (resultok ) {
                    try {
                      //  int size=blist.size();
                        blist.add(bline);
                        blogic.insertBillItem(bline,blist.size());
                        //qticket.insertQTicketLine(qt, qtl);
                    } catch(BasicException e) {
                        new MessageInf(e);
                    }
                   refreshItemModel();
                }*/
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       int flag1=checkForBillDiscount(binfo.getID());
       if(flag1==0){
         boolean flag= saveBill("Debt");
         if(flag==true){
         printTicket("Printer.Ticket", binfo, binfo.getPlace());
         
         AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();
         try{
           // String name=customertemp.getName();
           // String idt=customertemp.getId();
            new StaticSentence(m_App.getSession()
                        , " DELETE FROM SHAREDTICKETS  WHERE CID = ? AND NAME = ? AND COUNTER=?"
                        , new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING,Datas.STRING}))
                        .exec(new Object[] {customertemp.getId(),customertemp.getSearchkey(),m_App.getAppUserView().getUser().getRole()});
            List<PaymentInfo> l=new ArrayList<PaymentInfo>();
            PaymentInfo p=new PaymentInfoTicket(binfo.getAmount(), "debt");
            l.add(p);
            binfo.setPayments(l) ;
            printTicket( "Printer.Ticket_1", binfo, "cerditconf");
            //dlSales.payDebtBill(binfo);
            dispose();
         }
         catch(Exception e)
         {
         }
           JTicketsBagRestaurant.m_rest.newTicket();
       }
       }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            if(JOptionPane.showConfirmDialog(null, "Do you want to change the cash bill to debit bill ?", "Change bill type",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
          Transaction t=new Transaction(LookupUtilityImpl.getInstance(null).getAppView().getSession()) {

                @Override
                protected Object transact() throws BasicException {
                      AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();
                      String rno = blogic.getNextReceiptID(m_App.getAppUserView().getUser().getName());
                      boolean flag = false;
                      String role=null;
                      Date d = new Date();
                      Object[] obj = (Object[]) new PreparedSentence(m_App.getSession(), "SELECT B.PAID,B.CREATEDDATE,P.ROLE FROM BILL B,PEOPLE P WHERE B.ID=? AND B.CREATEDBY=P.NAME"
                         , SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.BOOLEAN,Datas.TIMESTAMP,Datas.STRING})).find(binfo.getID());
                       if (obj != null) {
                           flag = Boolean.valueOf(obj[0].toString());
                            d=(Date)obj[1];
                            role=String.valueOf(obj[2]);
                      }
                     if( m_App.getAppUserView().getUser().getRole().equals(role)){
                      if(m_App.getAppUserView().getUser().hasPermission("bar counter") && !flag){
                         int cnt=new PreparedSentence(m_App.getSession(), "UPDATE BILL SET PAID =TRUE WHERE ID=? ", SerializerWriteString.INSTANCE).exec(binfo.getID());
                          if(cnt>0){
                              new PreparedSentence(m_App.getSession(), "INSERT INTO CREDITCONFLIST (ID,  DATENEW, RUSER,CUSTOMER,BILLREF,AMOUNT,WAITER) VALUES (?, ?, ?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING})).exec(new Object[]{rno, d, LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName(), binfo.getCustomerId(), binfo.getID(), binfo.getTotal(), binfo.getWaiter()});
                              List<PaymentInfo> l=new ArrayList<PaymentInfo>();
                              PaymentInfo p=new PaymentInfoTicket(binfo.getTotal(), "debt");
                              l.add(p);
                              binfo.setPayments(l) ;
                              binfo.setReceiptRef(rno);
                              printTicket( "Printer.Ticket_1", binfo, "cerditconf");
                          }
                       }
                      else if(m_App.getAppUserView().getUser().hasPermission("res counter") && !flag){
                         int cnt=new PreparedSentence(m_App.getSession(), "UPDATE BILL SET PAID =TRUE WHERE ID=? ", SerializerWriteString.INSTANCE).exec(binfo.getID());
                          if(cnt>0){
                              new PreparedSentence(m_App.getSession(), "INSERT INTO CREDITCONFLIST (ID,  DATENEW, RUSER,CUSTOMER,BILLREF,AMOUNT,WAITER) VALUES (?, ?, ?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING})).exec(new Object[]{rno, d, LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName(), binfo.getCustomerId(), binfo.getID(), binfo.getTotal(), binfo.getWaiter()});
                              List<PaymentInfo> l=new ArrayList<PaymentInfo>();
                              PaymentInfo p=new PaymentInfoTicket(binfo.getTotal(), "debt");
                              l.add(p);
                              binfo.setPayments(l) ;
                              binfo.setReceiptRef(rno);
                              printTicket( "Printer.Ticket_1", binfo, "cerditconf");
                          }
                       }
                    dispose();
                }else
                    JOptionPane.showMessageDialog(null, "Sorry,the bill does not belong to you", "cannot covert to debit bill", JOptionPane.WARNING_MESSAGE);
                       return null;
                }
            };
            t.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
    * @param args the command line arguments
    */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnPay;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables

}
