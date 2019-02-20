/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UserSpecificSeries.java
 *
 * Created on Mar 1, 2009, 4:38:36 PM
 */

package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.admin.RoleInfo;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPanelView;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author swathi
 */
public class UserSpecificSeries extends javax.swing.JPanel implements JPanelView, BeanFactoryApp  {

    /** Creates new form UserSpecificSeries */
    private DataLogicSales m_dlSales;
     private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private String qt=null;
    private String bill=null;
    private String receipt=null;
    private Double qtmax=0.0;
    private Double billmax=0.0;
    private Double receiptmax=0.0;
    private ComboBoxValModel wmodel;
    private ComboBoxValModel umodel;
    public UserSpecificSeries() {
        initComponents();
    }

      public void init(AppView app) throws BeanFactoryException {

        m_App = app;
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        try{
           // m_dlSales.getRootCategories();
        
        umodel=new ComboBoxValModel();
        wmodel=new ComboBoxValModel();
             List<RoleInfo> roleslist=m_dlSales.getRoleList().list();
           for(int i=0;i<roleslist.size();i++){
               RoleInfo rinfo=roleslist.get(i);
                //warehouse changes -start
                Object[] obj2=(Object[])   new StaticSentence(m_App.getSession()//PRAVEEN:changed the query because id was in ambiguous
                        , "SELECT P.ID,P.NAME,P.APPPASSWORD,P.CARD,P.ROLE,P.IMAGE,P.LOGINTIME,P.CLOSECASHTIME,P.OPENCASHTIME,P.CLOSESALE,P.OPENSALE,P.PRCATEGORIES FROM PEOPLE P,ROLES WHERE P.ROLE=ROLES.ID AND ROLES.NAME=?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] {Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.IMAGE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING})).find(rinfo.getName());

             if(obj2!=null){
                 String warehouse = null;
                if (obj2[11] != null) {
                    String[] wArr = obj2[11].toString().split("#");
                    warehouse = wArr[0];
                }
             AppUser appuser1=new AppUser(obj2[0].toString(), obj2[1].toString(),obj2[4].toString(),warehouse);
              //warehouse changes -end
             appuser1.fillPermissions(m_dlSystem);
             boolean sflag=appuser1.hasPermission("sales");
             boolean pflag=appuser1.hasPermission("payment");
             if(sflag==true || pflag==true){
                 umodel.add(rinfo);
                 wmodel.add(rinfo);
             }
             }
           }
        }
        catch(Exception e){
            e.printStackTrace();
        }
      }
        public Object getBean() {
        return this;
    }

    public JComponent getComponent() {
        return this;
    }

   public String getTitle() {
        return "Series Detail";
    }

    public void activate() throws BasicException {
        loadData();
    }
 private void loadData() throws BasicException {
        
        jComboBox1.setModel(wmodel);
        jComboBox1.setSelectedIndex(-1);
        jComboBox1.setVisible(false);
        jComboBox2.setModel(umodel);
        jComboBox2.setSelectedIndex(-1);
        jLabel1.setVisible(false);
        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
        jLabel4.setVisible(false);
        qtseries.setVisible(false);
        billseries.setVisible(false);
        receiptseries.setVisible(false);
        jButton1.setVisible(false);
        jButton2.setVisible(false);
        jButton3.setVisible(false);
        jButton4.setVisible(false);
        jButton5.setVisible(false);
        jLabel6.setVisible(false);
        jLabel7.setVisible(false);
        jLabel8.setVisible(false);
        lbillno.setVisible(false);
        lqtno.setVisible(false);
        lrno.setVisible(false);
 }
 public boolean deactivate() {
        return true;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        qtseries = new javax.swing.JTextField();
        billseries = new javax.swing.JTextField();
        receiptseries = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lqtno = new javax.swing.JTextField();
        lbillno = new javax.swing.JTextField();
        lrno = new javax.swing.JTextField();

        jLabel1.setText("QT Series                 :");

        jLabel2.setText("Bill Series                  :");

        jLabel3.setText("Receipt Series          :");

        qtseries.setEditable(false);

        billseries.setEditable(false);

        receiptseries.setEditable(false);

        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Reset All");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel4.setText("Against Payment Of :");

        jLabel5.setText("User Role                  :");

        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Series Last No  :");

        jLabel7.setText("Series Last No  :");

        jLabel8.setText("Series Last No  :");

        lqtno.setEditable(false);

        lbillno.setEditable(false);

        lrno.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(receiptseries, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(qtseries, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(billseries, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 120, Short.MAX_VALUE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, 120, Short.MAX_VALUE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jButton2)))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lrno)
                    .addComponent(lbillno)
                    .addComponent(lqtno, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addGap(154, 154, 154))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qtseries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(lqtno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(billseries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(lbillno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(receiptseries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(lrno))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addGap(122, 122, 122))
        );
    }// </editor-fold>//GEN-END:initComponents
    private int temp=0;
    private int sales=0;
    private int payment=0;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                qtseries.requestFocus();
            }
        });
        qtseries.setText(null);
        qtseries.setEditable(true);
        billseries.setEditable(false);
        receiptseries.setEditable(false);
        jButton1.setVisible(false);
        jButton3.setVisible(false);
        jButton4.setVisible(false);
        jButton5.setVisible(false);
        jButton2.setVisible(true);
        temp=1;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String qttemp=qtseries.getText();
        String btemp=billseries.getText();
        String receipttemp=receiptseries.getText();
         int flag=0;
       if(sales==1 && payment==1){
       if(temp==1  ){
            if(qttemp!=null){
            if(qt.equals(qttemp))
                flag=1;
            qt=qtseries.getText();
            qtmax=0.0;
            }
        }

        if(temp==3 ){
            if(btemp!=null){
            if(bill.equals(btemp) )
                flag=1;
            bill=billseries.getText();
            billmax=0.0;
            }
        }
        receipt="";
        if(temp==4 ){
            if(receipttemp!=null){
            if(receipt.equals(receipttemp))
                flag=1;
            receipt=receiptseries.getText();
            receiptmax=0.0;
            }
        }
       bill="R";
        qt="";
       }else
       if(sales==1 || payment==0){
        if(temp==1  ){
            if(qttemp!=null){
            if(qt.equals(qttemp))
                flag=1;
            qt=qtseries.getText();
            qtmax=0.0;
            }
        }
        
        if(temp==3 ){
            if(btemp!=null){
            if(bill.equals(btemp) )
                flag=1;
            bill=billseries.getText();
            billmax=0.0;
            }
        }
        receipt="";
       }else
        if(payment==1 || sales==0){
        if(temp==4 ){
            if(receipttemp!=null){
            if(receipt.equals(receipttemp))
                flag=1;
            receipt=receiptseries.getText();
            receiptmax=0.0;
            }
        }
       bill="R";
        qt="";
       }
        if(temp==5){
            if((qt.equals(qttemp) && qttemp.length()>0) || (bill.equals(btemp) && btemp.length()>0) || (receipt.equals(receipttemp) && receipttemp.length()>0))
                flag=1;
            if(sales==1 && payment==1){
                if(receipttemp.length()<0)
                    flag=1;
                if(qttemp.length()<0 || btemp.length()<0)
                    flag=1;
            }else
            if(payment==1 || sales==0){
                if(receipttemp.length()<0)
                    flag=1;
            }else
            if(sales==1 || payment==0){
                if(qttemp.length()<0 || btemp.length()<0)
                    flag=1;
            }
            qt=qtseries.getText();
            qtmax=0.0;
            bill=billseries.getText();
            billmax=0.0;
            receipt=receiptseries.getText();
            receiptmax=0.0;
           
           
            
        }
         if(temp==0){
            qt=qtseries.getText();
            qtmax=0.0;
            bill=billseries.getText();
            billmax=0.0;
            receipt=receiptseries.getText();
            receiptmax=0.0;
         }
        if((!(receiptseries.getText().length()>0)  && payment==1) || (!(qtseries.getText().length()>0)  && !(billseries.getText().length()>0) && sales==1))
        {
              JOptionPane.showMessageDialog(this,"Cannot insert empty values", "Cannot Create", JOptionPane.OK_OPTION);
             jButton1.setVisible(false);
             jButton2.setVisible(false);
            jButton3.setVisible(false);
            jButton4.setVisible(false);
            jButton5.setVisible(false);
            jLabel1.setVisible(false);
           jLabel2.setVisible(false);
           jLabel3.setVisible(false);
           qtseries.setVisible(false);
           billseries.setVisible(false);
           receiptseries.setVisible(false);
           
          // jComboBox1.setVisible(false);
           jComboBox1.setSelectedIndex(-1);
           jComboBox2.setSelectedIndex(-1);
            jComboBox1.setVisible(false);
            jLabel4.setVisible(false);
        }else
        if(flag!=1){
            String name=jComboBox2.getSelectedItem().toString();
            String name1=jComboBox1.getSelectedItem().toString();
        Date d=new Date();
        String id=UUID.randomUUID().toString();
        try{
        Object[] obj=(Object[])  new StaticSentence(m_App.getSession()
                        , "SELECT COUNT(*),(SELECT COUNT(*) FROM SEQUENCEDETAIL WHERE  BSERIES=? ),(SELECT COUNT(*) FROM SEQUENCEDETAIL WHERE   QTSERIES=? ) FROM SEQUENCEDETAIL WHERE   RSERIES=? "
                        , new SerializerWriteBasic(new Datas[] {Datas.STRING,Datas.STRING,Datas.STRING})
                        ,new SerializerReadBasic(new Datas[]{Datas.INT,Datas.INT,Datas.INT}))
                        .find(new Object[] {bill,qt,receipt});
        int flag1=0;

        if(obj!=null){
            if(payment==1 && sales==1){
                 if(obj[0]!=null){
                 if(Integer.parseInt(obj[0].toString())>0 && (receiptseries.getText().length()>0)  ){
                 if(temp==4 || temp==5){
                 //  if(Integer.parseInt(obj[1].toString())>1)
                       flag1=1;
                 }else
                     if(temp==0)
                        flag1=1;
                 }
              }
                 if(obj[1]!=null){
             if(Integer.parseInt(obj[1].toString())>0 && (billseries.getText().length()>0) ){
                 if(temp==3 || temp==5){
                 //  if(Integer.parseInt(obj[1].toString())>1)
                       flag1=1;
                 }else
                     if(temp==0)
                        flag1=1;
            }
            }

            }
           if(payment==1) {
              if(obj[0]!=null){
                 if(Integer.parseInt(obj[0].toString())>0 && (receiptseries.getText().length()>0)  ){
                 if(temp==4 || temp==5){
                 //  if(Integer.parseInt(obj[1].toString())>1)
                       flag1=1;
                 }else
                     if(temp==0)
                        flag1=1;
                 }
              }
            }
            if(sales==1){
            if(obj[1]!=null){
             if(Integer.parseInt(obj[1].toString())>0 && (billseries.getText().length()>0) ){
                 if(temp==3 || temp==5){
                 //  if(Integer.parseInt(obj[1].toString())>1)
                       flag1=1;
                 }else
                     if(temp==0)
                        flag1=1;
            }
            }
        
            
             if(obj[2]!=null){
             if(Integer.parseInt(obj[2].toString())>0 && (qtseries.getText().length()>0) ){
                if(temp==1 || temp==5){
                 //  if(Integer.parseInt(obj[1].toString())>1)
                       flag1=1;
                 }else
                     if(temp==0)
                        flag1=1;
              }
              }
            }
        }
        if(flag1==0){
            //praveen:sequencedetail:inserting id instead of names
            String categoryid = ((RoleInfo)jComboBox1.getSelectedItem()).getID();
            String roleid = ((RoleInfo)jComboBox2.getSelectedItem()).getID();

        Object[] values = new Object[] {id,categoryid,roleid,receipt,receiptmax,bill,billmax,qt,qtmax,d,true};
        Datas[] datas = new Datas[] {Datas.STRING,Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING, Datas.DOUBLE,Datas.STRING, Datas.DOUBLE,Datas.TIMESTAMP,Datas.BOOLEAN};
        new PreparedSentence(m_App.getSession()
                , "INSERT INTO SEQUENCEDETAIL (ID,CATEGORY,USERNAME,RSERIES,RMAX,BSERIES,BMAX,QTSERIES,QTMAX,CRDATE,ACTIVE) VALUES (?,?,?,?,?,?,?,?,?,?,?) "
                , new SerializerWriteBasicExt(datas, new int[] {0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10})).exec(values);
         new StaticSentence(m_App.getSession()
                        , "UPDATE SEQUENCEDETAIL SET ACTIVE=FALSE  WHERE USERNAME = ? AND CATEGORY=? AND CRDATE != ?"
                        , new SerializerWriteBasic(new Datas[] {Datas.STRING,Datas.STRING,Datas.TIMESTAMP }))
                        .exec(new Object[] {categoryid,roleid,d});
          jButton2.setVisible(false);
          if(payment==1 && sales==1){
              jButton1.setVisible(true);
          jButton3.setVisible(true);
          jButton4.setVisible(true);

          jButton5.setVisible(true);

          }
          if(sales==1){
          jButton1.setVisible(true);
          jButton3.setVisible(true);
          }
          if(payment==1)
          jButton4.setVisible(true);
          
          jButton5.setVisible(true);
          qtseries.setEditable(false);
          billseries.setEditable(false);
          receiptseries.setEditable(false);
         // jComboBox1.setSelectedIndex(-1);
         // jComboBox2.setSelectedIndex(-1);
         }else{
            JOptionPane.showMessageDialog(this,"The Series Already Exist", "Cannot Create", JOptionPane.OK_OPTION);
             jButton1.setVisible(false);
             jButton2.setVisible(false);
            jButton3.setVisible(false);
            jButton4.setVisible(false);
            jButton5.setVisible(false);
            jLabel1.setVisible(false);
           jLabel2.setVisible(false);
           jLabel3.setVisible(false);
           qtseries.setVisible(false);
           billseries.setVisible(false);
           receiptseries.setVisible(false);
          // jLabel4.setVisible(false);
           
           jComboBox1.setSelectedIndex(-1);
           jComboBox2.setSelectedIndex(-1);
           jComboBox1.setVisible(false);
           jLabel4.setVisible(false);
            jLabel4.setVisible(false);
           jLabel6.setVisible(false);
           jLabel7.setVisible(false);
           jLabel8.setVisible(false);
           lbillno.setVisible(false);
           lqtno.setVisible(false);
           lrno.setVisible(false);
         }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        }else{
        JOptionPane.showMessageDialog(this,"The Series Already Exist", "Cannot Create", JOptionPane.OK_OPTION);
        jButton1.setVisible(false);
        jButton2.setVisible(false);
        jButton3.setVisible(false);
        jButton4.setVisible(false);
        jButton5.setVisible(false);
        jLabel1.setVisible(false);
        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
        qtseries.setVisible(false);
        billseries.setVisible(false);
        receiptseries.setVisible(false);
        //jLabel4.setVisible(false);
       // jComboBox1.setVisible(false);
        jComboBox1.setSelectedIndex(-1);
        jComboBox2.setSelectedIndex(-1);
        jComboBox1.setVisible(false);
        jLabel4.setVisible(false);
        jLabel6.setVisible(false);
        jLabel7.setVisible(false);
        jLabel8.setVisible(false);
        lbillno.setVisible(false);
        lqtno.setVisible(false);
        lrno.setVisible(false);
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                billseries.requestFocus();
            }
        });
        billseries.setText(null);
        billseries.setEditable(true);
        qtseries.setEditable(false);
        receiptseries.setEditable(false);
         jButton1.setVisible(false);
        jButton3.setVisible(false);
       // if(payment!=1)
        jButton4.setVisible(false);
        jButton5.setVisible(false);
        jButton2.setVisible(true);
        temp=3;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                receiptseries.requestFocus();
            }
        });
        receiptseries.setText(null);
        receiptseries.setEditable(true);
        billseries.setEditable(false);
        qtseries.setEditable(false);
         jButton1.setVisible(false);
        jButton3.setVisible(false);
        jButton4.setVisible(false);
        jButton5.setVisible(false);
        jButton2.setVisible(true);
        temp=4;
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        try{
       /*  new StaticSentence(m_App.getSession()
                        , "UPDATE SEQUENCEDETAIL SET ACTIVE=FALSE  WHERE USERNAME = ? AND CATEGORY=? "
                        , new SerializerWriteBasic(new Datas[] {Datas.STRING,Datas.STRING }))
                        .exec(new Object[] {jComboBox2.getSelectedItem().toString(),jComboBox1.getSelectedItem().toString()});*/


        qtseries.setText(null);
        qtseries.setEditable(true);
        jButton1.setVisible(false);
        billseries.setText(null);
        billseries.setEditable(true);

        jButton4.setVisible(false);
        receiptseries.setText(null);
        
        receiptseries.setEditable(true);
        jButton3.setVisible(false);
        jButton5.setVisible(false);
        jButton2.setVisible(true);
        if(sales==1 && payment==1 ){
             java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                receiptseries.requestFocus();
                qtseries.requestFocus();
            }
        });
        }else
        if(payment==1)
        {
              java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                receiptseries.requestFocus();
            }
        });
        }
        else if(sales==1){
             java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                qtseries.requestFocus();
            }
        });
        }
        temp=5;
        }catch(Exception e){}
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        qtmax=0.0;
        billmax=0.0;
        receiptmax=0.0;
        if(jComboBox1.getSelectedIndex()!=-1 &&jComboBox2.getSelectedIndex()!=-1)
        {
        try{
          /*   Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT ID,NAME,APPPASSWORD,CARD,ROLE,IMAGE,LOGINTIME,CLOSECASHTIME,OPENCASHTIME,CLOSESALE,OPENSALE FROM PEOPLE,ROLES WHERE PEOPLE.ROLE=ROLES.ID AND ROLES.NAME=?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] {Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.IMAGE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP})).find(jComboBox2.getSelectedItem().toString());
       /*if(obj[5]==null)
           obj[5]=new Icon();*/
          /*   if(obj1!=null){
       AppUser appuser=new AppUser(obj1[0].toString(), obj1[1].toString(),obj1[4].toString());
       appuser.fillPermissions(m_dlSystem);
       boolean pflag= appuser.hasPermission("payment");
       boolean qflag=appuser.hasPermission("sales");*/
      if(payment == 1 && sales==1){
          jLabel4.setVisible(true);
            jLabel3.setVisible(true);
          receiptseries.setVisible(true);
          jButton4.setVisible(true);
           lrno.setVisible(true);
           jLabel8.setVisible(true);
           jLabel1.setVisible(true);
              jLabel2.setVisible(true);
              qtseries.setVisible(true);
              billseries.setVisible(true);
              jButton1.setVisible(true);
              jButton3.setVisible(true);
              jButton2.setVisible(true);
              lqtno.setVisible(true);
         lbillno.setVisible(true);
         jLabel6.setVisible(true);
            jLabel7.setVisible(true);

      }else
      if(payment==1 || sales == 0){
         jLabel4.setVisible(true);
            jLabel3.setVisible(true);
          //  billseries.setVisible(true);
         // receiptseries.setVisible(true);
          receiptseries.setVisible(true);
          jButton4.setVisible(true);
           jLabel1.setVisible(false);
           jLabel2.setVisible(false);
            qtseries.setVisible(false);
            billseries.setVisible(false);
            jButton1.setVisible(false);
            jButton3.setVisible(false);
            lrno.setVisible(true);
            lqtno.setVisible(false);
            lbillno.setVisible(false);
            jLabel6.setVisible(false);
            jLabel7.setVisible(false);
            jLabel8.setVisible(true);
      }else
    if(sales==1 || payment==0){
             jLabel1.setVisible(true);
              jLabel2.setVisible(true);
              qtseries.setVisible(true);
              billseries.setVisible(true);
              jButton1.setVisible(true);
              jButton3.setVisible(true);
               jLabel4.setVisible(false);
            jLabel3.setVisible(false);
            jButton2.setVisible(true);
          receiptseries.setVisible(false);
          jButton4.setVisible(false);
         lrno.setVisible(false);
         lqtno.setVisible(true);
         lbillno.setVisible(true);
         jLabel6.setVisible(true);
            jLabel7.setVisible(true);

            jLabel8.setVisible(false);
    }
            String role=jComboBox1.getSelectedItem().toString();
         Object[] obj=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT QTSERIES,QTMAX,BSERIES,BMAX,RSERIES,RMAX FROM SEQUENCEDETAIL WHERE USERNAME=? AND ACTIVE=TRUE AND CATEGORY=? "
                       ,new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING,Datas.DOUBLE, Datas.STRING,Datas.DOUBLE,Datas.STRING,Datas.DOUBLE})).find(new Object[]{((RoleInfo)jComboBox2.getSelectedItem()).getID(),((RoleInfo)jComboBox1.getSelectedItem()).getID()});
       if(obj!=null){
           if(obj[0]!=null){
               qtseries.setText(obj[0].toString());
               qt=obj[0].toString();
               
               if(obj[1]!=null)
                   qtmax=Double.parseDouble(obj[1].toString());
               
           }else qtseries.setText("");
           if(obj[2]!=null){
               billseries.setText(obj[2].toString());
               bill=obj[2].toString();
              
               if(obj[3]!=null)
                   billmax=Double.parseDouble(obj[3].toString());
               
           }else billseries.setText("");
           if(obj[4]!=null){
               receiptseries.setText(obj[4].toString());
               receipt=obj[2].toString();
               
               if(obj[5]!=null)
                   receiptmax=Double.parseDouble(obj[5].toString());
               
           }else receiptseries.setText("");
        //  jButton2.setVisible(false);
          lrno.setText(receiptmax.toString());
          lbillno.setText(billmax.toString());
          lqtno.setText(qtmax.toString());
          jButton5.setVisible(true);
          qtseries.setEditable(false);
          billseries.setEditable(false);
          receiptseries.setEditable(false);
          jButton2.setVisible(false);
       }else{
           qtseries.setText(null);
           billseries.setText(null);
           receiptseries.setText(null);
           qtseries.setEditable(true);
           billseries.setEditable(true);
           receiptseries.setEditable(true);
           jButton1.setVisible(false);
           jButton3.setVisible(false);
           jButton4.setVisible(false);
           jButton2.setVisible(true);
           jButton5.setVisible(false);
           lrno.setVisible(false);
           lbillno.setVisible(false);
           lqtno.setVisible(false);
           jLabel6.setVisible(false);
           jLabel7.setVisible(false);
           jLabel8.setVisible(false);
       }
         /* }else{
             JOptionPane.showMessageDialog(this, "User doesnot exist for this role.Create a user first", "cannot", JOptionPane.OK_OPTION);
             }*/
        }
        catch(Exception e){}

        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        if(jComboBox2.getSelectedIndex()!=-1 ){
        try{
             payment=0;
            sales=0;
          Object name=jComboBox2.getSelectedItem();
           //warehouse changes -start
          Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()//PRAVEEN:CHANGED BELOW QUERY
                       // , "SELECT ID,NAME,APPPASSWORD,CARD,ROLE,IMAGE,LOGINTIME,CLOSECASHTIME,OPENCASHTIME,CLOSESALE,OPENSALE FROM PEOPLE,ROLES WHERE PEOPLE.ROLE=ROLES.ID AND ROLES.NAME=?"
                       ,"SELECT P.ID,P.NAME,P.APPPASSWORD,P.CARD,P.ROLE,P.IMAGE,P.LOGINTIME,P.CLOSECASHTIME,P.OPENCASHTIME,P.CLOSESALE,P.OPENSALE,P.PRCATEGORIES FROM PEOPLE P,ROLES R WHERE P.ROLE=R.ID AND R.NAME=?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] {Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.IMAGE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING})).find(jComboBox2.getSelectedItem().toString());

             if(obj1!=null){
                 String warehouse = null;
                if (obj1[11] != null) {
                    String[] wArr = obj1[11].toString().split("#");
                    warehouse = wArr[0];
                }
             AppUser appuser=new AppUser(obj1[0].toString(), obj1[1].toString(),obj1[4].toString(),warehouse);
              //warehouse changes -end
             appuser.fillPermissions(m_dlSystem);
             boolean pflag= appuser.hasPermission("payment");
             boolean qflag=appuser.hasPermission("sales");
              String del=jComboBox2.getSelectedItem().toString();

       if(pflag==true && qflag==true){
            payment=1;
         // jButton3.setVisible(true);
          // wmodel=new ComboBoxValModel();
           java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                receiptseries.requestFocus();
            }
        });
         jLabel4.setVisible(true);
           jComboBox1.setVisible(true);
        sales=1;
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                qtseries.requestFocus();
            }
        });
         jComboBox1.setSelectedItem(jComboBox2.getSelectedItem());
       }else
       if(pflag==true || qflag==false){
          
           payment=1;
         // jButton3.setVisible(true);
          // wmodel=new ComboBoxValModel();
           java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                receiptseries.requestFocus();
            }
        });
   
          
         //  jComboBox1.setModel(wmodel);
            jLabel1.setVisible(false);
           jLabel2.setVisible(false);
            qtseries.setVisible(false);
            billseries.setVisible(false);
            jButton1.setVisible(false);
            jButton3.setVisible(false);
           jLabel4.setVisible(true);
           jComboBox1.setVisible(true);
        jComboBox1.setSelectedIndex(-1);
        jButton2.setVisible(false);
         jButton5.setVisible(false);
            lrno.setVisible(false);
            lqtno.setVisible(false);
            lbillno.setVisible(false);
            jLabel6.setVisible(false);
            jLabel7.setVisible(false);
            jLabel8.setVisible(false);
            receiptseries.setVisible(false);
            jLabel3.setVisible(false);
            jButton4.setVisible(false);


       }else
       if(qflag==true || pflag==false)
       {
             java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                qtseries.requestFocus();
            }
        });
             jLabel4.setVisible(false);
             sales=1;
              jComboBox1.setVisible(false);
             jLabel4.setVisible(false);
            jLabel3.setVisible(false);
           //  jComboBox1.setSelectedIndex(0);
          receiptseries.setVisible(false);
          jButton4.setVisible(false);
          lrno.setVisible(false);
           jLabel8.setVisible(false);
              jComboBox1.setSelectedItem(jComboBox2.getSelectedItem());
             // jComboBox1ItemStateChanged(evt);

             
       }
          /*  jLabel1.setVisible(false);
            jLabel2.setVisible(false);
            jLabel3.setVisible(false);
            jButton4.setVisible(false);
            jButton3.setVisible(false);
            jButton2.setVisible(false);
            jButton1.setVisible(false);
            jButton5.setVisible(false);*/
           
          
           
           
             }else{
              JOptionPane.showMessageDialog(this, "User doesnot exist for this role.Create a user first", "cannot", JOptionPane.OK_OPTION);
             }
            }
          catch(Exception e){
              e.printStackTrace();
          }
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField billseries;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField lbillno;
    private javax.swing.JTextField lqtno;
    private javax.swing.JTextField lrno;
    private javax.swing.JTextField qtseries;
    private javax.swing.JTextField receiptseries;
    // End of variables declaration//GEN-END:variables

}
