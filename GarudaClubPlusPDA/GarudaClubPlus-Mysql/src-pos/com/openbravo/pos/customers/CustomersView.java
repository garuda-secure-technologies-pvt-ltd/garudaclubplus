package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.Accounts.AccountMasterExt;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.clubmang.DebtTypeTableModel;
import com.openbravo.pos.clubmang.Facility;
import com.openbravo.pos.clubmang.FacilityLogic;
import com.openbravo.pos.clubmang.MemCat;
import com.openbravo.pos.clubmang.MemLinkTableModel;
import com.openbravo.pos.clubmang.MemberDependentTableModel;
import com.openbravo.pos.clubmang.Periodicity;
import com.openbravo.pos.clubmang.TempClass;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.util.StringUtils;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;

/**
 *
 * @author  adrianromero
 */
public class CustomersView extends javax.swing.JPanel implements EditorRecord {

    private Object m_oId;
    private SentenceList m_sentcat;
    private DataLogicFacilities dlfac;
    private ComboBoxValModel m_CategoryModel;
    private ComboBoxValModel m_accountModel;
    private ComboBoxValModel m_memtype;
    private ComboBoxValModel m_stax;
    private ComboBoxValModel m_etax;
    private ComboBoxValModel m_sponsor1;
    private ComboBoxValModel m_sponsor2;
    private ComboBoxValModel m_sponsor3;
    private ComboBoxValModel m_memdtype;
    // private ComboBoxValModel dtyprModel
    //  private ComboBoxValModel m_memltype;
    private List<MemCat> mlist4;
    private String mtype;
    private AppView m_App;
    private DirtyManager m_Dirty;
    private DataLogicCustomers dlcus;
    private String customerid;
    private MemberDependentTableModel mdmodel;
    private MemLinkTableModel mlmodel;
    private double prevopeningbal = 0.0;
    private DataLogicSales m_dlSales;
    private TaxesLogic taxeslogic;
    private String servicetaxacc;
    private String cus_skey = null;
    Object[] customerold = new Object[35];
    // private String memtype;

    /** Creates new form CustomersView */
    public CustomersView(AppView app, DirtyManager dirty) {
        m_App = app;
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        dlfac = (DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        dlcus = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        initComponents();
        String datelist[] = new String[]{null, "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        String monthlist[] = new String[]{null, "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        date.setModel(new javax.swing.DefaultComboBoxModel(datelist));
        month.setModel(new javax.swing.DefaultComboBoxModel(monthlist));
        ddate.setModel(new javax.swing.DefaultComboBoxModel(datelist));
        dmonth.setModel(new javax.swing.DefaultComboBoxModel(monthlist));
        dtypelist.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Daughter", "Father", "Mother", "Son", "Spouse"}));
        editIndicator1.setVisible(false);
        m_sentcat = dlSales.getTaxCustCategoriesList();
        // m_sentacc=dlfac.getaccounts();
        m_stax = new ComboBoxValModel();
        m_etax = new ComboBoxValModel();
        m_sponsor1 = new ComboBoxValModel();
        m_sponsor2 = new ComboBoxValModel();
        m_sponsor3 = new ComboBoxValModel();
        m_CategoryModel = new ComboBoxValModel();
        m_accountModel = new ComboBoxValModel();
        m_memtype = new ComboBoxValModel();

        m_Dirty = dirty;
        m_jTaxID.getDocument().addDocumentListener(dirty);
        m_jSearchkey.getDocument().addDocumentListener(dirty);
        sowo.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jCategory.addActionListener(dirty);
        acctype.addActionListener(dirty);
        m_jNotes.getDocument().addDocumentListener(dirty);
        m_jVisible.addActionListener(dirty);
        jImageEditor1.addPropertyChangeListener("image", dirty);
        jImageEditor2.addPropertyChangeListener(dirty);
        date.addActionListener(dirty);
        month.addActionListener(dirty);
        year.getDocument().addDocumentListener(dirty);
        memtype.addActionListener(dirty);
        mobile.getDocument().addDocumentListener(dirty);
        serviceTaxcat.addActionListener(dirty);
        entTaxcat.addActionListener(dirty);
        sponsor1.addActionListener(dirty);
        sponsor2.addActionListener(dirty);
        sponsor3.addActionListener(dirty);
        joindate.getDocument().addDocumentListener(dirty);
        terminationdate.getDocument().addDocumentListener(dirty);
        txtFirstName.getDocument().addDocumentListener(dirty);
        txtLastName.getDocument().addDocumentListener(dirty);
        txtEmail.getDocument().addDocumentListener(dirty);
        txtPhone.getDocument().addDocumentListener(dirty);
        txtPhone2.getDocument().addDocumentListener(dirty);
        txtFax.getDocument().addDocumentListener(dirty);

        txtAddress.getDocument().addDocumentListener(dirty);
        txtAddress2.getDocument().addDocumentListener(dirty);
        txtPostal.getDocument().addDocumentListener(dirty);
        txtCity.getDocument().addDocumentListener(dirty);
        txtRegion.getDocument().addDocumentListener(dirty);
        txtCountry.getDocument().addDocumentListener(dirty);
        //openbalance.getDocument().addDocumentListener(dirty);
        openbalance.setVisible(false);
        openbalancelabel.setVisible(false);
        jComboBox1.setVisible(false);
        writeValueEOF();
    }

    public void activate() throws BasicException {
        taxeslogic = new TaxesLogic(m_dlSales.getTaxList().list());
        List a = m_sentcat.list();
        a.add(0, null); // The null item
        m_CategoryModel = new ComboBoxValModel(a);
        m_stax = new ComboBoxValModel(a);
        m_etax = new ComboBoxValModel(a);
        m_jCategory.setModel(m_CategoryModel);
        serviceTaxcat.setModel(m_stax);
        entTaxcat.setModel(m_etax);
        //List b=dlfac.getaccounts();
        // b.add(0, null);
        m_accountModel = new ComboBoxValModel(dlfac.getMemberReceivableAccount());
        acctype.setModel(m_accountModel);
        List mlist4 = dlfac.getMemberCategory();
        m_memtype = new ComboBoxValModel(mlist4);
        memtype.setModel(m_memtype);
        //m_memtype=new ComboBoxValModel(dlfac.getMemTypeBasic());
        //memtype.setModel(m_memtype);
        List mem = dlcus.getCustomerList().list();
        mem.add(0, null);
        m_sponsor1 = new ComboBoxValModel(mem);
        m_sponsor2 = new ComboBoxValModel(mem);
        m_sponsor3 = new ComboBoxValModel(mem);
        sponsor1.setModel(m_sponsor1);
        sponsor2.setModel(m_sponsor2);
        sponsor3.setModel(m_sponsor3);
        m_memdtype = new ComboBoxValModel();
        openbalance.setVisible(false);
        openbalancelabel.setVisible(false);
        jComboBox1.setVisible(false);
        effectivedate.setText(Formats.DATE.formatValue(new Date()));
    }

    public void writeValueEOF() {
        m_oId = null;

        m_jTaxID.setText(null);
        m_jSearchkey.setText(null);
        m_jName.setText(null);
        m_CategoryModel.setSelectedKey(null);
        // m_accountModel.setSelectedKey(-1);
        acctype.setSelectedIndex(-1);
        m_jNotes.setText(null);
        m_jVisible.setSelected(false);
        jcard.setText(null);
        date.setSelectedIndex(0);
        month.setSelectedIndex(0);

        ddate.setSelectedIndex(0);
        dmonth.setSelectedIndex(0);
        dtypelist.setSelectedIndex(-1);
        year.setText(null);

        memtype.setSelectedIndex(-1);

        mobile.setText(null);
        sowo.setText(null);
        m_stax.setSelectedKey(null);
        m_etax.setSelectedKey(null);
        m_sponsor1.setSelectedKey(null);
        m_sponsor2.setSelectedKey(null);


        m_sponsor3.setSelectedKey(null);

        joindate.setText(null);
        terminationdate.setText(null);

        jImageEditor2.setImage(null);


        txtFirstName.setText(null);
        txtLastName.setText(null);
        txtEmail.setText(null);
        txtPhone.setText(null);
        txtPhone2.setText(null);
        txtFax.setText(null);

        txtAddress.setText(null);
        txtAddress2.setText(null);
        txtPostal.setText(null);
        txtCity.setText(null);
        txtRegion.setText(null);
        txtCountry.setText(null);

        //    m_jTaxID.setEnabled(false);
        //    m_jSearchkey.setEnabled(false);
        //    m_jName.setEnabled(false);
        //   m_jCategory.setEnabled(false);
        //    acctype.setEnabled(false);
        //    m_jNotes.setEnabled(false);
        //    txtMaxdebt.setEnabled(false);
        //    txtCurdebt.setEnabled(false);
        //    txtCurdate.setEnabled(false);
   /*     m_jVisible.setEnabled(false);
        jcard.setEnabled(false);
        
        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);
        txtPhone2.setEnabled(false);
        txtFax.setEnabled(false);

        txtAddress.setEnabled(false);
        txtAddress2.setEnabled(false);
        txtPostal.setEnabled(false);
        txtCity.setEnabled(false);
        txtRegion.setEnabled(false);
        txtCountry.setEnabled(false);
        
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);*/
        jImageEditor1.setImage(null);
        // jImageEditor1.setEnabled(false);
        EnabledComponent(false);
        openbalance.setVisible(false);
        openbalancelabel.setVisible(false);
        jComboBox1.setVisible(false);


    }

    private void EnabledComponent(Boolean flag) {
        jImageEditor1.setEnabled(flag);
        m_jTaxID.setEnabled(flag);
        m_jSearchkey.setEnabled(flag);
        m_jName.setEnabled(flag);
        m_jCategory.setEnabled(flag);
        acctype.setEnabled(flag);
        m_jNotes.setEnabled(flag);
        m_jVisible.setEnabled(flag);
        jcard.setEnabled(flag);
        txtFirstName.setEnabled(flag);
        txtLastName.setEnabled(flag);
        txtEmail.setEnabled(flag);
        txtPhone.setEnabled(flag);
        txtPhone2.setEnabled(flag);
        txtFax.setEnabled(flag);
        txtAddress.setEnabled(flag);
        txtAddress2.setEnabled(flag);
        txtPostal.setEnabled(flag);
        txtCity.setEnabled(flag);
        txtRegion.setEnabled(flag);
        txtCountry.setEnabled(flag);
        jButton2.setEnabled(flag);
        jButton3.setEnabled(flag);
        jImageEditor2.setEnabled(flag);
        jButton4.setEnabled(flag);
        jButton5.setEnabled(flag);
        jButton6.setEnabled(flag);
        jButton7.setEnabled(flag);
        date.setEnabled(flag);
        month.setEnabled(flag);
        ddate.setEnabled(flag);
        dmonth.setEnabled(flag);
        year.setEnabled(flag);
        sowo.setEnabled(flag);
        sponsor3.setEnabled(flag);
        joindate.setEnabled(flag);
        sponsor2.setEnabled(flag);
        sponsor1.setEnabled(flag);
        entTaxcat.setEnabled(flag);
        serviceTaxcat.setEnabled(flag);
        mobile.setEnabled(flag);
        memtype.setEnabled(flag);
        terminationdate.setEnabled(flag);
    }

    public void writeValueInsert() {
        m_oId = null;
        m_jTaxID.setText(null);
        m_jSearchkey.setText(null);
        m_jName.setText(null);
        m_CategoryModel.setSelectedKey(null);
        acctype.setSelectedIndex(-1);
        m_jNotes.setText(null);
        m_jVisible.setSelected(false);
        jcard.setText(null);
        date.setSelectedIndex(0);
        month.setSelectedIndex(0);
        ddate.setSelectedIndex(0);
        dmonth.setSelectedIndex(0);
        year.setText(null);
        memtype.setSelectedIndex(-1);
        mobile.setText(null);
        sowo.setText(null);
        m_stax.setSelectedKey(null);
        m_etax.setSelectedKey(null);
        m_sponsor1.setSelectedKey(null);
        m_sponsor2.setSelectedKey(null);
        m_sponsor3.setSelectedKey(null);
        joindate.setText(null);
        terminationdate.setText(null);
        jImageEditor2.setImage(null);
        txtFirstName.setText(null);
        txtLastName.setText(null);
        txtEmail.setText(null);
        txtPhone.setText(null);
        txtPhone2.setText(null);
        txtFax.setText(null);
        txtAddress.setText(null);
        txtAddress2.setText(null);
        txtPostal.setText(null);
        txtCity.setText(null);
        txtRegion.setText(null);
        txtCountry.setText(null);
        jImageEditor1.setImage(null);
        openbalancelabel.setVisible(true);
        openbalance.setVisible(true);
        openbalance.setText("0.0");
        jComboBox1.setVisible(true);
        EnabledComponent(true);
        reset();
    }

    public void writeValueDelete(Object value) {
        Object[] customer = (Object[]) value;
        m_oId = customer[0];
        m_jTaxID.setText((String) customer[1]);
        m_jSearchkey.setText((String) customer[2]);
        m_jName.setText((String) customer[3]);
        m_jNotes.setText((String) customer[4]);
        m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
        jcard.setText((String) customer[6]);
        //  txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        //   txtCurdate.setText(Formats.DATE.formatValue(customer[8]));
        //  txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));

        txtFirstName.setText(Formats.STRING.formatValue(customer[7]));
        txtLastName.setText(Formats.STRING.formatValue(customer[8]));
        txtEmail.setText(Formats.STRING.formatValue(customer[9]));
        txtPhone.setText(Formats.STRING.formatValue(customer[10]));
        txtPhone2.setText(Formats.STRING.formatValue(customer[11]));
        txtFax.setText(Formats.STRING.formatValue(customer[12]));

        txtAddress.setText(Formats.STRING.formatValue(customer[13]));
        txtAddress2.setText(Formats.STRING.formatValue(customer[14]));
        txtPostal.setText(Formats.STRING.formatValue(customer[15]));
        txtCity.setText(Formats.STRING.formatValue(customer[16]));
        txtRegion.setText(Formats.STRING.formatValue(customer[17]));
        txtCountry.setText(Formats.STRING.formatValue(customer[18]));

        jImageEditor1.setImage((BufferedImage) customer[19]);

        m_CategoryModel.setSelectedKey(customer[20]);
        Object pid = "";
        try {
            if (customer[21] != null) {
                pid = dlcus.getparentaccountbyid(customer[21].toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!pid.equals("")) // if(customer[21]!=null)
        {
            m_accountModel.setSelectedKey(customer[21]);
        } else {
            m_accountModel.setSelectedKey(null);
        }
        //"MEMTYPE","FINGERPRINTDATA","SOWO"
        //, "MOBILE","SERVICETAXCAT","ENTTAXCAT","SIGNATURE","SPONSOR1","SPONSOR2","SPONSOR3"
        //, "JOINDATE","TERDATE","DOB"
        if (customer[22] != null) {
            m_memtype.setSelectedKey(customer[22]);
        } else {
            m_memtype.setSelectedKey(-1);
        }
        sowo.setText(Formats.STRING.formatValue(customer[23]));
        mobile.setText(Formats.STRING.formatValue(customer[24]));
        //  if(customer[25]!=null)
        m_stax.setSelectedKey(customer[25]);
        // if(customer[26]!=null)
        m_etax.setSelectedKey(customer[26]);
        jImageEditor1.setImage((BufferedImage) customer[27]);
        //   if(customer[28]!=null)
        m_sponsor1.setSelectedKey(customer[28]);
        //  if(customer[29]!=null)
        m_sponsor2.setSelectedKey(customer[29]);
        //  if(customer[30]!=null)
        m_sponsor3.setSelectedKey(customer[30]);
        joindate.setText(Formats.STRING.formatValue(customer[31]));
        terminationdate.setText(Formats.STRING.formatValue(customer[32]));
        if (customer[33] == null) {
            Date d = (Date) customer[33];
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTimeInMillis(d.getTime());
            date.setSelectedItem(cal.get(Calendar.DATE));
            month.setSelectedIndex(cal.get(Calendar.MONTH));
            year.setText(Formats.STRING.formatValue(cal.get(Calendar.YEAR)));
        }
        openbalance.setVisible(false);
        openbalance.setText(customer[34].toString());
        openbalancelabel.setVisible(false);
        jComboBox1.setVisible(false);
        EnabledComponent(false);
        reset();
    }

    public void writeValueEdit(Object value) {
        customerold = (Object[]) value;
        Object[] customer = (Object[]) value;
        m_oId = customer[0];
        customerid = m_oId.toString();
        m_jTaxID.setText((String) customer[1]);
        m_jSearchkey.setText((String) customer[2]);
        cus_skey = m_jSearchkey.getText().trim();
        m_jName.setText((String) customer[3]);
        m_jNotes.setText((String) customer[4]);
        m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
        jcard.setText((String) customer[6]);
        //  txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        //  txtCurdate.setText(Formats.DATE.formatValue(customer[8]));
        //  txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));

        txtFirstName.setText(Formats.STRING.formatValue(customer[7]));
        txtLastName.setText(Formats.STRING.formatValue(customer[8]));
        txtEmail.setText(Formats.STRING.formatValue(customer[9]));
        txtPhone.setText(Formats.STRING.formatValue(customer[10]));
        txtPhone2.setText(Formats.STRING.formatValue(customer[11]));
        txtFax.setText(Formats.STRING.formatValue(customer[12]));

        txtAddress.setText(Formats.STRING.formatValue(customer[13]));
        txtAddress2.setText(Formats.STRING.formatValue(customer[14]));
        txtPostal.setText(Formats.STRING.formatValue(customer[15]));
        txtCity.setText(Formats.STRING.formatValue(customer[16]));
        txtRegion.setText(Formats.STRING.formatValue(customer[17]));
        txtCountry.setText(Formats.STRING.formatValue(customer[18]));
        jImageEditor1.setImage((BufferedImage) customer[20]);

        prevopeningbal = 0;
        m_CategoryModel.setSelectedKey(customer[19]);
        Object pid = "";
        try {
            if (customer[21] != null) {
                pid = dlcus.getparentaccountbyid(customer[21].toString());
                if (customer[34] == (Object) 0.0 || customer[34] == (Object) (-0.0)) {
                    prevopeningbal = Double.parseDouble(customer[34].toString());
                    openbalance.setText(customer[34].toString());
                    openbalance.setEditable(true);
                    openbalance.setVisible(true);
                    openbalancelabel.setVisible(true);
                    jComboBox1.setVisible(true);
                } else if (customer[34] != null) {
                    prevopeningbal = Double.parseDouble(customer[34].toString());
                    openbalance.setText(customer[34].toString());
                    openbalance.setEditable(false);
                    openbalance.setVisible(true);
                    openbalancelabel.setVisible(true);
                    jComboBox1.setVisible(false);
                } else {
                    openbalance.setText("0.0");
                    openbalance.setVisible(true);
                    openbalance.setEditable(true);
                    openbalancelabel.setVisible(true);
                    jComboBox1.setVisible(true);
                }

            } else {
                openbalancelabel.setVisible(true);
                openbalance.setVisible(true);
                openbalance.setEditable(true);
                openbalance.setText("0.0");
                jComboBox1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!pid.equals("")) {
            m_accountModel.setSelectedKey(pid);
        } else {
            m_accountModel.setSelectedKey(null);
        }
        if (customer[22] != null) {
            m_memtype.setSelectedKey(customer[22]);
            mtype = customer[22].toString();
        } else {
            m_memtype.setSelectedKey(-1);
            mtype = null;
        }
        sowo.setText(Formats.STRING.formatValue(customer[23]));
        mobile.setText(Formats.STRING.formatValue(customer[24]));
        m_stax.setSelectedKey(customer[25]);
        m_etax.setSelectedKey(customer[26]);
        jImageEditor2.setImage((BufferedImage) customer[27]);
        //    if(customer[28]!=null)
        m_sponsor1.setSelectedKey(customer[28]);
        //   if(customer[29]!=null)
        m_sponsor2.setSelectedKey(customer[29]);
        //   if(customer[30]!=null)
        m_sponsor3.setSelectedKey(customer[30]);
        joindate.setText(Formats.TIMESTAMP.formatValue(customer[31]));
        terminationdate.setText(Formats.TIMESTAMP.formatValue(customer[32]));

        if (customer[33] != null && customer[33] != "") {
            Date d = (Date) customer[33];
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTimeInMillis(d.getTime());
            date.setSelectedIndex(cal.get(Calendar.DATE));
            month.setSelectedIndex(cal.get(Calendar.MONTH) + 1);
            year.setText(String.valueOf(cal.get(Calendar.YEAR)));
        } else {
            date.setSelectedIndex(-1);
            month.setSelectedIndex(-1);
            year.setText(null);
        }

        EnabledComponent(true);
        reset();
    }

    private Double getBilledAmount(Date d, TempClass tc) {
        Date dnow = new Date();
        Double amt = 0.0;
        Calendar caltemp1 = GregorianCalendar.getInstance();
        Calendar caltemp2 = GregorianCalendar.getInstance();
        Calendar caltemp3 = GregorianCalendar.getInstance();
        caltemp1.setTimeInMillis(tc.getlbilldate().getTime());
        caltemp2.setTimeInMillis(dnow.getTime());
        caltemp3.setTimeInMillis(caltemp1.getTimeInMillis());
        //caltemp2.set(Calendar.DATE,caltemp1.get(Calendar.DATE));
        //  Timestamp temp2=caltemp2.getTimeInMillis()
        long difMil1 = caltemp2.getTimeInMillis() - caltemp1.getTimeInMillis();
        int days1 = (int) ((difMil1 + 12 * 3600000L) / (24 * 3600000L));
        if (tc.getptype().equals("Days")) {


            int nop = days1 % tc.getpnum();//no of period
            if (nop == 0) {
                caltemp2.set(Calendar.DATE, caltemp1.get(Calendar.DATE));
                amt = (days1 / tc.getpnum()) * tc.getrfee();
            } else {
                int diff = tc.getpnum() - nop;
                amt = ((days1 / tc.getpnum()) + 1) * tc.getrfee();
                caltemp2.add(Calendar.DATE, diff);
            }
        } else if (tc.getptype().equals("Months")) {
            int i = 0;
            while (caltemp2.before(caltemp3)) {
                i++;
                caltemp3.add(Calendar.MONTH, tc.getpnum());
            }
            caltemp2.setTimeInMillis(caltemp3.getTimeInMillis());
            amt = i * tc.getrfee();
        } else if (tc.getptype().equals("Quaterly")) {
            int i = 0;
            while (caltemp2.before(caltemp3)) {
                i++;
                caltemp3.add(Calendar.MONTH, 3);
            }
            caltemp2.setTimeInMillis(caltemp3.getTimeInMillis());
            amt = i * tc.getrfee();
        } else if (tc.getptype().equals("Years")) {
            int i = 0;
            while (caltemp2.before(caltemp3)) {
                i++;
                caltemp3.add(Calendar.YEAR, 3);
            }
            caltemp2.setTimeInMillis(caltemp3.getTimeInMillis());
            amt = i * tc.getrfee();
        }
        long difMil = caltemp1.getTimeInMillis() - caltemp2.getTimeInMillis();
        int days = (int) ((difMil + 12 * 3600000L) / (24 * 3600000L));
        Double total = amt * days1 / days;
        return total;
    }

    private Date getDueDate(DebtTypeTableModel.DebtTypeline dueperiod) {
        String type = dueperiod.getperiod();
        int num = dueperiod.getNum();
        Date duedate = new Date();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(duedate.getTime());
        if (type.equals("Days")) {
            cal.add(Calendar.DATE, num);
        }
        if (type.equals("Months")) {
            cal.add(Calendar.MONTH, num);
        }
        if (type.equals("Years")) {
            cal.add(Calendar.YEAR, num);
        }
        duedate.setTime(cal.getTimeInMillis());
        return duedate;
    }

    private Date getdate(Date d) {
        //Date d=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        d.setTime(cal.getTimeInMillis());
        return d;
    }

    private void postbilling(Facility f, Date date1, String cust, String custid) throws BasicException {
        Object[] value1 = new Object[]{UUID.randomUUID().toString(), true, customerid, f.getid(), date1, m_App.getAppUserView().getUser().getName(), date1};
        new PreparedSentence(m_App.getSession(), "INSERT INTO MEMFACILITYUSAGE(ID, ACTIVE, MEMNO, FACILITYTYPE, SDATE, CREATEDBY, CRDATE) VALUES (?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP})).exec(value1);
        if (f.getjamt() > 0) {
            DebtTypeTableModel.DebtTypeline periodtype = dlfac.getDebtTypebyid(f.getdueperiod());
            Date duedate = getDueDate(periodtype);
            String billno = dlfac.getnewbillno(f.getid());
            String tid = UUID.randomUUID().toString();
            String facname = f.getName();

            Object[] value2 = new Object[]{UUID.randomUUID().toString(), tid, date1, "C", f.getid(), billno, f.getjamt(), date1, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), facname + " " + billno, f.getJoinfeeAccount(), 0.0, date1, true};
            dlfac.insertintoaccjoutnal1(value2);
            Object[] value3 = new Object[]{UUID.randomUUID().toString(), tid, customerid, date1, "D", f.getid(), billno, f.getjamt(), duedate, false, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Join Fees for " + facname, cust, f.getjamt(), true};
            dlfac.insertintoaccjoutnal(value3);
            dlfac.updatebillno(f.getid());
        }
    }

    private void prebilling(Facility f, Date date1, Periodicity p, String cid, String caccount) throws BasicException {
        String fid = f.getid();
        String fname = f.getName();
        Date d = new Date();
        d.setTime(date1.getTime());
        if ((f.getrperiod() != null && f.getramt() > 0) || f.getjamt() > 0) {
            double totalamt = 0.0;
            //  String servicetaxacc=null;
            Double taxrate = 0.0;
            if (f.getservicetax() != null) {
                TaxCategoryInfo tinfo = (TaxCategoryInfo) m_dlSales.getTaxCategoryByid(f.getservicetax());
                TaxInfo tax = taxeslogic.getTaxInfo(tinfo);
                taxrate = tax.getRate();
            }

            String billno = dlfac.getnewbillno(f.getid());
            if (f.getrperiod() != null && f.getramt() > 0) {
                double ramt = 0.0;
                Date lbdate = new Date();
                lbdate.setTime(date1.getTime());
                Date edate = new Date();
                edate.setTime(date1.getTime());
                Date duedate = new Date();
                duedate.setTime(date1.getTime());
                DebtTypeTableModel.DebtTypeline periodtype = dlfac.getDebtTypebyid(f.getdueperiod());
                lbdate.setTime(date1.getTime());
                FacilityLogic flogic = new FacilityLogic(dlfac);
                Calendar cal1 = Calendar.getInstance();
                cal1.setTimeInMillis(lbdate.getTime());
                int billabledate = cal1.get(Calendar.DATE);
                if (p.getdoj() == false) {
                    cal1.setTimeInMillis(flogic.getBillableDate(p, billabledate, cal1).getTimeInMillis());
                }
                lbdate.setTime(cal1.getTimeInMillis());
                billabledate = cal1.get(Calendar.DATE);
                edate.setTime(flogic.calculateEndDate(lbdate, p, billabledate, 1, new Date()).getTime());
                duedate.setTime(flogic.getDueDate(periodtype, flogic.getTemp()).getTime());
                if (p.getaccurate() == true) {
                    ramt = flogic.calulaterenewalamt(date1, edate, ramt);
                } else {
                    if (flogic.getPnum() > 0) {
                        ramt = f.getramt();
                        ramt = flogic.getPnum() * ramt;
                    }
                }
                double taxamt = Math.floor(taxrate * ramt);
                String tid = UUID.randomUUID().toString();
                if (taxamt > 0) {
                    if (servicetaxacc == null) {
                        JOptionPane.showMessageDialog(null, "Service tax account is missing", "Error", JOptionPane.OK_OPTION);
                    }
                    Object[] value4 = new Object[]{UUID.randomUUID().toString(), tid, d, "C", fid, billno, taxamt, d, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Service tax on Renewal Fees for " + f.getName(), servicetaxacc, 0.0, d, new Date(), true};
                    dlfac.insertintoaccjoutnal3(value4);
                }
                double tramt = dlfac.roundTwoDecimals(ramt + taxamt);
                if (tramt > 0) {
                    Object[] value2 = new Object[]{UUID.randomUUID().toString(), tid, d, "C", fid, billno, ramt, d, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Renewal Fees for Period :" + Formats.DATE.formatValue(lbdate) + " to " + Formats.DATE.formatValue(edate), f.getRenewalacc(), 0.0, d, new Date(), true};
                    dlfac.insertintoaccjoutnal3(value2);
                    Object[] value3 = new Object[]{UUID.randomUUID().toString(), tid, cid, d, "D", fid, billno, tramt, duedate, false, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Renewal Fees for Period :" + Formats.DATE.formatValue(lbdate) + " to " + Formats.DATE.formatValue(edate), caccount, tramt, new Date(), true};
                    dlfac.insertintoaccjoutnal0(value3);
                    totalamt = tramt;
                    Object[] value1 = new Object[]{UUID.randomUUID().toString(), true, cid, fid, d, m_App.getAppUserView().getUser().getName(), new Date(), edate, new Date(), billno, 0};
                    new PreparedSentence(m_App.getSession(), "INSERT INTO MEMFACILITYUSAGE(ID, ACTIVE, MEMNO, FACILITYTYPE, SDATE, CREATEDBY, CRDATE,LBILLDATE,NBILLDATE,BILLREF,STATUS_) VALUES (?,?,?,?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.INT})).exec(value1);
                } else {
                    Object[] value1 = new Object[]{UUID.randomUUID().toString(), true, cid, fid, d, m_App.getAppUserView().getUser().getName(), new Date(), 0};
                    new PreparedSentence(m_App.getSession(), "INSERT INTO MEMFACILITYUSAGE(ID, ACTIVE, MEMNO, FACILITYTYPE, SDATE, CREATEDBY, CRDATE,STATUS_) VALUES (?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.INT})).exec(value1);
                }
            }
            if (f.getjamt() > 0) {
                // DebtTypeTableModel.DebtTypeline periodtype=(DebtTypeTableModel.DebtTypeline)debttype.getSelectedItem();
                double jamt = f.getjamt();
                double taxamt = Math.floor(taxrate * jamt);
                String tid = UUID.randomUUID().toString();
                String facname = f.getName();
                if (taxamt > 0) {
                    if (servicetaxacc == null) {
                        JOptionPane.showMessageDialog(null, "Service tax account is missing", "Error", JOptionPane.OK_OPTION);
                    }
                    Object[] value4 = new Object[]{UUID.randomUUID().toString(), tid, d, "C", fid, billno, taxamt, d, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Service tax on Join Fees for " + facname, servicetaxacc, 0.0, d, new Date(), true};
                    dlfac.insertintoaccjoutnal3(value4);
                }
                double tjamt = dlfac.roundTwoDecimals(jamt + taxamt);
                Object[] value2 = new Object[]{UUID.randomUUID().toString(), tid, d, "C", fid, billno, dlfac.roundTwoDecimals(jamt), d, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Join Fees for " + facname, f.getJoinfeeAccount(), 0.0, d, new Date(), true};
                dlfac.insertintoaccjoutnal3(value2);
                Object[] value3 = new Object[]{UUID.randomUUID().toString(), tid, cid, d, "D", fid, billno, tjamt, d, false, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Join Fees for " + facname, caccount, tjamt, new Date(), true};
                dlfac.insertintoaccjoutnal0(value3);
                totalamt += tjamt;
            // dmang.updatebillno(fid);
            }
            dlfac.updatebillno(f.getid());
            dlfac.setmemberDebt(cid, f.getid(), totalamt);
        }
    }

    private void PermanentFacilities(Date date1, String custacc, String custid, List<Facility> fac) throws BasicException {
        for (Facility f : fac) {
            if (f.getrperiod() != null) {
                Periodicity p = dlfac.getPerioicitybyid(f.getrperiod());
                if (p.getqbtype() == false) {
                    postbilling(f, date1, custacc, custid);
                } else {
                    prebilling(f, date1, p, custid, custacc);
                }
            }
        }
    }

    //Sanjeev:min billing for new member
    private void insertMinimumUsageSpecificToMemType(Date date1, String custid, List<MinimumUsage> minusage) throws BasicException {
        for (MinimumUsage m : minusage) {
            Object[] value1 = new Object[]{UUID.randomUUID().toString(), true, custid, m.getMid(), date1, m_App.getAppUserView().getUser().getName(), date1, 0};
            new PreparedSentence(m_App.getSession(), "INSERT INTO MEMMINUSAGE(ID, ACTIVE, MEMNO, USAGETYPE, SDATE, CREATEDBY, CRDATE,STATUS) VALUES (?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.INT})).exec(value1);
        }
    }

    private void insertSingleMinimumUsageSpecificToMemType(Date date1, String custid, MinimumUsage minusage) throws BasicException {

        Object[] value1 = new Object[]{UUID.randomUUID().toString(), true, custid, minusage.getMid(), date1, m_App.getAppUserView().getUser().getName(), date1, 0};
        new PreparedSentence(m_App.getSession(), "INSERT INTO MEMMINUSAGE(ID, ACTIVE, MEMNO, USAGETYPE, SDATE, CREATEDBY, CRDATE,STATUS) VALUES (?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.INT})).exec(value1);

    }

    private void UpdateSingleMinimumUsageSpecificToMemType(String custid, MinimumUsage minusage) throws BasicException {

        Object[] value1 = new Object[]{new Date(), custid, minusage.getMid()};
        new PreparedSentence(m_App.getSession(), "UPDATE MEMMINUSAGE SET ACTIVE='FALSE',EDATE=? WHERE MEMNO=? AND USAGETYPE=?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.STRING, Datas.STRING})).exec(value1);

    }

    private void updateParent(int max, String parent) throws BasicException {
        new PreparedSentence(m_App.getSession(), "UPDATE ACCOUNTMASTER SET MAXIMUM=? WHERE SEARCHKEY=?",
                new SerializerWriteBasic(new Datas[]{Datas.INT, Datas.STRING})).exec(new Object[]{max, parent});
    }
    // private void getNewPermanentFacility(){

    //}
    public Object createValue() throws BasicException {
        Object[] customer = new Object[35];
        //  Transaction t=new Transaction(m_App.getSession()){
        //     public Object transact() throws BasicException {
        //     if(effectivedate.getText().length()>0){

        try {
            Date date1 = new Date();
            if (effectivedate.getText().length() <= 0) {
                date1.setTime(getdate(date1).getTime());
            } else {
                date1.setTime(getdate((Date) Formats.TIMESTAMP.parseValue(effectivedate.getText())).getTime());
            }
            //  Object[] customer = new Object[35];
            customerid = UUID.randomUUID().toString();
            customer[0] = m_oId == null ? customerid : m_oId;
            customer[1] = m_jTaxID.getText();
            customer[2] = m_jSearchkey.getText().trim().toUpperCase();
            customer[3] = m_jName.getText();
            customer[4] = m_jNotes.getText();

            boolean isvisible = Boolean.valueOf(m_jVisible.isSelected());
            customer[5] = isvisible;
            customer[6] = Formats.STRING.parseValue(jcard.getText()); // Format to manage NULL values
            customer[20] = jImageEditor1.getImage();

            customer[7] = Formats.STRING.parseValue(txtFirstName.getText());
            customer[8] = Formats.STRING.parseValue(txtLastName.getText());
            customer[9] = Formats.STRING.parseValue(txtEmail.getText());
            customer[10] = Formats.STRING.parseValue(txtPhone.getText());
            customer[11] = Formats.STRING.parseValue(txtPhone2.getText());
            customer[12] = Formats.STRING.parseValue(txtFax.getText());

            customer[13] = Formats.STRING.parseValue(txtAddress.getText());
            customer[14] = Formats.STRING.parseValue(txtAddress2.getText());
            customer[15] = Formats.STRING.parseValue(txtPostal.getText());
            customer[16] = Formats.STRING.parseValue(txtCity.getText());
            customer[17] = Formats.STRING.parseValue(txtRegion.getText());
            customer[18] = Formats.STRING.parseValue(txtCountry.getText());

            customer[19] = m_CategoryModel.getSelectedKey();
            String id;
            if (acctype.getSelectedIndex() != -1) {
                AccountMasterExt acc = (AccountMasterExt) acctype.getSelectedItem();
                String accparent = acc.getSerachkey();
                Object[] pid = dlcus.getaccountofcustomer(m_oId == null ? customerid : m_oId.toString());
                int flag = 0;
                if (pid != null) {
                    if (pid[0] == accparent) {
                        customer[21] = pid[1];
                        flag = 1;
                        if (pid[2] != null) {
                            id = pid[2].toString();
                        }
                    }
                }
                if (flag == 0) {
                    id = UUID.randomUUID().toString();
                    customer[21] = id;
                    String searchkey;
                    Object obj[] = (Object[]) new StaticSentence(m_App.getSession(), "SELECT MAXIMUM FROM ACCOUNTMASTER WHERE SEARCHKEY=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.INT})).find(accparent);
                    if (obj == null && obj[0] == null) {
                        searchkey = accparent + ".1";
                        updateParent(1, accparent);
                    } else {
                        int temp = (Integer.parseInt(obj[0].toString()) + 1);
                        searchkey = accparent + "." + temp;
                        updateParent(temp, accparent);
                    }
                    Date dnow = new Date();
                    Double obal = 0.0;
                    if (jComboBox1.getSelectedItem() == "credit") {
                        obal = Double.parseDouble(openbalance.getText()) * -1;
                    } else {
                        obal = Double.parseDouble(openbalance.getText());
                    }
                    Object[] value = new Object[]{id, searchkey, m_jSearchkey.getText() + " - " + m_jName.getText(), m_jSearchkey.getText(), acc.gettype(), acc.getsign(), false, false, acc.getSerachkey(), "S", dnow, obal, true};
                    new PreparedSentence(m_App.getSession(), "INSERT INTO ACCOUNTMASTER(ID,SEARCHKEY,NAME,DESCRIPTION,TYPE_,SIGN,DOCUMENT,SUMMARY,PARENT,LEVEL_,STARTDATE,OPENINGBALANCE,ACTIVE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.DOUBLE, Datas.BOOLEAN})).exec(value);
                //sanjeev:renamed type to type_,default to default_,level to level_
                } else if ((cus_skey != null && !cus_skey.equals(m_jSearchkey.getText().trim())) || (customerold[3] != null && !customerold[3].equals(m_jName.getText().trim()))) {
                    new PreparedSentence(m_App.getSession(), "UPDATE ACCOUNTMASTER SET NAME=?,DESCRIPTION=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING})).exec(new Object[]{m_jSearchkey.getText() + " - " + m_jName.getText(), m_jSearchkey.getText(), customer[21]});
                }
            }
            //sanjeev:checking for null account id
            if (customer[21] == null) {
                JOptionPane.showMessageDialog(this, "Under Account field cannot be null", "Cannot save", JOptionPane.ERROR_MESSAGE);
                return customerold;
            }
            customer[31] = Formats.TIMESTAMP.parseValue(joindate.getText());
          if (m_oId == null) {


                try {
                    customer[31] = Formats.TIMESTAMP.parseValue(joindate.getText());
                } catch (BasicException e) {
                    JOptionPane.showMessageDialog(this, "Please Enter a valid join date", "Cannot save", JOptionPane.ERROR_MESSAGE);
                    return customerold;
                }
                if (customer[31] == null) {
                    JOptionPane.showMessageDialog(this, "Join Date field cannot be null", "Cannot save", JOptionPane.ERROR_MESSAGE);
                    return customerold;
                }

            }
            Object stacc = new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE WHERE NAME = ? ", SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE).find("Service Tax Account");
            if (stacc != null) {
                servicetaxacc = stacc.toString();
            }
            if (m_oId == null || mtype == null) {
                if (memtype.getSelectedIndex() != -1) {
                    //  if(isvisible==true){
                    MemCat mtype1 = (MemCat) memtype.getSelectedItem();
                    List<Facility> fac = dlfac.getPermanentFacilitySpecifictToMemType().list("%" + mtype1.getID() + "%");
                    PermanentFacilities(date1, customer[21].toString(), customer[0].toString(), fac);
                    // Sanjev:minimum billing for new members
                    List<MinimumUsage> minusage = dlfac.getMinUsageSpecifictToMemType("%" + mtype1.getID() + "%");
                    insertMinimumUsageSpecificToMemType(date1, customer[0].toString(), minusage);
                //   }
                }
                customer[22] = m_memtype.getSelectedKey();
            } else {
                if (memtype.getSelectedIndex() != -1 && !mtype.equals(m_memtype.getSelectedKey().toString())) {
                    Date dnow = getDate(new Date());
                    MemCat mtype1 = (MemCat) memtype.getSelectedItem();
                    if (!m_memtype.getSelectedKey().toString().equals(mtype)) {
                        List<TempClass> list = dlfac.getPermanentFacilitiesTobeBilled().list(customer[0].toString());
                        Double ftotal = 0.0, gtotal = 0.0;
                        List<Facility> fac = dlfac.getPermanentFacilitySpecifictToMemType().list("%" + mtype1.getID() + "%");//new permanent facilities

                        //sanjeev:for changing memtype
                        List<MinimumUsage> minusageNew = dlfac.getMinUsageSpecifictToMemType("%" + mtype1.getID() + "%");//minusages for new memtype
                        List<MinimumUsage> minusageOld = dlfac.getMinUsageSpecifictToMemType("%" + mtype + "%");//minusages for new memtype

                        for (MinimumUsage muinfo : minusageNew) {
                            if (!minusageOld.contains(muinfo)) {
                                insertSingleMinimumUsageSpecificToMemType(date1, customer[0].toString(), muinfo);
                            }
                        }
                        for (MinimumUsage muinfo : minusageOld) {
                            if (!minusageNew.contains(muinfo)) {
                                UpdateSingleMinimumUsageSpecificToMemType(customer[0].toString(), muinfo);
                            }
                        }


                        for (TempClass tc : list) {
                            int flag = 0;
                            for (Facility fline : fac) {
                                if (fline.getid().equals(tc.getid())) {
                                    flag = 1;
                                    fac.remove(fline);
                                    break;
                                }
                            }
                            if (flag == 0) {
                                Facility f = dlfac.getFacilitybyID(tc.getid());
                                if (f.getrperiod() != null) {
                                    Periodicity p = dlfac.getPerioicitybyid(f.getrperiod());
                                    Calendar cal = Calendar.getInstance();//last bill date
                                    Calendar cal1 = Calendar.getInstance();//effectivedate
                                    Calendar cal2 = Calendar.getInstance();
                                    FacilityLogic flogic = new FacilityLogic(dlfac);
                                    cal.setTimeInMillis(tc.getlbilldate().getTime());
                                    cal1.setTimeInMillis(date1.getTime());
                                    cal1.setTimeInMillis(flogic.getBillableDate(p, cal1.get(Calendar.DATE), cal1).getTimeInMillis());
                                    Date edate = new Date();
                                    edate.setTime(cal1.getTimeInMillis());
                                    cal2.setTimeInMillis(cal.getTimeInMillis());
                                    cal2.add(Calendar.DATE, 1);
                                    int flag1 = 0;
                                    if (cal2.before(cal1)) {
                                        flogic.calculateEndDate(tc.getlbilldate(), p, cal.get(Calendar.DATE), 1, edate);
                                        DebtTypeTableModel.DebtTypeline periodtype = dlfac.getDebtTypebyid(f.getdueperiod());
                                        Date duedate = new Date();
                                        duedate.setTime(flogic.getDueDate(periodtype, flogic.getTemp()).getTime());
                                        String billno = dlfac.getnewbillno(tc.getid());
                                        String tid = UUID.randomUUID().toString();
                                        double taxrate = 0.0;
                                        TaxCategoryInfo tinfo = (TaxCategoryInfo) m_dlSales.getTaxCategoryByid(f.getservicetax());
                                        TaxInfo tax = taxeslogic.getTaxInfo(tinfo);
                                        taxrate = tax.getRate();
                                        if (tc.getlbilldate() == null) {
                                            double taxamt = Math.floor(tc.getjfee() * taxrate);
                                            if (taxamt > 0) {
                                                if (servicetaxacc == null) {
                                                    JOptionPane.showMessageDialog(null, "Service tax account is missing", "Error", JOptionPane.OK_OPTION);
                                                }
                                                Object[] value4 = new Object[]{UUID.randomUUID().toString(), tid, new Date(), "C", f.getid(), billno, taxamt, dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Service tax on Renewal Fees for " + f.getName(), servicetaxacc, 0.0, dnow, new Date(), true};
                                                dlfac.insertintoaccjoutnal3(value4);
                                            }
                                            Object[] value1 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "C", tc.getid(), billno, tc.getjfee(), dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), tc.getname(), tc.getjacc(), 0.0, dnow, true};
                                            dlfac.insertintoaccjoutnal1(value1);
                                            Object[] value = new Object[]{UUID.randomUUID().toString(), tid, customer[0], dnow, "D", tc.getid(), billno, tc.getjfee() + taxamt, duedate, false, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "joining fee", customer[21], tc.getjfee() + taxamt, true};
                                            dlfac.insertintoaccjoutnal(value);
                                            gtotal = f.getjamt();
                                        }
                                        double ramt = 0.0;
                                        if (flogic.getPnum() > 0) {
                                            ramt = (flogic.getPnum() - 1) * f.getramt();
                                            double taxamt = Math.floor(ramt * taxrate);
                                            if (taxamt > 0) {
                                                if (servicetaxacc == null) {
                                                    JOptionPane.showMessageDialog(null, "Service tax account is missing", "Error", JOptionPane.OK_OPTION);
                                                }
                                                Object[] value4 = new Object[]{UUID.randomUUID().toString(), tid, new Date(), "C", f.getid(), billno, taxamt, dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Service tax on Renewal Fees for " + f.getName(), servicetaxacc, 0.0, dnow, new Date(), true};
                                                dlfac.insertintoaccjoutnal3(value4);
                                            }
                                            if (ramt > 0) {
                                                Object[] value1 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "C", tc.getid(), billno, ramt, dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), tc.getname(), tc.getracc(), 0.0, dnow, true};
                                                dlfac.insertintoaccjoutnal1(value1);
                                                Object[] value = new Object[]{UUID.randomUUID().toString(), tid, customer[0], dnow, "D", tc.getid(), billno, ramt + taxamt, duedate, false, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Renewal fees upto ", customer[21], ramt + taxamt, true};
                                                dlfac.insertintoaccjoutnal(value);
                                            }
                                        }
                                        gtotal += ramt;
                                        dlfac.updatebillno(tc.getid());
                                        dlfac.setmemberDebt(customer[0].toString(), f.getid(), gtotal);
                                    } else if (cal2.after(cal1)) {
                                        JOptionPane.showMessageDialog(this, f.getName() + " is already bill till " + Formats.DATE.formatValue(tc.getlbilldate()), "Warining...", JOptionPane.WARNING_MESSAGE);
                                    }
                                    dlfac.DeactivateMemberFacility(new Object[]{customer[0], tc.getid()});
                                }
                            }
                        }
                        PermanentFacilities(date1, customer[21].toString(), customer[0].toString(), fac);
                    }
                    customer[22] = m_memtype.getSelectedKey();
                } else {
                    customer[22] = m_memtype.getSelectedKey();
                }
            }
            customer[23] = sowo.getText();
            customer[24] = mobile.getText();
            customer[25] = m_stax.getSelectedKey();
            customer[26] = m_etax.getSelectedKey();
            customer[27] = jImageEditor2.getImage();
            customer[28] = m_sponsor1.getSelectedKey();
            customer[29] = m_sponsor2.getSelectedKey();
            customer[30] = m_sponsor3.getSelectedKey();

            customer[32] = Formats.TIMESTAMP.parseValue(terminationdate.getText());
            Object d = null;
            if (date.getSelectedItem() != null) {
                d = getDate(date.getSelectedItem().toString(), month.getSelectedIndex(), year.getText());
            }
            customer[33] = d;
            if (jComboBox1.getSelectedItem() == "credit") {
                customer[34] = Double.parseDouble(openbalance.getText()) * -1;
            } else {
                customer[34] = Double.parseDouble(openbalance.getText());
            }
            double diff = prevopeningbal - Double.parseDouble(openbalance.getText());
        /*if(diff<0){
        String tid=UUID.randomUUID().toString();
        if(jComboBox1.getSelectedItem()=="credit"){
        Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,customer[0],new Date(),"C","Opening Balance","Opening bal",diff*-1,null,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Opening Balance",customer[21],diff*-1,true};
        dlfac.insertintoaccjoutnal(value1);
        Object[] value2=new Object[]{UUID.randomUUID().toString(),tid,customer[0],new Date(),"D","Opening Balance","Opening bal",diff*-1,null,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Opening Balance","5",0.0,true};
        dlfac.insertintoaccjoutnal(value2);
        }else{
        Object[] value1=new Object[]{UUID.randomUUID().toString(),tid,customer[0],new Date(),"D","Opening Balance","Opening bal",diff*-1,null,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Opening Balance",customer[21],diff*-1,true};
        dlfac.insertintoaccjoutnal(value1);
        Object[] value2=new Object[]{UUID.randomUUID().toString(),tid,customer[0],new Date(),"C","Opening Balance","Opening bal",diff*-1,null,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Opening Balance","5",diff*-1,true};
        dlfac.insertintoaccjoutnal(value2);
        }
        }else if(diff>0){

        }*/
        } catch (Exception e) {
            customer = customerold;
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error ", "Error", JOptionPane.WARNING_MESSAGE);
        }

        return customer;
    //  }
    //  };
    //}else{
    //}
    }

    public Component getComponent() {
        return this;
    }

    private Date getDate(String date, int month, String year) {
        // Date d=new Date();
        Calendar cal = Calendar.getInstance();
        // cal.setTimeInMillis(d.getTime());
        cal.set(Integer.parseInt(year), month - 1, Integer.parseInt(date));
        Date d = new Date();
        d.setTime(cal.getTimeInMillis());
        return d;

    }

    private Date getDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        d.setTime(cal.getTimeInMillis());
        return d;
    }

    private void reset() {
        dname.setText(null);
        ddate.setSelectedIndex(-1);
        dmonth.setSelectedIndex(-1);
        dyear.setText(null);
        ddoj.setText(null);
        dnum.setText(null);
        dtypelist.setSelectedIndex(-1);
        rmemno.setText(null);
        rmemname.setText(null);
        linktype.setText(null);
        linktypelist.setSelectedIndex(-1);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jVisible = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jcard = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        m_jTaxID = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jSearchkey = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtFax = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtPhone2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        mobile = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtCountry = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtAddress2 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtPostal = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtRegion = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jNotes = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        serviceTaxcat = new javax.swing.JComboBox();
        entTaxcat = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        joindate = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        terminationdate = new javax.swing.JTextField();
        sponsor1 = new javax.swing.JComboBox();
        sponsor2 = new javax.swing.JComboBox();
        sponsor3 = new javax.swing.JComboBox();
        jButton10 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jImageEditor1 = new com.openbravo.data.gui.JImageEditor();
        jLabel2 = new javax.swing.JLabel();
        jImageEditor2 = new com.openbravo.data.gui.JImageEditor();
        jLabel6 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        dname = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        dtypelist = new javax.swing.JComboBox();
        ddoj = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        dyear = new javax.swing.JTextField();
        ddate = new javax.swing.JComboBox();
        dmonth = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        dnum = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        rmemname = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        linktypelist = new javax.swing.JComboBox();
        jButton6 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        rmemno = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        editIndicator1 = new javax.swing.JLabel();
        linktype = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        acctypelabel = new javax.swing.JLabel();
        acctype = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        memtype = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        date = new javax.swing.JComboBox();
        month = new javax.swing.JComboBox();
        year = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        sowo = new javax.swing.JTextField();
        openbalancelabel = new javax.swing.JLabel();
        openbalance = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel40 = new javax.swing.JLabel();
        effectivedate = new javax.swing.JTextField();
        effdate_btn = new javax.swing.JButton();

        setAutoscrolls(true);
        setLayout(null);

        jLabel3.setText(AppLocal.getIntString("label.name")); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(20, 50, 140, 14);
        add(m_jName);
        m_jName.setBounds(200, 50, 270, 20);

        jLabel4.setText(AppLocal.getIntString("label.visible")); // NOI18N
        add(jLabel4);
        jLabel4.setBounds(20, 200, 140, 14);
        add(m_jVisible);
        m_jVisible.setBounds(200, 200, 110, 20);

        jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N
        add(jLabel5);
        jLabel5.setBounds(20, 110, 140, 14);

        jcard.setEditable(false);
        add(jcard);
        jcard.setBounds(200, 110, 270, 20);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line16.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        jButton2.setBounds(480, 110, 49, 25);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileclose.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3);
        jButton3.setBounds(540, 110, 49, 25);
        add(m_jTaxID);
        m_jTaxID.setBounds(200, 170, 140, 20);

        jLabel7.setText(AppLocal.getIntString("label.taxid")); // NOI18N
        add(jLabel7);
        jLabel7.setBounds(20, 170, 140, 14);

        jLabel8.setText(AppLocal.getIntString("label.searchkey")); // NOI18N
        add(jLabel8);
        jLabel8.setBounds(20, 20, 140, 14);
        add(m_jSearchkey);
        m_jSearchkey.setBounds(200, 20, 270, 20);

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setLayout(null);

        jLabel14.setText(AppLocal.getIntString("label.fax")); // NOI18N
        jPanel1.add(jLabel14);
        jLabel14.setBounds(10, 130, 140, 14);
        jPanel1.add(txtFax);
        txtFax.setBounds(150, 130, 270, 20);

        jLabel15.setText(AppLocal.getIntString("label.lastname")); // NOI18N
        jPanel1.add(jLabel15);
        jLabel15.setBounds(10, 40, 140, 14);
        jPanel1.add(txtLastName);
        txtLastName.setBounds(150, 40, 270, 20);

        jLabel16.setText(AppLocal.getIntString("label.email")); // NOI18N
        jPanel1.add(jLabel16);
        jLabel16.setBounds(10, 70, 140, 14);
        jPanel1.add(txtEmail);
        txtEmail.setBounds(150, 70, 270, 20);

        jLabel17.setText(AppLocal.getIntString("label.phone")); // NOI18N
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 100, 140, 14);
        jPanel1.add(txtPhone);
        txtPhone.setBounds(150, 100, 150, 20);

        jLabel18.setText(AppLocal.getIntString("label.phone2")); // NOI18N
        jPanel1.add(jLabel18);
        jLabel18.setBounds(310, 100, 70, 14);
        jPanel1.add(txtPhone2);
        txtPhone2.setBounds(380, 100, 160, 20);

        jLabel19.setText(AppLocal.getIntString("label.firstname")); // NOI18N
        jPanel1.add(jLabel19);
        jLabel19.setBounds(10, 10, 140, 14);
        jPanel1.add(txtFirstName);
        txtFirstName.setBounds(150, 10, 270, 20);

        jLabel11.setText("Mobile");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(10, 160, 110, 14);
        jPanel1.add(mobile);
        mobile.setBounds(150, 160, 270, 20);

        jTabbedPane1.addTab(AppLocal.getIntString("label.contact"), jPanel1); // NOI18N

        jPanel2.setLayout(null);

        jLabel13.setText(AppLocal.getIntString("label.address")); // NOI18N
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 10, 140, 14);
        jPanel2.add(txtAddress);
        txtAddress.setBounds(150, 10, 270, 20);

        jLabel20.setText(AppLocal.getIntString("label.country")); // NOI18N
        jPanel2.add(jLabel20);
        jLabel20.setBounds(10, 160, 140, 14);
        jPanel2.add(txtCountry);
        txtCountry.setBounds(150, 160, 270, 20);

        jLabel21.setText(AppLocal.getIntString("label.address2")); // NOI18N
        jPanel2.add(jLabel21);
        jLabel21.setBounds(10, 40, 140, 14);
        jPanel2.add(txtAddress2);
        txtAddress2.setBounds(150, 40, 270, 20);

        jLabel22.setText(AppLocal.getIntString("label.postal")); // NOI18N
        jPanel2.add(jLabel22);
        jLabel22.setBounds(10, 70, 140, 14);
        jPanel2.add(txtPostal);
        txtPostal.setBounds(150, 70, 270, 20);

        jLabel23.setText(AppLocal.getIntString("label.city")); // NOI18N
        jPanel2.add(jLabel23);
        jLabel23.setBounds(10, 100, 140, 14);
        jPanel2.add(txtCity);
        txtCity.setBounds(150, 100, 270, 20);

        jLabel24.setText(AppLocal.getIntString("label.region")); // NOI18N
        jPanel2.add(jLabel24);
        jLabel24.setBounds(10, 130, 140, 14);
        jPanel2.add(txtRegion);
        txtRegion.setBounds(150, 130, 270, 20);

        jTabbedPane1.addTab(AppLocal.getIntString("label.location"), jPanel2); // NOI18N

        jPanel3.setLayout(null);

        jScrollPane1.setViewportView(m_jNotes);

        jPanel3.add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 520, 160);

        jTabbedPane1.addTab(AppLocal.getIntString("label.notes"), jPanel3); // NOI18N

        jPanel4.setLayout(null);

        jLabel10.setText("Service Tax Category");
        jPanel4.add(jLabel10);
        jLabel10.setBounds(10, 10, 110, 14);

        jLabel12.setText("Ent. Tax Category");
        jPanel4.add(jLabel12);
        jLabel12.setBounds(290, 10, 100, 14);

        jPanel4.add(serviceTaxcat);
        serviceTaxcat.setBounds(140, 10, 140, 20);

        jPanel4.add(entTaxcat);
        entTaxcat.setBounds(400, 10, 140, 20);

        jLabel25.setText("Sponsor 1");
        jPanel4.add(jLabel25);
        jLabel25.setBounds(10, 40, 110, 14);

        jLabel26.setText("Sponsor 2");
        jPanel4.add(jLabel26);
        jLabel26.setBounds(10, 70, 90, 14);

        jLabel27.setText("Sponsor 3");
        jPanel4.add(jLabel27);
        jLabel27.setBounds(10, 100, 100, 14);

        jLabel28.setText("Join Date");
        jPanel4.add(jLabel28);
        jLabel28.setBounds(10, 130, 100, 14);
        jPanel4.add(joindate);
        joindate.setBounds(140, 130, 180, 20);

        jLabel29.setText("Termination Date");
        jPanel4.add(jLabel29);
        jLabel29.setBounds(10, 160, 110, 14);

        terminationdate.setEditable(false);
        jPanel4.add(terminationdate);
        terminationdate.setBounds(140, 160, 180, 20);

        jPanel4.add(sponsor1);
        sponsor1.setBounds(140, 40, 180, 20);

        jPanel4.add(sponsor2);
        sponsor2.setBounds(140, 70, 180, 20);

        jPanel4.add(sponsor3);
        sponsor3.setBounds(140, 100, 180, 20);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton10);
        jButton10.setBounds(340, 130, 40, 20);

        jTabbedPane1.addTab("Other Info", jPanel4);

        jPanel5.setLayout(null);
        jPanel5.add(jImageEditor1);
        jImageEditor1.setBounds(30, 20, 230, 150);

        jLabel2.setText("Photo");
        jPanel5.add(jLabel2);
        jLabel2.setBounds(10, 0, 80, 14);
        jPanel5.add(jImageEditor2);
        jImageEditor2.setBounds(300, 20, 220, 150);

        jLabel6.setText("Signature");
        jPanel5.add(jLabel6);
        jLabel6.setBounds(290, 0, 90, 14);

        jTabbedPane1.addTab("Image & Signature", jPanel5);

        jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane2StateChanged(evt);
            }
        });

        jPanel6.setLayout(null);

        jLabel32.setText("Name");
        jPanel6.add(jLabel32);
        jLabel32.setBounds(10, 10, 90, 14);
        jPanel6.add(dname);
        dname.setBounds(110, 10, 190, 20);

        jLabel33.setText("Type");
        jPanel6.add(jLabel33);
        jLabel33.setBounds(10, 100, 80, 14);

        jLabel34.setText("Date Of Joining");
        jPanel6.add(jLabel34);
        jLabel34.setBounds(10, 70, 90, 20);

        jPanel6.add(dtypelist);
        dtypelist.setBounds(110, 100, 190, 20);
        jPanel6.add(ddoj);
        ddoj.setBounds(110, 70, 190, 20);

        jLabel35.setText("Date Of Birth");
        jPanel6.add(jLabel35);
        jLabel35.setBounds(10, 40, 80, 14);
        jPanel6.add(dyear);
        dyear.setBounds(260, 40, 40, 20);

        jPanel6.add(ddate);
        ddate.setBounds(110, 40, 40, 20);

        jPanel6.add(dmonth);
        dmonth.setBounds(160, 40, 90, 20);

        jButton4.setText("Add");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton4);
        jButton4.setBounds(270, 140, 73, 23);

        jLabel39.setText("Number");
        jPanel6.add(jLabel39);
        jLabel39.setBounds(330, 10, 70, 14);

        dnum.setEditable(false);
        jPanel6.add(dnum);
        dnum.setBounds(410, 10, 80, 20);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1);
        jButton1.setBounds(320, 70, 40, 25);

        jTabbedPane2.addTab("Add", jPanel6);

        jPanel7.setLayout(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jPanel7.add(jScrollPane2);
        jScrollPane2.setBounds(1, 5, 490, 170);
        jPanel7.add(jTabbedPane3);
        jTabbedPane3.setBounds(380, -20, 5, 5);

        jButton5.setText("jButton5");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton5);
        jButton5.setBounds(320, 177, 73, 20);

        jButton7.setText("Deactive");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton7);
        jButton7.setBounds(400, 176, 90, 20);

        jTabbedPane2.addTab("List", jPanel7);

        jTabbedPane1.addTab("Member Dependent", jTabbedPane2);

        jTabbedPane4.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane4StateChanged(evt);
            }
        });

        jPanel8.setLayout(null);

        jLabel36.setText("Name");
        jPanel8.add(jLabel36);
        jLabel36.setBounds(10, 50, 60, 14);
        jPanel8.add(rmemname);
        rmemname.setBounds(80, 50, 180, 20);

        jLabel37.setText("Type");
        jPanel8.add(jLabel37);
        jLabel37.setBounds(10, 80, 60, 14);

        jPanel8.add(linktypelist);
        linktypelist.setBounds(80, 110, 180, 20);

        jButton6.setText("Add");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton6);
        jButton6.setBounds(250, 150, 73, 23);

        jLabel38.setText("Mem No");
        jPanel8.add(jLabel38);
        jLabel38.setBounds(10, 20, 60, 14);

        rmemno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rmemnoKeyPressed(evt);
            }
        });
        jPanel8.add(rmemno);
        rmemno.setBounds(80, 20, 180, 20);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton9);
        jButton9.setBounds(270, 50, 50, 25);

        editIndicator1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/greenled.png"))); // NOI18N
        jPanel8.add(editIndicator1);
        editIndicator1.setBounds(270, 20, 30, 20);
        jPanel8.add(linktype);
        linktype.setBounds(80, 80, 180, 20);

        jTabbedPane4.addTab("Add", jPanel8);

        jPanel9.setLayout(null);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        jPanel9.add(jScrollPane3);
        jScrollPane3.setBounds(2, 0, 490, 170);

        jButton8.setText("Edit");
        jButton8.setEnabled(false);
        jPanel9.add(jButton8);
        jButton8.setBounds(420, 170, 51, 23);

        jTabbedPane4.addTab("List", jPanel9);

        jTabbedPane1.addTab("Member Link", jTabbedPane4);

        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 330, 570, 230);

        jLabel9.setText(AppLocal.getIntString("label.custtaxcategory")); // NOI18N
        add(jLabel9);
        jLabel9.setBounds(20, 140, 170, 14);
        add(m_jCategory);
        m_jCategory.setBounds(200, 140, 270, 20);

        acctypelabel.setText("Under Account");
        add(acctypelabel);
        acctypelabel.setBounds(20, 300, 110, 14);

        add(acctype);
        acctype.setBounds(200, 300, 220, 20);

        jLabel1.setText("Membership Type");
        add(jLabel1);
        jLabel1.setBounds(20, 270, 110, 14);

        add(memtype);
        memtype.setBounds(200, 270, 220, 20);

        jLabel30.setText("Date Of Birth");
        add(jLabel30);
        jLabel30.setBounds(20, 230, 110, 14);

        add(date);
        date.setBounds(200, 230, 40, 20);

        add(month);
        month.setBounds(250, 230, 90, 20);
        add(year);
        year.setBounds(360, 230, 60, 20);

        jLabel31.setText("Son Of/Wife Of");
        add(jLabel31);
        jLabel31.setBounds(20, 80, 110, 14);
        add(sowo);
        sowo.setBounds(200, 80, 270, 20);

        openbalancelabel.setText("Opening Bal");
        add(openbalancelabel);
        openbalancelabel.setBounds(350, 200, 70, 14);
        add(openbalance);
        openbalance.setBounds(440, 200, 70, 20);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "credit", "debit" }));
        add(jComboBox1);
        jComboBox1.setBounds(520, 200, 80, 20);

        jLabel40.setText("Effective Date");
        add(jLabel40);
        jLabel40.setBounds(350, 170, 80, 14);
        add(effectivedate);
        effectivedate.setBounds(440, 170, 120, 20);

        effdate_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        effdate_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                effdate_btnActionPerformed(evt);
            }
        });
        add(effdate_btn);
        effdate_btn.setBounds(570, 170, 30, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardnew"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            jcard.setText("c" + StringUtils.getCardNumber());
            m_Dirty.setDirty(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardremove"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            jcard.setText(null);
            m_Dirty.setDirty(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTabbedPane4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane4StateChanged
        // TODO add your handling code here:
        try {
            javax.swing.JTabbedPane tabpane = (javax.swing.JTabbedPane) evt.getSource();
            int count = tabpane.getSelectedIndex();
            if (count == 0) {
            } else if (count == 1) {
                mlmodel = MemLinkTableModel.loadInstance(m_App, customerid);
                jTable1.setModel(mlmodel.getTableModel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTabbedPane4StateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        try {
            if (m_jSearchkey.getText().length() > 0 && (linktypelist.getSelectedIndex() != 0 || linktype.getText().length() > 0)) {
                String memltype;
                Object del = linktypelist.getSelectedItem();
                if (linktypelist.getSelectedItem() != null) {
                    memltype = linktypelist.getSelectedItem().toString();
                } else {
                    memltype = linktype.getText();
                    new PreparedSentence(m_App.getSession(), "INSERT INTO MEMLINKTYPE(ID,NAME,ACTIVE) VALUES(?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.BOOLEAN})).exec(new Object[]{UUID.randomUUID().toString(), memltype, true});
                }
                Object params[] = new Object[]{UUID.randomUUID().toString(), lcusInfo.getId(), customerid, true, memltype};
                new PreparedSentence(m_App.getSession(), "INSERT INTO MEMLINK(ID,MEMNO1,MEMNO2,ACTIVE,LINKTYPE) VALUES(?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING})).exec(params);
                rmemno.setText(null);
                rmemname.setText(null);
                linktype.setText(null);
                linktypelist.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member first", null, JOptionPane.OK_OPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed
    private CustomerInfo lcusInfo;
    private void rmemnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rmemnoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyText(evt.getKeyCode()).equals("Enter")) {
            editIndicator1.setVisible(false);
            String mno = rmemno.getText();
            try {
                Object[] obj = dlfac.getMamberbySkey(mno.toUpperCase());
                if (obj == null) {
                    JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                } else {
                    lcusInfo = new CustomerInfo(obj[0].toString());
                    lcusInfo.setName(obj[1].toString());
                    lcusInfo.setSearchkey(mno.toUpperCase());
                    //lcusInfo.setMemType(obj[2]);
                    rmemname.setText(obj[1].toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editIndicator1.setVisible(true);
        }
    }//GEN-LAST:event_rmemnoKeyPressed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlcus);
        //finder.search(m_oTicket.getCustomer());
        finder.setVisible(true);
        //  CustomerInfoExt customer;
        lcusInfo = finder.getSelectedCustomer();
        if (lcusInfo != null) {
            try {
                // customer = dlSales.loadCustomerExt(customerInfo.getId());
                rmemname.setText(lcusInfo.toString());
                rmemno.setText(lcusInfo.getSearchkey());
                editIndicator1.setVisible(false);
            } catch (Exception e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
                msg.show(this);
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        try {
            if (dname.getText().length() > 0 && dtypelist.getSelectedIndex() != -1 && dnum.getText().length() > 0) {

                Date d = null;
                if (ddate.getSelectedIndex() > 0 && dmonth.getSelectedIndex() > 0 && dyear.getText().length() > 0) {
                    d = getDate(ddate.getSelectedItem().toString(), dmonth.getSelectedIndex(), dyear.getText());
                }
                Object params[] = new Object[]{UUID.randomUUID().toString(), customerid, dnum.getText(), dname.getText(), true, d, Formats.TIMESTAMP.parseValue(ddoj.getText()), dtypelist.getSelectedItem()};
                new PreparedSentence(m_App.getSession(), "INSERT INTO MEMDEPENDENT(ID,MEMNO,DMEMNO,DNAME,ACTIVE,DOB,DOJ,DTYPE) VALUES(?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING})).exec(params);

                dname.setText(null);
                ddate.setSelectedIndex(-1);
                dmonth.setSelectedIndex(-1);
                dyear.setText(null);
                ddoj.setText(null);
                dnum.setText(null);
                dtypelist.setSelectedIndex(-1);
                mdmodel = MemberDependentTableModel.loadInstance(m_App, customerid);
                jTable1.setModel(mdmodel.getTableModel());
                int rowno = jTable1.getRowCount();
                dnum.setText(m_jSearchkey.getText() + "\\" + rowno);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member first", null, JOptionPane.OK_OPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        try {
            javax.swing.JTabbedPane tabpane = (javax.swing.JTabbedPane) evt.getSource();
            int num = tabpane.getSelectedIndex();
            if (num == 5) {
                dtypelist.setSelectedIndex(-1);
                jTabbedPane2.setSelectedIndex(1);
                jTabbedPane2.setSelectedIndex(0);
                revalidate();
            } else if (num == 6) {
                List mltype = dlfac.getMemLinkType();
                mltype.add(0, null);
                m_memdtype = new ComboBoxValModel(mltype);
                linktypelist.setModel(m_memdtype);
                linktypelist.setSelectedIndex(0);
                revalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(ddoj.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            date = getDate(date);
            ddoj.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(joindate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            date = getDate(date);
            joindate.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        // TODO add your handling code here:
        try {
            //  javax.swing.JTabbedPane tabpane=(javax.swing.JTabbedPane)evt.getSource();
            //  int num=tabpane.getSelectedIndex();
            //  if(num==1){
            mdmodel = MemberDependentTableModel.loadInstance(m_App, customerid);
            jTable1.setModel(mdmodel.getTableModel());
            //  }
            int rowno = jTable1.getRowCount();

            dnum.setText(m_jSearchkey.getText() + "\\" + rowno);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTabbedPane2StateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void effdate_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_effdate_btnActionPerformed
        // TODO add your handling code here:
        Date datenow;
        try {
            datenow = (Date) Formats.TIMESTAMP.parseValue(effectivedate.getText());
        } catch (BasicException e) {
            datenow = null;
        }
        datenow = JCalendarDialog.showCalendar(this, datenow);
        if (datenow != null) {
            effectivedate.setText(Formats.DATE.formatValue(datenow));
        }
}//GEN-LAST:event_effdate_btnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox acctype;
    private javax.swing.JLabel acctypelabel;
    private javax.swing.JComboBox date;
    private javax.swing.JComboBox ddate;
    private javax.swing.JTextField ddoj;
    private javax.swing.JComboBox dmonth;
    private javax.swing.JTextField dname;
    private javax.swing.JTextField dnum;
    private javax.swing.JComboBox dtypelist;
    private javax.swing.JTextField dyear;
    private javax.swing.JLabel editIndicator1;
    private javax.swing.JButton effdate_btn;
    private javax.swing.JTextField effectivedate;
    private javax.swing.JComboBox entTaxcat;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private com.openbravo.data.gui.JImageEditor jImageEditor1;
    private com.openbravo.data.gui.JImageEditor jImageEditor2;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jcard;
    private javax.swing.JTextField joindate;
    private javax.swing.JTextField linktype;
    private javax.swing.JComboBox linktypelist;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextArea m_jNotes;
    private javax.swing.JTextField m_jSearchkey;
    private javax.swing.JTextField m_jTaxID;
    private javax.swing.JCheckBox m_jVisible;
    private javax.swing.JComboBox memtype;
    private javax.swing.JTextField mobile;
    private javax.swing.JComboBox month;
    private javax.swing.JTextField openbalance;
    private javax.swing.JLabel openbalancelabel;
    private javax.swing.JTextField rmemname;
    private javax.swing.JTextField rmemno;
    private javax.swing.JComboBox serviceTaxcat;
    private javax.swing.JTextField sowo;
    private javax.swing.JComboBox sponsor1;
    private javax.swing.JComboBox sponsor2;
    private javax.swing.JComboBox sponsor3;
    private javax.swing.JTextField terminationdate;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAddress2;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCountry;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFax;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPhone2;
    private javax.swing.JTextField txtPostal;
    private javax.swing.JTextField txtRegion;
    private javax.swing.JTextField year;
    // End of variables declaration//GEN-END:variables
}
