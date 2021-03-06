/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.Library;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadInteger;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.Library.Lib_IssueRulesTableMaster.lib_Issueline;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Santhosh
 */
public class IssueRulesMaster extends javax.swing.JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private ComboBoxValModel memModel;
    private ComboBoxValModel periodModel;
    private String member;
    private String period;
    private Lib_IssueRulesTableMaster imodel;
    private String c_id;
    private String deact_id;

    /** Creates new form IssueRulesMaster */
    public IssueRulesMaster() {
        initComponents();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        appmem_com = new javax.swing.JComboBox();
        period_com = new javax.swing.JComboBox();
        name_tx = new javax.swing.JTextField();
        memb = new javax.swing.JRadioButton();
        mem_sp = new javax.swing.JRadioButton();
        mem_sp_dt = new javax.swing.JRadioButton();
        save = new javax.swing.JButton();
        savechange = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
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
        edit = new javax.swing.JButton();
        deact = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jLabel1.setText("Name");

        jLabel2.setText("AppMemberType");

        jLabel3.setText("Periodicity");

        appmem_com.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        period_com.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        buttonGroup1.add(memb);
        memb.setText("Member");

        buttonGroup1.add(mem_sp);
        mem_sp.setText("Member+Spouse");

        buttonGroup1.add(mem_sp_dt);
        mem_sp_dt.setText("Member+Spouse+Dependent");

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        savechange.setText("SaveChanges");
        savechange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savechangeActionPerformed(evt);
            }
        });

        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Rules Applicable For :");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(jLabel3)
                            .add(jLabel1)
                            .add(jLabel4))
                        .add(18, 18, 18)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(memb)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(name_tx)
                                .add(appmem_com, 0, 256, Short.MAX_VALUE)
                                .add(period_com, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(mem_sp)
                            .add(mem_sp_dt))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(0, 349, Short.MAX_VALUE)
                        .add(save, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 81, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(savechange)
                        .add(70, 70, 70))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(39, 39, 39)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(name_tx, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(appmem_com, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(period_com, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(memb)
                    .add(jLabel4))
                .add(18, 18, 18)
                .add(mem_sp)
                .add(18, 18, 18)
                .add(mem_sp_dt)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 209, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(savechange)
                    .add(jButton1)
                    .add(save))
                .addContainerGap())
        );

        jTabbedPane1.addTab("CreateNew", jPanel1);

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
        jScrollPane6.setViewportView(jTable1);

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        deact.setText("Deactivate");
        deact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deactActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Show All");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(455, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jCheckBox1)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(edit)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deact)))
                .add(78, 78, 78))
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {deact, edit}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBox1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 455, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(edit)
                    .add(deact))
                .addContainerGap())
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout.createSequentialGroup()
                    .add(52, 52, 52)
                    .add(jScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 420, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(60, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("ViewList", jPanel2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 721, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 569, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        
         try{
        if( name_tx.getText().length()>0 ){
           if( appmem_com.getSelectedIndex()!=-1 && period_com.getSelectedIndex()!=-1){
               
               int count=Integer.valueOf(new StaticSentence(m_App.getSession()
                    , "SELECT COUNT(*) FROM lib_issuerules WHERE NAME=? AND ACTIVE=TRUE"
                    ,SerializerWriteString.INSTANCE
                    ,SerializerReadInteger.INSTANCE).find(name_tx.getText()).toString()); 
               if(count==0){
                 if(memb.isSelected())  {
                    insertlib_issuerules(1);
                 }
                 else if(mem_sp.isSelected())  {
                    insertlib_issuerules(2);
                 }else{
                     insertlib_issuerules(3);
                 }
                   
               }else{
                JOptionPane.showMessageDialog(this, " the name "+name_tx.getText()+" already exist", null, JOptionPane.OK_OPTION);
            }
               
           }else{
            JOptionPane.showMessageDialog(this, "Please ensure that MemberType and Periodicity is not empty", null, JOptionPane.OK_OPTION);
        }
            
        }else{
            JOptionPane.showMessageDialog(this, "Please ensure that Name is not empty", null, JOptionPane.OK_OPTION);
        }
         }catch(Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_saveActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        
        if(jTable1.getSelectedRow()!=-1){
            int pub = JOptionPane.showConfirmDialog(jPanel1, " Do you want to Edit Data ?? " , "Editing Menu" , JOptionPane.YES_NO_OPTION);
            if(pub == JOptionPane.YES_OPTION){
                if(jTable1.getSelectedRow()<imodel.getSize()){
                    int row = jTable1.getSelectedRow();
                    if (Boolean.valueOf(jTable1.getModel().getValueAt(row, 9).toString())) {
                   lib_Issueline data = imodel.getList().get(row);
                    savechange.setVisible(true);
                    save.setVisible(false);
                    c_id=data.getId();
                    String name1=data.getName();
                    String apmem=data.getMemtyp();
                    String prdcty=data.getPeriodicity();
                    int memtyp=data.getMember();
                    
                    name_tx.setText(name1);
                    if(name1!=null && !name1.equals("") ){
                        
                        for(int i=0; i<appmem_com.getItemCount(); i++)
                            {
                              if(appmem_com.getItemAt(i).toString().equals(apmem))
                                {
                                    appmem_com.setSelectedIndex(i);
                                    break;
                                }
                            
                            }
                      for(int i=0; i<period_com.getItemCount(); i++)
                            {
                              if(period_com.getItemAt(i).toString().equals(prdcty))
                                {
                                    period_com.setSelectedIndex(i);
                                    break;
                                }
                            
                            }
                      if(memtyp==1){
                          memb.setSelected(true);
                          mem_sp.setSelected(false);
                          mem_sp_dt.setSelected(false);
                      }else if(memtyp==2){
                          memb.setSelected(false);
                          mem_sp.setSelected(true);
                          mem_sp_dt.setSelected(false);
                      } else{
                          memb.setSelected(false);
                          mem_sp.setSelected(false);
                          mem_sp_dt.setSelected(true);
                      }   
                        
                    }
                    jTabbedPane1.setSelectedIndex(0);
                }else {
                    JOptionPane.showMessageDialog(this, "selected issue is deactivated.cannot edit it");
                }
                }      
            }
        }
        
    }//GEN-LAST:event_editActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        
        javax.swing.JTabbedPane tabpane=(javax.swing.JTabbedPane)evt.getSource();
        int tabno=tabpane.getSelectedIndex();
        jCheckBox1.setSelected(false);
        if(tabno==1){
            try {
                imodel = Lib_IssueRulesTableMaster.loadInstance(m_App,1);

                jTable1.setModel(imodel.getTableModel());
                jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
               
                TableColumnModel cmodel=jTable1.getColumnModel();
                cmodel.getColumn(0).setPreferredWidth(200);
                cmodel.getColumn(1).setPreferredWidth(100);
                cmodel.getColumn(2).setPreferredWidth(100);
                cmodel.getColumn(3).setPreferredWidth(100);
                cmodel.getColumn(4).setPreferredWidth(100);
                
                
            } catch (BasicException ex) {
                ex.printStackTrace();
            }
            
          }
        
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        
        try {
            if (jCheckBox1.isSelected() == true) {
                imodel = Lib_IssueRulesTableMaster.loadInstance(m_App, 2);
                jTable1.setModel(imodel.getTableModel());
            } else if (jCheckBox1.isSelected() == false) {
                imodel = Lib_IssueRulesTableMaster.loadInstance(m_App, 1);
                jTable1.setModel(imodel.getTableModel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void savechangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savechangeActionPerformed
       
        try{
        if( name_tx.getText().length()>0 ){
            if( appmem_com.getSelectedIndex()!=-1 && period_com.getSelectedIndex()!=-1){
                if(memb.isSelected())  {
                    updatelib_issuerules(1);
                 }
                 else if(mem_sp.isSelected())  {
                    updatelib_issuerules(2);
                 }else{
                     updatelib_issuerules(3);
                 }
            }else{
            JOptionPane.showMessageDialog(this, "Please ensure that MemberType and Periodicity is not empty", null, JOptionPane.OK_OPTION);
        }
          
        }else{
            JOptionPane.showMessageDialog(this, "Please ensure that Name is not empty", null, JOptionPane.OK_OPTION);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_savechangeActionPerformed

    private void deactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deactActionPerformed
       
         if(jTable1.getSelectedRow()!=-1){
            int bill = JOptionPane.showConfirmDialog(jPanel1, " Do you want to deactivate ?? " , "Deactivation" , JOptionPane.YES_NO_OPTION);
             if(bill == JOptionPane.YES_OPTION){
                  if(jTable1.getSelectedRow() < imodel.getSize()){
                     int row = jTable1.getSelectedRow(); 
                     if (Boolean.valueOf(jTable1.getModel().getValueAt(row, 9).toString())) {
                     lib_Issueline showdata = imodel.getList().get(row);
                     deact_id=showdata.getId();
                     deactivate_issue();
                     }else {
                    JOptionPane.showMessageDialog(this, "selected Issue is already deactivated.");
                }
                  }
             }
         } 
        
    }//GEN-LAST:event_deactActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        reset(); 
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox appmem_com;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton deact;
    private javax.swing.JButton edit;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JRadioButton mem_sp;
    private javax.swing.JRadioButton mem_sp_dt;
    private javax.swing.JRadioButton memb;
    private javax.swing.JTextField name_tx;
    private javax.swing.JComboBox period_com;
    private javax.swing.JButton save;
    private javax.swing.JButton savechange;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getTitle() {
         return "Library IssueRules Management";
    }

    @Override
    public void activate() throws BasicException {
       memModel=new ComboBoxValModel(getmemname());
       appmem_com.setModel(memModel);
       periodModel=new ComboBoxValModel(getperiodicity());
       period_com.setModel(periodModel);
       reset(); 
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
    public void init(AppView app) throws BeanFactoryException {
         m_App=app;
    }

    @Override
    public Object getBean() {
         return this;
    }
    public void reset(){
         
         name_tx.setText(null);
         save.setVisible(true);
         savechange.setVisible(false);
         appmem_com.setSelectedIndex(-1);
         period_com.setSelectedIndex(-1);
         memb.setSelected(true);
         mem_sp.setSelected(false);
         mem_sp_dt.setSelected(false);
         jTabbedPane1.setSelectedIndex(0);
         jCheckBox1.setSelected(false);
     }
    
    public List getmemname() throws BasicException{
          List<Object> mem_list = new ArrayList<Object>();
           mem_list  = (List<Object>) new StaticSentence(m_App.getSession(), "SELECT m.name FROM memtype m WHERE m.active=1 ORDER by m.name",  SerializerWriteString.INSTANCE , SerializerReadString.INSTANCE).list();
          
          return mem_list;
      }
    
    public List getperiodicity() throws BasicException{
          List<Object> prd_list = new ArrayList<Object>();
           prd_list  = (List<Object>) new StaticSentence(m_App.getSession(), "SELECT p.name FROM periodicity p WHERE p.active=1 ORDER by p.name",  SerializerWriteString.INSTANCE , SerializerReadString.INSTANCE).list();
          
          return prd_list;
      }
    
    public  String getmemId(){
           
           List<Object> mem_list1 = new ArrayList<Object>();
        try {
            if(appmem_com.getSelectedIndex()!=-1){
            mem_list1  = (List<Object>) new StaticSentence(m_App.getSession(), "SELECT a.id FROM memtype a  WHERE a.name=? and a.ACTIVE=1",  SerializerWriteString.INSTANCE , SerializerReadString.INSTANCE).list(appmem_com.getSelectedItem());
        member =(String)mem_list1.get(0);
            }else{
                member=" ";
            }
        
        } catch (BasicException ex) {
            Logger.getLogger(IssueRulesMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
         
           return member;
      }
    
    public  String getperdId(){
           
           List<Object> ped_list1 = new ArrayList<Object>();
        try {
            if(period_com.getSelectedIndex()!=-1){
            ped_list1  = (List<Object>) new StaticSentence(m_App.getSession(), "SELECT a.id FROM periodicity a  WHERE a.name=? and a.ACTIVE=1",  SerializerWriteString.INSTANCE , SerializerReadString.INSTANCE).list(period_com.getSelectedItem());
        period =(String)ped_list1.get(0);
            }else{
                period=" ";
            }
        
        } catch (BasicException ex) {
            Logger.getLogger(IssueRulesMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
         
           return period;
      }
    private void insertlib_issuerules(int memb) {
        
         try{
          if(name_tx.getText().length()>0){
              
             Object[] param=new Object[]{UUID.randomUUID().toString(),name_tx.getText(),getmemId(),getperdId(),memb,true,m_App.getAppUserView().getUser().getName(),new Date(),m_App.getProperties().getHost()};
             new PreparedSentence(m_App.getSession()
                , "INSERT INTO lib_issuerules (ID,NAME,AppMemType,Periodicity,Memberint,ACTIVE,CREATEDBY,CREATEDATE,CREATEDHOST) VALUES (?,?,?,?,?,?,?,?,?)"
                , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.INT,Datas.BOOLEAN,Datas.STRING,Datas.TIMESTAMP,Datas.STRING})).exec(param);
          JOptionPane.showMessageDialog(this, "Created Successfully..!", "Success", JOptionPane.INFORMATION_MESSAGE);
           }

          
          reset();  
              
          
         }catch(NullPointerException e){
          
        e.printStackTrace();
      }
      catch(Exception e){
       e.printStackTrace();
      }
    }
    
    private void updatelib_issuerules(int member) {
        
        try{
          if(name_tx.getText().length()>0){
              
             Object[] param=new Object[]{name_tx.getText(),getmemId(),getperdId(),member,true,m_App.getAppUserView().getUser().getName(),new Date(),m_App.getProperties().getHost(),c_id};
             new PreparedSentence(m_App.getSession()
             , "UPDATE lib_issuerules SET NAME=?,AppMemType=?,Periodicity=?,Memberint=?,ACTIVE=?,CREATEDBY=?,CREATEDATE=?,CREATEDHOST=? WHERE ID=?"
             , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.INT,Datas.BOOLEAN,Datas.STRING,Datas.TIMESTAMP,Datas.STRING,Datas.STRING})).exec(param);
          JOptionPane.showMessageDialog(this, "Updated Successfully..!", "Success", JOptionPane.INFORMATION_MESSAGE);
           }

            imodel = Lib_IssueRulesTableMaster.loadInstance(m_App,1);
            jTable1.setModel(imodel.getTableModel());
          reset();  
              
          
         }catch(NullPointerException e){
          
        e.printStackTrace();
      }
      catch(Exception e){
       e.printStackTrace();
      }
    }
    
     private void deactivate_issue() {
           try{
           String newbookId = UUID.randomUUID().toString();
           
             new PreparedSentence(m_App.getSession(), "UPDATE lib_issuerules  SET ID=?, ACTIVE=0  , DEACTBY=? , DEACTDATE=? , DEACTHOST=?,DEACTREFERENCE=? WHERE ID = ? AND DEACTBY IS NULL AND DEACTDATE IS NULL"
                                                                           , new SerializerWriteBasic(new Datas[]{ Datas.STRING ,Datas.STRING , Datas.TIMESTAMP , Datas.STRING ,Datas.STRING , Datas.STRING })).exec
                                                                            (new Object[]{ newbookId, m_App.getAppUserView().getUser().getName() ,new Date(), m_App.getProperties().getHost(),deact_id , deact_id  });
       imodel  = Lib_IssueRulesTableMaster.loadInstance(m_App,1);
       jTable1.setModel(imodel.getTableModel()); 
       
       reset();
       JOptionPane.showMessageDialog(this, "De-Activated Successfully..!", "Success", JOptionPane.INFORMATION_MESSAGE);
       }catch(NullPointerException e){
          
        e.printStackTrace();
      }
      catch(Exception e){
       e.printStackTrace();
      }
    }
}
