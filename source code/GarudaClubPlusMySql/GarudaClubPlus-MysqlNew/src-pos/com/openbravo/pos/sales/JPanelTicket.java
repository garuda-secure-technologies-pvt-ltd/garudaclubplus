package com.openbravo.pos.sales;

//import com.a.a.a.g.p;
import java.util.logging.Level;
import javax.swing.*;
import java.util.logging.Logger;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;

import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.printer.*;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.JProductFinder;
import com.openbravo.pos.scale.ScaleException;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.DateUtils;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.admin.RoleInfo;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.clubmang.GeneralSettingsAccounting;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPrincipalApp;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.sales.restaurant.QTList;
import com.openbravo.pos.sms.SMSgeneralDBSettings;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.PrintCategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import com.openbravo.pos.util.StringUtils;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.print.PrintService;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author adrianromero
 */
public abstract class JPanelTicket extends JPanel implements JPanelView, BeanFactoryApp, TicketsEditor {

    // Variable numerica
    private final static int NUMBERZERO = 0;
    private final static int NUMBERVALID = 1;
    private final static int NUMBER_INPUTZERO = 0;
    private final static int NUMBER_INPUTZERODEC = 1;
    private final static int NUMBER_INPUTINT = 2;
    private final static int NUMBER_INPUTDEC = 3;
    private final static int NUMBER_PORZERO = 4;
    private final static int NUMBER_PORZERODEC = 5;
    private final static int NUMBER_PORINT = 6;
    private final static int NUMBER_PORDEC = 7;
    protected JTicketLines1 m_ticketlines;
    // private Template m_tempLine;
    private TicketParser m_TTP;
    protected TicketInfo m_oTicket;
    protected Object m_oTicketExt;
    // Estas tres variables forman el estado...
    private int m_iNumberStatus;
    private int m_iNumberStatusInput;
    private int m_iNumberStatusPor;
    private StringBuffer m_sBarcode;
    private JTicketsBag m_ticketsbag;
    private SentenceList senttax;
    private ListKeyed taxcollection;
    // private ComboBoxValModel m_TaxModel;
    private SentenceList senttaxcategories;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;
    private TaxesLogic taxeslogic;
//    private ScriptObject scriptobjinst;
    protected JPanelButtons m_jbtnconfig;
    protected AppView m_App;
    protected DataLogicSystem dlSystem;
    protected DataLogicSales dlSales;
    protected DataLogicCustomers dlCustomers;
    protected DataLogicReceipts dlReceipts;
    protected SMSgeneralDBSettings smsDBSettings;
//    protected QTKitchen qtk;
    protected Qticket qTicket;
    private JPaymentSelect paymentdialogreceipt;
    private JPaymentSelect paymentdialogrefund;
    protected String name;

    /**
     * Creates new form JTicketView
     */
    public JPanelTicket() {

        initComponents();
    }

    public void init(AppView app) throws BeanFactoryException {

        m_App = app;
        dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
        dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
        smsDBSettings = (SMSgeneralDBSettings) m_App.getBean("com.openbravo.pos.sms.SMSgeneralDBSettings");
        //      qtk = (QTKitchen) app.getBean("com.openbravo.pos.sales.QTKitchen");///aaa

        qTicket = (Qticket) m_App.getBean("com.openbravo.pos.sales.Qticket");
        qTicket.setDataLogicSales(dlSales);
        qTicket.setAppView(m_App);

        // borramos el boton de bascula si no hay bascula conectada
        if (!m_App.getDeviceScale().existsScale()) {
            m_jbtnScale.setVisible(false);
        }

        m_ticketsbag = getJTicketsBag();
        m_jPanelBag.add(m_ticketsbag.getBagComponent(), BorderLayout.LINE_START);
        add(m_ticketsbag.getNullComponent(), "null");

        m_ticketlines = new JTicketLines1(dlSystem.getResourceAsXML("Ticket.Line"));
        m_jPanelCentral.add(m_ticketlines, java.awt.BorderLayout.CENTER);

        m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);

        // Los botones configurables...
        m_jbtnconfig = new JPanelButtons("Ticket.Buttons", this);
        m_jButtonsExt.add(m_jbtnconfig);

        // El panel de los productos o de las lineas...        
        catcontainer.add(getSouthComponent(), BorderLayout.CENTER);

        // El modelo de impuestos
        senttax = dlSales.getTaxList();
        senttaxcategories = dlSales.getTaxCategoriesList();

        taxcategoriesmodel = new ComboBoxValModel();

        // ponemos a cero el estado
        stateToZero();

        // inicializamos
        m_oTicket = null;
        m_oTicketExt = null;
    }

    public Object getBean() {
        return this;
    }

    public JComponent getComponent() {
        return this;
    }
///aaa  

    public double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return new Double(twoDForm.format(d)).doubleValue();
    }
///aaa

    public void activate() throws BasicException {

        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(m_App);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this);
        paymentdialogrefund.init(m_App);

        // impuestos incluidos seleccionado ?
        m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

        // Inicializamos el combo de los impuestos.
        java.util.List<TaxInfo> taxlist = senttax.list();
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        java.util.List<TaxCategoryInfo> taxcategorieslist = senttaxcategories.list();
        taxcategoriescollection = new ListKeyed<TaxCategoryInfo>(taxcategorieslist);

        taxcategoriesmodel = new ComboBoxValModel(taxcategorieslist);
        m_jTax.setModel(taxcategoriesmodel);

        String taxesid = m_jbtnconfig.getProperty("taxcategoryid");
        if (taxesid == null) {
            if (m_jTax.getItemCount() > 0) {
                m_jTax.setSelectedIndex(0);
            }
        } else {
            taxcategoriesmodel.setSelectedKey(taxesid);
        }

        taxeslogic = new TaxesLogic(taxlist);

        // Show taxes options
        if (m_App.getAppUserView().getUser().hasPermission("sales.ChangeTaxOptions")) {
            m_jTax.setVisible(true);
            m_jaddtax.setVisible(true);
        } else {
            m_jTax.setVisible(false);
            m_jaddtax.setVisible(false);
        }

        // Authorization for buttons
        //btnSplit.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        btnSplit.setEnabled(true);
        m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        m_jNumberKeys.setMinusEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
//        m_jNumberKeys.setEqualsEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        m_jNumberKeys.setEqualsEnabled(true);
        m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());

        m_ticketsbag.activate();
    }

    public boolean deactivate() {

        return m_ticketsbag.deactivate();

    }

    protected abstract JTicketsBag getJTicketsBag();

    protected abstract Component getSouthComponent();

    public void setActiveTicket(TicketInfo oTicket, Object oTicketExt) {

        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;

        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");
        try {
            refreshTicket();
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public TicketInfo getActiveTicket() {
        return m_oTicket;
    }

    private void refreshTicket() throws BasicException {

        CardLayout cl = (CardLayout) (getLayout());

        if (m_oTicket == null) {
            m_jTicketId.setText(null);
            m_ticketlines.clearTicketLines();

            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null);

            stateToZero();

            // Muestro el panel de nulos.
            cl.show(this, "null");

        } else {

            // Refresh ticket taxes
            for (TicketLineInfo line : m_oTicket.getLines()) {
                line.setTaxInfo(taxeslogic.getTaxInfo(line.getProductTaxCategoryID(), m_oTicket.getCustomer()));
            }

            // The ticket name
            m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
///aaa
            jTextField1.setRequestFocusEnabled(false);//*imp*

            jTextField1.setBackground(java.awt.Color.white);
            jTextField1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jTextField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
            jTextField1.setOpaque(true);
            jTextField1.setPreferredSize(new java.awt.Dimension(150, 25));

            Font font = new Font("Times New Roman", Font.BOLD, 13);
            jTextField1.setFont(font);

            String cust = m_oTicket.getCustomerId();
            String id = cust;

            try {
                Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(id.toString());

                if (obj1 != null) {
                    String opb = obj1[0].toString();
                    Double OPB = new Double(opb);

                    //added by shweta
                    Object[] objm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT MEMTYPE FROM CUSTOMERS where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id.toString());

                    if (objm == null) {
                        String a = m_oTicket.getCustomerId();
                        String a1 = a.substring(0, 36);
                        Object[] gobjm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT MEMTYPE FROM CUSTOMERS where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(a1.toString());
                        String memtype = gobjm[0].toString();

                        Object[] gobj2 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT DEBTMAX FROM memtype where id=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(memtype);
                        String dMax = gobj2[0].toString();
                        Double debtMax = new Double(dMax);
                        Object[] objstotal1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT FROMDATE,TODATE,ADDAMOUNT FROM QTCR_LIMITAUTH WHERE CUSTOMER=? AND ACTIVE IS TRUE",
                                new SerializerWriteBasic(new Datas[]{Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.DOUBLE})).find(new Object[]{id});
                        Date fromdate = new Date();
                        Date todate = new Date();
                        Date currentdate = new Date();
                        double addmnt1 = 0.0;
                        if (objstotal1 != null) {
                            fromdate = (Date) objstotal1[0];
                            todate = (Date) objstotal1[1];
                            addmnt1 = (double) objstotal1[2];
                            if (currentdate.after(fromdate) && currentdate.before(todate)) {
                                debtMax = debtMax + addmnt1;

                                if (OPB > debtMax) {
                                    OPB = OPB - addmnt1;

                                } else if (OPB < debtMax) {
                                    OPB = OPB - addmnt1;

                                } else {
                                    OPB = OPB;
                                }
                            }
                        }
                    }

                    roundTwoDecimals(OPB);
                    if (OPB > 0) {
                        jTextField1.setForeground(Color.red);
                        jTextField1.setText("Dr.  " + OPB);
                    } else if (OPB < 0) {
                        jTextField1.setForeground(Color.green);
                        OPB = OPB * (-1);
                        jTextField1.setText("Cr.  " + OPB);
                    } else {
                        jTextField1.setForeground(Color.black);
                        jTextField1.setText("" + OPB);
                    }
                }
///aaa                       
            } catch (Exception e) {
                e.printStackTrace();
                throw new BasicException(e.getMessage());
            }
            // Limpiamos todas las filas y anadimos las del ticket actual
            m_ticketlines.clearTicketLines();

            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_ticketlines.addTicketLine(m_oTicket.getLine(i));
            }
            printPartialTotals();
            stateToZero();

            // Muestro el panel de tickets.
            cl.show(this, "ticket");

            // activo el tecleador...
            m_jKeyFactory.setText(null);
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    m_jKeyFactory.requestFocus();
                }
            });
        }
    }

    private void printPartialTotals() {

        if (m_oTicket.getLinesCount() == 0) {
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null);
        } else {

            m_jSubtotalEuros.setText(m_oTicket.printSubTotal());
            //  m_jTotalEuros.setText(m_oTicket.printTotal());
            System.out.println(m_oTicket.getLines());
            m_oTicket.getLinesCount();
            System.out.println(m_oTicket.getLine(0).getTaxInfo().getId());
            /////i got struck here

            String gett = m_oTicket.getLine(0).getTaxInfo().getId();
            ArrayList<TicketLineInfo> arrayList = (ArrayList<TicketLineInfo>) m_oTicket.getLines();
            for (int i = 0; i < arrayList.size(); i++) {

                Object st2 = arrayList.size();
                int ib = (Integer) st2;
                int ib1 = ib - 1;
                String st1 = m_oTicket.getLine(ib1).getTaxInfo().getId();
                name = st1;

                try {
                    Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT RATEROUNDOFF FROM TAXES WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(st1);
                    if (obj[0].equals("yes")) {
                        System.out.println(obj[0]);
                        System.out.println("yes");

                        double str = m_oTicket.getTax();
                        System.out.println(new Float(Math.round(str)));
                        Object f = new Float(Math.round(str));
                        String st = f.toString();
                        m_jTaxesEuros.setText(Formats.CURRENCY.formatValue(new Double(st)));
                        double str1 = m_oTicket.getSubTotal();
                        double str2 = str1 + Math.round(str);
                        Object o1 = new Double(str2);
                        o1.toString();
                        m_jTotalEuros.setText(Formats.CURRENCY.formatValue(new Double(o1.toString())));

                    } else if (obj[0].equals("yesnearest")) {
                        double str = m_oTicket.getTax();
                        System.out.println(new Float(Math.round(str)));
                        Object f = new Float(Math.round(str));
                        String st = f.toString();
                        m_jTaxesEuros.setText(Formats.CURRENCY.formatValue(new Double(st)));
                        double str1 = m_oTicket.getSubTotal();
                        double str2 = str1 + Math.round(str);
                        Object o1 = new Double(str2);
                        o1.toString();
                        m_jTotalEuros.setText(Formats.CURRENCY.formatValue(new Double(o1.toString())));

                    } else if (obj[0].equals("yesnext")) {
                        double str = m_oTicket.getTax();
                        System.out.println(new Float(Math.round(str) + 1));
                        Object f = new Float(Math.round(str) + 1);
                        String st = f.toString();
                        m_jTaxesEuros.setText(Formats.CURRENCY.formatValue(new Double(st)));
                        double str1 = m_oTicket.getSubTotal();
                        double str2 = str1 + Math.round(str) + 1;
                        Object o1 = new Double(str2);
                        o1.toString();
                        m_jTotalEuros.setText(Formats.CURRENCY.formatValue(new Double(o1.toString())));

                    } else if (obj[0].equals("yesprevious")) {
                        double str = m_oTicket.getTax();
                        System.out.println(new Float(Math.round(str)));
                        Object f = new Float(Math.round(str) - 1);
                        String st = f.toString();
                        m_jTaxesEuros.setText(Formats.CURRENCY.formatValue(new Double(st)));
                        double str1 = m_oTicket.getSubTotal();
                        double str2 = str1 + Math.round(str) - 1;
                        Object o1 = new Double(str2);
                        o1.toString();
                        m_jTotalEuros.setText(Formats.CURRENCY.formatValue(new Double(o1.toString())));

                    } else {
                        System.out.println("no");
                        System.out.println(obj[0]);
                        m_jSubtotalEuros.setText(m_oTicket.printSubTotal());
                        m_jTaxesEuros.setText(m_oTicket.printTax());
                        m_jTotalEuros.setText(m_oTicket.printTotal());
                    }
                } catch (BasicException ex) {
                    Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    private void paintTicketLine(int index, TicketLineInfo oLine) throws BasicException {

        if (executeEventAndRefresh("ticket.setline", new ScriptArg("index", index), new ScriptArg("line", oLine)) == null) {

            m_ticketlines.setTicketLine(index, oLine);
            m_ticketlines.setSelectedIndex(index);

            visorTicketLine(oLine); // Y al visor tambien...
            printPartialTotals();
            stateToZero();

            // event receipt
            executeEventAndRefresh("ticket.change");
        }
    }

    private void addTicketLine(ProductInfoExt oProduct, double dMul, double dPrice) throws BasicException {

        TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryInfo(), m_oTicket.getCustomer());
        TaxInfo tax2 = taxeslogic.getTaxInfo(oProduct.getTaxCategoryInfo2(), m_oTicket.getCustomer());                                                             // edited by aakash
        TaxInfo tax3 = taxeslogic.getTaxInfo(oProduct.getTaxCategoryInfo3(), m_oTicket.getCustomer());

        Boolean Basic2 = oProduct.getBASIC2();
        Boolean Basic3 = oProduct.getBASIC3();
        // edited by aakash foe kitchen products ............................................................................

        String Productid = oProduct.getID();
        Object[] prodDeac = (Object[]) new StaticSentence(m_App.getSession(), "SELECT PRODUCTID FROM deactiveproduct where PRODUCTID=? AND ACTIVE=1", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(Productid);
        if (prodDeac != null) {
            JOptionPane.showMessageDialog(null, "Product deactivated for the day... \n Sorry cannot create QT.", "Warning", JOptionPane.OK_OPTION);
        } else {

            addTicketLine(new TicketLineInfo(oProduct, dMul, dPrice, tax, (java.util.Properties) (oProduct.getProperties().clone()), tax2, tax3, Basic2, Basic3));
        }

    }

    protected void addTicketLine(TicketLineInfo oLine) {
        try {
            if (executeEventAndRefresh("ticket.addline", new ScriptArg("line", oLine)) == null) {

                if (oLine.isProductCom()) {
                    // Comentario entonces donde se pueda
                    int i = m_ticketlines.getSelectedIndex();

                    // me salto el primer producto normal...
                    if (i >= 0 && !m_oTicket.getLine(i).isProductCom()) {
                        i++;
                    }

                    // me salto todos los productos auxiliares...
                    while (i >= 0 && i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
                        i++;
                    }

                    if (i >= 0) {
                        m_oTicket.insertLine(i, oLine);
                        m_ticketlines.insertTicketLine(i, oLine); // Pintamos la linea en la vista...                 
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                } else {
                    // Producto normal, entonces al final
                    m_oTicket.addLine(oLine);
                    m_ticketlines.addTicketLine(oLine); // Pintamos la linea en la vista... 
                }

                visorTicketLine(oLine);
                printPartialTotals();
                stateToZero();

                // event receipt
                executeEventAndRefresh("ticket.change");
            }
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void removeTicketLine(int i) throws BasicException {

        if (executeEventAndRefresh("ticket.removeline", new ScriptArg("index", i)) == null) {

            if (m_oTicket.getLine(i).isProductCom()) {
                // Es un producto auxiliar, lo borro y santas pascuas.
                m_oTicket.removeLine(i);
                m_ticketlines.removeTicketLine(i);
            } else {
                // Es un producto normal, lo borro.
                m_oTicket.removeLine(i);
                m_ticketlines.removeTicketLine(i);
                // Y todos lo auxiliaries que hubiera debajo.
                while (i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                }
            }

            visorTicketLine(null); // borro el visor 
            printPartialTotals(); // pinto los totales parciales...                           
            stateToZero(); // Pongo a cero    

            // event receipt
            executeEventAndRefresh("ticket.change");
        }
    }

    private ProductInfoExt getInputProduct() {
        ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
        oProduct.setReference(null);
        oProduct.setCode(null);
        oProduct.setName("");
        oProduct.setTaxCategoryInfo((TaxCategoryInfo) taxcategoriesmodel.getSelectedItem());

        oProduct.setPriceSell(includeTaxes(oProduct.getTaxCategoryInfo(), getInputValue()));

        return oProduct;
    }

    private double includeTaxes(TaxCategoryInfo tc, double dValue) {
        if (m_jaddtax.isSelected()) {
            TaxInfo tax = taxeslogic.getTaxInfo(tc, m_oTicket.getCustomer());
            double dTaxRate = tax == null ? 0.0 : tax.getRate();
            return dValue / (1.0 + dTaxRate);
        } else {
            return dValue;
        }
    }

    private double getInputValue() {
        try {
            return Double.parseDouble(m_jPrice.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private double getPorValue() {
        try {
            return Double.parseDouble(m_jPor.getText().substring(1));
        } catch (NumberFormatException e) {
            return 1.0;
        } catch (StringIndexOutOfBoundsException e) {
            return 1.0;
        }
    }

    private void stateToZero() {
        m_jPor.setText("");
        m_jPrice.setText("");
        m_sBarcode = new StringBuffer();

        m_iNumberStatus = NUMBER_INPUTZERO;
        m_iNumberStatusInput = NUMBERZERO;
        m_iNumberStatusPor = NUMBERZERO;
    }

    private void incProductByCode(String sCode) {
        // precondicion: sCode != null

        try {
            ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {
                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                incProduct(oProduct);
            }
        } catch (BasicException eData) {
            stateToZero();
            new MessageInf(eData).show(this);
        }
    }

    private void incProductByCodePrice(String sCode, double dPriceSell) {
        // precondicion: sCode != null

        try {
            ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {
                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                if (m_jaddtax.isSelected()) {
                    // debemos quitarle los impuestos ya que el precio es con iva incluido...
                    TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryInfo(), m_oTicket.getCustomer());
                    addTicketLine(oProduct, 1.0, dPriceSell / (1.0 + tax.getRate()));
                } else {
                    addTicketLine(oProduct, 1.0, dPriceSell);
                }

            }
        } catch (BasicException eData) {
            stateToZero();
            new MessageInf(eData).show(this);
        }
    }

    private void incProduct(ProductInfoExt prod) throws BasicException {

        if (prod.isScale() && m_App.getDeviceScale().existsScale()) {
            try {
                Double value = m_App.getDeviceScale().readWeight();
                if (value != null) {
                    incProduct(value.doubleValue(), prod);
                }
            } catch (ScaleException e) {
                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);
                stateToZero();
            }
        } else {
            // No es un producto que se pese o no hay balanza
            incProduct(1.0, prod);
        }
    }

    private void incProduct(double dPor, ProductInfoExt prod) throws BasicException {
        // precondicion: prod != null
        addTicketLine(prod, dPor, prod.getPriceSell());
    }

    protected void buttonTransition(ProductInfoExt prod) {
        // precondicion: prod != null

        if (m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
            try {
                incProduct(prod);
            } catch (BasicException ex) {
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
            try {
                incProduct(getInputValue(), prod);

            } catch (BasicException ex) {
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void addUnits(double dUnits) throws BasicException {
        int i = m_ticketlines.getSelectedIndex();
        if (i >= 0) {
            TicketLineInfo oLine = m_oTicket.getLine(i);
            double qty = oLine.getMultiply() + dUnits;
            if (qty > 0) {
                oLine.setMultiply(oLine.getMultiply() + dUnits);
                // paintTicketLine(i, oLine);
            } else {
                JOptionPane.showMessageDialog(null, "The Total Quantity Should Be Greater Then Zero");
            }
            paintTicketLine(i, oLine);
            /*   InventoryLine inv = m_invlines.getLine(i);
        double dunits = inv.getMultiply() + dUnits;
        if (dunits == 0.0) {
        deleteLine(i);
        } else {
        inv.setMultiply(inv.getMultiply() + dUnits);
        m_invlines.setLine(i, inv);
        }*/
        }
    }
    //  EDITED BY AAKASH

    private void stateTransition(char cTrans) throws BasicException {

        if (cTrans == '\u007f') {
            m_jPor.setText("");
        } else if (cTrans == '+') {
            if (m_jPor.getText() == null || m_jPor.getText().equals("")) {
                // anadimos una unidad
                addUnits(1.0);
            } else {
                addUnits(Double.parseDouble(m_jPor.getText()));
                m_jPor.setText("");
            }
        } else if (cTrans == '-') {
            if (m_jPor.getText() == null || m_jPor.getText().equals("")) {
                // anadimos una unidad
                addUnits(-1.0);
            } else {
                addUnits(-Double.parseDouble(m_jPor.getText()));
                m_jPor.setText("");
            }
        }/*else  if (cTrans == '*' || cTrans == '=') {
        if ( m_jPor.getText() == null ||  m_jPor.getText().equals("")) {
        // anadimos una unidad
        addUnits(-1.0);
        } else {
        addUnits(-Double.parseDouble( m_jPor.getText()));
        m_jPor.setText("");
        }

        }*/ // EDITED BY AKASH
        else if (cTrans == ' ' || cTrans == '=') {
            //  if ( m_jPor.getCount() == 0) {
            // No podemos grabar, no hay ningun registro.
            //      Toolkit.getDefaultToolkit().beep();
            //   } //else {
            //saveData();
            //}
        } else {
            // String del=m_jPor.getText();
            //   String del1= m_jPor.getText() + cTrans;
            m_jPor.setText(m_jPor.getText() + cTrans);
        }
    }
    // EDITED BY AAKSH

    //******************************************** EDITED BY AAKASH *************************************** 
    /*
    private void stateTransition(char cTrans)  throws BasicException{

    if (cTrans == '\n') {
    // Codigo de barras introducido
    if (m_sBarcode.length() > 0) {
    String sCode = m_sBarcode.toString();
    if (sCode.startsWith("c")) {
    // barcode of a customers card
    try {
    CustomerInfoExt newcustomer = dlSales.findCustomerExt(sCode);
    if (newcustomer == null) {
    Toolkit.getDefaultToolkit().beep();
    new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer")).show(this);
    } else {
    m_oTicket.setCustomer(newcustomer);
    m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
    }
    } catch (BasicException e) {
    Toolkit.getDefaultToolkit().beep();
    new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer"), e).show(this);
    }
    stateToZero();
    } else if (sCode.length() == 13 && sCode.startsWith("250")) {
    // barcode of the other machine
    ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
    oProduct.setReference(null); // para que no se grabe
    oProduct.setCode(sCode);
    oProduct.setName("Ticket " + sCode.substring(3, 7));
    oProduct.setPriceSell(Double.parseDouble(sCode.substring(7, 12)) / 100);
    oProduct.setTaxCategoryInfo((TaxCategoryInfo) taxcategoriesmodel.getSelectedItem());
    // Se anade directamente una unidad con el precio y todo
    addTicketLine(oProduct, 1.0, includeTaxes(oProduct.getTaxCategoryInfo(), oProduct.getPriceSell()));
    } else if (sCode.length() == 13 && sCode.startsWith("210")) {
    // barcode of a weigth product
    incProductByCodePrice(sCode.substring(0, 7), Double.parseDouble(sCode.substring(7, 12)) / 100);
    } else {
    incProductByCode(sCode);
    }
    } else {
    Toolkit.getDefaultToolkit().beep();
    }
    } else {
    // otro caracter
    // Esto es para el codigo de barras...
    m_sBarcode.append(cTrans);

    // Esto es para el los productos normales...
    if (cTrans == '\u007f') {
    stateToZero();

    } else if ((cTrans == '0')
    && (m_iNumberStatus == NUMBER_INPUTZERO)) {
    m_jPor.setText("0");
    } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
    && (m_iNumberStatus == NUMBER_INPUTZERO)) {
    // Un numero entero
    m_jPor.setText(Character.toString(cTrans));
    m_iNumberStatus = NUMBER_INPUTINT;
    m_iNumberStatusInput = NUMBERVALID;
    } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
    && (m_iNumberStatus == NUMBER_INPUTINT)) {
    // Un numero entero
    m_jPrice.setText(m_jPrice.getText() + cTrans);

    } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTZERO) {
    m_jPrice.setText("0.");
    m_iNumberStatus = NUMBER_INPUTZERODEC;
    } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTINT) {
    m_jPrice.setText(m_jPrice.getText() + ".");
    m_iNumberStatus = NUMBER_INPUTDEC;

    } else if ((cTrans == '0')
    && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
    // Un numero decimal
    m_jPrice.setText(m_jPrice.getText() + cTrans);
    } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
    && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
    // Un numero decimal
    m_jPrice.setText(m_jPrice.getText() + cTrans);
    m_iNumberStatus = NUMBER_INPUTDEC;
    m_iNumberStatusInput = NUMBERVALID;

    } else if (cTrans == '*'
    && (m_iNumberStatus == NUMBER_INPUTINT || m_iNumberStatus == NUMBER_INPUTDEC)) {
    m_jPor.setText("x");
    m_iNumberStatus = NUMBER_PORZERO;
    } else if (cTrans == '*'
    && (m_iNumberStatus == NUMBER_INPUTZERO || m_iNumberStatus == NUMBER_INPUTZERODEC)) {
    m_jPrice.setText("0");
    m_jPor.setText("x");
    m_iNumberStatus = NUMBER_PORZERO;

    } else if ((cTrans == '0')
    && (m_iNumberStatus == NUMBER_PORZERO)) {
    m_jPor.setText("x0");
    } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
    && (m_iNumberStatus == NUMBER_PORZERO)) {
    // Un numero entero
    m_jPor.setText("x" + Character.toString(cTrans));
    m_iNumberStatus = NUMBER_PORINT;
    m_iNumberStatusPor = NUMBERVALID;
    } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
    && (m_iNumberStatus == NUMBER_PORINT)) {
    // Un numero entero
    m_jPor.setText(m_jPor.getText() + cTrans);

    } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORZERO) {
    m_jPor.setText("x0.");
    m_iNumberStatus = NUMBER_PORZERODEC;
    } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORINT) {
    m_jPor.setText(m_jPor.getText() + ".");
    m_iNumberStatus = NUMBER_PORDEC;

    } else if ((cTrans == '0')
    && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) {
    // Un numero decimal
    m_jPor.setText(m_jPor.getText() + cTrans);
    } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
    && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) {
    // Un numero decimal
    m_jPor.setText(m_jPor.getText() + cTrans);
    m_iNumberStatus = NUMBER_PORDEC;
    m_iNumberStatusPor = NUMBERVALID;

    } else if (cTrans == '\u00a7'
    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
    // Scale button pressed and a number typed as a price
    if (m_App.getDeviceScale().existsScale() && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
    try {
    Double value = m_App.getDeviceScale().readWeight();
    if (value != null) {
    ProductInfoExt product = getInputProduct();
    addTicketLine(product, value.doubleValue(), product.getPriceSell());
    }
    } catch (ScaleException e) {
    Toolkit.getDefaultToolkit().beep();
    new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);
    stateToZero();
    }
    } else {
    // No existe la balanza;
    Toolkit.getDefaultToolkit().beep();
    }
    } else if (cTrans == '\u00a7'
    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
    // Scale button pressed and no number typed.
    int i = m_ticketlines.getSelectedIndex();
    if (i < 0){
    Toolkit.getDefaultToolkit().beep();
    } else if (m_App.getDeviceScale().existsScale()) {
    try {
    Double value = m_App.getDeviceScale().readWeight();
    if (value != null) {
    TicketLineInfo oLine = m_oTicket.getLine(i);
    oLine.setMultiply(value.doubleValue());
    oLine.setPrice(Math.abs(oLine.getPrice()));
    paintTicketLine(i, oLine);
    }
    } catch (ScaleException e) {
    // Error de pesada.
    Toolkit.getDefaultToolkit().beep();
    new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);
    stateToZero();
    }
    } else {
    // No existe la balanza;
    Toolkit.getDefaultToolkit().beep();
    }

    // Anadimos un producto mas a la linea seleccionada
    } else if (cTrans == '+'
    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
    int i = m_ticketlines.getSelectedIndex();
    if (i < 0){
    Toolkit.getDefaultToolkit().beep();
    } else {
    // Sumamos uno a la seleccionada...
    TicketLineInfo oLine = m_oTicket.getLine(i);
    oLine.setMultiply(oLine.getMultiply() + 1.0);
    paintTicketLine(i, oLine);
    }

    // Eliminamos un producto mas a la linea seleccionada
    } else if (cTrans == '-'
    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO
    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {

    int i = m_ticketlines.getSelectedIndex();
    if (i < 0){
    Toolkit.getDefaultToolkit().beep();
    } else {
    // Restamos uno a la seleccionada...
    TicketLineInfo oLine = m_oTicket.getLine(i);
    oLine.setMultiply(oLine.getMultiply() - 1.0);
    if (oLine.getMultiply() <= 0.0) {
    removeTicketLine(i); // elimino la linea
    } else {
    paintTicketLine(i, oLine);
    }
    }

    // Ponemos n productos a la linea seleccionada
    } else if (cTrans == '+'
    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID) {
    int i = m_ticketlines.getSelectedIndex();
    if (i < 0){
    Toolkit.getDefaultToolkit().beep();
    } else {
    double dPor = getPorValue();
    TicketLineInfo oLine = m_oTicket.getLine(i);
    oLine.setMultiply(dPor);
    oLine.setPrice(Math.abs(oLine.getPrice()));
    paintTicketLine(i, oLine);
    }

    // Ponemos n productos negativos a la linea seleccionada
    } else if (cTrans == '-'
    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID
    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {

    int i = m_ticketlines.getSelectedIndex();
    if (i < 0){
    Toolkit.getDefaultToolkit().beep();
    } else {
    double dPor = getPorValue();
    TicketLineInfo oLine = m_oTicket.getLine(i);
    oLine.setMultiply(dPor);
    oLine.setPrice(-Math.abs(oLine.getPrice()));
    paintTicketLine(i, oLine);
    }

    // Anadimos 1 producto
    } else if (cTrans == '+'
    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
    ProductInfoExt product = getInputProduct();
    addTicketLine(product, 1.0, product.getPriceSell());

    // Anadimos 1 producto con precio negativo
    } else if (cTrans == '-'
    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
    ProductInfoExt product = getInputProduct();
    addTicketLine(product, 1.0, -product.getPriceSell());

    // Anadimos n productos
    } else if (cTrans == '+'
    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
    ProductInfoExt product = getInputProduct();
    addTicketLine(product, getPorValue(), product.getPriceSell());

    // Anadimos n productos con precio negativo ?
    } else if (cTrans == '-'
    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
    ProductInfoExt product = getInputProduct();
    addTicketLine(product, getPorValue(), -product.getPriceSell());

    // Totals() Igual;
    } else if (cTrans == ' ' || cTrans == '=') {
    if (m_oTicket.getLinesCount() > 0) {

    if (closeTicket(m_oTicket, m_oTicketExt)) {
    // Ends edition of current receipt
    m_ticketsbag.deleteTicket();
    } else {
    // repaint current ticket
    refreshTicket();
    }
    } else {
    Toolkit.getDefaultToolkit().beep();
    }
    }
    }
    }
     */
    // ************************* EDITED BY AAKASH *******************************************************
    private void createQTicket(TicketInfo ticket) throws Exception {
        QTLogic qtLogic = new QTLogic(ticket, dlSales, qTicket);
        qtLogic.dispatchQT();
        saveAndPrintQTs(qtLogic.getQTickets());

    }

    private void saveAndPrintQTs(final Collection<QticketInfo> qts) throws Exception {

        for (QticketInfo qtInfo : qts) {
            boolean flag = qTicket.saveQTicket(qtInfo);
            if (flag == true) {
                printqt(qtInfo.getprCategory(), qtInfo);
                checkSMSflagForQT(qtInfo);
            } else {
                break;
            }
        }
    }

    public void checkSMSflagForQT(QticketInfo qTicket) {
        boolean sendSMSwhileQT = smsDBSettings.getSMSvalue(SMSgeneralDBSettings.SMS_QT_ID);
        boolean isFacilityEnable = smsDBSettings.isFacilityEnable(SMSgeneralDBSettings.SMS_QT_ID, smsDBSettings.getFacilityId(qTicket.getWarehouse()));
        if (sendSMSwhileQT && isFacilityEnable) {
            String smsString = smsDBSettings.getMessage(SMSgeneralDBSettings.SMS_QT_ID);
            createSMS(smsString, qTicket);
        }
    }

    public void createSMS(String smsString, QticketInfo qTicket) {
        String sms = smsString;
        smsString = smsString.replace(SMSgeneralDBSettings.SMS_BILL_KEY, qTicket.getId());
        smsString = smsString.replace(SMSgeneralDBSettings.SMS_DTM_KEY, qTicket.printDate());
        smsString = smsString.replace(SMSgeneralDBSettings.SMS_FACILITY_KEY, getFacilityName(qTicket.getWarehouse()));
        smsString = smsString.replace(SMSgeneralDBSettings.SMS_WHAREHOUSE_NAME_KEY, getRdisplayName(qTicket.getWarehouse()));
        String x = m_App.getAppUserView().getUser().getRole();
        smsString = smsString.replace(SMSgeneralDBSettings.SMS_ROLE_KEY, LookupUtilityImpl.getInstance(null).getRoleMap().get(x).toString());

        Logger.getLogger(JPanelTicket.class.getName()).log(Level.INFO, "SMS for QT is ON : Customer : " + qTicket.getCustomer().getId() + " and  mobile number is " + qTicket.getCustomer().getmobile());

        if (qTicket.getCustomer().getId().contains("Guest")) {
            String custID = smsDBSettings.getCustIdFromGuestID(qTicket.getCustomer());
            if (custID != null) {
                try {
                    CustomerInfoExt custInfo = dlSales.loadCustomerExt(custID);
                    if (custInfo != null) {
                        smsString = smsString.replace(SMSgeneralDBSettings.SMS_MEMBER_NAME_KEY, custInfo.getName());
                        smsString = smsString.replace(SMSgeneralDBSettings.SMS_MEMBER_NO_KEY, custInfo.getSearchkey());
                        if (custInfo.getmobile() != null && !custInfo.getmobile().isEmpty()) {
                            smsDBSettings.insertSMStoActiveMsgTable(smsString, custInfo.getmobile(), custInfo.getId());
                        }

                    }
                } catch (BasicException ex) {
                    Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            if (qTicket.getCustomer().getmobile() != null && !qTicket.getCustomer().getmobile().isEmpty()) {
                smsString = smsString.replace(SMSgeneralDBSettings.SMS_MEMBER_NAME_KEY, qTicket.getCustomer().getName());
                smsString = smsString.replace(SMSgeneralDBSettings.SMS_MEMBER_NO_KEY, qTicket.getCustomer().getSearchkey());
                smsDBSettings.insertSMStoActiveMsgTable(smsString, qTicket.getCustomer().getmobile(), qTicket.getCustomer().getId());
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.INFO, "SMS sent successfully : " + smsString);
            }
        }

    }

    public void printqt(String prcategory, QticketInfo qTicket) throws BasicException {
        String sresource = LookupUtilityImpl.getInstance(null).getDataLogicSystem().getResourceAsXML("Printer.QT");
        String waitername;
        String table1 = qTicket.getPlace();
        try {
            AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();
            Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT NAME FROM WAITER WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(qTicket.getWaiter());

            if (obj == null) {
                waitername = "";
            } else {
                waitername = obj[0].toString();
            }
            Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT NAME FROM PLACES WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(qTicket.getPlace());

            if (obj1 == null) {
                table1 = "";
            } else {
                table1 = obj1[0].toString();
            }
            boolean flag = m_App.getAppUserView().getUser().hasPermission("bar counter");
            String namedel = m_App.getAppUserView().getUser().getName();
            int flag1 = 0;
            if (m_App.getAppUserView().getUser().hasPermission("bar counter")) {
                flag1 = 1;
            }
            if (m_App.getAppUserView().getUser().hasPermission("rest counter")) {
                flag1 = 2;
            }
            if (m_App.getAppUserView().getUser().hasPermission("chat counter")) {
                flag1 = 3;
            }

            String Displayname = "";
            Displayname = getRdisplayName(qTicket.getWarehouse());

            // script.put("flag", flag);
            ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
            script.put("waiter", waitername);
            script.put("place", table1);
            script.put("flag", 4);
            script.put("Displayname", Displayname);
            script.put("ticket", qTicket);
            script.put("printer", LookupUtilityImpl.getInstance(null).getPRCategoriesMap().get(prcategory).getPrinter());
            m_TTP.printTicket(script.eval(sresource).toString());
        } catch (ScriptException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
            msg.show(this);
            throw new BasicException(e.getMessage());
        } catch (TicketPrinterException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
            msg.show(this);
            throw new BasicException(e.getMessage());
        } catch (Exception e) {
            throw new BasicException(e.getMessage());
        }
    }

    private String getRdisplayName(String wareHouse) {
        try {
            Object[] obj = (Object[]) new StaticSentence(m_App.getSession(),
                    "select rdisplayname from locations where id=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING}))
                    .find(wareHouse);
            if (obj == null) {
                return "";
            } else {
                return obj[0].toString();
            }
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String getFacilityName(String wareHouse) {
        try {
            Object[] obj = (Object[]) new StaticSentence(m_App.getSession(),
                    "SELECT NAME FROM FACILITY WHERE ID = (SELECT FACILITY FROM LOCATIONS WHERE ID = ? )",
                    SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING}))
                    .find(wareHouse);
            if (obj == null) {
                return "";
            } else {
                return obj[0].toString();
            }
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean closeTicket(TicketInfo ticket, Object ticketext) {

        boolean resultok = false;

        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {

            // reset the payment info
            taxeslogic.calculateTaxes(ticket);
            ticket.resetPayments();

            if (executeEvent(ticket, ticketext, "ticket.total") == null) {

                // Muestro el total
                printTicket("Printer.TicketTotal", ticket, ticketext);

                // Select the Payments information
                JPaymentSelect paymentdialog = ticket.getTotal() >= 0.0
                        ? paymentdialogreceipt
                        : paymentdialogrefund;
                paymentdialog.setPrintSelected("true".equals(m_jbtnconfig.getProperty("printselected", "true")));

                if (paymentdialog.showDialog(ticket.getTotal(), ticket.getCustomer(), m_App.getAppUserView().getUser().getName(), true)) {

                    // assign the payments selected and calculate taxes.                    
                    ticket.setPayments(paymentdialog.getSelectedPayments());

                    // Asigno los valores definitivos del ticket...
                    ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                    ticket.setActiveCash(m_App.getActiveCashIndex());
                    ticket.setDate(new Date()); // Le pongo la fecha de cobro

                    if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                        // Save the receipt and assign a receipt number
                        try {
                            dlSales.saveTicket(ticket, m_App.getInventoryLocation());
                        } catch (Exception eData) {
                            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
                            msg.show(this);
                        }

                        executeEvent(ticket, ticketext, "ticket.close");

                        // Print receipt.
                        printTicket(paymentdialog.isPrintSelected()
                                ? "Printer.Ticket"
                                : "Printer.Ticket2", ticket, ticketext);
                        resultok = true;
                    }
                }
            }

            // reset the payment info
            m_oTicket.resetTaxes();
            m_oTicket.resetPayments();
        }

        // cancelled the ticket.total script
        // or canceled the payment dialog
        // or canceled the ticket.close script
        return resultok;
    }

    private void printTicket(String sresourcename, TicketInfo ticket, Object ticketext) {

        String sresource = dlSystem.getResourceAsXML(sresourcename);
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JPanelTicket.this);
        } else {
            try {
                boolean flag = m_App.getAppUserView().getUser().hasPermission("Bar Counter");
                Object[] obj2 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT prcategories from people  WHERE ID=?", SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE).find(m_App.getAppUserView().getUser().getId());
                String[] str = null;
                if (obj2[0] != null) {
                    str = obj2[0].toString().split("#");
                }
                Object[] obj3 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT RDISPLAYNAME FROM LOCATIONS WHERE ID=?", SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE).find(str[0]);
                String name = null;
                if (obj3 != null && obj3[0] != null) {
                    name = obj3[0].toString();
                }
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
                script.put("flag", flag);
                script.put("ticket", ticket);
                script.put("place", ticketext);
                script.put("eoe", StringUtils.encodeXML("E&OE"));
                script.put("displayName", name);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (BasicException ex) {
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JPanelTicket.this);
            }
        }
    }

    private void printReport(String resourcefile, TicketInfo ticket, Object ticketext) {

        try {

            JasperReport jr;

            InputStream in = getClass().getResourceAsStream(resourcefile + ".ser");
            if (in == null) {
                // read and compile the report
                JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(resourcefile + ".jrxml"));
                jr = JasperCompileManager.compileReport(jd);
            } else {
                // read the compiled reporte
                ObjectInputStream oin = new ObjectInputStream(in);
                jr = (JasperReport) oin.readObject();
                oin.close();
            }

            // Construyo el mapa de los parametros.
            Map reportparams = new HashMap();
            // reportparams.put("ARG", params);
            try {
                reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(resourcefile + ".properties"));
            } catch (MissingResourceException e) {
            }
            reportparams.put("TAXESLOGIC", taxeslogic);

            Map reportfields = new HashMap();
            reportfields.put("TICKET", ticket);
            reportfields.put("PLACE", ticketext);

            JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, new JRMapArrayDataSource(new Object[]{reportfields}));

            PrintService service = ReportUtils.getPrintService(m_App.getProperties().getProperty("machine.printername"));

            JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);

        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
            msg.show(this);
        }
    }

    private void visorTicketLine(TicketLineInfo oLine) {
        if (oLine == null) {
            m_App.getDeviceTicket().getDeviceDisplay().clearVisor();
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticketline", oLine);
                m_TTP.printTicket(script.eval(dlSystem.getResourceAsXML("Printer.TicketLine")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JPanelTicket.this);
            }
        }
    }

    private Object evalScript(ScriptObject scr, String code, ScriptArg... args) {

        try {
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            return scr.evalScript(code, args);
        } catch (ScriptException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"), e);
            msg.show(this);
            return msg;
        }
    }

    public void evalScriptAndRefresh(String code, ScriptArg... args) throws BasicException {

        if (code == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"));
            msg.show(this);
        } else {
            ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            evalScript(scr, code, args);
            refreshTicket();
            setSelectedIndex(scr.getSelectedIndex());
        }
    }

    private Object executeEventAndRefresh(String eventkey, ScriptArg... args) throws BasicException {

        String code = m_jbtnconfig.getEvent(eventkey);
        if (code == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            Object result = evalScript(scr, code, args);
            refreshTicket();
            setSelectedIndex(scr.getSelectedIndex());
            return result;
        }
    }

    private Object executeEvent(TicketInfo ticket, Object ticketext, String eventkey, ScriptArg... args) {

        String code = m_jbtnconfig.getEvent(eventkey);
        if (code == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(ticket, ticketext);
            return evalScript(scr, m_jbtnconfig.getEvent(eventkey), args);
        }
    }

    public String getResourceAsXML(String sresourcename) {
        return dlSystem.getResourceAsXML(sresourcename);
    }

    public BufferedImage getResourceAsImage(String sresourcename) {
        return dlSystem.getResourceAsImage(sresourcename);
    }

    private void setSelectedIndex(int i) {

        if (i >= 0 && i < m_oTicket.getLinesCount()) {
            m_ticketlines.setSelectedIndex(i);
        } else if (m_oTicket.getLinesCount() > 0) {
            m_ticketlines.setSelectedIndex(m_oTicket.getLinesCount() - 1);
        }
    }

    public static class ScriptArg {

        private String key;
        private Object value;

        public ScriptArg(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }

    public class ScriptObject {

        private TicketInfo ticket;
        private Object ticketext;
        private int selectedindex;

        private ScriptObject(TicketInfo ticket, Object ticketext) {
            this.ticket = ticket;
            this.ticketext = ticketext;
        }

        public double getInputValue() {
            if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
                return JPanelTicket.this.getInputValue();
            } else {
                return 0.0;
            }
        }

        public int getSelectedIndex() {
            return selectedindex;
        }

        public void setSelectedIndex(int i) {
            selectedindex = i;
        }

        public void printReport(String resourcefile) {
            JPanelTicket.this.printReport(resourcefile, ticket, ticketext);
        }

        public void printTicket(String sresourcename) {
            JPanelTicket.this.printTicket(sresourcename, ticket, ticketext);
        }

        public Object evalScript(String code, ScriptArg... args) throws ScriptException {

            ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
            script.put("ticket", ticket);
            script.put("place", ticketext);
            script.put("taxes", taxcollection);
            script.put("taxeslogic", taxeslogic);
            script.put("user", m_App.getAppUserView().getUser());
            script.put("sales", this);

            // more arguments
            for (ScriptArg arg : args) {
                script.put(arg.getKey(), arg.getValue());
            }

            return script.eval(code);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        m_jPanContainer = new javax.swing.JPanel();
        m_jOptions = new javax.swing.JPanel();
        m_jButtons = new javax.swing.JPanel();
        m_jTicketId = new javax.swing.JLabel();
        btnSplit = new javax.swing.JButton();
        m_prTicket = new javax.swing.JButton();
        m_jPanelScripts = new javax.swing.JPanel();
        m_jButtonsExt = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        m_jbtnScale = new javax.swing.JButton();
        m_jPanelBag = new javax.swing.JPanel();
        m_jPanTicket = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jUp = new javax.swing.JButton();
        m_jDown = new javax.swing.JButton();
        m_jDelete = new javax.swing.JButton();
        m_jList = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        m_jEditLine = new javax.swing.JButton();
        m_jPanelCentral = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jPanTotals = new javax.swing.JPanel();
        m_jTotalEuros = new javax.swing.JLabel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        m_jSubtotalEuros = new javax.swing.JLabel();
        m_jTaxesEuros = new javax.swing.JLabel();
        m_jLblTotalEuros2 = new javax.swing.JLabel();
        m_jLblTotalEuros3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        m_jContEntries = new javax.swing.JPanel();
        m_jPanEntries = new javax.swing.JPanel();
        m_jNumberKeys = new com.openbravo.beans.JNumberKeys();
        jPanel9 = new javax.swing.JPanel();
        m_jPrice = new javax.swing.JLabel();
        m_jPor = new javax.swing.JLabel();
        m_jEnter = new javax.swing.JButton();
        m_jTax = new javax.swing.JComboBox();
        m_jaddtax = new javax.swing.JToggleButton();
        m_jKeyFactory = new javax.swing.JTextField();
        catcontainer = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 204, 153));
        setLayout(new java.awt.CardLayout());

        m_jPanContainer.setLayout(new java.awt.BorderLayout());

        m_jOptions.setLayout(new java.awt.BorderLayout());

        m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_jTicketId.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTicketId.setOpaque(true);
        m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
        m_jTicketId.setRequestFocusEnabled(false);
        m_jButtons.add(m_jTicketId);

        btnSplit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editcut.png"))); // NOI18N
        btnSplit.setToolTipText("QT Split");
        btnSplit.setFocusPainted(false);
        btnSplit.setFocusable(false);
        btnSplit.setMargin(new java.awt.Insets(8, 14, 8, 14));
        btnSplit.setRequestFocusEnabled(false);
        btnSplit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSplitActionPerformed(evt);
            }
        });
        m_jButtons.add(btnSplit);

        m_prTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/tick1.PNG"))); // NOI18N
        m_prTicket.setToolTipText("QT list");
        m_prTicket.setFocusPainted(false);
        m_prTicket.setFocusable(false);
        m_prTicket.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_prTicket.setRequestFocusEnabled(false);
        m_prTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_prTicketActionPerformed(evt);
            }
        });
        m_jButtons.add(m_prTicket);

        m_jOptions.add(m_jButtons, java.awt.BorderLayout.LINE_START);

        m_jPanelScripts.setLayout(new java.awt.BorderLayout());

        m_jButtonsExt.setLayout(new javax.swing.BoxLayout(m_jButtonsExt, javax.swing.BoxLayout.LINE_AXIS));

        m_jbtnScale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/ark216.png"))); // NOI18N
        m_jbtnScale.setText(AppLocal.getIntString("button.scale")); // NOI18N
        m_jbtnScale.setFocusPainted(false);
        m_jbtnScale.setFocusable(false);
        m_jbtnScale.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnScale.setRequestFocusEnabled(false);
        m_jbtnScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnScaleActionPerformed(evt);
            }
        });
        jPanel1.add(m_jbtnScale);

        m_jButtonsExt.add(jPanel1);

        m_jPanelScripts.add(m_jButtonsExt, java.awt.BorderLayout.LINE_END);

        m_jOptions.add(m_jPanelScripts, java.awt.BorderLayout.LINE_END);

        m_jPanelBag.setLayout(new java.awt.BorderLayout());
        m_jOptions.add(m_jPanelBag, java.awt.BorderLayout.CENTER);

        m_jPanContainer.add(m_jOptions, java.awt.BorderLayout.NORTH);

        m_jPanTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jPanTicket.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel2.setLayout(new java.awt.GridLayout(0, 1, 5, 5));

        m_jUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1uparrow22.png"))); // NOI18N
        m_jUp.setFocusPainted(false);
        m_jUp.setFocusable(false);
        m_jUp.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jUp.setRequestFocusEnabled(false);
        m_jUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jUpActionPerformed(evt);
            }
        });
        jPanel2.add(m_jUp);

        m_jDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1downarrow22.png"))); // NOI18N
        m_jDown.setFocusPainted(false);
        m_jDown.setFocusable(false);
        m_jDown.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDown.setRequestFocusEnabled(false);
        m_jDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDownActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDown);

        m_jDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
        m_jDelete.setToolTipText("Remove");
        m_jDelete.setFocusPainted(false);
        m_jDelete.setFocusable(false);
        m_jDelete.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDelete.setRequestFocusEnabled(false);
        m_jDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDelete);

        m_jList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search22.png"))); // NOI18N
        m_jList.setToolTipText("Find Product");
        m_jList.setFocusPainted(false);
        m_jList.setFocusable(false);
        m_jList.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jList.setRequestFocusEnabled(false);
        m_jList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jListActionPerformed(evt);
            }
        });
        jPanel2.add(m_jList);

        jButton1.setText("QT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line.png"))); // NOI18N
        m_jEditLine.setToolTipText("Remark");
        m_jEditLine.setFocusPainted(false);
        m_jEditLine.setFocusable(false);
        m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine.setRequestFocusEnabled(false);
        m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLineActionPerformed(evt);
            }
        });
        jPanel2.add(m_jEditLine);

        jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

        m_jPanTicket.add(jPanel5, java.awt.BorderLayout.LINE_END);

        m_jPanelCentral.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);

        m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N

        m_jSubtotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jSubtotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros.setOpaque(true);
        m_jSubtotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jSubtotalEuros.setRequestFocusEnabled(false);

        m_jTaxesEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTaxesEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros.setOpaque(true);
        m_jTaxesEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTaxesEuros.setRequestFocusEnabled(false);

        m_jLblTotalEuros2.setText(AppLocal.getIntString("label.taxcash")); // NOI18N

        m_jLblTotalEuros3.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N

        jLabel1.setText("Curr.Op.Bal");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout m_jPanTotalsLayout = new org.jdesktop.layout.GroupLayout(m_jPanTotals);
        m_jPanTotals.setLayout(m_jPanTotalsLayout);
        m_jPanTotalsLayout.setHorizontalGroup(
            m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanTotalsLayout.createSequentialGroup()
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(m_jLblTotalEuros2)
                        .add(5, 5, 5)
                        .add(m_jTaxesEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(36, 36, 36)
                        .add(m_jLblTotalEuros3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(m_jSubtotalEuros, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(42, 42, 42)
                        .add(m_jLblTotalEuros1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 8, Short.MAX_VALUE)
                        .add(m_jTotalEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        m_jPanTotalsLayout.setVerticalGroup(
            m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanTotalsLayout.createSequentialGroup()
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jSubtotalEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jLblTotalEuros2)
                            .add(m_jTaxesEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(m_jLblTotalEuros3)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel1)
                        .add(m_jLblTotalEuros1)
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jTotalEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.add(m_jPanTotals, java.awt.BorderLayout.LINE_END);

        m_jPanelCentral.add(jPanel4, java.awt.BorderLayout.SOUTH);

        m_jPanTicket.add(m_jPanelCentral, java.awt.BorderLayout.CENTER);

        m_jPanContainer.add(m_jPanTicket, java.awt.BorderLayout.CENTER);

        m_jContEntries.setLayout(new java.awt.BorderLayout());

        m_jPanEntries.setLayout(new javax.swing.BoxLayout(m_jPanEntries, javax.swing.BoxLayout.Y_AXIS));

        m_jNumberKeys.addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {
            public void keyPerformed(com.openbravo.beans.JNumberEvent evt) {
                m_jNumberKeysKeyPerformed(evt);
            }
        });
        m_jPanEntries.add(m_jNumberKeys);

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        m_jPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPrice.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPrice.setOpaque(true);
        m_jPrice.setPreferredSize(new java.awt.Dimension(100, 22));
        m_jPrice.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(m_jPrice, gridBagConstraints);

        m_jPor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPor.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPor.setOpaque(true);
        m_jPor.setPreferredSize(new java.awt.Dimension(22, 22));
        m_jPor.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel9.add(m_jPor, gridBagConstraints);

        m_jEnter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/barcode.png"))); // NOI18N
        m_jEnter.setFocusPainted(false);
        m_jEnter.setFocusable(false);
        m_jEnter.setRequestFocusEnabled(false);
        m_jEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEnterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel9.add(m_jEnter, gridBagConstraints);

        m_jTax.setFocusable(false);
        m_jTax.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel9.add(m_jTax, gridBagConstraints);

        m_jaddtax.setText("+");
        m_jaddtax.setFocusPainted(false);
        m_jaddtax.setFocusable(false);
        m_jaddtax.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel9.add(m_jaddtax, gridBagConstraints);

        m_jPanEntries.add(jPanel9);

        m_jKeyFactory.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setForeground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setBorder(null);
        m_jKeyFactory.setCaretColor(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setPreferredSize(new java.awt.Dimension(1, 1));
        m_jKeyFactory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_jKeyFactoryKeyTyped(evt);
            }
        });
        m_jPanEntries.add(m_jKeyFactory);

        m_jContEntries.add(m_jPanEntries, java.awt.BorderLayout.NORTH);

        m_jPanContainer.add(m_jContEntries, java.awt.BorderLayout.LINE_END);

        catcontainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        catcontainer.setLayout(new java.awt.BorderLayout());
        m_jPanContainer.add(catcontainer, java.awt.BorderLayout.SOUTH);

        add(m_jPanContainer, "ticket");
    }// </editor-fold>//GEN-END:initComponents

    private void m_jbtnScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnScaleActionPerformed
        try {
            stateTransition('\u00a7');
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jbtnScaleActionPerformed

    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed

        int i = m_ticketlines.getSelectedIndex();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            TicketLineInfo oLine = m_oTicket.getLine(i);
            if (JProductLineRemarkEdit.showMessage(this, m_App, m_oTicket.getLine(i))) {
                try {
                    // se ha modificado la linea
                    paintTicketLine(i, oLine);
                } catch (BasicException ex) {
                    Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }//GEN-LAST:event_m_jEditLineActionPerformed

    private void m_jEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEnterActionPerformed
        try {
            stateTransition('\n');
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jEnterActionPerformed

    private void m_jNumberKeysKeyPerformed(com.openbravo.beans.JNumberEvent evt) {//GEN-FIRST:event_m_jNumberKeysKeyPerformed
        try {
            stateTransition(evt.getKey());
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jNumberKeysKeyPerformed

    private void m_jKeyFactoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jKeyFactoryKeyTyped

        m_jKeyFactory.setText(null);
        try {
            stateTransition(evt.getKeyChar());
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jKeyFactoryKeyTyped

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed

        int i = m_ticketlines.getSelectedIndex();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            try {
                removeTicketLine(i); // elimino la linea
            } catch (BasicException ex) {
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_m_jDeleteActionPerformed

    private void m_jUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jUpActionPerformed

        m_ticketlines.selectionUp();

    }//GEN-LAST:event_m_jUpActionPerformed

    private void m_jDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDownActionPerformed

        m_ticketlines.selectionDown();

    }//GEN-LAST:event_m_jDownActionPerformed

    private void m_jListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jListActionPerformed

        ProductInfoExt prod = JProductFinder.showMessage(JPanelTicket.this, dlSales);
        if (prod != null) {

            buttonTransition(prod);

        }

    }//GEN-LAST:event_m_jListActionPerformed

    private void btnSplitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSplitActionPerformed
        try {
            boolean berror = false;
            if (m_oTicket.getLinesCount() > 0) {
                ReceiptSplit splitdialog = ReceiptSplit.getDialog(this, dlSystem.getResourceAsXML("Ticket.Line"), dlSales, dlCustomers, taxeslogic);

                TicketInfo ticket1 = m_oTicket.copyTicket();
                TicketInfo ticket2 = new TicketInfo();
                ticket2.setCustomer(m_oTicket.getCustomer());
                ticket2.setUser(ticket1.getUser());
                ticket2.setPlace(ticket1.getPlace());
                ticket2.setWaiter(ticket1.getWaiter());
                ticket2.setFloor(ticket1.getFloor());

                if (splitdialog.showDialog(ticket1, ticket2, m_oTicketExt)) {

                    Date date = new Date();
                    AppUser user = LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser();
                    Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT OPENSALE FROM PEOPLE WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.TIMESTAMP})).find(user.getId());
                    if (obj != null) {
                        if (obj[0] != null) {
                            Date d = (Date) obj[0];
                            Calendar cal1 = Calendar.getInstance();
                            Calendar cal2 = Calendar.getInstance();
                            cal1.setTimeInMillis(date.getTime());
                            cal2.setTimeInMillis(d.getTime());
                            if (cal1.before(cal2)) {
                                if (JOptionPane.showConfirmDialog(null, "Present Time is less than Open sale Time.Previous Open sale Time is " + d + " .Do you want to override the open sale time ?", "Error-System Time was reset", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                                    berror = true;
                                } else {
                                    new PreparedSentence(m_App.getSession(), "UPDATE PEOPLE SET OPENSALE=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.STRING})).exec(new Object[]{date, user.getId()});
                                }
                            }
                        }
                    }
                    if (berror == false) {
                        int ticketsize = ticket2.getLines().size();
                        int j = 0;
                        // Boolean temp=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission("StockCheckNotRequired");

                        // if(!temp)
                        while (j < ticket2.getLines().size()) {
                            TicketLineInfo tl = ticket2.getLine(j);

                            System.out.println(">>>>>>>>>>>>>>>>>tl.isStockCheckRequired() >>>>>>>>>>>>>>>>>>>>>>" + tl.isStockCheckRequired());
                            Object stockcheck = new PreparedSentence(m_App.getSession(), "SELECT INVENTRYMAINTAIN FROM PRODUCTS WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.BOOLEAN})).find(tl.getProductID().toString());
                            System.out.println(">>>>>>>>>>>>>>>>>tl.isStockCheckRequired() >>>>>>>>>>>>>>>>>>>>>>" + Boolean.valueOf(stockcheck.toString()));

                            if (tl.isStockCheckRequired()) {
                                // if(tl.getProductCategoryID()){
                                Object o = dlSales.getStockVolume(tl.getProductID());
                                Double sqty = 0.0;
                                if (o != null) {
                                    sqty = Double.parseDouble(o.toString());
                                }
                                if (sqty > 0) {
                                    dlSales.updateStockVolume(-tl.getMultiply(), tl.getProductID());
                                    dlSales.updateStockVolume1(-tl.getMultiply(), tl.getProductID());
                                    j++;
                                } else {
                                    JOptionPane.showMessageDialog(this, "\"" + tl.getProductName() + " \" is Empty.Cannot prepare QT for it", "Stock Empty", JOptionPane.WARNING_MESSAGE);

                                    ticket2.deleteLine(j);
                                }
                            }
                            j++;
                            //   }

                        }
                        createQTicket(ticket2);
//                if (closeTicket(ticket2, m_oTicketExt)) { // already checked  that number of lines > 0
                        setActiveTicket(ticket1, m_oTicketExt);// set result ticket
                        dlReceipts.updateSharedTicket(ticket1.getCustomerId(), ticket1, m_App.getAppUserView().getUser().getRole());
                        //  dlReceipts.updateLastQtTimeOfSharedTicket(new Date(),ticket1.getCustomerId(), m_App.getAppUserView().getUser().getRole());
                    } else {
                        // if(berror==true){
                        JOptionPane.showMessageDialog(this, "Please reset the system time or consult your system admin", "Sorry Cannot Create QT", JOptionPane.OK_OPTION);
                        // }
                    }
                }
                //praveen:exit for every transaction for kiosk mode---start
                if (m_App.getAppUserView().getUser().getTypeOfUser() == 1) {
                    JPrincipalApp.m_approot.closeAppView();
                }
                //praveen:exit for every transaction for kiosk mode---end

            }
        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotcreateqt"), e);
            msg.show(this);
        }
}//GEN-LAST:event_btnSplitActionPerformed

    private void m_prTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_prTicketActionPerformed
        //has to open to a small dialog that lists all current qts of that customer
        QTList qtList = QTList.getDialog(this, m_App, dlSales, qTicket);
        try {
            boolean flag = qtList.showDialog(m_oTicket.getCustomer());
        } catch (BasicException ex) {
            new MessageInf(ex).show(this);
        }

}//GEN-LAST:event_m_prTicketActionPerformed
    Double AmtNotinStck = 0.00;
    Double DiscountAmt = 0.00;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        AmtNotinStck = 0.00;

        try {
            Transaction t = new Transaction(m_App.getSession()) {

                @Override
                protected Object transact() throws BasicException {

                    int x = 0;
                    Object[] stk = new Object[25];

                    boolean berror = false;
                    Date date = new Date();
                    AppUser user = LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser();
                    Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT OPENSALE FROM PEOPLE WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.TIMESTAMP})).find(user.getId());
                    if (obj != null) {
                        if (obj[0] != null) {
                            Date d = (Date) obj[0];
                            Calendar cal1 = Calendar.getInstance();
                            Calendar cal2 = Calendar.getInstance();
                            cal1.setTimeInMillis(date.getTime());
                            cal2.setTimeInMillis(d.getTime());
                            if (cal1.before(cal2)) {
                                if (JOptionPane.showConfirmDialog(null, "Present Time is less than Open sale Time.Previous Open sale Time is " + d + " .Do you want to override the open sale time ?", "Error-System Time was reset", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                                    berror = true;

                                } else {
                                    new PreparedSentence(m_App.getSession(), "UPDATE PEOPLE SET OPENSALE=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.STRING})).exec(new Object[]{date, user.getId()});
                                }
                            }
                        }
                    }
                    if (berror == false && m_oTicket.getLinesCount() > 0) {
                        // temp=1;
                        TicketInfo ticket1 = m_oTicket.copyTicket();
                        TicketInfo ticket2 = new TicketInfo();

                        ticket2.setCustomer(m_oTicket.getCustomer());
                        ticket2.setUser(ticket1.getUser());
                        ticket2.setPlace(ticket1.getPlace());
                        ticket2.setWaiter(ticket1.getWaiter());
                        ticket2.setFloor(ticket1.getFloor());

                        int count = m_oTicket.getLinesCount();
                        int j = 0;
                        int size = ticket1.getLines().size();
                        // Boolean temp=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().hasPermission("StockCheckNotRequired");

                        // if(!temp)
                        // edited by akash ................ 
                        DiscountAmt = 0.00;
                        String Taxcat2 = null;
                        String Taxcat3 = null;
                        while (j < size) {
                            TicketLineInfo tl = ticket1.getLine(j);
//                    System.out.println(">>>>>>>>>>>>>>>>>tl.isStockCheckRequired() >>>>>>>>>>>>>>>>>>>>>>" + tl.isStockCheckRequired());
//                    Object stockcheck = new PreparedSentence(m_App.getSession(), "SELECT INVENTRYMAINTAIN FROM PRODUCTS WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.BOOLEAN})).find(tl.getProductID().toString());
//                    System.out.println(">>>>>>>>>>>>>>>>>tl.isStockCheckRequired() >>>>>>>>>>>>>>>>>>>>>>" + Boolean.valueOf(stockcheck.toString()));

                            if (tl.isStockCheckRequired()) {
                                Object o = dlSales.getStockVolume(tl.getProductID());
                                stk[j] = o;
                                Double sqty = 0.0;
                                if (o != null) {
                                    sqty = Double.parseDouble(o.toString());
                                }
                                if (sqty >= tl.getMultiply()) {
                                    dlSales.updateStockVolume(-tl.getMultiply(), tl.getProductID());
                                    dlSales.updateStockVolume1(-tl.getMultiply(), tl.getProductID());
                                    j++;
                                } else {

                                    if (sqty == 0.0) {
                                        //						x=1;
                                        AmtNotinStck = AmtNotinStck + ticket1.getSubTotal() + ticket1.getTax();
                                        System.out.println("Amount of prod not in stock : " + AmtNotinStck);
                                        JOptionPane.showMessageDialog(null, "\"" + tl.getProductName() + " \" is Empty.Cannot prepare QT for it", "Stock Empty", JOptionPane.WARNING_MESSAGE);

                                        Double CurrTaxRate = 0.00;
                                        Double TotalTaxAmount = 0.00;
                                        String Taxcat = tl.getProductTaxCategoryID();
                                        CurrTaxRate = CurrTaxRate + getTaxrateByCategory(Taxcat);
                                        if (tl.getProductTaxCategoryID2() != null && tl.getProductTaxCategoryID2() != "") {
                                            Taxcat2 = tl.getProductTaxCategoryID2();
                                            CurrTaxRate = CurrTaxRate + getTaxrateByCategory(Taxcat2);
                                        }
                                        if (tl.getProductTaxCategoryID3() != null && tl.getProductTaxCategoryID3() != "") {
                                            Taxcat3 = tl.getProductTaxCategoryID3();
                                            CurrTaxRate = CurrTaxRate + getTaxrateByCategory(Taxcat3);
                                        }
                                        TotalTaxAmount = tl.getSubValue() * CurrTaxRate;
                                        DiscountAmt = DiscountAmt + tl.getSubValue() + TotalTaxAmount;

                                        System.out.println("Tax category id : " + tl.getProductTaxCategoryID2());

                                    } else {
                                        AmtNotinStck = AmtNotinStck + ticket1.getSubTotal() + ticket1.getTax();
                                        System.out.println("Amount of prod not in stock : " + AmtNotinStck);
                                        JOptionPane.showMessageDialog(null, "QT quantity Exceed the quantity in stock for \"" + tl.getProductName() + " \"", "Cannot Prepare QT", JOptionPane.WARNING_MESSAGE);
                                        DiscountAmt = DiscountAmt + tl.getSubValue() + tl.getTax();
                                        System.out.println("Tax category id : " + tl.getTaxRate());
                                    }
                                    ticket1.deleteLine(j);

                                    //j--;
                                    for (int i = j; i < size; i++) {
                                        stk[i] = stk[i++];
                                    }

                                    size--;

                                }
                            } else {
                                j++;
                            }
                        }

////////aaa..Start
                        //           if(x==0){  
                        System.out.println("Discounted Amount : " + DiscountAmt);

                        Double amt = 0.0;
                        //////////////////////////////////////shiv
                        System.out.println(ticket1.getTax());

                        Object[] obj4 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT RATEROUNDOFF FROM TAXES WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(name);
                        if (obj4[0].equals("yes")) {
                            ticket1.getTax();
                            Object f = new Float(Math.round(ticket1.getTax()));
                            String st = f.toString();
                            Double taxst = Double.parseDouble(st);
                            amt = ticket1.getSubTotal() + taxst;
                            System.out.println(taxst);

                        } else if (obj4[0].equals("yesnearest")) {
                            ticket1.getTax();
                            System.out.println(ticket1.getTax());
                            Object f = new Float(Math.round(ticket1.getTax()));
                            String st = f.toString();
                            Double taxst = Double.parseDouble(st);
                            amt = ticket1.getSubTotal() + taxst;
                            System.out.println(taxst);
                        } else if (obj4[0].equals("yesnext")) {
                            ticket1.getTax();
                            System.out.println(ticket1.getTax());
                            Object f = new Float(Math.round(ticket1.getTax()) + 1);
                            String st = f.toString();
                            Double taxst = Double.parseDouble(st);
                            amt = ticket1.getSubTotal() + taxst;
                            System.out.println(taxst);
                        } else if (obj4[0].equals("yesprevious")) {
                            ticket1.getTax();
                            System.out.println(ticket1.getTax());
                            Object f = new Float(Math.round(ticket1.getTax()) - 1);
                            String st = f.toString();
                            Double taxst = Double.parseDouble(st);
                            amt = ticket1.getSubTotal() + taxst;
                            System.out.println(taxst);
                        } else {

                            amt = ticket1.getTotal();

                        }

                        Object od = (Formats.CURRENCY.parseValue(m_jTotalEuros.getText()));

                        amt = Double.parseDouble(od.toString());
                        System.out.println("Amount Before Discount : " + amt);

                        amt = amt - DiscountAmt;               //// Edited By Akash

                        System.out.println("Amount After Discount : " + amt);

                        String cust = m_oTicket.getCustomerId();
                        String id = cust;

                        Object[] objm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT MEMTYPE FROM CUSTOMERS where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id.toString());
                        //    String memtype = objm[0].toString();

                        if (objm == null) {
                            String a = m_oTicket.getCustomerId();
                            String a1 = a.substring(0, 36);
                            String gname = "Credit check for Guests";
                            Object[] gobjm = (Object[]) new StaticSentence(m_App.getSession(), "SELECT MEMTYPE FROM CUSTOMERS where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(a1.toString());
                            String memtype = gobjm[0].toString();

                            Object[] gobj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(id.toString());
                            String gopb = gobj1[0].toString();
                            Double GOPB = new Double(gopb);

                            roundTwoDecimals(GOPB);

                            Double GSum = GOPB + amt;
                            // added by shweta
                            Object[] gobj2 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT DEBTMAX FROM memtype where id=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(memtype);
                            String dMax = gobj2[0].toString();
                            Double debtMax = new Double(dMax);

                            /* Object[] objstotal1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT FROMDATE,TODATE,ADDAMOUNT FROM QTCR_LIMITAUTH WHERE CUSTOMER=? AND ACTIVE IS TRUE",
                                        new SerializerWriteBasic(new Datas[]{Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.DOUBLE})).find(new Object[]{id});
               
                              Date fromdate=new Date();
                               Date todate=new Date();
                             Date currentdate=new Date();
                                Double addmnt1=0.0;
                                if(objstotal1 !=null){
                                 fromdate     =(Date) objstotal1[0]; 
                                 todate       =(Date) objstotal1[1];
                                   addmnt1       =(double) objstotal1[2];
                                }
                                   if(currentdate.after(fromdate) && currentdate.before(todate)){
                                       debtMax=debtMax+addmnt1;
                                       
                                   if(GSum>debtMax){
                                       GSum=GSum-addmnt1;
                                   
                        
                                  }
                          
                     else if(GSum<debtMax){
                       GSum=GSum+addmnt1;
                        
                            }
                    else{
                         GSum=GSum+addmnt1;                          
                     }
                                   }        */
                            GSum = roundTwoDecimals(GSum);

                            Object[] gobjqt = (Object[]) new StaticSentence(m_App.getSession(), "SELECT VALUE FROM generaltable where NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.BOOLEAN})).find(gname);
                            Boolean GQTcheck = (Boolean) gobjqt[0];

                            if (GQTcheck == true) {///Start of check

                                //Object[] gobj2 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT DEBTMAX FROM memtype where id=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(memtype);
                                //   String dMax = gobj2[0].toString();
                                //   Double debtMax = new Double(dMax);
                                if ((GSum > debtMax)) {

                                    //make a method for msg
                                    //to overcome the products reduction
                                    int s = 0;
                                    while (s < size) {
                                        TicketLineInfo tl = ticket1.getLine(s);
                                        if (tl.isStockCheckRequired()) {
                                            //    Object o = dlSales.getStockVolume(tl.getProductID());
                                            Double sqty = 0.0;
                                            if (stk[s] != null) {
                                                sqty = Double.parseDouble(stk[s].toString());
                                            }
                                            if (sqty >= tl.getMultiply()) {
                                                dlSales.updateStockVolume(tl.getMultiply(), tl.getProductID());
                                                dlSales.updateStockVolume1(tl.getMultiply(), tl.getProductID());
                                                s++;
                                            }
                                        } else {
                                            s++;
                                        }

                                    }//end of while
                                    JOptionPane.showMessageDialog(null, "Please clear the Balance.Cannot prepare QT for it", "Amount limit Exceeded", JOptionPane.WARNING_MESSAGE);

                                } else {
                                    amountUpdate(GSum, cust, ticket1, ticket2);
                                }
                            } else {
                                amountUpdate(GSum, cust, ticket1, ticket2);
                            }
                        } else {

                            String memtype = objm[0].toString();
                            Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT CAST((CURROPB)AS DECIMAL(10,2)) FROM billingmember where ID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(id.toString());
                            String opb = obj1[0].toString();
                            Double OPB = new Double(opb);
                            roundTwoDecimals(OPB);
                            Double Sum = OPB + amt;
                            //addded by shweta
                            Object[] obj2 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT DEBTMAX FROM memtype where id=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(memtype);
                            String dMax = obj2[0].toString();
                            Double debtMax = new Double(dMax);
                            /*       Object[] objstotal1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT FROMDATE,TODATE,ADDAMOUNT FROM QTCR_LIMITAUTH WHERE CUSTOMER=? AND ACTIVE IS TRUE",
                                        new SerializerWriteBasic(new Datas[]{Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.DOUBLE})).find(new Object[]{id});
               
                              Date fromdate=new Date();
                               Date todate=new Date();
                             Date currentdate=new Date();
                                Double addmnt1=0.0;
                                if(objstotal1 !=null){
                                 fromdate     =(Date) objstotal1[0]; 
                                 todate       =(Date) objstotal1[1];
                                   addmnt1       =(double) objstotal1[2];
                                }
                                   if(currentdate.after(fromdate) && currentdate.before(todate)){
                                       
                                 debtMax=debtMax+addmnt1;
                                       
//                                   if(Sum>debtMax){
//                                       Sum=Sum-addmnt1;
//                                   
//                        
//                                  }
//                          
//                     else if(Sum<debtMax){
//                       Sum=Sum+addmnt1;
//                        
//                            }
//                    else{
//                         Sum=Sum+addmnt1;                          
//                     }
                                   }     */

                            Sum = roundTwoDecimals(Sum);
                            String name = "Credit check for QT";
                            Object[] objqt = (Object[]) new StaticSentence(m_App.getSession(), "SELECT VALUE FROM generaltable where NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.BOOLEAN})).find(name);
                            Boolean QTcheck = (Boolean) objqt[0];

                            if (QTcheck == true) {///Start of check

                                //Object[] gobj2 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT DEBTMAX FROM memtype where id=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(memtype);
                                // String dMax = gobj2[0].toString();
                                //Double debtMax = new Double(dMax);
                                if ((Sum > debtMax)) {///LimitCross>>>Start
                                    //to overcome the products reduction

                                    int s = 0;
                                    while (s < size) {
                                        TicketLineInfo tl = ticket1.getLine(s);
                                        if (tl.isStockCheckRequired()) {
                                            //    Object o = dlSales.getStockVolume(tl.getProductID());
                                            Double sqty = 0.0;
                                            if (stk[s] != null) {
                                                sqty = Double.parseDouble(stk[s].toString());
                                            }
                                            if (sqty >= tl.getMultiply()) {
                                                dlSales.updateStockVolume(tl.getMultiply(), tl.getProductID());
                                                dlSales.updateStockVolume1(tl.getMultiply(), tl.getProductID());
                                                s++;
                                            }
                                        } else {
                                            s++;
                                        }
                                    }//end of while
                                    JOptionPane.showMessageDialog(null, "Please clear the Balance.Cannot prepare QT for it", "Amount limit Exceeded", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    amountUpdate(Sum, cust, ticket1, ticket2);

                                }//End of else //if(Sum>5000)End
                            } else {//if(gsa.checkQT()==true){////End of check
                                amountUpdate(Sum, cust, ticket1, ticket2);
                            }

                        }
//        } /////End of If(x==0);//Just to overcome the again initialization of printTotal
////////aaa..End

                    } else {
                        if (berror == true) {
                            JOptionPane.showMessageDialog(null, "Please reset the system time or consult your system admin", "Sorry Cannot login", JOptionPane.OK_OPTION);
                        }
                    }

                    return null;
                }
            };
            t.execute();
            // JOptionPane.showMessageDialog(this, "Qt Created..!!");

        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotcreateqt. \n Please Check  QT Bill Update  Button on main menu"), e);
            msg.show(this);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    public void amountUpdate(Double Sum, String cust, TicketInfo ticket1, TicketInfo ticket2) throws BasicException {
        try {

            String BMName = "Credit Check for billing member";
            String BName2 = "Facility for billing member";
            Object[] FacObj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE where NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.BOOLEAN})).find(BMName);

            if (FacObj != null) {
                Boolean v5 = (Boolean) FacObj[0];
                if (v5) {

                    ///////////       ///////////////////////     ////////////////////////////
                    new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{Sum, cust});

                    if (Sum > 0) {

                        jTextField1.setForeground(Color.red);
                        jTextField1.setText("Dr.  " + Sum);
                    } else if (Sum < 0) {
                        jTextField1.setForeground(Color.green);
                        Sum = Sum * (-1);
                        jTextField1.setText("Cr.  " + Sum);
                    } else {
                        jTextField1.setForeground(Color.black);
                        jTextField1.setText("" + Sum);
                    }
                    ////////////////////////           ////////////////////////////////////      /////////////////////////

                } else {

                    // code added by akash 
                    ////////////////////////           ////////////////////////////////////      /////////////////////////
                    String FacIDforBM = null;
                    Object s5 = m_App.getAppUserView().getUser().getWarehouse();
                    String warehouse5 = null;
                    if (s5 != null) {
                        String[] warehouses = s5.toString().split("#");
                        warehouse5 = warehouses[0];
                    }
                    Object obj5 = new StaticSentence(m_App.getSession(), "SELECT FACILITY FROM LOCATIONS WHERE ID=?", SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE).find(warehouse5);
                    if (obj5 != null) {
                        FacIDforBM = obj5.toString();
                    }

                    Object[] Fac2Obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE where NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(BName2);
                    String FacStrFull = Fac2Obj[0].toString();
                    String[] strarr = FacStrFull.split("#");
                    String FinFacId = strarr[0];

                    if (FinFacId.equals(FacIDforBM)) {

                        new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{Sum, cust});

                        if (Sum > 0) {

                            jTextField1.setForeground(Color.red);
                            jTextField1.setText("Dr.  " + Sum);
                        } else if (Sum < 0) {
                            jTextField1.setForeground(Color.green);
                            Sum = Sum * (-1);
                            jTextField1.setText("Cr.  " + Sum);
                        } else {
                            jTextField1.setForeground(Color.black);
                            jTextField1.setText("" + Sum);
                        }

                    }
                    ////////////////////////           ////////////////////////////////////      /////////////////////////

                }

            } else {

                ///////////       ///////////////////////     ////////////////////////////
                new PreparedSentence(m_App.getSession(), "UPDATE billingmember SET CURROPB=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.STRING})).exec(new Object[]{Sum, cust});

                if (Sum > 0) {

                    jTextField1.setForeground(Color.red);
                    jTextField1.setText("Dr.  " + Sum);
                } else if (Sum < 0) {
                    jTextField1.setForeground(Color.green);
                    Sum = Sum * (-1);
                    jTextField1.setText("Cr.  " + Sum);
                } else {
                    jTextField1.setForeground(Color.black);
                    jTextField1.setText("" + Sum);
                }
                ////////////////////////           ////////////////////////////////////      /////////////////////////

            }

            setActiveTicket(ticket2, m_oTicketExt);// set result ticket
            dlReceipts.updateSharedTicket(ticket2.getCustomerId(), ticket2, m_App.getAppUserView().getUser().getRole());
            //  dlReceipts.updateLastQtTimeOfSharedTicket(new Date(),ticket1.getCustomerId(), m_App.getAppUserView().getUser().getRole());
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_ticketlines.removeTicketLine(0);
            }
            createQTicket(ticket1);

            //         qtk.refreshQTModel(m_App);///aaa
            //praveen:exit for every transaction for kiosk mode---start
            if (m_App.getAppUserView().getUser().getTypeOfUser() == 1) {
                JPrincipalApp.m_approot.closeAppView();
            }
            //praveen:exit for every transaction for kiosk mode---end

        } // edited for checking whether qtickets are duplicated or not.......... ## Akash        
        catch (Exception e) {
            //  CheckDuplicateQT();
            //UpdateDuplicateQTCheck();
            throw new BasicException(e.getMessage());

        }
    }

private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSplit;
    private javax.swing.JPanel catcontainer;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JPanel m_jButtonsExt;
    private javax.swing.JPanel m_jContEntries;
    private javax.swing.JButton m_jDelete;
    private javax.swing.JButton m_jDown;
    private javax.swing.JButton m_jEditLine;
    private javax.swing.JButton m_jEnter;
    private javax.swing.JTextField m_jKeyFactory;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jLblTotalEuros2;
    private javax.swing.JLabel m_jLblTotalEuros3;
    private javax.swing.JButton m_jList;
    private com.openbravo.beans.JNumberKeys m_jNumberKeys;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanContainer;
    private javax.swing.JPanel m_jPanEntries;
    private javax.swing.JPanel m_jPanTicket;
    private javax.swing.JPanel m_jPanTotals;
    private javax.swing.JPanel m_jPanelBag;
    private javax.swing.JPanel m_jPanelCentral;
    private javax.swing.JPanel m_jPanelScripts;
    private javax.swing.JLabel m_jPor;
    private javax.swing.JLabel m_jPrice;
    private javax.swing.JLabel m_jSubtotalEuros;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JLabel m_jTaxesEuros;
    private javax.swing.JLabel m_jTicketId;
    private javax.swing.JLabel m_jTotalEuros;
    private javax.swing.JButton m_jUp;
    private javax.swing.JToggleButton m_jaddtax;
    private javax.swing.JButton m_jbtnScale;
    private javax.swing.JButton m_prTicket;
    // End of variables declaration//GEN-END:variables

    public Double getTaxrateByCategory(String Category) throws BasicException, BasicException {
        Double Taxrate = 0.00;
        Object[] obj2 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT RATE FROM TAXES where CATEGORY=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(Category);
        Taxrate = (Double) obj2[0];
        return Taxrate;
    }

}
