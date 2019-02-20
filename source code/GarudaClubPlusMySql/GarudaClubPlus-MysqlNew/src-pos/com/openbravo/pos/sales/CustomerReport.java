/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.admin.CardReader;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.clubmang.MemDebtBillingTableModel;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.CardSwipeNotifier;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.mant.WaiterInfo;
import com.openbravo.pos.sales.restaurant.JIntroPageRest;
import cos.card.acs.Cosacs;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author dev1
 */
public class CustomerReport extends javax.swing.JPanel implements CardSwipeNotifier,JPanelView, BeanFactoryApp {
    
    private Object bean;
 //private Object bean;
    private AppView m_App;
    private DataLogicFacilities dmang;
    private DataLogicSales m_dlSales;
    private DataLogicCustomers dlCustomers = null;
    private DataLogicSales dlSales = null;
    private CustomerInfoExt customer;
    private CustomerInfoExt notes;
    private String initiator;
    private boolean flag = true;
    private CardReader cr;
    private CustomerInfo customerInfo;
                     //  public static String cust1;
                   //  public String notes;
                    


    /**
     * Creates new customizer CustomerReport
     */
    public CustomerReport() {
        initComponents();
    }
     public void init(AppView app) throws BeanFactoryException {
        m_App = app;
        dmang = (DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
  dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
  dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
   
        

    }
    public void setObject(Object bean) {
        this.bean = bean;
    }
      private void loadData() throws BasicException {
         jButton1.setVisible(true);
         jTextField2.setEnabled(false);
         jButton2.setVisible(false);
         jTextField2.setVisible(false);
         jTextArea1.setVisible(false);
         
        // jLabel2.setVisible(false);
         
             
                 
          
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });

        jLabel1.setText("Mem Name.");
        jLabel1.setToolTipText("");

        jTextField1.setToolTipText("");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        jButton3.setToolTipText("");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText("Mem No.");

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField5KeyPressed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane1.setViewportView(jTextArea1);

        jLabel2.setText("Card No.");

        jTextField2.setToolTipText("");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton2.setText("Load");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addGap(63, 63, 63))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel4)
                                .addGap(54, 54, 54)
                                .addComponent(jButton2)
                                .addGap(109, 109, 109)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jButton1.getAccessibleContext().setAccessibleDescription("");

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
String card = jTextField3.getText();
        
        if (card.length() > 0) {

            try {
                 Object[] obj = dmang.getMamberbycard1(card);
                if (obj == null) {
                     jTextArea1.setVisible(false);
                       JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                        refresh();
                    //JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                } else {
                    customerInfo = new CustomerInfo(obj[0].toString());
                    customerInfo.setSearchkey(obj[1].toString());
                    customerInfo.setName(obj[2].toString());
                    customerInfo.setMobile(String.valueOf(obj[3]));
                    customerInfo.setAccno(obj[4].toString());
                   
                     customerInfo.setNotes(obj[6].toString());
                    System.out.println(customerInfo.getAccno());
                    jTextField5.setText(obj[1].toString());
                    jTextField1.setText(obj[2].toString());
                     jTextArea1.setVisible(true);
                    jTextArea1.setText(obj[6].toString());
                    load();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "swipe a card");
        }
        //akshatha:to read a card from card reader without port num
    }//GEN-LAST:event_jTextField3ActionPerformed
public long startSec = 0;
    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
char c = evt.getKeyChar();
        if(jTextField3.getText()!=null){
            int length = jTextField3.getText().trim().length();
            if(length==1){
                startSec = System.currentTimeMillis();
            }
            else if(length>1){
                long Currsec = System.currentTimeMillis();
                long diff = Currsec-startSec;
                if(diff>750){
                    JOptionPane.showMessageDialog(this, "Do not use keyboard. Please swipe card.");
                    jTextField3.setText(null);
                     System.out.println("Time Taken : "+diff);
                }
                if(c==KeyEvent.VK_ENTER){
                    System.out.println("Time Taken : "+diff);
                }
            }
        }
    }//GEN-LAST:event_jTextField3KeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        //finder.search(m_oTicket.getCustomer())
        finder.setVisible(true);
        CustomerInfo customerInfo = finder.getSelectedCustomer();
        if (customerInfo != null) {
            try {
                customer = dlSales.loadCustomerExt(customerInfo.getId());
                jTextField1.setText(customerInfo.toString());
                 jTextArea1.setVisible(true);
                  jTextArea1.setText(customer.getNotes());
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
                msg.show(this);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
               
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
      java.awt.EventQueue.invokeLater(new Runnable() {

        public void run() {

            jTextField3.requestFocus();
            jTextField3.setText(null);
            jTextField5.setText(null);
            jTextField1.setText(null);
            jTextArea1.setText(null);
            try {
                loadData();
            } catch (BasicException ex) {
                Logger.getLogger(CustomerReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });   

          
    }//GEN-LAST:event_jButton1ActionPerformed
           
    
    private void jTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyPressed
        // TODO add your handling code here:
         String custoid;

        if (evt.getKeyText(evt.getKeyCode()).equals("Enter")) {
            String cust = jTextField5.getText();
           // cust1=cust;
           
            try {
                Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ID,NAME,NOTES FROM CUSTOMERS WHERE SEARCHKEY = ? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING})).find(cust.toUpperCase()); 
               // String str=obj[2].toString();
// #CHANGE BY AAKASH... ON 6TH DEC 2013
                if (obj == null) {
                     jTextArea1.setVisible(false);
                    JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                     refresh();
                  
//                    Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT C.NOTES FROM CUSTOMERS C WHERE C.SEARCHKEY=? ",
//                        new SerializerWriteBasic(new Datas[]{Datas.STRING})).find(cust.toUpperCase());
//                   String notes=obj1[0].toString();
//                    //JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
//                   jTextArea1.setText(notes);
//                 
                } else {
                   custoid = obj[0].toString();
                    customer = dlSales.loadCustomerExt(custoid);
                    jTextField1.setText(obj[1].toString());
                    String str=obj[2].toString();
                     jTextArea1.setVisible(true);
                   jTextArea1.setText(str);
//                    Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT C.NOTES FROM CUSTOMERS C WHERE C.SEARCHKEY=? ",
//                        new SerializerWriteBasic(new Datas[]{Datas.STRING})).find(cust.toUpperCase());
//                   String notes=obj1[0].toString();
//                    //JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
//                   jTextArea1.setText(notes);
//                    
                     //  notes=obj1[0].toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_jTextField5KeyPressed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
   String card = cr.getData().toString();
        if (card != null) {
       try {
           loadMemberDetails(card);
       } catch (HeadlessException ex) {
           Logger.getLogger(CustomerReport.class.getName()).log(Level.SEVERE, null, ex);
       } catch (BasicException ex) {
           Logger.getLogger(CustomerReport.class.getName()).log(Level.SEVERE, null, ex);
       }
        }else{
            String cust = jTextField3.getText();
       try {
           loadMemberDetails(cust);
       } catch (HeadlessException ex) {
           Logger.getLogger(CustomerReport.class.getName()).log(Level.SEVERE, null, ex);
       } catch (BasicException ex) {
           Logger.getLogger(CustomerReport.class.getName()).log(Level.SEVERE, null, ex);
       }
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getTitle() {
        return " Member Status";
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void activate() throws BasicException {
        startCardReader();
        loadData();
         jTextField1.setText(null);
          jTextField5.setText(null);
         jTextArea1.setText(null);
//        
//         cust=null;
        //  jTextField2.setCursor(this.getCursor());
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                //akshatha:to read a card from card reader without port num
               String cardReaderPortName = m_App.getProperties().getProperty("card.portnumber");
        String CardRead = m_App.getProperties().getProperty("ACScard.port");
		if(cardReaderPortName.isEmpty() && CardRead.isEmpty()  ){
                    jTextField5.setEditable(true);
                    jButton1.setVisible(true);
                    jLabel2.setVisible(false);
                    jTextField3.requestFocus();
                } else {
                    jTextField5.requestFocus();
                    jTextField3.setEditable(false);
                    jButton1.setVisible(false);
                    jLabel2.setVisible(false);
                }
            }
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deactivate() {
        return true;
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JComponent getComponent() {
        
        return this;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getBean() {
        return this;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void loadMemberDetails(String card) throws HeadlessException, BasicException  {
       card = cr.getData();
         String cust = jTextField3.getText();
        if (card.length() > 0) {

            try {
                Object[] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT C.ID,C.SEARCHKEY,C.NAME,M.CARD,M.ID,C.NOTES FROM CUSTOMERS C,MEMDEPENDENT M WHERE M.MEMNO=C.ID AND M.CARD=?  UNION ALL  SELECT C.ID,C.SEARCHKEY,C.NAME,C.CARD,C.ID  FROM CUSTOMERS C WHERE C.CARD=?",
                        new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING})).find(new Object[]{card, card});
//                 Object[] obj1 = (Object[]) new StaticSentence(m_App.getSession(), "SELECT ID,NAME,NOTES FROM CUSTOMERS WHERE SEARCHKEY = ? AND VISIBLE = TRUE", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING})).find(cust.toUpperCase()); 
//                String str=obj1[2].toString();
////
                if (obj == null) {
                     jTextArea1.setVisible(false);
                       JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
             refresh();
                
                }
                   else {
                     customerInfo = new CustomerInfo(obj[0].toString());
                   customerInfo.setSearchkey(obj[1].toString());
                    customerInfo.setName(obj[2].toString());
                    customerInfo.setMobile(String.valueOf(obj[3]));
                    customerInfo.setAccno(obj[4].toString());
                    customerInfo.setNotes(obj[5].toString());
                     //System.out.println(customerInfo.getNotes());
                   System.out.println(customerInfo.getAccno());
                   jTextField5.setText(obj[1].toString());
                    jTextField1.setText(obj[2].toString());
                     jTextArea1.setVisible(true);
                   jTextArea1.setText(obj[5].toString());
                     // jTextArea1.setText(str);
                    load();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(cust.length()>0){
            try {
                 Object[] obj = dmang.getMamberbycard1(cust);
                  if (obj == null) {
                       jTextArea1.setVisible(false);
                     JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
                      refresh();
               } else {
                     customerInfo = new CustomerInfo(obj[0].toString());
                   customerInfo.setSearchkey(obj[1].toString());
                    customerInfo.setName(obj[2].toString());
                    customerInfo.setMobile(String.valueOf(obj[3]));
                    customerInfo.setAccno(obj[4].toString());
                     customerInfo.setNotes(obj[6].toString());
                    
                      //System.out.println(customerInfo.getNotes());
                   System.out.println(customerInfo.getAccno());
                    jTextField5.setText(obj[1].toString());
                   jTextField1.setText(obj[2].toString());
                    jTextArea1.setVisible(true);
                    jTextArea1.setText(obj[6].toString());
//                     jTextField2.setText(obj[7].toString());
                  // jTextArea1.setText(str);
                    load();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(this, "swipe a card");
        }
    }
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }
    
    public void startCardReader() {
        try {
            String portNumber = m_App.getProperties().getProperty("card.portnumber");
            boolean cardAccessOnlyFlag = false;
            if (m_App.getProperties().getProperty("cardAccessOnly") != null) {
                cardAccessOnlyFlag = Boolean.valueOf(m_App.getProperties().getProperty("cardAccessOnly"));
            }
            cr = new CardReader(portNumber, cardAccessOnlyFlag);
            cr.setCardSwipeNotifier( this);
            System.out.println(portNumber);
            cr.ConfigurePort();
        } catch (Exception ex) {
            ex.printStackTrace();
        }




    }
     public void execute(String card) {
        card = String.valueOf(m_App.getReader().getVariance() + Double.valueOf(card));
     }

     public void cardswiped(String custCard) {
        try {
            loadMemberDetails(custCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     private void load() {
        try {
            
            //jTable1.setVisible(true);
            String accid = dmang.getCustomerAccountByID(customerInfo.getId());
//            dbmodel = MemDebtBillingTableModel.loadInstance(m_App, customerInfo.getId(), accid, dmang);
//            tablemodel = dbmodel.getTableModel();
//            tablemodel.settext(total);
//            jTable1.setModel(tablemodel);
//            ctablemodel = dbmodel.getCreditTableModel();
//            ctablemodel.settext(ctotal);
//            jTable2.setModel(ctablemodel);
            
            jButton1.setVisible(true);
           
            
        } catch (Exception e) {
            e.printStackTrace();
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, "Cannot load the Data", e);
            msg.show(this);
        }
    }
     public void cardswiped(WaiterInfo waiter) {
       
    }
     public void refresh()
     {
          java.awt.EventQueue.invokeLater(new Runnable() {

        public void run() {

            jTextField3.requestFocus();
            jTextField3.setText(null);
            jTextField5.setText(null);
            jTextField1.setText(null);
            jTextArea1.setText(null);
            try {
                loadData();
            } catch (BasicException ex) {
                Logger.getLogger(CustomerReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });   
     }
}

     
    

