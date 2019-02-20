/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FacilityDetail.java
 *
 * Created on Mar 14, 2009, 10:38:09 AM
 */

package com.openbravo.pos.clubmang;

import com.openbravo.pos.Accounts.AccountMaster;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerReadInteger;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.catalog.WideComboBox;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.CustomerInfo1;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.TaxInfo;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author swathi
 */
public class FacilityDetail extends javax.swing.JPanel implements JPanelView, BeanFactoryApp  {
    private AppView m_App;
    private DataLogicFacilities dmang;
   // private TableDefinition facilityTable;
   // private int[] idata=new int[15];
   // private int i=0;
    private FacilitytableModel fmodel;
    
    private ComboBoxValModel periodicitymodel;
    private ComboBoxValModel periodicitymodel1;
   //private ComboBoxValModel periodicitymodel2;
    private ComboBoxValModel debttypemodel;
    private ComboBoxValModel accounttype;
    private ComboBoxValModel renewalacctype;
    private ComboBoxValModel usageacctype;
    private ComboBoxValModel memtype;
    private ComboBoxValModel staxmodel;
    private DataLogicSales m_dlSales;
    private TaxesLogic taxeslogic;
    private List memtypelist;
    private MemTypeListModel mmodel;
   // private ComboBoxValModel periodicitymodel1;
   /**Creates new form FacilityDetail */
    public FacilityDetail() {
        initComponents();
    }

    public void init(AppView app) throws BeanFactoryException {
        m_App = app;
       dmang=(DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
       m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");

       jLabel3.setVisible(false);
    }
    public Object getBean() {
        return this;
    }

    public JComponent getComponent() {
        return this;
    }

   public String getTitle() {
        return "Facility";
    }

    public void activate() throws BasicException {
        jButton4.setVisible(false);//praveen_comment this line wn u implement "edit"
        loadData();
    }
    private List addNull(List list){
       list.add(0, null);
       return  list;
    }
    private void loadData() throws BasicException {
         debttypemodel=new ComboBoxValModel(dmang.getDebtType());
       //  acctype.setModel(accounttype);
         try{
        // List memtypelist=dmang.getMemType();
             taxeslogic = new TaxesLogic(m_dlSales.getTaxList().list());
             List tyofmem=dmang.getMemType();
             tyofmem.add(0,"ALL");
         memtype=new ComboBoxValModel(tyofmem);
         typeOfMem.setModel(memtype);
         jTabbedPane1.setSelectedIndex(0);
        // List acc=addNull(dmang.getSalesaccounts());
         List acc=addNull(dmang.getaccounts());
      //   acc.add(0,null);
         accounttype=new ComboBoxValModel(acc);
         jfeeaccount.setModel(accounttype);
         renewalacctype=new ComboBoxValModel(acc);
         renewalaccount.setModel(renewalacctype);
         usageacctype=new ComboBoxValModel(acc);
         usageaccount.setModel(usageacctype);
         List period=addNull(dmang.getPeriodicity().list());
        // period.add(0, null);
         periodicitymodel=new ComboBoxValModel(period);
         periodicitymodel1=new ComboBoxValModel(period);
         debttype.setModel(debttypemodel);
         List staxlist=m_dlSales.getTaxCategoriesList().list();
         staxlist.add(0,null);
         staxmodel=new ComboBoxValModel(staxlist);
        servicetax.setModel(staxmodel);
        // periodicitymodel2=new ComboBoxValModel(period);
        java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    fname.requestFocus();
                }
            });
       }
       catch(Exception e){
           e.printStackTrace();
       }
         reset();
         set();

    }
    private void reset(){
        fname.setText(null);
        billsequence.setText(null);
        smssform.setText(null);
      //  jCheckBox6.setSelected(false);
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        jCheckBox3.setSelected(false);
        jCheckBox4.setSelected(false);
        jImageEditor1.setImage(null);
        jamount.setText("0");
        uamount.setText("0");
        ramount.setText("0");
        typeOfMem.setSelectedIndex(0);
        maxdebt.setText(null);
        standard.setSelected(true);
        optional.setSelected(true);
        servicetax.setSelectedIndex(0);
        memtypelist=new ArrayList();
        mmodel=new MemTypeListModel(memtypelist);
        jList1.setModel(mmodel);
    }

    private void set(){
           rperiod.setModel(periodicitymodel);
           uperiod.setModel(periodicitymodel1);
         //  dueperiod.setModel(periodicitymodel2);
           rperiod.setSelectedIndex(0);
           uperiod.setSelectedIndex(0);
           debttype.setSelectedIndex(-1);
         //  dueperiod.setSelectedIndex(0);
        //   acctype.setSelectedIndex(-1);
           jLabel12.setVisible(false);
           jamount.setVisible(false);
           jfeeaccount.setSelectedIndex(0);
           renewalaccount.setVisible(false);
           renewalaccount.setSelectedIndex(0);
           rperiod.setSelectedItem(null);
           uperiod.setSelectedItem(null);
           jLabel17.setVisible(false);
           usageaccount.setVisible(false);
           usageaccount.setSelectedIndex(0);
           jLabel16.setVisible(false);
           jLabel14.setVisible(false);
           jfeeaccount.setVisible(false);
           jLabel5.setVisible(false);
           jLabel6.setVisible(false);
           ramount.setVisible(false);
           rperiod.setVisible(false);
           jLabel8.setVisible(false);
           jLabel9.setVisible(false);
           uamount.setVisible(false);
           uperiod.setVisible(false);
    }
     public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }
   /*   private Date getDueDate(DebtTypeTableModel.DebtTypeline dueperiod,Date d){
      String type=dueperiod.getperiod();
        int num=dueperiod.getNum();
        Date duedate=new Date();
        duedate.setTime(d.getTime());
        Calendar cal=GregorianCalendar.getInstance();
        cal.setTimeInMillis(duedate.getTime());
        if(type.equals("Days"))
        cal.add(Calendar.DATE, num);
        if(type.equals("Months"))
        cal.add(Calendar.MONTH, num);
        if(type.equals("Years"))
        cal.add(Calendar.YEAR, num);
        duedate.setTime(cal.getTimeInMillis());
       return duedate;
    }*/
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
        public void remove(int i){
             memtype.remove(i);
        }

    }
   /*     private int pnum=0;
      private Date calculateEndDate(Date d,Periodicity p,int billabledate){
       /*   Date edate=new Date();
          edate.setTime(d.getTime());
          Calendar cal=Calendar.getInstance();
          cal.setTimeInMillis(edate.getTime());
          if(p.gettype().equals("Days")){
              cal.add(Calendar.DATE, p.getno());
          }else if(p.gettype().equals("Months")){
              cal.add(Calendar.MONTH, p.getno());
          } if(p.gettype().equals("Quaterly")){
              cal.add(Calendar.MONTH, 3);
          } if(p.gettype().equals("Years")){
              cal.add(Calendar.YEAR, p.getno());
          }
          edate.setTime(cal.getTimeInMillis());
          return edate;   ////
          // temp=new Date();
          Date edate=new Date();
         // edate.setTime(d.getTime());
          Calendar cal1=Calendar.getInstance();
          cal1.setTimeInMillis(d.getTime());
          Calendar cal=Calendar.getInstance();
          cal.setTimeInMillis(edate.getTime());
         if(p.getdoj()==true){
           while(cal1.before(cal)){
              temp.setTime(cal1.getTimeInMillis());
             if(p.gettype().equals("Days")){
              cal1.add(Calendar.DATE, p.getno());
              pnum++;
             }else if(p.gettype().equals("Months")){
              cal1.add(Calendar.MONTH, p.getno());
              pnum++;
             }else if(p.gettype().equals("Quaterly")){
              cal1.add(Calendar.MONTH, 3);
              pnum++;
             }else if(p.gettype().equals("Years")){
                cal1.add(Calendar.YEAR, p.getno());
                pnum++;
             }
           }
          }else{
            while(cal1.before(cal)){
              temp.setTime(cal1.getTimeInMillis());
             if(p.gettype().equals("Days")){
              cal1.add(Calendar.DATE, p.getno());
              pnum++;
             }else if(p.gettype().equals("Months")){
              cal1.set(Calendar.DATE, billabledate);
              cal1.add(Calendar.MONTH, p.getno());
              pnum++;
             }else if(p.gettype().equals("Quaterly")){
              cal1.set(Calendar.DATE, billabledate);
              cal1.add(Calendar.MONTH, 3);
              pnum++;
             }else if(p.gettype().equals("Years")){
               cal1.set(Calendar.DATE, billabledate);
               cal1.set(Calendar.MONTH, p.getmonth());
               cal1.add(Calendar.YEAR, p.getno());
               pnum++;
             }
           }
          }
          cal1.set(Calendar.DATE, -1);
          edate.setTime(cal1.getTimeInMillis());
          return edate;
      }
         private Double calulaterenewalamt(Date lbdate,Date edate,Double ramt){
                        Calendar caltemp1=Calendar.getInstance();
                         Calendar caltemp2=Calendar.getInstance();
                         caltemp1.setTimeInMillis(lbdate.getTime());
                         caltemp2.setTimeInMillis(edate.getTime());
                         Date d=new Date();
                       //  Timestamp temp2=caltemp2.getTimeInMillis()
                         long difMil1 = caltemp2.getTimeInMillis() - caltemp1.getTimeInMillis();
                         int days1 = (int)((difMil1 + 12 * 3600000L) / (24 * 3600000L));
                         caltemp1.set(Calendar.DATE, caltemp2.get(Calendar.DATE));
                         long difMil = d.getTime() - caltemp1.getTimeInMillis();
                         int days = (int)((difMil + 12 * 3600000L) / (24 * 3600000L));
                          return dmang.roundTwoDecimals(Math.round(ramt * days/days1));
         }*/
  /*   private void validateinput(JCheckBox temp){

     }*/
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        fname = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jamount = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        ramount = new javax.swing.JTextField();
        uamount = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jImageEditor1 = new com.openbravo.data.gui.JImageEditor();
        jLabel11 = new javax.swing.JLabel();
        uperiod = new javax.swing.JComboBox();
        rperiod = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        billsequence = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        debttype = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jfeeaccount = new WideComboBox();
        jLabel16 = new javax.swing.JLabel();
        usageaccount = new WideComboBox();
        jLabel17 = new javax.swing.JLabel();
        renewalaccount = new WideComboBox();
        jLabel18 = new javax.swing.JLabel();
        typeOfMem = new WideComboBox();
        jLabel19 = new javax.swing.JLabel();
        maxdebt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        standard = new javax.swing.JRadioButton();
        optional = new javax.swing.JRadioButton();
        jLabel21 = new javax.swing.JLabel();
        servicetax = new WideComboBox();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel22 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        date = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        applicableto = new javax.swing.JLabel();
        mem = new javax.swing.JRadioButton();
        memdep = new javax.swing.JRadioButton();
        jLabel24 = new javax.swing.JLabel();
        smssform = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 =  new javax.swing.JTable(){public Component prepareRenderer(TableCellRenderer renderer,
            int rowIndex, int vColIndex) {
            Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                jc.setToolTipText(String.valueOf(getValueAt(rowIndex, vColIndex)));

                if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
                    jc.setBackground(Color.lightGray);
                }
                else {
                    jc.setBackground(Color.white);
                }
                if(isCellSelected(rowIndex, vColIndex))
                jc.setBackground(Color.cyan);
            }
            return c;
        }};
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBox5 = new javax.swing.JCheckBox();

        setToolTipText("New");
        setAutoscrolls(true);
        setLayout(null);

        jPanel1.setLayout(null);

        jLabel3.setText("Amount :");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 10, 44, 14);

        add(jPanel1);
        jPanel1.setBounds(0, 0, 0, 0);

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel2.setLayout(null);

        jLabel1.setText("Name");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(10, 60, 100, 14);

        jLabel2.setText("Joining Fee");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 120, 79, 14);

        jLabel4.setText("Renewal Fee");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(10, 150, 94, 14);

        jLabel7.setText("Usage Fee");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(10, 180, 94, 14);

        jLabel10.setText("Entrance Control");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 210, 100, 14);

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton1.setBounds(600, 10, 80, 30);
        jPanel2.add(fname);
        fname.setBounds(120, 60, 220, 20);

        jCheckBox1.setText("Yes");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jPanel2.add(jCheckBox1);
        jCheckBox1.setBounds(120, 110, 50, 30);

        jCheckBox2.setText("Yes");
        jCheckBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox2ItemStateChanged(evt);
            }
        });
        jPanel2.add(jCheckBox2);
        jCheckBox2.setBounds(120, 140, 50, 30);

        jCheckBox3.setText("Yes");
        jCheckBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox3ItemStateChanged(evt);
            }
        });
        jPanel2.add(jCheckBox3);
        jCheckBox3.setBounds(120, 170, 60, 30);

        jCheckBox4.setText("Yes");
        jPanel2.add(jCheckBox4);
        jCheckBox4.setBounds(120, 210, 60, 23);

        jLabel12.setText("Amount");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(180, 120, 60, 14);

        jamount.setText("0");
        jPanel2.add(jamount);
        jamount.setBounds(250, 120, 60, 20);

        jLabel5.setText("Amount :");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(180, 150, 60, 14);

        ramount.setText("0");
        jPanel2.add(ramount);
        ramount.setBounds(250, 150, 60, 20);

        uamount.setText("0");
        jPanel2.add(uamount);
        uamount.setBounds(250, 180, 60, 20);

        jLabel8.setText("Amount :");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(180, 180, 60, 14);

        jLabel9.setText("Usage Period");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(320, 180, 80, 14);

        jLabel6.setText("Renewal Period");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(320, 150, 90, 14);
        jPanel2.add(jImageEditor1);
        jImageEditor1.setBounds(120, 400, 206, 150);

        jLabel11.setText("Image");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(10, 400, 70, 14);

        jPanel2.add(uperiod);
        uperiod.setBounds(410, 180, 120, 20);

        jPanel2.add(rperiod);
        rperiod.setBounds(410, 150, 120, 20);

        jButton3.setText("Reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);
        jButton3.setBounds(700, 10, 70, 30);

        jLabel15.setText("Bill Sequence");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(10, 280, 90, 14);
        jPanel2.add(billsequence);
        billsequence.setBounds(120, 280, 80, 20);

        jLabel13.setText("Credit Period");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 240, 90, 14);

        jPanel2.add(debttype);
        debttype.setBounds(120, 240, 170, 20);

        jLabel14.setText(" Account");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(320, 120, 60, 14);

        jPanel2.add(jfeeaccount);
        jfeeaccount.setBounds(410, 120, 200, 20);

        jLabel16.setText("Account");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(540, 180, 60, 14);

        usageaccount.setAutoscrolls(true);
        jPanel2.add(usageaccount);
        usageaccount.setBounds(600, 180, 180, 20);

        jLabel17.setText("Account");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(540, 150, 50, 14);

        jPanel2.add(renewalaccount);
        renewalaccount.setBounds(600, 150, 180, 20);

        jLabel18.setText("Type Of Member");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(390, 360, 100, 14);

        jPanel2.add(typeOfMem);
        typeOfMem.setBounds(500, 360, 170, 20);

        jLabel19.setText("Max Debt Allowed");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(10, 360, 100, 14);
        jPanel2.add(maxdebt);
        maxdebt.setBounds(120, 360, 120, 20);

        jLabel20.setText("Type");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(10, 90, 70, 14);

        buttonGroup1.add(standard);
        standard.setText("Standard");
        standard.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                standardStateChanged(evt);
            }
        });
        jPanel2.add(standard);
        standard.setBounds(120, 90, 110, 23);

        buttonGroup1.add(optional);
        optional.setText("Optional");
        optional.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                optionalStateChanged(evt);
            }
        });
        jPanel2.add(optional);
        optional.setBounds(230, 90, 120, 23);

        jLabel21.setText("Service Tax");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(10, 320, 90, 14);

        jPanel2.add(servicetax);
        servicetax.setBounds(120, 320, 170, 20);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnplus.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);
        jButton5.setBounds(680, 360, 40, 20);

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane2.setViewportView(jList1);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(500, 400, 170, 150);

        jLabel22.setText("List of Types");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(390, 400, 90, 14);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnminus.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);
        jButton6.setBounds(680, 400, 40, 20);

        jLabel23.setText("Effective date");
        jPanel2.add(jLabel23);
        jLabel23.setBounds(390, 320, 80, 14);
        jPanel2.add(date);
        date.setBounds(500, 320, 170, 20);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7);
        jButton7.setBounds(680, 320, 40, 25);

        applicableto.setText("Applicable to    :");
        jPanel2.add(applicableto);
        applicableto.setBounds(350, 90, 100, 20);

        buttonGroup2.add(mem);
        mem.setText("Member");
        jPanel2.add(mem);
        mem.setBounds(460, 90, 90, 23);

        buttonGroup2.add(memdep);
        memdep.setText("Member Dependent");
        jPanel2.add(memdep);
        memdep.setBounds(570, 90, 150, 23);

        jLabel24.setText("SMS Short Form");
        jPanel2.add(jLabel24);
        jLabel24.setBounds(380, 60, 100, 14);
        jPanel2.add(smssform);
        smssform.setBounds(490, 60, 160, 20);

        jTabbedPane1.addTab("Create New", jPanel2);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Joining Fee", "Renewal Fee", "Period", "Usage Fee", "Period", "Entrance Chk", "Created by", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton4.setText("Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton2.setText("Deactivate");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox5.setText("Show All");
        jCheckBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox5ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox5)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton2))))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox5)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("List View", jPanel3);

        add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 0, 790, 620);
        jTabbedPane1.getAccessibleContext().setAccessibleName("tab");
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        if(jCheckBox1.isSelected()==true){
          jLabel12.setVisible(true);
          jamount.setVisible(true);
          jLabel14.setVisible(true);
          jfeeaccount.setVisible(true);
          jfeeaccount.setSelectedIndex(0);
          jamount.setText("0");
        }else{
          jLabel12.setVisible(false);
          jamount.setVisible(false);
          jLabel14.setVisible(false);
          jfeeaccount.setVisible(false);
          jfeeaccount.setSelectedIndex(0);
          jamount.setText("0");
        }

    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
        // TODO add your handling code here:
         if(jCheckBox2.isSelected()==true){
           jLabel5.setVisible(true);
           jLabel6.setVisible(true);
           ramount.setVisible(true);
           rperiod.setVisible(true);
           renewalaccount.setVisible(true);
           renewalaccount.setSelectedIndex(0);
           rperiod.setSelectedItem(null);
           jLabel17.setVisible(true);
           ramount.setText("0");
        }else{
           jLabel5.setVisible(false);
           jLabel6.setVisible(false);
           ramount.setVisible(false);
           rperiod.setVisible(false);
           renewalaccount.setVisible(false);
           renewalaccount.setSelectedIndex(0);
           rperiod.setSelectedItem(null);
           jLabel17.setVisible(false);
           ramount.setText("0");
        }
    }//GEN-LAST:event_jCheckBox2ItemStateChanged

    private void jCheckBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox3ItemStateChanged
        // TODO add your handling code here:
         if(jCheckBox3.isSelected()==true){
           jLabel8.setVisible(true);
           jLabel9.setVisible(true);
           uamount.setVisible(true);
           uperiod.setVisible(true);
           uperiod.setSelectedItem(null);
           usageaccount.setVisible(true);
           usageaccount.setSelectedIndex(0);
           jLabel16.setVisible(true);
            uamount.setText("0");
        }else{
           jLabel8.setVisible(false);
           jLabel9.setVisible(false);
           uamount.setVisible(false);
           uperiod.setVisible(false);
           usageaccount.setVisible(false);
            uperiod.setSelectedItem(null);
           usageaccount.setSelectedIndex(0);
             jLabel16.setVisible(false);
             uamount.setText("0");
        }
    }//GEN-LAST:event_jCheckBox3ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //pnum=0;
        try{
         Transaction t = new Transaction(m_App.getSession()) {
         public Object transact() throws BasicException {
            Date d=new Date();
            Date effectivedate=new Date();
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(d.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 00);
            cal.set(Calendar.MILLISECOND,00);
            d.setTime(cal.getTimeInMillis());

            if(fname.getText().length()>0 && debttype.getSelectedIndex() != -1 && maxdebt.getText().length()>0 && billsequence.getText().length()>0 && smssform.getText().length()>0 ){
            int count=Integer.valueOf(new StaticSentence(m_App.getSession()
                    , "SELECT COUNT(*) FROM FACILITY WHERE NAME=? AND ACTIVE=TRUE"
                    ,SerializerWriteString.INSTANCE
                    ,SerializerReadInteger.INSTANCE).find(fname.getText()).toString());
            if(count==0){
             Periodicity p=(Periodicity)rperiod.getSelectedItem();
             String rtemp=null;
             if(p!=null){
               rtemp=p.getid();
             }
             Periodicity p1=(Periodicity)uperiod.getSelectedItem();
             String utemp=null;
             if(p1!=null){
                utemp=p1.getid();
             }
             String type;
             int appto=0;
             boolean flag=true;
             if(standard.isSelected()==true){
                 type="Standard";
                 d=(Date)Formats.TIMESTAMP.parseValue(date.getText());
                 if( memtypelist.size()<=0)
                     flag=false;
             }  else{
                 type="Optional";
                 if(memdep.isSelected())
                   appto=1;
             }
             DebtTypeTableModel.DebtTypeline debt=(DebtTypeTableModel.DebtTypeline)debttype.getSelectedItem();
             String jaccid=null,raccid=null,uaccid=null;
              if(jCheckBox1.isSelected()==true && flag==true){
                if(jfeeaccount.getSelectedIndex()!=-1){
                  AccountMaster amaster=(AccountMaster)jfeeaccount.getSelectedItem();
                  jaccid=amaster.getid();
                  flag=true;
                 }else{
                    flag=false;
                 }
              }

           if(jCheckBox2.isSelected()==true && flag==true){
            if(renewalaccount.getSelectedIndex()!=-1 && rperiod.getSelectedIndex()>0){
               AccountMaster renewalacc=(AccountMaster)renewalaccount.getSelectedItem();
               raccid=renewalacc.getid();
                flag=true;
             }else{
               flag=false;
           }
           }
        if(jCheckBox3.isSelected()==true && flag==true){
           if(usageaccount.getSelectedIndex()!=-1 && uperiod.getSelectedIndex()>0){
             AccountMaster usageacc=(AccountMaster)usageaccount.getSelectedItem();
             uaccid=usageacc.getid();
              flag=true;
           }else{
             flag=false;
          }
         }
          if(flag==true){
              String mtype=null;
              MemTypeListModel mtmodel=(MemTypeListModel)jList1.getModel();
              if(mtmodel.getSize()>0){
                  for(int j=0;j<mtmodel.getSize();j++){
                      if(mtmodel.getElementAt(j).toString().equals("ALL")){
                          mtype="ALL";
                          break;
                      }else{
                          MemTypeTableModel.MemTypeline mem=(MemTypeTableModel.MemTypeline)mtmodel.getElementAt(j);
                          if(mtype==null)
                              mtype=mem.getid();
                          else
                              mtype +="#" + mem.getid();
                      }
                  }
            
              }
              String val=maxdebt.getText();
              String fid=UUID.randomUUID().toString();
              String staxid=null;
              if(servicetax.getSelectedItem()!=null){
                TaxCategoryInfo staxcat=(TaxCategoryInfo)servicetax.getSelectedItem();
                staxid=staxcat.getID();
              }
              if(optional.isSelected()==true)
                  mtype="ALL";
              Object[] value=new Object[]{fid,true,fname.getText(),Double.valueOf(jamount.getText()),Double.valueOf(ramount.getText()),rtemp,Double.valueOf(uamount.getText()),utemp,jCheckBox4.isSelected(),m_App.getAppUserView().getUser().getName(),new Date(),jImageEditor1.getImage(),billsequence.getText(),0,debt.getid(),jaccid,raccid,uaccid,mtype,Double.valueOf(val),type,staxid,appto,smssform.getText()};
              new PreparedSentence(m_App.getSession()
                     , "INSERT INTO FACILITY(ID, ACTIVE, NAME,  JOINAMT,  RENEWALAMT, RPERIODICITY,  USAGEAMT, UPERIODICITY, ENTRANCECONTROL, CREATEDBY, CRDATE,IMAGE,BILLSEQUENCE,MAXBILLNO,DUEPERIOD,JFEEACCOUNT,RENEWALACC,USAGEACC,MEMTYPE,DEBTMAX,TYPE_,SERVICETAX,APPTO,SMSFORM) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                     , new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING,  Datas.DOUBLE,  Datas.DOUBLE, Datas.STRING,  Datas.DOUBLE, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.TIMESTAMP,Datas.IMAGE,Datas.STRING,Datas.INT,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.STRING,Datas.STRING,Datas.INT,Datas.STRING})
                    ).exec(value);
              if(standard.isSelected()==true){
              List<CustomerInfo1> memlist=new ArrayList<CustomerInfo1>();
              if(mtype.equals("ALL")){
                   memlist=new StaticSentence(m_App.getSession()
                      , "SELECT ID,SEARCHKEY,TAXID,NAME,MEMTYPE FROM CUSTOMERS WHERE VISIBLE=TRUE"
                      , null
                      ,new SerializerReadClass(CustomerInfo1.class)).list();
              }else{
                  String[] mtypearr=mtype.split("#");
                  if(mtypearr.length>0){
                   for(int j=0;j<mtypearr.length;j++){
                    List<CustomerInfo1> list=new StaticSentence(m_App.getSession()
                      , "SELECT ID,SEARCHKEY,TAXID,NAME,MEMTYPE FROM CUSTOMERS WHERE MEMTYPE= ? AND VISIBLE=TRUE"
                      , SerializerWriteString.INSTANCE
                      ,new SerializerReadClass(CustomerInfo1.class)).list(mtypearr[j]);
                    memlist.addAll(list);
                   }
                  }
              }
              FacilityLogic flogic=new FacilityLogic(dmang);
              Date duedate=new Date();
              Date duedate1=new Date();
              duedate1.setTime(flogic.getDueDate(debt, d).getTime());
              Double jamt=Double.parseDouble(jamount.getText());
              Double ramt=Double.parseDouble(ramount.getText());
              Double taxrate=0.0;
             // StringBuffer billnums=new StringBuffer();
              if(servicetax.getSelectedItem()!=null){
               TaxCategoryInfo tinfo=(TaxCategoryInfo)servicetax.getSelectedItem();
               TaxInfo tax = taxeslogic.getTaxInfo(tinfo);
               taxrate=tax.getRate();
              
              }
              Date edate=new Date();
              Date lbdate=new Date();
              lbdate.setTime(d.getTime());
              Calendar cal1=Calendar.getInstance();
              cal1.setTimeInMillis(lbdate.getTime());
              int billabledate=cal1.get(Calendar.DATE);
              if(p.getqbtype()==true){
                 if(p.getdoj()==false){
                   cal1.setTimeInMillis(flogic.getBillableDate(p, billabledate, cal1).getTimeInMillis());
                }
              }
              lbdate.setTime(cal1.getTimeInMillis());
              //temp=new Date();
              //temp.setTime(lbdate.getTime());
              billabledate=cal1.get(Calendar.DATE);
              flogic.setTemp(lbdate);
              edate.setTime(flogic.calculateEndDate(lbdate,p,billabledate,1,new Date()).getTime());
               if(p.getqbtype()==false)
                 duedate.setTime(flogic.getDueDate(debt,new Date()).getTime());
              else
                  duedate.setTime(flogic.getDueDate(debt,flogic.getTemp()).getTime());
              if(p.getaccurate()==true){
                ramt=flogic.calulaterenewalamt(lbdate,edate,ramt);
              }else{
                if(flogic.getPnum()>0)
                    ramt=flogic.getPnum()*ramt;
              }
               String billno=dmang.getnewbillno(fid);
               dmang.updatebillno(fid);
               //Service Tax Account
               String servicetaxacc=null;
               Object stacc=new StaticSentence(m_App.getSession()
                       , "SELECT VALUE FROM GENERALTABLE WHERE NAME = ? "
                       , SerializerWriteString.INSTANCE
                       ,SerializerReadString.INSTANCE).find("Service Tax Account");
               if(stacc!=null)
                   servicetaxacc=stacc.toString();
              for(int j=0;j<memlist.size();j++){
                  String cid=memlist.get(j).getId();
                  int count1=Integer.valueOf(new StaticSentence(m_App.getSession()
                    , "SELECT COUNT(*) FROM MEMFACILITYUSAGE WHERE MEMNO=? AND FACILITYTYPE=? AND ACTIVE=TRUE AND USERID IS NULL"
                    ,new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
                    ,SerializerReadInteger.INSTANCE).find(new Object[]{cid,fid}).toString());
               if(count1==0){
                  int flag1=0;
                  String caccount=null;
                  try{
                     caccount=dmang.getCustomerAccountByID(cid);
                  }catch(Exception e1){
                     flag1=1;
                     JOptionPane.showMessageDialog(null, "Member Account not present "+memlist.get(j).getName() +" .Please create one", null,JOptionPane.OK_OPTION);
                 }
               if(flag1==0){
                double totalamt=0.0;
               if(jCheckBox1.isSelected()==true){
                   if(jamt>0){
               // DebtTypeTableModel.DebtTypeline periodtype=(DebtTypeTableModel.DebtTypeline)debttype.getSelectedItem();
                  double taxamt=Math.floor(taxrate*jamt);
                  String tid =UUID.randomUUID().toString();
                  String facname=fname.getText();
                  if(taxamt>0){
                    if(servicetaxacc==null)
                        JOptionPane.showMessageDialog(null, "Service tax account is missing","Error",JOptionPane.OK_OPTION);
                   Object[] value4=new Object[]{UUID.randomUUID().toString(),tid,d,"C",fid,billno,taxamt,d,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Service tax on Join Fees for "+facname,servicetaxacc,0.0,d,new Date(),true};
                   dmang.insertintoaccjoutnal3(value4);
                  }
                  double tjamt=dmang.roundTwoDecimals(jamt+taxamt);
                  Object[] value2=new Object[]{UUID.randomUUID().toString(),tid,d,"C",fid,billno,jamt,d,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Join Fees for "+facname,jaccid,0.0,d,new Date(),true};
                  dmang.insertintoaccjoutnal3(value2);
                  Object[] value3=new Object[]{UUID.randomUUID().toString(),tid,cid,d,"D",fid,billno,tjamt,d,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Join Fees for "+facname,caccount,tjamt,new Date(),true};
                  dmang.insertintoaccjoutnal0(value3);
                  totalamt=tjamt;
                 // dmang.updatebillno(fid);
                   }
                 }
               
               // String billnum=null;
                if(p.getqbtype()==true){
                 if(ramt>0){
                  double taxamt=Math.floor(taxrate*ramt);
                  String tid =UUID.randomUUID().toString();

                  String facname=fname.getText();
                  if(taxamt>0){
                   if(servicetaxacc==null)
                        JOptionPane.showMessageDialog(null, "Service tax account is missing","Error",JOptionPane.OK_OPTION);
                   Object[] value4=new Object[]{UUID.randomUUID().toString(),tid,d,"C",fid,billno,taxamt,d,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Service tax on Renewal Fees for "+facname,servicetaxacc,0.0,d,new Date(),true};
                   dmang.insertintoaccjoutnal3(value4);
                  }
                  double tramt=dmang.roundTwoDecimals(ramt+taxamt);
                  Object[] value2=new Object[]{UUID.randomUUID().toString(),tid,d,"C",fid,billno,ramt,d,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Renewal Fees for Period :"+ Formats.DATE.formatValue(lbdate)+" to "+Formats.DATE.formatValue(edate),raccid,0.0,d,new Date(),true};
                  dmang.insertintoaccjoutnal3(value2);
                  Object[] value3=new Object[]{UUID.randomUUID().toString(),tid,cid,d,"D",fid,billno,tramt,duedate,false,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Renewal Fees for Period :"+ Formats.DATE.formatValue(lbdate)+" to "+Formats.DATE.formatValue(edate),caccount,tramt,new Date(),true};
                  dmang.insertintoaccjoutnal0(value3);
                  totalamt+=tramt;
                 }
                }
                 if(p.getqbtype()==false){
                   Object[] value1=new Object[]{UUID.randomUUID().toString(),true,cid,fid,d,m_App.getAppUserView().getUser().getName(),d,0};
                   new PreparedSentence(m_App.getSession()
                   , "INSERT INTO MEMFACILITYUSAGE(ID, ACTIVE, MEMNO, FACILITYTYPE, SDATE, CREATEDBY, CRDATE,STATUS_) VALUES (?,?,?,?,?,?,?,?)"
                   , new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING,  Datas.STRING, Datas.TIMESTAMP,Datas.STRING, Datas.TIMESTAMP,Datas.INT})
                   ).exec(value1);
                }else{
                   Object[] value1=new Object[]{UUID.randomUUID().toString(),true,cid,fid,d,m_App.getAppUserView().getUser().getName(),new Date(),edate,new Date(),billno,0};
                   new PreparedSentence(m_App.getSession()
                   , "INSERT INTO MEMFACILITYUSAGE(ID, ACTIVE, MEMNO, FACILITYTYPE, SDATE, CREATEDBY, CRDATE,LBILLDATE,NBILLDATE,BILLREF,STATUS_) VALUES (?,?,?,?,?,?,?,?,?,?,?)"
                   , new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING,  Datas.STRING, Datas.TIMESTAMP,Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING,Datas.INT})
                   ).exec(value1);
                }
                dmang.setmemberDebt(cid, fid, totalamt);
              }
            }
          }
          /*    if(jCheckBox1.isSelected()==true && taxamt>0){
                 taxamt= memlist.size()*taxamt;
                 Object[] value1=new Object[]{UUID.randomUUID().toString(),"transid",d,"C",fid,billnums,taxamt,d,true,m_App.getAppUserView().getUser().getName(),m_App.getProperties().getHost(),"Service tax for bill numbers "+billnums,"",0.0,d};
                  dmang.insertintoaccjoutnal1(value1);
              }*/
          }
              reset();
              set();
             }else{
                JOptionPane.showMessageDialog(null, "Account might not be selected", null, JOptionPane.OK_OPTION);
             }
            }else{
                JOptionPane.showMessageDialog(null, "Facility with the name "+fname.getText()+" already exist", null, JOptionPane.OK_OPTION);
            }
         }else{
                JOptionPane.showMessageDialog(null, "Please fill the form completely", "Incomplete Form", JOptionPane.OK_OPTION);
         }
            return null;
           }
         };
         t.execute();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting values.Ensure Correct values are inserted", "Error", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        set();
        reset();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try{
            int row=jTable1.getSelectedRow();
            if(row < jTable1.getRowCount() && row >= 0){
               String id=jTable1.getModel().getValueAt(row, 18).toString();
               String fname1=jTable1.getModel().getValueAt(row,0 ).toString();
               if(JOptionPane.showConfirmDialog(this, "Do you want to deactivate "+fname1+" facility ?",null ,JOptionPane.YES_NO_OPTION )==JOptionPane.YES_OPTION){
               new PreparedSentence(m_App.getSession()
                             ,"UPDATE FACILITY SET ACTIVE=FALSE,DEACTIVATEDBY =?,DEACTIVATEDDATE=? WHERE ID=? AND DEACTIVATEDBY IS NULL AND DEACTIVATEDDATE IS NULL"
                             , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{m_App.getAppUserView().getUser().getName(),new Date(),id});
                    if(jCheckBox5.isSelected()==true){
               fmodel = FacilitytableModel.loadInstance(m_App,2);
               jTable1.setModel(fmodel.getTableModel());
               }else
                 if(jCheckBox5.isSelected()==false){
                    fmodel = FacilitytableModel.loadInstance(m_App,1);
                     jTable1.setModel(fmodel.getTableModel());
               }
               }
            }
        }
        catch(Exception e){
           e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        javax.swing.JTabbedPane tabpane=(javax.swing.JTabbedPane)evt.getSource();
       int tabno=tabpane.getSelectedIndex();
       if(tabno==1){
           try{
             if(jCheckBox5.isSelected()==true){
               fmodel = FacilitytableModel.loadInstance(m_App,2);
               jTable1.setModel(fmodel.getTableModel());
               }else
                 if(jCheckBox5.isSelected()==false){
                    fmodel = FacilitytableModel.loadInstance(m_App,1);
                     jTable1.setModel(fmodel.getTableModel());
               }
          }
          catch(Exception e){
            e.printStackTrace();
          }
       }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int row=jTable1.getSelectedRow();
        try{
        if(row>=0 && row<jTable1.getRowCount()){
         BufferedImage img;
         ImageIcon icon;
          String id=jTable1.getModel().getValueAt(row, 11).toString();
          Object obj[]=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT IMAGE FROM FACILITY WHERE ID = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.IMAGE})).find(id);
            if(obj!=null)
            {
                if(obj[0]!=null){
                img=(BufferedImage)obj[0];

                icon=new ImageIcon(img);
                jLabel1.setIcon(icon);
                }
            }
        }
      }catch(Exception e){
          e.printStackTrace();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
         int row=jTable1.getSelectedRow();
       // try{
        if(row>=0 && row<jTable1.getRowCount()){
          //  JOptionPane.showInternalConfirmDialog(this, "aa");
         }
      //  }catch(Exception e){

      //  }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jCheckBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox5ItemStateChanged
        // TODO add your handling code here:
        try{
        if(jCheckBox5.isSelected()==true){
           fmodel = FacilitytableModel.loadInstance(m_App,2);
           jTable1.setModel(fmodel.getTableModel());
        }else
            if(jCheckBox5.isSelected()==false){
               fmodel = FacilitytableModel.loadInstance(m_App,1);
               jTable1.setModel(fmodel.getTableModel());
            }
        }catch(Exception e){
          e.printStackTrace();
        }
    }//GEN-LAST:event_jCheckBox5ItemStateChanged

    private void standardStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_standardStateChanged
        // TODO add your handling code here:
        if(standard.isSelected()){
            typeOfMem.setVisible(true);
            jLabel18.setVisible(true);
            jLabel22.setVisible(true);
            jScrollPane2.setVisible(true);
            jButton5.setVisible(true);
            jButton6.setVisible(true);
            jLabel23.setVisible(true);
            jButton7.setVisible(true);
            date.setVisible(true);
            date.setText(Formats.TIMESTAMP.formatValue(new Date()));
            applicableto.setVisible(false);
            mem.setVisible(false);
            memdep.setVisible(false);
        }
      /*  else{
            typeOfMem.setVisible(false);
            jLabel18.setVisible(false);
            jLabel22.setVisible(false);
            jScrollPane2.setVisible(false);
            jButton5.setVisible(false);
            jButton6.setVisible(false);
            jLabel23.setVisible(false);
            jButton7.setVisible(false);
            date.setVisible(false);
        }*/
    }//GEN-LAST:event_standardStateChanged

    private void optionalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_optionalStateChanged
        // TODO add your handling code here:
        if(optional.isSelected()){
            typeOfMem.setVisible(false);
            jLabel18.setVisible(false);
            jLabel22.setVisible(false);
            jScrollPane2.setVisible(false);
            jButton5.setVisible(false);
            jButton6.setVisible(false);
            jLabel23.setVisible(false);
            jButton7.setVisible(false);
            date.setVisible(false);
            applicableto.setVisible(true);
            mem.setVisible(true);
            memdep.setVisible(true);
            mem.setSelected(true);
        }
     /*   else{
            typeOfMem.setVisible(true);
            jLabel18.setVisible(true);
            jLabel22.setVisible(true);
            jList1.setVisible(true);
            jButton5.setVisible(true);
            jButton6.setVisible(true);
            jLabel23.setVisible(true);
            jButton7.setVisible(true);
            date.setVisible(true);
            date.setText(Formats.TIMESTAMP.formatValue(new Date()));
            applicableto.setVisible(false);
            mem.setVisible(false);
            memdep.setVisible(false);
        }*/
    }//GEN-LAST:event_optionalStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if(typeOfMem.getSelectedIndex()!=-1){
            if(!memtypelist.contains("ALL")){
          // }else{
            if(typeOfMem.getSelectedItem().toString().equals("ALL")){
               memtypelist.clear();
               memtypelist.add(typeOfMem.getSelectedItem());
            }else
             memtypelist.add(typeOfMem.getSelectedItem());
             mmodel=new MemTypeListModel(memtypelist);
             jList1.setModel(mmodel);
         }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        int row=jList1.getSelectedIndex();
        if(row>=0){
            memtypelist.remove(row);
            mmodel=new MemTypeListModel(memtypelist);
            jList1.setModel(mmodel);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
          Date d;
        try {
            d = (Date) Formats.TIMESTAMP.parseValue(date.getText());
        } catch (BasicException e) {
            d = null;
        }
        d = JCalendarDialog.showCalendarTimeHours(this, d);
        if (d != null) {
           // date=getDate(date);
            date.setText(Formats.TIMESTAMP.formatValue(d));
        }
    }//GEN-LAST:event_jButton7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel applicableto;
    private javax.swing.JTextField billsequence;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JTextField date;
    private javax.swing.JComboBox debttype;
    private javax.swing.JTextField fname;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private com.openbravo.data.gui.JImageEditor jImageEditor1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jamount;
    private javax.swing.JComboBox jfeeaccount;
    private javax.swing.JTextField maxdebt;
    private javax.swing.JRadioButton mem;
    private javax.swing.JRadioButton memdep;
    private javax.swing.JRadioButton optional;
    private javax.swing.JTextField ramount;
    private javax.swing.JComboBox renewalaccount;
    private javax.swing.JComboBox rperiod;
    private javax.swing.JComboBox servicetax;
    private javax.swing.JTextField smssform;
    private javax.swing.JRadioButton standard;
    private javax.swing.JComboBox typeOfMem;
    private javax.swing.JTextField uamount;
    private javax.swing.JComboBox uperiod;
    private javax.swing.JComboBox usageaccount;
    // End of variables declaration//GEN-END:variables

}
