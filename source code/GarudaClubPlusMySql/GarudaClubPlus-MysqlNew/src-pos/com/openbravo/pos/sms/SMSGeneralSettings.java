/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sms;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.util.ICallBack;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import com.openbravo.pos.sms.SMSgeneralDBSettings;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Akash
 */
public class SMSGeneralSettings extends javax.swing.JPanel implements JPanelView ,BeanFactoryApp {

    private static final String TITLE = "SMS Settings";
    private AppView m_AppView;
    private SMSgeneralDBSettings smsDBSettings;
    private final String DATA_SAVED_MSG = "Data Saved !";
    private final String DATA_SAVE_TITLE = "Success";
    private Map<String,String> predefine_list = new HashMap();
    private final static String ERROR_EMPTY_FACILITY = "Select facility to remove.";
    private DefaultComboBoxModel combo_box_model = new DefaultComboBoxModel();
    private static final String EMTPTY_ERROR_MESSAGE = "No message selected. Please enter message.";
    private List<String> facilityList = new ArrayList();
    private static final String SPACE_TEXT = " ";
    private List<String> qt_selected_facility = new ArrayList();
    private List<String> bill_selected_facility = new ArrayList();
    private List<String> st_selected_facility = new ArrayList();
    
    private DefaultListModel qt_fac_list_model  = new DefaultListModel();
    private DefaultListModel bill_fac_list_model  = new DefaultListModel();
    private DefaultListModel st_fac_list_model  = new DefaultListModel();
    
    
    public SMSGeneralSettings()
    {
        initComponents();
    }
    
    @Override
    public void init(AppView app) throws BeanFactoryException 
    {
       // class initialization starts here 
       m_AppView = app;
       smsDBSettings = (SMSgeneralDBSettings) m_AppView.getBean("com.openbravo.pos.sms.SMSgeneralDBSettings");
       initializeList();
    }
    @Override
    public void activate() throws BasicException 
    {
        initButtonGroups();
        loaddata();
    }
    
    
    private void loaddata()
    {
        loadSmsMasterInfo();
        loadFacilityList();
    }
    
    private void initializeList()
    {
        predefine_list = new HashMap();
        predefine_list.put("QT No.", SMSgeneralDBSettings.SMS_QT_KEY);
        predefine_list.put("Bill No.", SMSgeneralDBSettings.SMS_BILL_KEY);
        predefine_list.put("Date & Time", SMSgeneralDBSettings.SMS_DTM_KEY);
        predefine_list.put("Facility Name", SMSgeneralDBSettings.SMS_FACILITY_KEY);
        predefine_list.put("Guest Chrg No.", SMSgeneralDBSettings.SMS_GUEST_KEY);
        predefine_list.put("Wharehouse", SMSgeneralDBSettings.SMS_WHAREHOUSE_NAME_KEY);
        predefine_list.put("Role", SMSgeneralDBSettings.SMS_ROLE_KEY);
        
        List list  = new ArrayList<Object>(predefine_list.values());
        Set<String> set = predefine_list.keySet();
         
        for(String key : set)
        {
            combo_box_model.addElement(key);
        }
        combo_box_smsPrefix.setModel(combo_box_model);
        
        qt_fac_list_model  = new DefaultListModel();
        bill_fac_list_model  = new DefaultListModel();
        st_fac_list_model  = new DefaultListModel();
    }
    
    private void loadSmsMasterInfo()
    {
        List<SMSgeneralDBSettings.SmsMasterInfo> list = smsDBSettings.getSmsMasterList();
        resetAllMenu();
        if(list.size() > 0)
        {
            for(int i=0; i<list.size(); i++)
            {
                String smsId = list.get(i).getsmsId();
                switch(smsId)
                {
                    case SMSgeneralDBSettings.SMS_QT_ID:
                    {
                        setQTdetails(list.get(i));
                        break;
                    }
                    case SMSgeneralDBSettings.SMS_BILL_ID:
                    {
                        setBillDetails(list.get(i));
                        break;
                    }
                    case SMSgeneralDBSettings.SMS_SHARED_TKT_ID:
                    {
                        setSharedTktDetails(list.get(i));
                        break;
                    }
                    case SMSgeneralDBSettings.SMS_ACCOUNT_ID:
                    {
                        if(list.get(i).getActive().equals("0"))
                            account_radio_button_no.setSelected(true);
                        else
                             account_radio_button_yes.setSelected(true);
                        text_area_account.setText(list.get(i).getMessage());
                        break;
                    }
                    case SMSgeneralDBSettings.SMS_GUEST_ID:
                    {
                        if(list.get(i).getActive().equals("0"))
                            guest_radio_button_no.setSelected(true);
                        else
                             guest_radio_button_yes.setSelected(true);
                        text_area_guest.setText(list.get(i).getMessage());
                        break;
                    }
                }
            }
            
        }
    }
    
    
    // set qt details to UI
    public void setQTdetails(SMSgeneralDBSettings.SmsMasterInfo smsInfo)
    {
        if(smsInfo.getActive().equals("0"))
            qt_radio_button_no.setSelected(true);
        else
             qt_radio_button_yes.setSelected(true);
        text_area_qt.setText(smsInfo.getMessage());

        // set facility model
        qt_selected_facility = smsInfo.getFacilityList();
        if(smsInfo.getFacilityList().size() > 0 )
        {
            qt_fac_list_model = new DefaultListModel();
            List<String> qt_fac_list = smsInfo.getFacilityList();
            for(String  x : qt_fac_list)
            {
                qt_fac_list_model.addElement(x);
            }
        }
        qt_facility_jList.setModel(qt_fac_list_model);
    }
    
    // set Bill details to UI
    public void setBillDetails(SMSgeneralDBSettings.SmsMasterInfo smsInfo)
    {
        if(smsInfo.getActive().equals("0"))
            bill_radio_button_no.setSelected(true);
        else
            bill_radio_button_yes.setSelected(true);
        text_area_bill.setText(smsInfo.getMessage());

        bill_selected_facility = smsInfo.getFacilityList();
        if(smsInfo.getFacilityList().size() > 0 )
        {
            bill_fac_list_model = new DefaultListModel();
            List<String> bill_fac_list = smsInfo.getFacilityList();
            for(String  x : bill_fac_list)
            {
                bill_fac_list_model.addElement(x);
            }
        }
        bill_facility_jList.setModel(bill_fac_list_model);
    }
    
    
    // set shared ticket details
     public void setSharedTktDetails(SMSgeneralDBSettings.SmsMasterInfo smsInfo)
    { 
        if(smsInfo.getActive().equals("0"))
            st_radio_button_no.setSelected(true);
        else
             st_radio_button_yes.setSelected(true);
        text_area_st.setText(smsInfo.getMessage());


        st_selected_facility = smsInfo.getFacilityList();
        if(smsInfo.getFacilityList().size() > 0 )
        {
            List<String> st_fac_list = smsInfo.getFacilityList();
            st_fac_list_model = new DefaultListModel();
            for(String  x : st_fac_list)
            {
                st_fac_list_model.addElement(x);
            }
        }
        st_facility_jList.setModel(st_fac_list_model);
    }
    
    public void resetAllMenu()
    {
        qt_radio_button_no.setSelected(true);
        bill_radio_button_no.setSelected(true);
        st_radio_button_no.setSelected(true);
        account_radio_button_no.setSelected(true);
        guest_radio_button_no.setSelected(true);
    }
   
    public void loadFacilityList()
    {
        facilityList = smsDBSettings.getAllFacilityList();
    }
    
    
    private void initButtonGroups()
    {
        ButtonGroup qtGroup = new ButtonGroup();
        qtGroup.add(qt_radio_button_no);
        qtGroup.add(qt_radio_button_yes);
        
        ButtonGroup billGroup = new ButtonGroup();
        billGroup.add(bill_radio_button_no);
        billGroup.add(bill_radio_button_yes);
        
        ButtonGroup stGroup = new ButtonGroup();
        stGroup.add(st_radio_button_no);
        stGroup.add(st_radio_button_yes);
        
        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(account_radio_button_no);
        accountGroup.add(account_radio_button_yes);
        
        ButtonGroup guestChrgGroup = new ButtonGroup();
        guestChrgGroup.add(guest_radio_button_no);
        guestChrgGroup.add(guest_radio_button_yes);
        
    }
    
    @Override
    public String getTitle() {
        return TITLE;
    }

    

    @Override
    public boolean deactivate() {
         return true;
    }

    @Override
    public JComponent getComponent() {
         return this;
    }

    

    @Override
    public Object getBean() {
        return this;
    }
    
    
    public void setSMSFlag(String smsId , String smsName, String active, String message, List<String> falicityList)
    {
        smsDBSettings.setSMSflag(smsId , smsName, active , message, falicityList ,new ICallBack() 
        {
                @Override
                public void onSuccess() 
                {
                   JOptionPane.showMessageDialog(SMSGeneralSettings.this, DATA_SAVED_MSG, DATA_SAVE_TITLE, JOptionPane.INFORMATION_MESSAGE);
                }

                @Override
                public void onError() 
                {
                  JOptionPane.showMessageDialog(SMSGeneralSettings.this,"Error while saving.", "Error", JOptionPane.ERROR_MESSAGE);
                }
        });
    }
    
    public String facilityDialogBox()
    {
        JComboBox<String> comboBox = new JComboBox<String>();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(String x : facilityList)
        {
             model.addElement(x);
        }
       comboBox.setModel(model);

        Object[] options = {comboBox, "OK"};
        int x = JOptionPane.showOptionDialog(null, "Select Facility",
                "Select Facility",JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
        
        if (x == JOptionPane.NO_OPTION) 
        {
           return comboBox.getSelectedItem().toString();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        qt_radio_button_yes = new javax.swing.JRadioButton();
        qt_radio_button_no = new javax.swing.JRadioButton();
        save_qt_btn = new javax.swing.JButton();
        qt_panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        text_area_qt = new javax.swing.JTextArea();
        clear_button_qt = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        qt_facility_jList = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        qt_fac_add_btn = new javax.swing.JButton();
        qt_fac_remove_btn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        bill_radio_button_yes = new javax.swing.JRadioButton();
        bill_radio_button_no = new javax.swing.JRadioButton();
        save_bill_btn1 = new javax.swing.JButton();
        bill_panel = new javax.swing.JPanel();
        clear_button_bill = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        text_area_bill = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        bill_facility_jList = new javax.swing.JList<>();
        jLabel12 = new javax.swing.JLabel();
        bill_fac_add_btn = new javax.swing.JButton();
        bill_fac_remove_btn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        st_radio_button_yes = new javax.swing.JRadioButton();
        st_radio_button_no = new javax.swing.JRadioButton();
        save_st_btn = new javax.swing.JButton();
        shared_tkt_panel = new javax.swing.JPanel();
        clear_button_shared = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        text_area_st = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        st_facility_jList = new javax.swing.JList<>();
        st_fac_add_btn = new javax.swing.JButton();
        st_fac_remove_btn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        guest_radio_button_yes = new javax.swing.JRadioButton();
        guest_radio_button_no = new javax.swing.JRadioButton();
        save_guest_btn = new javax.swing.JButton();
        guest_chrg_panel = new javax.swing.JPanel();
        clear_button_guest = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        text_area_guest = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        account_radio_button_yes = new javax.swing.JRadioButton();
        account_radio_button_no = new javax.swing.JRadioButton();
        save_act_btn = new javax.swing.JButton();
        account_panel = new javax.swing.JPanel();
        clear_button_account = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        text_area_account = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        combo_box_smsPrefix = new javax.swing.JComboBox<>();
        add_text_button = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPane1.setForeground(new java.awt.Color(51, 51, 255));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Send SMS while creating  QT ");

        qt_radio_button_yes.setText("Yes");

        qt_radio_button_no.setText("No");
        qt_radio_button_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                qt_radio_button_noItemStateChanged(evt);
            }
        });

        save_qt_btn.setText("Save");
        save_qt_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_qt_btnActionPerformed(evt);
            }
        });

        text_area_qt.setColumns(20);
        text_area_qt.setRows(5);
        jScrollPane1.setViewportView(text_area_qt);

        clear_button_qt.setText("Clear Message");
        clear_button_qt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_button_qtActionPerformed(evt);
            }
        });

        qt_facility_jList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(qt_facility_jList);

        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("Type SMS here : ");

        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("Select facility : ");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        qt_fac_add_btn.setText("Add Facility");
        qt_fac_add_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qt_fac_add_btnActionPerformed(evt);
            }
        });

        qt_fac_remove_btn.setText("Remove");
        qt_fac_remove_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qt_fac_remove_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout qt_panelLayout = new javax.swing.GroupLayout(qt_panel);
        qt_panel.setLayout(qt_panelLayout);
        qt_panelLayout.setHorizontalGroup(
            qt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qt_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(qt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clear_button_qt)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(qt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(qt_panelLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(qt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(qt_fac_add_btn)
                            .addComponent(qt_fac_remove_btn))))
                .addContainerGap())
        );
        qt_panelLayout.setVerticalGroup(
            qt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qt_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(qt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(qt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(qt_panelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(clear_button_qt))
                    .addGroup(qt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(qt_panelLayout.createSequentialGroup()
                            .addComponent(qt_fac_add_btn)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(qt_fac_remove_btn))
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(qt_radio_button_yes)
                        .addGap(18, 18, 18)
                        .addComponent(qt_radio_button_no)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(qt_panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(save_qt_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(211, 211, 211)))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(qt_radio_button_yes)
                    .addComponent(qt_radio_button_no))
                .addGap(11, 11, 11)
                .addComponent(qt_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(save_qt_btn)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("QT", jPanel1);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Send SMS while creating Bill ");

        bill_radio_button_yes.setText("Yes");

        bill_radio_button_no.setText("No");
        bill_radio_button_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bill_radio_button_noItemStateChanged(evt);
            }
        });

        save_bill_btn1.setText("Save");
        save_bill_btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_bill_btn1ActionPerformed(evt);
            }
        });

        clear_button_bill.setText("Clear Message");
        clear_button_bill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_button_billActionPerformed(evt);
            }
        });

        text_area_bill.setColumns(20);
        text_area_bill.setRows(5);
        jScrollPane2.setViewportView(text_area_bill);

        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("Type SMS here :");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        bill_facility_jList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(bill_facility_jList);

        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("Select facility : ");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        bill_fac_add_btn.setText("Add Facility");
        bill_fac_add_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_fac_add_btnActionPerformed(evt);
            }
        });

        bill_fac_remove_btn.setText("Remove");
        bill_fac_remove_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_fac_remove_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bill_panelLayout = new javax.swing.GroupLayout(bill_panel);
        bill_panel.setLayout(bill_panelLayout);
        bill_panelLayout.setHorizontalGroup(
            bill_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bill_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bill_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clear_button_bill)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bill_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bill_panelLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bill_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bill_fac_add_btn)
                            .addComponent(bill_fac_remove_btn))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bill_panelLayout.setVerticalGroup(
            bill_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bill_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bill_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bill_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bill_panelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clear_button_bill))
                    .addGroup(bill_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(bill_panelLayout.createSequentialGroup()
                            .addComponent(bill_fac_add_btn)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bill_fac_remove_btn))
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bill_radio_button_yes)
                        .addGap(18, 18, 18)
                        .addComponent(bill_radio_button_no)
                        .addGap(0, 265, Short.MAX_VALUE))
                    .addComponent(bill_panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(save_bill_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(192, 192, 192))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(bill_radio_button_yes)
                    .addComponent(bill_radio_button_no))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bill_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(save_bill_btn1)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Bill", jPanel2);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Send SMS while creating Shared Ticket ");

        st_radio_button_yes.setText("Yes");

        st_radio_button_no.setText("No");
        st_radio_button_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                st_radio_button_noItemStateChanged(evt);
            }
        });

        save_st_btn.setText("Save");
        save_st_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_st_btnActionPerformed(evt);
            }
        });

        clear_button_shared.setText("Clear Message");
        clear_button_shared.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_button_sharedActionPerformed(evt);
            }
        });

        text_area_st.setColumns(20);
        text_area_st.setRows(5);
        jScrollPane3.setViewportView(text_area_st);

        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("Type SMS here :");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("Select facility : ");
        jLabel14.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        st_facility_jList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane8.setViewportView(st_facility_jList);

        st_fac_add_btn.setText("Add Facility");
        st_fac_add_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                st_fac_add_btnActionPerformed(evt);
            }
        });

        st_fac_remove_btn.setText("Remove");
        st_fac_remove_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                st_fac_remove_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout shared_tkt_panelLayout = new javax.swing.GroupLayout(shared_tkt_panel);
        shared_tkt_panel.setLayout(shared_tkt_panelLayout);
        shared_tkt_panelLayout.setHorizontalGroup(
            shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shared_tkt_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clear_button_shared))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(shared_tkt_panelLayout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(st_fac_add_btn)
                            .addComponent(st_fac_remove_btn))))
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(shared_tkt_panelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(407, Short.MAX_VALUE)))
        );
        shared_tkt_panelLayout.setVerticalGroup(
            shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shared_tkt_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, shared_tkt_panelLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clear_button_shared)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, shared_tkt_panelLayout.createSequentialGroup()
                        .addGroup(shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(shared_tkt_panelLayout.createSequentialGroup()
                                .addComponent(st_fac_add_btn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(st_fac_remove_btn))
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))))
            .addGroup(shared_tkt_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(shared_tkt_panelLayout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(192, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 330, Short.MAX_VALUE)
                        .addComponent(save_st_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(181, 181, 181))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(st_radio_button_yes)
                                .addGap(18, 18, 18)
                                .addComponent(st_radio_button_no))
                            .addComponent(shared_tkt_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(st_radio_button_yes)
                    .addComponent(st_radio_button_no))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(shared_tkt_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(save_st_btn)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Shared ticket", jPanel3);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("Send SMS while creating Guest Charges ");

        guest_radio_button_yes.setText("Yes");

        guest_radio_button_no.setText("No");
        guest_radio_button_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                guest_radio_button_noItemStateChanged(evt);
            }
        });

        save_guest_btn.setText("Save");
        save_guest_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_guest_btnActionPerformed(evt);
            }
        });

        clear_button_guest.setText("Clear Message");
        clear_button_guest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_button_guestActionPerformed(evt);
            }
        });

        text_area_guest.setColumns(20);
        text_area_guest.setRows(5);
        jScrollPane5.setViewportView(text_area_guest);

        javax.swing.GroupLayout guest_chrg_panelLayout = new javax.swing.GroupLayout(guest_chrg_panel);
        guest_chrg_panel.setLayout(guest_chrg_panelLayout);
        guest_chrg_panelLayout.setHorizontalGroup(
            guest_chrg_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guest_chrg_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(guest_chrg_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clear_button_guest))
                .addContainerGap())
        );
        guest_chrg_panelLayout.setVerticalGroup(
            guest_chrg_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guest_chrg_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clear_button_guest)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(guest_chrg_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(save_guest_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(guest_radio_button_yes)
                        .addGap(18, 18, 18)
                        .addComponent(guest_radio_button_no)))
                .addContainerGap(215, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(guest_radio_button_yes)
                    .addComponent(guest_radio_button_no))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(guest_chrg_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(save_guest_btn)
                .addContainerGap(173, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Guest Charges", jPanel4);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 51, 0));
        jLabel6.setText("Send SMS while debiting to Account ");

        account_radio_button_yes.setText("Yes");

        account_radio_button_no.setText("No");
        account_radio_button_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                account_radio_button_noItemStateChanged(evt);
            }
        });

        save_act_btn.setText("Save");
        save_act_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_act_btnActionPerformed(evt);
            }
        });

        clear_button_account.setText("Clear Message");
        clear_button_account.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_button_accountActionPerformed(evt);
            }
        });

        text_area_account.setColumns(20);
        text_area_account.setRows(5);
        jScrollPane4.setViewportView(text_area_account);

        javax.swing.GroupLayout account_panelLayout = new javax.swing.GroupLayout(account_panel);
        account_panel.setLayout(account_panelLayout);
        account_panelLayout.setHorizontalGroup(
            account_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(account_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(account_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clear_button_account))
                .addContainerGap())
        );
        account_panelLayout.setVerticalGroup(
            account_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(account_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clear_button_account)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(account_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(account_radio_button_yes)
                        .addGap(18, 18, 18)
                        .addComponent(account_radio_button_no))
                    .addComponent(save_act_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(219, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(account_radio_button_yes)
                    .addComponent(account_radio_button_no))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(account_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(save_act_btn)
                .addContainerGap(168, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Accounts", jPanel5);

        jLabel1.setFont(new java.awt.Font("Arabic Typesetting", 1, 24)); // NOI18N
        jLabel1.setText("SMS settings");

        combo_box_smsPrefix.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {  }));

        add_text_button.setText("Add");
        add_text_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_text_buttonActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 0, 0));
        jLabel7.setText("Select qt no./ bill no./ guest chrg/recept no.");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jLabel8.setText("token from below drop down");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combo_box_smsPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(add_text_button)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(325, 325, 325)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel7)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(combo_box_smsPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(add_text_button)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void save_qt_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_qt_btnActionPerformed
        String message = text_area_qt.getText().trim();
        String isActive = null;
        if(qt_radio_button_no.isSelected())
        {
            isActive = "0";
        }
        else
        {
            isActive = "1";
            if(message.isEmpty())
            {
               JOptionPane.showMessageDialog(SMSGeneralSettings.this,EMTPTY_ERROR_MESSAGE, "Error" , JOptionPane.ERROR_MESSAGE);
                return; 
            }
        }
       
        setSMSFlag(SMSgeneralDBSettings.SMS_QT_ID , SMSgeneralDBSettings.SMS_QT_NAME , isActive ,message, qt_selected_facility);
        
    }//GEN-LAST:event_save_qt_btnActionPerformed

    private void save_bill_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_bill_btn1ActionPerformed
        String message = text_area_bill.getText().trim();
        String isActive = null;
        if(bill_radio_button_no.isSelected())
        {
            isActive = "0";
        }
        else
        {
            isActive = "1";
            if(message.isEmpty())
            {
                JOptionPane.showMessageDialog(SMSGeneralSettings.this,EMTPTY_ERROR_MESSAGE, "Error" , JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        setSMSFlag(SMSgeneralDBSettings.SMS_BILL_ID , SMSgeneralDBSettings.SMS_BILL_NAME , isActive, message, bill_selected_facility); 

    }//GEN-LAST:event_save_bill_btn1ActionPerformed

    private void save_st_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_st_btnActionPerformed
        String message = text_area_st.getText().trim();
        String isActive = null;
        if(st_radio_button_no.isSelected())
        {
            isActive = "0";
        }
        else
        {
            isActive = "1";
            if(message.isEmpty())
            {
                JOptionPane.showMessageDialog(SMSGeneralSettings.this,EMTPTY_ERROR_MESSAGE, "Error" , JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        setSMSFlag(SMSgeneralDBSettings.SMS_SHARED_TKT_ID , SMSgeneralDBSettings.SMS_SHARED_TKT_NAME , isActive, message, st_selected_facility);
    }//GEN-LAST:event_save_st_btnActionPerformed

    private void save_guest_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_guest_btnActionPerformed
        String message = text_area_guest.getText().trim();
        String isActive = null;
        if(guest_radio_button_no.isSelected())
        {
            isActive = "0";
        }
        else
        {
            isActive = "1";
            if(message.isEmpty())
            {
                JOptionPane.showMessageDialog(SMSGeneralSettings.this,EMTPTY_ERROR_MESSAGE, "Error" , JOptionPane.ERROR_MESSAGE);    
                return;
            }
                 
            
        }
        setSMSFlag(SMSgeneralDBSettings.SMS_GUEST_ID , SMSgeneralDBSettings.SMS_GUEST_NAME , isActive, message, null);
    }//GEN-LAST:event_save_guest_btnActionPerformed

    private void save_act_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_act_btnActionPerformed
        String message = text_area_account.getText().trim();
        String isActive = null;
        if(account_radio_button_no.isSelected())
        {
            isActive = "0";
        }
        else
        {
            isActive = "1";
            if(message.isEmpty())
            {
                JOptionPane.showMessageDialog(SMSGeneralSettings.this,EMTPTY_ERROR_MESSAGE, "Error" , JOptionPane.ERROR_MESSAGE);  
                return;
            }
                 
        }
        setSMSFlag(SMSgeneralDBSettings.SMS_ACCOUNT_ID , SMSgeneralDBSettings.SMS_ACCOUNT_NAME , isActive, message, null);

    }//GEN-LAST:event_save_act_btnActionPerformed

    
    private void qt_radio_button_noItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_qt_radio_button_noItemStateChanged
        if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            qt_panel.setVisible(false);
        }
        else
        {
            qt_panel.setVisible(true);
        }
    }//GEN-LAST:event_qt_radio_button_noItemStateChanged

    private void add_text_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_text_buttonActionPerformed
        
        
        switch(jTabbedPane1.getSelectedIndex())
        {
            case 0:
            {   
                // QT table
                String myText = predefine_list.get(combo_box_smsPrefix.getSelectedItem().toString());
                String text = text_area_qt.getText() +SPACE_TEXT+ myText +SPACE_TEXT;
                text_area_qt.setText(text);
                //addHighlighterTotext(text_area_qt, text, myText);
                text_area_qt.requestFocus();
                break;
            }  
            case 1:
            {  // Bill Table
                String myText = predefine_list.get(combo_box_smsPrefix.getSelectedItem().toString());
                String text = text_area_bill.getText() +SPACE_TEXT+ myText +SPACE_TEXT;
                text_area_bill.setText(text);
                text_area_bill.requestFocus();
                //addHighlighterTotext(text_area_bill, text, myText);
                break;
            }
            case 2:
            {  //  Shared Ticket
                String myText = predefine_list.get(combo_box_smsPrefix.getSelectedItem().toString());
                String text = text_area_st.getText() +SPACE_TEXT+ myText +SPACE_TEXT;
                text_area_st.setText(text);
                text_area_st.requestFocus();
                //addHighlighterTotext(text_area_st, text, myText);
                break;
            }
            case 3:
            {   //  GUEST charges
                String myText = predefine_list.get(combo_box_smsPrefix.getSelectedItem().toString());
                String text = text_area_guest.getText() +SPACE_TEXT+ myText +SPACE_TEXT;
                text_area_guest.setText(text);
                text_area_guest.requestFocus();
                //addHighlighterTotext(text_area_guest, text, myText);
                break;
            }
            case 4:
            {   // ACCOUNTS
                String myText = predefine_list.get(combo_box_smsPrefix.getSelectedItem().toString());
                String text = text_area_account.getText() +SPACE_TEXT+ myText +SPACE_TEXT;
                text_area_account.setText(text);
                text_area_account.requestFocus();
                //addHighlighterTotext(text_area_account, text, myText);
                break;
            }
                
            default:{
                break;
            }
        }
    }//GEN-LAST:event_add_text_buttonActionPerformed

    
    public void addHighlighterTotext(JTextArea jTextArea, String text , String myText )
    {
        try 
        {
            Highlighter highlighter = jTextArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.PINK);
            int p0 = text.indexOf(myText);
            int p1 = p0 + myText.length();
            highlighter.addHighlight(p0, p1, painter );
        } 
        catch (BadLocationException ex)
        {
            Logger.getLogger(SMSGeneralSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void bill_radio_button_noItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_bill_radio_button_noItemStateChanged
        if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            bill_panel.setVisible(false);
        }
        else
        {
            bill_panel.setVisible(true);
        }
    }//GEN-LAST:event_bill_radio_button_noItemStateChanged

    private void st_radio_button_noItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_st_radio_button_noItemStateChanged
        if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            shared_tkt_panel.setVisible(false);
        }
        else
        {
            shared_tkt_panel.setVisible(true);
        }
    }//GEN-LAST:event_st_radio_button_noItemStateChanged

    private void guest_radio_button_noItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_guest_radio_button_noItemStateChanged
       if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            guest_chrg_panel.setVisible(false);
        }
        else
        {
            guest_chrg_panel.setVisible(true);
        }
    }//GEN-LAST:event_guest_radio_button_noItemStateChanged

    private void account_radio_button_noItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_account_radio_button_noItemStateChanged
       if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            account_panel.setVisible(false);
        }
        else
        {
            account_panel.setVisible(true);
        }
    }//GEN-LAST:event_account_radio_button_noItemStateChanged

    private void clear_button_qtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_button_qtActionPerformed
        text_area_qt.setText("");
    }//GEN-LAST:event_clear_button_qtActionPerformed

    private void clear_button_billActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_button_billActionPerformed
         text_area_bill.setText("");
    }//GEN-LAST:event_clear_button_billActionPerformed

    private void clear_button_sharedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_button_sharedActionPerformed
         text_area_st.setText("");
    }//GEN-LAST:event_clear_button_sharedActionPerformed

    private void clear_button_guestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_button_guestActionPerformed
        text_area_guest.setText("");
    }//GEN-LAST:event_clear_button_guestActionPerformed

    private void clear_button_accountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_button_accountActionPerformed
         text_area_account.setText("");
    }//GEN-LAST:event_clear_button_accountActionPerformed

    private void qt_fac_add_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qt_fac_add_btnActionPerformed
        String facility = facilityDialogBox();
        if(facility != null )
        {
            if(!qt_fac_list_model.contains(facility))
            {
                qt_fac_list_model.addElement(facility);
                qt_selected_facility.add(facility);
                qt_facility_jList.setModel(qt_fac_list_model);
            }
            else
            {
                JOptionPane.showMessageDialog(SMSGeneralSettings.this,"Facility Already Selected", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }//GEN-LAST:event_qt_fac_add_btnActionPerformed

    private void bill_fac_add_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_fac_add_btnActionPerformed
        String facility = facilityDialogBox();
        if(facility != null )
        {
            if(!bill_fac_list_model.contains(facility))
            {
                bill_fac_list_model.addElement(facility);
                bill_selected_facility.add(facility);
                bill_facility_jList.setModel(bill_fac_list_model);
            }
            else
            {
                JOptionPane.showMessageDialog(SMSGeneralSettings.this,"Facility Already Selected", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
    }//GEN-LAST:event_bill_fac_add_btnActionPerformed

    private void st_fac_add_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_st_fac_add_btnActionPerformed
        String facility = facilityDialogBox();
        if(facility != null)
        {
            if(!st_fac_list_model.contains(facility))
            {
               st_fac_list_model.addElement(facility);
               st_selected_facility.add(facility);
               st_facility_jList.setModel(st_fac_list_model);
            }
            else
            {
                JOptionPane.showMessageDialog(SMSGeneralSettings.this,"Facility Already Selected", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }//GEN-LAST:event_st_fac_add_btnActionPerformed

    private void qt_fac_remove_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qt_fac_remove_btnActionPerformed
        if(!qt_facility_jList.isSelectionEmpty())
        {
            String facility = qt_facility_jList.getSelectedValue();
            if(qt_selected_facility.contains(facility))
            {
                qt_selected_facility.remove(facility);
                qt_fac_list_model.removeElement(facility);
                qt_facility_jList.setModel(qt_fac_list_model);
            }
        }
        else
        {
             JOptionPane.showMessageDialog(SMSGeneralSettings.this,ERROR_EMPTY_FACILITY, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_qt_fac_remove_btnActionPerformed

    private void bill_fac_remove_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_fac_remove_btnActionPerformed
        if(!bill_facility_jList.isSelectionEmpty())
        {
            String facility = bill_facility_jList.getSelectedValue();
            if(bill_selected_facility.contains(facility))
            {
                bill_selected_facility.remove(facility);
                bill_fac_list_model.removeElement(facility);
                bill_facility_jList.setModel(bill_fac_list_model);
            }
        }
        else
        {
             JOptionPane.showMessageDialog(SMSGeneralSettings.this,ERROR_EMPTY_FACILITY, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_bill_fac_remove_btnActionPerformed

    private void st_fac_remove_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_st_fac_remove_btnActionPerformed
        if(!st_facility_jList.isSelectionEmpty())
        {
            String facility = st_facility_jList.getSelectedValue();
            if(st_selected_facility.contains(facility))
            {
                st_selected_facility.remove(facility);
                st_fac_list_model.removeElement(facility);
                st_facility_jList.setModel(st_fac_list_model);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(SMSGeneralSettings.this,ERROR_EMPTY_FACILITY, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_st_fac_remove_btnActionPerformed
        
   
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel account_panel;
    private javax.swing.JRadioButton account_radio_button_no;
    private javax.swing.JRadioButton account_radio_button_yes;
    private javax.swing.JButton add_text_button;
    private javax.swing.JButton bill_fac_add_btn;
    private javax.swing.JButton bill_fac_remove_btn;
    private javax.swing.JList<String> bill_facility_jList;
    private javax.swing.JPanel bill_panel;
    private javax.swing.JRadioButton bill_radio_button_no;
    private javax.swing.JRadioButton bill_radio_button_yes;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clear_button_account;
    private javax.swing.JButton clear_button_bill;
    private javax.swing.JButton clear_button_guest;
    private javax.swing.JButton clear_button_qt;
    private javax.swing.JButton clear_button_shared;
    private javax.swing.JComboBox<String> combo_box_smsPrefix;
    private javax.swing.JPanel guest_chrg_panel;
    private javax.swing.JRadioButton guest_radio_button_no;
    private javax.swing.JRadioButton guest_radio_button_yes;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton qt_fac_add_btn;
    private javax.swing.JButton qt_fac_remove_btn;
    private javax.swing.JList<String> qt_facility_jList;
    private javax.swing.JPanel qt_panel;
    private javax.swing.JRadioButton qt_radio_button_no;
    private javax.swing.JRadioButton qt_radio_button_yes;
    private javax.swing.JButton save_act_btn;
    private javax.swing.JButton save_bill_btn1;
    private javax.swing.JButton save_guest_btn;
    private javax.swing.JButton save_qt_btn;
    private javax.swing.JButton save_st_btn;
    private javax.swing.JPanel shared_tkt_panel;
    private javax.swing.JButton st_fac_add_btn;
    private javax.swing.JButton st_fac_remove_btn;
    private javax.swing.JList<String> st_facility_jList;
    private javax.swing.JRadioButton st_radio_button_no;
    private javax.swing.JRadioButton st_radio_button_yes;
    private javax.swing.JTextArea text_area_account;
    private javax.swing.JTextArea text_area_bill;
    private javax.swing.JTextArea text_area_guest;
    private javax.swing.JTextArea text_area_qt;
    private javax.swing.JTextArea text_area_st;
    // End of variables declaration//GEN-END:variables
}
