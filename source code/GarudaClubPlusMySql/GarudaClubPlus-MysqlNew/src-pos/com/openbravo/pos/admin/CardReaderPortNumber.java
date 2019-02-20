/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CadrReaderPortNumber.java
 *
 * Created on 14-Jul-2011, 11:27:06
 */
package com.openbravo.pos.admin;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.pos.customers.CustomersView;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/**
 *
 * @author swathi
 */
public class CardReaderPortNumber extends javax.swing.JPanel implements JPanelView, BeanFactoryApp {

    private AppView m_App;
    private AppConfig config;

    /** Creates new form CadrReaderPortNumber */
    public CardReaderPortNumber() {
        
        initComponents();
    }
  

    public String getTitle() {
        return "CardReaderPortNumber";
    }


    public void activate() throws BasicException {
        load();

    }

    public boolean deactivate() {
        return true;
    }

    public JComponent getComponent() {

        return this;
    }

    public void init(AppView app) throws BeanFactoryException {
        m_App = app;        
    }

    public Object getBean() {
        return this;
    }
    public void load(){
      
       
        jButton1.setVisible(false);
        jTextField1.setEditable(false);
//        if(m_App.getProperties().getProperty("card.portnumber")==null){
//            savePropertie(config);
//        }
        jTextField1.setText(m_App.getProperties().getProperty("card.portnumber"));
        jButton4.setVisible(false);
        jTextField2.setEditable(false);
        jTextField2.setText( m_App.getProperties().getProperty("ACScard.port"));
        
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
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setLayout(null);

        jLabel1.setText("Port Number");
        jLabel1.setName("jLabel1"); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 250, 90, 20);

        jTextField1.setName("jTextField1"); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        add(jTextField1);
        jTextField1.setBounds(110, 80, 170, 20);

        jButton1.setText("Save");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);
        jButton1.setBounds(120, 130, 70, 23);

        jButton2.setText("Reset");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        jButton2.setBounds(210, 130, 80, 23);

        jButton3.setText("Reset");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3);
        jButton3.setBounds(200, 300, 80, 23);

        jButton4.setText("Save");
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        add(jButton4);
        jButton4.setBounds(110, 300, 70, 23);

        jTextField2.setName("jTextField2"); // NOI18N
        add(jTextField2);
        jTextField2.setBounds(110, 250, 170, 20);

        jLabel2.setText("Port Number");
        jLabel2.setName("jLabel2"); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(30, 80, 90, 20);

        jLabel3.setText("3. For Contact RFID Card Reader with USB port (Plug & Play) keep above Port Numbers Blank");
        jLabel3.setName("jLabel3"); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(10, 360, 580, 40);

        jLabel4.setText("1. For RFID Card Reader with com port : Enter Com Port No. below (Ex: COM1 or COM2 etc)");
        jLabel4.setName("jLabel4"); // NOI18N
        add(jLabel4);
        jLabel4.setBounds(20, 24, 700, 40);

        jLabel5.setText("2. For Contact Card Reader( like ACR30S) with com port  : Enter Com Port No. below (Ex: COM1 or COM2 etc)");
        jLabel5.setName("jLabel5"); // NOI18N
        add(jLabel5);
        jLabel5.setBounds(10, 200, 700, 40);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel6.setText("Note: Please Ensure that only one of the Port Number is declared . Do not Enter Port Number in both "); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        add(jLabel6);
        jLabel6.setBounds(30, 490, 650, 40);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {                
                String port = jTextField1.getText().toUpperCase();                
                if (JOptionPane.showConfirmDialog(this,"Do u want changes to port number", LocalRes.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                m_App.getProperties().setProperty("card.portnumber", port);
                m_App.getProperties().save();
                JOptionPane.showMessageDialog(this, "Saved Successfully");
                }
                load();          
           
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
         String CardRead = m_App.getProperties().getProperty("ACScard.port");
		if(CardRead.isEmpty() ){   
            jButton1.setVisible(true);
            jTextField1.setEditable(true);
                }else{
                    JOptionPane.showMessageDialog(this, "Please Remove the port number from the box below ","CardRead", JOptionPane.OK_OPTION);
                }
        
    }//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
     String CardRead = m_App.getProperties().getProperty("card.portnumber");
     if(CardRead.isEmpty() ){   
            jButton4.setVisible(true);
            jTextField2.setEditable(true);
        }else{
                    JOptionPane.showMessageDialog(this, "Please Remove the port number from the above box with port no","CardRead", JOptionPane.OK_OPTION);
                }
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
 try {                
                String port = jTextField2.getText().toUpperCase();                
                if (JOptionPane.showConfirmDialog(this,"Do u want changes to port number", LocalRes.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                m_App.getProperties().setProperty("ACScard.port", port);
                m_App.getProperties().save();
                JOptionPane.showMessageDialog(this, "Saved Successfully");
                }
                load();          
           
        } catch (IOException ex) {
            ex.printStackTrace();
        }
}//GEN-LAST:event_jButton4ActionPerformed

private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
    
    

//    private void savePropertie(AppConfig config) {
//        config.setProperty("card.portNumber", jTextField1.getText());
//        config.setProperty("ACScard.port", jTextField1.getText());
//    }
}
