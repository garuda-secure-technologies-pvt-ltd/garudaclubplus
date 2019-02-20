/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BillList.java
 *
 * Created on Dec 12, 2008, 11:47:36 AM
 */
package com.openbravo.pos.sales.restaurant;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
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
import com.openbravo.pos.mant.WaiterInfo;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.BillInfo;
import com.openbravo.pos.sales.BillLineInfo;
import com.openbravo.pos.sales.BillListTableModel;
import com.openbravo.pos.sales.BillLogic;
import com.openbravo.pos.sales.BillTaxesLogic;
import com.openbravo.pos.sales.Billpage;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author swathi
 */
public class DebtBillList extends javax.swing.JDialog {

    private BillListTableModel billtablemodel;
    private CreditConfirmationTableModel cmodel;
    private List<BillInfo> blist;
    private BillLogic blogic;
    private DataLogicSales dlSales;
    private boolean resultok = false;
    private CustomerInfo customer;
    private AppView app;
    private List<CreditConfirmList> list;
    private boolean flag;
    private BillTaxesLogic taxeslogic;
    private ListKeyed taxcollection;
    private TicketParser m_TTP;
    private DataLogicSystem dlSystem;

    /** Creates new form BillList */
    public DebtBillList(java.awt.Frame parent, DataLogicSales dlSales, AppView app, boolean flag) {
        super(parent, true);
        this.dlSales = dlSales;
        this.app = app;
        this.flag = flag;
    }

    public DebtBillList(java.awt.Dialog parent, DataLogicSales dlSales, AppView app, boolean flag) {
        super(parent, true);
        this.dlSales = dlSales;
        this.app = app;
        this.flag = flag;
    }

    protected static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

    public static DebtBillList getDialog(Component parent, DataLogicSales dlSales, AppView app, boolean flag) {

        Window window = getWindow(parent);

        DebtBillList bill;

        if (window instanceof Frame) {
            bill = new DebtBillList((Frame) window, dlSales, app, flag);
        } else {
            bill = new DebtBillList((Dialog) window, dlSales, app, flag);
        }

        return bill;
    }

    public static class CreditConfirmList implements SerializableRead {

        private String id;
        private String billref;
        private String customer;
        private String ckey;
        private String waiter;
        private String ruser;
        private Timestamp date;
        private double amt;
        private String custid;

        public void readValues(DataRead dr) throws BasicException {
            date = dr.getTimestamp(1);
            customer = dr.getString(2);
            ckey = dr.getString(3);
            waiter = dr.getString(4);
            billref = dr.getString(5);
            id = dr.getString(6);
            ruser = dr.getString(7);
            amt = dr.getDouble(8);
            custid = dr.getString(9);
        // ckey=dr.getString(8);
        }

        public String getSearchkey() {
            return ckey;
        }

        public String getCustomer() {
            return customer;
        }

        public String getCSearchkey() {
            return ckey;
        }

        public String getWaiter() {
            return waiter;
        }

        public String getBillref() {
            return billref;
        }

        public String getID() {
            return id;
        }

        public String getRuser() {
            return ruser;
        }

        public Timestamp getDate() {
            return date;
        }

        public String printDate() {
            return Formats.DATE.formatValue(date);
        }

        public double getAmount() {
            return amt;
        }

        public String printAmount() {
            return Formats.ConvertDoubleToString(amt);
        }

        public String getCustomerID() {
            return custid;
        }
    }

    private void loadData() throws BasicException {
        list = new StaticSentence(app.getSession(), "SELECT D.DATENEW,C.NAME,C.SEARCHKEY,W.NAME,D.BILLREF,D.ID,D.RUSER,D.amount,c.id FROM CREDITCONFLIST D JOIN CUSTOMERS C ON D.CUSTOMER=C.ID JOIN WAITER W ON D.WAITER=W.ID ORDER BY D.DATENEW,W.NAME", null, new SerializerReadClass(CreditConfirmList.class)).list();
        if (list == null || list.size() <= 0) {
            list = new ArrayList<CreditConfirmList>();
        }
        cmodel = new CreditConfirmationTableModel(list);
        jTable1.setModel(cmodel);
    }

    public void init() throws BasicException {


        initComponents();
        jButton1.setText("Confirm");
        jButton2.setText("<html>Change to<br>Cash Bill</html>");
        jButton3.setText("Reprint");
        try {
            jButton2.setIcon(new ImageIcon(ImageIO.read(DebtBillList.class.getResourceAsStream("/com/openbravo/images/cash.png"))));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        loadData();
        List<TaxInfo> taxlist = dlSales.getTaxList().list();
        taxeslogic = new BillTaxesLogic(taxlist);
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        jButton1.setEnabled(flag);
        jButton2.setEnabled(flag);
        dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        m_TTP = new TicketParser(app.getDeviceTicket(), LookupUtilityImpl.getInstance(null).getDataLogicSystem());
        
        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(40);
        columnModel.getColumn(2).setPreferredWidth(60);
        columnModel.getColumn(2).setMaxWidth(60);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(3).setMaxWidth(80);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(4).setMaxWidth(100);
        columnModel.getColumn(5).setPreferredWidth(120);
        columnModel.getColumn(5).setMaxWidth(120);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(6).setMaxWidth(80);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(6).setMaxWidth(80);


    }

    public boolean showDialog() {
        try {
            init();
            setVisible(true);
        } catch (BasicException e) {
            new MessageInf(e).show(getParent());
        }
        return resultok;
    }

    public boolean markBillAsPaid(final BillInfo bill) throws BasicException {
        boolean status = true;
        Object[] values = new Object[]{bill.getID(), bill.getReceiptRef()};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        int cnt = new PreparedSentence(app.getSession(), "UPDATE BILL SET RECEIPT = ? WHERE ID = ? AND PAID=TRUE", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);
        if (cnt != 0) {
            PreparedSentence l = new PreparedSentence(app.getSession(), "UPDATE BILLITEM SET ATTRIBUTES = ? WHERE ID = ?", SerializerWriteParams.INSTANCE);
            for (final BillLineInfo line : bill.getLines()) {
                l.exec(new DataParams() {

                    @Override
                    public void writeValues() throws BasicException {
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            line.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(1, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(1, null);
                        }
                        setString(2, line.getID());
                    }
                });
            }
        } else {
            status = false;
            JOptionPane.showMessageDialog(null, "Error Occured...Try again...", null, JOptionPane.WARNING_MESSAGE);
        }
        return status;
    }

    private void printTicket(String sresourcename, BillInfo ticket, Object ticketext) {

        String sresource = LookupUtilityImpl.getInstance(null).getDataLogicSystem().getResourceAsXML(sresourcename);
        String receiptdate;

        String waitername;
        String table;
        int debt1 = 0;
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {

            Date date = new Date();
            receiptdate = date.toString();

            String sign = "";
            String temp = "";
            try {
                List<PaymentInfo> pi = ticket.getPayments();
                for (int i = 0; i < pi.size(); i++) {
                    temp = pi.get(i).getName();
                    if (pi.get(i).getName().equals("debt")) {
                        debt1 = 1;
                        sign = "sign";
                    }
                }
            } catch (Exception e) {
            }
            AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();

            try {

                WaiterInfo w = LookupUtilityImpl.getInstance(null).getWaiterMap().get(ticket.getWaiter());
                waitername = w.getName();
                Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT NAME FROM PLACES WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(ticket.getPlace());

                if (obj1 == null) {
                    table = "";
                } else {
                    table = obj1[0].toString();
                }

                Object[] obj2 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ID,NAME,ROLE FROM PEOPLE WHERE NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING})).find(ticket.getCreatedBy());

                AppUser appuser = new AppUser(obj2[0].toString(), obj2[1].toString(), obj2[2].toString());
                appuser.fillPermissions(dlSystem);
                boolean flag1 = appuser.hasPermission("bar counter");

                boolean flag = m_App.getAppUserView().getUser().hasPermission("bar counter");
                boolean crconf = false;
                if (ticketext.equals("cerditconf")) {
                    crconf = true;
                }
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

                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer,
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
            jButton1 = new javax.swing.JButton();
            jButton2 = new javax.swing.JButton();
            jButton3 = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Pending Bill List");

            jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null}
                },
                new String [] {
                    "Sl No", "Member Name", "Member ID", "Bill No", "Date"
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

            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/apply.png"))); // NOI18N
            jButton1.setText("Select");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jButton2.setText("jButton2");
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            jButton3.setText("jButton3");
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(61, 61, 61)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(26, 26, 26)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(26, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(29, 29, 29)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                            .addGap(52, 52, 52))))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        final int row = jTable1.getSelectedRow();
        if (row >= 0) {
            try {
                Transaction t = new Transaction(app.getSession()) {

                    @Override
                    protected Object transact() throws BasicException {
                        CreditConfirmList c = list.get(row);
                        boolean flag = true;
                        Object[] obj = (Object[]) new PreparedSentence(app.getSession(), "SELECT C.MOBILE,M.NAME FROM CUSTOMERS C JOIN MEMTYPE M ON C.MEMTYPE=M.ID WHERE ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING, Datas.STRING})).find(c.getCustomerID());
                        String str = null, memtype = null;
                        if (obj != null) {
                            if (obj[0] != null) {
                                str = String.valueOf(obj[0]);
                            }
                            if (obj[1] != null) {
                                memtype = String.valueOf(obj[0]);
                            } else {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                        if (flag && memtype.equals("Affiliated Member")) {
                            flag = false;
                        }
                        if (flag) {
                            BillInfo binfo = (BillInfo) new StaticSentence(app.getSession(), "SELECT BILL.ID, BILL.CUSTOMER, BILL.WAITER, BILL.PLACE, FLOORS.NAME, BILL.AMOUNT, BILL.CREATEDBY, BILL.CREATEDDATE, BILL.PAID, BILL.RECEIPT FROM BILL,FLOORS WHERE  BILL.ID=? AND BILL.FLOOR=FLOORS.ID AND BILL.PAID=TRUE ", SerializerWriteString.INSTANCE, new SerializerReadClass(BillInfo.class)).find(c.getBillref());
                            String rno = dlSales.getNextReceiptID(app.getAppUserView().getUser().getName());
                            binfo.setReceiptRef(rno);
                            BillLogic bl = LookupUtilityImpl.getInstance(null).getDataLogicBill();
                            binfo.setLines(bl.getBillLineList(binfo.getID()));
                            String user = app.getAppUserView().getUser().getName();
                            Date d = new Date();

                            int count = new StaticSentence(app.getSession(), "DELETE FROM CREDITCONFLIST WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.STRING})).exec(new Object[]{c.getID()});
                            if (count > 0) {
                                new StaticSentence(app.getSession(), "INSERT INTO RECEIPTS (ID,DATENEW,RUSER,DESC_) VALUES (?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING})).exec(new Object[]{rno, d, user, c.getID()});
                                new PreparedSentence(app.getSession(), "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, PUSER, PTIME, CUSTOMER) VALUES (?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING, Datas.TIMESTAMP, Datas.STRING})).exec(new Object[]{UUID.randomUUID().toString(), rno, "debt", c.getAmount(), user, d, c.getCustomerID()});
                                boolean status = markBillAsPaid(binfo);
                                if (status == false) {
                                    throw new BasicException();
                                }
                                try {
                                    // if("debt".equals(p.getName())){

                                    String msg = "Dear Member,\rYour a/c with us has been debited by " + Formats.CURRENCY.formatValue(c.getAmount()) + " for bar usage.Bill no. " + c.getBillref() + " On " + Formats.DATE.formatValue(c.getDate());
                                    if (str != null && str.length() == 10) {
                                        dlSales.updatetosendMsg(msg, c.getCustomerID(), str, 2);
                                    }
                                // }
                                } catch (Exception e) {
                                }
                                loadData();
                            }
                        }
                        return null;
                    }
                };
                t.execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        final int row = jTable1.getSelectedRow();
        if (row >= 0) {
            if (JOptionPane.showConfirmDialog(null, "Do you want to change to cash bill ", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Transaction t = new Transaction(app.getSession()) {

                        @Override
                        protected Object transact() throws BasicException {
                            CreditConfirmList c = list.get(row);
                            BillInfo binfo = (BillInfo) new StaticSentence(app.getSession(), "SELECT BILL.ID, BILL.CUSTOMER, BILL.WAITER, BILL.PLACE, FLOORS.NAME, BILL.AMOUNT, BILL.CREATEDBY, BILL.CREATEDDATE, BILL.PAID, BILL.RECEIPT FROM BILL,FLOORS WHERE  BILL.ID=? AND BILL.FLOOR=FLOORS.ID AND BILL.PAID=TRUE ", SerializerWriteString.INSTANCE, new SerializerReadClass(BillInfo.class)).find(c.getBillref());
                            String rno = dlSales.getNextReceiptID(app.getAppUserView().getUser().getName());
                            binfo.setReceiptRef(rno);
                            String user = app.getAppUserView().getUser().getName();
                            BillLogic bl = LookupUtilityImpl.getInstance(null).getDataLogicBill();
                            binfo.setLines(bl.getBillLineList(binfo.getID()));
                            //String user=app.getAppUserView().getUser().getName();
                            Date d = new Date();

                            int count = new StaticSentence(app.getSession(), "DELETE FROM CREDITCONFLIST WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.STRING})).exec(new Object[]{c.getID()});
                            if (count > 0) {
                                new StaticSentence(app.getSession(), "UPDATE BILL SET PAID=FALSE WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.STRING})).exec(new Object[]{c.getBillref()});
                    
                            }
                            loadData();
                            return null;
                        }
                    };
                    t.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //BillInfo binfo=dlSales.get
        int row = jTable1.getSelectedRow();
        if (row >= 0) {
            int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.reprintbill"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                try {
                    CreditConfirmList c = list.get(row);
                    BillLogic bl = LookupUtilityImpl.getInstance(null).getDataLogicBill();
                    BillInfo binfo = bl.getBillInfo(c.getBillref());
                    printTicket("Printer.Ticket", binfo, binfo.getPlace());
                    List<PaymentInfo> l = new ArrayList<PaymentInfo>();
                    PaymentInfo p = new PaymentInfoTicket(binfo.getTotal(), "debt");
                    l.add(p);
                    binfo.setPayments(l);
                    binfo.setReceiptRef(c.getID());
                    printTicket("Printer.Ticket_1", binfo, "cerditconf");
                } catch (BasicException ex) {
                    Logger.getLogger(DebtBillList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
