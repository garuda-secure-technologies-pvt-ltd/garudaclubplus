
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DueListPanel.java
 *
 * Created on Sep 14, 2009, 10:34:06 AM
 */

/**
 *
 * @author swathi
 */
package com.openbravo.pos.Accounts;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
//import com.openbravo.format.Formats;
//import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
//import com.openbravo.pos.Accounts.DueListTableModel.FacilityLine;
import com.openbravo.pos.clubmang.DataLogicFacilities;
//import com.openbravo.pos.clubmang.DataSourceProvider;
import com.openbravo.pos.clubmang.Facility;
//import com.openbravo.pos.clubmang.FacilityDetail;
//import com.openbravo.pos.clubmang.FacilityInfo;
//import com.openbravo.pos.clubmang.JasperReportNew;
import com.openbravo.pos.clubmang.GeneralSettingInfo;
import com.openbravo.pos.clubmang.MemTypeTableModel.MemTypeline;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.JPanelView;
//import java.awt.Color;
//import java.awt.Component;
import com.openbravo.pos.forms.LookupUtilityImpl;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
//import javax.swing.SpinnerNumberModel;
//import javax.swing.table.TableCellRenderer;

public class DueListPanel extends javax.swing.JPanel implements JPanelView,BeanFactoryApp{
    private ComboBoxValModel fModel;
    private ComboBoxValModel memmodel;
    private DataLogicFacilities dlfac;
    private AppView m_App;
    private DueListTableModel dmodel;
    private ListModel flistmodel;
    private List<Facility> rflist;

    public DueListPanel() {
        initComponents();
    }
   

    public void init(AppView app) {
       m_App=app;
       dlfac=(DataLogicFacilities) m_App.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
       jButton4.setText("Date");
       jButton2.setText("Execute ");//"reportheader" "Secretary" "Treasurer"
       GeneralSettingInfo ginfo=LookupUtilityImpl.getInstance(null).getGeneralSettingsMap().get("reportheader");
       if(ginfo==null)
           jTextArea1.setText("");
       else
           jTextArea1.setText(ginfo.getValue());
       ginfo=LookupUtilityImpl.getInstance(null).getGeneralSettingsMap().get("Secretary");
       if(ginfo==null)
           jTextField3.setText("");
       else
           jTextField3.setText(ginfo.getValue());
       ginfo=LookupUtilityImpl.getInstance(null).getGeneralSettingsMap().get("Treasurer");
       if(ginfo==null)
           jTextField4.setText("");
       else
           jTextField4.setText(ginfo.getValue());
    }

    public void activate() throws BasicException {
       rflist=new ArrayList<Facility>();
       List<Facility> flist=dlfac.getFacility();
       Facility f=new Facility();
       f.setID("others");
       f.setName("others");
       flist.add(0,f);
       fModel=new ComboBoxValModel(flist);
       jComboBox1.setModel(fModel);
       List<MemTypeline> mlist=dlfac.getMemType();
       memmodel=new ComboBoxValModel(mlist);
       jComboBox2.setModel(memmodel);
       jComboBox2.setSelectedIndex(-1);
       dmodel=DueListTableModel.loadEmptyInstance();
       flistmodel=new ListModel(new ArrayList());
       jList1.setModel(flistmodel);
       jCheckBox1.setSelected(false);
       jTextField1.setVisible(false);
       jLabel5.setVisible(false);
    }

    public String getTitle() {
        return null;
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setLayout(null);

        jPanel4.setName("jPanel4"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Facility"));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(null);

        jLabel1.setText("Facility");
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel2.add(jLabel1);
        jLabel1.setBounds(10, 20, 90, 14);

        jComboBox1.setName("jComboBox1"); // NOI18N
        jPanel2.add(jComboBox1);
        jComboBox1.setBounds(140, 20, 210, 20);

        jLabel6.setText("Facility List");
        jLabel6.setName("jLabel6"); // NOI18N
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 50, 90, 14);

        jButton1.setText("Add");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton1.setBounds(370, 20, 90, 23);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jList1.setName("jList1"); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(140, 50, 210, 70);

        jButton3.setText("Remove");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);
        jButton3.setBounds(370, 50, 90, 23);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Other"));
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel2.setText("Member Type");
        jLabel2.setName("jLabel2"); // NOI18N

        jComboBox2.setName("jComboBox2"); // NOI18N

        jLabel8.setText("Date");
        jLabel8.setName("jLabel8"); // NOI18N

        jTextField2.setName("jTextField2"); // NOI18N

        jButton4.setText("jButton4");
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel3.setName("jPanel3"); // NOI18N

        jLabel4.setText("30 days");
        jLabel4.setName("jLabel4"); // NOI18N

        jCheckBox1.setName("jCheckBox1"); // NOI18N
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        jTextField1.setName("jTextField1"); // NOI18N

        jLabel5.setText("days");
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel3.setText("OverDue");
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, 212, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(198, 198, 198))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(224, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel4);
        jPanel4.setBounds(0, 0, 680, 270);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Report Settings"));
        jPanel5.setName("jPanel5"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane3.setViewportView(jTextArea1);

        jLabel11.setText("Due Report Header  :");
        jLabel11.setName("jLabel11"); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Report Footer"));
        jPanel6.setName("jPanel6"); // NOI18N

        jLabel7.setText("Hon. Secretary");
        jLabel7.setName("jLabel7"); // NOI18N

        jTextField3.setName("jTextField3"); // NOI18N

        jLabel9.setText("Hon. treasurer");
        jLabel9.setName("jLabel9"); // NOI18N

        jTextField4.setName("jTextField4"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                .addContainerGap(276, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(26, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );

        add(jPanel5);
        jPanel5.setBounds(0, 280, 670, 280);

        jButton2.setText("Generate Report");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        jButton2.setBounds(480, 620, 185, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      if(jComboBox1.getSelectedIndex()!=-1){
          Facility f=(Facility)jComboBox1.getSelectedItem();
          if(!flistmodel.getList().contains(f)){
             List list=flistmodel.getList();
             list.add(f);
             flistmodel=new ListModel(list);
             jList1.setModel(flistmodel);
             jList1.validate();
             jComboBox1.setSelectedIndex(-1);
          }else
              JOptionPane.showMessageDialog(this, "Facility is already added to the list",null,JOptionPane.WARNING_MESSAGE);
      }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            if(jTextField2.getText().length()>0){
            List<Facility> flist = new ArrayList<Facility>();
            Facility f=dlfac.getFacilitybyID("1");
            Facility f2=dlfac.getFacilitybyID("2");
            ListModel mlist = (ListModel) jList1.getModel();
            flist.addAll(mlist.getList());
            for(int i=0;i<flist.size();i++){
                Facility f1=flist.get(i);
                if(f1.getid().equals("1")){
                    flist.remove(i);
                    flist.add(0,f);
                }else if(f1.getid().equals("2")){
                    flist.remove(i);
                    flist.add(0,f2);
                }
            }
            rflist.addAll(flist);
            String memtypeid=null;
            if(jComboBox2.getSelectedIndex() != -1)
            memtypeid= ((MemTypeline) jComboBox2.getSelectedItem()).getid();
            int overdue = 30;
            if (jCheckBox1.isSelected() && jTextField1.getText().length() > 0) {
                overdue = Integer.parseInt(jTextField1.getText());
            }
            Date d  =(Date) Formats.TIMESTAMP.parseValue(jTextField2.getText());
            Date d1 =(Date) Formats.TIMESTAMP.parseValue(jTextField2.getText());
            Calendar cal=Calendar.getInstance();
            cal.setTime(d);
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 00);
            cal.set(Calendar.MILLISECOND, 00);
            Date duedate=new Date(d.getTime());
            Calendar cald=Calendar.getInstance();
            cald.setTimeInMillis(duedate.getTime());
            int i=cald.get(Calendar.DATE);
            //int max=cald.getActualMaximum(Calendar.DATE);
            if(i>15)
                cald.set(Calendar.DATE, 15);
            else{
                cald.add(Calendar.MONTH, -1);
                cald.set(Calendar.DATE, cald.getActualMaximum(Calendar.DATE));
            }
            duedate.setTime(cald.getTimeInMillis());
            if(cal.get(Calendar.DATE)<=15){
                cal.set(Calendar.DATE, 1);
                d.setTime(cal.getTimeInMillis());
                d1.setTime(cal.getTimeInMillis());
            }else{
                cal.set(Calendar.DATE, 16);
                d.setTime(cal.getTimeInMillis());
                d1.setTime(cal.getTimeInMillis());
            }
            cal.add(Calendar.DATE, -overdue);
            d1.setTime(cal.getTimeInMillis());
            dmodel=DueListTableModel.loadData(m_App,flist,memtypeid,d,d1,dlfac);
            String header=jTextArea1.getText();
            String sec=jTextField3.getText();
            String tre=jTextField4.getText();
            dueListTable duetable=dueListTable.getDialog(this, m_App,duedate,header,sec,tre);
            duetable.showDialog(dmodel, rflist, m_App);
            int cnt=new PreparedSentence(m_App.getSession(), "UPDATE GENERALTABLE SET VALUE=? WHERE NAME=?", new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})).exec(new Object[]{jTextArea1.getText().trim(),"reportheader"});
            if(cnt<=0){
                new PreparedSentence(m_App.getSession(), "INSERT INTO GENERALTABLE (ID,VALUE,NAME) VALUES (?,?,?) ", new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING})).exec(new Object[]{UUID.randomUUID().toString(),jTextArea1.getText().trim(),"reportheader"});
            }
            cnt=new PreparedSentence(m_App.getSession(), "UPDATE GENERALTABLE SET VALUE=? WHERE NAME=?", new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})).exec(new Object[]{jTextField3.getText().trim(),"Secretary"});
            if(cnt<=0){
                new PreparedSentence(m_App.getSession(), "INSERT INTO GENERALTABLE (ID,VALUE,NAME) VALUES (?,?,?) ", new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING})).exec(new Object[]{UUID.randomUUID().toString(),jTextField3.getText().trim(),"Secretary"});
            }
            cnt=new PreparedSentence(m_App.getSession(), "UPDATE GENERALTABLE SET VALUE=? WHERE NAME=?", new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})).exec(new Object[]{jTextField4.getText().trim(),"Treasurer"});
            if(cnt<=0){
                new PreparedSentence(m_App.getSession(), "INSERT INTO GENERALTABLE (ID,VALUE,NAME) VALUES (?,?,?) ", new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING})).exec(new Object[]{UUID.randomUUID().toString(),jTextField4.getText().trim(),"Treasurer"});
            }
            LookupUtilityImpl.getInstance(null).reloadGeneralSettingsMap();
            /*jTable1.setModel(dmodel.getTableModel());
            int columncnt=jTable1.getColumnModel().getColumnCount();
            if(columncnt>0){
              jTable1.getColumnModel().getColumn(0).setMaxWidth(40);
              jTable1.getColumnModel().getColumn(0).setPreferredWidth(40);
              jTable1.getColumnModel().getColumn(1).setMaxWidth(150);
              jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
              for(int k=2;k<columncnt;k++){
               if((k-2)%3==0){
                jTable1.getColumnModel().getColumn(k).setMaxWidth(150);
                jTable1.getColumnModel().getColumn(k).setPreferredWidth(150);
               }else if((k-2)%3==1){
                jTable1.getColumnModel().getColumn(k).setMaxWidth(60);
                jTable1.getColumnModel().getColumn(k).setPreferredWidth(60);
               } else if((k-2)%3==2){
                jTable1.getColumnModel().getColumn(k).setMaxWidth(60);
                jTable1.getColumnModel().getColumn(k).setPreferredWidth(60);
               }
              }
            }*/
           /* Map reportparam=new HashMap();
            DataSourceProvider data1=new DataSourceProvider(dmodel.getReportList1());
            DueListDataSource ds=new DueListDataSource(dmodel.getReportList1());
            data1.setDataSource(ds);
           // JasperReportNew.runReport(m_App,  "./reports/com/openbravo/reports/DueReport.jrxml", reportparam, false, data1, true);
            //C:\Documents and Settings\swathi\Desktop\GarudaNew\reports\com\openbravo\reports
            JasperReportNew.runReport(m_App,  "C:\\Documents and Settings\\swathi\\Desktop\\GarudaNew\\reports\\com\\openbravo\\reports/DueReport.jrxml", reportparam, false, data1, true);
         //JasperPrint jp=JasperReportNew.runReport(m_app, "./reports/com/openbravo/reports/LedgerReport.jrxml", reportparam, false,data1,true);
            */
            //(AppView app,String reportFile,Map reportparams,boolean print,DataSourceProvider data1,boolean display)
            }
        }catch(NumberFormatException e1){
           JOptionPane.showMessageDialog(null, "Error");
        }
        catch (BasicException ex) {
            new MessageInf(ex).show(new JFrame());
           ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        if(jCheckBox1.isSelected()){
            jTextField1.setVisible(true);
            jLabel5.setVisible(true);
        }else{
            jTextField1.setVisible(false);
            jLabel5.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int row=jList1.getSelectedIndex();
        if(row>=0){
           ((ListModel)jList1.getModel()).removeElement(row);
           jList1.setModel(new ListModel(((ListModel)jList1.getModel()).getList()));
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Date date=new Date();
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(jTextField2.getText());
        } catch (BasicException e) {
            date = null;
        }
          try{
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTextField2.setText(Formats.TIMESTAMP.formatValue(date));
        }
          }catch(Exception e1){
              e1.printStackTrace();
          }
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables

}
