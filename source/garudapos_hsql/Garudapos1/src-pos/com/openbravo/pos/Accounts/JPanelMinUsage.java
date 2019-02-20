package com.openbravo.pos.Accounts;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerReadInteger;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.Accounts.BillPeriods;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.clubmang.DataSourceProvider;
import com.openbravo.pos.clubmang.DebtTypeTableModel;
import com.openbravo.pos.clubmang.Facility;
import com.openbravo.pos.clubmang.FacilityLogic;
import com.openbravo.pos.clubmang.JRBasicField;
import com.openbravo.pos.clubmang.MemCat;
import com.openbravo.pos.clubmang.Minusage;
import com.openbravo.pos.clubmang.Periodicity;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.TaxInfo;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperPrint;
import com.openbravo.pos.clubmang.JasperReportNew;
import com.openbravo.pos.forms.CustomerInfo1;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author swathi
 */
public class JPanelMinUsage extends javax.swing.JPanel implements JPanelView, BeanFactoryApp {

    private AppView m_App;
    private ComboBoxValModel periodtypemodel;
    private ComboBoxValModel facilitiesmodel;
    private ComboBoxValModel minusagetypemodel;
    private ComboBoxValModel MemType;
    private ComboBoxValModel accmodel;
    private DataLogicFacilities dfac;
    private MinUsageDataModel mmodel1;
    private ComboBoxValModel debttypemodel;
    private ComboBoxValModel staxmodel;
    private DataLogicSales m_dlSales;
    private Date dnow;
    private Date billingDate;
    private List facilitylist;
    private List periods;
    private List memusagelist;
    private List mlist1;
    private List mlist2;
    private List<MemCat> mlist4;
    private List<AccountMasterExt> alist;
    private List<Minusage> mlist3;
    private FacilityTypeListModel mltmodel;
    private FacilityTypeListModel mltmodel1;
    private MemTypeListModel mltmodel2;
    private List memtypelist;
    private List memtypelist1;
    private List memtypelist2;
    //private double taxrate;
    // private double tax1 = .103;
    private List memtype;
    private String ftype;
    private String utype;
    private String type;
    private String mtype;
    private String billnum;
    private String period;
    private String name;
    private MinUsageModel minmodel;
    private TaxesLogic taxeslogic;
    private Double totalsum = 0.0;
    private Double taxrate1 = 0.0;
    private Double taxtotal = 0.0;
    private Double rate = 0.0;
    private Double tax = 0.0;
    private boolean billed = false;
    Map chargableCustomers = new HashMap();
    List<MinUsageCustomer> custAll = new ArrayList<MinUsageCustomer>();
    String showBillNos = "";

    public void init(AppView app) throws BeanFactoryException {
        m_App = app;
        dfac = (DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        jTextField2.setEnabled(false);
        jTextField3.setEnabled(false);


    }

    public void activate() throws BasicException {
        jButton11.setVisible(false);
        taxeslogic = new TaxesLogic(m_dlSales.getTaxList().list());
        dnow = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dnow.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        dnow.setTime(cal.getTimeInMillis());
        loadData();
        jButton13.setEnabled(false);
    }

    /** Creates new form JPanelMinUsage */
    public JPanelMinUsage() {
        initComponents();
    }

    public double getTotal() {
        return totalsum;
    }

    public double getTaxtotal() {
        return taxtotal;
    }

    public String getBillNo() {
        return billnum;
    }

    public String getPeriod() {
        return period;
    }

    public String getName1() {
        return name;
    }

    public void loadData() throws BasicException {

        List mlist1 = dfac.getFacility();
        facilitiesmodel = new ComboBoxValModel(mlist1);
        jComboBox1.setModel(facilitiesmodel);
        List mlist2 = dfac.getPeriodicity().list();
        periodtypemodel = new ComboBoxValModel(mlist2);
        jComboBox2.setModel(periodtypemodel);
        List mlist3 = dfac.getMinUsage();
        minusagetypemodel = new ComboBoxValModel(mlist3);
        jComboBox3.setModel(minusagetypemodel);

        List alist = dfac.getaccounts();
        accmodel = new ComboBoxValModel(alist);
        jComboBox5.setModel(accmodel);
        List mlist4 = dfac.getMemberCategory();
        mlist4.add(0, "ALL");
        MemType = new ComboBoxValModel(mlist4);
        jComboBox4.setModel(MemType);
        jTabbedPane1.setSelectedIndex(0);
        debttypemodel = new ComboBoxValModel(dfac.getDebtType());
        jComboBox6.setModel(debttypemodel);
        List staxlist = m_dlSales.getTaxCategoriesList().list();
        staxlist.add(0, null);
        staxmodel = new ComboBoxValModel(staxlist);
        jComboBox7.setModel(staxmodel);
        //list2.add(0,"Monthly");
        //list2.add(1,"Quarterly");
        //list2.add(2,"Yearly");
        //periodtypemodel=new ComboBoxValModel(list2);
        //jComboBox2.setModel(periodtypemodel);
        memtypelist = new ArrayList();
        memtypelist1 = new ArrayList();
        memtype = new ArrayList();
        jTextField8.setText(Formats.TIMESTAMP.formatValue(new Date()));
        jTextField5.setText(Formats.TIMESTAMP.formatValue(new Date()));
        minmodel = MinUsageModel.loadInstance(m_App, 1);
        jTable2.setModel(minmodel.getMinUsageModel());
    }

    private void createbill(MinUsageCustomer cust, String billno, Minusage mintemp, String tid, DebtTypeTableModel.DebtTypeline dueperiod, TaxInfo taxinfo) throws BasicException {
        //  Date d=new Date();
        Object mobile = cust.getMobile();
        String customeraccount = cust.getAccount();
        String user = cust.getName();
        String memid = cust.getCid();
        Date bdate = cust.getBillDate();
        String narration = cust.getNarration();
        Timestamp bt = new Timestamp(bdate.getTime());


        FacilityLogic fl = new FacilityLogic();
        Date duedate = fl.getDueDate(dueperiod, bt);


        Double camount = cust.getCamtTotal();
        rate = dfac.roundTwoDecimals(camount);
        totalsum += camount;



        tax = taxinfo.getRate();
        taxrate1 = tax * rate;
        taxtotal += taxrate1;
        camount = camount + taxrate1;




        Object[] value = new Object[]{UUID.randomUUID().toString(), tid, memid, bdate, "D", mintemp.getName(), billno, camount, duedate, false, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), narration, customeraccount, camount, true};
        dfac.insertintoaccjoutnal(value);

        String smsmsg = "Dear Member,\rYour a/c with us has been debited by Rs " + dfac.ConvertDoubleToString(camount) + " for " + mintemp.getName() + " on " + Formats.DATE.formatValue(duedate) + " bill no " + billno + ".Thank u";
        if (mobile != null && mobile.toString().trim().length() == 10) {
            dfac.updatetosendMsg(smsmsg, memid, mobile.toString(), 2);
        }

    }

    public void showDialog(MinUsageDataModel mmodel1, AppView m_App) {

        this.m_App = m_App;
        jScrollPane4.setAutoscrolls(true);
        /*if(rflist.size()>1)
        jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
        else
        jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_SUBSEQUENT_COLUMNS);*/
        jTable1.setModel(mmodel1.getUsageModel());
        this.mmodel1 = mmodel1;
        int columncnt = jTable1.getColumnModel().getColumnCount();
        if (columncnt > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(150);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(150);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
            for (int k = 2; k < columncnt; k++) {
                if ((k - 2) % 3 == 0) {
                    jTable1.getColumnModel().getColumn(k).setMaxWidth(150);
                    jTable1.getColumnModel().getColumn(k).setPreferredWidth(150);
                //jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
                } else if ((k - 2) % 3 == 1) {
                    jTable1.getColumnModel().getColumn(k).setMaxWidth(60);
                    jTable1.getColumnModel().getColumn(k).setPreferredWidth(60);
                //jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_ALL_COLUMNS);
                } else if ((k - 2) % 3 == 2) {
                    jTable1.getColumnModel().getColumn(k).setMaxWidth(60);
                    jTable1.getColumnModel().getColumn(k).setPreferredWidth(60);
                // jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
                }
            }
        }
        setVisible(true);
    }

    private void manageButtons(boolean value) {
        jButton1.setEnabled(value);
        jButton2.setEnabled(value);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField7 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel19 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jComboBox6 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jTextField10 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();

        jLayeredPane1.setName("jLayeredPane1"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel2.setName("jPanel2"); // NOI18N

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.setName("jComboBox3"); // NOI18N
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel16.setText("Minimum Usage Type");
        jLabel16.setName("jLabel16"); // NOI18N

        jTextField4.setName("jTextField4"); // NOI18N

        jLabel17.setText("UpTo Date");
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel18.setText("Date");
        jLabel18.setName("jLabel18"); // NOI18N

        jButton2.setText("Bill");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField5.setName("jTextField5"); // NOI18N

        jButton1.setText("Generate");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane4.setViewportView(jTable1);

        jTextField7.setName("jTextField7"); // NOI18N

        jLabel23.setText("Bill No");
        jLabel23.setName("jLabel23"); // NOI18N

        jButton13.setText("Test Print");
        jButton13.setName("jButton13"); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel30.setText("Period");
        jLabel30.setName("jLabel30"); // NOI18N

        jTextField12.setName("jTextField12"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(341, 341, 341)
                        .addComponent(jButton13)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(29, 29, 29)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel30)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel23)
                                .addComponent(jLabel18)
                                .addComponent(jLabel16)
                                .addComponent(jLabel17))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(269, 269, 269))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(1069, 1069, 1069))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton13))
                .addContainerGap(170, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Billing", jPanel2);

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText("Name");
        jLabel1.setName("jLabel1"); // NOI18N

        jTextField1.setName("jTextField1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setName("jList1"); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jLabel2.setText("Facilities Included");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText("List Of Facilities Included ");
        jLabel3.setName("jLabel3"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("jComboBox1"); // NOI18N
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel4.setText("Period");
        jLabel4.setName("jLabel4"); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setName("jComboBox2"); // NOI18N

        jLabel5.setText("Overall Minimum Usage amount");
        jLabel5.setName("jLabel5"); // NOI18N

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("jRadioButton1");
        jRadioButton1.setName("jRadioButton1"); // NOI18N
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("jRadioButton2");
        jRadioButton2.setName("jRadioButton2"); // NOI18N
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });

        jLabel6.setText("Individual Minimum Usage Amt");
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText("Yes");
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText("No");
        jLabel8.setName("jLabel8"); // NOI18N

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setText("jRadioButton3");
        jRadioButton3.setName("jRadioButton3"); // NOI18N
        jRadioButton3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton3ItemStateChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("jRadioButton4");
        jRadioButton4.setName("jRadioButton4"); // NOI18N
        jRadioButton4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton4ItemStateChanged(evt);
            }
        });

        jLabel9.setText("Yes");
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText("No");
        jLabel10.setName("jLabel10"); // NOI18N

        jTextField2.setName("jTextField2"); // NOI18N

        jTextField3.setName("jTextField3"); // NOI18N

        jLabel13.setText("Charges full if minimum usage not achieved");
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel15.setText("Proportionate Charges to deficit ");
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel14.setText("(Stipulated Minimum Usage) -(Usage) OR Minimum Usage whichever is greater");
        jLabel14.setName("jLabel14"); // NOI18N

        jButton3.setText("Save");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("Add");
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("Remove");
        jButton7.setName("jButton7"); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton6.setText("Remove");
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton5);
        jRadioButton5.setText("jRadioButton5");
        jRadioButton5.setName("jRadioButton5"); // NOI18N

        buttonGroup3.add(jRadioButton6);
        jRadioButton6.setText("jRadioButton6");
        jRadioButton6.setName("jRadioButton6"); // NOI18N

        buttonGroup3.add(jRadioButton7);
        jRadioButton7.setText("jRadioButton7");
        jRadioButton7.setName("jRadioButton7"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.setName("jList2"); // NOI18N
        jScrollPane3.setViewportView(jList2);

        jLabel19.setText("Minimum Usage Charge");
        jLabel19.setName("jLabel19"); // NOI18N

        jTextField6.setName("jTextField6"); // NOI18N

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.setName("jComboBox4"); // NOI18N

        jLabel20.setName("jLabel20"); // NOI18N

        jLabel21.setText("Applicable to Type of Member");
        jLabel21.setName("jLabel21"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jList3.setName("jList3"); // NOI18N
        jScrollPane5.setViewportView(jList3);

        jButton9.setText("Add");
        jButton9.setName("jButton9"); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Remove");
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel22.setText("A/C Head");
        jLabel22.setName("jLabel22"); // NOI18N

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox5.setName("jComboBox5"); // NOI18N

        jLabel11.setText("Member Type List");
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel24.setText("Start Date");
        jLabel24.setName("jLabel24"); // NOI18N

        jTextField8.setName("jTextField8"); // NOI18N

        jLabel25.setText("Credit Period");
        jLabel25.setName("jLabel25"); // NOI18N

        jLabel26.setText("Bill Sequence");
        jLabel26.setName("jLabel26"); // NOI18N

        jLabel27.setText("Service Tax");
        jLabel27.setName("jLabel27"); // NOI18N

        jLabel28.setText("Max Debt Allowed");
        jLabel28.setName("jLabel28"); // NOI18N

        jTextField9.setName("jTextField9"); // NOI18N

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox6.setName("jComboBox6"); // NOI18N

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox7.setName("jComboBox7"); // NOI18N

        jTextField10.setName("jTextField10"); // NOI18N

        jLabel29.setText("SMS Short Form");
        jLabel29.setName("jLabel29"); // NOI18N

        jTextField11.setName("jTextField11"); // NOI18N

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        jButton8.setName("jButton8"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(jButton3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel3)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jButton6)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel29))
                                            .addGap(21, 21, 21)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jComboBox1, 0, 132, Short.MAX_VALUE)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jButton9)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jRadioButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel9)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel10)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jButton5))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel24)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jButton8))))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(33, 33, 33)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane3, 0, 0, Short.MAX_VALUE)
                                                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addGap(28, 28, 28)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                            .addComponent(jLabel11)
                                                            .addGap(18, 18, 18)
                                                            .addComponent(jButton10))
                                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addComponent(jLabel25)
                                                .addComponent(jLabel27)
                                                .addComponent(jLabel28)
                                                .addComponent(jLabel26))
                                            .addGap(26, 26, 26)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jTextField10)
                                                .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jTextField9)
                                                .addComponent(jComboBox6, 0, 116, Short.MAX_VALUE))))
                                    .addGap(67, 67, 67))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel21)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(123, 123, 123)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jRadioButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jRadioButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel15))))
                        .addGap(779, 779, 779)
                        .addComponent(jLabel20)
                        .addGap(280, 280, 280))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(94, 94, 94)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(48, 48, 48))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel22)
                                    .addGap(56, 56, 56))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8))))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(jRadioButton3)
                        .addComponent(jLabel9)
                        .addComponent(jRadioButton4)
                        .addComponent(jLabel10)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jButton7)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton10)
                        .addComponent(jLabel11))
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(jLabel20))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jRadioButton1)
                        .addComponent(jLabel7)
                        .addComponent(jRadioButton2)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel27)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jRadioButton5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jRadioButton6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3))
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(164, 164, 164))
        );

        jTabbedPane1.addTab("Define Minimum Usage", jPanel1);

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "name", "period", "usage amt", "charge", "created date", "acc head", "applicable to", "deativated by", "deactivated date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.setName("jTable2"); // NOI18N
        jScrollPane2.setViewportView(jTable2);

        jButton11.setText("Edit");
        jButton11.setName("jButton11"); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Deactivate");
        jButton12.setName("jButton12"); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.setName("jCheckBox1"); // NOI18N
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        jLabel12.setText("Show All");
        jLabel12.setName("jLabel12"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(428, 428, 428)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jButton12))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(487, 487, 487)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)))
                .addContainerGap(1216, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel12))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton12))
                .addContainerGap(234, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("List", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1869, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (jComboBox1.getSelectedItem() != null) {
            if (jRadioButton3.isSelected()) {
                if (jTextField2 != null && jTextField1 != null) {
                    try {
                        String fid1 = null;
                        /*Object[] value=new Object[]{UUID.randomUUID().toString(),jTextField1.getText(),Double.valueOf(jTextField2.getText()),fid1};
                        new PreparedSentence(m_App.getSession()
                        , "INSERT INTO MINUSAGETYPEIN(ID,NAME,AMOUNT,MINUSAGETYPE) VALUES (?,?,?,?)"
                        , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.STRING})
                        ).exec(value);*/
                        memtypelist1.add(jComboBox1.getSelectedItem());
                        mltmodel1 = new FacilityTypeListModel(memtypelist1);
                        jList2.setModel(mltmodel1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            memtypelist.add(jComboBox1.getSelectedItem());
            mltmodel = new FacilityTypeListModel(memtypelist);
            jList1.setModel(mltmodel);
            //jRadioButton3.isFocused(false);
            jTextField2.setEnabled(false);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        int row = jList2.getSelectedIndex();
        String name1 = jTextField1.getText();
        if (row >= 0) {
            try {
                /*new PreparedSentence(m_App.getSession()
                , "DELETE FROM MINUSAGETYPEIN WHERE NAME=?"
                , SerializerWriteString.INSTANCE).exec(name1);*/
                memtypelist1.remove(row);
                mltmodel1 = new FacilityTypeListModel(memtypelist1);
                jList2.setModel(mltmodel1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        int row = jList1.getSelectedIndex();
        if (row >= 0) {
            memtypelist.remove(row);
            mltmodel = new FacilityTypeListModel(memtypelist);
            jList1.setModel(mltmodel);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jRadioButton3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton3ItemStateChanged
        // TODO add your handling code here:
        if (jRadioButton3.isSelected()) {
            jTextField2.setEnabled(true);
        }
    }//GEN-LAST:event_jRadioButton3ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        jRadioButton4.setEnabled(true);
        jTextField2.setEnabled(false);
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged
        // TODO add your handling code here:
        if (jRadioButton1.isSelected()) {
            jTextField3.setEnabled(true);

        }
    }//GEN-LAST:event_jRadioButton1ItemStateChanged

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged
        // TODO add your handling code here:
        if (jRadioButton2.isSelected()) {
            jTextField3.setEnabled(false);

        }
    }//GEN-LAST:event_jRadioButton2ItemStateChanged

    private void jRadioButton4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton4ItemStateChanged
        // TODO add your handling code here:
        if (jRadioButton4.isSelected()) {
            jTextField2.setEnabled(false);
        }
    }//GEN-LAST:event_jRadioButton4ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try {
            Transaction t = new Transaction(m_App.getSession()) {

                public Object transact() throws BasicException {
                    Date d = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(d.getTime());
                    cal.set(Calendar.HOUR_OF_DAY, 00);
                    cal.set(Calendar.MINUTE, 00);
                    cal.set(Calendar.SECOND, 00);
                    cal.set(Calendar.MILLISECOND, 00);
                    d.setTime(cal.getTimeInMillis());

                    String fid = UUID.randomUUID().toString();
                    if (jTextField1.getText().length() > 0 && jComboBox2.getSelectedItem() != null && jComboBox5.getSelectedItem() != null && memtypelist1 != null && jTextField3.getText().length() > 0 && jTextField8.getText().length() > 0) {
                        int count = Integer.valueOf(new StaticSentence(m_App.getSession(), "SELECT COUNT(*) FROM MINUSAGE WHERE NAME=?", SerializerWriteString.INSTANCE, SerializerReadInteger.INSTANCE).find(jTextField1.getText()).toString());
                        Date effectivedate = (Date) Formats.TIMESTAMP.parseValue(jTextField8.getText());
                        if (count == 0) {
                            Periodicity p = (Periodicity) jComboBox2.getSelectedItem();
                            String rtemp = null;
                            if (p != null) {
                                rtemp = p.getid();
                            }
                            DebtTypeTableModel.DebtTypeline q = (DebtTypeTableModel.DebtTypeline) jComboBox6.getSelectedItem();
                            String rtemp1 = null;
                            if (q != null) {
                                rtemp1 = q.getid();
                            }
                            AccountMasterExt ac = (AccountMasterExt) jComboBox5.getSelectedItem();
                            String atemp = null;
                            if (ac != null) {
                                atemp = ac.getid();
                            }
                            FacilityTypeListModel fmodel = (FacilityTypeListModel) jList1.getModel();
                            if (fmodel.getSize() > 0) {
                                for (int j = 0; j < fmodel.getSize(); j++) {
                                    // FacilitytableModel.Facilityline fac =(FacilitytableModel.Facilityline)fmodel.getElementAt(j);
                                    Facility fac = (Facility) fmodel.getElementAt(j);
                                    if (ftype == null) {
                                        ftype = fac.getid();
                                    } else {
                                        ftype += "#" + fac.getid();
                                    }
                                }
                            }

                            TaxCategoryInfo staxcat = (TaxCategoryInfo) jComboBox7.getSelectedItem();
                            String staxid = staxcat.getID();
                            String sms = jTextField11.getText();
                            String mtype = null;
                            String bill = jTextField9.getText();
                            String val = jTextField10.getText();
                            MemTypeListModel mtmodel = (MemTypeListModel) jList3.getModel();
                            if (mtmodel.getSize() > 0) {
                                for (int j = 0; j < mtmodel.getSize(); j++) {
                                    if (mtmodel.getElementAt(j).toString().equals("ALL")) {
                                        mtype = "ALL";
                                        break;
                                    } else {
                                        MemCat mem = (MemCat) mtmodel.getElementAt(j);
                                        if (mtype == null) {
                                            mtype = mem.getID();
                                        } else {
                                            mtype += "#" + mem.getID();
                                        }
                                    }
                                }
                            }
                            boolean flag = true;
                            Integer i = 0;
                            if (jRadioButton5.isSelected()) {
                                utype = "1";
                                Object[] value = new Object[]{fid, jTextField1.getText(), rtemp, ftype, Double.valueOf(jTextField3.getText()), utype, new Date(), Double.valueOf(jTextField6.getText()), atemp, mtype, flag, rtemp1, m_App.getAppUserView().getUser().getName(), bill, staxid, val, sms, i, effectivedate};
                                new PreparedSentence(m_App.getSession(), "INSERT INTO MINUSAGE(ID, NAME,PERIOD,FACILITIES,AMOUNT,MINUSAGETYPE,CREATEDDATE,CHARGE,ACHEAD,MEMBERS,ACTIVE,CREDITPERIOD,CREATEDBY,BILLSEQUENCE,SERVICETAX,MAXDEBT,SMS,MAXNO,LASTBILLEDDATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING, Datas.TIMESTAMP, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.TIMESTAMP})).exec(value);
                            } else if (jRadioButton6.isSelected()) {
                                utype = "2";
                                Object[] value = new Object[]{fid, jTextField1.getText(), rtemp, ftype, Double.valueOf(jTextField3.getText()), utype, new Date(), Double.valueOf(jTextField6.getText()), atemp, mtype, flag, rtemp1, m_App.getAppUserView().getUser().getName(), bill, staxid, val, sms, i, effectivedate};
                                new PreparedSentence(m_App.getSession(), "INSERT INTO MINUSAGE(ID, NAME,PERIOD,FACILITIES,AMOUNT,MINUSAGETYPE,CREATEDDATE,CHARGE,ACHEAD,MEMBERS,ACTIVE,CREDITPERIOD,CREATEDBY,BILLSEQUENCE,SERVICETAX,MAXDEBT,SMS,MAXNO,LASTBILLEDDATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING, Datas.TIMESTAMP, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.TIMESTAMP})).exec(value);

                            } else if (jRadioButton7.isSelected()) {
                                utype = "3";
                                Object[] value = new Object[]{fid, jTextField1.getText(), rtemp, ftype, Double.valueOf(jTextField3.getText()), utype, new Date(), Double.valueOf(jTextField6.getText()), atemp, mtype, flag, rtemp1, m_App.getAppUserView().getUser().getName(), bill, staxid, val, sms, i, effectivedate};
                                new PreparedSentence(m_App.getSession(), "INSERT INTO MINUSAGE(ID, NAME,PERIOD,FACILITIES,AMOUNT,MINUSAGETYPE,CREATEDDATE,CHARGE,,ACHEAD,MEMBERS,ACTIVE,CREDITPERIOD,CREATEDBY,BILLSEQUENCE,SERVICETAX,MAXDEBT,SMS,MAXNO,LASTBILLEDDATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING, Datas.TIMESTAMP, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.TIMESTAMP})).exec(value);
                            }
                            List<CustomerInfo1> memlist = new ArrayList<CustomerInfo1>();
                            if (mtype.equals("ALL")) {
                                memlist = new StaticSentence(m_App.getSession(), "SELECT ID,SEARCHKEY,TAXID,NAME,MEMTYPE FROM CUSTOMERS WHERE VISIBLE=TRUE", null, new SerializerReadClass(CustomerInfo1.class)).list();
                            } else {
                                String[] mtypearr = mtype.split("#");
                                if (mtypearr.length > 0) {
                                    for (int j = 0; j < mtypearr.length; j++) {
                                        List<CustomerInfo1> list = new StaticSentence(m_App.getSession(), "SELECT ID,SEARCHKEY,TAXID,NAME,MEMTYPE FROM CUSTOMERS WHERE MEMTYPE= ? AND VISIBLE=TRUE", SerializerWriteString.INSTANCE, new SerializerReadClass(CustomerInfo1.class)).list(mtypearr[j]);
                                        memlist.addAll(list);
                                    }
                                }
                            }
                            for (int j = 0; j < memlist.size(); j++) {
                                String cid = memlist.get(j).getId();
                                int count1 = Integer.valueOf(new StaticSentence(m_App.getSession(), "SELECT COUNT(*) FROM MEMFACILITYUSAGE WHERE MEMNO=? AND FACILITYTYPE=? AND ACTIVE=TRUE AND USERID IS NULL", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING}), SerializerReadInteger.INSTANCE).find(new Object[]{cid, fid}).toString());
                                if (count1 == 0) {
                                    int flag1 = 0;
                                    String caccount = null;
                                    try {
                                        caccount = dfac.getCustomerAccountByID(cid);
                                    } catch (Exception e1) {
                                        flag1 = 1;
                                        JOptionPane.showMessageDialog(null, "Member Account not present " + memlist.get(j).getName() + " .Please create one", null, JOptionPane.OK_OPTION);
                                    }
                                }

                                Object[] value1 = new Object[]{UUID.randomUUID().toString(), true, cid, fid, d, m_App.getAppUserView().getUser().getName(), d, 0, effectivedate};
                                new PreparedSentence(m_App.getSession(), "INSERT INTO MEMMINUSAGE(ID, ACTIVE, MEMNO, USAGETYPE, SDATE, CREATEDBY, CRDATE,STATUS,LBILLDATE) VALUES (?,?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.INT, Datas.TIMESTAMP})).exec(value1);
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "Following usage type already exists", null, JOptionPane.OK_OPTION);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Fill the form completely", null, JOptionPane.OK_OPTION);
                    }
                    /*Object[] value=new Object[]{fid,jTextField1.getText()};
                    new PreparedSentence(m_App.getSession()
                    , "UPDATE MINUSAGETYPEIN SET MINUSAGETYPE=? WHERE NAME=?"
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
                    ).exec(value);*/

                    return null;
                }
            };
            t.execute();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting values.Ensure Correct values are inserted", "Error", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        newBillLogic();
    // TODO add your handling code here:
/*int noofrows = mmodel1.getUsageModel().getRowCount();
    if (billed == false) {
    if (noofrows > 0 && JOptionPane.showConfirmDialog(this, "Do u want to bill ", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
    manageButtons(false);
    billed = true;
    totalsum = 0.0;
    try {

    String tid = UUID.randomUUID().toString();
    Minusage mintemp = new Minusage();
    mintemp = (Minusage) jComboBox3.getSelectedItem();
    String billno = dfac.getnewbillno1(mintemp.getid());
    jTextField7.setText(billno);
    billnum = billno;

    int j = 0;

    for (int i = 0; i < noofrows; i++) {
    // FacilityBillingTableModel.Facilityline fline=fmodel.getfacilityline().get(i);
    boolean selected = (Boolean) mmodel1.getUsageModel().getValueAt(i, 12);
    String amt = mmodel1.getUsageModel().getValueAt(i, 3).toString();
    if (selected == true && !amt.equals("0.0")) {

    //updatememusage(i, billno, mintemp, tid);
    j++;
    }
    }


    dfac.updatebillno1(mintemp.getid());
    String servicetaxacc = null;
    Object stacc = new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE WHERE NAME = ? ", SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE).find("Service Tax Account");
    if (stacc != null) {
    servicetaxacc = stacc.toString();
    }
    //ID,TID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE
    Object[] value1 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "C", mintemp.getName(), billno, getTotal(), dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), mintemp.getName() + " on " + Formats.DATE.formatValue(dnow), mintemp.getAcchead(), 0.0, dnow, true};
    dfac.insertintoaccjoutnal1(value1);
    if (getTaxtotal() > 0 && servicetaxacc != null) {
    Object[] value2 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "C", mintemp.getName(), billno, getTaxtotal(), dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Service tax for bill number " + billno, servicetaxacc, 0.0, dnow, true};
    dfac.insertintoaccjoutnal1(value2);
    } else {
    if (servicetaxacc == null) {
    JOptionPane.showMessageDialog(this, "Please Create a service tax account", null, JOptionPane.OK_OPTION);
    }
    }
    //  mmodel1 = MinUsageDataModel.loadEmptyInstance();
    // jTable1.setModel(mmodel1.getUsageModel());
    jButton13.setEnabled(true);
    }
    catch




    (


    BasicExceptione) {
    e.printStackTrace();
    }
    manageButtons(


    true);

    }
    }

    else {
    JOptionPane.showMessageDialog(this, "Already billed. Please generate if u want to bill again");
    }

     */
    }//GEN-LAST:event_jButton2ActionPerformed

    public void newMinUsageLogic() {
        try {

            Minusage usage = (Minusage) jComboBox3.getSelectedItem();
            FacilityLogic flogic = new FacilityLogic();
            Date edate = usage.getLastbilleddate();

            String id = usage.getPeriod();
            Periodicity p1;
            p1 = dfac.getPerioicitybyid(id);
            Date sdate = flogic.calculateStartDate1(p1, edate);
            edate = flogic.calculateEndDate2(sdate, p1);
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String dateOnly = "";
            String enddate = df.format(usage.getLastbilleddate());
            Map lastBillPeriodsMap = new HashMap();


            if (edate.compareTo(dnow) < 0) {
                MinUsageLogic mulogic = new MinUsageLogic();
                String uid = usage.getid();
                double limit = usage.getAmount();
                double charge = usage.getCharge();
                String mutype = usage.getMinusagetype();
                double camt, camtTotal, limitTotal, usageTotal;
                String usageDisplay;
                String limitDisplay;
                String chargeDisplay;
                List<BillPeriods> bperiods;

                List<LastBillDate> lbillDates = dfac.getLastBillDates(usage.getid());
                for (LastBillDate lbillDate : lbillDates) {
                    bperiods = mulogic.findBillPeriods(p1, lbillDate.getDate());
                    dateOnly = df.format(lbillDate.getDate());
                    lastBillPeriodsMap.put(dateOnly, bperiods);
                    chargableCustomers.put(dateOnly, new ArrayList<MinUsageCustomer>());
                }
                List<MinUsageCustomer> muCustomers = dfac.getMinusageCustomers(usage.getid());
                List<MinUsageCustomer> temp = new ArrayList<MinUsageCustomer>();


                for (MinUsageCustomer cust : muCustomers) {

                    String cid = cust.getCid();
                    String custLBillDate = df.format(cust.getLastBillDate());

                    bperiods = (ArrayList<BillPeriods>) lastBillPeriodsMap.get(custLBillDate);
                    List<BillPeriods> tempBp = new ArrayList<BillPeriods>();
                    Date ed = cust.getEnddate();
                    String newMinUsageRef = cust.getNewMinUsageRef();

                    if (ed != null) {
                        if (newMinUsageRef != null) {
                            for (BillPeriods bp2 : bperiods) {
                                if (ed.after(bp2.getEndDate())) {
                                    tempBp.add(bp2);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            for (BillPeriods bp2 : bperiods) {
                                if (bp2.getStartDate().before(ed)) {
                                    tempBp.add(bp2);
                                } else {
                                    break;
                                }
                            }
                        }
                        bperiods = tempBp;
                    }
                    camtTotal = 0.0;
                    limitTotal = 0.0;
                    usageTotal = 0.0;
                    usageDisplay = "";
                    limitDisplay = "";
                    chargeDisplay = "";

                    for (BillPeriods bp : bperiods) {

                        camt = 0.0;

                        double custUsage = dfac.getCustomerUsage(cid, bp.getStartDate(), bp.getEndDate());

                        if (mutype.equals("1")) {
                            if (custUsage >= limit) {
                                camt = 0.0;
                            } else if (custUsage < limit) {
                                camt = charge;
                            }
                        } else if (mutype.equals("2")) {
                            if (custUsage >= limit) {
                                camt = 0.0;
                            } else if (custUsage < limit) {
                                double a2 = limit - custUsage;
                                if (a2 > charge) {
                                    camt = charge;
                                } else if (a2 < charge) {
                                    camt = a2;
                                }
                            }

                        } else if (mutype.equals("3")) {
                            if (custUsage >= limit) {
                                camt = 0.0;
                            } else if (custUsage < limit) {
                                double a13 = limit / 3;
                                double a23 = a13 * 2;
                                if (custUsage < a13) {
                                    camt = charge;
                                } else if (custUsage >= a13 && custUsage < a23) {
                                    camt = (charge / 3) * 2;
                                } else if (custUsage >= a23 && custUsage < limit) {
                                    camt = charge / 3;
                                }
                            }

                        }
                        camtTotal += camt;
                        limitTotal += limit;
                        usageTotal += custUsage;

                        if (!"".equals(usageDisplay)) {
                            usageDisplay += " / ";
                        }
                        usageDisplay += custUsage;

                        if (!"".equals(limitDisplay)) {
                            limitDisplay += " / ";
                        }
                        limitDisplay += limit;

                        if (!"".equals(chargeDisplay)) {
                            chargeDisplay += " / ";
                        }
                        chargeDisplay += camt;

                    }

                    cust.setCamtTotal(camtTotal);
                    cust.setLimitTotal(limitTotal);
                    cust.setUsageDisplay(usageDisplay);
                    cust.setLimitDisplay(limitDisplay);
                    cust.setChargeDisplay(chargeDisplay);
                    cust.setUsageTotal(usageTotal);


                    cust.setBillDate(bperiods.get(bperiods.size() - 1).getEndDate());


                    if (camtTotal > 0) {
                        cust.setNarration("Minimum usage charge for period : " + bperiods.get(0).getStartDate().toString() + "To" + bperiods.get(bperiods.size() - 1).getEndDate().toString() + ":" + cust.getSearchkey());

                        //System.out.println(c);
                        // c++;
                        temp = (List<MinUsageCustomer>) chargableCustomers.get(custLBillDate);
                        temp.add(cust);
                        chargableCustomers.put(custLBillDate, temp);

                    }
                    custAll.add(cust);

                }
                bperiods = (ArrayList<BillPeriods>) lastBillPeriodsMap.get(enddate);
                billingDate = bperiods.get(bperiods.size() - 1).getEndDate();
                jTextField4.setText(Formats.TIMESTAMP.formatValue(billingDate));
                mmodel1 = MinUsageDataModel.loadData(m_App, custAll);
                showDialog(mmodel1, m_App);
            } else {
                JOptionPane.showMessageDialog(this, "Already billed", null, JOptionPane.OK_OPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        manageButtons(true);

    }

    public void newBillLogic() {
        if (billed == false) {
            if (chargableCustomers.size() > 0 && JOptionPane.showConfirmDialog(this, "Do u want to bill ", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                manageButtons(false);
                try {
                    Transaction t = new Transaction(m_App.getSession()) {

                        @Override
                        protected Object transact() throws BasicException {

                            String servicetaxacc = null;
                            Object stacc = new StaticSentence(m_App.getSession(), "SELECT VALUE FROM GENERALTABLE WHERE NAME = ? ", SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE).find("Service Tax Account");
                            if (stacc != null) {
                                servicetaxacc = stacc.toString();
                            }

                            if (servicetaxacc == null) {
                                JOptionPane.showMessageDialog(null, "Please Create a service tax account", null, JOptionPane.OK_OPTION);
                            } else {


                                Minusage mintemp = new Minusage();

                                mintemp = (Minusage) jComboBox3.getSelectedItem();
                                name = mintemp.getName();
                                DebtTypeTableModel.DebtTypeline dueperiod = dfac.getDebtTypebyid(mintemp.getCreditperiod());
                                TaxCategoryInfo tinfo = (TaxCategoryInfo) m_dlSales.getTaxCategoryByid(mintemp.getServiceTax());
                                TaxInfo taxinfo = taxeslogic.getTaxInfo(tinfo);
                                totalsum = 0.0;
                                for (Object key : chargableCustomers.keySet()) {

                                    List<MinUsageCustomer> mucustomers = (ArrayList<MinUsageCustomer>) chargableCustomers.get(key);
                                    if (mucustomers.size() > 0) {


                                        String tid = UUID.randomUUID().toString();

                                        String billno = dfac.getnewbillno1(mintemp.getid());
                                        if (!billno.equals("")) {
                                            if (!"".equals(showBillNos)) {
                                                showBillNos += " / ";
                                            }
                                            showBillNos += billno;
                                            jTextField7.setText(showBillNos);
                                            billnum = billno;

                                            for (MinUsageCustomer cust : mucustomers) {
                                                boolean selected = (Boolean) cust.getBillit();
                                                String amt = cust.getCamtTotal().toString();
                                                if (selected == true && !amt.equals("0.0")) {


                                                    createbill(cust, billno, mintemp, tid, dueperiod, taxinfo);
                                                //System.out.println(count++);
                                                }
                                            }

                                            dfac.updatebillno1(mintemp.getid());


                                            //ID,TID,DATE,TRANSTYPE,TRANSREF,TRANSNO,AMOUNT,DUEDATE,ADJUSTED,CREATEDBY,COUNTER,NARRATION,ACCOUNTID,BALANCEAMOUNT,CLEARDATE
                                            Object[] value1 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "C", mintemp.getName(), billno, getTotal(), dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), mintemp.getName() + " on " + Formats.DATE.formatValue(dnow), mintemp.getAcchead(), 0.0, dnow, true};
                                            dfac.insertintoaccjoutnal1(value1);

                                            if (getTaxtotal() > 0) {
                                                Object[] value2 = new Object[]{UUID.randomUUID().toString(), tid, dnow, "C", mintemp.getName(), billno, getTaxtotal(), dnow, true, m_App.getAppUserView().getUser().getName(), m_App.getProperties().getHost(), "Service tax for bill number " + billno, servicetaxacc, 0.0, dnow, true};
                                                dfac.insertintoaccjoutnal1(value2);
                                            }

                                        } else {
                                            JOptionPane.showMessageDialog(null, "Define a bill sequence", "Create bill", JOptionPane.OK_OPTION);
                                        }
                                    }
                                }
                                for (MinUsageCustomer mucust : custAll) {
                                    dfac.updateLastBilledDateOfMember(mucust.getBillDate(), mucust.getCid(), mintemp.getid());
                                }
                                dfac.updateLastBilledDate(billingDate, mintemp.getid());

                                jButton13.setEnabled(true);
                                manageButtons(true);
                            // int count = 0;

                            }

                            return null;
                        }
                    };
                    t.execute();

                } catch (BasicException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Already billed. Please generate if u want to bill again");
        }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         /*try{
        jTable1.setVisible(true);
        Minusage fac=(Minusage)jComboBox3.getSelectedItem();

        if(fac.getCharge()>0){
        Periodicity p=dfac.getPerioicitybyid(fac.getPeriod());
        if(p.getqbtype()==false){
        try{
        mmodel = MinUsageTableModel.loadInstance(m_App,fac,dfac);
        jTable1.setModel(mmodel.getTableModel());
        // jTable1.getModel().addTableModelListener(this);
        }
        catch(Exception e){
        e.printStackTrace();
        }
        jButton2.setEnabled(true);
        jButton2.setVisible(true);
        }else{
        mmodel = MinUsageTableModel.loadInstance1(m_App,fac,dfac,m_dlSales,taxrate);
        jTable1.setModel(mmodel.getTableModel());
        jButton2.setEnabled(true);
        jButton2.setVisible(true);
        }
        }else{
        mmodel = MinUsageTableModel.emptyinstance();
        jTable1.setModel(mmodel.getTableModel());
        }
        }catch(Exception e){
        e.printStackTrace();
        }*/
        newMinUsageLogic();
    /*try {
    newMinUsageLogic();
    jButton13.setEnabled(false);
    manageButtons(false);
    billed = false;
    totalsum = 0.0;
    taxtotal = 0.0;
    Minusage usage = (Minusage) jComboBox3.getSelectedItem();
    if (usage.getMinusagetype().equals("1")) {
    type = "1";
    } else if (usage.getMinusagetype().equals("2")) {
    type = "2";
    } else if (usage.getMinusagetype().equals("3")) {
    type = "3";
    }
    String faci = usage.getFacilities();
    String u = usage.getid();
    double a = usage.getAmount();
    double a1 = usage.getCharge();
    FacilityLogic flogic = new FacilityLogic();
    Date dnow = new Date();
    Date edate = usage.getLastbilleddate();
    String id = usage.getPeriod();
    Periodicity p1;
    p1 = dfac.getPerioicitybyid(id);
    Date sdate = flogic.calculateStartDate1(p1, edate);
    edate = flogic.calculateEndDate2(sdate, p1);
    /*if(edate.compareTo(dnow)<0)
    {
    edate=flogic.calculateEndDate2(edate, p1);
    if(edate.compareTo(dnow)<0){
    edate=flogic.calculateEndDate2(edate, p1);
    }
    }
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(sdate.getTime());
    cal.set(Calendar.HOUR_OF_DAY, 00);
    cal.set(Calendar.MINUTE, 00);
    cal.set(Calendar.SECOND, 00);
    cal.set(Calendar.MILLISECOND, 01);
    cal.set(Calendar.AM_PM, Calendar.AM);
    sdate.setTime(cal.getTimeInMillis());
    Calendar cal1 = Calendar.getInstance();
    cal1.setTimeInMillis(edate.getTime());
    cal1.set(Calendar.HOUR_OF_DAY, 23);
    cal1.set(Calendar.MINUTE, 59);
    cal1.set(Calendar.SECOND, 59);
    cal1.set(Calendar.MILLISECOND, 59);
    cal1.set(Calendar.AM_PM, Calendar.PM);
    edate.setTime(cal1.getTimeInMillis());
    if (edate.compareTo(dnow) < 0) {
    jTextField4.setText(Formats.TIMESTAMP.formatValue(edate));
    mmodel1 = MinUsageDataModel.loadData(m_App, type, a, sdate, edate, a1, u);
    showDialog(mmodel1, m_App);
    } else {
    JOptionPane.showMessageDialog(this, "Already billed", null, JOptionPane.OK_OPTION);
    }
    } catch (Exception e) {
    e.printStackTrace();

    }
    manageButtons(true);*/

}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        if (jComboBox4.getSelectedItem() != null) {

            memtype.add(jComboBox4.getSelectedItem());
            mltmodel2 = new MemTypeListModel(memtype);
            jList3.setModel(mltmodel2);

        }

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        javax.swing.JTabbedPane tabpane = (javax.swing.JTabbedPane) evt.getSource();
        int tabno = tabpane.getSelectedIndex();
        if (tabno == 2) {
            try {
                if (jCheckBox1.isSelected() == true) {
                    minmodel = MinUsageModel.loadInstance(m_App, 2);
                    jTable2.setModel(minmodel.getMinUsageModel());
                } else if (jCheckBox1.isSelected() == false) {
                    minmodel = MinUsageModel.loadInstance(m_App, 1);
                    jTable2.setModel(minmodel.getMinUsageModel());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        try {
            if (jCheckBox1.isSelected() == true) {
                minmodel = MinUsageModel.loadInstance(m_App, 2);
                jTable2.setModel(minmodel.getMinUsageModel());
            } else if (jCheckBox1.isSelected() == false) {
                minmodel = MinUsageModel.loadInstance(m_App, 1);
                jTable2.setModel(minmodel.getMinUsageModel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        try {
            int row = jTable2.getSelectedRow();
            if (row < jTable2.getRowCount() && row >= 0) {
                String id = jTable2.getModel().getValueAt(row, 16).toString();
                String fname1 = jTable2.getModel().getValueAt(row, 0).toString();
                if (JOptionPane.showConfirmDialog(this, "Do you want to deactivate " + fname1 + " UsageType ?", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    new PreparedSentence(m_App.getSession(), "UPDATE MINUSAGE SET ACTIVE=FALSE,DEACTIVATEDBY =?,DEACTIVATEDDATE=? WHERE ID=? AND DEACTIVATEDBY IS NULL AND DEACTIVATEDDATE IS NULL", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.STRING})).exec(new Object[]{m_App.getAppUserView().getUser().getName(), new Date(), id});
                    if (jCheckBox1.isSelected() == true) {
                        minmodel = MinUsageModel.loadInstance(m_App, 2);
                        jTable2.setModel(minmodel.getMinUsageModel());
                    } else if (jCheckBox1.isSelected() == false) {
                        minmodel = MinUsageModel.loadInstance(m_App, 1);
                        jTable2.setModel(minmodel.getMinUsageModel());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        int row = jList3.getSelectedIndex();
        if (row >= 0) {
            memtype.remove(row);
            mltmodel2 = new MemTypeListModel(memtype);
            jList3.setModel(mltmodel2);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
        if (jComboBox3.getSelectedItem() != null) {
            Minusage pid = (Minusage) jComboBox3.getSelectedItem();
            String id = pid.getPeriod();
            Periodicity p;
            try {
                p = dfac.getPerioicitybyid(id);
                jTextField12.setText(p.getName());
                period = p.getName();
            } catch (BasicException ex) {
                ex.printStackTrace();
            }


        }
    }//GEN-LAST:event_jComboBox3ActionPerformed
    private JRField[] getFields() throws JRException, UnsupportedOperationException {
        JRField[] fields = new JRField[4];
        fields[0] = (JRField) new JRBasicField("Mem No", "memno", java.lang.String.class, "java.lang.String");
        fields[1] = (JRField) new JRBasicField("Mem Name", "mname", java.lang.String.class, "java.lang.String");
        fields[2] = (JRField) new JRBasicField("Amount", "amount", java.lang.Double.class, "java.lang.Double");
        fields[3] = (JRField) new JRBasicField("Charge", "Charge", java.lang.Double.class, "java.lang.Double");
        fields[2] = (JRField) new JRBasicField("showUsage", "showUsage", java.lang.String.class, "java.lang.String");
        fields[2] = (JRField) new JRBasicField("showCharge", "showCharge", java.lang.String.class, "java.lang.String");

        return fields;
    }
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        try {
            Minusage usage = (Minusage) jComboBox3.getSelectedItem();
            Map reportparams = new HashMap();
            reportparams.put("UsageType", this.getName1());
            reportparams.put("Rate", this.rate);
            reportparams.put("Period", this.getPeriod());
            reportparams.put("billnum", this.getBillNo());
            reportparams.put("billedby", m_App.getAppUserView().getUser().getName());
            reportparams.put("tax", this.tax);
            reportparams.put("taxvalue", this.getTaxtotal());
            reportparams.put("totalrate", this.getTotal());
            DataSourceProvider data1 = new DataSourceProvider(mmodel1.getMinUsageCustomer());
            data1.setFields(getFields());
            DataSource4 ds = new DataSource4(mmodel1.getMinUsageCustomer());
            data1.setDataSource(ds);
            //JasperPrint jp= JasperReportNew.runReport(m_App, "./reports/com/openbravo/reports/MinUse.jrxml",reportparams,true,data1,false,null);
            JasperPrint jp = JasperReportNew.runReport(m_App, "./reports/com/openbravo/reports/MinUse.jrxml", reportparams, false, data1, true, null);
            jButton3.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(jTextField8.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTime(this, date);
        if (date != null) {
            jTextField8.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

    public String getTitle() {
        return "MinimumUsage";
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

    private class FacilityTypeListModel extends AbstractListModel {

        private java.util.List facilitiesmodel;

        public FacilityTypeListModel(java.util.List facilitiesmodel) {
            this.facilitiesmodel = facilitiesmodel;
        }

        public int getSize() {
            return facilitiesmodel.size();
        }

        public Object getElementAt(int i) {
            return facilitiesmodel.get(i);
        }

        public void remove(int i) {
            facilitiesmodel.remove(i);
        }
    }

    private class MemTypeListModel extends AbstractListModel {

        private java.util.List memtype;

        public MemTypeListModel(java.util.List memtype) {
            this.memtype = memtype;
        }

        public int getSize() {
            return memtype.size();
        }

        public Object getElementAt(int i) {
            return memtype.get(i);
        }

        public void remove(int i) {
            memtype.remove(i);
        }
    }
}
